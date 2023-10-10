<template>
  <div v-loading="loading" ref="recommendapp">
    <AutoContainer
      :con-type="containerType.type"
      :con-height="containerType.value"
      :scroll="{ scrollingX: true }"
      v-if="apps && apps.length"
    >
      <div class="recommendapp">
        <ul :style="{ width }">
          <li
            v-for="(item, index) in apps"
            :key="`${item.serviceName}-${index}`"
            v-serviceLayout="{ columns }"
          >
            <div
              class="recommendapp-list"
              :class="{
                'portal-font-color-lv1': item.permission,
                'portal-font-color-lv4': !item.permission,
                'portal-primary-backgroundcolor-hover-lv5': true,
                'portal-primary-color-hover-lv1': item.permission,
                'recommendapp-list-hover': true,
              }"
              @click="goLink(item)"
            >
              <div
                class="recommendapp-left"
                :class="{ 'no-permission-service': !item.permission }"
              >
                <img
                  :src="item.iconLink"
                  alt="加载失败"
                  @error="
                    () => {
                      item.iconLink = errorImg;
                    }
                  "
                />
              </div>
              <div class="recommendapp-center">
                <we-tooltip
                  class="item"
                  effect="dark"
                  :content="item.serviceName"
                  placement="top-start"
                  :open-delay="800"
                >
                  <h3 style="word-break: break-all;">
                    <v-clamp autoresize :max-lines="2">
                      {{ item.serviceName }}
                    </v-clamp>
                  </h3>
                </we-tooltip>
              </div>
              <div class="recommendapp-right" v-if="userInfo">
                <i
                  class="iconfont icon-favorites"
                  :class="{
                    favorite_font: item.favorite,
                    unfavorite_font: !item.favorite,
                  }"
                  @click.stop="collectApp(item)"
                ></i>
              </div>
              <div
                v-if="userInfo && serviceAppraise == '1'"
                class="app-tag portal-primary-backgroundcolor-lv1"
                @click.stop="openServiceComment(item)"
              >
                <i class="we-icon-edit" />
              </div>
            </div>
          </li>
          <li
            @click="goAllservice(allServiceUrl)"
            v-show="isShowAllService !== 2"
            v-serviceLayout="{ columns }"
          >
            <div
              class="portal-font-color-lv1 portal-primary-backgroundcolor-hover-lv5 portal-primary-color-hover-lv1 last-all recommendapp-list"
            >
              <div class="recommendapp-left">
                <i class="iconfont icon-AllServices Word_Lv4"></i>
              </div>
              <div class="recommendapp-center">
                <h3 class="ellipsis">
                  {{ $Lan("CUS_CARD_RECOMMENDAPP", "allServices", "全部服务") }}
                </h3>
              </div>
            </div>
          </li>
        </ul>
      </div>
      <ServiceCommentModal
        ref="ServiceCommentModal"
        :router="router"
      ></ServiceCommentModal>
    </AutoContainer>
    <EmptyCon
      :tip="$Lan('CUS_CARD_RECOMMENDAPP', 'noData', '暂无相关服务')"
      :height="containerType.value"
      v-if="!apps || !apps.length"
    ></EmptyCon>
  </div>
</template>

<script>
import ServiceCommentModal from "./component/ServiceCommentModal";
import { initTrack } from "./mixins/track.js";
export default {
  name: "recommendapp",
  props: {
    index: Number,
    router: Object,
  },
  components: {
    ServiceCommentModal,
  },
  mixins: [initTrack],
  data() {
    const { cardWid, cardId } = this.router;
    return {
      checked: false,
      loading: false,
      fontClass: "",
      apps: null,
      userInfo: window.shell.getUserInfo(),
      allServiceUrl: "",
      isShowAllService: 0,
      width: "100%",
      card: {
        cardWid,
        cardId,
      },
      columns: 4,
      containerType: {
        type: 0,
        value: 500,
      },
      errorImg: window.shell.ErrorImg,
      isCollect: true,
      serviceAppraise: "0",
    };
  },
  methods: {
    openServiceComment(item) {
      this.$refs.ServiceCommentModal.show({
        wid: item.serviceWid,
        serviceName: item.serviceName,
      });
    },
    goLink(item) {
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.serviceWid,
      });
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
      window.shell.openService(item);
    },
    goAllservice(url) {
      this.handleClickTrack({
        infoType: 4,
        filterInfo: {
          pageCode: "apps",
        },
      });
      this.$nextTick(() => {
        if (this.isShowAllService === 0) {
          window.shell.handleBackTop();
        }
      });
      window.shell.openPage(url.split("#")[1], this.isShowAllService, 1);
    },
    collectApp(item) {
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.serviceWid,
        fucType: item.favorite ? 3 : 2,
      });
      if (!this.isCollect) {
        return;
      }
      this.isCollect = false;
      window.shell.collectService({
        operate: item.favorite ? 0 : 1,
        id: item.serviceWid,
      });
    },
    handleUpdateFavorite({ id, res }) {
      this.isCollect = true;
      if (res.errcode === "0") {
        this.apps.some((element) => {
          if (element.serviceWid === id) {
            element.favorite = !element.favorite;
          }
        });
      }
    },
    renderData() {
      this.loading = true;
      this.execCardMethod(
        {
          ...this.card,
          method: "renderData",
        },
        (data) => {
          if (data.errcode == 200 || "0" === data.errcode) {
            this.setData(data.data);
            this.serviceAppraise = data.data?.config.serviceAppraise;
          } else {
            this.apps = [];
          }
          this.loading = false;
          this.loadedEndTrack();
        }
      );
    },
    execCardMethod(params, callback) {
      window.shell.execCardMethod(params, (data) => {
        callback && typeof callback === "function" && callback(data);
      });
    },
    setData(data) {
      const { recommendAppList, config = {} } = data;
      const {
        containerType,
        columns,
        isShowAllService,
        allServiceUrl,
      } = config;
      this.apps =
        (recommendAppList &&
          recommendAppList.slice(0).map((v) => {
            return { ...v, throttle: true, wid: v.serviceWid };
          })) ||
        [];
      this.allServiceUrl = allServiceUrl;
      this.isShowAllService = isShowAllService;
      this.columns = Number(columns);
      this.width =
        this.cardWidth - 154 * columns >= 0 ? "100%" : `${154 * columns}px`;
      this.containerType = containerType;
    },
    getCardWidth() {
      let node = this.$refs.recommendapp;
      if (node && node.offsetWidth) {
        this.cardWidth = node.offsetWidth;
        return;
      }
      let hasClass = false;
      while (node && !hasClass) {
        node = node.parentNode;
        hasClass = node.classList.contains("gateway-card");
      }
      this.cardWidth = hasClass ? node.offsetWidth : 0;
    },
  },
  mounted() {
    this.getCardWidth();
    window.shell.on("collectApp", this.handleUpdateFavorite);
    this.renderData();
  },
  beforeDestroy() {
    window.shell.off("collectApp");
  },
};
</script>

<style scoped lang="less">
.ellipsis-2 {
  display: -webkit-box;
  overflow: hidden;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.recommendapp {
  > ul {
    padding-top: 12px;
    display: flex;
    flex-wrap: wrap;
    > li {
      > .recommendapp-list-hover {
        &:hover {
          > .recommendapp-right {
            > .iconfont {
              opacity: 1;
            }
          }
          > .app-tag {
            display: block;
          }
        }
      }
      > .recommendapp-list {
        display: flex;
        height: 72px;
        padding: 12px;
        width: 100%;
        border-radius: 4px;
        align-items: center;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        > .recommendapp-left {
          height: 44px;
          width: 44px;
          flex-shrink: 0;
          > img {
            height: 100%;
            width: 100%;
          }
        }
        > .recommendapp-center {
          flex: 1;
          overflow: hidden;
          padding: 0 12px;
          > h3 {
            line-height: 24px;
            width: 100%;
            font-size: 16px;
          }
        }
        > .recommendapp-right {
          font-size: 16px;
          flex-shrink: 0;
          > .iconfont {
            opacity: 0;
          }
        }
        > .app-tag {
          display: none;
          width: 50px;
          height: 50px;
          position: absolute;
          top: -25px;
          right: -25px;
          transform: rotate(45deg);

          > i {
            position: absolute;
            left: 20px;
            bottom: 4px;
            transform: rotate(-45deg);
            color: #fff;
          }
        }
      }
      > .last-all {
        > .recommendapp-left {
          height: 44px;
          width: 44px;
          flex-shrink: 0;
          line-height: 44px;
          text-align: center;
          > .iconfont {
            font-size: 36px;
          }
        }
        > .recommendapp-center {
          > h3 {
            margin-bottom: 0px;
          }
        }
        &:hover {
          background-color: none;
        }
      }
    }
  }
}
</style>
