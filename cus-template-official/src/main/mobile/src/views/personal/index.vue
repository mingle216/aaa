<template>
  <div class="personal">
    <header class="personal_header">
      <div class="wrapper login" v-if="currentUser && currentUser.userAccount">
        <div class="header_userInfo">
          <!-- <img
            width="60"
            height="60"
            :src="!currentUser.userIcon ? defaultIcon : currentUser.userIcon"
          /> -->
          <div style="width: 60px; height: 60px; border-radius: 0.533333rem; background-size: 100% 100%; background-repeat: no-repeat; background-position: center;" :style="{'background-image': `url(${ !currentUser.userIcon ? require('../../assets/images/defaultAvatar.png') : currentUser.userIcon })`}"></div>
          <div class="info">
            <p class="username">{{ currentUser.userName }}</p>
            <p class="usercode">{{ currentUser.userAccount }}</p>
          </div>
        </div>
        <div ref="set">
          <img
            @click="loginOutShow = true"
            :src="setIcon"
            height="24"
            width="24"
            alt="管理"
          />
        </div>
      </div>
      <div v-else class="please-login-box">
        <div style="width: 60px; height: 60px; border-radius: 0.533333rem; background-size: 100% 100%; background-repeat: no-repeat; background-position: center;" :style="{'background-image': `url(${ require('../../assets/images/defaultAvatar.png') })`}"></div>
        <!-- <img width="60" height="60" :src="defaultIcon" /> -->
        <span class="please-login-text" @click="goLogin">
          {{ $Lan('SYS_TEMPLATE_OFFICIAL_h5', 'login', '请登录') }}
        </span>
      </div>
    </header>
    <main class="personal_content">
      <card-layout :cardLayout="cardLayout"></card-layout>
    </main>
    <!-- actionsheet -->
    <customActionSheet
      v-model="loginOutShow"
      :actions="actions"
      :cancel-text="$Lan('SYS_TEMPLATE_OFFICIAL_h5', 'cancel', '取消')"
      close-on-click-action
      @select="logout"
      :duration="0"
      style="height: initial"
    >
    </customActionSheet>
  </div>
</template>

<script>
/* eslint-disable no-debugger */
import CardLayout from "../cardLayout";
export default {
  name: "Personal",
  components: {
    CardLayout,
  },
  props: {
    pageData: Object,
    showFooter: [String, Boolean],
  },
  data() {
    return {
      loginOutShow: false,
      setIcon: require("../../assets/images/set.png"),
      defaultIcon: require("../../assets/images/defaultAvatar.png"),
      actions: [{ name: `${this.$Lan('SYS_TEMPLATE_OFFICIAL_h5', 'logout', '退出登录')}`, color: "#ee0a24" }],
    };
  },
  computed: {
    currentUser() {
      return window.shell.getUserInfo() || {};
    },
    cardLayout() {
      return JSON.parse(
        (this.pageData &&
          this.pageData.pageInfoEntity &&
          this.pageData.pageInfoEntity.cardLayout) ||
          "[]"
      );
    },
  },
  methods: {
    goLogin() {
      const page = window.shell.getLocation().hash.replace("#", "");
      window.shell.login({
        params: {
          localHref: page,
        },
      });
    },
    logout() {
      const location = window.shell.getLocation();
      const page = location.hash.replace("#", "");
      window.shell.logout({
        params: {
          localHref: page,
        },
      });
    },
  },
  mounted() {},
};
</script>

<style lang="less" scoped>
.personal {
  width: 100vw;
  position: relative;
  height: 100%;
  box-sizing: border-box;
  display: flex;
  flex-flow: column;
  padding-top:100px;
  .please-login-box {
    display: flex;
    line-height: 24px;
    align-items: center;
    .please-login-text {
      // font-family: PingFangSC-Semibold;
      font-weight: bold;
      font-size: 20px;
      color: #102645;
      letter-spacing: 0;
      text-align: justify;
      line-height: 24px;
      margin-left: 12px;
    }
  }

  .personal_header {
    height: 100px;
    padding: 20px 17px;
    font-size: 14px;
    position: fixed;
    top: 0;
    .wrapper {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .we-button {
        padding: 2px 10px;
      }
    }
    .header_userInfo {
      display: flex;
      align-items: center;
      .info {
        margin: 0 12px;
        width: calc(~"100vw - 158px");
        .username {
          // font-family: PingFangSC-Semibold;
          font-weight: bold;
          font-size: 20px;
          color: #102645;
          letter-spacing: 0;
          text-align: justify;
          line-height: 24px;
        }
        .usercode {
          margin-top: 4px;
          // font-family: PingFangSC-Regular;
          font-size: 14px;
          color: #707d8f;
          letter-spacing: 0;
          text-align: justify;
          line-height: 18px;
        }
      }
      img {
        border-radius: 20px;
      }
    }
  }

  /deep/ .we-action-sheet__name {
    // font-family: PingFangSC-Regular;
    font-size: 16px;
    color: #ff230c;
    letter-spacing: 0;
    line-height: 20px;
  }

  .personal_content {
    position: relative;
    flex: 1;
    height: 0;
    padding-top: 0.32rem;
    display: flex;
    flex-flow: column;
    user-select: none;
    // height: calc(~"100vh - 160px");
    overflow-y: auto;
    &.hiddenFooter {
      height: calc(~"100vh - 100px");
    }
  }

  /deep/ .we-overlay {
    z-index: 111;
  }
}
</style>
