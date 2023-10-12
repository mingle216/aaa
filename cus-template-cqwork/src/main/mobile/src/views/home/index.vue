<template>
  <div class="container" :style="headerStyle">
    <div class="common-head" ref="commonHead">
      <div class="head-title">
        <div v-if="!currentUser" class="head-isNotLogin">
          <span @click="handleLogin">
            {{ $Lan("SYS_TEMPLATE_OFFICIAL_h5", "login", "请登录") }}
          </span>
        </div>
        <div
          v-else
          class="headerInfo__img"
          :style="`background-image: url(${
            userIcon || require('../../assets/images/defaultAvatar.png')
          })`"
          @click="handlePersonal"
        />
        <we-search
          class="homeSearch"
          show-action
          :clearable="false"
          v-model="newplaceholder"
          :placeholder="newplaceholder"
          @click="inputSearch"
        >
          <template #action>
            <i class="iconfont icon-32sousuotubiao" @click="handleSearch" />
          </template>
        </we-search>
        <div style="display: flex">
          <div
            v-if="currentUser && messageUrl"
            class="message-count"
            @click="handleMessage"
          >
            <i class="iconfont icon-xiaoxi" />
          </div>
          <div
            class="site-switch"
            @click="handleChangeSite"
            v-if="siteList.length > 1"
          >
            <i class="iconfont icon-fangan" />
          </div>
        </div>
      </div>
    </div>

    <div
      class="head-title-top portal-primary-backgroundcolor-lv1"
      v-if="false"
    >
      <img class="logo" :src="logoUrl" height="50" />

      <div style="display: flex">
        <span class="login-btn" v-if="!currentUser" @click="handleLogin">
          {{ $Lan("SYS_TEMPLATE_OFFICIAL_h5", "login", "请登录") }}
        </span>
        <div class="search-icon" @click="handleSearch">
          <i class="iconfont icon-32sousuotubiao" />
        </div>
        <div
          v-if="currentUser && messageUrl"
          class="message-count"
          @click="handleMessage"
        >
          <i class="iconfont icon-xiaoxi" />
        </div>
        <div
          class="site-switch"
          @click="handleChangeSite"
          v-if="siteList.length > 1"
        >
          <i class="iconfont icon-fangan" />
        </div>
      </div>
    </div>

    <div class="common-content" ref="commonContent">
      <card-layout :cardLayout="cardLayout" />
    </div>
    <customActionSheet
      v-model="siteVisible"
      close-on-click-action
      :duration="0"
      style="height: initial"
    >
      <div class="we-action-sheet__content">
        <button
          type="button"
          class="we-action-sheet__item"
          v-for="item in siteList"
          :key="item.wid"
          @click="switchSite(item)"
        >
          <span
            :class="{
              'we-action-sheet__name': true,
              'portal-primary-color-lv1':
                currentSite && currentSite.wid === item.wid,
            }"
          >
            {{ item.siteName }}
          </span>
        </button>
      </div>
    </customActionSheet>
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
      scrollTop: 0,
      isFixed: false,
      siteVisible: false,
    };
  },
  computed: {
    actions() {
      const list =
        (this.currentUser && this.currentUser.supportLanguages) || [];
      return list.map((el) => ({
        name: el.langName,
        value: el.langCode,
      }));
    },
    siteList() {
      let list = window.shell.getSiteList() || [];
      return list.map((el) => ({
        ...el,
        name: el.siteName,
      }));
    },
    userIcon() {
      const userIcon = this.currentUser.userIcon;
      return userIcon
        ? /^http(s)?:\/\//.test(userIcon)
          ? userIcon
          : `data:image/png;base64,${userIcon}`
        : "";
    },
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
              keyword: this.newplaceholder,
            },
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.openPage(
        `result?searchKey=${this.newplaceholder}&placeholder=${this.newplaceholder}`,
        0,
        1
      );
    },
    async getPlaceholderVal(t) {
      const renderData = t && t.pageContext.pageInfoEntity;
      const [res] = await window.shell.getPlaceholderVal({
        wid: renderData.wid,
      });

      let placeholder = "";
      if (res.errcode === "0" && res.data) {
        placeholder = res.data["search.placeholderVal"];
      } else {
        placeholder = "";
      }
      this.newplaceholder = placeholder;
    },
    handleMessage() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
        },
        startTime: new Date().getTime(),
      });
      window.shell.openPage(this.messageUrl, 1, 2);
    },
    handlePersonal() {
      window.shell.openPage(`personal`, 0, 1);
    },
    handleChangeSite() {
      this.siteVisible = true;
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
.ellipsis {
  display: inline-block;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.common-head,
.head-title-top {
  .site-switch,
  .search-icon,
  .message-count {
    width: 30px;
    height: 30px;
    border-radius: 20px;
    background: #ffffff28;
    border: 1px solid #ffffff47;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .search-icon,
  .message-count {
    margin-right: 8px;
  }
  div:last-child {
    margin-right: 0;
  }
}
.common-head {
  height: 68px;
  .head-title {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 12px;
    color: #fff;
  }
}
.head-title-top {
  width: 100%;
  padding: 0 16px;
  display: flex;
  align-items: center;
  height: 52px;
  justify-content: space-between;
  position: fixed;
  top: 0px;
  z-index: 999;
  overflow: hidden;
  color: #fff;
  .logo {
    height: auto;
    max-width: 100%;
    max-height: 100%;
    width: 120px;
  }
  .login-btn {
    line-height: 28px;
    margin-right: 8px;
  }
}
.headerInfo__img {
  width: 36px;
  height: 36px;
  border: 2px solid #fff;
  border-radius: 50%;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  background-color: #fff;
}
.homeSearch {
  flex: 1;
  margin: 0 12px;
  padding: 0;
  background: transparent;
  /deep/.we-search__content {
    background: transparent;
    border-radius: 20px;
    background: #f5f5f519;
    border: 1px solid #ffffff4c;
    padding: 0 16px 0 16px;
    .we-field__control {
      color: #fff;
      opacity: 0.7;
    }
    .we-field__left-icon {
      display: none;
    }
  }
  /deep/.we-search__action {
    color: #fff;
    position: absolute;
    right: 6px;
    top: 0;
    z-index: 1;
    &:active {
      background: transparent;
    }
  }
}
/deep/.we-action-sheet__name {
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  display: inline-block;
}
</style>
