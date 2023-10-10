<template>
  <router-view />
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      isPageHide: false
    }
  },
  methods: {
    refreshPage(event) {
      //pageshow里的 persisted 属性 表示网页是否是来自缓存。  
      if (this.isPageHide && event.persisted) {
        window.location.reload();
      }
    },
    refreshPageHide() {
      this.isPageHide = true;
    }
  },
  created() {
    window.addEventListener("pageshow", this.refreshPage);
    window.addEventListener("pagehide", this.refreshPageHide);
  },
  beforeDestroy() {
    window.removeEventListener("pageshow", this.refreshPage);
    window.removeEventListener("pagehide", this.refreshPageHide);
  }
}
</script>

<style>
html, body {
  padding: 0;
  margin: 0;
  height: 100%;
  width: 100%;
  line-height: 0;
  overflow: hidden;
  box-sizing: border-box;
  font-size: 16px;
}
</style>
