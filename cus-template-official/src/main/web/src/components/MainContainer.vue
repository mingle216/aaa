<template>
  <we-main
    :class="[cardArea === 'default' ? 'main__center' : 'main__default',
    getpageName ? 'main__center2' : 'main__center', 
    getpageName1 ? 'main__center' : 'main__center', 
    
    ]"
    :style="cardArea === 'default' ? { padding: calPadding } : {}"
  >
    <slot></slot>
  </we-main>
</template>

<script>
import { isFirefox } from "../utils/util.js";
export default {
  props: {
    cardArea: {
      type: String,
      default: "all",
    },
    pageData: Object,
  },
  data() {
    return {
      pd: 600,
      isFirefox: isFirefox(),
    };
  },
  computed: {
    getpageName1(){
      if(this.pageData.renderData.breadcrumb.length==2 && this.pageData.renderData.breadcrumb[0].pageCode.indexOf('hall')!=-1 && this.pageData.renderData.breadcrumb[0].pageCode.indexOf('itemCategoryDetail')!=-1){
        return true
      }else{
        return false
      }
    },
    getpageName(){
      if(this.pageData.renderData.breadcrumb.length==1 && this.pageData.renderData.breadcrumb[0].pageCode.indexOf('hall')!=-1){
        return true
      }else{
        return false
      }
    },
    calPadding() {
      return this.isFirefox ? `12px ${this.pd}px 0` : `12px calc(50% - 37.5rem) 0`
    }
  },
  methods: {
    setFireFoxPadding() {
      const conEle = document.querySelectorAll(".tempalteConWrap"); // 模板主容器dom
      const conWidth =
        (conEle && conEle.length && conEle[0] && conEle[0].offsetWidth) ||
        document.body.offsetWidth;
      this.pd = conWidth / 2 - 600;
    },
  },
  mounted() {
    if (this.isFirefox && this.cardArea === 'default') {
      this.setFireFoxPadding();
      window.addEventListener("resize", this.setFireFoxPadding, false);
    }
  },
  beforeDestroy() {
    console.log("beforeDestroy");
    window.removeEventListener("resize", this.setFireFoxPadding, false);
  },
};
</script>

<style scoped>
.main__center {
  overflow: hidden;
  width: 0;
}
.main__center2 {
  width: 1200px;
  margin: 40px auto 0 auto;
  background: #fff;
  overflow: hidden;
  padding: 0!important;
}
.main__default{
  overflow-x: hidden;
}
</style>