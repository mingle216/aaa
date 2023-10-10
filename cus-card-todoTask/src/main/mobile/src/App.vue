<template>
  <div class="card-todotask" v-loading="loading">
    <div v-if="taskInfo.length">
      <div
        class="card-content-list"
        v-for="(item, index) in taskInfo"
        :key="index + 'todotask'"
        @click="goLink(item)"
      >
        <div class="card-content-title">
          <div class="taskInfo-topLeft">
            <span
              class="taskInfo-title text__ellipsis"
              :class="[
                readWids.includes(item.wid)
                  ? 'portal-font-color-lv2'
                  : 'portal-font-color-lv1',
              ]"
            >
              {{ item.subject }}
            </span>
            <template v-if="configer.includes('level')">
              <span
                class="title-tip"
                :class="[
                  item.priority == 1 ? 'error-tip' : '',
                  item.priority == 2 ? 'warn-tip' : '',
                ]"
                v-if="item.priority == 1 || item.priority == 2"
                >{{
                  item.priority == 1
                    ? $Lan(lanFunName, "extraUrgent", "特急")
                    : $Lan(lanFunName, "urgent", "紧急")
                }}</span
              >
            </template>
            <div v-if="item.isTop" class="set-top-icon"></div>
          </div>
          <span
            v-if="showFavorite == '1'"
            class="iconfont favoriteIcon"
            :class="[
              item.favorite
                ? 'icon-icon-mianxingshoucang'
                : 'unfavorite icon-icon-xiankuangxingshoucang',
            ]"
            @click.stop="handleFavorite(item)"
          ></span>
        </div>
        <div class="card-content-detail portal-font-color-lv2">
          <div class="data-item" v-if="configer.includes('source')">
            <div class="data-left">
              {{ $Lan(lanFunName, "source", "所属应用") }}：
            </div>
            <div class="data-right ellipsis">{{ item.bizDomain || "-" }}</div>
          </div>
          <div
            class="data-item"
            v-if="configer.includes('scene') && taskSource === 2"
          >
            <div class="data-left">
              {{ $Lan(lanFunName, "scene", "业务场景") }}：
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
            <div class="data-left">
              {{ $Lan(lanFunName, "time", "时间") }}：
            </div>
            <div class="data-right ellipsis">{{ item.createTime }}</div>
          </div>
        </div>
      </div>
    </div>
    <template v-if="moreShow && taskSource === 1">
      <we-button class="showmore portal-primary-color-lv1" @click="showAllBtn">
        <span
          >{{ $Lan(lanFunName, "showMoreTip", "更多待办任务") }} ></span
        ></we-button
      >
    </template>
    <EmptyCon
      :tip="$Lan(lanFunName, 'noData', '暂无待办任务')"
      v-if="!taskInfo.length"
    />
    <pc-link
      ref="PcLink"
      :pcViewUrl="pcViewUrl"
      :lanFunName="lanFunName"
    ></pc-link>
  </div>
</template>

<script>
import PcLink from "./component/pcLink";
export default {
  components: {
    PcLink,
  },
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
      taskInfo: [], //任务列表
      configer: [],
      // flag1: false, //发起人，发起部门，来源均为存在，则时间靠左
      // flag2: false, //发起方不为空，来源为空，则发起人，时间一行
      moreShow: false, //显示展示过多按钮
      taskcenter_url: "", //任务中心代办任务的地址
      wids: [],
      taskSource: 1,
      showFavorite: "0",
      pcViewUrl: "",
    };
  },
  created() {
    if (window.shell) {
      this.getCardData(true);
      window.shell.on("collectTask", this.handleUpdateFavorite);
    }
  },
  beforeDestroy() {
    window.shell.off("collectTask", this.handleUpdateFavorite);
  },
  methods: {
    handleUpdateFavorite({ id, res }) {
      if (res.errcode === "0") {
        this.taskInfo.some((element) => {
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
            fucType: item.favorite ? 3 : 2, //  3:取消收藏 2:收藏
            infoType: 21,
            taskId: item.taskId,
          },
        },
        startTime: new Date().getTime(),
      });
    },
    handleTimedRefresh(isInit) {
      // 未登录或者开关未开启不自动刷新
      if (!window.shell.getUserInfo() || !this.refreshRate) {
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
      }, parseInt(this.refreshRate * 1000));
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
          param: {},
        })
        .then((data) => {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime(),
          });
          this.originData = JSON.stringify(data.data);
          this.loading = false;
          this.configer = data.data.config.itemConfigure;
          this.refreshRate = data.data.config?.refreshRate;
          this.showFavorite = data.data.config?.showFavorite || "0";
          this.setData(data.data);
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
    // setConfig(configer) {
    //   this.configer = configer;
    //   if (
    //     (configer.includes("dept") || configer.includes("author")) &&
    //     !configer.includes("source")
    //   ) {
    //     this.flag1 = true;
    //   }
    //   if (configer.includes("source")) {
    //     this.flag2 = true;
    //   }
    // },
    setData(data) {
      this.taskInfo = data.taskInfo;
      // //发起人，发起部门合一
      this.taskSource = parseInt(data.taskSource) || 1;
      this.taskcenter_url = data.taskcenter_url;
      this.showMoreBtn();
    },
    // 展开全部
    showAllBtn() {
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
    // 详情跳转
    // openDetail(item) {
    // 移动端同pc端，点流程图和整条数据均跳
    // window.shell.openPage(item.formMobileUrl, 1, 2);

    // this.wids = JSON.parse(localStorage.getItem("DONETASK_WIDS")) || [];
    // if (!this.wids.includes(data.wid)) {
    //   this.wids.push(data.wid);
    //   localStorage.setItem("DONETASK_WIDS", JSON.stringify(this.wids));
    // }
    // },
    // timeformat(createtime) {
    //   let sTime = new Date(createtime);
    //   let eTime = new Date();
    //   let sDay = sTime.getDate();
    //   let eDay = eTime.getDate();
    //   let min = parseInt((eTime - sTime) / 60000);
    //   let hour = parseInt((eTime - sTime) / 3600000);
    //   if (min >= 0) {
    //     if (min < 15) {
    //       createtime = "刚刚";
    //     } else if (min >= 15 && min < 60) {
    //       createtime = `${min}分钟前`;
    //     } else if (min > 60 && hour < 24 && sDay === eDay) {
    //       createtime = `${hour}小时前`;
    //     } else if (hour < 48 && eDay - sDay === 1) {
    //       createtime = "昨天";
    //     } else {
    //       return createtime;
    //     }
    //     return createtime;
    //   }
    //   return createtime;
    // },
    goLink(item) {
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
      if (item.formMobileUrl) {
        this.wids = JSON.parse(localStorage.getItem("TODOTASK_WIDS")) || [];
        if (!this.wids.includes(item.wid)) {
          this.wids.push(item.wid);
          localStorage.setItem("TODOTASK_WIDS", JSON.stringify(this.wids));
        }
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
    // 校验是否满足展开条件
    showMoreBtn() {
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getMarkNumber",
        },
        ({ errcode, data }) => {
          if (
            errcode === "0" &&
            data > (this.taskInfo?.length || 0) &&
            this.taskcenter_url
          ) {
            this.moreShow = true;
          } else {
            this.moreShow = false;
          }
          if (errcode === "0") {
            // 更新角标
            window.shell.emit("getMarkNumber", {
              cardId: this.router.cardId,
              cardWid: this.router.cardWid,
              data,
            });
          }
        }
      );
    },
  },
  computed: {
    readWids() {
      return this.wids.length
        ? this.wids
        : JSON.parse(localStorage.getItem("TODOTASK_WIDS")) || [];
    },
    formatData() {
      return function(str) {
        return str ? str : "-";
      };
    },
    viewLabelHandle() {
      if (!this.configer.includes("dept") && this.configer.includes("author")) {
        return this.$Lan(this.lanFunName, "author", "发起人");
      }
      if (this.configer.includes("dept") && !this.configer.includes("author")) {
        return this.$Lan(this.lanFunName, "initiatingDept", "发起部门");
      }
      if (this.configer.includes("dept") && this.configer.includes("author")) {
        return this.$Lan(this.lanFunName, "initiator", "发起方");
      }
      return "-";
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
        return "";
      };
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
.card-todotask {
  position: relative;
  width: 100%;
  padding: 0 17px;
  .card-content-list {
    padding: 16px 0;
    border-bottom: 1px #e7edf1 solid;
    .card-content-title {
      margin-bottom: 12px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .error-tip {
        border: 1px solid #ff230c;
        color: #ff230c;
      }
      .warn-tip {
        border: 1px solid #ff9000;
        color: #ff9000;
      }
      .title-tip {
        min-width: 36px;
        max-width: 100px;
        padding: 0 3px;
        text-align: center;
        // height: 20px;
        line-height: 18px;
        border-radius: 2px;
        border-radius: 2px;
        background: rgba(255, 35, 12, 0.1);
        flex-shrink: 0;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      span {
        font-size: 12px;
        letter-spacing: 0;
        text-align: justify;
      }
      .taskInfo-title {
        font-size: 16px;
        color: #102645;
        letter-spacing: 0;
        line-height: 20px;
        margin-right: 6px;
        // height: 20px;
      }
      .set-top-icon {
        width: 14px;
        height: 14px;
        background: url("./assets/setTop.svg") no-repeat center;
        background-size: 100% 100%;
        margin-left: 5px;
        margin-top: 2px;
        flex-shrink: 0;
      }
    }
    .card-content-detail {
      & > div:not(:last-child) {
        margin-bottom: 8px;
      }

      font-size: 14px;
      color: #707d8f;
      letter-spacing: 0;
      line-height: 18px;

      .data-item {
        height: 18px;
        width: 100%;
        line-height: 18px;
        display: flex;
        .data-left {
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          flex: 1;
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
      // .detail-link {
      //   display: flex;
      //   justify-content: flex-end;
      //   /deep/.we-button--plain.we-button--primary {
      //     border: 0.02667rem solid #d6dade;
      //     height: 32px;
      //     border-radius: 4px;
      //     .we-button__text {
      //       color: #707d8f;
      //     }
      //   }
      // }
    }
  }
  .card-content-list:last-child {
    border: none;
  }
}
.showmore {
  width: 100%;
  background: #f9fafb;
  border-radius: 4px;
  border: none;
  margin-bottom: 12px;
  span {
    font-size: 16px;
    // color: #307afb;
    letter-spacing: 0;
    text-align: center;
    line-height: 20px;
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
