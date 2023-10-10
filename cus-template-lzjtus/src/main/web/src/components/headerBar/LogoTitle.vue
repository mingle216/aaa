<template>
  <div class="header__title">
    <div class="logo">
      <img :src="logoUrl" alt="" height="50" />
    </div>
    <we-divider v-if="logoUrl" direction="vertical" />
    <div v-else style="margin: 0 20px"></div>
    <!-- <span
      class="navbar__title"
      :title="title && title.replace(/<\/?.+?>/g, '')"
      :style="{marginRight: title ? '40px' :'0'}"
      v-html="title"
    ></span> -->
  </div>
</template>

<script>
export default {
  props: {
    navbarTitle: Array,
    logoImgSrc: Object,
    pageCode: String
  },
  computed: {
    title() {
      const zhName = (this.navbarTitle || []).find(el => el.langKey === 'zh_CN')
      const temp = (this.navbarTitle || []).find(el => el.langKey === this.$i18n.locale)
      return temp?.langValue || zhName?.langValue;
    },
    logoUrl() {
      const sColor = this.logoImgSrc && this.logoImgSrc.logoUrl || ''
      const bColor = this.logoImgSrc && this.logoImgSrc.whiteLogoUrl || ''
      return this.pageCode === 'blank' ? sColor : bColor
    }
  }
}
</script>

<style scoped>
.header__title {
  display: flex;
  align-items: center;
}

.header__title .logo img {
  vertical-align: middle;
  width: 190px;
  height: 48px;
}

/deep/.we-divider--vertical {
  height: 26px;
  opacity: 0.4;
  background: #E6E6F2;
  margin: 0 20px;
}

.navbar__title {
  max-width: 90px;
  line-height: 18px;
  color: #fff;
  text-align: center;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
