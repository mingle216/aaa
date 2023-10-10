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
          :cardData="card"
          :isshow="activeName.includes(cardIndex)"
          :key="card.cardWid"
        ></card-link>
      </template>
    </template>
    <we-tabs v-model="activeName" ref="Tabs" :before-leave="handleBeforeLeave">
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
        <div
          :style="{
            height: activeName === `card_${cardIndex}` ? prevHeight : 'auto',
          }"
        >
          <remote-component
            :url="getCardUrl(card)"
            :router="card"
            :colWidth="colWidth"
            :i18n="$i18n"
          />
        </div>
      </we-tab-pane>
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
        const zhTitle = card.layoutCardTitle.cardTitle || "";
        const cardTitleLang =
          (card.layoutCardTitle && card.layoutCardTitle.cardTitleLang) || [];
        const temp = cardTitleLang.find(
          (el) => el.langCode === this.$i18n.locale
        );
        return (temp && temp.langName) || zhTitle;
      };
    },
  },
  data: () => ({
    activeName: "card_0",
    prevHeight: "auto",
    loadedTab: {},
  }),
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
    this.loadedTab[this.activeName] = true;
    this.getNum(this.cardsData);
    window.shell.on("change-tabs", this.handleChangeHeight);
  },
  beforeDestroy() {
    window.shell.off("change-tabs", this.handleChangeHeight);
  },
  methods: {
    handleChangeHeight(param) {
      const cardWid = this.cardsData[Number(this.activeName.split("card_")[1])]
        .cardWid;
      if (param.wid === cardWid && param.loaded) {
        //卡片加载成功
        this.prevHeight = "auto";
      }
    },
    handleBeforeLeave(activeName, oldActiveName) {
      // 解决第一次切换tab页面抖动的；新tab的卡片高度默认为上一个tab的高度，新tab中的卡片接口有一个请求成功之后恢复其高度
      if (!this.loadedTab[activeName]) {
        const index = Number(oldActiveName.split("card_")[1]);
        this.prevHeight = `${this.$refs.Tabs.panes[index]?.$el?.clientHeight}px`;
        this.loadedTab[activeName] = true;
      }
      return true;
    },
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
.hideTab {
  width: 100%;
  position: absolute;
  left: 0;
  top: 0;
  visibility: hidden;
  z-index: -2;
}
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
