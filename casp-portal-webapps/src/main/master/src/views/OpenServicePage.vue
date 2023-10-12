<template>
  <div class="wrap">
    <TheLoading v-if="fullscreenLoading"/>
    <div class="empty" v-if="showEmpty">
      <img :src="CommonEmpty" alt="">
    </div>
  </div>
</template>

<script>
import globalObj from "../utils/global";
import { queryServiceByWid } from '../api/service'
import PcEmpty from '../assets/images/pc-empty.png'
import MobileEmpty from '../assets/images/mobile-empty.png'
import CommonEmpty from '../assets/images/common-empty.png'
import TheLoading from './TheLoading.vue'
import { loadAsyncJs } from '../libs/tools';
export default {
  components: {
    TheLoading
  },
  data() {
    return {
      PcEmpty,
      MobileEmpty,
      CommonEmpty,
      showEmpty: false,
      fullscreenLoading: false,
      serviceInfo: []
    }
  },
  methods: {
    async getServiceInfo() {
      this.fullscreenLoading = true
      const serviceId = this.$route.params.id || ''
      const [res] = await queryServiceByWid({
        serviceId,
        filterKey: 1
      })
      if (res.errcode === '0') {
        // console.log(res.data)
        this.serviceInfo = res.data && res.data.serviceInfo || []
        if (this.serviceInfo.length) {
          const item = this.serviceInfo[0]
          globalObj.serviceVisitData(item)

          const serviceStation = item.serviceStation
          const pcAccessUrl = item.pcAccessUrl
          const mobileAccessUrl = item.mobileAccessUrl
          let url = ''
          if (serviceStation === 1) {
            url = mobileAccessUrl
          } else if (serviceStation === 2) {
            url = this.$_IS_MOBILE ? mobileAccessUrl : pcAccessUrl || mobileAccessUrl
          } else {
            url = pcAccessUrl
          }
          let timer = setInterval(() => {
            const report_success = sessionStorage.getItem('report_success') // 定时获取report接口
            if (report_success === 'true') {
              location.href = url
              this.fullscreenLoading = false
              clearInterval(timer)
            }
          }, 50)
          // 如果5s后report接口没有成功调用，清空定时器
          setTimeout(() => {
            location.href = url
            this.fullscreenLoading = false
            clearInterval(timer)
          }, 5000)
        } else {
          this.showEmpty = true
          this.fullscreenLoading = false
        }
      } else {
        this.showEmpty = true
        this.fullscreenLoading = false
      }
    }
  },
  created() {
    window.sessionStorage.setItem('openServicePage', true)
    let origin = location.origin;
    // location.origin IE不存在
    if (!origin) {
      origin = location.protocol + "//" + location.host;
    }
    let saUrl = `/js/track.min.js`
    try {
      loadAsyncJs(saUrl)
    } catch (e) {
      console.log(e)
    }
  },
  mounted() {
    this.getServiceInfo()
  },
  beforeDestroy() {
    window.sessionStorage.removeItem('openServicePage')
  },
}
</script>

<style scoped>
  .wrap {
    width: 100%;
    height: 100%;
    line-height: 22px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>