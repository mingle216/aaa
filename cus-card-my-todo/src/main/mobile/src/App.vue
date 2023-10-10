<i18n>
{
  "en_US": {
    "emptyTip": "No Data"
  },
  "zh_CN": {
    "emptyTip": "暂无数据"
  }
}
</i18n>
<template>
  <AutoContainer>
    <div>
      <EmptyCon
        :tip="$t('emptyTip')"
        :height="200"
      ></EmptyCon>
    </div>
  </AutoContainer>
</template>

<script>
export default {
  name: "App",
  components: {},
  props: {
    index: Number,
    router: Object,
  },
  watch: {},
  data() {
    const { cardWid, cardId } = this.router;
    return {
      card: {
        cardWid,
        cardId,
      }
    };
  },
  created() {},
  mounted() {},
  beforeDestroy() {},
  computed: {},
  methods: {},
};
</script>

<style lang="less" scoped>
</style>