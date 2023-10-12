<template>
  <div class="portal-font-color-lv1">
    <div class="headerInfo">
      <div class="headerInfo__imgWrap">
        <div
          class="headerInfo__img"
          :style="`background-image: url(${headImg})`"
        />
      </div>
      <p class="headerInfo__title ellipsis">
        {{ personalInfo.name }}
      </p>
      <p class="headerInfo__account ellipsis">
        {{ personalInfo.stuNumber }}
      </p>
      <p class="headerInfo__org ellipsis portal-font-color-lv3">
        {{
          personalInfo.orgName && personalInfo.orgName.replaceAll(",", " | ")
        }}
      </p>
    </div>
    <div style="min-height: 80px">
      <PersonalApp
        :personalDataConfig="leftConfig.personalDataConfig.join(',')"
      ></PersonalApp>
      <ServiceBus :lists="serviceLists" v-loading="loading"></ServiceBus>
    </div>
  </div>
</template>

<script>
import PersonalApp from "./personalApp";
import ServiceBus from "./serviceBus";
import defaultImg from "../../assets/images/defaultAvatar.png";
export default {
  props: ["leftConfig"],
  components: {
    PersonalApp,
    ServiceBus,
  },
  data() {
    return {
      personLists: [],
      serviceLists: [],
      personalInfo: {},
      loading: false,
    };
  },
  computed: {
    headImg() {
      const userIcon = this.personalInfo.picUrl;
      return userIcon
        ? /^http(s)?:\/\//.test(userIcon)
          ? userIcon
          : `data:image/png;base64,${userIcon}`
        : defaultImg;
    },
  },
  created() {
    this.getLists();
    this.getPersonInfo();
    this.leftConfig.displayAreaConfig.type === 1 &&
      window.shell.on("collectApp", this.getLists);
  },
  beforeDestroy() {
    window.shell.off("collectApp");
  },
  methods: {
    handleError(e) {
      let img = e.srcElement;
      img.src = defaultImg;
      img.onerror = null; //防止闪图
    },
    getPersonInfo() {
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
    getLists() {
      this.loading = true;
      window.shell.execTemplateMethod(
        "getWorkConfigData",
        {
          lang: this.$i18n.locale || "zh_CN",
          personalDataConfig: this.leftConfig.personalDataConfig.join(","),
          type: this.leftConfig.displayAreaConfig.type,
          value: this.leftConfig.displayAreaConfig.value,
        },
        (res) => {
          this.loading = false;
          if (res.data) {
            this.personLists = res.data.personalDataEntityList || [];
            this.serviceLists =
              res.data.latestServiceList ||
              res.data.favoriteServiceList ||
              res.data.classifyServiceList ||
              [];
          }
        }
      );
    },
  },
};
</script>

<style scoped>
.headerInfo {
  text-align: center;
  margin-bottom: 12px;
}
.headerInfo__imgWrap {
  width: 98px;
  height: 98px;
  border-radius: 49px;
  background: #fff;
  box-shadow: 0px 2px 16px 0px #155b9a66;
  margin: 0 auto 12px;
  padding: 4px;
  text-align: center;
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
  font-weight: bold;
  font-size: 18px;
  line-height: 26px;
  margin-bottom: 4px;
}
.headerInfo__account {
  font-size: 14px;
  line-height: 22px;
  margin-bottom: 2px;
}
.headerInfo__org {
  font-size: 12px;
  line-height: 20px;
}
/deep/.we-loading-mask {
  background-color: transparent;
}
</style>
