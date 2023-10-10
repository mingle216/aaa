<template>
  <p
    class="more-con portal-font-color-lv3"
    @click="clickHandle(isOpen)"
  >
    <template v-if="isOpen">
      {{ $Lan(lanFunName, "showMore", "展开更多") }}
      <we-icon name="arrow-down" />
    </template>
    <template v-else>
      {{ $Lan(lanFunName, "packUpMore", "收起更多") }}
      <we-icon name="arrow-up" />
    </template>
  </p>
</template>

<script>
export default {
  props: {
    isOpen: {
      type: Boolean,
      default: false,
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
  padding-top: 12px;
  line-height: 20px;
  text-align: center;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  i {
    margin-left: 5px;
  }
}
</style>
