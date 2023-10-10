import { mount } from './utils'
import App from './App.vue'
import AppMature from './AppMature.vue'

const compMap = {
  NORMAL: App,
  SYS_TEMPLATE_MATURE: AppMature,
};

export function bootstrap(container, options) {
  const templateStyle = options.props.router?.templateStyle;
  if (templateStyle && templateStyle in compMap) {
    return mount(compMap[templateStyle], container, options);
  }
  return mount(App, container, options);
}

if (process.env.NODE_ENV === 'development' && process.env.RUN_MODE !== 'shell') {
  mount(App, '#app')
}
