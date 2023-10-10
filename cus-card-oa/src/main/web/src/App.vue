<template>
  <div class="newsAnnouncement dz-news" ref="NewsAnnouncement" v-loading="loading">
    <!-- 栏目tab -->
    <TabNav ref="TabNav" :router="router" :cardWidth="cardWidth" :cardHeight="containerHeight"
      :newsShowSubscribe="config.newsShowSubscribe" @change-tab="handleChangeTab"></TabNav>
    <AutoContainer :scroll="scroll" :con-type="containerType" :con-height="containerHeight">
      <template v-if="newList && newList.length > 0">
        <div v-loading="newsLoading">
          <div :class="[imgFloatLayout ? 'news-list-float' : 'news-list']" :style="conWidth">
            <div v-for="(item, index) in newList" :key="index" :style="getCardStyle(index)"
              :class="[imgFloatLayout ? 'float' : '']">
              <!-- 标题、时间及摘要 -->
              <TimeTitleAbstractval v-if="config.newsDisplay && config.newsDisplay.type == 1" :itemInfo="item"
                :config="config" :lastRow="isLastRow(index)" @go-link="goLink">
              </TimeTitleAbstractval>
              <!-- 图片、标题、时间及摘要 -->
              <TimeTitleImg v-else-if="config.newsDisplay && config.newsDisplay.type == 2" :itemInfo="item"
                :config="config" :lastRow="isLastRow(index)" :extremum="conWidth.includes('px')" @go-link="goLink">
              </TimeTitleImg>
              <!-- 标题、时间及发布人 -->
              <TimeTitlePublisher v-else-if="config.newsDisplay && config.newsDisplay.type == 3" :index="index"
                :itemInfo="item" :config="config" :lastRow="isLastRow(index)" @go-link="goLink">
              </TimeTitlePublisher>
              <!-- 标题、时间 -->
              <TimeTitle v-else :itemInfo="item" :config="config" :cardWidth="cardWidth" :lastRow="isLastRow(index)"
                @go-link="goLink">
              </TimeTitle>
            </div>
          </div>

          <div class="pages" v-if="showPage && total && total > 0">
            <p class="total-size portal-font-color-lv1">共<span>{{ total }}</span>条</p>
            <we-pagination background layout="prev, pager, next" :total="total"
              @current-change="handleCurrentChange"></we-pagination>
          </div>

          <!-- 展开更多 -->
          <div class="newsAnnouncement__page portal-primary-color-lv1" v-if="showMore" @click="handleShowMore">
            <span class="ellipsis">{{
              $Lan("CUS_CARD_OA", "showMore", "查看更多")
            }}</span>
            <i class="iconfont icon-menu-Unfold newsAnnouncement__page-icon"></i>
          </div>
        </div>
      </template>
      <EmptyCon v-else :tip="
        showSubscribe && config.newsShowSubscribe == 1
          ? ''
          : $Lan('CUS_CARD_OA', 'noData', '暂无内容')
      " :subTip="
  showSubscribe && config.newsShowSubscribe == 1
    ? $Lan('CUS_CARD_OA', 'noData', '暂无内容')
    : ''
" :height="emptyHeight + 46" :switchSize="switchSize" :class="[emptyHeight > switchSize ? '' : 'emptySmall']"
        style="width: 100%">
        <template v-slot:operate>
          <div v-if="showSubscribe && config.newsShowSubscribe == 1" class="portal-primary-color-lv1 subscribeBtn">
            <span @click="handleSubscribe">{{
              $Lan("CUS_CARD_OA", "goSubscribe", "点击订阅")
            }}</span>
          </div>
        </template>
      </EmptyCon>
    </AutoContainer>
  </div>
</template>
<script>
import TabNav from "./components/TabNav.vue";
import TimeTitleAbstractval from "./components/TimeTitleAbstractval";
import TimeTitle from "./components/TimeTitle";
import TimeTitleImg from "./components/TimeTitleImg";
import TimeTitlePublisher from "./components/TimeTitlePublisher";
import { initTrack } from "./mixins/track.js";
const minWidth = 280;
export default {
  components: {
    TabNav,
    TimeTitleAbstractval,
    TimeTitle,
    TimeTitleImg,
    TimeTitlePublisher,
  },
  props: {
    router: Object,
  },
  mixins: [initTrack],
  data() {
    return {
      scroll: { barKeepShow: true },
      checked: false,
      fontClass: "",
      newList: [],
      config: {},
      containerType: 2, //初始化卡片高度选项 0：代表自适应（无最大限制）1：代表自适应（有最大限制）2：固定高度
      containerHeight: 500, // 高度 只有当heightFlag为1,2时，此值才生效
      cardWidth: 0,
      loading: false,
      newsLoading: false,
      current: 1,
      total: 0,
      pageSize: 6,
      channelIds: "",
      showSubscribe: false,
      newsRead: JSON.parse(window.localStorage.getItem("newsRead") || "[]"),
      pageNum: 1,
      showPage: false
    };
  },
  computed: {
    showMore() {
      return this.total > this.pageSize && this.config.showMore == 1;
    },
    imgFloatLayout() {
      return (
        this.config.newsDisplay &&
        this.config.newsDisplay.type == 3 &&
        this.config.newsColumns !== 1 &&
        this.newList.length &&
        this.newList[0].picUrl
      );
    },
    conWidth() {
      const columns = Number(this.config.newsColumns || 1);
      const itemWidth = (this.cardWidth - 10) / columns;
      if (itemWidth < minWidth) {
        return `width: ${columns * minWidth}px`;
      }
      return "width: 100%";
    },
    emptyHeight() {
      const height = this.channelIds
        ? this.containerHeight
        : this.config.newsHeight?.value;
      return this.containerType !== 0 && height < 450 ? height : 450;
    },
    switchSize() {
      return this.showSubscribe && this.config.newsShowSubscribe == 1
        ? 150
        : 100;
    },
  },
  created() {
    this.renderData();
    this.getConfig();
  },
  methods: {
    handleCurrentChange(page) {
      this.pageNum = page;
      this.renderData();
    },
    getConfig() {
      window.shell.execCardMethod({
        cardId: this.router.cardId,
        cardWid: this.router.cardWid,
        method: 'getConfig'
      }, (data) => {
        if (data.errcode === "0") {
          const datas = data.data;
          let config = null
          if (typeof datas === 'string') {
            config = JSON.parse(JSON.parse(datas))
            this.showPage = config.showPage === 1 ? true : false || true
          }
        }
      })
    },
    goLink(item) {
      this.handleClickTrack(); //点击埋点
      let source_flag = this.config?.newsDetailJump || 0;
      if (Number(source_flag)) {
        //跳转到本地新闻详情页面
        this.handleRecordNews(item);
        window.shell.openPage(`/newsDetail?wid=${item.wid}`, 1, 1);
      } else {
        //跳转到数据源
        if (item.url) {
          this.handleRecordNews(item);
          window.shell.openPage(item.url, 1, 2);
        } else {
          window.shell.showMessage({
            message: this.$Lan(
              "CUS_CARD_OA",
              "noLink",
              "暂无跳转地址"
            ),
            type: "warning",
          });
        }
      }
    },
    handleRecordNews(item) {
      item.read = true;
      if (this.config.isEnableRead) {
        window.shell.execTemplateMethod(
          'newsRead',
          {
            wid: item.wid,
            // title: item.title,
            // contexts: item.contents
          }
        )
      } else {
        if (!this.newsRead.find((curr) => curr === item.url)) {
          this.newsRead.push(item.url);
          window.localStorage.setItem("newsRead", JSON.stringify(this.newsRead));
        }
      }
    },
    changeLoading() {
      if (this.loading) {
        this.$nextTick(function () {
          this.loading = false;
        });
      } else {
        this.loading = !this.loading;
      }
    },
    handleChangeTab(channelIds) {
      this.channelIds = channelIds;
      this.renderData();
    },
    handleSubscribe() {
      this.$refs.TabNav.handleSubscribe();
    },
    handleShowMore() {
      const channelIds = this.channelIds.includes(",") ? "" : this.channelIds;
      window.shell.openPage(
        `newList?cardId=${this.router.cardId}&cardWid=${this.router.cardWid}&channelIds=${channelIds}`,
        1,
        1
      );
      this.handleClickTrack(); //点击埋点
    },
    renderData() {
      this.newsLoading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getChannelNews",
          param: {
            channelIds: this.channelIds,
            pageNumber: this.pageNum,
          },
        },
        (res) => {
          this.$nextTick(function () {
            this.newsLoading = false;
            this.loadedEndTrack();
          });
          if (res.errcode === "0") {
            const data = res.data;
            const lists = data.datas?.data || [];
            this.config = data.configInfo || {};
            // 是否开启后端记录已读未读
            if (!this.config.isEnableRead) {
              lists.forEach((element) => {
                element.read = this.newsRead.find((curr) => curr === element.url)
                  ? true
                  : false;
              });
            }
            this.newList = lists;
            this.total = data.datas?.totalSize || 0;
            this.pageSize = data.datas?.pageSize || 6;
            this.containerType = this.config.newsHeight.type;
            this.containerHeight =
              this.containerType === 0
                ? 1200 - 42
                : this.config.newsHeight.value - 42;
          }
        }
      );
    },
    /**
     * 如果一行显示超过一个,且不是则需要加右边距
     * */
    getPaddingRight(index) {
      const num = Number(this.config.newsColumns || 1);
      if (this.imgFloatLayout && num === 2) {
        if (index === 0 || (index !== 1 && index % num)) {
          return "20px";
        } else {
          return "0";
        }
      } else if (this.imgFloatLayout && num === 3) {
        if (index === 2 || (index !== 1 && !((index - 1) % num))) {
          return "0";
        } else {
          return "20px";
        }
      } else {
        if ((index + 1) % num) {
          return "20px";
        } else {
          return "0";
        }
      }
    },
    /**
     * 设置卡片的样式
     * @param num 设置一行显示多少个（平均显示）
     * @returns {string}
     */
    getCardStyle(index) {
      const newsColumns = Number(this.config.newsColumns || 1);
      const percent = Math.round((1 / newsColumns) * 10000) / 100.0 + "%";
      return (
        "width:" +
        percent +
        ";" +
        "padding-right:" +
        this.getPaddingRight(index)
      );
    },
    isLastRow(index) {
      if (!this.showMore) {
        return false;
      }
      const newsColumns = Number(this.config.newsColumns || 1);
      // 图片布局时，图片占两个元素的位置
      const length = this.imgFloatLayout
        ? this.newList.length + 1
        : this.newList.length;
      const row = parseInt(length / newsColumns, 10);
      const relRow = length % newsColumns ? row + 1 : row;
      if (this.imgFloatLayout) {
        return index + 1 >= (relRow - 1) * newsColumns;
      }
      return index >= (relRow - 1) * newsColumns;
    },
    handleResize() {
      this.getCardWidth();
    },
    getCardWidth() {
      let node = this.$refs.NewsAnnouncement;
      if (node && node.offsetWidth) {
        this.cardWidth = node.offsetWidth;
        return;
      }
      let hasClass = false;
      while (node && !hasClass) {
        node = node.parentNode;
        hasClass = node.classList.contains("gateway-card");
      }
      this.cardWidth = hasClass ? node.offsetWidth : minWidth;
    },
  },
  mounted() {
    this.getCardWidth();
    window.addEventListener("resize", this.handleResize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleResize);
  },
};
</script>

<style lang="less" scoped>
.dz-news {
  box-shadow: 0.125rem 0.5rem 0.625rem 0rem rgba(0, 0, 0, 2%), -0.0625rem 0rem 0.625rem 0rem rgba(0, 0, 0, 2%);
  border-radius: 0rem 0rem 0.5rem 0.5rem;
}
.newsAnnouncement {
  width: 100%;
  min-height: 150px;
  line-height: initial;
  position: relative;

  .emptySmall {
    .subscribeBtn {
      text-align: left;
    }

    /deep/.small-empty-img {
      background-size: 180px 85px;
      margin-right: 24px;
    }
  }

  /deep/.__rail-is-vertical,
  /deep/.__rail-is-horizontal {
    background: #fff !important;
  }
}

.news-list {
  display: flex;
  flex-wrap: wrap;
  padding: 0 14px;
}

.subscribeBtn {
  text-align: center;
  cursor: pointer;
  font-size: 16px;
  line-height: 24px;
  margin-bottom: 2px;
  margin-top: 8px;
}

.news-list-float {
  .float {
    float: left;
  }

  &:after {
    display: block;
    content: "";
    visibility: hidden;
    height: 0;
    overflow: hidden;
    clear: both;
  }
}

.newsAnnouncement__page {
  background: #f5f5f5;
  height: 46px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  cursor: pointer;
  padding: 0 40px;

  .newsAnnouncement__page-icon {
    flex-shrink: 0;
    margin-left: 4px;
  }
}

.we-loading-mask {
  display: none;
}

.pages {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 14px;

  .total-size {
    font-size: 14px;
    font-weight: 400;

    &>span {
      margin: 0 5px;
    }
  }
}
</style>
