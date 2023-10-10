<template>
  <div class="task-con dz-mytask" ref="taskCon" v-loading="loading">
    <AutoContainer :con-type="0" v-if="datacount">
      <div class="mytask portal-font-color-lv1">
        <SourceList v-if="datacount && showSourceList" v-bind="{
          conType: type,
          conHeight: (height - 40),
          sourceList,
          lanFunName,
          router: router,
        }" @change="sourceChange" />
        <TaskList v-show="datacount" v-bind="{
          conType: type,
          conHeight: height,
          emptyword,
          lanFunName,
          itemConfigure,
          defaultModel,
          subList,
          router: router,
          total,
          pageSize
        }" @handlePage="handlePage" />
      </div>
      <!-- <div class="pages" v-if="total > 0">
        <p class="portal-font-color-lv1">共{{ total }}条</p>
        <we-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize"
          @current-change="handlePage">
        </we-pagination>
      </div> -->
    </AutoContainer>
    <EmptyCon :tip="$Lan(lanFunName, 'emptyTip', '暂无我申请的任务')"  :height="height"  v-if="!datacount" style="flex: 1"></EmptyCon>
  </div>
</template>

<script>
import SourceList from "./components/SourceList.vue";
import TaskList from "./components/TaskList.vue";
export default {
  name: "mytask",
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
      // morecount: "right: 1px;",
      // rightClass3: "calc(100% - 140px);",
      // rightcansee3: "",
      conScroll: { barKeepShow: true, scrollingX: true, scrollingY: false },
      rightScroll: { barKeepShow: true, scrollingX: false },
      // len: document.getElementsByClassName("lctword").length,
      emptyword: "",
      datacount: null,
      // currentcount: 0,
      height: 500, //容器高度，type为1时该值无效，type为2时最大高度，type为3时固定高度
      type: 3, //1：代表自适应（无最大限制）2：代表自适应（有最大限制）3：固定高度
      sourceList: [],
      subList: [],
      itemConfigure: [],
      defaultModel: true,
      loading: false,
      myListRouter: {
        cardId: "SYS_CARD_MYTASKLIST",
        cardWid: "46360479910810093"
      },
      pageNum: 1,
      pageSize: 6,
      total: 1,
      sourcewid: ""
      // timeFlag: false,
      // cId: "",
    };
  },
  computed: {},
  created() {
    this.getConfig();
  },
  mounted() {
    this.initCon();
  },
  beforeDestroy() { },
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
            this.myListRouter.cardWid = parseData?.mylistWid?.[0]?.langValue || '46360479910810093';
            this.pageSize = parseData.rows;
            this.getData(true);
          }
        }
      })
    },
    disabledToolTip(item, field) {
      return !item[field] || item[field] === "-";
    },
    move(val) {
      if (val.formUrl) {
        window.shell.openPage(val.formUrl, 1, 2);
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
            ...this.myListRouter,
            method: "renderData",
            param: {
              endTime: "",
              processInstanceStatus: "",
              startTime: "",
              subject: "",
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              _t: new Date().getTime()
            }
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

      this.loading = true;
      window.shell
        .execCardMethod({
          ...this.card,
          method: "render",
          param: {
            sourceWid: this.sourcewid,
          },
        })
        .then((data) => {
          this.$nextTick(() => {
            this.loading = false;
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
          window.shell.execCardMethod({
            ...this.myListRouter,
            method: "renderData",
            param: {
              endTime: "",
              processInstanceStatus: "",
              startTime: "",
              subject: "",
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              _t: new Date().getTime()
            }
          }, (mytask) => {
            if (mytask.errcode === '0') {
              this.originData = JSON.stringify(mytask.data.datas);
              this.total = mytask.data.total;
              // this.originData = JSON.stringify(data.data);
              if (data.errcode == 0) {
                // data.data = 0
                if (!data.data) {
                  // this.timeFlag = true;
                  this.datacount = false;
                } else {
                  this.datacount =
                    data?.data?.sourceList?.length ||
                    mytask?.data?.datas?.length ||
                    0;
                  let currentcount = mytask?.data?.datas?.length || 0;

                  if (!currentcount) {
                    this.emptyword = this.$Lan(
                      this.lanFunName,
                      "emptyWord",
                      "暂无{name}我申请的任务",
                      { name: data.data.sourceList[0]?.source_NAME || "" }
                    );
                  }
                  this.sourceList = data.data.sourceList;

                  if (this.datacount) {
                    let config = data.data.config; // 管控台配置数据
                    this.type = config.containerType.type; // 高度类型
                    this.height = config.containerType.value;// 卡片高度
                    this.showSourceList =
                      config.sourceList !== 0 && this.sourceList.length > 0; // 左侧list是否显示: 1显示，0不显示
                    this.itemConfigure = config.itemConfigure;
                    this.subList = mytask.data.datas;
                  }
                  this.height = data.data.config.containerType.value;
                  this.refreshRate = data.data?.config?.refreshRate;
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
            }
            // 定时更新触发的无需重新计时，其他操作触发的需重新计时
            !fromRefresh && this.handleTimedRefresh(this.sourcewid);

          })
        });
    },
    sourceChange(item) {
      // console.log(item);

      // this.currentcount = 0;
      // this.currentcount = parseInt(item.count);
      this.getData(item.wid);

      if (item.count == 0) {
        this.emptyword = this.$Lan(
          this.lanFunName,
          "emptyWord",
          "暂无{name}我申请的任务",
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
.dz-mytask {
  box-shadow: 0.125rem 0.5rem 0.625rem 0rem rgba(0, 0, 0, 2%), -0.0625rem 0rem 0.625rem 0rem rgba(0, 0, 0, 2%);
  border-radius: 0rem 0rem 0.5rem 0.5rem;
}

.mytask {
  min-width: 360px;
  width: 100%;
  display: flex;
  overflow: hidden;
  position: relative;
}

// .pages {
//   display: flex;
//   align-items: center;
//   justify-content: space-between;
//   margin-top: 20px;
// }
</style>
