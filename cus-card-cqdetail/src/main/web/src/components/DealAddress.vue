<template>
  <!--办理地点-->
  <div>
    <div class="title1 portal-font-color-lv1">
      {{ fieldName }}
    </div>
    <div style="display: flex">
      <div class="locationMap" style="margin-right: 34px" v-if="mapFlag">
        <!--地图是否可见，test之后把！去掉-->
        <div style="height: 280px; width: 500px" :lng="lng" :lat="lat">
          <baidu-map
            class="map"
            style="width: 100%; height: 100%"
            :scroll-wheel-zoom="true"
            :zoom="15"
            :center="{ lng: lng, lat: lat }"
          >
            <!-- <bm-local-search
                    style="display:none"
                    keyword="南京大学(鼓楼校区)"
                    :auto-viewport="true"
                    :location="location"
                  ></bm-local-search>-->
            <bm-marker
              :position="{ lng: lng, lat: lat }"
              :dragging="true"
              animation="BMAP_ANIMATION_BOUNCE"
            />
          </baidu-map>
        </div>
      </div>
      <div class="locationName" :style="locStyle">
        <div v-for="(it, i) in locationData" :key="i + it.siteName">
          <div
            class="lname-item"
            @click="changePlace(it)"
            :class="{
              'portal-primary-color-lv1': activePlace == it.siteName,
            }"
          >
            <i
              class="icon iconfont icon-place place-icon portal-font-color-lv4"
            ></i>
            {{ it.siteName }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BaiduMap from "vue-baidu-map";
import Vue from "vue";
Vue.use(BaiduMap, { ak: "" });
export default {
  props: {
    router: Object,
    item: {
      type: Object,
      default: () => {
        return {};
      },
    },
    mapFlag: {
      type: Boolean,
      default: true,
    },
    fieldName:String
  },
  watch: {
    item: {
      handler(it) {
        if (it && it.fieldValue.length) {
         
            this.locationData = it.fieldValue;
            this.activePlace = this.mapFlag ? this.locationData[0].siteName : ""; // 地图默认选中第一个地点
            this.locStyle = this.mapFlag ? "" : "pointer-events: none;";
            this.lat = this.locationData[0].latitude || 0;
            this.lng = this.locationData[0].longitude || 0;
         
        }
      },
      immediate: true,
    },
  },
  data() {
    return {
      activePlace: "",
      lat: 0,
      lng: 0,
      locStyle: "",
      locationData: [],
    };
  },
  methods: {
    changePlace(val) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName
        },
        startTime: new Date().getTime()
      });
      this.activePlace = val.siteName;
      this.locationData.forEach((item) => {
        if (item.siteName === this.activePlace) {
          this.lng = item.longitude;
          this.lat = item.latitude;
        }
      });
      // console.log(val);
    },
  },
};
</script>

<style lang="less" scoped>
.lname-item {
  cursor: pointer;
  line-height: 22px;
  position: relative;
  left: 22px;
  padding-right: 22px;
  font-size: 14px;
  &.portal-primary-color-lv1 .place-icon {
    color: inherit !important;
  }
}
.place-icon {
  font-size: 16px;
  margin-right: 8px;
  // top: 1px;
  position: absolute;
  left: -22px;
}
.locationName > div:not(:last-child) {
  margin-bottom: 20px;
}
</style> 