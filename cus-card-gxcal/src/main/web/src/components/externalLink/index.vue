<template>
  <we-dialog
    :title="$Lan('CUS_CARD_GXCAL', 'externalReferenceDesc', '外部引用说明')"
    :append-to-body="true"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :visible.sync="visible"
    custom-class="extLinkModal"
    top="10vh"
    width="832px"
    @closed="handleClickTrack()"
  >
    <div class="extLink__address portal-font-color-lv1">
      <span style="flex-shrink: 0;">{{
        $Lan("CUS_CARD_GXCAL", "calendarAddress", "日历地址：")
      }}</span>
      <span class="extLink__url">{{ address }}</span>
      <span
        class="extLink__copy portal-primary-color-lv1"
        @click="handleCopy"
        >{{ $Lan("CUS_CARD_GXCAL", "copy", "复制") }}</span
      >
    </div>
    <div class="extLink__tab">
      <div
        class="extLink__tabItem "
        v-for="item in tabsLists"
        :key="item"
        :class="[
          item,
          selected === item
            ? 'selected portal-primary-before-backgroundcolor-lv1 portal-primary-after-backgroundcolor-lv1'
            : '',
        ]"
        @click="handleChangeTab(item)"
      >
        {{ item }}
      </div>
    </div>
    <div :style="`height: ${conHeight}px`">
      <ContainerScroll :barKeepShow="true" ref="ContainerScroll">
        <component :is="compId" class="portal-font-color-lv1"></component>
      </ContainerScroll>
    </div>
  </we-dialog>
</template>

<script>
import iphoneZh from "./iphoneZh";
import iphoneEn from "./iphoneEn";
import macZh from "./macZh";
import macEn from "./macEn";
import outlookZh from "./outlookZh";
import outlookEn from "./outlookEn";
import { clickTrack } from "../../mixins/track.js";
export default {
  props: ["router"],
  components: {
    iphoneZh,
    iphoneEn,
    macZh,
    macEn,
    outlookZh,
    outlookEn,
  },
  mixins: [clickTrack],
  data() {
    const bodyHeight = document.body.clientHeight;
    return {
      visible: false,
      locale: this.$i18n.locale || "zh_CN",
      selected: "iPhone",
      tabsLists: ["iPhone", "Mac", "Outlook"],
      conHeight: bodyHeight * 0.8 - 230,
      address: "",
    };
  },
  computed: {
    compId() {
      return `${this.selected.toLowerCase()}${
        this.locale === "zh_CN" ? "Zh" : "En"
      }`;
    },
  },
  methods: {
    show(item) {
      this.selected = "iPhone";
      this.setAdress(item);
      this.visible = true;
    },
    setAdress(item) {
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "share",
          param: {
            calId: item.wid,
          },
        },
        (res) => {
          if (res && res.errcode === "0") {
            this.address = `${
              window.shell.getLocation().origin
            }/cal/${res.data || ""}`;
          }
        }
      );
    },
    handleChangeTab(val) {
      this.selected = val;
      this.$refs.ContainerScroll.scrollTo({ y: 0 });
      this.handleClickTrack(); // 点击埋点
    },
    handleCopy() {
      window.shell.copyText(this.address);
      this.handleClickTrack({
        infoType: 14,
      }); // 点击埋点
    },
  },
};
</script>

<style lang="less" scoped>
.extLink__address {
  font-size: 16px;
  line-height: 24px;
  display: flex;
  .extLink__url {
    margin-left: 4px;
    margin-right: 12px;
    font-weight: bold;
  }
  .extLink__copy {
    cursor: pointer;
    flex-shrink: 0;
  }
}
.extLink__tab {
  margin-top: 30px;
  margin-bottom: 24px;
  display: flex;
  justify-content: center;
  .extLink__tabItem {
    width: 140px;
    height: 39px;
    font-size: 16px;
    text-align: center;
    line-height: 39px;
    position: relative;
    z-index: 1;
    cursor: pointer;
    &.iPhone:before {
      content: "";
      width: 39px;
      left: 0;
      top: 0;
      bottom: 0;
      position: absolute;
      border-radius: 39px 0 0 39px;
      z-index: -1;
      background: #f5f5f5;
    }
    &.iPhone:after {
      content: "";
      width: 110px;
      right: 0;
      top: 0;
      bottom: 0;
      position: absolute;
      transform: skewX(15deg);
      z-index: -1;
      background: #f5f5f5;
    }
    &.Mac:after {
      content: "";
      width: 130px;
      right: 0;
      top: 0;
      bottom: 0;
      position: absolute;
      transform: skewX(15deg);
      z-index: -1;
      background: #f5f5f5;
    }
    &.Outlook:before {
      content: "";
      width: 39px;
      right: 0;
      top: 0;
      bottom: 0;
      position: absolute;
      border-radius: 0 39px 39px 0;
      z-index: -1;
      background: #f5f5f5;
    }
    &.Outlook:after {
      content: "";
      width: 110px;
      right: 20px;
      top: 0;
      bottom: 0;
      position: absolute;
      transform: skewX(15deg);
      z-index: -1;
      background: #f5f5f5;
    }
    &.selected {
      color: #fff;
    }
  }
}
/deep/.extLink__info {
  font-size: 16px;
  line-height: 24px;
  font-weight: bold;
}
/deep/.extLink__img {
  margin-top: 16px;
  margin-bottom: 32px;
}
</style>
<style>
.extLinkModal .we-dialog__body {
  padding: 20px 36px;
}
.extLinkModal .we-dialog__title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-break: break-all;
  display: inline-block;
  max-width: 752px;
}
</style>
