/* eslint-disable */
/**
 * 本文件由工具自动生成，请勿随意改动！！！
 * @name CASP-PORTAL——基础数据API说明文档
 * @description CASP-PORTAL——基础数据API说明文档
 * @tutorial /Users/zc/Documents/casp/minos-fusion-console-web/casp-portal-web/api/casp-api.yaml
 */
const DEV_MODE = process.env.NODE_ENV === 'development'

const API_BASE = DEV_MODE ? '/manager' : ''

const _basePath = (url) => {
    return `${API_BASE}${url.replace(/\{.+\}/,'')}`
}

export default {
    'getPageInfo': _basePath('/getPageInfo'),
    'getPageView': _basePath('/getPageView'),
    'execCardMethod': _basePath('/execCardMethod'),
    'execTemplateMethod': _basePath('/execTemplateMethod'),
    'login': _basePath('/getLoginHref'),
    'logout': _basePath('/logout'),
    'getLoginOrLogoutUrl': _basePath('/getLoginOrLogoutUrl'),
    'queryServiceByWid': _basePath('/queryServiceByWid'),
    'serviceShow': _basePath('/serviceShow'),
    'queryI18nList': _basePath('/queryI18nList'),
    'collectService': _basePath('/collectService'),
    'collectServiceItem': _basePath('/collectServiceItem'),
    'getPlaceholderVal': _basePath('/getPlaceholderVal'),
    'getProgrammeLocalStyle': _basePath('/common/getProgrammeLocalStyle'),
    'getSearchHisVal': _basePath('/getSearchHisVal'),
    'getMessageCount': _basePath('/getMessageCount'),
    'queryPopupWindowDisplay': _basePath('/programme/queryPopupWindowDisplay'),
    'updatePopupWindowStatus': _basePath('/programme/updatePopupWindowStatus'),
    'getScene': _basePath('/getScene'),
    'getStaticData': _basePath('/language/getStaticData'),
    'switchUserLanguage': _basePath('/portal/switchUserLanguage'),
    'getPortalConfig': _basePath('/base/getPortalConfig'),
    'getLoginUser': _basePath('/getLoginUser'),
    'getSidebarCount': _basePath('/getSidebarCount'),
    'getUserPermissionRouters': _basePath('/getUserPermissionRouters'),
    'getThemeData': _basePath('/base/getThemeData'),
    'getShareSchedule': _basePath('/schedule/getShareEvent'),
    'handleShareSchedule': _basePath('/schedule/handlerShareEvent'),
    'appraiseSummary': _basePath('/appAppraise/appraiseSummary'),
    'queryAppraiseByPage': _basePath('/appAppraise/queryAppraiseByPage'),
    'saveAppraise': _basePath('/appAppraise/saveAppraise'),
    'saveSiteitching': _basePath('/saveSiteitching'),
    'queryUserSiteWitching': _basePath('/queryUserSiteWitching'),
    'getServiceHealth': _basePath('/service/getHealthInfo'),
    'downloadFileByUrl': _basePath('/fileTodo/downloadFileByUrl'),
    'queryAppraiseByPageNew': _basePath('/appAppraise/queryAppraiseByPageNew'),
    'saveAppraiseWithPic': _basePath('/appAppraise/saveAppraiseWithPic'),
    'saveAppraiseWithPicByItem': _basePath('/itemAppraise/saveAppraiseWithPic'),
    'saveAppraiseWithPicOfOneThing': _basePath('/oneThingAppraise/saveAppraiseWithPic'),
    'submitFeedbackWithPic': _basePath('/feedback/submitFeedbackWithPic'),
    'saveAppraiseWithPicBase64': _basePath('/appAppraise/saveAppraiseWithPicBase64'),
    'saveAppraiseWithPicByItemBase64': _basePath('/itemAppraise/saveAppraiseWithPicBase64'),
    'saveAppraiseWithPicOfOneThingBase64': _basePath('/oneThingAppraise/saveAppraiseWithPicBase64'),
    'submitFeedbackWithPicBase64': _basePath('/feedback/submitFeedbackWithPicBase64'),
    'queryOrgUsers': _basePath('/cal/queryOrgUsers'),
    'searchUserByKeywordAndOrgwid': _basePath('/cal/searchUserByKeywordAndOrgwid'),
    'collectOneThing': _basePath('/collectOneThing'),
    'collectNews': _basePath('/collectNews'),
    'collectTask': _basePath('/collectTask'),
    'collectMessage': _basePath('/collectMessage'),
}
