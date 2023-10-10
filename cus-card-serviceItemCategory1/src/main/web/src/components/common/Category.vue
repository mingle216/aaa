<template>
  <div class="show-type" :class="{ empty: !categoryList }">
    <template v-if="categoryList">
      <we-tabs
        class="service-tab"
        v-if="categoryList"
        v-model="serviceTab"
        @tab-click="typeClick"
        v-tab-disable-btn
      >
        <we-tab-pane
          class="type-item"
          v-for="item in categoryList"
          :name="'tab' + item.categoryWid"
          :label="translateName(item)"
          :key="item.categoryWid"
        ></we-tab-pane>
      </we-tabs>
    </template>
  </div>
</template>

<script>
import { clickTrack } from '../../mixins/track.js';
export default {
  props: {
    categoryList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    categoryWid: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
    router: Object
  },
  mixins: [clickTrack],
  data() {
    return {
      serviceTab: "",
      isFirstLoad: true,
    };
  },
  watch: {
    categoryList: {
      handler(val) {
        if (val && val.length && this.isFirstLoad) {
          this.$nextTick(function() {
            this.serviceTab = "tab" + this.categoryList[0].categoryWid;
          });

          this.isFirstLoad = false;
        }
      },
      immediate: true,
    },
  },
  mounted() {},
  methods: {
    translateName(item) {
      let categoryNames = item.categoryName.match(/按([\s\S]*)分类/) || [];
      if (item.categoryWid === "dept-subject") {
        categoryNames = [
          "",
          this.$Lan(this.lanFunName, "department", "部门"),
        ];
      }
      return this.$Lan(this.lanFunName, "categoryName", "按{name}分类", {
        name: categoryNames.length > 1 ? categoryNames[1] : "",
      });
    },
    typeClick() {
      this.handleClickTrack({
        infoType: 8,
        filterInfo: {
          categoryWid: this.serviceTab.replace("tab", "")
        }
      }); // 点击埋点
      if ("tab" + this.categoryWid === this.serviceTab) {
        return;
      }
      this.$emit("category-change", this.serviceTab.replace("tab", ""));
    },
  },
};
</script>

<style lang="less" scoped>
.show-type {
  height: 54px;
  box-sizing: border-box;
  font-size: 14px;
  display: none;

  &.empty {
    height: 25px;
    padding: 0;
  }
  .service-tab {
    /deep/.we-tabs__header {
      margin: 0;
      border-bottom: none !important;
    }
    /deep/.we-tabs__item {
      height: 54px;
      line-height: 54px;
      font-size: 14px;
      font-weight: normal;
      position: relative;
      padding: 0 16px;
      &:nth-child(2) {
        padding-left: 0;
      }
      &:not(:last-child)::after {
        content: "";
        width: 1px;
        height: 12px;
        border-right: 1px solid #cdd8ec;
        text-align: center;
        position: absolute;
        right: 0;
        top: 50%;
        margin-top: -6px;
      }
    }
    /deep/.we-tabs__active-bar {
      background-color: transparent;
      display: none;
    }
    /deep/.we-tabs__nav-wrap::after {
      height: 0 !important;
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
