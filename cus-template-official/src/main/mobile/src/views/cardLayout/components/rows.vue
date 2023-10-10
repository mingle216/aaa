<template>
  <div class="gateway-rows">
    <we-row v-for="(item, index) in rowsData" :key="index" :gutter="40">
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
  },
  data() {
    return {
      type: ["card", "cards", "rows"],
      pageCode: window.shell.getRoute()
    };
  },
   watch: {
    rowsData() {
      this.pageCode = window.shell.getRoute()
    }
  },
  methods: {
    changeStyle() {
      // let timer = setInterval(() => {
      // const pageCardList = document.querySelectorAll('.Col')
      // console.log(pageCard)
      // const body = document.querySelector('.gateway-rows')
      // const clientWidth = document.body.offsetWidth
      // const offsetWidth = body.offsetWidth
      // pageCardList.forEach(pageCard => {
      //   if (pageCard) {
      //     if (clientWidth - offsetWidth > 40) {
      //       pageCard.style.marginBottom = `${-4}px`
      //       pageCard.style.padding = 0
      //       pageCard.style.marginLeft = `-${(clientWidth - offsetWidth) / 2 - 20}px`
      //       pageCard.style.width = clientWidth + 'px'
      //     } else {
      //       const newLeft = 20 - (clientWidth - offsetWidth) / 2
      //       pageCard.style.marginLeft = `${newLeft > 20 ? 20 : newLeft}px`
      //     }
      // clearInterval(timer)
      // timer = null
      //   }
      // })
      // }, 100)
    },
  },
  mounted() {
    // this.changeStyle();
    // let _this = this;
    // window.addEventListener('resize', () => {
    //   console.log('resize')
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
  overflow: hidden;
}
.mr-tb-30 {
  margin: 30px 0;
}
/deep/ .we-card {
  width: 100%;
}
</style>