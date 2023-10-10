<template>
  <div style="position: relative">
    <div class="newsTab " v-if="showHeader">
      <div class="newsTab__left portal-font-color-lv2" ref="NewsTab">
        <div class="newsTab__item__wrap" ref="NewsTabCon" :style="offsetStyle" v-if="subscribeLists.length">
          <div class="newsTab__item portal-primary-color-hover-lv1" :id="`channel-${item?.wid}`"
            v-for="item in subscribeListsAll" :key="item?.wid" :class="[
              item?.wid === selectedTab ? 'active portal-primary-color-lv1' : '',
            ]" @click="handleChangeMoreTab(item)">
            {{ item.name }}
          </div>
        </div>
      </div>
      <div class="newsTab__right portal-font-color-lv2" v-if="showMore || showSubscribe"
        :class="[showMore ? 'moreShadow' : '']">
        <span class="newsTab__right__btn" @click.stop="handleClickMore" v-if="showMore"><span class="text__ellipsis"
            style="max-width: 54px;vertical-align: middle;">{{
              $Lan("SYS_CARD_NEWSANNOUNCEMENT", "more", "更多")
            }}</span><i class="iconfont icon-Down newsTab__right__MoreIcon portal-font-color-lv4"
            :class="[expand ? 'expand' : '']"></i></span>
        <span class="newsTab__right__split" v-if="showMore && showSubscribe"></span>
        <span class="newsTab__right__btn portal-primary-color-lv1" v-if="showSubscribe" @click="handleSubscribe"><i
            v-if="!showMore" class="iconfont icon-icon-dingyue newsTab__right__subscribeIcon"></i><span
            class="text__ellipsis" style="max-width: 110px; vertical-align: middle;">{{
              $Lan("SYS_CARD_NEWSANNOUNCEMENT", "subscribeBtn", "订阅")
            }}</span></span>
      </div>
    </div>
    <!-- 更多栏目 -->
    <div class="newsTab__more portal-font-color-lv1" :class="[expand ? 'show' : '']" @click.stop>
      <ContainerScroll :maxHeight="cardHeight * 0.8" :barKeepShow="true">
        <div class="newsTab__more__item portal-primary-color-hover-lv1 portal-primary-border-color-hover-lv1"
          v-for="item in subscribeListsAll" :key="item?.wid" :class="[
            item?.wid === selectedTab
              ? 'portal-primary-color-lv1 portal-primary-border-color-lv1'
              : '',
          ]" @click="handleChangeMoreTab(item)">
          {{ item.name }}
        </div>
      </ContainerScroll>
    </div>
    <!-- 订阅modal -->
    <we-dialog :title="$Lan('SYS_CARD_NEWSANNOUNCEMENT', 'programLists', '栏目列表')" :append-to-body="true"
      :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="visible"
      custom-class="newsSubscibeModal" top="15vh" width="800px" @closed="handleClickTrack()">
      <subscribe-modal :dataLists="configureLists" :defaultSelected="defaultSelected" :router="router" v-loading="loading"
        ref="SubscribeModal"></subscribe-modal>
      <div slot="footer" class="dialog-footer">
        <we-button
          class="portal-primary-color-hover-lv1 portal-primary-border-color-hover-lv4 portal-primary-backgroundcolor-hover-lv5"
          @click="handleClose">{{ $Lan("SYS_CARD_NEWSANNOUNCEMENT", "cancel", "取 消") }}</we-button>
        <we-button type="primary" :class="[
          disabled
            ? 'portal-primary-backgroundcolor-lv3 portal-primary-border-color-lv3'
            : 'portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1',
        ]" :loading="okLoading" :disabled="disabled" @click="handleOk">{{ $Lan("SYS_CARD_NEWSANNOUNCEMENT", "ok", "确定") }}</we-button>
      </div>
    </we-dialog>
  </div>
</template>

<script>
import SubscribeModal from "./SubscribeModal.vue";
import { clickTrack } from '../mixins/track.js';
export default {
  props: {
    cardWidth: Number,
    cardHeight: Number,
    router: Object,
    newsShowSubscribe: [Number, String],
  },
  components: {
    SubscribeModal,
  },
  mixins: [clickTrack],
  data() {
    return {
      selectedTab: "",
      expand: false,
      showMore: false,
      visible: false,
      disabled: true,
      okLoading: false,
      offsetStyle: "",
      subscribeLists: [],
      configureLists: [],
      loading: false,
      isLogin: window.shell.getUserInfo() ? true : false,
    };
  },
  computed: {
    subscribeListsAll() {
      const allTabName = this.$Lan(
        "SYS_CARD_NEWSANNOUNCEMENT",
        "allChannle",
        "全部"
      );
      return this.subscribeLists.length > 1
        ? [{ wid: "all", name: allTabName }].concat(this.subscribeLists)
        : this.subscribeLists || [];
    },
    defaultSelected() {
      return this.subscribeLists.map((item) => item?.wid);
    },
    showSubscribe() {
      return (
        this.isLogin &&
        this.newsShowSubscribe == 1 &&
        this.subscribeLists.length
      );
    },
    showHeader() {
      // 只配置一个栏目同时固定该栏目时不展示栏目名称，不显示订阅按钮
      const channelLists =
        this.configureLists.filter((item) => item.isChannel) || [];
      if (channelLists.length === 1 && this.subscribeLists.length === 1) {
        return false;
      }
      return this.subscribeLists.length || this.showSubscribe;
    },
  },
  watch: {
    cardWidth() {
      this.$nextTick(function () {
        this.calculateShowMore();
      });
    },
    selectedTab() {
      this.handleUpdateNews();
    },
    newsShowSubscribe() {
      if (this.newsShowSubscribe == 1) {
        this.$nextTick(function () {
          this.calculateShowMore();
        });
      }
    },
  },
  methods: {
    handleUpdateNews() {
      this.$emit(
        "change-tab",
        this.selectedTab === "all"
          ? this.subscribeLists.map((item) => item?.wid).join(",")
          : this.selectedTab
      );
    },
    async getSubscribedChannels(isUpdate) {
      this.$parent.changeLoading();
      await window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getSubscribedChannels",
        },
        (res) => {
          this.$parent.changeLoading();
          this.subscribeLists = res.data || [];
          if (isUpdate) {
            this.updateTabLists();
          } else {
            if (this.subscribeLists.length > 1) {
              this.selectedTab = "all";
            } else {
              this.selectedTab = this.subscribeLists.length
                ? this.subscribeLists[0]?.wid
                : "";
            }
          }
          this.$parent.showSubscribe =
            this.isLogin &&
            !this.subscribeLists.length &&
            this.configureLists.length;
          if (!isUpdate && this.$parent.showSubscribe) {
            // 获取配置
            this.$parent.renderData();
          }
          this.$nextTick(function () {
            this.calculateShowMore();
          });
          if (!this.subscribeLists.length) { // 加载结束
            this.$parent.loadedEndTrack();
          }
        }
      );
    },
    async getConfiguredChannels() {
      this.loading = true;
      await window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getConfiguredChannel",
        },
        (res) => {
          this.loading = false;
          this.configureLists = res.data || [];
        }
      );
    },
    handleChangeMoreTab(item) {
      this.handleClickTrack(); //点击埋点
      if (item.wid === this.selectedTab) {
        this.expand = false;
        return;
      }
      this.selectedTab = item.wid;
      this.expand = false;
      const ele = document.getElementById(`channel-${item.wid}`);
      const conWid = this.$refs.NewsTab.offsetWidth;
      // 选中的栏目不在可视区域
      if (ele && ele.offsetLeft + ele.offsetWidth > conWid) {
        this.offsetStyle = `transform: translateX(${conWid -
          ele.offsetLeft -
          ele.offsetWidth -
          50}px)`;
      } else {
        this.offsetStyle = `transform: translateX(0)`;
      }
    },
    handleClickMore() {
      this.expand = !this.expand;
      this.handleClickTrack(); //点击埋点
    },
    calculateShowMore() {
      this.showMore = this.$refs.NewsTabCon
        ? this.$refs.NewsTabCon.scrollWidth > this.$refs.NewsTab.offsetWidth
        : false;
    },
    handleSubscribe() {
      this.visible = true;
      this.getConfiguredChannels();
      this.handleClickTrack(); //点击埋点
    },
    handleOk() {
      const channelIds = this.$refs.SubscribeModal.checkedLists.map(
        (item) => item.id
      );
      this.okLoading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "subscribeChannel",
          param: {
            channelIds: channelIds && channelIds.join(","),
          },
        },
        (res) => {
          this.okLoading = false;
          if (res.errcode === "0") {
            this.visible = false;
            // this.getSubscribedChannels(true);
            window.shell.emit("update-news");
          } else {
            this.handleClickTrack(); //点击埋点
          }
        }
      );
    },
    handleClose() {
      this.visible = false;
    },
    updateTabLists() {
      if (this.selectedTab === "all") {
        this.handleUpdateNews();
      } else {
        // 当前选中的tab被取消订阅，默认选中全部
        const includeCurr = this.subscribeLists.find(
          (curr) => curr.wid === this.selectedTab
        );
        if (!includeCurr) {
          this.selectedTab =
            this.subscribeLists.length === 1
              ? this.subscribeLists[0]?.wid
              : "all";
        } else {
          if (this.subscribeLists.length === 1) {
            this.selectedTab = this.subscribeLists[0]?.wid;
          }
        }
      }
    },
    handleClickOut() {
      this.expand = false;
    },
    handleUpdateEvents() {
      this.getSubscribedChannels(true);
    }
  },
  async mounted() {
    // 获取后台配置的栏目
    await this.getConfiguredChannels();
    // 获取用户已订阅的栏目
    await this.getSubscribedChannels();
    window.addEventListener("click", this.handleClickOut);
    window.shell.on("update-news", this.handleUpdateEvents);
  },
  beforeDestroy() {
    window.removeEventListener("click", this.handleClickOut);
    window.shell.off("update-news", this.handleUpdateEvents);
  },
};
</script>

<style lang="less" scoped>
.newsTab {
  height: 42px;
  display: flex;
  align-items: center;
  background: #ffffff;
  box-shadow: inset 0 -1px 0 0 #f0f0f0;

  .newsTab__left {
    flex: 1;
    min-width: 0;
    overflow: hidden;

    .newsTab__item__wrap {
      display: inline-flex;
      align-items: center;
      transition: transform 0.3s ease-in;

      .newsTab__item {
        margin-right: 16px;
        cursor: pointer;
        flex-shrink: 0;

        &.active {
          font-weight: 700;
        }
      }
    }
  }

  .newsTab__right {
    height: 42px;
    line-height: 42px;
    padding: 0 0 0 8px;
    position: relative;

    .newsTab__right__btn {
      cursor: pointer;
    }

    .newsTab__right__MoreIcon {
      font-size: 12px;
      margin-left: 4px;

      &.expand {
        display: inline-block;
        transform: rotate(180deg);
      }
    }

    .newsTab__right__subscribeIcon {
      margin-right: 4px;
      vertical-align: middle;
      margin-top: -2px;
    }

    .newsTab__right__split {
      margin: 0 8px;
      display: inline-block;
      height: 13px;
      width: 1px;
      vertical-align: middle;
      box-shadow: inset -1px 0 0 0 #f0f0f0;
    }
  }

  .moreShadow:before {
    content: "";
    position: absolute;
    display: block;
    width: 10px;
    height: 42px;
    top: 0;
    left: 0;
    box-shadow: -5px 0 12px -2px rgba(0, 0, 0, 0.08);
  }
}

.newsTab__more {
  visibility: hidden;
  overflow: hidden;
  max-height: 0;
  transition: max-height 0.4s ease-in;
  position: absolute;
  top: 42px;
  left: 0;
  width: 100%;
  background: #ffffff;
  box-shadow: 0 3px 16px 0 rgba(0, 0, 0, 0.09);
  z-index: 99;
  padding: 8px 20px 20px 20px;
  border-radius: 0 0 4px 4px;

  &.show {
    visibility: visible;
    max-height: initial;
  }

  .newsTab__more__item {
    cursor: pointer;
    display: inline-block;
    border-radius: 4px;
    margin-right: 12px;
    margin-top: 12px;
    padding: 3px 12px;
    line-height: 22px;
    border: 1px solid #d9d9d9;
  }
}
</style>
