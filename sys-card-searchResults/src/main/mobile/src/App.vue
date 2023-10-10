<template>
  <div
    class="card-content"
    :id="templateCode === 'SYS_TEMPLATE_WORK' ? 'work_searchcard' : ''"
  >
    <div class="search we-hairline--bottom">
      <we-search
        class="search-input"
        v-model="keyword"
        :autofocus="true"
        maxlength="50"
        @search="onSearch"
        show-action
        @input="inputChange"
        @clear="clearInput"
        :placeholder="
          placeholder
            ? placeholder
            : $Lan(lanFunName, 'placeholder', '请输入搜索关键词')
        "
      >
        <template #action>
          <div class="search-btn portal-font-color-lv2" @click="onSearch">
            {{ $Lan(lanFunName, "search", "搜索") }}
          </div>
        </template>
      </we-search>
    </div>
    <History
      v-if="!keyword"
      class="search-main"
      :list="historyData"
      :lan-fun-name="lanFunName"
      :router="router"
      @item-active="activeHistory"
      @clear-history="
        () => {
          historyData = [];
        }
      "
    />
    <SearchResult
      v-else-if="dataObj.length"
      class="search-main"
      v-bind="{
        keyword,
        lanFunName,
        router,
        dataObj,
        analyzeData,
      }"
      :sortType.sync="sortType"
      :newsJump="config.newsJump"
      :showCollectNews="config.showCollectNews"
      :showFavoriteTask="config.showFavoriteTask"
      v-loading="loading"
      @record-cache="handleRecordCache"
    />
    <EmptyCon
      v-else-if="!loading && isSearchResult"
      style="padding-top: 120px"
      :tip="$Lan(lanFunName, 'tipMsg', '未找到相关内容')"
    ></EmptyCon>
  </div>
</template>

<script>
import History from "./components/History";
import SearchResult from "./components/SearchResult";
// import {searchData} from "./utils/mock"
export default {
  components: { History, SearchResult },
  name: "",
  props: {
    index: Number,
    router: Object,
  },
  data() {
    const { cardWid, cardId } = this.router;
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      lanFunName: this.router.cardId + "_h5",
      loading: false,
      historyData: [],
      serviceList: [],
      serviceItemList: [],
      FAQList: [],
      placeholder: "",
      searchPrompt: "",
      keyword: "",
      isSearchResult: false,
      isLogined: window.shell.getUserInfo(),
      dataObj: [],
      config: {
        noSeachResult: "0",
        newsJump: "0", // 数据源
      },
      analyzeData: [],
      sortType: 1,
    };
  },
  created() {
    const params = window.shell.getUrlParam();
    this.keyword = params?.searchKey?.trim() || "";
    this.searchPrompt = params?.searchPrompt?.trim() || "";
    this.placeholder = this.searchPrompt || params?.placeholder?.trim() || "";
    if (window.shell) {
      this.getCardData("init");
      this.getSearchHis();
    }
  },
  mounted() {
    setTimeout(() => {
      this.$vnode.elm.querySelector("input").focus();
    }, 200);
    window.shell.on("collectNews", this.collectNews);
  },
  beforeDestroy() {
    window.shell.off("collectNews", this.collectNews);
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 5000);
      }
    },
    keyword() {
      this.isSearchResult = false;
    },
  },
  computed: {
    // 显示搜索分组标题
    noSearchResult() {
      return this.config.noSeachResult === "0";
    },
    templateCode() {
      return window.shell.getTemplateCode();
    },
  },
  methods: {
    clearInput() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
        },
        startTime: new Date().getTime(),
      });
    },
    inputChange() {
      this.serviceList = [];
      this.serviceItemList = [];
      this.FAQList = [];
    },
    getAnalyzeData() {
      window.shell
        .execTemplateMethod("analyzeSearch", {
          keywords: this.keyword,
          lang: localStorage.getItem("locale") || "zh_CN",
        })
        .then((res) => {
          this.analyzeData = res.data?.analyzeData?.map((el) => el.token) || [];
        });
    },
    handleRecordCache(activeTab) {
      // 更改地址栏参数
      const path = window.shell.getLocation()?.href.split("?")[0];
      window.parent?.history.replaceState(
        null,
        null,
        `${path}?searchKey=${this.keyword}&placeholder=${
          this.placeholder
        }&activeTab=${activeTab || 0}`
      );
    },
    async getCardData(isInit) {
      await this.getAnalyzeData();
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
      if (!this.keyword?.trim()) {
        if (isInit && isInit === "init") {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime(),
          });
        }
        return;
      }
      this.loading = true;
      const isShowPcService = window.shell.getPageData().pageContext
        .showProgrammeEntity.isShowPcService;
      window.shell
        .execCardMethod({
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "renderData",
          param: {
            keyword: this.keyword?.trim(),
            isShowPcService: isShowPcService,
            sortType: this.sortType,
          },
        })
        .then((data) => {
          if (isInit && isInit === "init") {
            window.minosStataCollect.loadEnd({
              listId: this.listId,
              endTime: new Date().getTime(),
            });
          }
          this.loading = false;
          // data=searchData;
          if (this.isLogined) {
            this.recordHistory();
          }
          // window.shell.openPage(`result?searchKey=${window.shell.urlParamsEncode(this.keyword)}&placeholder=${window.shell.urlParamsEncode(this.placeholder)}`,0,1);
          if (data.errcode === "0") {
            this.config = data.data.config || this.config;
            let searchData = data.data.searchData;
            let customGroupData = searchData.customGroupData.map((el) => ({
              ...el,
              list: el.groupDataList || [],
              id: `id_${this.router.cardWid}${el.wid}`,
              name: el.groupName,
              code: "CustomGroup",
              enabled: true,
              page: 1,
              total: el.groupDataSize || 0,
            }));
            this.dataObj = [
              ...customGroupData,
              {
                list: searchData.serviceData || [],
                name:
                  searchData.serviceGroupName ??
                  this.$Lan(this.lanFunName, "onlineService", "在线服务"),
                code: "onlineService",
                orderNumber: searchData.serviceOrderNumber,
                enabled: searchData.serviceEnable,
              },
              {
                list: searchData.serviceItemData || [],
                name:
                  searchData.serviceItemGroupName ??
                  this.$Lan(this.lanFunName, "serviceItem", "服务事项"),
                code: "serviceItem",
                orderNumber: searchData.serviceItemOrderNumber,
                enabled: searchData.serviceItemEnable,
              },
              {
                list: searchData.newsData || [],
                name:
                  searchData.newsGroupName ??
                  this.$Lan(this.lanFunName, "newsItem", "资讯"),
                code: "newsItem",
                orderNumber: searchData.newsOrderNumber,
                enabled: searchData.newsEnable,
                page: 1,
                total: searchData.newsSize,
              },
              {
                list: searchData.oneThingData || [],
                name:
                  searchData.oneThingGroupName ??
                  this.$Lan("public", "oneThing", "一件事"),
                code: "oneThing",
                orderNumber: searchData.oneThingOrderNumber,
                enabled: searchData.oneThingEnable,
              },
              {
                list: searchData.deptData || [],
                name:
                  searchData.deptGroupName ??
                  this.$Lan("public", "department", "部门"),
                code: "department",
                orderNumber: searchData.deptOrderNumber,
                enabled: searchData.deptEnable,
              },
              {
                list: searchData.tdcData || [],
                name:
                  searchData.tdcGroupName ??
                  this.$Lan(this.lanFunName, "task", "服务事项"),
                code: "task",
                page: 1,
                total: searchData.tdcSize,
                orderNumber: searchData.tdcOrderNumber,
                enabled: searchData.tdcEnable,
              },
            ]
              .sort((a, b) => a.orderNumber - b.orderNumber)
              .filter((el) => el.enabled);
            // 如果隐藏搜索分项标题，则过滤掉没有数据的tab
            if (this.noSearchResult) {
              this.dataObj = this.dataObj.filter((el) => el.list.length > 0);
            }
            this.isSearchResult = true;
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
    },
    onSearch() {
      this.keyword = this.keyword?.trim();
      this.placeholder = this.placeholder?.trim();
      if (this.searchPrompt && !this.keyword) {
        return
      }
      if (this.placeholder && !this.keyword) {
        this.keyword = this.placeholder;
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
            infoType: 3,
            filterInfo: { keyword: this.keyword?.trim() },
          },
        },
        startTime: new Date().getTime(),
      });
      this.getCardData();
      this.handleRecordCache();
    },
    async getSearchHis() {
      const pageData = JSON.parse(JSON.stringify(window.shell.getPageData()));
      const wid = pageData?.pageInfoEntity?.wid;
      if (this.isLogined && wid) {
        const [res] = await window.shell.getSearchHisVal({
          wid: wid,
        });
        if (res.errcode === "0" && res.data) {
          const data = res.data || [];
          this.historyData = data.map((item) => item.searchKey);
        } else {
          this.historyData = [];
        }
      }
    },
    recordHistory() {
      window.shell.execTemplateMethod(
        "globalSearch",
        {
          keywords: this.keyword?.trim(),
        },
        () => {
          this.getSearchHis();
        }
      );
    },
    activeHistory(item) {
      this.keyword = item;
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
            infoType: 3,
            filterInfo: { keyword: this.keyword?.trim() },
          },
        },
        startTime: new Date().getTime(),
      });
      this.getCardData();
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
  },
};
</script>

<style lang="less" scoped>
.card-content {
  position: relative;
  width: 100%;
  height: 100vh;
  box-sizing: border-box;
  display: flex;
  flex-flow: column;
  .search {
    // border-bottom: 1px solid #f3f6f8;
    height: 48px;
    box-sizing: 48px;
    .search-input {
      padding: 7px 0 7px 15px;
      /deep/.we-search__action {
        padding: 0 17px 0 12px;
      }
      /deep/.we-field__control {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      .search-btn {
        font-size: 16px;
      }
    }
  }
  .search-main {
    flex: 1;
    height: 0;
  }
}
#work_searchcard {
  /deep/.we-tab__pane {
    padding: 12px 10px;
  }
  /deep/.service-item {
    width: 159px;
  }
}
</style>
