<template>
  <div class="title block-title">
    <span class="title-line portal-primary-backgroundcolor-lv1"></span>
    {{ title }}
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
    },
  },
};
</script>

<style lang="less" scoped>
.title {
  font-size: 18px;
  font-weight: bold;

  &-line {
    width: 4px;
    height: 14px;
    margin-right: 6px;
    display: block;
    float: left;
    margin-top: 3px;
    border-radius: 2px;
  }
  // &::before {
  //   content: "";
  //   width: 4px;
  //   height: 14px;
  //   position: absolute;
  //   top:2px;
  //   left: -8px;
  //   background: #307afb;
  //   border-radius: 2px;
  // }
}
</style>