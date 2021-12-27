package com.dingtalk.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.common.util.ding.DingTalkUtil;
import com.dingtalk.core.exception.CustomException;
import com.dingtalk.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录逻辑
 *
 * @author xueyu
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    public static Map<String, Map<String, Object>> userInfoMaps = new HashMap<>();


    /**
     * 钉钉登录
     *
     * @param code code 五分钟
     * @return string
     */
    @Override
    public Object dingTalkLongByCode(String code) {
        String userId = DingTalkUtil.getDdUserIdByCode(code);
        OapiV2UserGetResponse.UserGetResponse user = DingTalkUtil.getDingUserByUserId(userId);
        String ddUserId = user.getUserid();
        String token = UUID.randomUUID().toString(true);
        Map<String, Object> userInfo = new HashMap<>(2);
        userInfo.put("token", token);
        userInfo.put("userId", ddUserId);
        Date loginTime = new Date();
        userInfo.put("loginTime", loginTime);
        userInfo.put("expireTime", DateUtil.offset(loginTime, DateField.MINUTE, 30));
        userInfoMaps.put("user", userInfo);
        log.info("钉钉登录成功:{}", user.getName());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", ddUserId);
        jsonObject.put("userName", user.getName());
        jsonObject.put("token", token);
        return jsonObject;
    }

    public static String getUesrId(){
        return LoginServiceImpl.userInfoMaps.get("user").get("userId").toString();
    }
}