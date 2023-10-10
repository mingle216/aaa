<template>
  <we-action-sheet
    class="custom-action-sheet"
    :class="{ active: asValue }"
    v-model="asValue"
    get-container="body"
    v-on="$listeners"
    v-bind="$attrs"
    @close="closeAs"
  >
    <slot></slot>
  </we-action-sheet>
</template>

<script>
export default {
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    value: {
      type: Boolean,
      default: false,
    },
  },
  watch: {
    value(val) {
      this.asValue = val;

      // if (val && window.shell.isIphone()) {
      //   var str = navigator.userAgent.toLowerCase();
      //   var ver = str.match(/cpu iphone os (.*?) like mac os/);
      //   if (parseInt(ver[1]) < 13) {
      //     this.$nextTick(() => {
      //       this.scrollPaneTouchHandle();
      //     });
      //   }  
      // }else if(!val){
      //   let $node = this.$vnode.elm.querySelector(
      //       ".we-action-sheet__content"
      //     );
      //     $node.removeEventListener("touchstart", this.touchStartHandle, true);
      //     $node.removeEventListener("touchmove", this.touchMoveHandle, true);
      // }
    },
  },
  computed: {},
  mounted() {},
  data() {
    return {
      asValue: false,
      conHeight: "auto",
      scrollTop: 0,
      touchStart: 0,
    };
  },
  methods: {
    closeAs() {
      this.$emit("change", false);
      this.$emit("close", false);
    },
    scrollPaneTouchHandle() {
      //  console.log(this.$vnode.elm)
      let $node = this.$vnode.elm.querySelector(".we-action-sheet__content");
      $node.addEventListener("touchstart", this.touchStartHandle, true);
      $node.addEventListener("touchmove", this.touchMoveHandle, true);
      //  $node.addEventListener('touchend', this.touchEndHandle, true);
    },
    touchStartHandle(ev) {
      let $node = this.$vnode.elm.querySelector(".we-action-sheet__content");
      console.log($node.scrollTop);
      this.scrollTop = $node.scrollTop;

      this.touchStart = ev.changedTouches[0].clientY;
      // $node.scrollTop =$node.scrollTop +1;
    },
    touchMoveHandle(ev) {
      let $node = this.$vnode.elm.querySelector(".we-action-sheet__content");
      let touch_y = ev.changedTouches[0].clientY;
      console.log(touch_y);
      $node.scrollTop = this.scrollTop + (this.touchStart - touch_y);
    },
  },
};
</script>

<style lang="less" >
.custom-action-sheet {
  max-height: 90vh;
  height: 90vh;
  // z-index: 10000  !important;
  &.active {
    .we-action-sheet__content {
      z-index: 10000;
      // -webkit-overflow-scrolling: touch;
      .as-content {
        position: relative;
        overflow: hidden;
        width: 100%;
      }
    }
  }
  .we-action-sheet__header {
    text-align: left;
    padding-left: 17px;
    padding-right: 54px;
    height: 56px;
    line-height: 56px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    border-bottom: 1px solid #e7edf1;
    font-weight: bold;
    font-size: 18px;
  }
}
</style> 