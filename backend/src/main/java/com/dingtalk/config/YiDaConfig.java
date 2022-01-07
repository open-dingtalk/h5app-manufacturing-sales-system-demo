package com.dingtalk.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 宜搭相关配置
 *
 * @author xueyu
 */
@Slf4j
@Component
public class YiDaConfig {
    /**
     * 应用编码
     */
    public static String APP_CODE;


    /**
     * 应用密钥
     */
    public static String APP_SECRET_KEY;

    /**
     * 订货单 表单id
     */
    public static String ORDER_FORM_ID;


    /**
     * 钉钉应用AgentId
     */
    public static String DD_GENT_ID;


    /**
     * 钉钉应用AppKey
     */
    public static String DD_APP_KEY;


    /**
     * 钉钉AppSecret
     */
    public static String DD_APP_SECRET;


    public static String DD_CORP_ID;


    public static String getDdCorpId() {
        return DD_CORP_ID;
    }

    @Value(value = "${dingTalk.corpId}")
    public void setDdCorpId(String ddCorpId) {
        DD_CORP_ID = ddCorpId;
    }

    /**
     * 宜搭的应用编码
     *
     * @param appCode 应用编码
     */
    @Value(value = "${yida.appCode}")
    public void setAppCode(String appCode) {
        YiDaConfig.APP_CODE = appCode;
    }

    /**
     * 宜搭应用密钥
     *
     * @param appSecretKey 应用密钥
     */
    @Value(value = "${yida.appSecretKey}")
    public void setAppSecretKey(String appSecretKey) {
        YiDaConfig.APP_SECRET_KEY = appSecretKey;
    }

    /**
     * 订货单 表单id
     *
     * @param orderFormId 订货单表单id
     */
    @Value(value = "${yida.orderFormId}")
    public void setOrderFormId(String orderFormId) {
        YiDaConfig.ORDER_FORM_ID = orderFormId;
    }

    /**
     * 钉钉应用AgentId
     */
    @Value(value = "${dingTalk.agentId}")
    private void setDdGentId(String ddGentId) {
        YiDaConfig.DD_GENT_ID = ddGentId;
    }

    @Value(value = "${dingTalk.appKey}")
    private void setDdAppKey(String ddAppKey) {
        YiDaConfig.DD_APP_KEY = ddAppKey;
    }

    @Value(value = "${dingTalk.appSecret}")
    private void setDdAppSecret(String ddAppSecret) {
        YiDaConfig.DD_APP_SECRET = ddAppSecret;
    }
}
