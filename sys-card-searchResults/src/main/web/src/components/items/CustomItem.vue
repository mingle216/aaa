<template>
  <div
    :class="{
      custom_item: true,
      'portal-font-color-lv1': true,
    }"
    :style="{ width: '100%' }"
  >
    <div class="custom_item_wrap">
      <div
        :class="[
          'text_ellipsis',
          index === 0 ? 'custom_item_title' : 'custom_item_content',
          dataItem.sourceAsMap.url
            ? 'portal-primary-color-hover-lv1 pointer'
            : '',
        ]"
        v-html="highLightTitle(dataItem.sourceAsMap.paramAttr, item.paramWid)"
        :title="getTitle(dataItem.sourceAsMap.paramAttr, item.paramWid)"
        v-for="(item, index) in groupParamList.slice(0, 2)"
        :key="item.paramWid"
        @click="handleClick"
      ></div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    dataItem: Object,
    analyzeData: Array,
    keyword: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
    groupParamList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    showNav: {
      type: Boolean,
      default: true,
    },
    searchResultWidth: {
      type: Number,
      default: 1026,
    }
  },
  data() {
    return {
      errorImg: window.shell.ErrorImg,
    };
  },
  methods: {
    highLightTitle(obj, key) {
      const data = JSON.parse(JSON.stringify(this.analyzeData))
      return window.shell.strHighlighted(obj[key], data);
    },
    getTitle(obj, key) {
      return obj[key];
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null;
    },
    handleClick() {
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
      if (this.dataItem?.sourceAsMap?.url) {
        window.shell.openUrl(this.dataItem?.sourceAsMap?.url);
      }
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
  // max-width: 1200px;
}
.pointer {
  cursor: pointer;
}
.custom_item {
  padding: 19px 0;
  border-bottom: 1px solid #e7edf1;
  display: flex;
  justify-content: space-between;
  img {
    width: 92px;
    height: 70px;
    margin-right: 16px;
    border-radius: 4px;
  }
  .custom_item_wrap {
    max-width: 100%;
    display: flex;
    flex-direction: column;
    .custom_item_title {
      font-size: 16px;
      letter-spacing: 0;
      line-height: 24px;
    }
    .custom_item_content {
      font-size: 14px;
      color: #8c8c8c;
      letter-spacing: 0;
      line-height: 22px;
    }
    .custom_item_info {
      font-size: 14px;
      color: #8c8c8c;
      letter-spacing: 0;
      line-height: 22px;
      display: flex;
      justify-content: space-between;
    }
  }
  .hasImg {
    max-width: calc(100% - 108px);
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
}
</style>
