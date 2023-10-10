<template>
  <div class="container">
    <div class="blank">
      <!-- header -->
      <div class="blank__header">
        <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" pageCode="blank" />
        <header-menu :menus="menus" :activeMenuId="activeMenuId" />
        <login :pageRenderData="pageRenderData" :currentUser="currentUser" />
      </div>
      <!-- body -->
      <div class="content__wrapper">
        <we-container class="hall">
          <main-container :cardArea="cardArea">
            <card-layout :cardLayout="cardLayout"></card-layout>
          </main-container>
        </we-container>
      </div>
      <we-footer class="blank__footer" v-html="footer"></we-footer>
      <!-- 左右侧边栏 -->
      <side-bar direction="left" :sideList="leftSidebarList" />
      <side-bar direction="right" :sideList="rightSidebarList" />
    </div>
  </div>
</template>

<script>
import HeaderMenu from '../../components/headerBar/Menu'
import LogoTitle from '../../components/headerBar/LogoTitle'
import Login from '../../components/headerBar/Login'
import CardLayout from "../cardLayout"
import SideBar from '../../components/SideBar'
import MainContainer from '../../components/MainContainer'
export default {
  components: {
    HeaderMenu,
    LogoTitle,
    Login,
    CardLayout,
    SideBar,
    MainContainer
  },
  props: {
    pageData: Object
  },
  data() {
    return {
      currentUser: window.shell.getUserInfo()
    }
  },
  computed: {
    menus() {
      return (this.pageData && this.pageData.treeMenu) || []
    },
    activeMenuId() {
      return this.pageData && this.pageData.activeMenuId || ''
    },
    navbarTitle() {
      return this.pageData
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ''))
            .navigationBarConfig
        : []
    },
    logoImgSrc() {
      return this.pageData
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ''))
            .configLogo
        : {}
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
    cardLayout() {
      return JSON.parse(this.pageData && this.pageData.pageInfoEntity && this.pageData.pageInfoEntity.cardLayout || '[]')
    },
    leftSidebarList() {
      return this.pageData && this.pageData.sideBarGroup && this.pageData.sideBarGroup.leftSidebarList || []
    },
    rightSidebarList() {
      return this.pageData && this.pageData.sideBarGroup && this.pageData.sideBarGroup.rightSidebarList || []
    },
    cardArea() {
      return this.pageData && this.pageData.renderData && this.pageData.renderData.pageConfig['card.area']
    },
    pageRenderData() {
      return this.pageData.renderData || {}
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
}
</script>

<style lang="less" scoped>
.container {
  display: flex;
  .blank {
    min-height: 100vh;
    width: 100%;
    flex-direction: column;
    display: flex;
    justify-content: space-between;
    .blank__header {
      min-height: 60px;
      border-bottom: 1px #E6E6F2 solid;
      position: relative;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 calc(50% - 37.5rem);
      min-width: 75rem;
      box-sizing: border-box;
    }
    .content__wrapper {
      display: flex;
      flex-grow: 1;
    }
    .blank__footer {
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
    color: #707D90!important;
    i {
      color: #707D90;
    }
    .more__title {
      color: #707D90;
    }
  }

  .el-submenu__icon-arrow {
    right: 0;
  }

  .el-menu-item {
    height: 100%;
    margin: 0 15px;
    padding: 0;
    color: #707D90;
    background: transparent !important;
    &:not(.is-disabled):hover {
      color: #707D90!important;
    }
    i {
      color: #707D90!important;
    }
  }

  .el-menu-item.is-active {
    color: #102645!important;
    border-bottom: 2px solid #102645;
    &:hover {
      color: #102645!important;
    }
    i {
      color: #102645!important;
    }
  }

  .active__menu {
    color: #102645;
    border-bottom: 2px solid #102645;
    i {
      color: #102645!important;
    }
  }
  
  .more_menu {
    color: #707D90;
  }
}

/deep/.navbar__title {
  color: #102645;
}
/deep/.we-dropdown-link {
  color: #102645;
}
/deep/.we-divider--vertical {
  opacity: 1;
  background: #E6E6F2;
}
</style>
