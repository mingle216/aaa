import { weekArr, weekIndexObj, weekIndexArr } from "../constant.js";
import { formatDate } from "../utils/date-util";
export default {
  computed: {
    repeatData() {
      const startTime = new Date(
        this.ruleForm?.timerange[0] || this.itemInfo?.startTime
      );
      const month = startTime.getMonth() + 1;
      const day = startTime.getDay();
      const date = startTime.getDate();
      return [
        {
          value: 0,
          label: this.$Lan("SYS_CARD_CALENDAR_DETAIL", "repeatNever", "不重复"),
        },
        {
          value: 1,
          label: this.$Lan("SYS_CARD_CALENDAR_DETAIL", "repeatDay", "每天"),
        },
        {
          value: 2,
          label: `${this.$Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            "repeatWeek",
            "每周"
          )}${this.$Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            `week${day}`,
            weekArr[day]
          )}`,
        },
        {
          value: 3,
          label: this.$Lan("SYS_CARD_CALENDAR_DETAIL", "repeatMonth", "每月"),
        },
        {
          value: 4,
          label: this.$Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            `repeatYearDay${month}`,
            `每年${month}月${date}日`,
            {
              index: 1,
              month: month,
              date: date,
            }
          ),
        },
        {
          value: 5,
          label: this.$Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            "repeatEveryWeekday",
            "每个工作日 (星期一至星期五)"
          ),
        },
      ];
    },
    remindData() {
      // 值是距离开始日期的时间，单位为分钟
      if (this.ruleForm?.isAllDay == 1 || this.itemInfo?.isAllDay == 1) {
        return [
          {
            value: "",
            label: this.$Lan(
              "SYS_CARD_CALENDAR_DETAIL",
              "remindNever",
              "无提醒"
            ),
          },
          {
            value: -540,
            label: `${this.$Lan(
              "SYS_CARD_CALENDAR_DETAIL",
              "remindTheDay",
              "当天"
            )} (9:00)`,
          },
          {
            value: 900,
            label: `${this.$Lan(
              "SYS_CARD_CALENDAR_DETAIL",
              "remindOneDay",
              "1天前"
            )} (9:00)`,
          },
          {
            value: 2340,
            label: `${this.$Lan(
              "SYS_CARD_CALENDAR_DETAIL",
              "remindTwoDay",
              "2天前"
            )} (9:00)`,
          },
          {
            value: 9540,
            label: `${this.$Lan(
              "SYS_CARD_CALENDAR_DETAIL",
              "remindOneWeek",
              "1周前"
            )} (9:00)`,
          },
        ];
      }
      return [
        {
          value: "",
          label: this.$Lan("SYS_CARD_CALENDAR_DETAIL", "remindNever", "无提醒"),
        },
        {
          value: 0,
          label: this.$Lan("SYS_CARD_CALENDAR_DETAIL", "remindStart", "开始时"),
        },
        {
          value: 5,
          label: this.$Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            "remindfiveMin",
            "5分钟前"
          ),
        },
        {
          value: 15,
          label: this.$Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            "remindFifteenMin",
            "15分钟前"
          ),
        },
        {
          value: 30,
          label: this.$Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            "remindThirtyMin",
            "30分钟前"
          ),
        },
        {
          value: 60,
          label: this.$Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            "remindOneHour",
            "1小时前"
          ),
        },
        {
          value: 1440,
          label: this.$Lan("SYS_CARD_CALENDAR_DETAIL", "remindOneDay", "1天前"),
        },
      ];
    },
  },
  methods: {
    formatRepeatType(item) {
      if (item.repeatType == 0) {
        return this.$Lan("SYS_CARD_CALENDAR_DETAIL", "repeatNever", "不重复");
      }
      let params = {
        eventRepeatType: item.repeatType,
        eventRepeatData: item.eventRepeatData,
      };
      let label = "";
      switch (item.repeatType) {
        case 1: {
          // 天
          label =
            item.eventRepeatData == 1
              ? this.$Lan(this.cardId, "repeatDay", `每天`)
              : this.$Lan(
                  this.cardId,
                  "repeatDaysCustom",
                  `每${item.eventRepeatData}天`,
                  {
                    index: item.eventRepeatData,
                  }
                );
          break;
        }
        case 2: {
          // 周
          const weeks = item.dayOfWeek
            .split(",")
            .map((key) => {
              const obj = weekIndexObj[key];
              return this.$Lan(
                this.cardId,
                `week${obj.weekIndex}`,
                obj.labelZh
              );
            })
            .join(",");
          label = this.$Lan(
            this.cardId,
            "repeatWeeksCustom",
            `每${item.eventRepeatData}周的${weeks}`,
            {
              index: item.eventRepeatData,
              weeks,
            }
          );
          params.dayOfWeek = item.dayOfWeek;
          break;
        }
        case 3: {
          // 月
          label = this.$Lan(
            this.cardId,
            "repeaMonthDayCustom",
            `每${item.eventRepeatData}个月的第${item.dayOfMonth}天`,
            {
              index: item.eventRepeatData,
              date: item.dayOfMonth,
            }
          );
          params.dayOfMonth = item.dayOfMonth;
          break;
        }
        case 4: {
          // 年
          const dateObj = new Date(item.startTime);
          const month = dateObj.getMonth() + 1;
          const date = dateObj.getDate();
          label = this.$Lan(
            this.cardId,
            `repeatYearDay${month}`,
            `每${item.eventRepeatData}年${month}月${date}日`,
            {
              index: item.eventRepeatData,
              date: date,
            }
          );
          break;
        }
        case 5: {
          // 月 -- 周
          const weekOrder = weekIndexArr.findIndex(curr => curr === item.weekIndex) + 1;
          const weekObj = weekIndexObj[item.dayOfWeek];
          const weekStr = this.$Lan(
            this.cardId,
            `week${weekObj.weekIndex}`,
            weekObj.labelZh
          );
          label = this.$Lan(
            this.cardId,
            "repeaMonthWeekCustom",
            `每${item.eventRepeatData}个月的第${weekOrder}个${weekStr}`,
            {
              index: item.eventRepeatData,
              weekIndex: this.$Lan(
                this.cardId,
                `weekNo${weekOrder}`,
                `第${weekOrder}个`
              ),
              day: weekStr,
            }
          );
          params.weekIndex = item.weekIndex;
          params.dayOfWeek = item.dayOfWeek;
          break;
        }
      }
      if (item.repeatType) {
        params.rangeType = item.rangeType;
        switch (params.rangeType) {
          case "endDate": //循环至指定日期结束
            params.deadLine = formatDate(item.deadline, "yyyy-MM-dd");
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
            params.rangeNum = item.rangeNum;
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
      }

      if (this.isDetail) {
        return label;
      } else {
        this.handleCustomCycle(params, label);
      }
    },
  },
};
