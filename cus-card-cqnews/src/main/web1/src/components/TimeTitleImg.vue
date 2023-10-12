<template>
  <div class="timeTitleImg" :class="[lastRow ? '' : 'borderBootom']">
    <a
      class="timeTitleImg__img"
      :style="bgStyle"
      @click="
        () => {
          $emit('go-link', itemInfo);
        }
      "
      target="_blank"
      v-if="itemInfo.picUrl"
    ></a>
    <div
      class="timeTitleImg__info"
      :class="[itemInfo.picUrl && extremum ? 'extremum' : '']"
    >
      <div class="timeTitleImg__titleWrap">
        <a
          class="timeTitleImg__info__title ellipsis newsLink-visited"
          :class="[itemInfo.read ? 'portal-font-color-lv3' : '']"
          @click="
            () => {
              $emit('go-link', itemInfo);
            }
          "
          target="_blank"
          >{{ itemInfo.title && itemInfo.title.replace(/\n/g, " ") }}</a
        >
        <div class="tag topTag" v-if="itemInfo.isTop" alt="" />
        <div class="tag newTag" v-if="itemInfo.newDisplay" alt="" />
      </div>
      <div class="lineHeight_22 ellipsis portal-font-color-lv3">
        {{ itemInfo.abstractVal || itemInfo.contents || "-" }}
      </div>
      <div class="lineHeight_22 timeTitleImg__info__foot portal-font-color-lv3">
        <div class="timeTitleImg__info__channel ellipsis">
          {{ `${itemInfo.columnName}${itemInfo.channelName}` || "-" }}
        </div>
        <div
          class="timeTitleImg__info__time ellipsis"
          v-if="config.newsDisplay.dateFormat !== 0"
        >
          {{ itemInfo.pubTime || "-" }}
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
    lastRow: Boolean,
    extremum: Boolean,
  },
  computed: {
    bgStyle() {
      return `background-image: url('${this.itemInfo.picUrl}')`;
    },
  },
};
</script>

<style lang="less" scoped>
.timeTitleImg {
  display: flex;
  align-items: center;
  padding: 14px 0;
  &.borderBootom {
    box-shadow: inset 0 -1px 0 0 #f0f0f0;
  }
  .lineHeight_22 {
    line-height: 22px;
  }
  .timeTitleImg__img {
    margin-right: 16px;
    width: 92px;
    height: 70px;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    border-radius: 4px;
    cursor: pointer;
  }
  .timeTitleImg__info {
    flex: 1;
    min-width: 0;
    .timeTitleImg__titleWrap {
      display: flex;
      align-items: center;
      margin-bottom: 2px;
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
    .timeTitleImg__info__title {
      min-width: 0;
      max-width: 1000px;
      display: block;
      font-size: 16px;
      line-height: 24px;
      height: 24px;
      white-space: pre;
      cursor: pointer;
    }
    .timeTitleImg__info__foot {
      display: flex;
      margin-top: 2px;
      .timeTitleImg__info__channel {
        flex: 1;
        min-width: 0;
      }
      .timeTitleImg__info__time {
        margin-left: 20px;
      }
    }
    &.extremum {
      .timeTitleImg__info__foot {
        .timeTitleImg__info__time {
          max-width: 64px;
        }
      }
    }
  }
}
</style>
