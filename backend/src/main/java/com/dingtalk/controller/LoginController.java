package com.dingtalk.controller;

import cn.hutool.core.lang.Assert;
import com.dingtalk.core.annotation.NotNeedLogin;
import com.dingtalk.core.domain.AjaxResult;
import com.dingtalk.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口控制器
 *
 * @author xueyu
 */
@Slf4j
@RestController
@RequestMapping(value = "/login/")
public class LoginController {

    private final LoginService loginService;


    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @NotNeedLogin
    @RequestMapping(value = "/dingtalk/{code}", method = RequestMethod.GET)
    public AjaxResult<Object> dingTalkLoginByCode(@PathVariable(value = "code") String code) {
        Assert.notBlank(code, () -> new RuntimeException("code不能为空!"));
        return AjaxResult.success(loginService.dingTalkLongByCode(code));
    }


}
