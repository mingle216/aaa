<template>
  <div ref="MyFavoriteApp" v-loading="loading">
    <div class="manage-con" v-if="showManage !== 0 && apps && apps.length">
      <a
        @click="handleManage"
        class="
          favoriteapp-managebtn
          portal-primary-color-lv1 portal-primary-border-color-lv1
        "
        ><i class="we-icon-s-tools"></i
        >{{ $Lan(lanFunName, "manage", "管理") }}</a
      >
    </div>

    <AutoContainer
      :con-type="containerType.type"
      :con-height="containerType.value"
      :scroll="{ scrollingX: true }"
      v-if="apps && apps.length"
    >
      <div class="favoriteapp" ref="favoriteapp">
        <div>
          <ul
            v-for="pItem in (() => {
              return rows == 0
                ? appsList
                : appsList.filter((v, i) => i + 1 <= rows);
            })()"
            :key="pItem.index"
            :class="[
              linkDisplayRadio == 2 && otherClassIcon ? 'show-direction' : '',
              isUdNoimg ? 'show-direction-noicon' : '',
            ]"
            :style="ul_width"
          >
            <li
              v-for="(item, index) in pItem.children"
              :key="`${item.serviceName}-${index}`"
              :style="{
                'min-width': `${minWidth}px`,
                width: `${100 / columns}%`,
              }"
              @mouseover="
                item.permission ? (item.hover = true) : (item.hover = false)
              "
              @mouseout="item.hover = false"
            >
              <div
                class="favoriteapp-list"
                :class="{
                  'portal-font-color-lv1': item.permission,
                  'portal-font-color-lv4': !item.permission,
                  'portal-primary-backgroundcolor-hover-lv5': true,
                  'portal-primary-color-hover-lv1': item.permission,
                  'favoriteapp-list-hover': true,
                  'favorite-list-nomargin': isRightEndItem(index),
                }"
                :style="{
                  'align-items': otherClassIcon ? 'center' : 'initial',
                  height:
                    linkDisplayRadio == 2 && otherClassIcon ? '128px' : '72px',
                }"
                @click="goLink(item)"
              >
                <!-- 左侧 -->
                <div
                  class="favoriteapp-left"
                  :class="{ 'no-permission-service': !item.permission }"
                  v-if="otherClassIcon"
                >
                  <img
                    :src="item.iconLink"
                    :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                    @error="
                      () => {
                        item.iconLink = errorImg;
                      }
                    "
                  />
                </div>
                <div
                  v-if="!otherClassIcon"
                  :class="{ 'portal-primary-backgroundcolor-lv1': item.hover }"
                  style="
                    width: 6px;
                    height: 6px;
                    margin-right: 6px;
                    border-radius: 50%;
                    margin-top: 9px;
                    background-color: #bfbfbf;
                  "
                ></div>
                <!-- <span class="circle-dot portal-primary-backgroundcolor-lv1" v-if="!otherClassIcon" ></span> -->
                <!-- 中间部分 -->
                <div
                  class="favoriteapp-center"
                  :class="
                    linkDisplayRadio == '2' && otherClassIcon
                      ? ['isWidth100']
                      : ['isFlex1']
                  "
                  style="display: flex; overflow: hidden"
                  :style="{
                    height:
                      otherClassIcon && linkDisplayRadio == '1'
                        ? 'initial'
                        : '48px',
                    'margin-right': linkDisplayRadio == '1' ? '12px' : '0px',
                    'text-align':
                      otherClassIcon && linkDisplayRadio == '2'
                        ? 'center'
                        : 'initial',
                  }"
                >
                  <we-tooltip
                    class="item"
                    effect="dark"
                    :content="item.serviceName"
                    :open-delay="800"
                    placement="top-start"
                  >
                    <v-clamp
                      class="h3_style ellipsis"
                      :class="{
                        'portal-font-color-lv1': item.permission && !item.hover,
                        'portal-font-color-lv4':
                          !item.permission && !item.hover,
                        'portal-primary-color-lv1': item.hover,
                      }"
                      autoresize
                      :max-lines="2"
                    >
                      {{ item.serviceName }}
                    </v-clamp>
                    <!-- <h3
                      class="ellipsis2"
                      :class="{
                        'portal-font-color-lv1': item.permission && !item.hover,
                        'portal-font-color-lv4': !item.permission && !item.hover,
                        'portal-primary-color-lv1': item.hover
                      }"
                    >
                      {{ item.serviceName }}
                    </h3> -->
                  </we-tooltip>
                </div>
                <!-- 右侧 -->
                <div
                  :style="{
                    'padding-top':
                      otherClassIcon && linkDisplayRadio == '1' ? '0px' : '3px',
                  }"
                  class="favoriteapp-right"
                  v-if="userInfo && (linkDisplayRadio == 1 || !otherClassIcon)"
                >
                  <i
                    class="iconfont icon-favorites favorite_font"
                    @click.stop="collectApp(item, index)"
                  ></i>
                </div>
                <div
                  v-if="userInfo && serviceAppraise == '1'"
                  class="app-tag portal-primary-backgroundcolor-lv1"
                  @click.stop="openServiceComment(item)"
                >
                  <i class="we-icon-edit" />
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
      <we-dialog
        :title="$Lan(lanFunName, 'dialogTitle', '我收藏的服务')"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        :append-to-body="true"
        :visible.sync="manageDialogshow"
        :width="dialogWidth"
        @closed="handleClickTrack()"
      >
        <template #title>
          <h3 class="dialog-title portal-font-color-lv1">
            {{ $Lan(lanFunName, "dialogTitle", "我收藏的服务") }}
          </h3>
        </template>

        <div class="dialog-actical">
          <manage-apps
            ref="manage"
            :dataList="apps"
            v-if="manageDialogshow"
            :lan-fun-name="lanFunName"
            :router="router"
          />
        </div>

        <div slot="footer" class="dialog-footer">
          <div class="footer-button">
            <we-button @click="handleClose" class="text-ellipsis">{{
              $Lan(lanFunName, "cancel", "取 消")
            }}</we-button>
            <we-button
              type="primary"
              @click="confirm"
              class="
             text-ellipsis portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1
            "
              >{{ $Lan(lanFunName, "confirm", "确 定") }}</we-button
            >
          </div>
        </div>
      </we-dialog>
      <ServiceCommentModal
        ref="ServiceCommentModal"
        :router="router"
      ></ServiceCommentModal>
    </AutoContainer>
    <EmptyCon
      :tip="$Lan(lanFunName, 'noData', '暂无相关服务')"
      :height="containerType.value"
      v-if="!apps || !apps.length"
    ></EmptyCon>
  </div>
</template>

<script>
import ManageApps from "./components/ManageApps.vue";
import ServiceCommentModal from "./components/ServiceCommentModal";
import { initTrack } from "./mixins/track.js";
export default {
  name: "favoriteapp",
  props: {
    index: Number,
    router: Object,
  },
  components: { ManageApps, ServiceCommentModal },
  mixins: [initTrack],
  data() {
    const { cardWid, cardId } = this.router;
    return {
      lanFunName: this.router.cardId,
      loading: false,
      checked: false,
      userInfo: window.shell.getUserInfo(),
      fontClass: "",
      apps: null,
      appsList: [],
      card: {
        cardWid,
        cardId,
      },
      columns: 4,
      containerType: {
        type: 2,
        value: 300,
      },
      linkDisplayRadio: "1", //列表展现方式 1 左右布局  2 上下布局
      otherClassIcon: "0", //服务图标是否展示  1 显示 0 隐藏
      rows: "0", //展示的行数 0 无限制
      manageDialogshow: false,
      dialogWidth: "825px",
      isUdNoimg: false,
      errorImg: window.shell.ErrorImg,
      throttleTime: 0,
      serviceAppraise: "0",
      showManage: 1,
      ul_width: { width: "100%" },
      minWidth: 96,
    };
  },
  methods: {
    openServiceComment(item) {
      this.$refs.ServiceCommentModal.show({
        wid: item.serviceWid,
        serviceName: item.serviceName,
      });
    },
    isRightEndItem(index) {
      return index + 1 == Math.ceil((index + 1) / this.columns) * this.columns;
    },

    goLink(item) {
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.serviceWid,
      });
      if (!item.permission) {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan(
            this.lanFunName,
            "goLinkMessage",
            "暂无使用权限，请联系管理员"
          ),
        });
        return;
      }
      window.shell.openService(item);
    },
    collectApp(item) {
      this.handleClickTrack();
      this.$confirm(
        this.$Lan(this.lanFunName, "collectAppConfirm", "取消收藏此服务？"),
        this.$Lan(this.lanFunName, "collectAppTip", "提示"),
        {
          confirmButtonText: this.$Lan(this.lanFunName, "confirm", "确 定"),
          cancelButtonText: this.$Lan(this.lanFunName, "cancel", "取消"),
          type: "warning",
          closeOnClickModal: false,
          closeOnPressEscape: false,
          confirmButtonClass:
            "portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1",
          cancelButtonClass:
            "bgHoverWhite portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1 ",
        }
      )
        .then(() => {
          this.handleClickTrack({
            infoType: 0,
            serviceId: item.serviceWid,
            fucType: 3,
          });
          window.shell.collectService({
            operate: 0,
            id: item.serviceWid,
          });
        })
        .catch((action) => {
          if (action === "cancel") {
            this.handleClickTrack(); //点击埋点
          }
        });
    },
    handleUpdateFavorite() {
      clearTimeout(this.throttleTime);
      this.throttleTime = setTimeout(() => {
        this.renderData();
      }, 200);
    },
    renderData() {
      this.loading = true;
      this.execCardMethod(
        {
          ...this.card,
          method: "renderData",
        },
        (data) => {
          if (data && (data.errcode == 200 || data.errcode === "0") && data.data) {
            this.setData(data.data || {});
            this.serviceAppraise = data.data?.config.serviceAppraise;
            this.showManage = data.data?.config.showManage;
          } else {
            this.apps = [];
          }
          this.minWidth = this.otherClassIcon
            ? (() => {
                return this.linkDisplayRadio == 2 ? 44 : 154;
              })()
            : 96;
          this.$nextTick(() => {
            this.ul_width = {
              width:
                this.cardWidth - this.minWidth * this.columns >= 0
                  ? "100%"
                  : this.cardWidth + "px",
            };
            this.loading = false;
            this.loadedEndTrack();
          });
        }
      );
    },
    execCardMethod(params, callback) {
      window.shell.execCardMethod(params, (data) => {
        callback && typeof callback === "function" && callback(data);
      });
    },
    setData(data) {
      const { appList, config = {} } = data;
      const {
        containerType,
        columns,
        linkDisplayRadio,
        otherClassIcon,
        rows,
      } = config;

      this.apps =
        (appList &&
          appList.slice(0).map((v) => {
            return { ...v, throttle: true, wid: v.serviceWid, hover: false };
          })) ||
        [];
      this.columns = columns;
      this.containerType = containerType;
      this.linkDisplayRadio = linkDisplayRadio;
      this.otherClassIcon = otherClassIcon;
      this.rows = rows;
      this.isUdNoimg = !this.otherClassIcon;

      if (this.apps && this.apps.length) {
        //生成实际渲染列表数据
        let arr = [],
          zIndex = Math.ceil(this.apps.length / parseInt(columns));
        for (let i = 0; i < zIndex; i++) {
          arr.push({
            index: i,
            children: [],
          });
          for (let j = 0; j < this.apps.length; j++) {
            if (Math.floor(j / parseInt(columns)) === i) {
              arr[i].children.push(this.apps[j]);
            }
          }
        }
        this.appsList = arr;
      }
    },
    handleManage() {
      this.manageDialogshow = true;
      this.handleClickTrack(); //点击埋点
    },
    confirm() {
      let list = this.$refs.manage.newDataList;
      let delList = [...this.$refs.manage.delList];
      let params = "";
      list.forEach((item) => {
        params = params + item.serviceWid + ",";
      });

      this.execCardMethod(
        {
          ...this.card,
          method: "batchFavoriteService",
          param: { serviceWid: params.substring(0, params.length - 1) },
        },
        (data) => {
          if (data.errcode == 0) {
            window.shell.showMessage({
              type: "success",
              message: this.$Lan(this.lanFunName, "methodMessage", "操作成功"),
            });
            this.manageDialogshow = false;
            if (!delList.length) {
              this.renderData();
            } else {
              delList.forEach((item) => {
                window.shell.emit("collectApp", {
                  operate: 0,
                  id: item.serviceWid,
                  res: { ...item, errcode: "0" },
                });
              });
            }
          } else {
            window.shell.showMessage({
              type: "warning",
              message: data.errmsg,
            });
            this.handleClickTrack(); //点击埋点
          }
        }
      );
    },
    handleClose() {
      this.manageDialogshow = false;
    },
    getCardWidth() {
      let node = this.$refs.MyFavoriteApp;
      if (node && node.offsetWidth) {
        this.cardWidth = node.offsetWidth;
        return;
      }
      let hasClass = false;
      while (node && !hasClass) {
        node = node.parentNode;
        hasClass = node.classList.contains("gateway-card");
      }
      this.cardWidth = hasClass ? node.offsetWidth : 0;
    },
  },
  created() {
    window.shell.on("collectApp", this.handleUpdateFavorite);
  },
  mounted() {
    this.getCardWidth();
    this.renderData();
  },
  beforeDestroy() {
    window.shell.off("collectApp");
  },
};
</script>
<style>
.isFlex1 {
  flex: 1;
}
.isWidth100 {
  width: 100%;
}
.bgHoverWhite:hover {
  background-color: white !important;
}
</style>
<style scoped lang="less">
/deep/ .we-dialog__body {
  padding-right: 6px !important;
  .dialog-actical {
    height: 100% !important;
  }
}
/deep/ .we-dialog__footer {
  padding: 0px 25px 20px;
  .we-button {
    border-radius: 4px;
    font-size: 14px;
    max-width: 383px;
    height: 40px;
  }
  .footer-button {
    display: flex;
    justify-content: flex-end;
    width: 100%;
    flex-wrap: nowrap;
  }
  .text-ellipsis {
    white-space: nowrap; //当列表内容长度抵达容器边界时不转到下一行
    overflow: hidden; //不显示超过对象尺寸的内容，数据无论有多少，都不会换行
    text-overflow: ellipsis; //将被隐藏的那部分用省略号代替
  }
  /deep/ .dialog-footer {
    margin-top: 0 !important;
  }
}
.manage-con {
  height: 32px;
  padding: 10px 10px 0 0;
  .favoriteapp-managebtn {
    height: 22px;
    display: block;
    border-radius: 2px;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    float: right;
    letter-spacing: 1px;
    margin-top: 0;
    > i {
      position: relative;
      margin-top: 2px;
    }
  }
}
.favoriteapp {
  max-width: 100%;
  min-width: 250px;
  padding-top: 10px;

  .show-direction {
    .favoriteapp-list {
      flex-direction: column;
      // align-items: center;
      // justify-content: center;
      // height: 112px;
      width: 100%;
      // min-width: 98px !important;
    }
    .favoriteapp-left {
      margin-bottom: 12px;
      margin-right: 0 !important;
    }
    .favoriteapp-center-noimgwidth {
      width: 100% !important;
    }
    // .favoriteapp-center {
    //   width: 100% !important;
    //   text-align: center;
    //   > h3 {
    //     height: 40px;
    //   }
    // }
  }
  .show-direction-noicon {
    .favoriteapp-list {
      // height: 56px !important;
      // flex-direction: inherit !important;
      .circle-dot {
        width: 4px;
        height: 12px;
        border-radius: 2px;
        margin-right: 6px;
        display: inline-table;
      }
      // .favoriteapp-center {
      //   text-align: left !important;

      //   h3 {
      //     height: 100% !important;
      //     // &:before {
      //     //   content: "•";
      //     //   font-size: 17px;
      //     //   opacity: 0.2;
      //     // }
      //     // &:hover {
      //     //   &:before {
      //     //     opacity: 1;
      //     //   }
      //     // }
      //   }
      // }
    }
  }

  ul {
    // padding-top: 10px;
    display: flex;
    flex-wrap: nowrap;
    width: 100%;
    > li {
      // margin-top: 12px;
      // min-width:224px;
      > .favoriteapp-list-hover {
        &:hover {
          > .favoriteapp-right {
            > .iconfont {
              display: block;
            }
          }
          > .app-tag {
            display: block;
          }
        }
      }
      > .favoriteapp-list {
        display: flex;
        // height: 72px;
        padding: 12px 12px;
        // // width: 250px;
        width: 100%;
        // //  min-width: 224px;
        border-radius: 4px;
        // align-items: center;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        // margin-right: 20px;
        > .favoriteapp-left {
          height: 44px;
          width: 44px;
          flex-shrink: 0;
          margin-right: 12px;
          > img {
            height: 100%;
            width: 100%;
          }
        }

        > .favoriteapp-center {
          // width: calc(100% - 80px);
          margin-right: 12px;
          > .h3_style {
            // line-height: 20px;
            // width: 100%;
            // font-size: 14px;
            // font-family: MicrosoftYaHei;
            font-size: 16px;
            // color: #262626;
            letter-spacing: 0;
            line-height: 24px;
            width: 100%;
          }
        }
        > .favoriteapp-center-noimgwidth {
          width: calc(100% - 24px);
        }
        > .favoriteapp-right {
          width: 16px;
          flex-shrink: 0;
          > .iconfont {
            display: none;
          }
        }
        > .app-tag {
          display: none;
          width: 50px;
          height: 50px;
          position: absolute;
          top: -25px;
          right: -25px;
          transform: rotate(45deg);

          > i {
            position: absolute;
            left: 20px;
            bottom: 4px;
            transform: rotate(-45deg);
            color: #fff;
          }
        }
      }
      .favorite-list-nomargin {
        margin-right: 0;
      }
    }
  }
}
.dialog-actical {
  height: 300px;
}
.dialog-footer {
  margin-top: 24px;
}
.dialog-title {
  font-size: 18px;
  letter-spacing: 0;
  text-align: justify;
  line-height: 22px;
  font-weight: bold;
}
.ellipsis-clamp-2 {
  display: -webkit-box;
  overflow: hidden;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  word-break: break-word;
}
.ellipsis-clamp-1 {
  display: -webkit-box;
  overflow: hidden;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  word-break: break-word;
}

/deep/.we-dialog__header {
  box-shadow: inset 0 -1px 0 0 #f0f0f0;
  padding-bottom: 16px;
}
.ellipsis2 {
  display: -webkit-box;
  overflow: hidden;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
