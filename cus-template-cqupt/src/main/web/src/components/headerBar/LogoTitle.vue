<template>
  <div class="header__title">
    <div
      class="logo"
      v-if="logoUrl"
    >
      <img :src="logoUrl" alt="" height="50" />
    </div>
    <span class="header-line"></span>
    <span class="navbar__title">一网通办</span>
    <!--<img class="header-img" src="../../assets/images/title.png" alt="">-->
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
  height:44px;
}
.header-img{
  height: 20px;
}
.header__title .logo img {
  vertical-align: middle;
  width: 162px;
  height: auto;
  max-height: 38px;
}
.header-line{
  margin: 0 12px;
  width: 1px;
  height: 30px;
  background: rgba(255, 255, 255, 0.2);
}
.navbar__title {
  max-width: 260px;
  font-size: 21px;
  line-height: 22px;
  color: #fff;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
  font-family: 'Microsoft YaHei UI';
}
@media screen and (max-width: 1281px) {
  .navbar__title {
    max-width: 104px;
  }
}
</style>
