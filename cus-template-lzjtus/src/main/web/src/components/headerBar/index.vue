<template>
  <header class="header">
    <div class="home__header">
      <div class="home__header_inner">
        <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" />
        <header-menu :menus="menus" :activeMenuId="activeMenuId" />
        <login
                :pageRenderData="pageRenderData"
                :pageData="pageData"
                :currentUser="currentUser"
        />
      </div>
    </div>
    <div class="header__tool__wrap">
      <bread-crumb :pageRenderData="pageRenderData"></bread-crumb>
      <search-bar
        class="searchBar__wrap"
        :pageRenderData="pageRenderData"
        :pageCode="pageCode"
        :currentUser="currentUser"
      />
    </div>
  </header>
</template>

<script>

import HeaderMenu from "./Menu";
import LogoTitle from "./LogoTitle";
import Login from "./Login";
import SearchBar from "../searchBar";
import BreadCrumb from "../BreadCrumb";
import configMixin from "../../mixins/configMixin";
export default {
  components: {
    HeaderMenu,
    LogoTitle,
    Login,
    SearchBar,
    BreadCrumb,
  },
  props: ["pageData", "pageCode"],
  mixins: [configMixin],

  computed: {
    menus() {
      return (this.pageData && this.pageData.treeMenu) || [];
    },
    activeMenuId() {
      return (this.pageData && this.pageData.activeMenuId) || "";
    },
    navbarTitle() {
      return this.pageData
        ? JSON.parse(
            JSON.parse(this.pageData.showProgrammeEntity.templateConfig || "")
          ).navigationBarConfig
        : [];
    },
    logoImgSrc() {
      return this.pageData
        ? JSON.parse(
            JSON.parse(this.pageData.showProgrammeEntity.templateConfig || "")
          ).configLogo
        : {};
    },
    pageRenderData() {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData?.pageInfoEntity?.wid,
      };
    },
    // currentUser() {
    //   return window.shell.getUserInfo();
    // },
  },
};
</script>

<style scoped>
.header {
  width: 100%;
  /* height: 300px; */
}
.home__header {
  border-bottom: 1px hsla(0, 0%, 100%, 0.2) solid;

}
.home__header_inner{
  min-width: 1200px;
  width: 90%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  box-sizing: border-box;
  margin: 0 auto;
}
.header__tool__wrap {
  min-width: 1200px;
  width: 90%;
  margin: 0 auto;
  min-height: 40px;
}
</style>
