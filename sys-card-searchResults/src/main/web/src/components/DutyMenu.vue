<template>
  <layout
    :id="id"
    :title="$Lan(lanFunName, 'responsibilityList', '责任清单')"
    :total="10"
  >
    <div class="item-list">
      <dutyItem v-for="item in 10" :key="item" :lan-fun-name="lanFunName" />
    </div>
  </layout>
</template>

<script>
export default {
  props: {
    id: {
      type: String,
      default: "duty" + new Date().getTime(),
    },
    keyword: {
      type: String,
      default: "",
    },
    list: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
  data() {
    return {
      lanFunName: this.router.cardId,
    };
  },
};
</script>

<style lang="less" scoped></style>
