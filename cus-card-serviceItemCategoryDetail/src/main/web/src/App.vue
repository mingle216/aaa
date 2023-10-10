<template>
  <div class="itemCategoryDetai">
    <template v-if="categoryDatas.length">
      <div class="portal-font-color-lv1">
        <!-- tab标题 -->
        <we-tabs
          class="itemDetail__tabs"
          v-if="categoryDatas.length > 1"
          v-model="activeTypeKey"
          @tab-click="handleChangeTab"
          v-tab-disable-btn
        >
          <we-tab-pane
            :name="item.categoryWid"
            v-for="item in categoryDatas"
            :key="item.categoryWid"
            :label="translateName(item)"
          >
          </we-tab-pane>
        </we-tabs>
        <h3 v-else class="itemCategoryDetai__header ellipsis">
          {{ translateName(categoryDatas[0]) }}
        </h3>
        <tab-body
          ref="TabBody"
          :configInfo="configInfo"
          :categroyType="categroyType"
          :router="router"
          @loadEnd="loadedEndTrack"
        ></tab-body>
      </div>
    </template>
    <empty-con
      v-else-if="inited"
      :tip="
        $Lan(
          'SYS_CARD_SERVICEITEMCATEGORYDETAIL',
          'noServiceItem',
          '暂无相关服务事项'
        )
      "
    ></empty-con>
  </div>
</template>

<script>
import TabBody from "./components/TabBody";
import { initTrack } from './mixins/track.js';
export default {
  name: "CardItemCategoryDetail",
  props: ["router"],
  mixins: [initTrack],
  components: {
    TabBody,
  },
  data() {
    return {
      activeTypeKey: "",
      categroyType: null,
      optRow: null,
      configInfo: null,
      categoryDatas: [],
      urlParams: null,
      inited: false,
    };
  },
  methods: {
    translateName(item) {
      let categoryName = item.categoryName.match(/按([\s\S]*)分类/) || [];
      if (item.categoryWid === "dept-subject") {
        categoryName = [
          "",
          this.$Lan("SYS_CARD_SERVICEITEMCATEGORYDETAIL", "department", "部门"),
        ];
      }
      return this.$Lan(
        "SYS_CARD_SERVICEITEMCATEGORYDETAIL",
        "categoryName",
        "按{name}分类",
        {
          name: categoryName.length > 1 ? categoryName[1] : "",
        }
      );
    },
    init(notChangeSelect) {
      this.urlParams = window.shell.getUrlParam();
      if (!this.urlParams) {
        this.inited = true;
        return;
      }
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "render",
          param: {
            roleWid: this.urlParams.roleWid,
            oneCategoryWid: this.urlParams.oneCategoryWid,
            categoryWid: this.urlParams.categoryWid,
            parentCardWid: this.urlParams.cardWid
          },
        },
        (data) => {
          if (data && data.errcode === "0") {
            const configure = (data.data && data.data.config) || {};
            this.configInfo = {
              cardId: this.router && this.router.cardId,
              cardWid: this.router && this.router.cardWid,
              roleWid: data.data.roleWid,
              oneCategoryWid: data.data.oneCategoryWid,
              categoryWid: data.data.categoryWid,
              hasIcon: configure.hasIcon, // 1:显示 0:隐藏
              sortType: configure.sortType, // 0:默认排序    1:按访问量排序
              quickConfig: configure.quickConfig,    // 可配置仅移动端办理
              deptIconType: configure.departClassIcon, // 事项分类卡片的部门图标配置
              otherClassIcon: configure.otherClassIcon
            };
            // 一级分类数据
            this.getCategoryData(notChangeSelect);
            if (data.data && data.data.roleName) {
              // 修改面包屑名称
              window.shell.changeBreadcrumbName(
                this.$Lan(
                  "SYS_CARD_SERVICEITEMCATEGORYDETAIL",
                  "breadName",
                  "{name}办事",
                  {
                    name: data.data.roleName,
                  }
                )
              );
            }
          } else {
            this.inited = true;
          }
        }
      );
    },
    getCategoryData(notChangeSelect) {
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "getOneCategoryList",
        },
        (data) => {
          this.inited = true;
          if (data && data.errcode === "0") {
            this.categoryDatas = data.data.data.reverse() || [];
            if (this.categoryDatas.length) {
              if (notChangeSelect) {
                // 切换多语言，重新请求接口
                this.$refs.TabBody.pureUpdateData();
              } else {
                // 选中默认标签
                const matchedKey =
                  (this.urlParams && this.urlParams.oneCategoryWid) ||
                  this.categoryDatas[0].categoryWid;
                const arr = this.categoryDatas.filter(
                  (item) => item.categoryWid === matchedKey
                );
                this.$nextTick(function() {
                  this.activeTypeKey = arr.length
                    ? arr[0].categoryWid
                    : this.categoryDatas[0].categoryWid;
                  this.categroyType = arr.length
                    ? arr[0]
                    : this.categoryDatas[0];
                });
              }
            }
          } else {
            this.categoryDatas = [];
          }
        }
      );
    },
    handleChangeTab({ index }) {
      const item = this.categoryDatas[Number(index)] || {};
      this.categroyType = {
        categoryName: item.categoryName,
        categoryWid: item.categoryWid,
      };
      this.handleClickTrack({
        infoType: 8,
        filterInfo: {
          categoryWid: item.categoryWid
        }
      }); // 点击埋点
    },
  },
  created() {
    window.shell.handleBackTop();
  },
  beforeDestroy() {},
  mounted() {
    // 获取配置信息
    this.init();
  },
};
</script>

<style scoped lang="less">
.itemCategoryDetai {
  width: 100%;
  .itemCategoryDetai__header {
    padding: 12px 0px;
    font-size: 18px;
    line-height: 26px;
    font-weight: bold;
    box-shadow: inset 0 -1px 0 0 #f0f0f0;
  }
  .itemDetail__tabs {
    /deep/.we-tabs__header {
      margin: 0;
    }
    /deep/.we-tabs__item {
      height: 56px;
      line-height: 56px;
      font-size: 18px;
      background:rgba(29, 29, 31, 0.5);
      color: #FFFFFF!important;
      width: 248px;
      padding:0;
      text-align: center;
      &.is-active {
        color: #000000!important;
        font-weight: bold;
        background:#FFFFFF;
      }
    }
    /deep/.we-tabs__active-bar {
      /* background-color: transparent; */
      &::before {
        content: "";
        width: 100%;
        height: 2px;
        bottom: 0px;
        left: 0;
        position: absolute;
      background-color: #ffffff!important;
      }
    }
    /deep/.we-tabs__nav-wrap::after {
      height: 1px;
      background-color: #f0f0f0;
    }
    /deep/.is-scrollable {
      padding-left: 0;
      padding-right: 68px;
      .we-tabs__nav-next,
      .we-tabs__nav-prev {
        width: 28px;
        height: 28px;
        line-height: 28px;
        text-align: center;
        border: 1px solid #d9d9d9;
        border-radius: 2px;
        top: 50%;
        font-size: 14px;
        transform: translateY(-50%);
      }
      .we-tabs__nav-prev {
        left: auto;
        right: 34px;
      }
    }
  }
}
</style>
