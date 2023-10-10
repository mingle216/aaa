import axios from "axios";
import config from "./config";
import globalObj from '../../utils/global'
// 取消重复请求
const cancelToken = axios.CancelToken;

const pendingList = [];
function equal(config) {
  return `${config.url}&request_type=${config.method}
  &params=${JSON.stringify(config.data)}&query=${JSON.stringify(
    config.params
  )}`;
}

const removePending = (config) => {
  for (const p in pendingList) {
    const item = p;
    const pendingObj = pendingList[p];
    // 当前请求在数组中存在时执行函数体
    if (pendingObj.url === equal(config)) {
      // 执行取消操作
      pendingObj.cancel("canceled");
      // 从数组中移除记录
      pendingList.splice(item, 1);
    }
  }
};
const service = axios.create(config);

// 添加请求拦截器
service.interceptors.request.use(
  (config) => {
    removePending(config);
    config.cancelToken = new cancelToken((c) => {
      pendingList.push({
        url: equal(config),
        cancel: c,
      });
    });
    if (config.method == 'get') {
      config.params = {
        _t: encodeURIComponent(Math.random()),
        ...config.params
      }
    }
    return config;
  },
  (error) => () => {
    Promise.reject(error);
  }
);

// 返回状态判断(添加响应拦截器)
service.interceptors.response.use((res) => {
  const code = res.status;
  if(res.data && res.data.errcode === '999'){
    if (res.data.errmsg === 'notLogin') {
      globalObj.showMessage({
        type: "warning",
        message: globalObj.getLanguageValue('public', 'notLogin', '当前用户未登录')
      })
      globalObj.emit('update-login', null)
    } else {
      globalObj.showMessage({
        type: "warning",
        message: globalObj.getLanguageValue('public', res.data.errmsg, res.data.errmsg || '系统异常')
      })
    }
    return Promise.reject(res);
  }
  if ((code >= 200 && code < 300) || code === 304) {
    if(res.headers.redirect === "REDIRECT"){
      location.href = res.headers.path;
      return
    }
    removePending(res.config);
    return Promise.resolve(res);
  } else {
    return Promise.reject(res);
  }
});

export default service;
