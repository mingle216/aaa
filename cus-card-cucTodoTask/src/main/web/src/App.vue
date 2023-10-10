<template>
  <div class="task-con dz-todo-task" ref="taskCon" v-loading="loadIng">
    <AutoContainer :con-type="0" v-if="datacount">
      <div class="todo-task portal-font-color-lv1" v-if="datacount">
        <SourceList v-if="datacount && showSourceList" v-bind="{
          conType: type,
          conHeight: (height - 40),
          sourceList,
          defaultModel,
          lanFunName,
          router: router,
        }" @change="sourceChange" />
        <TaskList v-show="datacount" v-bind="{
          conType: type,
          conHeight: height,
          emptyword,
          itemConfigure,
          taskSource,
          subList,
          defaultModel,
          lanFunName,
          card,
          router: router,
          showFavorite,
          total,
          pageSize
        }" @handlePage="handlePage" />
      </div>

    </AutoContainer>
    <EmptyCon :tip="$Lan(lanFunName, 'emptyTip', '暂无待办任务')" v-if="!datacount" :height="height" style="flex: 1"></EmptyCon>
  </div>
</template>

<script>
import SourceList from "./components/SourceList.vue";
import TaskList from "./components/TaskList.vue";
export default {
  name: "todatask",
  components: { SourceList, TaskList },
  props: {
    index: Number,
    router: Object,
  },
  watch: {},
  data() {
    const { cardWid, cardId } = this.router;
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      card: {
        cardWid,
        cardId,
      },
      lanFunName: this.router.cardId,
      conScroll: { barKeepShow: true, scrollingX: true, scrollingY: false },
      showSourceList: "",
      emptyword: "",
      datacount: 0,
      // leftcount: null,
      height: 500, //容器高度，type为1时该值无效，type为2时最大高度，type为3时固定高度
      type: 3, //1：代表自适应（无最大限制）2：代表自适应（有最大限制）3：固定高度
      sourceList: [],
      subList: [],

      itemConfigure: [],
      // lctcansee: null,
      // mWid: null,
      // nowwidth: null,
      defaultModel: true,
      taskSource: 1,
      //  currentcount:0,
      loadIng: false,
      showFavorite: '0',
      todoListRouter: {
        cardId: "SYS_CARD_TODOTASKLIST",
        cardWid: "21903842819026664"
      },
      pageNum: 1,
      pageSize: 6,
      total: 1,
      sourcewid: ""
    };
  },
  computed: {},
  created() {
    // this.getData();
    this.getConfig();
  },
  mounted() {
    this.initCon();

    window.shell.on("collectTask", this.handleUpdateFavorite);
  },
  beforeDestroy() {
    window.shell.off("collectTask", this.handleUpdateFavorite);
  },
  methods: {
    handlePage(page) {
      this.pageNum = page;
      this.getData(false)
    },
    getConfig() {
      window.shell.execCardMethod({
        ...this.card,
        method: "getConfig"
      }, (data) => {
        if (data.errcode === '0') {
          const datas = data.data;
          if (typeof datas == 'string') {
            let parseData = JSON.parse(JSON.parse(datas));
            this.todoListRouter.cardWid = parseData?.todolistWid?.[0]?.langValue || '21903842819026664';
            this.pageSize = parseData.rows;
            this.getData(true);
          }
        }
      })
    },

    initCon() {
      this.$nextTick(() => {
        let nowwidth = this.$refs.taskCon.offsetWidth;
        if (nowwidth <= 360) {
          this.defaultModel = false;
        } else {
          this.defaultModel = true;
        }
      });
    },
    handleTimedRefresh(sourcewid) {
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
            ...this.todoListRouter,
            method: "renderData",
            param: {
              isExpire: "0",
              isIgnore: "0",
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              sceneCode: this.sourcewid,
              _t: new Date().getTime()
            },
          })
          .then((res) => {
            if (res.errcode == 0) {
              // 后端接口 createTime字段会变化，故需忽略该字段
              const tempData = JSON.parse(this.originData);
              if (tempData) {
                tempData.map((item) => {
                  delete item.createTime;
                  return item;
                });
              }
              if (res.data && res.data.datas) {
                res.data.datas.map((item) => {
                  delete item.createTime;
                  return item;
                });
              }
              // 数据不一致更新数据
              if (JSON.stringify(res.data.datas) !== JSON.stringify(tempData)) {
                this.getData(false, true);
                // 更新角标
                window.shell.emit("getMarkNumber", {
                  cardId: this.card.cardId,
                  cardWid: this.card.cardWid,
                });
              }
            }
          });
      }, parseInt(this.refreshRate * 1000));
      if (!sourcewid) {
        this.$once("hook:beforeDestroy", () => {
          window.clearInterval(this.refreshTimer);
          this.refreshTimer = null;
        });
      }
    },
    handleUpdateFavorite({ id, res }) {
      if (res.errcode === "0") {
        this.subList.some((element) => {
          if (element.taskId === id) {
            element.favorite = !element.favorite;
          }
        });
      }
    },
    getData(init, fromRefresh) {
      if (!this.sourcewid && !fromRefresh) {
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
      }

      this.loadIng = true;
      window.shell
        .execCardMethod({
          // cardId: "SYS_CARD_DONETASK",
          // cardWid: "123456",
          ...this.card,
          method: "render",
          param: {
            sourceWid: this.sourcewid || "",
          },
        })
        .then((data) => {
          this.$nextTick(() => {
            this.loadIng = false;
            if (!this.sourcewid) {
              window.minosStataCollect.loadEnd({
                listId: this.listId,
                endTime: new Date().getTime(),
              });
            }
          });
          if (init) {
            // 初始化
            if (data?.data?.sourceList?.length) {
              this.sourcewid = data?.data?.sourceList[0].wid
            } else {
              this.sourcewid = ""
            }
          }
          // 获取待办任务列表数据
          window.shell.execCardMethod({
            ...this.todoListRouter,
            method: "renderData",
            param: {
              isExpire: "0",
              isIgnore: "0",
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              sceneCode: this.sourcewid,
              _t: new Date().getTime()
            }
          }, (todo) => {
            if (todo.errcode === '0') {
              this.originData = JSON.stringify(todo.data.datas);
              // console.log(todo.data.datas,'todo.datas')
              this.total = todo.data.total;
              // this.originData = JSON.stringify(data.data);
              if (data.errcode == 0) {
                if (!data.data) {
                  this.datacount = 0;
                } else {
                  this.datacount =
                    data?.data?.sourceList?.length ||
                    todo.data.datas?.length ||
                    0;

                  if (!todo.data.datas?.length) {
                    this.emptyword = this.GetLanguageValue(
                      this.lanFunName,
                      "emptyName",
                      "暂无{name}待办任务",
                      { name: data.data.sourceList[0]?.source_NAME || "" }
                    );
                  }
                  this.sourceList = data?.data?.sourceList || [];
                  if (this.datacount) {
                    let config = data.data.config; // 管控台配置数据
                    this.type = config.containerType.type; // 高度类型
                    this.height = config.containerType.value || 500; // 卡片高度
                    this.showSourceList =
                      config.sourceList === 1 && this.sourceList.length > 0; // 左侧list是否显示: 1显示，0不显示

                    this.itemConfigure = config.itemConfigure || [];
                    this.taskSource = parseInt(data.data.taskSource) || 1;
                    this.subList = todo.data.datas || [];
                  }
                  this.height = data.data.config.containerType.value || 500;
                }
                this.refreshRate = data.data?.config?.refreshRate;
                this.showFavorite = data.data.config?.showFavorite || '0';
              } else {
                this.$message({
                  showClose: false,
                  message: this.GetLanguageValue(
                    this.card.cardId,
                    "message",
                    "获取任务中心错误"
                  ),
                  type: "error",
                });
              }
            }
            // 定时更新触发的无需重新计时，其他操作触发的需重新计时
            !fromRefresh && this.handleTimedRefresh(this.sourcewid);
          })
        });
    },

    sourceChange(item) {
      //  this.currentcount = parseInt( item.count);
      this.pageNum = 1;
      this.sourcewid = item.wid
      this.getData(false);

      if (item.count == 0) {
        this.emptyword = this.GetLanguageValue(
          this.card.cardId,
          "emptyName",
          "暂无{name}待办任务",
          { name: item.source_NAME }
        );
        // "暂无" + item.source_NAME + "待办任务";
      }
    },
  },
};
</script>
<style lang="less">
.text-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
<style lang="less" scoped>
.dz-todo-task {
  box-shadow: 0.125rem 0.5rem 0.625rem 0rem rgba(0, 0, 0, 2%), -0.0625rem 0rem 0.625rem 0rem rgba(0, 0, 0, 2%);
  border-radius: 0rem 0rem 0.5rem 0.5rem;
}

.task-con {
  width: 100%;
  position: relative;

  /deep/.todo-task {
    min-width: 360px;
    width: 100%;
    display: flex;
    white-space: nowrap;
    /** 不换行*/
    position: relative;
    /* min-width: 580px; */
  }
}
</style>
