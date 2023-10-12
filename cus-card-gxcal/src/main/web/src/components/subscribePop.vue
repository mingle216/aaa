<template>
  <div :style="heightStyle">
    <ContainerScroll :barKeepShow="true">
      <div class="subscribePop">
        <div
          class="subscribePop__row"
          :class="[external ? 'external' : '']"
          v-for="item in lists"
          :key="item.wid"
          @click="external && $emit('external', item)"
        >
          <we-checkbox
            v-if="!external"
            v-model="item.subscribe"
            @change="handleChange(item)"
            >{{ handleCalName(item) }}</we-checkbox
          >
          <div class="subscribePop__name" v-else>
            {{ handleCalName(item) }}
          </div>
          <we-tooltip
            class="subscribePop__tip"
            effect="dark"
            v-if="item.calDesc"
            :content="handleCalName(item, true)"
            placement="bottom"
          >
            <div
              class="portal-font-color-lv3 portal-primary-color-hover-lv1"
              style="margin-left: 4px; cursor: pointer"
            >
              <i class="iconfont icon-icon-tip" style="font-size: 12px"></i>
            </div>
          </we-tooltip>
        </div>
      </div>
    </ContainerScroll>
  </div>
</template>

<script>
import { clickTrack } from "../mixins/track.js";
export default {
  props: ["external", "router", "calendarLists"],
  mixins: [clickTrack],
  computed: {
    lists() {
      return this.calendarLists.concat();
    },
    heightStyle() {
      const len = this.calendarLists.length;
      return len > 8 ? "height: 240px" : "";
    },
  },
  methods: {
    handleCalName(item, isDesc) {
      if (item.wid == "1") {
        return this.$Lan(
          "CUS_CARD_GXCAL",
          isDesc ? "selfCalendarDesc" : "selfCalendar",
          "个人日历"
        );
      } else if (item.wid == "2") {
        return this.$Lan(
          "CUS_CARD_GXCAL",
          isDesc ? "groupCalendarDesc" : "groupCalendar",
          "群组日历"
        );
      }
      return isDesc ? item.calDesc : item.calName;
    },
    handleChange() {
      const subscribe = [],
        unscribe = [];
      this.lists.forEach((item) => {
        if (item.subscribe) {
          subscribe.push(item.wid);
        } else {
          unscribe.push(item.wid);
        }
      });
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "subscribe",
          param: {
            subscribe: subscribe.join(","),
            unSubscribe: unscribe.join(","),
          },
        },
        (res) => {
          if (res && res.errcode === "0") {
            this.$emit("update");
          }
        }
      );
      this.handleClickTrack({
        infoType: 12,
      }); // 点击埋点
    },
  },
};
</script>

<style lang="less" scoped>
.subscribePop {
  padding-right: 6px;
  .subscribePop__row {
    display: flex;
    align-items: center;
    &.external {
      cursor: pointer;
    }
    &:not(:last-child) {
      margin-bottom: 12px;
    }
    .subscribePop__tip {
      flex-shrink: 0;
    }
    .subscribePop__name {
      font-size: 12px;
      line-height: 20px;
    }
    /deep/.we-checkbox {
      white-space: pre-wrap;
      display: flex;
      align-items: center;
    }
    /deep/.we-checkbox__label {
      font-size: 12px;
      line-height: 20px;
      padding-left: 6px;
    }
  }
}
</style>
