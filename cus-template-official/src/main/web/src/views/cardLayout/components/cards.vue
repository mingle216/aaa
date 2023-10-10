<template>
  <div class="gateway-card position-relateive">
    <template v-for="(card, cardIndex) in cardsData">
      <template v-if="card.layoutCardTitle.layoutCardLink &&
        card.layoutCardTitle.layoutCardLink.length
        ">
        <card-link :cardLinkData="card.layoutCardTitle.layoutCardLink" :cardData="card"
          :isshow="activeName.includes(cardIndex)" :key="card.cardWid"></card-link>
      </template>
    </template>
    <we-tabs v-model="activeName" @tab-click="clickTab" :before-leave="handleBeforeLeave" ref="Tabs">
      <we-tab-pane v-for="(card, cardIndex) in cardsData" :name="`card_${cardIndex}`" :key="card.cardWid" :lazy="true"
        :cardID="card.cardId" :cardName="cardTitle(card)">
        <span slot="label" class="label-title" :cardID="card.cardId" :cardName="cardTitle(card)">
          {{ cardTitle(card) }}
          <span class="lable-dot" v-show="num[card.cardId] && num[card.cardId]['num']"></span>
          <!-- {{ card.layoutCardTitle.cardTitle }} -->
          <!-- <sup class="bage" v-show="num[card.cardId] && num[card.cardId]['num']">{{
            `${num[card.cardId] && num[card.cardId]["num"] || ""}`
          }}</sup> -->
        </span>
        <div :style="{
            height: activeName === `card_${cardIndex}` ? prevHeight : 'auto',
          }">
          <remote-component :url="getCardUrl(card)" :router="card" :colWidth="colWidth" :i18n="$i18n" />
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
        const zhTitle = card.layoutCardTitle.cardTitle || ''
        const cardTitleLang = card.layoutCardTitle && card.layoutCardTitle.cardTitleLang || []
        const temp = cardTitleLang.find(el => el.langCode === this.$i18n.locale)
        return temp && temp.langName || zhTitle
      }
    },
    templateConfig() {
      return JSON.parse(JSON.parse(JSON.parse(JSON.stringify(window.shell.getPageData())).showProgrammeEntity.templateConfig))
    }
  },
  data: () => ({
    activeName: "card_0",
    prevHeight: "auto",
    loadedTab: {},
  }),
  beforeDestroy() {
    window.shell.off("change-tabs", this.handleChangeHeight);
    window.shell.off("update-lwps-tzgg", this.updatalwps);
  },
  created() {
    this.loadedTab[this.activeName] = true;
    this.getNum(this.cardsData);
    window.shell.on("update-lwps-tzgg", this.updatalwps);
    window.shell.on("change-tabs", this.handleChangeHeight);
    if (sessionStorage.getItem('userBehavior')) {
      // 直通车、我的收藏、推荐服务 定位
      let toCardId = this.templateConfig.cardIds?.[0].langValue || 'SYS_CARD_SERVICEBUS,SYS_CARD_FAVORITEAPP,SYS_CARD_RECOMMENDAPP'
      const toCardIdArr = toCardId.split(",");
      // 获取存储的定位
      const userBehavior = JSON.parse(sessionStorage.getItem("userBehavior"))
      // 过滤未定义的卡片
      if (toCardIdArr.indexOf(userBehavior.cardID) > -1) {
        if (this.cardsData?.length) {
          this.cardsData.forEach(val => {
            if (val.cardId == userBehavior.cardID) {
              this.activeName = userBehavior.paneName;
            }
          })
        }
      }
    }
  },
  methods: {
    updatalwps() {
      this.getNum(this.cardsData);
    },
    clickTab(tab, el) {
      let obj = {
        cardID: "",
        cardName: "",
        index: tab.index,
        paneName: tab.paneName,
      }
      if (!el.target.getAttribute("cardID")) {
        obj.cardID = el.target.children[0].getAttribute("cardID")
        obj.cardName = el.target.children[0].getAttribute("cardName")
      } else {
        obj.cardID = el.target.getAttribute("cardID")
        obj.cardName = el.target.getAttribute("cardName")
      }
      // 如果包含这些id 则记录
      let toCardId = this.templateConfig.cardIds?.[0].langValue || 'SYS_CARD_SERVICEBUS,SYS_CARD_FAVORITEAPP,SYS_CARD_RECOMMENDAPP'
      if (toCardId.indexOf(obj.cardID) > -1) {
        sessionStorage.setItem("userBehavior", JSON.stringify(obj))
      }
    },
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
  }
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

.portal-card-btn.portal-card-btn-block-1~.we-tabs /deep/ .we-tabs__nav-wrap {
  /* width: calc(100% - 112px); */
  width: calc(100% - 80px);
}

.portal-card-btn.portal-card-btn-block-2~.we-tabs /deep/ .we-tabs__nav-wrap {
  /* width: calc(100% - 224px); */
  width: calc(100% - 160px);
}

.portal-card-btn.portal-card-btn-block-3~.we-tabs /deep/ .we-tabs__nav-wrap {
  /* width: calc(100% - 336px); */
  width: calc(100% - 240px);
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
  padding: 0 5px;
}

/* /deep/.we-tabs--bottom .we-tabs__item.is-bottom:nth-child(2),
/deep/.we-tabs--bottom .we-tabs__item.is-top:nth-child(2), 
/deep/.we-tabs--top .we-tabs__item.is-bottom:nth-child(2),
/deep/ .we-tabs--top .we-tabs__item.is-top:nth-child(2) {
  padding-left: 10px;
} */

/deep/.we-tabs__content {
  overflow: visible;
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

.bage {
  border-radius: 10px;
  color: #fff;
  display: inline-block;
  font-size: 12px;
  height: 18px;
  line-height: 18px;
  padding: 0 6px;
  text-align: center;
  white-space: nowrap;
  border: 1px solid #fff;
  background: #ff0000;
}

.label-title {
  position: relative;
  padding: 0 8px;
}

.lable-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  position: absolute;
  top: -2px;
  right: 0;
  background: #ff0000;
}
</style>
