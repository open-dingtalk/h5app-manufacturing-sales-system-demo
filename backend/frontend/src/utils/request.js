import axios from "axios";
import { Message } from "element-ui";

let baseURL = "";

// 创建一个axios实例
const axiosService = axios.create({
    baseURL,
    timeout: 5000, // 设置超时时间为5s
});

// request拦截器 ==> 对请求参数进行处理
axiosService.interceptors.request.use(
    (config) => {
        let dindinToken = localStorage.getItem("dindinToken");
        if (dindinToken) {
            config.headers.token = dindinToken;
        }
        return config;
    },
    (error) => {
        // 处理请求错误
        Promise.reject(error);
    }
);

// respone拦截器 ==> 对响应做处理
// let that = this;

axiosService.interceptors.response.use(
    (response) => {
        // console.log("response", response)
        const res = response.data;
        if (res.code == 200) {
            return response
        } else {
            Message.error('请求错误')
        }
    },
    (error) => {
        console.log("【ERROR】", JSON.stringify(error));
        // 判断error的status代码，并将对应的信息告知用户
        var errorText = "timeout of 5000ms exceeded";
        if (error.message == errorText) {
            Message({
                showClose: true,
                message: "请求超时",
                type: "error",
            });
        }
        let text = "";
        let err = JSON.parse(JSON.stringify(error));
        if (err.response?.status) {
            switch (error.response.status) {
                case 400:
                    text = "请求错误(400)，请重新申请";
                    break;
                case 401:
                    text = "登录错误(401)，请重新登录";
                    return this.$router.replace("/login");
                case 403:
                    text = "拒绝访问(403)";
                    break;
                case 404:
                    text = "请求出错(404)";
                    break;
                case 408:
                    text = "请求超时(408)";
                    break;
                case 500:
                    text = "服务器错误(500)，请重启软件或切换功能页！";
                    break;
                case 501:
                    text = "服务未实现(501)";
                    break;
                case 502:
                    text = "网络错误(502)";
                    break;
                case 503:
                    text = "服务不可用(503)";
                    break;
                case 504:
                    text = "网络超时(504)";
                    break;
                case 505:
                    text = "HTTP版本不受支持(505)";
                    break;
                case 702:
                    text = "Session过期,请重新登录s";
                    return this.$router.replace("/login");
                default:
                    text = "网络连接出错";
            }
        } else {
            text = "连接服务器失败,请退出重试!";
        }
        Message({
            showClose: true,
            message: text,
            type: "error",
        });
        return Promise.reject(error);
    }
);

// 将写好的axios实例暴露出去
export default axiosService;
