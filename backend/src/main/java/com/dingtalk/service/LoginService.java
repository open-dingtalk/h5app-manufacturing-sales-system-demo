package com.dingtalk.service;

/**
 * 登录逻辑处理
 *
 * @author xueyu
 */
public interface LoginService {

    /**
     * 钉钉登录
     *
     * @param code code 五分钟
     * @return string
     */
    Object dingTalkLongByCode(String code);
}
