<template>
  <div class="gateway-rows">
    <we-row
      v-for="(item, index) in rowsData"
      :key="index"
      :gutter="40"
    >
      <we-col
        :class="['mr-tb-12','p-lr-20', el.card && el.card.cardId === 'SYS_CARD_SERVICEITEMCOUNT' ? 'Count' : 'Col']"
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
  },
  data() {
    return {
      type: ["card", "cards", "rows"],
      pageCode: window.shell.getRoute()
    };
  },
  watch: {
    rowsData() {
      // this.changeStyle();
      this.pageCode = window.shell.getRoute()
    }
  },
  methods: {
    changeStyle() {
      let timer = setInterval(() => {
        const pageCardList = document.querySelectorAll('.Count')  // 事项统计dom
        const body = document.querySelector('.gateway-rows')
        const conEle = document.querySelectorAll('.tempalteConWrap'); // 模板主容器dom
        const conWidth = (conEle && conEle.length && conEle[0] && conEle[0].offsetWidth) || document.body.offsetWidth;
        const clientWidth = conWidth < 1280 ? 1280 : conWidth
        const offsetWidth = body.offsetWidth
        const colList = document.querySelectorAll('.Col')
        colList.forEach(pageCard => {
          if (pageCard) {
            pageCard.setAttribute('style', 'padding-left: 18px; padding-right: 18px;')
          }
        })
        // 如果有事项统计设置style，否则清空定时器
        if (pageCardList.length) {
          pageCardList.forEach(pageCard => {
            if (pageCard) {
              // we-row左右间距20
              if (clientWidth - offsetWidth >= 40) {
                pageCard.style.marginBottom = `${-4}px`
                pageCard.style.padding = 0
                pageCard.style.marginLeft = `-${(clientWidth - offsetWidth) / 2 - 20}px`
                pageCard.style.width = clientWidth + 'px'
              } else {
                const newLeft = 20 - (clientWidth - offsetWidth) / 2
                pageCard.style.marginLeft = `${newLeft > 20 ? 20 : newLeft}px`
              }
              clearInterval(timer)
              timer = null
            }
          })
        } else {
          clearInterval(timer)
          timer = null
        }
      }, 100)
    }
  },
  mounted() {
    // this.changeStyle();
    // let _this = this;
    // window.addEventListener('resize', () => {
    //   _this.changeStyle()
    // }, false)
  },
};
</script>
<style scoped>
.gateway-rows {
  width: 100%;
  /* overflow-y: auto; */
}
.position-relateive {
  position: relative;
  /* overflow: hidden; */
}
.mr-tb-12 {
  margin: 12px 0;
}
.p-lr-20 {
  padding-left: 18px;
  padding-right: 18px;
}
.col-gutter {
  padding-left: 36px;
}
/deep/ .we-card {
  width: 100%;
}
</style>