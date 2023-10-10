<!--
 * @Author: 杨芯悦 xyyang01@wisedu.com
 * @Date: 2023-07-25 20:19:26
 * @LastEditors: 杨芯悦 xyyang01@wisedu.com
 * @LastEditTime: 2023-07-27 11:03:02
-->
<template>
  <div class="container">
    <div class="home">
      <!-- header -->
      <header-bar :pageData="pageData" pageCode="home" />
      <!-- body -->
      <div class="content__wrapper">
        <img
          v-if="pageBgImg"
          class="top-bg hall-bg"
          :src="pageBgImg"
          alt=""
          style="height: 587px; top: 30px"
        />
        <we-container class="hall">
          <main-container :cardArea="cardArea">
            <div v-if="globalSearch == 'Y'" class="page-title">
              <p class="main-title portal-font-color-lv1">
                {{ $Lan("CUS_TEMPLATE_OFFICIAL", "myHome", "我的首页") }}
              </p>
              <p class="title-info">
                {{
                  $Lan(
                    "CUS_TEMPLATE_OFFICIAL",
                    "welcomePersonalCenter",
                    "欢迎来到用户中心!"
                  )
                }}
              </p>
            </div>
            <card-layout :cardLayout="cardLayout"></card-layout>
          </main-container>
        </we-container>
      </div>
      <cus-footer :pageData="pageData"></cus-footer>
      <we-footer class="home__footer" v-html="footer"></we-footer>
      <!-- 左右侧边栏 -->
      <side-bar direction="left" :sideList="leftSidebarList" />
      <side-bar direction="right" :sideList="rightSidebarList" />
    </div>
  </div>
</template>

<script>
/* eslint-disable no-debugger */
import CardLayout from "../cardLayout";
import SideBar from "../../components/SideBar";
import HeaderBar from "../../components/CusHomeHeader/index";
import MainContainer from "../../components/MainContainer";
import CusFooter from "../../components/CusFooter/index.vue";
import { DEV_URL } from '../../../env'

export default {
  name: "Home",
  components: {
    CardLayout,
    SideBar,
    HeaderBar,
    MainContainer,
    CusFooter,
  },
  props: {
    pageData: Object,
  },
  data() {
    return {
      x: 1,
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
    cardArea() {
      return (
        this.pageData &&
        this.pageData.renderData &&
        this.pageData.renderData.pageConfig["card.area"]
      );
    },
    globalSearch() {
      return (
        this.pageData &&
        this.pageData.renderData &&
        this.pageData.renderData.pageConfig["global.pagesearch.display"]
      );
    },
    pageRenderData() {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData?.pageInfoEntity?.wid
      };
    },
    pageBgImg() {
      const imgUrl = this.pageRenderData.pageConfig && this.pageRenderData.pageConfig["header.imgUrl"] || '';
      const isHttp = /^http(s)?:\/\//.test(imgUrl);
      const relUrl = isHttp ? process.env.NODE_ENV === 'development' ? imgUrl.split(".cn")[1] : imgUrl : (process.env.NODE_ENV === 'development' ? `${DEV_URL}${imgUrl}` : imgUrl);
      console.log(relUrl);
      return relUrl;
    },
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
  .home {
    min-height: 100vh;
    width: 100%;
    flex-direction: column;
    display: flex;
    justify-content: space-between;
    .content__wrapper {
      display: flex;
      flex-grow: 1;
      position: relative;
    }
    .home__footer {
      padding: 0;
    }
  }
}

/deep/.el-menu--horizontal {
  background: transparent;
  border: none;

  .nav__menus:focus {
    outline: none;
  }

  .el-submenu__title {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: transparent;
    color: #212a39;
    i {
      color: #212a39;
    }
    .el-submenu__icon-arrow {
      right: 0;
    }
  }

  .el-menu-item {
    height: 100%;
    margin: 0 15px;
    padding: 0;
    color: #212a39;
    background: transparent;
    &:not(.is-disabled):hover {
      color: #276eed;
    }
    &:not(.is-disabled):focus {
      color: #276eed;
    }
    i {
      color: #212a39;
    }
  }

  .el-menu-item.is-active {
    color: #276eed;
    border-bottom: 2px solid #276eed;
    font-weight: 700;
    &:not(.is-disabled):focus,
    &:not(.is-disabled):hover {
      color: #276eed;
    }
    i {
      color: #276eed;
    }
  }

  .active__menu {
    color: #276eed;
    border-bottom: 2px solid #276eed;
    i {
      color: #276eed;
    }
    & + i {
      color: #276eed;
    }
  }

  .el-menu-item,
  .el-submenu__title {
    height: 48px;
    line-height: 48px;
  }
}
.page-title {
  padding: 25px 0 35px;

  .main-title {
    font-size: 32px;
    font-weight: 700;
    letter-spacing: 2px;
  }

  .title-info {
    color: #6b6f8c;
    font-size: 14px;
    letter-spacing: 2px;
    margin-top: 9px;
    opacity: 0.8;
  }
}
</style>
