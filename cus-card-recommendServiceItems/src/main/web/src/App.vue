<!-- <i18n>
{
  "en_US": {
    "nodata": "No Recommended  ServiceItems"
  },
  "zh_CN": {
    "nodata": "暂无相关事项"
  }
}
</i18n> -->
<template>
  <div v-loading="loading" ref="recommendserviceitems">
    <AutoContainer
      :con-type="type"
      :con-height="height"
      v-if="serviceItemsInfo && serviceItemsInfo.length"
    >
      <div class="recommendserviceitems">
        <div>
          <ul v-for="pItem in serviceItemsInfoList" :key="pItem.index" :style="ul_width">
            <li
              v-for="(item, index) in pItem.children"
              :key="`${item.appName}-${index}`"
              :style="li_width"
              @click="openServiceItem(item)"
              @mouseover="item.hover = true"
              @mouseout="item.hover = false"
            >
              <div
                class="recommendserviceitems-content portal-font-color-lv1 portal-primary-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1"
              >
                <!-- 左侧 -->
                <div v-if="configure2" class="recommendserviceitems-left">
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
                  class="recommendserviceitems-left-nopic"
                  :class="{ 'portal-primary-backgroundcolor-lv1': item.hover }"
                  style="width: 6px; height: 6px; border-radius: 50%; margin-top: 9px"
                ></div>
                <!-- 中间内容 -->
                <div
                  :class="{
                    onlyImgT: configure2 && !configure0 && !configure1,
                  }"
                  class="recommendserviceitems-center"
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
                        height: configure2 || showDealOnline ? 'initial' : '48px',
                      }"
                      v-if="!configure0 && !configure1"
                      class="h3_style ellipsis"
                      autoresize
                      :max-lines="2"
                      >{{ item.itemName }}</v-clamp
                    >
                    <v-clamp v-else class="h3_style ellipsis" autoresize :max-lines="1">{{
                      item.itemName
                    }}</v-clamp>
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
                  <div v-if="configure0" class="portal-font-color-lv4 content-icon">
                    <i class="iconfont icon-serviceSubject icon-default"></i
                    ><span class="ellipsis">{{ item.itemCategory }}</span>
                  </div>
                  <!-- 责任部门 -->
                  <div v-if="configure1" class="portal-font-color-lv4 content-icon">
                    <i class="iconfont icon-responsibleDepartment"></i>
                    <span class="ellipsis">{{ item.itemDept }}</span>
                  </div>
                  <!-- 访问量 -->
                  <div v-if="configure5" class="portal-font-color-lv4 content-icon">
                    <i class="iconfont icon-views"></i>
                    <span class="ellipsis">{{ formatNum(item.visitCount) }}{{ $Lan(lanFunName, "visitCount", "次浏览") }}</span>
                  </div>
                </div>
                <!-- 右侧 -->
                <div
                  v-if="currentUser"
                  class="recommendserviceitems-right"
                  style="padding-top: 3px"
                >
                  <i
                    class="iconfont icon-favorites"
                    :class="[
                      item.favorite
                        ? 'favorite_font favorite_font_color'
                        : 'unfavorite_font',
                    ]"
                    @click.stop="isornotcollect(item, index)"
                  ></i>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </AutoContainer>
    <EmptyCon
      :tip="$Lan(lanFunName, 'noData', '暂无相关事项')"
      :height="height"
      v-if="!serviceItemsInfo || !serviceItemsInfo.length"
    ></EmptyCon>
  </div>
</template>

<script>
export default {
  name: "recommendserviceitems",
  props: {
    index: Number,
    router: Object,
  },
  data() {
    const { cardWid, cardId } = this.router;
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      card: {
        cardWid,
        cardId,
      },
      lanFunName: this.router.cardId,
      checked: false,
      fontClass: "",
      // apps,
      serviceItemsInfo: null,
      serviceItemsInfoList: [],
      config: [],
      configure0: false,
      configure1: false,
      configure2: false,
      configure5: false,
      height: 300,
      type: 3,
      currentUser: null,
      ul_width: { width: "100%" },
      li_width: { width: "25%" },
      loading: false,
      errorImg: require("./assets/default.png"),
      showDealOnline: 0,
    };
  },
  created() {
    window.shell.on("check-card", (val) => {
      this.checked = val;
    });
    this.initdata("init");
    this.currentUser = window.shell.getUserInfo();
    // 初始化时，订阅服务收藏消息
    window.shell.on("collectServiceItem", this.process);
  },
  methods: {
    formatNum(num) {
      return num.toString().replace(/(?=\B(\d{3})+$)/g, ",");
    },
    openCard() {
      window.shell.openPage("https://www.baidu.com");
    },
    check() {
      this.checked = !this.checked;
      window.shell.emit("check-card", this.checked);
    },
    goLink(link) {
      window.shell.openPage(link.linkUrl, true);
    },
    overShow(link) {
      link.fontClass = "portal-primary-color-lv1";
    },
    outHide(link) {
      link.fontClass = "";
    },
    async isornotcollect(item, index) {
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
              fucType: item.favorite ? 3 : 2,
            },
          },
          startTime: new Date().getTime(),
        });
        item.throttle = false;
        await window.shell.collectServiceItem({
          id: item.itemWid,
          operate: item.favorite ? 0 : 1,
          cardId: this.router.cardId,
        });
        setTimeout(() => {
          item.throttle = true;
        }, 300);
        item.favorite = item.favorite ? 0 : 1;
        this.$set(this.serviceItemsInfo, index, item);
      }
    },

    process(el) {
      if (el.cardId !== this.router.cardId) {
        this.initdata();
      }
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
        .execCardMethod({ ...this.card, method: "renderData" })
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
                v.iconLink || (v.iconLink = require("./assets/default.png"));
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
          this.configure5 = showItem.includes("visit");
          // 自定义高度
          if (serviceItemsInfo && serviceItemsInfo.length) {
            this.type = containerType.type;
            this.height = containerType.value;
            //生成实际渲染列表数据
            let arr = [],
              zIndex = Math.ceil(this.serviceItemsInfo.length / parseInt(columns));
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
            this.ul_width = { width: this.$el.offsetWidth + "px" };
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
  },
  watch: {
    checked() {
      console.log("aaa");
    },
  },

  beforeDestroy() {
    window.shell.off("check-card");
    window.shell.off("collectServiceItem");
  },
};
</script>

<style scoped lang="less">
.favorite_font_color {
  color: #fa6444;
}
.portal-font-color-lv1 {
  color: #102645;
}
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.recommendserviceitems {
  max-width: 100%;
  min-width: 360px;
  padding-top: 12px;
  ul {
    display: flex;
    flex-wrap: nowrap;
    > li {
      // margin-top: 12px;
      min-width: 250px;
      padding: 8px 8px;
      .recommendserviceitems-content {
        display: flex;
        height: auto;
        padding: 12px;
        width: 100%;
        border-radius: 8px;
        border: 1px solid #eff3fa;
        background: #fff;
        cursor: pointer;
        &:hover {
          > .recommendserviceitems-right {
            > .iconfont {
              display: block;
              font-size: 14px;
            }
          }
        }
        > .recommendserviceitems-left {
          height: 44px;
          width: 44px;
          flex-shrink: 0;
          > img {
            height: 100%;
            width: 100%;
          }
        }
        > .recommendserviceitems-left-nopic {
          border-radius: 2px;
          width: 4px;
          height: 12px;
          background-color: #bfbfbf;
        }
        > .recommendserviceitems-center {
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
        > .recommendserviceitems-right {
          width: 16px;
          flex-shrink: 0;
          // margin-left: 41px;
          > .iconfont {
            // display: none;
          }
        }
      }
      .is_center {
        align-items: center;
      }
    }
  }
}
.onlyImgT {
  display: flex;
  align-items: center;
}
.ellipsis2 {
  display: -webkit-box;
  overflow: hidden;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
