<template>
  <div :id="id" class="result-con" v-if="showTitle">
    <div class="con-header portal-font-color-lv1">
      <div>
        <span class="header-text">{{ title }}</span>
        <span>({{ total }})</span>
      </div>
      <slot name="title-opt"></slot>
    </div>
    <div class="result-con-main">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    id: {
      type: String,
      default: "",
    },
    title: {
      type: String,
      default: "",
    },
    total: {
      type: Number,
      default: 0,
    },
    showTitle: {
      type: Boolean,
      default: true
    }
  },
};
</script>

<style lang="less" scoped>
.result-con {
  padding-bottom: 40px;
  .con-header {
    height: 48px;
    line-height: 48px;
    font-size: 18px;
    font-weight: bold;
    border-bottom: 1px solid #e7edf1;
    display: flex;
    justify-content: space-between;
    .header-text {
      margin-right: 10px;
    }
  }
}
</style>
