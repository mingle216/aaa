import VueRouter from 'vue-router'
import Home from './views/Home'
import ChooseUserGroup from './views/ChooseUserGroup'
import OpenServicePage from './views/OpenServicePage'
import { getPortalConfig } from './api/service'
import { loadAsyncJs } from './libs/tools';

const router = new VueRouter({
  routes: [{
    path: '/chooseGroup',
    component: ChooseUserGroup
  },{
    path: '/openService/:id',
    component: OpenServicePage
  },
  {
    path: '*',
    component: Home
  }]
})

router.beforeEach(async(to, from, next) => {
  const [res] = await getPortalConfig({
    key: 'casp_ioc_domin'
  })
  if (res.errcode === '0' && res.data) {
    let saUrl = 'http://172.16.29.60:8012/casp-ioc/track.min.js'
    if (res.data.configValue) {
      saUrl = `${res.data.configValue}/casp-ioc/track.min.js`
      try {
        loadAsyncJs(saUrl)
      } catch (e) {
        console.log(e)
      }
    }
  }
  next()
})

export default router
