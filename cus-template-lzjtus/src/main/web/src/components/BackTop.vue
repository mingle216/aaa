<template>
  <div class="backtop__wrap" :class="[rightOffset ? 'backtop__wrapIE' : '']">
    <div
      class="backtop portal-font-color-lv1 portal-primary-backgroundcolor-hover-lv1"
      @click="handleBack"
    >
      <i class="we-icon-arrow-up" style="font-size: 20px; margin-bottom: 4px" />
      <span class="backtop-txt">
        {{ $Lan($TEMPLATE_NAME, "backtop", "回到顶部") }}
      </span>
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
  right: 8px;
  z-index: 999;
  top: calc(50% + 233px);
  transform: translate(0, -39px);
}
.backtop__wrapIE {
  right: 17px;
}
.backtop {
  width: 66px;
  height: 77px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  letter-spacing: 0;
  text-align: center;
  box-shadow: 0 0 18px 0 rgba(159, 176, 196, 0.2);
  background: #fff;
  border-radius: 3px;
  word-wrap: break-word;
  white-space: normal;
  word-break: break-all;
  /* display: block; */
}
.backtop-txt {
  word-wrap: break-word;
  white-space: normal;
  word-break: break-all;
  display: block;
  padding: 0 4px;
  width: 64px;
}
.backtop:hover {
  color: #fff !important;
}
</style>
