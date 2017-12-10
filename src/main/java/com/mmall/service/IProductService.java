package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * Created by Administrator on 2017/12/10.
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);
}
