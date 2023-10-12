<template>
  <div class="login__wrap">
    <!-- 导航栏配置 -->
    <nav-link
      v-if="headerNavBar.length"
      style="margin-right: 20px;"
      :headerNavBar="headerNavBar"
    />
    <!-- 消息 -->
    <div
      class="we-dropdown-link mr-20"
      v-if="currentUser && messageUrl"
      @click="goToMessageCenter"
    >
      <we-badge
        :hidden="ishasUnReadMsgUrl == '1' || count == 0"
        :value="count"
        :max="99"
      >
        <span class="icon iconfont icon-xiaoxi we-dropdown-link"> </span>
      </we-badge>
    </div>
    <!-- 切换站点 -->
    <we-dropdown
      v-if="siteList.length > 1"
      @command="handleCommandSites"
      placement="bottom"
      class="mr-20"
    >
      <span class="icon iconfont icon-fangan we-dropdown-link"> </span>
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
    <!-- 多语言 -->
    <language-menu
      :pageRenderData="pageRenderData"
      :currentUser="currentUser"
      class="mr-20"
    />
    <!-- 登陆用户菜单 -->
    <we-dropdown v-if="currentUser" @command="handleCommand">
      <span class="we-dropdown-link">
        <i class="iconfont icon-UserName"></i>
      </span>
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
            {{ $Lan("CUS_TEMPLATE_CQWORK", "logout1", "退出") }}
          </span>
        </we-dropdown-item>
      </we-dropdown-menu>
    </we-dropdown>
    <div v-else class="login__btn_box">
      <div class="login__btn" @click="handleLogin" style="color: #fff;">
        {{ $Lan("CUS_TEMPLATE_CQWORK", "login", "登录") }}
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
import NavLink from "./NavLink";
import LanguageMenu from "./Language";
export default {
  name: "login",
  components: {
    NavLink,
    LanguageMenu,
  },
  props: ["pageData", "currentUser"],
  data() {
    return {
      isLogin: false,
      userName: "超级管理员",
      count: 0,
      ishasUnReadMsgUrl: "1",
    };
  },
  computed: {
    pageRenderData() {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData?.pageInfoEntity?.wid,
      };
    },
    portalManager() {
      return this.pageRenderData.portalManager || {};
    },
    siteList() {
      return window.shell.getSiteList();
    },
    currentSite() {
      return window.shell.getCurrentSite();
    },
    // 模板全局配置
    templateConfig() {
      return this.pageData
        ? JSON.parse(
            JSON.parse(this.pageData.showProgrammeEntity.templateConfig || "")
          )
        : {};
    },
    headerNavBar() {
      return (
        (this.templateConfig &&
          this.templateConfig.headerNavBar &&
          this.templateConfig.headerNavBar.datas) ||
        []
      );
    },
    // 消息中心链接
    messageUrl() {
      return (this.templateConfig && this.templateConfig.messageListUrl) || "";
    },
    portalSelectMenus() {
      return this.pageData && this.pageData.pageContext && this.pageData.pageContext.portalSelectMenus;
    }
  },
  methods: {
    handleLogin() {
      const location = window.shell.getLocation();
      const page = location.hash.replace("#", "");
      window.shell.login({
        params: {
          localHref: page,
        },
      });
    },
    handleCommand(name) {
      let nameMap = {
        logout: this.$Lan("CUS_TEMPLATE_CQWORK", "logout", "注销"),
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
              personalCenterName: `${nameMap[name]}`,
            },
          },
        },
        startTime: new Date().getTime(),
      });
      if (name === "logout") {
        const location = window.shell.getLocation();
        const page = location.hash.replace("#", "");
        window.shell.logout({
          params: {
            localHref: page,
          },
        });
      } else {
        if (name === "changeSites") {
          window.shell.openSitesModal();
        } else {
          const url = this.portalSelectMenus.filter(v=> v.wid === name)[0].url;
          const openType = this.portalSelectMenus.filter(v=> v.wid === name)[0].openType;
          // 后台管理url要加上时间戳
          if (name === "backendManageUrl") {
            window.shell.openPage(
              `${url}?t=${new Date().valueOf()}`,
              openType,
              2
            );
          } else {
            window.shell.openPage(url, openType, 2);
          }
        }
      }
    },
    handleCommandSites(siteRoute) {
      window.shell.changeSite(siteRoute);
    },
    goToMessageCenter() {
      const pageInfoEntity = window.shell.getPageData().pageInfoEntity;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        startTime: new Date().getTime(),
        actionParams: {
          pageCode: pageInfoEntity.pageCode,
          pageName: pageInfoEntity.pageName,
        },
      });
      if (this.messageUrl) {
        window.shell.openPage(this.messageUrl, 1, 2);
      } else {
        return;
      }
    },
    async getMessageCount() {
      const [res] = await window.shell.getMessageCount();
      if (res.errcode === "0" && res.data) {
        this.count = res.data;
      }
    },
  },
  watch: {
    pageData: {
      handler(val) {
        if (val) {
          this.ishasUnReadMsgUrl = JSON.parse(
            JSON.parse(val.showProgrammeEntity.templateConfig || "")
          ).isShowMsgCount;
          if (!this.ishasUnReadMsgUrl || this.ishasUnReadMsgUrl == "0") {
            this.getMessageCount();
          }
        }
      },
      immediate: true,
    },
  },
};
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
.mr-20 {
  margin-right: 20px;
}
.login__wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-shrink: 0;
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
  color: #fff;
}
.user__name {
  width: 80px;
  padding-left: 6px;
  font-size: 16px;
}
.drop__site__item {
  width: 146px;
  white-space: nowrap;
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
/deep/.we-badge__content.is-fixed {
  right: 9px;
}
/deep/.we-badge__content {
  height: 14px;
  line-height: 14px;
  padding: 0 4px;
  border-radius: 7px;
}
</style>
