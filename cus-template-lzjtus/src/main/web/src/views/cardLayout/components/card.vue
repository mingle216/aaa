<template>
  <div
    class="gateway-card position-relateive"
    :style="{
      'margin-top': `${Number(containerMargin) || 0}px`,
      'margin-bottom': `${
        !isServiceItemCount ? (isLastItem ? '24' : Number(containerMargin) || 0) : '-8'
      }px`,
    }"
    :class="{ 'mr-tb-0': isClearTbDistance(cardData) }"
  >
    <h3
      class="card-sys-title portal-font-color-lv1 ellipsis"
      v-if="cardData.showTitle"
    >
      <span
        >{{ cardTitle }}
        <span style="margin-left: 4px">{{
          num[cardData.cardId] && num[cardData.cardId]["num"]
        }}</span></span
      >
      <template
        v-if="
          cardData.layoutCardTitle.layoutCardLink &&
          cardData.layoutCardTitle.layoutCardLink.length
        "
      >
        <card-link
          :cardLinkData="cardData.layoutCardTitle.layoutCardLink"
          :cardData="cardData"
          :isshow="true"
        ></card-link>
      </template>
    </h3>
    <remote-component
      :url="getCardUrl(cardData)"
      :router="cardData"
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
    containerMargin: String,
    isServiceItemCount: Boolean,
    isLastItem: Boolean,
  },
  data() {
    return {};
  },
  computed: {
  cardTitle() {
      const zhTitle = this.cardData.layoutCardTitle.cardTitle || ''
      const cardTitleLang = this.cardData.layoutCardTitle && this.cardData.layoutCardTitle.cardTitleLang || []
      const temp = cardTitleLang.find(el => el.langCode === this.$i18n.locale)
      return temp && temp.langName || zhTitle
    }
    },
  created() {
    this.getNum([this.cardData]);
  },
  methods: {
    // 需要去掉上下边距的卡片
    isClearTbDistance({ cardId }) {
      let arr = ["SYS_CARD_PROCESSINGPROGRESS"];
      return arr.includes(cardId);
    },
  },
};
</script>

<style scoped>
.mr-tb-0 {
  margin: 0 !important;
}
/* .gateway-card {
  overflow-x: auto;
} */
.card-sys-title {
  border-bottom: 1px #f0f0f0 solid;
  padding: 13px 0px 15px 0px;
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}
.card-sys-title span {
  cursor: pointer;
}
</style>
