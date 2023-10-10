<i18n>
{
  "en_US": {
    "emptyTip": "No myTask"
  },
  "zh_CN": {
    "emptyTip": "暂无我申请的任务"
  }
}
</i18n>
<template>
  <div class="task-con" ref="taskCon">
    <AutoContainer :con-type="0">
      <div class="mytask portal-font-color-lv1">
        <div class="left portal-font-color-lv2" v-if="datacount && showSourceList">
          <AutoContainer
            :con-type="type"
            :con-height="height"
            :scroll="leftScroll"
          >
            <div
              v-for="item in sourceList"
              class="tab-item"
              :key="item.wid"
              :class="[{ active: item.wid === selected }]"
              @click="getList(item)"
            >
              <span
                class="tword"
                :class="[{ 'portal-primary-color-lv1': item.wid === selected }]"
                >{{ item.source_NAME }}</span
              >
              <span
                class="item-count portal-font-color-lv3"
                :style="item.count === '99+' ? 'right: 1px;' : ''"
                >{{ item.count }}</span
              >
            </div>
          </AutoContainer>
        </div>
        <div class="right" v-if="datacount">
          <AutoContainer
            :con-type="type"
            :con-height="height"
            :scroll="rightScroll"
          >
            <div class="right-main" v-if="currentcount">
              <div
                v-for="(item, index) in subList"
                class="list-item"
                :key="item.appId + item.taskId + index"
              >
                <!-- {{curwidth}} -->
                <div
                  class="item-top"
                  :class="{
                    'none-bootom':
                      noneShowItem && !itemConfigure.includes('time'),
                  }"
                >
                  <div class="title-con">
                    <we-tooltip
                      class="tast-title text-ellipsis portal-font-color-lv1"
                      :class="{
                        'has-point': itemConfigure.includes('point'),
                        'portal-primary-color-hover-lv1': item.formUrl,
                      }"
                      effect="dark"
                      placement="bottom-start"
                      :open-delay="800"
                    >
                      <div slot="content">{{ item.subject }}</div>
                      <div @click="move(item)">
                        {{ item.subject }}
                      </div>
                    </we-tooltip>
                    <div
                      class="state-con"
                      v-if="itemConfigure.includes('point')"
                    >
                      <div
                        class="state-tag st1"
                        v-if="item.processStatus === 'COMPLETE'"
                      >
                        已完成
                      </div>
                      <div
                        class="state-tag st2"
                        v-if="item.processStatus === 'RUNNING'"
                      >
                        待处理
                      </div>
                      <div
                        class="state-tag st1"
                        v-if="item.processStatus === 'ABORT'"
                      >
                        已撤销
                      </div>
                    </div>
                  </div>
  
                </div>
                <div
                  class="item-bottom portal-font-color-lv3"
                  v-if="!noneShowItem || itemConfigure.includes('time')"
                >
                  <div class="item-bottom-left" v-if="!noneShowItem">
                    <we-tooltip
                      v-if="itemConfigure.includes('source')"
                      class="item c1"
                      effect="dark"
                      placement="bottom-start"
                      :open-delay="800"
                      :disabled="disabledToolTip(item,'bizDomain')"
                    >
                      <div slot="content">{{ item.bizDomain || "-" }}</div>
                      <div class="source">
                        所属应用: {{ item.bizDomain || "-" }}
                      </div>
                    </we-tooltip>

                    <we-tooltip
                      v-if="itemConfigure.includes('handler')"
                      class="item c2"
                      effect="dark"
                      placement="bottom-start"
                      :open-delay="800"
                      :disabled="disabledToolTip(item,'assignNames')"
                    >
                      <div slot="content">
                        {{ item.assignNames || "-" }}
                      </div>
                      <div class="source">
                        当前待处理人：{{ item.assignNames || "-" }}
                      </div>
                    </we-tooltip>
                  </div>
                  <div
                    class="time text-ellipsis"
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
            <EmptyCon
              :tip="emptyword"
              :height="500"
              v-if="!currentcount"
            ></EmptyCon>
          </AutoContainer>
        </div>
      </div>
      <EmptyCon
        :tip="$t('emptyTip')"
        :height="500"
        v-if="!datacount"
        style="flex: 1"
      ></EmptyCon>
    </AutoContainer>
  </div>
</template>

<script>
export default {
  name: "mytask",
  components: {},
  props: {
    index: Number,
    router: Object,
  },
  watch: {},
  data() {
    const { cardWid, cardId } = this.router;
    return {
      card: {
        cardWid,
        cardId,
      },
      // morecount: "right: 1px;",
      // rightClass3: "calc(100% - 140px);",
      // rightcansee3: "",
      conScroll: { barKeepShow: true, scrollingX: true, scrollingY: false },
      leftScroll: { barKeepShow: true },
      rightScroll: { barKeepShow: true, scrollingX: false },
      // len: document.getElementsByClassName("lctword").length,
      emptyword: "",
      datacount: null,
      leftcount: null,
      currentcount: null,
      height: "500", //容器高度，type为1时该值无效，type为2时最大高度，type为3时固定高度
      type: "3", //1：代表自适应（无最大限制）2：代表自适应（有最大限制）3：固定高度
      sourceList: [],
      subList: [],
      selected: "",
      defaultModel: true,
      // timeFlag: false,
      cId: "",
    };
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
  created() {
    this.getData();
  },
  mounted() {},
  beforeDestroy() {},
  methods: {
    disabledToolTip(item,field){
        return !item[field]||item[field]==='-'
    },
    move(val) {
      if (val.formUrl) {
        window.shell.openPage(val.formUrl, 1, 2);
      }
    },
    initCon() {
      this.$nextTick(() => {
        let nowwidth = this.$refs.taskCon.offsetWidth;
        if (nowwidth < 528) {
          this.defaultModel = false;
        } else {
          this.defaultModel = true;
        }
      });
    },
    getData(sourcewid) {
      window.shell
        .execCardMethod({
          // cardId: "SYS_CARD_MYTASK",
          // cardWid: "123456",
          ...this.card,
          method: "render",
          param: {
            sourceWid: sourcewid,
          },
        })
        .then((data) => {
          // console.log(data.data.taskInfo);
          this.initCon();
          if (data.errcode == 0) {
            // data.data = 0
            if (!data.data) {
              // this.timeFlag = true;
              this.datacount = false;
            } else {
              this.datacount = data?.data?.sourceList?.length || data?.data?.taskInfo?.length|| 0;
              this.currentcount = data?.data?.taskInfo?.length || 0;

              if (
                data?.data?.sourceList?.length &&
                !data?.data?.taskInfo?.length &&
                !this.cId
              ) {
                this.emptyword =
                  "暂无" + data.data.sourceList[0]?.source_NAME ||
                  "" + "我申请的任务";
              }
              this.sourceList = data.data.sourceList;
              
              if (this.datacount) {
                let config = data.data.config; // 管控台配置数据
                this.type = config.containerType.type; // 高度类型
                this.height = config.containerType.value; // 卡片高度
                this.showSourceList = config.sourceList === 1&&this.sourceList.length>0; // 左侧list是否显示: 1显示，0不显示
                this.itemConfigure = config.itemConfigure;
                
                this.subList = data.data.taskInfo;
                
              }
              //  window.console.log(this.subList)
              if (this.sourceList.length && this.selected === "") {
                this.selected = this.sourceList[0].wid;
              }
            }
          } else {
            this.$message({
              showClose: false,
              message: "获取任务中心错误",
              type: "error",
            });
          }
        });
    },
    getList(item) {
      // console.log(item);
      this.cId = item.wid;
      this.currentcount = 0;
      if (this.selected !== item.wid) {
        this.selected = item.wid;
        this.getData(item.wid);
      }
      if (item.count == 0) {
        this.emptyword = "暂无" + item.source_NAME + "我申请的任务";
      }
      this.currentcount = item.count;
    },
  },
};
</script>

<style lang="less" scoped>
.text-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.tword {
  overflow: hidden;
  display: inline-block;
  width: 120px;
  white-space: nowrap;
  text-overflow: ellipsis;

  padding-left: 12px;
  box-sizing: border-box;
}
.mytask {
  min-width: 360px;
  width: 100%;
  display: flex;
  overflow: hidden;
  position: relative;
  .icon1 {
    color: #ed1d12;
    position: absolute;
    left: -9px;
    top: -11px;
  }
  .icon2 {
    color: #f3a300;
    position: absolute;
    left: -9px;
    top: -11px;
  }
  .left {
    /* background: pink; */
    width: 162px;
    // min-width: 140px;
    background: #ffffff;
    box-shadow: inset -1px 0 0 0 #f1f2f4;
  }
  .right {
    /* background: skyblue; */
    width: 0; /* 减去左侧固定140px的任务栏宽度*/
    flex: 1;
  }

  .list-item {
    // height: 76px;

    background: #ffffff;
    box-shadow: inset 0 -1px 0 0 #f1f2f4;
    cursor: default;
    font-size: 12px;
    /* font-family: PingFangSC; */
    overflow: hidden;
    padding: 14px 3px 14px 12px;
    position: relative;
    &:last-child {
      box-shadow: none;
    }
  }
  .tab-item {
    /* font-family: PingFangSC; */
    font-size: 16px;
    /* color: #707d8f; */
    letter-spacing: 0;
    text-align: justify;
    width: 100%;
    height: 48px;
    line-height: 48px;
    position: relative;
    cursor: pointer;
  }
  .active {
    background: #f6f6f8;
    border-radius: 3px;
    font-weight: bold;
  }

  .item-count {
    position: absolute;
    font-size: 14px;
    right: 12px;
  }
  .item:focus {
    outline: none;
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
    &.portal-primary-color-hover-lv1 {
      cursor: pointer;
    }
    &.has-point {
      max-width: calc(100% - 76px);
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
  .item-bottom-left > div {
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
  .link-icon-font {
    background-color: transparent;
    border-radius: 50%;
    font-size: 64px;
  }

  .link-text {
    margin-top: 10px;
    height: 35px;
    font-size: 13px;
    text-overflow: ellipsis;
    word-break: break-all;
    overflow: hidden;
    white-space: nowrap;
    text-align: center;
  }

  .link-div {
    display: flex;
    justify-content: center;
    align-items: center;
    color: #9aa1b0;
  }

  .link-inner-div {
    cursor: pointer;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
}
</style>
