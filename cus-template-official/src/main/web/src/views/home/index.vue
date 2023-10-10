<template>
  <div class="container">
    <div class="home">
      <!-- header -->
      <header-bar style="display: none;" :pageData="pageData" pageCode="home" />
      <headerbar1 :pageData="pageData" pageCode="home" />
      <!-- body -->
      <div class="content__wrapper">
        <we-carousel indicator-position="outside" :interval="6000" :loop='true'>
          <we-carousel-item>
            <img class="content__wrapper_bg" src="./dzimage/1.jpg">
          </we-carousel-item>
          <we-carousel-item>
            <img class="content__wrapper_bg" src="./dzimage/2.jpg">
          </we-carousel-item>
          <we-carousel-item>
            <img class="content__wrapper_bg" src="./dzimage/3.jpg">
          </we-carousel-item>
        </we-carousel>
        <!-- <img class="content__wrapper_bg" src="./dzimage/bg.png"> -->
        <we-container class="hall">
          <main-container :cardArea="cardArea">
            <card-layout :cardLayout="cardLayout"></card-layout>
          </main-container>
        </we-container>
      </div>
      <we-footer class="home__footer">
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
                    <!-- <p class="text">当日访问人数：{{DQFWRS}}</p> -->
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
  /* eslint-disable no-debugger */
  import CardLayout from "../cardLayout";
  import SideBar from "../../components/SideBar";
  import HeaderBar from "../../components/headerBar/index";
  import MainContainer from "../../components/MainContainerdz";
  import Headerbar1 from "../../components/HeaderBar1/index";
  import axios from 'axios';
  axios.defaults.withCredentials = true

  export default {
    name: "Home",
    components: {
      CardLayout,
      SideBar,
      HeaderBar,
      MainContainer,
      Headerbar1,
    },
    props: {
      pageData: Object,
    },
    data() {
      return {
        x: 1,
        pv: 0,
        FWZL: 0,
        DQFWRS: 0,
        DRFWL: 0,
      };
    },
    created(){
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
      //     "https://newehall.snut.edu.cn/casp-ioc/api/data/homeVisitBasic",
      //     {
      //       params: {
      //         model: "ampPc"
      //       }
      //     }
      //   ).then(res => {
      //     if (res.data.errCode == "SUCCESS") {
      //       this.pv = res.data.data.pv;
      //     }
      //   })
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
      cardArea() {
        return (
          this.pageData &&
          this.pageData.renderData &&
          this.pageData.renderData.pageConfig["card.area"]
        );
      },
    },
  };
</script>

<style lang="less" scoped>
  .we-carousel--horizontal {
    width: 100%;
    position: absolute;

  }

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

  .app>img {
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

    .home {
      min-height: 100vh;
      width: 100%;
      flex-direction: column;
      display: flex;
      justify-content: space-between;

      .content__wrapper {
        display: flex;
        flex-grow: 1;
        /* background-image: url('./dzimage/bg.png');
      background-repeat:no-repeat;
      background-size: 100% auto; */
        background: #F4F4F4;
        position: relative;

        .content__wrapper_bg {
          position: absolute;
          width: 100%;
          height: 310px;
        }
      }

      .home__footer {
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

      &+i {
        color: #fff !important;
      }
    }
  }
</style>