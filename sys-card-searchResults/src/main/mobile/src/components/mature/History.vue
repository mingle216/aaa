<template>
  <div class="history">
    <div class="h-top portal-font-color-lv3" v-if="list.length">
      <div class="h-left">
        {{ $Lan(lanFunName, "searchHistory", "历史搜索") }}
      </div>
      <div class="h-right" @click="clearHistory">
        <i class="iconfont icon-Maturemoban-shanchu"></i>
      </div>
    </div>
    <div class="h-list" v-if="list.length">
      <div class="h-wrap">
        <div
          class="h-item"
          v-for="(item, index) in list"
          :key="index"
          @click="
            () => {
              $emit('item-active', item);
            }
          "
        >
          <span class="portal-font-color-lv1">{{ item }}</span>
        </div>
      </div>
      <HotSearch :config="config" :dataObj="dataObj" :router="router" />
    </div>
    <div class="h-list" v-if="!list.length && !loading">
      <EmptyCon
        :pageImg="true"
        style="padding-top: 120px"
        :tip="$Lan(lanFunName, 'historyTipMsg', '暂无历史搜索')"
      ></EmptyCon>
    </div>
  </div>
</template>

<script>
import HotSearch from "./HotSearch.vue";
export default {
  components: {
    HotSearch,
  },
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
    config: Object,
    dataObj: Array,
    loading: Boolean
  },
  data() {
    return {
      defaultColor: '#1C6BFF'
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
          extraInfo: "",
        },
        startTime: new Date().getTime(),
      });
      this.$dialog
        .confirm({
          className: "historyConfirm portal-font-color-lv1",
          message: this.$Lan(this.lanFunNanme, "deleteConfirm", "确认删除全部历史记录？"),
          cancelButtonText: this.$Lan(this.lanFunNanme, "cancel", "取消"),
          cancelButtonColor: "#121212",
          confirmButtonText: this.$Lan(this.lanFunNanme, "delete", "删除"),
          confirmButtonColor: this.defaultColor,
        })
        .then(() => {
          window.shell.execTemplateMethod("clearSearchHis", {}, (data) => {
            if (data.errcode === "0") {
              this.$emit("clear-history", item);
            }
          });
        })
        .catch(() => {});
    },
  },
  mounted() {
    this.defaultColor = JSON.parse(
      JSON.parse(
        window.shell.getPageData().pageContext.showProgrammeEntity
          .templateConfig
      )
    ).themeColorSetting["portal-primary-color-lv1"];
  },
};
</script>
<style lang="less" scoped>
.history {
  height: 100%;
  display: flex;
  flex-flow: column;
  // padding: 0 12px 12px;
  background: #fff;

  .h-top {
    font-size: 14px;
    display: flex;
    margin: 13px 0 15px;
    padding: 0 12px;
    box-sizing: border-box;
    .h-left {
      flex: 1;
      line-height: 20px;
    }
    .h-right {
      width: 28px;
      text-align: right;
      i {
        font-size: 16px;
      }
    }
  }
  .h-list {
    flex: 1;
    overflow-y: auto;
    &::-webkit-scrollbar {
      width: 0;
      background: transparent;
    }
    .h-wrap {
      display: flex;
      flex-wrap: wrap;
      margin-bottom: 10px;
      padding: 0 12px;
      .h-item {
        background: #f5f5f5;
        border-radius: 14px;
        font-size: 13px;
        line-height: 18px;
        padding: 5px 12px;
        margin: 0 10px 10px 0;
      }
    }
  }
}
</style>

<style lang="less">
.historyConfirm {
  .we-dialog__message {
    font-weight: 600;
    font-size: 17px;
    line-height: 24px;
  }
  .we-dialog__content--isolated {
    min-height: 92px;
  }
}
</style>
