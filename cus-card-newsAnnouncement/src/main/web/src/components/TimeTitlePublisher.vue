<template>
  <div
    class="publisher"
    :class="[lastRow ? '' : 'borderBootom']"
  >
    <!-- 第一条为图片、标题格式展示，若抓取的第一条无图片，则不展示 -->
    <a v-if="showImg" class="imgWrap" @click="()=>{$emit('go-link',itemInfo)}" target="_blank">
      <div class="img" :style="bgStyle" alt="" />
      <!-- 标题 -->
      <div class="imgTitle__bg"></div>
      <div class="imgTitle__content ellipsis">{{ itemInfo.GGBT && itemInfo.GGBT.replace(/\n/g, ' ') }}</div>
    </a>
    <div v-else class="publisher__list">
      <div class="publisher__list__info">
        <a
          class="publisher__list__title ellipsis newsLink-visited"
          @click="()=>{$emit('go-link',itemInfo)}"
          target="_blank"
        >{{ itemInfo.GGBT && itemInfo.GGBT.replace(/\n/g, ' ') }}</a>
        <div class="tag topTag" v-if="itemInfo.isTop" alt="" />
        <div class="tag newTag" v-if="itemInfo.newDisplay" alt="" />
      </div>
      <div class="publisher__list__subsInfo portal-font-color-lv3">
        <div class="publisher__list__name ellipsis">
          {{ itemInfo.FBBM_DISPLAY || "-" }}
        </div>
        <div
          class="publisher__list__time ellipsis"
          v-if="config.newsDisplay.dateFormat !== 0"
        >
          {{ itemInfo.FBSJ || "-" }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    itemInfo: Object,
    config: Object,
    index: {
      type: Number,
      default: 0,
    },
    lastRow: Boolean,
  },
  computed: {
    bgStyle() {
      return `background-image: url('${this.itemInfo.picUrl}')`;
    },
    showImg() {
      return this.index === 0 && this.itemInfo.picUrl;
    },
  },
};
</script>

<style lang="less" scoped>
.publisher {
  &.borderBootom {
    box-shadow: inset 0 -1px 0 0 #f0f0f0;
  }
  .imgWrap {
    position: relative;
    display: block;
  cursor: pointer;
    .img {
      width: 100%;
      height: 152px;
      border-radius: 4px;
      background-size: cover;
      background-repeat: no-repeat;
      background-position: center;
    }
    .imgTitle__bg {
      position: absolute;
      width: 100%;
      left: 0;
      bottom: 0;
      height: 60px;
      background-image: linear-gradient(
        180deg,
        rgba(0, 0, 0, 0) 0%,
        rgba(0, 0, 0, 1) 99%
      );
      border-radius: 0 0 4px 4px;
      color: #fff;
      opacity: 0.6;
    }
    .imgTitle__content {
      position: absolute;
      width: 100%;
      left: 0;
      bottom: 0;
      padding: 24px 16px 15px 16px;
      color: #fff;
      font-size: 16px;
      z-index: 1;
    }
  }
  .publisher__list {
    height: 76px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    .publisher__list__info {
      display: flex;
      align-items: center;
      .publisher__list__title {
        min-width: 0;
        max-width: 1000px;
        font-size: 16px;
        line-height: 24px;
        height: 24px;
        display: block;
        white-space: pre;
        cursor: pointer;
      }
      .tag {
        width: 28px;
        height: 14px;
        margin-left: 6px;
        flex-shrink: 0;
        background-size: 100% 100%;
      }
      .topTag {
        background-image: url("../assets/topTag.svg");
      }
      .newTag {
        background-image: url("../assets/newTag.svg");
      }
    }
    .publisher__list__subsInfo {
      margin-top: 2px;
      line-height: 22px;
      display: flex;
      align-items: center;
    }
    .publisher__list__name {
      flex: 1;
      min-width: 0;
    }
    .publisher__list__time {
      margin-left: 20px;
      min-width: 64px;
    }
  }
}
</style>
