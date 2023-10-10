<template>
  <div class="card_carousel" v-loading="loading">
    <we-swipe
      v-if="dataList.length"
      class="my_swipe"
      :autoplay="3000"
      indicator-color="white"
      :show-indicators="dataList.length > 1"
    >
      <we-swipe-item
        v-for="item in dataList"
        :key="item.name"
        :style="{
          backgroundImage: `url(${item.image})`
        }"
        @click.native="handleGoPage(item)"
      >
        <div class="bg_wrap" v-if="item.title"></div>
        <div class="title_wrap" v-if="item.title">
          {{ item.title }}
        </div>
      </we-swipe-item>
    </we-swipe>
    <EmptyCon
      v-if="!dataList.length"
      :tip="GetLanguageValue('CUS_CARD_CAROUSEL_h5', 'nodata', '暂无数据')"
    />
  </div>
</template>

<script>
import TrackMixin from './mixins/track.js';
export default {
  name: 'swipe',
  props: {
    index: Number,
    router: Object
  },
  mixins: [TrackMixin],
  data() {
    return {
      loading: false,
      dataList: []
    }
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false
        }, 5000)
      }
    }
  },
  computed: {
    errorImg() {
      return window.shell.ErrorImg
    }
  },
  methods: {
    // 设置多语言
    getLang(list) {
      const temp = (list || []).find((el) => el.langKey === this.$i18n.locale);
      const zhName = (list || []).find((el) => el.langKey === 'zh_CN')
      return temp?.langValue || zhName?.langValue;
    },
    handleGoPage(item) {
      this.handleClickTrack(); // 点击埋点
      if (item.url) {
        window.shell.openUrl(item.url)
      }
    },
    getCardData() {
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: 'renderData',
          param: {}
        },
        res => {
          this.loading = false
          this.loadedEndTrack(); // 页面加载结束埋点
          if (res.errcode === '0') {
            if (res.data.config) {
              const toJSON = JSON.parse(
                JSON.parse(res.data.config || '[]') || '[]'
              )
              const list = toJSON.slideList.datas || []
              this.dataList = list.map(el => ({
                ...el,
                title: this.getLang(el.title)
              }))
            }
          } else {
            window.shell.showMessage({
              message: '获取轮播图错误',
              type: 'error'
            })
          }
        }
      )
    }
  },
  created() {
    if (window.shell) {
      this.getCardData()
    }
  }
}
</script>

<style lang="less" scoped>
.card_carousel {
  position: relative;
  width: 100%;
  height: 140px;
  padding: 12px 17px;
  .my_swipe {
    width: 100%;
    height: 100%;
    border-radius: 4px;
    .bg_wrap {
      width: 100%;
      height: 66px;
      line-height: 22px;
      position: absolute;
      bottom: 0;
      left: 0;
      opacity: 0.8;
      background-image: linear-gradient(
        180deg,
        rgba(0, 0, 0, 0) 0%,
        rgba(0, 0, 0, 0.75) 66%,
        #000000 100%
      );
    }
    .title_wrap {
      position: absolute;
      width: 100%;
      padding: 0 15px;
      bottom: 16px;
      left: 0;
      color: #fff;
      font-size: 16px;
      line-height: 22px;
      text-align: left;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
  .we-swipe-item {
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center;
  }
  /deep/.we-swipe__indicators {
    bottom: 8px;
    .we-swipe__indicator {
      background-color: #fff;
      opacity: 0.5;
    }
    .we-swipe__indicator--active {
      opacity: 1;
    }
  }
  /deep/.we-empty__image img {
    width: 162px;
    height: 85px;
  }
  /deep/.empty-content {
    padding-top: 0;
    height: 100%;
  }
}
</style>
