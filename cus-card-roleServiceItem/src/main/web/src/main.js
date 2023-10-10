import { mount } from './utils'
import './filters'
import App from './cusApp.vue'
import Vue from 'vue'
import translate from './mixins'

Vue.mixin(translate);

export function bootstrap(container, options) {
  return mount(App, container, options)
}

if (process.env.NODE_ENV === 'development' && process.env.RUN_MODE !== 'shell') {
  mount(App, '#app')
}
