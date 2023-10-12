<template>
  <div class="blank">
    <card-layout :cardLayout="cardLayout"></card-layout>
  </div>
</template>

<script>
import CardLayout from "../cardLayout";
export default {
  components: {
    CardLayout
  },
  props: {
    pageData: Object
  },
  computed: {
    cardLayout() {
      return JSON.parse(this.pageData?.pageInfoEntity?.cardLayout || "[]");
    }
  }
};
</script>

<style lang="less" scoped>
  .blank {
    padding-top: 12px;
  }
</style>
