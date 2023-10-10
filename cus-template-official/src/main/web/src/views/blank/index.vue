<template>
  <div class="container">
    <div class="blank">
      <!-- header -->
      <headerbar1 :pageData="pageData" pageCode="home" />
      <div class="blank__header" style="display: none;">
        <logo-title :navbarTitle="navbarTitle" :logoImgSrc="logoImgSrc" pageCode="blank" />
        <header-menu :menus="menus" :activeMenuId="activeMenuId" />
        <login :pageRenderData="pageRenderData" :currentUser="currentUser" />
      </div>
      <!-- body -->
      <div class="content__wrapper">
        <img class="content__wrapper_bg" src="./dzimage/bg.png">
        <we-container class="hall">
          <main-container :cardArea="cardArea">
            <card-layout :cardLayout="cardLayout"></card-layout>
          </main-container>
        </we-container>
      </div>
      <!-- v-html="footer" -->
      <we-footer class="blank__footer">
        <div class="top newtop">
          <img class="img1" src="./dzimage/16346312491044991.png" />
          <div class="official-footer">
            <div class="official-footer-left">
              <label class="label">线下服务大厅：</label>
              <ul class="info">
                <li class="info-item">
                  <div class="info-item-title">联系方式 </div>
                  <i class="iconfont icon-place"></i>
                  <div class="textdiv">
                    <p class="text">地址：陕西理工大学知行楼三楼</p>
                    <p class="text">电话（传真）：0916-2641525</p>
                    <p class="text">邮编：723001</p>
                  </div>
                </li>
                <li class="info-item">
                  <div class="info-item-title">访问量 </div>
                  <i class="iconfont icon-SelfService"></i>
                  <div class="textdiv">
                    <p class="text"> 当日访问量：{{DRFWL}}</p>
                    <!-- <p class="text" >当日访问人数：{{DQFWRS}}</p> -->
                    <p class="text">累计访问量：{{FWZL}}</p>
                  </div>
                </li>
              </ul>
            </div>
            <div class="official-footer-right">
              <div class="wx">
                <img class="logoo" src="./dzimage/16346332008211961.png" />
                <div class="otherimg">
                  <!-- <div><img src="./dzimage/16346337796402076.png" />
                    <p>官方微信</p>
                  </div>
                  <div><img src="./dzimage/16346337929277846.png" />
                    <p> 信息化公众号</p>
                  </div>
                  <div><img src="./dzimage/16346338049404712.png" />
                    <p>陕西理工App</p>
                  </div> -->
                </div>
              </div>
              <div class="app">
                <img src="./dzimage/16346332450445753.png" />
                <p>信息化处微信公众号</p>
              </div>
            </div>
          </div>
        </div>
        <div class="widewrapper copyright">
          版权所有Copyright©&nbsp;2021&nbsp;陕西理工大学信息化建设与管理处&nbsp;&nbsp;&nbsp;陕ICP备05001610号-1
        </div>
      </we-footer>
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
  import MainContainer from '../../components/MainContainerdz'
  import Headerbar1 from "../../components/HeaderBar1/index";
  import axios from 'axios';
  axios.defaults.withCredentials = true
  export default {
    components: {
      HeaderMenu,
      LogoTitle,
      Login,
      CardLayout,
      SideBar,
      MainContainer,
      Headerbar1
    },
    props: {
      pageData: Object
    },
    data() {
      return {
        currentUser: window.shell.getUserInfo(),
        obj: {},
        pv: 0,
        FWZL: 0,
        DQFWRS: 0,
        DRFWL: 0,
      }
    },
    mounted() {
      // 获取总访问量
      window.shell.execTemplateMethod('getZFWL', {}, data => {
        if (data.errcode === '0') {
          this.FWZL = data.data == null ? 0 : data.data
        }
      })
      // 获取当日问量
      window.shell.execTemplateMethod('getFWL', {}, data => {
        if (data.errcode === '0') {
          this.DRFWL = data.data == null ? 0 : data.data
        }
      })
      // 获取总访人数
      window.shell.execTemplateMethod('getFWRS', {}, data => {
        if (data.errcode === '0') {
          this.DQFWRS = data.data == null ? 0 : data.data
        }
      })
      // axios
      //   .post(
      //     "http://newehall.snut.edu.cn/casp-ioc/api/data/todayFlow",
      //     {
      //       params: {
      //         model: "ampPc"
      //       }
      //     }
      //   ).then(res => {
      //     if (res.data.errCode == "SUCCESS") {
      //       this.obj = res.data.data;
      //     }
      //   }),
      //   axios
      //     .post(
      //       "http://newehall.snut.edu.cn/casp-ioc/api/data/homeVisitBasic",
      //       {
      //         params: {
      //           model: "ampPc"
      //         }
      //       }
      //     ).then(res => {
      //       if (res.data.errCode == "SUCCESS") {
      //         this.pv = res.data.data.pv;
      //       }
      //     })
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
          return temp ?.langValue || zh ?.langValue;
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
       // 获取总访问量
       window.shell.execTemplateMethod('getZFWL', {}, data => {
        if (data.errcode === '0') {
          this.FWZL = data.data == null ? 0 : data.data
        }
      })
      // 获取当日问量
      window.shell.execTemplateMethod('getFWL', {}, data => {
        if (data.errcode === '0') {
          this.DRFWL = data.data == null ? 0 : data.data
        }
      })
      // 获取总访人数
      window.shell.execTemplateMethod('getFWRS', {}, data => {
        if (data.errcode === '0') {
          this.DQFWRS = data.data == null ? 0 : data.data
        }
      })
    },
    beforeDestroy() {
      window.shell.off('update-login')
    },
  }
</script>

<style lang="less" scoped>
  .newtop {
    position: relative;
    height: 170px;
  }

  .img1 {
    position: absolute;
    bottom: 0;
    z-index: 3;
    height: 100%;
    width: 100%;
  }

  .official-footer-left {
    z-index: 4;
  }

  .label {
    font-size: 14px;
    color: #6F7D8F;
    display: none !important;
  }

  .info-item {
    width: 270px !important;
    height: 86px !important;
    display: block !important;
    background-color: rgba(225, 225, 225, 0) !important;
  }

  .info-item-title {
    height: 26px;
    font-family: MicrosoftYaHei;
    font-size: 20px;
    color: #FFFFFF;
    letter-spacing: 0;
    font-weight: 400;
  }

  .iconfont {
    font-size: 20px;
    margin-right: 8px;
    margin-top: -1px;
    display: none !important;
  }

  .textdiv {
    margin-top: 10px;
    color: #fff
  }

  .textdiv>.text {
    margin-bottom: 13px;
    display: block !important;
  }

  .official-footer-right {
    z-index: 4;
  }

  .wx {
    margin-right: 80px;
  }

  .logoo {
    height: 60px;
    margin-top: 35px;
  }

  .otherimg {
    display: flex;
    margin-top: 15px;
    color: #fff
  }

  .otherimg>div {
    width: 33.33%;
    text-align: center;
  }

  .otherimg>div>p {
    margin-top: 10px;
  }

  .app {
    margin-right: 10px;
    text-align: center;
  }
  
  .app>img{
    height: 100px;
  }

  .app>p {
    text-align: center;
    height: 19px;
    font-family: MicrosoftYaHei;
    font-size: 14px;
    color: #FFFFFF;
    letter-spacing: 0;
    font-weight: 400;
  }

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
        padding: 0 calc(50% - 41.5rem);
        min-width: 75rem;
        box-sizing: border-box;
        background: #0B5AA5;
      }

      .content__wrapper {
        display: flex;
        flex-grow: 1;
        position: relative;

        .content__wrapper_bg {
          position: absolute;
          width:100%;
          height: 310px;
        }
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
      color: #707D90 !important;

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
      color: rgba(255, 255, 255, 0.6);
      background: transparent !important;

      &:not(.is-disabled):hover {
        color: #707D90 !important;
      }

      i {
        color: rgba(255, 255, 255, 0.6) !important;
      }
    }

    .el-menu-item.is-active {
      color: #fff !important;
      border-bottom: 2px solid #fff;

      &:hover {
        color: #102645 !important;
      }

      i {
        color: #fff !important;
      }
    }

    .active__menu {
      color: #102645;
      border-bottom: 2px solid #102645;

      i {
        color: #102645 !important;
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