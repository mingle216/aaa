<template>
  <div class="container">
    <div class="home" :style="headerStyle">
      <!-- header -->
      <header-bar :pageData="pageData" pageCode="home" />
      <!-- body -->
      <div class="content__wrapper">
        <we-container class="hall">
          <main-container :cardArea="cardArea">
            <card-layout :cardLayout="cardLayout" :upDownMargin="upDownMargin"
              :style="{
                padding: `12px ${leftRightMargin}px`
              }"></card-layout>
          </main-container>
        </we-container>
      </div>
      <we-footer class="home__footer" v-html="footer"></we-footer>
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
    pageRenderData() {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData && this.pageData.pageInfoEntity.wid,
      };
    },
    headerStyle() {
      let relUrl = ''
      let num = Math.floor(Math.random() * 4)
      relUrl = this.imgList[num]
      return {
        background: `url(${relUrl}) no-repeat center top/100%`,
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
    cardArea() {
      return (
        this.pageData &&
        this.pageData.renderData &&
        this.pageData.renderData.pageConfig["card.area"]
      );
    },
    leftRightMargin() {
      const margin =
        this.pageRenderData &&
        this.pageRenderData.pageConfig &&
        this.pageRenderData.pageConfig["container.leftRightMargin"];
      const num = Number(margin || "24");
      if (isNaN(num)) {
        return 24;
      } else {
        return num >= 12 && num <= 36 ? num : 24;
      }
    },
    upDownMargin() {
      return (
        this.pageRenderData &&
        this.pageRenderData.pageConfig &&
        this.pageRenderData.pageConfig["container.upDownMargin"]
      );
    },
  },
};
</script>

<style lang="less" scoped>
.container {
  display: flex;
  .home {
    min-height: 100vh;
    width: 100%;
    flex-direction: column;
    display: flex;
    justify-content: space-between;
    .content__wrapper {
      display: flex;
      flex-grow: 1;
      min-width: 1200px;
      width: 90%;
      margin: 0 auto 24px;
      background: #ffffff;
      border-radius: 8px;
      box-shadow: 0 4px 20px 0 rgba(226, 234, 242, 0.5);
    }
    .home__footer {
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
