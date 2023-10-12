<template>
  <div class="container" ref="container">
    <div
      class="container-top"
      ref="container-top"
      v-if="$_IS_MOBILE && isShowBackButton && !isHideBack && showMobileTitle"
    >
      <div class="go-history" @click="goHistortHandle">
        <img class="return-icon" :src="returnImg" />
      </div>
      {{ pageTitle }}
    </div>
    <div class="container-main" ref="container-main">
      <iframe
        id="template-container"
        ref="frame"
        border="0"
        frameborder="0"
        class="template-container "
        :class="{ 'mobile-container': $_IS_MOBILE }"
        :src="templateId"
        @load="handleLoaded"
      />
    </div>
  </div>
</template>

<script>
import globalObj from "../utils/global";
import Popup from "./popup";
import blackAndWhiteMode from "../libs/mixins/blackAndWhiteMode.js";
import {
  getLoginUser,
  queryUserSiteWitching,
  getPortalConfig,
} from "../api/service";
import { themeMapPc, themeMapH5 } from "../utils/weTheme.js";
// import Vconsole from 'vconsole';
// new Vconsole();
export default {
  components: {},
  name: "Home",
  mixins: [blackAndWhiteMode],
  mounted() {
    if (!this.$_IS_MOBILE) {
      globalObj.on("startPopup", () => {
        new Popup(globalObj.getPageData(), globalObj.getUserInfo()).init({
          notice: this.$Lan("public", "notice", "公告"),
          noTip: this.$Lan("public", "noTip", "下次不再提醒"),
        });
        globalObj.off("startPopup");
      });
    }
  },
  data() {
    let origin = location.origin;
    if (!origin) {
      origin = location.protocol + "//" + location.host;
    }
    return {
      templateId: "",
      pageTitle: "",
      returnImg: require("../assets/images/return.png"),
      isShowBackButton: false,
      showMobileTitle: false,
      isHideBack: false,
      origin,
      timer: null, // 心跳检测会话是否过期
      themeInited: false,
      compatibleBrowserList: [
        {
          key: '0',
          value: globalObj.is360()
        },
        {
          key: '1',
          value: globalObj.isSafari()
        },
        {
          key: '2',
          value: globalObj.isChrome()
        },
        {
          key: '3',
          value: globalObj.isFireFox()
        },
        {
          key: '4',
          value: globalObj.isIE()
        },
        {
          key: '5',
          value: globalObj.isEdge()
        }
      ],
      isInit: true,
      isMobile: globalObj.isMobile(),
      localeStyleInited: false // 是否加载过本地化样式
    };
  },
  methods: {
    goHistortHandle() {
      globalObj.emit("goBack");
      window.history.back(-1);
      globalObj.closeServiceModal();
    },
    handleLoaded() {
      const innerWindow = this.$refs.frame.contentWindow;
      innerWindow.shell = globalObj;
      innerWindow.minosStataCollect = window.minosStataCollect;
      innerWindow.postMessage("env-ready", "*");
      if (this.templateId) {
        // 加载组件定制样式和本地化样式
        const iframe = this.$refs.frame;
        const innderDoc =
          iframe?.contentDocument || iframe?.contentWindow?.document;
        this.setThirdThemeColor(innderDoc, this.$_IS_MOBILE ? themeMapH5 : themeMapPc);
        this.$_IS_MOBILE && this.setLocaleStyle(innderDoc)
      }
    },
    update() {
      const pageData = globalObj.getPageData();
      const commonConfig = pageData?.commonConfig || {}
      const { isBrowserCompatible, compatibleBrowser, compatibleInfo } = commonConfig
      this.isShowBackButton = pageData.isShowBackButton ? true : false;
      this.showMobileTitle = pageData.showMobileTitle ? true : false;
      if (process.env.NODE_ENV === "development") {
        this.templateId = "/" + pageData.templateEntity.templateId;
      } else {
        // 根据后端返回的展示方案类型来判断，0：pc 1: 移动
        const platformType = pageData.showProgrammeEntity.platformType;
        if (platformType === 0) {
          if (this.$_IS_MOBILE) {
            window.Qmsg.warning(
              `${this.$Lan(
                "public",
                "notSupportDisplay",
                "当前展示方案不支持在移动端展示!"
              )}`,
              {
                autoClose: false,
                showClose: true,
              }
            );
          }
          this.templateId =
            "/" + pageData.templateEntity.templateId + "/pc/index.html";
        } else {
          this.templateId =
            "/" + pageData.templateEntity.templateId + "/mobile/index.html";
        }
      }
      if (isBrowserCompatible && !this.isMobile) {
        const temp = this.compatibleBrowserList.filter(el => compatibleBrowser.includes(el.key))
        const locale = sessionStorage.getItem("locale") || 'zh_CN';
        const cnMsg = (compatibleInfo || []).find(el => el.langCountry === 'zh_CN')?.langValue
        const message = (compatibleInfo || []).find(el => el.langCountry === locale)?.langValue || cnMsg
        if (this.isInit && !temp.some(el => el.value)) {
          window.Qmsg.warning(
            `<span class="portal-font-color-lv1">${message}</span>`,
            {
              html: true,
              autoClose: false,
              showClose: true,
            }
          )
          this.isInit = false;
        }
      }
      globalObj.setThemeColor(document, pageData);
      this.checkSession();
    },
    setTempSite() {
      const siteList = globalObj.getSiteList();
      let curSiteRoute = null;
      const sessionSite = sessionStorage.getItem("newSite");
      if (sessionSite) {
        curSiteRoute = sessionSite;
      } else {
        const randomSiteRoute =
          siteList &&
          siteList[Math.round(Math.random() * (siteList.length - 1))].siteRoute;
        sessionStorage.setItem("newSite", randomSiteRoute);
        curSiteRoute = randomSiteRoute;
      }
      if (
        curSiteRoute &&
        curSiteRoute !== globalObj.getCurrentSite().siteRoute
      ) {
        globalObj.openPage(
          `${this.origin}/${curSiteRoute}/index.html${location.hash}`,
          0,
          2
        );
      } else {
        globalObj.updateRouter(this.$route.path);
      }
    },
    async handleTempSite() {
      let isLogin = false;
      let isTemp = false;
      const [loginRes] = await getLoginUser();
      if (loginRes.errcode === "0" && loginRes.data) {
        isLogin = true;
      }
      // 获取所有站点和当前选择站点
      await globalObj.getCurrentRouteSite();
      if (isLogin) {
        const [res] = await queryUserSiteWitching({
          userAccount: loginRes.data.userAccount,
        });
        if (res.errcode === "0" && res.data.siteWid) {
          const curSite = globalObj
            .getSiteList()
            .find((item) => item.wid === res.data.siteWid);
          if (curSite) {
            // 是否选择过站点
            if (curSite.siteRoute !== globalObj.getCurrentSite().siteRoute) {
              globalObj.openPage(
                `${this.origin}/${curSite.siteRoute}/index.html${location.hash}`,
                0,
                2
              );
            } else {
              globalObj.updateRouter(this.$route.path);
            }
            return;
          }
        }
        isTemp = true;
      } else {
        // 未登录时是否切过站点
        if (!sessionStorage.getItem("siteRoute")) {
          isTemp = true;
        }
      }
      // 已选择过站点，直接打开该站点，未选择过随机站点
      isTemp ? this.setTempSite() : globalObj.updateRouter(this.$route.path);
    },
    beforeunloadHandler() {
      sessionStorage.removeItem('casp_ioc_domin');
      sessionStorage.removeItem('i18nList');
      // sessionStorage.removeItem('staticData');
      sessionStorage.removeItem(`pageData`);
      sessionStorage.removeItem('routeSite');
      sessionStorage.removeItem('loginUser');
      if (
        sessionStorage.getItem("newSite") ==
        globalObj.getCurrentSite().siteRoute
      ) {
        sessionStorage.removeItem("newSite");
      }
    },
    async updateLocationSite() {
      const userInfo = globalObj.getUserInfo();
      const pathname = location.pathname;
      const siteRoute =
        pathname === "/" ? "" : pathname.split("/index.html")[0];
      let newSiteRoute = null;
      if (userInfo) {
        // 获取当前用户选择站点
        const [res] = await queryUserSiteWitching({
          userAccount: userInfo.userAccount,
        });
        if (res.errcode === "0" && res.data.siteWid) {
          const curSite = globalObj
            .getSiteList()
            .find((item) => item.wid === res.data.siteWid);
          if (curSite && curSite.siteRoute != siteRoute) {
            newSiteRoute = curSite;
          }
        }
      } else {
        let noLoginSite = sessionStorage.getItem("siteRoute") || "null";
        noLoginSite = JSON.parse(noLoginSite);
        if (noLoginSite && siteRoute != noLoginSite.siteRoute) {
          newSiteRoute = noLoginSite;
        }
      }
      if (newSiteRoute) {
        window.history.replaceState(
          null,
          null,
          `${this.origin}/${newSiteRoute.siteRoute}/index.html${location.hash}`
        );
        globalObj.currentSite = newSiteRoute;
      }
    },
    async fetchPortalConfig() {
      const [res] = await getPortalConfig({
        key: "SITE_RULE",
      });
      if (res.errcode === "0" && res.data) {
        globalObj.openRandomSite = res.data.configValue || "0";
      }
    },
    checkSession() {
      const userInfo = globalObj.getUserInfo();
      if (!this.timer && userInfo) {
        this.timer = setInterval(() => {
          this.getLoginUser();
        }, 1000 * 60 * 5);
      }
    },
    clearTimer() {
      clearInterval(this.timer);
      this.timer = null;
    },
    async getLoginUser() {
      const [res] = await getLoginUser();
      if (res.errcode === "0" && !res.data) {
        const page = window.location.hash.replace("#", "");
        globalObj.logout({
          params: {
            localHref: page,
          },
        });
      }
    },
    visibilityChange() {
      if (window.document.visibilityState === "visible") {
        const userInfo = globalObj.getUserInfo();
        if (userInfo) {
          this.getLoginUser();
        }
      }
    },
    setThirdThemeColor(innderDoc, themeMap) {
      if (this.themeInited || !innderDoc) {
        return;
      }
      const pageData = globalObj.getPageData();
      const templateConfig = pageData
        ? pageData.showProgrammeEntity.templateConfig
        : "";
      const configObj = JSON.parse(JSON.parse(templateConfig || "{}"));
      const themeColor = configObj.themeColorSetting || {};
      const colorMap = {};
      for (let el in themeColor) {
        colorMap[el] = themeColor[el];
      }
      let themeStr = "";
      // 组装成字符串
      for (const key in themeMap) {
        themeStr += `${key}${themeMap[key]}`;
      }
      // 替换颜色值
      Object.keys(colorMap).forEach((key) => {
        themeStr = themeStr.replace(new RegExp(`@${key}`, "ig"), colorMap[key]);
      });
      globalObj.addCSS(innderDoc, themeStr);
      this.themeInited = true;
    },
    setLocaleStyle(innderDoc) {
      const pageData = globalObj.getPageData();
      const wid = pageData?.showProgrammeEntity?.wid;
      if (this.localeStyleInited || !innderDoc || !wid) {
        return;
      }
      globalObj.setLocalStyle(innderDoc, wid)
      this.localeStyleInited = true;
    },
  },
  beforeRouteUpdate(to, from, next) {
    globalObj.updateRouter(to.path);
    next();
    this.updateLocationSite(); //其他tab切换站点，当前tab切换路由站点未更新场景
  },
  async created() {
    await this.fetchPortalConfig();
    if (globalObj.openRandomSite == "1") {
      //开启随机站点
      await this.handleTempSite();
    } else {
      globalObj.updateRouter(this.$route.path);
    }

    globalObj.on("page-title-change", (val) => {
      this.pageTitle = val;
    });
    globalObj.on("page-change", () => {
      this.update();
      if (this.$_IS_MOBILE) {
        this.setGrayMode(globalObj.getPageData()?.mobileBlackAndWhiteMode);
      } else {
        this.setGrayMode(globalObj.getPageData()?.blackAndWhiteMode);
      }
    });
    globalObj.on("copy-text", (text) => {
      this.$copyText(text).then(
        () => {
          globalObj.showMessage({
            type: "success",
            message: this.$Lan("public", "copySuccess", "复制成功"),
          });
        },
        () => {
          globalObj.showMessage({
            type: "error",
            message: this.$Lan("public", "copyFail", "复制失败"),
          });
        }
      );
    });

    //唐素林 url中有isHideBack 隐藏头部
    if (
      globalObj.getUrlParam() &&
      typeof globalObj.getUrlParam().isHideBack != "undefined" &&
      globalObj.getUrlParam().isHideBack == 1
    ) {
      this.isHideBack = true;
    } else {
      this.isHideBack = false;
    }
    // 隐藏/展示头部
    globalObj.on("hide-back", (val) => {
      this.isHideBack = val;
    });
    window.addEventListener("beforeunload", this.beforeunloadHandler, false);
    window.addEventListener("visibilitychange", this.visibilityChange, false);
  },
  beforeDestroy() {
    this.clearTimer();
    globalObj.off("page-change");
    globalObj.off("copy-text");
    globalObj.off("hide-back");
    window.removeEventListener("beforeunload", this.beforeunloadHandler, false);
    window.removeEventListener(
      "visibilitychange",
      this.visibilityChange,
      false
    );
  },
};
</script>

<style scoped>
.container {
  height: 100%;
  border: none;
  display: flex;
  flex-flow: column;
  /* vant tabbar已经设置了安全距离，所以先注释掉 */
  /* padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom); */
  box-sizing: border-box;
}

.container-top {
  touch-action: none;
  height: 2.625rem;
  /* min-height: 36px; */
  line-height: 2.625rem;
  width: 100%;
  box-sizing: border-box;
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
  text-align: center;
  font-size: 1.125rem;
  color: #102645;
  position: relative;
  padding: 0 3.5rem;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.go-history {
  position: absolute;
  height: 100%;
  width: 2.5rem;
  top: 0;
  line-height: 2.625;
  left: 0;
}
.return-icon {
  height: 1.125rem;
}
.container-main {
  flex: 1;
  width: 100%;
  position: relative;
  z-index: 10;
  min-height: 0;
  /* overflow-y: scroll; */
}
.template-container {
  width: 100%;
  height: 100%;
  border: none;
  position: relative;
  z-index: 100;
}
.mobile-container {
  overflow: scroll;
  -webkit-overflow-scrolling: touch;
  min-width: 100%;
  *width: 100%;
  width: 1px;
  cursor: pointer;
  pointer-events: auto;
}
.iframe-lv {
  z-index: 10000;
}
iframe {
  display: block;
}
</style>
