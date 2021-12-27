package com.dingtalk.common.util.ding;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.http.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.dingtalk.common.util.yida.YiDaConfig;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 钉钉工具类
 *
 * @author xueyu
 */
@Slf4j
public class DingTalkUtil {

    private static final String DING_TOKEN_KEY = "ding_token_key";
    /**
     * 钉钉appKey
     */
    private static final String DING_APP_KEY = YiDaConfig.DD_APP_KEY;
    /**
     * 钉钉App Secret
     */
    private static final String DING_APP_SECRET = YiDaConfig.DD_APP_SECRET;

    /**
     * accesstoken
     */
    private static final Map<String, Object> accessTokenCacheMap = new LinkedHashMap<>();


    /**
     * 获取accesstoken
     *
     * @return obj
     */
    public static String getAccessToken() {

        Map<String, Object> accessMap = (Map<String, Object>) accessTokenCacheMap.get(DING_TOKEN_KEY);
        //优先从缓存获取
        String accessToken = (String) accessMap.get("accessToken");
        Date expireTime = (Date) accessMap.get("expireTime");
        if (expireTime.before(new Date())) {
            accessToken = "";
        }
        if (StringUtils.isBlank(accessToken)) {
            DingTalkClient dingTalkClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(DING_APP_KEY);
            request.setAppsecret(DING_APP_SECRET);
            try {
                OapiGettokenResponse response = dingTalkClient.execute(request);
                if (response.isSuccess()) {
                    accessToken = response.getAccessToken();
                    accessMap.put("accessToken", accessToken);
                    accessMap.put("expireTime", DateUtil.offset(new Date(), DateField.SECOND, 7200));
                }
            } catch (ApiException e) {
                log.error("获取accessToken出现异常", e);
            }
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new RuntimeException("获取钉钉AccessToken异常!");
        }
        return accessToken;
    }


    /**
     * 通过code  获取用户信息
     *
     * @param code code 免登码
     * @return obj
     */
    public static String getDdUserIdByCode(String code) {
        Assert.notBlank(code, () -> new RuntimeException("code不能为空!"));
        String accessToken = getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getuserinfo");
        OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
        req.setCode(code);
        try {
            OapiV2UserGetuserinfoResponse rsp = client.execute(req, accessToken);
            if (rsp.isSuccess()) {
                return rsp.getResult().getUserid();
            } else {
                throw new RuntimeException(rsp.getMessage());
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 通过userId  获取钉钉用户身份
     *
     * @param userId userId
     * @return UserGetResponse
     */
    public static OapiV2UserGetResponse.UserGetResponse getDingUserByUserId(String userId) {
        Assert.notBlank(userId, () -> new RuntimeException("用户id不能为空!"));
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(userId);
        req.setLanguage("zh_CN");
        try {
            OapiV2UserGetResponse rsp = client.execute(req, getAccessToken());
            if (rsp.isSuccess()) {
                return rsp.getResult();
            } else {
                throw new RuntimeException(rsp.getMessage());
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 查询宜搭表单实例数据
     *
     * @param params 参数
     * @return obj
     */
    public static String getYidaInstances(JSONObject params) {
        HttpRequest request = HttpUtil.createPost(DingTalkUrlConstant.getYidaInstancesUrl())
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .contentType(ContentType.JSON.getValue())
                .body(params.toJSONString());
        HttpResponse response = request.execute();
        if (response.getStatus() == HttpStatus.HTTP_OK) {
            return response.body();
        } else {
            throw new RuntimeException("http--请求错误");
        }
    }


    /**
     * 查询宜搭表单数据
     *
     * @param instancesId 流程实例ID
     * @param appType     应用ID
     * @param systemToken 应用密钥
     * @param userId      用户id
     * @return String
     */
    public static String getYidaFormData(String instancesId, String appType, String systemToken, String userId) {
        String url = DingTalkUrlConstant.getYidaFormDataUrl(instancesId, appType, systemToken, userId);
        log.info("url=====>:{} ", url);
        HttpRequest request = HttpUtil.createGet(url)
                .header("x-acs-dingtalk-access-token", getAccessToken());
        HttpResponse response = request.execute();
        if (response.getStatus() == HttpStatus.HTTP_OK) {
            return response.body();
        } else {
            log.error("error:{}", response.getStatus());
            throw new RuntimeException("http--请求错误");
        }
    }


    /**
     * 保存宜搭表单数据
     *
     * @param params json参数
     * @return string
     */
    public static String saveYidaFormData(JSONObject params) {
        String url = DingTalkUrlConstant.getYidaSaveFormDateUrl();
        HttpResponse httpResponse = HttpUtil.createPost(url)
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .contentType(ContentType.JSON.getValue())
                .body(params.toJSONString())
                .execute();
        if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
            return httpResponse.body();
        } else {
            throw new RuntimeException("http--请求错误!");
        }
    }

    /**
     * 更新宜搭表单数据
     *
     * @param params 参数
     * @return string
     */
    public static String updateYidaFormData(JSONObject params) {
        params.put("useLatestVersion", true);
        String url = DingTalkUrlConstant.getYidaSaveFormDateUrl();
        HttpResponse httpResponse = HttpUtil.createPost(url)
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .contentType(ContentType.JSON.getValue())
                .body(params.toJSONString())
                .execute();
        if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
            return httpResponse.body();
        } else {
            throw new RuntimeException("http--请求错误!");
        }
    }

    /**
     * 发起宜搭表单流程实例
     *
     * @param params 参数
     * @return string
     */
    public static String startYidaInstances(JSONObject params) {
        String url = DingTalkUrlConstant.startYidaInstances();
        HttpResponse httpResponse = HttpUtil.createPost(url)
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .contentType(ContentType.JSON.getValue())
                .body(params.toJSONString())
                .execute();
        if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
            return httpResponse.body();
        } else {
            throw new RuntimeException("http--请求错误!");
        }
    }

    /**
     * 更新宜搭表单数据
     *
     * @param params 参数
     * @return obj
     */
    public static String updateYidaInstances(JSONObject params) {
        String url = DingTalkUrlConstant.getUpdateYidaInstancesUrl();
        HttpRequest httpRequest = HttpRequest.put(url).contentType(ContentType.JSON.getValue())
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .body(params.toJSONString());
        HttpResponse httpResponse = httpRequest.execute();
        if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
            return httpResponse.body();
        } else {
            throw new RuntimeException("http--请求错误!");
        }
    }

    /**
     * 终止宜搭流程实例
     *
     * @param appType           应用id
     * @param systemToken       应用密钥
     * @param userId            用户的userID
     * @param processInstanceId 流程实例id
     * @return String
     */
    public static String terminateYidaInstances(String appType, String systemToken, String userId, String processInstanceId) {
        String url = DingTalkUrlConstant.getTerminateYidaInstancesUrl(appType, systemToken, userId, processInstanceId);
        HttpResponse response = HttpRequest.put(url)
                .contentType(ContentType.JSON.getValue())
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .execute();
        if (response.getStatus() == HttpStatus.HTTP_OK) {
            return response.body();
        } else {
            throw new RuntimeException("http--请求错误!");
        }
    }

    /**
     * 删除宜搭流程实例数据
     *
     * @param appType           应用标识
     * @param systemToken       应用秘钥
     * @param userId            用户的userid
     * @param processInstanceId 流程实例Id
     * @return string
     */
    public static String deleteYidaInstances(String appType, String systemToken, String userId, String processInstanceId) {
        String url = DingTalkUrlConstant.getDeleteYidaInstancesUrl(appType, systemToken, userId, processInstanceId);
        HttpResponse httpResponse = HttpRequest.delete(url)
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .contentType(ContentType.JSON.getValue())
                .execute();
        if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
            return httpResponse.body();
        } else {
            throw new RuntimeException("http--请求错误!");
        }
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
    public static String getYidaProcessesOperationRecords(String appType, String systemToken, String userId, String processInstanceId) {
        String url = DingTalkUrlConstant.getYidaProcessesOperationRecordsUrl(appType, systemToken, userId, processInstanceId);
        log.info("url=====> :{}", url);
        HttpResponse httpResponse = HttpRequest.get(url)
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .execute();
        if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
            return httpResponse.body();
        } else {
            throw new RuntimeException("http--请求错误!");
        }
    }


    /**
     * 获取宜搭实例ID列表
     *
     * @param pageSize   页大小
     * @param pageNumber 页码
     * @param bodyParams 参数
     * @return string
     */
    public static String getYidaInstanceIds(long pageSize, long pageNumber, JSON bodyParams) {
        String url = DingTalkUrlConstant.getYidaInstanceIdsUrl(pageSize, pageNumber);
        HttpResponse httpResponse = HttpRequest.post(url)
                .contentType(ContentType.JSON.getValue())
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .body(bodyParams.toJSONString())
                .execute();
        if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
            return httpResponse.body();
        } else {
            throw new RuntimeException("http--请求错误!");
        }
    }

    /**
     * 根据宜搭流程实例id获取宜搭流程数据
     *
     * @param instancesId 实例id
     * @param appType     应用id
     * @param systemToken 应用秘钥
     * @param userId      用户userid
     * @return string
     */
    public static String getYidaInstancesInfos(String instancesId, String appType, String systemToken, String userId) {
        String url = DingTalkUrlConstant.getYidaInstancesInfosUrl(instancesId, appType, systemToken, userId);
        HttpResponse response = HttpRequest.get(url)
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .execute();
        if (response.getStatus() == HttpStatus.HTTP_OK) {
            return response.body();
        } else {
            throw new RuntimeException("http--请求错误!");
        }
    }


}