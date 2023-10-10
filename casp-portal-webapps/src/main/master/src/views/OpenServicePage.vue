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

          setTimeout(() => {
            const serviceStation = item.serviceStation
            const pcAccessUrl = item.pcAccessUrl
            const mobileAccessUrl = item.mobileAccessUrl
  
            if (serviceStation === 1) {
              location.href = mobileAccessUrl
            } else if (serviceStation === 2) {
              location.href = this.$_IS_MOBILE ? mobileAccessUrl : pcAccessUrl || mobileAccessUrl
            } else {
              location.href = pcAccessUrl
            }
            this.fullscreenLoading = false
          }, 6000)
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
  mounted() {
    this.getServiceInfo()
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