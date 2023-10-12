<template>
  <div class="chart" ref="myChart"></div>
</template>
<script>
export default {
  name: "hallNumber",
  props: ["startDay", "endDay"],
  data() {
    return {
      myChart: null,
      option: {},
      dataList: [],
      timer: null,
      daysArr: [],
    };
  },
  mounted() {
    let date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let daysCount = new Date(year, month, 0).getDate(); // 本月多少天
    let daysArr = [];
    for (let i = 1; i <= daysCount; i++) {
      daysArr.push(i.toString().padStart(2, "0"));
    }
    this.daysArr = daysArr;
    this.getData();
    // this.drawChart();
  },
  methods: {
    getData() {
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_DP",
          cardWid: "5439364064588545",
          method: "getTdcTask",
          param: {
            startTime: `${this.startDay}`,
            endTime: `${this.endDay}`,
          },
        })
        .then((res) => {
          if (res.errcode === "0") {
            if (res.data.code === "0") {
              let data1 =
                res.data.data.map((item) => {
                  return {
                    ...item,
                    date: item.create_time.substring(8),
                  };
                }) || [];
              let respArr = [];
              this.daysArr.map((item) => {
                let obj = data1.find((lt) => {
                  return lt.date == item;
                });
                if (!obj) {
                  respArr.push({
                    date: item,
                    count: 0,
                  });
                } else {
                  respArr.push({
                    date: obj.date,
                    count: obj.count,
                  });
                }
              });
              this.dataList = respArr;
              this.drawChart();
            }
          }
        });
    },
    drawChart() {
      let xData = this.dataList.length ? this.dataList.map((v) => v.date) : [];
      let yData = this.dataList.length ? this.dataList.map((v) => v.count) : [];
      this.option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            crossStyle: {
              color: "#999",
            },
          },
        },
        grid: {
          top: 80,
          bottom: 10,
          right: 20,
          left: 20,
          containLabel: true,
        },
        legend: {
          data: ["应用业务数量"],
          right: 20,
          top: 20,
          itemWidth: 9,
          itemHeight: 5,
          textStyle: {
            color: "#FFFFFF",
          },
        },
        xAxis: [
          {
            type: "category",
            data: xData,
            axisLine: {
              lineStyle: {
                color: "rgba(255, 255, 255, 0.15)",
                type: "solid",
              },
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              // interval: 0,
              textStyle: {
                color: "rgba(255,255,255,0.5)",
              },
            },
            // boundaryGap: false,
          },
        ],
        yAxis: [
          {
            type: "value",
            name: "业务数",
            axisTick: {
              show: false,
            },
            axisLine: {
              show: false,
            },
            axisLabel: {
              textStyle: {
                color: "rgba(255,255,255,0.5)",
              },
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: "rgba(255, 255, 255, 0.15)",
              },
            },
            nameTextStyle: {
              color: "rgba(255,255,255,0.5)",
              padding: [0, 0, 0, -40],
            },
          },
        ],
        series: [
          {
            name: "应用业务数量",
            type: "line",
            data: yData,
            symbolSize: 8,
            symbol: "circle",
            smooth: true,
            lineStyle: {
              color: "#FFAE26",
            },
            itemStyle: {
              normal: {
                color: "#FFAE26",
                borderColor: "#391B05",
              },
            },
            label: {
              show: true,
              position: "top",
              color: "#ffffff",
              offset: [0, 0],
            },
          },
        ],
      };

      this.myChart = this.$echarts.init(this.$refs.myChart);
      this.myChart.clear();
      this.myChart.setOption(this.option);
      window.addEventListener("resize", () => {
        this.myChart.resize();
      });
    },
  },
  beforeDestroy() {
    window.removeEventListener("resize", () => {
      this.myChart.resize();
    });
    clearInterval(this.timer);
  },
};
</script>
<style lang="less" scoped>
.chart {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>
