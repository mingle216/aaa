<template>
  <div>
    <we-dialog
      :title="$Lan('SYS_TEMPLATE_OFFICIAL', 'goToService', '跳转服务')"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="visible"
      top="0"
      @close="close"
      custom-class="service-dialog"
      width="450px"
    >
      <vue-scroll :barKeepShow="true" ref="vs" @vshandle-scroll="handleScroll">
        <div class="modal__wrap portal-font-color-lv1">
          <p class="sub__title portal-font-color-lv3">
            <span>
              {{ $Lan("SYS_TEMPLATE_OFFICIAL", "choose", "选择") }}
            </span>
            <strong>“{{ itemName }}”</strong>
            <span>
              {{ $Lan("SYS_TEMPLATE_OFFICIAL", "service", "的服务") }}
            </span>
          </p>
          <div
            class="service__item portal-primary-color-hover-lv1 portal-primary-backgroundcolor-hover-lv5"
            v-for="(item, index) in serviceList"
            :key="index"
            @mouseenter="handleMouseEnter(index)"
            @mouseleave="handleMouseLeave()"
            @click="(e) => handleSelect(e, item, index)"
          >
            <div class="item__wrap">
              <div
                style="display: flex"
                :class="{ 'no-permission-service': !item.permission }"
              >
                <img
                  width="40px"
                  height="40px"
                  :src="getIconUrl(item) || errorImg"
                  alt=""
                  @error="handleError"
                />
              </div>
              <span
                :class="{
                  service__name: true,
                  'portal-font-color-lv4': !item.permission,
                }"
                :title="item.serviceName"
              >
                {{ item.serviceName }}
                <i
                  class="we-icon-mobile-phone"
                  style="margin-left: 6px"
                  v-if="item.serviceStation === 1"
                ></i>
              </span>
            </div>
          </div>
        </div>
      </vue-scroll>

      <div slot="footer" class="dialog__footer"></div>
    </we-dialog>
    <mobile-app-popover
      ref="MobileAppPopover"
      :itemInfo="selectedItem"
      :serviceList="serviceList"
    ></mobile-app-popover>
  </div>
</template>

<script>
import MobileAppPopover from "./MobileAppPopover.vue";
export default {
  components: {
    MobileAppPopover,
  },
  data() {
    return {
      errorImg: window.shell.ErrorImg,
      visible: false,
      serviceList: [],
      itemName: "",
      selectedInex: 0,
      emptyCheck: -1,
      selectedItem: {},
    };
  },
  methods: {
    getIconUrl(item) {
      const { serviceStation, mobileIconLink, iconLink } = item;
      if (serviceStation === 1) {
        return mobileIconLink && mobileIconLink.includes("http:")
          ? mobileIconLink
          : `data:image/png/jpeg;base64,${mobileIconLink}`;
      } else if (serviceStation === 0) {
        return iconLink && iconLink.includes("http:")
          ? iconLink
          : `data:image/png/jpeg;base64,${iconLink}`;
      } else {
        return iconLink
          ? iconLink.includes("http:")
            ? iconLink
            : `data:image/png/jpeg;base64,${iconLink}`
          : mobileIconLink && mobileIconLink.includes("http:")
          ? mobileIconLink
          : `data:image/png/jpeg;base64,${mobileIconLink}`;
      }
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    show(serviceItem, isOnlineDeal, targetEle) {
      window.shell.execTemplateMethod(
        "getServiceItemRelService",
        {
          id: serviceItem.wid,
          langCountry: this.$i18n.locale || "zh_CN",
        },
        (res) => {
          this.$refs.MobileAppPopover.handleMobileclose();
          if (res.errcode === "0") {
            this.itemName = serviceItem.name;
            this.serviceList = (res.data && res.data.serviceList) || [];
            const isShow = res.data && res.data.isShow; // 0无需展示办事指南1需要
            const isEnabled = res.data && res.data.isEnabled; // 是否启用
            // 如果停用给出提示
            if (!isEnabled) {
              window.shell.showMessage({
                type: "warning",
                message: `${this.$Lan(
                  "public",
                  "stopService",
                  "此服务事项已经停止使用，如有问题请联系管理员"
                )}`,
              });
              return;
            }
            // 如果开启无需办事指南或者点击在线办理，走打开服务逻辑，反之直接跳转服务详情
            if (!isShow || isOnlineDeal) {
              if (this.serviceList.length > 1) {
                this.visible = true;
                this.selectedItem = this.serviceList[0];
                window.shell.visitService(serviceItem.wid, "serviceItem"); // 除了服务无权限时，其他情况下事项计数+1
              } else if (this.serviceList.length === 1) {
                const isMobile = this.serviceList[0].serviceStation === 1; // 如果是服务是mobile,提示无使用权限
                const isPermission = this.serviceList[0].permission;
                // 先判断是否有权限再判断是否是移动端服务
                if (!isPermission) {
                  window.shell.showMessage({
                    type: "warning",
                    message: `${this.$Lan(
                      "public",
                      "noPermission",
                      "暂无使用权限，请联系管理员"
                    )}`,
                  });
                } else if (isMobile) {
                  this.selectedItem = this.serviceList[0];
                  console.log(targetEle);
                  this.handleMobileOpen(targetEle);
                } else {
                  window.shell.openService(this.serviceList[0], (res) => {
                    if (res.errcode === "0") {
                      window.shell.visitService(serviceItem.wid, "serviceItem");
                    }
                  });
                }
              } else {
                window.shell.showMessage({
                  type: "warning",
                  message: `${this.$Lan(
                    "public",
                    "noPermission",
                    "暂无使用权限，请联系管理员"
                  )}`,
                });
              }
            } else {
              window.shell.visitService(serviceItem.wid, "serviceItem");
              const reg = new RegExp("[%\\/?#&=]", "g");
              let keyword = this.itemName;
              keyword = keyword.replace(reg, (match) =>
                encodeURIComponent(match)
              );
              let url = `itemDetail?wid=${serviceItem.wid}&name=${keyword}`;
              if (window.shell.isSafari() || window.shell.isFireFox()) {
                let origin = location.origin;
                serviceItem.pcAccessUrl = `${origin}/#${url}`;
                window.shell.emit("safari-open-serve", serviceItem);
              } else {
                window.shell.openPage(url, 1, 1);
              }
            }
          }
        }
      );
    },
    close() {
      this.visible = false;
      this.itemName = "";
      this.selectedInex = 0;
      this.emptyCheck = -1;
      this.selectedItem = {};
      this.$refs.vs.scrollTo({ y: 0 });
    },
    handleOk() {
      window.shell.openService(this.selectedItem);
      this.close();
    },
    handleMouseEnter(index) {
      this.emptyCheck = index;
    },
    handleMouseLeave() {
      this.emptyCheck = -1;
    },
    handleSelect(e, item, index) {
      // 如果没有权限或者是移动端服务，提示无权限
      // console.log(!item.permission || item.serviceStation === 1)
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
        window.shell.showMessage({
          type: "warning",
          message: `${this.$Lan(
            "public",
            "noPermission",
            "暂无使用权限，请联系管理员"
          )}`,
        });
        return;
      }
      this.selectedInex = index;
      this.selectedItem = item;
      this.handleOk();
    },
    handleMobileOpen(targetEle) {
      this.$refs.MobileAppPopover.show(targetEle);
    },
    handleScroll() {
      this.$refs.MobileAppPopover.handleScroll();
    },
  },
};
</script>

<style lang="less" scoped>
/deep/.we-dialog {
  top: 50%;
  left: 50%;
  margin: 0;
  transform: translate(-40%, -50%);
  .we-dialog__title {
    font-size: 18px;
  }
  .we-dialog__body {
    padding: 0;
  }
  .we-dialog__header {
    // border-bottom: 1px solid #dee2e6;
    padding: 18px 20px;
  }
  .we-dialog__wrapper {
    overflow: hidden;
  }
}
.modal__wrap {
  max-height: 350px;
  padding: 16px 0;
}
.modal__wrap__mobile {
  padding: 16px;
}
.mt-20 {
  margin-top: 20px;
}
.sub__title {
  padding: 0 20px 15px;
}
.service__item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  background: white;
  height: 60px;
  cursor: pointer;
  box-sizing: border-box;
}
.item__wrap {
  width: calc(100% - 20px);
  display: flex;
  align-items: center;
}
.item__wrap img {
  border-radius: 8px;
  width: 40px;
  height: 40px;
}
.service__name {
  width: calc(100% - 40px);
  line-height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  padding: 0 5px 0 12px;
}
.check__wrap {
  width: 18px;
}
.choose-service-icon-empty {
  background: #ffffff;
  border-radius: 9px;
  width: 17px;
  height: 17px;
  border: 1px solid #d6dade;
}
.dialog__footer {
  display: flex;
  justify-content: flex-end;
}
.btn {
  border-radius: 4px;
  color: #ffffff;
  letter-spacing: 0;
  font-size: 14px;
  width: 84px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  cursor: pointer;
}
.cancel__btn {
  background: #ffffff;
  border: 1px solid #d6dade;
  color: #637389;
  margin-right: 10px;
}
.confirm__btn {
  color: #ffffff;
}
</style>
