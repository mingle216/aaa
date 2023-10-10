<template>
  <we-popup
    round
    position="bottom"
    :value="visible"
    :style="{ width: '100%', height: '90%' }"
    @click-overlay="close"
    get-container="body"
  >
    <div class="modal__title">
      <span class="ellipsis">{{ $Lan('CUS_TEMPLATE_OFFICIAL_h5', 'goToService', '跳转服务') }}</span>
      <we-icon name="cross" color="#9FA8B5" @click="close" />
    </div>
    <div class="modal__wrap">
      <div class="group__wrap">
        <div class="icon__wrap">
          <img
            v-if="iconUrl"
            class="service__icon"
            :src="
              /^http(s)?:\/\//.test(iconUrl)
                ? iconUrl
                : `data:image/png;base64,${iconUrl}`
            "
            alt=""
          />
          <img v-else class="service__icon" :src="errorImg" alt="" />
          <img v-if="hidden" class="error__img" :src="ErrorIcon" alt="" />
        </div>
        <div class="service__name" :title="serviceName">
          {{ serviceName }}
        </div>
        <div class="group__title ellipsis" v-if="!hidden">
          {{ $Lan('CUS_TEMPLATE_OFFICIAL_h5', 'groupTips', '您有如下身份可访问该服务，请选择:') }}
        </div>
        <div class="diable__title" v-else>
          {{ $Lan('CUS_TEMPLATE_OFFICIAL_h5', 'unavailableService', '此服务暂时无法使用') }}
        </div>
        <ul class="group__box" v-if="groupList.length">
          <li
            class="group__item portal-primary-backgroundcolor-hover-lv1 portal-primary-color-lv1 portal-primary-border-color-lv1"
            v-for="item in groupList"
            :key="item && item.modelWid"
            :title="item.mulName"
            @click="handleClick(item)"
          >
            {{ item.mulName }}
          </li>
        </ul>
        <p v-if="hidden">
          {{ $Lan('CUS_TEMPLATE_OFFICIAL_h5', 'contactAdmin', '如有问题请联系管理员') }}
        </p>
      </div>
    </div>
  </we-popup>
</template>

<script>
import ErrorIcon from '../assets/images/error.png'
export default {
  data() {
    return {
      errorImg: window.shell.ErrorImg,
      ErrorIcon,
      visible: false,
      serviceList: "",
      itemName: "",
      selectedItem: {},
      pcOpen: false,
      showTitle: true,
      serviceName: '',
      iconUrl: '',
      accessUrl: '',
      groupList: [],
      logoUrl: '',
      hidden: false,
      isShowGroup: false,
      isFirstPage: true,
      serviceId: '',
      userLang: ''
    };
  },
  methods: {
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    async show(item) {
      console.log(item)
      this.visible = true
      this.serviceId = item.wid
      await this.getServiceInfo()
      await this.getUserGroup()
    },
    close() {
      console.log('用户组弹窗关闭')
      this.visible = false;
    },
    async getServiceInfo() {
      const [res] = await window.shell.queryServiceByWid({
        serviceId: this.serviceId,
        filterKey: 0
      })
      if (res.errcode === '0') {
        const data = res.data
        const isLogin = data.isLogin
        const temp = data && data.serviceInfo[0]
        const serviceStation = temp && temp.serviceStation
        const templateConfig = data && data.showProgrammeEntity
          ? JSON.parse(JSON.parse(data.showProgrammeEntity.templateConfig || '{}'))
          : {}
        this.logoUrl =
          (templateConfig &&
            templateConfig.configLogo &&
            templateConfig.configLogo.logoUrl) ||
          ''
        this.userLang = data && data.localLang || 'zh_CN'
        const serviceNameLangKeys = temp && temp.serviceNameLangKeys || []
        if (serviceNameLangKeys.length) {
          const t = serviceNameLangKeys.find(el => el.langCountry === this.userLang)
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
          this.iconUrl = temp && temp.mobileIconUrl
          this.accessUrl = temp && temp.mobileAccessUrl
        } else if (serviceStation === 2) {
          this.iconUrl = mobileIconUrl || pcIconUrl
          this.accessUrl = mobileAccessUrl || pcAccessUrl
        }
        // 服务不可用
        const loginVisibility = isLogin && temp.loginVisibility === '2' // 已登录不可见
        const notLoginVisibility = !isLogin && temp.guestVisibility === '1' // 游客不可见
        this.hidden = temp && (temp.isDeleted === 1 || temp.appEnabled === 0 || !temp.hasPermission || loginVisibility || notLoginVisibility)
        this.isShowGroup = temp && temp.isShowGroup === '1'  // 是否显示用户组
        // 如果服务可用且不显示用户组，跳转原来的url
        if (!this.hidden && !this.isShowGroup) {
          window.shell.openUrl(this.accessUrl)
        }
      }
    },
    async getUserGroup() {
      if (this.hidden) {
        this.groupList = []
        return
      }
      const [res] = await window.shell.serviceShow({
        params: {
          serviceId: this.serviceId,
          serviceUrl: this.accessUrl
        }
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
          window.shell.openUrl(this.accessUrl)
        }
      }
    },
    async handleClick(item) {
      await this.getServiceInfo()
      if (!this.hidden && !this.isShowGroup) {
        window.shell.openUrl(this.accessUrl)
      } else if (this.hidden) {
        this.groupList = []
      } else {
        await this.getUserGroup()
        if (this.groupList.length) {
          window.shell.openUrl(item.serviceUrl)
        }
      }
      this.visible = false
    }
  }
}
</script>

<style lang="less" scoped>
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.modal__title {
  height: 56px;
  box-shadow: inset 0 -1px 0 0 #e7edf1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 17px;
  font-size: 18px;
  color: #102645;
}
.modal__wrap {
  height: calc(100% - 56px);
  overflow: auto;
  color: #102645;
  z-index: 9999;
  -webkit-overflow-scrolling: touch;
}
.group__wrap {
  text-align: center;
  padding: 80px 50px 30px;
  // display: flex;
  // flex-direction: column;
  // align-items: center;
  // height: 100%;
  // justify-content: flex-start;
}
.icon__wrap {
  position: relative;
  width: 60px;
  height: 60px;
  margin: 0 auto;
}
.icon__wrap .error__img {
  position: absolute;
  right: -5px;
  bottom: -5px;
}
.service__icon {
  width: 60px;
  height: 60px;
}
.service__name {
  font-size: 20px;
  color: #111d35;
  font-weight: bold;
  line-height: 24px;
  min-height: 24px;
  max-width: 235px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
  margin: 16px auto 38px;
}
.group__title {
  font-size: 14px;
  color: #707D8F;
  padding-bottom: 16px;
  // height: 20px;
  // line-height: 20px;
  max-width: 270px;
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
  width: 100%;
  border-radius: 4px;
  height: 48px;
  line-height: 48px;
  text-align: center;
  cursor: pointer;
  font-size: 16px;
  margin-bottom: 20px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding: 0 20px;
  border: 1px solid;
  &:hover {
    color: #fff!important;
  }
}
</style>
