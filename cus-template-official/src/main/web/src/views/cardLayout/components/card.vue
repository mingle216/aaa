<template>
  <div class="gateway-card position-relateive">
    <h3 class="card-sys-title portal-font-color-lv1 ellipsis" v-if="cardData.showTitle">
      <span class="newspan">
        {{ cardTitle }}
        <span style="margin-left: 4px">{{
          num[cardData.cardId] && num[cardData.cardId]["num"]
        }}</span></span>
      <template v-if="
          cardData.layoutCardTitle.layoutCardLink &&
          cardData.layoutCardTitle.layoutCardLink.length
        ">
        <card-link :cardLinkData="cardData.layoutCardTitle.layoutCardLink" :isshow="true"></card-link>
      </template>
    </h3>
    <remote-component :url="getCardUrl(cardData)" :router="cardData" :colWidth="colWidth" :i18n="$i18n" />
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
    /* padding: 13px 0px 15px 0px; */
    display: flex;
    align-items: center;
    font-size: 18px;
    font-weight: bold;
    background: rgba(242, 242, 242, 0.2);
  }

  .newspan::before {
    position: absolute;
    content: "";
    width: 50px;
    height: 50px;
    right: -25px;
    top: -25px;
    z-index: 100;
    background-color: #ffffff;
    transform: rotateZ(45deg);
  }

  .newspan {
    background: #0B5AA5;
    border-radius: 4px 4px 0 4px;
    color: #FFF;
    padding: 8px 45px 8px 20px;
    vertical-align: middle;
    position: relative;
  }

  .card-sys-title span {
    cursor: pointer;
  }
</style>