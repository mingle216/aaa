<template>
  <div class="search__bar" v-if="title || showSearch">
    <div class="section">
      <div class="title text__ellipsis" v-if="title" :title="title">
        {{ title }}
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
            style="width: 466px"
          />
          <div
            class="search__btn portal-primary-backgroundcolor-lv1 portal-primary-backgroundcolor-hover-lv2"
            @click="handleSearch"
          >
            {{ $Lan($TEMPLATE_NAME, "searchTxt", "搜索") }}
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
          v-if="showHistory && !isSecond"
          @change-search="handleChangeSearch"
        />
      </div>
      <div v-if="isSecond" class="search-back" @click="goBack"><i class="we-icon-arrow-left"></i>返回首页</div>
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
    isSecond: {
      type: Boolean,
      default: false,
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
      const arr =
        (this.pageConfig && this.pageConfig["header.titleLangs"]) || [];
      const zhName = arr.find((el) => el.key === "zh_CN");
      const temp = arr.find((el) => el.key === this.$i18n.locale);
      return temp?.value || zhName?.value;
    },
    // 是否显示搜索框
    showSearch() {
      return this.pageCode === "search"
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
      if (width > 340) {
        while (width > 340) {
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
    goBack() {
      window.history.back()
    },
    getUrlParams() {
      const params = window.shell.getUrlParam();
      if (params && params.searchKey) {
        this.keyword = params.searchKey;
        this.searchKeyword = params.searchKey;
      }
      if (params && params.placeholder) {
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
      // 未输入关键词或者占位符不存在时，不触发搜索
      let keyword = this.searchKeyword || this.placeholder || "";
      keyword = keyword.trim();
      this.handleClickTrack(keyword)// 点击埋点
      if (this.loading || !keyword) {
        return;
      }
      if (!this.keyword.trim() && this.placeholder) {
        // 搜索结果页，直接以placeholder搜索时，填充到输入框中
        this.keyword = this.placeholder;
      }
      this.loading = true;
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
            window.shell.emit("update-search-results");
          }
          const reg = new RegExp("[%\\/?#&=]", "g");
          keyword = keyword.replace(reg, (match) => encodeURIComponent(match));
          const placeholder = this.placeholder.replace(reg, (match) =>
            encodeURIComponent(match)
          );
          window.shell.openPage(
            `search?searchKey=${keyword}&placeholder=${placeholder}`,
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
    // 点击埋点
    handleClickTrack(keyword) {
      const pageInfoEntity = window.shell.getPageData().pageInfoEntity;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        startTime: new Date().getTime(),
        actionParams: {
          pageCode: pageInfoEntity.pageCode,
          pageName: pageInfoEntity.pageName,
          extraInfo: {
            infoType: 3,
            filterInfo: {
              keyword
            }
          }
        },
      });
    }
  },
};
</script>

<style lang="less" scoped>
  .search-back{
    color: #fff;
    cursor: pointer;
    font-size: 16px;
    margin-top: 15px;
    width: 100px;
    height: 36px;
    border: 1px solid #fff;
    border-radius: 5px;
    font-weight: 600;
    text-align: center;
    line-height: 36px;
  }
.search__bar {
  position: relative;
  margin: 40px 0;
}

.section .title {
  width: 100%;
  font-size: 36px;
  letter-spacing: 0.9px;
  line-height: 44px;
  font-weight: bold;
  color: #fff;
  margin-bottom: 30px;
}

.search__btn__wrap /deep/.we-input__inner {
  height: 52px;
  line-height: 52px;
  font-size: 18px;
  // &::placeholder {
  //   font-size: 18px;
  // }
}

.search__btn__wrap {
  display: flex;
  align-items: center;
  justify-content: center;
}

.search__btn {
  width: 120px;
  height: 52px;
  font-size: 18px;
  font-weight: bold;
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
  width: 586px;
}

.search__dropdown {
  position: absolute;
  box-sizing: border-box;
  top: 60px;
  padding: 12px 0;
  background: #ffffff;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  border-radius: 4px;
  transform: rotateX(90deg);
  transform-origin: 0 0;
  transition: transform 0.3s;
  transition-delay: 0.5s;
  width: 100%;
  text-align: left;
  z-index: 9999;
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
