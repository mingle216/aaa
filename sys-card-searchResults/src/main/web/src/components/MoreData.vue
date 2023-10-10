<template>
  <div
    class="more-con portal-primary-color-lv1 portal-primary-backgroundcolor-hover-lv5"
    @click="clickHandle(isOpen)"
  >
    <template v-if="isOpen">
      <i class="more-icon we-icon-arrow-down"></i>
      {{ $Lan(lanFunName, "spread", "展开") }}
    </template>
    <template v-else>
      <i class="more-icon we-icon-arrow-up"></i>
      {{ $Lan(lanFunName, "packUp", "收起") }}
    </template>
  </div>
</template>

<script>
export default {
  props: {
    isOpen:{
      type:Boolean,
      default:false
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  methods: {
    clickHandle(ev) {
      this.$emit("click", ev);
    },
  },
};
</script>

<style lang="less" scoped>
.more-con {
  height: 46px;
  line-height: 46px;
  text-align: center;
  background-color: #f5f5f5;
  font-size: 14px;
  cursor: pointer;
}
</style>
