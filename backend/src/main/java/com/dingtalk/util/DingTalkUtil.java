package com.dingtalk.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.dingtalk.config.YiDaConfig;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;

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
     * 通过code  获取用户信息
     *
     * @param code code 免登码
     * @return obj
     */
    public static String getDdUserIdByCode(String code, String accessToken) {
        Assert.notBlank(code, () -> new RuntimeException("code不能为空!"));
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
    public static OapiV2UserGetResponse.UserGetResponse getDingUserByUserId(String userId, String accessToken) {
        Assert.notBlank(userId, () -> new RuntimeException("用户id不能为空!"));
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(userId);
        req.setLanguage("zh_CN");
        try {
            OapiV2UserGetResponse rsp = client.execute(req, accessToken);
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
    public static String getYiDaInstances(JSONObject params, String accessToken) {
        HttpRequest request = HttpUtil.createPost(DingTalkUrlConstant.getYiDaInstancesUrl())
                .header("x-acs-dingtalk-access-token", accessToken)
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
     * 保存宜搭表单数据
     *
     * @param params json参数
     * @return string
     */
    public static String saveYiDaFormData(JSONObject params, String accessToken) {
        System.out.println(params.toJSONString());
        String url = DingTalkUrlConstant.getYiDaSaveFormDateUrl();
        String result = HttpRequest.post(url).header("x-acs-dingtalk-access-token", accessToken).body(params.toJSONString()).execute().body();
        if (ObjectUtil.isNotNull(result) ||JSONObject.parseObject(result).getString("code").equals(HttpStatus.HTTP_OK)) {
            return result;
        } else {
            throw new RuntimeException("http--请求错误! 错误信息：" + result);
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
    public static String deleteYiDaInstances(String appType, String systemToken, String userId, String processInstanceId, String accessToken) {
        String url = DingTalkUrlConstant.getDeleteYiDaInstancesUrl(appType, systemToken, userId, processInstanceId);
        HttpResponse httpResponse = HttpRequest.delete(url)
                .header("x-acs-dingtalk-access-token", accessToken)
                .contentType(ContentType.JSON.getValue())
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
    public static String getYiDaInstanceIds(long pageSize, long pageNumber, JSON bodyParams,  String accessToken) {
        String url = DingTalkUrlConstant.getYiDaInstanceIdsUrl(pageSize, pageNumber);
        HttpResponse httpResponse = HttpRequest.post(url)
                .contentType(ContentType.JSON.getValue())
                .header("x-acs-dingtalk-access-token", accessToken)
                .body(bodyParams.toJSONString())
                .execute();
        if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
            return httpResponse.body();
        } else {
            throw new RuntimeException("http--请求错误!");
        }
    }



}
