<script>
import fecha from "../../utils/date";
import {
  range as rangeArr,
  getFirstDayOfMonth,
  getPrevMonthLastDays,
  getMonthDays,
  getI18nSettings,
  validateRangeInOneMonth,
} from "../../utils/date-util";

export default {
  props: {
    selectedDay: String, // formated date yyyy-MM-dd
    range: {
      type: Array,
      validator(val) {
        if (!(val && val.length)) return true;
        const [start, end] = val;
        return validateRangeInOneMonth(start, end);
      },
    },
    date: Date,
    hideHeader: Boolean,
    firstDayOfWeek: Number,
  },

  inject: ["elCalendar"],

  methods: {
    toNestedArr(days) {
      return rangeArr(days.length / 7).map((_, index) => {
        const start = index * 7;
        return days.slice(start, start + 7);
      });
    },

    getFormateDate(day, type) {
      if (!day || ["prev", "current", "next"].indexOf(type) === -1) {
        throw new Error("invalid day or type");
      }
      let prefix = this.curMonthDatePrefix;
      if (type === "prev") {
        prefix = this.prevMonthDatePrefix;
      } else if (type === "next") {
        prefix = this.nextMonthDatePrefix;
      }
      day = `00${day}`.slice(-2);
      return `${prefix}-${day}`;
    },

    getCellClass({ text, type }) {
      const classes = [type];
      const date = this.getFormateDate(text, type);
      if (type === "current") {
        if (date === this.selectedDay) {
          classes.push("is-selected");
          if (date === this.formatedToday) {
            classes.push("portal-primary-backgroundcolor-lv1");
          }
        }
        if (date === this.formatedToday) {
          classes.push("is-today portal-primary-color-lv1 portal-primary-backgroundcolor-hover-lv1");
        }
      } else if (type === "prev" || type === "next") {
        if (date === this.selectedDay) {
          classes.push("is-selected portal-primary-backgroundcolor-lv1");
        } else {
          classes.push("portal-font-color-lv4");
        }
      }
      return classes;
    },

    pickDay({ text, type }) {
      const date = this.getFormateDate(text, type);
      this.$emit("pick", date);
    },

    cellRenderProxy({ text, type }) {
      let render = this.elCalendar.$scopedSlots.dateCell;
      if (!render) return <span>{text}</span>;

      const day = this.getFormateDate(text, type);
      const date = new Date(day);
      const data = {
        isSelected: this.selectedDay === day,
        type: `${type}-month`,
        day,
      };
      return render({ date, data, text });
    },
  },

  computed: {
    WEEK_DAYS() {
      return getI18nSettings().dayNames;
    },
    prevMonthDatePrefix() {
      const temp = new Date(this.date.getTime());
      temp.setDate(0);
      return fecha.format(temp, "yyyy-MM");
    },

    curMonthDatePrefix() {
      return fecha.format(this.date, "yyyy-MM");
    },

    nextMonthDatePrefix() {
      const temp = new Date(
        this.date.getFullYear(),
        this.date.getMonth() + 1,
        1
      );
      return fecha.format(temp, "yyyy-MM");
    },

    formatedToday() {
      return this.elCalendar.formatedToday;
    },

    isInRange() {
      return this.range && this.range.length;
    },

    rows() {
      let days = [];
      // if range exists, should render days in range.
      if (this.isInRange) {
        const [start, end] = this.range;
        const currentMonthRange = rangeArr(
          end.getDate() - start.getDate() + 1
        ).map((_, index) => ({
          text: start.getDate() + index,
          type: "current",
        }));
        let remaining = currentMonthRange.length % 7;
        remaining = remaining === 0 ? 0 : 7 - remaining;
        const nextMonthRange = rangeArr(remaining).map((_, index) => ({
          text: index + 1,
          type: "next",
        }));
        days = currentMonthRange.concat(nextMonthRange);
      } else {
        const date = this.date;
        let firstDay = getFirstDayOfMonth(date);
        firstDay = firstDay === 0 ? 7 : firstDay;
        const firstDayOfWeek =
          typeof this.firstDayOfWeek === "number" ? this.firstDayOfWeek : 1;
        const offset = (7 + firstDay - firstDayOfWeek) % 7;
        const prevMonthDays = getPrevMonthLastDays(date, offset).map((day) => ({
          text: day,
          type: "prev",
        }));
        const currentMonthDays = getMonthDays(date).map((day) => ({
          text: day,
          type: "current",
        }));
        days = [...prevMonthDays, ...currentMonthDays];
        const nextMonthDays = rangeArr(42 - days.length).map((_, index) => ({
          text: index + 1,
          type: "next",
        }));
        days = days.concat(nextMonthDays);
      }
      return this.toNestedArr(days);
    },

    weekDays() {
      const start = this.firstDayOfWeek;
      const { WEEK_DAYS } = this;

      if (typeof start !== "number" || start === 0) {
        return WEEK_DAYS.slice();
      } else {
        return WEEK_DAYS.slice(start).concat(WEEK_DAYS.slice(0, start));
      }
    },
  },

  render() {
    const thead = this.hideHeader ? null : (
      <div class="calendar-table__row portal-font-color-lv3">
        {this.weekDays.map((day) => (
          <div class="calendar-table__th" key={day}>
            {day}
          </div>
        ))}
      </div>
    );
    return (
      <div
        class={{
          "calendar-table": true,
          "is-range": this.isInRange,
        }}
      >
        {thead}
        <div class="calendar-table__body portal-font-color-lv1">
          {this.rows.map((row, index) => (
            <div
              class={{
                "calendar-table__row": true,
                "calendar-table__row--hide-border":
                  index === 0 && this.hideHeader,
              }}
              key={index}
            >
              {row.map((cell, key) => (
                <div
                  key={key}
                  class={["calendar-table__td"].concat(this.getCellClass(cell))}
                  onClick={this.pickDay.bind(this, cell)}
                >
                  {this.cellRenderProxy(cell)}
                </div>
              ))}
            </div>
          ))}
        </div>
      </div>
    );
  },
};
</script>

<style lang="less" scoped>
.calendar-table__row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
  .calendar-table__th {
    width: 30px;
    line-height: 22px;
    text-align: center;
  }
  .calendar-table__td {
    border-radius: 15px;
    border: 1px solid transparent;
  }
}
.calendar-table__body {
  .calendar-table__td {
    cursor: pointer;
    &:not(.is-today):hover, &.is-selected:not(.is-today) {
      background: #f0f0f0;
      border: 1px solid #d9d9d9;
    }
    &.is-today.is-selected, &.is-today:hover{
      color: #fff !important;
    }
  }
}
</style>
