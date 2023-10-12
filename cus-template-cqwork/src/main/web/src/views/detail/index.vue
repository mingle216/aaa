<template>
  <div class="container">
    <div class="detail" :style="headerStyle">
      <!-- header -->
      <header-bar :pageData="pageData" />
      <!-- body -->
      <div class="content__wrapper">
        <we-container class="detailWrap">
          <we-main style="overflow: hidden;padding: 0">
            <card-layout :cardLayout="cardLayout"></card-layout>
          </we-main>
        </we-container>
      </div>
      <we-footer
        class="detail__footer"
        style="height: auto"
        v-html="footer"
      ></we-footer>
    </div>
  </div>
</template>

<script>
import HeaderBar from "../../components/headerBar/index";
import CardLayout from "../cardLayout";
import { DEV_URL } from "../../../env";
export default {
  components: {
    HeaderBar,
    CardLayout,
  },
  props: {
    pageData: Object,
  },
  computed: {
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
  },
};
</script>

<style lang="less" scoped>
.container {
  display: flex;
  background: #eff3fa;
  .detail {
    min-height: 100vh;
    width: 100%;
    .content__wrapper {
      margin: 40px 24px 0;
      padding: 24px;
      border-radius: 16px;
      background: #ffffff;
      display: flex;
      min-height: calc(100vh - 150px);
    }
    .detail__footer {
      padding: 0;
    }
  }
}
</style>
