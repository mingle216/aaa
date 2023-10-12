<template>
  <div class="ev-filter" :class="{ 'fixed-filter': isFixed }">
    <div
      class="btn"
      :class="[
        markType === 0 ? 'portal-primary-color-lv1' : 'portal-font-color-lv1',
      ]"
      @click="setMarkType(0)"
    >
      {{ $Lan(lanFunName, "markType0", "全部") }}
    </div>
    <div
      class="btn"
      :class="[
        markType === 1 ? 'portal-primary-color-lv1' : 'portal-font-color-lv1',
      ]"
      @click="setMarkType(1)"
    >
      {{ $Lan(lanFunName, "markType1", "好评") }}
    </div>
    <div
      class="btn"
      :class="[
        markType === 2 ? 'portal-primary-color-lv1' : 'portal-font-color-lv1',
      ]"
      @click="setMarkType(2)"
    >
      {{ $Lan(lanFunName, "markType2", "差评") }}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    markType: {
      type: Number,
      default: 0,
    },
    isFixed: {
      type: Boolean,
      default: false,
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  data() {
    return {};
  },
  methods: {
    setMarkType(num) {
      this.$emit("filter-change", num);
    },
  },
};
</script>

<style lang="less" scoped>
.ev-filter {
  height: 40px;
  background: #f5f5f5;
  border-radius: 4px;
  border-radius: 4px;

  display: flex;

  &.fixed-filter {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    z-index: 10;
    margin-top: 0;
    padding: 0 20px;
  }
  .btn {
    white-space: nowrap;
    margin-left: 20px;
    height: 40px;
    line-height: 40px;
    // width: 62px;
    font-size: 16px;
    text-align: center;
    cursor: pointer;
  }
}
</style>
