<template>
  <div style="height: 100%; position: relative">
    <img class="icon" :src="giftImageUrl" alt="" />
    <div class="chart" ref="myChart"></div>
  </div>
</template>
<script>
import giftImageUrl from "../../assets/screen/icon1.png";
import { loopShowTooltip } from "./tooltip-carousel";
export default {
  name: "browserAnalysis",
  props: ["otherCount"],
  data() {
    return {
      myChart: null,
      option: {},
      dataList: [],
      timer: null,
      toolTipTimer: null,
      giftImageUrl,
    };
  },
  watch: {
    otherCount: {
      handler(val) {
        this.dataList =
          val.browseCountData.filter((item) => {
            return item.platform_wid == "0";
          }) || [];
        this.drawChart();
      },
      deep: true,
    },
  },
  mounted() {
    this.dataList =
      (this.otherCount.browseCountData &&
        this.otherCount.browseCountData.filter((item) => {
          return item.platform_wid == "0";
        })) ||
      [];
    this.drawChart();
  },
  methods: {
    drawChart() {
      let data = this.dataList.length
        ? this.dataList.map((v) => {
            return {
              name: v.browser_name,
              value: v.visits,
            };
          })
        : [];
      let legendData = this.dataList.length
        ? this.dataList.map((v) => {
            return v.browser_name;
          })
        : [];
      this.option = {
        tooltip: {
          trigger: "item",
          formatter: "{b} <br/>{c} ({d}%)",
        },
        color: [
          "#0089EE",
          "#17BF6A",
          "#FF9900",
          "#EE3F15",
          "#EC38A4",
          "#7A2EE5",
          "#1DBBBB",
          "#BFBFBF",
          "#1890FF",
          "#FFD15C",
          "#29F1FA",
          "#7eacea",
          "#e15777",
          "#95ea71",
          "#ea9b4f",
          "#7577df",
          "#be72d8",
          "#fff",
          "#69cce6",
          "#ff8282",
        ],
        legend: {
          data: legendData,
          // right: 0,
          left: "65%",
          icon: "circle",
          top: "center",
          itemWidth: 8,
          itemHeight: 8,
          itemGap: 14,
          textStyle: {
            color: "#FFFFFF",
          },
          orient: "vertical",
          type: "scroll",
          pageIconColor: "#fff",
          selectedMode: false,
          pageTextStyle: {
            color: "#fff",
          },
        },
        graphic: {
          elements: [
            // {
            //   type: "image",
            //   style: {
            //     image: giftImageUrl,
            //     width: 32,
            //     height: 32,
            //   },
            //   left: "21%",
            //   top: "center",
            // },
            // {
            //   type: "image",
            //   style: {
            //     image: bgImage,
            //     width: utils.resizeChart(135),
            //     height: utils.resizeChart(135),
            //   },
            //   left: '5%',
            //   top: "center",
            // },
          ],
        },
        grid: {
          // containLabel: true,
        },
        series: [
          {
            name: "浏览器分析",
            type: "pie",
            radius: ["30%", "50%"],
            center: ["28%", "50%"],
            data: data,
            label: {
              normal: {
                show: false,
              },
            },
          },
          {
            type: "pie",
            radius: ["70%", "72%"],
            center: ["28%", "50%"],
            hoverAnimation: false,
            clockWise: false,
            itemStyle: {
              normal: {
                shadowBlur: 10,
                shadowColor: "#05A9F9",
                color: "#182476",
              },
            },
            label: {
              show: false,
            },
            data: [100],
          },
          {
            type: "pie",
            radius: ["26%", "27%"],
            center: ["28%", "50%"],
            hoverAnimation: false,
            clockWise: false,
            itemStyle: {
              normal: {
                color: "#007CEB",
              },
            },
            label: {
              show: false,
            },
            data: [100],
          },
          {
            type: "pie",
            radius: ["60%", "60%"],
            center: ["28%", "50%"],
            hoverAnimation: false,
            clockWise: false,
            itemStyle: {
              normal: {
                color: "rgba(0,0,0,0)",
                borderType: "dashed",
                borderWidth: 1,
                borderColor: "#007CEB",
                borderDashOffset: 4,
              },
            },
            label: {
              show: false,
            },
            data: [100],
          },
        ],
      };

      this.myChart = this.$echarts.init(this.$refs.myChart);
      this.myChart.clear();
      this.myChart.setOption(this.option);
      // 可调用clearLoop方法，清除定时器
      this.tootipTimer && this.tootipTimer.clearLoop();
      this.tootipTimer = null;
      // 调用轮播的方法
      this.tootipTimer = loopShowTooltip(this.myChart, this.option, {
        interval: 2000, // 轮播间隔时间
        loopSeries: true, // 是否开启轮播循环
      });
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
  height: 100%;
}
.icon {
  width: 32px;
  height: 32px;
  position: absolute;
  top: 50%;
  left: 28%;
  transform: translate(-50%, -50%);
}
</style>
