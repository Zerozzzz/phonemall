package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.pojo.Cart;
import com.mmall.service.ICartService;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public ServerResponse add(Integer userId, Integer count, Integer productId) {
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
        return null;
    }

    private CartVo getCartProductLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        //初始化购物城总价
        BigDecimal cartTotalPrice = new BigDecimal("0");

        return null;
    }
}
