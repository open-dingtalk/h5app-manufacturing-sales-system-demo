import * as dd from 'dingtalk-jsapi';

export function getCode(callback, corpId) {
    if (dd.env.platform !== "notInDingTalk") {
        dd.ready(() => {
            //使用SDK 获取免登授权码
            dd.runtime.permission.requestAuthCode({
                corpId: corpId,
                onSuccess: (info) => {
                    // 根据钉钉提供的api 获得code后,再次调用这个callback方法
                    // 由于是钉钉获取code是异步操作,不知道什么时候执行完毕
                    // callback 函数会等他执行完毕后在自己调用自己
                    callback(info.code)
                },
                onFail: (err) => {
                    alert('fail');
                    alert(JSON.stringify(err));
                },
            });
        });
    }
}