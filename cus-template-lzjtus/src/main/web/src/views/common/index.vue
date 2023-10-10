<template>
  <div class="container">
    <div class="common" :style="headerStyle">
      <!-- header -->
      <header class="header" :style="headerHeight">
        <div class="common__header">
          <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" />
          <header-menu :menus="menus" :activeMenuId="activeMenuId" />
          <login :pageRenderData="pageRenderData" :pageData="pageData" :currentUser="currentUser" />
        </div>
        <div class="section__wrap" :class="[langTitle || langSubTitle ? 'showTitle' : '']">
          <search-bar
                  class="searchBar__wrap"
                  :pageRenderData="pageRenderData"
                  :pageCode="pageCode"
                  :isSecond="pageCodes"
                  :currentUser="currentUser"
          />

        </div>
      </header>
      <!-- body -->
      <div class="content__wrapper">
        <we-container class="hall">
          <main-container :cardArea="cardArea">
            <card-layout
              :cardLayout="cardLayout"
              :upDownMargin="upDownMargin"
              :style="{
                padding: `12px ${leftRightMargin}px`,
              }"
            />
          </main-container>
        </we-container>
      </div>
      <we-footer class="common__footer" v-html="footer" />
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
  import SearchBar from "../../components/searchBar";
import HeaderMenu from "../../components/headerBar/Menu";
import LogoTitle from "../../components/headerBar/LogoTitle";
import Login from "../../components/headerBar/Login";
import CardLayout from "../cardLayout";
import SideBar from "../../components/SideBar";
import MainContainer from "../../components/MainContainer";
export default {
  components: {
    HeaderMenu,
    LogoTitle,
    Login,
    CardLayout,
    SideBar,
    MainContainer,
    SearchBar
  },
  data() {
    return {
      imgList: [chun,xia,qiu,don],
      pageCode: 'search',
      pageCodes: true
    }
  },
  props: {
    pageData: Object,
  },
  computed: {
    showBreadCrumb() {
      const breadLists = this.pageRenderData.breadcrumb || []
      return (
        this.pageRenderData.pageConfig &&
        this.pageRenderData.pageConfig["breadcrumb.show"] === "Y" &&
        breadLists.length > 1
      );
    },
    upDownMargin() {
      return (
        this.pageRenderData &&
        this.pageRenderData.pageConfig &&
        this.pageRenderData.pageConfig["container.upDownMargin"]
      );
    },
    leftRightMargin() {
      const margin =
        this.pageRenderData &&
        this.pageRenderData.pageConfig["container.leftRightMargin"];
      const num = Number(margin || "24");
      if (isNaN(num)) {
        return 24;
      } else {
        return num >= 12 && num <= 36 ? num : 24;
      }
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
      return this.pageData.renderData || {};
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
      return this.pageRenderData.pageConfig["header.imgUrl"] || "";
    },
    headerStyle() {
      let relUrl = ''
      let num = Math.floor(Math.random() * 4)
      relUrl = this.imgList[num]
      return {
        background: `url(${relUrl}) no-repeat center top/1920px 740px`,
      };

    },
    currentUser() {
      return window.shell.getUserInfo();
    },
    cardArea() {
      return this.pageRenderData.pageConfig["card.area"];
    },
    langTitle() {
      const arr =
        (this.pageRenderData &&
          this.pageRenderData.pageConfig["header.titleLangs"]) ||
        [];
      const zhName = arr.find(el => el.key === 'zh_CN')
      const temp = arr.find((el) => el.key === this.$i18n.locale);
      return temp?.value || zhName?.value;
    },
    langSubTitle() {
      const arr =
        (this.pageRenderData &&
          this.pageRenderData.pageConfig["header.subTitleLangs"]) ||
        [];
      const zhName = arr.find(el => el.key === 'zh_CN')
      const temp = arr.find((el) => el.key === this.$i18n.locale);
      return temp?.value || zhName?.value;
    },
    headerHeight() {
      if (this.langTitle && this.langSubTitle) {
        return { minHeight: "216px" };
      } else {
        return { minHeight: "96px" };
      }
    },
  },
};
</script>

<style lang="less" scoped>
.container {
  display: flex;
  .common {
    min-height: 100vh;
    width: 100%;
    flex-direction: column;
    display: flex;
    justify-content: space-between;
    .header {
      width: 100%;
      min-height: 216px;
      .common__header {
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
      .section__wrap {
        color: #fff;
        min-width: 75rem;
        padding: 0 calc(50% - 37.5rem);
        &.showTitle {
          /deep/.breadcrumb__wrap {
            margin-bottom: 0;
          }
        }
        display: flex;
        flex-direction: column;
        justify-content: center;
        .text__wrap {
          display: flex;
          flex-direction: column;
          margin: 40px 0;
          &.showBreadCrumb {
            margin-top: 24px;
          }
          .title {
            font-size: 36px;
            max-width: 65rem;
            line-height: 44px;
            font-weight: bold;
          }
          .sub__title {
            font-size: 16px;
            line-height: 24px;
            max-width: 65rem;
            margin-top: 16px;
            opacity: 0.8;
          }
        }
      }
    }
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
    .common__footer {
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
    color: rgba(255, 255, 255, 0.6);
    i {
      color: rgba(255, 255, 255, 0.6);
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
      color: rgba(255, 255, 255, 0.6);
    }
  }

  .el-menu-item.is-active {
    border-bottom: 2px solid #fff;
    color: #fff !important;
    &:hover,
    &:focus {
      color: #fff !important;
    }
    i {
      color: #fff !important;
    }
  }

  .active__menu {
    color: #fff !important;
    border-bottom: 2px solid #fff;
    i {
      color: #fff !important;
    }
  }
}
</style>
