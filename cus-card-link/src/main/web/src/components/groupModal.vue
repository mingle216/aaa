<template>
  <we-dialog
    :title="title"
    :append-to-body="true"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :visible.sync="visible"
    width="708px"
    custom-class="groupModal"
  >
    <ContainerScroll style="height: 400px" :barKeepShow="true">
      <div class="groupModal__wrap">
        <div
          class="groupModal__item portal-font-color-lv1 portal-primary-color-hover-lv1"
          v-for="(item, index) in itemInfo.linkList || []"
          :key="index"
          @click="goLink(item)"
          @mouseover="overShow(index)"
          @mouseout="outHide()"
        >
          <!-- 不展示图标 -->
          <div
            class="groupModal__item-noIcon"
            v-if="linkImgShowRadio === 0"
            :class="[
              index === hoverIndex
                ? 'portal-primary-before-backgroundcolor-lv1'
                : '',
            ]"
          >
            <div style="flex:1">
              <v-clamp autoresize :max-lines="2">
                {{ getI18nMessage(item.label) }}
              </v-clamp>
            </div>
          </div>
          <!-- 展示图标：linkDisplayRadio === 2：上下布局、左右布局 -->
          <div
            v-else
            :class="[
              linkDisplayRadio === 2
                ? 'groupModal__item-vertical '
                : 'groupModal__item-horizontal',
            ]"
          >
            <div
              class="groupModal__iconWrap"
              :class="[
                linkDisplayRadio === 2 && index === hoverIndex
                  ? 'portal-primary-border-color-lv1'
                  : '',
              ]"
            >
              <img
                v-if="item.iconType === '1'"
                :src="item.iconUrl"
                @error="imgError"
                class="groupModal__img"
              />
              <i
                v-else
                class="iconfont groupModal__icon"
                :class="[
                  item.iconUrl,
                  index === hoverIndex ? 'portal-primary-color-lv1' : '',
                ]"
              ></i>
            </div>
            <div class="groupModal__text">
              <v-clamp autoresize :max-lines="2">
                {{ getI18nMessage(item.label) }}
              </v-clamp>
            </div>
          </div>
        </div>
      </div>
    </ContainerScroll>
  </we-dialog>
</template>

<script>
import TrackMixin from "../mixins/track.js";
export default {
  props: ["linkDisplayRadio", "linkImgShowRadio", "router"],
  mixins: [TrackMixin],
  data() {
    return {
      visible: false,
      itemInfo: {},
      hoverIndex: "",
    };
  },
  methods: {
    show(item) {
      this.visible = true;
      this.itemInfo = item || {};
      this.title = this.getI18nMessage(this.itemInfo.label);
    },
    imgError(event) {
      let img = event.srcElement;
      img.src = window.shell.ErrorImg;
      img.onerror = null;
    },
    overShow(index) {
      this.hoverIndex = index;
    },
    outHide() {
      this.hoverIndex = "";
    },
    getI18nMessage(labels) {
      if (!this.$i18n.locale) {
        return labels[0].langValue;
      }
      const res = labels.filter((label) => label.langKey === this.$i18n.locale);
      const defaultLabel = labels.filter((label) => label.langKey === "zh_CN");
      if (res.length > 0) {
        return res[0].langValue || defaultLabel[0].langValue;
      }
      return defaultLabel[0].langValue;
    },
    goLink(link) {
      this.handleClickTrack({
        infoType: 4,
        filterInfo: {
          linkName: this.getI18nMessage(link.label),
        },
      });
      window.shell.openPage(link.url, 1, 2);
    },
  },
};
</script>

<style lang="less" scoped>
.groupModal__wrap {
  display: flex;
  flex-wrap: wrap;
}
.groupModal__item {
  padding: 0 12px;
  width: 136px;
  font-size: 16px;
  line-height: 24px;
  cursor: pointer;
  margin-bottom: 20px;
}
.groupModal__item-noIcon {
  display: flex;
  position: relative;
  &:before {
    content: "";
    width: 6px;
    height: 6px;
    border-radius: 50%;
    margin-right: 6px;
    margin-top: 8px;
    background-color: #bfbfbf;
  }
}
.groupModal__item-horizontal {
  display: flex;
  .groupModal__iconWrap {
    margin-right: 8px;
    flex-shrink: 0;
  }
  .groupModal__text {
    flex: 1;
  }
}
.groupModal__img {
  width: 24px;
  height: 24px;
}
.groupModal__icon {
  font-size: 24px;
  color: #b3b8c2;
}
.groupModal__item-vertical {
  display: flex;
  flex-direction: column;
  align-items: center;
  .groupModal__iconWrap {
    margin-bottom: 8px;
    width: 64px;
    height: 64px;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    border: 1.5px solid #d6dade;
  }
}
/deep/.groupModal {
  margin-top: calc(50vh - 250px) !important;
}
/deep/.we-dialog__body {
  padding: 20px 8px;
}
</style>
