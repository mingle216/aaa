<template>
  <div class="header">
    <div class="left-info">
      <span class="blue-div"></span>
      <span class="yellow-div"></span>
      <span class="title">{{ title }}</span>
    </div>
    <slot name="more"></slot>
  </div>
</template>
<script>
export default {
  props: {
    title: {
      type: String,
      default: ""
    }
  },
  data() {
    return {

    }
  }
}
</script>
<style lang="less" scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 50px;
  background: #fff;
  padding: 14px 16px;
  border-radius: 8px 8px 0 0;
  box-shadow: inset 0px -0.5px 0px #eeeeee;

  .left-info {
    display: flex;
    align-items: center;
  }

  .title {
    font-weight: 600;
    font-size: 16px;
    line-height: 22px;
    color: #262626;
    margin-left: 6px;
  }

  .blue-div {
    display: inline-block;
    position: relative;
    width: 6px;
    height: 14px;
    background: #238bff;
    right: -5px;
    bottom: 2px;
    z-index: 1;
  }

  .yellow-div {
    display: inline-block;
    position: relative;
    left: 0px;
    bottom: -2px;
    width: 10px;
    height: 10px;
    opacity: 0.6000000238418579;
    background: #fdd000;
  }
}
</style>