<template>
  <div class="personal" :style="headerStyle">
    <header class="personal_header">
      <div class="wrapper login" v-if="currentUser && currentUser.userAccount">
        <div class="header_userInfo">
          <div
            class="avatar"
            :style="{
              'background-image': `url(${
                !userIcon
                  ? require('../../assets/images/defaultAvatar.png')
                  : userIcon
              })`,
            }"
          ></div>
          <div class="info">
            <p class="username">{{ currentUser.userName }}</p>
            <p class="usercode">{{ currentUser.userAccount }}</p>
          </div>
        </div>
        <div class="setting">
          <we-icon
            name="setting-o"
            @click="loginOutShow = true"
            size="14"
            color="#fff"
          />
        </div>
      </div>
      <div v-else class="please-login-box">
        <div
          class="avatar"
          :style="{
            'background-image': `url(${require('../../assets/images/defaultAvatar.png')})`,
          }"
        ></div>
        <!-- <img width="60" height="60" :src="defaultIcon" /> -->
        <span class="please-login-text" @click="goLogin">
          {{ $Lan("SYS_TEMPLATE_OFFICIAL_h5", "login", "请登录") }}
        </span>
      </div>
    </header>
    <main class="personal_content">
      <card-layout :cardLayout="cardLayout"></card-layout>
    </main>

    <we-popup
      v-model="loginOutShow"
      position="right"
      get-container="body"
      :style="{ width: '100%', height: '100%' }"
    >
      <!-- <div class="popbox"> -->
      <account-manage
        @closeAccount="closeAccount"
        :currentUser="currentUser"
        :showFooter="showFooter"
      />
      <!-- </div> -->
    </we-popup>
  </div>
</template>

<script>
import common from "@/mixins/common";
import AccountManage from ".././account";
import CardLayout from "../cardLayout";

export default {
  name: "Personal",
  mixins: [common],
  components: {
    AccountManage,
    CardLayout,
  },
  props: {
    pageData: Object,
    showFooter: [String, Boolean],
  },
  data() {
    return {
      loginOutShow: false,
      defaultIcon: require("../../assets/images/defaultAvatar.png"),
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
    userIcon() {
      const userIcon = this.currentUser.userIcon
      return userIcon ? /^http(s)?:\/\//.test(userIcon) ? userIcon : `data:image/png;base64,${userIcon}` : ''
    }
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
    closeAccount() {
      this.loginOutShow = false;
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
  // padding-top: 100px;
  .please-login-box {
    display: flex;
    line-height: 24px;
    align-items: center;
    .please-login-text {
      font-weight: bold;
      font-size: 20px;
      letter-spacing: 0;
      text-align: justify;
      line-height: 24px;
      margin-left: 12px;
    }
  }

  .personal_header {
    // height: 88px;
    padding: 16px 12px;
    font-size: 14px;
    color: #fff;
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
      .avatar {
        width: 56px;
        height: 56px;
        border: 2px solid #fff;
        border-radius: 50%;
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        background-color: #fff;
      }
      .info {
        margin: 0 12px;
        width: calc(~"100vw - 158px");
        .username {
          font-weight: bold;
          font-size: 20px;
          letter-spacing: 0;
          text-align: justify;
          line-height: 28px;
          margin-bottom: 6px;
        }
        .usercode {
          font-size: 14px;
          letter-spacing: 0;
          text-align: justify;
          line-height: 22px;
        }
        .username, .usercode {
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
      }
      img {
        border-radius: 20px;
      }
    }
    .setting {
      width: 30px;
      height: 30px;
      border-radius: 20px;
      background: #ffffff28;
      border: 1px solid #ffffff47;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .personal_content {
    position: relative;
    flex: 1;
    height: 0;
    padding-bottom: 30px;
    display: flex;
    flex-flow: column;
    user-select: none;
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
