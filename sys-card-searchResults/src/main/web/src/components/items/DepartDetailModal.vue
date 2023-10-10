<template>
  <we-dialog
    :title="title"
    :append-to-body="true"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :visible.sync="visible"
    custom-class="departModal"
    width="982px"
    class="departModalWrap"
  >
    <ContainerScroll :barKeepShow="true">
      <div class="departModal__body portal-font-color-lv1" v-loading="loading">
        <div class="departModal__top">
          <div class="departModal__row">
            <div class="departModal__label">
              {{ $Lan(lanFunName, "departName", "部门名称") }}
            </div>
            <div class="departModal__value">{{ editItem.deptName }}</div>
          </div>
          <div class="departModal__row" v-if="editItem.parentDeptName">
            <div class="departModal__label">
              {{ $Lan(lanFunName, "parentDepart", "父部门名称") }}
            </div>
            <div class="departModal__value">{{ editItem.parentDeptName }}</div>
          </div>
          <div class="departModal__row">
            <div class="departModal__label">
              {{ $Lan(lanFunName, "departPhone", "部门电话") }}
            </div>
            <div class="departModal__value">
              {{ fomatPhone(editItem.deptPhone) }}
            </div>
          </div>
          <div class="departModal__row">
            <div class="departModal__label">
              {{ $Lan(lanFunName, "departLocation", "办公地点") }}
            </div>
            <div class="departModal__value">
              {{ editItem.deptAddress || "-" }}
            </div>
          </div>
          <div class="departModal__row">
            <div class="departModal__label">
              {{ $Lan(lanFunName, "departDesc", "部门简介") }}
            </div>
            <div class="departModal__value">
              {{ editItem.deptDesc || "-" }}
            </div>
          </div>
        </div>
        <we-table
          v-if="tableData.length"
          :data="tableData"
          border
          header-cell-class-name="portal-font-color-lv1"
          header-cell-style="background-color:#FAFAFA;font-weight:normal"
          class="departModal__table"
          style="width: 100%"
        >
          <we-table-column
            prop="deptName"
            :label="$Lan(lanFunName, 'childDepart', '子部门名称')"
            show-overflow-tooltip
          >
          </we-table-column>
          <we-table-column
            prop="name"
            :label="$Lan(lanFunName, 'departPhone', '部门电话')"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              {{ fomatPhone(scope.row.deptPhone) }}
            </template>
          </we-table-column>
          <we-table-column
            prop="address"
            :label="$Lan(lanFunName, 'departLocation', '办公地点')"
            show-overflow-tooltip
            ><template slot-scope="scope">
              {{ scope.row.deptAddress || "-" }}
            </template></we-table-column
          >
          <we-table-column
            prop="address"
            :label="$Lan(lanFunName, 'departDesc', '部门简介')"
            show-overflow-tooltip
            ><template slot-scope="scope">
              {{ scope.row.deptDesc || "-" }}
            </template></we-table-column
          >
        </we-table>
        <div class="departModal__page" v-if="total">
          <div class="portal-font-color-lv3">
            {{
              $Lan(
                lanFunName,
                "pageTotal",
                "共 {total} 条，显示第 {start} ~ {end} 条",
                {
                  total: total,
                  start: (current - 1) * pageSize + 1,
                  end: current * pageSize > total ? total : current * pageSize,
                }
              )
            }}
          </div>
          <we-pagination
            background
            layout="prev, pager, next"
            :current-page="current"
            :page-size="pageSize"
            :total="total"
            @current-change="handlePageChange"
          >
          </we-pagination>
        </div>
      </div>
    </ContainerScroll>
  </we-dialog>
</template>

<script>
export default {
  props: ["lanFunName", "router"],
  data() {
    return {
      visible: false,
      total: 0,
      current: 1,
      pageSize: 5,
      loading: false,
      editItem: {},
    };
  },
  computed: {
    title() {
      return this.$Lan(this.lanFunName, "departDetails", "部门详情");
    },
    tableData() {
      const children = this.editItem.childDept || [];
      let start = parseInt((this.current - 1) * this.pageSize);
      return children.slice(
        start,
        this.current * this.pageSize > this.total
          ? this.total
          : start + this.pageSize
      );
    },
  },
  methods: {
    show(item) {
      this.visible = true;
      this.current = 1;
      this.total = 0;
      this.loading = false;
      this.editItem = {};
      window.shell
        .execCardMethod({
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getDeptInfoData",
          param: {
            deptWid: item.deptWid,
          },
        })
        .then((resp) => {
          this.loading = false;
          this.editItem = resp.data?.data || {};
          this.total = (this.editItem.childDept || []).length;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    handlePageChange(page) {
      this.current = page;
    },
    fomatPhone(deptPhone) {
      if (!deptPhone || deptPhone === "[]") {
        return "-";
      }
      const phones = JSON.parse(deptPhone).map((item) =>
        item.comments ? `${item.comments} ${item.phone}` : item.phone
      );
      return phones.join(" ;  ");
    },
  },
};
</script>

<style lang="less" scoped>
/deep/.departModal {
  margin: 0 !important;
  .we-dialog__body {
    padding: 0;
  }
}
.departModal__body {
  padding: 20px;
  max-height: 514px;
}
.departModal__top {
  border: 1px solid #f0f0f0;
}
.departModal__row {
  line-height: 22px;
  display: flex;
  &:not(:last-child) {
    border-bottom: 1px solid #f0f0f0;
  }
}
.departModal__label {
  padding: 13px 16px;
  background: #fafafa;
  border-right: 1px solid #f0f0f0;
  width: 120px;
  flex-shrink: 0;
}
.departModal__value {
  padding: 13px 16px;
}
.departModal__table {
  margin-top: 20px;
  /deep/.we-table {
    td,
    th {
      padding: 13px 0;
    }
    th > .cell,
    .cell {
      padding-left: 16px;
      padding-right: 16px;
    }
    td:first-child .cell,
    th:first-child .cell {
      padding-left: 16px;
    }
  }
}
.departModal__page {
  margin-top: 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 20px;
}
</style>

<style lang="less">
.departModalWrap {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
