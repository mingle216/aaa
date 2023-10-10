<template>
  <div class="faq-item" v-if="item">
    <div class="question portal-font-color-lv1">
      <span class="tag">{{ $Lan(lanFunName, "question", "问") }}：</span>

      <span v-html="question"> </span>
    </div>
    <div class="answer portal-font-color-lv2">
      <span class="tag">{{ $Lan(lanFunName, "answer", "答") }}：</span>
      <span v-html="answer"></span>
    </div>
    <div class="related portal-font-color-lv1">
      {{ $Lan(lanFunName, "responses", "对应事项") }}：
      <span class="related-right portal-primary-color-lv1" @click="goHandle(item)">
        {{ item.serviceItemName }}
      </span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    item: Object,
    keyword: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  computed: {
    question() {
      return window.shell.strHighlighted(this.item.question, this.keyword);
    },
    answer() {
      return window.shell.strHighlighted(this.item.answer, this.keyword);
    },
  },
  methods: {
    /**
     * 跳转
     */
    goHandle(item) {
      if(item.workGuide && 'isAuthorized' in item && item.isAuthorized === 0) {
        this.$message({ message: '无权限查看', type: 'warning' });
        return false;
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
          extraInfo: {
            infoType: 1,
            itemId: item.itemWid
          }
        },
        startTime: new Date().getTime()
      });
      window.shell.openServiceItem({
        wid: this.item.serviceItemWid,
        name: this.item.serviceItemName,
      });
    },
  },
};
</script>

<style lang="less" scoped>
.faq-item {
  padding: 16px 0;
  border-bottom: 1px dashed #e7edf1;
  width: 100%;
  .question {
    color: #102645;
    word-break: break-all;
    white-space: pre-wrap;
    margin-bottom: 8px;
    font-size: 16px;
    line-height: 24px;
  }
  .answer {
    color: #707d8f;
    line-height: 22px;
    word-break: break-all;
    white-space: pre-wrap;
    margin-bottom: 8px;
    font-size: 14px;
  }
  .related {
    width: 100%;
    color: #102645;
    font-size: 14px;
    line-height: 22px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    // .related-left {
    //   min-width: 76px;
    // }
    .related-right {
      // flex: 1;
      cursor: pointer;
      //  padding: 0 10px;
    }
  }
}
</style>
