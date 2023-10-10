<template>
  <we-list
    class="oneThing__list"
    v-model="loading"
    :finished="finished"
    :finished-text="$Lan(lanFunName, 'finishedText', '没有更多了')"
    @load="loadData"
  >
    <template v-for="(item, index) in list">
      <div
        class="oneThing__item"
        :key="index"
        v-if="index < page * pageSize"
        @click="goDetail(item)"
      >
        <div
          class="item-left"
        >
          <we-image :src="item.iconLink || errImg" fit="cover" class="item-icon">
            <div slot="error">
              <img :src="errImg" />
            </div>
          </we-image>
        </div>
        <div class="item-center">
          <div class="item-name portal-font-color-lv1">
            <div class="name-con">
              <span v-html="name(item)"></span>
            </div>
          </div>
        </div>
      </div>
    </template>
  </we-list>
</template>

<script>
export default {
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
  },
  data() {
    return {
      errImg: window.shell.ErrorImg,
      loading: false,
      finished: true,
      page: 1,
      pageSize: 20,
    };
  },
  computed: {
    name() {
      return function(item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.oneThingName, data);
      };
    },
    oneThingDesc() {
      return function(item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.oneThingDesc, data);
      };
    },
  },
  watch: {
    list: {
      immediate: true,
      handler(val) {
        this.page = 1;
        if (val.length > this.pageSize) {
          this.finished = false;
        } else {
          this.finished = true;
        }
      },
    },
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 200);
      }
    },
  },
  methods: {
    loadData() {
      if (this.page * this.pageSize < this.list.length) {
        this.finished = false;
        this.page++;
      } else {
        this.finished = true;
      }
    },
    /**
     * 跳转办事指南
     */
    goDetail(item) {
      // window.minosStataCollect.collect({
      //   actionType: 0,
      //   functionType: 1,
      //   actionParams: {
      //     pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
      //     pageName: window.shell.getPageData().pageInfoEntity.pageName,
      //     cardWid: this.router.cardWid,
      //     cardId: this.router.cardId,
      //     cardName: this.router.cardName,
      //     extraInfo: {
      //       infoType: 1,
      //       itemId: item.oneThingWid,
      //       fucType: 0,
      //     },
      //   },
      //   startTime: new Date().getTime(),
      // });
      this.$emit('record-cache')
      window.shell.visitService(item.oneThingWid, "OneThing"); // 一件事详情
      window.shell.openPage(`/thingdetail?wid=${item.oneThingWid}`, 0, 1);
    },
  },
};
</script>

<style lang="less" scoped>
.flex {
  display: flex;
  align-items: center;
}
.oneThing__item {
  display: flex;
  padding: 10px;
  box-sizing: border-box;
  width: 100%;
  height: 56px;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  margin-bottom: 12px;
  background-color: #f6f6f8;
  .item-left {
    width: 36px;
    height: 36px;
    position: relative;
    overflow: hidden;
    border-radius: 4px;
    .item-icon {
      height: 36px;
      width: 36px;
      border-radius: 4px;
      // box-shadow: 0 0 10px 5px rgba(0, 0, 0, 0.03);
    }
  }
  .item-center {
    flex: 1;
    padding: 0 10px;
    width: 0;
    .item-name {
      font-size: 14px;
      color: #102645;
      height: 36px;
      display: table;
      .name-con {
        display: table-cell;
        vertical-align: middle;
        span {
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 1;
          overflow: hidden;
          word-break: break-all;
        }
      }
    }
  }
}
</style>
