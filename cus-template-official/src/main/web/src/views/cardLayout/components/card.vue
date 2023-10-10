<template>
  <div class="gateway-card position-relateive">
    <h3
      class="card-sys-title portal-font-color-lv1 ellipsis"
      v-if="cardData.showTitle"
    >
      <span
      class="label-title" 
        >
        {{ cardTitle }}
        <span class="lable-dot" v-show="num[cardData.cardId] && num[cardData.cardId]['num']"></span>
        <!-- <sup class="bage" v-if="num[cardData.cardId] && num[cardData.cardId]['num']">{{
          num[cardData.cardId] && num[cardData.cardId]["num"]
        }}</sup> -->
        </span
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

<style scoped>
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
}

.lable-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  position: absolute;
  top: -2px;
  right:-7px;
  background: #ff0000;
}
</style>
