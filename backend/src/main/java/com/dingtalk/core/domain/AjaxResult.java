package com.dingtalk.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 操作消息提醒
 *
 * @param <T> 泛型
 * @author xueyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AjaxResult<T> implements Serializable {

    private static final long serialVersionUID = -2098930654261445692L;


    /**
     * 状态码
     */
    private int code;

    /**
     * 返回内容
     */
    private String msg;

    /**
     * 数据对象
     */
    private T data;


    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<>(200, "success", data);
    }

    /**
     * 返回错误消息
     *
     * @return AjaxResult
     */
    public static <T> AjaxResult<T> error(int code, String msg) {
        return new AjaxResult<>(code, msg, null);
    }


    /**
     * 返回错误消息
     *
     * @return AjaxResult
     */
    public static <T> AjaxResult<T> error(String msg) {
        return new AjaxResult<>(500, msg, null);
    }

}
