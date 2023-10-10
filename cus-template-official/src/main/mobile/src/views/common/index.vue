 <template>
  <div class="container">
      <!-- :style="headerStyle" -->
    <div class="common-head">
        <we-swipe class="my-swipe" :autoplay="6000" indicator-color="white" :show-indicators='false'>
            <we-swipe-item><img src="../dzimages/1.jpg"/></we-swipe-item>
            <we-swipe-item><img src="../dzimages/2.jpg"/></we-swipe-item>
            <we-swipe-item><img src="../dzimages/3.jpg"/></we-swipe-item>
          </we-swipe>
      <div class="head-title">
        <img :src="logoUrl" height="50" />
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
            {{ $Lan('SYS_TEMPLATE_OFFICIAL_h5', 'login', '请登录') }}
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
          {{ $Lan('SYS_TEMPLATE_OFFICIAL_h5', 'login', '请登录') }}
        </span>
      </div>
    </div>

    <div class="common-content" ref="commonContent">
      <card-layout :cardLayout="cardLayout" />
    </div>
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
    };
  },
  computed: {
    // currentUser() {
    //   return this.pageData && this.pageData.currentUser;
    // },
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
  
.common-head {
 
  height: 180px;
  .we-swipe{
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    .we-swipe__track{
    width: 100%;
    height: 100%;

    }
  }
  .head-title {
    display: flex;
    align-items: center;
    height: 48px;
    justify-content: space-between;
    //box-shadow: 1px 1px 1px rgba(255, 255, 255, 0.3);
    border-bottom: 1px solid rgba(255,255,255,0.1);
    overflow: hidden;
    .head-isNotLogin {
      margin-right: 17px;
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
    margin-left: 17px;
    width: 120px;
  }
  i {
    font-size: 22px;
    margin-right: 17px;
    color: #102645;
  }
  .head-isNotLogin {
    margin-right: 17px;
    display: flex;
    align-items: center;
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
</style>
