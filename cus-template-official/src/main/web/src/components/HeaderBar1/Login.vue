<template>
  <div class="login__wrap">
    <language-menu :pageRenderData="pageRenderData" :currentUser="currentUser" style="margin-right: 30px" />
    <we-dropdown v-if="currentUser" @command="handleCommand" placement="bottom">
      <span class="we-dropdown-link">
        <i class="iconfont icon-UserName"></i>
        <span class="welcome">欢迎，</span>
        <span class="text__ellipsis user__name" :title="currentUser.userName">
          {{ currentUser.userName }}
        </span>
        <!-- <i class="we-icon-caret-bottom we-icon--right"></i> -->
        <span class="logonout" @click="loginout()">退出</span>
      </span>
      <we-dropdown-menu slot="dropdown">
        <we-dropdown-item command="personCenterUrl" v-if="portalManager.personCenterUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item">
          <i class="iconfont icon-personCenter"></i>
          <span class="text__ellipsis">
            {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'personCenter', '个人中心') }}
          </span>
        </we-dropdown-item>
        <we-dropdown-item command="accountManageUrl" v-if="portalManager.accountManageUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item">
          <i class="iconfont icon-accountManage"></i>
          <span class="text__ellipsis">
            {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'accountManage', '帐号管理') }}
          </span>
        </we-dropdown-item>
        <we-dropdown-item command="backendManageUrl" v-if="portalManager.backendManageUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item">
          <i class="iconfont icon-backstageManage"></i>
          <span class="text__ellipsis">
            {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'backendManage', '后台管理') }}
          </span>
        </we-dropdown-item>
        <!-- <we-dropdown-item command="logout"
          class="portal-font-color-lv1 portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item">
          <i class="iconfont icon-logout logout__item"></i>
          <span class="logout__item text__ellipsis">
            {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'logout', '安全退出') }}
          </span>
        </we-dropdown-item> -->
      </we-dropdown-menu>
    </we-dropdown>
    <div v-else class="login__btn portal-primary-color-lv1 portal-primary-backgroundcolor-hover-lv5"
      @click="handleLogin"><img style="margin-right: 10px;" src="./images/login.png">
      {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'login', '登录') }}
    </div>
  </div>
</template>

<script>
  import LanguageMenu from "./Language";
  export default {
    name: 'login',
    components: {
      LanguageMenu
    },
    props: ['pageRenderData', 'currentUser'],
    data() {
      return {
        isLogin: false,
        userName: '超级管理员'
      }
    },
    computed: {
      portalManager() {
        return this.pageRenderData.portalManager || {}
      }
    },
    methods: {
      handleLogin() {
        const location = window.shell.getLocation()
        const page = location.hash.replace('#', '')
        window.shell.login({
          params: {
            localHref: page
          }
        })
      },
      loginout(){
        const location = window.shell.getLocation()
          const page = location.hash.replace('#', '')
          window.shell.logout({
            params: {
              localHref: page
            }
          })
      },
      handleCommand(name) {
        if (name === 'logout') {
          const location = window.shell.getLocation()
          const page = location.hash.replace('#', '')
          window.shell.logout({
            params: {
              localHref: page
            }
          })
        } else {
          // 后台管理url要加上时间戳
          if (name === 'backendManageUrl') {
            window.shell.openPage(`${this.portalManager[name]}?t=${new Date().valueOf()}`, 1, 2)
          } else {
            window.shell.openPage(this.portalManager[name], 1, 2)
          }
        }
      }
    }
  }
</script>

<style scoped>
  .login__wrap {
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: absolute;
    top: 34px;
    right: calc(50% - 41.5rem);
  }

  .welcome {
    font-family: MicrosoftYaHei;
    font-size: 14px;
    color: #000000;
    letter-spacing: 0;
    font-weight: 400;
    display: inline-block;
    /* width: 40px; */
    margin-left: 7px;
  }

  .logonout {
    font-family: MicrosoftYaHei;
    font-size: 14px;
    color: #0B5AA5;
    letter-spacing: 0;
    font-weight: 400;
    margin-left: 15px;
  }

  .login__btn {
    min-width: 80px;
    max-width: 120px;
    height: 35px;
    background: #fff;
    border-radius: 0.25rem;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 17px;
    cursor: pointer;
  }

  .we-dropdown-link {
    cursor: pointer;
    /* color: rgba(255, 255, 255, 0.6); */
    /* width: 200px; */
    display: flex;
    align-items: center;
    /* justify-content: space-between; */
  }

  .user__name {
    /* width: 80px; */
    padding-left: 6px;
    font-size: 16px;
  }

  .logout__item {
    /* color: #e22727; */
  }

  .drop__item {
    width: 146px;
    display: flex;
    align-items: center;
  }

  .drop__item i {
    margin-right: 10px;
    font-size: 20px;
  }

  /deep/.we-dropdown-menu__item:focus {
    color: #606266;
  }

  /deep/.we-dropdown-menu__item:not(.is-disabled):hover {
    color: #606266;
  }

  /deep/.we-dropdown-menu__item {
    padding: 0 12px;
  }
</style>