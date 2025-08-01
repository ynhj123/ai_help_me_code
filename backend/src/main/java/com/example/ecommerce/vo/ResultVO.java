package com.example.ecommerce.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 *
 * @param <T> 数据类型
 */
@Data
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 私有构造函数
     */
    private ResultVO() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 私有构造函数
     *
     * @param code    响应码
     * @param message 响应消息
     * @param data    响应数据
     */
    private ResultVO(int code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应
     *
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResultVO<T> success() {
        return new ResultVO<>(200, "success", null);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应结果
     */
    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(200, "success", data);
    }

    /**
     * 成功响应
     *
     * @param message 响应消息
     * @param data    数据
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> ResultVO<T> success(String message, T data) {
        return new ResultVO<>(200, message, data);
    }

    /**
     * 错误响应
     *
     * @param code    响应码
     * @param message 响应消息
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> ResultVO<T> error(int code, String message) {
        return new ResultVO<>(code, message, null);
    }

    /**
     * 错误响应
     *
     * @param code    响应码
     * @param message 响应消息
     * @param data    数据
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> ResultVO<T> error(int code, String message, T data) {
        return new ResultVO<>(code, message, data);
    }
}