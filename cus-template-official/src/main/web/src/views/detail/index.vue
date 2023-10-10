<template>
  <div class="container">
    <div class="detail">
      <!-- header -->
      <!-- <header class="header" :style="headerStyle">
        <div class="detail__header">
          <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" />
          <header-menu :menus="menus" :activeMenuId="activeMenuId" />
          <login :pageData="pageData" :pageRenderData="pageRenderData" :currentUser="currentUser" />
        </div>
      </header> -->
      <header-bar :pageData="pageData" pageCode="detail" />
      <!-- body -->
      <div class="content__wrapper">
        <img
          v-if="pageBgImg"
          class="top-bg hall-bg"
          :src="pageBgImg"
          alt=""
          style="height: 180px; top: 64px"
        />
        <we-container class="hall">
          <main-container :cardArea="cardArea">
            <card-layout :cardLayout="cardLayout"></card-layout>
          </main-container>
        </we-container>
      </div>
      <cus-footer :pageData="pageData"></cus-footer>
      <we-footer class="detail__footer" v-html="footer"></we-footer>
      <!-- 左右侧边栏 -->
      <side-bar direction="left" :sideList="leftSidebarList" />
      <side-bar direction="right" :sideList="rightSidebarList" />
    </div>
  </div>
</template>

<script>
import HeaderMenu from "../../components/headerBar/Menu";
import LogoTitle from "../../components/headerBar/LogoTitle";
import Login from "../../components/headerBar/Login";
import CardLayout from "../cardLayout";
import SideBar from "../../components/SideBar";
import MainContainer from "../../components/MainContainer";
import { DEV_URL } from "../../../env";
import HeaderBar from "../../components/CusHomeHeader/index";
import CusFooter from "../../components/CusFooter/index.vue";

export default {
  components: {
    HeaderMenu,
    LogoTitle,
    Login,
    CardLayout,
    SideBar,
    MainContainer,
    HeaderBar,
    CusFooter,
  },
  props: {
    pageData: Object,
  },
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
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ""))
            .navigationBarConfig
        : [];
    },
    logoImgSrc() {
      return this.pageData
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ""))
            .configLogo
        : {};
    },
    pageConfig() {
      return this.pageData.renderData.pageConfig || {};
    },
    footer() {
      const footerConfig = this.pageData
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ""))
            .footerConfig
        : "";
      if (Object.prototype.toString.call(footerConfig) === "[object Array]") {
        const temp = (footerConfig || []).find((el) => el.langKey === this.$i18n.locale);
        const zh = (footerConfig || []).find((el) => el.langKey === "zh_CN");
        return temp?.langValue || zh?.langValue;
      } else {
        return footerConfig;
      }
    },
    leftSidebarList() {
      return (
        (this.pageData &&
          this.pageData.sideBarGroup &&
          this.pageData.sideBarGroup.leftSidebarList) ||
        []
      );
    },
    rightSidebarList() {
      return (
        (this.pageData &&
          this.pageData.sideBarGroup &&
          this.pageData.sideBarGroup.rightSidebarList) ||
        []
      );
    },
    cardLayout() {
      return JSON.parse(
        (this.pageData &&
          this.pageData.pageInfoEntity &&
          this.pageData.pageInfoEntity.cardLayout) ||
          "[]"
      );
    },
    headerImg() {
      return this.pageConfig["header.imgUrl"] || "";
    },
    headerStyle() {
      const isHttp = /^http(s)?:\/\//.test(this.headerImg);
      const relUrl = isHttp
        ? this.headerImg
        : process.env.NODE_ENV === "development"
        ? `${DEV_URL}${this.headerImg}`
        : this.headerImg;
      return {
        background: `url(${relUrl}) no-repeat center / cover`,
      };
    },
    pageRenderData() {
      return this.pageData.renderData || {};
    },
    cardArea() {
      return "default";
      // return this.pageData && this.pageData.renderData && this.pageData.renderData.pageConfig['card.area']
    },
    pageRenderData() {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData?.pageInfoEntity?.wid,
      };
    },
    pageBgImg() {
      const imgUrl =
        (this.pageRenderData.pageConfig &&
          this.pageRenderData.pageConfig["header.imgUrl"]) ||
        "";
      const isHttp = /^http(s)?:\/\//.test(imgUrl);
      const relUrl = isHttp
        ? process.env.NODE_ENV === "development"
          ? imgUrl.split(".cn")[1]
          : imgUrl
        : process.env.NODE_ENV === "development"
        ? `${DEV_URL}${imgUrl}`
        : imgUrl;
      console.log(relUrl);
      return relUrl;
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

<style lang="less" scoped>
.container {
  display: flex;
  .top-bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 702px;
    z-index: -1;
  }
  .detail {
    min-height: 100vh;
    width: 100%;
    // flex-direction: column;
    // display: flex;
    // justify-content: space-between;
    .header {
      width: 100%;
      min-height: 300px;
      .detail__header {
        height: 60px;
        border-bottom: 1px solid hsla(0, 0%, 100%, 0.2);
        position: relative;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 calc(50% - 37.5rem);
        min-width: 75rem;
        box-sizing: border-box;
      }
    }
    .content__wrapper {
      display: flex;
      flex-grow: 1;
      position: relative;
    }
    .detail__footer {
      padding: 0;
    }
  }
}
/deep/.el-menu--horizontal {
  background: transparent !important;
  border: none;

  .nav__menus:focus {
    outline: none;
  }

  .el-submenu__title {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: transparent !important;
    color: rgba(255, 255, 255, 0.6) !important;
    i {
      color: rgba(255, 255, 255, 0.6) !important;
    }
    .el-submenu__icon-arrow {
      right: 0;
    }
  }

  .el-menu-item {
    height: 100%;
    margin: 0 15px;
    padding: 0;
    color: rgba(255, 255, 255, 0.6);
    background: transparent !important;
    &:not(.is-disabled):hover {
      color: inherit !important;
    }
    &:not(.is-disabled):focus {
      color: inherit !important;
    }
    i {
      color: rgba(255, 255, 255, 0.6) !important;
    }
  }

  .el-menu-item.is-active {
    color: #fff !important;
    border-bottom: 2px solid #fff !important;
    &:not(.is-disabled):focus,
    &:not(.is-disabled):hover {
      color: #fff !important;
    }
    i {
      color: #fff !important;
    }
  }

  .active__menu {
    color: #fff;
    border-bottom: 2px solid #fff;
    i {
      color: #fff !important;
    }
    & + i {
      color: #fff !important;
    }
  }
}
</style>
