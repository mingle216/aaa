<template>
  <div class="service-count">
    <div class="card">
      <img src="../../assets/screen/card1.png" alt="" />
      <p>
        <span class="name">{{ $Lan(LanFunName, "serviceCountItem", "进驻事项数") }}</span>
      </p>
      <p class="service-text">{{ formatNum(currentItem.count || 0) }}</p>
    </div>
    <div class="card card2">
      <img src="../../assets/screen/card2.png" alt="" />
      <p>
        <span class="name cursor">{{
          $Lan(LanFunName, "serviceCountInfo", "正在办理数/办理完成数")
        }}</span>
      </p>
      <p class="statistics">
        <span class="service-text">{{ formatNum(currentCount.count || 0) }}</span>
        <span> / {{ formatNum(doneCount.count || 0) }}</span>
      </p>
    </div>
  </div>
</template>
<script>
export default {
  name: "serviceCount",
  props: ["tdcCount", "LanFunName"],
  data() {
    return {
      dataList: [],
    };
  },
  mounted() {
    this.getData();
  },
  computed: {
    currentCount() {
      return (
        (this.dataList &&
          this.dataList.find((item) => {
            return item.dataId == "currentCount";
          })) ||
        {}
      );
    },
    doneCount() {
      return (
        (this.dataList &&
          this.dataList.find((item) => {
            return item.dataId == "doneCount";
          })) ||
        {}
      );
    },
    currentItem() {
      return (
        (this.dataList &&
          this.dataList.find((item) => {
            return item.dataId == "currentItem";
          })) ||
        {}
      );
    },
  },
  methods: {
    getData() {
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_MYCOUNT",
          cardWid: "7983081050388543",
          method: "render2",
        })
        .then((res) => {
          if (res.errcode === "0") {
            this.dataList = res.data.serviceItemList || [];
          }
        });
    },
    formatNum(num){
      return num.toString().replace(/(?=\B(\d{3})+$)/g, ",")
    }
  },
};
</script>
<style lang="less" scoped>
.service-count {
  height: 100%;
  .card {
    height: calc(50% - 6px);
    position: relative;
    padding: 8px 12px 13.6px;
    margin: 0;
    &:nth-of-type(1) {
      margin-bottom: 12px;
      background: linear-gradient(
        180deg,
        rgba(11, 172, 255, 0.72) 0%,
        rgba(11, 172, 255, 0.0001) 100%
      );
      mix-blend-mode: normal;
      border-radius: 8px;
    }
    &:nth-of-type(2) {
      background: linear-gradient(
        180deg,
        rgba(255, 207, 38, 0.72) 0%,
        rgba(255, 207, 38, 0.0001) 99.06%
      );
      mix-blend-mode: normal;
      border-radius: 8px;
    }
    img {
      width: 72px;
      height: 72px;
      position: absolute;
      bottom: 0;
      right: 12px;
    }
    .name {
      color: rgba(255, 255, 255, 0.8);
      &::before {
        content: "";
        display: inline-block;
        width: 6px;
        height: 6px;
        background: #00ddde;
        box-shadow: 0px 0px 4px rgba(0, 221, 222, 0.48);
        border-radius: 50%;
        vertical-align: middle;
        margin-right: 4px;
      }
    }
    .service-text {
      color: #ffffff;
      text-shadow: 0px 3px 4px #453326;
      font-size: 40px;
      font-weight: 600;
      margin-top: 8px;
    }

    .statistics {
      margin-top: 8px;
      font-size: 16px;
      color: #fff;
      .service-text {
        color: #ffb244;
        margin-top: 0;
      }
    }
  }
}
</style>
