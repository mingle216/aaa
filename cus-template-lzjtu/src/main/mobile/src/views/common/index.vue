 <template>
  <div class="container">
    <div class="common-head" :style="headerStyle">
      <div class="head-title">
        <img :src="logoUrl" height="50" />
        <div class="site-switch" @click="handleChangeSite" v-if="siteList.length > 1">
          <span class="ellipsis">{{ currentSite && currentSite.siteName }}</span>
          <we-icon name="arrow-down" size="12px"/>
        </div>
        <div v-if="currentUser">
          <i
            class="iconfont icon-menu-MyMessage"
            v-if="messageUrl"
            @click="handleMessage"
          ></i>
          <i class="iconfont icon-UserName" @click="handlePersonal"></i>
        </div>
        <div v-if="!currentUser" class="head-isNotLogin">
          <span @click="handleLogin">
            {{ $Lan('CUS_TEMPLATE_LZJTU_h5', 'login', '请登录') }}
          </span>
        </div>
      </div>
      <div class="text__wrap">
        <p class="title text__ellipsis">
          <span>
            {{ langTitle }}
          </span>
        </p>
        <p class="sub__title text__ellipsis">
          <span>
            {{ langSubTitle }}
          </span>
        </p>
      </div>
    </div>

    <div class="head-title-top" v-show="isFixed">
      <img :src="colorLogoUrl" height="50" />
      <div class="site-switch-top" @click="handleChangeSite" v-if="siteList.length > 1">
        <span class="ellipsis">{{ currentSite && currentSite.siteName }}</span>
        <we-icon name="arrow-down" size="12px"/>
      </div>
      <div v-if="currentUser">
        <i
          class="iconfont icon-menu-MyMessage"
          v-if="messageUrl"
          @click="handleMessage"
        ></i>
        <i class="iconfont icon-UserName" @click="handlePersonal"></i>
      </div>
      <div v-if="!currentUser" class="head-isNotLogin">
        <span @click="handleLogin">
          {{ $Lan('CUS_TEMPLATE_LZJTU_h5', 'login', '请登录') }}
        </span>
      </div>
    </div>

    <div class="common-content" ref="commonContent">
      <card-layout :cardLayout="cardLayout" />
    </div>
    <customActionSheet
      v-model="siteVisible"
      :actions="siteList"
      close-on-click-action
      @select="switchSite"
      :duration="0"
      style="height: initial"
    />
  </div>
</template>

<script>
import common from "@/mixins/common";
import CardLayout from "../cardLayout";
// import MainContainer from "../../components/MainContainer";
export default {
  mixins: [common],
  components: {
    CardLayout,
    // MainContainer,
  },
  props: {
    pageData: Object,
  },
  data() {
    return {
      scrollToTop: false,
      scrollTop: 0,
      isFixed: false,
      siteVisible: false
    };
  },
  computed: {
    siteList() {
      let list = window.shell.getSiteList() || []
      return list.map(el => ({
        ...el,
        name: el.siteName
      }))
    }
  },
  methods: {
    handleScroll() {
      if (
        this.$refs.commonContent &&
        this.$refs.commonContent.getBoundingClientRect().top < 0
      ) {
        this.scrollToTop = true;
      } else {
        this.scrollToTop = false;
      }
    },
    handleMessage() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName
        },
        startTime: new Date().getTime()
      });
      // window.open(this.messageUrl, "_blank");
      window.shell.openPage(this.messageUrl, 1, 2);
    },
    handleLogin() {
      const location = window.shell.getLocation();
      const page = location.hash.replace("#", "");
      window.shell.login({
        params: {
          localHref: page,
        },
      });
    },
    handleSearch() {
      window.shell.openPage(`result?searchKey=&placeholder=`, 0, 1);
    },
    handlePersonal() {
      window.shell.openPage(`personal`, 0, 1);
    },
    handleChangeSite() {
      this.siteVisible = true
    },
    switchSite(item) {
      window.shell.changeSite(item.siteRoute);
    },
  },
  watch: {
    scrollTop(val) {
      this.isFixed = val >= 180;
    },
  },
  mounted() {
    // window.addEventListener("scroll", this.handleScroll, true);
    window.shell.on("getScrollTop", (it) => {
      this.scrollTop = it;
    });
  },
  beforeDestroy() {
    window.shell.off("getScrollTop");
  },
};
</script>

<style lang="less" scoped>
.ellipsis {
  display: inline-block;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.common-head {
  height: 180px;
  .head-title {
    display: flex;
    align-items: center;
    height: 48px;
    justify-content: space-between;
    //box-shadow: 1px 1px 1px rgba(255, 255, 255, 0.3);
    border-bottom: 1px solid rgba(255,255,255,0.1);
    overflow: hidden;
    .site-switch {
      padding-left: 40px;
      color: #fff;
      display: flex;
      align-items: center;
      .ellipsis {
        padding-right: 5px;
      }
    }
    .head-isNotLogin {
      margin-right: 17px;
      display: flex;
      justify-content: space-between;
      span {
        // font-family: PingFangSC-Regular;
        font-size: 14px;
        color: #ffffff;
        letter-spacing: 0;
        text-align: justify;
        line-height: 18px;
      }
    }
    img {
      height: auto;
      max-width: 100%;
      max-height: 100%;
      margin-left: 17px;
      width: 120px;
    }
    i {
      font-size: 22px;
      margin-right: 17px;
      color: #ffffff;
    }
  }
  .text__wrap {
    display: flex;
    flex-direction: column;
    margin: 36px 17px 8px 17px;
    .title {
      // font-family: PingFangSC-Semibold;
      font-size: 26px;
      color: #ffffff;
      letter-spacing: 0;
      text-align: justify;
      line-height: 32px;
      font-weight: bold;
    }
    .sub__title {
      opacity: 0.8;
      // font-family: PingFangSC-Regular;
      font-size: 13px;
      color: #ffffff;
      letter-spacing: 0;
      text-align: justify;
      line-height: 17px;
      margin-top: 9px;
    }
  }
}
.head-title-top {
  display: flex;
  align-items: center;
  height: 48px;
  justify-content: space-between;
  border-bottom: 1px solid #dcdfe6;
  //position: sticky;
  position: fixed;
  width: 100%;
  top: 0px;
  background: #ffffff;
  box-shadow: 0 4px 12px 0 rgba(112, 125, 143, 0.2);
  z-index: 999;
  overflow: hidden;
  img {
    height: auto;
    max-width: 100%;
    max-height: 100%;
    margin-left: 17px;
    width: 120px;
  }
  i {
    font-size: 22px;
    margin-right: 17px;
    color: #102645;
  }
  .site-switch-top {
    padding-left: 40px;
    display: flex;
    align-items: center;
    .ellipsis {
      padding-right: 5px;
    }
  }
  .head-isNotLogin {
    margin-right: 17px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    span {
      // font-family: PingFangSC-Regular;
      font-size: 14px;
      color: #102645;
      letter-spacing: 0;
      text-align: justify;
      line-height: 18px;
    }
  }
}
.common-content {
  padding: 17px 0;
  width: 100%;
}
/deep/.we-action-sheet__name {
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  display: inline-block;
}
</style>
