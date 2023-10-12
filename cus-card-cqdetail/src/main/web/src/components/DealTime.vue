<template>
  <!--办理时间-->
  <div>
    <div class="title1 portal-font-color-lv1">
      {{ item.fieldName }}
    </div>
    <div class="detail-wrapper" v-html="item.fieldValue" @click="aClick"></div>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    item: {
      type: Object,
      default: () => {
        return {};
      },
    },
    fieldName: String,
  },
  methods: {
    aClick(event) {
      if(event.path.some(v=> v.tagName === 'A')) {
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
  }
};
</script>

<style lang="less" scoped></style>
