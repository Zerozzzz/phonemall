package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @author Zero
 * create in 13:42 2018/8/21
 */
public interface ICartService {
    ServerResponse<CartVo> add (Integer userId, Integer count, Integer productId);
}
