<template>
  <div class="gateway-rows">
    <we-row
      v-for="(item, index) in rowsData.filter(d => d.columns.length > 0)"
      :key="index"
      :gutter="40"
      :class="{
        row_card: true,
        detail_row: templateCode === 'detail'
      }"
    >
      <we-col
        v-for="(el, colIndex) in item.columns"
        :key="colIndex"
        :span="el.width"
      >
        <template v-for="(value, key) in el">
          <component
            v-if="type.includes(key)"
            :is="key"
            :key="`${key}-${value.cardWid || pageCode}`"
            :cardData="value"
            :cardsData="value.cards"
            :rowsData="value"
            :colWidth="el.width"
            :code="index"
          ></component>
        </template>
      </we-col>
    </we-row>
  </div>
</template>
<script>
/* eslint-disable no-debugger */
import card from "./card";
import cards from "./cards";
export default {
  name: "rows",
  components: { card, cards },
  props: {
    rowsData: Array,
    templateCode: String
  },
  data() {
    return {
      type: ["card", "cards", "rows"],
      pageCode: window.shell.getRoute()
    };
  },
  watch: {
    rowsData() {
      this.pageCode = window.shell.getRoute();
    }
  }
};
</script>
<style lang="less" scoped>
.gateway-rows {
  width: calc(100% - 24px);
  margin: 0 auto;
  .row_card {
    background: #fff;
    margin-bottom: 12px;
    border-radius: 16px;
    box-shadow: 0px 2px 4px 0px #0031630A;
  }
  .detail_row {
    background: transparent;
  }
}
.position-relateive {
  position: relative;
  overflow: hidden;
}
.mr-tb-30 {
  margin: 30px 0;
}
/deep/ .we-card {
  width: 100%;
}
</style>
