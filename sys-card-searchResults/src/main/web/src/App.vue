<template>
  <div class="search-result-con" :id="id">
    <div class="search-nav" v-if="showNav && navArr.length">
      <div class="nav-con" :style="navStyle" v-if="!loading">
        <template v-for="(item, index) in navArr">
          <navItem
            v-if="item.enabled"
            :router="router"
            :key="index"
            :navId="item.id"
            :navText="item.name"
            :startTop="startTop"
            :indexNavId="indexNavId"
            v-model="clickScrolling"
            @activeItem="itemNavHandle"
          />
        </template>
      </div>
    </div>
    <div
      class="search-result"
      v-loading="loading"
      ref="SearchResult"
      :style="{
        width: `calc(100% - ${showNav && dataObj.length ? '126' : '0'}px - ${
          showHot ? '298' : '0'
        }px)`,
      }"
    >
      <template v-if="dataObj.length">
        <div v-for="item in dataObj" :key="item.id">
          <component
            :is="item.code"
            :item="item"
            :page.sync="item.page"
            :sortType.sync="sortType"
            :hasExpand.sync="item.flag"
            :router="router"
            :keyword="keyword"
            :id="item.id"
            :title="item.name"
            :list.sync="item.list"
            :lan-fun-name="lanFunName"
            :noSearchResult="noSearchResult"
            :showNav="showNav"
            :searchResultWidth="searchResultWidth"
            :analyzeData="analyzeData"
            :serviceAppraise="config.serviceAppraise"
            :newsJump="config.newsJump"
            :showCollectNews="config.showCollectNews"
            :showFavoriteTask="config.showFavoriteTask"
            v-if="item.enabled"
          />
        </div>
      </template>
      <template v-else>
        <EmptyCon
          :tip="$Lan(lanFunName, 'tipMsg', '未找到相关数据')"
          mainTipClass="portal-font-color-lv3"
        />
      </template>
    </div>
    <HotSearch
      ref="HotSearch"
      v-if="showHot && !loading"
      :router="router"
      :hotSearchDisplay="hotSearchDisplay"
      :dataObj="dataObj"
    />
  </div>
</template>

<script>
import NewsItem from "./components/News";
import Task from "./components/Task";
import CustomGroup from "./components/Custom";
import serviceItem from "./components/ServiceMatter";
import onlineService from "./components/OnlineService";
import navItem from "./components/items/NavItem";
import oneThing from "./components/OneThing";
import HotSearch from "./components/HotSearch";
import department from "./components/Department";

const defaultCfg = {
  noSeachResult: "0",
  showAnchor: "1",
  serviceAppraise: "0",
  newsJump: "0", // 数据源
  hotSearchDisplay: {
    dataTypes: "0,1,2",
    displaySwitch: 0,
    timeDelay: 30,
  },
};

export default {
  name: "sysCardSearchResults",
  components: {
    NewsItem,
    Task,
    CustomGroup,
    serviceItem,
    onlineService,
    navItem,
    oneThing,
    HotSearch,
    department,
  },
  props: {
    index: Number,
    router: Object,
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 6000);
      }
    },
  },

  data() {
    const { cardWid, cardId } = this.router;
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      id: "",
      navArr: [],
      dataObj: [],
      loading: false,
      config: defaultCfg,
      keyword: "",
      isFloat: false,
      navStyle: { top: 0 },
      startTop: 0,
      indexNavId: "",
      clickScrolling: false,
      lanFunName: this.router.cardId,
      searchResultWidth: 1026,
      analyzeData: [], // 分词
      sortType: 1,
    };
  },
  computed: {
    showNav() {
      return this.config.showAnchor === "1";
    },
    // 显示搜索分组标题
    noSearchResult() {
      return this.config.noSeachResult === "1";
    },
    hotSearchDisplay() {
      return this.config.hotSearchDisplay;
    },
    showHot() {
      return this.hotSearchDisplay.displaySwitch;
    },
  },
  async created() {
    let params = window.shell.getUrlParam() || {};
    this.keyword = params.searchKey || "";
    if (params.groupWid) {
      this.searchTab =
        params.groupType == "1" ? params.groupWid : params.groupType || ""; // 1为自建分组，非自建分组用type作为wid
    }
    this.id = `result_${this.router.wid}`;
    await this.getAnalyzeData();
    await this.renderData("init");
  },
  mounted() {
    window.shell.on("update-search-results", (keyword, searchTab) => {
      setTimeout(async () => {
        this.keyword = keyword || window.shell.getUrlParam().searchKey || "";
        if (searchTab) {
          this.searchTab =
            searchTab.groupType == "1"
              ? searchTab.groupWid
              : String(searchTab.groupType || ""); // 1为自建分组，非自建分组用type作为wid
        }
        await this.getAnalyzeData();
        await this.renderData();
      }, 200);
    });

    this.$nextTick(() => {
      let con_el = document.getElementById(this.id);
      if (con_el) {
        this.startTop = window.shell.getElementTop(con_el);
      }
      window.shell.on("onScoll", (ev) => {
        this.initNavScoll(ev.scrollTop);
      });
    });
    window.shell.on("collectApp", this.collectService);
    window.shell.on("collectNews", this.collectNews);
    window.addEventListener("resize", this.setSearchResultWidth);
  },
  methods: {
    collectService(params) {
      const service = this.dataObj.find((el) => el.code === "onlineService");
      let index = service.list.findIndex((m_item) => {
        return params.id === m_item.serviceWid;
      });
      if (index !== -1) {
        service.list[index].favorite = params.operate == 1 ? true : false;
      }
    },
    collectNews(params) {
      const news = this.dataObj.find((el) => el.code === "newsItem");
      let index = news.list.findIndex((m_item) => {
        return params.id === m_item.wid;
      });
      if (index !== -1) {
        news.list[index].favoriteFlag = params.operate == 1 ? true : false;
      }
    },
    initNavScoll(topNum) {
      let indexNavTop = topNum;
      if (indexNavTop >= this.startTop) {
        let con_el = document.getElementById(this.id);
        let m_left = con_el.offsetWidth / 2 - 90;
        this.navStyle = {
          top: 0,
          position: "fixed",
          right: "50%",
          "margin-right": `${m_left}px`,
        };
      } else {
        this.navStyle = { top: 0 };
      }
    },
    itemNavHandle(navId) {
      this.indexNavId = navId;
    },
    getAnalyzeData() {
      window.shell
        .execTemplateMethod("analyzeSearch", {
          keywords: this.keyword,
          lang: sessionStorage.getItem("locale") || "zh_CN",
        })
        .then((res) => {
          this.analyzeData = res.data?.analyzeData?.map((el) => el.token) || [];
        });
    },
    renderData(isInit) {
      if (isInit && isInit === "init") {
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
      if (this.loading) {
        return;
      }
      this.loading = true;
      window.shell
        .execCardMethod({
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "renderData",
          param: { keyword: this.keyword, sortType: this.sortType },
        })
        .then((data) => {
          if (isInit && isInit === "init") {
            window.minosStataCollect.loadEnd({
              listId: this.listId,
              endTime: new Date().getTime(),
            });
          }
          if (data.errcode === "0") {
            setTimeout(() => {
              this.loading = false;
            }, 10);

            this.dataArr = [];
            this.navArr = [];
            this.config = data.data.config || defaultCfg;
            let searchData = data.data.searchData;
            let customGroupData = searchData.customGroupData.map((el) => ({
              ...el,
              list: el.groupDataList || [],
              id: `id_${this.router.cardWid}${el.wid}`,
              name: el.groupName,
              code: "CustomGroup",
              enabled: true,
              page: 1,
              flag: false, // 全部展开然后收起，下次展开只需要改变分页不用再调接口
            }));
            this.dataObj = [
              ...customGroupData,
              {
                list: searchData.serviceData || [],
                id: `id_${this.router.cardWid}onlineService`,
                name:
                  searchData.serviceGroupName ??
                  this.$Lan(this.lanFunName, "onlineService", "在线服务"),
                code: "onlineService",
                orderNumber: searchData.serviceOrderNumber,
                enabled: searchData.serviceEnable,
                page: 1,
                wid: "2",
              },
              {
                list: searchData.serviceItemData || [],
                id: `id_${this.router.cardWid}serviceItem`,
                name:
                  searchData.serviceItemGroupName ??
                  this.$Lan(this.lanFunName, "serviceItem", "服务事项"),
                code: "serviceItem",
                orderNumber: searchData.serviceItemOrderNumber,
                enabled: searchData.serviceItemEnable,
                page: 1,
                wid: "3",
              },
              {
                list: searchData.newsData || [],
                id: `id_${this.router.cardWid}News`,
                name:
                  searchData.newsGroupName ??
                  this.$Lan(this.lanFunName, "newsItem", "资讯"),
                code: "newsItem",
                orderNumber: searchData.newsOrderNumber,
                enabled: searchData.newsEnable,
                page: 1,
                total: searchData.newsSize,
                flag: false,
                wid: "4",
              },
              {
                list: searchData.oneThingData || [],
                id: `id_${this.router.cardWid}OneThing`,
                name:
                  searchData.oneThingGroupName ??
                  this.$Lan("public", "oneThing", "一件事"),
                code: "oneThing",
                orderNumber: searchData.oneThingOrderNumber,
                enabled: searchData.oneThingEnable,
                page: 1,
                wid: "5",
              },
              {
                list: searchData.deptData || [],
                id: `id_${this.router.cardWid}Department`,
                name:
                  searchData.deptGroupName ??
                  this.$Lan("public", "department", "部门"),
                code: "department",
                orderNumber: searchData.deptOrderNumber,
                enabled: searchData.deptEnable,
                page: 1,
                wid: "6",
              },
              {
                list: searchData.tdcData || [],
                id: `id_${this.router.cardWid}Task`,
                name:
                  searchData.tdcGroupName ??
                  this.$Lan("public", "task", "部门"),
                code: "task",
                orderNumber: searchData.tdcOrderNumber,
                enabled: searchData.tdcEnable,
                page: 1,
                total: searchData.tdcSize,
                wid: "7",
              },
            ]
              .sort((a, b) => a.orderNumber - b.orderNumber)
              .filter((el) => el.enabled);
            if (this.searchTab && this.searchTab !== "all") {
              const index = this.dataObj.findIndex(
                (item) => item.wid === this.searchTab
              );
              if (index !== -1 && index) {
                const value = this.dataObj[index];
                this.dataObj.splice(index, 1);
                this.dataObj.unshift(value);
              }
            }
            if (!this.noSearchResult) {
              this.dataObj = this.dataObj.filter((el) => el.list.length > 0);
            }
            this.navArr = this.dataObj;
            setTimeout(() => {
              let con_el = document.getElementById(this.id);
              if (con_el) {
                this.startTop = window.shell.getElementTop(con_el);
              }
            }, 500);
          } else {
            setTimeout(() => {
              this.loading = false;
            }, 10);
            this.$message({
              showClose: false,
              message: this.$Lan(this.lanFunName, "message", "获取搜索失败"),
              type: "error",
            });
          }
        })
        .catch(() => {
          if (isInit && isInit === "init") {
            window.minosStataCollect.loadEnd({
              listId: this.listId,
              endTime: new Date().getTime(),
            });
          }
        });
      this.$nextTick(() => {
        const navWidth =
          (isInit && isInit === "init") || !this.showNav ? 0 : 126;
        this.searchResultWidth =
          (this.$refs.SearchResult.offsetWidth || 1026) +
          (this.dataObj.length ? navWidth : 0);
      });
    },
    setSearchResultWidth() {
      this.$nextTick(() => {
        const navWidth = !this.showNav ? 0 : 126;
        this.searchResultWidth =
          (this.$refs.SearchResult.offsetWidth || 1026) + navWidth;
      });
    },
  },

  beforeDestroy() {
    window.shell.off("onScoll");
    window.shell.off("collectApp", this.collectService);
    window.shell.off("collectNews", this.collectNews);
    window.shell.off("update-search-results");
    window.removeEventListener("resize", this.setSearchResultWidth, false);
  },
};
</script>
<style lang="less">
*:focus {
  outline: none;
}
</style>
<style lang="less" scoped>
.search-result-con {
  width: 100%;
  display: flex;
  background: #fff;

  .search-nav {
    width: 126px;
    position: relative;
    padding-right: 36px;
    .nav-con {
      width: 90px;
      border-right: 1px solid #e7edf1;
      position: relative;
      background-color: #fff;
    }
  }
  .search-result {
    // flex: 1;
    // width: 100%;
    // width: calc(100% - 126px);
    // padding-right: 36px;
  }
  .full-width {
    width: 100%;
  }
}
</style>
