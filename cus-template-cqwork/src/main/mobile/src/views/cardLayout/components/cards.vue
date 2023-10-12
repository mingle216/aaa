<template>
  <div class="gateway-card position-relateive" :class="className">
    <div class="cardlink-list">
      <template v-for="(card, cardIndex) in cardsData">
        <template
          v-if="
            card.layoutCardTitle.layoutCardLink &&
              card.layoutCardTitle.layoutCardLink.length
          "
        >
          <card-link
            :cardLinkData="card.layoutCardTitle.layoutCardLink"
            :cardData="card"
            :isshow="activeName.includes(cardIndex)"
            :key="card.cardWid"
          ></card-link>
        </template>
      </template>
    </div>
    <we-tabs
      v-model="activeName"
      swipe-threshold="1"
      class="card-tabs"
      @click="handleTabChange"
    >
      <we-tab
        v-for="(card, cardIndex) in cardsData"
        :name="`card_${cardIndex}`"
        :key="card.cardWid"
        :lazy="true"
      >
        <template #title>
          <div slot="label" class="cardTitle">
            {{ cardTitle(card) }}
            <!-- {{ card.layoutCardTitle.cardTitle }} -->
            <span style="margin-left: 4px">
              {{ getDataNum(card) }}
            </span>
          </div>
        </template>

        <remote-component
          :url="getCardUrl(card)"
          :router="card"
          :dataNum="num[card.cardId] && num[card.cardId]['num']"
          :colWidth="colWidth"
          :i18n="$i18n"
      /></we-tab>
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
    code: Number,
  },
  computed: {
    cardTitle() {
      return (card) => {
        const zhTitle = card.layoutCardTitle.cardTitle || "";
        const cardTitleLang =
          (card.layoutCardTitle && card.layoutCardTitle.cardTitleLang) || [];
        const temp = cardTitleLang.find(
          (el) => el.langCode === this.$i18n.locale
        );
        return (temp && temp.langName) || zhTitle;
      };
    },
    linkLength() {
      let cardLink = this.cardsData[
        Number(this.activeName.charAt(this.activeName.length - 1))
      ].layoutCardTitle.layoutCardLink;
      return cardLink && cardLink.length > 0;
    },
  },
  data() {
    return { activeName: "card_0", className: `gateway-card-${this.code}` };
  },

  methods: {
    handleTabChange() {
      window.shell.emit("card-tab-change", this.activeName);
      this.$nextTick(() => {
        const { className } = this;
        let query = document.querySelector.bind(document);
        let cardLink = query(`.${className}>.cardlink-list`).offsetWidth;
        query(
          `.${className} .card-tabs>.we-tabs__wrap`
        ).style.width = `calc(100% - ${this.linkLength ? cardLink : 0}px`;
      });
    },
  },
  created() {
    if (window.shell.getUrlParam()) {
      //taskCardId 为任务中心三张新卡片cardID
      let taskCardId = window.shell.getUrlParam().taskCardId || "";
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
  mounted() {
    this.handleTabChange();
  },
};
</script>
<style lang="less" scoped>
.gateway-card {
  border-radius: 16px;
}
.card-tabs {
  &.has-link {
    /deep/& > .we-tabs__wrap {
      margin-right: 105px;
    }
  }
  &.has-link-more {
    /deep/& > .we-tabs__wrap {
      margin-right: 47px;
    }
  }
}
</style>
<style lang="less">
.cardlink-list {
  height: 54px;
  position: absolute;
  right: 0px;
  top: 0;
  z-index: 1;
  border-bottom: 1px solid #e7edf1;
  padding-right: 20px;
  padding-left: 7px;
}

.card-tabs > .we-tabs__wrap {
  height: 54px !important;
  padding: 0;
  border-bottom: 1px solid #e7edf1;
  line-height: 1;
  &::before,
  &::after {
    content: "";
    position: absolute;
    z-index: 1;
    height: 46px;
    width: 17px;
    top: 0;
  }
  &::before {
    left: 0;
    background: linear-gradient(to right, #fff, rgba(255, 255, 255, 0));
  }
  &::after {
    right: 0;
    background: linear-gradient(to left, #fff, rgba(255, 255, 255, 0));
  }
  & > .we-tabs__nav {
    padding: 0 17px 15px 17px;
    // box-sizing: border-box;
    font-weight: bold;
    .we-tab__text {
      font-weight: bold !important;
    }
    & > .we-tab {
      flex: none;
      padding: 14px 0;
      font-size: 16px;
      margin-right: 24px;
      > .we-tab__text {
        display: block;
        > .cardTitle {
          height: 20px;
          line-height: 20px;
        }
      }
    }
    & > .we-tab--active {
      font-size: 16px;
    }
    & > .we-tab:after {
      content: "";
      position: absolute;
      bottom: 0px;
      height: 2px;
      left: 2px;
      right: 2px;
      border-radius: 3px;
    }
    & > .we-tabs__line {
      display: none;
    }
    .we-tab:nth-last-child(2) {
      padding-right: 20px !important;
      &:after {
        right: 22px;
      }
    }
  }
}
</style>
