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
import { clickTrack } from '../mixins/track.js';
export default {
  props: {
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
    lanFunName:String,
    router: Object
  },
  mixins: [clickTrack],
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
      this.handleClickTrack(); // 点击埋点
      if (this.indexWid != item.wid) {
        this.indexWid = item.wid;
        this.$emit("change", item);
      }
    },
  },
};
</script>

<style lang="less" scoped>
.source-list {
  /* background: pink; */
  // width: 162px;
  width: 140px;
  background: rgba(245, 245, 245, 1);
  // min-width: 140px;
  // background: #ffffff;
  box-shadow: inset -1px 0 0 0 #f1f2f4;
  padding: 20px 0;
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
    height: 40px;
    line-height: 40px;
    position: relative;
    cursor: pointer;
  }
  .active {
    // background: #f6f6f8;
    border-radius: 3px;
    font-weight: bold;
    background: rgba(234,234,234,1);
  }

  .active::before {
    content: "";
    width: 2px;
    height: 40px;
    position: absolute;
    top: 0;
    left: 0;
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