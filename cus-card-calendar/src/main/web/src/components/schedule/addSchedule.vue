<template>
  <div @click.native.stop>
    <we-dialog
      :title="title"
      :append-to-body="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="visible"
      custom-class="addScheduleModal"
      width="620px"
      @closed="handleClose"
      @click.native.stop
    >
      <ContainerScroll style="height: 435px" :barKeepShow="true">
        <we-form
          :model="ruleForm"
          :rules="rules"
          ref="Form"
          label-width="98px"
          style="padding: 20px 20px 0 20px"
          :class="{ ieForm: isIE }"
          size="medium"
        >
          <we-form-item
            v-if="canCreateSchedule == 1 && canCreateGroupSche == 1"
            :label="$Lan(cardId, 'scheduleType', '日程类型')"
            prop="calendarId"
            required
          >
            <we-radio-group v-model="ruleForm.calendarId" :disabled="isEdit">
              <we-radio label="1">{{
                $Lan(cardId, "personalSchedule", "个人日程")
              }}</we-radio>
              <we-radio label="2">{{
                $Lan(cardId, "groupSchedule", "群组日程")
              }}</we-radio>
            </we-radio-group>
          </we-form-item>
          <we-form-item
            :label="$Lan(cardId, 'scheduleTitle', '日程主题')"
            prop="eventTitle"
          >
            <we-input
              v-model.trim="ruleForm.eventTitle"
              :maxlength="50"
              :placeholder="$Lan(cardId, 'inputPlaceholder', '请输入')"
              show-word-limit
            ></we-input>
          </we-form-item>
          <we-form-item
            :label="$Lan(cardId, 'scheduleTime', '日程时间')"
            prop="timerange"
          >
            <we-date-picker
              v-model="ruleForm.timerange"
              :type="dateType"
              popper-class="caledarDatePopper"
              :editable="false"
              range-separator="~"
              :start-placeholder="$Lan(cardId, 'startTime', '开始时间')"
              :end-placeholder="$Lan(cardId, 'endTime', '结束时间')"
              :format="dateFormat"
              :clearable="false"
              style="margin-right:20px;width:395px"
              @change="handleRepeatDisabled()"
              @blur="handleBlur"
              @focus="allDayDisabled = true"
            >
            </we-date-picker>
            <we-checkbox
              v-model="ruleForm.isAllDay"
              :true-label="1"
              :false-label="0"
              :disabled="allDayDisabled"
              @change="handleCheckAllChange"
              >{{ $Lan(cardId, "allDay", "全天") }}</we-checkbox
            >
          </we-form-item>
          <!-- 群组日程参与人员 -->
          <we-form-item
            :label="$Lan(cardId, 'participants', '参与人员')"
            prop="authObjs"
            v-if="ruleForm.calendarId === '2'"
            :rules="participantsRule"
          >
            <div
              class="customBtn portal-font-color-lv2 ellipsis"
              v-if="!ruleForm.authObjs.length"
              @click="handleChoose"
            >
              <span class="iconfont icon-geren portal-font-color-lv3"></span
              >{{ $Lan(cardId, "selectPeople", "选择人员") }}
            </div>
            <div
              v-else
              class="customBtn ellipsis portal-primary-color-lv1 portal-primary-backgroundcolor-lv5 portal-primary-border-color-lv1"
              @click="handleChoose"
            >
              <span class="iconfont icon-geren"></span
              >{{
                $Lan(cardId, "chosen", "已选择{num}人", {
                  num: ruleForm.authObjs.length,
                })
              }}
            </div>
          </we-form-item>
          <!-- 超过24h的日程不允许重复，重复日程仅选择编辑本条日程时，不允许修改重复类型 -->
          <we-form-item
            :label="$Lan(cardId, 'repeat', '重复')"
            prop="eventRepeatType"
          >
            <we-select
              v-model="ruleForm.repeatType"
              style="width:100%"
              :disabled="repeatDisabled"
              popper-class="calendarSelectPop calendarRepeatSelectPop"
            >
              <we-option
                v-for="item in repeatData"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </we-option>
              <we-option
                :value="6"
                disabled
                class="customSelctOption portal-primary-backgroundcolor-hover-lv5"
              >
                <div class="portal-font-color-lv1" @click.stop="handleCustomClick">
                  {{
                    this.$Lan(
                      "SYS_CARD_CALENDAR_DETAIL",
                      "repeatCustom",
                      "自定义"
                    )
                  }}
                </div>
              </we-option>
            </we-select>
          </we-form-item>
          <we-form-item
            :label="$Lan(cardId, 'location', '地点')"
            prop="eventAddr"
          >
            <we-input
              v-model.trim="ruleForm.eventAddr"
              :maxlength="100"
              :placeholder="$Lan(cardId, 'inputPlaceholder', '请输入')"
              show-word-limit
            ></we-input>
          </we-form-item>
          <we-form-item
            :label="$Lan(cardId, 'description', '描述内容')"
            prop="eventDesc"
          >
            <we-input
              v-model.trim="ruleForm.eventDesc"
              type="textarea"
              :rows="3"
              :maxlength="500"
              :placeholder="$Lan(cardId, 'inputPlaceholder', '请输入')"
              show-word-limit
            ></we-input>
          </we-form-item>
          <we-form-item
            :label="$Lan(cardId, 'linkUrl', '链接地址')"
            prop="linkUrl"
          >
            <we-input
              v-model.trim="ruleForm.linkUrl"
              :maxlength="500"
              :placeholder="$Lan(cardId, 'inputPlaceholder', '请输入')"
              show-word-limit
            ></we-input>
          </we-form-item>
          <we-form-item
            :label="$Lan(cardId, 'remind', '提醒')"
            prop="remindTimes"
          >
            <we-select
              v-model="ruleForm.remindTimes"
              style="width:100%"
              popper-class="calendarSelectPop"
              multiple
              @change="handleRemindChange"
            >
              <we-option
                v-for="item in remindData"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </we-option>
            </we-select>
          </we-form-item>
          <we-form-item
            v-if="ruleForm.remindTimes[0] !== ''"
            :label="$Lan(cardId, 'remindType', '提醒方式')"
            prop="remindChannel"
          >
            <we-checkbox-group v-model="ruleForm.remindChannel">
              <we-checkbox
                v-for="item in scheduleRemindChannel"
                :key="item.value"
                :label="item.value"
                :disabled="item.value == 'INBOX'"
                >{{ item.label }}</we-checkbox
              >
            </we-checkbox-group>
          </we-form-item>
        </we-form>
      </ContainerScroll>
      <div slot="footer" class="dialog-footer">
        <we-button
          size="medium"
          class="ellipsis portal-primary-color-hover-lv1 portal-primary-border-color-hover-lv1"
          @click="visible = false"
          >{{
            $Lan("SYS_CARD_CALENDAR_DETAIL", "cancelModal", "取 消")
          }}</we-button
        >
        <we-button
          type="primary"
          size="medium"
          class="ellipsis portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
          :loading="okLoading"
          @click="handleOk"
          >{{ $Lan("SYS_CARD_CALENDAR_DETAIL", "okModal", "确 定") }}</we-button
        >
      </div>
    </we-dialog>
    <custom-cycle-modal
      ref="CustomCycleModal"
      @update-data="handleCustomCycle"
    ></custom-cycle-modal>
    <choose-person
      :router="router"
      :defaultChecked="ruleForm.authObjs"
      ref="ChoosePerson"
      @change-check="handleChangeCheck"
    ></choose-person>
  </div>
</template>

<script>
import { weekEnArr } from "../../constant.js";
import { clickTrack } from "../../mixins/track.js";
import repeatRemindData from "../../mixins/repeatRemindData.js";
import { formatDate } from "../../utils/date-util";
import CustomCycleModal from "./customCycleModal";
import ChoosePerson from "./choosePerson";
const oneDay = 86400000;
const oneHour = 3600000;
export default {
  props: [
    "router",
    "scheduleRemindChannel",
    "canCreateSchedule",
    "canCreateGroupSche",
  ],
  mixins: [clickTrack, repeatRemindData],
  components: {
    CustomCycleModal,
    ChoosePerson,
  },
  data() {
    return {
      cardId: "SYS_CARD_CALENDAR_DETAIL",
      visible: false,
      isEdit: false,
      okLoading: false,
      isIE: window.shell.isIE(),
      dateFormat: "yyyy-MM-dd HH:mm",
      dateType: "datetimerange",
      ruleForm: {
        calendarId: "1",
        eventTitle: "",
        timerange: [],
        eventAddr: "",
        eventDesc: "",
        linkUrl: "",
        repeatType: 0,
        remindTimes: [15],
        remindChannel: ["INBOX"],
        isAllDay: 0,
        authObjs: [],
      },
      rules: {
        eventTitle: [
          {
            required: true,
            message: `${this.$Lan(
              "SYS_CARD_CALENDAR_DETAIL",
              "inputPlaceholder",
              "请输入"
            )} ${this.$Lan(
              "SYS_CARD_CALENDAR_DETAIL",
              "scheduleTitle",
              "日程主题"
            ).toLowerCase()}`,
            trigger: "blur",
          },
        ],
        timerange: [
          {
            type: "array",
            required: true,
            message: `${this.$Lan(
              "SYS_CARD_CALENDAR_DETAIL",
              "inputPlaceholder",
              "请输入"
            )} ${this.$Lan(
              "SYS_CARD_CALENDAR_DETAIL",
              "scheduleTime",
              "日程时间"
            ).toLowerCase()}`,
            trigger: "blur",
          },
        ],
      },
      repeatDisabled: false,
      customCycleParams: {
        eventRepeatType: 0,
      },
      range: 0,
      allDayDisabled: false,
    };
  },
  computed: {
    title() {
      return this.$Lan(
        "SYS_CARD_CALENDAR_DETAIL",
        this.isEdit ? "editScheduleModal" : "addScheduleModal",
        this.isEdit ? "编辑日程" : "添加日程"
      );
    },
    participantsRule() {
      if (this.ruleForm.calendarId === "2") {
        return [
          {
            required: true,
            message: `${this.$Lan(
              this.cardId,
              "inputPlaceholder",
              "请输入"
            )} ${this.$Lan(
              this.cardId,
              "participants",
              "参与人员"
            ).toLowerCase()}`,
            trigger: "blur",
          },
        ];
      }
      return [];
    },
  },
  methods: {
    show(item, selecteDate, range) {
      this.visible = true;
      this.isEdit = item ? true : false;
      this.range = range || 0;
      this.allDayDisabled = false;
      if (this.isEdit) {
        this.$nextTick(function() {
          const endTime = item.endTimeOrigin && new Date(item.endTimeOrigin);
          this.ruleForm = {
            wid: item.eventId,
            calendarId: item.calWid || "1",
            modifyType: this.range,
            modifyDay: formatDate(selecteDate, "yyyy-MM-dd"),
            eventTitle: item.eventTitle || "",
            timerange: [new Date(item.startTimeOrigin), endTime],
            eventAddr: item.location || "",
            eventDesc: item.eventDesc || "",
            linkUrl: item.linkUrl || "",
            repeatType: item.repeatType || 0,
            remindTimes: item.remindTime || [""],
            remindChannel: item.remindChannel?.split(",") || ["INBOX"],
            deadline: formatDate(item.startTime, "yyyy-MM-dd HH:mm:ss"),
            isAllDay: item.isAllDay || 0,
            authObjs: item.dubboCalGroupReceiver?.userInfos || [],
          };
          this.dateFormat = item.isAllDay ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm";
          this.formatRepeatType(item);
          this.handleRepeatDisabled(true);
          this.$refs.Form && this.$refs.Form.clearValidate();
        });
      } else {
        const start = new Date(selecteDate);
        this.ruleForm.timerange = [
          new Date(selecteDate) || new Date(),
          new Date(start.getTime() + oneHour),
        ]; // 结束时间默认为开始时间之后一小时
        this.ruleForm.calendarId = this.canCreateSchedule == 1 ? "1" : "2";
        this.$refs.Form && this.$refs.Form.clearValidate();
      }

      this.handleClickTrack(); // 点击埋点
    },
    handleClose() {
      this.ruleForm = {
        calendarId: "1",
        eventTitle: "",
        timerange: [],
        eventAddr: "",
        eventDesc: "",
        linkUrl: "",
        repeatType: 0,
        remindTimes: [15],
        remindChannel: ["INBOX"],
        isAllDay: 0,
        authObjs: [],
      };
      this.dateFormat = "yyyy-MM-dd HH:mm";
      this.dateType = "datetimerange";
      this.repeatDisabled = false;
      this.customCycleParams = { eventRepeatType: 0 };
      this.okLoading = false;
      this.loading = false;
      this.handleClickTrack(); // 点击埋点
    },
    handleBlur() {
      // 解决快速点击全天导致时间选择器面板样式错乱问题
      setTimeout(() => {
        this.allDayDisabled = false;
      }, 350);
    },
    handleCheckAllChange(flag) {
      if (flag) {
        this.dateFormat = "yyyy-MM-dd";
        this.dateType = "daterange";
        this.ruleForm.remindTimes = [-540];
      } else {
        this.dateFormat = "yyyy-MM-dd HH:mm";
        this.dateType = "datetimerange";
        this.ruleForm.remindTimes = [15];
      }
    },
    handleRemindChange(val) {
      if (!val || !val.length) {
        this.ruleForm.remindTimes = [""];
      } else if (val[val.length - 1] === "") {
        //选择无提醒时，去掉其他的提醒方式
        this.ruleForm.remindTimes = [""];
      } else if (val.length > 1 && val.includes("")) {
        this.ruleForm.remindTimes = val.filter((item) => item !== ""); //选择其他提醒方式时，去掉无提醒
      }
    },
    handleRepeatDisabled(init) {
      // 超过24h的日程不允许重复,选择重复日程的当天日程也不允许修改重复
      if (this.range === 1) {
        this.repeatDisabled = true;
      } else if (this.ruleForm.timerange.length === 2) {
        const diff =
          new Date(this.ruleForm.timerange[1]).setSeconds(0) -
          new Date(this.ruleForm.timerange[0]).setSeconds(0);
        if (diff > oneDay) {
          this.ruleForm.repeatType = 0;
          this.repeatDisabled = true;
        } else {
          this.repeatDisabled = false;
        }
      } else {
        this.repeatDisabled = false;
      }
      if (!init && this.ruleForm.repeatType.toString().includes("custom")) {
        this.$refs.CustomCycleModal.updateData(
          this.ruleForm.timerange,
          this.ruleForm.isAllDay,
          this.customCycleParams
        );
      }
    },
    handleChoose() {
      this.$refs.ChoosePerson.show();
    },
    handleCustomClick() {
      this.$refs.CustomCycleModal.show(
        this.ruleForm.timerange,
        this.ruleForm.isAllDay,
        this.customCycleParams
      );
    },
    handleChangeRepeat() {
      let params = {
        eventRepeatType: this.ruleForm.repeatType,
      };
      switch (this.ruleForm.repeatType) {
        case 1: //每天
          params.eventRepeatType = 1;
          params.eventRepeatData = 1;
          break;
        case 2: //每周周*
          params.eventRepeatType = 2;
          params.eventRepeatData = 1;
          params.dayOfWeek =
            weekEnArr[new Date(this.ruleForm.timerange[0]).getDay()];
          break;
        case 3: //每月
          params.eventRepeatType = 3;
          params.eventRepeatData = 1;
          params.dayOfMonth = new Date(this.ruleForm.timerange[0]).getDate();
          break;
        case 4: //每年
          params.eventRepeatType = 4;
          params.eventRepeatData = 1;
          break;
        case 5: //每个工作日
          params.eventRepeatType = 2;
          params.eventRepeatData = 1;
          params.dayOfWeek = "monday,tuesday,wednesday,thursday,friday";
          break;
      }
      if (params.eventRepeatType) {
        params.rangeType = "noEnd";
      }
      this.customCycleParams = params;
    },
    handleCustomCycle(data, label) {
      const temp = `custom${new Date().getTime()}`;
      // 组装自定义选择的重复数据
      if (this.repeatData.length > 6) {
        this.repeatData.splice(6, 1, {
          label,
          value: temp,
        });
      } else {
        this.repeatData.splice(6, 0, {
          label,
          value: temp,
        });
      }
      this.ruleForm.repeatType = temp;
      this.customCycleParams = data;
    },
    handleChangeCheck(val) {
      this.ruleForm.authObjs = val;
      this.$refs.Form.validateField("authObjs");
    },
    handleOk() {
      this.handleClickTrack({
        infoType: 13,
      }); // 点击埋点
      this.$refs.Form.validate((valid) => {
        if (valid) {
          this.fetchAdd();
        }
      });
    },
    assembleReq() {
      if (!this.ruleForm.repeatType.toString().includes("custom")) {
        this.handleChangeRepeat();
      }
      const authObjs = this.ruleForm.authObjs.map((item) => ({
        authType: "1",
        wid: item.wid,
      }));
      const param = {
        ...this.ruleForm,
        ...this.customCycleParams,
        startTime: formatDate(
          new Date(this.ruleForm.timerange[0]).setSeconds(0),
          this.ruleForm.isAllDay ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss"
        ),
        endTime: formatDate(
          new Date(this.ruleForm.timerange[1]).setSeconds(0),
          this.ruleForm.isAllDay ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss"
        ),
        remindTimes:
          !this.ruleForm.remindTimes ||
          !this.ruleForm.remindTimes.length ||
          this.ruleForm.remindTimes[0] === ""
            ? ""
            : this.ruleForm.remindTimes.join(","),
        remindChannel:
          this.ruleForm.remindChannel &&
          this.ruleForm.remindChannel
            .filter((id) =>
              this.scheduleRemindChannel.find((item) => item.value === id)
            )
            .join(","),
        authObjs: JSON.stringify(authObjs),
      };
      delete param.timerange;
      delete param.repeatType;
      return param;
    },
    fetchAdd() {
      this.okLoading = true;

      window.shell
        .execCardMethod(
          {
            cardId: this.router && this.router.cardId,
            cardWid: this.router && this.router.cardWid,
            method: this.isEdit ? "modify" : "create",
            param: this.assembleReq(),
          },
          (data) => {
            this.$nextTick(function() {
              this.okLoading = false;
            });
            if (data && data.errcode === "0" && data.data?.errcode === "0") {
              this.visible = false;
              this.$emit("update-data");
              !this.isEdit && this.$emit("update-cal");
              this.$message.success(
                this.$Lan(this.cardId, "optSuccess", "操作成功")
              );
            } else {
              this.$message.error(data?.data?.errmsg || data?.errmsg);
            }
          }
        )
        .catch(() => {
          this.okLoading = false;
        });
    },
  },
};
</script>

<style lang="less" scoped>
/deep/.addScheduleModal {
  margin-top: calc(50vh - 290px) !important;
  .we-dialog__title {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    word-break: break-all;
    display: inline-block;
    max-width: 520px;
  }
  .we-dialog__body {
    padding: 0;
  }
  .we-textarea__inner {
    border-color: #d9d9d9 !important;
    height: 110px;
  }
  .we-textarea .we-input__count {
    line-height: 20px;
  }
  // IE展示错乱
  .ieForm .we-input .we-input__count .we-input__count-inner {
    line-height: 30px;
  }
  .we-form-item__label {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    word-break: break-all;
  }
  .we-dialog__footer {
    box-shadow: inset 0px 1px 0px #f0f0f0;
  }
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    width: 100%;
    flex-wrap: nowrap;
    padding-top: 20px;
    .we-button {
      max-width: 475px;
      &:first-child:hover {
        background-color: #ffffff;
      }
    }
  }
  .we-radio {
    display: inline-flex;
    align-items: center;
  }
  .we-radio__label {
    margin-top: -1px;
  }
  .we-radio__inner {
    border: 1px solid #d9d9d9;
  }
  .we-select .we-tag__close.we-icon-close {
    background-color: rgba(0, 0, 0, 0.45);
  }
  .we-tag.we-tag--info .we-tag__close {
    color: #ffffff;
  }
  .we-radio__input.is-disabled .we-radio__inner,
  .we-radio__input.is-disabled.is-checked .we-radio__inner {
    background-color: #f5f7fa !important;
    border-color: #d9d9d9 !important;
  }
}
.customBtn {
  font-size: 14px;
  line-height: 22px;
  padding: 0 12px;
  border-radius: 4px;
  border: 1px solid #d9d9d9;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  max-width: 100%;
  vertical-align: bottom;
  height: 40px;
  /deep/.iconfont {
    font-size: 14px;
    margin-right: 6px;
  }
}
.customSelctOption.we-select-dropdown__item.is-disabled {
  color: #606266;
  cursor: pointer;
}
</style>
<style>
.calendarSelectPop .we-select-dropdown__item {
  width: 472px;
}
/* 隐藏清空按钮 */
.caledarDatePopper
  .we-picker-panel__footer
  .we-picker-panel__link-btn.we-button--text {
  display: none;
}
.caledarDatePopper .we-time-panel__footer {
  display: none;
}
</style>
