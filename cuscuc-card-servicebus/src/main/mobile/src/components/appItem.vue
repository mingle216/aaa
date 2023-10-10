<template>
  <div>
    <div
      :class="[styleclass]"
      v-for="(app, idx) in appData.slice(0, maxappsnum)"
      :key="idx"
      @click="openApp(app)"
    >
      <div
        class="img-content"
        :class="{ 'no-permission-service': !app.permission }"
        style="display: flex"
      >
        <img class="img" :src="getAppicon(app)" v-err-img="errorImg" />
      </div>

      <div class="appname">{{ app.appName }}</div>
      <img
        class="favoriteimg"
        v-if="userInfo"
        @click.stop="handleFav(app)"
        :src="getFavoriteIcon(app)"
      />
    </div>
  </div>
</template>

<script>
import { clickTrack } from '../mixins/track.js';
export default {
  props: {
    appData: {
      type: Array,
      default: () => {
        return [];
      },
    },
    maxappsnum: {
      type: Number,
      default: 99999,
    },
    styleclass: {
      type: String,
      default: "app",
    },
    LanFunName:String,
    router: Object,
  },
  mixins: [clickTrack],
  data() {
    return {
      errorImg: window.shell.ErrorImg,
      favouriteImg: require("../.././public/img/favorite.png"),
      unfavoriteImg: require("../.././public/img/unfavorite.png"),
      userInfo: window.shell.getUserInfo(),
    };
  },
  methods: {
    handleFav(app) {
      this.$emit("handleFavorite", app);
    },
    getFavoriteIcon(app) {
      return app.appFavorite ? this.favouriteImg : this.unfavoriteImg;
    },
    openApp(app) {
      this.handleClickTrack({
        infoType: 0,
        serviceId: app.appId,
      }) // 点击埋点
      if (!app.permission) {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan(this.LanFunName, "message", "暂无使用权限，请联系管理员"),
        });
        return;
      }
      window.shell.openService({
        ...app,
        wid: app.appId,
      });
    },
    getAppicon(app) {
      if (app.serviceStation) {
        return app.mobileIconLink;
      } else {
        return app.iconLink;
      }
    },
  },
};
</script>

<style lang="less" scoped>
.app {
  height: 58px;
  display: flex;
  display: -webkit-flex;
  align-items: center;
  .appname {
    font-size: 16px;
    padding-left: 12px;
    padding-right: 8px;
    width: calc(100% - 50px);
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .img-content {
    width: 36px;
    height: 36px;
    position: relative;
    overflow: hidden;
    .img {
      width: 100%;
      height: 100%;
    }
  }
}
.app-line {
  width: calc(50% - 6px);
  height: 56px;
  background: #f6f6f8;
  border-radius: 4px;
  display: flex;
  display: -webkit-flex;
  align-items: center;
  float: left;
  margin-bottom: 12px;
  padding: 12px;
  .appname {
    font-size: 14px;
    padding-left: 12px;
    padding-right: 4px;
    width: calc(100% - 50px);
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    line-clamp: 2;
    -webkit-box-orient: vertical;
    word-break: break-word;
  }
  .img-content {
    display: flex;
    width: 36px;
    height: 36px;
    position: relative;
    overflow: hidden;
    .img {
      width: 100%;
      height: 100%;
    }
  }
  &:nth-child(2n + 1) {
    margin-right: 12px;
  }
}
.favoriteimg {
  width: 13px;
  height: 13px;
}
</style>