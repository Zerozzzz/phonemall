package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Zero
 * create in 18:46 2018/7/12
 */
@RequestMapping("/product/")
@Controller
public class ProductController {

    @Autowired
    IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> getDetail(Integer productID) {
        return iProductService.getProductDetail(productID);
    }

    public ServerResponse<PageInfo> productList(@RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "categoryID", required = false)Integer categoryID,
                                                @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return iProductService.listProduct(keyword, categoryID, pageNum, pageSize);
    }
}
