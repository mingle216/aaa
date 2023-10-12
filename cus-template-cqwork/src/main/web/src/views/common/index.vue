<template>
  <div class="container">
    <div class="common" :style="headerStyle">
      <header-bar :pageData="pageData" />
      <div class="section__wrap" v-if="langTitle || langSubTitle">
        <!-- 标题栏 -->
        <p class="title ellipsis" v-if="langTitle">
          {{ langTitle }}
        </p>
        <p
          class="sub__title ellipsis"
          v-if="langSubTitle"
          :style="`marginTop: ${langTitle ? 20 : 0}px;`"
        >
          {{ langSubTitle }}
        </p>
      </div>
      <!-- body -->
      <div class="content__wrapper">
        <!-- 面包屑 -->
        <bread-crumb :pageRenderData="pageRenderData"></bread-crumb>
        <we-container class="commonHall">
          <we-main style="overflow: hidden;padding: 0">
            <card-layout :cardLayout="cardLayout"></card-layout>
          </we-main>
        </we-container>
      </div>
      <we-footer class="common__footer" style="height: auto" v-html="footer" />
    </div>
  </div>
</template>

<script>
import HeaderBar from "../../components/headerBar/index";
import CardLayout from "../cardLayout";
import BreadCrumb from "../../components/BreadCrumb";
import { DEV_URL } from "../../../env";
export default {
  components: {
    HeaderBar,
    CardLayout,
    BreadCrumb,
  },
  props: {
    pageData: Object,
  },
  data() {
    return {
      currentUser: window.shell.getUserInfo(),
    };
  },
  computed: {
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
        const zh = (footerConfig || []).find((el) => el.langKey === "zh_CN");
        return temp?.langValue || zh?.langValue;
      } else {
        return footerConfig;
      }
    },
    cardLayout() {
      return JSON.parse(
        (this.pageData &&
          this.pageData.pageInfoEntity &&
          this.pageData.pageInfoEntity.cardLayout) ||
          "[]"
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
    cardArea() {
      return this.pageRenderData.pageConfig["card.area"];
    },
    langTitle() {
      const arr =
        (this.pageRenderData &&
          this.pageRenderData.pageConfig["header.titleLangs"]) ||
        [];
      const temp = arr.find((el) => el.key === this.$i18n.locale);
      const zhName = arr.find((el) => el.key === "zh_CN");
      return temp?.value || zhName?.value;
    },
    langSubTitle() {
      const arr =
        (this.pageRenderData &&
          this.pageRenderData.pageConfig["header.subTitleLangs"]) ||
        [];
      const temp = arr.find((el) => el.key === this.$i18n.locale);
      const zhName = arr.find((el) => el.key === "zh_CN");
      return temp?.value || zhName?.value;
    },
  },
  created() {
    window.shell.on("update-login", (data) => {
      this.currentUser = data;
    });
  },
  beforeDestroy() {
    window.shell.off("update-login");
  },
};
</script>

<style lang="less" scoped>
.container {
  display: flex;
  background: #eff3fa;
  .common {
    min-height: 100vh;
    width: 100%;
    flex-direction: column;
    display: flex;
    justify-content: space-between;
    .section__wrap {
      margin-top: 40px;
      color: #fff;
      padding: 0 24px;
      .title {
        font-weight: bold;
        font-size: 36px;
        line-height: 44px;
        text-align: center;
      }
      .sub__title {
        opacity: 0.8;
        font-size: 16px;
        line-height: 24px;
        text-align: center;
      }
    }
    .content__wrapper {
      margin: 40px 24px 0;
      padding: 24px;
      border-radius: 16px;
      background: #ffffff;
      flex-grow: 1;
    }
    .common__footer {
      padding: 0;
    }
  }
}
</style>
