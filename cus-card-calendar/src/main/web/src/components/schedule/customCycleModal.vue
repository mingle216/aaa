<template>
  <we-dialog
    :title="$Lan(cardId, 'customCycle', '自定义周期')"
    :append-to-body="true"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :visible.sync="visible"
    custom-class="customCycleModal"
    width="600px"
    @click.native.stop
  >
    <div class="formRow">
      <div class="formRow__label">
        {{ $Lan(cardId, "repeatFrequency", "重复频率") }}
      </div>
      <div class="mr-10">{{ $Lan(cardId, "every", "每") }}</div>
      <we-input-number
        v-model="eventRepeatData"
        controls-position="right"
        :min="1"
        :max="100"
        size="small"
        class="mr-10"
        style="width:90px"
      ></we-input-number>
      <we-select
        v-model="eventRepeatType"
        size="small"
        :clearable="false"
        class="mr-10"
        style="width:100px"
      >
        <we-option
          v-for="item in unitLists"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </we-option>
      </we-select>
      <!-- 月 -->
      <we-select
        v-if="eventRepeatType === 3"
        v-model="selectedMonth"
        size="small"
        :clearable="false"
        style="width:180px"
      >
        <we-option
          v-for="item in monthModeLists"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </we-option>
      </we-select>
    </div>
    <!-- 周 -->
    <div class="formRow" v-if="eventRepeatType === 2">
      <div class="formRow__label"></div>
      <div
        v-for="item in weekModeLists"
        :key="item.value"
        class="weekItem"
        :class="[
          selectedWeek.includes(item.value)
            ? 'selected portal-primary-border-color-lv1 portal-primary-backgroundcolor-lv1'
            : '',
        ]"
        @click="handleChangeWeek(item.value)"
      >
        {{ item.label }}
      </div>
    </div>
    <div class="formRow">
      <div class="formRow__label">
        {{ $Lan(cardId, "startDate", "开始日期") }}
      </div>
      <we-date-picker
        v-model="startDate"
        type="date"
        :clearable="false"
        disabled
        size="small"
        style="width: 180px"
      >
      </we-date-picker>
    </div>
    <div class="formRow">
      <div class="formRow__label">
        {{ $Lan(cardId, "endRepeat", "结束重复") }}
      </div>
      <we-select
        v-model="endRepeat"
        size="small"
        :clearable="false"
        style="width:180px"
        class="mr-10"
      >
        <we-option
          v-for="item in endRepeatLists"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </we-option>
      </we-select>
      <we-date-picker
        v-if="endRepeat === 'endDate'"
        v-model="deadLine"
        type="date"
        :clearable="false"
        size="small"
        :picker-options="endDateOpt"
        style="width: 180px"
      >
      </we-date-picker>
      <template v-else-if="endRepeat === 'numbered'">
        <we-input-number
          v-model="rangeNum"
          controls-position="right"
          :min="1"
          :max="9999"
          size="small"
          class="mr-10"
          style="width:90px"
        ></we-input-number>
        {{ $Lan(cardId, "timesAfter", "次后") }}
      </template>
    </div>
    <div class="formRow">
      <div class="formRow__label">
        {{ $Lan(cardId, "scheduleDuration", "日程时长") }}
      </div>
      <we-input
        v-model="range"
        :disabled="true"
        size="small"
        style="width: 180px"
      >
      </we-input>
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
        @click="handleOk"
        >{{ $Lan(cardId, "okModal", "确 定") }}</we-button
      >
    </div>
  </we-dialog>
</template>

<script>
import { weekArr, weekEnArr, weekIndexArr } from "../../constant.js";
import { getWeekNumOfMonth, formatDate } from "../../utils/date-util";
export default {
  data() {
    const cardId = "SYS_CARD_CALENDAR_DETAIL";
    return {
      cardId,
      visible: false,
      originStartDate: "",
      eventRepeatData: 1,
      eventRepeatType: 1,
      startDate: "",
      endRepeat: "noEnd",
      range: "",
      selectedWeek: [],
      selectedMonth: 0,
      rangeNum: 1,
      deadLine: "",
      unitLists: [
        {
          label: this.$Lan(cardId, "dayMode", "天"),
          value: 1,
          unit: "day",
        },
        {
          label: this.$Lan(cardId, "weekMode", "周"),
          value: 2,
          unit: "week",
        },
        {
          label: this.$Lan(cardId, "monthMode", "月"),
          value: 3,
          unit: "month",
        },
        {
          label: this.$Lan(cardId, "yearMode", "年"),
          value: 4,
          unit: "year",
        },
      ],
      weekModeLists: [
        {
          label: this.$Lan(cardId, "weekShort1", "一"),
          value: "monday",
          weekIndex: 1,
          labelZh: "星期一",
        },
        {
          label: this.$Lan(cardId, "weekShort2", "二"),
          value: "tuesday",
          weekIndex: 2,
          labelZh: "星期二",
        },
        {
          label: this.$Lan(cardId, "weekShort3", "三"),
          value: "wednesday",
          weekIndex: 3,
          labelZh: "星期三",
        },
        {
          label: this.$Lan(cardId, "weekShort4", "四"),
          value: "thursday",
          weekIndex: 4,
          labelZh: "星期四",
        },
        {
          label: this.$Lan(cardId, "weekShort5", "五"),
          value: "friday",
          weekIndex: 5,
          labelZh: "星期五",
        },
        {
          label: this.$Lan(cardId, "weekShort6", "六"),
          value: "saturday",
          weekIndex: 6,
          labelZh: "星期六",
        },
        {
          label: this.$Lan(cardId, "weekShort7", "天"),
          value: "sunday",
          weekIndex: 0,
          labelZh: "星期日",
        },
      ],
      endRepeatLists: [
        {
          label: this.$Lan(cardId, "endOnAday", "终止于某天"),
          value: "endDate",
        },
        {
          label: this.$Lan(cardId, "limitTimes", "限定次数"),
          value: "numbered",
        },
        {
          label: this.$Lan(cardId, "infiniteRepetition", "无限重复"),
          value: "noEnd",
        },
      ],
      endDateOpt: {
        disabledDate: (curr) => {
          const start = new Date(this.originStartDate);
          if (curr && start) {
            return curr.getTime() < new Date(start.toDateString()).getTime();
          }
          return false;
        },
      },
    };
  },
  computed: {
    monthModeLists() {
      const dateObj = new Date(this.originStartDate);
      const date = dateObj.getDate();
      const day = dateObj.getDay();
      const weekOrder = getWeekNumOfMonth(this.originStartDate);
      return [
        {
          label: this.$Lan(this.cardId, "dayOrder", `第${date}天`, {
            order: date,
          }),
          value: 0,
        },
        {
          label: this.$Lan(
            this.cardId,
            "weekOrder",
            `${weekOrder === 5 ? "最后一个" : `第${weekOrder}个`}${
              weekArr[day]
            }`,
            {
              order: this.$Lan(
                this.cardId,
                `weekNo${weekOrder}`,
                `第${weekOrder}个`
              ),
              day: this.$Lan(this.cardId, `week${day}`, weekArr[day]),
            }
          ),
          value: 1,
        },
      ];
    },
  },
  methods: {
    show(timerange, isAllDay, item) {
      this.visible = true;
      this.initData(timerange, isAllDay, item);
    },
    initData(timerange, isAllDay, item) {
      this.range = isAllDay
        ? this.$Lan(this.cardId, "allDay", "全天")
        : `${parseInt(
            (new Date(timerange[1]).getTime() -
              new Date(timerange[0]).getTime()) /
              (1000 * 60)
          )}${this.$Lan(this.cardId, "minute", "分钟")}`; //以分钟为单位
      this.originStartDate = new Date(timerange[0]);
      const day = new Date(timerange[0]).getDay();
      this.startDate = new Date(timerange[0]);
      this.deadLine =
        item && item.deadLine
          ? item.deadLine
          : new Date(timerange[0]).setMonth(
              new Date(timerange[0]).getMonth() + 3
            );
      this.selectedWeek =
        item && item.dayOfWeek
          ? item.dayOfWeek.split(",")
          : [this.weekModeLists[day ? day - 1 : 6].value]; //周日为6
      this.eventRepeatData = item?.eventRepeatData || 1;
      this.selectedMonth = item?.eventRepeatType == 5 ? 1 : 0;
      this.eventRepeatType = item?.eventRepeatType || 1;
      if (this.eventRepeatType == 5) {
        this.eventRepeatType = 3;
      }
      this.endRepeat = item?.rangeType || "noEnd";
      this.rangeNum = Number(item?.rangeNum || 1);
    },
    updateData(timerange, isAllDay, item) {
      this.initData(timerange, isAllDay, item);
      this.handleOk();
    },
    handleChangeWeek(item) {
      const findIndex = this.selectedWeek.findIndex((curr) => curr === item);
      if (findIndex > -1) {
        if (this.selectedWeek.length > 1) {
          this.selectedWeek.splice(findIndex, 1);
        }
      } else {
        this.selectedWeek.push(item);
      }
    },
    handleOk() {
      let label = "";
      const params = {
        eventRepeatType: this.eventRepeatType,
      };
      switch (this.eventRepeatType) {
        case 1: //天
          params.eventRepeatData = this.eventRepeatData;
          label =
            this.eventRepeatData == 1
              ? this.$Lan(this.cardId, "repeatDay", `每天`)
              : this.$Lan(
                  this.cardId,
                  "repeatDaysCustom",
                  `每${this.eventRepeatData}天`,
                  {
                    index: this.eventRepeatData,
                  }
                );
          break;
        case 2: {
          //周
          const weeks = this.weekModeLists
            .filter((item) => this.selectedWeek.includes(item.value))
            .sort((curr, prev) => {
              return curr.weekIndex == 0 || curr.weekIndex - prev.weekIndex;
            });
          const dayOfWeek = [];
          const weekStr = [];
          weeks.forEach((element) => {
            dayOfWeek.push(element.value);
            weekStr.push(
              this.$Lan(
                this.cardId,
                `week${element.weekIndex}`,
                element.labelZh
              )
            );
          });
          params.eventRepeatData = this.eventRepeatData;
          params.dayOfWeek = dayOfWeek.join(",");
          label = this.$Lan(
            this.cardId,
            "repeatWeeksCustom",
            `每${this.eventRepeatData}周的${weekStr.join(",")}`,
            {
              index: this.eventRepeatData,
              weeks: weekStr.join(","),
            }
          );
          break;
        }
        case 3: {
          //月
          const dateObj = new Date(this.originStartDate);
          const date = dateObj.getDate();
          const day = dateObj.getDay();
          const weekOrder = getWeekNumOfMonth(this.originStartDate);
          params.eventRepeatData = this.eventRepeatData;
          if (this.selectedMonth == 0) {
            params.dayOfMonth = date;
            label = this.$Lan(
              this.cardId,
              "repeaMonthDayCustom",
              `每${this.eventRepeatData}个月的第${date}天`,
              {
                index: this.eventRepeatData,
                date,
              }
            );
          } else {
            params.eventRepeatType = 5;
            params.weekIndex = weekIndexArr[weekOrder - 1];
            params.dayOfWeek = weekEnArr[day];
            label = this.$Lan(
              this.cardId,
              "repeaMonthWeekCustom",
              `每${this.eventRepeatData}个月的第${weekOrder}个${weekArr[day]}`,
              {
                index: this.eventRepeatData,
                weekIndex: this.$Lan(
                  this.cardId,
                  `weekNo${weekOrder}`,
                  `第${weekOrder}个`
                ),
                day: this.$Lan(this.cardId, `week${day}`, weekArr[day]),
              }
            );
          }
          break;
        }
        case 4: {
          //年
          params.eventRepeatData = this.eventRepeatData;
          const dateObj1 = new Date(this.originStartDate);
          const month1 = dateObj1.getMonth() + 1;
          const date1 = dateObj1.getDate();
          label = this.$Lan(
            this.cardId,
            `repeatYearDay${month1}`,
            `每${this.eventRepeatData}年${month1}月${date1}日`,
            {
              index: this.eventRepeatData,
              date: date1,
            }
          );
          break;
        }
      }
      params.rangeType = this.endRepeat;
      switch (this.endRepeat) {
        case "endDate": //循环至指定日期结束
          params.deadLine = formatDate(this.deadLine, "yyyy-MM-dd");
          label += this.$Lan(
            this.cardId,
            "endRepeatDate",
            `(到${params.deadLine}结束)`,
            {
              date: params.deadLine,
            }
          );
          break;
        case "numbered": //循环指定次数后结束
          params.rangeNum = this.rangeNum;
          label += this.$Lan(
            this.cardId,
            "endRepeatNum",
            `(重复${params.rangeNum}次)`,
            {
              num: params.rangeNum,
            }
          );
          break;
      }
      this.$emit("update-data", params, label);
      this.visible = false;
    },
  },
};
</script>

<style lang="less" scoped>
.formRow {
  display: flex;
  align-items: center;
  &:not(:first-child) {
    margin-top: 15px;
  }
  /deep/.we-input-number__decrease,
  /deep/.we-input-number__increase {
    width: 20px;
  }
  /deep/.we-input-number.is-controls-right .we-input__inner {
    padding-right: 35px;
  }
}
.formRow__label {
  width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
}
.mr-10 {
  margin-right: 10px;
}
.weekItem {
  width: 30px;
  line-height: 30px;
  height: 30px;
  border-radius: 15px;
  border: 1px solid #f0f0f0;
  text-align: center;
  cursor: pointer;
  margin-right: 10px;
  &.selected {
    color: #ffffff;
  }
}
</style>
