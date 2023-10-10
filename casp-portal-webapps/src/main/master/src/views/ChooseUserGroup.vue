<template>
  <div class="choose__user__group">
    <vue-scroll  :ops="{bar: { size: '6px', background: '#d8d8d8' }, rail: { size: '6px' }}" >
      <header class="head__wrap" v-if="logoUrl">
        <img
          class="head__logo"
          :src="logoUrl"
          alt=""
        />
      </header>
      <div class="group__wrap">
        <div class="icon__wrap">
          <img
            v-if="iconUrl"
            class="service__icon"
            :src="/^http(s)?:\/\//.test(iconUrl) ? iconUrl : `data:image/png;base64,${iconUrl}`"
            alt=""
          />
          <img v-else class="service__icon" :src="errorUrl" alt="" />
          <img v-if="hidden" class="error__img" :src="ErrorIcon" alt="">
        </div>
        <p class="service__name" :title="serviceName">
          {{ serviceName }}
        </p>
        <p class="group__title" v-if="!hidden">
          {{ $Lan('public', 'groupTips', '您有如下身份可访问该服务，请选择') }}
        </p>
        <p class="diable__title" v-else>
          {{ $Lan('public', 'unavailableService', '此服务暂时无法使用') }}
        </p>
        <ul class="group__box" v-if="groupList.length">
          <li
            class="group__item"
            v-for="item in groupList"
            :key="item && item.modelWid"
            :title="item.mulName"
            @click="handleClick(item)"
          >
            {{ item.mulName }}
            <div class="mask-bg portal-primary-backgroundcolor-lv1"></div>
            <div class="mask"></div>
          </li>
        </ul>
        <p v-if="hidden">{{ $Lan('public', 'contactAdmin', '如有问题请联系管理员') }}</p>
      </div>
     </vue-scroll>
  </div>
</template>

<script>
import { queryServiceByWid, serviceShow, getLoginUser, getThemeData } from '../api/service'
import globalObj from '../utils/global'
import ErrorIcon from '../assets/images/error.png'
import vueScroll from 'vuescroll/dist/vuescroll-native'
import blackAndWhiteMode from '../libs/mixins/blackAndWhiteMode.js'
export default {
  components: { vueScroll },
  mixins: [blackAndWhiteMode],
  data() {
    return {
      serviceName: '',
      iconUrl: '',
      accessUrl: '',
      groupList: [],
      logoUrl: '',
      hidden: false,
      isShowGroup: false,
      ErrorIcon,
      loginUser: null,
			userLang: ''
    }
  },
  computed: {
    errorUrl() {
      return globalObj.ErrorImg
    }
  },
  methods: {
    async getServiceInfo() {
      const serviceId = this.$route.query.wid
      const serviceWid = this.$route.query.serviceWid // 其他业务线跳转的地址会带serviceiWid,如果有就加到参数里
      let params = serviceWid ? {
        serviceId: serviceWid
      } : {}
      const [res] = await queryServiceByWid({
        serviceId,
        filterKey: 0,
        params
      })
      if (res.errcode === '0') {
        const data = res.data
        const isLogin = data.isLogin
        const temp = data && data.serviceInfo[0]
        const serviceStation = temp && temp.serviceStation
        const userLang = data && data.localLang || 'zh_CN'
				this.userLang = userLang
        const serviceNameLangKeys = temp && temp.serviceNameLangKeys || []
        if (serviceNameLangKeys.length) {
          const t = serviceNameLangKeys.find(el => el.langCountry === userLang)
          this.serviceName = t && t.langValue || (temp && temp.serviceName)
        } else {
          this.serviceName = temp && temp.serviceName
        }

        const pcIconUrl = temp && temp.pcIconUrl
        const pcAccessUrl = temp && temp.pcAccessUrl
        const mobileIconUrl = temp && temp.mobileIconUrl
        const mobileAccessUrl = temp && temp.mobileAccessUrl

        this.iconUrl = pcIconUrl
        this.accessUrl = pcAccessUrl
        if (serviceStation === 1) {
          this.iconUrl = mobileIconUrl
          this.accessUrl = mobileAccessUrl
        } else if (serviceStation === 2) {
          this.iconUrl = pcIconUrl || mobileIconUrl
          this.accessUrl = pcAccessUrl || mobileAccessUrl
        }
        // 服务不可用
        const loginVisibility = isLogin && temp.loginVisibility === '2' // 已登录不可见
        const notLoginVisibility = !isLogin && temp.guestVisibility === '1' // 游客不可见
        this.hidden = temp && (temp.isDeleted === 1 || temp.appEnabled === 0 || !temp.hasPermission || loginVisibility || notLoginVisibility)
        this.isShowGroup = temp && temp.isShowGroup === '1'  // 是否显示用户组
        // 如果服务可用且不显示用户组，跳转原来的url
        if (!this.hidden && !this.isShowGroup) {
          location.replace(this.accessUrl)
        }
      }
    },
    async getUserGroup() {
      if (this.hidden) {
        this.groupList = []
        return
      }
      const wid = this.$route.query.wid
      const isMobile = this.$route.query.isMobile
      const serviceWid = this.$route.query.serviceWid // 其他业务线跳转的地址会带serviceiWid,如果有就加到参数里
      let params = {
        isMobile,
        serviceId: wid
      }
      if (serviceWid) {
        params = {
          ...params,
          serviceWid
        }
      }
      const [res] = await serviceShow({
        params
      })
      if (res.errcode === '0') {
        const data = res.data || {}
        this.groupList = data.grantData || []
				this.groupList.map(item => {
					const userGroupName = item.nameMultiLang || []
					const temp = userGroupName.find(el => el.langCountry === this.userLang)
					item.mulName = (temp && temp.langValue) || (item && item.modelName)
					return item
				})
        if (!this.groupList.length) {
          location.replace(this.accessUrl)
        }
      }
    },
    async handleClick(item) {
      await this.getServiceInfo()
      if (!this.hidden && !this.isShowGroup) {
        location.replace(this.accessUrl)
      } else if (this.hidden) {
        this.groupList = []
      } else {
        await this.getUserGroup()
        if (this.groupList.length) {
          location.href = item.serviceUrl
        }
      }
    },
    async getThemeData() {
      const [res] = await getThemeData();
      if (res.errcode === "0") {
        const data = res.data;
        globalObj.setThemeColor(document, data)
        this.setGrayMode(data.blackAndWhiteMode);
        const templateConfig = data && data.showProgrammeEntity
          ? JSON.parse(JSON.parse(data.showProgrammeEntity.templateConfig || '{}'))
          : {}
        this.logoUrl =
          (templateConfig &&
            templateConfig.configLogo &&
            templateConfig.configLogo.logoUrl) ||
          ''
      }
    }
  },
  async mounted() {
    // 记录平台访问数据
    globalObj.pageCollect()
    this.getThemeData();
    
    const [res] = await getLoginUser()
    if (res.errcode === '0') {
      this.loginUser = res.data
    }
    await globalObj.getStaticData({
      languageKey: this.loginUser?.preferredLanguage || 'zh_CN',
      modelKey: "casp-portal",
    })
    document.title = this.$Lan('public', 'chooseGroup', '选择用户组')
    await this.getServiceInfo()
    await this.getUserGroup()
    document.title = this.$Lan('public', 'chooseGroup', '选择用户组')
  }
}
</script>

<style scoped>
@import '../styles/minireset.css';

.choose__user__group {
  width: 100%;
  height: 100%;
  background-image: url('../assets/images/bg.png');
  background-size: cover;
  background-repeat: no-repeat;
  
}
.head__wrap {
  padding: 0 calc(50% - 37.5rem);
  /* min-width: 75rem; */
  padding-top: 32px;
}
.container-top{
  height: 2.625rem;
  line-height: 2.625rem;
  width: 100%;
  box-sizing: border-box;
  background-color: #fff;
  border-bottom:1px solid  #dcdfe6;
  text-align: center;
  font-size: 1.125rem;
  color: #102645;
  position: relative;
  padding:0 3.5rem;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.go-history{
  position: absolute;
  height: 100%;
  width: 2.5rem;
  top: 0;
  line-height: 2.625;
  left: 0;
}
.return-icon{
  height: 1.125rem;
}
.head__logo {
  max-width: 300px;
  min-width: 120px;
  width: auto;
  max-height: 80px;
}
.group__wrap {
  width: 360px;
  padding-top: 140px;
  margin: 0 auto;
  text-align: center;
  height: calc(100% - 80px);
  padding-bottom: 20px;
  box-sizing: border-box;
}
.icon__wrap {
  position: relative;
  width: 80px;
  height: 80px;
  margin: 0 auto;
}
.icon__wrap .error__img {
  position: absolute;
  right: -5px;
  bottom: -5px;
}
.service__icon {
  width: 80px;
  height: 80px;
}
.service__name {
  font-size: 24px;
  color: #111d35;
  height: 33px;
  line-height: 33px;
  max-width: 288px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
  margin: 22px auto 70px;
}
.group__title {
  font-size: 16px;
  color: #111d35;
  margin-bottom: 20px;
  /* height: 22px; */
  line-height: 22px;
}
.diable__title {
  font-size: 24px;
  color: #000;
  height: 33px;
  line-height: 33px;
  margin-bottom: 20px;
}
.group__box {
  width: 100%;
}
.group__item {
  width: 360px;
  border-radius: 4px;
  height: 48px;
  line-height: 48px;
  text-align: center;
  cursor: pointer;
  font-size: 16px;
  color: #ffffff;
  margin-bottom: 20px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding: 0 20px;
  position: relative;
  z-index: 1;
}
.mask-bg {
  width: 360px;
  height: 48px;
  display: block;
  position: absolute;
  left: 0;
  top: 0;
  z-index: -1;
}
.mask {
  width: 360px;
  height: 48px;
  display: block;
  background: transparent;
  position: absolute;
  left: 0;
  top: 0;
  z-index: -1;
  background: rgba(0,0,0,.2);
  visibility: hidden;
}
.group__item:hover .mask {
  visibility: visible;
}
</style>
