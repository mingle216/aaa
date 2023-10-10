<template>
  <div :style="heightStyle">
    <ContainerScroll :barKeepShow="true">
      <div class="weekRemindPop">
        <span style="margin:5px 3px 0 0">{{
          $Lan("SYS_CARD_CALENDAR", "weekRemindEnabled", "是否开启")
        }}</span>
        <we-switch
          class="portal-primary-color-lv"
          v-model="weConfig.weekReminEnabled"
          :active-color="themeColor"
        >
        </we-switch>
        <div>
          <p class="requiredTitle">
            {{ $Lan("SYS_CARD_CALENDAR", "remindTime", "提醒日期") }}
          </p>
          <we-select
            v-model="weConfig.remindTime"
            style="width:100%"
            class="weekRemindSelect"
            popper-class="weekRemindSelectPop"
            :placeholder="
              $Lan('SYS_CARD_CALENDAR', 'selectPlaceholder', '请选择')
            "
            clearable
            :disabled="!weConfig.weekReminEnabled"
          >
            <we-option
              v-for="item in remindTimeData"
              :key="item.value"
              :label="$Lan('SYS_CARD_CALENDAR', item.label, item.labelZh)"
              :value="item.value"
            >
            </we-option>
          </we-select>
          <div
            v-show="weConfig.weekReminEnabled && weConfig.remindTime === ''"
            class="errorMsg"
          >
            {{ $Lan("SYS_CARD_CALENDAR", "errorMsg", "提醒时间不为空") }}
          </div>
          <p style="margin:5px 0;">
            {{ $Lan("SYS_CARD_CALENDAR", "remindTimeHour", "提醒时间") }}
          </p>
          <we-select
            v-model="weConfig.remindTimeHour"
            style="width:80%;"
            class="weekRemindSelect"
            popper-class="weekRemindSelectPop"
            :placeholder="
              $Lan('SYS_CARD_CALENDAR', 'selectPlaceholder', '请选择')
            "
            clearable
            :disabled="!weConfig.weekReminEnabled"
          >
            <we-option
              v-for="key in 23"
              :key="key"
              :label="key < 10 ? `0${key}` : key"
              :value="`${key}`"
            >
            </we-option>
          </we-select>
          <p style="margin: 5px 0">
            {{ $Lan("SYS_CARD_CALENDAR", "remindWay", "提醒方式") }}
          </p>
          <we-checkbox-group
            v-model="weConfig.remindWay"
            class="remind-checkbox"
            :disabled="!weConfig.weekReminEnabled"
          >
            <we-checkbox
              v-for="item in weekRemindChannel"
              style="margin-right:15px;"
              :key="item.value"
              :label="item.value"
              :disabled="item.value == 'INBOX'"
              >{{ item.label }}</we-checkbox
            >
          </we-checkbox-group>
        </div>
        <div class="btn-list">
          <we-button size="mini" @click="handleCancel()">{{
            $Lan("SYS_CARD_CALENDAR", "cancel", "取消")
          }}</we-button>
          <we-button size="mini" type="primary" @click="handleSave()">{{
            $Lan("SYS_CARD_CALENDAR", "confirm", "确定")
          }}</we-button>
        </div>
      </div>
    </ContainerScroll>
  </div>
</template>

<script>
import { clickTrack } from "../mixins/track.js";
import { remindTimeData } from "../constant.js";
export default {
  props: ["router", "weekRemindConfig", "weekRemindChannel", "themeColor"],
  mixins: [clickTrack],
  data() {
    return {
      remindTimeData,
      weConfig: {
        weekReminEnabled: false,
        remindTime: "", //周提醒日期
        remindTimeHour: "12", //周提醒时间
        remindWay: ["INBOX"], //周提醒方式
      },
    };
  },
  computed: {
    primaryColor() {
      return "portal-primary-color-hover-lv1";
    },
    heightStyle() {
      const len = this.weConfig.remindWay.length;
      return len > 8 ? "height: 240px" : "";
    },
  },
  watch: {
    weekRemindConfig: {
      immediate: true,
      handler(val) {
        if (val) {
          this.weConfig = Object.assign(this.weConfig, {
            weekReminEnabled: true,
            remindTime: val.remindTime || "",
            remindTimeHour: val.remindTimeHour || "12",
            remindWay: val.remindWay ? val.remindWay.split(",") : ["INBOX"],
          });
        }
      },
      deep: true,
    },
  },
  methods: {
    handleChange() {
      const subscribe = [],
        unscribe = [];
      this.lists.forEach((item) => {
        if (item.subscribe) {
          subscribe.push(item.wid);
        } else {
          unscribe.push(item.wid);
        }
      });
    },
    handleCancel() {
      this.$emit("cancelPop");
      setTimeout(() => {
        //表单数据初始化
        this.resetForm();
      }, 300);
    },
    resetForm() {
      if (this.weekRemindConfig) {
        this.weConfig = Object.assign(this.weConfig, {
          weekReminEnabled: true,
          remindTime: this.weekRemindConfig.remindTime, //周提醒日期
          remindTimeHour: this.weekRemindConfig.remindTimeHour, //周提醒时间
          remindWay: this.weekRemindConfig.remindWay.split(","), //周提醒方式
        });
      } else {
        this.weConfig = Object.assign(this.weConfig, {
          weekReminEnabled: false,
          remindTime: "", //周提醒日期
          remindTimeHour: "12", //周提醒时间
          remindWay: ["INBOX"], //周提醒方式
        });
      }
    },
    handleSave() {
      if (this.weConfig.remindTime === "" && this.weConfig.weekReminEnabled)
        return;
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "setWeeklyReminder",
          param: {
            openRemind: this.weConfig.weekReminEnabled ? "1" : "0",
            remindTime: this.weConfig.remindTime,
            remindTimeHour: this.weConfig.remindTimeHour,
            remindWay: this.weConfig.remindWay.join(","),
          },
        },
        (res) => {
          if (res && res.errcode === "0") {
            this.$message.success({
              message: this.$Lan("SYS_CARD_CALENDAR", "successMsg", "修改成功"),
            });
            //表单数据初始化
            this.$emit("cancelPop");
            this.$emit("update");
          }
        }
      );
      this.handleClickTrack({
        infoType: 12,
      }); // 点击埋点
    },
  },
};
</script>

<style lang="less" scoped>
.weekRemindPop {
  padding-right: 6px;
  .weekRemindSelect {
    /deep/.we-input__inner {
      height: 25px !important;
      line-height: 25px;
    }
    /deep/.we-input__icon {
      line-height: 25px;
    }
  }
  .requiredTitle {
    margin: 5px 0;
    &:before {
      content: "*";
      color: red;
      margin-right: 3px;
    }
  }
  .errorMsg {
    color: red;
    font-size: 12px;
    margin-top: 5px;
  }
  .we-checkbox__inner {
    border-color: transparent !important;
  }
  .we-switch__core {
    border-color: transparent !important;
  }
  .btn-list {
    margin-top: 15px;
    text-align: right;
  }
}
</style>
<style>
.weekRemindSelectPop .we-select-dropdown__item {
  height: 25px;
  line-height: 25px;
}
</style>
