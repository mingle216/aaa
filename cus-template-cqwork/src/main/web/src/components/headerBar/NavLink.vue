<template>
  <div class="nav">
    <div class="navItem" v-for="(item, index) in headerNavBar" :key="index">
      <span
        class="navItem__name"
        @click="openPage(item, 1, 2)"
        style="cursor: pointer"
      >
        {{ getZhTitle(item) }}
      </span>
      <div class="divide" v-if="index !== headerNavBar.length - 1"></div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["headerNavBar"],
  methods: {
    openPage(item, openType, menuType) {
      this.handleClickTrack(this.getZhTitle(item.title));
      window.shell.openPage(item.url, openType, menuType);
    },
    /**
     * @description 获取中文名称
     * @param {Array|String} title 导航名称
     */
    getZhTitle(item) {
      const title = item.title;
      if (Array.isArray(title)) {
        const zhName = (title || []).find((el) => el.langKey === "zh_CN") || {};
        const zhTitle = zhName.langValue;
        const temp = title.find((el) => el.langKey === this.$i18n.locale);
        return (temp && temp.langValue) || zhTitle;
      } else {
        return title;
      }
    },
    // 点击埋点
    handleClickTrack(name) {
      const pageInfoEntity = window.shell.getPageData().pageInfoEntity;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        startTime: new Date().getTime(),
        actionParams: {
          pageCode: pageInfoEntity.pageCode,
          pageName: pageInfoEntity.pageName,
          extraInfo: {
            infoType: 6,
            filterInfo: {
              navigationName: name,
            },
          },
        },
      });
    },
  },
};
</script>

<style lang="less" scoped>
.nav {
  display: flex;
  align-items: center;
  .divide {
    height: 16px;
    width: 1px;
    background: #fff;
    display: inline-block;
    margin: 0 8px;
    opacity: 0.5;
  }
  .navItem__name {
    color: #fff;
    font-size: 14px;
    line-height: 22px;
  }
}
.navItem {
  display: flex;
  align-items: center;
}
</style>
