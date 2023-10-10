import Vue from 'vue'

export function mount(component, container, options) {
  // eslint-disable-next-line no-debugger
  return new Vue({
    render: h => h(component, options)
  }).$mount(container)
}
