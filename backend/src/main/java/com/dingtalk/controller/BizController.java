package com.dingtalk.controller;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.core.domain.AjaxResult;
import com.dingtalk.service.BizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务接口控制层
 *
 * @author xueyu
 */
@Slf4j
@RestController
@RequestMapping(value = "/biz/")
public class BizController {

    private final BizService bizService;

    @Autowired
    public BizController(BizService bizService) {
        this.bizService = bizService;
    }


    /**
     * 获取客户列表
     *
     * @return obj
     */
    @RequestMapping(value = "/getCustomerList")
    public AjaxResult<?> getCustomerList() {
        return AjaxResult.success(bizService.getCustomerList());
    }

    /**
     * 获取商品列表数据
     *
     * @return Ajax
     */
    @RequestMapping(value = "/getCommodityList")
    public AjaxResult<Object> getCommodityList() {
        return AjaxResult.success(bizService.getCommodityList());
    }


    /**
     * 保存单据信息
     *
     * @param billInfo 单据信息
     * @return ajax
     */
    @RequestMapping(value = "saveSaleBillInfo")
    public AjaxResult<Object> saveSaleBillInfo(@RequestBody JSONObject billInfo) {
        return AjaxResult.success(bizService.saveSaleBillInfo(billInfo));
    }
}
