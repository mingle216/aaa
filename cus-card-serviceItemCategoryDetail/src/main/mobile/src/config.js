import { mount } from './utils'
import Config from './Config.vue'

export function bootstrap(container, options) {
  return mount(Config, container, options)
}

if (process.env.NODE_ENV === 'development') {
  mount(Config, '#app')
}
