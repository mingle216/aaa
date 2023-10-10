<template>
  <ul
    class="portal-card-btn"
    :class="[isshow ? `portal-card-btn-block-${cardLinkData.length}` : '']"
  >
    <li
      v-for="(btn, index) in cardLinkData"
      :key="`${btn.linkUrl}_${index}`"
      @click="open(btn.linkUrl)"
    >
      <!--<we-tooltip
        placement="top"
        class="item"
        effect="dark"
        :content="btn.linkTitle"
        :open-delay="800"
      >
       -->
      <span class="portal-primary-color-lv1  portal-card-btn-item"
        ><i class="iconfont m-r-3" :class="btn.linkIcon"></i
        ><span class='ellipsis' :title="linkTitle(btn)">{{ linkTitle(btn) }}</span> </span
      >
      <!--</we-tooltip> -->
    </li>
  </ul>
</template>

<script>
/* eslint-disable no-debugger */

export default {
  name: "cardLink",
  props: {
    cardData: Object,
    cardLinkData: Array,
    isshow: Boolean,
  },
  computed: {
    linkTitle() {
      return (btn) => {
        const zhTitle = btn.linkTitle || ''
        const linkLang = btn.linkLang || []
        const temp = linkLang.find(el => el.langCode === this.$i18n.locale)
        return temp && temp.langName || zhTitle
      }
    }
  },
  methods: {
    open(url) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.cardData.cardWid,
          cardId: this.cardData.cardId,
          cardName: this.cardData.cardName
        },
        startTime: new Date().getTime()
      });
      document.activeElement.blur();
      window.shell.openPage(url, 1, 2);
    },
  },
};
</script>

<style scoped>
.portal-card-btn-item:focus {
  outline: none;
}
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.align-center {
  display: flex;
  align-items: center;
}
.m-r-3 {
  margin-right: 3px;
}
.portal-card-btn {
  position: absolute;
  right: 0;
  top: 0;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 0px;
  z-index: 10;
  transition: max-width 0.2s;
  font-size: 14px;
  overflow: hidden;
}
.portal-card-btn > li {
  display: flex;
  align-items: center;
}
.portal-card-btn > li > span {
  max-width: 112px;
  display: inline-flex;
  align-items: center;
  padding-right: 7px;
  height: 100%;
  cursor: pointer;
  font-weight: 400;
}
.portal-card-btn > li:last-child > span {
  padding-right: 0px;
}

.portal-card-btn > li:last-child {
  margin-right: 0px;
}
.portal-card-btn-block-1,
.portal-card-btn-block-2,
.portal-card-btn-block-3 {
  max-width: 336px;
}
li:focus {
  outline: none;
}
</style>
