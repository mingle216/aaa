<template>
  <div class="search__bar" :class="[showBreadCrumb ? 'flex-start' : '']">
    <div class="section">
      <div class="title text__ellipsis">
        <span :title="title">{{ title }}</span>
      </div>
      <div class="pos-relative" v-if="showSearch">
        <div class="search__btn__wrap">
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
            style="width: 530px"
          />
          <div
            class="search__btn portal-primary-backgroundcolor-lv1 portal-primary-backgroundcolor-hover-lv2"
            @click="handleSearch"
          >
            {{ $Lan('CUS_TEMPLATE_OFFICIAL', 'search', '搜索') }}
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
              ></associative-dropdown>
            </div>
          </vue-scroll>
        </div>
        <!-- 历史搜索 -->
        <history-list
          :historyData="hisDataLists"
          v-if="showHistory"
          @change-search="handleChangeSearch"
        />
      </div>
    </div>
  </div>
</template>

<script>
import HistoryList from "./HistoryList";
import HistoryDropdown from "./HistoryDropdown";
import AssociativeDropdown from "./AssociativeDropdown";
export default {
  components: {
    HistoryList,
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
    currentUser: Object,
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
      searchPrompt: "", // 搜索提示语，优先级 > 搜索词
      loading: false,
    };
  },
  mounted() {
    // 搜索结果页，默认填充搜索关键词
    this.pageCode === "search" && this.getUrlParams();  

  },
  computed: {
    pageConfig() {
      return this.pageRenderData.pageConfig || {};
    },
    // 多语言标题
    title() {
      const arr = this.pageConfig["header.titleLangs"] || [];
      const temp = arr.find((el) => el.key === this.$i18n.locale);
      const zhName = arr.find((el) => el.key === 'zh_CN')
      return temp?.value || zhName?.value;
      // return this.pageConfig[`header.title.${this.$i18n.locale}`];
    },
    // 是否显示搜索框
    showSearch() {
      return this.pageCode === "search"
        ? true
        : this.pageConfig["search.display"] === "Y" ||
            this.pageConfig["search.display"] === "";
    },
    // 是否展示面包屑
    showBreadCrumb() {
      return (
        this.pageConfig["breadcrumb.show"] === "Y" &&
        this.pageRenderData.breadcrumb &&
        this.pageRenderData.breadcrumb.length > 1
      );
    },
    placeholder() {
      // 搜索结果页使用其他页面带过来的默认词
      return this.pageCode === "search"
        ? this.placeholderVal
        : this.searchPrompt || this.placeholderValFetch;
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
      if (width > 490) {
        while (width > 490) {
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
    // 输入框下的历史搜索数据，最多展示五条
    hisDataLists() {
      return this.hisDataDropdown.length > 5
        ? this.hisDataDropdown.slice(0, 5)
        : this.hisDataDropdown;
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
          val &&
          (val["search.display"] === "Y" || val["search.display"] === "")
        ) {
          this.getPlaceholderVal();
        }
        this.getSearchHis();
      },
      immediate: true,
    },
  },
  methods: {
    getUrlParams() {
      const params = window.shell.getUrlParam();
      console(params,'params');
      if (params && params.searchKey) {
        this.keyword = params.searchKey;
        this.searchKeyword = params.searchKey;
      }
      if (params && params.placeholder) {
        this.placeholderVal = params.placeholder;
      }
      if (params && params.searchPrompt) {
        this.searchPrompt = params.searchPrompt;
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
        const arr = res.data["search.searchPromptLangs"] || [];
        this.searchPrompt = arr.find((el) => el.key === this.$i18n.locale)?.value || "";
        this.placeholderValFetch = res.data["search.placeholderVal"] || "";
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
      if (!this.keyword && this.searchPrompt) {
        window.shell.showMessage({
          type: 'warning',
          message: this.$Lan("public", "placeholder", "请输入内容"),
        })
        return
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
              keyword: keyword
            }
          }
        },
        startTime: new Date().getTime()
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
            window.shell.emit("update-search-results", keyword);
          }
          const reg = new RegExp("[%\\/?#&=]", "g");
          keyword = keyword.replace(reg, (match) => encodeURIComponent(match));
          const placeholder = this.placeholder.replace(reg, (match) =>
            encodeURIComponent(match)
          );
          const searchPrompt = this.searchPrompt.replace(reg, (match) =>
            encodeURIComponent(match)
          );
          window.shell.openPage(
            `search?searchKey=${keyword}&placeholder=${placeholder}&searchPrompt=${searchPrompt}`,
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
          const data = res.data || []
          this.hisDataDropdown = data.length > 10 ? data.slice(0, 10) : data;
        } else {
          this.hisDataDropdown = [];
        }
      }
    },
  },
};
</script>

<style scoped>
.search__bar {
  position: relative;
}

.section {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  z-index: 999;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.flex-start .section {
  transform: translate(-50%, -60%);
}

.section .title {
  font-size: 36px;
  color: #fff;
  margin-bottom: 20px;
  text-align: center;
  max-width: 750px;
}

.search__btn__wrap /deep/.we-input__inner {
  height: 48px;
  line-height: 48px;
}

.search__btn__wrap {
  display: flex;
  align-items: center;
  justify-content: center;
}

.search__btn {
  min-width: 100px;
  padding: 0 20px;
  height: 48px;
  font-size: 16px;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: -2px;
  z-index: 1;
  border-bottom-right-radius: 4px;
  border-top-right-radius: 4px;
  cursor: pointer;
  user-select: none;
}

.pos-relative {
  position: relative;
  width: 630px;
}

.search__dropdown {
  position: absolute;
  box-sizing: border-box;
  top: 55px;
  padding: 14px 0;
  background: #ffffff;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  border-radius: 4px;
  transform: rotateX(90deg);
  transform-origin: 0 0;
  transition: transform 0.3s;
  transition-delay: 0.5s;
  width: 100%;
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
</style>
