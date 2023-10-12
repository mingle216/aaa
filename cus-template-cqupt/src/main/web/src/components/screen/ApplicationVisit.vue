<template>
  <div class="chart" ref="myChart"></div>
</template>
<script>
// import axios from "axios";
export default {
  name: "applicationVisit",
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
    this.daysArr = daysArr; // 本月天数数组
    this.getData();
  },
  methods: {
    getData() {
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_DP",
          cardWid: "5439364064588545",
          method: "getPortalAccess",
          param: {
            startTime: `${this.startDay.replaceAll("-", "")}`,
            endTime: `${this.endDay.replaceAll("-", "")}`,
          },
        })
        .then((res) => {
          if (res.errcode === "0") {
            if (res.data.code === "0") {
              // 筛选当前月份的数据
              let data1 =
                res.data.data.map((item) => {
                  return {
                    ...item,
                    date: item.date_wid.substring(6),
                  };
                }) || [];
              let respArr = [];
              // 补充当月天数没有数据的日期
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
                    count: obj.visit,
                  });
                }
              });
              this.getSeviceAccess(respArr);
            }
          }
        });
    },
    getSeviceAccess(arr1) {
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_DP",
          cardWid: "5439364064588545",
          method: "getSeviceAccess",
          param: {
            startTime: `${this.startDay.replaceAll("-", "")}`,
            endTime: `${this.endDay.replaceAll("-", "")}`,
          },
        })
        .then((res) => {
          if (res.errcode === "0") {
            if (res.data.code === "0") {
              let monthStart = this.startDay.replaceAll("-", "");
              let data1 =
                res.data.data
                  .filter((lt) => {
                    return Number(lt.date_wid) >= Number(monthStart);
                  })
                  .map((item) => {
                    return {
                      ...item,
                      date: item.date_wid.substring(6),
                    };
                  }) || [];
              let arr2 = [];
              arr1.map((item) => {
                let obj = data1.find((lt) => {
                  return lt.date == item.date;
                });
                if (!obj) {
                  arr2.push({
                    ...item,
                    count2: 0,
                  });
                } else {
                  arr2.push({
                    ...item,
                    count2: obj.visit,
                  });
                }
              });
              this.dataList = arr2;
              this.drawChart();
            }
          }
        });
    },
    drawChart() {
      let xData = this.dataList.length ? this.dataList.map((v) => v.date) : [];
      let yData1 = this.dataList.length ? this.dataList.map((v) => v.count) : [];
      let yData2 = this.dataList.length ? this.dataList.map((v) => v.count2) : [];
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
          data: ["服务用户", "应用访问"],
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
              formatter(params) {
                return params;
              },
            },
            // boundaryGap: false,
          },
        ],
        yAxis: [
          {
            type: "value",
            name: "服务人数",
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
              show: false,
            },
            nameTextStyle: {
              color: "rgba(255,255,255,0.5)",
              padding: [0, 0, 0, -20],
            },
          },
          {
            type: "value",
            name: "访问人次",
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
              padding: [0, 0, 0, 20],
            },
          },
        ],
        series: [
          {
            name: "服务用户",
            type: "bar",
            tooltip: {
              valueFormatter: function (value) {
                return value;
              },
            },
            data: yData2,
            itemStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "#0BACFF",
                },
                {
                  offset: 1,
                  color: "rgba(41,241,250,0)",
                },
              ]),
            },
            label: {
              show: true,
              position: "top",
              color: "#ffffff",
              offset: [0, 10],
            },
          },
          {
            name: "应用访问",
            type: "line",
            yAxisIndex: 1,
            data: yData1,
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
