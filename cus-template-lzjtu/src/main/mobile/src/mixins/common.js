import { DEV_URL } from "../../env";
export default {
    computed: {
        logoImgSrc() {
          return this.pageData
            ? JSON.parse(
                JSON.parse(this.pageData.showProgrammeEntity.templateConfig || "")
              ).configLogo
            : {};
        },
        logoUrl() {
          const sColor = (this.logoImgSrc && this.logoImgSrc.logoUrl) || "";
          const bColor = (this.logoImgSrc && this.logoImgSrc.whiteLogoUrl) || "";
          return this.pageCode === "blank" ? sColor : bColor;
        },
        colorLogoUrl() {
          const sColor = (this.logoImgSrc && this.logoImgSrc.logoUrl) || "";
          return sColor;
        },
        messageUrl(){
          return  this.pageData
          ? JSON.parse(
              JSON.parse(this.pageData.showProgrammeEntity.templateConfig || "")
            ).messageUrl
          :'';
        },
        pageRenderData() {
          return (this.pageData && this.pageData.renderData) || {};
        },
        langTitle() {
          const arr =
            (this.pageRenderData.pageConfig &&
              this.pageRenderData.pageConfig["header.titleLangs"]) ||
            [];
          const temp = arr.find((el) => el.key === this.$i18n.locale);
          const zhName = arr.find((el) => el.key === 'zh_CN')
          return temp && temp.value || zhName?.value;
        },
        langSubTitle() {
          const arr = this.pageRenderData && this.pageRenderData.pageConfig['header.subTitleLangs'] || []
          const temp = arr.find(el => el.key === this.$i18n.locale)
          const zhName = arr.find((el) => el.key === 'zh_CN')
          return temp && temp.value || zhName?.value;
        },
        headerImg() {
          return this.pageRenderData.pageConfig &&this.pageRenderData.pageConfig["header.imgUrl"] || "";
        },
        headerStyle() {
          const isHttp = /^http(s)?:\/\//.test(this.headerImg);
          const relUrl = isHttp
            ? this.headerImg
            : process.env.NODE_ENV === "development"
            ? `${DEV_URL}${this.headerImg}`
            : this.headerImg;
          return {
            background: `url(${relUrl}) no-repeat center`,
            backgroundSize: '100%'
          };
        },
        // cardArea() {
        //   return this.pageRenderData.pageConfig["card.area"];
        // },
        cardLayout() {
          return JSON.parse(
            (this.pageData &&
              this.pageData.pageInfoEntity &&
              this.pageData.pageInfoEntity.cardLayout) ||
              "[]"
          );
        },
        currentUser() {
          return window.shell.getUserInfo()
        },
        currentSite() {
          return window.shell.getCurrentSite()
        }
        // placeholder() {
        //   return this.pageRenderData.pageConfig && this.pageRenderData.pageConfig["search.placeholder"] || ""
        // },
        // keyword() {
        //   return "" || this.placeholder 
        // },
      },
    methods: {

    }
}