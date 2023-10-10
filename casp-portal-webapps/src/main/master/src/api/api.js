/* eslint-disable */
/**
 * 本文件由工具自动生成，请勿随意改动！！！
 * @name CASP-PORTAL——基础数据API说明文档
 * @description CASP-PORTAL——基础数据API说明文档
 * @tutorial /Users/zc/Documents/casp/minos-fusion-console-web/casp-portal-web/api/casp-api.yaml
 */
const DEV_MODE = process.env.NODE_ENV === 'development'

const API_BASE = DEV_MODE ? '' : ''

const _basePath = (url) => {
    return `${API_BASE}${url.replace(/\{.+\}/,'')}`
}

export default {
    'getPageInfo': _basePath('/getPageInfo'),
    'getPageView': _basePath('/getPageView'),
    'execCardMethod': _basePath('/execCardMethod'),
    'execTemplateMethod': _basePath('/execTemplateMethod'),
    'login': _basePath('/getLoginHref'),
    'logout': _basePath('logout'),
    'queryServiceByWid': _basePath('/queryServiceByWid'),
    'serviceShow': _basePath('/serviceShow'),
    'queryI18nList': _basePath('/queryI18nList'),
    'collectService': _basePath('/collectService'),
    'collectServiceItem': _basePath('/collectServiceItem'),
    'getPlaceholderVal': _basePath('/getPlaceholderVal'),
    'getProgrammeLocalStyle': _basePath('/common/getProgrammeLocalStyle'),
    'getSearchHisVal': _basePath('/getSearchHisVal'),
    'getMessageCount': _basePath('/getMessageCount'),
    'queryPopupWindowDisplay': process.env.NODE_ENV === 'development' ? _basePath('/manager/programme/queryPopupWindowDisplay') : _basePath('/programme/queryPopupWindowDisplay'),
    'updatePopupWindowStatus': process.env.NODE_ENV === 'development' ? _basePath('/manager/programme/updatePopupWindowStatus') : _basePath('/programme/updatePopupWindowStatus'),
    'getScene': _basePath('/getScene'),
    'getStaticData': _basePath('/language/getStaticData'),
    'switchUserLanguage': _basePath('/portal/switchUserLanguage'),
    'getPortalConfig': _basePath('/base/getPortalConfig'),
    'getLoginUser': _basePath('/getLoginUser'),
    'getSidebarCount': _basePath('/getSidebarCount')
}
