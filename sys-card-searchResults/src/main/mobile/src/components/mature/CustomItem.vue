<template>
  <div class="custom_list">
    <div class="title portal-font-color-lv3">{{ name }} ({{ total }})</div>
    <div>
      <template v-for="(el, index) in list">
        <div
          class="custom_item"
          v-if="index < currentPage * pageSize"
          :key="index"
          @click="goHandle(el)"
        >
          <div class="wrap">
            <p
              :class="[index === 0 ? 'title' : 'info', 'text_ellipsis']"
              v-html="highLightTitle(el.sourceAsMap.paramAttr, param.paramWid)"
              :title="getTitle(el.sourceAsMap.paramAttr, param.paramWid)"
              v-for="(param, index) in item.groupParamList.slice(0, 2)"
              :key="param.paramWid"
            ></p>
          </div>
          <img v-if="el.sourceAsMap.url" src="../../assets/images/arrow.png" alt="" />
          <img v-else src="../../assets/images/disableArrow.png" alt="" />
        </div>
      </template>
    </div>
    <moreData
      :lan-fun-name="lanFunName"
      @click="pageAdd"
      :isOpen="currentPage * pageSize < total"
      v-if="pageSize < total"
    />
  </div>
</template>

<script>
import MoreData from "./MoreData";
export default {
  components: {
    MoreData,
  },
  props: {
    router: Object,
    analyzeData: Array,
    list: {
      type: Array,
      default: () => {
        return [];
      },
    },
    keyword: {
      type: String,
      default: "",
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
    groupParamList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    currentPage: {
      type: Number,
      default: 1,
    },
    name: String,
    total: Number,
    hasExpand: Boolean,
  },
  data() {
    return {
      errorImg: window.shell.ErrorImg,
      loading: false,
      finished: false,
      pageSize: 10,
    };
  },
  methods: {
    highLightTitle(obj, key) {
      const data = JSON.parse(JSON.stringify(this.analyzeData));
      return window.shell.strHighlighted(obj[key], data);
    },
    getTitle(obj, key) {
      return obj[key];
    },
    pageAdd(isAdd) {
      if (isAdd) {
        if (this.hasExpand) {
          this.$emit("update:currentPage", this.currentPage + 1);
          return;
        } else {
          window.shell
            .execCardMethod({
              cardId: this.router.cardId,
              cardWid: this.router.cardWid,
              method: "moreCustomGroupDataForPortal",
              param: {
                groupWid: this.item.wid,
                keyword: this.keyword,
                pageNumber: this.currentPage,
                pageSize: this.pageSize,
              },
            })
            .then((res) => {
              const list = res.data?.searchData?.customGroupData[0]?.groupDataList || [];
              this.$emit("update:list", this.list.concat(list));
              this.$emit("update:currentPage", this.currentPage + 1);
            });
        }
      } else {
        this.$emit("update:currentPage", 1);
        this.$emit("update:hasExpand", true);
      }
    },
    loadData() {
      if (this.currentPage * this.pageSize < this.item.total) {
        window.shell
          .execCardMethod({
            cardId: this.router.cardId,
            cardWid: this.router.cardWid,
            method: "moreCustomGroupDataForPortal",
            param: {
              groupWid: this.item.wid,
              keyword: this.keyword,
              pageNumber: this.currentPage,
              pageSize: this.pageSize,
            },
          })
          .then((res) => {
            const list = res.data?.searchData?.customGroupData[0]?.groupDataList || [];
            this.$emit("update:list", this.list.concat(list));
            this.finished = false;
            this.loading = false;
            this.$emit("update:currentPage", this.currentPage + 1);
          });
      } else {
        this.finished = true;
      }
    },
    goHandle(item) {
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
      if (item?.sourceAsMap?.url) {
        this.$emit("record-cache");
        window.shell.openUrl(item?.sourceAsMap?.url);
      } else {
        window.shell.showMessage({
          message: "暂无跳转链接",
        });
      }
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null;
    },
  },
};
</script>

<style lang="less" scoped>
.text_ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  width: 100%;
}
.custom_list {
  background-color: #fff;
  padding: 12px;
  .custom_item {
    border-radius: 4px;
    margin-bottom: 12px;
    padding: 12px 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-shadow: inset 0 -0.01333rem 0 #f0f0f0;
    &:last-child {
      box-shadow: none;
      margin-bottom: 0;
    }
    .wrap {
      width: calc(100% - 28px);
    }
    .title {
      font-size: 16px;
      color: #000;
      font-weight: bold;
      text-align: justify;
      letter-spacing: 0;
      line-height: 20px;
      margin-bottom: 7px;
    }
    .info {
      font-size: 14px;
    }
  }
}
</style>
