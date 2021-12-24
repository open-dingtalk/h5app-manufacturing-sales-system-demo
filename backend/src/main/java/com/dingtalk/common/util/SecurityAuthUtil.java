package com.dingtalk.common.util;

import cn.hutool.core.lang.Assert;
import com.dingtalk.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author xueyu
 */
@Slf4j
public class SecurityAuthUtil {


    /**
     * 获取userId
     *
     * @return userId Long
     */
    public static String getUserIdByRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Assert.notNull(servletRequestAttributes);
        String userId = (String) servletRequestAttributes.getRequest().getAttribute("ddUserId");
        Assert.notBlank(userId, () -> new CustomException("登录失败!"));
        return userId;
    }


}
