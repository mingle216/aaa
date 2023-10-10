<template>
  <header class="header" :style="headerStyle">
    <div class="home__header">
      <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" />
      <header-menu :menus="menus" :activeMenuId="activeMenuId" />
      <login :pageRenderData="pageRenderData" :currentUser="currentUser" />
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
  import chun from '../../assets/images/chun.png'
  import xia from '../../assets/images/xia.png'
  import qiu from '../../assets/images/qiu.png'
  import don from '../../assets/images/don.png'
import HeaderMenu from "./Menu";
import LogoTitle from "./LogoTitle";
import Login from "./Login";
import SearchBar from "../searchBar";
import BreadCrumb from "../BreadCrumb";
export default {
  components: {
    HeaderMenu,
    LogoTitle,
    Login,
    SearchBar,
    BreadCrumb
  },
  props: ["pageData", "pageCode"],
  data() {
    return {
      currentUser: window.shell.getUserInfo()
    }
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
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ''))
            .navigationBarConfig
        : [];
    },
    logoImgSrc() {
      return this.pageData
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ''))
            .configLogo
        : {};
    },
    pageRenderData() {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData?.pageInfoEntity?.wid
      };
    },
    headerStyle() {
      // TODO img前缀
      let times = new Date()
      let month = times.getMonth() + 1
      let relUrl = ''
      if(month >2 && month < 6) {
        relUrl = chun
      } else if(month >5 && month < 9) {
        relUrl = xia
      } else if(month >8 && month < 12) {
        relUrl = qiu
      } else if(month == 12 || month == 1 || month == 2) {
        relUrl = don
      }
      return {
        background: `url(${relUrl}) no-repeat center / cover`,
      };
    }
  },
  created() {
    window.shell.on('update-login', (data) => {
      this.currentUser = data;
    })
  },
  beforeDestroy() {
    window.shell.off('update-login')
  },
};
</script>

<style scoped>
.header {
  width: 100%;
  /* height: 300px; */
}
.home__header {
  height: 60px;
  border-bottom: 1px hsla(0, 0%, 100%, 0.2) solid;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 calc(50% - 37.5rem);
  min-width: 75rem;
  box-sizing: border-box;
}
.header__tool__wrap {
  height: 240px;
  padding: 0 calc(50% - 37.5rem);
  min-width: 75rem;
  display: flex;
  flex-direction: column;
}
.searchBar__wrap {
  flex: 1;
}
</style>
