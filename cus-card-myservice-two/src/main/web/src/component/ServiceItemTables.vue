<i18n src="../languages/locale.json"></i18n>
<template>
  <div class="service_item_table">
    <we-table
      tooltip-effect="dark"
      style="width: 100%"
      :data="tableData"
      v-loading="tableLoading"
    >
      <we-table-column
        prop="itemName"
        :label="getLanguageValue('serviceItemName', '服务事项名称')"
        min-width="110%"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <span
            :class="[
              'item_name',
              scope.row.workGuide && scope.row.isAuthorized !== 0
                ? 'pointer portal-primary-color-hover-lv1'
                : '',
            ]"
            @click="openServiceDetail(scope.row)"
          >
            <i class="iconfont icon-favorites" v-if="currentUser" :class="scope.row.favorite?'isFav':''" @click.stop="handleCollect(scope.row)"></i>
            {{ scope.row.itemName }}
          </span>
        </template>
      </we-table-column>
      <we-table-column
        prop="itemCategory"
        :label="getLanguageValue('serviceItemCategory', '服务主题')"
        :show-overflow-tooltip="true"
        v-if="showItem.includes('1')"
      >
      </we-table-column>
      <we-table-column
        v-if="showItem.includes('3')"
        prop="itemDept"
        :label="getLanguageValue('serviceItemDept', '责任部门')"
        :show-overflow-tooltip="true"
      >
      </we-table-column>
      <we-table-column
        prop="roleName"
        :label="getLanguageValue('roleName', '服务对象')"
        :show-overflow-tooltip="true"
        v-if="showItem.includes('2')"
      >
      </we-table-column>
      <we-table-column
        prop="visitCount"
        :label="getLanguageValue('visitCount', '访问量')"
        width="100"
        v-if="showItem.includes('4')"
      >
        <template slot-scope="scope">
          <span> {{ scope.row.visitCount | toThousand }} </span>
        </template>
      </we-table-column>
      <we-table-column
        prop="action"
        :label="getLanguageValue('operate', '操作')"
        width="184"
      >
        <template slot-scope="scope">
          <div style="display:flex;align-items:center">
            <span
              class="pointer portal-primary-color-lv1"
              @click="openServiceDetail(scope.row)"
              v-if="scope.row.workGuide && scope.row.isAuthorized !== 0"
            >
              {{ getLanguageValue("businessGuide", "办事指南") }}
            </span>
            <we-tooltip
              :content="
                $Lan(
                  'public',
                  !scope.row.workGuide ? 'workGuideHide' : 'workGuideNoAuth',
                  !scope.row.workGuide
                    ? '此事项暂未配置办事指南'
                    : '您无权限查看'
                )
              "
              :open-delay="500"
              placement="top"
              v-else
              ><span class="pointer portal-font-color-lv4">
                {{ getLanguageValue("businessGuide", "办事指南") }}
              </span></we-tooltip
            >
            <span class="divide"></span>
            <span
              :class="['pointer portal-primary-color-lv1']"
              @click="handleOnline(scope.row)"
              v-if="scope.row.onlineServiceType === 2"
            >
              {{ getLanguageValue("online", "在线办理") }}
            </span>
            <we-tooltip
              :content="
                $Lan(
                  'public',
                  scope.row.onlineServiceType === 0
                    ? 'onlineHide'
                    : 'onlineNoAuth',
                  scope.row.onlineServiceType === 0
                    ? '此事项暂未开通在线办理功能'
                    : '您无权限在线办理此事项'
                )
              "
              :open-delay="500"
              placement="top"
              v-else
            >
              <span class="pointer portal-font-color-lv4">
                {{ getLanguageValue("online", "在线办理") }}
              </span></we-tooltip
            >
          </div>
        </template>
      </we-table-column>
      <template #empty>
        <!-- <EmptyCon :tip="$t('nodata')" :height="300"></EmptyCon> -->
        <EmptyCon
          :tip="getLanguageValue('nodata', '暂无数据')"
          :height="300"
          style="flex: 1"
        />
      </template>
    </we-table>
    <!-- 分页 -->
    <div class="page_container" v-if="tableData && tableData.length">
      <span class="page_total">
        {{
          getLanguageValue(
            "pageTotal",
            `共${toThousand(page.total)}条 显示第${(page.current - 1) *
              page.size +
              1} ~ ${sizeShow}条`,
            {
              total: toThousand(page.total),
              start: (page.current - 1) * page.size + 1,
              end: sizeShow,
            }
          )
        }}
        <!-- 共{{ page.total | toThousand }}条 显示第
        {{ (page.current - 1) * page.size + 1 }} ~ {{ sizeShow }} 条 -->
      </span>
      <we-pagination
        background
        @size-change="handlePageSizeChange"
        @current-change="handlePageChange"
        :current-page="page.current"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="page.size"
        layout="sizes, prev, pager, next"
        :total="page.total"
      >
      </we-pagination>
    </div>
    <we-dialog
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :append-to-body="true"
            :title="detailObj.title"
            :visible.sync="dialogVisible"
            width="420px"
    >
      <div>
        <div class="detail-line">
          <p>服务内容：</p>
          <div>{{detailObj.content || '--'}}</div>
        </div>
        <div class="detail-line">
          <p>责任单位：</p>
          <div>{{detailObj.zrdw || '--'}}</div>
        </div>
        <div class="detail-line">
          <p>责任人：</p>
          <div>{{detailObj.lxr || '--'}}</div>
        </div>
        <div class="detail-line">
          <p>联系电话：</p>
          <div>{{detailObj.phone || '--'}}</div>
        </div>
        <div class="detail-bot"><p class="portal-primary-backgroundcolor-lv1" @click="goOnline">进入服务</p></div>
      </div>
    </we-dialog>
  </div>
</template>

<script>
import getLanguageValue from "../mixins";
export default {
  mixins: [getLanguageValue],
  props: [
    "currentUser",
    "tableData",
    "page",
    "showItem",
    "tableLoading",
    "router",
  ],
  components: {
  },
  data() {
    return {
      detailObj: {},
      dialogVisible: false
      // tableLoading: false
    };
  },
  computed: {
    sizeShow() {
      return this.page.total <= this.page.current * this.page.size
        ? this.page.total
        : this.page.current * this.page.size;
    },
    nameWidth() {
      console.log(this.showItem);
      switch (this.showItem.length) {
        case 0:
          return "40%";
        case 1:
          return "120%";
        case 2:
          return "120%";
        case 3:
          return "110%";
        case 4:
          return "120%";
        default:
          return "22.2%";
      }
    },
    optionWidth() {
      switch (this.showItem.length) {
        case 0:
          return "40%";
        case 1:
          return "27%";
        case 2:
          return "15.6%";
        case 3:
          return "12.8%";
        case 4:
          return "12.8%";
        default:
          return "12.8%";
      }
    },
  },
  methods: {
    toThousand(num) {
      if (typeof num !== "number") {
        num = Number(num);
      }
      if (isNaN(num)) {
        return 0;
      }
      return num.toLocaleString("en");
    },
    handlePageChange(page) {
      this.page.current = page;
      this.$emit("current-change", page);
    },
    handlePageSizeChange(size) {
      this.page.size = size;
      this.$emit("size-change", size);
    },
    handleCollect(item) {
      this.$emit("collect-item", item);
    },
    handleOnline(item) {
      this.tempObj = item
      window.shell.execCardMethod(
              {
                cardId: "SYS_CARD_DETAILSOFSERVICEITEMS",
                cardWid: "31762098404221595",
                method: "renderData",
                param: {
                  wid: item.itemWid,
                },
              },
              (data) => {
                let obj = data.data
                this.detailObj = {
                  title: obj.itemName,
                }
                obj.serviceItemInfo.baseInfos.forEach(item => {
                  if(item.fieldWid == 'SERVICE_CONTENT') {
                    this.detailObj.content = item.fieldValue
                  } else if(item.fieldWid == '1085200981112393728') {
                    this.detailObj.lxr = item.fieldValue
                  } else if(item.fieldWid == 'DUTY_DEPT_ID') {
                    this.detailObj.zrdw = item.fieldValue
                  }
                })
                obj.serviceItemInfo.indptModuls.forEach(item => {
                  if(item.fieldWid == 'CONTACT_PHONE' && item.fieldValue && item.fieldValue.length) {
                    item.fieldValue.forEach((v,i) => {
                      if(i==0) {
                        this.detailObj.phone = v.phone
                      } else {
                        this.detailObj.phone = this.detailObj.phone + ',' + v.phone
                      }
                    })
                  }
                })
                this.dialogVisible = true
              }
      );


    },
    goOnline() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: {
            infoType: 1,
            itemId: this.tempObj.itemWid,
          },
        },
        startTime: new Date().getTime(),
      });
      this.dialogVisible = false
      window.shell.openOnlineDeal(
        {
          ...this.tempObj,
          wid: this.tempObj.itemWid,
          name: this.tempObj.itemName,
        },
        null
      );
    },
    openServiceDetail(item) {
      if (!item.workGuide || item.isAuthorized === 0) {
        return;
      }
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: {
            infoType: 1,
            itemId: item.itemWid,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.openServiceItem({
        ...item,
        wid: item.itemWid,
        name: item.itemName,
      });
    },
  },
};
</script>

<style lang="less" scoped>
.service_item_table {
  width: 100%;

}
/deep/.we-dialog__title{
  max-width: 90%;
  overflow: hidden;
  display: block;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.pointer {
  cursor: pointer;
}
.divide {
  display: inline-table;
  width: 1px;
  background: #d9d9d9;
  font-weight: normal;
  height: 12px;
  vertical-align: middle;
  margin: 0 4px;
}

.item_name {
  width: 100%;
  height: 100%;
  i{
    font-size: 16px;
    color: #BFBFBF;
    &.isFav{
      color: #ffbc00;
    }
  }
}
.visible {
  visibility: visible;
}
.hidden {
  visibility: hidden;
}
.star {
  width: 12px;
  height: 12px;
  margin-right: 6px;
  cursor: pointer;
}
.favorite {
  color: #ffbc00;
}
.un-favorite {
  color: #bfbfbf;
}
.detail-bot{
  height: 60px;
  border-top: 1px solid #F0F0F0;
  display: flex;
  align-items: center;
  justify-content: center;
  p{
    width: 147px;
    height: 36px;
    text-align: center;
    line-height: 36px;
    background: #2468F2;
    color: #fff;
    border-radius: 18px;
    cursor: pointer;
  }
}
.detail-line{
  display: flex;
  margin-bottom: 14px;
  p{
    width: 74px;
    text-align: right;
    font-size: 14px;
    line-height: 22px;
    color: #8C8C8C;
  }
  div{
    font-size: 14px;
    line-height: 22px;
    color: #262626;
    width: calc(100% - 74px);
  }
}
.page_container {
  margin-top: 15px;
  text-align: right;
  position: relative;
  /deep/.we-pagination__sizes{
    float: left;
  }
  .page_total {
    float: left;
    font-size: 12px;
    color: #8c8c8c;
    margin-top: 10px;
  }
}
/deep/.we-table__header {
  thead {
    font-size: 14px;
    // color: #262626;
    font-weight: bold;
    tr,
    th {
      background: #fafafa;
      height: 44px;
    }
    th,
    td {
      padding: 10px 0;
      height: 44px;
    }
  }
}
/deep/.we-table__body {
  td {
    padding: 0;
    height: 44px;
  }
}
/deep/.we-table {
  &::before {
    display: none;
  }
  tr:hover > td {
    background-color: #fafafa;
  }
  td,
  th.is-leaf {
    border-color: #f0f0f0;
  }
  // 解决IE show-overflow-tooltip临界值失效问题
  th div {
    padding-left: 24px;
  }
  .cell {
    padding-left: 24px;
    padding-right: 0;
    // 解决safari浏览器出现双title
    &::after {
      content: "";
      display: block;
    }
  }
  .we-table__empty-text {
    line-height: 22px;
  }
}
</style>
