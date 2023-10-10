import Vue from 'vue'

export function mount(component, container, options) {
  // eslint-disable-next-line no-debugger
  // console.log(component, container,  options)
  return new Vue({
    i18n: options.props.i18n,
    render: h => h(component, options)
  }).$mount(container)
}
