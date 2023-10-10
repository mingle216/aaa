<template>
  <div class="itemCategoryDetail" v-loading="initLoading">
    <!-- 筛选项 -->
    <div class="itemCatDetail__search sideline-border-color-lv2">
      <we-search
        class="itemCatDetail__input"
        v-model="keyword"
        :placeholder="
          $Lan(
            'CUS_CARD_MYGORYDETAIL_h5',
            'itemPlaceholder',
            '请输入事项名称'
          )
        "
        @search="handleSearch"
        @focus="showSearch = true"
      />
      <span
        v-if="showSearch"
        class="itemCatDetail__searchEnter ellipsis portal-font-color-lv2"
        @click="handleSearch"
        >{{
          $Lan("CUS_CARD_MYGORYDETAIL_h5", "search", "搜索")
        }}</span
      >
    </div>
    <div
      class="itemCatDetail__searchSub sideline-border-color-lv2"
      v-show="!showSearch"
    >
      <div class="itemCatDetail__checkbox">
        <we-checkbox v-model="isOnline" shape="square">{{
          $Lan(
            "CUS_CARD_MYGORYDETAIL_h5",
            "canDealOnline",
            "仅显示可在线办理事项"
          )
        }}</we-checkbox>
      </div>
      <!-- 二级分类 -->
      <div
        class="itemCatDetail__category portal-font-color-lv1"
        v-if="secondCategoryLists.length"
        @click="handleSelect"
      >
        <div
          class="itemCatDetail__category__item"
          :class="[
            item.categoryWid === selectedId
              ? 'portal-primary-backgroundcolor-lv5 portal-primary-color-lv1'
              : '',
          ]"
          v-for="item in secondCategoryLists"
          :key="item.categoryWid"
          :data-id="item.categoryWid"
        >
          {{ item.categoryName }}
        </div>
      </div>
    </div>
    <template v-if="!showSearch">
      <div
        v-if="dataLists.length"
        class="itemCatDetail__listsWrap"
        :class="{ noSubInfo: !showSubInfo }"
      >
        <we-list
          v-model="loading"
          class="itemCatDetail__lists"
          :finished="finished"
          :finished-text="
            $Lan(
              'CUS_CARD_MYGORYDETAIL_h5',
              'noMore',
              '— 没有更多内容了 —'
            )
          "
          :immediate-check="false"
          @load="handlePageChange"
        >
          <div
            class="itemCatDetail__item sideline-border-color-lv2"
            v-for="item in dataLists"
            :key="item.itemWid"
            @click="openServiceDetail(item)"
          >
            <img
              v-if="hasIcon == 1"
              class="itemCatDetail__itemImg"
              :src="item.iconLink || errorImg"
              @error="handleError"
            />
            <div class="itemCatDetail__itemInfoWrap portal-font-color-lv1">
              <div
                class="itemCatDetail__itemName ellipsis"
                v-html="handleHighLighter(item.itemName)"
              ></div>
              <div
                class="itemCatDetail__itemInfo"
                v-for="(category, index) in categoryLists"
                :key="category.label"
                :class="[index === 0 ? 'mt-12' : '']"
              >
                <div class="portal-font-color-lv2 itemCatDetail__itemLabel">
                  {{
                    category.name && category.name.length > 6
                      ? `${category.name.slice(0, 4)}...${category.name.slice(
                          category.name.length - 1
                        )}`
                      : category.name
                  }}
                </div>
                <div>:</div>
                <div
                  class="itemCatDetail__itemContent ellipsis"
                  v-if="category.label === 'duty_dept_id'"
                >
                  {{ item.itemDept || "-" }}
                </div>
                <div class="itemCatDetail__itemContent ellipsis" v-else>
                  {{
                    category.label === "service_obj"
                      ? item.roleName || "-"
                      : item.otherCategory[category.label] || "-"
                  }}
                </div>
              </div>
            </div>
            <!-- 在线办理 -->
            <!-- onlineServiceType 2 显示， 1 禁用， 0 不显示 -->
            <div
              class="itemCatDetail__button"
              v-if="item.onlineServiceType !== 0"
            >
              <we-button
                plain
                size="small"
                class="portal-primary-color-lv1 portal-primary-border-color-lv1"
                :class="[item.onlineServiceType == 1 ? 'button-disabled' : '']"
                @click.stop="handleOnline(item)"
                >{{
                  $Lan(
                    "CUS_CARD_MYGORYDETAIL_h5",
                    "dealOnline",
                    "在线办理"
                  )
                }}</we-button
              >
            </div>
          </div>
        </we-list>
      </div>
      <EmptyCon
        :tip="
          $Lan(
            'CUS_CARD_MYGORYDETAIL_h5',
            'noServiceItem',
            '未找到相关服务事项'
          )
        "
        v-if="!dataLists.length && !loading"
      ></EmptyCon>
    </template>
  </div>
</template>

<script>
import TrackMixin from './mixins/track.js';
export default {
  components: {},
  name: "",
  props: {
    router: Object,
  },
  mixins: [TrackMixin],
  data() {
    return {
      keyword: "",
      isOnline: false,
      loading: false,
      initLoading: false,
      dataLists: [],
      total: 0,
      current: 1,
      pageSize: 15,
      hasIcon: false,
      errorImg: window.shell.ErrorImg,
      showSearch: false,
      categoryLists: [],
      secondCategoryLists: [],
      selectedId: "",
    };
  },
  computed: {
    finished() {
      return this.total < this.current * this.pageSize;
    },
    showSubInfo() {
      return this.categoryLists.length;
    },
  },
  watch: {
    isOnline() {
      this.current = 1;
      this.showSearch = false;
      this.dataLists = [];
      this.fetchLists();
      this.handleClickTrack(); // 点击埋点
    },
  },
  created() {
    if (window.shell) {
      this.init();
    }
  },
  beforeDestroy() {},
  methods: {
    init() {
      this.urlParams = window.shell.getUrlParam();
      if (!this.urlParams) {
        return;
      }
      // 设置浏览器展示名称
      if (this.urlParams.categoryName) {
        window.shell.emit("page-title-change", this.urlParams.categoryName);
      }
      this.initLoading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "render",
          param: {
            roleWid: this.urlParams.roleWid,
            oneCategoryWid: this.urlParams.oneCategoryWid,
            categoryWid: this.urlParams.categoryWid,
          },
        },
        (data) => {
          this.initLoading = false;
          if (data && data.errcode === "0") {
            const configure = (data.data && data.data.config) || {};
            (this.hasIcon = configure.hasIcon), // 1:显示 0:隐藏
              this.handleCatConfig(
                configure.displayInfo || [],
                data.data.cateNames || []
              );
            this.fetchLists();
          } else {
            this.loadedEndTrack(); // 加载结束埋点
          }
        }
      );
      this.fetchCategory();
    },
    handlePageChange() {
      this.current += 1;
      this.fetchLists();
    },
    handleCatConfig(displayInfo, cateNames) {
      this.categoryLists = displayInfo.map((item) => {
        // const key = item.toLowerCase().replace(/^(cat_)/g, "");
        const match = cateNames.find((curr) => curr.value === item) || {};
        return {
          label: item,
          name: match.label || "",
        };
      });
    },
    handleSearch() {
      this.current = 1;
      this.showSearch = false;
      this.dataLists = [];
      this.fetchLists();
      this.handleClickTrack({
        infoType: 3,
        filterInfo: {
          keyword: this.keyword.trim()
        }
      }); // 点击埋点
    },
    handleHighLighter(item) {
      return window.shell.strHighlighted(item, this.keyword.trim());
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    handleOnline(item) {
      this.handleClickTrack({
        infoType: 1,
        itemId: item.itemWid,
        fucType: 1
      }); // 点击埋点
      if (item.onlineServiceType == 1) {
        window.shell.showMessage({
          type: "warning",
          message: `${this.$Lan("public", "noPermission", "暂无使用权限，请联系管理员")}`,
        });
        return;
      }
      window.shell.openOnlineDeal({
        ...item,
        wid: item.itemWid,
        name: item.itemName,
      });
    },
    openServiceDetail(item) {
      if (item.workGuide) {
        this.handleClickTrack({
          infoType: 1,
          itemId: item.itemWid,
          fucType: 0
        }); // 点击埋点
        window.shell.openServiceItem({
          ...item,
          wid: item.itemWid,
          name: item.itemName,
        });
      } else {
        this.handleOnline(item);
      }
    },
    fetchLists() {
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "queryItemByCategoryList",
          param: {
            // isRelate: "0",
            roleWid: this.urlParams.roleWid,
            categoryWid: this.selectedId || this.urlParams.categoryWid,
            searchKey: this.keyword.trim().toLowerCase(),
            pageNumber: this.current,
            pageSize: this.pageSize,
            isAuthority: this.isOnline,
          },
        },
        (data) => {
          this.loading = false;
          if (data && data.errcode === "0") {
            this.dataLists = this.dataLists.concat(
              (data.data && data.data.data) || []
            );
            this.total = (data.data && data.data.totalSize) || 0;
          } else {
            this.dataLists = [];
            this.total = 0;
          }
          this.loadedEndTrack(); // 加载结束埋点
        }
      );
    },
    fetchCategory() {
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "allCateGoryList",
          param: {
            isRelate: "0",
            roleWid: this.urlParams.roleWid,
            oneCategoryWid: this.urlParams.oneCategoryWid,
            searchValue: "",
          },
        },
        (resp) => {
          if (resp && resp.errcode === "0") {
            const lists = (resp.data.data && resp.data.data.children) || [];
            const currCat =
              lists.find(
                (item) => item.categoryWid === this.urlParams.categoryWid
              ) || {};
            this.secondCategoryLists = currCat.children || [];
          }
        }
      );
    },
    handleSelect(e) {
      const id = e.target.dataset.id;
      if (id) {
        this.selectedId = this.selectedId == id ? "" : id;
        this.dataLists = [];
        this.current = 1;
        this.fetchLists();
      }
      this.handleClickTrack({
        infoType: 8,
        filterInfo: {
          categoryWid: id
        }
      }); // 点击埋点
    },
  },
};
</script>

<style lang="less" scoped>
.itemCategoryDetail {
  height: 100vh;
  font-size: 14px;
  display: flex;
  flex-direction: column;
  .itemCatDetail__search {
    padding: 0 17px;
    display: flex;
    align-items: center;
    border-bottom: 1px solid;
    .itemCatDetail__input {
      flex: 1;
      padding: 7px 0;
      /deep/.we-search__content {
        background: #f6f6f8;
      }
    }
    .itemCatDetail__searchEnter {
      font-size: 16px;
      margin-left: 12px;
      max-width: 80px;
    }
  }
  .itemCatDetail__searchSub {
    padding: 12px 17px;
    border-bottom: 1px solid;
    .itemCatDetail__checkbox {
      display: flex;
      align-items: center;
      /deep/.we-checkbox__icon {
        font-size: 16px;
        .we-icon {
          width: 16px;
          height: 16px;
          display: table;
          line-height: 1;
          border-radius: 2px;
          border-color: #d6dade;
        }
        .we-icon-success:before {
          width: 1em;
          height: 1em;
          display: table-cell;
          vertical-align: middle;
        }
      }
      /deep/.we-checkbox__label {
        height: 20px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        word-break: break-all;
      }
    }
    .itemCatDetail__category {
      margin-top: 12px;
      display: flex;
      overflow-x: auto;
      overflow-y: hidden;
      flex-wrap: nowrap;
      white-space: nowrap;
      -webkit-overflow-scrolling: touch;
      // 隐藏滚动条
      scrollbar-width: none;
      /* Firefox */
      -ms-overflow-style: none;
      &::-webkit-scrollbar {
        display: none;
      }
      .itemCatDetail__category__item {
        background: #f6f6f8;
        border-radius: 4px;
        padding: 6px;
        line-height: 18px;
        &:not(:last-child) {
          margin-right: 15px;
        }
      }
    }
  }
  .itemCatDetail__listsWrap {
    flex: 1;
    height: 0;
    // min-height: 0;
    .itemCatDetail__lists {
      height: 100%;
      padding: 0 17px;
      overflow-y: auto;
    }
    .itemCatDetail__item {
      display: flex;
      padding: 16px 0;
      &:not(:last-child) {
        border-bottom: 1px solid;
      }
      .itemCatDetail__itemImg {
        width: 36px;
        height: 36px;
        border-radius: 3px;
        margin-right: 12px;
      }
      .itemCatDetail__itemInfoWrap {
        flex: 1;
        min-width: 0;
        .itemCatDetail__itemName {
          font-size: 16px;
          font-weight: bold;
        }
        .itemCatDetail__itemInfo {
          display: flex;
          align-items: center;
          &.mt-12 {
            margin-top: 12px;
          }
          &:not(:last-child) {
            margin-bottom: 4px;
          }
          .itemCatDetail__itemLabel {
            margin-right: 2px;
          }
          .itemCatDetail__itemContent {
            flex: 1;
            margin-left: 6px;
          }
        }
      }
      .itemCatDetail__button {
        margin-left: 20px;
        align-self: center;
        .button-disabled {
          opacity: 0.3;
        }
        /deep/ .we-button {
          border-radius: 4px;
        }
        /deep/.we-button--small {
          height: 36px;
          padding: 0 12px;
        }
      }
    }
  }
  .noSubInfo {
    .itemCatDetail__item {
      align-items: center;
      height: 60px;
      padding: 0;
    }
  }
}
</style>
