<template>
  <layout
    :id="id"
    :title="title"
    :total="list.length"
    :showTitle="showTitle"
  >
    <template v-if="list.length">
      <div class="item-list">
        <template v-for="(item, index) in list">
          <matterItem
            :router="router"
            :lan-fun-name="lanFunName"
            :key="index"
            :item="item"
            :keyword="keyword"
            :analyzeData="analyzeData"
            v-if="index < page * pageSize"
          />
        </template>
      </div>
      <moreData
        :isOpen="page * pageSize < list.length"
        :lan-fun-name="lanFunName"
        @click="pageAdd"
        v-if="pageSize < list.length"
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
import matterItem from "./items/MatterItem";
export default {
  components: {
    layout,
    moreData,
    matterItem,
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
    title: String
  },
  computed: {
    showTitle() {
      return this.noSearchResult || this.list.length > 0
    }
  },
  data() {
    return {
      page: 1,
      pageSize: 10
    };
  },
  methods: {
    pageAdd(isAdd) {
      if (isAdd) {
        this.page++;
      } else {
        this.page = 1
      }
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
    }
  }
};
</script>

<style lang="less" scoped>
.item-list {
  margin-bottom: 16px;
}
</style>
