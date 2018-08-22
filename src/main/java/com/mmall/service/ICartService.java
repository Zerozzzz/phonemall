package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @author Zero
 * create in 13:42 2018/8/21
 */
public interface ICartService {
    /**
     * 在购物车中添加商品
     * @param userId user_id
     * @param count product count
     * @param productId product_id
     * @return CartVo
     */
    ServerResponse<CartVo> add (Integer userId, Integer count, Integer productId);

    /**
     * 更新购物车中商品
     * @param userId user_id
     * @param count product count
     * @param productId product_id
     * @return CartVo
     */
    ServerResponse<CartVo> update (Integer userId, Integer count, Integer productId);

    /**
     * 删除购物车中的商品
     * @param userId user_id
     * @param productIds product_id list
     * @return CartVo
     */
    ServerResponse<CartVo> delete (Integer userId, String productIds);

    /**
     * 查询购物车，暂时没有做分页
     * @param userId user_id
     * @return CartVo
     */
    ServerResponse<CartVo> list (Integer userId);

    /**
     * 购物车中的商品全选或全反选的通用方法
     * @param userId user_id
     * @return CartVo
     */
    ServerResponse<CartVo> selectOrNot (Integer userId,Integer productId, Integer checked);

    /**
     * 获取购物车中商品数量
     * @param userId user_id
     * @return number of product
     */
    ServerResponse<Integer> countProductNum (Integer userId);
}
