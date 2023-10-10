import qs from 'qs'

const headers = {
  'Content-Type': 'application/json',
  'X-Requested-With': 'XMLHttpRequest'
}
const axiosConfig = {
  baseURL: '',
  headers: headers,
  // 请求后的数据处理
  transformResponse: [
    function(data) {
      try{
        const response = typeof data === 'object' ? data : JSON.parse(data)
        return response
      }catch(err) {
        return null
      }
    }
  ],
  transformRequest: [
    function(data = {}) {
      data.n = encodeURIComponent(Math.random()) // 解決ie緩存問題
      return JSON.stringify(data)
    }
  ],
  //   查询对象序列化函数
  paramsSerializer: function(params) {
    return qs.stringify(params)
  },
  // 超时设置s
  timeout: 60000,
  // 跨域是否带Token
  withCredentials: true,
  responseType: 'json',
  // xsrf 设置
  xsrfCookieName: 'XSRF-TOKEN',
  xsrfHeaderName: 'X-XSRF-TOKEN',
  // 最多转发数，用于node.js
  maxRedirects: 5,
  // 最大响应数据大小
  maxContentLength: 2000,
  // 自定义错误状态码范围
  validateStatus: function(status) {
    return status >= 200
  }
  // 用于node.js
  //   httpAgent: new http.Agent({ keepAlive: true }),
  //   httpsAgent: new https.Agent({ keepAlive: true })
}
export default axiosConfig
