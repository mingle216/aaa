<template>
  <div class="backtop__wrap" :class="[rightOffset ? 'backtop__wrapIE' : '']">
    <div class="backtop portal-primary-color-lv1 portal-primary-backgroundcolor-hover-lv1" @click="handleBack">
      <i
        class="iconfont icon-fanhuidingbu"
        style="font-size: 20px; "
      />
      <!-- <div style="word-break: break-all; line-height: 20px; width: 100%">{{ $Lan('CUS_TEMPLATE_OFFICIAL', 'backtop', '回到顶部') }}</div> -->
    </div>
  </div>
</template>

<script>
import { isIE, isFirefox } from '../utils/util.js'
export default {
  data() {
    return {
      rightOffset: isIE() || isFirefox()
    }
  },
  methods: {
    handleBack() {
      this.$emit('handle-back')
    }
  }
}
</script>

<style scoped>
.backtop__wrap {
  position: fixed;
  right: 32px;
  z-index: 999;
  top: calc(50% + 233px + 110px);
  transform: translate(0, -39px);
}
.backtop__wrapIE {
  right: 17px;
}
.backtop {
  width: 50px;
  height: 50px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  letter-spacing: 0;
  text-align: center;
  box-shadow: 0 0 18px 0 rgba(159,176,196,0.20);
  background: #fff;
  border-radius: 3px;
  padding: 18px 8px;
}
.backtop:hover {
  color: #fff!important;
}
</style>
