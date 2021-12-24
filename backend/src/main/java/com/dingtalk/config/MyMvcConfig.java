package com.dingtalk.config;

import com.dingtalk.core.interceptor.DingTalkLoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * mvc 配置
 *
 * @author xueyu
 */
@Slf4j
@EnableWebMvc
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Autowired
    private DingTalkLoginInterceptor dingTalkLoginInterceptor;


    /**
     * Add Spring MVC lifecycle interceptors for pre- and post-processing of
     * controller method invocations and resource handler requests.
     * Interceptors can be registered to apply to all requests or be limited
     * to a subset of URL patterns.
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(dingTalkLoginInterceptor).addPathPatterns("/**");
    }

    /**
     * 配置静态资源
     *
     * @param registry 资源处理注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }


    /**
     * cors 方式绕过跨域
     *
     * @param registry cors注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                //设置放行哪些原始域   SpringBoot2.4.4下低版本使用.allowedOrigins("*")
                /**
                 * When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on
                 *  the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins,
                 *  list them explicitly or consider using "allowedOriginPatterns" instead.
                 */
                // .allowedOrigins("*")
                // 是否允许证书（cookies）
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                // 允许跨域的请求头
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }


}
