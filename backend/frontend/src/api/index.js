import axiosService from '../utils/request'

export const getDingTalkUserInfo = data => {
    return axiosService({
        url: "/login/dingtalk/" + data.code,
        method: "get",
    });
}
// 订单
export const getOrderList = data => {
    return axiosService({
        url: "/biz/getOrderList",
        method: "get",
        params: data
    });
}

// 客户
export const getClientList = data => {
    return axiosService({
        url: "/biz/getCustomerList",
        method: "get",
        params: data,
    });
}

// 商品
export const getGoodsList = data => {
    return axiosService({
        url: "/biz/getCommodityList",
        method: "get",
        params: data,
    });
}

// 新增 订单
export const addOrder = data => {
    return axiosService({
        url: "/biz/saveSaleBillInfo",
        method: "post",
        params: data,
    });
}