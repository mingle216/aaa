<template>
  <div class="page-main">
    <div id="app" class="page-con" ref="app">
      <div class="card-view" ref="subView" @scroll="handleScroll">
        <component
          :id="`template_${templateCode}`"
          :is="comp"
          :pageData="pageData"
          :showFooter="showFooter"
          class="view-con"
        />
      </div>
      <!-- <button @click="()=>{loading=!loading}">loading{{loading}}</button> -->
      <menus
        class="menu-con"
        v-if="showFooter"
        :active="avtiveMenu"
        :menus="treeMenu"
      ></menus>
      <service-item-modal ref="ServiceItemModal" :pcServiceType="pcServiceType"/>
      <choose-group-modal ref="ChooseGroupModal" />
    </div>
    <div class="page-loading" v-if="pageLoading">
      <we-loading type="spinner" size="42px" />
    </div>
  </div>
</template>

<script>
import Home from "./views/home";
import Blank from "./views/blank";
import Config from "./views/config";
import Common from "./views/common";
import Detail from "./views/detail";
import Search from "./views/search";
import Personal from "./views/personal";

import themeMap from "./utils/weTheme";
import ErrorImg from "./assets/images/icon-err.png";
import menus from "./components/FooterMenu";
import ServiceItemModal from "./components/ServiceItemModal";
import ChooseGroupModal from "./components/ChooseGroupModal";
const ROUTER_MAP = {
  home: Home,
  config: Config,
  blank: Blank,
  common: Common,
  detail: Detail,
  search: Search,
  personal: Personal,
};
// const SHOW_FOOTER = ["home", "common", "personal"];
// import VConsole from 'vconsole'
// new VConsole()
export default {
  name: "App",
  components: { Home, Config, menus, ServiceItemModal, ChooseGroupModal },
  data: () => ({
    // 展示卡片配置列表
    pageData: null,
    showBackTop: false,
    // loading:false
    scrollTop: 0,
    pageLoading: false,
    loadingNum: 0,
		pcServiceType: 0,
		noLoadPage:['result','taskList']
  }),
  computed: {
    templateCode() {
      return (
        this.pageData &&
        this.pageData.pageInfoEntity &&
        this.pageData.pageInfoEntity.templateCode
      );
    },
    getCardNum() {
      return (
        JSON.parse(
          (this.pageData &&
            this.pageData.pageInfoEntity &&
            this.pageData.pageInfoEntity.cardLayout) ||
            "[]"
        ).length || 0
      );
    },
    comp() {
      return ROUTER_MAP[this.templateCode];
    },
    avtiveMenu() {
      return (this.pageData && this.pageData.activeMenuId) || "";
    },
    treeMenu() {
      return (this.pageData && this.pageData.treeMenu) || [];
    },
    showFooter() {
      let p = window.shell.getUrlParam();
      const isShowMenu = p && p.isShowMenu && p.isShowMenu == 0 ? false : true;
      console.log(isShowMenu)
      return isShowMenu &&
        this.pageData?.renderData?.pageConfig["bottom.isShowMenu"] === "Y" &&
        this.avtiveMenu
        ? true
        : false;
    },
  },
  mounted() {
    window.shell.loadIconfont(document)
  },
  watch: {
    pageLoading(val) {
      clearTimeout(this.loadingNum);
      if (val) {
        this.loadingNum = setTimeout(() => {
          this.pageLoading=false;
        }, 5000);
      }
    },
  },
  async created() {
    await window.shell.getLanguageList(this.$i18n.locale, (res) => {
      if (res.errcode === "0") {
        this.$i18n.mergeLocaleMessage(this.$i18n.locale, res.data);
      }
    });
    window.shell.on("page-change", () => {
      this.update();
      window.focus();
      this.showHeader = false;
    });
    window.shell.on("show-message", (option) => {
      console.log('option', option)
      let params = {
        message: "消息提示",
        duration: 3000,
        // overlay:true,
        // forbidClick:true,
        position: "top",
      };
      params = Object.assign(
        params,
        typeof option === "string" ? { message: option } : option
      );
      // this.$notify(params);
      this.$toast(params);
    });
    window.shell.on("show-service-item-modal", (serviceItem) => {
      console.log(serviceItem);
      this.$refs.ServiceItemModal.show(serviceItem);
    });
    window.shell.on("show-pc-service-modal", (serviceItem) => {
      this.$refs.ServiceItemModal.showPcModal(serviceItem);
    });
    window.shell.on("show-online-deal", (serviceItem) => {
      this.$refs.ServiceItemModal.show(serviceItem, true);
    });
    window.shell.on("card-loaded", () => {
         this.pageLoading=false;
      
    });
    window.shell.on("close-service-modal", () => {
      this.$refs.ServiceItemModal.close();
    });
    window.shell.on("show-choose-group", (serviceItem) => {
      // this.$refs.ServiceItemModal.close();
      this.$refs.ChooseGroupModal.show(serviceItem);
    });
    if (window.shell.getPageLoading) {
      this.update();
    }
    // 图片加载失败占位图
    if (!window.shell.ErrorImg) {
      window.shell.ErrorImg = ErrorImg;
    }
    window.shell.setThemeColor(document, this.pageData);
    this.setThirdThemeColor();
  },
  beforeDestroy() {
    window.shell.off("page-change");
    window.shell.off("show-message");
    window.shell.off("show-service-item-modal");
    window.shell.off("show-pc-service-modal");
    window.shell.off("show-online-deal");
    window.shell.off("close-service-modal");
    window.shell.off("show-choose-group");
    window.shell.off("fixed-header-on");
    window.shell.off("fixed-header-off");
    window.shell.off("card-loaded");
    // window.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    update() {
      this.pageData = JSON.parse(JSON.stringify(window.shell.getPageData()));
			this.pcServiceType = this.pageData.showProgrammeEntity.isPcServiceSupported
      this.pageLoading = false;
      if(!this.noLoadPage.includes(this.pageData.pageInfoEntity.pageCode)&& this.pageData?.pageInfoEntity?.pageCode){
          this.pageLoading = true;
      }
      // 防止覆盖事项详情浏览器title
      //  console.log(this.pageData.pageInfoEntity.pageCode)
      if (this.pageData.pageInfoEntity.pageCode !== "itemDetail") {
        window.shell.setBroswerTitle(
          this.$t(this.pageData.pageInfoEntity.pageTitleLangKey) || ""
        );
      }
      // console.log("pageData:",this.pageData.pageInfoEntity.pageTitleLangKey)
    },
    setThirdThemeColor() {
      const templateConfig = this.pageData
        ? this.pageData.showProgrammeEntity.templateConfig
        : "";
      const configObj = JSON.parse(JSON.parse(templateConfig || "{}"));
      const themeColor = configObj.themeColorSetting || {};
      const colorMap = {};
      for (let el in themeColor) {
        colorMap[el] = themeColor[el];
      }
      let themeStr = "";
      // 组装成字符串
      for (const key in themeMap) {
        themeStr += `${key}${themeMap[key]}`;
      }
      // 替换颜色值
      Object.keys(colorMap).forEach((key) => {
        themeStr = themeStr.replace(new RegExp(`@${key}`, "ig"), colorMap[key]);
      });
      window.shell.addCSS(document, themeStr);
    },
    handleScroll() {
      this.scrollTop = this.$refs.subView.scrollTop;

      window.shell.emit("getScrollTop", this.scrollTop);
    },
  },
};
</script>

<style  scoped>
html,
body {
  padding: 0;
  margin: 0;
  height: 100vh;
  font-size: 16px;
}
body {
  overflow: scroll;
  /* -webkit-overflow-scrolling: touch; */
}
#app {
  min-height: 100vh;
  width: 375px;
  /* height:calc(100vh + 2px); */
  overflow-x: hidden;
}
</style>
<style lang="less" scoped>
.page-main{
  position: relative;
 .page-loading {
      position: absolute;
      left: 0;
      top: 0;
      right: 0;
      bottom: 0;
      text-align: center;
      background-color: #fff;
      z-index: 10000;
      padding-top:calc(50vh - 20px) ;
    }
}
.page-con {
  height: 100vh;
  box-sizing: border-box;
  overflow-y: auto;
  display: flex;
  flex-flow: column;
  // &.has-footer {
  //   padding-bottom: 80px;
  // }
  .fixed-header {
    width: 100%;
    height: 0;
  }
  .card-view {
    flex: 1;
    height: 0;
    overflow: auto;
    position: relative;
    
  }
  .menu-con {
    height: 56px;
    position: relative !important;
  }
}
</style>

 