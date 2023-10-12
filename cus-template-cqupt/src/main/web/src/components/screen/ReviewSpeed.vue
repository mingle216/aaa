<template>
  <div class="review-speed">
    <p class="flag">{{ $Lan(LanFunName, "averageHour", "平均耗时") }}</p>
    <img class="speed-img" src="../../assets/screen/speed.png" alt="" />
    <div class="speed-content">
      <div v-for="(item, index) in dataList.slice(0, 5)" :key="index">
        <div class="flex">
          <img :src="icons[index]" alt="" />
          <span class="speed-name">{{ item.yymc }}</span>
        </div>
        <span class="hours">{{ item.clsc }} H</span>
      </div>
    </div>
  </div>
</template>
<script>
import speed1 from "../../assets/screen/speed1.png";
import speed2 from "../../assets/screen/speed2.png";
import speed3 from "../../assets/screen/speed3.png";
import speed4 from "../../assets/screen/speed4.png";
import speed5 from "../../assets/screen/speed5.png";
export default {
  name: "reviewSpeed",
  props: ["LanFunName"],
  data() {
    return {
      dataList: [],
      icons: [speed1, speed2, speed3, speed4, speed5],
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    getData() {
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_AVG_TIME",
          cardWid: "5968139505716161",
          method: "getTop5",
        })
        .then((res) => {
          if (res.errcode === "0") {
            if (res.data.code === "0") {
              this.dataList = res.data.data.sort(function(a,b){
                  return a.clsc-b.clsc
              }) || [];
            }
          }
        });
    },
  },
};
</script>
<style lang="less" scoped>
.review-speed {
  display: flex;
  padding-top: 12px;
  .speed-img {
    height: 100%;
    position: relative;
    z-index: 2;
  }
  @media screen and (max-width: 1460px) {
    .speed-img {
      width: 86px;
    }
  }
  .speed-content {
    flex: 1;
    margin-left: 10%;
    & > div {
      height: calc(100% / 5);
      display: flex;
      align-items: center;
      justify-content: space-between;
      position: relative;
      &::before {
        content: "";
        display: inline-block;
        height: 1px;
        border-top: 1px dashed rgba(255, 255, 255, 0.3);
        min-width: 12px;
        position: absolute;
        width: 64px;
        right: calc(100% + 4px);
        z-index: 0;
      }
      &:nth-of-type(1) {
        top: 4px;
      }
      &:nth-of-type(2) {
        top: 8px;
      }
      &:nth-of-type(3) {
        top: 10px;
      }
      &:nth-of-type(4) {
        top: 12px;
      }
      &:nth-of-type(5) {
        top: 12px;
        &::before {
          width: 72px;
        }
      }
      img {
        width: 28.8px;
      }
      .speed-name {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
        margin-left: 8px;
        max-width: 66%;
        // flex: 1;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
      }
      .hours {
        flex-shrink: 0;
      }
    }
    .flex {
      display: flex;
      align-items: center;
      margin-right: 10px;
      // max-width: 70%;
      flex: 1;
      position: relative;
      overflow: hidden;
      &::after {
        content: "";
        display: inline-block;
        height: 1px;
        border-top: 1px dashed rgba(255, 255, 255, 0.3);
        flex: 1;
        min-width: 12px;
        margin-left: 2px;
      }
    }
  }
  .flag {
    position: absolute;
    top: 20px;
    right: 20px;
  }
}
</style>
