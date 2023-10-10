<template>
  <we-tabs v-model="active" class="result-con" :swipe-threshold="2" swipeable scrollspy sticky offset-top="1.733rem">
    <we-tab v-for="item in dataObj" :key="item.id">
      <template slot="title">
        <div :class="[activeCode === item.code ? 'portal-primary-color-lv1' : 'portal-font-color-lv2']">
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
            >({{ item.list.length > 99 ? "99+" : item.total }})</span
          >
        </div>
      </template>
      <component
        :is="item.code"
        :total="item.total"
        :name="item.name"
        :list.sync="item.list"
        :hasExpand.sync="item.flag"
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
    activeCode() {
      return this.dataObj.find((el, index) => index === this.active)?.code
    }
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
  /deep/.we-tabs__nav {
    background: transparent;
  }
  /deep/.we-tabs__wrap {
    height: 40px;
    overflow: auto;
    &::-webkit-scrollbar {
      height: 0;
      background: transparent;
    }
    .we-tab {
      flex: none;
      height: 24px;
      line-height: 24px !important;
      font-size: 17px;
      margin-right: 16px;
      z-index: 1;
      padding: 0 3px;
      &:after {
        content: "";
        position: absolute;
        bottom: -6px;
        height: 3px;
        left: calc(50% - 8px);
        right: 3px;
        width: 16px;
        border-radius: 13px;
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
      padding: 0 12px;
    }
  }
  /deep/.we-tabs__content {
    overflow-y: auto;
    padding: 0 12px;
    .we-tab__pane {
      margin-bottom: 12px;
    }
  }
}
/deep/.we-sticky--fixed {
  // top: 65px;
  .we-tabs__nav {
    background: #fff;
    border-bottom: 0.5px solid #F0F0F0;
  }
}
</style>
