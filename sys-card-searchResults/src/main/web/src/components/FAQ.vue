<template>
  <layout
    :id="id"
    :title="$Lan(lanFunName, 'FAQ', '常见问题')"
    :total="list.length"
  >
    <template v-if="list.length">
      <div class="item-list">
        <template v-for="(item, index) in list">
          <FAQItem
            :router="router"
            :lan-fun-name="lanFunName"
            :key="index"
            :item="item"
            :keyword="keyword"
            v-if="index < page * pageSize"
          />
        </template>
      </div>
      <moreData
        v-if="page * pageSize < list.length"
        :isOpen="showMore"
        @click="pageAdd"
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
import FAQItem from "./items/FAQItem";
export default {
  components: {
    layout,
    moreData,
    FAQItem,
  },
  props: {
    router: Object,
    id: {
      type: String,
      default: "faq" + new Date().getTime(),
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
  },
  data() {
    return {
      page: 1,
      pageSize: 5,
      // tip: {
      //   en_US: {
      //     tipMsg: "No relevant data could be found",
      //   },
      //   zh_CN: {
      //     tipMsg: "未找到相关数据",
      //   },
      // },
    };
  },
  methods: {
    pageAdd() {
      this.page++;
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

<style lang="less" scoped></style>
