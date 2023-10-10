<template>
  <div class="swiper" ref="Swipe">
    <we-carousel
      class="carousel"
      trigger="click"
      ref="Carousel"
      label="test"
      :interval="5000"
      v-if="dataList.length"
      :autoplay="true"
      :height="conHeight"
      arrow="never"
      indicator-position="none"
      @change="handleChange"
    >
      <we-carousel-item
        v-for="(item, dataIndex) in dataList"
        :key="item.name"
        class="img_slider"
        :style="{
          backgroundImage: `url(${item.image})`,
          cursor: item.url ? 'pointer' : 'default',
        }"
      >
        <div class="content_container" :class="{'animate': currentIndex === dataIndex}">
          <div class="content_text_container">
            <div
              class="swipe_title"
              :style="contentMaxWidth"
              :title="item.title"
              v-if="display.includes('1')"
            >
              {{ item.title }}
            </div>

            <div
              class="swipe_sub_title"
              :title="item.summary"
              :style="contentMaxWidth"
              v-if="display.includes('2')"
            >
              {{ item.summary }}
            </div>

            <div class="swipe_link_container">
              <div
                @click.stop="handleGoPage(item)"
                class="swipe_link"
                title="查看详情"
              >
                查看详情 <i class="we-icon-right"></i>
              </div>
            </div>
          </div>
          
          <!-- 分页器 -->
          <template v-if="dataList.length > 1">
            <ul
              v-if="!showNumPage"
              class="carousel_items"
              :style="{ minWidth: `${pageWidth}px` }"
            >
              <li
                v-for="(item, index) in dataList"
                :key="item.index"
                @click.stop="handleClickPage(index)"
              >
                <svg
                  width="33"
                  height="5"
                  viewBox="0 0 33 5"
                  v-if="currentIndex === index"
                >
                  <rect
                    x="0"
                    y="0"
                    stroke-width="0"
                    fill="#fff"
                    stroke="#fff"
                    opacity=".5"
                    width="33"
                    height="5"
                  />
                  <path
                    :d="d"
                    fill="#fff"
                    stroke-width="6"
                    stroke="#fff"
                  />
                </svg>
                <span v-else class="rect" />
              </li>
            </ul>
            <page-num
              v-else
              :currentIndex="currentIndex"
              :total="dataList.length"
            />
          </template>
        </div>
      </we-carousel-item>
    </we-carousel>
    <empty-con :tip="GetLanguageValue('CUS_CARD_CAROUSEL', 'nodata', '暂无数据')" v-else />
  </div>
</template>

<script>
import PageNum from "./components/PageNum";
import TrackMixin from "./mixins/track.js";
export default {
  name: "carousel",
  components: {
    PageNum,
  },
  props: {
    index: Number,
    router: Object,
  },
  mixins: [TrackMixin],
  data() {
    return {
      containerWidth: 1000,
      currentIndex: 0,
      timer: null,
      circleTimer: null,
      ratio: 20,
      r: 6,
      containerType: { type: 0, value: 500 },
      dataList: [],
      display: [],
    };
  },
  computed: {
    d() {
      const { ratio } = this;
      return this.getD(ratio);
    },
    contentMaxWidth() {
      const circle = 18;
      const len = this.dataList.length - 1;
      const result = circle * len + this.r * 2;
      const offset = this.showNumPage ? 50 : 10 + result;
      return {
        maxWidth: `calc(100% - ${offset}px)`,
      };
    },
    conHeight() {
      const { type, value } = this.containerType;
      if (type == 2) {
        return value + "px";
      } else {
        let height = this.autoHeight;
        if (type === 1) {
          height = this.autoHeight > value ? value : this.autoHeight;
        }
        return height + "px";
      }
    },
    pageWidth() {
      return (this.dataList.length - 1) * 18 + 20;
    },
    showNumPage() {
      // if (this.containerWidth < 580) {
      //   return true;
      // } else {
      //   return false;
      // }
      return false;
    },
    // 是否展示标题和摘要
    isShowAll() {
      return this.display.includes("1") && this.display.includes("2");
    },
  },
  methods: {
    // 标题没有时摘要垂直居中样式
    alignStyle(item) {
      const { title, summary } = item;
      return {
        alignItems: title && summary && this.isShowAll ? "flex-end" : "center",
      };
    },
    handleChangeArrow(index) {
      this.handleClickPage(index)
    },
    // 轮播图切换的change事件
    handleChange(val) {
      this.currentIndex = val;
      this.ratio = 20;
      this.runRect();
    },
    // 点击轮播图跳转
    handleGoPage(item) {
      if (item.url) {
        window.open(item.url);
      }
    },
    // 点击分页器切换
    handleClickPage(index) {
      this.currentIndex = index;
      this.ratio = 20;
      this.$refs.Carousel.setActiveItem(index);
      this.runRect();
    },
    getD(ratio) {
      if (ratio >= 100) {
        ratio = 100;
      }

      const currentX = ratio / 100 * 33
      
      return `M 0 1.5 L ${currentX} 1.5 Z`;
    },
    // 分页器加载动画
    runRect() {
      clearInterval(this.rectTimer);
      this.rectTimer = setInterval(() => {
        if (this.ratio >= 100) {
          this.ratio = 100;
          clearInterval(this.rectTimer);
          this.rectTimer = null;
          return;
        }
        this.ratio += 0.5;
      }, 30);
    },
    // 设置多语言
    getLang(list) {
      const temp = (list || []).find((el) => el.langKey === this.$i18n.locale);
      const zhName = (list || []).find((el) => el.langKey === 'zh_CN')
      return temp?.langValue || zhName?.langValue;
    },
    // 获取卡片数据
    getCardData() {
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "renderData",
          param: {},
        },
        (res) => {
          if (res.errcode === "0") {
            if (res.data.config) {
              const toJSON = JSON.parse(JSON.parse(res.data.config || "[]") || "[]");
              const list = toJSON.slideList.datas || [];
              this.dataList = list.map((el) => ({
                ...el,
                title: this.getLang(el.title),
                summary: this.getLang(el.summary),
                image:
                  process.env.NODE_ENV === "development"
                    ? el.image.split(".cn")[1]
                    : el.image,
              }));
              this.containerType = toJSON.containerType;

              this.display = toJSON.display; // 是否展示标题摘要信息

              const proportion = toJSON.slideList.proportion; // 图片比例
              const { width, height } = proportion;
              this.containerWidth = this.$refs.Swipe.offsetWidth; // 容器宽度
              this.autoHeight = (this.containerWidth * height) / width; // 自适应下容器高度
              // console.log(this.containerWidth, this.autoHeight)
            }
          } else {
            window.shell.showMessage({
              message: "获取轮播图错误",
              type: "error",
            });
          }
          this.loadedEndTrack();
        }
      );
    },
  },
  mounted() {
    this.runRect();
  },
  created() {
    this.getCardData();
  },
};
</script>

<style lang="less" scoped>
ul,
li {
  list-style: none;
}
.swiper {
  position: relative;
	color: #fff;
  .carousel {
    .carousel__arrow {
      width: 44px;
      height: 44px;
      position: absolute;
      top: 50%;
      z-index: 10;
      transform: translateY(-50%);
      background: rgba(0, 0, 0, 0.2);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      visibility: hidden;
      img {
        width: auto;
        height: 20px;
      }
      &:hover {
        background: rgba(0, 0, 0, 0.6);
      }
    }
    .carousel__arrow--left {
      left: 20px;
    }
    .carousel__arrow--right {
      right: 20px;
    }
    &:hover {
      .carousel__arrow {
        visibility: visible;
      }
    }
  }
  .content_container {
    width: 100%;
    height: 100%;
    cursor: default;
    padding: 80px 60px 50px 60px;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    &:hover {
      .content_text_container {
        transform: translateY(-8px);
      }
      
    }

    .content_text_container {
      display: flex;
      flex-direction: column;
      flex: 1;
			transition: transform 0.5s;

      .swipe_title {
        font-size: 28px;
        font-weight: 600;
        //color: #fff;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .swipe_sub_title {
        //color: #fff;
        font-size: 16px;
        padding: 24px 0 0 2px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .swipe_link_container {
        flex: 1;
        display: flex;
        align-items: flex-end;
        padding-left: 2px;
        margin-bottom: 50px;
        .swipe_link {
          cursor: pointer;
          display: inline-block;
          //color: #fff;
          font-size: 16px;
        }
      }
    }
    
    

    .carousel_items {
      // max-width: 120px;
      height: 20px;
      margin-bottom: -8px;
      padding: 0;
      overflow: hidden;
      display: flex;
      align-items: center;
      cursor: pointer;
      li {
        display: flex;
        align-items: center;
        svg {
          margin: 0 5px;
        }
      }
      
      .rect {
        display: inline-block;
        width: 33px;
        height: 5px;
        background: rgba(239, 241, 245, 0.4);
        margin: 0 5px;
        &:hover {
          background: #434a52;
        }
      }
    }
  }
  .img_slider {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center top;
    cursor: pointer;
  }
  /deep/.we-carousel__arrow {
    width: 50px;
    height: 50px;
    background-color: rgba(0, 0, 0, 0.2);
    &:hover {
      background-color: rgba(0, 0, 0, 0.6);
    }
    i {
      font-size: 32px;
      font-weight: bold;
    }
  }
}
/deep/.is-active {
  font-weight: normal;
}
</style>
