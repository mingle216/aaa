<template>
  <AutoContainer :scroll="scroll1" ref="parentContainer" :con-type="containerType" :con-height="containerHeight">
    <div ref="father" class="serviceTransverse-father">
      <EmptyCon :tip="$Lan(LanFunName, 'emptytip_data', '暂无服务')" :height="(containerHeight - 12)" style="width: 100%"
        v-if="!sourceList || sourceList.length === 0" />
      <div class="serviceTransverse" v-else>
        <header v-if="allClassifyData.length">
          <div class="serviceTransverse-tablist" ref="containter">
            <ul ref="tablist" :style="{
              transform: `translate(${tabConfigure.translateX}px,1px)`,
            }">
              <li v-for="(item, index) in sourceList" :key="index" @click="clickService(item.typeId)" :class="{
                'portal-primary-color-lv1': selectedTypeId === item.typeId,
                'color-bold':
                  selectedTypeId === item.typeId,
                'portal-font-color-lv2': selectedTypeId !== item.typeId,
              }">
                {{ item.typeName }}
              </li>
            </ul>
            <div class="tab-pn" v-if="tabConfigure.isShow">
              <span
                class="pn-prev portal-primary-backgroundcolor-lv5 portal-primary-border-color-lv4 portal-primary-color-lv1"
                @click="prev" :class="{ 'pn-disabled': tabConfigure.translateX === 0 }">
                <i class="icon-dongdamoban-youjiantou iconfont"></i>
              </span>
              <span
                class="pn-next portal-primary-backgroundcolor-lv5 portal-primary-border-color-lv4 portal-primary-color-lv1"
                @click="next" :class="{
                  'pn-disabled':
                    Math.abs(tabConfigure.translateX) ===
                    tabConfigure.maxDistance,
                }">
                <i class="icon-dongdamoban-youjiantou iconfont"></i>
              </span>
            </div>
          </div>
          <span v-if="allClassifyData.length > 1 && userInfo && showSubscribe" @click="handleSubscribe"
            class="serviceTransverse-setting portal-primary-color-lv1 portal-primary-border-color-lv1 portal-primary-backgroundcolor-lv5"><i
              class="iconfont icon-icon-dingyue"></i>{{ $Lan(LanFunName, 'operate', '订阅') }}</span>
        </header>
        <main style="margin-top:8px;">
          <AutoContainer :con-type="containerType" :con-height="containerHeight - 41" v-loading="appLoading"
            ref="parentContainer">
            <EmptyCon :tip="$Lan(LanFunName, 'emptytip', '该分类下无服务')" :height="200" style="width: 100%"
              v-if="serviceList.length === 0" />
            <template v-else>
              <ul v-for="(services, pIndex) in servicesData" :key="pIndex">
                <li v-for="(item, index) in services" :key="index" :style="itemWidth">
                  <div class="service-list portal-primary-backgroundcolor-hover-lv5" v-if="!item.serviceTabItemNoShow"
                    :class="{
                      'portal-font-color-lv1': item.permission,
                      'portal-font-color-lv4': !item.permission,
                      'portal-primary-color-hover-lv1': item.permission,
                    }" @click="openService(item)">
                    <div class="serviceTransverse-icon" :class="{ 'no-permission-service': !item.permission }"
                      v-if="!item.serviceTabItemNoShow">
                      <img :src="item.appIcon" alt="error" @error="
                        () => {
                          item.appIcon = require('../assets/default.jpg');
                        }
                      " />
                    </div>
                    <!-- <img :src="item.appIcon" alt="" v-err-img="errorImg" /> -->
                    <div style="width:calc(100% - 86px);word-wrap:break-word;">
                      <we-tooltip effect="dark" :content="item.appName" placement="bottom-start" :open-delay="800">
                        <v-clamp autoresize :max-lines="2">{{ item.appName }}</v-clamp>
                      </we-tooltip>
                    </div>
                    <span class="favorite-box-icon">
                      <i v-if="userInfo" class="iconfont icon-favorites" :class="{ active: item.appFavorite }"
                        @click.stop="collectApp(item.appId, item.appFavorite)"></i>
                    </span>
                  </div>
                </li>
              </ul>
            </template>
          </AutoContainer>
        </main>
      </div>
    </div>
  </AutoContainer>
</template>
<script>
import { clickTrack } from '../mixins/track.js';
export default {
  mixins: [clickTrack],
  props: {
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
    showSubscribe: {
      type: Boolean,
      default: false
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
      LanFunName: 'SYS_CAR_SERVICEBUS',
      selectedTypeId: "",
      errorImg: window.shell.ErrorImg,
      scroll1: { barKeepShow: true },
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
        serviceId: item.appId,
      })
      if (!item.permission) {
        window.shell.showMessage({
          type: "warning",
          message: `${this.$Lan("public", "noPermission", "暂无使用权限，请联系管理员")}`,
        });
        return;
      }
      window.shell.openService({ ...item, pcAccessUrl: item.url, wid: item.appId, name: item.appName });
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
      this.$parent.isShowTopics = true
      this.handleClickTrack();
    }
  },
};
</script>
<style scoped lang='less'>
.serviceTransverse-father {
  max-width: 100%;
  min-width: 204px;
}

.serviceTransverse {

  // max-width: 100%;
  // min-width: 204px;
  .favorite-box-icon {
    width: 18px;
    display: block;
    height: 20px;
    margin-left: 12px;
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

    >img {
      height: 44px;
      width: 44px;
    }
  }

  >header {
    height: 41px;
    display: flex;
    padding-top: 12px;

    border-bottom: 1px solid #f0f0f0;

    >.serviceTransverse-tablist {
      width: calc(100% - 68px);
      position: relative;
      overflow: hidden;

      >ul {
        white-space: nowrap;
        display: inline-block;
        transition: transform 1s;
        padding-right: 47px;

        .color-bold {
          font-weight: bold;
        }

        >li {
          display: inline-block;
          font-size: 14px;
          padding-bottom: 5px;
          margin-right: 23px;
          // border-bottom: 2px solid transparent;
          border-radius: 1px;
          cursor: pointer;

        }
      }

      >.tab-pn {
        display: flex;
        position: absolute;
        top: 0;
        right: 0;
        z-index: 10;
        background: white;
        height: 40px;

        &::before {
          background-image: linear-gradient(90deg,
              rgba(255, 255, 255, 0) 3%,
              #ffffff 100%);
          content: "";
          height: 18px;
          width: 17px;
          position: absolute;
          top: 0;
          left: -17px;
        }

        >.pn-prev,
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

          >i {
            font-size: 12px;
            display: block;
          }
        }

        >.pn-prev {
          margin-right: 4px;
          transform: rotate(180deg);
          line-height: 18px;
          display: block;

          >i {
            transform: translateY(-1px);
          }
        }

        >.pn-disabled {
          border: 1px solid #dfe6ec !important;
          color: #dfe6ec !important;
          background-color: #fff !important;
          cursor: not-allowed;
        }
      }
    }

    >.tablist-width {
      width: 100% !important;
    }

    >.serviceTransverse-setting {
      flex-shrink: 0;
      margin-left: 12px;
      text-align: center;
      padding: 4px 8px;
      font-size: 12px;
      margin-left: 12px;
      border-radius: 2px;
      cursor: pointer;
      height: 21px;
      transform: translateY(-2px);

      >i {
        margin-right: 2px;
        font-size: 12px;
      }
    }
  }

  >main {
    ul {
      display: flex;
      justify-content: space-between;

      // margin-bottom: 20px;
      // padding-top: 16px;
      >li {
        // width: 204px;
        min-width: 154px;
        height: 72px;

        >.service-list {
          border-radius: 4px;
          height: 100%;
          width: 100%;
          // margin-right: 25px;
          font-size: 16px;

          display: flex;
          align-items: center;
          padding: 0 12px;
          cursor: pointer;

          .ellsips-appName {
            flex: 1;
            overflow: hidden;
          }

          >h3 {
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
}
</style>

