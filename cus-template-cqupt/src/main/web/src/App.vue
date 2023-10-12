<template>
  <div id="app" ref="appCon">
    <vue-scroll
      ref="vs"
      :barKeepShow="true"
      @vshandle-scroll="handleScroll"
      :scrollingX="true"
      :scrollCover="true"
      :nativeModel="true"
      @handle-scroll-complete="handleScrollComplete"
      v-if="templateCode!='screen'"
    >
      <component
        :id="`template_${templateCode}`"
        :is="comp"
        :pageData="pageData"
        class="tempalteConWrap"
        style="min-width: 1280px"
      />
    </vue-scroll>
    <component
        :id="`template_${templateCode}`"
        :is="comp"
        :pageData="pageData"
        class="tempalteConWrap"
        style="min-width: 1280px"
        v-else
      />
    <!-- 回到顶部 -->
    <back-top v-show="showBackTop" @handle-back="handleBack" />
    <service-item-modal ref="ServiceItemModal" />
    <safariOpenServeDialog />
  </div>
</template>

<script>
import Home from "./views/home";
import Common from "./views/common";
import Detail from "./views/detail";
import Search from "./views/search";
import BackTop from "./components/BackTop";
import ServiceItemModal from "./components/serviceItemModal";
import themeMap from "./utils/weTheme";
import ErrorImg from "./assets/images/icon-err.png";
import safariOpenServeDialog from "./components/safariOpenServeDialog";
import Screen from "./views/screen";
const ROUTER_MAP = {
  home: Home,
  common: Common,
  detail: Detail,
  search: Search,
  screen: Screen
};

export default {
  name: "App",
  components: {
    Home,
    BackTop,
    ServiceItemModal,
    safariOpenServeDialog,
  },
  data: () => ({
    // 展示卡片配置列表
    pageData: null,
    showBackTop: false,
  }),
  computed: {
    templateCode() {
      return (
        this.pageData &&
        this.pageData.pageInfoEntity &&
        this.pageData.pageInfoEntity.templateCode
      );
    },
    comp() {
      return ROUTER_MAP[this.templateCode];
    },
  },
  async created() {
    let that = this;
    await window.shell.getLanguageList(this.$i18n.locale, (res) => {
      if (res.errcode === "0") {
        this.$i18n.mergeLocaleMessage(this.$i18n.locale, res.data);
      }
    });
    window.shell.getScrollInfo = function() {
      return that.$refs["vs"].getPosition();
    };
    window.shell.on("page-change", () => {
      this.update();
    });
    window.shell.on("show-message", (option) => {
      // console.log(option);
      this.$message(option);
    });
    window.shell.on("show-service-item-modal", (serviceItem) => {
      this.$refs.ServiceItemModal.show(serviceItem);
    });
    window.shell.on("vs-scroll-to", (params, speed) => {
      this.$refs.vs.scrollTo(params, speed);
    });
    window.shell.on("show-online-deal", (serviceItem, targetEle) => {
      this.$refs.ServiceItemModal.show(serviceItem, true, targetEle);
    });
    window.shell.on("handle-back-top", () => {
      const dom = document.querySelector(`#template_${this.templateCode}`);
      dom.scrollIntoView();
    });

    if (window.shell.getPageLoading) {
      this.update();
    }
    // 图片加载失败占位图
    if (!window.shell.ErrorImg) {
      window.shell.ErrorImg = ErrorImg;
    }
    window.shell.setThemeColor(document, this.pageData);
    window.shell.setLocalStyle(document, this.pageData.showProgrammeEntity.wid);
    this.setThirdThemeColor();
  },
  mounted() {
    window.shell.loadIconfont(document)
  },
  beforeDestroy() {
    window.shell.off("page-change");
    window.shell.off("show-message");
    window.shell.off("show-service-item-modal");
    window.shell.off("show-online-deal");
    window.shell.off("handle-back-top");
  },
  methods: {
    handleScrollComplete() {
      window.shell.emit("page-scroll-complete");
    },
    handleScroll(vertical) {
      // console.log(ev)
      window.shell.emit("onScoll", vertical);
      if (vertical.scrollTop > 100) {
        this.showBackTop = true;
      } else {
        this.showBackTop = false;
      }
      window.shell.emit("card-scroll", vertical);
    },
    handleBack() {
      // const dom = document.querySelector(`#template_${this.templateCode}`);
      // dom.scrollIntoView({ behavior: "smooth" });
      this.$refs.vs.scrollTo({ y: 0 }, 500);
    },
    update() {
      this.pageData = JSON.parse(JSON.stringify(window.shell.getPageData()));
      // 防止覆盖事项详情浏览器title
      // console.log(this.pageData.pageInfoEntity.pageCode)
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
  },
};
</script>

<style scoped>
#app {
  height: 100%;
  width: 100%;
  /* overflow: hidden; */
}
.tempalteConWrap {
   height: 100%;
}
</style>

<style>
html,
body {
  height: 100%;
  padding: 0;
  margin: 0;
}
</style>
