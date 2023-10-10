<template>
  <we-popup
    round
    position="bottom"
    :value="visible"
    :style="{ width: '100%', height: '90%' }"
    @click-overlay="close"
    get-container="body"
  >
    <div class="modal_title" v-if="showTitle">
      <span>
        {{ $Lan('SYS_TEMPLATE_OFFICIAL_h5', 'onlineService', '在线办理') }}
      </span>
      <we-icon name="cross" color="#9FA8B5" @click="close" />
    </div>
    <div class="modal_close" v-else>
      <we-icon name="cross" color="#9FA8B5" @click="close" />
    </div>
    <div class="modal__wrap" v-if="!pcOpen">
      <div
        class="service__item"
        v-for="(item, index) in serviceList"
        :key="index"
        @click="handleSelect(item, index)"
      >
        <div class="item__wrap">
          <div
            style="display:flex"
            :class="{ 'no-permission-service': !item.permission }"
          >
            <img
              style="width:40px;height:40px"
              :src="getIconUrl(item) || errorImg"
              alt=""
              @error="handleError"
            />
          </div>
          <span
            :class="{
              service__name: true,
            }"
            :title="item.serviceName"
          >
            {{ item.serviceName }}
          </span>
          <img
            style="width:14px;height:16px;border-radius:0"
            src="../assets/images/icon-pc.png"
            alt=""
            v-if="item.serviceStation === 0"
          />
        </div>
        <we-icon name="arrow" color="#CCD4DC" />
      </div>
    </div>
    <div class="modal__wrap pc_service" v-else>
      <img
        style="width:50px;height:50px"
        :src="getIconUrl(selectedItem)"
        alt=""
        @error="handleError"
      />
      <p class="pc_service_name">{{ selectedItem.serviceName }}</p>
      <p class="pc_tips_top clamp">
        {{ $Lan('SYS_TEMPLATE_OFFICIAL_h5', 'pcService', '该服务为PC服务') }}
      </p>
      <p class="pc_tips_bottom clamp">
        {{ $Lan('SYS_TEMPLATE_OFFICIAL_h5', 'openInMobile', '是否在移动端打开?') }}
      </p>
      <we-button
        class="open_btn portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
        type="info"
        @click.native="handlePcOk"
        style="width:100%"
      >
        {{ $Lan('SYS_TEMPLATE_OFFICIAL_h5', 'open', '打开') }}
      </we-button>
    </div>
  </we-popup>
</template>

<script>
export default {
  data() {
    return {
      errorImg: window.shell.ErrorImg,
      visible: false,
      serviceList: [],
      itemName: "",
      selectedItem: {},
      pcOpen: false,
      showTitle: true,
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
        return mobileIconLink
          ? mobileIconLink.includes("http:")
            ? mobileIconLink
            : `data:image/png/jpeg;base64,${mobileIconLink}`
          : iconLink && iconLink.includes("http:")
          ? iconLink
          : `data:image/png/jpeg;base64,${iconLink}`;
      }
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    show(serviceItem, isOnlineDeal) {
      this.showTitle = true;
      window.shell.execTemplateMethod(
        "getServiceItemRelService",
        {
          id: serviceItem.wid,
          langCountry: this.$i18n.locale || "zh_CN",
        },
        (res) => {
          if (res.errcode === "0") {
            this.itemName = serviceItem.name;
            this.serviceList = (res.data && res.data.serviceList) || [];
            const isShow = res.data && res.data.isShow; // 0无需展示办事指南1需要
            const isEnabled = res.data && res.data.isEnabled; // 是否启用
            // 如果停用给出提示
            if (!isEnabled) {
              window.shell.showMessage({
                type: "warning",
                message: `${this.$Lan("public", "stopService", "此服务事项已经停止使用，如有问题请联系管理员")}`,
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
                const isPc = this.serviceList[0].serviceStation === 0; // 如果是服务是pc,跳转提示页面
                const isPermission = this.serviceList[0].permission;
                if (!isPermission) {
                  window.shell.showMessage({
                    type: "warning",
                    message: `${this.$Lan("public", "noPermission", "暂无使用权限，请联系管理员")}`,
                  });
                } else if (isPc) {
                  this.handlePcOpen(this.serviceList[0]);
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
                  message: `${this.$Lan("public", "noPermission", "暂无使用权限，请联系管理员")}`,
                });
              }
            } else {
              window.shell.visitService(serviceItem.wid, "serviceItem");
              const reg = new RegExp("[%\\/?#&=]", "g");
              let keyword = this.itemName;
              keyword = keyword.replace(reg, (match) =>
                encodeURIComponent(match)
              );
              if (window.shell.isMobile()) {
                window.shell.openPage(
                  `itemDetail?wid=${serviceItem.wid}&name=${keyword}`,
                  0,
                  1
                );
              } else {
                window.shell.openPage(
                  `itemDetail?wid=${serviceItem.wid}&name=${keyword}`,
                  1,
                  1
                );
              }
            }
          }
        }
      );
    },
    close() {
      this.visible = false;
      this.itemName = "";
      this.selectedItem = {};
      this.serviceList = [];
      this.pcOpen = false;
      console.log('在线办理弹窗关闭')
    },
    handleOk() {
      window.shell.openService(this.selectedItem);
      this.close();
    },
    handleSelect(item) {
      if (!item.permission) {
        window.shell.showMessage({
          type: "warning",
          message: `${this.$Lan("public", "noPermission", "暂无使用权限，请联系管理员")}`,
        });
        return;
      }
      // 如果没有权限或者是pc端服务，提示无权限
      if (item.serviceStation === 0) {
        // 移动端服务
        this.handlePcOpen(item);
        return;
      }
      this.selectedItem = item;
      this.handleOk();
    },
    handlePcOpen(item) {
      this.visible = true;
      this.selectedItem = item;
      this.pcOpen = true;
    },
    showPcModal(item) {
      this.showTitle = false;
      this.handlePcOpen(item);
    },
    handlePcOk() {
      console.log('pc服务跳转')
      window.shell.openService(this.selectedItem, null, true);
      this.close();
    },
  },
};
</script>

<style lang="less" scoped>
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
/deep/.we-popup--bottom {
  border-radius: 8px 8px 0 0;
}
.modal_title, .modal_close {
  height: 56px;
  box-shadow: inset 0 0 0 1px #f1f2f4;
  font-size: 18px;
  color: #102645;
  letter-spacing: 0;
  line-height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 17px;
}
.modal_close {
  box-shadow: none;
  justify-content: flex-end;
}
.modal__wrap {
  max-height: calc(100% - 56px);
  overflow: auto;
  padding: 10px 0;
  color: #102645;
}
.pc_service {
  text-align: center;
  padding: 16% 50px 25%;
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  justify-content: center;
  img {
    width: 50px;
    height: 50px;
  }
  .pc_service_name {
    font-size: 16px;
    font-weight: bold;
    color: #102645;
    margin: 10px 0 16px;
    max-width: 246px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .pc_tips_top,
  .pc_tips_bottom {
    font-size: 14px;
    color: #707d8f;
    letter-spacing: 0;
    text-align: justify;
    line-height: 18px;
    text-align: center;
  }
  .pc_tips_bottom {
    margin: 6px 0 28px;
  }
}
.mt-20 {
  margin-top: 20px;
}
.service__item {
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 11px 17px;
  background: white;
  height: 58px;
  box-sizing: border-box;
}
.item__wrap {
  width: calc(100% - 20px);
  display: flex;
  align-items: center;
}
.item__wrap img {
  border-radius: 8px;
  width: 36px;
  height: 36px;
}
.service__name {
  max-width: calc(100% - 65px);
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
.open_btn {
  /deep/.we-button__text {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
.clamp {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
