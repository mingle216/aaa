<template>
  <div ref="autoCon" class="auto-con" :style="autoHeight">
    <vueScroll
      ref="conScroll"
      :max-height="maxHeight"
      :size="scConfig.size"
      :rail-bg-color="scConfig.railBgColor"
      :bar-bg-color="scConfig.barBgColor"
      :bar-keep-show="scConfig.barKeepShow"
      :bar-min-len="scConfig.barMinLen"
      :scroll-duration="scConfig.scrollDuration"
      :speed="scConfig.speed"
      :scrolling-x="scConfig.scrollingX"
      :scrolling-y="scConfig.scrollingY"
      :scroll-cover="scConfig.scrollCover"
      @vshandle-resize="resizeHandle"
      @vshandle-scroll="handleScroll"
    >
      <!-- 默认插槽  -->
      <slot />
    </vueScroll>
  </div>
</template>

<script>
export default {
  name: "AutoContainer",
  components: {},
  props: {
    /**
     * 容器类型 0：代表自适应（无最大限制,不产生滚动条）1：代表自适应（有最大限制）2：固定高度
     */
    conType: {
      type: [String, Number],
      default: 0,
    },
    /**
     * 容器高度，type为0时该值无效，type为1时最大高度，type为2时固定高度
     */
    conHeight: {
      type: [String, Number],
      default: 500,
    },
    /**
     * 容器最大高度，type为0时该值有效效，其他情况下无效,默认值0表示不限制
     */
    conMaxHeight: {
      type: [Number, String],
      default: 1200,
    },
    /**
     * 滚动条参数，具体参照@wed-pro/container-scroll
     */
    scroll: Object,
  },
  data() {
    return {
      inHeight: 499.5,
      timeNum: 0,
      isIE: window.navigator.userAgent.indexOf("Trident") !== -1,
      scConfig: {
        size: 6, // 滚动条滑块大小
        railBgColor: "", // 滚动条滑槽颜色
        barBgColor: "rgba(191,191,191,0.4)", // 滚动条背景色
        barKeepShow: true, // 滚动条是否保持一直显示，true:一直显示，false:滚动时显示
        barMinLen: 0.2, // 滚动条最小长度
        scrollDuration: 500, // 滚动时间毫秒
        speed: 300, // 滚动速度
        scrollingX: true, // 是否显示横向滚动条
        scrollingY: false, // 是否显示纵向滚动条
        scrollCover: false, // 滚动条是否覆盖内容
      },
    };
  },

  computed: {
    autoHeight() {
      if (parseInt(this.conType) === 1) {
        return {
          maxHeight: this.conHeight + "px",
          height: this.inHeight + "px",
        };
      } else if (parseInt(this.conType) === 2) {
        return { height: this.conHeight + "px" };
      } else if (parseInt(this.conMaxHeight)) {
        return {
          maxHeight: this.conMaxHeight + "px",
          height: this.inHeight + "px",
        };
      } else {
        return { height: this.inHeight + "px" };
      }
    },
    maxHeight() {
      if (parseInt(this.conType) === 1 || parseInt(this.conType) === 2) {
        return parseInt(this.conHeight);
      } else if (parseInt(this.conType) === 1 && parseInt(this.conMaxHeight)) {
        return parseInt(this.conMaxHeight);
      }

      return null;
    },
  },
  watch: {
    conType: {
      handler() {
        this.scrollingYHandle();
      },
      immediate: true,
    },
    inHeight: {
      handler() {
        this.scrollingYHandle();
      },
      immediate: true,
    },
    scroll: {
      handler(val) {
        if (typeof val === "object") {
          if (parseInt(this.conType) === 0&&this.conMaxHeight===0) {
            val.scrollingY = false;
          } else {
            val.scrollingY = true;
          }
          this.scConfig = Object.assign(this.scConfig, val);
        }
      },
      immediate: true,
    },
  },
  mounted() {
    clearInterval(this.timeNum);
    this.timeNum = setInterval(() => {
      this.refresh();
    }, 50);
  },
  beforeDestroy() {
    clearInterval(this.timeNum);
  },
  methods: {
    scrollingYHandle() {
      if (
        (parseInt(this.conType) === 0 &&
          (this.inHeight < parseInt(this.conMaxHeight)) || parseInt(this.conMaxHeight) === 0))
       {
        this.scConfig = Object.assign(this.scConfig, { scrollingY: false });
      } else {
        this.scConfig = Object.assign(this.scConfig, { scrollingY: true });
      }
    },
    /**
     * 刷新容器
     */
    refresh() {
      if (this.$refs.conScroll) {
        const conEl = this.$refs.conScroll.$vnode.elm.querySelectorAll(
          ".vs-con-body"
        )[0];
        const tempH = conEl.offsetHeight || 500;
        // const tempW = conEl.offsetWidth
        // const conW = this.$refs.autoCon.offsetWidth

        if (tempH !== this.inHeight) {
          this.inHeight = this.isIE ? tempH + 4 : tempH;
          if (
            parseInt(this.conType) === 0 &&
            this.inHeight > parseInt(this.conMaxHeight) &&
            parseInt(this.conMaxHeight) !== 0
          ) {
            this.scConfig = Object.assign(this.scConfig, { scrollingY: true });
          }
          this.$refs.conScroll.refresh();
        }

        // if (tempW <= conW && this.scConfig.scrollingX) {
        //   this.scConfig = Object.assign(this.scConfig, { scrollingX: false })
        // } else if (tempW > conW && !this.scConfig.scrollingX) {
        //   this.scConfig = Object.assign(this.scConfig, { scrollingX: true })
        // }
      }
    },
    /**
     * 内容容器发生变化
     * @vuese
     */
    resizeHandle(vertical, horizontal, nativeEvent) {
      this.$emit("vshandle-resize", vertical, horizontal, nativeEvent);
    },
    /**
     * 滚动事件
     * * @vuese
     */
    handleScroll(vertical, horizontal, nativeEvent) {
      this.$emit("vshandle-scroll", vertical, horizontal, nativeEvent);
    },
    /**
     * 指定滚动条滚动到指定位置
     */
    scrollTo(params, speed, easing = "easeInQuad") {
      this.$refs.conScroll.scrollTo(params, speed, easing);
    },
  },
};
</script>

<style lang="less" scoped>
.auto-con {
  overflow: hidden;
  display: flex;
  & > * {
    flex: 1;
    width: 0 !important;
  }
}
</style>
