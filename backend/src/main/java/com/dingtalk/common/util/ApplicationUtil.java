package com.dingtalk.common.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 应用Bean工具获取类
 *
 * @author xueyu
 * @date 2021-08-13 00:01:38
 */
@Slf4j
@Component
public class ApplicationUtil implements ApplicationContextAware {

    private static ApplicationContext appContext = null;

    /**
     * 通过指定的id 获取实例bean
     *
     * @param id id
     * @return bean
     */
    public static Object getBean(String id) {
        return appContext.getBean(id);
    }

    /**
     * 判断平台中是否包含指定的ID实例。
     *
     * @param id id
     * @return boolean
     */
    public static boolean containBean(String id) {
        return appContext.containsBeanDefinition(id);
    }

    /**
     * 根据类型获取系统中配置的对象实例。
     *
     * @param beanClass beanClass
     * @return Bean
     */
    public static <T> T getBean(Class<T> beanClass) {
        return appContext.getBean(beanClass);
    }

    /**
     * 根据接口获取平台中的实现类。
     *
     * @param beanClass beanClass
     * @return Bean
     */
    public static <T> Collection<T> getBeanList(Class<T> beanClass) {
        Map<String, T> map = appContext.getBeansOfType(beanClass);
        return map.values();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }


}
