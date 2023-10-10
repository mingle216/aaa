<template>
  <we-dialog
    :title="$Lan(cardId, 'syncDingTalk', '同步钉钉数据')"
    :append-to-body="true"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :visible.sync="visible"
    width="560px"
  >
    <div style="height:228px">
      <div v-if="userSubscribe.synchronizeResult !== 1" class="syncError">
        <span
          class="icon iconfont icon-tixing"
          style="flex-shrink: 0;font-size: 12px;"
        ></span>
        <div style="margin-left: 8px">
          <div>
            {{
              $Lan(
                cardId,
                "syncError",
                "数据同步失败，可能由于某一端数据变为多条导致。"
              )
            }}
          </div>
          <div>
            {{ $Lan(cardId, "lastSyncErrorTime", "上一次同步失败时间") }}:
            {{ handleFomateDate(userSubscribe.lastSynchronizeTime) }}
          </div>
        </div>
      </div>
      <div class="syncForm">
        <div class="syncForm__label ellipsis">
          {{ $Lan(cardId, "syncDingTalk", "同步钉钉数据") }}
        </div>
        <we-switch
          class="syncForm__input"
          inactive-color="#BFBFBF"
          :active-color="themeColor"
          :active-value="1"
          :inactive-value="0"
          v-model="syncFlag"
        ></we-switch>
      </div>
      <div class="syncForm">
        <div class="syncForm__label ellipsis" style="width:84px">
          {{ $Lan(cardId, "pullDingTalk", "拉取钉钉日程") }}
        </div>
        <we-tooltip
          effect="dark"
          :content="
            $Lan(cardId, 'syncDingTalkTips', '单次同步时间范围限制在3个月内')
          "
          placement="top"
        >
          <span
            class="icon iconfont icon-tixing"
            style="flex-shrink: 0;font-size: 13px;color:#BFBFBF;margin-left:4px"
          ></span>
        </we-tooltip>
        <we-date-picker
          class="syncForm__input"
          v-model="timerange"
          type="datetimerange"
          :editable="false"
          range-separator="~"
          format="yyyy-MM-dd HH:mm:ss"
          :clearable="false"
        >
        </we-date-picker>
      </div>
    </div>
    <div slot="footer" class="dialog-footer" v-if="isLogin">
      <we-button
        size="medium"
        class="ellipsis portal-primary-color-hover-lv1 portal-primary-border-color-hover-lv4 portal-primary-backgroundcolor-hover-lv5"
        @click="visible = false"
        >{{ $Lan(cardId, "cancelModal", "取 消") }}</we-button
      >
      <we-button
        size="medium"
        type="primary"
        class="ellipsis portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
        :loading="okLoading"
        @click="handleOk"
        >{{ $Lan(cardId, "okModal", "确 定") }}</we-button
      >
    </div>
  </we-dialog>
</template>

<script>
import { formatDate } from "../utils/date-util";
const oneDay = 86400000;
export default {
  props: ["themeColor", "userSubscribe", "isLogin", "router"],
  watch: {
    userSubscribe: {
      immediate: true,
      handler() {
        this.syncFlag = this.userSubscribe?.canUserSubscribe || 0;
      },
    },
  },
  data() {
    return {
      cardId: "CUS_CARD_CALENDAR",
      visible: false,
      okLoading: false,
      syncFlag: 0,
      timerange: [],
    };
  },
  methods: {
    show() {
      const start = new Date(new Date().toLocaleDateString());
      const end = new Date(start).setMonth(start.getMonth() + 3) + oneDay - 1;
      this.visible = true;
      this.okLoading = false;
      this.timerange = [start, new Date(end)];
      this.syncFlag = this.userSubscribe?.canUserSubscribe || 0;
    },
    handleFomateDate(time) {
      return formatDate(time, "yyyy-MM-dd HH:mm:ss");
    },
    validDate() {
      const start = this.timerange[0];
      const end = this.timerange[1];
      const startYear = start.getFullYear();
      const startMonth = start.getMonth() + 1;
      const startDay = start.getDate();
      let stopDate = "";
      if (startMonth > 9) {
        stopDate = `${startYear + 1}/${3 -
          (12 - startMonth)}/${startDay} 23:59:59`;
      } else {
        stopDate = `${startYear}/${startMonth + 3}/${startDay} 23:59:59`;
      }
      // 不知道为啥new Date('2022-09-01 23:59:59').getTime()获取的秒数比end为2022-09-01 23:59:59时 new Date(end).getTime()少999
      if (new Date(stopDate).getTime() + 999 < new Date(end).getTime()) {
        return true; //已经超出范围了
      } else {
        return false; //没有超出范围
      }
    },
    handleOk() {
      if (this.okLoading) {
        return;
      }
      this.okLoading = true;
      if (this.validDate()) {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan(
            this.cardId,
            "syncDingTalkTips",
            "单次同步时间范围限制在3个月内"
          ),
        });
        return;
      }
      const start = this.timerange[0];
      const end = this.timerange[1];
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "pullDingdingData",
          param: {
            startTime: formatDate(start, "yyyy-MM-dd HH:mm:ss"),
            endTime: formatDate(end, "yyyy-MM-dd HH:mm:ss"),
            isPublished: this.syncFlag,
          },
        },
        (data) => {
          this.$nextTick(function() {
            this.okLoading = false;
          });
          if (data && data.errcode === "0") {
            this.visible = false;
            this.$emit("update-data");
          } else if (data) {
            let msg = "";
            switch (data.errcode) {
              case "351111":
                msg = this.$Lan(
                  this.cardId,
                  "syncError351111",
                  "全局钉钉同步开关尚未开启"
                );
                break;
              case "351112":
                msg = this.$Lan(
                  this.cardId,
                  "syncError351112",
                  `在3min内不能重复拉取数据，可在${data.errmsg}秒以后再拉取`,
                  {
                    second: data.errmsg,
                  }
                );
                break;
              case "351113":
                msg = this.$Lan(
                  this.cardId,
                  "syncError351113",
                  "已经有同步任务在执行，请稍后再试"
                );
                break;
            }
            this.$message.error(msg || data.errmsg);
          }
        }
      );
    },
  },
};
</script>

<style lang="less" scoped>
.syncForm {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
.syncForm__label {
  width: 105px;
}
.syncForm__input {
  margin-left: 12px;
}
.syncError {
  padding: 7px 16px;
  background: #ffefe6;
  border-radius: 4px;
  font-size: 12px;
  line-height: 20px;
  color: #ee3f15;
  display: flex;
  margin-bottom: 20px;
}
</style>
