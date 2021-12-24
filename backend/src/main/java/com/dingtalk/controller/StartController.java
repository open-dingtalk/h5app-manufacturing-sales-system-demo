package com.dingtalk.controller;


import com.dingtalk.core.annotation.NotNeedLogin;
import com.dingtalk.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xueyu
 */
@Slf4j
@RestController
@RequestMapping(value = "/")
public class StartController {


    @NotNeedLogin
    @RequestMapping("")
    public AjaxResult<Object> start() {
        return AjaxResult.success("启动成功!");
    }


}
