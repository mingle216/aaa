<template>
  <div class="container">
    <div class="home" :style="headerStyle">
      <!-- header -->
      <header-bar :pageData="pageData" />
      <search-bar
        pageCode="home"
        :allShowSearch="true"
        :pageRenderData="pageRenderData"
      />
      <!-- body -->
      <div
        class="content__wrapper"
        :class="[leftAbsolute ? 'content__wrapper-absolute' : '']"
      >
        <div
          class="content__left"
          ref="LeftContent"
          :style="leftStyle"
          v-if="currentUser"
        >
          <LeftInfo
            :currentUser="currentUser"
            :leftConfig="leftConfig"
          ></LeftInfo>
        </div>
        <we-container class="content__right" ref="RightContent">
          <we-main style="overflow: hidden;padding: 0">
            <card-layout :cardLayout="cardLayout"></card-layout>
          </we-main>
        </we-container>
      </div>
      <we-footer
        class="home__footer"
        style="height: auto"
        v-html="footer"
      ></we-footer>
    </div>
  </div>
</template>

<script>
/* eslint-disable no-debugger */
import HeaderBar from "../../components/headerBar/index";
import SearchBar from "../../components/searchBar/index";
import CardLayout from "../cardLayout";
import LeftInfo from "./leftInfo";
import { DEV_URL } from "../../../env";
export default {
  name: "Home",
  components: {
    HeaderBar,
    SearchBar,
    CardLayout,
    LeftInfo,
  },
  props: {
    pageData: Object,
  },
  data() {
    return {
      currentUser: window.shell.getUserInfo(),
      leftAbsolute: false,
      leftStyle: "",
    };
  },
  computed: {
    pageRenderData() {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData && this.pageData.pageInfoEntity.wid,
      };
    },
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
        const zh = (footerConfig || []).find((el) => el.langKey === "zh_CN");
        return temp?.langValue || zh?.langValue;
      } else {
        return footerConfig;
      }
    },
    cardArea() {
      return (
        this.pageData &&
        this.pageData.renderData &&
        this.pageData.renderData.pageConfig["card.area"]
      );
    },
    headerStyle() {
      const imgUrl =
        (this.pageData.renderData.pageConfig &&
          this.pageData.renderData.pageConfig["header.imgUrl"]) ||
        "";
      const isHttp = /^http(s)?:\/\//.test(imgUrl);
      const relUrl = isHttp
        ? imgUrl
        : process.env.NODE_ENV === "development"
        ? `${DEV_URL}${imgUrl}`
        : imgUrl;
      return {
        background: `url(${relUrl}) no-repeat center top/auto 262px`,
      };
    },
    leftConfig() {
      const templateConfig = this.pageData
        ? JSON.parse(
            JSON.parse(this.pageData.showProgrammeEntity.templateConfig || "")
          )
        : {};
      return {
        dataSourceConfig: templateConfig.dataSourceConfig || "",
        displayAreaConfig: templateConfig.displayAreaConfig || {},
        personalDataConfig: templateConfig.personalDataConfig || [],
      };
    },
  },
  methods: {
    handleScroll(e) {
      const ele = this.$refs.LeftContent;
      const rightEle = this.$refs.RightContent;
      if (ele) {
        const eleHeight = ele.clientHeight; // 元素高度
        if (
          rightEle === undefined ||
          rightEle.$el.scrollHeight <= ele.scrollHeight
        ) {
          //右侧内容高度少于左侧高度时，不改变布局
          this.leftAbsolute = false;
          this.leftStyle = "";
          return;
        }
        const wrapHeight = document.body.clientHeight; //当前窗口可视区域的高度
        const flag =
          eleHeight < wrapHeight
            ? e.scrollTop > 300
            : e.scrollTop > eleHeight - wrapHeight + 180; // 左侧内容一屏展示不下时，滚动条滚动到距离元素底部20px时改变位置；否则滚动300px就改变位置
        if (flag) {
          this.leftAbsolute = true;
          this.leftStyle =
            eleHeight < wrapHeight ? "top: 20px" : "bottom: 20px";
        } else {
          this.leftAbsolute = false;
          this.leftStyle = "";
        }
      }
    },
  },
  created() {
    window.shell.on("update-login", (data) => {
      this.currentUser = data;
    });
    window.shell.on("onScoll", this.handleScroll);
  },
  beforeDestroy() {
    window.shell.off("update-login");
    window.shell.off("onScoll");
  },
};
</script>

<style lang="less" scoped>
.container {
  display: flex;
  background: #eff3fa;
  .home {
    min-height: 100vh;
    width: 100%;
    flex-direction: column;
    display: flex;
    justify-content: space-between;
    .content__wrapper {
      margin-top: 24px;
      padding: 0 24px;
      display: flex;
      flex-grow: 1;
      /deep/.gateway-card {
        border-radius: 16px;
        padding: 0 24px 24px;
        &.calenderCard {
          padding: 0;
          border-radius: 8px;
        }
      }
      .content__right {
        flex: 1;
        min-width: 0;
        width: 0;
      }
    }
    .content__wrapper-absolute {
      .content__left {
        position: fixed;
        left: 24px;
      }
      .content__right {
        margin-left: 312px;
      }
    }
    .content__left {
      flex-shrink: 0;
      width: 272px;
      margin: 0 32px 0 8px;
      align-self: flex-start;
    }
    @media screen and (max-width: 1281px) {
      .content__wrapper-absolute {
        .content__left {
          left: 24px;
        }
        .content__right {
          margin-left: 204px;
        }
      }
      .content__left {
        width: 180px;
        margin: 0 24px 0 0;
      }
    }
    .home__footer {
      padding: 0;
    }
    // 事项统计卡片宽度固定1200px，不知道什么原因，work模板暂时取消这一限制
    /deep/.item_count_wrap {
      width: 100%;
    }
  }
}
</style>
