<template>
  <div class="task-con dz-done-task" ref="taskCon" v-loading="loading">
    <AutoContainer :con-type="0" v-if="datacount">
      <div class="donetask">
        <SourceList :router="router" v-if="datacount && showSourceList" v-bind="{
          conType: type,
          conHeight: (height - 40),
          sourceList,
          lanFunName,
        }" @change="sourceChange" />
        <TaskList :router="router" v-show="datacount" v-bind="{
          conType: type,
          conHeight: height,
          emptyword,
          lanFunName,
          itemConfigure,
          taskSource,
          subList,
          defaultModel,
          showFavorite,
          total,
          pageSize
        }"  @handlePage="handlePage"/>
      </div>
     
    </AutoContainer>
    <EmptyCon :tip="$Lan(lanFunName, 'tipMsg', '暂无已办任务')" v-if="!datacount" :height="height" style="flex: 1"></EmptyCon>
  </div>
</template>

<script>
import SourceList from "./components/SourceList.vue";
import TaskList from "./components/TaskList.vue";
import { initTrack } from "./mixins/track.js";
export default {
  name: "donetask",
  components: { SourceList, TaskList },
  props: {
    index: Number,
    router: Object,
  },
  mixins: [initTrack],
  watch: {},
  data() {
    const { cardWid, cardId } = this.router;
    return {
      card: {
        cardWid,
        cardId,
      },
      lanFunName: this.router.cardId,
      conScroll: { barKeepShow: true, scrollingX: true, scrollingY: false },
      showSourceList: true,
      defaultModel: false,
      // len: document.getElementsByClassName("lctword").length,
      emptyword: "",
      datacount: null,
      leftcount: null,
      currentcount: null,
      height: 500, //容器高度，type为1时该值无效，type为2时最大高度，type为3时固定高度
      type: "3", //1：代表自适应（无最大限制）2：代表自适应（有最大限制）3：固定高度
      sourceList: [],
      subList: [],
      // cId: "",
      loading: false,
      itemConfigure: [],
      taskSource: 1,
      showFavorite: '0',
      doneListRouter: {
        cardId: "SYS_CARD_DONETASKLIST",
        cardWid: "5166769946361796"
      },
      pageNum: 1,
      pageSize: 6,
      total: 1,
      sourcewid: ""
    };
  },
  created() {
    this.getConfig()
    // this.getData();
    window.shell.on("collectTask", this.handleUpdateFavorite);
  },

  mounted() {
    this.initCon();
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
            this.doneListRouter.cardWid = parseData?.donelistWid?.[0]?.langValue || '5166769946361796';
            this.pageSize = parseData.rows;
            this.getData(true);
          }
        }
      })
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
    initCon() {
      this.$nextTick(() => {
        let nowwidth = this.$refs.taskCon.offsetWidth;
        if (nowwidth <= 360) {
          this.defaultModel = false;
        } else {
          this.defaultModel = true;
        }
        console.log("defaultModel", nowwidth);
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
            ...this.doneListRouter,
            method: "renderData",
            param: {
              endTime: "",
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              sceneCode: this.sourcewid,
              startTime: "",
              subject: "",
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

    getData(init, fromRefresh) {
      this.loading = true;
      window.shell
        .execCardMethod({
          ...this.card,
          method: "render",
          param: {
            sourceWid: this.sourcewid || "",
          },
        })
        .then((data) => {
          this.$nextTick(() => {
            this.loading = false;
            this.loadedEndTrack(); // 加载结束埋点
          });
          if (init) {
            // 初始化
            if (data?.data?.sourceList?.length) {
              this.sourcewid = data?.data?.sourceList[0].wid
            } else {
              this.sourcewid = ""
            }
          }
          // 获取已办任务列表数据
          window.shell.execCardMethod({
            ...this.doneListRouter,
            method: "renderData",
            param: {
              endTime: "",
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              sceneCode: this.sourcewid,
              startTime: "",
              subject: "",
              _t: new Date().getTime()
            }
          }, (done) => {
            this.originData = JSON.stringify(done.data.datas);
            this.total = done.data.total;
            // this.originData = JSON.stringify(data.data);
            if (data.errcode == 0) {
              this.height = data.data.config.containerType.value || 500;
              this.refreshRate = data.data.config?.refreshRate;
              this.showFavorite = data.data.config?.showFavorite || '0';
              if (!data.data) {
                this.datacount = 0;
              } else {
                this.datacount =
                  data?.data?.sourceList?.length ||
                  done.data.datas?.length ||
                  0;
                this.sourceList = data?.data?.sourceList || [];
                let currentcount = done.data.datas?.length || 0;
                if (!currentcount) {
                  this.emptyword = this.$Lan(
                    this.lanFunName,
                    "tipMsgName",
                    "暂无{name}已办任务",
                    { name: data.data.sourceList[0]?.source_NAME || "" }
                  );
                }

                this.taskSource = parseInt(data.data.taskSource) || 1;
                if (this.datacount) {
                  let config = data.data.config; // 管控台配置数据
                  this.type = config.containerType.type; // 高度类型
                  this.height = config.containerType.value || 500; // 卡片高度
                  this.showSourceList =
                    config.sourceList === 1 && this.sourceList.length; // 左侧list是否显示: 1显示，0不显示
                  this.itemConfigure = config.itemConfigure;
                  this.subList = done.data.datas || [];
                }
                //  window.console.log(this.subList)
              }
            } else {
              this.$message({
                showClose: false,
                message: this.$Lan(
                  this.lanFunName,
                  "message",
                  "获取任务中心错误"
                ),
                type: "error",
              });
            }
            // 定时更新触发的无需重新计时，其他操作触发的需重新计时
            !fromRefresh && this.handleTimedRefresh(this.sourcewid);
          })
        });
    },

    sourceChange(item) {
      // this.currentcount = 0;
      // this.currentcount =parseInt(item.count);
      this.pageNum = 1;
      this.sourcewid = item.wid
      this.getData(false);

      // this.getData(item.wid);
      if (item.count == 0) {
        this.emptyword = this.$Lan(
          this.lanFunName,
          "tipMsgName",
          "暂无{name}已办任务",
          { name: item.source_NAME }
        );
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
.dz-done-task {
  box-shadow: 0.125rem 0.5rem 0.625rem 0rem rgba(0, 0, 0, 2%), -0.0625rem 0rem 0.625rem 0rem rgba(0, 0, 0, 2%);
  border-radius: 0rem 0rem 0.5rem 0.5rem;
}
.donetask {
  min-width: 360px;
  /* overflow-x: auto; */
  /* border: 1px solid orange; */
  width: 100%;
  display: flex;
  overflow: hidden;
  white-space: nowrap;
  /** 不换行*/
  position: relative;
}

</style>
