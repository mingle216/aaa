<template>
  <div class="portal-font-color-lv1">

    <div class="headerInfo" v-show="loading" :class="[isCollapse?'expand':'fold']">

      <div class="expand-box" :style="isCollapse?headerInfoStyle:''">
        <ContainerScroll :maxHeight="scrollHeight" :barKeepShow="true" :size="4">
          <div v-if="currentUser">
          <div class="headerInfo__imgBox" :style="headerImgStyle">
            <div class="headerInfo__imgWrap">
              <div class="headerInfo__img" :style="`background-image: url(${headImg})`" />
            </div>
            <!-- <Set type="expand" :currentUser="currentUser" :pageData="pageData" /> -->
          </div>

          <p class="headerInfo__title ellipsis" :title="personalInfo.stuNumber">
            {{ personalInfo.stuNumber }}
          </p>
          <p class="headerInfo__account ellipsis" :title="personalInfo.name">
            <img alt="图像" :src="defaultSrc" width="14" /> {{ personalInfo.name }}
          </p>
          </div>
          <Menu :menus="menus" type="expand" :activeMenuId="activeMenuId" />
        </ContainerScroll>
      </div>
      <div class="fold-box" :style="!isCollapse?headerInfoStyle:''">
        <ContainerScroll :maxHeight="scrollHeight" :barKeepShow="true" :size="4">
          <div class="headerInfo__imgWrap">
            <div class="headerInfo__img" :style="`background-image: url(${headImg})`" />
            <!-- <Set type="fold" :currentUser="currentUser" :pageData="pageData" /> -->
          </div>
          <Menu :menus="menus" type="fold" :activeMenuId="activeMenuId" />
        </ContainerScroll>
      </div>

    </div>

    <div class="headerInfo__btn">
      <span class="box" @click="toggleCollapse"><img :src="isCollapse?putImg:openImg" alt="图片" /></span>
    </div>

  </div>
</template>

<script>

import defaultImg from "../../assets/images/defaultAvatar.png";
import Menu from './menu'
// import Set from './set'
export default {
  props: ["leftConfig", "pageData"],
  components: {
    Menu,
    // Set
  },
  data () {
    return {
      personalInfo: {},
      loading: false,
      openImg: require("../../assets/images/open.png"),
      putImg: require("../../assets/images/put.png"),
      leftImg: require('../../assets/images/decorate.png'),
      defaultSrc: require('../../assets/images/man.png'),
      isCollapse: true,
      currentUser: window.shell.getUserInfo(),
    };
  },
  computed: {
    scrollHeight () {
      return (document.documentElement.clientHeight - 56 - 76)
    },
    headerImgStyle () {
      return `background: #ffffff url(${this.leftImg})no-repeat; background-size: contain;`

    },
    headerInfoStyle () {
      let h = document.documentElement.clientHeight - 56 - 76
      return `height:${h}px;overflow-y:hidden;overflow-x:hidden;`
    },
    activeMenuId () {
      return (this.pageData && this.pageData.activeMenuId) || "";
    },
    menus () {
      return (this.pageData && this.pageData.treeMenu) || [];
    },
    headImg () {
      const userIcon = this.personalInfo.picUrl;
      return userIcon
        ? /^http(s)?:\/\//.test(userIcon)
          ? userIcon
          : `data:image/png;base64,${userIcon}`
        : defaultImg;
    },
  },
  created () {
    let _t = this
    this.getPersonInfo();
    this.$nextTick(() => {
      this.isCollapse = this.leftConfig.leftMenuAutoOpen == "0" ? true : false
      _t.loading = true

    })

  },
  beforeDestroy () {
  },
  methods: {
    handleError (e) {
      let img = e.srcElement;
      img.src = defaultImg;
      img.onerror = null; //防止闪图
    },
    getPersonInfo () {
      window.shell.execTemplateMethod(
        "getPersonalData",
        {
          lang: this.$i18n.locale || "zh_CN",
          personalWid: this.leftConfig.dataSourceConfig,
        },
        (res) => {
          if (res.data) {
            this.personalInfo = res.data || {};
          }
        }
      );
    },

    toggleCollapse () {
      this.isCollapse = !this.isCollapse
    },
  },

};
</script>

<style scoped lang="less">
.headerInfo {
  text-align: center;
  // margin-bottom: 12px;
  transition: all 0.3s;

  &.fold {
    // padding-top: 16px;
    width: 68px;
    background: #ffffff;
    transform: translateX(0);
    .headerInfo__imgWrap {
      width: 44px;
      height: 44px;
      border-radius: 49px;
      background: #fff;
      // box-shadow: 0px 2px 16px 0px #155b9a66;
      border: 1px solid #eff3fa;
      margin: 0 auto 0;
      padding: 4px;
      text-align: center;
      margin-top: 16px;
    }
    .expand-box {
      height: 0;
      opacity: 0;
      transition-duration: 0s;
      transition-property: opacity;
    }
    .fold-box {
      display: block;
      transition-duration: 2s;
      transition-property: opacity;
    }
  }
  &.expand {
    // padding-top: 24px;
    width: 176px;
    background: #ffffff;
    transform: translateX(0);
    .headerInfo__imgWrap {
      width: 76px;
      height: 76px;
      border-radius: 49px;
      background: #fff;
      // box-shadow: 0px 2px 16px 0px #155b9a66;
      border: 1px solid #eff3fa;
      margin: 0 auto 12px;
      padding: 4px;
      text-align: center;
    }
    .headerInfo__imgBox {
      width: 116px;
      height: 78px;
      margin: 0 auto;
      padding-top: 2px;
      margin-bottom: 10px;
      margin-top: 24px;
    }
    /deep/.el-menu {
      width: 138px;
    }
    /deep/.el-menu-item.is-active {
      border-radius: 30px;
    }
    .fold-box {
      display: none;
      transition-duration: 1s;
      transition-property: opacity;
    }
    .expand-box {
      // height: 100%;
      height: calc(100vh - 133px) !important;
      opacity: 1;
      transition-duration: 2s;
      transition-property: opacity;
    }
  }
}
.headerInfo__img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}
.headerInfo__title {
  // font-weight: bold;
  font-size: 16px;
  line-height: 24px;
  margin-bottom: 4px;
  width:calc(100% - 32px);
  margin:0 auto;
}
.headerInfo__account {
  width:calc(100% - 34px);
  margin:0 auto;
  font-size: 14px;
  line-height: 22px;
  margin-bottom: 2px;
  color: #595959;
  img {
    vertical-align: middle;
  }
}
.headerInfo__org {
  font-size: 12px;
  line-height: 20px;
}
/deep/.we-loading-mask {
  background-color: transparent;
}
.headerInfo__btn {
  padding-top: 20px;
  height: 76px;
  position: absolute;
  width: 100%;
  display: flex;
  justify-content: center;
  .box {
    width: 36px;
    height: 36px;
    background: #f5f5f5;
    display: block;
    line-height: 46px;
    text-align: center;
    border-radius: 21px;
    cursor: pointer;
  }
  &:before {
    content: "";
    width: 100%;
    height: 40px;
    background: linear-gradient(
      rgba(255, 255, 255, 0.1),
      rgba(255, 255, 255, 1)
    );
    pointer-events: none;
    position: absolute;
    top: -40px;
  }
}
</style>
