import { getPageView } from '../api/service'
import { _sessionStorage } from './storage';
export const getPageDatas = async(pageCode) => {
  const pageData = _sessionStorage.get('pageData')
  let defaultLanKey = navigator.language.replace("-", "_");
  defaultLanKey = defaultLanKey.includes("zh") ? "zh_CN" : defaultLanKey; 
  let obj = pageData || {};
  if (Reflect.has(obj, pageCode || 'default')) {
    return obj[pageCode || 'default']
  } else {
    const [res] = await getPageView({
      params: {
        pageCode,
        originalUrl: encodeURIComponent(window.location.href),
        lang: defaultLanKey
      }
    })
    if (res.errcode === '0') {
      if (!Reflect.has(obj, pageCode || 'default')) {
        obj[pageCode || 'default'] = res.data || {}
      }
      _sessionStorage.set('pageData', obj, 1000*60*30)
    }
    return res.data || {}
  }
}



