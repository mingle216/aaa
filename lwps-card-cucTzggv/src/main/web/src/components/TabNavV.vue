<template>
  <div :style="{height:cardHeight+'px'}" class="lwpsTzggvCard">
    <div class="newsTab">
      <div class="newsTab__left portal-font-color-lv2" ref="NewsTab">
        <div
          class="newsTab__item__wrap"
          ref="NewsTabCon"
          :style="offsetStyle"
          v-if="subscribeLists.length && okLoading"
        >
          <div
            class="newsTab__item"
            :id="`channel-${item.wid}`"
            v-for="item in subscribeListsAll"
            :key="item.wid"
            :class="[
              item.wid === selectedTab ? 'active portal-primary-color-lv1' : '',
            ]"
            @click="handleChangeMoreTab(item)"
          >{{item.name}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { clickTrack } from '../mixins/track.js';
export default {
  props: {
    cardWidth: Number,
    cardHeight: Number,
    router: Object,
    subscribeLists: Array
  },
  mixins: [clickTrack],
  data() {
    return {
      selectedTab: "",
      showArrow: false,
      disabled: true,
      okLoading: false,
      offsetStyle: "",
      configureLists: [],
      loading: false,
      isLogin: window.shell.getUserInfo() ? true : false,
    };
  },
  computed: {
    subscribeListsAll() {
      return this.subscribeLists.length > 1
        ? [{ wid: "all", name: "全部" }].concat(this.subscribeLists)
        : this.subscribeLists || [];
    },
    defaultSelected() {
      return this.subscribeLists.map((item) => item.wid);
    }
  },
  watch: {
    cardWidth() {
      this.$nextTick(function() {
        this.calculateShowMore();
      });
    },
    selectedTab() {
      this.handleUpdateNews();
    },
  },
  methods: {
    handleUpdateNews() {
      this.$emit(
        "change-tab",
        this.selectedTab
      );
    },
    async getConfiguredChannels() {
      this.loading = true;
      await window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getConfiguredChannel",
        },
        (res) => {
          this.loading = false;
          this.configureLists = res.data || [];
        }
      );
    },
    handleChangeMoreTab(item) {
      this.handleClickTrack(); //点击埋点
      if (item.wid === this.selectedTab) {
        return;
      }
      this.selectedTab = item.wid;
      const ele = document.getElementById(`channel-${item.wid}`);
      const conWid = this.$refs.NewsTab.offsetWidth;
      // 选中的栏目不在可视区域
      if (ele && ele.offsetLeft + ele.offsetWidth > conWid) {
        this.offsetStyle = `transform: translateX(${conWid -
          ele.offsetLeft -
          ele.offsetWidth -
          16}px)`;
      } else {
        this.offsetStyle = `transform: translateX(0)`;
      }
    },
    handleClickMore() {
      this.expand = !this.expand;
      this.handleClickTrack(); //点击埋点
    },
    calculateShowMore() {
      this.showArrow = this.$refs.NewsTabCon
        ? this.$refs.NewsTabCon.scrollWidth > this.$refs.NewsTab.offsetWidth
        : false;
        // console.log('=====================' + this.$refs.NewsTabCon.scrollWidth)
    },
  },
  async mounted() {
    this.selectedTab = (this.subscribeLists.length==1 && this.subscribeLists[0].wid) || "all";
    this.okLoading = true;
    this.$nextTick(function() {
        this.calculateShowMore();
      });
  },
  beforeDestroy() {
  },
};
</script>
<style lang="less" scoped>
.lwpsTzggvCard{
  position: relative;
  width: 140px;
  float: left;
  border-right: 1px solid #f0f0f0;
}
.newsTab::-webkit-scrollbar{
  width:2px;
}
.newsTab:hover::-webkit-scrollbar{
  width:6px;
}
.newsTab::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 10px;
    //background-color: #8c8c8c;
    background-color: rgba(0, 0, 0, 0.25);
}
.newsTab {
  height: 100%;
  align-items: center;
  overflow: auto;
  overflow-x: hidden;
  padding: 20px 0;
  background: #f5f5f5;
  
  .newsTab__left {
    flex: 1;
    min-width: 0;
    overflow: hidden;
    overflow-y: auto;
    .newsTab__item__wrap {
      align-items: center;
      transition: transform 0.3s ease-in;
      .newsTab__item {
        margin-right: 16px;
        cursor: pointer;
        flex-shrink: 0;
        width: 100%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        padding: 0 5px 0 12px;
        line-height: 40px;
        font-size: 16px;
        color: #1d1d1f;
        &.active {
          font-weight: 700;
          // background: #f2f2f2;
          background: #eaeaea;
          border-left: 2px solid #A40000;
        }
      }
    }
  }
  .newsTab__right {
    height: 42px;
    line-height: 42px;
    padding: 0 0 0 8px;
    position: relative;
    .newsTab__right__btn {
      cursor: pointer;
    }
    .newsTab__right__MoreIcon {
      font-size: 12px;
      margin-left: 4px;
      &.expand {
        display: inline-block;
        transform: rotate(180deg);
      }
    }
    .newsTab__right__subscribeIcon {
      margin-right: 4px;
      vertical-align: middle;
      margin-top: -2px;
    }
    .newsTab__right__split {
      margin: 0 8px;
      display: inline-block;
      height: 13px;
      width: 1px;
      vertical-align: middle;
      box-shadow: inset -1px 0 0 0 #f0f0f0;
    }
  }
  .moreShadow:before {
    content: "";
    position: absolute;
    display: block;
    width: 10px;
    height: 42px;
    top: 0;
    left: 0;
    box-shadow: -5px 0 12px -2px rgba(0, 0, 0, 0.08);
  }
}
.newsTab__more {
  visibility: hidden;
  overflow: hidden;
  max-height: 0;
  transition: max-height 0.4s ease-in;
  position: absolute;
  top: 42px;
  left: 0;
  width: 100%;
  background: #ffffff;
  box-shadow: 0 3px 16px 0 rgba(0, 0, 0, 0.09);
  z-index: 99;
  padding: 8px 20px 20px 20px;
  border-radius: 0 0 4px 4px;
  &.show {
    visibility: visible;
    max-height: initial;
  }
  .newsTab__more__item {
    cursor: pointer;
    display: inline-block;
    border-radius: 4px;
    margin-right: 12px;
    margin-top: 12px;
    padding: 3px 12px;
    line-height: 22px;
    border: 1px solid #d9d9d9;
  }
}
</style>
