<template>
  <div class="base-info">
    <ul class="base-info-content">
      <li
        v-for="(item, index) in data"
        :key="index"
        class="base-info-content-item"
      >
        <div class="base-info-content-item-label">{{ item.fieldName }}</div>
        <div class="base-info-content-item-value">
          {{ formatter(item) || "-" }}
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    data: {
      type: Array,
      default: () => {
        return [];
      },
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  data() {
    return {};
  },
  methods: {
    formatter(row) {
      if (row.fieldWid === "BUSINESS_CYCLE") {
        if (row.fieldValue === "常年") {
          row.fieldValue = this.$Lan(this.lanFunName, "perennial", "常年");
          return row.fieldValue;
        } else {
          return row.fieldValue;
        }
      } else if (row.fieldWid === "DEAL_TIME_LIMIT") {
        if (row.fieldValue === "当场办结") {
          row.fieldValue = this.$Lan(
            this.lanFunName,
            "finishOnTheSpot",
            "当场办结"
          );
          return row.fieldValue;
        } else {
          return row.fieldValue;
        }
      } else {
        return row.fieldValue;
      }
    },
  },
};
</script>

<style lang="less" scoped>
.base-info {
  font-size: 14px;
  &-content {
    margin-top: 12px;
    border: 1px solid #e7edf1;
    border-radius: 4px;

    &-item {
      display: flex;
      align-items: stretch;
      border-top: 1px solid #e7edf1;
      word-break: break-all;

      &:first-child {
        border-top: none;
      }

      &-label {
        display: flex;
        flex-shrink: 0;
        align-items: center;
        width: 112px;
        border-right: 1px solid #e7edf1;
        background-color: #f6f6f8;
        padding: 12px;
        border-radius: 4px 0 0 4px;
      }

      &-value {
        flex-grow: 1;
        padding: 12px;
        white-space: pre-wrap;
      }
    }
  }
}
</style>
