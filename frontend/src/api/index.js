import axiosService from '../utils/request'

export const getCorpId = data => {
    return axiosService({
        url: "/login/getCorpId/" + data.code,
        method: "get",
    });
}

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

// 新增 订单
export const addOrder = data => {
    return axiosService({
        url: "/biz/saveSaleBillInfo",
        method: "post",
        data,
    });
}

// 删除 订单
export const deleteOrder = data => {
    return axiosService({
        url: "/biz/delete",
        method: "delete",
        params: data,
    });
}