<template>
  <div class="container">
    <div class="search" :style="headerStyle">
      <!-- header -->
      <header-bar :pageData="pageData" pageCode="search" />
      <!-- body -->
      <div class="content__wrapper">
        <we-container class="hall">
          <main-container cardArea="default">
            <card-layout :cardLayout="cardLayout" style="padding: 12px 24px;"></card-layout>
          </main-container>
        </we-container>
      </div>
      <we-footer class="search__footer" v-html="footer"></we-footer>
      <!-- 左右侧边栏 -->
      <side-bar direction="left" :sideList="leftSidebarList" />
      <side-bar direction="right" :sideList="rightSidebarList" />
    </div>
  </div>
</template>

<script>
/* eslint-disable no-debugger */
import chun from '../../assets/images/chun.png'
import xia from '../../assets/images/xia.png'
import qiu from '../../assets/images/qiu.png'
import don from '../../assets/images/don.png'
import CardLayout from "../cardLayout";
import SideBar from "../../components/SideBar";
import HeaderBar from "../../components/headerBar/index";
import MainContainer from "../../components/MainContainer";
// import { DEV_URL } from "../../../env";

export default {
  name: "Home",
  components: {
    CardLayout,
    SideBar,
    HeaderBar,
    MainContainer,
  },
  props: {
    pageData: Object,
  },
  data() {
    return {
      x: 1,
      imgList: [chun,xia,qiu,don]
    };
  },
  computed: {
    cardLayout() {
      return JSON.parse(
        (this.pageData &&
          this.pageData.pageInfoEntity &&
          this.pageData.pageInfoEntity.cardLayout) ||
          "[]"
      );
    },
    headerStyle() {
      let relUrl = ''
      let num = Math.floor(Math.random() * 4)
      relUrl = this.imgList[num]
      return {
        background: `url(${relUrl}) no-repeat center top/1920px 740px`,
      };
    },
    // headerStyle() {
    //   const rendedata = (this.pageData && this.pageData.renderData) || {};
    //   const imgUrl =
    //     (rendedata.pageConfig && rendedata.pageConfig["header.imgUrl"]) || "";
    //   const isHttp = /^http(s)?:\/\//.test(imgUrl);
    //   const relUrl = isHttp
    //     ? imgUrl
    //     : process.env.NODE_ENV === "development"
    //     ? `${DEV_URL}${imgUrl}`
    //     : imgUrl;
    //   return {
    //     background: `url(${relUrl}) no-repeat center top/1920px 740px`,
    //   };
    // },
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
        const zhName = (footerConfig || []).find(
          (el) => el.langKey === "zh_CN"
        );
        return temp?.langValue || zhName?.langValue;
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
  },
};
</script>

<style lang="less" scoped>
.container {
  display: flex;
  .search {
    min-height: 100vh;
    width: 100%;
    display: flex;
    flex-direction: column;
    .content__wrapper {
      display: flex;
      flex-grow: 1;
      width: 1200px;
      margin: 0 auto;
      background: #ffffff;
      border-radius: 8px;
      box-shadow: 0 4px 20px 0 rgba(226, 234, 242, 0.5);
      margin-bottom: 24px;
    }
    .search__footer {
      padding: 0;
      background: #ffffff;
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
    &:not(.is-disabled):hover,
    &:not(.is-disabled):focus {
      color: inherit !important;
    }
    i {
      color: rgba(255, 255, 255, 0.6) !important;
    }
  }

  .el-menu-item.is-active {
    border-bottom: 2px solid #fff;
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
  }
}
</style>
