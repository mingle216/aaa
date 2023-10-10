<template>
  <div
    class="recommendApp"
    :class="[
      config.columns == 1
        ? 'recommendApp-oneColumn'
        : 'recommendApp-twoColumns',
    ]"
    v-loading="loading"
  >
    <div
      class="recommendApp__lists portal-font-color-lv1"
      v-if="dataList.length"
    >
      <div
        class="recommendApp__item"
        v-for="item in dataList"
        :key="item.serviceWid"
        @click="handleGoLink(item)"
      >
        <!-- serviceStation 0代表pc服务  1代表移动服务 2代表即是pc服务又是移动服务 -->
        <div class="recommendApp__item__imgWrap">
          <img
            class="recommendApp__item__img"
            :src="
              (item.serviceStation === 0
                ? item.iconLink
                : item.mobileIconLink) || errorImg
            "
            @error="handleError"
          />
          <div v-if="!item.permission" class="recommendApp__item__img-lock">
            <img
              style="width: 0.533333rem; height: 0.533333rem;"
              :src="require('./assets/lock.png')"
              alt="加载失败"
            />
          </div>
        </div>
        <div
          class="recommendApp__item__name"
          :class="[config.columns == 1 ? 'ellipsis' : 'lineClamp']"
        >
          {{ item.serviceName }}
        </div>
        <i
          v-if="userInfo"
          class="iconfont recommendapp__item__favorite"
          :class="[
            item.favorite
              ? 'favorite_font icon-favorites'
              : 'unfavorite_font icon-icon-xiankuangxingshoucang',
          ]"
          @click.stop="handleCollectApp(item)"
        ></i>
      </div>
      <!-- 全部服务 -->
      <div
        class="recommendApp__item"
        v-if="config.isShowAllService"
        @click="handleGoAllApp"
      >
        <img
          class="recommendApp__item__imgWrap"
          src="./assets/allAppIcon.svg"
        />
        <div
          class="recommendApp__item__name"
          :class="[config.columns == 1 ? 'ellipsis' : 'lineClamp']"
        >
          {{ $Lan("SYS_CARD_RECOMMENDAPP_h5", "allServices", "全部服务") }}
        </div>
      </div>
    </div>
    <EmptyCon
      :tip="$Lan('SYS_CARD_RECOMMENDAPP_h5', 'noData', '暂无相关服务')"
      v-else
    ></EmptyCon>
  </div>
</template>

<script>
import TrackMixin from "./mixins/track.js";
export default {
  props: {
    router: Object,
  },
  mixins: [TrackMixin],
  data() {
    return {
      loading: false,
      dataList: [],
      config: {},
      userInfo: window.shell.getUserInfo(),
      errorImg: window.shell.ErrorImg,
      itemLoading: {},
    };
  },
  created() {
    if (window.shell) {
      this.getCardData();
      window.shell.on("collectApp", this.handleUpdateFavorite);
    }
  },
  beforeDestroy() {
    window.shell.off("collectApp", this.handleUpdateFavorite);
  },
  methods: {
    getCardData() {
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "renderData",
        },
        (data) => {
          this.loading = false;
          this.loadedEndTrack(); // 页面加载结束埋点
          if (data.errcode === "0" && data.data) {
            this.dataList = data.data.recommendAppList || [];
            this.config = data.data.config || {};
          }
        }
      );
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    handleCollectApp(item) {
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.serviceWid,
        fucType: item.favorite ? 3 : 2,
      }); // 点击埋点
      if (this.itemLoading[item.serviceWid]) {
        return;
      }
      this.itemLoading[item.serviceWid] = true;
      window.shell.collectService({
        id: item.serviceWid,
        operate: item.favorite ? 0 : 1, //  0:取消收藏 1:收藏
      });
    },
    handleUpdateFavorite({ id, res }) {
      this.itemLoading[id] = false;
      if (res.errcode === "0") {
        this.dataList.some((element) => {
          if (element.serviceWid === id) {
            element.favorite = !element.favorite;
          }
        });
      }
    },
    handleGoLink(item) {
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.serviceWid,
      }); // 点击埋点
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
      window.shell.openService({
        ...item,
        wid: item.serviceWid,
      });
    },
    handleGoAllApp() {
      this.handleClickTrack({
        infoType: 4,
        filterInfo: {
          pageCode: "apps",
        },
      }); // 点击埋点
      window.shell.openPage(this.config.allServiceUrl, 0, 2);
    },
  },
};
</script>

<style lang="less" scoped>
.recommendApp {
  position: relative;
  width: 100%;
  height: 100%;
  font-size: 14px;
  padding: 0 17px;
  min-height: 56px;
  .recommendApp-allService {
    padding-bottom: 0;
  }
  .recommendApp__lists {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
  }
  .recommendApp__item {
    width: 100%;
    display: flex;
    height: 56px;
    align-items: center;
    .recommendApp__item__imgWrap {
      width: 36px;
      height: 36px;
      border-radius: 7px;
      overflow: hidden;
    }
    .recommendApp__item__img {
      width: 100%;
      height: 100%;
      border-radius: 7px;
    }
    .recommendapp__item__favorite {
      font-size: 13px;
      &.favorite_font {
        font-size: 12px;
      }
    }
    .recommendApp__item__name {
      flex: 1;
      min-width: 0;
      margin: 0 8px 0 12px;
    }
  }
  .recommendApp__item__img-lock {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  &.recommendApp-oneColumn {
    .recommendApp__item {
      height: 58px;
      padding: 11px 0;
      font-size: 16px;
    }
  }
  &.recommendApp-twoColumns {
    .recommendApp__lists {
      padding-top: 12px;
    }
    .recommendApp__item {
      width: calc(50% - 6px);
      margin-bottom: 12px;
      background-color: #f6f6f8;
      padding: 10px 6px 10px 12px;
      border-radius: 4px;
    }
    .recommendApp__item__name {
      line-height: 18px;
    }
  }
  .lineClamp {
    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-all;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
}
</style>
