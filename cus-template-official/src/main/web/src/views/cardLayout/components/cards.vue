<template>
  <div class="gateway-card position-relateive">
    <template v-for="(card, cardIndex) in cardsData">
      <template
        v-if="
          card.layoutCardTitle.layoutCardLink &&
          card.layoutCardTitle.layoutCardLink.length
        "
      >
        <card-link
          :cardLinkData="card.layoutCardTitle.layoutCardLink"
          :isshow="activeName.includes(cardIndex)"
          :key="card.cardWid"
        ></card-link>
      </template>
    </template>
    <we-tabs v-model="activeName">
      <we-tab-pane
        v-for="(card, cardIndex) in cardsData"
        :name="`card_${cardIndex}`"
        :key="card.cardWid"
        :lazy="true"
      >
        <span slot="label">
          {{ cardTitle(card) }}
          <!-- {{ card.layoutCardTitle.cardTitle }} -->
          <span style="margin-left: 4px">{{
            `${(num[card.cardId] && num[card.cardId]["num"]) || ""}`
          }}</span>
        </span>
        <remote-component
          :url="getCardUrl(card)"
          :router="card"
          :colWidth="colWidth"
          :i18n="$i18n"
      /></we-tab-pane>
    </we-tabs>
  </div>
</template>

<script>
/* eslint-disable no-debugger */

import getCardUrl from "../minxins/index.js";

export default {
  name: "cards",
  mixins: [getCardUrl], //混入
  props: {
    cardsData: Array,
    colWidth: Number,
  },
  computed: {
    cardTitle() {
      return (card) => {
        const zhTitle = card.layoutCardTitle.cardTitle || ''
        const cardTitleLang = card.layoutCardTitle && card.layoutCardTitle.cardTitleLang || []
        const temp = cardTitleLang.find(el => el.langCode === this.$i18n.locale)
        return temp && temp.langName || zhTitle
      }
    }
  },
  data: () => ({
    activeName: "card_0",
  }),
  created() {
    if (window.shell.getUrlParam()) {
      //taskCardId 为任务中心三张新卡片cardID
      let taskCardId = window.shell.getUrlParam().taskCardId || '';
      if (taskCardId) {
        let num = 0;
        this.cardsData.filter((card, index) => {
          if (card.cardId == taskCardId) {
            num = index;
          }
        });
        this.activeName = `card_${num}`;
      }
    }
    this.getNum(this.cardsData);
  },
};
</script>

<style scoped>
/* @keyframes down {
  from {
    transform: scaleY(0);
    overflow: hidden;
  }
  to {
    transform: scaleY(1);
  }
} */
/* .gateway-card {
  overflow-x: auto;
} */
.portal-card-btn.portal-card-btn-block-1 ~ .we-tabs /deep/ .we-tabs__nav-wrap {
  width: calc(100% - 112px);
}
.portal-card-btn.portal-card-btn-block-2 ~ .we-tabs /deep/ .we-tabs__nav-wrap {
  width: calc(100% - 224px);
}
.portal-card-btn.portal-card-btn-block-3 ~ .we-tabs /deep/ .we-tabs__nav-wrap {
  width: calc(100% - 336px);
}
/deep/ .we-tabs__header {
  border-bottom: 1px #f0f0f0 solid;
}
/deep/ .we-tabs__nav-wrap {
  width: 100%;
  transition: width 0.2s;
}
/deep/ .we-tabs__item {
  font-size: 18px;
  font-weight: 400;
  line-height: 48px;
  height: 48px;
}
/deep/ .we-tabs__item:hover {
  font-weight: 700;
}
/* /deep/ .we-tab-pane {
  animation: down 0.5s ease-out;
  transform-origin: 50% 0;
} */
/deep/ .is-active {
  font-weight: bold;
  font-size: 18px;
  box-shadow: none;
}
/deep/ .we-tabs__active-bar {
  background-color: transparent;
  display: flex;
  justify-content: center;
  height: 2px;
}
/deep/ .we-tabs__item:focus.is-active.is-focus:not(:active) {
  box-shadow: none;
}
/deep/ .we-tabs__active-bar::before {
  content: "";
  width: 100%;
  height: 100%;
  display: inline-block;
}
/deep/ .we-tabs__nav-wrap::after {
  background-color: #e7edf1;
  height: 1px;
}
/deep/ .we-tabs__header {
  margin-bottom: 0px;
}
</style>
