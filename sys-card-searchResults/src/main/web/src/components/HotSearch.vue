<template>
  <div class="hot_search" v-loading="loading">
    <div class="header">
      <div
        :class="{ tab_item: true, active_item: activeTab.value === item.value }"
        v-for="(item, index) in hotTabs"
        :key="index"
        @click="changeTab(item)"
        :title="item.name"
        v-show="showHotTabs.includes(item.wid)"
      >
        <span class="ellipsis">{{ item.name }}</span>
      </div>
    </div>
    <div class="content" v-if="hostList.length">
      <div
        class="item_content"
        v-for="(item, index) in hostList"
        :key="item[activeTab.widLabel]"
        @click="goToLink(item)"
      >
        <div class="name_wrap">
          <div style="display: flex">
            <rank-item :rank="index + 1"></rank-item>
            <span class="name portal-font-color-lv1 portal-primary-color-hover-lv1" :title="item.name">
              {{ item[activeTab.nameLabel] }}
            </span>
          </div>
          <i
            v-if="currentUser && activeTab.wid == '0'"
            class="iconfont icon-favorites collect"
            :class="[
              item.favorite || item.isFavorite
                ? 'favorite_service_color'
                : 'unfavorite_font'
            ]"
            @click.stop="handleCollect(item)"
          />
          <i
            v-if="currentUser && activeTab.wid != '0'"
            class="iconfont icon-shixiangshoucang collect"
            :class="[
              item.favorite || item.isFavorite
                ? 'favorite_font favorite_font_color'
                : 'unfavorite_font'
            ]"
            @click.stop="handleCollect(item)"
          />
        </div>
        <div class="visit_count">
          <img src="../assets/images/count.png" alt="" />
          <span class="portal-font-color-lv3">
            {{ item.searchCount }}
          </span>
        </div>
      </div>
    </div>
    <template v-else>
      <EmptyCon
        :tip="$Lan(lanFunName, 'noHotData', '无热门数据')"
        mainTipClass="portal-font-color-lv3"
      />
    </template>
  </div>
</template>

<script>
import RankItem from "./items/RankItem.vue";
import { clickTrack } from "../mixins/track.js";
export default {
  components: {
    RankItem
  },
  mixins: [clickTrack],
  props: {
    hotSearchDisplay: Object,
    router: Object,
    dataObj: Array
  },
  computed: {
    showHotTabs() {
      return this.hotSearchDisplay.dataTypes;
    },
    hostList() {
      return this.hostSearchData[this.activeTab.value] || [];
    }
  },
  data() {
    return {
      loading: false,
      activeTab: {},
      hostSearchData: {},
      currentUser: window.shell.getUserInfo(),
      hotTabs: [
        {
          name: "服务",
          wid: "0",
          value: "serviceData",
          nameLabel: "serviceName",
          widLabel: "serviceWid",
          code: "onlineService"
        },
        {
          name: "事项",
          wid: "1",
          value: "serviceItemData",
          nameLabel: "itemName",
          widLabel: "itemWid",
          code: "serviceItem"
        },
        {
          name: "一件事",
          wid: "2",
          value: "oneThingData",
          nameLabel: "oneThingName",
          widLabel: "oneThingWid",
          code: "oneThing"
        }
      ],
      isServiceCollect: false,
      isItemCollect: false,
      isThingCollect: false
    };
  },
  methods: {
    changeTab(item) {
      this.activeTab = item;
    },
    setTabName() {
      this.hotTabs.forEach(el => {
        this.dataObj.forEach(item => {
          if (el.code === item.code) {
            el.name = item.name;
          }
        });
      });
    },
    getHotSearchData() {
      this.loading = true;
      window.shell
        .execCardMethod({
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getHotSearchData",
          param: {
            lang: sessionStorage.getItem("locale") || "zh_CN",
            pageSize: 5
          }
        })
        .then(res => {
          this.loading = false;
          this.hostSearchData = res.data || {};
          const temp = this.hotTabs.filter(el =>
            this.showHotTabs.includes(el.wid)
          );
          this.activeTab = temp && temp[0];
        });
    },
    handleCollect(item) {
      if (this.activeTab.wid == "0") {
        this.collectService(item);
      } else if (this.activeTab.wid == "1") {
        this.collectServiceItem(item);
      } else {
        this.collectOneThing(item);
      }
    },
    collectService(item) {
      if (this.isServiceCollect) {
        return;
      }
      this.isServiceCollect = true;
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.serviceWid,
        fucType: item.favorite ? 3 : 2
      });
      window.shell
        .collectService({
          id: item.serviceWid,
          operate: item.favorite ? "0" : "1"
        })
        .then(() => {
          this.isServiceCollect = false;
        })
        .catch(() => {
          this.isServiceCollect = false;
        });
    },
    collectServiceItem(item) {
      if (this.isItemCollect) {
        return;
      }
      this.handleClickTrack({
        infoType: 1,
        itemId: item.itemWid,
        fucType: item.favorite ? 3 : 2
      });
      this.isItemCollect = true;
      window.shell
        .collectServiceItem({
          operate: item.favorite ? "0" : "1",
          id: item.itemWid
        })
        .then(() => {
          this.isItemCollect = false;
        })
        .catch(() => {
          this.isItemCollect = false;
        });
    },
    collectOneThing(item) {
      if (this.isThingCollect) {
        return;
      }
      // this.handleClickTrack({
      //   infoType: 1,
      //   itemId: item.oneThingWid,
      //   fucType: item.isFavorite ? 3 : 2
      // });
      this.isThingCollect = true;
      window.shell
        .collectOneThing({
          operate: item.isFavorite ? "0" : "1",
          id: item.oneThingWid
        })
        .then(() => {
          this.isThingCollect = false;
        })
        .catch(() => {
          this.isThingCollect = false;
        });
    },
    goToLink(item) {
      if (this.activeTab.wid == "0") {
        this.goService(item);
      } else if (this.activeTab.wid == "1") {
        this.goDetail(item);
      } else {
        this.goThingDetail(item);
      }
    },
    goService(item) {
      if (!item.permission) {
        this.handleClickTrack({
          infoType: 0,
          serviceId: item.serviceWid
        });
        window.shell.showMessage({
          message: this.$Lan(
            this.lanFunName,
            "showMessage",
            "暂无使用权限，请联系管理员"
          ),
          type: "warning"
        });
        return;
      }
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.serviceWid
      });
      window.shell.openService({
        ...item,
        wid: item.serviceWid,
        name: item.serviceName
      });
    },
    goDetail(item) {
      if ("isAuthorized" in item && item.isAuthorized === 0) {
        this.$message({ message: "无权限查看", type: "warning" });
        return false;
      }
      this.handleClickTrack({
        infoType: 1,
        itemId: item.itemWid
      });
      window.shell.openServiceItem({
        ...item,
        wid: item.itemWid,
        name: item.itemName
      });
    },
    goThingDetail(item) {
      // this.handleClickTrack({
      //   infoType: 1,
      //   itemId: item.oneThingWid
      // });
      window.shell.visitService(item.oneThingWid, "OneThing"); // 一件事详情
      window.shell.openPage(`/thingdetail?wid=${item.oneThingWid}`, 1, 1);
    },
    collectApp(params) {
      (this.hostSearchData.serviceData || []).forEach(el => {
        if (params.id === el.serviceWid) {
          el.favorite = params.operate == 1 ? true : false;
        }
      });
    },
    collectItem(params) {
      (this.hostSearchData.serviceItemData || []).forEach(el => {
        if (params.id === el.itemWid) {
          el.favorite = params.operate == 1 ? true : false;
        }
      });
    },
    collectThing(params) {
      (this.hostSearchData.oneThingData || []).forEach(el => {
        if (params.id === el.oneThingWid) {
          el.isFavorite = params.operate == 1 ? true : false;
        }
      });
    }
  },
  mounted() {
    this.setTabName();
    this.getHotSearchData();
    window.shell.on("collectApp", this.collectApp);
    window.shell.on("collectServiceItem", this.collectItem);
    window.shell.on("collectOneThing", this.collectThing);
  },
  beforeDestroy() {
    window.shell.off("collectApp", this.collectApp);
    window.shell.off("collectServiceItem", this.collectItem);
    window.shell.off("collectOneThing", this.collectThing);
  }
};
</script>

<style lang="less" scoped>
.unfavorite_font {
  color: #ccd0d3;
}
.favorite_service_color {
  color: #ffbc00;
}
.favorite_font_color {
  color: #fa6444;
}
.ellipsis {
  display: inline-block;
  max-width: 65px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.hot_search {
  border: 1px solid #f0f0f0;
  border-radius: 4px 4px 0 0;
  width: 262px;
  height: 426px;
  position: relative;
  margin-left: 36px;
  .header {
    font-size: 16px;
    color: rgba(255, 255, 255, 0.8);
    height: 46px;
    display: flex;
    align-items: center;
    padding: 11px 20px;
    border-radius: 4px;
    background: #ff9605 url("../assets/images/bg.png") no-repeat center center;
    .tab_item {
      margin-right: 16px;
      cursor: pointer;
    }
    .active_item {
      color: #fff;
      position: relative;
      &::after {
        content: "";
        width: 18px;
        height: 3px;
        border-radius: 4px;
        background: #fff;
        display: inline-block;
        position: absolute;
        bottom: -6px;
        left: calc(50% - 9px);
      }
    }
  }
  .content {
    padding: 0 20px;
    .item_content {
      height: 76px;
      padding: 16px 0;
      border-bottom: 1px solid #f0f0f0;
      cursor: pointer;
      &:last-child {
        border-bottom: 0;
      }
      .name_wrap {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 2px;
        .name {
          padding-left: 10px;
          line-height: 22px;
          max-width: 172px;
          display: inline-block;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        .collect {
          font-size: 12px;
          display: none;
        }
      }
      .visit_count {
        padding-left: 30px;
        color: #8c8c8c;
        line-height: 20px;
        font-size: 12px;
        display: flex;
        align-items: center;
        img {
          margin-right: 4px;
        }
      }
      &:hover .collect {
        display: block;
      }
    }
  }
}
</style>
