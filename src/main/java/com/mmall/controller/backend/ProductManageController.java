package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Zero
 * @date 2017/12/10
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save_product.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse saveProduct(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限进行此项操作，需管理员权限");
        }
    }

    @RequestMapping(value = "set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer productStatus){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            return iProductService.setSaleStatus(productId,productStatus);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限进行此项操作，需管理员权限");
        }
    }

    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            return iProductService.manageProductDetail(productId);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限进行此项操作，需管理员权限");
        }
    }

    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse gitList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue="10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            return iProductService.getProductList(pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限进行此项操作，需管理员权限");
        }
    }

    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServerResponse searchProduct(HttpSession session, String productName, Integer productId,
                                        @RequestParam(value="pageNum",defaultValue="1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限进行此项操作，需管理员权限");
        }
    }

    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map<String, String > fileMap = new HashMap<>(16);
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
        }else {
            return ServerResponse.createByErrorMessage("您没有权限进行此项操作，需管理员权限");
        }
    }

    @RequestMapping(value = "richText_upload.do")
    @ResponseBody
    public Map richTextUpload(HttpSession session, MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        Map<String, Object>  fileMap= new HashMap<>(16);
        if (user == null) {
            fileMap.put("success", false);
            fileMap.put("msg", "用户未登录，请先登录");
            return fileMap;
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            String path = request.getSession().getServletContext().getRealPath("richTest_upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                fileMap.put("success", false);
                fileMap.put("msg", "上传失败");
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            response.addHeader("Access-Control-Allow_Headers", "X-File-Name");
            fileMap.put("success", false);
            fileMap.put("msg", "上传失败");
            fileMap.put("file_path", url);
            return fileMap;
        }else {
            fileMap.put("success", false);
            fileMap.put("msg", "无权限操作");
            return fileMap;
        }
    }

}
