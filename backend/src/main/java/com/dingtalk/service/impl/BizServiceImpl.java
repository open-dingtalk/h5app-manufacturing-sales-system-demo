package com.dingtalk.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingboot.common.token.ITokenManager;
import com.aliyun.dingtalkyida_1_0.models.DeleteFormDataHeaders;
import com.aliyun.dingtalkyida_1_0.models.DeleteFormDataRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.util.DingTalkClientUtil;
import com.dingtalk.util.DingTalkUtil;
import com.dingtalk.config.YiDaConfig;
import com.dingtalk.core.exception.CustomException;
import com.dingtalk.service.BizService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 业务逻辑层
 *
 * @author xueyu
 */
@Slf4j
@Service
public class BizServiceImpl implements BizService {

    @Autowired
    private ITokenManager tokenManager;

    /**
     * 获取accesstoken
     *
     * @return obj
     */
    public String getAccessToken() {
        try {
            return tokenManager.getAccessToken(YiDaConfig.DD_APP_KEY, YiDaConfig.DD_APP_SECRET);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取订货单列表
     *
     * @return obj
     */
    @Override
    public List getOrderList(String userId) {
        JSONObject customerQueryJson = new JSONObject();
        customerQueryJson.put("appType", YiDaConfig.APP_CODE);
        customerQueryJson.put("systemToken", YiDaConfig.APP_SECRET_KEY);
        customerQueryJson.put("userId", userId);
        customerQueryJson.put("language", "zh_CN");
        //表单id
        customerQueryJson.put("formUuid", YiDaConfig.ORDER_FORM_ID);

        //分页参数 当前页数
        customerQueryJson.put("currentPage", 1);
        //每页显示数量
        customerQueryJson.put("pageSize", 10);

        String resString = DingTalkUtil.getYiDaInstances(customerQueryJson, getAccessToken());
        if (JSONUtil.isJson(resString)) {
            JSONObject responseJson = JSONObject.parseObject(resString);
            log.info("当前页：{}", responseJson.getString("currentPage"));
            log.info("总条数：{}", responseJson.getString("totalCount"));
            return orderFormToData(responseJson.getJSONArray("data"));
        }
        return Collections.emptyList();
    }

    private List<Map<String, String>> orderFormToData(JSONArray jsonArray){
        List<Map<String, String>> list = new ArrayList<>();
        for (Object obj : jsonArray) {
            Map<String, String> data = new HashMap<>();
            JSONObject jsonObject = JSONObject.parseObject(obj.toString());
            JSONObject formData = jsonObject.getJSONObject("formData");
            data.put("depositMoney", formData.getString("numberField_ksr76rpe"));
            data.put("deposit", formData.getString("numberField_ksr76rpg"));
            data.put("clientOrder", formData.getString("textField_kwemovzr"));
            data.put("prepaidOrder", formData.getString("radioField_kwemow0p"));
            data.put("handInDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(formData.getLong("dateField_kwemow04"))));
            data.put("note", formData.getString("textareaField_ksr76rpi"));
            data.put("clientEntire", formData.getString("selectField_ksr76rov"));
            data.put("selectUser", formData.getString("selectField_ksr76rov"));
            data.put("instanceTitle", jsonObject.getString("formInstanceId"));
            list.add(data);
        }
        System.out.println("送货单列表： " + JSON.toJSONString(list));
        return list;
    }

    /**
     * 新增订货单订单
     *
     * @param billInfo 订货单信息
     * @return obj
     */
    @Override
    public Object saveSaleBillInfo(JSONObject billInfo) throws ParseException {
        JSONObject saveBillJson = new JSONObject();
        saveBillJson.put("appType", YiDaConfig.APP_CODE);
        saveBillJson.put("systemToken", YiDaConfig.APP_SECRET_KEY);
        //用户的userid。 此处应该实时获取当前登录人的用户信息
        saveBillJson.put("userId", billInfo.getString("userId"));
        saveBillJson.put("language", "zh_CN");
        //单据formid
        saveBillJson.put("formUuid", YiDaConfig.ORDER_FORM_ID);
        JSONObject formDataJson = new JSONObject();

        // region  开始构建表单保存参数
        //客户订单号
        formDataJson.put("textField_kwemovzr", billInfo.getString("serialNum"));
        //选择客户
        String selectUser = billInfo.getString("selectUser");
        Assert.notBlank(selectUser, () -> new CustomException("请选择客户信息!"));
        formDataJson.put("selectField_ksr76rov", selectUser);

        //交货日期
        String handInDateStr = billInfo.getString("handInDate");
        Assert.notNull(handInDateStr, () -> new CustomException("请选择交货日期!"));
        formDataJson.put("dateField_kwemow04", handInDateStr);
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
        if (goodsList != null) {
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
        }
        saveBillJson.put("formDataJson", formDataJson.toJSONString());
        //endregion
        String resString = DingTalkUtil.saveYiDaFormData(saveBillJson, getAccessToken());
        log.info("保存单据返回响应的结果：{}", resString);
        if (JSONUtil.isJson(resString)) {
            JSONObject responseJson = JSONObject.parseObject(resString);
            return responseJson.getString("result");
        } else {
            throw new CustomException("保存单据失败!");
        }
    }

    /**
     * 删除销售订单
     *
     * @param id
     * @return
     */
    @Override
    public Object deleteSaleBillInfoById(String id, String userId) throws Exception {
        com.aliyun.dingtalkyida_1_0.Client client = DingTalkClientUtil.createClient();
        DeleteFormDataHeaders deleteFormDataHeaders = new DeleteFormDataHeaders();
        deleteFormDataHeaders.xAcsDingtalkAccessToken = getAccessToken();
        DeleteFormDataRequest deleteFormDataRequest = new DeleteFormDataRequest()
                .setAppType(YiDaConfig.APP_CODE)
                .setSystemToken(YiDaConfig.APP_SECRET_KEY)
                .setUserId(userId)
                .setLanguage("zh_CN")
                .setFormInstanceId(id);
        try {
            client.deleteFormDataWithOptions(deleteFormDataRequest, deleteFormDataHeaders, new RuntimeOptions());
        } catch (Exception err) {
            TeaException error = new TeaException(err.getMessage(), err);
            if (!com.aliyun.teautil.Common.empty(error.code) && !com.aliyun.teautil.Common.empty(error.message)) {
                throw new CustomException("参数错误，删除失败");
            }
        }
        return "删除成功";
    }

}
