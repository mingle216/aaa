<template>
  <div class="mytask-list">
    <AutoContainer :con-type="conType" :con-height="conHeight" :scroll="rightScroll">
      <div class="mytask-list-main" v-if="subList.length">
        <div v-for="(item, index) in subList" class="list-item" :key="item.appId + item.taskId + index">
          <!-- {{curwidth}} -->
          <div class="item-top" :class="{
            'none-bootom': noneShowItem && !itemConfigure.includes('time'),
          }">
            <div class="title-con">
              <we-tooltip class="tast-title text-ellipsis portal-font-color-lv1" :class="{
                'has-point': itemConfigure.includes('point'),
              }" effect="dark" placement="bottom-start" :open-delay="800">
                <div slot="content">{{ item.subject }}</div>
                <div class="portal-primary-color-hover-lv1" @click="e => move(item, e)" style="cursor: pointer;">
                  {{ item.subject }}
                </div>
              </we-tooltip>
              <div class="state-con" v-if="itemConfigure.includes('point')">
                <div class="state-tag st1" v-if="item.processInstanceStatus === 'COMPLETE'">
                  {{ $Lan(lanFunName, "COMPLETE", "已完成") }}
                </div>
                <div class="state-tag st2" v-if="item.processInstanceStatus === 'RUNNING'">
                  {{ $Lan(lanFunName, "RUNNING", "待处理") }}
                </div>
                <div class="state-tag st3" v-if="item.processInstanceStatus === 'ABORT'">
                  {{ $Lan(lanFunName, "ABORT", "已撤销") }}
                </div>
              </div>
            </div>
          </div>
          <div class="item-bottom portal-font-color-lv3" v-if="!noneShowItem || itemConfigure.includes('time')">
            <div class="item-bottom-left" v-if="!noneShowItem">
              <we-tooltip v-if="itemConfigure.includes('source')" class="item c1" effect="dark" placement="bottom-start"
                :open-delay="800" :disabled="disabledToolTip(item, 'bizDomain')">
                <div slot="content">{{ item.bizDomain || "-" }}</div>
                <div class="source">
                  <template v-if="defaultModel">
                    {{ $Lan(lanFunName, "bizDomain", "所属应用") }}:
                  </template>
                  {{ item.bizDomain || "-" }}
                </div>
              </we-tooltip>

              <we-tooltip v-if="itemConfigure.includes('handler')" class="item c2" effect="dark" placement="bottom-start"
                :open-delay="800" :disabled="disabledToolTip(item, 'processors')">
                <div slot="content">
                  {{ item.processors?.[0] || "-" }}
                </div>
                <div class="source">
                  <template v-if="defaultModel">
                    {{ $Lan(lanFunName, "assignNames", "当前待处理人") }}：
                  </template>
                  {{ item.processors?.[0] || "-" }}
                </div>
              </we-tooltip>
              <we-tooltip v-if="itemConfigure.includes('assignDepts')" class="item c2" effect="dark"
                placement="bottom-start" :open-delay="800" :disabled="disabledToolTip(item, 'assignDepts')">
                <div slot="content">
                  {{ item.processorDeptList?.[0] || "-" }}
                </div>
                <div class="source">
                  <template v-if="defaultModel">
                    {{ $Lan(lanFunName, "assignDepts", "当前处理人所属部门") }}：
                  </template>
                  {{ item.processorDeptList?.[0] || "-" }}
                </div>
              </we-tooltip>
            </div>
            <div class="time text-ellipsis" :class="{
              'text-left': noneShowItem,
              'mini-width': !defaultModel,
            }" v-if="itemConfigure.includes('time')">
              {{ item.createTime }}
            </div>
          </div>
        </div>
      </div>
      <EmptyCon :tip="emptyword" v-if="!subList.length"></EmptyCon>
    </AutoContainer>
    <we-popover ref="MobilePop" popper-class="myTaskPop" placement="bottom" width="175">
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
    lanFunName: {
      type: String,
      default: "CUS_CARD_CUSMYTASK",
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
    total: {
      type: Number,
      default: 0
    },
    pageSize: {
      type: Number,
      default: 6
    }
  },

  computed: {
    noneShowItem() {
      if (
        this.itemConfigure.includes("source") ||
        this.itemConfigure.includes("handler")
      ) {
        return false;
      } else {
        return true;
      }
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
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
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
    },
  },
};
</script>

<style lang="less" scoped>
.mytask-list {
  flex: 1;
  width: 0;
  position: relative;

  .mytask-list-main {
    width: 100%;
    padding: 0 14px;

    // min-width: 360px;
    .list-item {
      position: relative;
      // height: 76px;
      background: #ffffff;
      box-shadow: inset 0 -1px 0 0 #f1f2f4;

      // font-size: 12px;
      /* font-family: PingFangSC; */
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
    line-height: 24px;
    font-size: 16px;
    margin-bottom: 2px;

    &.none-bootom {
      margin-bottom: 0px;
    }
  }

  .item-bottom {
    height: 22px;
    line-height: 22px;
    font-size: 14px;
    /* color: #707d8f; */
    /* overflow: hidden; */
  }

  .tast-title {
    &.has-point {
      max-width: calc(100% - 65px);
    }
  }

  .title-con {
    // min-width: 198px;
    width: 0;
    flex: 1;
    display: flex;
    align-items: center;
  }

  .state-con {
    white-space: nowrap;
    margin-left: 8px;
    margin-right: 5px;
    position: relative;
    top: -1px;

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
        // width: 44px;
        // min-width: 44px;
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
        background: rgba(191, 191, 191, 0.1);
        border: 1px solid #979797;
        color: #8c8c8c;
      }
    }
  }

  .item-bottom-left {
    /* border: 1px solid red; */
    align-items: center;
    display: flex;
    // min-width: 108px;
    width: 0;
    flex: 1;
    /* max-width: 384px; */
    // max-width: 70%;
    // width: calc(100% - 105px);
    position: relative;
  }

  .item-bottom-left>div {
    /* background: skyblue; */
    min-width: 48px;
    /* width: 33.33%; */
  }

  .c1 {
    min-width: 48px;
    /* background: pink; */
    width: 0;
    flex: 3;
  }

  .c2 {
    /* background: skyblue; */
    min-width: 68px;
    width: 0;
    flex: 7;
  }

  .item-bottom-left .item {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .source,
  .person {
    margin-right: 20px;
  }

  .time {
    // position: absolute;
    /* right: 0px; */
    // min-width: 102px;
    width: 140px;
    text-align: right;

    &.mini-width {
      width: 50px;
    }

    &.text-left {
      flex: 1;
      text-align: left;
    }
  }

  .lct {
    position: absolute;
    right: 2px;
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
.myTaskPop {
  padding: 14px 12px 0 12px !important;
}
</style>
