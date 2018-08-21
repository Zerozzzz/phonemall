package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * @author Zero
 * create in 13:42 2018/8/21
 */
public interface ICartService {
    ServerResponse add (Integer userId, Integer count, Integer productId);
}
