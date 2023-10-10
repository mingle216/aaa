<template>
  <layout
    :id="id"
    :title="title"
    :total="item.total"
    :showTitle="showTitle"
  >
    <template slot="title-opt">
      <we-dropdown trigger="click" @command="handleCommand">
        <span class="we-dropdown-link drop_link">
          {{ sortType === 1 ? '按匹配度' : '按时间' }}
          <i class="we-icon-arrow-down we-icon--right"></i>
        </span>
        <we-dropdown-menu slot="dropdown">
          <we-dropdown-item :command="1">按匹配度</we-dropdown-item>
          <we-dropdown-item :command="2">按时间</we-dropdown-item>
        </we-dropdown-menu>
      </we-dropdown>
    </template>
    <template v-if="list.length">
      <div class="item-list">
        <template v-for="(item, index) in list">
          <news-item
            :router="router"
            :lan-fun-name="lanFunName"
            :key="index"
            :item="item"
            :keyword="keyword"
            :showNav="showNav"
            :searchResultWidth="searchResultWidth"
            :analyzeData="analyzeData"
            :newsJump="newsJump"
            :showCollectNews="showCollectNews"
            v-if="index < page * pageSize"
          />
        </template>
      </div>
      <moreData
        v-if="pageSize < item.total"
        :lan-fun-name="lanFunName"
        @click="pageAdd"
        :isOpen="page * pageSize < item.total"
      />
    </template>
    <EmptyCon
      v-else
      :tip="$Lan(lanFunName, 'tipMsg', '未找到相关数据')"
      mainTipClass="portal-font-color-lv2"
    />
  </layout>
</template>

<script>
import layout from "./Layout";
import moreData from "./MoreData";
import NewsItem from "./items/NewsItem";
export default {
  components: {
    layout,
    moreData,
    NewsItem,
  },
  props: {
    router: Object,
    analyzeData: Array,
    id: {
      type: String,
      default: "matter" + new Date().getTime(),
    },
    keyword: {
      type: String,
      default: "",
    },
    list: {
      type: Array,
      default: () => {
        return [];
      },
    },
    lanFunName: {
      type: String,
      default: "",
    },
    noSearchResult: {
      type: Boolean,
      default: true
    },
    item: {
      type: Object,
      default: () => {
        return {}
      }
    },
    page: {
      type: Number,
      default: 1
    },
    showNav: {
      type: Boolean,
      default: true
    },
    hasExpand: {
      type: Boolean,
      default: false
    },
    searchResultWidth: {
      type: Number,
      default: 1026
    },
    sortType: {
      type: Number,
      default: 1
    },
    newsJump: String,
    title: String,
    showCollectNews: Number
  },
  data() {
    return {
      pageSize: 10,
      btnLoading: false
    };
  },
  computed: {
    showTitle() {
      return this.noSearchResult || this.list.length > 0
    }
  },
  methods: {
    handleCommand(name) {
      this.$emit('update:sortType', name)
      this.$emit('update:page', 1)
      window.shell
        .execCardMethod({
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "sortNewsForPortal",
          param: { 
            keyword: this.keyword,
            pageNumber: this.page,
            pageSize: this.pageSize,
            lang: sessionStorage.getItem("locale") || "zh_CN",
            sortType: name
          },
        })
        .then(res => {
          const list = res.data?.searchData?.newsData || []
          this.$emit('update:list', list)
        })
    },
    pageAdd(isAdd) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName
        },
        startTime: new Date().getTime()
      });
      if (isAdd) {
        // 已经全部展开后，不需要再请求接口，直接改变分页
        if (this.hasExpand) {
          this.$emit('update:page', this.page + 1)
          return
        } else {
          if (this.btnLoading) {
            return
          }
          this.btnLoading = true
          window.shell
            .execCardMethod({
              cardId: this.router.cardId,
              cardWid: this.router.cardWid,
              method: "moreNewsForPortal",
              param: { 
                keyword: this.keyword,
                pageNumber: this.page,
                pageSize: this.pageSize,
                sortType: this.sortType
              },
            })
            .then(res => {
              this.btnLoading = false
              const list = res.data?.searchData?.newsData || []
              this.$emit('update:list', this.list.concat(list))
              this.$emit('update:page', this.page + 1)
            })
        }
      } else {
        this.$emit('update:page', 1)
        this.$emit('update:hasExpand', true)
      }
    }
  }
};
</script>

<style lang="less" scoped>
.item-list {
  margin-bottom: 16px;
}
/deep/.we-dropdown {
  height: 35px;
}
.drop_link {
  cursor: pointer;
  font-weight: normal;
}
</style>
