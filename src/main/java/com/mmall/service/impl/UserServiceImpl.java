package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * Created by Administrator on 2017/12/10.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        // 密码登录MD5加密
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        //处理返回值密码，将密码置成空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功",user);
    }

    @Override
    public ServerResponse<String> register(User user){
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //密码MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str,String type){
        if (StringUtils.isNotBlank(type)) {
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }

            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("验证成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username){
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题未设置！");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if (resultCount > 0) {//用户的问题及答案正确

            String forgetToken = UUID.randomUUID().toString();
            //"token_"是命名标识
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案不匹配");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        //判断用户名是否存在
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if (validResponse.isSuccess()) {//用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        //判断forgetToken是否为空
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        //从缓存中取得token
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token错误或是过期");
        }
        if (StringUtils.equals(forgetToken,token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取修改密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //新密码与原密码不能相同
        if (passwordOld == passwordNew) {
            return ServerResponse.createByErrorMessage("新密码不能与原密码不能相同，请重新输入");
        }
        //验证原密码,防止横向越权，必须指定是这个用户的，
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("原密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updataCount = userMapper.updateByPrimaryKeySelective(user);
        if (updataCount > 0) {
            return  ServerResponse.createBySuccessMessage("更新密码成功");
        }
        return  ServerResponse.createByErrorMessage("更新密码失败");
    }

    @Override
    public ServerResponse<User> updataInformation(User user){
        //修改信息时用户名是不能更新的
        //email修改后是不能与其他用户相同的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已被其他用户注册，请换一个试试呀！");
        }

        User updataUser = new User();
        updataUser.setId(user.getId());
        updataUser.setEmail(user.getEmail());
        updataUser.setPhone(user.getPhone());
        updataUser.setQuestion(user.getQuestion());
        updataUser.setAnswer(user.getAnswer());

        int updataCount = userMapper.updateByPrimaryKeySelective(updataUser);
        if (updataCount > 0) {
            return ServerResponse.createBySuccess("用户信息更新成功",updataUser);
        }
        return ServerResponse.createByErrorMessage("信息更新失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("您查询的用户不存在，请核实后重新查询");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    /**
    * 检验用户为管理员
    * */
    @Override
    public ServerResponse checkAdminRole(User user){
        if(user.getRole().intValue() == Const.Role.ROLE_ADMIN && user != null){
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByError();
        }
    }
}
