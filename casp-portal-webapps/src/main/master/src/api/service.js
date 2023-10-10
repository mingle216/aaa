/* eslint-disable */
/**
 * 本文件由工具自动生成，请勿随意改动！！！
 * @name CASP-PORTAL——基础数据API说明文档
 * @description CASP-PORTAL——基础数据API说明文档
 * @tutorial /Users/zc/Documents/casp/minos-fusion-console-web/casp-portal-web/api/casp-api.yaml
 */
import api from '@api/api'
import http from '@libs/ajax'
import { awaitWrap } from "@tools";

/**
 * 替换url参数
 * @param url
 * @param params
 * @returns {*}
 */
const replaceParams = (url, params) => {
  for(var key in params){
    url = url.replace('{'+key+'}', params[key+''])
  }
  return url
}

/**
 * &lt;b&gt;使用场景：&lt;/br&gt;&lt;/b&gt;在卡片/模板需要到配置页面时，通过此接口获取配置页面html渲染字符串 提供给前端渲染   &lt;/br&gt;
 * @param { Array } params 请求参数
 */
export const  getPageInfo =async (params, config) => {
    // send request
    return awaitWrap(http.get(api['getPageInfo'], params, config))
}

export const  getPageView =async (params, config) => {
  // send request
  return awaitWrap(http.get(`${api['getPageView']}`, params, config))
}

export const execCardMethod = async (params, config) => {
  return awaitWrap(http.post(`${api['execCardMethod']}/${params.cardWid}/${params.cardId}`, params, config))
}

export const execTemplateMethod = async (params, config) => {
  return awaitWrap(http.post(api['execTemplateMethod'], params, config))
}

export const login = async (params, config) => {
  return awaitWrap(http.get(`${api['login']}`, params, config))
}

export const logout = async (params, config) => {
  return awaitWrap(http.get(`${api['logout']}`, params, config))
}

export const queryServiceByWid = async (params, config) => {
  return awaitWrap(http.get(`${api['queryServiceByWid']}/${params.serviceId}/${params.filterKey}`, params, config))
}

export const serviceShow = async (params, config) => {
  return awaitWrap(http.get(`${api['serviceShow']}`, params, config))
}

// 多语言
export const queryI18nList = async (params, config) => {
  return awaitWrap(http.get(`${api['queryI18nList']}`, params, config))
}

// 收藏服务
export const collectService = async (params, config) => {
  return awaitWrap(http.get(`${api['collectService']}`, params, config))
}

// 收藏服务事项
export const collectServiceItem = async (params, config) => {
  return awaitWrap(http.get(`${api['collectServiceItem']}`, params, config))
}

// 获取全局搜索placeholder
export const getPlaceholderVal = async (params, config) => {
  const lang = sessionStorage.getItem('locale') || "zh_CN";
  return awaitWrap(http.get(`${api['getPlaceholderVal']}/${params.wid}/${lang}`, null, config))
}

// 获取东大消息中心数量
export const getMessageCount = async (params, config) => {
  return awaitWrap(http.get(`${api['getMessageCount']}`, params, config))
}

// 获取本地化样式
export const getProgrammeLocalStyle = async (params, config) => {
  return awaitWrap(http.post(`${api['getProgrammeLocalStyle']}`, params, config))
}

// 获取搜索历史
export const getSearchHisVal = async (params, config) => {
  return awaitWrap(http.get(`${api['getSearchHisVal']}/${params.wid}`, null, config))
}

// 门户前端查询弹窗
export const queryPopupWindowDisplay = async (params, config) => {
  return awaitWrap(http.post(`${api['queryPopupWindowDisplay']}`, params, config))
}

// 设置弹窗不再显示
export const updatePopupWindowStatus = async (params, config) => {
  return awaitWrap(http.post(`${api['updatePopupWindowStatus']}`, params, config))
}

// 获取任务中心业务场景的数据
export const getScene = async (params, config) => {
  return awaitWrap(http.get(`${api['getScene']}/${params.type}`, null, config))
}

// 获取多语言静态资源
export const getStaticData = async (params, config) => {
  return awaitWrap(http.post(`${api['getStaticData']}`, params, config))
}

// 获取多语言静态资源
export const switchUserLanguage = async (params, config) => {
  return awaitWrap(http.get(`${api['switchUserLanguage']}`, params, config))
}

// 获取门户配置
export const getPortalConfig = async (params, config) => {
  return awaitWrap(http.post(`${api['getPortalConfig']}`, params, config))
}

// 获取登录用户信息
export const getLoginUser = async (params, config) => {
  return awaitWrap(http.get(`${api['getLoginUser']}`, params, config))
}

// 获取侧边栏消息数
export const getSidebarCount = async (params, config) => {
  return awaitWrap(http.get(`${api['getSidebarCount']}`, params, config))
}