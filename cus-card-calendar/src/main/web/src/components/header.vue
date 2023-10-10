<template>
  <div
    ref="Header"
    class="cardCalendar__header portal-primary-backgroundcolor-lv5 portal-primary-after-backgroundcolor-lv5"
  >
    <div
      class="cardCalendar__header-block portal-primary-backgroundcolor-lv5"
    ></div>
    <div
      ref="HeaderScroll"
      class="cardCalendar__header-scroll"
      :class="[isScroll ? 'scroll' : '']"
    >
      <span
        >{{
          $Lan(
            "SYS_CARD_CALENDAR",
            "term",
            `${headerInfo.xn}-${headerInfo.xn + 1}学年`,
            {
              start: headerInfo.xn,
              end: headerInfo.xn + 1,
            }
          )
        }}&nbsp;{{
          $Lan(
            "SYS_CARD_CALENDAR",
            headerInfo.xq == 1 ? "firstSemester" : "sencondSemester",
            headerInfo.xq == 1 ? "第一学期" : "第二学期"
          )
        }}</span
      >
      <span style="margin:0 4px" v-if="headerInfo.weekNumberUnder">{{
        $Lan(
          "SYS_CARD_CALENDAR",
          "underWeek",
          `第${headerInfo.weekNumberUnder}周(本)`,
          {
            under: headerInfo.weekNumberUnder,
          }
        )
      }}</span>
      <span v-if="headerInfo.weekNumberUnder && headerInfo.weekNumberPost"
        >|</span
      >
      <span style="margin:0 4px" v-if="headerInfo.weekNumberPost">{{
        $Lan(
          "SYS_CARD_CALENDAR",
          "postWeek",
          `第${headerInfo.weekNumberPost}周(研)`,
          {
            post: headerInfo.weekNumberPost,
          }
        )
      }}</span>
    </div>
  </div>
</template>

<script>
export default {
  props: ["headerInfo"],
  data() {
    return {
      isScroll: false,
    };
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.calIsScroll);
  },
  mounted() {
    window.addEventListener("resize", this.calIsScroll);
    this.$nextTick(function() {
      this.calIsScroll();
    });
  },
  methods: {
    calIsScroll() {
      this.isScroll =
        this.$refs.HeaderScroll.scrollWidth >
        this.$refs.Header.offsetWidth - 40;
    },
  },
};
</script>

<style lang="less" scoped>
.cardCalendar__header {
  height: 54px;
  line-height: 54px;
  font-size: 16px;
  padding: 0 20px;
  border-radius: 8px 8px 0 0;
  overflow: hidden;
  position: relative;
  .cardCalendar__header-block {
    position: absolute;
    top: 0;
    height: 100%;
    z-index: 1;
    left: 0;
    width: 20px;
  }
  &:after {
    content: "";
    position: absolute;
    top: 0;
    bottom: 0;
    z-index: 1;
    right: 0;
    width: 20px;
  }
  .cardCalendar__header-scroll {
    display: inline-block;
    white-space: nowrap;
    &.scroll {
      animation: headerScroll 10s infinite normal linear;
    }
  }
}
@keyframes headerScroll {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-90%);
  }
}
</style>
