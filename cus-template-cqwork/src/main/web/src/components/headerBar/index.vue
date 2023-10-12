<template>
  <header class="home__header">
    <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" />
    <header-menu :menus="menus" :activeMenuId="activeMenuId" />
    <login :pageData="pageData" :currentUser="currentUser" />
  </header>
</template>

<script>
import HeaderMenu from "./Menu";
import LogoTitle from "./LogoTitle";
import Login from "./Login";
export default {
  components: {
    HeaderMenu,
    LogoTitle,
    Login,
  },
  props: ["pageData"],
  data() {
    return {
      currentUser: window.shell.getUserInfo(),
    };
  },
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
  height: 64px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-sizing: border-box;
  box-shadow: inset 0 -1px 0 0 hsla(0, 0%, 100%, 0.1);
}
</style>
