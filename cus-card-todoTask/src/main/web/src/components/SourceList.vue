<template>
  <div class="source-list portal-font-color-lv2">
    <AutoContainer
      :con-type="conType"
      :con-height="conHeight"
      :scroll="leftScroll"
    >
      <div
        v-for="item in sourceList"
        :key="item.wid"
        :class="[{ active: item.wid === indexWid }]"
        class="tab-item"
        @click="getList(item)"
      >
        <span
          class="tword"
          :class="[{ 'portal-primary-color-lv1': item.wid === indexWid }]"
          >{{ item.wid==='OTHER'?$Lan(lanFunName, "OTHER", "其他"): item.source_NAME }}</span
        >
        <!-- </we-tooltip> -->
        <span
          class="item-count portal-font-color-lv3"
          :style="item.count === '99+' ? 'right:1px' : ''"
          >{{ item.count }}</span
        >
      </div>
    </AutoContainer>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    conType: {
      type: [String,Number],
      default: "3",
    },
    conHeight: {
      type: [String,Number],
      default: "500",
    },
    sourceList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    lanFunName:String
  },
   watch:{
    sourceList:{
      handler:function(val){
          if (val?.length && this.indexWid === "") {
                this.indexWid = val[0].wid;
              }
      },
      immediate: true,
    }
  },
  data() {
    return {
      indexWid: "",
      leftScroll: { barKeepShow: true },
    };
  },
  methods: {
    getList(item) {
      if (this.indexWid != item.wid) {
        this.indexWid = item.wid;
        this.$emit("change", item);
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
      }
    },
  },
};
</script>

<style lang="less" scoped>
.source-list {
  /* background: pink; */
  width: 162px;
  // min-width: 140px;
  background: #ffffff;
  box-shadow: inset -1px 0 0 0 #f1f2f4;
  .tword {
    overflow: hidden;
    display: inline-block;
    width: 120px;
    white-space: nowrap;
    text-overflow: ellipsis;
    padding-left: 12px;
    box-sizing: border-box;
  }
  .tab-item {
    /* font-family: PingFangSC; */
    font-size: 16px;
    /* color: #707d8f; */
    letter-spacing: 0;
    text-align: justify;
    width: 100%;
    height: 48px;
    line-height: 48px;
    position: relative;
    cursor: pointer;
  }
  .active {
    background: #f6f6f8;
    border-radius: 3px;
    font-weight: bold;
  }

  .item-count {
    position: absolute;
    font-size: 14px;
    right: 12px;
  }
  .item:focus {
    outline: none;
  }
}
</style>