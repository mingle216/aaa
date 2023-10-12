<template>
  <div class="timeTitleAbstractVal">
    <div style="width: 62px; margin-right: 12px; font-family: Arial;border: 1px solid #F0F0F0;border-radius: 4px;">
      <div class="card-day" :class="cardDayBackground">
        {{ itemInfo.day ? itemInfo.day : "-" }}
      </div>
      <div class="card-month" :class="cardMonthBackground">
        {{ itemInfo.month ? itemInfo.month + "月" : "-" }}
      </div>
    </div>
    <div class="timeTitleAbstractVal__right">
      <div
        class="timeTitleAbstractVal__title"
        @mouseenter="mouseenter"
        @mouseleave="mouseleave"
      >
        <a
          class="ellipsis"
          :class="[itemInfo.read ? 'portal-font-color-lv3' : '', titleColor]"
          target="_blank"
          @click="
            () => {
              $emit('go-link', itemInfo);
            }
          "
          style="max-width: 1000px;white-space: pre;line-height: 24px;height: 24px;"
          >{{ itemInfo.title && itemInfo.title.replace(/\n/g, " ") }}</a
        >
        <div class="tag topTag" v-if="itemInfo.isTop" alt="" />
        <div class="tag newTag" v-if="itemInfo.newDisplay" alt="" />
      </div>
      <div class="timeTitleAbstractVal__content ellipsis portal-font-color-lv3">
        {{ itemInfo.abstractVal || itemInfo.contents || "-" }}
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
    },
    mouseleave() {
      this.cardDayBackground = "card-day-background portal-font-color-lv1";
      this.cardMonthBackground = "card-month-background portal-font-color-lv3";
    },
  },
};
</script>

<style scoped lang="less">
.newsLink-visited {
  cursor: pointer;
}
.timeTitleAbstractVal {
  height: 76px;
  display: flex;
  align-items: center;
  overflow: hidden;
  margin-top: 16px;
  .timeTitleAbstractVal__right {
    flex: 1;
    min-width: 0;
    .timeTitleAbstractVal__title {
      font-size: 16px;
      line-height: 24px;
      display: flex;
      align-items: center;
      cursor: pointer;
      &:hover{
        color: #000!important;
        font-weight: 700;
      }
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
      margin-top: 10px;
    }
  }
}

.card-day {
  height: 34px;
  line-height: 34px;
  text-align: center;
  border-radius: 4px 4px 0 0;
  font-size: 24px;
  color: #262626;
}

.card-day-background {
  background: #F5F8FD;
}

.card-month {
  line-height: 28px;
  font-size: 12px;
  text-align: center;
  border-radius: 0 0 4px 4px;
  color: #595959;

}

.card-month-background {
  background-color: #fff;
}

.white-font {
  color: white;
}
</style>
