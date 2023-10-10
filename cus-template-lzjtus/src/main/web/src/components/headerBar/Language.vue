<template>
  <div v-if="supportLanguages.length > 1 && currentUser">
    <we-dropdown @command="handleCommand" placement="bottom">
      <span class="we-dropdown-link">
        <i class="iconfont icon-duoyuyan" />
        <span class="text__ellipsis language__name" :title="langName">
          {{ langName }}
        </span>
        <i
          class="we-icon-caret-bottom we-icon--right"
          style="font-size: 12px;margin-top:2px"
        />
      </span>
      <we-dropdown-menu slot="dropdown">
        <we-dropdown-item
          :command="item.langCode"
          class="portal-primary-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1"
          v-for="(item,index) in supportLanguages"
          :key="item.langCode+index"
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
  props: ['pageRenderData', 'currentUser'],
  computed: {
    supportLanguages() {
      return (this.currentUser && this.currentUser.supportLanguages) || []
    },
    langName() {
      const temp = this.supportLanguages.find(el => el.langCode === this.$i18n.locale)
      return temp && temp.langName || '中文'
    }
  },
  methods: {
    handleCommand(name) {
       window.shell.setLanguage(name);
       this.handleClickTrack(this.supportLanguages.find(item => item.langCode === name)); // 点击埋点
    },
    // 点击埋点
    handleClickTrack(item) {
      const pageInfoEntity = window.shell.getPageData().pageInfoEntity;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        startTime: new Date().getTime(),
        actionParams: {
          pageCode: pageInfoEntity.pageCode,
          pageName: pageInfoEntity.pageName,
          extraInfo: {
            infoType: 10,
            filterInfo: item
          }
        },
      });
    }
  }
}
</script>

<style scoped>
.we-dropdown-link {
  cursor: pointer;
  color: rgba(255, 255, 255, 0.6);
  max-width: 100px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.language__name {
  padding-left: 5px;
  font-size: 16px;
  line-height: 24px;
}
.drop__item {
  min-width: 128px;
  white-space:nowrap;
  display: flex;
  align-items: center;
  justify-content: space-between;
  
}
.lan-name{
  max-width: 200px;
  display: inline-block;
}
/deep/.we-dropdown-menu__item {
  padding: 0 12px;
}
</style>
