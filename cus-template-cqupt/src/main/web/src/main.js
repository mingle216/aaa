import Vue from 'vue'
import App from './App.vue'
import './styles/index.css'
// IE兼容性 垫片
import 'core-js/stable';
import 'regenerator-runtime/runtime';
import  './utils/directive'
import './utils/getLanguage'
import vueScroll from './components/ContainerScroll'
import EmptyContainer from './components/EmptyContainer'
import AutoContainer  from './components/AutoContainer'
import flexContainer from './views/flexContainer/index.js'
import Menu from './packages/menu/index.js';
import Submenu from './packages/submenu/index.js';
import MenuItem from './packages/menu-item/index';
import echarts from 'echarts';

Vue.prototype.$echarts = echarts
const components = [
  Menu,
  Submenu,
  MenuItem
]
components.forEach(component => {
  Vue.component(component.name, component);
});
Vue.component("vueScroll", vueScroll);//滚动条组件
Vue.component("ContainerScroll", vueScroll );//滚动条组件
Vue.component("EmptyCon", EmptyContainer);//公共为空显示
Vue.component("AutoContainer", AutoContainer );//卡片自适应容器
Vue.component("flexContainer", flexContainer );//flex布局自动填充
// 多行截断组件
import vClamp from 'vue-clamp'
Vue.component("v-clamp", vClamp);

Vue.config.productionTip = false

import VueI18n from "vue-i18n";
Vue.use(VueI18n);
const i18n = new VueI18n({
  locale: sessionStorage.getItem('locale') || "zh_CN", // 定义默认语言为中文
  fallbackLocale: 'zh_CN',
  silentFallbackWarn: true,
  messages: {
    zh_CN: Object.assign({}, require("./assets/languages/zh_CN.json")),
    en: Object.assign({}, require("./assets/languages/en_US.json")),
  },
});
const i18nWe = new VueI18n({
  locale: sessionStorage.getItem('locale') !== "zh_CN" ? 'en' : 'zh_CN', // 定义默认语言为中文
  fallbackLocale: 'zh_CN',
  silentFallbackWarn: true,
  messages: {
    zh_CN: Object.assign({}, window.WE.lang.zhCN),
    en: Object.assign({}, window.WE.lang.en),
  },
});
// WE组件国际化  如果不使用 vue-i18n@5.x，而是用其他的 i18n 插件，Element 将无法兼容
window.WE.i18n((key, value) => i18nWe.t(key, value))

function init() {
  new Vue({
    i18n,
    render: h => h(App)
  }).$mount('#app')
}

console.log('process.env.RUN_MODE', process.env.NODE_ENV, process.env.RUN_MODE)

if (
  process.env.NODE_ENV === 'development' &&
  process.env.RUN_MODE !== 'shell'
) {
  init()
} else {
  window.addEventListener('message', e => {
    if (e.data === 'env-ready') {
      init()
    }
  })
}
