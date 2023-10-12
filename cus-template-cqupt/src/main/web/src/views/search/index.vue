<template>
  <div class="container">
    <div class="search" :style="headerStyle">
      <!-- header -->
      <header-bar :pageData="pageData" />
      <search-bar
        pageCode="search"
        :allShowSearch="true"
        :pageRenderData="pageRenderData"
      />
      <!-- body -->
      <div class="content__wrapper">
        <we-container class="searchWrap">
          <we-main style="overflow: hidden;padding: 0;width:0">
            <card-layout :cardLayout="cardLayout"></card-layout>
          </we-main>
        </we-container>
      </div>
      <we-footer
        class="search__footer"
        style="height: auto"
        v-html="footer"
      ></we-footer>
    </div>
  </div>
</template>

<script>
/* eslint-disable no-debugger */
import HeaderBar from "../../components/headerBar/index";
import SearchBar from "../../components/searchBar/index";
import CardLayout from "../cardLayout";
import { DEV_URL } from "../../../env";
export default {
  name: "Home",
  components: {
    HeaderBar,
    SearchBar,
    CardLayout,
  },
  props: {
    pageData: Object,
  },
  computed: {
    pageRenderData() {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData && this.pageData.pageInfoEntity.wid,
      };
    },
    cardLayout() {
      return JSON.parse(
        (this.pageData &&
          this.pageData.pageInfoEntity &&
          this.pageData.pageInfoEntity.cardLayout) ||
          "[]"
      );
    },
    footer() {
      const footerConfig = this.pageData
        ? JSON.parse(
            JSON.parse(this.pageData.showProgrammeEntity.templateConfig || "")
          ).footerConfig
        : "";
      if (Object.prototype.toString.call(footerConfig) === "[object Array]") {
        const temp = (footerConfig || []).find(
          (el) => el.langKey === this.$i18n.locale
        );
        const zh = (footerConfig || []).find((el) => el.langKey === "zh_CN");
        return temp?.langValue || zh?.langValue;
      } else {
        return footerConfig;
      }
    },
    headerStyle() {
      const imgUrl =
        (this.pageData.renderData.pageConfig &&
          this.pageData.renderData.pageConfig["header.imgUrl"]) ||
        "";
      const isHttp = /^http(s)?:\/\//.test(imgUrl);
      const relUrl = isHttp
        ? imgUrl
        : process.env.NODE_ENV === "development"
        ? `${DEV_URL}${imgUrl}`
        : imgUrl;
      return {
        background: `url(${relUrl}) no-repeat center top/auto 262px`,
      };
    },
  },
};
</script>

<style lang="less" scoped>
.container {
  display: flex;
  background: #eff3fa;
  .search {
    min-height: 100vh;
    width: 100%;
    flex-direction: column;
    display: flex;
    justify-content: space-between;
    .main__center {
      width: 100% !important;
    }
    .content__wrapper {
      margin: 24px 24px 0;
      padding: 24px;
      border-radius: 16px;
      background: #ffffff;
      flex-grow: 1;
    }
    .search__footer {
      padding: 0;
    }
  }
}
</style>
