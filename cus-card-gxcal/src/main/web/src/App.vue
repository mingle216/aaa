<template>
  <div
    class="cardCalendar portal-font-color-lv1"
    :class="[router.showTitle ? 'mt-12' : '']"
    v-loading="loading"
  >
    <!-- 标题 -->
    <CalendarHeader v-if="showHeader" :headerInfo="headerInfo"></CalendarHeader>
    <!-- 日历 -->
    <ContainerScroll :scrollingX="true" :barKeepShow="true">
      <!-- 操作按钮 -->
      <calendar v-model="selectedDate" ref="Calendar">
        <template slot="dateBtn">
          <OptBtn
            :isCurrentMon="isCurrentMon"
            :showSubscribe="config.calendarShowSubscribe"
            :router="router"
            :selectedDate="selectedDate"
            :weekRemindCalendar="weekRemindCalendar"
            :themeColor="themeColor"
            :userSubscribe="userSubscribe"
            @goToday="selectDate"
            @external="handleExternalLink"
            @update="init"
          ></OptBtn>
        </template>
        <template slot="dateCell" slot-scope="{ text, data }">
          <div class="cardCalendar__date">
            <span
              :class="{ hasSchedule: scheduleLists.includes(data.day) }"
              class="cardCalendar__dateText portal-primary-after-backgroundcolor-lv1"
              >{{ text }}</span
            >
          </div>
        </template>
      </calendar>
      <!-- 日程列表 -->
      <auto-container
        class="cardCalendar__lists"
        :conType="containerType"
        :conHeight="conHeight"
        :scroll="scroll"
      >
        <div v-if="originLists.length">
          <InfoList
            v-for="item in dataLists"
            :key="item.id"
            :item="item"
            :router="router"
          ></InfoList>
        </div>
        <div v-else class="cardCalendar__empty ellipsis portal-font-color-lv4">
          {{ $Lan("CUS_CARD_GXCAL", "noDaycircle", "暂无日程") }}
        </div>
        <div
          v-if="originLists.length > (config.expandNum || 2)"
          class="expand portal-primary-color-lv1"
          @click="handleFold"
        >
          <span class="text__ellipsis">{{
            $Lan(
              "CUS_CARD_GXCAL",
              unfold ? "unfold" : "fold",
              unfold ? "展开" : "收起"
            )
          }}</span>
          <i
            class="iconfont icon-icon-gengduodaibanrenwu"
            style="margin-left: 4px; font-size: 12px"
            :class="{ fold: !unfold }"
          ></i>
        </div>
      </auto-container>
    </ContainerScroll>
    <ExternalLink :router="router" ref="ExternalLinkModal"></ExternalLink>
  </div>
</template>

<script>
import CalendarHeader from "./components/header";
import Calendar from "./components/calendar/index";
import OptBtn from "./components/optBtn";
import InfoList from "./components/infoList";
import ExternalLink from "./components/externalLink/index";
import { initTrack } from "./mixins/track.js";
import {
  formatDate,
  getFirstDayOfMonth,
  getPrevMonthLastDays,
} from "./utils/date-util";
const oneDay = 86400000;

export default {
  name: "CardCalendar",
  props: ["router"],
  components: {
    CalendarHeader,
    Calendar,
    OptBtn,
    InfoList,
    ExternalLink,
  },
  mixins: [initTrack],
  data() {
    const templateConfig =
      window.shell.getPageData()?.showProgrammeEntity?.templateConfig || "";
    const configObj = JSON.parse(JSON.parse(templateConfig || "{}"));
    const themeColor = configObj.themeColorSetting || {};
    return {
      loading: false,
      containerType: 0,
      scroll: { barKeepShow: true },
      selectedDate: new Date(),
      unfold: true,
      config: {},
      showHeader: false,
      headerInfo: null,
      dataListsObj: {},
      originLists: [],
      weekRemindCalendar: {},
      themeColor: themeColor["portal-primary-color-lv1"] || "#307AFB",
      userSubscribe: {},
    };
  },
  computed: {
    isCurrentMon() {
      const now = new Date();
      return (
        this.selectedDate.getFullYear() !== now.getFullYear() ||
        this.selectedDate.getMonth() !== now.getMonth()
      );
    },
    dataLists() {
      if (this.unfold) {
        const size = this.config.expandNum || 2;
        return this.originLists.length > size
          ? this.originLists.slice(0, size)
          : this.originLists;
      }
      return this.originLists;
    },
    scheduleLists() {
      return Object.keys(this.dataListsObj);
    },
    conHeight() {
      let value = this.config.calendarHeight?.value;
      value = value - (this.router.showTitle ? 12 : 0);
      return this.containerType == 0
        ? value
        : value - (this.showHeader ? 54 : 0) - 320; //减掉标题和日历的高度
    },
  },
  watch: {
    selectedDate(newVal, oldVal) {
      this.getWeek();
      if (newVal.getMonth() === oldVal.getMonth()) {
        this.getCurrentSchedule();
      } else {
        this.calStartAndEnd();
        this.init();
      }
      this.handleClickTrack(); // 点击埋点
    },
  },
  methods: {
    init() {
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "renderData",
          param: {
            startTime: this.startTime,
            endTime: this.endTime,
            selectDate: formatDate(this.selectedDate),
          },
        },
        (data) => {
          this.$nextTick(function() {
            this.loading = false;
            this.loadedEndTrack(); // 加载结束埋点
          });
          if (data && data.errcode === "0") {
            this.config = data.data.config || {};
            this.weekRemindCalendar = this.config.WeekRemindCalendar || {};
            this.containerType = this.config.calendarHeight?.type || 0;
            this.userSubscribe = data.data.userSubscribe || {};
            this.dataListsObj = data.data.datas || {};
            this.getCurrentSchedule();
          }
        }
      );
    },
    calStartAndEnd() {
      const selectedDate = new Date(this.selectedDate);
      let firstDay = getFirstDayOfMonth(selectedDate);
      firstDay = firstDay === 0 ? 7 : firstDay;
      const firstDayOfWeek = 1;
      const offset = (7 + firstDay - firstDayOfWeek) % 7;
      const prevMonthDays = getPrevMonthLastDays(selectedDate, offset); // 获取上个月的天数
      const prev = prevMonthDays.length * oneDay;
      const curr = new Date(selectedDate.setDate(1)).setHours(0, 0, 0, 0); // 当前月的第一天的零点
      const start = parseInt(curr - prev);
      const end = parseInt(start + 42 * oneDay - 1);
      this.startTime = formatDate(new Date(start), "yyyy-MM-dd HH:mm:ss");
      this.endTime = formatDate(new Date(end), "yyyy-MM-dd HH:mm:ss");
    },
    getCurrentSchedule() {
      const curr = formatDate(this.selectedDate);
      this.originLists = this.dataListsObj[curr] || [];
    },
    selectDate() {
      this.$refs.Calendar.selectDate("today");
    },
    handleExternalLink(item) {
      this.$refs.ExternalLinkModal.show(item);
      this.handleClickTrack({
        infoType: 14,
      }); // 点击埋点
    },
    handleFold() {
      this.unfold = !this.unfold;
      this.handleClickTrack(); // 点击埋点
    },
    getWeek() {
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "getWeek",
          param: {
            selectDate: formatDate(this.selectedDate),
          },
        },
        (res) => {
          if (res && res.errcode === "0") {
            if (res.data.postInfo || res.data.underInfo) {
              this.showHeader = true;
            } else {
              this.showHeader = false;
            }
            if (this.showHeader) {
              this.headerInfo = {
                weekNumberPost: res.data.postInfo?.weekNumber,
                xn: Number(res.data.postInfo?.xn || res.data.underInfo?.xn),
                xq: res.data.postInfo?.xq || res.data.underInfo?.xq,
                weekNumberUnder: res.data.underInfo?.weekNumber,
              };
            }
          }
        }
      );
    },
  },
  mounted() {
    this.calStartAndEnd();
    this.getWeek();
    this.init();
  },
};
</script>

<style lang="less" scoped>
.mt-12 {
  margin-top: 12px;
}
.cardCalendar {
  width: 100%;
  position: relative;
  border: 1px solid #f0f0f0;
  border-radius: 8px;

  .cardCalendar__date {
    width: 30px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;

    .cardCalendar__dateText {
      position: relative;
      line-height: 22px;
      &.hasSchedule:after {
        content: "";
        position: absolute;
        top: 4px;
        right: -5px;
        width: 5px;
        height: 5px;
        border-radius: 2.5px;
      }
    }
  }
  /deep/.calendar-table__td {
    &.is-today.is-selected,
    &.is-today:hover {
      .hasSchedule:after {
        background: #fff !important;
      }
    }
    &.prev,
    &.next {
      .hasSchedule:after {
        background: #bfbfbf !important;
      }
    }
  }
  .cardCalendar__lists {
    margin-top: 8px;
    padding: 0 20px;

    .expand {
      padding: 2px 20px 20px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      .fold {
        transform: rotate(180deg);
      }
    }
  }
  .cardCalendar__empty {
    text-align: center;
    padding: 0 20px;
    line-height: 86px;
    height: 86px;
  }
}
</style>
