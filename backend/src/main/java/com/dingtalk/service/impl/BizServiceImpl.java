package com.dingtalk.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.common.util.ding.DingTalkUtil;
import com.dingtalk.common.util.yida.YiDaConfig;
import com.dingtalk.core.exception.CustomException;
import com.dingtalk.service.BizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 业务逻辑层
 *
 * @author xueyu
 */
@Slf4j
@Service
public class BizServiceImpl implements BizService {


    /**
     * 获取客户列表
     *
     * @return obj
     */
    @Override
    public Object getCustomerList(String userId) {
        JSONObject customerQueryJson = new JSONObject();
        customerQueryJson.put("appType", YiDaConfig.APP_CODE);
        customerQueryJson.put("systemToken", YiDaConfig.APP_SECRET_KEY);
        customerQueryJson.put("userId", userId);
        customerQueryJson.put("language", "zh_CN");
        //表单id
        customerQueryJson.put("formUuid", YiDaConfig.CUSTOMER_FORM_ID);
        //根据表单内组件值查询
        JSONObject searchFieldJson = new JSONObject();
        customerQueryJson.put("searchFieldJson", searchFieldJson.toJSONString());
        //分页参数 当前页数
        customerQueryJson.put("currentPage", 1);
        //每页显示数量
        customerQueryJson.put("pageSize", 10);

        String resString = DingTalkUtil.getYidaInstances(customerQueryJson);
        if (JSONUtil.isJson(resString)) {
            JSONObject responseJson = JSONObject.parseObject(resString);
            log.info("当前页：{}", responseJson.getString("currentPage"));
            log.info("总条数：{}", responseJson.getString("totalCount"));
            return responseJson.getJSONArray("data");
        }
        return Collections.emptyList();
    }


    /**
     * 获取商品列表信息
     *
     * @return obj
     */
    @Override
    public Object getCommodityList(String userId) {
        JSONObject customerQueryJson = new JSONObject();
        customerQueryJson.put("appType", YiDaConfig.APP_CODE);
        customerQueryJson.put("systemToken", YiDaConfig.APP_SECRET_KEY);
        customerQueryJson.put("userId", userId);
        customerQueryJson.put("language", "zh_CN");
        //表单id
        customerQueryJson.put("formUuid", YiDaConfig.COMMODITY_FORM_ID);
        //根据表单内组件值查询
        JSONObject searchFieldJson = new JSONObject();
        customerQueryJson.put("searchFieldJson", searchFieldJson.toJSONString());
        //分页参数 当前页数
        customerQueryJson.put("currentPage", 1);
        //每页显示数量
        customerQueryJson.put("pageSize", 10);

        String resString = DingTalkUtil.getYidaInstances(customerQueryJson);
        if (JSONUtil.isJson(resString)) {
            JSONObject responseJson = JSONObject.parseObject(resString);
            log.info("当前页：{}", responseJson.getString("currentPage"));
            log.info("总条数：{}", responseJson.getString("totalCount"));
            return responseJson.getJSONArray("data");
        }
        return Collections.emptyList();
    }


    /**
     * 保存销售订单
     *
     * @param billInfo 单据信息
     * @return obj
     */
    @Override
    public Object saveSaleBillInfo(JSONObject billInfo) {
        JSONObject saveBillJson = new JSONObject();
        saveBillJson.put("appType", YiDaConfig.APP_CODE);
        saveBillJson.put("systemToken", YiDaConfig.APP_SECRET_KEY);
        //用户的userid。 此处应该实时获取当前登录人的用户信息
        saveBillJson.put("userId", billInfo.getString("userId"));
        saveBillJson.put("language", "zh_CN");
        //单据formid
        saveBillJson.put("formUuid", YiDaConfig.ORDERBILL_FORM_ID);
        JSONObject formDataJson = new JSONObject();
        saveBillJson.put("formDataJson", formDataJson.toJSONString());
        // region  开始构建表单保存参数
        //客户订单号
        formDataJson.put("textField_kwemovzr", billInfo.getString("serialNum"));
        //选择客户
        String selectUser = billInfo.getString("selectUser");
        Assert.notBlank(selectUser, () -> new CustomException("请选择客户信息!"));
        formDataJson.put("selectField_ksr76rov", selectUser);
        //交货日期
        Date handInDate = billInfo.getDate("handInDate");
        Assert.notNull(handInDate, () -> new CustomException("请选择交货日期!"));
        formDataJson.put("column_kwemow01", handInDate);
        //备注
        String note = billInfo.getString("note");
        formDataJson.put("textareaField_ksr76rpi", note);
        //是否预付订单
        Integer prepaidOrder = billInfo.getInteger("prepaidOrder");
        String prepaidOrderSelected = Objects.isNull(prepaidOrder) || prepaidOrder.equals(0) ? "否" : "是";
        formDataJson.put("radioField_kwemow0p", prepaidOrderSelected);
        //定金
        Double deposit = billInfo.getDouble("deposit");
        if (Objects.isNull(deposit)) {
            deposit = (double) 0;
        }
        formDataJson.put("numberField_ksr76rpg", deposit);
        //订单总额
        Double depositMoney = billInfo.getDouble("depositMoney");
        if (Objects.isNull(depositMoney)) {
            depositMoney = (double) 0;
        }
        formDataJson.put("numberField_ksr76rpe", depositMoney);
        JSONArray goodsList = billInfo.getJSONArray("goodlist");
        if (Objects.isNull(goodsList) || goodsList.isEmpty()) {
            throw new CustomException("请选择商品信息");
        }
        List<Map<String, Object>> shoppingList = new ArrayList<>();
        for (int i = 0; i < goodsList.size(); i++) {
            JSONObject goodsInfo = goodsList.getJSONObject(i);
            if (Objects.isNull(goodsInfo)) {
                continue;
            }
            Map<String, Object> goodsMap = new HashMap<>(6);
            shoppingList.add(goodsMap);
            //商品名称
            String goodsName = goodsInfo.getString("a");
            goodsMap.put("selectField_kwfy2eyc", goodsName);
            goodsMap.put("textField_kwemow0u", goodsName);
            //商品类别
            String goodType = goodsInfo.getString("b");
            goodsMap.put("textField_ksr76rp0", goodType);
            //商品编码
            String goodNumber = goodsInfo.getString("c");
            goodsMap.put("textField_ksr76roz", goodNumber);
            //规格
            String specification = goodsInfo.getString("d");
            goodsMap.put("textField_ksr76rp2", specification);
            //单位
            String units = goodsInfo.getString("e");
            goodsMap.put("textField_ksr76rp1", units);
            //单价
            String unitPrice = goodsInfo.getString("f");
            goodsMap.put("numberField_ksr76rp4", unitPrice);
            //数量
            String numbers = goodsInfo.getString("g");
            goodsMap.put("numberField_ksr76rp3", numbers);
            //合计数
            String allPrice = goodsInfo.getString("h");
            goodsMap.put("numberField_ksr76rp5", allPrice);

        }
        formDataJson.put("tableField_ksr76rox", shoppingList);
        //endregion
        String resString = DingTalkUtil.saveYidaFormData(saveBillJson);
        log.info("保存单据返回响应的结果：{}", resString);
        if (JSONUtil.isJson(resString)) {
            JSONObject responseJson = JSONObject.parseObject(resString);
            return responseJson.getString("result");
        } else {
            throw new CustomException("保存单据失败!");
        }
    }
}
