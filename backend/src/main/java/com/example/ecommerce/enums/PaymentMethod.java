package com.example.ecommerce.enums;

/**
 * 订单支付方式枚举
 */
public enum PaymentMethod {
    /**
     * 微信支付
     */
    WECHAT_PAY(1, "微信支付"),
    
    /**
     * 支付宝
     */
    ALIPAY(2, "支付宝"),
    
    /**
     * 银行卡
     */
    BANK_CARD(3, "银行卡"),
    
    /**
     * 货到付款
     */
    CASH_ON_DELIVERY(4, "货到付款");

    private final int code;
    private final String description;

    PaymentMethod(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取支付方式
     *
     * @param code 支付方式代码
     * @return 支付方式枚举
     */
    public static PaymentMethod fromCode(int code) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.getCode() == code) {
                return method;
            }
        }
        throw new IllegalArgumentException("未知的支付方式代码: " + code);
    }
}