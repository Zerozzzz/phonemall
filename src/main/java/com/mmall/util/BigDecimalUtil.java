package com.mmall.util;

import java.math.BigDecimal;

/**
 * @author Zero
 * create in 15:50 2018/8/21
 */
public class BigDecimalUtil {

    private BigDecimalUtil() {}

    public static BigDecimal add (double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.add(b2);
    }

    public static BigDecimal subtract (double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2);
    }

    public static BigDecimal multiply (double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2);
    }

    /**
     *
     * @param value1
     * @param value2
     * @return 返回值四舍五入
     */
    public static BigDecimal divide (double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
    }
}
