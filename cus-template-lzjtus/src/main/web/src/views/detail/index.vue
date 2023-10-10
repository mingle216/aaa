<template>
  <div class="container">
    <div class="detail" :style="headerStyle">
      <!-- header -->
      <header class="header">
        <div class="detail__header">
          <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" />
          <header-menu :menus="menus" :activeMenuId="activeMenuId" />
          <login
            :pageRenderData="pageRenderData"
            :pageData="pageData"
            :currentUser="currentUser"
          />
        </div>
      </header>
      <!-- body -->
      <div class="content__wrapper">
        <we-container class="hall">
          <main-container cardArea="default">
            <card-layout
              :cardLayout="cardLayout"
              style="padding: 24px"
            ></card-layout>
          </main-container>
        </we-container>
      </div>
      <we-footer class="detail__footer" v-html="footer"></we-footer>
      <!-- 左右侧边栏 -->
      <side-bar direction="left" :sideList="leftSidebarList" />
      <side-bar direction="right" :sideList="rightSidebarList" />
    </div>
  </div>
</template>

<script>
import chun from '../../assets/images/chun.png'
import xia from '../../assets/images/xia.png'
import qiu from '../../assets/images/qiu.png'
import don from '../../assets/images/don.png'
import HeaderMenu from "../../components/headerBar/Menu";
import LogoTitle from "../../components/headerBar/LogoTitle";
import Login from "../../components/headerBar/Login";
import CardLayout from "../cardLayout";
import SideBar from "../../components/SideBar";
import MainContainer from "../../components/MainContainer";
// import { DEV_URL } from "../../../env";
export default {
  components: {
    HeaderMenu,
    LogoTitle,
    Login,
    CardLayout,
    SideBar,
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
    pageConfig() {
      return this.pageData.renderData.pageConfig || {};
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
      let relUrl = ''
      let num = Math.floor(Math.random() * 4)
      relUrl = this.imgList[num]
      return {
        background: `url(${relUrl}) no-repeat center top/1920px 740px`,
      };
    },
    // headerStyle() {
    //   const isHttp = /^http(s)?:\/\//.test(this.headerImg);
    //   const relUrl = isHttp
    //     ? this.headerImg
    //     : process.env.NODE_ENV === "development"
    //     ? `${DEV_URL}${this.headerImg}`
    //     : this.headerImg;
    //   return {
    //     background: `url(${relUrl}) no-repeat center top/1920px 740px`,
    //   };
    // },
    currentUser() {
      return window.shell.getUserInfo();
    },
    pageRenderData() {
      return this.pageData.renderData || {};
    },
  },
};
</script>

<style lang="less" scoped>
.container {
  display: flex;
  .detail {
    min-height: 100vh;
    width: 100%;
    // flex-direction: column;
    // display: flex;
    // justify-content: space-between;
    .header {
      width: 100%;
      min-height: 96px;
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
      margin-top: -200px;
      width: 1200px;
      margin: 0 auto;
      // background: #ffffff;
      // border-radius: 8px;
      // box-shadow: 0 4px 20px 0 rgba(226, 234, 242, 0.5);
    }
    .main__center {
      background: transparent !important;
      border-radius: 0px !important;
    }
    .detail__footer {
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
    &::not(.is-disabled):focus {
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

/deep/.itemdetails > .content {
  background: #ffffff;
  box-shadow: 0 4px 20px 0 rgba(226, 234, 242, 0.5);
  border-radius: 8px;
  // padding: 24px;
  margin-top: -70px;
}

/deep/.itemdetails > .content .content-left {
  margin: 24px 0 24px 24px;
}

/deep/.itemdetails > .content .content-right {
  margin: 24px 24px 0 0;
}

/deep/.itemdetails > .content .navbar {
  top: 28px;
}

/deep/.itemdetails {
  padding-top: 0 !important;
  margin-top: -11px !important;
}
</style>
