<template>
  <div class="header__title">
    <div class="logo" v-if="logoUrl">
      <img :src="logoUrl" alt="" height="50" />
    </div>
    <we-divider v-if="logoUrl && title" direction="vertical" />
    <div v-if="logoUrl && !title" style="margin-right:15px"></div>
    <span
      v-if="title"
      class="navbar__title"
      :title="title && title.replace(/<\/?.+?>/g, '')"
      :style="{marginRight: title ? '15px' :'0'}"
      v-html="title"
    ></span>
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
      const temp = (this.navbarTitle || []).find(el => el.langKey === this.$i18n.locale)
      const zhName = (this.navbarTitle || []).find(el => el.langKey === 'zh_CN')
      return temp?.langValue || zhName?.langValue
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
  max-width: 200px;
  min-width: 160px;
  height: auto;
  max-height: 50px;
}

/deep/.we-divider--vertical {
  height: 26px;
  opacity: 0.4;
  background: #E6E6F2;
  margin: 0 12px 0 16px;
}

.navbar__title {
  width: 79px;;
  line-height: 18px;
  color: #fff;
  text-align: center;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  word-break: break-all;
}
</style>
