<template>
  <div class="container" ref="container">
    <div class="container-top" ref="container-top"  v-if="$_IS_MOBILE && isShowBackButton && !isHideBack">
      <div class="go-history" @click="goHistortHandle">
        <img class="return-icon" :src="returnImg" />
      </div>
      {{ pageTitle }}
    </div>
    <div class="container-main" ref="container-main" >
      <iframe
        id="template-container"
        ref="frame"
        border="0"
        frameborder="0"
        class="template-container "
        :class="{'mobile-container':$_IS_MOBILE}"
        :src="templateId"
        @load="handleLoaded"
      />
    </div> 
  </div>
</template>

<script>

import globalObj from "../utils/global";
import Popup from './popup'
import blackAndWhiteMode from '../libs/mixins/blackAndWhiteMode.js'
// import Vconsole from 'vconsole';
// new Vconsole();
export default {
  components: {  },
  name: "Home",
  mixins: [blackAndWhiteMode],
  mounted(){
 
    if(this.$_IS_MOBILE && globalObj.isSafari()){
      // 解决ios返回缓存的问题
      window.onpageshow = function(event){
        // 页面从浏览器的缓存中读取
        if (event.persisted) {
          window.location.reload();
        }
      }
    }else {
      globalObj.on('startPopup', ()=> {
        new Popup(globalObj.getPageData(), globalObj.getUserInfo()).init({notice:this.$Lan('public', 'notice', '公告'),noTip:this.$Lan('public', 'noTip', '下次不再提醒')});
        globalObj.off('startPopup');
      })
    }
    
  },
   
  data() {
    return {
      templateId: "",
      pageTitle: "",
      returnImg: require("../assets/images/return.png"),
      isShowBackButton: false,
      isHideBack: false
    };
  },
  methods: {  
    goHistortHandle() {
      globalObj.emit("goBack");
      window.history.back(-1);
      globalObj.closeServiceModal();
    },
    handleLoaded() {
      const innerWindow = this.$refs.frame.contentWindow;
      innerWindow.shell = globalObj;
      innerWindow.postMessage("env-ready", "*");
    },
    update() {
      this.isShowBackButton = globalObj.getPageData().isShowBackButton ? true : false;
      if (process.env.NODE_ENV === "development") {
        this.templateId =
          "/" + globalObj.getPageData().templateEntity.templateId;
      } else {
        if (this.$_IS_MOBILE) {
          this.templateId =
            "/" +
            globalObj.getPageData().templateEntity.templateId +
            "/mobile/index.html";
        } else {
          this.templateId =
            "/" +
            globalObj.getPageData().templateEntity.templateId +
            "/pc/index.html";
        }
      }
      globalObj.setThemeColor(document, globalObj.getPageData());
    },
  },
  beforeRouteUpdate(to, from, next) {
    globalObj.updateRouter(to.path);
    next();
  },
  created() {
      
    globalObj.updateRouter(this.$route.path);
    globalObj.on("page-title-change", (val) => {
      this.pageTitle = val;
    });
    globalObj.on("page-change", () => {
      this.update();
      this.setGrayMode(globalObj.getPageData()?.blackAndWhiteMode);
    });
    globalObj.on("copy-text", (text) => {
      this.$copyText(text).then(
        () => {
          globalObj.showMessage({
            type: "success",
            message: this.$Lan('public', 'copySuccess', '复制成功'),
          });
        },
        () => {
          globalObj.showMessage({
            type: "error",
            message: this.$Lan('public', 'copyFail', '复制失败'),
          });
        }
      );
    })

    //唐素林 url中有isHideBack 隐藏头部
    if(globalObj.getUrlParam() && typeof globalObj.getUrlParam().isHideBack!='undefined' && globalObj.getUrlParam().isHideBack ==1){
        this.isHideBack =true
    }else {
       this.isHideBack =false
    }
      // 收藏的服务 弹窗
      // globalObj.on("more-favoriteApp", (val) => {
      //   this.moreFavoriteAppShow = val;
      // });
  },
  beforeDestroy() {
    globalObj.off("page-change");
    globalObj.off("copy-text");
  },
};
</script>

<style scoped >
.container {
  height: 100%;
  border: none;
  display: flex;
  flex-flow: column;
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
  box-sizing: border-box;
}

.container-top {
  touch-action: none;
  height: 2.625rem;
  /* min-height: 36px; */
  line-height: 2.625rem;
  width: 100%;
  box-sizing: border-box;
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
  text-align: center;
  font-size: 1.125rem;
  color: #102645;
  position: relative;
  padding: 0 3.5rem;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.go-history {
  position: absolute;
  height: 100%;
  width: 2.5rem;
  top: 0;
  line-height: 2.625;
  left: 0;
}
.return-icon {
  height: 1.125rem;  
}
.container-main {
  flex: 1;
  width: 100%;
  position: relative;
  z-index: 10;
  min-height: 0;
  /* overflow-y: scroll; */
}
.template-container {
  width: 100%;
  height: 100%;
  border: none;
  position: relative;
  z-index: 100;
}
.mobile-container{
  overflow:scroll;
  -webkit-overflow-scrolling: touch;
  min-width: 100%;
  *width:100%;
  width:1px;
  cursor: pointer;
  pointer-events:auto;
}
.iframe-lv{
  z-index: 10000;
}
iframe {
  display: block;
}
</style>
