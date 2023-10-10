<template>
  <div v-loading="loading" ref="hotApp">
    <AutoContainer
      :con-type="containerType.type"
      :con-height="containerType.value"
      :scroll="{ scrollingX: true }"
      v-if="apps && apps.length"
    >
      <div class="hotApp">
        <ul :style="{ width }">
          <li
            v-for="(item, index) in apps"
            :key="`${item.serviceName}-${index}`"
            v-serviceLayout="{ columns }"
          >
            <div
              class="hotApp-list"
              :class="{
                'portal-font-color-lv1': item.permission,
                'portal-font-color-lv4': !item.permission,
                'portal-primary-backgroundcolor-hover-lv5': true,
                'portal-primary-color-hover-lv1': item.permission,
                'hotApp-list-hover': true,
                'portal-primary-border-color-lv4': true,
              }"
              @click="goLink(item)"
            >
              <div
                class="hotApp-left"
                :class="{ 'no-permission-service': !item.permission }"
              >
                <img
                  :src="item.iconLink"
                  :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                  @error="
                    () => {
                      item.iconLink = errorImg;
                    }
                  "
                />
              </div>
              <div class="hotApp-center">
                <we-tooltip
                  class="item"
                  effect="dark"
                  :content="item.serviceName + ''"
                  :open-delay="800"
                  :hide-after="500"
                  placement="top-start"
                >
                  <h3 class="ellipsis">{{ item.serviceName }}</h3>
                </we-tooltip>
                <we-tooltip
                  class="item"
                  effect="dark"
                  :content="item.visitCount + ''"
                  :open-delay="800"
                  :hide-after="500"
                  placement="top-start"
                >
                  <p
                    class="ellipsis"
                    :class="{
                      'portal-font-color-lv3': item.permission,
                      'portal-font-color-lv4': !item.permission,
                    }"
                  >
                    {{ formatNum(item.visitCount) }}{{ $Lan(lanFunName, "visits", "次访问") }}
                  </p>
                </we-tooltip>
              </div>
              <div class="hotApp-right" v-if="userInfo">
                <i
                  class="iconfont icon-favorites"
                  :class="{
                    favorite_font: item.favorite,
                    unfavorite_font: !item.favorite,
                  }"
                  @click.stop="collectApp(item)"
                ></i>
              </div>
              <span class="portal-primary-color-lv3">
                <i class="iconfont icon-shixiangfenleijiantou"> </i>
              </span>
              <div
                v-if="userInfo && serviceAppraise == '1'"
                class="app-tag portal-primary-backgroundcolor-lv1"
                @click.stop="openServiceComment(item)"
              >
                <i class="we-icon-edit" />
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
      :tip="$Lan(lanFunName, 'noData', '暂无相关服务')"
      :height="containerType.value"
      v-if="!apps || !apps.length"
    ></EmptyCon>
  </div>
</template>

<script>
import ServiceCommentModal from "./component/ServiceCommentModal";
import { initTrack } from "./mixins/track.js";
export default {
  name: "hotApp",
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
      lanFunName: this.router.cardId,
      loading: false,
      checked: false,
      apps: null,
      userInfo: window.shell.getUserInfo(),
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
    formatNum(num) {
      return num.toString().replace(/(?=\B(\d{3})+$)/g, ",");
    },
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
          message: this.$Lan(
            this.lanFunName,
            "goLinkMessage",
            "暂无使用权限，请联系管理员"
          ),
        });
        return;
      }
      window.shell.openService(item);
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
          if (data.errcode === "0") {
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
      const { appList, config = {} } = data;
      const { containerType, columns, rows } = config;
      this.apps =
        (appList &&
          appList.slice(0, Number(rows)).map((v) => {
            return { ...v, throttle: true, wid: v.serviceWid };
          })) ||
        [];

      this.width = this.cardWidth - 154 * columns >= 0 ? "100%" : `${154 * columns}px`;
      this.columns = Number(columns);
      this.containerType = containerType;
    },
    getCardWidth() {
      let node = this.$refs.hotApp;
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
    this.renderData();
    window.shell.on("collectApp", this.handleUpdateFavorite);
  },
  beforeDestroy() {
    window.shell.off("collectApp");
  },
};
</script>

<style scoped lang="less">
.hotApp {
  width: 100%;
  > ul {
    padding-top: 12px;
    display: flex;
    flex-wrap: wrap;
    > li {
      padding: 10px;
      > .hotApp-list-hover {
        &:hover {
          > .hotApp-right {
            > .iconfont {
              opacity: 1;
            }
          }
          > .app-tag {
            display: block;
          }
        }
      }
      > .hotApp-list {
        display: flex;
        height: 82px;
        align-items: center;
        width: 100%;
        position: relative;
        overflow: hidden;
        cursor: pointer;
        padding: 0 12px;
        border-radius: 4px;
        border: 1px solid #e3edff;
        background: #fff;

        > .hotApp-left {
          height: 44px;
          width: 44px;
          flex-shrink: 0;
          > img {
            height: 100%;
            width: 100%;
          }
        }
        > .hotApp-center {
          flex: 1;
          overflow: hidden;
          padding: 0 12px;
          > h3 {
            line-height: 24px;
            margin-bottom: 2px;
            width: 100%;
            font-size: 16px;
          }
          > p {
            font-size: 14px;
            line-height: 22px;
            width: 100%;
          }
        }
        > .hotApp-right {
          font-size: 16px;
          flex-shrink: 0;
          position: absolute;
          top: 6px;
          right: 6px;
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
    }
  }
}
</style>
