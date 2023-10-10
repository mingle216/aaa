<template>
  <header class="header" :style="headerStyle">
    <!-- <div class="header_login">
      <span> 陕西理工大学一网通办服务门户欢迎您！</span>
    </div> -->
    <div class="headerLogo">
      <img src="./images/colorlogo.png" />
      <span>一网通办服务门户</span>
      <login :pageRenderData="pageRenderData" :currentUser="currentUser" />
    </div>
    <div class="home__header">
      <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" />
      <header-menu :menus="menus" :activeMenuId="activeMenuId" />
      <search-bar class="searchBar__wrap" :pageRenderData="pageRenderData" :pageCode="pageCode"
          :currentUser="currentUser" />
      <div class="header__tool__wrap">
        <bread-crumb :pageRenderData="pageRenderData"></bread-crumb>
        
      </div>
    </div>
    
  </header>
</template>

<script>
  import HeaderMenu from "./Menu";
  import LogoTitle from "./LogoTitle";
  import Login from "./Login";
  import SearchBar from "../searchBar1";
  import BreadCrumb from "../BreadCrumb";
  import { DEV_URL } from '../../../env'
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
          wid: this.pageData ?.pageInfoEntity ?.wid
      };
      },
      headerStyle() {
        // TODO img前缀
        const imgUrl = this.pageRenderData.pageConfig && this.pageRenderData.pageConfig["header.imgUrl"] || '';
        const isHttp = /^http(s)?:\/\//.test(imgUrl);
        const relUrl = isHttp ? imgUrl : (process.env.NODE_ENV === 'development' ? `${DEV_URL}${imgUrl}` : imgUrl);
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

  .header_login {
    height: 40px;
    line-height: 40px;
    background: rgba(18, 117, 198, 0.05);
    padding: 0px calc(50% - 41.5rem);
    position: relative;

  }

  .header_login>span {
    font-family: MicrosoftYaHei;
    font-size: 14px;
    color: #000000;
    letter-spacing: 0;
    font-weight: 400;
  }

  .headerLogo {
    padding: 16px calc(50% - 41.5rem);
    position: relative;
  }

  .headerLogo>img {
    height: 60px;
    vertical-align: middle;
    margin-right: 31.8px;
  }

  .headerLogo>span {
    height: 37px;
    font-family: MicrosoftYaHei-Bold;
    font-size: 28px;
    color: #000000;
    letter-spacing: 2.8px;
    font-weight: 700;
    vertical-align: middle;

  }

  .home__header {
    height: 60px;
    border-bottom: 1px hsla(0, 0%, 100%, 0.2) solid;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 calc(50% - 41.5rem);
    min-width: 75rem;
    box-sizing: border-box;
    background: #0B5AA5;

  }

  .header__tool__wrap {
    /* height: 240px; */
    padding: 0 calc(50% - 37.5rem);
    min-width: 75rem;
    display: flex;
    flex-direction: column;
  }

  .searchBar__wrap {
    position: absolute;
    right: calc(50% - 41.5rem);
  }
</style>