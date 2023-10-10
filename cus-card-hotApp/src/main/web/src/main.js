import { mount } from './utils'
import App from './App.vue'

export function bootstrap(container, options) {
  return mount(App, container, options)
}

if (process.env.NODE_ENV === 'development' && process.env.RUN_MODE !== 'shell') {
  mount(App, '#app')
}

// mount(App, '#app')
