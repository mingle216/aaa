<template>
  <div class="search__bar" v-if="showSearch">
    <!-- 搜索tab -->
    <div class="search__tab">
      <div
        class="search__tab__item ellipsis"
        v-for="item in tabLists"
        :key="item.groupWid"
        :class="[selectedTab.groupWid === item.groupWid ? 'selected' : '']"
        @click="handleChangeTab(item)"
      >
        {{ item.groupName }}
      </div>
    </div>
    <div class="pos-relative">
      <div
        class="search__btn__wrap"
        :class="[selectedTab.groupWid === 'all' ? 'no-radiusTop' : '']"
      >
        <we-input
          ref="InputKeyword"
          size="large"
          v-model="keyword"
          :placeholder="placeholderEllipsis"
          :maxlength="100"
          @focus="handleFocus"
          @blur="handleBlur"
          @input="handleInput"
          @keyup.native="handleKeyUp"
        />
        <div
          class="search__btn portal-primary-color-lv1 ellipsis"
          @click="handleSearch"
        >
          {{ $Lan("CUS_TEMPLATE_CQWORK", "search", "搜索") }}
        </div>
      </div>
      <!-- 搜索结果/历史搜索下拉面板 -->
      <div
        class="search__dropdown portal-font-color-lv1"
        v-show="dropdownShow"
        :class="{ 'search__dropdown-show': dropdownShow }"
      >
        <vue-scroll>
          <div class="search__dropdown__lists">
            <history-dropdown
              v-if="dropdownType === 'HistoryDropdown'"
              :historyData="hisDataDropdown"
              @change-search="handleChangeSearch"
              @clear-his="handleClearHis"
            ></history-dropdown>
            <associative-dropdown
              v-else
              ref="AssociateDown"
              :keyword="searchKeyword"
              :searchTab="selectedTab"
            ></associative-dropdown>
          </div>
        </vue-scroll>
      </div>
    </div>
  </div>
</template>

<script>
import HistoryDropdown from "./HistoryDropdown";
import AssociativeDropdown from "./AssociativeDropdown";
export default {
  components: {
    HistoryDropdown,
    AssociativeDropdown,
  },
  name: "searchBar",
  props: {
    pageCode: {
      type: String,
      default: "home",
    },
    pageRenderData: {
      type: Object,
      default: () => {
        return {};
      },
    },
    allShowSearch: {
      type: Boolean,
    },
  },
  data() {
    return {
      keyword: "",
      searchKeyword: "", // 联想搜索时的关键词
      hisDataDropdown: [],
      dropdownShow: false,
      dropdownType: "HistoryDropdown",
      timer: null,
      placeholderVal: "", // 搜索默认词
      placeholderValFetch: "", // 搜索默认词, 从接口获取
      loading: false,
      tabLists: [
        {
          groupWid: "all",
          groupName: this.$Lan("CUS_TEMPLATE_CQWORK", "all", "全部"),
        },
      ],
      selectedTab: { groupWid: "all", groupType: "" },
    };
  },
  mounted() {
    this.getSearchTabs();
    // 搜索结果页，默认填充搜索关键词
    this.pageCode === "search" && this.getUrlParams();
  },
  computed: {
    currentUser() {
      return window.shell.getUserInfo();
    },
    pageConfig() {
      return this.pageRenderData.pageConfig || {};
    },
    // 是否显示搜索框
    showSearch() {
      return this.allShowSearch
        ? true
        : this.pageConfig["search.display"] === "Y" ||
            this.pageConfig["search.display"] === "";
    },
    placeholder() {
      // 搜索结果页使用其他页面带过来的默认词
      return this.pageCode === "search"
        ? this.placeholderVal
        : this.placeholderValFetch;
    },
    // 折叠展示的placeholder
    placeholderEllipsis() {
      const val = this.placeholder || "";
      const dom = document.createElement("span");
      dom.style.visibility = "hidden";
      dom.style.display = "inline-block";
      dom.textContent = val;
      document.body.appendChild(dom);
      let width = dom.clientWidth;
      let offset = val.length;
      let realText = val;
      if (width > 550) {
        while (width > 550) {
          realText = `${val.slice(0, offset)}...`;
          dom.textContent = realText;
          width = dom.clientWidth;
          offset = offset - 1;
        }
      }
      document.body.removeChild(dom);
      return realText;
    },
    // 未登录或者无历史数据时，不展示历史记录
    showHistory() {
      return (
        this.showSearch &&
        (this.pageConfig["search.history.display"] === "Y" ||
          this.pageConfig["search.history.display"] === "") &&
        this.currentUser &&
        this.hisDataDropdown.length
      );
    },
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 6000);
      }
    },
    pageConfig: {
      handler(val) {
        if (
          this.allShowSearch ||
          (val &&
            (val["search.display"] === "Y" || val["search.display"] === ""))
        ) {
          this.getPlaceholderVal();
        }
        this.getSearchHis();
      },
      immediate: true,
    },
  },
  methods: {
    handleChangeTab(item) {
      this.selectedTab = {
        groupWid: item.groupWid,
        groupType: item.groupType || "",
      };
    },
    getUrlParams() {
      const params = window.shell.getUrlParam();
      if (!params) {
        return;
      }
      this.selectedTab = {
        groupWid: params.groupWid || "all",
        groupType: params.groupType || "",
      };
      if (params.searchKey) {
        this.keyword = params.searchKey;
        this.searchKeyword = params.searchKey;
      }
      if (params.placeholder) {
        this.placeholderVal = params.placeholder;
      }
    },
    async getPlaceholderVal() {
      if (this.pageCode === "search") {
        return;
      }
      const [res] = await window.shell.getPlaceholderVal({
        wid: this.pageRenderData.wid,
      });
      if (res.errcode === "0" && res.data) {
        this.placeholderValFetch = res.data["search.placeholderVal"];
      } else {
        this.placeholderValFetch = "";
      }
    },
    handleFocus() {
      this.searchKeyword = this.keyword.trim();
      // 有关键词时，展示联想搜索；否则展示历史搜索
      this.dropdownType = this.searchKeyword
        ? "AssociativeDropdown"
        : "HistoryDropdown";
      this.dropdownShow =
        this.dropdownType === "HistoryDropdown" ? this.showHistory : true;
      if (this.searchKeyword) {
        this.$refs.AssociateDown && this.$refs.AssociateDown.handleSearch();
      }
    },
    handleBlur() {
      setTimeout(() => {
        this.dropdownShow = false;
      }, 300);
    },
    handleInput() {
      if (this.timer !== null) clearTimeout(this.timer);
      this.timer = setTimeout(() => {
        this.searchKeyword = this.keyword.trim();
        if (this.searchKeyword) {
          this.dropdownType = "AssociativeDropdown";
        } else {
          this.dropdownType = "HistoryDropdown";
        }
        this.dropdownShow =
          this.dropdownType === "HistoryDropdown" ? this.showHistory : true;
      }, 300);
    },
    handleKeyUp(e) {
      if (e.key === "Enter") {
        //按下回车
        this.searchKeyword = this.keyword.trim();
        this.handleSearch();
        // 失去焦点
        this.$refs.InputKeyword.blur();
      }
    },
    handleSearch() {
      if (this.loading) {
        return;
      }
      // 未输入关键词或者占位符不存在时，不触发搜索
      let keyword = this.searchKeyword || this.placeholder || "";
      keyword = keyword.trim();
      if (!keyword) {
        return;
      }
      if (!this.keyword.trim() && this.placeholder) {
        // 搜索结果页，直接以placeholder搜索时，填充到输入框中
        this.keyword = this.placeholder;
      }
      this.loading = true;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 3,
            filterInfo: {
              keyword: keyword,
            },
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.execTemplateMethod(
        "globalSearch",
        {
          keywords: keyword,
        },
        () => {
          setTimeout(() => {
            this.loading = false;
          }, 1000);
          const curr = window.shell.getRoute();
          if (curr === "/search") {
            window.shell.emit(
              "update-search-results",
              keyword,
              this.selectedTab
            );
          }
          const reg = new RegExp("[%\\/?#&=]", "g");
          keyword = keyword.replace(reg, (match) => encodeURIComponent(match));
          const placeholder = this.placeholder.replace(reg, (match) =>
            encodeURIComponent(match)
          );
          window.shell.openPage(
            `search?searchKey=${keyword}&groupWid=${this.selectedTab.groupWid}&groupType=${this.selectedTab.groupType}&placeholder=${placeholder}`,
            0,
            1
          );
          this.getSearchHis();
        }
      );
    },
    handleChangeSearch(item) {
      this.keyword = item;
      this.searchKeyword = this.keyword.trim();
      this.handleSearch();
    },
    handleClearHis() {
      this.hisDataDropdown = [];
    },
    async getSearchHis() {
      if (
        this.showSearch &&
        (this.pageConfig["search.history.display"] === "Y" ||
          this.pageConfig["search.history.display"] === "") &&
        this.currentUser
      ) {
        const [res] = await window.shell.getSearchHisVal({
          wid: this.pageRenderData.wid,
        });
        if (res.errcode === "0" && res.data) {
          const data = res.data || [];
          this.hisDataDropdown = data.length > 10 ? data.slice(0, 10) : data;
        } else {
          this.hisDataDropdown = [];
        }
      }
    },
    getSearchTabs() {
      // const nameLan = {
      //   2: {
      //     key: "onlineService",
      //     value: "在线服务",
      //   },
      //   3: {
      //     key: "serviceItem",
      //     value: "服务事项",
      //   },
      //   5: {
      //     key: "oneThing",
      //     value: "一件事",
      //     funcName: "public",
      //   },
      //   4: {
      //     key: "newsItem",
      //     value: "资讯",
      //   },
      // };
      window.shell.execTemplateMethod(
        "enabledGroupSearch",
        {
          lang: this.$i18n.locale || "zh_CN",
        },
        (res) => {
          if (res.data) {
            const resp = res.data || [];
            const data = resp.length > 6 ? resp.slice(0, 6) : resp;
            // data.forEach((element) => {
            //   if (element.groupType != "1") {
            //     const item = nameLan[element.groupType];
            //     element.groupName = item.groupName
            //       ? this.$Lan(
            //           item.funcName || "CUS_TEMPLATE_CQWORK",
            //           item.key,
            //           item.value
            //         )
            //       : element.groupName;
            //   }
            // });
            this.tabLists = this.tabLists.concat(data);
          }
        }
      );
    },
  },
};
</script>

<style scoped>
.search__bar {
  padding-top: 24px;
  width: 704px;
  margin: 0 auto;
}

.search__tab {
  height: 36px;
  line-height: 36px;
  color: #ffffff;
}

.search__tab__item {
  max-width: 88px;
  padding: 0 15px;
  font-size: 14px;
  display: inline-block;
  cursor: pointer;
  text-shadow: 0px 2px 10px #5b79a3ce;
}

.search__tab__item.selected {
  font-weight: bold;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 6px 6px 0px 0px;
}

.pos-relative {
  position: relative;
}

.search__btn__wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 6px;
}

.no-radiusTop {
  border-top-left-radius: 0;
}

.search__btn__wrap /deep/.we-input__inner {
  height: 52px;
  line-height: 52px;
  background-color: transparent;
  border: none;
  border-radius: 6px 0 0 6px;
  color: #ffffff;
  font-size: 16px;
}

.search__btn {
  width: 100px;
  padding: 0 20px;
  height: 52px;
  line-height: 52px;
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  background: #ffffff;
  border-radius: 6px;
  z-index: 1;
  cursor: pointer;
  user-select: none;
  flex-shrink: 0;
}

.search__dropdown {
  position: absolute;
  box-sizing: border-box;
  top: 60px;
  padding: 14px 0;
  background: #ffffff;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  border-radius: 4px;
  transform: rotateX(90deg);
  transform-origin: 0 0;
  transition: transform 0.3s;
  transition-delay: 0.5s;
  width: 604px;
  z-index: 2001;
  text-align: left;
}

.search__dropdown__lists {
  max-height: 450px;
}

.search__dropdown-show {
  transform: rotateX(0deg);
}

/deep/.we-input__inner:focus {
  border-color: transparent;
}
/deep/.we-input__inner::-webkit-input-placeholder {
  color: rgba(255, 255, 255, 0.3);
}
/deep/.we-input__inner::-moz-placeholder {
  color: rgba(255, 255, 255, 0.3);
}
/deep/.we-input__inner:-ms-input-placeholder {
  color: rgba(255, 255, 255, 0.3);
}
</style>
