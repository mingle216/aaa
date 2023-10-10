<template>
  <!--咨询电话-->
  <div>
    <div class="title1 portal-font-color-lv1">{{ item.fieldName }}</div>
    <div
      v-for="(it, i) in item.fieldValue"
      :key="i + it.phone"
      class="tel-item"
    >
      <we-tooltip
        class="item"
        effect="dark"
        placement="bottom-start"
        :open-delay="900"
      >
        <span slot="content">{{ it.phone }}</span>
        <span class="tel-left">{{ it.phone }}</span>
      </we-tooltip>
      <span class="tel-num">{{ it.comments }}</span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      default: () => {
        return {};
      },
    },
    fieldName: String,
  },
};
</script>

<style lang="less" scoped>
.tel-item {
  height: 48px;
  border-bottom: 1px solid #f0f0f0;
  line-height: 48px;
  position: relative;
  &:last-child {
    border: none;
  }
}
.tel-num {
  display: inline-block;
  position: absolute;
  left: 145px;
}
.tel-left {
  width: 130px;
  display: inline-block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
