package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Administrator on 2017/12/11.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String USERNAME = "username";

    public static final String EMAIL = "email";

    public interface ProductOrderByPrice{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    public interface Role{

        /**
         * 普通用户
         */
        int ROLE_CUSTOMER = 0;
        /**
         * 管理员
         */
        int ROLE_ADMIN = 1;
    }

    public interface Cart{
        /**
         * 选中状态
         */
        int CHECK = 1;

        /**
         * 为选中状态
         */
        int UN_CHECK = 0;

        String STOCK_NUM_SUFFICIENT = "STOCK_NUM_SUFFICIENT";
        String STOCK_NUM_INSUFFICIENT = "STOCK_NUM_INSUFFICIENT";
    }

    public enum SaleStateEnum{
        ON_SALE(1,"在售");
        private int code;
        private String msg;
        SaleStateEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
