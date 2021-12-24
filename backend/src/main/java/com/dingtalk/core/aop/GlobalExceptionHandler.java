package com.dingtalk.core.aop;


import cn.hutool.http.HttpStatus;
import com.dingtalk.core.domain.AjaxResult;
import com.dingtalk.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

/**
 * 异常消息处理器
 *
 * @author xueyu
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public AjaxResult<?> businessException(CustomException e) {
        if (Objects.isNull(e.getCode())) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public AjaxResult<?> handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error(HttpStatus.HTTP_NOT_FOUND, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error("系统出现异常,请联系管理员!");
    }

    @ExceptionHandler(RuntimeException.class)
    public AjaxResult<?> handleException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error("系统出现异常,请联系管理员!");
    }


}
