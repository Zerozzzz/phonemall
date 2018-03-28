package com.mmall.service.impl;

import com.google.common.base.Preconditions;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/10.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse saveOrUpdateProduct(Product product){
        //guava的Preconditions做参数前置判断
        Preconditions.checkNotNull(product,"product不能为空");

        //子图的第一张图给主图
        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImageArray = product.getSubImages().split(",");
            if (subImageArray.length > 0) {
                product.setMainImage(subImageArray[0]);
            }
        }

        if (product.getId() != null) {
            int resultRow = productMapper.updateByPrimaryKey(product);
            if (resultRow > 0) {
                return ServerResponse.createBySuccessMessage("更新产品成功");
            }
            return ServerResponse.createBySuccessMessage("更新产品失败");
        }else{
            int resultRow = productMapper.insert(product);
            if (resultRow > 0) {
                return ServerResponse.createBySuccessMessage("新增产品成功");
            }
            return ServerResponse.createBySuccessMessage("新增产品失败");
        }
    }

    public ServerResponse<String> setSaleStatus(Integer productId,Integer productStatus){
        if (productId == null || productStatus == null) {
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(productStatus);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);

        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("产品状态更新成功");
        }else {
            return ServerResponse.createByErrorMessage("产品状态更新失败");
        }
    }

    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if (productId == null) {
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品不存在或已下架");
        }

        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);//默认根节点
        }else {
            productDetailVo.setCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.DateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.DateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

}
