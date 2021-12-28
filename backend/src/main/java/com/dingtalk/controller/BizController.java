package com.dingtalk.controller;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.core.domain.AjaxResult;
import com.dingtalk.service.BizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * 业务接口控制层
 *
 * @author xueyu
 */
@Slf4j
@RestController
@RequestMapping(value = "/biz")
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
    @RequestMapping(value = "/getCustomerList", method = RequestMethod.POST)
    public AjaxResult<?> getCustomerList(@RequestParam String userId) {
        return AjaxResult.success(bizService.getCustomerList(userId));
    }

    /**
     * 获取商品列表数据
     *
     * @return Ajax
     */
    @RequestMapping(value = "/getCommodityList", method = RequestMethod.POST)
    public AjaxResult<Object> getCommodityList(@RequestParam String userId) {
        return AjaxResult.success(bizService.getCommodityList(userId));
    }


    /**
     * 保存单据信息
     *
     * @param params 单据信息
     * @return ajax
     */
    @RequestMapping(value = "/saveSaleBillInfo", method = RequestMethod.POST)
    public AjaxResult<Object> saveSaleBillInfo(@RequestBody String params) {
        System.out.println(params);
        JSONObject billInfo = JSONObject.parseObject(params);
        try {
            return AjaxResult.success(bizService.saveSaleBillInfo(billInfo));
        } catch (ParseException e) {
            e.printStackTrace();
            return AjaxResult.error("时间格式有误");
        }
    }
}
