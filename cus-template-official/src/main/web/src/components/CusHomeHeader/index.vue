<template>
  <div :class="{ sticky: pageCode != 'detail' && pageCode != 'search' }">
    <div class="dzhome-header" :style="{ padding: calPadding }">
      <div class="header-left">
        <span v-html="title"></span>
      </div>
      <div class="nav" v-if="headerNavBar.length">
        <div class="navItem" v-for="(item, index) in headerNavBar" :key="index">
          <we-tooltip
            class="item"
            effect="dark"
            :content="getZhTitle(item)"
            placement="bottom"
            :open-delay="500"
          >
            <div class="c-box">
              <span
                class="navItem__name"
                @click="openPage(item, 1, 2)"
                style="cursor: pointer"
                v-html="getZhTitle(item)"
              >
              </span>
            </div>
          </we-tooltip>
        </div>
      </div>
    </div>
    <div class="header-wrap" :style="{ padding: calPadding }">
      <div class="left">
        <div class="logo" v-if="logoUrl">
          <img :src="logoUrl" alt="" height="50" />
        </div>
        <div class="search__bar">
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
              />
              <i class="we-icon-search search-btn" @click="handleSearch"></i>
            </div>
            <!-- 搜索结果/历史搜索下拉面板 -->
            <div
              class="search__dropdown portal-font-color-lv1"
             
              :class="{ 'search__dropdown-show': dropdownShow }"
            >
            <!-- v-show="dropdownShow" -->
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
                    :showAssociation="showAssociation"
                  ></associative-dropdown>
                </div>
              </vue-scroll>
            </div>
          </div>
        </div>
      </div>
      <div class="right">
        <header-menu :menus="menus" :activeMenuId="activeMenuId" />
        <login
          :pageData="pageData"
          :pageRenderData="pageRenderData"
          :currentUser="currentUser"
        />
      </div>
    </div>
  </div>
</template>
<script>
import HistoryDropdown from "../searchBar/HistoryDropdown";
import AssociativeDropdown from "../searchBar/AssociativeDropdown";
import HeaderMenu from "./Menu";
import Login from "../headerBar/Login";
import hallLogo from "../../assets/images/halllogo.png";
export default {
  name: "dzHomeHeader",
  props: ["pageData", "pageCode"],
  components: {
    HistoryDropdown,
    AssociativeDropdown,
    HeaderMenu,
    Login,
  },
  data() {
    return {
      currentUser: window.shell.getUserInfo(),
      keyword: "",
      searchKeyword: "", // 联想搜索时的关键词
      hisDataDropdown: [],
      dropdownShow: false,
      dropdownType: "HistoryDropdown",
      timer: null,
      placeholderVal: "", // 搜索默认词
      placeholderValFetch: "", // 搜索默认词, 从接口获取
      loading: false,
      pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
      hallLogo,
    };
  },
  mounted() {
    // 搜索结果页，默认填充搜索关键词
    this.pageCode === "search" && this.getUrlParams();  

  },
  computed: {
    menus() {
      return (this.pageData && this.pageData.treeMenu) || [];
    },
    activeMenuId() {
      return (this.pageData && this.pageData.activeMenuId) || "";
    },
    calPadding() {
      return this.isFirefox ? `0 ${this.pd}px 0` : `0 calc(50% - 37.5rem) 0`;
    },
    navbarTitle() {
      return this.pageData
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ""))
            .navigationBarConfig
        : [];
    },
    title() {
      const temp = (this.navbarTitle || []).find(
        (el) => el.langKey === this.$i18n.locale
      );
      const zhName = (this.navbarTitle || []).find((el) => el.langKey === "zh_CN");
      return temp?.langValue || zhName?.langValue;
    },
    // 模板全局配置
    templateConfig() {
      return this.pageData
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ""))
        : {};
    },
    headerNavBar() {
      return (
        (this.templateConfig &&
          this.templateConfig.headerNavBar &&
          this.templateConfig.headerNavBar.datas) ||
        []
      );
    },
    logoImgSrc() {
      return this.pageData
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ""))
            .configLogo
        : {};
    },
    logoUrl() {
      const sColor = (this.logoImgSrc && this.logoImgSrc.logoUrl) || "";
      // const bColor = (this.logoImgSrc && this.logoImgSrc.whiteLogoUrl) || "";
      let url = sColor;
      url = process.env.NODE_ENV === "development" ? url.split(".cn")[1] : url;
      // url = this.pageData?.pageInfoEntity?.pageCode=='hall' ? this.hallLogo : url;
      return url;
    },
    pageRenderData() {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData?.pageInfoEntity?.wid,
      };
    },
    pageConfig() {
      return this.pageRenderData.pageConfig || {};
    },
    // 是否显示搜索框
    showSearch() {
      return this.pageCode && this.pageCode === "search"
        ? true
        : this.pageConfig["search.display"] === "Y" ||
            this.pageConfig["search.display"] === "";
    },
    placeholder() {
      // 搜索结果页使用其他页面带过来的默认词
      return this.pageCode && this.pageCode === "search"
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
    logoTitle() {
      return this.pageData?.pageInfoEntity?.pageCode == "hall"
        ? `| 网上办事大厅`
        : "智慧校园门户";
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
        if (val && (val["search.display"] === "Y" || val["search.display"] === "")) {
          this.getPlaceholderVal();
        }
        this.getSearchHis();
      },
      immediate: true,
    },
  },
  methods: {
     // 联想搜索下拉是否支持收藏
     showAssociation() {
      return (
        this.pageConfig["search.Association"] === "Y" && this.currentUser
      );
    },
    /**
     * @description 获取中文名称
     * @param {Array|String} title 导航名称
     */
    getZhTitle(item) {
      const title = item.title;
      if (Array.isArray(title)) {
        const zhName = (title || []).find((el) => el.langKey === "zh_CN") || {};
        const zhTitle = zhName.langValue;
        const temp = title.find((el) => el.langKey === this.$i18n.locale);
        return (temp && temp.langValue) || zhTitle;
      } else {
        return title;
      }
    },
    openPage(item, openType, menuType) {
      window.shell.openPage(item.url, openType, menuType);
    },
    getUrlParams() {
      const params = window.shell.getUrlParam();
      console.log(params,'paramsparamsparamsparams89');
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
      this.dropdownType = this.searchKeyword ? "AssociativeDropdown" : "HistoryDropdown";
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
            window.shell.emit("update-search-results", keyword);
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
  },
};
</script>
<style scoped lang="less">
.sticky {
  position: sticky;
  top: 0;
  z-index: 99;
}
.dzhome-header {
  height: 34px;
  background: #2b2b2b;
  color: #ffffff;
  font-size: 12px;
  line-height: 14px;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .nav {
    display: flex;
    align-items: center;
    .divide {
      height: 16px;
      width: 1px;
      background: #fff;
      display: inline-block;
      margin: 0 8px;
      opacity: 0.5;
    }
    .navItem__name {
      color: rgba(255, 255, 255, 1);
    }
  }
  .navItem {
    display: flex;
    align-items: center;
    border-right: 1px solid #fff;
    &:last-of-type {
      border: none;
      .c-box {
        padding-right: 0;
      }
    }
  }
  .c-box {
    padding: 0 12px;
  }
  .c-box:hover {
    .navItem__name {
      color: rgba(255, 255, 255, 0.6);
    }
  }
}

.header-wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #ffffff;
  box-shadow: 0px 2px 12px rgba(36, 71, 131, 0.09);
  height: 80px;

  .left {
    display: flex;
    align-items: center;
    flex: 1;
    margin-right: 40px;
  }

  .logo {
    display: flex;
    align-items: center;

    img {
      height: 45px;
    }

    span {
      margin-left: 6px;
      font-size: 16px;
    }
  }

  .search__bar {
    position: relative;
    margin-left: 22px;
    width: 300px;
  }

  .search-btn{
    position: absolute;
    right: 8px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 18px;
    cursor: pointer;
    color: #8C8C8C;
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
    height: 40px;
    line-height: 40px;
    background: #f4f6f9;
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
    z-index: 99;
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

  .right {
    display: flex;
    align-items: center;
    /deep/ .we-dropdown-link {
      color: #212a39;
    }

    /deep/ .login__wrap {
      margin-left: 22px;
    }
  }
}
</style>
