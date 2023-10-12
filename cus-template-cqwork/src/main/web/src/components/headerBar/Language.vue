<template>
  <div v-if="supportLanguages.length > 1 && currentUser">
    <we-dropdown @command="handleCommand" placement="bottom">
      <span class="icon iconfont icon-duoyuyan1 we-dropdown-link"> </span>
      <we-dropdown-menu slot="dropdown">
        <we-dropdown-item
          :command="item.langCode"
          class="portal-primary-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1"
          v-for="(item, index) in supportLanguages"
          :key="item.langCode + index"
        >
          <div class="drop__item">
            <span class="lan-name text__ellipsis">{{ item.langName }}</span>
            <i
              class="we-icon-check portal-primary-color-lv1"
              style="font-weight:bold"
              v-if="item.langCode === $i18n.locale"
            />
          </div>
        </we-dropdown-item>
      </we-dropdown-menu>
    </we-dropdown>
  </div>
</template>

<script>
export default {
  props: ["pageRenderData", "currentUser"],
  computed: {
    supportLanguages() {
      return (this.currentUser && this.currentUser.supportLanguages) || [];
    },
    langName() {
      const temp = this.supportLanguages.find(
        (el) => el.langCode === this.$i18n.locale
      );
      return (temp && temp.langName) || "中文";
    },
  },
  methods: {
    handleCommand(name) {
      window.shell.setLanguage(name);
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 10,
            filterInfo: this.supportLanguages.find(item => item.langCode === name)
          },
        },
        startTime: new Date().getTime(),
      });
    },
  },
};
</script>

<style scoped>
.we-dropdown-link {
  cursor: pointer;
  color: #fff;
}
.language__name {
  padding-left: 5px;
  font-size: 16px;
  line-height: 24px;
}
.drop__item {
  min-width: 128px;
  white-space: nowrap;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.lan-name {
  max-width: 200px;
  display: inline-block;
}
/deep/.we-dropdown-menu__item {
  padding: 0 12px;
}
</style>
