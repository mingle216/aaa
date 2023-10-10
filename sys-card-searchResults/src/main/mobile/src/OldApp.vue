<template>
  <div class="card-content">
    <div class="search we-hairline--bottom">
      <we-search
        class="search-input"
        v-model="keyword"
        :autofocus="true"
        maxlength="50"
        @search="onSearch"
        show-action
        @input="inputChange"
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
      v-else-if="showResult"
      class="search-main"
      v-bind="{ serviceList, serviceItemList, FAQList, keyword, lanFunName, router }"
      v-loading="loading"
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
      keyword: "",
      isSearchResult: false,
      isLogined: window.shell.getUserInfo(),
      // tip: {
      //   en_US: {
      //     tipMsg: "No relevant content found",
      //   },
      //   zh_CN: {
      //     tipMsg: "未找到相关内容",
      //   },
      // },
    };
  },
  created() {
    const params = window.shell.getUrlParam();
    this.keyword = params?.searchKey.trim() || "";
    this.placeholder = params?.placeholder.trim() || "";
    if (window.shell) {
      this.getCardData('init');
      this.getSearchHis();
    }
  },
  mounted() {
    setTimeout(() => {
      this.$vnode.elm.querySelector("input").focus();
    }, 200);
  },
  beforeDestroy() {},
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
    showResult() {
      return (
        this.serviceList.length ||
        this.serviceItemList.length ||
        this.FAQList.length
      );
    },
  },
  methods: {
    inputChange() {
      this.serviceList = [];
      this.serviceItemList = [];
      this.FAQList = [];
    },
    getCardData(isInit) {
      if(isInit && isInit === 'init') {
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
            extraInfo: ''
          },
          startTime: new Date().getTime()
        });
      }
      if (!this.keyword.trim()) {
        if(isInit && isInit === 'init') {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime()
          });
        }
        return;
      }
      this.loading = true;
      window.shell.execCardMethod({
        cardId: this.router.cardId,
        cardWid: this.router.cardWid,
        method: "renderData",
        param: { keyword: this.keyword.trim() },
      })
      .then(data=> {
        if(isInit && isInit === 'init') {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime()
          });
        }
        this.loading = false;
        // data=searchData;
        if (this.isLogined) {
          this.recordHistory();
        }
        // window.shell.openPage(`result?searchKey=${window.shell.urlParamsEncode(this.keyword)}&placeholder=${window.shell.urlParamsEncode(this.placeholder)}`,0,1);
        if (data.errcode === "0") {
          this.serviceList = data.data?.appList || [];
          this.serviceItemList = data.data?.serviceItemList || [];
          this.FAQList = data.data?.questionList || [];
          this.isSearchResult = true;
        }
      })
      .catch(()=> {
        if(isInit && isInit === 'init') {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime()
          });
        }
      });
    },
    onSearch() {
      this.keyword = this.keyword.trim();
      this.placeholder = this.placeholder.trim();
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
            filterInfo: { keyword: this.keyword.trim() }
          }
        },
        startTime: new Date().getTime()
      });
      this.getCardData();
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
          keywords: this.keyword.trim(),
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
            filterInfo: { keyword: this.keyword.trim() }
          }
        },
        startTime: new Date().getTime()
      });
      this.getCardData();
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
</style>
