<template>
  <div v-loading="loading" ref="myfavoritesserviceitems">
    <AutoContainer :con-type="type" :con-height="height" v-if="serviceItemsInfo && serviceItemsInfo.length">
      <div class="manage-con" v-if="config.showManage !== 0">
        <a
                @click="handleManage"
                class="
          favoriteapp-managebtn
          portal-primary-color-lv1 portal-primary-border-color-lv1
        "
        ><i class="we-icon-s-tools" style="margin-right: 4px"></i
        >{{ $Lan(lanFunName, "serviceItemsInfo", "管理") }}</a
        >
      </div>

      <div class="myfavoritesserviceitems">
        <div v-if="serviceItemsInfo && serviceItemsInfo.length">
          <ul
                  v-for="pItem in serviceItemsInfoList"
                  :key="pItem.index"
                  :style="{ width }"
          >
            <li
                    v-for="(item, index) in pItem.children"
                    :key="`${item.appName}-${index}`"
                    :style="li_width"
                    @click="openServiceItem(item)"
                    @mouseover="item.hover = true"
                    @mouseout="item.hover = false"
            >
              <div
                      class="
                myfavoritesserviceitems-content
                portal-font-color-lv1
                portal-primary-backgroundcolor-hover-lv5
                portal-primary-color-hover-lv1
              "
              >
                <!-- 左侧 -->
                <div v-if="configure2" class="myfavoritesserviceitems-left">
                  <img
                          v-if="item.iconLink"
                          :src="item.iconLink"
                          @error="
                      () => {
                        item.iconLink = errorImg;
                      }
                    "
                  />
                </div>
                <div
                        v-else
                        class="myfavoritesserviceitems-left-nopic"
                        :class="{ 'portal-primary-backgroundcolor-lv1': item.hover }"
                        style="
                  width: 6px;
                  height: 6px;
                  border-radius: 50%;
                  margin-top: 9px;
                "
                ></div>
                <!-- 中间内容 -->
                <div
                        :class="{
                    onlyImgT: configure2 && !configure0 && !configure1,
                  }"
                        class="myfavoritesserviceitems-center"
                        :style="{ 'padding-left': configure2 ? '12px' : '6px' }"
                >
                  <we-tooltip
                          class="item"
                          effect="dark"
                          :content="item.itemName"
                          :open-delay="800"
                          placement="bottom-start"
                  >
                    <v-clamp
                            :style="{
                        height:
                          configure2 || showDealOnline ? 'initial' : '48px',
                      }"
                            v-if="!configure0 && !configure1"
                            class="h3_style"
                            autoresize
                            :max-lines="showDealOnline ? 1 : 2"
                    >{{ item.itemName }}</v-clamp
                    >
                    <v-clamp
                            v-else
                            class="h3_style ellipsis"
                            autoresize
                            :max-lines="1"
                    >{{ item.itemName }}</v-clamp
                    >
                    <!-- <h3 :style="{'height': configure2 ? 'initial' : '48px'}" v-if="!configure0 && !configure1" class="ellipsis2">{{ item.itemName }}</h3>
                  <h3 v-else class="ellipsis">{{ item.itemName }}</h3> -->
                  </we-tooltip>
                  <!-- 线上办理or线下办理 -->
                  <template v-if="showDealOnline">
                    <div
                            :class="[
                        'isOnline',
                        item.onlineServiceType === 2
                          ? 'portal-primary-color-lv1 portal-primary-backgroundcolor-lv5 portal-primary-border-color-lv1'
                          : 'disabled-online',
                      ]"
                            v-if="item.onlineServiceType !== 0"
                    >
                      {{ $Lan(lanFunName, "online", "在线办理") }}
                    </div>
                    <div class="isOnline disabled-online" v-else>
                      {{ $Lan(lanFunName, "offline", "线下办理") }}
                    </div>
                  </template>
                  <!-- 服务主题 -->
                  <div
                          v-if="configure0"
                          class="portal-font-color-lv3 content-icon"
                  >
                    <i class="iconfont icon-serviceSubject icon-default"></i
                    ><span class="ellipsis">{{ item.itemCategory }}</span>
                  </div>
                  <!-- 责任部门 -->
                  <div
                          v-if="configure1"
                          class="portal-font-color-lv3 content-icon"
                  >
                    <i class="iconfont icon-responsibleDepartment"></i>
                    <span class="ellipsis">{{ item.itemDept }}</span>
                  </div>
                </div>
                <!-- 右侧 -->
                <div
                        class="myfavoritesserviceitems-right"
                        style="padding-top: 3px"
                >
                  <i
                          class="
                    iconfont
                    icon-favorites
                    favorite_font favorite_font_color
                  "
                          @click.stop="sureCollect(item)"
                  ></i>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
      <we-dialog
              :title="$Lan(lanFunName, 'dialogTitle', '我收藏的事项')"
              :close-on-click-modal="false"
              :close-on-press-escape="false"
              :append-to-body="true"
              :visible.sync="manageDialogshow"
              :width="dialogWidth"
              @close="digClose"
      >
        <template #title>
          <h3 class="dialog-title portal-font-color-lv1">
            {{ $Lan(lanFunName, "dialogTitle", "我收藏的事项") }}
          </h3>
        </template>

        <div class="dialog-actical">
          <manage-apps
                  ref="manage"
                  :router="router"
                  :dataList="serviceItemsInfo"
                  :lan-fun-name="lanFunName"
                  v-if="manageDialogshow"
          />
        </div>

        <div slot="footer" class="dialog-footer">
          <we-button
                  class="
            bgHoverWhite
            portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1
          "
                  @click="manageDialogshowClick"
          >{{ $Lan(lanFunName, "buttonCancle", "取消") }}</we-button
          >
          <we-button
                  type="primary"
                  @click="confirm"
                  class="
            portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1
          "
          >{{ $Lan(lanFunName, "buttonCommit", "确定") }}</we-button
          >
        </div>
      </we-dialog>
    </AutoContainer>
    <EmptyCon
            :tip="$Lan(lanFunName, 'noData', '暂无相关事项')"
            :height="height"
            v-if="!serviceItemsInfo || !serviceItemsInfo.length"
    ></EmptyCon>
  </div>
</template>

<script>
  import ManageApps from "./components/ManageApps.vue";
  export default {
    name: "myfavoritesserviceitems",
    props: {
      index: Number,
      router: Object,
    },
    components: { ManageApps },
    data() {
      const { cardWid, cardId } = this.router;
      return {
        listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
        errorImg: window.shell.ErrorImg,
        card: {
          cardWid,
          cardId,
        },
        lanFunName: this.router.cardId,

        // apps,
        serviceItemsInfo: null,
        serviceItemsInfoList: [],
        config: {},
        favorite: true, //默认是已收藏，若取消收藏则不在该列表
        configure0: false,
        configure1: false,
        configure2: false,
        height: 500,
        type: 3,
        li_width: { width: "25%" },
        fatherWidth: 0,
        loading: false,
        manageDialogshow: false,
        dialogWidth: "825px",
        throttleTime: 0,
        width: "100%",
        showDealOnline: 0, // 是否显示在线办理标签
      };
    },
    created() {
      // this.initdata();
      // 初始化时，订阅服务收藏消息
      window.shell.on("collectServiceItem", this.process);
    },
    mounted() {
      this.getCardWidth();
      this.initdata("init");
    },
    methods: {
      digClose() {
        window.minosStataCollect.collect({
          actionType: 0,
          functionType: 1,
          actionParams: {
            pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
            pageName: window.shell.getPageData().pageInfoEntity.pageName,
            cardWid: this.router.cardWid,
            cardId: this.router.cardId,
            cardName: this.router.cardName,
          },
          startTime: new Date().getTime(),
        });
      },
      manageDialogshowClick() {
        this.manageDialogshow = false;
      },
      handleManage() {
        window.minosStataCollect.collect({
          actionType: 0,
          functionType: 1,
          actionParams: {
            pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
            pageName: window.shell.getPageData().pageInfoEntity.pageName,
            cardWid: this.router.cardWid,
            cardId: this.router.cardId,
            cardName: this.router.cardName,
          },
          startTime: new Date().getTime(),
        });
        this.manageDialogshow = true;
        setTimeout(() => {
          this.$refs.manage.removeData = [];
        }, 500);
      },
      confirm() {
        let list = this.$refs.manage.newDataList;
        let params = "";
        list.forEach((item) => {
          params = params + item.itemWid + ",";
        });

        this.execCardMethod(
                {
                  cardId: "SYS_CARD_MYFAVORITESSERVICEITEM",
                  cardWid: "47388428666648474",
                  method: "sort",
                  param: { itemIds: params.substring(0, params.length - 1) },
                },
                (data) => {
                  if (data.errcode == 0) {
                    window.shell.showMessage({
                      type: "success",
                      message: this.$Lan(this.lanFunName, "message", "操作成功"),
                    });
                    this.manageDialogshow = false;
                    let removeData = this.$refs.manage.removeData;
                    if (!removeData.length) {
                      this.initdata();
                    } else {
                      removeData.forEach((item) => {
                        window.shell.emit("collectServiceItem", {
                          id: item.itemWid,
                          operate: 0,
                          cardId: this.router.cardId,
                          ...item,
                        });
                      });
                    }

                    this.$refs.manage.removeData = [];
                  } else {
                    window.shell.showMessage({
                      type: "warning",
                      message: data.errmsg,
                    });
                  }
                }
        );
      },
      execCardMethod(params, callback) {
        window.shell.execCardMethod(params, (data) => {
          callback && typeof callback === "function" && callback(data);
        });
      },

      sureCollect(item) {
        window.minosStataCollect.collect({
          actionType: 0,
          functionType: 1,
          actionParams: {
            pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
            pageName: window.shell.getPageData().pageInfoEntity.pageName,
            cardWid: this.router.cardWid,
            cardId: this.router.cardId,
            cardName: this.router.cardName,
          },
          startTime: new Date().getTime(),
        });
        this.$confirm(
                this.$Lan(this.lanFunName, "confirmContent", "取消收藏此服务事项？"),
                this.$Lan(this.lanFunName, "confirmTip", "提示"),
                {
                  confirmButtonText: this.$Lan(this.lanFunName, "buttonCommit", "确定"),
                  cancelButtonText: this.$Lan(this.lanFunName, "buttonCancle", "取消"),
                  type: "warning",
                  closeOnClickModal: false,
                  closeOnPressEscape: false,
                  confirmButtonClass:
                          "portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1",
                  cancelButtonClass:
                          "bgHoverWhite portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1",
                }
        )
                .then(() => {
                  this.isOrNotCollect(item);
                })
                .catch(() => {
                  window.minosStataCollect.collect({
                    actionType: 0,
                    functionType: 1,
                    actionParams: {
                      pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
                      pageName: window.shell.getPageData().pageInfoEntity.pageName,
                      cardWid: this.router.cardWid,
                      cardId: this.router.cardId,
                      cardName: this.router.cardName,
                    },
                    startTime: new Date().getTime(),
                  });
                });
      },
      //取消收藏
      async isOrNotCollect(item) {
        if (item.throttle) {
          window.minosStataCollect.collect({
            actionType: 0,
            functionType: 1,
            actionParams: {
              pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
              pageName: window.shell.getPageData().pageInfoEntity.pageName,
              cardWid: this.router.cardWid,
              cardId: this.router.cardId,
              cardName: this.router.cardName,
              extraInfo: {
                infoType: 1,
                itemId: item.itemWid,
                fucType: 3,
              },
            },
            startTime: new Date().getTime(),
          });
          item.throttle = false;
          await window.shell.collectServiceItem({
            id: item.itemWid,
            operate: 0,
            cardId: this.router.cardId,
          });
          setTimeout(() => {
            item.throttle = true;
          }, 300);
          this.initdata();
        }
      },

      process() {
        clearTimeout(this.throttleTime);
        this.throttleTime = setTimeout(() => {
          this.initdata();
        }, 200);
      },
      initdata(isInit) {
        if (isInit && isInit === "init") {
          window.minosStataCollect.loadStart({
            listId: this.listId,
            actionType: 3,
            functionType: 1,
            actionParams: {
              pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
              pageName: window.shell.getPageData().pageInfoEntity.pageName,
              cardWid: this.router.cardWid,
              cardId: this.router.cardId,
              cardName: this.router.cardName,
              extraInfo: "",
            },
            startTime: new Date().getTime(),
          });
        }
        this.loading = true;
        window.shell
                .execCardMethod({ cardId: "SYS_CARD_MYFAVORITESSERVICEITEM",cardWid: "47388428666648474", method: "renderData" })
                .then((data) => {
                  if (isInit && isInit === "init") {
                    window.minosStataCollect.loadEnd({
                      listId: this.listId,
                      endTime: new Date().getTime(),
                    });
                  }
                  let res = data.data;
                  const { serviceItemsInfo, config } = res;
                  const { columns, containerType, showItem, showDealOnline } = config;
                  this.showDealOnline = showDealOnline ?? 0;
                  this.serviceItemsInfo =
                          (serviceItemsInfo &&
                                  serviceItemsInfo.map((v) => {
                                    v.iconLink || (v.iconLink = require("./assets/no-data.png"));
                                    return {
                                      ...v,
                                      throttle: true,
                                      wid: v.itemWid,
                                      name: v.itemName,
                                      hover: false,
                                    };
                                  })) ||
                          [];
                  this.config = config;
                  this.configure0 = showItem.includes("service");
                  this.configure1 = showItem.includes("dept");
                  this.configure2 = showItem.includes("icon");
                  // 自定义高度
                  this.height = containerType.value;
                  if (serviceItemsInfo && serviceItemsInfo.length) {
                    this.type = containerType.type;
                    this.height = containerType.value;
                    //生成实际渲染列表数据
                    let arr = [],
                            zIndex = Math.ceil(
                                    this.serviceItemsInfo.length / parseInt(columns)
                            );
                    for (let i = 0; i < zIndex; i++) {
                      arr.push({
                        index: i,
                        children: [],
                      });
                      for (let j = 0; j < this.serviceItemsInfo.length; j++) {
                        if (Math.floor(j / parseInt(columns)) === i) {
                          arr[i].children.push(this.serviceItemsInfo[j]);
                        }
                      }
                    }
                    this.serviceItemsInfoList = arr;
                    this.width =
                            this.cardWidth - 154 * columns >= 0
                                    ? "100%"
                                    : `${154 * columns}px`;
                    this.li_width = {
                      width: `${100 / parseInt(columns)}%`,
                      "min-width": this.configure2 ? "154px" : "96px",
                    };
                  }
                  this.$nextTick(() => {
                    this.loading = false;
                  });
                })
                .catch(() => {
                  if (isInit && isInit === "init") {
                    window.minosStataCollect.loadEnd({
                      listId: this.listId,
                      endTime: new Date().getTime(),
                    });
                  }
                });
      },
      openServiceItem(item) {
        window.minosStataCollect.collect({
          actionType: 0,
          functionType: 1,
          actionParams: {
            pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
            pageName: window.shell.getPageData().pageInfoEntity.pageName,
            cardWid: this.router.cardWid,
            cardId: this.router.cardId,
            cardName: this.router.cardName,
            extraInfo: {
              infoType: 1,
              itemId: item.itemWid,
            },
          },
          startTime: new Date().getTime(),
        });
        window.shell.openServiceItem(item);
      },
      getCardWidth() {
        let node = this.$refs.myfavoritesserviceitems;
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
    watch: {},

    beforeDestroy() {
      window.shell.off("collectServiceItem");
    },
  };
</script>
<style>
  .favorite_font_color {
    color: #ffbc00;
  }
  .bgHoverWhite:hover {
    background-color: white !important;
  }
</style>
<style scoped lang="less">
  /deep/ .we-dialog__header {
    padding: 0 !important;
    display: flex;
    align-items: center;
    padding-left: 20px !important;
  }
  /deep/ .we-dialog__body {
    padding-right: 6px !important;
    .dialog-actical {
      height: 100% !important;
    }
  }
  /deep/ .dialog-footer {
    margin-top: 0 !important;
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
      letter-spacing: 1px;
      float: right;
      margin-top: 0;
    }
  }

  .portal-font-color-lv1 {
    color: #102645;
  }
  .ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .myfavoritesserviceitems {
    max-width: 100%;
    min-width: 360px;
    padding-top: 10px;

    ul {
      display: flex;
      flex-wrap: nowrap;
      > li {
        // margin-top: 12px;
        min-width: 250px;
        .myfavoritesserviceitems-content {
          display: flex;
          height: auto;
          padding: 12px;
          width: 100%;
          border-radius: 4px;
          cursor: pointer;
          // align-items: center;
          &:hover {
            > .myfavoritesserviceitems-right {
              > .iconfont {
                display: block;
                font-size: 14px;
              }
            }
          }
          > .myfavoritesserviceitems-left {
            height: 44px;
            width: 44px;
            flex-shrink: 0;
            > img {
              height: 100%;
              width: 100%;
            }
          }
          > .myfavoritesserviceitems-left-nopic {
            border-radius: 2px;
            width: 4px;
            height: 12px;
            background-color: #bfbfbf;
          }
          > .myfavoritesserviceitems-center {
            // width: calc(100% - 68px);
            flex-direction: column;
            justify-content: center;
            align-items: flex-start;
            flex: 1;
            padding: 0 12px;
            overflow: hidden;
            .isOnline {
              max-width: 56px;
              height: 20px;
              padding: 0 2px;
              font-size: 12px;
              text-align: center;
              line-height: 1.5;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              border: 1px solid #979797;
              border-radius: 2px;
            }
            .disabled-online {
              color: #8c8c8c;
              background: #f9f9f9;
            }
            > .h3_style {
              line-height: 24px;
              width: 100%;
              // max-width: 100px;
              // margin-bottom: 4px;
              // font-family: MicrosoftYaHei;
              font-size: 16px;
              // color: #262626;
              letter-spacing: 0;
              word-break: break-all;
            }
            > h3.h3_style {
              margin: 4px 0;
            }
            > p {
              font-size: 12px;
              color: #707d8f;
              line-height: 17px;
              width: 100%;
            }
            i {
              font-size: 11px;
              // color: #707d8f;
            }
            span {
              // display: inline-block;
              // width: 100px;
              // color: #707d8f;
              // line-height: 17px;
              padding-left: 6px;
              // font-size: 12px;
            }
            > .content-icon {
              // line-height: 17px;
              // display: flex;
              // align-items: center;
              // margin-bottom: 4px;
              display: flex;
              align-items: center;
              // font-family: MicrosoftYaHei;
              font-size: 14px;
              color: #8c8c8c;
              letter-spacing: 0;
              line-height: 22px;
            }
          }
          > .myfavoritesserviceitems-right {
            width: 16px;
            flex-shrink: 0;
            // margin-left: 41px;
            > .iconfont {
              display: none;
            }
          }
        }
        .is_center {
          align-items: center;
        }
      }
    }
  }
  /deep/.we-dialog__header {
    box-shadow: inset 0 -1px 0 0 #f0f0f0;
    padding-bottom: 16px;
  }
  .dialog-actical {
    height: 338px;
  }
  .dialog-footer {
    // margin-top: 4px;
  }
  .dialog-title {
    font-size: 18px;
    letter-spacing: 0;
    text-align: justify;
    line-height: 22px;
    font-weight: bold;
  }
  .ellipsis2 {
    display: -webkit-box;
    overflow: hidden;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  .onlyImgT {
    display: flex;
    align-items: center;
  }
</style>
