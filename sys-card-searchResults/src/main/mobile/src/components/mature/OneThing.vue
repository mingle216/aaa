<template>
  <div class="oneThing__list">
    <div class="title portal-font-color-lv3">{{ name }} ({{ list.length }})</div>
    <div class="wrap">
      <template v-for="(item, index) in list">
        <div
          class="oneThing__item"
          :key="index"
          v-if="index < page * pageSize"
          @click="goDetail(item)"
        >
          <div class="item-left">
            <we-image :src="item.iconLink || errImg" fit="cover" class="item-icon">
              <div slot="error">
                <img :src="errImg" />
              </div>
            </we-image>
          </div>
          <div class="item-center">
            <div class="item-name portal-font-color-lv1">
              <span v-html="nameHighLight(item)"></span>
            </div>
          </div>
        </div>
      </template>
    </div>
    <moreData
      :lan-fun-name="lanFunName"
      @click="pageAdd"
      :isOpen="page * pageSize < list.length"
      v-if="pageSize < list.length"
    />
  </div>
</template>

<script>
import MoreData from "./MoreData";
export default {
  components: {
    MoreData
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
    name: String
  },
  data() {
    return {
      errImg: window.shell.ErrorImg,
      loading: false,
      finished: true,
      page: 1,
      pageSize: 6,
    };
  },
  computed: {
    nameHighLight() {
      return function (item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.oneThingName, data);
      };
    },
    oneThingDesc() {
      return function (item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.oneThingDesc, data);
      };
    },
  },
  methods: {
    pageAdd(isAdd) {
      if (isAdd) {
        this.page++;
      } else {
        this.page = 1;
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
      this.$emit("record-cache");
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
.oneThing__list {
  padding: 12px;
  background: #ffffff;
  border-radius: 8px;
  margin-bottom: 12px;
  .title {
    margin-bottom: 12px;
  }
  .wrap {
    display: flex;
    flex-wrap: wrap;
    .oneThing__item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 16px 12px;
      box-sizing: border-box;
      width: calc(50% - 6px);
      height: 112px;
      border-radius: 4px;
      overflow: hidden;
      cursor: pointer;
      margin-bottom: 12px;
      margin-right: 12px;
      background-color: #FAFAFA;
      &:last-child {
        margin-bottom: 0;
      }
      &:nth-child(2n) {
        margin-right: 0;
      }
      .item-left {
        width: 32px;
        height: 32px;
        position: relative;
        overflow: hidden;
        border-radius: 4px;
        margin-bottom: 8px;
        .item-icon {
          height: 32px;
          width: 32px;
          border-radius: 4px;
        }
      }
      .item-center {
        display: flex;
        align-items: center;
        height: 38px;
        .item-name {
          font-size: 15px;
          line-height: 20px;
          text-align: center;
          span {
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
            word-break: break-all;
          }
        }
      }
    }
  }
}
</style>
