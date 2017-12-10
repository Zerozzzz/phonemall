package com.mmall.service.impl;

import com.google.common.base.Preconditions;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/10.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

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
}
