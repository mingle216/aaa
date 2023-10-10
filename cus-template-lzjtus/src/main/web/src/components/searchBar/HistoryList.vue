<template>
  <div class="history__list">
    <span class="history-tag" >{{ $Lan($TEMPLATE_NAME, 'historyTxt', '历史搜索') }}：</span>
    <span
      class="history__list__item ellipsis"
      v-for="item in historyData"
      :key="item.wid"
      :title="item.searchKey"
      @click="openSearchResult(item)"
    >
      {{ item.searchKey.length > 6 ? `${item.searchKey.slice(0, 6)}...` : item.searchKey }}
    </span>
  </div>
</template>

<script>
export default {
  props: ["historyData"],
  methods: {
    openSearchResult(item) {
      this.$emit('change-search', item.searchKey)
    }
  }
};
</script>
<style lang="less"  scoped>
.history__list {
  display: flex;
  justify-content: left;
  width: 100%;
  color: #fff;
  margin-top: 14px;
  font-size: 16px;
  line-height: 24px;
  .history-tag{
    // width: 80px;
    min-width: 80px;
    white-space: nowrap;
    padding-right: 12px;
  }
}
.history__list__item {
  cursor: pointer;
  min-width: 0;
  /* max-width: 80px; */
}
/* .history__list__item:hover {
  text-decoration: underline;
} */
.history__list__item:not(:last-child) {
  margin-right: 16px;
}
</style>
