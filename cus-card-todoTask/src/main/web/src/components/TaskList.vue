<template>
  <div class="todotask-list">
    <AutoContainer
      :con-type="conType"
      :con-height="conHeight"
      :scroll="rightScroll"
    >
      <div class="todotask-list-main" v-if="subList.length">
        <div
          v-for="(item, index) in subList"
          class="list-item"
          :key="item.appId + item.taskId + index"
        >
          <template
            v-if="
              itemConfigure.includes('level') &&
                (item.priority === 1 || item.priority === 2)
            "
          >
            <we-tooltip
              :offset="14"
              class="tip"
              effect="dark"
              :content="
                item.priority === 1
                  ? $Lan(lanFunName, 'extraUrgent', '特急')
                  : $Lan(lanFunName, 'urgent', '紧急')
              "
              placement="bottom-start"
              :open-delay="800"
            >
              <div
                v-if="item.priority"
                class="icon-con"
                :class="['icon' + item.priority]"
              >
                <i class="iconfont icon-urgent"></i>
              </div>
            </we-tooltip>
          </template>
          <!-- {{curwidth}} -->
          <div
            class="item-top"
            :class="{
              'none-bootom': noneShowItem && !itemConfigure.includes('time'),
            }"
          >
            <div v-if="item.isRead === 0" class="unread"></div>
            <div class="tast-title">
              <we-tooltip
                class="text-ellipsis"
                effect="dark"
                placement="bottom-start"
                :open-delay="800"
              >
                <div slot="content">{{ item.subject }}</div>
                <div class="portal-font-color-lv1 portal-primary-color-hover-lv1" style="cursor:pointer" @click="e => move(item, e)">
                  {{ item.subject }}
                </div>
              </we-tooltip>
              <div class="newTag" v-if="item.isNew == 1" />
              <we-tooltip
                class="text-ellipsis portal-font-color-lv1"
                effect="dark"
                v-show="item.isTop"
              >
                <div slot="content">
                  {{ $Lan(lanFunName, "topTip", "已置顶") }}
                </div>
                <div class="set-top-icon"></div>
              </we-tooltip>
            </div>
            <!-- 收藏 -->
            <span
              v-if="showFavorite == '1'"
              class="iconfont icon-icon-mianxingshoucang favoriteIcon"
              :class="[item.favorite ? '' : 'unfavorite']"
              @click.stop="handleFavorite(item)"
            ></span>
          </div>
          <div
            class="item-bottom portal-font-color-lv3"
            v-if="!noneShowItem || itemConfigure.includes('time')"
          >
            <!-- <div class="item-bottom-left" :style="`${curwidth}`"> -->
            <!--放开并注释下面一行-->
            <div
              class="item-bottom-left"
              :class="[showItemNum]"
              v-if="!noneShowItem"
            >
              <we-tooltip
                v-if="itemConfigure.includes('source')"
                class="show-item text-ellipsis"
                effect="dark"
                placement="bottom-start"
                :open-delay="800"
                :disabled="disabledToolTip(item, 'bizDomain')"
              >
                <div slot="content">{{ item.bizDomain || "-" }}</div>
                <div class="source text-ellipsis">
                  <template v-if="defaultModel">
                    {{ $Lan(lanFunName, "bizDomain", "所属应用") }} :
                  </template>
                  {{ item.bizDomain || "-" }}
                </div>
              </we-tooltip>
              <we-tooltip
                v-if="itemConfigure.includes('scene') && taskSource === 2"
                class="show-item text-ellipsis"
                effect="dark"
                placement="bottom-start"
                :open-delay="800"
                :disabled="disabledToolTip(item, 'source_NAME')"
              >
                <div slot="content">{{ item.source_NAME || "-" }}</div>
                <div class="source text-ellipsis">
                  <template v-if="defaultModel">
                    {{ $Lan(lanFunName, "sourceName", "业务场景") }}:
                  </template>
                  {{ item.source_NAME || "-" }}
                </div>
              </we-tooltip>
              <we-tooltip
                v-if="itemConfigure.includes('author')"
                class="show-item text-ellipsis"
                effect="dark"
                placement="bottom-start"
                :open-delay="800"
                :disabled="disabledToolTip(item, 'author')"
              >
                <div slot="content">{{ item.author || "-" }}</div>
                <div class="person text-ellipsis">
                  <template v-if="defaultModel">
                    {{ $Lan(lanFunName, "author", "发起人") }} ：
                  </template>
                  {{ item.author || "-" }}
                </div>
              </we-tooltip>
              <we-tooltip
                v-if="itemConfigure.includes('dept')"
                class="show-item text-ellipsis"
                effect="dark"
                placement="bottom-start"
                :open-delay="800"
                :disabled="disabledToolTip(item, 'createdByDepts')"
              >
                <div slot="content">
                  {{ item.createdByDepts || "-" }}
                </div>
                <div class="dept">
                  <template v-if="defaultModel">
                    {{ $Lan(lanFunName, "createdByDepts", "发起部门") }}：
                  </template>
                  {{ item.createdByDepts || "-" }}
                </div>
              </we-tooltip>
            </div>
            <!--发起时间-->
            <div
              class="creat-time text-ellipsis"
              :class="{
                'text-left': noneShowItem,
                'mini-width': !defaultModel,
              }"
              v-if="itemConfigure.includes('time')"
            >
              {{ item.createTime }}
            </div>
          </div>
        </div>
      </div>
      <EmptyCon :tip="emptyword" v-else></EmptyCon>
    </AutoContainer>
    <we-popover
      ref="MobilePop"
      popper-class="todoTaskPop"
      placement="bottom"
      width="175"
    >
      <div class="MobilePop">
        <div class="portal-font-color-lv3 ellipsis">
          {{ $Lan("public", "mobileUrl", "当前为移动地址扫一扫查看") }}
        </div>
        <div class="popQrcode portal-primary-border-color-lv3">
          <div :id="`${listId}Qrcode`" ref="qrcode2"></div>
        </div>
        <div
          class="popCopy portal-primary-color-lv1 portal-primary-backgroundcolor-lv5 ellipsis"
          @click="copyLink"
        >
          <i class="iconfont icon-lianjie" style="font-size: inherit"></i>
          {{ $Lan("public", "copyUrl", "复制链接") }}
        </div>
      </div>
    </we-popover>
  </div>
</template>

<script>
import QRCode from "qrcodejs2";
export default {
  props: {
    router: Object,
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
    lanFunName: {
      type: String,
      default: "",
    },
    card: {
      type: Object,
      default: () => {
        return {};
      },
    },
    showFavorite: String
  },

  computed: {
    showItemNum() {
      let num = 0;
      this.itemConfigure.includes("time") && num++;
      this.itemConfigure.includes("level") && num++;
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
  mounted () {
    document.addEventListener("click", this.handleDocumentClick);
    document.addEventListener('scroll', this.handleScroll, true);
  },
  beforeDestroy () {
    document.removeEventListener("click", this.handleDocumentClick);
    document.removeEventListener("scroll", this.handleScroll);
    this.reference = null;
  },
  methods: {
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
            taskId: item.taskId
          },
        },
        startTime: new Date().getTime(),
      });
    },
    disabledToolTip(item, field) {
      return !item[field] || item[field] === "-";
    },
    move(row, e) {
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
      row.isRead = 1;
      window.shell
        .execCardMethod({
          ...this.card,
          method: "updateTaskReadStatus",
          param: {
            taskId: row.taskId || "",
            isRead: 1,
          },
        })
        .then((data) => {
          console.log(data);
        });
      if (row.formUrl) {
        window.shell.openPage(row.formUrl, 1, 2);
      } else if (!row.formUrl && row.formMobileUrl) {
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
    },
    // showFlowChart(item) {
    //   if (
    //     this.itemConfigure.includes("flowchart") &&
    //     item.processInstanceImageUrl
    //   ) {
    //     return true;
    //   } else {
    //     return false;
    //   }
    // },

    // move2(val) {

    //   window.shell.openPage(val.processInstanceImageUrl, 1, 2);
    // },
  },
};
</script>

<style lang="less" scoped>
.todotask-list {
  /* background: skyblue; */
  // width: calc(100% - 140px); /* 减去左侧固定140px的任务栏宽度*/
  flex: 1;
  width: 0;

  .todotask-list-main {
    width: 100%;
    .icon-con {
      position: absolute;
      left: -9px;
      top: -11px;
      i {
        font-size: 32px !important;
      }

      &.icon1 {
        color: #ed1d12;
      }
      &.icon2 {
        color: #f3a300;
      }
    }
    .set-top-icon {
      width: 14px;
      height: 14px;
      background: url("../assets/setTop.svg") no-repeat center;
      background-size: 100% 100%;
      margin-left: 10px;
      flex-shrink: 0;
    }
  }

  .list-item {
    // height: 76px;
    // min-width: 360px;
    background: #ffffff;
    box-shadow: inset 0 -1px 0 0 #f1f2f4;
    overflow: hidden;
    padding: 14px 2px 14px 18px;
    position: relative;
    &:last-child {
      box-shadow: none;
    }
    .item-top,
    .item-bottom {
      align-items: center;
      display: flex;
      justify-content: space-between;

      /* min-width: 340px; */
    }

    .item-top {
      position: relative;
      height: 24px;
      line-height: 24px;
      margin-bottom: 2px;
      font-size: 16px;
      .unread {
        position: absolute;
        height: 6px;
        width: 6px;
        border-radius: 50%;
        background: #ff230c;
        left: -10px;
      }
      &.none-bootom {
        margin-bottom: 0px;
      }
      .tast-title {
        flex: 1;
        width: 0;
        display: flex;
        align-items: center;
      }
      .newTag {
        width: 28px;
        height: 14px;
        margin-left: 6px;
        flex-shrink: 0;
        background-size: 100% 100%;
        background-image: url("../assets/newTag.svg");
      }
    }
    .item-bottom {
      height: 22px; // 没有三个内容时间居左时不会有偏移
      line-height: 22px;
      font-size: 14px;
      .item-bottom-left {
        align-items: center;
        display: flex;
        flex: 1;
        position: relative;
        top: 1px;
        width: 0;
        .show-item {
          margin-right: 20px;
          width: 0;
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
      .creat-time {
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
</style>
<style lang="less">
.todoTaskPop {
  padding: 14px 12px 0 12px !important;
}
</style>
