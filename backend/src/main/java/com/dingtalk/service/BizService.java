package com.dingtalk.service;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;

/**
 * 业务逻辑层
 *
 * @author xueyu
 */
public interface BizService {


    /**
     * 获取客户列表
     *
     * @return obj
     */
    Object getCustomerList(String userId);

    /**
     * 获取商品列表信息
     *
     * @return obj
     */
    Object getCommodityList(String userId);

    /**
     * 保存销售订单
     *
     * @param billInfo 单据信息
     * @return obj
     */
    Object saveSaleBillInfo(JSONObject billInfo) throws ParseException;
}
