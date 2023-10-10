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
    <we-dropdown v-if="currentUser" @command="handleCommand">
      <span class="we-dropdown-link">
        <i class="iconfont icon-UserName"></i>
        <span class="text__ellipsis user__name" :title="currentUser.userName">
          {{ currentUser.userName }}
        </span>
        <i class="we-icon-caret-bottom we-icon--right"></i>
      </span>
      <!-- 登陆用户菜单 -->
      <we-dropdown-menu slot="dropdown" class="ty_drop_menu" style="padding: 0px !important; margin: 4px 0px 0px 0px !important;">
        <AutoContainer :conMaxHeight="200" v-if="portalSelectMenus && portalSelectMenus.length > 0">
          <we-dropdown-item
            v-for="item in portalSelectMenus"
            :key="item.wid"
            :command="item.wid"
            class="portal-primary-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item portal-font-color-lv1"
          >
            <img v-if="item.iconType === 1 && item.iconUrl" style="width: 20px; margin-right: 10px;" :src="item.iconUrl" alt="">
            <i v-if="item.iconType === 0 && item.iconUrl" :class="`iconfont ${item.iconUrl}`"></i>
            <span class="text__ellipsis">
              {{ item.menuName }}
            </span>
          </we-dropdown-item>
        </AutoContainer>
        <we-dropdown-item
          command="logout"
          class="portal-primary-backgroundcolor-hover-lv5 drop__item"
          style="color: #EE3F15;"
          :class="[portalSelectMenus && portalSelectMenus.length > 0 ? 'btop' : '']"
        >
          <span class="logout__item text__ellipsis">
            {{ $Lan('CUS_TEMPLATE_OFFICIAL', 'logout1', '退出') }}
          </span>
        </we-dropdown-item>
      </we-dropdown-menu>
    </we-dropdown>
    <div v-else class="login__btn_box">
      <div
        class="login__btn"
        style="color: #fff;"
        @click="handleLogin"
      >
        {{ $Lan('CUS_TEMPLATE_OFFICIAL', 'login', '登录') }}
      </div>
      <div class="l_line" v-if="portalSelectMenus && portalSelectMenus.length > 0"></div>
      <!-- 游客下拉菜单 -->
      <we-dropdown v-if="portalSelectMenus && portalSelectMenus.length > 0" @command="handleCommand">
        <div class="a_bot">
          <i class="we-icon-caret-bottom" style="color: #fff;"></i>
        </div>
        <we-dropdown-menu slot="dropdown" class="ty_drop_menu" style="padding: 0px !important; margin: 4px 0px 0px 0px !important;">
          <div style="width: 148px;">
            <AutoContainer :conMaxHeight="200">
              <we-dropdown-item
                v-for="item in portalSelectMenus"
                :key="item.wid"
                :command="item.wid"
                class="portal-primary-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item portal-font-color-lv1"
              >
                <img v-if="item.iconType === 1 && item.iconUrl" style="width: 20px; margin-right: 10px;" :src="item.iconUrl" alt="">
                <i v-if="item.iconType === 0 && item.iconUrl" :class="`iconfont ${item.iconUrl}`"></i>
                <span class="text__ellipsis">
                  {{ item.menuName }}
                </span>
              </we-dropdown-item>
            </AutoContainer>
          </div>
        </we-dropdown-menu>
      </we-dropdown>
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
  props: ['pageRenderData', 'currentUser', 'pageData'],
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
    },
    portalSelectMenus() {
      return this.pageData && this.pageData.pageContext && this.pageData.pageContext.portalSelectMenus;
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
      let nameMap = {
        logout: this.$Lan('CUS_TEMPLATE_OFFICIAL', 'logout', '注销')
      };
      this.portalSelectMenus.forEach(v=> {
        nameMap[v.wid] = v.menuName;
      });
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
          const url = this.portalSelectMenus.filter(v=> v.wid === name)[0].url;
          const openType = this.portalSelectMenus.filter(v=> v.wid === name)[0].openType;
          // 后台管理url要加上时间戳
          if (name === 'backendManageUrl') {
            window.shell.openPage(`${url}?t=${new Date().valueOf()}`, openType, 2)
          } else {
            window.shell.openPage(url, openType, 2)
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

<style scoped lang="less">
.ty_drop_menu{
  /deep/ .popper__arrow{
    display: none;
  }
  
}
.btop{
  border-top: 1px solid #e5e5e5 !important;
}
.login__wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.login__btn_box{
  border-radius: 0.25rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  cursor: pointer;
  border: 1px solid #fff;
}
.login__btn_box:hover{
  background-color: rgba(255, 255, 255, 0.1); font-weight: bold;
}
.a_bot{
  width: 22px; height: 30px; display: flex; justify-content: center; align-items: center; cursor: pointer;
}
.l_line{
  width: 1px; height: 30px; background-color: #fff;
}
.login__btn {
  width: 66px;
  height: 30px;
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
