<template>
  <div class="timeTitle" :class="[getCardStyle, lastRow ? '' : 'borderBootom']">
    <div class="timeTitle__titleWrap">
      <a
        class="timeTitle__title ellipsis newsLink-visited"
        :class="[itemInfo.read ? 'portal-font-color-lv3' : '']"
        target="_blank"
        @click="
          () => {
            $emit('go-link', itemInfo);
          }
        "
        >{{ itemInfo.title && itemInfo.title.replace(/\n/g, " ") }}</a
      >
      <div class="tag topTag" v-if="itemInfo.isTop" alt="" />
      <div class="tag newTag" v-if="itemInfo.newDisplay" alt="" />
    </div>
    <div
      class="portal-font-color-lv3 timeDisplay"
      v-if="config.newsDisplay.dateFormat !== 0"
    >
      {{ itemInfo.pubTime || "-" }}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    itemInfo: Object,
    config: Object,
    cardWidth: Number,
    lastRow: Boolean,
  },
  computed: {
    getCardStyle() {
      if (
        this.config.newsDisplay.dateFormat === 0 ||
        (this.cardWidth >= 600 && this.config.newsColumns === 1)
      ) {
        return "title-row-lower";
      } else {
        return "title-row-higher";
      }
    },
  },
};
</script>

<style scoped lang="less">
.timeTitle {
  background: #ffffff;
  display: flex;
  &.borderBootom {
    box-shadow: inset 0 -1px 0 0 #f0f0f0;
  }
  .timeDisplay {
    letter-spacing: 0;
  }
}
.timeTitle__titleWrap {
  display: flex;
  align-items: center;
  .timeTitle__title {
    min-width: 0;
    max-width: 1000px;
    display: block;
    font-size: 16px;
    white-space: pre;
    line-height: 24px;
    height: 24px;
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
.title-row-higher {
  height: 76px;
  flex-direction: column;
  justify-content: center;
  .timeTitle__title {
    line-height: 24px;
  }
  .timeDisplay {
    margin-top: 2px;
    line-height: 22px;
  }
}
.title-row-lower {
  height: 48px;
  align-items: center;
  .timeTitle__titleWrap {
    flex: 1;
    min-width: 0;
  }
  .timeDisplay {
    margin-left: 16px;
  }
}
</style>
