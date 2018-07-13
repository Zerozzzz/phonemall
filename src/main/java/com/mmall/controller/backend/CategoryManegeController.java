package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**分类管理
 * Created by Administrator on 2017/12/10.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManegeController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
    *添加分类管理
    * */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登录");
        }
        //检验用户是否为管理员
        if (iUserService.checkAdminRole(currentUser).isSuccess()) {
            //是管理员
            return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return ServerResponse.createByErrorMessage("您不是管路员，没有权限");
        }

    }

    /**
     * 修改分类的名称
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登录");
        }
        if (iUserService.checkAdminRole(currentUser).isSuccess()) {
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return ServerResponse.createByErrorMessage("您无权限进行相关操作");
        }
    }
    /**
     *通过category的ID获得平级的子节点
     */
    @RequestMapping("get_children_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登录");
        }
        if (iUserService.checkAdminRole(currentUser).isSuccess()) {
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("您不是管理员，没有权限");
        }
    }

    /**
    * 通过categoryID递归出所有的子分类
    * 0 -> 10001 -> 100001 -> ...
    * */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录，请先登录");
        }
        if (iUserService.checkAdminRole(currentUser).isSuccess()) {
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("您不是管理员，没有权限");
        }
    }

}
