<template>
  <div class="login__wrap">
    <we-dropdown v-if="siteList.length > 1" @command="handleCommandSites" placement="bottom" style="padding-right: 40px">
      <span class="we-dropdown-link">
        <!-- <i class="iconfont icon-UserName"></i> -->
        <span class="text__ellipsis user__name" :title="currentSite && currentSite.siteName">
          {{ currentSite && currentSite.siteName }}
        </span>
        <i class="we-icon-caret-bottom we-icon--right"></i>
      </span>
      <we-dropdown-menu slot="dropdown">
        <we-dropdown-item
          :command="item.siteRoute"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__site__item"
          v-for="item in siteList"
          :key="item.wid"
        >
          <span class="text__ellipsis" :title="item.siteName">
            {{ item.siteName }}
          </span>
          <i
            class="we-icon-check portal-primary-color-lv1"
            style="font-size:14px;font-weight:bold;margin-right:0"
            v-if="item.wid === currentSite.wid"
          />
        </we-dropdown-item>
      </we-dropdown-menu>
    </we-dropdown>
    <language-menu :pageRenderData="pageRenderData" :currentUser="currentUser" style="margin-right: 30px" />
    <we-dropdown v-if="currentUser" @command="handleCommand" placement="bottom">
      <span class="we-dropdown-link">
        <i class="iconfont icon-UserName"></i>
        <span class="text__ellipsis user__name" :title="currentUser.userName">
          {{ currentUser.userName }}
        </span>
        <i class="we-icon-caret-bottom we-icon--right"></i>
      </span>
      <we-dropdown-menu slot="dropdown">
        <we-dropdown-item
          command="personCenterUrl"
          v-if="portalManager.personCenterUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item"
        >
          <i class="iconfont icon-personCenter"></i>
          <span class="text__ellipsis">
            {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'personCenter', '个人中心') }}
          </span>
        </we-dropdown-item>
        <we-dropdown-item
          command="postitionManagerUrl"
          v-if="portalManager.postitionManagerUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item"
        >
          <i class="iconfont icon-gangweiguanli"></i>
          <span class="text__ellipsis">
            {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'positionManage', '岗位管理') }}
          </span>
        </we-dropdown-item>
        <we-dropdown-item
          command="accountManageUrl"
          v-if="portalManager.accountManageUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item"
        >
          <i class="iconfont icon-accountManage"></i>
          <span class="text__ellipsis">
            {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'accountManage', '帐号管理') }}
          </span>
        </we-dropdown-item>
        <we-dropdown-item
          command="backendManageUrl"
          v-if="portalManager.backendManageUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item"
        >
          <i class="iconfont icon-backstageManage"></i>
          <span class="text__ellipsis">
            {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'backendManage', '后台管理') }}
          </span>
        </we-dropdown-item>
        <we-dropdown-item
          command="logout"
          class="portal-font-color-lv1 portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item"
        >
          <i class="iconfont icon-logout logout__item"></i>
          <span class="logout__item text__ellipsis">
            {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'logout', '安全退出') }}
          </span>
        </we-dropdown-item>
      </we-dropdown-menu>
    </we-dropdown>
    <div
      v-else
      class="login__btn portal-primary-color-lv1 portal-primary-backgroundcolor-hover-lv5"
      @click="handleLogin"
    >
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
    },
    siteList() {
      return window.shell.getSiteList()
    },
    currentSite() {
      return window.shell.getCurrentSite()
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
    handleCommand(name) {
      const nameMap = {
        personCenterUrl: this.$Lan('SYS_TEMPLATE_OFFICIAL', 'personcenter', '个人中心'),
        postitionManagerUrl: this.$Lan('SYS_TEMPLATE_OFFICIAL', 'positionManage', '岗位管理'),
        accountManageUrl: this.$Lan('SYS_TEMPLATE_OFFICIAL', 'accountManage', '帐号管理'),
        changeSites: this.$Lan('SYS_TEMPLATE_OFFICIAL', 'changeSites', '切换站点'),
        backendManageUrl: this.$Lan('SYS_TEMPLATE_OFFICIAL', 'backManage', '后台管理'),
        logout: this.$Lan('SYS_TEMPLATE_OFFICIAL', 'logout', '注销')
      }
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 7,
            filterInfo: {
              personalCenterName: `${nameMap[name]}`
            }
          }
        },
        startTime: new Date().getTime()
      });
      if (name === 'logout') {
        const location = window.shell.getLocation()
        const page = location.hash.replace('#', '')
        window.shell.logout({
          params: {
            localHref: page
          }
        })
      } else {
        if (name === 'changeSites') {
          window.shell.openSitesModal()
        } else {
          // 后台管理url要加上时间戳
          if (name === 'backendManageUrl') {
            window.shell.openPage(`${this.portalManager[name]}?t=${new Date().valueOf()}`, 1, 2)
          } else {
            window.shell.openPage(this.portalManager[name], 1, 2)
          }
        }
      }
    },
    handleCommandSites(siteRoute) {
      window.shell.changeSite(siteRoute);
    }
  }
}
</script>

<style scoped>
.login__wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.login__btn {
  min-width: 80px;
  max-width:120px;
  height: 35px;
  background: #fff;
  border-radius: 0.25rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  cursor: pointer;
}
.we-dropdown-link {
  cursor: pointer;
  color: rgba(255, 255, 255, 0.6);
  width: 105px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.user__name {
  width: 80px;
  padding-left: 6px;
  font-size: 16px;
}
.logout__item {
  /* color: #e22727; */
}
.drop__site__item {
  width: 146px;
  white-space:nowrap;
  display: flex;
  align-items: center;
  justify-content: space-between;
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
