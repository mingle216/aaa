<template>
  <we-dialog
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :visible.sync="visible"
    custom-class="deleteScheduleModal"
    top="30vh"
    width="420px"
    @closed="handleClickTrack()"
    @click.native.stop
  >
    <div class="deleteSchedule__body">
      <div class="deleteSchedule__title">
        <i
          class="we-icon-warning"
          style="font-size:20px;margin-right: 5px;color: #FF9900;flex-shrink:0"
        ></i
        >{{
          $Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            "deleteScheduleTitle",
            "确认要删除该日程吗？"
          )
        }}
      </div>
      <div class="deleteSchedule__content portal-font-color-lv2">
        {{
          $Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            "deleteScheduleContent",
            "删除后将不可恢复"
          )
        }}
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <we-button
        size="medium"
        class="portal-primary-color-hover-lv1 portal-primary-border-color-hover-lv4 portal-primary-backgroundcolor-hover-lv5"
        @click="visible = false"
        >{{
          $Lan("SYS_CARD_CALENDAR_DETAIL", "cancelModal", "取 消")
        }}</we-button
      >
      <we-button
        type="primary"
        size="medium"
        style="background: #FF230C;border-color:#FF230C"
        :loading="okLoading"
        @click="handleOk"
        >{{ $Lan("SYS_CARD_CALENDAR_DETAIL", "okModal", "确 定") }}</we-button
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
      visible: false,
      okLoading: false,
      itemInfo: null,
    };
  },
  methods: {
    show(item) {
      this.visible = true;
      this.okLoading = false;
      this.itemInfo = item;
    },
    handleOk() {
      this.okLoading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "remove",
          param: {
            wid: this.itemInfo.eventId,
            delType: 0,
            delDay: this.itemInfo.startDate,
          },
        },
        (data) => {
          this.$nextTick(function() {
            this.okLoading = false;
          });
          if (data && data.errcode === "0") {
            this.visible = false;
            this.$emit("update-data");
          }
        }
      );
    },
  },
};
</script>

<style lang="less" scoped>
/deep/.deleteScheduleModal {
  .we-dialog__header {
    display: none;
  }
}
.deleteSchedule__body {
  padding: 20px;
}
.deleteSchedule__title {
  font-weight: bold;
  font-size: 16px;
  line-height: 24px;
  display: flex;
  align-items: center;
}
.deleteSchedule__content {
  margin-top: 12px;
  margin-left: 25px;
  font-size: 12px;
  line-height: 20px;
}
</style>
