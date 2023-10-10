<i18n>
{
  "en_US": {
    "emptyTip": "No doneTask"
  },
  "zh_CN": {
    "emptyTip": "暂无待办任务"
  }
}
</i18n>
<template>
  <div class="task-con" ref="taskCon">
    <AutoContainer :con-type="0">
      <div class="todo-task portal-font-color-lv1" v-if="datacount">
        <div class="todo-left portal-font-color-lv2" v-if="showSourceList">
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
                :style="item.count === '99+' ? morecount : ''"
                >{{ item.count }}</span
              >
            </div>
          </AutoContainer>
        </div>
        <div class="todo-right" ref="rt">
          <AutoContainer
            :con-type="type"
            :con-height="height"
            :scroll="rightScroll"
          >
            <div v-if="subList.length">
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
                    :content="item.priority === 1 ? '特急' : '紧急'"
                    placement="bottom-start"
                    :open-delay="800"
                  >
                    <div
                      v-if="item.priority"
                      class="icon-con"
                      :class="['icon' + item.priority]"
                    >
                      <i
                        class="iconfont icon-urgent"
                        style="font-size: 32px"
                      ></i>
                    </div>
                  </we-tooltip>
                </template>
                <!-- {{curwidth}} -->
                <div
                  class="item-top"
                  :class="{
                    'none-bootom':
                      noneShowItem && !itemConfigure.includes('time'),
                  }"
                >
                  <we-tooltip
                    class="tast-title text-ellipsis portal-font-color-lv1"
                    effect="dark"
                    :class="{
                      'portal-primary-color-hover-lv1': item.formUrl,
                    }"
                    placement="bottom-start"
                    :open-delay="800"
                  >
                    <div slot="content">{{ item.subject }}</div>
                    <div @click="move(item)">
                      {{ item.subject }}
                    </div>
                  </we-tooltip>
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
                      :disabled="disabledToolTip(item,'bizDomain')"
                    >
                      <div slot="content">{{ item.bizDomain || "-" }}</div>
                      <div class="source text-ellipsis">
                        所属应用: {{ item.bizDomain || "-" }}
                      </div>
                    </we-tooltip>
                    <we-tooltip
                      v-if="itemConfigure.includes('scene') && taskSource === 2"
                      class="show-item text-ellipsis"
                      effect="dark"
                      placement="bottom-start"
                      :open-delay="800"
                      :disabled="disabledToolTip(item,'source_NAME')"
                    >
                      <div slot="content">{{ item.source_NAME || "-" }}</div>
                      <div class="source text-ellipsis">
                        业务场景: {{ item.source_NAME || "-" }}
                      </div>
                    </we-tooltip>
                    <we-tooltip
                      v-if="itemConfigure.includes('author')"
                      class="show-item text-ellipsis"
                      effect="dark"
                      placement="bottom-start"
                      :open-delay="800"
                      :disabled="disabledToolTip(item,'author')"
                    >
                      <div slot="content">{{ item.author || "-" }}</div>
                      <div class="person text-ellipsis">
                        发起人：{{ item.author || "-" }}
                      </div>
                    </we-tooltip>
                    <we-tooltip
                      v-if="itemConfigure.includes('dept')"
                      class="show-item text-ellipsis"
                      effect="dark"
                      placement="bottom-start"
                      :open-delay="800"
                      :disabled="disabledToolTip(item,'createdByDepts')"
                    >
                      <div slot="content">
                        {{ item.createdByDepts || "-" }}
                      </div>
                      <div class="dept">
                        发起部门：{{ item.createdByDepts || "-" }}
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
            <EmptyCon :tip="emptyword" :height="500" v-else></EmptyCon>
          </AutoContainer>
        </div>
      </div>
      <EmptyCon
        :tip="$t('emptyTip')"
        :height="500"
        v-else
        style="flex: 1"
      ></EmptyCon>
    </AutoContainer>
  </div>
</template>

<script>
export default {
  name: "todatask",
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
      conScroll: { barKeepShow: true, scrollingX: true, scrollingY: false },
      leftScroll: { barKeepShow: true },
      rightScroll: { barKeepShow: true, scrollingX: false },
      showSourceList: "",
      emptyword: "",
      datacount: 0,
      // leftcount: null,
      height: 500, //容器高度，type为1时该值无效，type为2时最大高度，type为3时固定高度
      type: 2, //1：代表自适应（无最大限制）2：代表自适应（有最大限制）3：固定高度
      sourceList: [],
      subList: [],
      selected: "",
      itemConfigure: [],
      // lctcansee: null,
      // mWid: null,
      // nowwidth: null,
      defaultModel: true,
      taskSource: 1,
      cId: "",
      loadIng: false,
      // cStyle0: "",
    };
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
  created() {
    this.getData();
  },
  mounted() {},
  beforeDestroy() {},
  methods: {
    disabledToolTip(item,field){
        return !item[field]||item[field]==='-'
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
    move(val) {
      if (val.formUrl) {
        window.shell.openPage(val.formUrl, 1, 2);
      }
    },

    getData(sourcewid) {
      this.loadIng = true;
      window.shell
        .execCardMethod({
          // cardId: "CUS_CARD_TODOTASK",
          // cardWid: "123456",
          ...this.card,
          method: "render",
          param: {
            sourceWid: sourcewid || "",
          },
        })
        .then((data) => {
          this.initCon();
          if (data.errcode == 0) {
            if (!data.data) {
              this.datacount = 0;
            } else {
              this.loadIng = false;
              this.datacount =
                (data.data.sourceList && data.data.sourceList.length) ||
                (data.data.taskInfo && data.data.taskInfo.length);

              if (!data?.data?.taskInfo?.length && !this.cId) {
                this.emptyword =
                  "暂无" + data.data.sourceList[0]?.source_NAME ||
                  "" + "待办任务";
              }
              this.sourceList = data.data.sourceList;
              if (this.datacount) {
                let config = data.data.config; // 管控台配置数据
                this.type = config.containerType.type; // 高度类型
                this.height = config.containerType.value; // 卡片高度
                this.showSourceList =
                  config.sourceList === 1 && this.sourceList.length > 0; // 左侧list是否显示: 1显示，0不显示

                this.itemConfigure = config.itemConfigure;
                this.taskSource = parseInt(data.data.taskSource) || 1;
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
      this.cId = item.wid;
      if (this.selected !== item.wid) {
        this.selected = item.wid;
        this.getData(item.wid);
      }
      if (item.count == 0) {
        this.emptyword = "暂无" + item.source_NAME + "已办任务";
      }
      // this.currentcount = item.count;
    },
  },
};
</script>

<style lang="less"  scoped >
.text-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.task-con {
  width: 100%;
  position: relative;
  /deep/.todo-task {
    min-width: 360px;
    width: 100%;
    display: flex;
    white-space: nowrap; /** 不换行*/
    position: relative;
    /* min-width: 580px; */
  }
}
.icon-con {
  position: absolute;
  left: -9px;
  top: -11px;
  &.icon1 {
    color: #ed1d12;
  }
  &.icon2 {
    color: #f3a300;
  }
}

.todo-left {
  width: 162px;
  // min-width: 140px;
  background: #ffffff;
  box-shadow: inset -1px 0 0 0 #f1f2f4;
  .tword {
    overflow: hidden;
    display: inline-block;
    width: 120px;
    white-space: nowrap;
    text-overflow: ellipsis;
    padding-left: 12px;
    box-sizing: border-box;
  }
  .item-count {
    position: absolute;
    font-size: 14px;
    right: 12px;
  }
  .tab-item {
    // font-family: PingFangSC;
    font-size: 16px;
    /* color: #707d8f; */
    letter-spacing: 0;
    text-align: justify;
    width: 100%;
    height: 48px;
    line-height: 48px;
    position: relative;
    cursor: pointer;
    &.active {
      background: #f6f6f8;
      border-radius: 3px;
      font-weight: bold;
      // color: #102645; /**word-lv1的颜色，写死 */
      // &::before {
      //   content: " ";
      //   width: 3px;
      //   height: 30px;
      //   margin: 9px 0;
      //   position: absolute;
      //   background: #102645; /**word-lv1的颜色，写死 */
      // }
    }
  }
}
.todo-right {
  flex: 1;
  width: 0;
  .list-item {
    // height: 76px;
    // min-width: 360px;
    background: #ffffff;
    box-shadow: inset 0 -1px 0 0 #f1f2f4;
    cursor: default;
    overflow: hidden;
    padding: 14px 2px 14px 12px;
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
      height: 24px;
      line-height: 24px;
      margin-bottom: 2px;
      font-size: 16px;
      &.none-bootom {
        margin-bottom: 0px;
      }
      .tast-title {
        flex: 1;
        width: 0;
        &.portal-primary-color-hover-lv1 {
          cursor: pointer;
        }
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
}
</style>
