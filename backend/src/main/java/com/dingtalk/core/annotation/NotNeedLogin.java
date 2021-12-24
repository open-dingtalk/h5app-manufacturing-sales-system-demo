package com.dingtalk.core.annotation;

import java.lang.annotation.*;

/**
 * 是否需要登录注解
 *
 * @author xueyu
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNeedLogin {
    boolean value() default true;
}
