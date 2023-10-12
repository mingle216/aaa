import Vue from 'vue'
import App from './App.vue'
import './styles/index.css'
import './styles/wed.mobile.css'
import './utils/date'
// import 'regenerator-runtime/runtime';
import EmptyCon from './components/EmptyContainer';
import customActionSheet from './components/customActionSheet';
// import flexContainer from './views/flexContainer/index.js'
import  './utils/directive';
import './utils/getLanguage';
Vue.component("EmptyCon", EmptyCon);//公共为空显示
Vue.component("customActionSheet", customActionSheet);//自定义ActionSheet组件
// Vue.component("flexContainer", flexContainer );//flex布局自动填充

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
// console.log(i18n)
// WE组件国际化  如果不使用 vue-i18n@5.x，而是用其他的 i18n 插件，Element 将无法兼容
 
// Locale.add(messages);
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
      console.log(window.shell)
      init()
    }
  })
}
