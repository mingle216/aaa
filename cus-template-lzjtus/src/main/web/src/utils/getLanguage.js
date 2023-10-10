import Vue from 'vue'
/**
 * 
 * @param {卡片名称} functionName 
 * @param {字段键} dataKey 
 * @param {默认值} defualtValue 
 * @param {变量} obj 
 * @returns 多语言值
 */ 
const GetLanguageValue=function (functionName, dataKey, defualtValue, obj) {  
  return window.shell.getLanguageValue(functionName, dataKey, defualtValue, obj)
}
 Vue.prototype.GetLanguageValue = GetLanguageValue
 Vue.prototype.$Lan = GetLanguageValue