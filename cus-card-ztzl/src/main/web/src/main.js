/*
 * @Author: 杨芯悦 xyyang01@wisedu.com
 * @Date: 2023-07-25 18:44:11
 * @LastEditors: 杨芯悦 xyyang01@wisedu.com
 * @LastEditTime: 2023-07-25 18:49:23
 */
import { mount } from './utils'
import App from './cusApp.vue'

export function bootstrap(container, options) {
  return mount(App, container, options)
}

if (process.env.NODE_ENV === 'development' && process.env.RUN_MODE !== 'shell') {
  mount(App, '#app')
}
