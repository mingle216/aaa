 <template>
  <div class="container">
    <div class="common-head" :style="headerStyle" ref="commonHead">
      <div class="head-title">
        <img :src="logoUrl" height="50" />
        <div class="site-switch" @click="handleChangeSite" v-if="siteList.length > 1">
          <span class="ellipsis">{{ currentSite && currentSite.siteName }}</span>
          <we-icon name="arrow-down" size="12px"/>
        </div>
        <div v-if="currentUser">
          <!-- <i class="iconfont icon-duoyuyan" @click="showLangList" v-if="actions.length > 1" /> -->
          <i
            class="iconfont icon-menu-MyMessage"
            v-if="messageUrl"
            @click="handleMessage"
          ></i>
          <i class="iconfont icon-UserName" @click="handlePersonal"></i>
        </div>
        <div v-if="!currentUser" class="head-isNotLogin">
          <span @click="handleLogin">
            {{ $Lan('CUS_TEMPLATE_OFFICIAL_h5', 'login', '请登录') }}
          </span>
        </div>
      </div>
      <div class="text__wrap">
        <p class="title text__ellipsis">
          <span>
            {{ langTitle }}
          </span>
        </p>
        <div class="title-search">
          <we-cell-group class="title_input" @click="inputSearch">
            <we-field
              :placeholder="searchPrompt"
              :readonly="true"
              class="title_search"
              :value="newplaceholder"
            />
          </we-cell-group>
          <we-button
            class="title_input_button portal-primary-backgroundcolor-lv1"
            @click="handleSearch"
            >
              {{ $Lan('CUS_TEMPLATE_OFFICIAL_h5', 'search', '搜索') }}
          </we-button>
        </div>
      </div>
    </div>

    <div class="head-title-top" v-show="isFixed">
      <img :src="colorLogoUrl" height="50" />
      <div class="site-switch-top" @click="handleChangeSite" v-if="siteList.length > 1">
        <span class="ellipsis">{{ currentSite && currentSite.siteName }}</span>
        <we-icon name="arrow-down" size="12px"/>
      </div>
      <div v-if="currentUser">
        <!-- <i class="iconfont icon-duoyuyan" @click="showLangList" v-if="actions.length > 1" /> -->
        <!-- <i class="iconfont icon-search" @click="inputSearch"></i> -->
        <i
          class="iconfont icon-menu-MyMessage"
          v-if="messageUrl"
          @click="handleMessage"
        ></i>
        <i class="iconfont icon-UserName" @click="handlePersonal"></i>
      </div>
      <div v-if="!currentUser" class="head-isNotLogin">
        <!-- <i class="iconfont icon-search" @click="inputSearch"></i> -->
        <span @click="handleLogin">
          {{ $Lan('CUS_TEMPLATE_OFFICIAL_h5', 'login', '请登录') }}
        </span>
      </div>
    </div>
    <div class="common-content" ref="commonContent">
      <card-layout :cardLayout="cardLayout" />
    </div>
    <customActionSheet
      v-model="langShow"
      :actions="actions"
      :cancel-text="$Lan('CUS_TEMPLATE_OFFICIAL_h5', 'cancel', '取消')"
      close-on-click-action
      @select="changeLanguage"
      :duration="0"
      style="height: initial"
    />
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
/* eslint-disable no-debugger */
import common from "@/mixins/common";
import CardLayout from "../cardLayout";
export default {
  mixins: [common],
  components: {
    CardLayout,
  },
  props: {
    pageData: Object,
  },
  data() {
    return {
      scrollToTop: false,
      newplaceholder: "",
      searchPrompt: "", // 搜索提示语，优先级 > 搜索词
      scrollTop: 0,
      isFixed: false,
      langShow: false,
      siteVisible: false
      // actions: [{ name: "退出登录", color: "#ee0a24" }],
    };
  },
  computed: {
    actions() {
      const list = this.currentUser && this.currentUser.supportLanguages || []
      return list.map(el => ({
        name: el.langName,
        value: el.langCode
      }))
    },
    siteList() {
      let list = window.shell.getSiteList() || []
      return list.map(el => ({
        ...el,
        name: el.siteName,
        className: (this.currentSite && this.currentSite.siteName) === el.siteName ? 'portal-primary-color-lv1' : ''
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
    handleLogin() {
      const location = window.shell.getLocation();
      const page = location.hash.replace("#", "");
      window.shell.login({
        params: {
          localHref: page,
        },
      });
    },
    inputSearch() {
      window.shell.openPage(
        `result?searchKey=&placeholder=${this.newplaceholder}`,
        0,
        1
      );
    },
    handleSearch() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 3,
            filterInfo: {
              keyword: this.newplaceholder
            }
          }
        },
        startTime: new Date().getTime()
      });
      window.shell.openPage(
        `result?searchKey=${this.newplaceholder}&placeholder=${this.newplaceholder}&searchPrompt=${this.searchPrompt}`,
        0,
        1
      );
    },
    async getPlaceholderVal(t) {
      const renderData = t && t.pageContext.pageInfoEntity;
      console.log("placeholderwid", t);
      const [res] = await window.shell.getPlaceholderVal({
        wid: renderData.wid,
      });

      if (res.errcode === "0" && res.data) {
        const arr = res.data["search.searchPromptLangs"] || [];
        this.searchPrompt = arr.find((el) => el.key === this.$i18n.locale)?.value || "";
        this.newplaceholder = this.searchPrompt ? "" : res.data["search.placeholderVal"] || "";
      } else {
        this.newplaceholder = "";
      }
      //  this.placeholderEllipsis(this.newplaceholder);
    },
    // 折叠展示的placeholder
    placeholderEllipsis(newplaceholder) {
      const val = newplaceholder || "";
      const dom = document.createElement("span");
      dom.style.visibility = "hidden";
      dom.style.display = "inline-block";
      dom.textContent = val;
      document.body.appendChild(dom);
      let width = dom.clientWidth;
      let offset = val.length;
      let realText = val;
      if (width > 350) {
        while (width > 350) {
          realText = `${val.slice(0, offset)}...`;
          dom.textContent = realText;
          width = dom.clientWidth;
          offset = offset - 1;
        }
      }
      document.body.removeChild(dom);
      this.newplaceholder = realText;
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
    handlePersonal() {
      window.shell.openPage(`personal`, 0, 1);
    },
    showLangList() {
      this.langShow = true
    },
    changeLanguage(item) {
      sessionStorage.setItem('locale', item.value)
      this.$i18n.locale = item.value || 'zh_CN'
      window.shell.setLanguage(item.value)
      window.shell.getLanguageList(item.value, res => {
        if (res.errcode === '0') {
          this.$i18n.mergeLocaleMessage(item.value, res.data)
        }
      })
    },
    handleChangeSite() {
      this.siteVisible = true
    },
    switchSite(item) {
      window.shell.changeSite(item.siteRoute);
    },
  },

  watch: {
    pageData: {
      handler(res) {
        this.getPlaceholderVal(res);
      },
      deep: true,
    },
    scrollTop(val) {
      this.isFixed = val >= 180;
    },
  },
  mounted() {
    window.shell.on("getScrollTop", (it) => {
      this.scrollTop = it;
    });
  },
  created() {
    if (this.pageData && !this.newplaceholder) {
      this.getPlaceholderVal(this.pageData);
    }
  },
  beforeDestroy() {
    window.shell.off("getScrollTop");
  },
};
</script>

<style lang="less" scoped>
// .container {
//   width: 100vw;
//   padding: 0 15px;
// }
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
      display: flex;
      justify-content: space-between;
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
  }
  .text__wrap {
    display: flex;
    flex-direction: column;
    margin: 26px 17px;
    .title {
      // font-family: PingFangSC-Semibold;
      font-weight: bold;
      font-size: 26px;
      color: #ffffff;
      letter-spacing: 0;
      text-align: justify;
      line-height: 32px;
    }
    .title-search {
      margin: 10px 0;
      display: flex;
      align-items: center;
      .title_input {
        width: calc(100% - 72px);
        height: 42px;
        background: #ffffff;
        box-shadow: 0 2px 8px 0 rgba(112, 125, 143, 0.2);
        border-radius: 4px;
        border-radius: 4px;
        &::after {
          border: none !important;
        }
        .title_search {
          padding: 8px 0.42667rem;
          border-radius: 4px;
          margin-top:2px;
         /deep/input[class="we-field__control"] {
            width: 100%;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            color: #9FA8B5;
          }
        
        }
      }
      .title_input_button {
        background: #3f83fb;
        font-size: 16px;
        color: #fff;
        margin-left: -2px;
        z-index: 1;
        border-radius: 0px 4px 4px 0px;
        min-width: 72px;
        height: 42px;
        border: none;
        max-width: 111px;
        /deep/.we-button__text {
          text-overflow: ellipsis;
          overflow: hidden;
          white-space: nowrap;
        }
      }
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
