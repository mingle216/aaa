<template>
  <we-dialog
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :visible.sync="visible"
    custom-class="repeatSchedualModal"
    top="30vh"
    width="420px"
    @closed="handleClickTrack()"
    @click.native.stop
  >
    <div
      class="repeatSchdule__close portal-font-color-lv3 portal-primary-color-hover-lv1"
      @click="visible = false"
    >
      <span
        class="icon iconfont icon-danchuangclose"
        style="font-size: 14px;"
      ></span>
    </div>
    <div class="repeatSchdule__body">
      <div class="repeatSchdule__title">
        <i
          class="we-icon-warning"
          style="font-size:20px;margin-right: 5px;color: #FF9900;flex-shrink:0"
        ></i
        >{{
          $Lan(
            cardId,
            optType === "delete"
              ? "deleteRepeatSchedule"
              : "editRepeatSchedule",
            optType === "delete" ? "重复日程删除提示" : "重复日程编辑提示"
          )
        }}
      </div>
      <div style="margin-left: 25px;">
        <div class="repeatSchdule__content portal-font-color-lv3">
          {{ $Lan(cardId, "chooseRange", "请选择生效范围") }}
        </div>
        <we-radio-group v-model="range">
          <we-radio :label="1" style="width: 100%;">{{
            $Lan(cardId, "onlyCurrSchedule", "仅本条日程")
          }}</we-radio>
          <we-radio :label="0" style="width: 100%;margin-top:12px">{{
            $Lan(
              cardId,
              optType === "delete" ? "allSchedule" : "everyFollowSchedule",
              optType === "delete" ? "全部日程" : "后续每一条日程"
            )
          }}</we-radio>
        </we-radio-group>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <we-button
        size="medium"
        class="portal-primary-color-hover-lv1 portal-primary-border-color-hover-lv4 portal-primary-backgroundcolor-hover-lv5"
        @click="visible = false"
        >{{ $Lan(cardId, "cancelModal", "取 消") }}</we-button
      >
      <we-button
        type="primary"
        size="medium"
        class="ellipsis portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
        :loading="okLoading"
        @click="handleOk"
        >{{ $Lan(cardId, "okModal", "确 定") }}</we-button
      >
    </div>
  </we-dialog>
</template>

<script>
import { clickTrack } from "../../mixins/track.js";
export default {
  props: ["router"],
  mixins: [clickTrack],
  data() {
    return {
      cardId: "SYS_CARD_CALENDAR_DETAIL",
      visible: false,
      okLoading: false,
      itemInfo: null,
      optType: "delete",
      range: 1,
    };
  },
  methods: {
    show(item, type) {
      this.optType = type || "delete";
      this.visible = true;
      this.okLoading = false;
      this.itemInfo = item;
      this.range = 1;
    },
    handleOk() {
      this.visible = false;
      if (this.optType === "edit") {
        this.$emit("edit-ok", this.itemInfo, this.range);
        return;
      }
      this.okLoading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "remove",
          param: {
            wid: this.itemInfo.eventId,
            delType: this.range,
            delDay: this.range == 1 ? this.itemInfo.startDate : "",
          },
        },
        (data) => {
          this.$nextTick(function() {
            this.okLoading = false;
          });
          if (data && data.errcode === "0") {
            this.$emit("update-data");
          }
        }
      );
    },
  },
};
</script>

<style lang="less" scoped>
/deep/.repeatSchedualModal {
  .we-dialog__header {
    display: none;
  }
}
.repeatSchdule__close {
  float: right;
  cursor: pointer;
}
.repeatSchdule__body {
  padding: 10px 20px 20px;
}
.repeatSchdule__title {
  font-weight: bold;
  font-size: 16px;
  line-height: 24px;
  display: flex;
  align-items: center;
}
.repeatSchdule__content {
  margin-top: 8px;
  margin-bottom: 16px;
  font-size: 12px;
  line-height: 20px;
}
</style>
