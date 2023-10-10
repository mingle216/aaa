<template>
  <div class="timeTitleAbstractVal">
    <div style="width: 52px; margin-right: 12px; font-family: Arial;">
      <div class="card-day" :class="cardDayBackground">
        {{ itemInfo.FBSJ ? itemInfo.FBSJ.split(' ')[0].split('-')[2] : "-" }}
      </div>
      <div class="card-month" :class="cardMonthBackground">
        {{ itemInfo.FBSJ ? itemInfo.FBSJ.split(' ')[0].split('-')[1] + "月" : "-" }}
      </div>
    </div>
    <div class="timeTitleAbstractVal__right">
      <div
        class="timeTitleAbstractVal__title"
        @mouseenter="mouseenter"
        @mouseleave="mouseleave"
      >
        <a
          class="ellipsis newsLink-visited"
          :class="titleColor"
          target="_blank"
          @click="()=>{$emit('go-link',itemInfo)}"
          style="max-width: 1000px;white-space: pre;line-height: 24px;height: 24px;"
          >{{ itemInfo.GGBT && itemInfo.GGBT.replace(/\n/g, ' ') }}</a
        >
        <div class="tag topTag" v-if="itemInfo.isTop" alt="" />
        <div class="tag newTag" v-if="itemInfo.newDisplay" alt="" />
      </div>
      <div class="timeTitleAbstractVal__content ellipsis portal-font-color-lv3" style="text-align: right;">
        {{ itemInfo.FBBM_DISPLAY}}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    itemInfo: Object,
    config: Object,
    lastRow: Boolean,
  },
  data() {
    return {
      cardDayBackground: "card-day-background portal-font-color-lv1",
      cardMonthBackground: "card-month-background portal-font-color-lv3",
      titleColor: "",
    };
  },
  methods: {
    mouseenter() {
      this.cardDayBackground = "portal-primary-backgroundcolor-lv1 white-font";
      this.cardMonthBackground =
        "portal-primary-backgroundcolor-lv2 white-font";
      this.titleColor = "portal-primary-color-lv1";
    },
    mouseleave() {
      this.cardDayBackground = "card-day-background portal-font-color-lv1";
      this.cardMonthBackground = "card-month-background portal-font-color-lv3";
      this.titleColor = "";
    },
  },
};
</script>

<style scoped lang="less">
.newsLink-visited{
  cursor: pointer;
}
.timeTitleAbstractVal {
  height: 76px;
  display: flex;
  align-items: center;
  overflow: hidden;
  .timeTitleAbstractVal__right {
    flex: 1;
    min-width: 0;
    .timeTitleAbstractVal__title {
      font-size: 16px;
      display: flex;
      align-items: center;
    }
    .tag {
      width: 28px;
      height: 14px;
      margin-left: 6px;
      flex-shrink: 0;
      background-size: 100% 100%;
    }
    .topTag {
      background-image: url("../assets/topTag.svg");
    }
    .newTag {
      background-image: url("../assets/newTag.svg");
    }
    .timeTitleAbstractVal__content {
      font-size: 14px;
      line-height: 22px;
      margin-top: 2px;
    }
  }
}

.card-day {
  height: 26px;
  line-height: 1.7;
  font-size: 16px;
  text-align: center;
  border-radius: 4px 4px 0 0;
  font-weight: bold;
}

.card-day-background {
  background-color: #e3e3e3;
}

.card-month {
  line-height: 26px;
  font-size: 12px;
  text-align: center;
  border-radius: 0 0 4px 4px;
}

.card-month-background {
  background-color: #f5f5f5;
}

.white-font {
  color: white;
}
</style>
