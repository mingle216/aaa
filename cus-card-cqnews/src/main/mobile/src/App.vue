<template>
  <div class="card-newsAnnouncement">
    <div
      class="card-newsAnnouncement-content"
      :class="`card-newsAnnouncement-${card.cardWid}`"
    >
      <template v-if="tabs.length">
        <we-tabs
          v-model="active"
          sticky
          :swipe-threshold="1"
          @click="
            () => {
              this.changeTab();
              this.handleClickTrack();
            }
          "
          @scroll="scroll"
          :class="[hideTab ? 'hideTab' : '']"
        >
          <we-tab
            v-for="(item, index) in tabs"
            :title="item.name"
            :key="index"
            class="newsAnnouncement-tab"
          >
            <div class="newsAnnouncement-listFather">
              <div
                class="newsAnnouncement-listContent"
                v-if="news[newsId].totalSize"
              >
                <template v-for="(item, index) in news[newsId].list">
                  <div
                    class="newsAnnouncement-list"
                    :key="index"
                    @click="goLink(item)"
                  >
                    <div class="list-left">
                      <h3
                        :class="[
                          `portal-font-color-lv${item.read ? 3 : 1}`,
                          ellipsisClass(item),
                        ]"
                      >
                        {{ item.title && item.title.replace(/\n/g, " ") }}
                      </h3>
                      <div class="left-icon">
                        <span class="topIcon" v-if="item.isTop"
                          ><top-tag></top-tag
                        ></span>
                        <new-tag v-if="item.newDisplay"></new-tag>
                      </div>
                      <p class="portal-font-color-lv2">
                        <span class="ellipsis"
                          >{{ item.columnName }}{{ item.channelName }}</span
                        >
                        <span class="newsTime" v-show="item.pubTime">{{
                          filterTime(item.pubTime)
                        }}</span>
                      </p>
                    </div>
                    <div class="list-right" v-if="item.picUrl">
                      <img
                        :src="item.picUrl"
                        alt=""
                        @error="
                          () => {
                            item.picUrl = null;
                          }
                        "
                      />
                    </div>
                  </div>
                </template>
                <div
                  class="lookmore portal-primary-color-lv1"
                  @click="downMore"
                  v-if="!news[newsId].finished"
                >
                  <span class="ellipsis">{{
                    $Lan("CUS_CARD_CQNEWS_h5", "showMore", "查看更多")
                  }}</span>
                  <i
                    class="icon-icon-gengduodaibanrenwu iconfont more-icon"
                  ></i>
                </div>
              </div>
              <EmptyCon
                v-else
                :tip="
                  $Lan(
                    'CUS_CARD_CQNEWS_h5',
                    'noData',
                    '暂无内容'
                  )
                "
                :height="179"
              >
              </EmptyCon>
            </div>
          </we-tab>
        </we-tabs>
      </template>
      <EmptyCon v-else :height="179">
        <we-button
          class="portal-primary-color-lv1 subscribe-nodata-btn"
          @click="handleSub"
          v-if="userInfo"
          ><i class="icon-icon-dingyue iconfont"></i>
          {{
            $Lan("CUS_CARD_CQNEWS_h5", "goSubscribe", "点击订阅")
          }}</we-button
        >
        <p class="portal-font-color-lv2">
          {{ $Lan("CUS_CARD_CQNEWS_h5", "noChannel", "暂无栏目") }}
        </p>
      </EmptyCon>
    </div>
    <subscribe
      v-model="isShow"
      ref="sub"
      :tabs="tabs"
      :card="card"
      :router="router"
      :subscribeProgramme="config.subscribeProgramme"
      @renderTab="getTabs"
    ></subscribe>
  </div>
</template>

<script>
import subscribe from "./components/subscribe";
import topTag from "./components/topTag.vue";
import newTag from "./components/newTag.vue";
import { initTrack } from "./mixins/track.js";
export default {
  components: { subscribe, topTag, newTag },
  name: "",
  props: {
    index: Number,
    router: Object,
  },
  mixins: [initTrack],
  data() {
    const { cardWid, cardId } = this.router;
    return {
      news: {},
      tabs: [],
      userInfo: window.shell.getUserInfo() ? true : false,
      loading: false,
      finished: true,
      active: 0,
      isShow: false,
      pageNumber: 1,
      card: {
        cardWid,
        cardId,
      },
      configChannelNum: 0,
      newsRead: JSON.parse(window.localStorage.getItem("newsRead") || "[]"),
      config: {},
    };
  },
  created() {
    this.getTabs(true);
  },
  mounted() {
    window.shell.on("update-news", this.getTabs);
  },
  computed: {
    newsId() {
      return `tab-${this.active}`;
    },
    tabId() {
      return this.tabs[this.active] && this.tabs[this.active].wid;
    },
    hideTab() {
      return this.configChannelNum === 1 && this.tabs.length;
    },
  },
  beforeDestroy() {
    window.shell.off("update-news", this.getTabs);
  },
  methods: {
    ellipsisClass(item) {
      if (item.picUrl) {
        return item.isTop || item.newDisplay ? "ellipsis-2" : "ellipsis-3";
      }
      return "ellipsis-1";
    },
    scroll() {
      // isFixed
      //   ? window.shell.emit("fixed-header-on", { height: 49 })
      //   : window.shell.emit("fixed-header-off");
    },
    goLink(item) {
      this.handleClickTrack(); // 点击埋点
      let source_flag = this.config?.newsDetailJump || 0;
      if (Number(source_flag)) {
        //跳转到本地新闻详情页面
        this.handleRecordNews(item);
        window.shell.openPage(`/newsDetail?wid=${item.wid}`, 1, 1);
      } else {
        if (item.url) {
          this.handleRecordNews(item);
          window.shell.openPage(item.url, 1, 2);
        } else {
          window.shell.showMessage({
            message: this.$Lan(
              "CUS_CARD_CQNEWS_h5",
              "noLink",
              "暂无跳转地址"
            ),
            type: "warning",
          });
        }
      }
    },
    handleRecordNews(item) {
      item.read = true;
      if (this.config.isEnableRead) {
        window.shell.execTemplateMethod("newsRead", {
          wid: item.wid,
          // title: item.title,
          // contexts: item.contents
        });
      } else {
        if (!this.newsRead.find((curr) => curr === item.url)) {
          this.newsRead.push(item.url);
          window.localStorage.setItem(
            "newsRead",
            JSON.stringify(this.newsRead)
          );
        }
      }
    },
    handleSub() {
      this.isShow = true;
      this.handleClickTrack(); // 点击埋点
    },
    reset() {
      this.setAll();
      this.active = 0;
      this.news = {};
      this.tabs.forEach((v, index) => {
        this.$set(this.news, `tab-${index}`, {
          list: [],
          pageNumber: 1,
          loading: false,
          finished: false,
          totalSize: -1,
          init: false,
        });
      });
      this.$nextTick(() => {
        this.tabs.length && this.userInfo && this.addSubHtml();
      });
    },
    filterTime(time) {
      if (!time) {
        return "";
      }
      const getTimeStamp = (time = "") => {
        let date = time ? new Date(time) : new Date();
        return parseInt(date.getTime() / 1000);
      };

      let timeStamp = getTimeStamp() - getTimeStamp(time.replace(/-/g, "/"));
      if (timeStamp < 0) {
        return time
          .split(":")
          .filter((v, i) => i <= 1)
          .join(":");
      }
      let TimeText = "";
      let TimeArr = [
        {
          time: 15 * 60,
          text: this.$Lan("CUS_CARD_CQNEWS_h5", "timeJust", "刚刚"),
        },
        {
          time: 60 * 60,
          text: (time) =>
            `${parseInt(time / 60)}${this.$Lan(
              "CUS_CARD_CQNEWS_h5",
              "minsutesAgo",
              "分钟前"
            )}`,
        },
        {
          time: 24 * 60 * 60,
          text: (time) =>
            `${parseInt(time / (60 * 60))}${this.$Lan(
              "CUS_CARD_CQNEWS_h5",
              "hoursAgo",
              "小时前"
            )}`,
        },
        {
          time: 30 * 24 * 60 * 60,
          text: (time) =>
            `${parseInt(time / (24 * 60 * 60))}${this.$Lan(
              "CUS_CARD_CQNEWS_h5",
              "daysAgo",
              "天前"
            )}`,
        },
        {
          time: new Date().getTime(),
          text: time
            .split(":")
            .filter((v, i) => i <= 1)
            .join(":"),
        },
      ];
      TimeArr.some((v) => {
        if (timeStamp < v.time) {
          TimeText = typeof v.text === "function" ? v.text(timeStamp) : v.text;
          return true;
        }
      });
      return TimeText;
    },
    addSubHtml() {
      const fatherNode = document.querySelector(
        `.card-newsAnnouncement-${this.card.cardWid} .we-tabs--line .we-tabs__wrap`
      );
      const isrenderBtn = document.querySelector(
        `.card-newsAnnouncement-${this.card.cardWid} .we-tabs--line .we-tabs__wrap .subtn`
      );
      if (isrenderBtn) {
        return;
      }
      const btn = document.createElement("span");
      fatherNode.addEventListener("click", (e) => {
        if (e.target.className === "subtn") {
          this.isShow = true;
          this.handleClickTrack(); // 点击埋点
        }
      });
      btn.className = "subtn";
      fatherNode.appendChild(btn);
    },
    execCardMethod(params, callback) {
      window.shell.execCardMethod(params, (data) => {
        callback && typeof callback === "function" && callback(data);
      });
    },
    getTabs(isInit) {
      //获取订阅栏目
      this.execCardMethod(
        { ...this.card, method: "getConfiguredAndSubscribedChannel" },
        (data) => {
          this.tabs = data.data?.subscribedChannel || [];
          this.reset();
          this.tabs.length && this.changeTab();
          if (isInit) {
            const configureLists = data.data?.configuredChannel || [];
            this.configChannelNum = configureLists.filter(
              (item) => item.isChannel
            ).length;
            this.$refs.sub?.getAllList(configureLists);
          }
        }
      );
    },
    setAll() {
      if (this.tabs.length > 1) {
        this.tabs.unshift({
          name: this.$Lan("CUS_CARD_CQNEWS_h5", "allChannle", "全部"),
          wid: this.tabs
            .map((v) => {
              return v.wid;
            })
            .join(","),
        });
      }
    },
    getNewsList(param, callback) {
      this.execCardMethod(
        {
          ...this.card,
          method: "getChannelNews",
          param,
        },
        (data) => {
          callback && callback(data);
        }
      );
    },
    changeTab() {
      if (this.news[this.newsId].init) {
        return;
      }
      this.news[this.newsId].loading = true;
      const tabIds = this.tabId.split(",");
      let channelIds = [],
        programIds = [];
      tabIds.forEach((wid) => {
        const item = this.tabs.find((curr) => curr.wid === wid);
        if (item.type === 1) {
          //站点
          programIds.push(wid);
        } else {
          // 频道
          channelIds.push(wid);
        }
      });

      this.getNewsList(
        {
          channelIds: channelIds.join(","),
          programIds: programIds.join(","),
          pageNumber: "1",
        },
        (res) => {
          let { data: list = [], totalSize } = res.data.datas;
          this.config = res.data.configInfo || {};
          // 是否开启后端记录已读未读
          if (!this.config.isEnableRead) {
            list = list.map((v) => {
              return {
                ...v,
                read: this.newsRead.find((curr) => curr === v.url)
                  ? true
                  : false,
              };
            });
          }
          this.news[this.newsId].loading = false;
          this.news[this.newsId].init = true;
          this.news[this.newsId].list = list;
          this.news[this.newsId].totalSize = totalSize;
          if (list.length >= totalSize) {
            this.news[this.newsId].finished = true;
          }

          this.loadedEndTrack(); // 加载结束埋点
        }
      );
    },
    downMore() {
      this.handleClickTrack(); // 点击埋点
      const channelIds = this.tabId.includes(",") ? "" : this.tabId;
      window.shell.openPage(
        `newList?cardId=${this.router.cardId}&cardWid=${this.router.cardWid}&channelIds=${channelIds}`,
        0,
        1
      );
    },
  },
  watch: {},
};
</script>

<style lang="less" scoped>
.loop(@counter) when (@counter <= 3) {
  .ellipsis-@{counter} {
    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-all;
    white-space: pre-wrap;
    display: -webkit-box;
    -webkit-line-clamp: @counter;
    -webkit-box-orient: vertical;
  }
  .loop(@counter + 1);
}
.loop(1);
.card-newsAnnouncement {
  // font-family: PingFangSC-Regular;
  letter-spacing: 0;
  font-size: 14px;
  > .card-newsAnnouncement-content {
    /deep/ .we-sticky--fixed {
      -webkit-transform: translateZ(0);
    }
    .hideTab /deep/.we-tabs__wrap {
      display: none;
    }
    /deep/ .we-tabs--line .we-tabs__wrap {
      height: 49px;
      border-bottom: 1px solid #eef2f5;
      position: relative;
      padding-right: 54px;
      &::before {
        position: absolute;
        top: 0;
        left: 0;
        content: "";
        height: 100%;
        width: 17px;
        background-image: linear-gradient(
          90deg,
          #ffffff 0%,
          rgba(255, 255, 255, 0) 100%
        );
        z-index: 1;
      }
      .subtn {
        position: absolute;
        top: 0;
        right: 0;
        content: "";
        z-index: 1;
        height: 100%;
        width: 54px;
        background: #ffffff url("./assest/images/icon-灰色订阅@3x.png")
          no-repeat center/20px 20px;
        box-shadow: 0 0 10px 0 rgba(112, 125, 143, 0.3);
      }
      > .we-tabs__nav {
        > .we-tab {
          flex: none;
          padding: 0 12px;
        }
        // > .we-tab:nth-last-of-type(1) {
        //   padding-right: 78px;
        // }
      }
    }
    /deep/ .we-tabs__line {
      display: none;
    }
    /deep/ .we-tabs__wrap--scrollable .we-tabs__nav--complete {
      padding: 0 5px;
    }
    .we-tab:last-child {
      margin-right: 54px;
    }
    .newsAnnouncement-listFather {
      position: relative;
    }
    .newsAnnouncement-listContent {
      //height: calc(100vh - 100px);
      padding: 0 17px;
      .newsAnnouncement-list {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        padding: 16px 0;
        &:not(:first-child) {
          border-top: 1px solid #e7edf1;
        }
        > .list-left {
          flex: 1;
          overflow: hidden;
          > h3 {
            font-size: 16px;
            line-height: 20px;
            width: 100%;
          }
          > .left-icon {
            margin-top: 6px;
            .topIcon {
              margin-right: 6px;
            }
          }
          > p {
            display: flex;
            justify-content: space-between;
            font-size: 12px;
            line-height: 16px;
            margin-top: 10px;
            width: 100%;
            > span:first-child {
              flex: 1;
            }
            > .newsTime {
              width: 120px;
              text-align: right;
            }
          }
        }
        > .list-right {
          margin-left: 12px;
          height: 86px;
          width: 109px;
          border-radius: 4px;
          flex-shrink: 0;

          > img {
            object-fit: cover;
            width: 100%;
            height: 100%;
          }
        }
      }
    }
    .lookmore {
      background: #f9fafb;
      border-radius: 4px;
      width: 100%;
      padding: 0 20px;
      height: 48px;
      font-size: 16px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 12px;
      .more-icon {
        font-size: 12px;
        margin-left: 4px;
        transform: rotate(-90deg);
        flex-shrink: 0;
      }
    }
  }
  .subscribe-nodata-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    text-align: center;
    line-height: 20px;
    border: none;
    width: 100%;
    height: 20px;
    margin-bottom: 6px;
    &:active {
      &::before {
        opacity: 0;
      }
    }
    .subscribe-icon {
      height: 14px;
      width: 14px;
      background: url("./assest/images/icon-订阅@3x.png") no-repeat center/100%
        100%;
      display: inline-block;
    }
  }
}
</style>
