<template>
  <div class="ev-item">
    <div class="ev-top portal-font-color-lv3">
      <div class="ev-top-left">
        <we-rate
          v-model="score"
          disabled
          text-color="#ff9900"
          score-template="{value}"
        ></we-rate>
        <div class="score portal-font-color-lv3">{{ score.toFixed(1) }}</div>
      </div>
      <div class="ev-top-right portal-font-color-lv3">
        <span class="ev-u-name">{{ item.userName }}</span>
        <span class="ev-u-time">{{ item.createTime }}</span>
      </div>
    </div>
    <div class="score-group portal-font-color-lv3">
      <span class="score-item" v-for="citem in item.appraiseList" :key="citem.dimenWid">
        <span class="label-item ellipsis">{{ citem.dimenName }}</span>
        <span class="score-item-score">：{{ citem.dimenScore.toFixed(1) }}</span>
      </span>
      <!-- <span class="score-item">
        <span class="label-item ellipsis">
          {{
            nameDic["SERVICE_MANNER"] ||
            $Lan(lanFunName, "serviceManner", "服务态度")
          }}</span
        ><span class="score-item-score">：{{
          item.mannerScore.toFixed(1)
        }}</span></span
      >
      <span class="score-item">
        <span class="label-item ellipsis">{{
          nameDic["SERVICE_SPEED"] ||
          $Lan(lanFunName, "serviceSpeed", "办事速度")
        }}</span
        ><span class="score-item-score">：{{
          item.speedScore.toFixed(1)
        }}</span></span
      >
      <span class="score-item"
        ><span class="label-item ellipsis">{{
          nameDic["EVENT_INTEGRITY"] ||
          $Lan(lanFunName, "eventIntegrity", "信息完整度")
        }}</span
        ><span class="score-item-score">：{{
          item.integrityScore.toFixed(1)
        }}</span></span
      >
      <span class="score-item"
        ><span class="label-item ellipsis">{{
          nameDic["TOTAL_SATISFY"] ||
          $Lan(lanFunName, "totalSatisfy", "整体满意度")
        }}</span
        ><span class="score-item-score">：{{
          item.satusfyScore.toFixed(1)
        }}</span></span
      > -->
    </div>
    <div class="ev-text portal-font-color-lv1">
      {{ item.suggest || "" }}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    item: Object,
    appraiseName: {
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
  watch: {
    appraiseName: {
      handler(val) {
        if (val && val.length) {
          let obj = {};
          val.forEach((item) => {
            obj[item.code] = item.name;
          });
          this.nameDic = obj;
        }
      },
      immediate: true,
    },
    item: {
      handler(val) {
        if (val) {
          // this.score =
          //   (val.mannerScore +
          //     val.speedScore +
          //     val.integrityScore +
          //     val.satusfyScore) /
          //   4;
          this.score = val.appraiseList.map(v=> v.dimenScore).reduce((prev, cur)=> prev + cur, 0) / val.appraiseList.length;
          console.log(this.score);
        }
      },
      immediate: true,
    },
  },

  data() {
    return {
      score: 4,
      nameDic: {},
    };
  },
  methods: {},
};
</script>

<style lang="less" scoped>
.ev-item {
  padding: 16px 0;
  border-bottom: 1px solid #f5f5f5;
  // &:first-child{
  //     border-top: none;
  // }
  .ev-top {
    font-size: 14px;
    line-height: 22px;
    display: flex;
    margin-bottom: 8px;
    &.portal-font-color-lv3 {
      /deep/.we-rate .we-rate__text {
        font-weight: bold;
        color: inherit !important;
      }
    }
    .ev-top-left {
      flex: 1;
      display: flex;
      /deep/.we-rate {
        width: 100px;
        .we-rate__icon {
          font-size: 16px;
          margin-right: 3px;
        }
      }
      .score {
        flex: 1;
        font-weight: bold;
      }
    }
    .ev-top-right {
      width: 300px;
      text-align: right;
      .ev-u-name {
        margin-right: 12px;
      }
    }
  }
  .score-group {
    line-height: 22px;
    margin-bottom: 8px;
    .score-item {
      margin-right: 28px;
      .label-item{
        display: inline-block;
        // max-width: 80px;
        height: 16px;
        line-height: 16px;
        position: relative;
        top: 3px;
      }
      .score-item-score {
        font-weight: bold;
      }
    }
  }
  .ev-text {
    line-height: 22px;
  }
}
</style>
