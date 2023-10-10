<template>
  <div :class="[{'lastNoBorder': false}]">
    <div class="favTasks__list">
      <div class="favTasks__listTop portal-font-color-lv1">
        <div class="favTasks__flex favTasks__flex__top" style="flex: 1; width: 0">
          <!-- 被撤回 -->
          <!-- <div
            class="favTasks__listTitle portal-font-color-lv4 ellipsis"
            v-if="item.isDelete === 1"
          >
            {{ $Lan(lanFunName, "deletedTask", "此项任务已被删除") }}
          </div> -->
          <template>
            <div
              class="favTasks__listTitle portal-primary-color-hover-lv1 ellipsis"
              @click="(e) => handleGoLink(item, e)"
              v-html="highLight('subject')"
            >
            </div>
            <div
              class="favTasks__list-type undone ellipsis"
              v-if="item.statusCode === 'ACTIVE'"
            >
              {{ $Lan(lanFunName, "todo", "待办") }}
            </div>
            <div class="favTasks__list-type done ellipsis" v-else>
              {{ $Lan(lanFunName, "done", "已办") }}
            </div>
          </template>
        </div>
        <div class="favTasks__flex">
          <!-- 流程图 -->
          <we-button
            v-if="item.processInstanceImgUrl"
            type="text"
            style="margin-left:8px;padding:0"
            size="small"
            @click.stop="handleFlow(item)"
            ><span
              class="iconfont icon-flowChart"
              style="margin-right: 6px; font-size: 14px"
            ></span
            >{{ $Lan(lanFunName, "flowChart", "流程图") }}</we-button
          >
          <!-- 收藏 -->
          <span v-if="showFavoriteTask == 1">
            <span
              class="iconfont icon-icon-mianxingshoucang favoriteIcon"
              @click.stop="fetchCollect(item)"
              v-if="item.isFavorite == 1"
            ></span>
            <span
              v-else
              class="iconfont icon-icon-mianxingshoucang favoriteIcon unfavorite"
              @click.stop="fetchCollect(item)"
            ></span>
          </span>
        </div>
      </div>
      <div
        class="favTasks__flex mt-2 portal-font-color-lv3"
        v-if="item.isDelete !== 1"
      >
        <div class="favTasks__listInfo favTasks__flex">
          <div class="favTasks__listLabel ellipsis">
            {{ $Lan(lanFunName, "filterApps", "所属应用") }}
          </div>
          ：
          <div class="favTasks__listVal ellipsis">
            {{ item.serviceName || item.appName || "-" }}
          </div>
        </div>
        <div class="favTasks__listInfo favTasks__flex">
          <div class="favTasks__listLabel ellipsis">
            {{ $Lan(lanFunName, "sponsor", "发起人") }}
          </div>
          ：
          <div class="favTasks__listVal ellipsis">
            {{ item.initiatorName || "-" }}
          </div>
        </div>
        <!-- 待办 -->
        <div
          class="favTasks__listInfo favTasks__flex"
          v-if="item.statusCode === 'ACTIVE'"
        >
          <div class="favTasks__listLabel ellipsis">
            {{ $Lan(lanFunName, "receivingTime", "接收时间") }}
          </div>
          ：
          <div class="favTasks__listVal ellipsis">
            {{ item.createTime || "-" }}
          </div>
        </div>
        <!-- 已办 -->
        <div class="favTasks__listInfo favTasks__flex" v-else>
          <div class="favTasks__listLabel ellipsis">
            {{ $Lan(lanFunName, "processingTime", "处理时间") }}
          </div>
          ：
          <div class="favTasks__listVal ellipsis">
            {{ item.updateTime || "-" }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    item: Object,
    analyzeData: Array,
    keyword: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
    showNav: {
      type: Boolean,
      default: true,
    },
    searchResultWidth: {
      type: Number,
      default: 1026,
    },
    createQrcode: Function,
    newsJump: String,
    showFavoriteTask: Number || String
  },
  computed: {
    highLight() {
      return (type) => {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(this.item[type], data);
      };
    },
    newsItemWidth() {
      const num = window.shell.isIE() ? 18 : 8;
      return this.showNav
        ? `${this.searchResultWidth - 126}px`
        : `${this.searchResultWidth - num}px`;
    },
  },
  data() {
    return {
      errImg: window.shell.ErrorImg,
      currentUser: window.shell.getUserInfo(),
      isPosting: false
    };
  },
  methods: {
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errImg;
      img.onerror = null;
    },
    linkUrl(item) {
      this.handleClickTrack()
      if (item.sideFlag == 2 || (item.sideFlag != 1 && this.newsJump === "1")) {
        //跳转到本地新闻详情页面
        window.shell.openPage(`/newsDetail?wid=${item.wid}`, 1, 1);
      } else {
        if (item.url) {
          window.shell.openUrl(item.url);
        } else {
          window.shell.showMessage({
            type: "error",
            message: "暂无跳转链接",
          });
        }
      }
    },
    handleClickTrack(){
      return
      // window.minosStataCollect.collect({
      //   actionType: 0,
      //   functionType: 1,
      //   actionParams: {
      //     pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
      //     pageName: window.shell.getPageData().pageInfoEntity.pageName,
      //     cardWid: this.router.cardWid,
      //     cardId: this.router.cardId,
      //     cardName: this.router.cardName,
      //     extraInfo: extraInfo,
      //   },
      //   startTime: new Date().getTime(),
      // });
    },
    handleFlow(row) {
      this.handleClickTrack(); //点击埋点
      window.shell.openPage(row.processInstanceImgUrl, 1, 2);
    },
    handleGoLink(row, e) {
      if (row.pcViewUrl) {
        this.handleClickTrack(); //点击埋点
        window.shell.openPage(row.pcViewUrl, 1, 2);
      } else if (!row.pcViewUrl && row.h5ViewUrl) {
        this.createQrcode(row, e);
      } else {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan("public", "noLinkTip", "暂无跳转链接"),
        });
      }
    },
    handleFavorite(item) {
      let confirmTip = this.$Lan(this.lanFunName, "confirmTip", "确认取消收藏此条任务？"), 
      confirmTitle = this.$Lan(this.lanFunName, "confirmTitle", "取消收藏提示"),
      confirmButtonText = this.$Lan( this.lanFunName, "confirmOk", "取消收藏" ),
      cancelButtonText = this.$Lan(this.lanFunName, "confirmCancel", "不取消")
      if(item.isFavorite == 0){
        /**当前状态为已收藏，点击时为取消收藏 */
        confirmTip = this.$Lan(this.lanFunName, "confirmTipF", "确认收藏此条任务？"),
        confirmTitle = this.$Lan(this.lanFunName, "confirmTitleF", "收藏提示")
        confirmButtonText = this.$Lan( this.lanFunName, "confirmOkF", "收藏" )
        cancelButtonText = this.$Lan(this.lanFunName, "confirmCancelF", "取消")
      }
      this.$confirm(
        confirmTip,
        confirmTitle,
        {
          confirmButtonText: confirmButtonText,
          cancelButtonText: cancelButtonText,
          type: "warning",
          closeOnClickModal: false,
          closeOnPressEscape: false,
          confirmButtonClass:
            "portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1",
          cancelButtonClass:
            "bgHoverWhite portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1",
        }
      )
        .then(() => {
          this.fetchCollect(item);
        })
        .catch(() => {});
      this.handleClickTrack({
        fucType: 3, //  3:取消收藏 2:收藏
        infoType: 21,
        taskId: item.taskWid,
      });
    },
    async fetchCollect(item) {
      item.loading = true;
      // 请求
      try {
        await window.shell.collectTask({
          operate: item.isFavorite == 1 ? 0 : 1, //  0:取消收藏 1:收藏
          id: item.taskId,
        });
        /**反转收藏状态 */
        item.isFavorite == 1 ? item.isFavorite = 0 : item.isFavorite = 1
        item.loading = false;
      } catch (error) {
        item.loading = false;
      }
    },
  },
};
</script>

<style lang="less" scoped>

.favTasks__filter {
  padding: 20px 0 6px 0;
  display: flex;
  align-items: center;
  /deep/.we-icon-search {
    cursor: pointer;
  }
}
.favTasks__flex__top{
  height: 24px;
  line-height: 24px;
}
.favTasks__flex {
  display: flex;
  align-items: center;
  height: 22px;
}
.favTasks__label {
  margin-left: 40px;
  max-width: 80px;
  flex-shrink: 0;
}
.favTasks__list {
  padding: 14px 0;
  box-shadow: inset 0px -1px 0px #f0f0f0;
  &:hover {
    .favoriteIcon {
      display: block;
    }
  }
}
.lastNoBorder .favTasks__list:last-child {
  box-shadow: none;
}
.favTasks__listTitle {
  font-size: 16px;
  line-height: 24px;
  cursor: pointer;
}
.favTasks__listTop {
  display: flex;
  align-items: center;
  justify-content: space-between;
  .favoriteIcon {
    cursor: pointer;
    font-size: 14px;
    margin-left: 8px;
    color: #ffbc00;
    display: block;
    &.unfavorite {
      color: #bfbfbf;
    }
  }
}
.mt-2 {
  margin-top: 2px;
}
.ml-8 {
  margin-left: 8px;
}
.favTasks__listInfo {
  flex: 1;
  width: 0;
  padding-right: 8px;
}
.favTasks__listLabel {
  flex-shrink: 0;
  max-width: 90px;
}
.favTasks__list-type {
  border-radius: 10px;
  font-size: 12px;
  line-height: 16px;
  padding: 2px 8px;
  max-width: 80px;
  margin-left: 8px;
  flex-shrink: 0;
  &.done {
    background: rgba(37, 177, 77, 0.1);
    color: #25b14d !important;
  }
  &.undone {
    background: rgba(255, 144, 0, 0.1);
    color: #ff9000 !important;
  }
}
.favTasks__list-status {
  border-radius: 2px;
  font-size: 12px;
  line-height: 16px;
  padding: 2px 6px;
  max-width: 80px;
  margin-left: 8px;
  flex-shrink: 0;
  border: 1px solid;
  &.handleUpdateFavorite {
    background: rgba(255, 144, 0, 0.1);
    color: #ff9000 !important;
  }
  &.undone {
    background: rgba(255, 35, 12, 0.1);
    color: #ff230c !important;
  }
}
.favTasks__more {
  background: #f5f5f5;
  border-radius: 4px;
  height: 46px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}
.favTasks__page {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 12px;
  line-height: 20px;
  margin-top: 12px;
}
.MobilePop {
  font-size: 12px;
  line-height: 20px;
  text-align: center;
}
.popQrcode {
  width: 92px;
  height: 92px;
  border: 1px solid #abd2ff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin: 8px auto;
}
.popCopy {
  cursor: pointer;
  height: 30px;
  line-height: 30px;
  text-align: center;
  margin: 0 -12px;
}
</style>
