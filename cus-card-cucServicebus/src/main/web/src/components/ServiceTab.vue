<template>
  <!-- <AutoContainer
    :scroll="scroll1"
    :con-type="0"
    :con-height="containerHeight"
    ref="parentContainer"
  > -->
  <div class="card-container portal-font-color-lv1" style="min-width: 250px">
    <!--空数据-->
    <!--    <div class="card-empty no-data-min-div-g" >-->
    <!--      <div class="card-empty-title">暂无服务</div>-->
    <!--    </div>-->
    <AutoContainer :scroll="scrollTab" :con-type="containerType" :con-height="containerHeight"
      v-if="!sourceList || sourceList.length === 0">
      <EmptyCon :tip="$Lan(LanFunName, 'emptytip_data', '暂无服务')" :height="containerHeight" style="width: 100%" />
    </AutoContainer>

    <!--tabs-->
    <AutoContainer :scroll="scrollTab" :con-type="containerType" :con-height="containerHeight" class="auto-tabs">
      <div class="source-list" v-if="sourceList && sourceList.length && allClassifyData.length > 0">
        <div class="source-item portal-font-color-lv2" :class="showChecked(item.typeId)"
          v-for="(item, index) in aa" :key="index" @click="clickService(item.typeId)"
          @mouseenter="mouseenterType(item.typeId)" @mouseleave="mouseleaveType(item.typeId)">
          <div class="source-item-title" :class="{
            'portal-primary-color-lv1': selectedTypeId === item.typeId,
          }">
            <span class="card-title-ellipsis source-color-size">{{
                item.typeName
              }}</span>
            <!-- <we-tooltip class="item" effect="dark" :content="item.typeName" :open-delay="800" :hide-after="500"
              placement="bottom">
            
            </we-tooltip> -->
            <!-- <span v-if="item.count" class="card-title-num">{{
              item.count
            }}</span> -->
          </div>
        </div>
        <span v-if="allClassifyData.length > 1 && isLogin && showSubscribe" class="setting portal-primary-color-lv1"
          @click="handleSubscribe"><i class="iconfont icon-icon-dingyue"></i>{{ $Lan(LanFunName, "operate", "订阅")
          }}</span>
      </div>

      <EmptyCon :tip="$Lan(LanFunName, 'emptytip_data', 'emptytip_data')" :height="containerHeight" style="width: 100%"
        v-else />
    </AutoContainer>



    <AutoContainer :scroll="scroll" :con-type="containerType" :con-height="containerHeight" :style="getWidth()"
      v-loading="appLoading" v-if="
        sourceList && sourceList.length >= 1 && serviceList.length >= 0
      ">
      <div class="task-list" ref="taskList">
        <!--空数据 -->
        <EmptyCon :tip="$Lan(LanFunName, 'emptytip', '该分类下无服务')" :height="(containerHeight - 12)" style="width: 100%"
          v-if="sourceList.length > 1 && serviceList.length === 0" />
        <template v-else>
          <AutoContainer>
            <div class="rowStyle" v-for="(services, pIndex) in servicesData" :key="pIndex">
              <div v-for="(item, index) in services" :key="index" :style="itemWidth">
                <div class="app-item portal-primary-color-hover-lv1 portal-primary-backgroundcolor-hover-lv5"
                  v-if="!item.serviceTabItemNoShow" :class="[
                    'task-list-item-empty-type',
                    { 'task-list-item': sourceList && sourceList.length > 1 },
                  ]" :serviceName="item.appName" :style="itemWidth" @mouseenter="mouseenter(item.appId)"
                  @mouseleave="mouseleave(item.appId)" @click="openService(item)">
                  <div class="app-item-card">
                    <div class="app-item-img" :class="{ 'no-permission-service': !item.permission }">
                      <img class="app-img" :src="item.appIcon" v-err-img="errorImg" />
                    </div>
                    <div class="app-item-appName-Con" :class="getAppNameHoverWidth()">
                      <div class="app-item-appName">
                        <we-tooltip effect="dark" :content="item.appName" placement="bottom-start" :open-delay="800">
                          <a class="app-item-a" :class="
                            getAppItemAPrimaryColor(
                              item.appId,
                              item.permission
                            )
                          ">
                            <v-clamp autoresize :max-lines="2">{{
                              item.appName
                            }}</v-clamp>
                          </a>
                        </we-tooltip>
                      </div>
                    </div>
                    <div class="appfavorite-icon">
                      <div :class="getAppItemColl(item.appId)">
                        <a @click.stop="collectApp(item.appId, item.appFavorite)">
                          <i v-if="item.appFavorite" class="iconfont icon-favorites" style="color: #ffbc00"></i>
                          <i v-else class="iconfont icon-favorites" style="color: #ccd0d3"></i>
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
                <div v-if="item.serviceTabItemNoShow" :class="[
                  'task-list-item-empty-type',
                  { 'task-list-item': sourceList && sourceList.length > 1 },
                ]"></div>
              </div>
            </div>
          </AutoContainer>
        </template>
      </div>
    </AutoContainer>
  </div>
  <!-- </AutoContainer> -->
</template>

<script>
import { clickTrack } from '../mixins/track.js';
export default {
  name: "ServiceTab",
  mixins: [clickTrack],
  props: {
    servicesData: {
      type: Array,
      default: () => {
        return [];
      },
    },
    serviceList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    sourceList: {
      type: Array,
      default: () => {
        return [];
      },
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
  data() {
    return {
      isLogin: window.shell.getUserInfo() ? true : false,
      scroll: { barKeepShow: true, scrollCover: true },
      scrollTab: { barKeepShow: true },
      scroll1: { barKeepShow: true, scrollingX: true, scrollingY: false },
      selectedTypeId: "",
      mouseOverAppId: "",
      errorImg: window.shell.ErrorImg,
      mouseOverType: "",
      LanFunName: "SYS_CAR_SERVICEBUS",
      aa:[],
      //服务分组后的数据
      // servicesData: [],
    };
  },
  mounted(){
    this.aa=this.sourceList.filter(ele=> ele.count >0 );
    // console.log(this.aa,'serviceListserviceListserviceList');
  },
  methods: {
    mouseenter(appId) {
      this.mouseOverAppId = appId;
    },
    mouseenterType(typeId) {
      this.mouseOverType = typeId;
    },
    mouseleave() {
      this.mouseOverAppId = "";
    },
    mouseleaveType() {
      this.mouseOverType = "";
    },
    getWidth() {
      if (this.sourceList && this.sourceList.length > 1) {
        return "width: calc(100% - 152px);min-width:210px";
      }
      return "width:100%;min-width:250px";
    },
    getAppItemColl(appId) {
      if (this.mouseOverAppId === appId && this.isLogin) {
        return "app-item-coll-hover";
      } else {
        return "app-item-coll";
      }
    },
    getAppNameHoverWidth() {
      if (this.sourceList && this.sourceList.length > 1) {
        if (this.isLogin) {
          return "app-name-hover-width";
        } else {
          return "app-name-hover-width-login";
        }
      } else {
        return "app-name-hover-width-empty-type";
      }
    },
    getAppItemAPrimaryColor(appId, permission) {
      if (!permission) {
        return "portal-font-color-lv4";
      }
      if (this.mouseOverAppId === appId) {
        return "portal-primary-color-lv1";
      } else {
        return "portal-font-color-lv1";
      }
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
    collectApp(appId, appFavorite) {
      this.$emit("collect-app", {
        appId: appId,
        id: appId,
        operate: appFavorite ? 0 : 1,
        typeId: this.selectedTypeId,
      });
    },
    refreshParent() {
      //this.$refs.parentContainer.refresh();
    },
    showChecked(typeId) {
      if (this.selectedTypeId === typeId) {
        return "service-bus-source-item-active";
      } else if (this.mouseOverType === typeId) {
        return "portal-primary-color-lv1";
      } else {
        return "source-item-noactive";
      }
    },
    openService(item) {
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.appId,
      })
      if (!item.permission) {
        window.shell.showMessage({
          type: "warning",
          message: `${this.$Lan(
            "public",
            "noPermission",
            "暂无使用权限，请联系管理员"
          )}`,
        });
        return;
      }
      window.shell.openService({ ...item, pcAccessUrl: item.url, wid: item.appId, name: item.appName });
    },
    getAppItemLengthClass() {
      if (this.sourceList && this.sourceList.length > 1) {
        return "task-list-item ";
      } else {
        return "task-list-item " + "task-list-item-empty-type";
      }
    },
    /**
     *获取服务div的宽度
     */
    getServiceDivWidth() {
      return this.$refs.taskList.offsetWidth;
    },
    watchInSelectedTypeId(afVal) {
      this.selectedTypeId = afVal;
    },
    handleSubscribe() {
      this.$parent.isShowTopics = true
      this.handleClickTrack();
    }
  },
  watch: {
    inSelectedTypeId: {
      handler: "watchInSelectedTypeId",
      immediate: true,
    },
  },
};
</script>

<style scoped lang="less">
*:focus {
  outline: none;
}

.setting {
  display: flex;
  align-items: center;
  margin: 16px 0;

  i {
    // background: url("../assets/icon-管理收藏@3x.png") no-repeat center/100% 100%;
    // content: "";
    // height: 16px;
    // width: 16px;
    font-size: 16px;
    display: inline-block;
    vertical-align: middle;
    margin-right: 4px;
  }

  font-size: 14px;
  color: #307afb;
  margin-left: 12px;
  cursor: pointer;
}

.task-list {
  overflow: auto;
  padding-top: 12px;
}

.source-list {
  height: auto;
}

.auto-tabs {
  width: 140px;
  min-width: 140px;
  margin-right: 12px;
  box-shadow: inset -1px 0 0 0 #f1f2f4;
  background: rgba(245, 245, 245, 1);
  padding: 20px 0;
}

.source-item {
  position: relative;
  font-size: 16px;
  cursor: pointer;
  height: 40px;
  line-height: 40px;
  display: flex;
  align-items: center;
  // justify-content: center;
  justify-content: flex-start;
  padding-left: 15px;
}

.source-item-noactive {
  color: #707d8f;
}

.source-item-title {
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
}

.card-title-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 20px;
}

.source-color-size {}

.card-title-num {
  min-width: 18px;
  height: 20px;
  line-height: 20px;
  border-radius: 10px;
  text-align: center;
  padding: 0 3px;
  font-size: 12px;
  white-space: nowrap;
}

// .task-list-item {
//   width: 210px !important;
// }

.task-list-item-empty-type {
  //width: 250px;
  height: 72px;
}

.app-item {
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 4px;

  background: #ffffff;
}

.appfavorite-icon {
  width: 18px;
  margin-left: 2px;
}

.app-item-card {
  width: 100%;
  padding: 14px 12px;
  display: flex;
  align-items: center;
  /*justify-content: center;*/
  cursor: pointer;
}

.app-item-img {
  border-radius: 4px;
  width: 44px;
  height: 44px;
  margin-right: 12px;
}

.app-img {
  width: 44px;
  height: 44px;
}

.card-container {
  min-width: 165px;
  display: flex;
  // overflow-x: auto;
  // &::-webkit-scrollbar {
  //   height: 6px;
  //   background-color: #f5f5f5;
  // }
}

.app-item:hover .app-name-hover-width {
  //width: 108px !important;
}

.app-item-coll {
  display: none;
  margin-left: 12px;
}

.app-item-a {
  width: 100%;
  text-overflow: -o-ellipsis-lastline;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.app-item-appName {
  width: 100%;
  // overflow: hidden;
  // text-overflow: ellipsis;
  // white-space: nowrap;
  font-size: 16px;
  color: #102645;
  letter-spacing: 0;
  word-wrap: break-word;
  word-break: break-all;
  // height: 20px;
  // line-height: 20px;
}

.app-item-appName-Con {
  position: relative;
  flex: 1;
  overflow: hidden;
  //overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;

  /*width: 112px*/
}

.app-name-hover-width {
  // width: 112px !important;
}

.app-name-hover-width-login {
  width: 112px !important;
}

.app-name-hover-width-empty-type {
  width: 142px !important;
}

.service-bus-source-item-active {
  background: rgba(234, 234, 234, 1);
  border-radius: 3px;
  font-weight: bold;
}

.service-bus-source-item-active::before {
  content: "";
  width: 2px;
  height: 40px;
  position: absolute;
  top: 0;
  left: 0;
}

.rowStyle {
  // margin-top: 12px;
  display: flex;
  justify-content: space-between;
}
</style>
