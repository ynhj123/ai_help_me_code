package com.example.ecommerce.exception;

import com.example.ecommerce.vo.ResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e 业务异常
     * @return 响应结果
     */
    @ExceptionHandler(BusinessException.class)
    public ResultVO<Void> handleBusinessException(BusinessException e) {
        return ResultVO.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数验证异常
     *
     * @param e 参数验证异常
     * @return 响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResultVO.error(400, "参数验证失败", errors);
    }

    /**
     * 处理绑定异常
     *
     * @param e 绑定异常
     * @return 响应结果
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Map<String, String>> handleBindException(BindException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResultVO.error(400, "参数绑定失败", errors);
    }

    /**
     * 处理用户名未找到异常
     *
     * @param e 用户名未找到异常
     * @return 响应结果
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResultVO<Void> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResultVO.error(401, "用户名或密码错误");
    }

    /**
     * 处理凭证错误异常
     *
     * @param e 凭证错误异常
     * @return 响应结果
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResultVO<Void> handleBadCredentialsException(BadCredentialsException e) {
        return ResultVO.error(401, "用户名或密码错误");
    }

    /**
     * 处理访问拒绝异常
     *
     * @param e 访问拒绝异常
     * @return 响应结果
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResultVO<Void> handleAccessDeniedException(AccessDeniedException e) {
        return ResultVO.error(403, "权限不足");
    }

    /**
     * 处理其他异常
     *
     * @param request 请求
     * @param e       异常
     * @return 响应结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO<Void> handleException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        return ResultVO.error(500, "系统内部错误");
    }
}