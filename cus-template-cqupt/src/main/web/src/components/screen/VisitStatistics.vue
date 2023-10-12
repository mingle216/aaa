<template>
  <div class="visit-statistics">
    <div>
      <p>{{ $Lan(LanFunName, "visitTotal", "访问总人次") }}</p>
      <span class="num-box" v-for="(item, index) in total" :key="index">{{ item }}</span
      ><span class="unin">{{ $Lan(LanFunName, "visitUnin", "人次") }}</span>
    </div>
    <div class="visit-nums">
      <div>
        <img src="../../assets/screen/t1.png" alt="" />
        <span class="type">{{ $Lan(LanFunName, "todayActive", "今日活跃") }}</span>
        <span class="num1">{{ formatNum(visit.todayStatistics || 0) }}</span>
      </div>
      <div>
        <img src="../../assets/screen/t2.png" alt="" />
        <span class="type">{{ $Lan(LanFunName, "todayVisit", "浏览量(PV)") }}</span>
        <span class="num2">{{ formatNum(portalLogin.portalLoginUser || 0) }}</span>
      </div>
      <div>
        <img src="../../assets/screen/t3.png" alt="" />
        <span class="type">{{ $Lan(LanFunName, "monthActive", "本月活跃") }}</span>
        <span class="num3">{{ formatNum(visit.monthStatistics || 0) }}</span>
      </div>
      <div>
        <img src="../../assets/screen/t4.png" alt="" />
        <span class="type">{{ $Lan(LanFunName, "mounthVisit", "访问次数") }}</span>
        <span class="num4">{{ formatNum(monthVisit || 0) }}</span>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "visitStatics",
  props: ["portalLogin", "LanFunName", "otherCount", "startDay", "endDay"],
  data() {
    return {
      total: ["0"],
      visit: {},
      monthVisit: 0,
    };
  },
  watch: {
    portalLogin: {
      handler(val) {
        this.total =
          (val.potalLoginTotalCount &&
            val.potalLoginTotalCount
              .toString()
              .replace(/(?=\B(\d{3})+$)/g, ",")
              .split("")) ||
          [];
      },
      deep: true,
    },
    otherCount: {
      handler(val) {
        this.visit = val;
      },
      deep: true,
    },
  },
  mounted() {
    this.total =
      this.portalLogin.potalLoginTotalCount &&
      this.portalLogin.potalLoginTotalCount
        .toString()
        .replace(/(?=\B(\d{3})+$)/g, ",")
        .split("");
    this.visit = this.otherCount;
    // 访问次数
    this.getData();
  },
  methods: {
    formatNum(num) {
      return num.toString().replace(/(?=\B(\d{3})+$)/g, ",");
    },
    getData() {
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_DP",
          cardWid: "5439364064588545",
          method: "getPortalCount",
          param: {
            startTime: `${this.startDay.replaceAll("-", "")}`,
            endTime: `${this.endDay.replaceAll("-", "")}`,
          },
        })
        .then((res) => {
          if (res.errcode === "0") {
            if (res.data.code === "0") {
              let data1 = res.data.data;
              this.monthVisit = data1 && data1.length && data1[0].usercount || 0;
            }
          }
        });
    },
  },
};
</script>
<style lang="less" scoped>
.visit-statistics {
  width: 100%;
  height: 100%;
  padding: 16px 14px 20px 40px;
  display: flex;
  flex-direction: column;

  .num-box {
    background: linear-gradient(
      90deg,
      rgba(0, 0, 0, 0.0001) 0%,
      rgba(0, 0, 0, 0.195168) 51.92%,
      rgba(0, 0, 0, 0.0001) 100%
    );
    box-shadow: inset 0px 0px 10px 0.4px #3189fb;
    border-radius: 2px;
    padding: 8px 6px;
    display: inline-block;
    font-size: 40px;
    margin-top: 20px;
    margin-right: 6px;
    width: 32px;
    text-align: center;
  }
  .unin {
    vertical-align: middle;
    font-size: 16px;
  }

  .visit-nums {
    display: flex;
    flex-wrap: wrap;
    align-items: flex-end;
    align-content: flex-end;
    flex: 1;
    & > div {
      width: 50%;
      display: flex;
      align-items: center;
      margin-top: 16px;

      img {
        width: 28px;
        height: 28px;
      }

      .type {
        margin: 0 4px;
      }

      .num1 {
        color: #19d4ff;
        font-size: 24px;
      }
      .num2 {
        color: #54a7ff;
        font-size: 24px;
      }
      .num3 {
        color: #ffcf26;
        font-size: 24px;
      }
      .num4 {
        color: #1cc69f;
        font-size: 24px;
      }
    }
  }
}
@media screen and (max-height: 1000px) {
  .visit-statistics {
    transform: scale(0.9);
    transform-origin: top left;
    width: 140%;
  }
}

@media screen and (max-height: 870px) {
  .visit-statistics {
    transform: scale(0.8);
    transform-origin: top left;
  }
}
</style>
