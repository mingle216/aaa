<template>
  <!--基本信息-->
  <div class="basicInformation" style="position: relative">
    <div class="title1 portal-font-color-lv1">
      {{ $Lan(lanFunName, "basicInformation", "基本信息") }}
    </div>

    <we-table
      class="table portal-font-color-lv1"
      :data="tableData"
      border
      :cell-style="cellStyle"
      :show-header="false"
    >
      <we-table-column
        prop="fieldName"
        label="name"
        width="120"
      ></we-table-column>
      <we-table-column
        prop="fieldValue"
        label="value"
        :formatter="formatter"
        :width="itemdetail_width - 295"
      ></we-table-column>
    </we-table>
  </div>
</template>

<script>
export default {
  props: {
    itemdetail_width: {
      type: [Number, String],
      default: 0,
    },
    serviceItemInfo: {
      type: Object,
      default: () => {
        return null;
      },
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },

  computed: {
    tableData() {
      if (this.serviceItemInfo) {
        return this.serviceItemInfo.baseInfos;
      }
      return [];
    },
  },
  data() {
    return {};
  },
  methods: {
    cellStyle({ columnIndex }) {
      // 给表格第一列设置颜色
      if (columnIndex === 0) {
        return {
          background: "#f6f6f8",
        };
      }
      return {};
    },
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
      } else if (row.fieldWid === 'DEAL_TYPE'){
				switch (row.fieldValue) {
          case "即办件":
						row.fieldValue = this.$Lan(this.lanFunName, "immediatelyItem", "即办件");
						break;
					case "承诺件":
						row.fieldValue = this.$Lan(this.lanFunName, "promiseItem", "承诺件");
						break;
					case "联办件":
						row.fieldValue = this.$Lan(this.lanFunName, "unionItem", "联办件");
						break;
					default:
						row.fieldValue = this.$Lan(this.lanFunName, "noCategory", "不分类");
				}
				return row.fieldValue;
			}  else if (row.fieldWid === 'LIST_TYPE'){
				switch (row.fieldValue) {
					case "服务清单":
						row.fieldValue = this.$Lan(this.lanFunName, "serviceList", "服务清单");
						break;
					case "审批清单":
						row.fieldValue = this.$Lan(this.lanFunName, "approvalList", "审批清单");
						break;
					default:
						row.fieldValue = this.$Lan(this.lanFunName, "noList", "不在三张清单中显示");
				}
				return row.fieldValue;
			}  else if (row.fieldWid === 'SCENE_TIMES'){
				switch (row.fieldValue) {
					case "1次":
						row.fieldValue = this.$Lan(this.lanFunName, "once", "1次");
						break;
					case "2次":
						row.fieldValue = this.$Lan(this.lanFunName, "twice", "2次");
						break;
					case "3次":
						row.fieldValue = this.$Lan(this.lanFunName, "thrice", "3次");
						break;
					case "3次以上":
						row.fieldValue = this.$Lan(this.lanFunName, "overThree", "3次以上");
						break;
					default:
						row.fieldValue = this.$Lan(this.lanFunName, "noSite", "无需到现场");
				}
				return row.fieldValue;
			} else {
        return row.fieldValue;
      }
    },
  },
};
</script>

<style lang="less" scoped>
/deep/.we-table__body {
  width: 100% !important;
}
</style>
