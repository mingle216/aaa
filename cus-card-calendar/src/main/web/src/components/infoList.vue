<template>
  <div class="cardCalendar__listsRow">
    <div class="timeRange portal-font-color-lv3">
      <span class="timeRange__time">{{ formatTime(item.startTime) }}</span>
      <span class="splitLine" v-if="item.endTime"></span>
      <span class="timeRange__time" v-if="item.endTime">{{
        formatTime(item.endTime)
      }}</span>
    </div>
    <div
      class="scheduleContent"
      :style="
        `background:${hexToRgba(
          item.calWid == '1' || item.calWid == '2'
            ? themeColor
            : item.primaryColor
        )}`
      "
      :class="[item.linkUrl ? 'pointer' : '']"
      @click="handleClick"
    >
      <div
        class="scheduleContent__border"
        :style="
          `background:${
            item.calWid == '1' || item.calWid == '2'
              ? themeColor
              : item.primaryColor
          }`
        "
      ></div>
      <div
        class="scheduleContent__title ellipsis"
        :class="[item.linkUrl ? 'portal-primary-color-hover-lv1' : '']"
      >
        {{ item.eventTitle }}
      </div>
      <div
        class="scheduleContent__info portal-font-color-lv3 ellipsis"
        v-if="item.location"
      >
        <i
          class="iconfont icon-location"
          style="font-size: 12px;margin-right:4px"
        ></i>
        {{ item.location }}
      </div>
    </div>
  </div>
</template>

<script>
import { formatDate } from "../utils/date-util";
import { clickTrack } from "../mixins/track.js";
export default {
  props: ["item", "router"],
  mixins: [clickTrack],
  data() {
    const templateConfig =
      window.shell.getPageData()?.showProgrammeEntity?.templateConfig || "";
    const configObj = JSON.parse(JSON.parse(templateConfig || "{}"));
    const themeColor = configObj.themeColorSetting || {};
    return {
      themeColor: themeColor["portal-primary-color-lv1"] || "#307AFB",
    };
  },
  methods: {
    hexToRgba(val) {
      let color = val.toLowerCase();
      let pattern = /^#([0-9|a-f]{3}|[0-9|a-f]{6})$/;
      if (color && pattern.test(color)) {
        if (color.length == 4) {
          // 将三位转换为六位
          color =
            "#" +
            color[1] +
            color[1] +
            color[2] +
            color[2] +
            color[3] +
            color[3];
        }
        //处理六位的颜色值
        let colorNew = [];
        for (let i = 1; i < 7; i += 2) {
          colorNew.push(parseInt("0x" + color.slice(i, i + 2)));
        }
        return `rgba(${colorNew.join(",")},0.05)`;
      }
      return color;
    },
    formatTime(time) {
      if (time) {
        return formatDate(time, "HH:mm");
      }
      return "";
    },
    handleClick() {
      this.handleClickTrack(); // 点击埋点
      if (this.item.linkUrl) {
        window.shell.openPage(this.item.linkUrl, 1, 2);
      }
    },
  },
};
</script>

<style lang="less" scoped>
.cardCalendar__listsRow {
  display: flex;
  align-items: center;
  .timeRange {
    width: 32px;
    margin-right: 20px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .timeRange__time {
      font-size: 12px;
      line-height: 20px;
    }
    .splitLine {
      width: 1px;
      height: 9px;
      background: #979797;
    }
  }
  .scheduleContent {
    flex: 1;
    min-width: 0;
    border-radius: 4px;
    height: 68px;
    position: relative;
    display: flex;
    flex-direction: column;
    justify-content: center;
    border-radius: 4px;
    padding: 2px 24px;
    &.pointer {
      cursor: pointer;
    }
    .scheduleContent__border {
      height: 100%;
      position: absolute;
      width: 2px;
      left: 0;
      top: 0;
      border-top-left-radius: 2px;
      border-bottom-left-radius: 2px;
    }
    .scheduleContent__title {
      font-size: 16px;
      line-height: 24px;
      margin-bottom: 2px;
    }
    .scheduleContent__info {
      line-height: 22px;
    }
  }
  &:not(:last-child) {
    margin-bottom: 8px;
  }
  &:last-child {
    margin-bottom: 18px;
  }
}
</style>
