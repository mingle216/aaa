import VueRouter from 'vue-router'
import Home from './views/Home'
import ChooseUserGroup from './views/ChooseUserGroup'
import ChooseService from './views/ChooseService'
import OpenServicePage from './views/OpenServicePage'
import ServiceShow from './views/ServiceShow'
import AuthPage from './views/AuthPage'
import DealShareSchedule from './views/DealShareSchedule.vue'
import { getPortalConfig } from './api/service'
import { loadAsyncJs } from './libs/tools';
import { _sessionStorage } from './utils/storage.js'

const router = new VueRouter({
  routes: [{
    path: '/chooseGroup',
    component: ChooseUserGroup
  },{
    path: '/serviceShow',
    component: ServiceShow
  },{
    path: '/openService/:id',
    component: OpenServicePage
  },
  {
    path: '/chooseService',
    component: ChooseService
  },
  {
    path: '/authSites',
    component: AuthPage
  },
  {
    path: '/dealShareSchedule/:id',
    component: DealShareSchedule
  },
  {
    path: '*',
    component: Home
  }]
})

router.beforeEach(async(to, from, next) => {
  let saUrl = 'http://172.16.29.60:8012/casp-ioc/track.min.js'
  const portalConfig = _sessionStorage.get('casp_ioc_domin')
  if (portalConfig) {
    saUrl = `${portalConfig.configValue}/casp-ioc/track.min.js`
  } else {
    const [res] = await getPortalConfig({
      key: 'casp_ioc_domin'
    })
    if (res.errcode === '0' && res.data) {
      if (res.data.configValue) {
        saUrl = `${res.data.configValue}/casp-ioc/track.min.js`
        _sessionStorage.set('casp_ioc_domin', res.data, 1000*60*30)
      }
    }
  }
  try {
    loadAsyncJs(saUrl)
  } catch (e) {
    console.log(e)
  }
  next()
})

export default router
