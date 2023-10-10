<template>
  <div class="one-card position-relateive">
    <div
      class="one-card-sys-title portal-font-color-lv1 "
      v-if="cardData.showTitle"
    >
      <span class="card-name ellipsis"
        >
        {{ cardTitle }}
        <span style="margin-left: 4px"> 
         {{getDataNum(cardData) }}
        </span>
        </span>
      <template
        v-if="
          cardData.layoutCardTitle.layoutCardLink &&
          cardData.layoutCardTitle.layoutCardLink.length
        "
      >
        <card-link
          :cardLinkData="cardData.layoutCardTitle.layoutCardLink"
          :isshow="true"
        ></card-link>
      </template>
    </div>
    <remote-component
      :url="getCardUrl(cardData)"
      :router="cardData"
      :dataNum="num[cardData.cardId] && num[cardData.cardId]['num']"
      :colWidth="colWidth"
      :i18n="$i18n"
    />
  </div>
</template>

<script>
/* eslint-disable no-debugger */
import getCardUrl from "../minxins/index.js";

export default {
  name: "card",
  mixins: [getCardUrl], //混入
  props: {
    cardData: Object,
    colWidth: Number,
  },
  computed: {
    cardTitle() {
      const zhTitle = this.cardData.layoutCardTitle.cardTitle || ''
      const cardTitleLang = this.cardData.layoutCardTitle && this.cardData.layoutCardTitle.cardTitleLang || []
      const temp = cardTitleLang.find(el => el.langCode === this.$i18n.locale)
      return temp && temp.langName || zhTitle
    }
  },
  data() {
    return {};
  },
  created() {
    this.getNum([this.cardData]);
  },
};
</script>

<style lang="less" scoped>
 
.one-card-sys-title {
  border-bottom: 1px  solid #E7EDF1;
  display: flex;
  padding: 0 17px;
  display: flex;
  height: 48px;
  align-items: center;
  justify-content: space-between;
  color: #646566;
  font-size: 18px;
  // line-height: 48px;
  
  box-sizing: border-box;
  .card-name{
    height: 100%;
    position: relative;
    flex: 1;
    width: 0;
    font-weight: bold;
    padding: 14px 0;
    &:after{
      content: '';
      position: absolute;
      bottom: 0px;
      height: 2px;
      left:3px;
      right:3px;
    }
  }
}
.one-card-sys-title  {
  cursor: pointer;
}
</style>
