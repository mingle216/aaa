<template>
  <header class="home__header" :style="headerStyle">
    <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" />
    <!-- <header-menu :menus="menus" :activeMenuId="activeMenuId" /> -->
      <img class="xiaoxun" src="../../assets/images/xiaoxun.png" alt="">
     <search-bar
        pageCode="home"
        :allShowSearch="true"
        :pageRenderData="pageRenderData"
      />
    <login :pageData="pageData" :currentUser="currentUser" />
  </header>
</template>

<script>
// import HeaderMenu from "./Menu";
import LogoTitle from "./LogoTitle";
import Login from "./Login";
import SearchBar from "../../components/searchBar/index";
export default {
  components: {
    // HeaderMenu,
    LogoTitle,
    Login,
    SearchBar,
  },
  props: ["pageData"],
  data() {
    return {
      currentUser: window.shell.getUserInfo(),
      bgUrl:require("../../assets/images/banner.png"),
    };
  },
  computed: {
    headerStyle() {
      const relUrl = this.bgUrl
      return {
        'background': `url(${relUrl}) no-repeat center top/auto 56px`,
      };
    },
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
        wid: this.pageData && this.pageData.pageInfoEntity.wid,
      };
    },
  },
  created() {
    window.shell.on("update-login", (data) => {
      this.currentUser = data;
    });
  },
  beforeDestroy() {
    window.shell.off("update-login");
  },
};
</script>

<style scoped>
.home__header {
  width: 100%;
  height: 44px;
  position: relative;
  display: flex;
  align-items: center;
  padding: 0 16px;
  box-sizing: border-box;
  box-shadow: inset 0 -1px 0 0 hsla(0, 0%, 100%, 0.1);
}
    .xiaoxun{
        height: 22px;
        margin-left: 30px;
    }
</style>
