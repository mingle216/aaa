import Vue from 'vue'
import App from './App.vue'
import router from './router'
import VueClipboard from 'vue-clipboard2'
import globalObj from './utils/global'
import './assets/css/popup.css'
VueClipboard.config.autoSetContainer = true
Vue.use(VueClipboard)

Vue.config.productionTip = false
Vue.prototype.$_IS_MOBILE =globalObj.isMobile()
Vue.prototype.$Lan = globalObj.getLanguageValue
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
