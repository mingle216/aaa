<template>
  <div :class="getpageName.indexOf('itemDetail') != -1 ? 'gateway-rows dzclass' : 'gateway-rows'"
    :style="getpageName.indexOf('hall') != -1 ? 'margin-top:40px' : ''">
    <we-row v-for="(item, index) in rowsData" :key="index" :gutter="40" :class="[
      item.columns[0].card && item.columns[0].card.cardId === 'CUS_CARD_SERVICEITEMCOUNT' ? 'DzCount' : '',
    ]">
      <we-col
        :class="['mr-tb-12', 'p-lr-20', el.card && el.card.cardId === 'CUS_CARD_SERVICEITEMCOUNT' ? 'Count' : 'Col']"
        v-for="(el, colIndex) in item.columns" :key="colIndex" :span="el.width">
        <template v-for="(value, key) in el">
          <component v-if="type.includes(key)" :is="key" :key="`${key}-${value.cardWid || pageCode}`" :cardData="value"
            :cardsData="value.cards" :rowsData="value" :colWidth="el.width"></component>
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
    pageData: Array,
  },
  data() {
    return {
      type: ["card", "cards", "rows"],
      pageCode: window.shell.getRoute()
    };
  },
  computed: {
    getpageName() {
      console.log(999, this.pageData)
      return this.pageData?.renderData?.breadcrumb[0]?.pageCode || '';
    },
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
.DzCount {
  padding: 0;
  background: url("../../../assets/images/Frame.png") no-repeat;
  background-size: cover;
  height: 168px;
}

.gateway-rows {
  width: 100%;
  padding: 0 !important;
  /* overflow-y: auto; */
}

.dzclass {
  padding: 0 20px !important;
  width: 100%;
  background: #fff;
}

.gateway-rows>div {
  padding: 0 calc(50% - 37.5rem);
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

.Col {
  /* box-shadow: 0 0.5rem 1.5rem 0 rgba(29, 29, 31, 12%);
  border-radius: 4px;
  padding: 20px 0; */
  /* box-shadow: none !important; */
}

.we-col-12 {
  /* margin-right: 20px;
  width: calc(50% - 10px); */
}

.we-col-12:last-child {
  /* margin-right: 0; */
}
</style>