<template>
  <div class="choose__user__group">
    <header class="head__wrap" v-if="logoUrl">
      <img class="head__logo" :src="logoUrl" alt="" />
    </header>

    <div
      class="modal__wrap"
      :style="{ height: logoUrl ? 'calc(100% - 80px)' : '100%' }"
      v-if="!hidden"
    >
      <div class="group__wrap" v-if="serviceList.length && isPermission">
        <div class="service__image">
          <img src="../assets/images/jumpService.png" alt="" />
        </div>
        <div style="padding: 50px 0 20px">
          <div class="service__title">
            {{ $Lan("public", "goToService", "跳转服务") }}
          </div>
          <div
            class="service__name__title portal-font-color-lv3"
            :title="
              `${$Lan('public', 'choose', '选择')}“${$route.query.name}”${$Lan(
                'public',
                'service',
                '的服务'
              )}`
            "
          >
            <span>
              {{ $Lan("public", "choose", "选择") }}
            </span>
            <strong>“{{ $route.query.name }}”</strong>
            <span>
              {{ $Lan("public", "service", "的服务") }}
            </span>
          </div>
          <template v-if="!isMobile">
            <vue-scroll
              :ops="{
                bar: { size: '6px', background: '#d8d8d8' },
                rail: { size: '6px' },
              }"
            >
              <ul class="group__box" v-if="serviceList.length">
                <div
                  class="service__item portal-primary-color-hover-lv1 portal-primary-backgroundcolor-hover-lv5"
                  v-for="(item, index) in serviceList"
                  :key="index"
                  :title="item.modelName"
                  @click="(e) => handleSelect(e, item, index)"
                >
                  <div class="item__wrap">
                    <span
                      :class="{
                        service__name: true,
                        'portal-font-color-lv4': !item.permission,
                      }"
                      :title="item.serviceName"
                    >
                      {{ item.serviceName }}
                    </span>
                    <i
                      :class="{
                        'we-icon-mobile-phone': true,
                        'portal-font-color-lv4': !item.permission,
                      }"
                      style="margin-left: 6px"
                      v-if="item.serviceStation === 1"
                    />
                  </div>
                </div>
              </ul>
            </vue-scroll>
          </template>
          <template v-else>
            <div class="code__wrap">
              <div class="left__code">
                <div class="mobileAppPopover__qrcode" ref="MobileQrcode" />
              </div>
              <div class="right__info">
                <div
                  style="display: flex;margin-bottom: 10px"
                  :class="{ 'no-permission-service': !selectedItem.permission }"
                >
                  <img
                    width="56px"
                    height="56px"
                    :src="getIconUrl(selectedItem) || errorImg"
                    alt=""
                    @error="handleError"
                  />
                </div>
                <span
                  class="right__info__name"
                  :title="selectedItem.serviceName"
                >
                  {{ selectedItem.serviceName }}
                </span>
                <div class="right__info__word">
                  <p>
                    {{ $Lan("public", "mobileService", "此服务为移动端服务") }}
                  </p>
                  <p>
                    {{ $Lan("public", "scanCode", "请手机扫二维码打开") }}
                  </p>
                </div>
              </div>
            </div>
          </template>
        </div>
      </div>
      <div class="no__permission" v-else v-show="!loading">
        <img src="../assets/images/no-permission.png" alt="" />
        <span>
          {{ $Lan("public", "noPermission", "暂无使用权限，请联系管理员") }}
        </span>
      </div>
    </div>
    <div class="disable__matter" v-else>
      <img src="../assets/images/disable-matter.png" alt="" />
      <span>
        {{
          $Lan(
            "public",
            "stopService",
            "此服务事项已经停止使用，如有问题请联系管理员"
          )
        }}
      </span>
    </div>
    <mobile-app-popover
      ref="MobileAppPopover"
      :itemInfo="selectedItem"
      :serviceList="serviceList"
    ></mobile-app-popover>
  </div>
</template>

<script>
import globalObj from "../utils/global";
import vueScroll from "vuescroll/dist/vuescroll-native";
import blackAndWhiteMode from "../libs/mixins/blackAndWhiteMode.js";
import MobileAppPopover from "./MobileAppPopover.vue";
import QRCode from "qrcodejs2";
import { getThemeData } from "../api/service";
export default {
  components: { vueScroll, MobileAppPopover },
  mixins: [blackAndWhiteMode],
  data() {
    return {
      logoUrl: "",  // 彩色logo
      hidden: false, // 事项是否停用
      errorImg: globalObj.ErrorImg,
      itemName: "",
      serviceList: [],
      selectedItem: {},
      isMobile: false,  // 是否是移动端服务
      isPermission: true, // 关联的服务是否有权限
      loading: true,
    };
  },
  methods: {
    getIconUrl(item) {
      const { serviceStation, mobileIconLink, iconLink } = item;
      if (serviceStation === 1) {
        return /^((ht|f)tps?):/.test(mobileIconLink)
          ? mobileIconLink
          : `data:image/png/jpeg;base64,${mobileIconLink}`;
      } else if (serviceStation === 0) {
        return /^((ht|f)tps?):/.test(iconLink)
          ? iconLink
          : `data:image/png/jpeg;base64,${iconLink}`;
      } else {
        return iconLink
          ? /^((ht|f)tps?):/.test(iconLink)
            ? iconLink
            : `data:image/png/jpeg;base64,${iconLink}`
          : /^((ht|f)tps?):/.test(mobileIconLink)
          ? mobileIconLink
          : `data:image/png/jpeg;base64,${mobileIconLink}`;
      }
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    createQrCode() {
      if (!this.selectedItem.mobileAccessUrl) {
        return;
      }
      if (this.qrcode) {
        this.qrcode.makeCode(this.selectedItem.mobileAccessUrl);
      } else {
        this.qrcode = new QRCode(this.$refs.MobileQrcode, {
          text: this.selectedItem.mobileAccessUrl,
          width: 160,
          height: 160,
          colorDark: "#000000",
          colorLight: "#ffffff",
          correctLevel: QRCode.CorrectLevel.L,
        });
      }
    },
    handleSelect(e, item, index) {
      if (item.serviceStation === 1) {
        // 移动端服务
        this.selectedItem = item;
        let parent = e.target;
        let name = parent.className;
        while (!name.includes("service__item")) {
          parent = parent.parentNode;
          if (parent) {
            name = parent.className;
          }
        }
        this.handleMobileOpen(parent);
        return;
      }
      this.$refs.MobileAppPopover.handleMobileclose();
      if (!item.permission) {
        window.Qmsg.warning(
          `${this.$Lan("public", "noPermission", "暂无使用权限，请联系管理员")}`
        );
        return;
      }
      this.selectedInex = index;
      this.selectedItem = item;
      globalObj.openService(this.selectedItem, null, false, true);
    },
    handleMobileOpen(targetEle) {
      this.$refs.MobileAppPopover.show(targetEle);
    },
    async getServiceItemRelService() {
      globalObj.execTemplateMethod(
        "getServiceItemRelService",
        {
          id: this.$route.query.wid,
          langCountry: sessionStorage.getItem("locale") || "zh_CN",
        },
        (res) => {
          // this.$refs.MobileAppPopover.handleMobileclose();
          this.loading = false;
          if (res.errcode === "0") {
            this.itemName = this.$route.query.name;
            const isShow = res.data && res.data.isShow; // 0无需展示办事指南1需要
            const isEnabled = res.data && res.data.isEnabled; // 是否启用
            const isOnlineDeal = Number(this.$route.query.isOnline);
            // 如果停用给出提示
            if (!isEnabled) {
              this.hidden = true;
              return;
            }
            // 如果开启无需办事指南或者点击在线办理，走打开服务逻辑，反之直接跳转服务详情
            if (!isShow || isOnlineDeal) {
              this.serviceList = (res.data && res.data.serviceList) || [];
              if (this.serviceList.length > 1) {
                // this.selectedItem = this.serviceList[0];
                globalObj.visitService(this.$route.query.wid, "serviceItem"); // 除了服务无权限时，其他情况下事项计数+1
              } else if (this.serviceList.length === 1) {
                this.isMobile = this.serviceList[0].serviceStation === 1; // 如果是服务是mobile,创建二维码
                this.isPermission = this.serviceList[0].permission;
                // 先判断是否有权限再判断是否是移动端服务
                if (!this.isPermission) {
                  return;
                } else if (this.isMobile) {
                  this.selectedItem = this.serviceList[0];
                  this.$nextTick(() => {
                    this.createQrCode();
                  });
                } else {
                  globalObj.openService(this.serviceList[0], null, false, true);
                }
              }
            } else {
              globalObj.visitService(this.$route.query.wid, "serviceItem");
              const reg = new RegExp("[%\\/?#&=]", "g");
              let keyword = this.itemName;
              keyword = keyword.replace(reg, (match) =>
                encodeURIComponent(match)
              );
              let url = `itemDetail?wid=${this.$route.query.wid}&name=${keyword}`;
              globalObj.openPage(url, 0, 1);
            }
          }
        }
      );
    },
    async getThemeData() {
      const [res] = await getThemeData();
      if (res.errcode === "0") {
        const data = res.data;
        globalObj.setThemeColor(document, data);
        this.setGrayMode(data.blackAndWhiteMode);
        const templateConfig =
          data && data.showProgrammeEntity
            ? JSON.parse(
                JSON.parse(data.showProgrammeEntity.templateConfig || "{}")
              )
            : {};
        this.logoUrl =
          (templateConfig &&
            templateConfig.configLogo &&
            templateConfig.configLogo.logoUrl) ||
          "";
      }
    },
  },
  async mounted() {
    this.getThemeData();
    // 记录平台访问数据
    await globalObj.getStaticData({
      languageKey: sessionStorage.getItem("locale") || "zh_CN",
      modelKey: "casp-portal",
    });
    await this.getServiceItemRelService();
    globalObj.pageCollect();
    document.title = this.$Lan("public", "goToService", "跳转服务");
  },
};
</script>

<style scoped>
@import "../styles/minireset.css";
body {
  line-height: normal;
}
/*服务无权限加锁样式*/
.no-permission-service {
  position: relative;
  -webkit-filter: grayscale(100%);
  filter: grayscale(100%);
}
.no-permission-service::before,
.no-permission-service::after {
  position: absolute;
  top: 0;
  left: 0;
  content: "";
  height: 100%;
  width: 100%;
  border-radius: 4px;
}
.no-permission-service::before {
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 10;
}
.no-permission-service::after {
  z-index: 20;
  content: "";
  background: url("../assets/images/lock.png") no-repeat center;
  background-size: 24px 24px;
}
.choose__user__group {
  width: 100%;
  height: 100%;
  background-image: url("../assets/images/bg.png");
  background-size: cover;
  background-repeat: no-repeat;
}
.head__wrap {
  padding: 0 calc(50% - 37.5rem);
  /* min-width: 75rem; */
  padding-top: 32px;
}
.head__logo {
  max-width: 300px;
  min-width: 120px;
  width: auto;
  max-height: 80px;
}
.modal__wrap {
  display: flex;
  align-items: center;
  justify-content: center;
}
.group__wrap {
  width: 572px;
  text-align: center;
  background: #fff;
  box-sizing: border-box;
  position: relative;
  border-radius: 4px;
}
.group__wrap::after {
  content: "";
  position: absolute;
  left: calc(50% - 75px);
  top: -65px;
  width: 150px;
  height: 70px;
  background: #fff;
  border-radius: 70px 70px 0 0;
}
.group__wrap .service__image {
  position: absolute;
  left: calc(50% - 34px);
  top: -34px;
  width: 68px;
  height: 68px;
  border-radius: 50%;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #307afb;
}
.group__wrap .service__image img {
  width: 34px;
  height: 34px;
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
.service__title {
  font-size: 20px;
  font-weight: bold;
  color: #262626;
  letter-spacing: 0;
  line-height: 26px;
  margin-bottom: 16px;
}
.service__name__title {
  font-size: 14px;
  color: #8c8c8c;
  letter-spacing: 0;
  line-height: 22px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding: 0 84px;
  margin: 0 auto 16px;
}
.group__box {
  min-height: 250px;
  max-height: 318px;
  padding: 0 76px;
}
.service__item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  height: 48px;
  cursor: pointer;
  box-sizing: border-box;
  background: #f8f8f8;
  box-shadow: inset 0 -1px 0 0 rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  margin-bottom: 12px;
}
.item__wrap {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.item__wrap img {
  border-radius: 8px;
  width: 40px;
  height: 40px;
}
.service__name {
  text-align: center;
  line-height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  max-width: 360px;
  padding: 0 5px 0 12px;
}
.code__wrap {
  width: 420px;
  height: 274px;
  background: #f8f8f8;
  box-shadow: inset 0 -1px 0 0 rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  margin: 0 auto;
  display: flex;
  position: relative;
}
.code__wrap::before {
  content: "";
  position: absolute;
  left: calc(50% - 13px);
  top: -5px;
  width: 25px;
  height: 13px;
  background: #fff;
  border-radius: 0 0 70px 70px;
}
.code__wrap::after {
  content: "";
  position: absolute;
  left: calc(50% - 13px);
  bottom: -5px;
  width: 25px;
  height: 13px;
  background: #fff;
  border-radius: 70px 70px 0 0;
}
.left__code {
  width: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border-right: 1px dashed #d9d9d9;
}
.right__info {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 50%;
}
.right__info__name {
  font-size: 16px;
  font-weight: bold;
  color: #262626;
  letter-spacing: 0;
  line-height: 24px;
  margin-bottom: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  max-width: 100px;
}
.right__info__word {
  font-size: 12px;
  color: #8c8c8c;
  letter-spacing: 0;
  line-height: 20px;
}
.mobileAppPopover__qrcode {
  display: inline-block;
}
.no__permission,
.disable__matter {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.no__permission span,
.disable__matter span {
  margin-top: 30px;
}
.disable__matter {
  height: calc(100% - 96px);
  justify-content: center;
}
</style>
