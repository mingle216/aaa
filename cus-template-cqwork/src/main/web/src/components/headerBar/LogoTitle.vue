<template>
  <div class="header__title">
    <div
      class="logo"
      v-if="logoUrl"
      :style="{
        marginRight: !title ? '40px' : '0',
      }"
    >
      <img :src="logoUrl" alt="" height="50" />
    </div>
    <div v-if="logoUrl && !title" style="margin-right:40px"></div>
    <span
      v-if="title"
      class="navbar__title"
      :title="title && title.replace(/<\/?.+?>/g, '')"
      :style="{
        marginRight: title ? '40px' : '0',
        marginLeft: logoUrl ? '15px' : '0',
      }"
      v-html="title"
    ></span>
  </div>
</template>

<script>
export default {
  props: {
    navbarTitle: Array,
    logoImgSrc: Object,
    pageCode: String,
  },
  computed: {
    title() {
      const temp = (this.navbarTitle || []).find(
        (el) => el.langKey === this.$i18n.locale
      );
      const zhName = (this.navbarTitle || []).find(
        (el) => el.langKey === "zh_CN"
      );
      return temp?.langValue || zhName?.langValue;
    },
    logoUrl() {
      const sColor = (this.logoImgSrc && this.logoImgSrc.logoUrl) || "";
      const bColor = (this.logoImgSrc && this.logoImgSrc.whiteLogoUrl) || "";
      return this.pageCode === "blank" ? sColor : bColor;
    },
  },
};
</script>

<style scoped>
.header__title {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.header__title .logo img {
  vertical-align: middle;
  width: 162px;
  height: auto;
  max-height: 50px;
}

.navbar__title {
  max-width: 260px;
  line-height: 36px;
  font-size: 26px;
  color: #fff;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
}
@media screen and (max-width: 1281px) {
  .navbar__title {
    max-width: 104px;
  }
}
</style>
