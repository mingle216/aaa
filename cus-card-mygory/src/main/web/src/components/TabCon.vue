<template>
  <div class="tab-con">
    <we-tabs
      class="service-tab"
      v-if="tabList"
      v-model="serviceTab"
      @tab-click="clickHandle"
      v-tab-disable-btn
    >
      <we-tab-pane
        v-for="item in tabList"
        :name="'tab' + item.roleWid"
        :label="
          $Lan(lanFunName, 'labelText', '{name}办事', { name: item.roleName })
        "
        :key="item.roleWid"
      ></we-tab-pane>
    </we-tabs>
    <category
      :categoryList="categoryList"
      @category-change="categoryHandle"
      :categoryWid="active.categoryWid"
      :lan-fun-name="lanFunName"
      :router="router"
    ></category>
    <serviceList
      v-loading="loading"
      :dataList="serviceList"
      :config="config"
      :lan-fun-name="lanFunName"
      :isDept="active.categoryWid === 'dept-subject'"
      @clickItem="clickItemHandle"
    />
  </div>
</template>

<script>
import category from "./common/Category";
import serviceList from "./common/ServiceList";
import { clickTrack } from '../mixins/track.js';
export default {
  components: {
    category,
    serviceList,
  },
  props: {
    active: Object,
    config: Object,
    categoryList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    tabList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    serviceList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    loading: {
      type: Boolean,
      default: false,
    },
    lanFunName: {
      type: String,
      default: "",
    },
    router: Object,
  },
  mixins: [clickTrack],
  data() {
    return {
      serviceTab: "",
    };
  },
  computed: {},
  watch: {
    active: {
      handler(val) {
        if (val) {
          this.serviceTab = "tab" + val.roleWid;
        }
      },
      immediate: true,
    },
  },
  methods: {
    clickHandle(ev) {
      this.handleClickTrack({
        infoType: 8,
        filterInfo: {
          roleWid: ev.name.replace("tab", "")
        }
      }); // 点击埋点
      if ("tab" + this.active.roleWid === ev.name) {
        return;
      }
      this.$emit("tab-change", ev.name.replace("tab", ""));
    },
    categoryHandle(ev) {
      this.$emit("category-change", {
        categoryWid: ev,
        roleWid: this.serviceTab.replace("tab", ""),
      });
    },
    clickItemHandle(item) {
      this.$emit("clickItem", {
        subjectWid: item.subjectWid,
        roleWid: this.serviceTab.replace("tab", ""),
        categoryWid: this.active.categoryWid,
      });
    },
  },
};
</script>

<style lang="less" scoped>
.tab-con {
  position: relative;
  .service-tab {
    /deep/.we-tabs__header {
      margin: 0;
    }
    /deep/.we-tabs__item {
      height: 54px;
      line-height: 54px;
      padding: 0;
      font-size: 18px;
      color: rgba(16,38,69,.6)!important;
      margin-right: 20px;
      &:hover{
        color: #102645!important;
      }
      &.is-top:nth-child(2) {
        padding-left: 0 !important;
      }
      &.is-active {
        // font-size: 18px;
        font-weight: bold;
        color: #102645!important;
        border-bottom: 2px solid #2E8CF0 !important;
      }
    }
    /deep/.we-tabs__active-bar {
      background-color: transparent;

      // &::before {
      //   content: "";
      //   width: 24px;
      //   height: 4px;
      //   bottom: 0px;
      //   left: 50%;
      //   margin-left: -12px;
      //   border-radius: 2px;
      //   position: absolute;
      // }
    }
    /deep/.we-tabs__nav-wrap::after {
      height: 1px;
    }
    /deep/.is-scrollable {
      padding-left: 0;
      padding-right: 60px;
      .we-tabs__nav-next,
      .we-tabs__nav-prev {
        width: 24px;
        height: 24px;
        line-height: 24px;
        text-align: center;
        border: 1px solid #cdd5e0;
        border-radius: 2px;
        top: 50%;
        font-size: 14px;
        margin-top: -12px;
        // &.is-disabled {
        //   opacity: 0.5;
        // }
      }
      .we-tabs__nav-prev {
        left: auto;
        right: 30px;
      }
    }
  }
}
</style>
