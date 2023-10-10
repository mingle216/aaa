<template>
  <div class="portal-font-color-lv1">
    <we-table
      size="medium"
      style="width: 100%"
      v-loading="tableLoading"
      :data="tableData"
      :highlight-current-row="false"
      :border="false"
      :header-cell-style="{ background: '#f6f8fa' }"
    >
      <we-table-column prop="itemName" label="服务名称">
        <template slot-scope="scope">
          <we-link :title="scope.row.itemName" class="text_ellipsis portal-font-color-lv1"  @click="openServiceDetail(scope.row)">
            {{ scope.row.itemName }}
          </we-link>
        </template>
      </we-table-column>
      <we-table-column prop="itemDept" label="归属部门" :width="240">
        <template slot-scope="scope">
          <span class="dept_name">{{ scope.row.itemDept }}</span>
        </template>
      </we-table-column>
      <we-table-column prop="favorite" label="是否收藏" align="center" :width="100">
        <template slot-scope="scope">
          <i
            class="iconSpan collect"
            v-if="scope.row.favorite"
            @click="handleCollect(scope.row)"
          />
          <i
            class="iconSpan noCollect"
            v-else
            @click="handleCollect(scope.row)"
          />
        </template>
      </we-table-column>
      <we-table-column label="操作" align="center" :width="200">
        <template slot-scope="scope">
          <button
            size="small"
            :class="{
              opt_btn: true,
              'portal-primary-border-color-lv1 portal-primary-color-lv1': scope.row.workGuide,
              disable_work_guide: !scope.row.workGuide
            }"
            @click="openServiceDetail(scope.row)"
          >
            办事指南
          </button>
          <button
            size="small"
            class="online_work opt_btn portal-primary-backgroundcolor-lv1"
            type="primary"
            :disabled="scope.row.onlineServiceType == 0"
            @click="handleOnline($event, scope.row)"
						:class="{disable_click: scope.row.onlineServiceType == 0}"
          >
            在线办理
          </button>
        </template>
      </we-table-column>
    </we-table>
    <div class="page_container" v-if="tableData.length">
      <span class="page_total">
        {{
          `共${toThousand(page.total)}条 显示第${(page.current - 1) *
            page.size +
            1} ~ ${sizeShow}条`
        }}
      </span>
      <we-pagination
        background
        @size-change="handlePageSizeChange"
        @current-change="handlePageChange"
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="page.size"
        layout="prev, pager, next, jumper"
        :total="page.total"
      >
      </we-pagination>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    tableData: Array,
    page: Object,
    tableLoading: Boolean
  },
  computed: {
    sizeShow() {
      return this.page.total <= this.page.current * this.page.size
        ? this.page.total
        : this.page.current * this.page.size
    }
  },
  methods: {
    fetchTableData() {
      this.$emit('fetch-table-data')
    },
    toThousand(num) {
      if (typeof num !== 'number') {
        num = Number(num)
      }
      if (isNaN(num)) {
        return 0
      }
      return num.toLocaleString('en')
    },
    handlePageSizeChange(val) {
      this.$emit('update:page', {
        current: 1,
        size: val,
        total: this.page.total
      })
      this.fetchTableData()
    },
    handlePageChange(val) {
      this.$emit('update:page', {
        current: val,
        size: this.page.size,
        total: this.page.total
      })
      this.fetchTableData()
    },
    handleCollect(item) {
      this.$emit('collect-item', item)
    },
    handleOnline(e, item) {
      let parent = e.target;
      let name = parent.nodeName;
      while (name !== "DIV") {
        parent = parent.parentNode;

        if (parent) {
          name = parent.nodeName;
        }
      }
      window.shell.openOnlineDeal({
        ...item,
        wid: item.itemWid,
        name: item.itemName,
      }, parent);
    },
    openServiceDetail(item) {
      if (item.workGuide) {
        window.shell.openServiceItem({
          ...item,
          wid: item.itemWid,
          name: item.itemName,
        });
      }
    },
  },
  mounted() {
    // this.fetchTableData()
  }
}
</script>

<style lang="less" scoped>
.text_ellipsis {
  max-width: 80%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: inline-block;
}
.noCollect {
  background-position: -4px -303px;
}
.collect {
  background-position: -38px -303px;
}
.iconSpan {
  display: inline-block;
  width: 16px;
  height: 23px;
  background-image: url('../assets/icons.png');
  background-repeat: no-repeat;
  cursor: pointer;
}
.opt_btn {
  display: inline-block;
  width: 75px;
  height: 25px;
  font-size: 12px;
  color: #4a98f6;
  line-height: 25px;
  border-radius: 13px;
  text-align: center;
  margin-right: 10px;
  cursor: pointer;
  border: 1px solid transparent;
	background: none;
}
.disable_work_guide {
  border-color: #ececec;
  color: #cbcbcb;
  cursor: text;
}
.online_work {
  color: #fff !important;

	&.disable_click{
		color: #C1CBD4!important;
		background: none !important;
	}
}

.page_container {
  margin-top: 15px;
  text-align: right;
  position: relative;
  .page_total {
    float: left;
    line-height: 32px;
    font-size: 12px;
    color: #8c8c8c;
  }
}
/deep/ .we-table {
  color: #212A39;
  // &::before {
  //   height: 0;
  // }
  // td,
  // th {
  //   border-bottom: none;
  // }
  .we-table__row:hover > td {
    background-color: #ffffff !important;
  }

  // .we-table_row > td {
  //   border: none;
  // }
}
</style>
