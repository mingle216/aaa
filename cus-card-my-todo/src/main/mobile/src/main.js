import Vue from 'vue'
import App from './App.vue'

export function mount(component, container, options) {
  // eslint-disable-next-line no-debugger
  return new Vue({
    i18n: options.props.i18n,
    render: h => h(component, options)
  }).$mount(container)
}

export function bootstrap(container, options) {
  return mount(App, container, options)
}

