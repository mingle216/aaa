<template>
  <div class="hot_search" v-loading="loading">
    <!-- <div :style="{ width: hotTabs.length > 1 ? `${hotTabs.length * (200 + 12) - 11}px` : '100%' }"> -->
    <div
      :class="[hotTabs.length > 1 ? 'hot_item' : 'one_hot_item', item.className]"
      v-for="(item, index) in hotTabs"
      :key="index"
    >
      <div class="hot_title">
        {{ item.name }}
      </div>
      <div class="content" v-if="hostSearchData[item.value] && hostSearchData[item.value].length">
        <div
          class="item_content"
          v-for="(el, index) in hostSearchData[item.value]"
          :key="el.wid"
          @click="goToLink(item, el)"
        >
          <div class="name_wrap">
            <rank-item :rank="index + 1"></rank-item>
            <div class="wrap">
              <div
                class="name portal-font-color-lv1 portal-primary-color-hover-lv1"
                :title="el.name"
              >
                {{ el[item.nameLabel] }}
              </div>
              <div class="visit_count">
                <i class="iconfont icon-Maturemoban-guankanliang"></i>
                <!-- <img src="../../assets/images/count.png" alt="" /> -->
                <span class="portal-font-color-lv3">
                  {{ el.searchCount }}
                </span>
              </div>
            </div>
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
    <!-- </div> -->
  </div>
</template>

<script>
import RankItem from "./RankItem.vue";
import { clickTrack } from "../../mixins/track.js";
export default {
  components: {
    RankItem,
  },
  mixins: [clickTrack],
  props: {
    router: Object,
    dataObj: Array,
  },
  computed: {
    showHotTabs() {
      return this.config?.dataTypes || "0,1,2";
    },
  },
  data() {
    return {
      loading: false,
      activeTab: {},
      hostSearchData: {},
      currentUser: window.shell.getUserInfo(),
      config: {
        dataTypes: "0,1,2",
        displaySwitch: 1,
        timeDelay: 30,
      },
      hotTabs: [],
      isServiceCollect: false,
      isItemCollect: false,
      isThingCollect: false,
    };
  },
  methods: {
    changeTab(item) {
      this.activeTab = item;
    },
    setTabName() {
      this.hotTabs.forEach((el) => {
        this.dataObj.forEach((item) => {
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
            pageSize: 5,
          },
        })
        .then((res) => {
          this.loading = false;
          if (res && res.errcode === "0") {
            this.hostSearchData = res.data || {};
            this.config = res.data.hotSearchConfig;
            this.hotTabs = [
              {
                name: "服务",
                wid: "0",
                value: "serviceData",
                nameLabel: "serviceName",
                widLabel: "serviceWid",
                code: "onlineService",
                className: "service_class",
              },
              {
                name: "事项",
                wid: "1",
                value: "serviceItemData",
                nameLabel: "itemName",
                widLabel: "itemWid",
                code: "serviceItem",
                className: "service_item_class",
              },
              // {
              //   name: "一件事",
              //   wid: "2",
              //   value: "oneThingData",
              //   nameLabel: "oneThingName",
              //   widLabel: "oneThingWid",
              //   code: "oneThing",
              //   className: "#D37700",
              // },
            ];
            this.hotTabs = this.hotTabs.filter((el) => this.showHotTabs.includes(el.wid));
          } else {
            this.hostSearchData = {};
            this.hotTabs = [];
          }
        });
    },
    goToLink(el, item) {
      if (el.wid == "0") {
        this.goService(item);
      } else if (el.wid == "1") {
        this.goDetail(item);
      } else {
        this.goThingDetail(item);
      }
    },
    goService(item) {
      if (!item.permission) {
        this.handleClickTrack({
          infoType: 0,
          serviceId: item.serviceWid,
        });
        window.shell.showMessage({
          message: this.$Lan(this.lanFunName, "showMessage", "暂无使用权限，请联系管理员"),
          type: "warning",
        });
        return;
      }
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.serviceWid,
      });
      window.shell.openService({
        ...item,
        wid: item.serviceWid,
        name: item.serviceName,
      });
    },
    goDetail(item) {
      if ("isAuthorized" in item && item.isAuthorized === 0) {
        this.$message({ message: "无权限查看", type: "warning" });
        return false;
      }
      this.handleClickTrack({
        infoType: 1,
        itemId: item.itemWid,
      });
      window.shell.openServiceItem({
        ...item,
        wid: item.itemWid,
        name: item.itemName,
      });
    },
    goThingDetail(item) {
      window.shell.visitService(item.oneThingWid, "OneThing"); // 一件事详情
      window.shell.openPage(`/thingdetail?wid=${item.oneThingWid}`, 1, 1);
    },
  },
  mounted() {
    this.setTabName();
    this.getHotSearchData();
  },
};
</script>

<style lang="less" scoped>
.hot_search {
  display: flex;
  position: relative;
  overflow: auto;
  width: 100%;
  padding: 0 12px;
  &::-webkit-scrollbar {
    height: 0;
    background: transparent;
  }
  .hot_item,
  .one_hot_item {
    height: 350px;
    padding: 16px;
    border-radius: 8px;
    float: left;
  }
  .service_class {
    background: url("../../assets/images/hotBg1.png") no-repeat left top / cover,
      linear-gradient(180deg, #fff4ea 0%, rgba(255, 255, 255, 0) 60.95%);
    border: 0.5px solid #ffeaca;
    .hot_title {
      color: #d37700;
    }
  }
  .service_item_class {
    background: url("../../assets/images/hotBg2.png") no-repeat left top / cover,
      linear-gradient(180deg, #eaf5ff 0%, rgba(255, 255, 255, 0) 60.95%);
    border: 0.5px solid #dfecff;
    .hot_title {
      color: #1c6bff;
    }
  }
  .hot_item {
    width: 200px;
    margin-right: 12px;
    flex-shrink: 0;
    // width: 413px;
  }
  .one_hot_item {
    width: 100%;
  }
  .hot_title {
    font-weight: 500;
    font-size: 17px;
    line-height: 24px;
    margin-bottom: 10px;
  }
  .content {
    .item_content {
      padding: 6px 0;
      cursor: pointer;
      &:last-child {
        border-bottom: 0;
      }
      .name_wrap {
        height: 46px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        img {
          width: 22px;
          height: 22px;
        }
        .name {
          font-size: 15px;
          line-height: 28px;
          max-width: 100%;
          display: inline-block;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        .wrap {
          margin-left: 10px;
          width: calc(100% - 32px);
        }
        .visit_count {
          color: #8c8c8c;
          // line-height: 18px;
          font-size: 12px;
          display: flex;
          align-items: center;
          i {
            font-size: 12px;
            margin-right: 6px;
          }
        }
      }
    }
  }

  .one_hot_item {
    width: 100%;
    height: 100%;
    .item_content {
      margin-top: 16px;
      padding: 0;
      .wrap {
        display: flex;
        justify-content: space-between;
      }
      .name_wrap {
        height: 28px;
      }
    }
  }

  /deep/.empty-content {
    padding-top: 50%;
  }
}
</style>
