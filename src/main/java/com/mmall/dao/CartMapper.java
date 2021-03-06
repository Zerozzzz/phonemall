package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.vo.CartProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    List<CartProductVo> selectCartProductVoByUserId(Integer userId);

    int selectCheckCount(Integer userId);

    int deleteByUserIdProductIds (@Param("userId") Integer userId, @Param("productIdList") List<String> productIdLst);

    int selectOrNot (@Param("userId") Integer userId,@Param("productId") Integer productId, @Param("checked") Integer checked);

    int countProductNum (Integer userId);
}