package com.dingtalk.core.interceptor;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.dingtalk.core.annotation.NotNeedLogin;
import com.dingtalk.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.web.cors.CorsUtils.isPreFlightRequest;

/**
 * 拦截器
 *
 * @author xueyu
 */
@Slf4j
@Component
public class DingTalkLoginInterceptor implements AsyncHandlerInterceptor {

    public final static Map<String, Map<String, Object>> userInfoMaps = new LinkedHashMap<>();


    /**
     * Interception point before the execution of a handler. Called after
     * HandlerMapping determined an appropriate handler object, but before
     * HandlerAdapter invokes the handler.
     * <p>DispatcherServlet processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can decide to abort the execution chain,
     * typically sending an HTTP error or writing a custom response.
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing. For more details see
     * {@link AsyncHandlerInterceptor}.
     * <p>The default implementation returns {@code true}.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     * @throws Exception in case of errors
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //是否为预检查请求
        if (!isPreFlightRequest(request)) {
            return preHandleNoCors(request, response, handler);
        }
        return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 认证处理
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return boolean
     */
    private boolean preHandleNoCors(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!isNeedLogin((HandlerMethod) handler)) {
            return true;
        }
        Date interceptTime = new Date();
        String token = request.getHeader("authentication");
        Assert.notBlank(token, () -> new CustomException("登录失败!"));
        Map<String, Object> userMap = userInfoMaps.get(token);
        Assert.notNull(userMap, () -> new CustomException("登录失败!"));
        String userId = (String) userMap.get("userId");
        Date expireTime = (Date) userMap.get("expireTime");
        if (interceptTime.after(expireTime)) {
            userInfoMaps.remove(token);
            throw new CustomException("登录过期!");
        }
        //续签
        userMap.put("expireTime", DateUtil.offset(new Date(), DateField.MINUTE, 30));
        Assert.notNull(expireTime, () -> new CustomException("登录失败！"));
        log.info("loginUserId:{}", userId);
        Assert.notBlank(userId, () -> new CustomException("登录失败!"));
        request.setAttribute("ddUserId", userId);
        return true;
    }

    private boolean isNeedLogin(HandlerMethod handler) {
        NotNeedLogin classNotNeedLogin = handler.getBean().getClass().getAnnotation(NotNeedLogin.class);
        //方法上
        NotNeedLogin methodNotNeedLogin = handler.getMethodAnnotation(NotNeedLogin.class);
        if (Objects.nonNull(methodNotNeedLogin)) {
            return false;
        }
        return Objects.isNull(classNotNeedLogin);
    }


}
