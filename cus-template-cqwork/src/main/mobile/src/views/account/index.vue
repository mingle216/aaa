<template>
  <div class="account-manage">
    <div class="head">
      <we-icon
        name="arrow-left"
        size="22"
        color="#A19797"
        class="arrow"
        @click="closeDialog"
      />
      <span class="title">
        {{ $Lan("SYS_TEMPLATE_OFFICIAL_h5", "accountManage", "帐号管理") }}
      </span>
    </div>

    <div class="content">
      <div class="item" @click="selectLanguage">
        <span>
          {{ $Lan("SYS_TEMPLATE_OFFICIAL_h5", "Language", "语言") }}
        </span>

        <div class="pos-right">
          <span>{{ langName }}</span>
          <we-icon
            name="arrow"
            size="22"
            color="#A19797"
            class="arrow-right"
            v-if="supportLanguages.length > 1"
          />
        </div>
      </div>
      <div class="item" @click="logout" style="margin-top: 16px">
        {{ $Lan("SYS_TEMPLATE_OFFICIAL_h5", "logout", "退出登录") }}
      </div>
    </div>
    <we-action-sheet v-model="languageShow" get-container="body">
      <div class="we-action-sheet__content">
        <button
          type="button"
          class="we-action-sheet__item"
          v-for="item in supportLanguages"
          :key="item.langCode"
          @click="onSelect(item)"
        >
          <span
            :class="{
              'we-action-sheet__name': true,
              'portal-primary-color-lv1': $i18n.locale === item.langCode
            }"
          >
            {{ item.langName }}
          </span>
        </button>
      </div>
    </we-action-sheet>
  </div>
</template>

<script>
export default {
  props: ["currentUser", "showFooter"],
  data() {
    return {
      languageShow: false
    };
  },
  computed: {
    supportLanguages() {
      let lanarr =
        (this.currentUser && this.currentUser.supportLanguages) || [];
      if (lanarr.length > 0) {
        lanarr.forEach(tem => {
          tem.name = tem.langName;
        });
      }

      return lanarr;
    },
    langName() {
      const temp = this.supportLanguages.find(
        el => el.langCode === this.$i18n.locale
      );
      return (temp && temp.langName) || "中文";
    },
    lanactions() {
      let arr = this.supportLanguages.forEach(tem => {
        tem.name = tem.langCname;
      });

      return arr;
    },
    selectedSite() {
      const site = window.shell.getCurrentSite();
      return site?.siteName;
    }
  },
  methods: {
    closeDialog() {
      this.$emit("closeAccount");
    },
    onSelect(item) {
      window.shell.setLanguage(item.langCode);
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 10,
            filterInfo: this.supportLanguages.find(curr => curr.langCode === item.langCode)
          }
        },
        startTime: new Date().getTime()
      });
    },

    selectLanguage() {
      if (this.supportLanguages.length < 2) {
        return;
      }
      this.languageShow = true;
    },
    changeSites() {
      this.siteVisible = true;
    },
    switchSite(item) {
      window.shell.changeSite(item.siteRoute);
    },
    logout() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 7,
            filterInfo: {
              personalCenterName: "logout"
            }
          }
        },
        startTime: new Date().getTime()
      });
      const location = window.shell.getLocation();
      const page = location.hash.replace("#", "");
      window.shell.logout({
        params: {
          localHref: page
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
.account-manage {
  background: #f5f5f5;
  height: 100%;
  .head {
    width: 100%;
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #d1d1d1;
    text-align: center;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    background: #ffffff;

    .title {
      font-size: 18px;
    }
    .we-icon-arrow-left {
      position: absolute;
      left: 6px;
    }
  }
  .content {
    .item {
      width: 100%;
      height: 40px;
      background: #ffffff;
      font-size: 14px;
      display: flex;
      align-items: center;
      padding-left: 16px;
      position: relative;
      .pos-right {
        position: absolute;
        right: 16px;
        display: flex;
        align-items: center;
        .ellipsis {
          max-width: 200px;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
      .arrow-right {
        margin-left: 4px;
      }
    }
  }
}
.sheet-accounts {
  margin-bottom: 56px;
}
/deep/.we-action-sheet__name {
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  display: inline-block;
}
</style>
