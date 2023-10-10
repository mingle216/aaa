<template>
  <div class="card-mytask" v-loading="loading">
    <div
      class="card-mytask-list"
      v-for="(item, index) in taskList"
      :key="`done_${index}`"
      @click="openDetail(item)"
    >
      <div class="card-mytask-title">
        <span
          class="title text__ellipsis"
          :class="[
            readWids.includes(item.wid)
              ? 'portal-font-color-lv2'
              : 'portal-font-color-lv1',
          ]"
          :style="{
            'max-width': configer.includes('point')
              ? `calc(100% - ${!item.tagWidth ? 0 : item.tagWidth}px)`
              : '100%',
          }"
          >{{ item.subject }}</span
        >
        <template :ref="`tag_${index}`">
          <span
            class="tag"
            v-if="configer.includes('point')"
            :class="{
              'tag-complete': item.processStatus === 'COMPLETE',
              'tag-withdraw': item.processStatus === 'ABORT',
              'tag-audit': item.processStatus === 'RUNNING',
            }"
          >
            <template v-if="item.processStatus === 'COMPLETE'">
              {{ $Lan(lanFunName, "COMPLETE", "已完成") }}
            </template>
            <template v-if="item.processStatus === 'RUNNING'">
              {{ $Lan(lanFunName, "RUNNING", "待处理") }}
            </template>
            <template v-if="item.processStatus === 'ABORT'">
              {{ $Lan(lanFunName, "ABORT", "已撤销") }}
            </template>
          </span>
        </template>
      </div>
      <div class="card-mytask-info portal-font-color-lv2">
        <div class="data-item" v-if="configer.includes('source')">
          <div class="data-left">
            {{ $Lan(lanFunName, "bizDomain", "所属应用") }}：
          </div>
          <div class="data-right ellipsis">{{ item.bizDomain || "-" }}</div>
        </div>
        <div class="data-item" v-if="configer.includes('handler')">
          <div class="data-left">
            {{ $Lan(lanFunName, "handler", "待处理人") }}：
          </div>
          <div class="data-right ellipsis">
            <span class="handler_name text__ellipsis"
              >{{ splitAssignNames(item.assignNames).first }}
            </span>
            <span
              class="tip"
              :ref="`tip2_${index}`"
              :class="
                splitAssignNames(item.assignNames).num > 1
                  ? 'tipShow'
                  : 'tipHide'
              "
              v-if="splitAssignNames(item.assignNames).num > 1"
              >{{
                $Lan(lanFunName, "etc", "等{num}人", {
                  num: splitAssignNames(item.assignNames).num,
                })
              }}</span
            >
          </div>
        </div>
        <div class="data-item" v-if="configer.includes('time')">
          <div class="data-left">{{ $Lan(lanFunName, "time", "时间") }}：</div>
          <div class="data-right ellipsis">{{ item.createTime }}</div>
        </div>
      </div>
    </div>
    <EmptyCon
      v-if="!taskList.length"
      :tip="$Lan(lanFunName, 'tipMsg', '暂无我的申请')"
    ></EmptyCon>
    <we-button
      class="expand-btn portal-primary-color-lv1"
      block
      v-if="moreShow && taskSource === 1"
      @click="viewMore"
    >
      {{ $Lan(lanFunName, "buttonText", "更多我申请的任务") }}
      <we-icon name="arrow" size="12" />
    </we-button>
  </div>
</template>

<script>
export default {
  components: {},
  name: "",
  props: {
    index: Number,
    router: Object,
  },
  data() {
    const { cardWid, cardId } = this.router;
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      lanFunName: this.router.cardId + "_h5",
      loading: false,
      moreShow: false,
      isExpand: false,
      // tip: {
      //   en_US: {
      //     tipMsg: "No Data",
      //   },
      //   zh_CN: {
      //     tipMsg: "暂无我的申请",
      //   },
      // },
      wids: [],
      configer: [],
      configInfo: {},
      // timeWidths: [],
      taskList: [], //任务列表
      dataList: [],
      taskcenter_url: null,
      taskSource: 1, //1:老待办，2：新待办
    };
  },
  created() {},
  mounted() {
    if (window.shell) {
      this.getCardData(true);
    }
    this.calcWidth();
  },
  beforeDestroy() {},
  methods: {
    openDetail(val) {
      if (!val.formUrl && !val.formMobileUrl) {
        this.$toast(this.$Lan(this.lanFunName, "noLink", "暂无跳转链接"));
        return
      }
      if (val) {
        window.minosStataCollect.collect({
          actionType: 0,
          functionType: 1,
          actionParams: {
            pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
            pageName: window.shell.getPageData().pageInfoEntity.pageName,
            cardWid: this.router.cardWid,
            cardId: this.router.cardId,
            cardName: this.router.cardName,
            extraInfo: "",
          },
          startTime: new Date().getTime(),
        });
        window.shell.openPage(val.formMobileUrl || val.formUrl, 1, 2);
      }
    },
    handleTimedRefresh(isInit) {
      // 未登录或者开关未开启不自动刷新
      if (!window.shell.getUserInfo() || !this.configInfo?.refreshRate) {
        return;
      }
      if (this.refreshTimer) {
        window.clearInterval(this.refreshTimer);
        this.refreshTimer = null;
      }
      this.refreshTimer = window.setInterval(() => {
        window.shell
          .execCardMethod({
            cardId: this.router.cardId,
            cardWid: this.router.cardWid,
            method: "render",
            param: {
              mobile: 1,
            },
          })
          .then((res) => {
            if (res.errcode == 0) {
              // 后端接口 createTime字段会变化，故需忽略该字段
              const tempData = JSON.parse(this.originData);
              if (tempData && tempData.taskInfo) {
                tempData.taskInfo.map((item) => {
                  delete item.createTime;
                  return item;
                });
              }
              if (res.data && res.data.taskInfo) {
                res.data.taskInfo.map((item) => {
                  delete item.createTime;
                  return item;
                });
              }
              // 数据不一致更新数据
              if (JSON.stringify(res.data) !== JSON.stringify(tempData)) {
                this.getCardData(false, true);
                // 更新角标
                window.shell.emit("getMarkNumber", {
                  cardId: this.router.cardId,
                  cardWid: this.router.cardWid,
                });
              }
            }
          });
      }, parseInt(this.configInfo.refreshRate * 1000));
      if (!isInit) {
        this.$once("hook:beforeDestroy", () => {
          window.clearInterval(this.refreshTimer);
          this.refreshTimer = null;
        });
      }
    },
    getCardData(isInit, fromRefresh) {
      window.minosStataCollect.loadStart({
        listId: this.listId,
        actionType: 3,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: "",
        },
        startTime: new Date().getTime(),
      });
      this.loading = true;
      window.shell
        .execCardMethod({
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "render",
          param: {
            mobile: 1,
          },
        })
        .then((data) => {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime(),
          });
          this.originData = JSON.stringify(data.data);
          this.loading = false;
          this.configInfo = data.data.config;
          this.configer = data.data.config.itemConfigure;
          this.dataList = data.data.taskInfo;
          this.taskcenter_url = data.data.taskcenter_url;
          this.taskSource = parseInt(data?.data?.taskSource) || 1;
          // this.$nextTick(function () {
          //   if (this.dataList.length) {
          //     this.$refs["time"].forEach((ele) => {
          //       this.timeWidths.push(ele.clientWidth);
          //     });
          //   }
          // });
          this.showMoreBtn();
          // 定时更新触发的无需重新计时，其他操作触发的需重新计时
          !fromRefresh && this.handleTimedRefresh(isInit);
        })
        .catch(() => {
          this.loading = false;
          // 定时更新触发的无需重新计时，其他操作触发的需重新计时
          !fromRefresh && this.handleTimedRefresh(isInit);
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime(),
          });
        });
    },

    // 查看更多 -> 进入任务中心
    viewMore() {
      // this.taskList = this.dataList;
      // this.moreShow = false;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: "",
        },
        startTime: new Date().getTime(),
      });
      window.shell.openPage(this.taskcenter_url, 1, 2);
    },

    calcWidth() {
      this.$nextTick(() => {
        this.taskList.forEach((ele, i) => {
          ele.tipWidth = this.$refs[`tip_${i}`][0].clientWidth;
          ele.tagWidth = this.$refs[`tag_${i}`][0].clientWidth;
        });
      });
    },

    // 校验是否满足展开条件
    showMoreBtn() {
      const rowsNum = this.configInfo.rows;
      const taskLen = this.dataList.length;
      this.moreShow = taskLen > rowsNum ? true : false;
      this.taskList =
        taskLen > rowsNum ? this.dataList.slice(0, rowsNum) : this.dataList;
    },

    // 数组化待处理人
    splitAssignNames(data) {
      const arrs = !data ? [] : data.split(",");
      return { num: arrs.length, first: arrs[0] || "-" };
    },
  },
  computed: {
    readWids() {
      return this.wids.length
        ? this.wids
        : JSON.parse(localStorage.getItem("mytask_WIDS")) || [];
    },
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 5000);
      }
    },
  },
};
</script>

<style lang="less" scoped>
[flex-col-center] {
  display: flex;
  align-items: center;
}
.card-mytask {
  position: relative;
  width: 100%;
  padding: 0 17px;
  .card-mytask-list {
    padding: 16px 0;
    &:not(:last-of-type) {
      border-bottom: 1px solid #e7edf1;
    }
    .card-mytask-title {
      margin-bottom: 12px;
      height: 20px;
      line-height: 20px;
      white-space: nowrap;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .title {
        // font-family: PingFangSC-Regular;
        font-size: 16px;
        color: #102645;
        display: inline-block;
      }
    }
    .card-mytask-info {
      & > div:not(:last-child) {
        margin-bottom: 8px;
      }

      font-size: 14px;
      color: #707d8f;
      .data-item {
        height: 18px;
        width: 100%;
        line-height: 18px;
        display: flex;
        .data-left {
          flex: 1;
        }
        .data-right {
          flex: 3;
          text-align: right;
          width: 0;
          .tip {
            display: inline-block;
          }
          .handler_name {
            display: inline-block;
            max-width: calc(~"100% - 90px");
          }
        }
      }
    }
  }
  .expand-btn {
    height: 48px;
    background: #f9fafb;
    border-radius: 4px;
    // color: #307afb;
    border: none;
    font-size: 16px;
    margin-bottom: 12px;
    /deep/ .we-button__text {
      line-height: 20px;
    }
  }
  .tag {
    // font-family: PingFangSC-Regular;
    font-size: 12px;
    border-radius: 2px;
    padding: 0 6px;
    line-height: 18px;
  }
  .tag-withdraw {
    background: rgba(99, 115, 137, 0.1);
    border: 1px solid #9fa8b5;
    color: #9fa8b5;
  }
  .tag-complete {
    background: rgba(16, 149, 27, 0.1);
    border: 1px solid #25b14d;
    color: #25b14d;
  }
  .tag-audit {
    background: rgba(255, 144, 0, 0.1);
    border: 1px solid #ff9000;
    color: #ff9000;
  }
  .tipShow {
    display: inline;
  }
  .tipHide {
    display: none;
  }
  .no_wrap {
    white-space: nowrap;
  }
}
</style>
