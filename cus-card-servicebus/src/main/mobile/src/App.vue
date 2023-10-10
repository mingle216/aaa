 
<template>
  <div class="card-servicebus" v-loading="loading">
    <div class="servicebus-content">
      <we-tabs
        v-if="classifyData.length > 1"
        @change="handleQuery"
        v-model="curclassify"
        swipe-threshold="1"
      >
        <we-tab
          v-for="it in classifyData"
          :title="it.typeName"
          :key="it.typeId"
          :name="it.typeId"
        >
          <template #title>
            <span
              :class="[
                'item-title',
                it.typeId == curclassify ? 'portal-primary-color-lv1' : '',
              ]"
              >{{ it.typeName }}</span
            >
          </template>
          <data-item
            :appData="appData"
            :mobileConfig="mobileConfig"
            :maxappsnum="maxappsnum"
            :router="router"
            @handleFavorite="handleFavorite"
            @getFavoriteIcon="getFavoriteIcon"
            @handleMore="handleMore"
          ></data-item>
          <EmptyCon
            :tip="$Lan(LanFunName, 'emptytips', '该分类下无服务')"
            :height="180"
            style="width: 100%"
            v-if="!appData.length"
          />
        </we-tab>
      </we-tabs>
      <div v-if="classifyData.length == 1" class="servicebus-content-classify">
        <data-item
          :appData="appData"
          :mobileConfig="mobileConfig"
          :maxappsnum="maxappsnum"
          :LanFunName="LanFunName"
          :router="router"
          @handleFavorite="handleFavorite"
          @getFavoriteIcon="getFavoriteIcon"
          @handleMore="handleMore"
        ></data-item>
      </div>
      <EmptyCon
        :tip="$Lan(LanFunName, 'emptytips_data', '暂无数据')"
        :height="180"
        style="width: 100%"
        v-if="
          !classifyData.length || (classifyData.length == 1 && !appData.length)
        "
      />
    </div>
    <custom-action-sheet v-model="show" class="all-sheet" :title="cardName" @closed="handleClickTrack()">
      <!-- <div class="action-sheet-title">
       <h3> {{ cardName }}</h3>
        <we-icon name="cross" class="close" @click="close" />
      </div> -->
      <servicebus-more
        :classifyData="classifyData"
        :appData="appData"
        @handleFavorite="handleFavorite"
        @getCardData="getCardData"
        @close="close"
        :selected="curclassify"
        :cardName="cardName"
        :LanFunName="LanFunName"
        :router="router"
      />
    </custom-action-sheet>
  </div>
</template>

<script>
import servicebusMore from "./views/servicebusMore.vue";
import dataItem from "./components/dataItem.vue";
import { initTrack } from './mixins/track.js';
// import { getdata } from "./mock";
export default {
  components: { servicebusMore, dataItem },
  name: "ServiceBus",
  props: {
    index: Number,
    router: Object,
  },
  mixins: [initTrack],
  data() {
    const { cardId } = this.router;
    return {
      errorImg: window.shell.ErrorImg,
      loading: false,
      classifyData: [],
      appData: [],
      curclassify: "",
      mobileConfig: {},
      show: false,
      cardName: this.router.cardName,
      loadingcomplete: {},
      LanFunName: cardId + "_h5",
    };
  },
  computed: {
    maxappsnum() {
      return (
        this.mobileConfig.serviceBusDisplayNumSelect *
        this.mobileConfig.serviceBusDisplayrowSelect
      );
    },
  },

  created() {
    window.shell.on("collectApp", ({ id, res }) => {
      this.loadingcomplete[id] = false;
      if (res.errcode === "0") {
        this.appData.length &&
          this.appData.some((el) => {
            if (el.appId === id) {
              el.appFavorite = !el.appFavorite;
            }
          });
      }
    });

    if (window.shell) {
      this.getCardData();
      this.SETModalTitle()
    }
  },
  methods: {
    getFavoriteIcon(app) {
      return app.appFavorite ? this.favouriteImg : this.unfavoriteImg;
    },
    getCardData(typeId) {
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "renderData",
          param: typeId ? { typeId } : {},
        },
        (data) => {
          this.loading = false;
          this.classifyData = data.data.classifyData;
          this.appData = data.data.appData;
          this.mobileConfig = data.data.mobileConfig;
          this.curclassify = typeId;
          this.loadedEndTrack(); // 加载结束埋点
        }
      );
    },
    handleQuery() {
      this.getCardData(this.curclassify);
      this.handleClickTrack({
        infoType: 8,
        filterInfo: {
          typeId: this.curclassify,
        }
      }); // 点击埋点
    },
    handleFavorite(app) {
      this.handleClickTrack({
        infoType: 0,
        serviceId: app.appId,
        fucType: app.appFavorite ? 3 : 2,
      }); // 点击埋点
      if (this.loadingcomplete[app.appId]) {
        return;
      }
      this.loadingcomplete[app.appId] = true;
      window.shell.collectService(
        {
          id: app.appId,
          operate: app.appFavorite ? 0 : 1, //  0:取消收藏 1:收藏
        },
        async (res) => {
          if (res.errcode === "0") {
            this.appData.forEach((val) => {
              if (val.appId === app.appId) {
                val.appFavorite = !app.appFavorite;
              }
            });
          }
        }
      );
    },
    handleMore() {
      this.show = true;
      this.handleClickTrack(); // 点击埋点
    },
    close() {
      this.show = false;
    },
    SETModalTitle() {
      let card_title = this.router.layoutCardTitle;
      const zhTitle = card_title.cardTitle || "";
      const cardTitleLang = (card_title && card_title.cardTitleLang) || [];
      const temp = cardTitleLang.find(
        (el) => el.langCode === this.$i18n.locale
      );
      this.cardName = (temp && temp.langName) || zhTitle;
    },
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 5000);
      }
    },
  },
  beforeDestroy() {
    window.shell.off("collectApp");
  },
};
</script>

<style lang="less" scoped>
/deep/ .we-action-sheet__content {
  height: 100%;
  overflow: hidden;
}
.card-servicebus {
  position: relative;

  /deep/.we-action-sheet .we-action-sheet__header {
    border: 1px solid #eef2f5;
  }

  .servicebus-content {
    width: 100%;

    &-classify {
      padding: 12px 16px;
    }
    .item-title {
      max-width: 72px;
      display: inline-block;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      line-height: 28px;
      margin-top: 4px;
      font-size: 14px;
    }
    /deep/.we-tabs__wrap {
      border-bottom: 1px solid #eef2f5;
      padding: 0 16px;
      .we-tabs__line {
        display: none;
      }
      .we-tab {
        max-width: 72px;
        margin-right: 8px;

        .we-tab__text {
          width: 100%;
        }
      }
    }
    /deep/.we-tabs__content {
      padding: 12px 16px;
    }
    /deep/.we-tabs__wrap--scrollable {
      .we-tabs__nav--complete {
        padding-left: 0 !important;
        .we-tab:first-child {
          padding: 0 0 !important;
        }
      }
    }
  }
}
</style>
