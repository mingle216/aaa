<template>
  <div class="personal">
    <header class="personal_header" :style="{
              'background-image': `url(${require('../../assets/images/personalbg.png')})`,
              'background-repeat': 'no-repeat',
              'background-position': 'top center',
              'background-size': '100%',
              'background-color': '#fff',
              'background-position-y': '-36px',
            }">
      <div class="userinfo-content">
        <div class="wrapper login" v-if="currentUser && currentUser.userAccount">
          <div class="header_userInfo">
            <!-- <img
              width="60"
              height="60"
              :src="!currentUser.userIcon ? defaultIcon : currentUser.userIcon"
            /> -->
            <div
              style="
                width: 80px;
                height: 80px;
                border-radius: 50%;
                border: 2px solid #fff;
                background-size: cover;
                background-repeat: no-repeat;
                background-position: center;
                flex-shrink: 0;
                box-shadow: 0px 4px 6px rgba(28, 50, 128, 0.1);
                background-color: #fff;
              "
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
              <div class="usercode">
                <p class="usercodebg"></p>
                <p class="usercodeval">
                  {{ currentUser.userAccount }}
                </p>
              </div>
            </div>
          </div>
        </div>
        <div ref="set" class="set-div" v-if="currentUser && currentUser.userAccount"
            @click="loginOutShow = true">
          <!-- <i class="iconfont icon-dongdamoban-guanli"></i> -->
          <img :src="require('../../assets/images/setpersonal.png')" alt="">
        </div>
        <div v-if="!currentUser || !currentUser.userAccount" class="wrapper login">
          <div class="header_userInfo">
            <div
              style="
                width: 80px;
                height: 80px;
                border-radius: 50%;
                border: 2px solid #fff;
                background-size: cover;
                background-repeat: no-repeat;
                background-position: center;
                flex-shrink: 0;
                box-shadow: 0px 4px 6px rgba(28, 50, 128, 0.1);
                background-color: #fff;
              "
              :style="{
                'background-image': `url(${require('../../assets/images/defaultAvatar.png')})`,
              }"
            ></div>
            <div class="info">
              <p class="username" @click="goLogin">
                {{ $Lan("SYS_CARD_PERSONALDATA_h5", "login", "请登录") }}
              </p>
            </div>
          </div>
      </div>
      </div>
      
    </header>
    <main class="personal_content">
      <card-layout :cardLayout="cardLayout"></card-layout>
    </main>
    <div data-v-3351b6ca="" class="fixedFooterBar"></div>
    <we-popup
      v-model="loginOutShow"
      position="bottom"
      get-container="body"
      :style="{ width: '100%', 'background-color': 'unset',}"
    >
      <!-- <div class="popbox"> -->
        <account-manage  @closeAccount="closeAccount" :currentUser="currentUser" :showFooter="showFooter"/>
      <!-- </div> -->
    </we-popup>
  </div>
</template>

<script>
import AccountManage from ".././account";
import CardLayout from "../cardLayout";

export default {
  name: "Personal",
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
      setIcon: require("../../assets/images/set.png"),
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
      this.loginOutShow = false
    }
  },
  mounted() {},
};
</script>

<style lang="less" scoped>
/deep/.menu-con{
  position: fixed !important;
}
.fixedFooterBar{
  height: 56px !important;
}
.personal {
  width: 100vw;
  position: relative;
  height: 100%;
  box-sizing: border-box;
  // display: flex;
  // flex-flow: column;
  background: #F4F6F8;
  // padding-top: 230px;
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
    height: 182px;
    padding: 20px 17px 11px;
    width: 100%;
    font-size: 14px;
    // position: fixed;
    top: 0;
    .userinfo-content{
      position: relative;
      height: 100%;
    }
    .wrapper {
      position: absolute;
      bottom: 0px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      .we-button {
        padding: 2px 10px;
      }
    }
    .header_userInfo {
      display: flex;
      align-items: center;
      width: 100%;
      .info {
        margin: 35px 0px 0 12px;
        width: calc(100% - 82px);
        display: flex;
        flex-wrap: nowrap;
        align-items: center;
        .username {
          // font-family: PingFangSC-Semibold;
          font-weight: bold;
          font-size: 20px;
          // color: #102645;
          letter-spacing: 0;
          text-align: justify;
          line-height: 24px;
          max-width: 70%;
          margin-right: 6px;
          text-overflow: ellipsis;
          white-space: nowrap;
          overflow: hidden;
        }
        .usercode {
          // font-family: PingFangSC-Regular;
          font-size: 14px;
          color: #707d8f;
          letter-spacing: 0;
          text-align: center;
          line-height: 18px;
          padding: 2px 8px;
          max-width: 40%;
          min-width: 30%;
          border-radius: 16px;
          // border: 1px solid  rgba(35,139,255);
          overflow: hidden;
          position: relative;
        }
        .usercodebg{
          width: 100%;
          height: 100%;
          position: absolute;
          left: 0px;
          bottom: 0px;
        }
        .usercodeval{
          width: 100%;
          text-overflow: ellipsis;
          white-space: nowrap;
          overflow: hidden;
        }
      }
      img {
        border-radius: 20px;
      }
    }
    .set-div{
      width: 36px;
      height: 36px;
      line-height: 36px;
      border-radius: 28px;
      background: linear-gradient(141.27deg, #2468F2 0%, #2468F2 100%);
      box-shadow: 0px 4px 4px 0px #2E71F53D;
      color: #fff;
      font-size: 24px;
      text-align: center;
      position: absolute;
      bottom: 56px;
      right: 10px;
      display: flex;
      justify-content: center;
      align-items: center;
      img{
        width: 50%;
      }
      .iconfont{
        font-size: 20px;
      }
    }
  }

  // /deep/ .we-action-sheet__name {
  //   // font-family: PingFangSC-Regular;
  //   font-size: 16px;
  //   color: #ff230c;
  //   letter-spacing: 0;
  //   line-height: 20px;
  // }

  .personal_content {
    position: relative;
    // flex: 1;
    // height: 0;
    padding-top: 12px;
    background: #fff;
    margin-top: 12px;
    display: flex;
    flex-flow: column;
    user-select: none;
    // padding-bottom: 56px;
    // height: calc(~"100vh - 160px");
    overflow-y: auto;
    /deep/.gateway-rows{
      padding-bottom: 12px;
    }
    &.hiddenFooter {
      height: calc(~"100vh - 100px");
    }
    .gateway-rows{
      background: #fff;
    }
  }

  /deep/ .we-overlay {
    z-index: 111;
  }
}
</style>
