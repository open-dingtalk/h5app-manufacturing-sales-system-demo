package com.dingtalk.common.util.yida;

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
     * 商品 表单id
     */
    public static String COMMODITY_FORM_ID;


    /**
     * 客户表单id
     */
    public static String CUSTOMER_FORM_ID;


    /**
     * 订货单 表单id
     */
    public static String ORDERBILL_FORM_ID;


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
     * 商品 表单id
     *
     * @param commodityFormId 商品表单id
     */
    @Value(value = "${yida.commodityFormId}")
    public void setCommodityFormId(String commodityFormId) {
        YiDaConfig.COMMODITY_FORM_ID = commodityFormId;
    }

    /**
     * 客户表单id
     *
     * @param customerFormId 客户表单id
     */
    @Value(value = "${yida.customerFormId}")
    public void setCustomerFormId(String customerFormId) {
        YiDaConfig.CUSTOMER_FORM_ID = customerFormId;
    }

    /**
     * 订货单 表单id
     *
     * @param orderbillFormId 订货单表单id
     */
    @Value(value = "${yida.orderbillFormId}")
    public void setOrderbillFormId(String orderbillFormId) {
        YiDaConfig.ORDERBILL_FORM_ID = orderbillFormId;
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
