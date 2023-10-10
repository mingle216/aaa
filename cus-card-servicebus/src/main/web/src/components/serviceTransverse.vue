<template>
  <!-- <AutoContainer :scroll="scroll1" :con-type="0" ref="parentContainer"> -->
  <div ref="father" class="serviceTransverse-father">
    <div id="cardName" class="portal-primary-color-lv1">{{tabPosition=='0' ? '在线服务' : '业务系统' }}</div>
    <EmptyCon
      :tip="$Lan(LanFunName, 'emptytip_data', '暂无服务')"
      :height="containerHeight"
      style="width: 100%"
      v-if="!sourceList || sourceList.length === 0"
    />
    <div class="serviceTransverse" v-else>
      <header  v-if="allClassifyData.length > 1">
        <div class="serviceTransverse-tablist" ref="containter">
          <ul
            ref="tablist"
            :style="{
              transform: `translate(${tabConfigure.translateX}px,1px)`,
            }"
          >
            <li
              v-for="(item, index) in sourceList"
              :key="index"
              @click="clickService(item.classifyName)"
              :class="{
                'dzline': selectedTypeId === item.classifyName,
                'color-bold':
                  selectedTypeId === item.classifyName,
                'dzline': selectedTypeId !== item.classifyName,
              }"
            >
              {{ item.classifyWid }}
            </li>
          </ul>
          <div class="tab-pn" v-if="tabConfigure.isShow">
            <span
              class="pn-prev portal-primary-backgroundcolor-lv5 portal-primary-border-color-lv4 portal-primary-color-lv1"
              @click="prev"
              :class="{ 'pn-disabled': tabConfigure.translateX === 0 }"
            >
              <i class="icon-dongdamoban-youjiantou iconfont"></i>
            </span>
            <span
              class="pn-next portal-primary-backgroundcolor-lv5 portal-primary-border-color-lv4 portal-primary-color-lv1"
              @click="next"
              :class="{
                'pn-disabled':
                  Math.abs(tabConfigure.translateX) ===
                  tabConfigure.maxDistance,
              }"
            >
              <i class="icon-dongdamoban-youjiantou iconfont"></i>
            </span>
          </div>
        </div>
        <span
          v-if="userInfo"
					@click="handleSubscribe"
          class="serviceTransverse-setting portal-primary-color-lv1 portal-primary-border-color-lv1 portal-primary-backgroundcolor-lv5"
          ><i class="iconfont icon-icon-dingyue"></i>更多{{tabPosition=='0' ? '服务' : '系统'}}</span
        >
      </header>
      <main style="margin-top:8px;">
        <AutoContainer
          :con-type="containerType"
          :con-height="containerHeight - 41"
          v-loading="appLoading"
          ref="parentContainer"
        >
          <EmptyCon
            tip="暂无数据"
            :height="200"
            style="width: 100%"
            v-if="serviceList.length === 0"
          />
          <template v-else>
            <ul v-for="(services, pIndex) in servicesData" :key="pIndex">
              <li
                v-for="(item, index) in services"
                :key="index"
                :style="itemWidth"
                
              >
                <div
                  class="service-list portal-primary-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 portal-font-color-lv1"
                  v-if="!item.serviceTabItemNoShow"
                  @click="openService(item)"
                >
                  <div
                    class="serviceTransverse-icon"
                    v-if="!item.serviceTabItemNoShow"
                  >
                  <!-- {{item.serviceDetail}}:src="'data:image/png;base64,'+ item.serviceDetail.pcIconUrl" -->
                    <img 
                    :src="item.serviceDetail.pcIconUrl"
                    alt="error"
                      @error="
                        () => {
                          item.serviceDetail.pcIconUrl = require('../assets/default.jpg');
                        }
                      "
                    />
                  </div>
                  <!-- <img :src="item.appIcon" alt="" v-err-img="errorImg" /> -->
               <div style="width:calc(100% - 86px);word-wrap:break-word;">
                  <we-tooltip
                    effect="dark"
                    :content="item.serviceName"
                    placement="bottom-start"
                    :open-delay="800"
                    
                  >
										<v-clamp autoresize :max-lines="2">{{ item.serviceName }}</v-clamp>
                  </we-tooltip>
                  </div>
                  <!-- <span class="favorite-box-icon">
                    <i
                      v-if="userInfo"
                      class="iconfont icon-favorites"
                      :class="{ active: item.appFavorite }"
                      @click.stop="collectApp(item.serviceDetail.appWid, item.appFavorite)"
                    ></i>
                  </span> -->
                </div>
              </li>
            </ul>
          </template>
        </AutoContainer>
      </main>
    </div>
  </div>
  <!-- </AutoContainer> -->
</template>
<script>
import { clickTrack } from '../mixins/track.js';
export default {
  mixins: [clickTrack],
  props: {
    moreLink:{
      type: String,
      default: "",
    },
    tabPosition:{
      type: String,
      default: "",
    },
    servicesData: {
      type: Array,
      default: () => {
        return [];
      },
    },
    sourceList: {
      type: Array,
      default: () => [],
    },
    serviceList: {
      type: Array,
      default: () => [],
    },
    allClassifyData: {
      type: Array,
      default: () => [],
    },
    inSelectedTypeId: {
      type: String,
      default: "",
    },
    containerType: {
      type: String,
      default: "",
    },
    containerHeight: {
      type: String,
      default: "",
    },
    appLoading: {
      type: Boolean,
      default: true,
    },
    itemWidth: {
      type: String,
      default: "width:154px",
    },
    router: Object,
  },
  watch: {
    inSelectedTypeId: {
      handler: "watchInSelectedTypeId",
      immediate: true,
    },
  },
  data() {
    return {
      LanFunName:'SYS_CAR_SERVICEBUS',
      selectedTypeId: "",
      errorImg: window.shell.ErrorImg,
      scroll1: { barKeepShow: true, scrollingX: true, scrollingY: false },
      userInfo: window.shell.getUserInfo(),
      tabConfigure: {
        isShow: false,
        maxDistance: "",
        sonWidth: "",
        fatherWidth: "",
        translateX: 0,
      },
    };
  },
  methods: {
    watchInSelectedTypeId(afVal) {
      this.selectedTypeId = afVal;
    },
    openService(item) {
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.serviceWid,
      })
      // if (!item.permission) {
      //   window.shell.showMessage({
      //     type: "warning",
      //     message: `${this.$Lan("public", "noPermission", "暂无使用权限，请联系管理员")}`,
      //   });
      //   return;
      // }
      window.shell.openService({ ...item, pcAccessUrl: item.serviceDetail.pcUrl, wid: item.serviceWid, name: item.serviceName });
    },
    collectApp(appId, appFavorite) {
      this.$emit("collect-app", {
        appId: appId,
        id: appId,
        operate: appFavorite ? 0 : 1,
        typeId: this.selectedTypeId,
      });
    },
    async clickService(typeId) {
      this.selectedTypeId = typeId;
      this.$emit("click-type", typeId);
      this.handleClickTrack({
        infoType: 8,
        filterInfo: {
          typeId
        },
      })
    },
    init() {
      this.$nextTick(() => {
        if (!this.$refs.tablist) {
          return;
        }
        this.tabConfigure.sonWidth = this.$refs.tablist.offsetWidth;
        this.tabConfigure.fatherWidth = this.$refs.containter.offsetWidth;
        let { sonWidth, fatherWidth } = this.tabConfigure;
        this.tabConfigure.isShow = sonWidth > fatherWidth;
        this.tabConfigure.maxDistance = sonWidth - fatherWidth;
        this.tabConfigure.translateX = 0;
      });
    },
    next() {
      let {
        sonWidth,
        fatherWidth,
        maxDistance,
        translateX,
      } = this.tabConfigure;
      if (Math.abs(translateX) === maxDistance) {
        return;
      }
      let remainingWidth = sonWidth - fatherWidth * 2 - Math.abs(translateX);
      if (remainingWidth > 0) {
        this.tabConfigure.translateX += -fatherWidth;
      } else {
        this.tabConfigure.translateX = -maxDistance;
      }
      this.handleClickTrack()
    },
    prev() {
      let { fatherWidth, translateX } = this.tabConfigure;
      if (translateX === 0) {
        return;
      }
      let remainingWidth = Math.abs(translateX) - fatherWidth;
      if (remainingWidth >= 0) {
        this.tabConfigure.translateX += fatherWidth;
      } else {
        this.tabConfigure.translateX = 0;
      }
      this.handleClickTrack()
    },
    getServiceDivWidth() {
      return this.$refs.father.offsetWidth;
    },
    refreshParent() {
      //this.$refs.parentContainer && this.$refs.parentContainer.refresh();
    },
    handleSubscribe() {
      if(this.moreLink!=''){
        window.open(this.moreLink)
      }
      
      // this.$parent.isShowTopics = true
      // this.handleClickTrack();
    }
  },
};
</script>
<style scoped lang='less'>
.serviceTransverse-father {
  
  min-width: 204px;
  display: flex;
}
#cardName{
    width: 50px;
    margin-right: 10px;
    font-size: 18px;
    writing-mode : tb-rl;
    background: #F5F5F5;
    line-height: 50px;
    text-align: center;
    font-weight: 900;
    background: #F8EFF0;
}
.serviceTransverse {
  width: calc(100% - 60px);
  // max-width: 100%;
  // min-width: 204px;
  .favorite-box-icon {
    width: 18px;
    display: block;
    height: 20px;
    margin-left:12px;
  }
  .ellsips-2 {
    display: -webkit-box;
    overflow: hidden;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  .serviceTransverse-icon {
    margin-right: 12px;
    height: 44px;
    > img {
      height: 44px;
      width: 44px;
    }
  }
  > header {
    height: 48px;
    display: flex;
    border-bottom: 1px solid #e7edf1;
    align-items: center;
    > .serviceTransverse-tablist {
      width: calc(100% - 68px);
      position: relative;
      overflow: hidden;

      > ul {
        white-space: nowrap;
        display: inline-block;
        transition: transform 1s;
        padding-right: 47px;
        height: 48px;
        display: flex;
        align-items: center;
         .color-bold {
            font-weight: bold;
            border-bottom: 4px solid #A40000;
          }
        > li {
          display: inline-block;
          /* font-size: 14px; */
          /* padding-bottom: 5px; */
          margin-right: 23px;
          // border-bottom: 2px solid transparent;
          border-radius: 1px;
          cursor: pointer;
          height: 48px;
          line-height: 48px;
          font-size: 18px;
          font-weight: 400;
         
        }
      }
      > .tab-pn {
        display: flex;
        position: absolute;
        top: 0;
        right: 0;
        z-index: 10;
        background: white;
        height: 40px;
        &::before {
          background-image: linear-gradient(
            90deg,
            rgba(255, 255, 255, 0) 3%,
            #ffffff 100%
          );
          content: "";
          height: 18px;
          width: 17px;
          position: absolute;
          top: 0;
          left: -17px;
        }
        > .pn-prev,
        .pn-next {
          border-radius: 2px;
          height: 18px;
          text-align: center;
          width: 18px;
          border: 1px solid transparent;
          cursor: pointer;
          justify-content: space-around;
          display: flex;
          align-items: center;
          > i {
            font-size: 12px;
            display: block;
          }
        }
        > .pn-prev {
          margin-right: 4px;
          transform: rotate(180deg);
          line-height: 18px;
          display: block;
          > i {
            transform: translateY(-1px);
          }
        }
        > .pn-disabled {
          border: 1px solid #dfe6ec !important;
          color: #dfe6ec !important;
          background-color: #fff !important;
          cursor: not-allowed;
        }
      }
    }
    > .tablist-width {
       width:100%!important;
    }
    > .serviceTransverse-setting {
      flex-shrink: 0;
      margin-left: 12px;
      text-align: center;
      padding: 4px 8px;
      font-size: 14px;
      margin-left: 12px;
      border-radius: 2px;
      cursor: pointer;
      height: 21px;
      transform: translateY(-2px);
      > i {
        margin-right: 2px;
        font-size: 12px;
      }
    }
  }
  > main {
    ul {
      display: flex;
      justify-content: space-between;
      // margin-bottom: 20px;
      // padding-top: 16px;
      > li {
        // width: 204px;
        min-width: 154px;
        height: 72px;
        > .service-list {
          border-radius: 4px;
          height: 100%;
          width: 100%;
          // margin-right: 25px;
          font-size:16px;

          display: flex;
          align-items: center;
          padding: 0 12px;
          cursor: pointer;
          .ellsips-appName {
            flex:1;
            overflow:hidden;
          }

          > h3 {
            // margin: 0 12px;
            font-size: 16px;
            flex: 1;
          }

          i {
            color: #ccd0d3;
            display: none;
          }
          i.active {
            color: #ffbc00;
          }
          &:hover {
            i {
              display: block;
            }
          }
        }
      }
    }
  }
  .dzline{

  }
}
</style>

