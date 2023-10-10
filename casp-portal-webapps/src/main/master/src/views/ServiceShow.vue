<template>
  <div
    class="serviceError portal-font-color-lv1"
    :class="[isMobile ? 'mobile' : '']"
  >
    <TheLoading v-if="fullscreenLoading" />
    <di class="showError" v-if="showError">
      <img class="img" src="/styles/images/serviceError.png" />
      <div class="showError__flex">
        <img
          v-if="iconUrl || errorUrl"
          class="service__icon"
          :src="
            /^http(s)?:\/\//.test(iconUrl)
              ? iconUrl
              : `data:image/png;base64,${iconUrl}`
          "
          alt=""
          @error="handleError"
        />
        <span class="serviceName" :title="serviceName">{{ serviceName }}</span>
      </div>
      <v-clamp
        v-if="serviceDesc"
        class="serviceDesc portal-font-color-lv2"
        :title="serviceDesc"
        autoresize
        :max-lines="2"
      >
        {{ serviceDesc }}
      </v-clamp>
      <div
        class="serviceInfo portal-font-color-lv3"
        :title="
          $Lan('public', 'serviceError', '对不起，该服务打不开，可能正在维护中')
        "
      >
        {{
          $Lan("public", "serviceError", "对不起，该服务打不开，可能正在维护中")
        }}
      </div>
    </di>
  </div>
</template>

<script>
import {
  getThemeData,
  serviceShow,
  getLoginUser,
  queryServiceByWid,
} from "../api/service";
import router from "../router";
import blackAndWhiteMode from "../libs/mixins/blackAndWhiteMode.js";
import globalObj from "../utils/global";
import TheLoading from "./TheLoading";
import vClamp from "vue-clamp";

export default {
  components: {
    TheLoading,
    vClamp,
  },
  mixins: [blackAndWhiteMode],
  data() {
    return {
      fullscreenLoading: true,
      showError: false,
      iconUrl: "",
      serviceName: "",
      serviceDesc: "",
      errorUrl: globalObj.ErrorImg,
      serviceId: this.$route.query.wid,
      isMobileService: this.$route.query.isMobile,
      userLang: "",
      isMobile: globalObj.isMobile(),
    };
  },
  methods: {
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorUrl;
      img.onerror = null; //防止闪图
    },
    async getThemeData() {
      const [res] = await getThemeData();
      if (res.errcode === "0") {
        const data = res.data;
        globalObj.setThemeColor(document, data);
      }
    },
    async getServiceShow() {
      const [res] = await serviceShow({
        params: {
          isMobile: this.isMobileService,
          serviceId: this.serviceId,
        },
      });
      this.fullscreenLoading = false;
      if (res.errcode === "0") {
        // 服务计数
        globalObj.visitService(this.serviceId, "service");
        // 如果用户组大于1跳到选择用户组页面
        const grantData = (res.data && res.data.grantData) || [];
        if (grantData.length > 1) {
          let { href } = router.resolve({
            path: "/chooseGroup",
            query: {
              isMobile: this.isMobileService,
              wid: this.serviceId,
            },
          });
          if (
            (navigator.userAgent.indexOf("compatible") > -1 &&
              navigator.userAgent.indexOf("MSIE") > -1) ||
            (navigator.userAgent.indexOf("Trident") > -1 &&
              navigator.userAgent.indexOf("rv:11.0") > -1)
          ) {
            href =
              (window.location.origin || "") +
              (window.location.pathname || "") +
              "?_t=" +
              new Date().getTime() +
              href;
          }
          window.location = href;
        } else if (grantData.length === 1) {
          window.location = grantData[0] && grantData[0].serviceUrl;
        } else {
          window.location = res.data && res.data.serviceUrl;
        }
      }
    },
    async getServiceInfo() {
      const serviceWid = this.$route.query.serviceWid; // 其他业务线跳转的地址会带serviceiWid,如果有就加到参数里
      let params = serviceWid
        ? {
            serviceId: serviceWid,
          }
        : {};
      const [res] = await queryServiceByWid({
        serviceId: this.serviceId,
        filterKey: 0,
        params,
      });
      if (res.errcode === "0") {
        const data = res.data;
        const temp = data && data.serviceInfo[0];
        const serviceStation = temp && temp.serviceStation;
        const userLang = (data && data.localLang) || "zh_CN";
        this.userLang = userLang;
        const serviceNameLangKeys = (temp && temp.serviceNameLangKeys) || [];
        if (serviceNameLangKeys.length) {
          const t = serviceNameLangKeys.find(
            (el) => el.langCountry === userLang
          );
          this.serviceName = (t && t.langValue) || (temp && temp.serviceName);
        } else {
          this.serviceName = temp && temp.serviceName;
        }
        
        const serviceDescLangKeys = (temp && temp.serviceDescLangKeys) || [];
        if (serviceDescLangKeys.length) {
          const t = serviceDescLangKeys.find(
            (el) => el.langCountry === userLang
          );
          this.serviceDesc = (t && t.langValue) || (temp && temp.serviceDesc);
        } else {
          this.serviceDesc = temp && temp.serviceDesc;
        }

        const pcIconUrl = temp && temp.pcIconUrl;
        const mobileIconUrl = temp && temp.mobileIconUrl;

        this.iconUrl = pcIconUrl;
        if (serviceStation === 1) {
          this.iconUrl = mobileIconUrl;
        } else if (serviceStation === 2) {
          this.iconUrl = pcIconUrl || mobileIconUrl;
        }

        document.title = this.serviceName
      }
      this.fullscreenLoading = false;
    },
  },
  created() {
    if (!this.isMobile) {
      this.getThemeData();
    }
  },
  async mounted() {
    // 先查询是否开启服务url查询功能，1为开启；开启后需先检测服务的url是否可访问，不可访问时展示异常信息;移动端在底座处理了该逻辑，直接展示错误异常信息
    if (this.isMobile) {
      this.showError = true;
      this.getServiceInfo();
    } else {
      const isValid = await globalObj.checkServiceUrl(
        decodeURIComponent(this.$route.query.url)
      );
      if (isValid) {
        this.getServiceShow();
      } else {
        this.showError = true;
        const [res] = await getLoginUser();
        if (res.errcode === "0") {
          this.loginUser = res.data;
        }
        await globalObj.getStaticData({
          languageKey: this.loginUser?.preferredLanguage || "zh_CN",
          modelKey: "casp-portal",
        });
        this.getServiceInfo();
      }
    }
  },
};
</script>

<style scoped>
.serviceError {
  width: 100%;
  height: 100%;
  line-height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}
.serviceError .img {
  width: 256px;
}
.showError {
  max-width: 468px;
}
.showError__flex {
  margin-top: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.serviceName {
  font-size: 16px;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-break: break-all;
}
.serviceDesc {
  margin-top: 12px;
  word-break: break-all;
}
.serviceError .serviceInfo {
  margin-top: 12px;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-break: break-all;
}
.service__icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  margin-right: 16px;
}
.mobile .showError {
  max-width: 14rem;
}
.mobile.serviceError .img {
  width: 8rem;
}
</style>
