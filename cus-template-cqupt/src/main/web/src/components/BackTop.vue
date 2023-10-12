<template>
  <div class="backtop__wrap" :class="[rightOffset ? 'backtop__wrapIE' : '']">
    <div
      class="backtop portal-font-color-lv1 portal-primary-color-hover-lv1"
      @click="handleBack"
    >
      <span class="icon iconfont icon-fanhuidingbu" style="font-size: 20px" />
    </div>
  </div>
</template>

<script>
import { isIE, isFirefox } from "../utils/util.js";
export default {
  data() {
    return {
      rightOffset: isIE() || isFirefox(),
    };
  },
  methods: {
    handleBack() {
      this.$emit("handle-back");
    },
  },
};
</script>

<style scoped>
.backtop__wrap {
  position: fixed;
  right: 16px;
  z-index: 999;
  top: calc(50% + 233px);
  transform: translate(0, -39px);
}
.backtop__wrapIE {
  right: 17px;
}
.backtop {
  width: 52px;
  height: 52px;
  cursor: pointer;
  border-radius: 26px;
  background: #ffffff;
  box-shadow: 0px 0px 16px 0px #41414121;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  letter-spacing: 0;
  text-align: center;
}
</style>
