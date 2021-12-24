package com.dingtalk.common.util.ding;

/**
 * dingTalk 网页常用链接
 *
 * @author xueyu
 */
public class DingTalkUrlConstant {


    private static final String BASE_URL = "https://oapi.dingtalk.com";

    private static final String BASE_URL_NEW = "https://api.dingtalk.com";


    /**
     * 获取 AccessToken  Url
     *
     * @param appKey    appKey
     * @param appSecret appSecret
     * @return Str
     */
    public static String getAccessTokenUrl(String appKey, String appSecret) {
        String baseUrl = "https://oapi.dingtalk.com/gettoken?appkey=";
        return baseUrl.concat(appKey).concat("&appsecret=").concat(appSecret);
    }


    /**
     * 获取 宜搭表单实例数据 接口url
     *
     * @return Str
     */
    public static String getYidaInstancesUrl() {
        return String.format("%s/v1.0/yida/forms/instances/search", BASE_URL_NEW);
    }


    /**
     * 获取 查询宜搭表单数据的 接口地址
     *
     * @param instancesId 流程实例ID
     * @param appType     应用ID
     * @param systemToken 应用密钥
     * @param userId      用户id
     * @return String
     */
    public static String getYidaFormDataUrl(String instancesId, String appType, String systemToken, String userId) {
        return BASE_URL_NEW.concat(" /v1.0/yida/forms/instances/").concat(instancesId).concat("?appType=")
                .concat(appType).concat("&systemToken=").concat(systemToken).concat("&userId=").concat(userId)
                .concat("&language=zh_CN");
    }


    /**
     * 获取  发起宜搭审批实例 url
     *
     * @return url
     */
    public static String startYidaInstances() {
        return BASE_URL_NEW.concat("/v1.0/yida/processes/instances/start");
    }


    /**
     * 获取 保存宜搭表单数据 接口url
     *
     * @return url
     */
    public static String getYidaSaveFormDateUrl() {
        return BASE_URL_NEW.concat("/v1.0/yida/forms/instances");
    }

    /**
     * 获取  更新宜搭流程 接口 url
     *
     * @return url
     */
    public static String getUpdateYidaInstancesUrl() {
        return BASE_URL_NEW.concat("/v1.0/yida/processes/instances");
    }

    /**
     * 获取  宜搭终止流程 接口url
     *
     * @param appType           应用id
     * @param systemToken       应用秘钥
     * @param userId            用户的userid
     * @param processInstanceId 流程实例ID
     * @return string
     */
    public static String getTerminateYidaInstancesUrl(String appType, String systemToken, String userId, String processInstanceId) {
        return BASE_URL_NEW +
                "/v1.0/yida/processes/instances/terminate?" +
                "appType=" + appType + "&systemToken=" +
                systemToken + "&userId=" +
                "&language=zh_CN" +
                "&processInstanceId=" + processInstanceId;
    }

    /**
     * 获取  宜搭删除流程 接口url
     *
     * @param appType           应用id
     * @param systemToken       应用秘钥
     * @param userId            用户的userid
     * @param processInstanceId 流程实例ID
     * @return string
     */
    public static String getDeleteYidaInstancesUrl(String appType, String systemToken, String userId, String processInstanceId) {
        return BASE_URL_NEW.concat("/v1.0/yida/processes/instances")
                .concat("?appType=").concat(appType).concat("&systemToken=").concat(systemToken)
                .concat("&userId=").concat(userId).concat("&language=zh_CN")
                .concat("&processInstanceId=").concat(processInstanceId);
    }

    /**
     * 获取宜搭审批记录
     *
     * @param appType           应用ID
     * @param systemToken       应用秘钥
     * @param userId            用户的userid
     * @param processInstanceId 流程实例ID
     * @return string
     */
    public static String getYidaProcessesOperationRecordsUrl(String appType, String systemToken, String userId, String processInstanceId) {
        return BASE_URL_NEW.concat("/v1.0/yida/processes/operationRecords")
                .concat("?appType=").concat(appType)
                .concat("&systemToken=").concat(systemToken)
                .concat("&userId=").concat(userId)
                .concat("&language=zh_CN")
                .concat("&processInstanceId=").concat(processInstanceId);
    }

    /**
     * 获取宜搭流程 获取实例ID列表
     *
     * @param pageSize   分页大小
     * @param pageNumber 分页页码
     * @return url
     */
    public static String getYidaInstanceIdsUrl(long pageSize, long pageNumber) {
        return BASE_URL_NEW.concat("/v1.0/yida/processes/instanceIds")
                .concat("?pageSize=").concat(String.valueOf(pageSize))
                .concat("&pageNumber=").concat(String.valueOf(pageNumber));
    }

    /**
     * 根据宜搭流程实例ID获取宜搭流程实例
     *
     * @param instancesId 流程实例id
     * @param appType     应用ID
     * @param systemToken 应用秘钥
     * @param userId      用户userid
     * @return string
     */
    public static String getYidaInstancesInfosUrl(String instancesId, String appType, String systemToken, String userId) {
        return BASE_URL_NEW.concat("/v1.0/yida/processes/instancesInfos/")
                .concat(instancesId).concat("?appType=").concat(appType)
                .concat("&systemToken=").concat(systemToken).concat("&userId=").concat(userId)
                .concat("&language=zh_CN");
    }
}
