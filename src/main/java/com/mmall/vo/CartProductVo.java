package com.mmall.vo;

import java.math.BigDecimal;

/**
 * @author Zero
 * create in 14:54 2018/8/21
 */
public class CartProductVo {
    private Integer cartId;
    private Integer userId;
    private Integer productId;
    private String productName;
    //购物车中此商品的数量
    private Integer quantity;
    private String subtitle;
    private String productMainImage;
    //库存
    private Integer productStock;
    private Integer productStatus;
    //是否勾选
    private Integer productCheck;
    private BigDecimal productPrice;
    private BigDecimal productTotalPrice;

    /**
     * 限制数量的一个返回结果
     */
    private String limitQuantity;

    public CartProductVo(Integer cartId, Integer userId, Integer productId, String productName, Integer quantity,
                         String subtitle, String productMainImage, Integer productStock, Integer productStatus,
                         Integer productCheck, BigDecimal productPrice, BigDecimal productTotalPrice, String limitQuantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.subtitle = subtitle;
        this.productMainImage = productMainImage;
        this.productStock = productStock;
        this.productStatus = productStatus;
        this.productCheck = productCheck;
        this.productPrice = productPrice;
        this.productTotalPrice = productTotalPrice;
        this.limitQuantity = limitQuantity;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getProductCheck() {
        return productCheck;
    }

    public void setProductCheck(Integer productCheck) {
        this.productCheck = productCheck;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(String limitQuantity) {
        this.limitQuantity = limitQuantity;
    }
}
