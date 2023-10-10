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
          <serviceItem
            v-if="index < page * pageSize"
            :width="itemWidth"
            :item="item"
            :isCollect="item % 2"
            :key="index"
            :keyword="keyword"
            :lan-fun-name="lanFunName"
            :router="router"
            :analyzeData="analyzeData"
            :serviceAppraise="serviceAppraise"
            @open-comment="openServiceComment"
          ></serviceItem>
        </template>
      </div>
      <moreData
        :lan-fun-name="lanFunName"
        @click="pageAdd"
        :isOpen="page * pageSize < list.length"
        v-if="pageSize < list.length"
      />
    </template>

    <EmptyCon
      v-else
      :tip="$Lan(lanFunName, 'tipMsg', '未找到相关数据')"
      mainTipClass="portal-font-color-lv2"
    />
    <ServiceCommentModal ref="ServiceCommentModal" :router="router"></ServiceCommentModal>
  </layout>
</template>
<script>
import layout from "./Layout";
import serviceItem from "./items/ServiceItem";
import moreData from "./MoreData";
import ServiceCommentModal from "./ServiceCommentModal";
export default {
  components: {
    layout,
    serviceItem,
    moreData,
    ServiceCommentModal,
  },
  props: {
    router: Object,
    analyzeData: Array,
    id: {
      type: String,
      default: "service" + new Date().getTime(),
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
      default: true,
    },
    serviceAppraise: String,
    title: String
  },
  watch: {
    list: {
      immediate: true,
      handler() {
        this.page = 1;
      },
    },
  },
  computed: {
    showTitle() {
      return this.noSearchResult || this.list.length > 0;
    },
  },
  data() {
    return {
      itemWidth: 240,
      page: 1,
      pageSize: 40,
    };
  },
  created() {
    setTimeout(() => {
      const num = window.shell.isIE() ? 18 : 8
      let rl_w = document.getElementById(this.id).offsetWidth - num;  // 获取宽度时滚动条还未加载出来导致8px误差
      this.itemWidth = parseInt(rl_w / 4);
    }, 1);
  },
  methods: {
    openServiceComment(item) {
      this.$refs.ServiceCommentModal.show({
        wid: item.serviceWid,
        serviceName: item.serviceName,
      });
    },
    pageAdd(isAdd) {
      if (isAdd) {
        this.page++;
      } else {
        this.page = 1;
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
        },
        startTime: new Date().getTime(),
      });
    },
  },
};
</script>
<style lang="less" scoped>
.item-list {
  overflow: hidden;
  padding: 20px 0;
  & > div {
    float: left;
  }
}
</style>
