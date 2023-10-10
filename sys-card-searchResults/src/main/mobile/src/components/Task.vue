<template>
  <div>
    <we-list
      class="news-list"
      v-model="loading"
      :finished="finished"
      :finished-text="$Lan(lanFunName, 'finishedText', '没有更多了')"
      @load="loadData"
    >
      <template v-for="(item, index) in list">
        <div class="taskItem portal-font-color-lv2"
          v-if="index < currentPage * pageSize"
          :key="index">
          <div class="sub-subject">
            <div class="taskInfo-topLeft">
              <span
                class="subject portal-font-color-lv1"
                @click="handleSubject(item)"
                v-html="highLight(item.subject)"
                ></span
              >
              <div
                class="taskInfo-type undone ellipsis"
                v-if="item.statusCode === 'ACTIVE'"
              >
                {{ $Lan(lanFunName, "todo", "待办") }}
              </div>
              <div class="taskInfo-type done ellipsis" v-else>
                {{ $Lan(lanFunName, "done", "已办") }}
              </div>
            </div>
            <span v-if="showFavoriteTask == 1">
              <span
                v-if="item.isFavorite == 1"
                class="iconfont icon-icon-mianxingshoucang favoriteIcon"
                @click.stop="fetchCollect(item)"
              ></span>
              <span
                v-else
                class="iconfont icon-icon-mianxingshoucang favoriteIcon unfavorite"
                @click.stop="fetchCollect(item)"
              ></span>
            </span>
            
          </div>
          <template v-if="item.isDelete !== 1">
            <div class="mt-12 b-flex sub-info">
              <div class="sub-item">
                <span class="title ellipsis">{{
                  $Lan(lanFunName, "filterApps", "所属应用")
                }}</span
                >：<span class="content ellipsis">{{
                  item.serviceName || item.appName || "-"
                }}</span>
              </div>
              <div class="sub-item ml-12">
                <span class="title ellipsis">{{
                  $Lan(lanFunName, "sponsor", "发起人")
                }}</span
                >：<span class="content ellipsis">{{
                  item.initiatorName || "-"
                }}</span>
              </div>
            </div>
            <div class="sub-info b-flex mt-8">
              <!-- 待办 -->
              <template v-if="item.statusCode === 'ACTIVE'">
                <span class="timeTitle ellipsis">{{
                  $Lan(lanFunName, "receiptTime", "接收时间")
                }}</span
                >：<span class="content ellipsis">{{ item.createTime || "-" }}</span>
              </template>
              <!-- 已办 -->
              <template v-else>
                <span class="timeTitle ellipsis">{{
                  $Lan(lanFunName, "processingTime", "处理时间")
                }}</span
                >：<span class="content ellipsis">{{ item.updateTime || "-" }}</span>
              </template>
              <a
                v-if="item.processInstanceImgUrl"
                class="btn-check portal-primary-color-lv1 ellipsis"
                target="_blank"
                :href="item.processInstanceImgUrl"
                >{{ $Lan(lanFunName, "checkFlow", "查看流程图") }}</a
              >
            </div>
          </template>
        </div>
      </template>
    </we-list>
    
    <!-- pc任务跳转 -->
    <we-popup
      v-model="isPcUrlShow"
      round
      position="bottom"
      :style="{ height: '80%' }"
    >
      <div class="moreListHeader moreListHeaderTask">
        <span class="portal-font-color-lv1 title ellipsis">
          {{ $Lan(lanFunName, "popTitle", "跳转任务详情") }}
        </span>
        <span
          class="iconfont icon-danchuangclose portal-font-color-lv3"
          @click="isPcUrlShow = false"
        ></span>
      </div>
      <pc-link
        :pcViewUrl="pcViewUrl"
        :lanFunName="lanFunName"
        @closePc="isPcUrlShow = false"
      />
    </we-popup>
  </div>
</template>

<script>
import PcLink from "./PcLink";
export default {
  props: {
    router: Object,
    analyzeData: Array,
    list: {
      type: Array,
      default: () => {
        return [];
      }
    },
    keyword: {
      type: String,
      default: ""
    },
    lanFunName: {
      type: String,
      default: ""
    },
    item: {
      type: Object,
      default: () => {
        return {};
      }
    },
    currentPage: {
      type: Number,
      default: 1
    },
    sortType: {
      type: Number,
      default: 1
    },
    newsJump: String,
    showFavoriteTask: Number || String
  },
  watch: {
    list: {
      immediate: true,
      handler() {
        if (this.item.total > this.currentPage * this.pageSize) {
          this.finished = false;
        } else {
          this.finished = true;
        }
      }
    }
  },
  computed: {
    highLight() {
      return val => {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(val, data);
      };
    }
  },
  components: {
    PcLink
  },
  data() {
    return {
      loading: false,
      finished: true,
      pageSize: 10,
      filterList: [
        {
          key: 1,
          name: '按匹配度排序'
        },
        {
          key: 2,
          name: '按时间排序'
        }
      ],
      currentUser: window.shell.getUserInfo(),
      isPosting: false,
      isPcUrlShow: false,
      pcViewUrl: ''
    };
  },
  methods: {
    
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
    handleFavorite(item) {
      if (item.loading) {
        return;
      }
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
      this.$dialog
        .confirm({
          title: confirmTitle,
          message: confirmTip,
          confirmButtonText: confirmButtonText,
          cancelButtonText: cancelButtonText,
        })
        .then(() => {
          this.fetchCollect(item);
        })
        .catch(() => {});
      this.handleClickTrack({
        fucType: 3, //  3:取消收藏 2:收藏
        infoType: 21,
        taskId: item.taskWid,
      }); //点击埋点
    },
    async fetchCollect(item) {
      item.loading = true;
      // 请求
      try {
        await window.shell.collectTask({
          operate: item.isFavorite == 1 ? 0 : 1, //  0:取消收藏 1:收藏
          id: item.taskId,
        });
        item.isFavorite == 1 ? item.isFavorite = 0 : item.isFavorite = 1
        item.loading = false;
      } catch (error) {
        item.loading = false;
      }
    },
    handleSubject(item) {
      this.handleClickTrack(); //点击埋点
      if (item.h5ViewUrl) {
        window.shell.openPage(item.h5ViewUrl, 1, 2);
      } else if (!item.h5ViewUrl && item.pcViewUrl) {
        //弹出 底部弹框
        this.pcViewUrl = item.pcViewUrl;
        this.isPcUrlShow = true;
      } else {
        //暂无链接
        this.$toast(this.$Lan(this.lanFunName, "noLink", "暂无跳转链接"));
      }
    },
    loadData() {
      if (this.currentPage * this.pageSize < this.item.total) {
        window.shell
          .execCardMethod({
            cardId: this.router.cardId,
            cardWid: this.router.cardWid,
            method: "moreTdcForPortal",
            param: {
              keyword: this.keyword,
              pageNumber: this.currentPage,
              pageSize: this.pageSize,
            }
          })
          .then(res => {
            const list = res.data?.searchData?.tdcData || [];
            this.$emit("update:list", this.list.concat(list));
            this.finished = false;
            this.loading = false;
            this.$emit("update:currentPage", this.currentPage + 1);
          });
      } else {
        this.finished = true;
      }
    },
  }
};
</script>

<style lang="less">
  .moreListHeaderTask {
    padding: 0 17px;
    background: rgba(0, 0, 0, 0.0001);
    box-shadow: inset 0px -1px 0px #e7edf1;
    height: 56px;
    display: flex;
    align-items: center;
    .title {
      font-weight: bold;
      font-size: 18px;
      line-height: 22px;
      flex: 1;
    }
  }
</style>
<style lang="less" scoped>
.taskItem {
  padding: 16px 0;
  width: 100%;
  // height: 148px;
  border-bottom: 1px solid #e7edf1;

  .sub-subject {
    display: flex;
  }

  .taskInfo-type {
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

  .subject {
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    word-break: break-all;
    display: block;
    font-size: 16px;
  }
  .sub-info {
    font-size: 14px;
    color: #707d8f;
    min-height: 20px;
    clear: both;
    .sub-item {
      width: 50%;
      display: flex;
      align-items: center;
      .title {
        max-width: 80px;
      }
    }
    .timeTitle {
      max-width: 80px;
    }
    .content {
      flex: 1;
      width: 0;
    }
  }
  .btn-check {
    margin-left: 8px;
    font-size: 14px;
    max-width: 100px;
  }
}
.ml-12 {
  margin-left: 12px;
}
.mt-12 {
  margin-top: 12px;
}
.mb-8 {
  margin-bottom: 8px;
}
.mt-8 {
  margin-top: 8px;
}
.b-flex {
  display: flex;
  align-items: center;
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
