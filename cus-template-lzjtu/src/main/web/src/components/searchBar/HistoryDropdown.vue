<template>
  <div class="his__dropDown__wrap">
    <div class="his__dropDown-info clear portal-font-color-lv3">
      {{ $Lan('CUS_TEMPLATE_LZJTU', 'historySearch', '历史搜索') }}
      <i
        class="we-icon-delete his__dropDown-delete portal-primary-color-hover-lv1"
        @click="deleteHis"
      ></i>
    </div>
    <div
      class="his__dropDown-list ellipsis portal-primary-color-hover-lv1 portal-primary-backgroundcolor-hover-lv5"
      v-for="(item, index) in historyData"
      :key="index"
      @click="openSearchResult(item)"
    > {{ item.searchKey }} </div>
  </div>
</template>

<script>
export default {
  props: ["historyData"],
  methods: {
    openSearchResult(item) {
      this.$emit("change-search", item.searchKey);
    },
    deleteHis() {
      window.shell.execTemplateMethod("clearSearchHis", {}, (data) => {
        if (data.errcode === "0") {
          this.$emit("clear-his");
        }
      });
    },
  },
};
</script>

<style scoped>
.his__dropDown-info {
  font-size: 12px;
  padding: 0 12px;
  margin-bottom: 8px;
}
.his__dropDown-list {
  cursor: pointer;
  padding: 8px 12px;
  font-size: 14px;
}
.his__dropDown-delete {
  float: right;
  font-size: 16px;
  cursor: pointer;
}
</style>