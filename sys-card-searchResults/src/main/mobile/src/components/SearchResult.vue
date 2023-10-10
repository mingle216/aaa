<template>
  <we-tabs v-model="active" class="result-con" :swipe-threshold="2" swipeable>
    <we-tab v-for="item in dataObj" :key="item.id">
      <template slot="title">
        <div>
          <span>{{ item.name }}</span>
          <span v-if="item.code === 'newsItem'"
            >({{ item.total > 99 ? "99+" : item.total }})</span
          >
          <span v-else-if="item.code === 'CustomGroup'"
            >({{ item.groupDataSize > 99 ? "99+" : item.groupDataSize }})</span
          >
          <span v-else-if="item.code === 'department'"
            >({{ getDepartTotal(item.list || []) }})</span
          >
          <span v-else
            >({{ item.list.length > 99 ? "99+" : item.list.length }})</span
          >
        </div>
      </template>
      <component
        :is="item.code"
        :name="item.code"
        :list.sync="item.list"
        v-bind="$attrs"
        :lan-fun-name="lanFunName"
        :router="router"
        :item="item"
        :currentPage.sync="item.page"
        :sortType="sortType"
        :analyzeData="analyzeData"
        :newsJump="newsJump"
        :showCollectNews="showCollectNews"
        @change-sort="changeSortType"
        @change-page="changePage"
        @record-cache="$emit('record-cache', active)"
      />
    </we-tab>
  </we-tabs>
</template>

<script>
import onlineService from "./OnlineService";
import serviceItem from "./ServieItem";
import NewsItem from "./News";
import CustomGroup from "./CustomItem";
import oneThing from "./OneThing";
import department from "./Department";
import task from "./Task";
export default {
  components: {
    onlineService,
    serviceItem,
    NewsItem,
    CustomGroup,
    oneThing,
    department,
    task
  },
  props: {
    router: Object,
    dataObj: Array,
    analyzeData: Array,
    lanFunName: {
      type: String,
      default: "",
    },
    newsPageData: {
      type: Object,
      default: () => {
        return {};
      },
    },
    currentPage: {
      type: Number,
      default: 1,
    },
    sortType: {
      type: Number,
      default: 1,
    },
    newsJump: String,
    showCollectNews: Number
  },
  computed: {
    itemsNum() {
      return function(arr) {
        let t_arr = arr || [];
        return t_arr.length >= 100 ? "99+" : t_arr.length;
      };
    },
  },
  data() {
    const params = window.shell.getUrlParam();
    return {
      active: params?.activeTab ? Number(params.activeTab) : 0,
    };
  },
  methods: {
    changePage(val) {
      this.$emit("update:currentPage", val);
    },
    changeSortType(val) {
      this.$emit("update:sortType", val);
    },
    getDepartTotal(list) {
      let sum = 0;
      if (list.length) {
        sum = list.reduce(
          (prev, cur) => prev + (cur.childDept ? cur.childDept.length : 0),
          list.length
        );
      }
      return sum > 99 ? "99+" : sum;
    },
  },
};
</script>

<style lang="less" scoped>
.result-con {
  display: flex;
  flex-flow: column;
  user-select: none;
  /deep/.we-list__loading {
    width: 100%;
  }
  /deep/.we-tabs__wrap {
    height: 48px;
    overflow: auto;
    border-bottom: 1px solid #e7edf1;
    &::-webkit-scrollbar {
      height: 0;
      background: transparent;
    }
    // &::before,
    // &::after {
    //   content: "";
    //   position: absolute;
    //   z-index: 10;
    //   height: 46px;
    //   width: 17px;
    //   top: 0;
    // }
    // &::before {
    //   left: 0;
    //   background: linear-gradient(to right, #fff, rgba(255, 255, 255, 0));
    // }
    // &::after {
    //   right: 0;
    //   background: linear-gradient(to left, #fff, rgba(255, 255, 255, 0));
    // }
    .we-tab {
      flex: none;
      height: 48px;
      line-height: 48px !important;
      font-size: 16px;
      margin-right: 24px;
      z-index: 1;
      padding: 0 3px;
      &:after {
        content: "";
        position: absolute;
        bottom: 0px;
        height: 2px;
        left: 3px;
        right: 3px;
      }
      &.we-tab--active {
        font-size: 18px;
      }
    }
    .we-tabs__line {
      display: none;
    }
    .we-tabs__nav {
      box-sizing: border-box;
      // border-bottom: 1px solid #dcdfe6;
      padding: 0 17px;
    }
  }
  /deep/.we-tabs__content {
    flex: 1;
    height: 0;
    // overflow-y: auto;
    .we-tab__pane {
      height: 100%;
      padding: 12px 17px;
      overflow-y: auto;
    }
  }
}
</style>
