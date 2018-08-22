package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.pojo.Cart;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author Zero
 * create in 13:42 2018/8/21
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;


    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVo = this.getCartProductLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer count, Integer productId) {
        if (count == null || productId == null) {
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECK);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        } else {
            count +=cart.getQuantity();
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer count, Integer productId) {
        if (count == null || productId == null) {
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> delete(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productIdList)) {
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productIdList);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> selectOrNot(Integer userId,Integer productId, Integer checked) {
        cartMapper.selectOrNot(userId, productId, checked);
        return this.list(userId);
    }

    @Override
    public ServerResponse<Integer> countProductNum(Integer userId) {
        if (userId == null) {
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.countProductNum(userId));
    }

    private CartVo getCartProductLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = cartMapper.selectCartProductVoByUserId(userId);

        if (CollectionUtils.isEmpty(cartProductVoList)) {
            return null;
        }

        //初始化购物车总价
        BigDecimal cartTotalPrice = new BigDecimal("0");
        for (CartProductVo cartProductVo : cartProductVoList) {
            int limitQuantity = 0;
            if (cartProductVo.getQuantity() > cartProductVo.getProductStock()) {
                limitQuantity = cartProductVo.getProductStock();
                cartProductVo.setLimitQuantity(Const.Cart.STOCK_NUM_INSUFFICIENT);
                //更新购物车记录
                Cart cartUpdateQuantity = new Cart();
                cartUpdateQuantity.setId(cartProductVo.getCartId());
                cartUpdateQuantity.setQuantity(limitQuantity);
                cartMapper.updateByPrimaryKeySelective(cartUpdateQuantity);
            } else {
                limitQuantity = cartProductVo.getQuantity();
                cartProductVo.setLimitQuantity(Const.Cart.STOCK_NUM_SUFFICIENT);
            }

            cartProductVo.setQuantity(limitQuantity);
            //计算单类商品的总价
            cartProductVo.setProductTotalPrice(BigDecimalUtil.multiply(cartProductVo.getQuantity(),
                    cartProductVo.getProductPrice().doubleValue()));
            //如果商品勾选，则计入结算总价中
            if (cartProductVo.getProductCheck() == Const.Cart.CHECK) {
                cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),
                        cartProductVo.getProductTotalPrice().doubleValue());
            }
        }

        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        cartVo.setCheckAll(this.getAllCheckedStatus(userId));

        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        int uncheckCount = cartMapper.selectCheckCount(userId);
        return uncheckCount == 0;
    }
}
