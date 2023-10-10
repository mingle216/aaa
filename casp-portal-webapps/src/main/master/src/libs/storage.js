// 设置localStorage
export const setStorage = function(key, obj) {
  const json = JSON.stringify(obj)
  window.localStorage.setItem(key, json)
}

// 获取localStorage
export const getStorage = function(key) {
  const str = window.localStorage.getItem(key)
  if (!str) {
    return null
  }
  return JSON.parse(str)
}

// 移除localStorage
export const removeStorage = function(key) {
  window.localStorage.removeItem(key)
}
