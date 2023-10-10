<template>
  <layout
    :id="id"
    :title="item.name"
    :total="item.groupDataSize"
    :showTitle="showTitle"
  >
    <template v-if="list.length">
      <div class="item-list">
        <template v-for="(el, index) in list">
          <custom-item
            :router="router"
            :lan-fun-name="lanFunName"
            :key="index"
            :dataItem="el"
            :groupParamList="item.groupParamList"
            :keyword="keyword"
            :showNav="showNav"
            :searchResultWidth="searchResultWidth"
            :analyzeData="analyzeData"
            v-if="index < page * pageSize"
          />
        </template>
      </div>
      <moreData
        v-if="pageSize < item.groupDataSize"
        :lan-fun-name="lanFunName"
        @click="pageAdd"
        :isOpen="page * pageSize < item.groupDataSize"
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
import CustomItem from "./items/CustomItem";
export default {
  components: {
    layout,
    moreData,
    CustomItem,
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
    item: {
      type: Object,
      default: () => {
        return {};
      },
    },
    noSearchResult: {
      type: Boolean,
      default: true,
    },
    page: {
      type: Number,
      default: 1,
    },
    hasExpand: {
      type: Boolean,
      default: false,
    },
    showNav: {
      type: Boolean,
      default: true
    },
    searchResultWidth: {
      type: Number,
      default: 1026
    }
  },
  data() {
    return {
      pageSize: 10,
      btnLoading: false,
    };
  },
  computed: {
    showTitle() {
      return this.noSearchResult || this.list.length > 0;
    },
  },
  methods: {
    pageAdd(isAdd) {
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
      if (isAdd) {
        // 已经全部展开后，不需要再请求接口，直接改变分页
        if (this.hasExpand) {
          this.$emit("update:page", this.page + 1);
          return;
        } else {
          if (this.btnLoading) {
            return;
          }
          this.btnLoading = true;
          window.shell
            .execCardMethod({
              cardId: this.router.cardId,
              cardWid: this.router.cardWid,
              method: "moreCustomGroupDataForPortal",
              param: {
                groupWid: this.item.wid,
                keyword: this.keyword,
                pageNumber: this.page,
                pageSize: this.pageSize
              },
            })
            .then((res) => {
              this.btnLoading = false;
              const list =
                res.data?.searchData?.customGroupData[0]?.groupDataList || [];
              this.$emit("update:list", this.list.concat(list));
              this.$emit("update:page", this.page + 1);
            });
        }
      } else {
        this.$emit("update:page", 1);
        this.$emit("update:hasExpand", true);
      }
    },
  },
};
</script>

<style lang="less" scoped>
.item-list {
  margin-bottom: 16px;
}
</style>
