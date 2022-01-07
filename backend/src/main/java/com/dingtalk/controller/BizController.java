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
     * 获取订货单列表
     *
     * @return obj
     */
    @RequestMapping(value = "/getOrderList", method = RequestMethod.GET)
    public AjaxResult<?> getOrderList(@RequestParam String userId) {
        return AjaxResult.success(bizService.getOrderList(userId));
    }

    /**
     * 保存订货单订单
     *
     * @param params 订货单信息
     * @return ajax
     */
    @PostMapping(value = "/saveSaleBillInfo")
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

    /**
     * 删除单据信息
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public AjaxResult<Object> deleteSaleBillInfoById(@RequestParam(value = "id") String id,
                                                     @RequestParam(value = "userId") String userId) throws Exception {
        return AjaxResult.success(bizService.deleteSaleBillInfoById(id, userId));
    }

    public AjaxResult<Object> querySaleBillInfo(){
        return null ;
    }

}
