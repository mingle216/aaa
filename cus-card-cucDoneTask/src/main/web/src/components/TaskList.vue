<template>
  <div class="donetask-list">
    <AutoContainer :con-type="conType" :con-height="conHeight" :scroll="rightScroll" v-if="subList.length">
      <div class="donetask-list-main">
        <template>
          <div v-for="(item, index) in subList" class="list-item" :key="item.appId + item.taskId + index">
            <!-- {{curwidth}} -->
            <div class="item-top" :class="{
              'none-bootom': noneShowItem && !itemConfigure.includes('time'),
            }">
              <div class="title-con">
                <we-tooltip class="tast-title text-ellipsis portal-font-color-lv1" :class="{
                  'has-state': itemConfigure.includes('state'),
                }" effect="dark" placement="bottom-start" :open-delay="800">
                  <div slot="content">{{ item.subject }}</div>
                  <div class="portal-primary-color-hover-lv1" @click="e => move(item, e)" style="cursor: pointer;">
                    {{ item.subject }}
                  </div>
                </we-tooltip>
                <div class="state-con" v-if="itemConfigure.includes('state')">
                  <div class="state-tag st1" v-if="item.statusCode === 'COMPLETE'">
                    {{ $Lan(lanFunName, "complete", "已完成") }}
                  </div>
                  <div class="state-tag st2" v-if="
                    item.statusCode === 'RUNNING' ||
                    item.statusCode === 'ACTIVE'
                  ">
                    {{ $Lan(lanFunName, "running", "进行中") }}
                  </div>
                  <div class="state-tag st3" v-if="
                    item.statusCode === 'ABORT' ||
                    item.statusCode === 'WITHDRAW'
                  ">
                    {{ $Lan(lanFunName, "abort", "已终止") }}
                  </div>
                </div>
              </div>
              <!-- <div style="width: 160px"></div> -->
              <div class="lct portal-primary-color-lv1" :class="{ 'hide-text': !defaultModel }" v-if="showFlowChart(item)"
                @click.stop="move2(item)">
                <i class="iconfont icon-flowChart"></i>
                <span class="lctword">{{
                  $Lan(lanFunName, "flowSheet", "流程图")
                }}</span>
              </div>
              <!-- 收藏 -->
              <span v-if="showFavorite == '1'" class="iconfont icon-icon-mianxingshoucang favoriteIcon"
                :class="[item.isFavorite ? '' : 'unfavorite']" @click.stop="handleFavorite(item)"></span>
            </div>
            <div class="item-bottom portal-font-color-lv3" v-if="!noneShowItem || itemConfigure.includes('time')">
              <div v-if="!noneShowItem" class="item-bottom-left" :class="[showItemNum]">
                <we-tooltip v-if="itemConfigure.includes('source')" class="show-item text-ellipsis" effect="dark"
                  placement="bottom-start" :open-delay="800" :disabled="disabledToolTip(item, 'bizDomain')">
                  <div slot="content">{{ item.bizDomain || "-" }}</div>
                  <div class="source">
                    <template v-if="defaultModel">
                      {{ $Lan(lanFunName, "bizDomain", "所属应用") }}:
                    </template>
                    {{ item.bizDomain || "-" }}
                  </div>
                </we-tooltip>

                <we-tooltip v-if="itemConfigure.includes('scene') && taskSource === 2" class="show-item text-ellipsis"
                  effect="dark" placement="bottom-start" :open-delay="800"
                  :disabled="disabledToolTip(item, 'source_NAME')">
                  <div slot="content">
                    {{ item.sceneName || "-" }}
                  </div>
                  <div class="source text-ellipsis">
                    <template v-if="defaultModel">
                      {{ $Lan(lanFunName, "sourceName", "业务场景") }}:
                    </template>
                    {{ item.sceneName || "-" }}
                  </div>
                </we-tooltip>

                <we-tooltip v-if="itemConfigure.includes('author')" class="show-item text-ellipsis" effect="dark"
                  placement="bottom-start" :open-delay="800" :disabled="disabledToolTip(item, 'author')">
                  <div slot="content">{{ item.initiatorName || "-" }}</div>
                  <div class="source">
                    <template v-if="defaultModel">
                      {{ $Lan(lanFunName, "author", "发起人") }}：
                    </template>
                    {{ item.initiatorName || "-" }}
                  </div>
                </we-tooltip>

                <we-tooltip v-if="itemConfigure.includes('dept')" class="show-item text-ellipsis" effect="dark"
                  placement="bottom-start" :open-delay="800" :disabled="disabledToolTip(item, 'createdByDepts')">
                  <div slot="content">
                    {{ item.initiatorDept || "-" }}
                  </div>
                  <div class="source">
                    <template v-if="defaultModel">
                      {{ $Lan(lanFunName, "initiatorDept", "发起部门") }}：
                    </template>
                    {{ item.initiatorDept || "-" }}
                  </div>
                </we-tooltip>
              </div>

              <div class="time text-ellipsis" :class="{
                'text-left': noneShowItem,
                'mini-width': !defaultModel,
              }" v-if="itemConfigure.includes('time')">
                {{ item.updateTime }}
              </div>
            </div>
          </div>
        </template>
      </div>
    </AutoContainer>
    <EmptyCon :tip="emptyword" v-if="!subList.length"></EmptyCon>
    <we-popover ref="MobilePop" popper-class="doneTaskPop" placement="bottom" width="175">
      <div class="MobilePop">
        <div class="portal-font-color-lv3 ellipsis">
          {{ $Lan("public", "mobileUrl", "当前为移动地址扫一扫查看") }}
        </div>
        <div class="popQrcode portal-primary-border-color-lv3">
          <div :id="`${listId}Qrcode`" ref="qrcode2"></div>
        </div>
        <div class="popCopy portal-primary-color-lv1 portal-primary-backgroundcolor-lv5 ellipsis" @click="copyLink">
          <i class="iconfont icon-lianjie" style="font-size: inherit"></i>
          {{ $Lan("public", "copyUrl", "复制链接") }}
        </div>
      </div>
    </we-popover>
    <div class="pages" v-if="total > 0">
      <p class="portal-font-color-lv1">共{{ total }}条</p>
      <we-pagination background layout="prev, pager, next" :pager-count="5" :total="total" :page-size="pageSize"
        @current-change="handlePage">
      </we-pagination>
    </div>
  </div>
</template>

<script>
import QRCode from "qrcodejs2";
import { clickTrack } from "../mixins/track.js";
export default {
  props: {
    conType: {
      type: [String, Number],
      default: "3",
    },
    conHeight: {
      type: [String, Number],
      default: "500",
    },
    emptyword: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
    itemConfigure: {
      type: Array,
      default: () => {
        return [];
      },
    },
    subList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    defaultModel: {
      type: Boolean,
      default: true,
    },
    taskSource: {
      type: Number,
      default: 1,
    },
    router: Object,
    showFavorite: String,
    total: {
      type: Number,
      default: 0
    },
    pageSize: {
      type: Number,
      default: 6
    }
  },
  mixins: [clickTrack],
  computed: {
    showItemNum() {
      let num = 0;
      this.itemConfigure.includes("time") && num++;
      this.itemConfigure.includes("state") && num++;
      this.itemConfigure.includes("flowchart") && num++;
      if (this.itemConfigure.length - num === 3) {
        return "item-three";
      } else if (this.itemConfigure.length - num === 2) {
        return "item-two";
      }
      return "";
    },
    noneShowItem() {
      if (
        this.itemConfigure.includes("source") ||
        this.itemConfigure.includes("author") ||
        this.itemConfigure.includes("dept") ||
        (this.itemConfigure.includes("scene") && this.taskSource === 2)
      ) {
        return false;
      }
      return true;
    },
  },
  data() {
    return {
      rightScroll: { barKeepShow: true, scrollingX: false },
      listId: `${this.router.cardId}_${this.router.cardWid}_${new Date().getTime()}`,
    };
  },
  mounted() {
    document.addEventListener("click", this.handleDocumentClick);
    document.addEventListener('scroll', this.handleScroll, true);
  },
  beforeDestroy() {
    document.removeEventListener("click", this.handleDocumentClick);
    document.removeEventListener("scroll", this.handleScroll);
    this.reference = null;
  },
  methods: {
    handlePage(page) {
      this.$emit('handlePage', page)
    },
    handleDocumentClick(e) {
      const reference = this.reference;
      const popper = this.$refs.MobilePop;
      if (
        !reference ||
        reference.contains(e.target) ||
        !popper ||
        !popper.$refs.popper ||
        popper.$refs.popper.contains(e.target)
      )
        return;
      popper.doClose();
    },
    handleScroll() {
      const popper = this.$refs.MobilePop;
      popper && popper.doClose();
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
        taskId: item.taskId
      }); // 点击埋点
    },
    disabledToolTip(item, field) {
      return !item[field] || item[field] === "-";
    },
    move(row, e) {
      this.handleClickTrack(); // 点击埋点
      if (row.pcViewUrl) {
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
    createQrcode(row, event) {
      const pop = this.$refs.MobilePop;
      if (pop) {
        pop.doClose();
        pop.referenceElm = event.target;
        this.reference = event.target;
        pop.$refs.popper && (pop.$refs.popper.style.display = "none");
        pop.doDestroy();
        setTimeout(() => {
          pop.doShow();
          const el = document.getElementById(`${this.listId}Qrcode`);
          if (el) {
            el.innerHTML = "";
            // 使用$nextTick确保数据渲染
            this.$nextTick(() => {
              new QRCode(el, {
                width: 84,
                height: 84, // 高度
                text: row.formMobileUrl, // 二维码内容
              });
            });
          }
          this.clickMobileUrl = row.formMobileUrl;
        }, 10)
      }
    },
    copyLink() {
      window.shell.copyText(this.clickMobileUrl);
      this.handleClickTrack(); // 点击埋点
    },
    showFlowChart(item) {
      if (
        this.itemConfigure.includes("flowchart") &&
        item.processInstanceImgUrl
      ) {
        return true;
      } else {
        return false;
      }
    },

    move2(val) {
      // 流程图跳转
      this.handleClickTrack(); // 点击埋点
      window.shell.openPage(val.processInstanceImageUrl, 1, 2);
    },
  },
};
</script>

<style lang="less" scoped>
.donetask-list {
  flex: 1;
  width: 0;
  position: relative;

  .donetask-list-main {
    width: 100%;
    padding: 0 14px;

    // min-width: 360px;
    .list-item {
      position: relative;
      background: #ffffff;
      box-shadow: inset 0 -1px 0 0 #f1f2f4;
      overflow: hidden;
      padding: 14px 0;
      // padding: 14px 3px 14px 12px;

      &:last-child {
        box-shadow: none;
      }
    }
  }

  .item-top,
  .item-bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;
    /* min-width: 340px; */
  }

  .item-top {
    position: relative;
    font-size: 16px;
    line-height: 24px;
    // top: -6px;
    margin-bottom: 2px;

    &.none-bootom {
      margin-bottom: 0px;
    }
  }

  .line1 {
    position: relative;
  }

  .item-bottom {
    height: 22px;
    line-height: 22px;
    font-size: 14px;
  }

  .tast-title {

    // width: 0;
    &.has-state {
      max-width: calc(100% - 60px);
    }
  }

  .title-con {
    // min-width: 198px;
    display: flex;
    align-items: center;
    width: 0;
    flex: 1;
  }

  .state-con {
    // min-width: 70px;
    position: relative;
    top: -1px;
    margin-left: 8px;
    margin-right: 5px;

    .state-tag {
      /* font-family: PingFangSC; */
      font-size: 12px;
      letter-spacing: 0;
      line-height: 20px;
      border-radius: 4px;
      height: 21px;
      text-align: center;
      padding: 0 6px;

      &.st1 {
        color: #25b14d;
        background: rgba(16, 149, 27, 0.1);
        border: 1px solid #25b14d;
      }

      &.st2 {
        background: rgba(255, 144, 0, 0.1);
        border: 1px solid #ff9000;
        color: #ff9000;
      }

      &.st3 {
        background: rgba(255, 35, 12, 0.1);
        border: 1px solid #ff230c;
        color: #ff230c;
      }
    }
  }

  .item-bottom-left {
    /* border: 1px solid red; */
    display: flex;
    /* justify-content: space-between; */
    // min-width: 196px;
    flex: 1;
    align-items: center;
    position: relative;
    top: 1px;
    width: 0;
    overflow: hidden;

    .show-item {
      // min-width: 48px;
      padding-right: 20px;
      flex: 3;
    }

    &.item-two {
      .show-item {
        &:nth-child(2) {
          flex: 7;
        }
      }
    }

    &.item-three {
      .show-item {
        &:nth-child(3) {
          flex: 4;
        }
      }
    }
  }

  .time {
    // position: absolute;
    // right: 0px;
    width: 140px;
    text-align: right;

    &.mini-width {
      width: 50px;
    }

    &.text-left {
      text-align: left;
      flex: 1;
    }
  }

  .lct {
    height: 20px;
    line-height: 20px;
    font-size: 12px;
    cursor: pointer;
    width: 80px;
    min-width: 80px;
    text-align: right;

    .iconfont {
      position: relative;
      left: -6px;
      font-size: 13px;
      cursor: pointer;
    }

    &.hide-text {
      width: 30px;
      min-width: 30px;

      .lctword {
        display: none;
      }
    }
  }

  .favoriteIcon {
    cursor: pointer;
    font-size: 14px;
    margin-left: 8px;
    color: #ffbc00;

    &.unfavorite {
      color: #bfbfbf;
    }
  }
}

.MobilePop {
  font-size: 12px;
  line-height: 20px;
  text-align: center;

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
}

.pages {
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: absolute;
  bottom: 10px;
  left: 14px;
  width: calc(100% - 14px);
}
</style>
<style lang="less">
.doneTaskPop {
  padding: 14px 12px 0 12px !important;
}
</style>

