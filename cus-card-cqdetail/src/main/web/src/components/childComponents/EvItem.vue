<template>
  <div class="ev-item">
    <!-- 姓名 && 时间 -->
    <div class="ev-top">
      <div class="name portal-font-color-lv1">{{ item.userName }}</div>
      <div class="portal-font-color-lv3">{{ item.createTime }}</div>
    </div>
    <!-- 总评分 -->
    <div class="ev-score-box">
      <we-rate v-model="score" disabled text-color="#ff9900" score-template="{value}"></we-rate>
      <div style="color: #FFBC00;">{{ score.toFixed(1) }}</div>
    </div>
    <!-- 列表评分 -->
    <div class="score-group portal-font-color-lv3">
      <span class="score-item" v-for="citem in item.appraiseList" :key="citem.dimenWid">
        <span class="label-item ellipsis">{{ citem.dimenName }}</span>
        <span class="score-item-score">：{{ citem.dimenScore.toFixed(1) }}</span>
      </span>
    </div>
    <!-- 评论 -->
    <div v-show="item.suggest" class="ev-text portal-font-color-lv1">
      {{ item.suggest || "-" }}
    </div>
    <!-- 图片列表 -->
    <div class="imgList" v-if="item.appraisePics && item.appraisePics.length > 0">
      <div class="imgListBox" v-for="citem in item.appraisePics" :key="citem">
        <img :src="citem" alt="" style="width: 100%; height: 100%; object-fit: cover;">
        <div class="hover" @click="toPreview(citem)">
          <i class="we-icon-zoom-in zoomUp"></i>
        </div>
      </div>
    </div>
    <!-- 评价回复 -->
    <div class="evaBox" v-if="item.replyInfo">
      <div class="ty_flex ty_between ty_center_y">
        <div class="persons portal-font-color-lv1">
          <!-- <span>{{ item.replyInfo.replyName || '-' }}</span>
          <span style="padding: 0 3px;">{{ $Lan(lanFunName, 'toReply', '回复') }}</span>
          <span>{{ item.userName || '-' }}</span> -->
          <span>{{ $Lan(lanFunName, 'replyUser', '管理员回复：') }}</span>
        </div>
        <div class="times portal-font-color-lv3">{{ item.replyInfo.replyTime || '-' }}</div>
      </div>
      <div class="evaCon portal-font-color-lv2">{{ item.replyInfo.content || '-' }}</div>
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
  methods: {
    toPreview(citem) {
      this.$emit('toPreview', citem);
    }
  },
};
</script>

<style lang="less" scoped>
.imgList{
  margin-top: 8px; display: flex; align-items: center;
  .imgListBox{
    width: 90px; height: 90px; border-radius: 4px; margin-right: 8px; cursor: pointer; border: 1px solid #f1f1f1; overflow: hidden; position: relative;
  }
  .imgListBox:hover{
    .hover{
      display: flex; justify-content: center; align-items: center;
    }
  }
  .hover{
    width: 100%; height: 100%; background-color: rgba(0, 0, 0, 0.6); display: none; position: absolute; top: 0; left: 0;
    .zoomUp{
      color: white; font-size: 20px;
    }
  }
}
.evaBox{
  margin-top: 14px; padding: 16px; background-color: #F5F5F5; border-radius: 4px;
  .persons{
    color: #262626; font-size: 14px; font-weight: 700;
  }
  .times{
    color: #8C8C8C; font-size: 14px; font-weight: 400;
  }
  .evaCon{
    margin-top: 8px; color: #595959; font-size: 14px; line-height: 22px;
  }
}
.ty_flex{
  display: flex;
}
.ty_center_y{
  align-items: center;
}
.ty_center_x{
  justify-content: center;
}
.ty_between{
  justify-content: space-between;
}
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
    justify-content: space-between;
    &.portal-font-color-lv3 {
      /deep/.we-rate .we-rate__text {
        font-weight: bold;
        color: inherit !important;
      }
    }
    .name{
      color: #262626; font-size: 14px; font-weight: 700;
    }
  }
  .ev-score-box {
    display: flex; margin-top: 8px;
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
  .score-group {
    line-height: 22px;
    margin-top: 8px;
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
    line-height: 22px; color: #262626; margin-top: 8px;
  }
}
.ev-item:nth-of-type(1) {
  padding-top: 0;
}
</style>
