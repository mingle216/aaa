import { setStorage } from '@libs/storage'

export const awaitWrap = (promise) =>
  promise
    .then((res) => {
      // 登陆请求成功,需要存储authorization,为了登陆成功之后，所有的请求均需要使用
      const auth = res.headers.authorization
      auth ? setStorage('authorization', auth) : ''
      if (res.status === 200 && res.data && res.data.errcode === '0') {
        return [res.data, null]
      }
      return [
        res.data
          ? res.data.errcode === '999' && !res.data.errmsg
            ? { errmsg: '系统错误' }
            : res.data
          : { errmsg: '请求超时 or 联系管理员' },
        null
      ]
    })
    .catch((error) => {
      return Promise.reject(error)
    })

/**
 * 校验对象类型
 * @param {*} obj
 */
export function typeOf(obj) {
  const toString = Object.prototype.toString
  const map = {
    '[object Boolean]': 'boolean',
    '[object Number]': 'number',
    '[object String]': 'string',
    '[object Function]': 'function',
    '[object Array]': 'array',
    '[object Date]': 'date',
    '[object RegExp]': 'regExp',
    '[object Undefined]': 'undefined',
    '[object Null]': 'null',
    '[object Object]': 'object'
  }
  return map[toString.call(obj)]
}

/**
 *
 * @param {*} arr
 * @param {*} size
 */
export const chunk = (arr, size) => {
  const arr2 = []
  for (let i = 0; i < arr.length; i = i + size) {
    arr2.push(arr.slice(i, i + size))
  }
  return arr2
}

/**
 * 生成入学日历
 */
export const yearChunk = () => {
  const arr2 = []
  var nowTime = new Date()
  const nowYear = nowTime.getFullYear()
  for (let i = parseInt(nowYear) + 1; i > 1948; i--) {
    arr2.push({
      label: `${i}`,
      value: `${i}`
    })
  }
  return arr2
}

/**
 * @param {Array} arr1
 * @param {Array} arr2
 * @description 得到两个数组的交集, 两个数组的元素为数值或字符串
 */
export const getIntersection = (arr1, arr2) => {
  const s = new Set(arr2)
  return arr1.filter((x) => s.has(x))
}

/**
 * @param {*} obj1 对象
 * @param {*} obj2 对象
 * @description 判断两个对象是否相等，这两个对象的值只能是数字或字符串
 */
export const objEqual = (obj1, obj2) => {
  const keysArr1 = Object.keys(obj1)
  const keysArr2 = Object.keys(obj2)
  if (keysArr1.length !== keysArr2.length) return false
  else if (keysArr1.length === 0 && keysArr2.length === 0) return true
  else return !keysArr1.some((key) => obj1[key] != obj2[key])
}

/**
 * 截取千位
 */
export const miliFormat = (num) => {
  return (
    num &&
    num.toString().replace(/^\d+/, (m) => m.replace(/(?=(?!^)(\d{3})+$)/g, ','))
  )
}

// 生成随机字符串
export function random(len = 32) {
  const $chars =
    'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890'
  const maxPos = $chars.length
  let str = ''
  for (let i = 0; i < len; i++) {
    str += $chars.charAt(Math.floor(Math.random() * maxPos))
  }
  return str
}

/**
 * @returns {String} 当前浏览器名称
 */
export const getExplorer = () => {
  const ua = window.navigator.userAgent
  const isExplorer = (exp) => {
    return ua.indexOf(exp) > -1
  }
  if (isExplorer('MSIE')) return 'IE'
  else if (isExplorer('Firefox')) return 'Firefox'
  else if (isExplorer('Chrome')) return 'Chrome'
  else if (isExplorer('Opera')) return 'Opera'
  else if (isExplorer('Safari')) return 'Safari'
}

/**
 * 数字转千分位，超过10万展示万，保留一位小数
 */
export const formatNumber = num => {
  if (typeof(num) !== 'number') {
    num = Number(num)
  }
  if (isNaN(num)) {
    return 0
  }
  if (num > 100000) {
    return `${(num / 10000).toFixed(1)}万`
  }
  return num.toLocaleString()
}

/**
 * 插入css
 */
export const addCSS = (document, cssText) => {
  let style = document.createElement('style'), //创建一个style元素
  head = document.head || document.getElementsByTagName('head')[0] //获取head元素
  style.type = 'text/css'
  if (style.styleSheet) {
    //IE
    const func = function () {
      try {
        //防止IE中stylesheet数量超过限制而发生错误
        style.styleSheet.cssText = cssText;
      } catch (e) {
        console.log(e)
      }
    };
    //如果当前styleSheet还不能用，则放到异步中则行
    if (style.styleSheet.disabled) {
      setTimeout(func, 10)
    } else {
      func()
    }
  } else {
    const textNode = document.createTextNode(cssText)
    style.appendChild(textNode)
  }
  head.appendChild(style)
}

 /**
 * @description: 加载埋点url
 */
export const loadAsyncJs = (src) => {
  const srcUrl = src
  const scripts = document.getElementsByTagName('script')
  for (const item of scripts) {
    if (item.src === srcUrl) {
        return
    }
  }
  const scriptUrl = document.createElement('script')
  scriptUrl.type = 'text/javascript'
  scriptUrl.defer = true
  scriptUrl.src = srcUrl
  const sUrl = scripts[0]
  sUrl.parentNode.insertBefore(scriptUrl, sUrl);
}

export const addCssByLink = (document, url) => {
  var link = document.createElement("link");
  link.setAttribute("rel", "stylesheet");
  link.setAttribute("type", "text/css");
  link.setAttribute("href", url);

  var heads = document.getElementsByTagName("head");
  if (heads.length) {
    heads[0].appendChild(link);
  } else {
    document.documentElement.appendChild(link);
  }
};