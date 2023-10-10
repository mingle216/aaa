<template>
  <div class="category-con" :id="id" :class="[showMore ? 'show-more' : '']">
    <we-tabs
      class="category-tab"
      v-model="tabIndex"
      @change="tabChange"
      swipe-threshold="1"
    >
      <we-tab
        v-for="(item, index) in categoryItemInfo"
        :key="index"
        :title=" translateName(item)"
        :name="item.categoryWid"
      >
        <ServiceList
          v-if="serviceItemInfo.length"
          v-bind="$attrs"
          :serviceItemInfo="serviceItemInfo"
          :lan-fun-name="lanFunName"
          :categoryWid="categoryWid"
          :router="router"
        />
        <EmptyCon
          v-else
          :tip="$Lan(lanFunName, 'tipMsg', '暂无服务事项权限，请联系管理员')"
        ></EmptyCon>
      </we-tab>
    </we-tabs>
    <div
      class="category-btn"
      v-if="showMore"
      @click="showMoreFunc"
    >
      <i class="iconfont icon-More"></i>
    </div>
    <customActionSheet v-model="asModel" :title="roleName">
      <div class="as-con">
        <div
          class="as-item"
          v-for="item in categoryItemInfo"
          :key="item.categoryWid"
          @click="setCategory(item)"
          :class="[
            item.categoryWid === categoryWid
              ? 'portal-primary-backgroundcolor-lv5 portal-primary-color-lv1'
              : 'portal-font-color-lv1',
          ]"
        >
          {{ translateName(item) }}
        </div>
      </div>
    </customActionSheet>
  </div>
</template>

<script>
import ServiceList from "./ServiceList";
export default {
  components: { ServiceList },
  props: {
    router: Object,
    categoryWid: {
      type: String,
      default: "",
    },
    categoryItemInfo: {
      type: Array,
      default: () => {
        return [];
      },
    },
    serviceItemInfo: {
      type: Array,
      default: () => {
        return [];
      },
    },
    roleName: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      id: "",
      tabIndex: "",
      showMore: false,
      isFirstLoad: true,
      asModel: false,
      // tip: {
      //   en_US: {
      //     tipMsg: "No relevant services at present",
      //   },
      //   zh_CN: {
      //     tipMsg: "暂无相关服务事项",
      //   },
      // },
    };
  },

  watch: {
    categoryWid: {
      immediate: true,
      handler(val) {
        this.tabIndex = val;
        this.id = `id${new Date().getTime()}`;
      },
    },
    categoryItemInfo: {
      immediate: true,
      handler(val) {
        //   console.log(val)
        if (this.isFirstLoad && val.length) {
          this.isFirstLoad = false;
          setTimeout(() => {
            let tab_w = 0;
            let web_w = document.body.clientWidth;
            let tabs = document
              .getElementById(this.id)
              .querySelectorAll(".we-tab");
            for (let n = 0; n < tabs.length; n++) {
              tab_w += tabs[n].offsetWidth + 34;
            }
            this.showMore = web_w < tab_w ? true : false;
          }, 10);
        }
      },
    },
  },
  methods: {
    showMoreFunc() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      this.asModel = true;
    },
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
    tabChange(name) {
      this.$emit("category-change", name);
    },
    setCategory(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      this.tabChange(item.categoryWid);
      this.asModel = false;
    },
  },
};
</script>
<style lang="less" scoped>
.as-con {
  // height: 100%;
  // overflow-y: auto;
  width: 100%;
  box-sizing: border-box;
  padding: 17px 17px 50px 17px;
  -webkit-user-select: none;
  user-select: none;
  overflow: hidden;
  .as-item {
    font-size: 14px;
    width: 105px;
    height: 36px;
    line-height: 36px;
    text-align: center;
    padding: 0 10px;
    box-sizing: border-box;
    background: #f6f6f8;
    border-radius: 4px;
    float: left;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
    margin-bottom: 12px;
    &:nth-child(3n-1) {
      margin: 0 12px 12px 12px;
    }
  }
}
.category-con {
  position: relative;
  &.show-more {
    .category-btn {
      position: absolute;
      height: 46px;
      box-sizing: border-box;
      border-bottom: 1px solid #e7edf1;
      width: 54px;
      background: #fff;
      box-shadow: -5px 0 5px 0 rgba(0, 0, 0, 0.04);
      right: 0;
      top: 0;
      // z-index: 2;
      .iconfont {
        font-size: 24px;
        color: #9fa8b5;
        position: absolute;
        left: 50%;
        top: 50%;
        margin: -12px;
      }
    }
    .category-tab {
      /deep/.we-tabs__wrap {
        display: flex;

        &::after {
          width: 54px !important;
          position: relative !important;
          background: #fff;
          z-index: -1;
        }
        .we-tabs__nav {
          flex: 1;

          .we-tab:nth-last-child(2) {
            padding-right: 20px !important;
          }
          .we-tab__text {
            font-weight: bold !important;
          }
        }
      }
    }
  }
}
.category-tab {
  /deep/.we-tabs__wrap {
    height: 46px !important;

    &::before {
      left: 0;
      background: linear-gradient(to right, #fff, rgba(255, 255, 255, 0));
    }

    .we-tab {
      height: 46px !important;
      line-height: 46px !important;
      font-size: 14px !important;
      &.we-tab--active {
        font-size: 14px !important;
      }
      &:after {
        display: none;
      }
    }
  }
  /deep/.we-tabs__content {
    padding: 12px 12px;
  }
}
</style>
