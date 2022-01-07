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
     * 获取订货单列表
     *
     * @param userId
     * @return obj
     */
    Object getOrderList(String userId);

    /**
     * 保存订货单
     *
     * @param billInfo 订货单信息
     * @return obj
     * @throws ParseException
     */
    Object saveSaleBillInfo(JSONObject billInfo) throws ParseException;

    /**
     * 根据流程实例id删除销售订单单据
     *
     * @param formInstanceId 流程实例ID
     * @param userId         用户的userId
     * @return
     * @throws Exception
     */
    Object deleteSaleBillInfoById(String formInstanceId, String userId) throws Exception;

}
