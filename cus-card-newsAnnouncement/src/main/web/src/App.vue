<template>
  <div class="newsAnnouncement" ref="NewsAnnouncement" v-loading="loading">
    <AutoContainer
      :scroll="scroll"
      v-if="newList && newList.length > 0"
      :con-type="containerType"
      :con-height="containerHeight"
    >
      <div v-loading="newsLoading">
        <div
          :class="[imgFloatLayout ? 'news-list-float' : 'news-list']"
          :style="conWidth"
        >
          <div
            v-for="(item, index) in newList"
            :key="index"
            :style="getCardStyle(index)"
            :class="[imgFloatLayout ? 'float' : '']"
          >
            <!-- 标题、时间及摘要 -->
            <TimeTitleAbstractval
              v-if="config.newsDisplay && config.newsDisplay.type == 1"
              :itemInfo="item"
              :config="config"
              :lastRow="isLastRow(index)"
              @go-link="goLink"
            >
            </TimeTitleAbstractval>
            <!-- 图片、标题、时间及摘要 -->
            <TimeTitleImg
              v-else-if="config.newsDisplay && config.newsDisplay.type == 2"
              :itemInfo="item"
              :config="config"
              :lastRow="isLastRow(index)"
              :extremum="conWidth.includes('px')"
               @go-link="goLink"
            >
            </TimeTitleImg>
            <!-- 标题、时间及发布人 -->
            <TimeTitlePublisher
              v-else-if="config.newsDisplay && config.newsDisplay.type == 3"
              :index="index"
              :itemInfo="item"
              :config="config"
              :lastRow="isLastRow(index)"
               @go-link="goLink"
            >
            </TimeTitlePublisher>
            <!-- 标题、时间 -->
            <TimeTitle
              v-else
              :itemInfo="item"
              :config="config"
              :cardWidth="cardWidth"
              :lastRow="isLastRow(index)"
               @go-link="goLink"
            >
            </TimeTitle>
          </div>
        </div>
        <!-- 展开更多 -->
        <div
          class="newsAnnouncement__page portal-primary-color-lv1"
          v-if="showMore"
          @click="handleShowMore"
        >
          <span class="ellipsis">查看更多</span>
          <i class="iconfont icon-menu-Unfold newsAnnouncement__page-icon"></i>
        </div>
      </div>
    </AutoContainer>
    <EmptyCon
      v-else
      :tip="
        showSubscribe && config.newsShowSubscribe == 1
          ? ''
          : $Lan('CUS_CARD_NEWSANNOUNCEMENT', 'noData', '暂无内容')
      "
      :subTip="
        showSubscribe && config.newsShowSubscribe == 1
          ? $Lan('CUS_CARD_NEWSANNOUNCEMENT', 'noData', '暂无内容')
          : ''
      "
      :height="emptyHeight"
      :switchSize="switchSize"
      :class="[emptyHeight > switchSize ? '' : 'emptySmall']"
      style="width: 100%"
    >
    </EmptyCon>
  </div>
</template>
<script>
import axios from 'axios';
axios.defaults.withCredentials=true
import TimeTitleAbstractval from "./components/TimeTitleAbstractval";
import TimeTitle from "./components/TimeTitle";
import TimeTitleImg from "./components/TimeTitleImg";
import TimeTitlePublisher from "./components/TimeTitlePublisher";

export default {
  components: {
    TimeTitleAbstractval,
    TimeTitle,
    TimeTitleImg,
    TimeTitlePublisher,
  },
  props: {
    router: Object,
  },
  data() {
    return {
      scroll: { barKeepShow: true },
      checked: false,
      fontClass: "",
      newList: [],
      config: {},
      containerType: 0, //初始化卡片高度选项 0：代表自适应（无最大限制）1：代表自适应（有最大限制）2：固定高度
      containerHeight: 0, // 高度 只有当heightFlag为1,2时，此值才生效
      cardWidth: 0,
      loading: false,
      newsLoading: false,
      current: 1,
      total: 0,
      pageSize: 6,
      channelIds: "",
      showSubscribe: false,
      dataList:[
        {
          FBBM: "0220",
          FBBM_DISPLAY: "党政办",
          FBR: "080032",
          FBSJ: "2022-02-11 16:59:04",
          FJ: null,
          GGBT: "1234",
          GGDM: "D7BB43F08563571DE0533C6DC3DAAF9E",
          GGNR: "<p style=\"line-height: 2em;\"><span style=\"font-family: 微软雅黑; font-size: 14px;\"><span style=\"font-family: 微软雅黑; font-size: 14px;\"></span>12344556</span><br/></p>",
          GGZT: "1",
          LLQX: "1",
          LMDM: "D069C1C90FC426E6E0533C6DC3DA3B0D",
          LMMC: "信息化建设管理处",
          NRLX: "1",
          PCURL: null,
          YDURL: null,
          YXSJ: null,
          ZDZT: null,
        },
        {
          FBBM: "0248",
          FBBM_DISPLAY: "信息化建设与管理处",
          FBR: "ampadmin",
          FBSJ: "2022-01-24 15:21:19",
          FJ: null,
          GGBT: "测试123456",
          GGDM: "D64D85437BAB6591E0533C6DC3DACAD3",
          GGNR: "<p style=\"line-height: 2em;\"><span style=\"font-family: 微软雅黑; font-size: 14px;\">23344353</span><br/></p>",
          GGZT: "1",
          LLQX: "1",
          LMDM: "D64FCCCC4C17159BE0533C6DC3DABA3E",
          LMMC: "测试2343",
          NRLX: "1",
          PCURL: null,
          YDURL: null,
          YXSJ: null,
          ZDZT: null,
        },{
          FBBM: "信息化管理处",
          FBBM_DISPLAY: "信息化管理处",
          FBR: "080032",
          FBSJ: "2021-12-30 20:56:56",
          FJ: null,
          GGBT: "测试",
          GGDM: "D0596EE9E6D53EE5E0533C6DC3DACCE0",
          GGNR: "<p style=\"line-height: 2em;\"><span style=\"font-family: 微软雅黑; font-size: 14px;\">测试</span><br/></p>",
          GGZT: "1",
          LLQX: "1",
          LMDM: "D069C1C90FC426E6E0533C6DC3DA3B0D",
          LMMC: "信息化建设管理处",
          NRLX: "1",
          PCURL: null,
          YDURL: null,
          YXSJ: null,
          ZDZT: null,
        },{
          FBBM: "信息化管理处",
          FBBM_DISPLAY: "信息化管理处",
          FBR: "080032",
          FBSJ: "2021-12-30 20:56:56",
          FJ: null,
          GGBT: "测试",
          GGDM: "D0596EE9E6D53EE5E0533C6DC3DACCE0",
          GGNR: "<p style=\"line-height: 2em;\"><span style=\"font-family: 微软雅黑; font-size: 14px;\">测试</span><br/></p>",
          GGZT: "1",
          LLQX: "1",
          LMDM: "D069C1C90FC426E6E0533C6DC3DA3B0D",
          LMMC: "信息化建设管理处",
          NRLX: "1",
          PCURL: null,
          YDURL: null,
          YXSJ: null,
          ZDZT: null,
        },{
          FBBM: "信息化管理处",
          FBBM_DISPLAY: "信息化管理处",
          FBR: "080032",
          FBSJ: "2021-12-30 20:56:56",
          FJ: null,
          GGBT: "测试",
          GGDM: "D0596EE9E6D53EE5E0533C6DC3DACCE0",
          GGNR: "<p style=\"line-height: 2em;\"><span style=\"font-family: 微软雅黑; font-size: 14px;\">测试</span><br/></p>",
          GGZT: "1",
          LLQX: "1",
          LMDM: "D069C1C90FC426E6E0533C6DC3DA3B0D",
          LMMC: "信息化建设管理处",
          NRLX: "1",
          PCURL: null,
          YDURL: null,
          YXSJ: null,
          ZDZT: null,
        }
        
      ]
      
    };
  },
  computed: {
    showMore() {
      if(this.config.showMore == 1){
        return true
      }else{
        if(this.config.newsTotal < this.total){
          return true
        }else{
          return false
        }

      }
      ;
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
      const minWidth = 280;
      if (itemWidth < minWidth) {
        return `width: ${columns * minWidth}px`;
      }
      return "width: 100%";
    },
    emptyHeight() {
      const height = this.channelIds
        ? this.containerHeight
        : this.config.newsHeight?.value;
        console.log(this.containerType !== 0 && height < 450 ? height : 450)
      return this.containerType !== 0 && height < 450 ? height : 450;
    },
    switchSize() {
      return this.showSubscribe && this.config.newsShowSubscribe == 1
        ? 150
        : 100;
    },
  },
  methods: {
    goLink(item) {
      if(item.GGDM){
        window.open('http://newehall.snut.edu.cn/psfw/sys/tzggapp/*default/index.do#/ggll/1/'+item.GGDM);
      }else{
         window.shell.showMessage({
            message: this.$Lan(
              "CUS_CARD_NEWSANNOUNCEMENT_h5",
              "noLink",
              "暂无跳转地址"
            ),
            type: "warning",
          });
      }
      
    },
    handleShowMore() {
      window.open('http://newehall.snut.edu.cn/psfw/sys/tzggapp/*default/index.do#/ggll')
    },
    getdata(pageSize){
      axios.get("http://newehall.snut.edu.cn/psfw/sys/tzggapp/modules/ggll/cxlmxdggxx.do?LMDM=ALL_LMDM&pageSize="+ pageSize+"&pageNumber=1",{
            //withCredentials:true
            }).then(res => {
              if(res.data.code=='0'){
                this.newList= res.data.datas.cxlmxdggxx.rows
                this.total = res.data.datas.cxlmxdggxx ? res.data.datas.cxlmxdggxx.totalSize : 0
              }
            })
            .catch(error => {
              console.log(error)
            })
    },
    renderData() {
      this.newsLoading = true;
      //this.newList= this.dataList
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getChannelNews",
          param: {
            channelIds: this.channelIds,
            pageNumber: 1,
          },
        },
        (res) => {
          this.$nextTick(function() {
            this.newsLoading = false;
          });
          if (res.errcode === "0") {
            const data = res.data;
            //const lists = data.datas?.data || [];
            this.config = data.configInfo || {};
            this.getdata(this.config.newsTotal)
            //this.newList = lists;
            //this.total = data.datas?.totalSize || 0;
            //this.pageSize = data.datas ? data.datas.pageSize: 6;
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
  },
  mounted() {
    this.cardWidth = this.$refs.NewsAnnouncement.offsetWidth;
    this.renderData();
    //this.getdata();
    window.addEventListener("resize", () => {
      this.cardWidth = this.$refs.NewsAnnouncement.offsetWidth;
    });
  },
  beforeDestroy() {
    window.removeEventListener("resize", () => {});
  },
};
</script>

<style lang="less" scoped>
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
