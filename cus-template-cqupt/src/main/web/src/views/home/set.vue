<template>
  <div class="set" :class="type">
    <we-dropdown placement="bottom" @command="handleCommand">
      <span class="we-dropdown-link">
        <i class="we-icon-setting portal-primary-color-lv1"></i>
      </span>
      <we-dropdown-menu slot="dropdown">
        <we-dropdown-item command="personCenterUrl" v-if="portalManager.personCenterUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item">
          <i class="iconfont icon-personCenter"></i>
          <span class="text__ellipsis">
            {{ $Lan("CUS_TEMPLATE_CQUPT", "personCenter", "个人中心") }}
          </span>
        </we-dropdown-item>
        <we-dropdown-item command="postitionManagerUrl" v-if="portalManager.postitionManagerUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item">
          <i class="iconfont icon-gangweiguanli"></i>
          <span class="text__ellipsis">
            {{ $Lan("CUS_TEMPLATE_CQUPT", 'positionManage', '岗位管理') }}
          </span>
        </we-dropdown-item>
        <we-dropdown-item command="accountManageUrl" v-if="portalManager.accountManageUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item">
          <i class="iconfont icon-accountManage"></i>
          <span class="text__ellipsis">
            {{ $Lan("CUS_TEMPLATE_CQUPT", "accountManage", "帐号管理") }}
          </span>
        </we-dropdown-item>
        <we-dropdown-item command="backendManageUrl" v-if="portalManager.backendManageUrl"
          class="portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item">
          <i class="iconfont icon-backstageManage"></i>
          <span class="text__ellipsis">
            {{ $Lan("CUS_TEMPLATE_CQUPT", "backendManage", "后台管理") }}
          </span>
        </we-dropdown-item>
        <we-dropdown-item command="logout"
          class="portal-font-color-lv1 portal-font-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 drop__item">
          <i class="iconfont icon-logout logout__item"></i>
          <span class="logout__item text__ellipsis">
            {{ $Lan("CUS_TEMPLATE_CQUPT", "logout", "安全退出") }}
          </span>
        </we-dropdown-item>

      </we-dropdown-menu>
    </we-dropdown>
  </div>
</template>

<script>
export default {
  name: "Set",
  props: ["type", "currentUser", "pageData"],
  data () {
    return {}
  },
  computed: {
    pageRenderData () {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData?.pageInfoEntity?.wid,
      };
    },
    portalManager () {

      return this.pageRenderData.portalManager || {};
    },
  },
  methods: {
    handleCommand (name) {
      const nameMap = {
        personCenterUrl: this.$Lan(
          "SYS_TEMPLATE_WORK",
          "personcenter",
          "个人中心"
        ),
        postitionManagerUrl: this.$Lan("CUS_TEMPLATE_CQUPT", 'positionManage', '岗位管理'),
        accountManageUrl: this.$Lan(
          "SYS_TEMPLATE_WORK",
          "accountManage",
          "帐号管理"
        ),
        changeSites: this.$Lan("CUS_TEMPLATE_CQUPT", "changeSites", "切换站点"),
        backendManageUrl: this.$Lan(
          "SYS_TEMPLATE_WORK",
          "backManage",
          "后台管理"
        ),
        logout: this.$Lan("CUS_TEMPLATE_CQUPT", "logout", "注销"),
      };
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
          // 后台管理url要加上时间戳
          if (name === "backendManageUrl") {
            window.shell.openPage(
              `${this.portalManager[name]}?t=${new Date().valueOf()}`,
              1,
              2
            );
          } else {
            window.shell.openPage(this.portalManager[name], 1, 2);
          }
        }
      }
    },

  },
}
</script>

<style type="text/css" lang="less" scoped >
.set {
  position: absolute;

  border-radius: 17px;
  background: #edf2f6;
  &.expand {
    top: -10px;
    right: 23px;
    width: 24px;
    height: 24px;
    .we-icon-setting {
      line-height: 24px;
      cursor: pointer;
    }
  }
  &.fold {
    top: -13px;
    right: 4px;
    width: 20px;
    height: 20px;
    .we-icon-setting {
      line-height: 20px;
      cursor: pointer;
    }
  }
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
.login__btn {
  min-width: 58px;
  max-width: 120px;
  padding: 0 16px;
  height: 30px;
  border: 1px solid #fff;
  border-radius: 4px;
  color: #fff;
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
