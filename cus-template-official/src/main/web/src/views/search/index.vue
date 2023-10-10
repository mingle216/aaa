<template>
  <div class="container">
    <div class="search">
      <!-- header -->
      <header-bar :pageData="pageData" pageCode="search" />
      <!-- body -->
      <div class="content__wrapper">
        <we-container class="hall">
          <main-container cardArea="default">
            <card-layout :cardLayout="cardLayout"></card-layout>
          </main-container>
          <we-footer class="search__footer" v-html="footer"></we-footer>
        </we-container>
      </div>
      <!-- 左右侧边栏 -->
      <side-bar direction="left" :sideList="leftSidebarList" />
      <side-bar direction="right" :sideList="rightSidebarList" />
    </div>
  </div>
</template>

<script>
/* eslint-disable no-debugger */
import CardLayout from "../cardLayout";
import SideBar from '../../components/SideBar'
import HeaderBar from '../../components/headerBar/index'
import MainContainer from '../../components/MainContainer'

export default {
  name: 'Home',
  components: {
    CardLayout,
    SideBar,
    HeaderBar,
    MainContainer
  },
  props: {
    pageData: Object
  },
  computed: {
    cardLayout() {
      return JSON.parse(this.pageData && this.pageData.pageInfoEntity && this.pageData.pageInfoEntity.cardLayout || '[]')
    },
    footer() {
      const footerConfig = this.pageData 
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || '')).footerConfig
        : "";
      if (Object.prototype.toString.call(footerConfig) === '[object Array]') {
        const temp = (footerConfig || []).find(el => el.langKey === this.$i18n.locale)
        const zh = (footerConfig || []).find(
          (el) => el.langKey === 'zh_CN'
        );
        return temp?.langValue || zh?.langValue;
      } else {
        return footerConfig
      }
    },
    leftSidebarList() {
      return this.pageData && this.pageData.sideBarGroup && this.pageData.sideBarGroup.leftSidebarList || []
    },
    rightSidebarList() {
      return this.pageData && this.pageData.sideBarGroup && this.pageData.sideBarGroup.rightSidebarList || []
    },
    // cardArea() {
    //   return this.pageData && this.pageData.renderData && this.pageData.renderData.pageConfig['card.area']
    // }
  }
}
</script>

<style lang="less" scoped  >
.container {
  display: flex;
  .search {
    min-height: 100vh;
    width: 100%;
    // flex-direction: column;
    // display: flex;
    // justify-content: space-between;
    .main__center{
      width:100%  !important;
    }
    .content__wrapper {
      display: flex;
      flex-grow: 1;
    }
    .search__footer {
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
    color: rgba(255, 255, 255, 0.6)!important;
    i {
      color: rgba(255, 255, 255, 0.6)!important;
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
    &:not(.is-disabled):hover, &:not(.is-disabled):focus {
      color: inherit!important;
    }
    i {
      color: rgba(255, 255, 255, 0.6)!important;
    }
  }

  .el-menu-item.is-active {
    border-bottom: 2px solid #fff;
    i {
      color: #fff!important;
    }
  }
  
  .active__menu {
    color: #fff;
    border-bottom: 2px solid #fff;
    i {
      color: #fff!important;
    }
  }
}
</style>
