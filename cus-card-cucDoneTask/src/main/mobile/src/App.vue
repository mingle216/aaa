<template>
  <div class="card-donetask" v-loading="loading">
    <div
      class="card-donetask-list"
      v-for="(item, index) in taskList"
      :key="`done_${index}`"
      @click="openDetail(item)"
    >
      <div class="card-donetask-title">
        <div class="taskInfo-topLeft">
          <span
            class="title text__ellipsis"
            :class="[
              readWids.includes(item.wid)
                ? 'portal-font-color-lv2'
                : 'portal-font-color-lv1',
            ]"
            :style="{
              width: !configer.includes('state') ? '100%' : '',
            }"
          >
            {{ item.subject }}</span
          >
          <template
            v-if="
              configer.includes('state') && processStatus[item.processStatus]
            "
          >
            <span
              class="tag"
              :class="{
                'tag-on':
                  item.processStatus === 'RUNNING' ||
                  item.processStatus === 'ACTIVE',
                'tag-complete': item.processStatus === 'COMPLETE',
                'tag-finish':
                  item.processStatus === 'ABORT' ||
                  item.processStatus === 'WITHDRAW',
              }"
            >
              {{
                $Lan(
                  lanFunName,
                  item.processStatus,
                  processStatus[item.processStatus]
                )
              }}
            </span>
          </template>
        </div>
        <span
          v-if="configInfo && configInfo.showFavorite == '1'"
          class="iconfont favoriteIcon"
          :class="[
            item.favorite
              ? 'icon-icon-mianxingshoucang'
              : 'unfavorite icon-icon-xiankuangxingshoucang',
          ]"
          @click.stop="handleFavorite(item)"
        ></span>
      </div>
      <div class="card-donetask-detail">
        <div class="data-item" v-if="configer.includes('source')">
          <div class="data-left">
            {{ $Lan(lanFunName, "bizDomain", "所属应用") }}：
          </div>
          <div class="data-right ellipsis">{{ item.bizDomain || "-" }}</div>
        </div>
        <div
          class="data-item"
          v-if="configer.includes('scene') && taskSource === 2"
        >
          <div class="data-left">
            {{ $Lan(lanFunName, "sourceName", "业务场景") }}：
          </div>
          <div class="data-right ellipsis">{{ item.source_NAME || "-" }}</div>
        </div>
        <div
          class="data-item"
          v-if="configer.includes('dept') || configer.includes('author')"
        >
          <div class="data-left">{{ viewLabelHandle }}：</div>
          <div class="data-right ellipsis">{{ viewDataHandle(item) }}</div>
        </div>
        <div class="data-item" v-if="configer.includes('time')">
          <div class="data-left">{{ $Lan(lanFunName, "time", "时间") }}：</div>
          <div class="data-right ellipsis">{{ item.endTime }}</div>
        </div>

        <template v-if="configer.includes('flowchart')">
          <div class="detail-link" v-if="item.processInstanceImageUrl">
            <a
              class="link"
              @click.stop="viewChart(item)"
              :href="item.processInstanceImageUrl"
              >{{ $Lan(lanFunName, "showLct", "showLct") }}</a
            >
            <!-- <we-button plain @click.stop="viewChart(item)">{{
              $Lan(lanFunName, "showLct", "showLct")
            }}</we-button> -->
          </div>
        </template>
      </div>
    </div>
    <EmptyCon
      v-if="!taskList.length"
      :tip="$Lan(lanFunName, 'tipMsg', '暂无已办任务')"
    />
    <we-button
      class="expand-btn portal-primary-color-lv1"
      block
      v-if="moreShow && taskSource === 1"
      @click="viewMore"
    >
      {{ $Lan(lanFunName, "showMoreTip", "更多已办任务") }}
      <we-icon name="arrow" size="12" />
    </we-button>
    <pc-link
      ref="PcLink"
      :pcViewUrl="pcViewUrl"
      :lanFunName="lanFunName"
    ></pc-link>
  </div>
</template>

<script>
import PcLink from "./component/pcLink";
import TrackMixin from "./mixins/track.js";
export default {
  components: {
    PcLink
  },
  name: "",
  props: {
    index: Number,
    router: Object,
    dataNum: {
      type: Number,
      default: 0,
    },
  },
  mixins: [TrackMixin],
  data() {
    return {
      loading: false,
      moreShow: false,
      emptyShow: false,
      // tip: {
      //   en_US: {
      //     tipMsg: "No todoTask",
      //     showMoreTip: "Show More",
      //   },
      //   zh_CN: {
      //     tipMsg: "暂无已办任务",
      //     showMoreTip: "更多已办任务",
      //   },
      // },
      lanFunName: this.router.cardId + "_h5",
      processStatus: {
        RUNNING: "进行中",
        ACTIVE: "进行中",
        COMPLETE: "已完成",
        ABORT: "已终止",
        WITHDRAW: "已终止",
      },

      taskcenter_url: "",
      configInfo: {},
      wids: [],
      taskList: [], //任务列表
      configer: [],
      // timeWidths: [],
      dataList: [],
      taskSource: 1,
      pcViewUrl: "",
    };
  },
  created() {},
  mounted() {
    if (window.shell) {
      this.getCardData(true);
      window.shell.on("collectTask", this.handleUpdateFavorite);
    }
  },
  computed: {
    readWids() {
      return this.wids.length
        ? this.wids
        : JSON.parse(localStorage.getItem("DONETASK_WIDS")) || [];
    },
    viewLabelHandle() {
      if (!this.configer.includes("dept") && this.configer.includes("author")) {
        return this.$Lan(this.lanFunName, "author", "发起人");
      }
      if (this.configer.includes("dept") && !this.configer.includes("author")) {
        return this.$Lan(this.lanFunName, "createdByDepts", "发起部门");
      }
      if (this.configer.includes("dept") && this.configer.includes("author")) {
        return this.$Lan(this.lanFunName, "initiator", "发起方");
      }
      return "";
    },
    viewDataHandle() {
      return function(item) {
        if (
          !this.configer.includes("dept") &&
          this.configer.includes("author")
        ) {
          return item.author || "-";
        }
        if (
          this.configer.includes("dept") &&
          !this.configer.includes("author")
        ) {
          return item.createdByDepts || "-";
        }
        if (
          this.configer.includes("dept") &&
          this.configer.includes("author")
        ) {
          if (!item.author && !item.createdByDepts) {
            return "-";
          }
          return `${item.author || ""} ${item.createdByDepts || ""}` || "-";
        }
        return "-";
      };
    },
    diffStr() {
      return function(str1, str2) {
        let new_str1 = str1 || "";
        let new_str2 = str2 || "";
        if (new_str1.length > new_str2.length) {
          return true;
        }
        return false;
      };
    },
  },
  beforeDestroy() {
    window.shell.off("collectTask", this.handleUpdateFavorite);
  },
  methods: {
    handleUpdateFavorite({ id, res }) {
      if (res.errcode === "0") {
        this.taskList.some((element) => {
          if (element.taskId === id) {
            element.favorite = !element.favorite;
          }
        });
      }
    },
    async handleFavorite(item) {
      if (item.loading) {
        return;
      }
      item.loading = true;
      // 请求
      try {
        await window.shell.collectTask({
          operate: item.favorite ? 0 : 1, //  0:取消收藏 1:收藏
          id: item.taskId,
        });
        item.loading = false;
      } catch (error) {
        item.loading = false;
      }
      this.handleClickTrack({
        fucType: item.favorite ? 3 : 2, //  3:取消收藏 2:收藏
        infoType: 21,
        taskId: item.taskId,
      }); // 点击埋点
    },
    // 详情跳转
    openDetail(item) {
      this.handleClickTrack(); // 点击埋点
      // 移动端同pc端，点流程图和整条数据均跳
      if (item.formMobileUrl) {
        window.shell.openPage(item.formMobileUrl, 1, 2);
      } else if (!item.formMobileUrl && item.formUrl) {
        //弹出 底部弹框
        this.pcViewUrl = item.formUrl;
        this.$refs.PcLink.show();
      } else {
        //暂无链接
        this.$toast(this.$Lan(this.lanFunName, "noLink", "暂无跳转链接"));
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
            param: {},
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
              }
            }
          });
      }, parseInt(this.configInfo?.refreshRate * 1000));
      if (isInit) {
        this.$once("hook:beforeDestroy", () => {
          window.clearInterval(this.refreshTimer);
          this.refreshTimer = null;
        });
      }
    },
    getCardData(isInit, fromRefresh) {
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "render",
          param: {},
        },
        (data) => {
          this.loading = false;
          this.originData = JSON.stringify(data.data);
          this.configer = data.data.config.itemConfigure;
          this.configInfo = data.data.config || {};
          this.taskcenter_url = data.data.taskcenter_url;
          this.taskList = data.data.taskInfo || [];
          this.taskSource = parseInt(data?.data?.taskSource) || 1;
          this.showMoreBtn();
          this.loadedEndTrack(); // 页面加载结束埋点
          // 定时更新触发的无需重新计时，其他操作触发的需重新计时
          !fromRefresh && this.handleTimedRefresh(isInit);
        }
      );
    },
    viewMore() {
      this.handleClickTrack(); // 点击埋点
      window.shell.openPage(this.taskcenter_url, 1, 2);
    },

    // viewMore() {
    //   this.taskList = this.dataList;
    //   this.moreShow = false;
    // },

    // 查看流程图
    viewChart(data) {
      this.handleClickTrack(); // 点击埋点
      if (!data.processInstanceImageUrl) {
        return;
      }
      this.wids = JSON.parse(localStorage.getItem("DONETASK_WIDS")) || [];
      if (!this.wids.includes(data.wid)) {
        this.wids.push(data.wid);
        localStorage.setItem("DONETASK_WIDS", JSON.stringify(this.wids));
      }
      // window.shell.openPage(data.processInstanceImageUrl, 1, 2);
    },

    // 校验是否满足展开条件
    showMoreBtn() {
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getMarkNumber",
        },
        ({ errcode, data }) => {
          if (errcode === "0") {
            this.moreShow = data > this.taskList.length;
            // 更新角标
            window.shell.emit("getMarkNumber", {
              cardId: this.router.cardId,
              cardWid: this.router.cardWid,
              data,
            });
          } else {
            this.moreShow = false;
          }
        }
      );
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
.card-donetask {
  position: relative;
  width: 100%;
  padding: 0 17px;
  .expand-btn {
    // color: #307afb;
    margin-bottom: 12px;
    background: #f9fafb;
    border-radius: 4px;
    border: none;
    width: 100%;
    span {
      font-size: 16px;
      // color: #307afb;
      letter-spacing: 0;
      text-align: center;
      line-height: 20px;
    }
  }
  .card-donetask-list {
    padding: 16px 0;
    border-bottom: 1px solid #e7edf1;
    &:last-child {
      border-bottom: none;
    }
    .card-donetask-title {
      margin-bottom: 12px;
      white-space: nowrap;
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 20px;
      .title {
        // font-family: PingFangSC-Regular;
        font-size: 16px;
      }
    }
    .card-donetask-detail {
      font-size: 14px;
      color: #707d8f;

      & > div:not(:last-child) {
        margin-bottom: 8px;
      }
      .data-item {
        height: 18px;
        width: 100%;
        line-height: 18px;
        display: flex;
        .data-left {
          flex: 1;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .data-right {
          flex: 3;
          text-align: right;
          width: 0;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
      .detail-link {
        display: flex;
        justify-content: flex-end;
        // /deep/.we-button--plain.we-button {
        //   border: 1px solid #d6dade;
        //   height: 32px;
        //   border-radius: 4px;
        //   .we-button__text {
        //     color: #707d8f;
        //   }
        // }
        .link {
          height: 32px;
          line-height: 32px;
          border: 1px solid #d6dade;
          border-radius: 4px;
          font-size: 14px;
          color: #707d8f;
          display: block;
          text-align: center;
          padding: 0 5px;
        }
      }
    }
  }
  .tag {
    // font-family: PingFangSC-Regular;
    font-size: 12px;
    border-radius: 2px;
    padding: 0 6px;
    color: #8c8c8c;
    line-height: 18px;
    flex-shrink: 0;
    margin-left: 6px;
    max-width: 100px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .tag-on {
    background: rgba(255, 144, 0, 0.1);
    border: 1px solid #ff9000;
    color: #ff9000;
  }
  .tag-complete {
    background: rgba(16, 149, 27, 0.1);
    border: 1px solid #25b14d;
    color: #25b14d;
  }
  .tag-finish {
    background: rgba(255, 35, 12, 0.1);
    border: 1px solid #ff230c;
    color: #ff230c;
  }
}
.taskInfo-topLeft {
  display: flex;
  align-items: center;
  flex: 1;
  width: 0;
}
.favoriteIcon {
  font-size: 14px !important;
  margin-left: 20px;
  color: #ffbc00;
  flex-shrink: 0;
  &.unfavorite {
    color: #bfbfbf;
  }
}
</style>
