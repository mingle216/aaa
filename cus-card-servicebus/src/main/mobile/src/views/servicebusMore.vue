<template>
  <div class="servicebus-more">
    <!-- <div class="title">
      <img class="arrow" :src="arrowIcon" width="36" @click="close" />
      <h3>业务直通车</h3>
      <span></span>
    </div> -->

    <div class="body-content">
      <we-tabs
        v-if="classifyData.length > 1"
        @change="handleQuery"
        v-model="selectedClassify"
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
                it.typeId == selectedClassify ? 'portal-primary-color-lv1' : '',
              ]"
              >{{ it.typeName }}</span
            >
          </template>
          <app-item
            :appData="appData"
            :router="router"
            @handleFavorite="handleFavorite"
          ></app-item>
          <EmptyCon
            :tip="$Lan(LanFunName, 'emptytips', '该分类下无服务')"
            :height="180"
            style="width: 100%"
            v-if="!appData.length"
          />
        </we-tab>
      </we-tabs>
      <div v-if="classifyData.length == 1" class="body-content-classify">
        <app-item
          :appData="appData"
          :LanFunName="LanFunName"
          :router="router"
          @handleFavorite="handleFavorite"
        ></app-item>
      </div>
    </div>
  </div>
</template>

<script>
import appItem from ".././components/appItem.vue";
import { clickTrack } from '../mixins/track.js';
export default {
  props: {
    classifyData: {
      type: Array,
      default: () => {
        return [];
      },
    },
    appData: {
      type: Array,
      default: () => {
        return [];
      },
    },
    selected: String,
    cardName: String,
    LanFunName:String,
    router: Object,
  },
  components: { appItem },
  mixins: [clickTrack],
  data() {
    return {
      selectedClassify: this.selected,
      arrowIcon: require("../.././public/img/icon-nav-arrow.png"),
    };
  },
  computed: {
    getbodyHeight() {
      return {
        height: `${document.body.clientHeight * 0.9 - 42}px`,
        overflow: "auto",
      };
    },
  },
  methods: {
    handleQuery() {
      this.$emit("getCardData", this.selectedClassify);
      this.handleClickTrack({
        infoType: 8,
        typeId: this.selectedClassify,
      }); // 点击埋点
    },
    handleFavorite(app) {
      this.$emit("handleFavorite", app);
    },
    close() {
      this.$emit("close");
    },
  },
  watch: {
    selected(v) {
      this.selectedClassify = v;
    },
  },
};
</script>

<style lang="less" scoped>

.servicebus-more {
  position: relative;
  width: 100%;
  height: 100%;

  .title {
    width: 100%;
    height: 42px;
    display: flex;
    text-align: center;
    align-items: center;
    background: #ffffff;
    font-size: 18px;
    box-shadow: inset 0 -1px 0 0 #e7edf1;
    .arrow {
      width: 18px;
      height: 18px;
      margin-left: 16px;
    }
    h3 {
      flex: 1;
    }
    h3,
    span {
      width: 40px;
    }
  }
  .body-content {
    height: 100%;
    overflow-y: hidden;
    /deep/.we-tabs {
      height: 100%;
    }
    /deep/.we-tabs__content {
      height: calc(100% - 42px);
      overflow: auto;
    }
    &-classify {
      height:100%;
      overflow-y: auto;
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
      font-size:14px;
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

  .app {
    height: 58px;
    display: flex;
    display: -webkit-flex;
    align-items: center;
    .appname {
      font-size: 14px;
      margin-left: 12px;
      width: calc(100% - 48px - 60px);
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}
</style>