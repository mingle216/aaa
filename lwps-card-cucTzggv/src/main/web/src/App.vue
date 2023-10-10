<template>
  <div class="dz_newsAnnouncement" ref="NewsAnnouncement" v-loading="loading">
    <!-- 栏目tab -->
    <!-- <TabNav ref="TabNav" v-if="lmReady && lmDisplayType == '1'" :router="router" :cardWidth="cardWidth"
      :cardHeight="containerHeight" :subscribeLists="subscribeLists" @change-tab="handleChangeTab"></TabNav>
    <TabNavV ref="TabNav" v-if="lmReady && lmDisplayType == '0'" :router="router" :cardWidth="cardWidth"
      :cardHeight="containerHeight" :subscribeLists="subscribeLists" @change-tab="handleChangeTab"></TabNavV> -->
    <AutoContainer :scroll="scroll" :con-type="containerType" :con-height="containerHeight" :style="getAutoContinerStyle">
      <template v-if="newList && newList.length > 0">
        <div v-loading="newsLoading">
          <div class="news-list" :style="conWidth">
            <div v-for="(item, index) in newList" :key="index" :style="getCardStyle(index)">
              <!-- 标题、时间及摘要 
              <TimeTitleAbstractval
                v-if="config.newsDisplay && config.newsDisplay.type == 1"
                :itemInfo="item"
                :config="config"
                @go-link="goLink"
              >
              </TimeTitleAbstractval>-->
              <!-- 图片、标题、时间及摘要 
              <TimeTitleImg
                v-else-if="config.newsDisplay && config.newsDisplay.type == 2"
                :itemInfo="item"
                :config="config"
                :extremum="conWidth.includes('px')"
                @go-link="goLink"
              >
              </TimeTitleImg>-->
              <!-- 标题、时间及发布人 
              <TimeTitlePublisher
                v-else-if="config.newsDisplay && config.newsDisplay.type == 3"
                :index="index"
                :itemInfo="item"
                :config="config"
                @go-link="goLink"
              >
              </TimeTitlePublisher>-->
              <!-- 标题、时间 -->
              <TimeTitle :itemInfo="item" :config="config" @go-link="goLink">
              </TimeTitle>
            </div>
          </div>
        </div>
      </template>

      <EmptyCon v-else :tip="getTips()" :height="containerHeight - (!lmReady || lmDisplayType == '0' ? 0 : 42)"
        :switchSize="switchSize" :class="[containerHeight > switchSize ? '' : 'emptySmall']" style="width: 100%">
      </EmptyCon>
    </AutoContainer>
    <div class="flex-col-center" v-if="newList && newList.length > 0">
      <p>共{{ total }}条</p>
      <we-pagination background layout="prev, pager, next" :pager-count="5" :total="total" :page-size="newsPageSize"
        :current-page="pageNum" @current-change="handlePage">
      </we-pagination>
    </div>
  </div>
</template>
<script>
// import TabNav from "./components/TabNav.vue";
// import TabNavV from "./components/TabNavV.vue";
// import TimeTitleAbstractval from "./components/TimeTitleAbstractval";
import TimeTitle from "./components/TimeTitleAbstractval";
// import TimeTitleImg from "./components/TimeTitleImg";
// import TimeTitlePublisher from "./components/TimeTitlePublisher";
import { initTrack } from "./mixins/track.js";
import axios from "axios";
const minWidth = 280;
export default {
  components: {
    // TabNav,
    // TabNavV,
    // TimeTitleAbstractval,
    TimeTitle,
    // TimeTitleImg,
    // TimeTitlePublisher,
  },
  props: {
    router: Object,
  },
  mixins: [initTrack],
  data() {
    const { cardWid, cardId } = this.router;
    return {
      contextPath: 'http://localhost:8081/emap/',
      newsColumns: 2,
      lmDisplayType: '0',
      newsPageSize: 10,
      card: {
        cardWid,
        cardId,
      },
      subscribeLists: [],
      isLogin: window.shell.getUserInfo() ? true : false,
      user: window.shell.getUserInfo(),
      lmReady: false,

      scroll: { barKeepShow: true },
      checked: false,
      fontClass: "",
      newList: [],
      config: {},
      containerType: 2, //初始化卡片高度选项 0：代表自适应（无最大限制）1：代表自适应（有最大限制）2：固定高度
      containerHeight: 470, // 高度 只有当heightFlag为1,2时，此值才生效
      cardWidth: 0,
      loading: false,
      newsLoading: false,
      total: 0,
      pageNum: 1,
      pageSize: 6,
      channelIds: "",
      showSubscribe: false,
      newsRead: JSON.parse(window.localStorage.getItem("newsRead") || "[]"),
    };
  },
  computed: {
    conWidth() {
      // // const columns = Number(this.config.newsColumns || 1);
      // const columns = 2;
      // // const itemWidth = (this.cardWidth - 10) / columns;
      // // if (itemWidth < minWidth) {
      // if(columns == 2){
      //     return "width: calc(50% - 10px)";
      // }
      // // }
      return `width: 100%;`;
    },
    getAutoContinerStyle() {
      if (this.lmDisplayType == '0') {
        return {
          width: '100%',
          float: 'left'
        }
      }
      return "";
    },
    emptyHeight() {
      const height = this.channelIds
        ? this.containerHeight
        : this.config.newsHeight?.value;
      return this.containerType !== 0 && height < 450 ? height : 450;
    }
  },
  created() {
    this.getConfig();
    // this.renderData();
  },
  methods: {
    getTips() {
      var tip = !!this.channelIds && this.channelIds != 'all' ? '当前栏目下暂时没有通知或公告' : '您暂时没有通知或公告';
      console.log(tip);
      return tip;
    },
    getConfig() {
      window.shell.execCardMethod(
        {
          ...this.card,
          method: "getConfig"
        },
        (res) => {
          if (res.errcode === "0") {
            let data = typeof res.data == 'string' ? JSON.parse(res.data) : res.data;
            data = typeof data == 'string' ? JSON.parse(data) : data;
            this.config = data;
            this.newsColumns = data.newsColumns || this.newsColumns;
            this.newsPageSize = data.newsPageSize || this.newsPageSize;
            this.lmDisplayType = data.lmDisplayType || this.lmDisplayType;
            this.contextPath = process.env.NODE_ENV === "development" ? "/qljfwapp" : data.contextPath || this.contextPath;
            // this.getColumns();
            this.renderData()
            this.containerType = data.newsHeight.type;
            this.containerHeight =
              this.containerType === 0
                ? 1200 - 42
                : data.newsHeight.value - 42;
          }
        }
      );
    },
    parseParams(data) {
      try {
        var tempArr = [];
        for (var i in data) {
          var key = encodeURIComponent(i);
          var value = encodeURIComponent(data[i]);
          tempArr.push(key + '=' + value);
        }
        var urlParamsStr = tempArr.join('&');
        return urlParamsStr;
      } catch (err) {
        return '';
      }
    },
    async getColumns() {
      let data = {
        'encoid': window.btoa(this.user && this.user.userAccount || ''),
        'data': JSON.stringify({})
      };
      const { data: res } = await axios.post(
        (this.contextPath + "/sys/lwPsTzggApp/lmgl/getColumnList.do?" + this.parseParams(data)).replace('//sys', '/sys'), null, {
        withCredentials: true //本地开发的时候遇到跨域请将这个改成false并且配置tomcat
      }
      );
      if (res.code == "0") {
        const data = res.data;
        this.subscribeLists = data.map(el => {
          el.wid = el.LMDM;
          el.name = el.LMMC;
          return el;
        });
        this.lmReady = true;
        this.renderData()
        this.$nextTick(function () {
          this.getCardWidth();
        });
      }
    },
    handleChangeTab(channelIds) {
      this.channelIds = channelIds;
      this.pageNum = 1;
      this.renderData();
    },
    renderData() {
      this.newsLoading = true;
      let data = {
        'encoid': window.btoa(this.user && this.user.userAccount || ''),
        "pageNum": this.pageNum,
        "pageSize": this.newsPageSize
      };
      if (this.channelIds != 'all') {
        data.columnId = this.channelIds;
      }
      axios.post(
        (this.contextPath + "/sys/lwPsTzggApp/ggll/getNoticeListByColumnId.do?" + this.parseParams(data)).replace('//sys', '/sys'), null, {
        withCredentials: true //本地开发的时候遇到跨域请将这个改成false并且配置tomcat
      }
      ).then(result => {
        const res = result.data;
        this.$nextTick(function () {
          this.newsLoading = false;
          this.loadedEndTrack();
        });
        this.$nextTick(function () {
          this.newsLoading = false;
          this.loadedEndTrack();
        });
        if (res.code === "0") {
          const data = res.data;
          let lists = data || [];
          this.newList = lists.map(el => {
            el.title = el.GGBT;
            el.isTop = el.SFZD == '1';
            el.pubTime = el.FBSJ;
            el.sub = el.GGZY;
            return el;
          });
          this.total = res.totalCount || 0;
          // this.pageSize = data.datas?.pageSize || 6;
        }
      }
      );
    },
    goLink(item) {
      this.handleClickTrack(); //点击埋点
      window.shell.emit('update-lwps-tzgg', { ...this.router })
      window.shell.openPage(item.PCURL, 1, 2);
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
    /**
     * 设置卡片的样式
     * @param num 设置一行显示多少个（平均显示）
     * @returns {string}
     */
    getCardStyle() {
      const newsColumns = Number(this.newsColumns || 1);
      const percent = Math.round((1 / newsColumns) * 10000) / 100.0 + "%";
      return (
        "width:" +
        percent +
        ";padding-left:" + (this.lmDisplayType == '0' ? 10 : 0) + "px;"
      );
    },
    handleResize() {
      this.getCardWidth();
    },
    handlePage(page) {
      this.pageNum = page;
      this.renderData();
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
    window.addEventListener("resize", this.handleResize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleResize);
  },
};
</script>

<style lang="less" scoped>
.dz_newsAnnouncement {
  width: 100%;
  min-height: 150px;
  line-height: initial;
  position: relative;
  overflow: hidden;
  box-shadow: 0.125rem 0.5rem 0.625rem 0 rgba(0, 0, 0, 2%), -0.0625rem 0 0.625rem 0 rgba(0, 0, 0, 2%);
  border-radius: 0 0 0.5rem 0.5rem;

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
  // border-bottom: 1px solid #f0f0f0;
  padding: 0 10px;
}

.flex-col-center {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 5px 0 10px 20px;
  position: absolute;
  width: 100%;
  right: 0;
  bottom: 0;
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
</style>
