<template>
  <div class="container">
    <div class="common">
      <!-- header -->
      <header class="header" :style="headerStyle">
        <div class="common__header">
          <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" />
          <header-menu :menus="menus" :activeMenuId="activeMenuId" />
          <login :pageRenderData="pageRenderData" :currentUser="currentUser" />
        </div>
        <div class="section__wrap">
          <!-- 面包屑 -->
          <bread-crumb :pageRenderData="pageRenderData"></bread-crumb>
          <!-- 标题栏 -->
          <div class="text__wrap">
            <p class="title text__ellipsis">
              <span>
                {{ langTitle }}
              </span>
            </p>
            <p class="sub__title text__ellipsis">
              <span>
                {{ langSubTitle }}
              </span>
            </p>
          </div>
        </div>
      </header>
      <!-- body -->
      <div class="content__wrapper" :style="getpageName ? 'position:relative;z-index:2;margin-top:-80px;' : ''">
        <we-container class="hall">
          <main-container :cardArea="cardArea" :pageData='pageData'>
            <card-layout :cardLayout="cardLayout" />
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
import HeaderMenu from "../../components/headerBar/Menu";
import LogoTitle from "../../components/headerBar/LogoTitle";
import Login from "../../components/headerBar/Login";
import CardLayout from "../cardLayout";
import SideBar from "../../components/SideBar";
import BreadCrumb from "../../components/BreadCrumb";
import MainContainer from "../../components/MainContainer";
import { DEV_URL } from "../../../env";
export default {
  components: {
    HeaderMenu,
    LogoTitle,
    Login,
    CardLayout,
    SideBar,
    BreadCrumb,
    MainContainer,
  },
  props: {
    pageData: Object,
  },
  data() {
    return {
      currentUser: window.shell.getUserInfo()
    }
  },
  computed: {
    getpageName(){
      if(this.pageData.renderData.breadcrumb[0].pageCode.indexOf('hall')!=-1 && this.pageData.renderData.breadcrumb[1].pageCode.indexOf('itemCategoryDetail')!=-1){
        return true
      }else{
        return false
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
        const zh = (footerConfig || []).find(
          (el) => el.langKey === 'zh_CN'
        );
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
      return this.pageRenderData.pageConfig["header.imgUrl"] || "";
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
    cardArea() {
      return this.pageRenderData.pageConfig["card.area"];
    },
    langTitle() {
      const arr =
        (this.pageRenderData &&
          this.pageRenderData.pageConfig["header.titleLangs"]) ||
        [];
      const temp = arr.find((el) => el.key === this.$i18n.locale);
      const zhName = arr.find((el) => el.key === 'zh_CN')
      return temp?.value || zhName?.value;
    },
    langSubTitle() {
      const arr =
        (this.pageRenderData &&
          this.pageRenderData.pageConfig["header.subTitleLangs"]) ||
        [];
      const temp = arr.find((el) => el.key === this.$i18n.locale);
      const zhName = arr.find((el) => el.key === 'zh_CN')
      return temp?.value || zhName?.value;
    },
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
      height: 342px;
      position: relative;
      z-index: 1;
      .common__header {
  background: rgba(0,0,0,0.3);
        height: 56px;
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
        display: flex;
        flex-direction: column;
        justify-content: center;
        height: calc(100% - 100px);
        .text__wrap {
          display: flex;
          flex-direction: column;
          /* margin-top: 36px; */
          .title {
            font-size: 36px;
            margin-bottom: 16px;
            max-width: 65rem;
          }
          .sub__title {
            font-size: 14px;
            max-width: 65rem;
          }
        }
      }
    }
    .content__wrapper {
      display: flex;
      flex-grow: 1;

    }
    .common__footer {
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
