import { mount } from './utils'
import directive from './utils/directive'
import App from './App.vue'
import Vue from 'vue'

Vue.use(directive);
export function bootstrap(container, options) {
  return mount(App, container, options)
}

if (process.env.NODE_ENV === 'development' && process.env.RUN_MODE !== 'shell') {
  mount(App, '#app')
}
