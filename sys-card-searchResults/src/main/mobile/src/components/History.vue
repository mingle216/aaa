<template>
  <div class="history" v-if="list.length">
    <div class="h-top portal-font-color-lv2">
      <div class="h-left">
        {{ $Lan(lanFunName, "searchHistory", "历史搜索") }}
      </div>
      <div class="h-right" @click="clearHistory">
        <i class="iconfont icon-Ashcan01"></i>
      </div>
    </div>
    <div class="h-list" v-if="list.length">
      <template v-for="(item, index) in list">
        <we-cell
          v-if="index < 5"
          :key="index"
          class="h-item portal-font-color-lv1"
          :value="item"
          @click="
            () => {
              $emit('item-active', item);
            }
          "
        />
      </template>
    </div>
    <div class="h-list" v-else>
      <EmptyCon
        style="padding-top: 120px"
        :tip="$Lan(lanFunName, 'historyTipMsg', '暂无历史搜索')"
      ></EmptyCon>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
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
      // tip: {
      //   en_US: {
      //     tipMsg: "No history search yet",
      //   },
      //   zh_CN: {
      //     tipMsg: "暂无历史搜索",
      //   },
      // },
    };
  },
  methods: {
    clearHistory(item) {
      window.minosStataCollect.collect({
        actionType: 0,
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
      window.shell.execTemplateMethod("clearSearchHis", {}, (data) => {
        if (data.errcode === "0") {
          this.$emit("clear-history", item);
        }
      });
    },
  },
};
</script>
<style lang="less" scoped>
.history {
  height: 100%;
  display: flex;
  flex-flow: column;
  padding: 0 17px;

  .h-top {
    font-size: 13px;
    display: flex;
    line-height: 28px;
    padding: 15px 0;
    height: 42px;
    box-sizing: border-box;
    .h-left {
      flex: 1;
    }
    .h-right {
      width: 28px;
      text-align: right;
    }
  }
  .h-list {
    flex: 1;
    overflow-y: auto;
    .h-item {
      padding: 13px 0;
      font-size: 16px;
      /deep/.we-cell__value {
        box-sizing: border-box;
        // height: 50px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      &::after {
        left: 0;
        right: 0;
      }
    }
  }
}
</style>
