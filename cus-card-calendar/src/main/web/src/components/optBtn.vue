<template>
  <div class="optBtnWrap">
    <!-- 同步钉钉 -->
    <div
      class="optIcon portal-primary-color-lv1"
      @click="$refs.SyncDingTalkModal.show()"
      v-if="userSubscribe.canGlobalSubscribe === 1"
    >
      <we-tooltip
        effect="dark"
        :content="$Lan('CUS_CARD_CALENDAR', 'syncDingTalk', '同步钉钉数据')"
        placement="top"
        :open-delay="500"
      >
        <div style="position:relative">
          <span
            class="icon iconfont icon-fangxingdingding"
            style="font-size: 18px"
          ></span>
          <span
            v-if="userSubscribe.synchronizeResult !== 1"
            class="icon iconfont icon-tixing errorTip"
            style="flex-shrink: 0;font-size: 10px;color:#EE3F15;margin-left:4px"
          ></span>
        </div>
      </we-tooltip>
    </div>
    <!-- 周提醒 -->
    <we-popover
      v-model="remindPopShow"
      placement="bottom"
      width="116"
      trigger="manual"
      popper-class="remindPop calendarPop"
      @show="
        () => {
          this.remindPopShow = true;
          this.handleClickTrack();
        }
      "
      @hide="
        () => {
          this.remindPopShow = false;
          this.handleClickTrack();
        }
      "
      v-if="isLogin && isShowWeekRemind"
    >
      <WeekRemindPop
        :router="router"
        :weekRemindChannel="weekRemindChannel"
        :weekRemindConfig="weekRemindConfig"
        :themeColor="themeColor"
        @update="
          () => {
            this.$emit('update');
            this.getWeeklyReminder();
          }
        "
        @cancelPop="handleClosePop"
      ></WeekRemindPop>
      <div
        class="optIcon portal-primary-color-lv1"
        :class="[remindPopShow ? 'clicked' : '']"
        slot="reference"
        @click="remindPopShow = true"
      >
        <we-tooltip
          effect="dark"
          :content="
            $Lan(
              'CUS_CARD_CALENDAR',
              'weekRemind',
              '开启后将自动提醒您下一周日程'
            )
          "
          placement="top"
          :open-delay="500"
        >
          <span
            class="iconfont icon-zhoutixing portal-primary-color-lv1"
            style="font-size: 18px;"
          ></span>
        </we-tooltip>
      </div>
    </we-popover>
    <div
      v-if="isCurrentMon"
      class="optIcon portal-primary-color-lv1"
      @click="$emit('goToday')"
    >
      <we-tooltip
        effect="dark"
        :content="$Lan('CUS_CARD_CALENDAR', 'backToday', '回到今天')"
        placement="top"
        :open-delay="500"
      >
        <span
          class="icon iconfont icon-huidaojintian"
          style="font-size: 18px"
        ></span>
      </we-tooltip>
    </div>
    <!-- 订阅 -->
    <we-popover
      placement="bottom"
      width="155"
      trigger="click"
      popper-class="calendarPop"
      @show="
        () => {
          this.popShow = true;
          this.handleClickTrack();
        }
      "
      @hide="
        () => {
          this.popShow = false;
          this.handleClickTrack();
        }
      "
      v-if="isLogin && showSubscribe == 1 && calendarLists.length"
    >
      <SubscribePop
        :calendarLists="calendarLists"
        :router="router"
        @update="$emit('update')"
      ></SubscribePop>
      <div
        class="optIcon portal-primary-color-lv1"
        :class="[popShow ? 'clicked' : '']"
        slot="reference"
      >
        <we-tooltip
          effect="dark"
          :content="$Lan('CUS_CARD_CALENDAR', 'subscribe', '订阅')"
          placement="top"
          :open-delay="500"
        >
          <span
            class="iconfont icon-icon-dingyue"
            style="font-size: 18px"
            :class="[isIE ? 'mt-2' : '']"
          ></span>
        </we-tooltip>
      </div>
    </we-popover>
    <!-- 外部引用 -->
    <we-popover
      placement="bottom"
      width="116"
      trigger="click"
      popper-class="calendarPop"
      v-if="calendarLists.length"
      v-model="linkPopShow"
      @show="handleClickTrack({ infoType: 14 })"
      @hide="handleClickTrack()"
    >
      <SubscribePop
        :external="true"
        :calendarLists="calendarLists"
        @external="handleExternal"
      ></SubscribePop>
      <div
        class="optIcon portal-primary-color-lv1"
        :class="[linkPopShow ? 'clicked' : '']"
        slot="reference"
      >
        <we-tooltip
          effect="dark"
          :content="$Lan('CUS_CARD_CALENDAR', 'externalReference', '外部引用')"
          placement="top"
          :open-delay="500"
        >
          <span
            class="iconfont icon-waibuyinyong"
            style="font-size: 18px"
          ></span>
        </we-tooltip>
      </div>
    </we-popover>
    <!-- 查看更多 -->
    <div
      class="optIcon portal-primary-color-lv1"
      @click="handleMore"
      v-if="isLogin"
    >
      <we-tooltip
        effect="dark"
        :content="$Lan('CUS_CARD_CALENDAR', 'showMore', '查看更多')"
        placement="top"
        :open-delay="500"
      >
        <DetailSvg style="width: 18px; height: 18px"></DetailSvg>
      </we-tooltip>
    </div>
    <sync-ding-talk-modal
      :router="router"
      :userSubscribe="userSubscribe"
      :isLogin="isLogin"
      @update-data="$emit('update')"
      ref="SyncDingTalkModal"
    ></sync-ding-talk-modal>
  </div>
</template>

<script>
import DetailSvg from "./icon/detailSvg";
import SubscribePop from "./subscribePop";
import WeekRemindPop from "./weekRemindPop";
import SyncDingTalkModal from "./syncDingTalkModal";
import { clickTrack } from "../mixins/track.js";
import { formatDate } from "../utils/date-util";
export default {
  components: {
    DetailSvg,
    SubscribePop,
    WeekRemindPop,
    SyncDingTalkModal,
  },
  props: [
    "isCurrentMon",
    "showSubscribe",
    "router",
    "selectedDate",
    "weekRemindCalendar",
    "themeColor",
    "userSubscribe",
  ],
  mixins: [clickTrack],
  data() {
    return {
      popShow: false,
      linkPopShow: false,
      remindPopShow: false,
      calendarLists: [],
      isLogin: window.shell.getUserInfo() ? true : false,
      isIE: window.navigator.userAgent.indexOf("Trident") !== -1,
      weekRemindConfig: {}, //用户周提醒配置参数
    };
  },
  computed: {
    isShowWeekRemind() {
      return (
        this.weekRemindCalendar.radiobox &&
        this.weekRemindCalendar.radiobox === "1"
      );
    },
    weekRemindChannel() {
      return this.weekRemindCalendar.checkbox || [];
    },
  },
  mounted() {
    this.getSubsLists();
    this.getWeeklyReminder();
  },
  methods: {
    getSubsLists() {
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "getPermissionCal",
          param: null,
        },
        (data) => {
          if (data && data.errcode === "0") {
            this.calendarLists = data.data || [];
          }
        }
      );
    },
    handleClosePop() {
      this.remindPopShow = false;
    },
    getWeeklyReminder() {
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "getWeeklyReminder",
          param: null,
        },
        (data) => {
          if (data && data.errcode === "0") {
            this.weekRemindConfig = data.data;
            console.log(this.weekRemindConfig);
          }
        }
      );
    },
    handleMore() {
      this.handleClickTrack({
        infoType: 4,
        filterInfo: {
          pageCode: "schedule",
        },
      }); //点击埋点
      document.activeElement.blur && document.activeElement.blur();
      window.shell.openPage(
        `schedule?cardWid=${this.router.cardWid}&date=${formatDate(
          this.selectedDate
        )}`,
        1,
        1
      );
    },
    handleRemind() {
      //周提醒
    },
    handleExternal(item) {
      this.$emit("external", item);
      this.linkPopShow = false;
    },
  },
};
</script>

<style lang="less" scoped>
.mt-2 {
  margin-top: 2px;
}
.optBtnWrap {
  display: flex;
  align-items: center;
  .optIcon {
    &:focus {
      outline: 0;
    }
    cursor: pointer;
    width: 26px;
    height: 26px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-left: 4px;
    border-radius: 2px;
    &:hover,
    &.clicked {
      background: #f0f0f0;
    }
  }
  .errorTip {
    position: absolute;
    top: -5px;
    right: -5px;
    z-index: 1;
  }
}
</style>

<style lang="less">
.calendarPop.we-popover {
  padding-right: 0;
  &:focus,
  &:focus:active {
    // 解决safari在focus时会出现outline
    outline: none;
  }
}
.remindPop {
  width: 200px !important;
}
</style>
