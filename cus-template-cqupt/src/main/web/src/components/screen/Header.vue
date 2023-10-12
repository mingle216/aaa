<template>
  <div class="top-header">
    <div class="logo">
      <img src="../../assets/screen/logo.png" alt="" />
    </div>
    <!-- <img class="title" src="../../assets/screen/title.png" alt="" /> -->
    <span class="title">{{
      $Lan(LanFunName, "screenTitle", "一网通办服务效能概览")
    }}</span>
    <div class="date-box">
      <span>{{ time }}</span>
      <span>{{ date }}</span>
      <span>{{ week }}</span>
    </div>
  </div>
</template>
<script>
export default {
  name: "topHeader",
  props: ["LanFunName"],
  data() {
    return {
      time: "",
      Timer: null,
      date: "",
      week: "",
    };
  },
  mounted() {
    this.getTime();
    this.getDate();
    this.getWeek();
    this.Timer = setInterval(() => {
      this.getTime();
    }, 1000);
  },
  destroyed() {
    clearInterval(this.Timer);
  },
  methods: {
    getTime() {
      const d = new Date();
      this.time = `${d
        .getHours()
        .toString()
        .padStart(2, "0")} : ${d
        .getMinutes()
        .toString()
        .padStart(2, "0")} : ${d.getSeconds().toString().padStart(2, "0")}`;
    },
    getDate() {
      const d = new Date();
      this.date = `${d.getFullYear()}.${(d.getMonth() + 1)
        .toString()
        .padStart(2, "0")}.${d.getDate().toString().padStart(2, "0")}`;
    },
    getWeek() {
      const d = new Date();
      switch (d.getDay()) {
        case 0:
          this.week = "星期日";
          break;
        case 1:
          this.week = "星期一";
          break;
        case 2:
          this.week = "星期二";
          break;
        case 3:
          this.week = "星期三";
          break;
        case 4:
          this.week = "星期四";
          break;
        case 5:
          this.week = "星期五";
          break;
        case 6:
          this.week = "星期六";
          break;
        default:
          this.week = "星期日";
          break;
      }
    },
  },
};
</script>
<style lang="less" scoped>
.top-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  flex-shrink: 0;
  .logo {
    width: 25%;
    height: 100%;
    display: flex;
    align-items: flex-end;
    img {
      height: 45px;
    }
    span {
    }
  }
  .title {
    // width: 37%;
    font-size: 40px;
    line-height: 42px;
    text-align: center;
    letter-spacing: 10px;
    background: linear-gradient(180deg, #f1f8ff 33.33%, #96caff 131.94%);
    background-clip: text;
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    text-fill-color: transparent;
    text-shadow: 2px 2px 4px rgba(2, 39, 67, 0.1);
    color: transparent;
    font-weight: 600;
  }
  .date-box {
    width: 25%;
    height: 100%;
    display: flex;
    align-items: flex-end;
    justify-content: flex-end;
    color: #d5dadd;
    padding-bottom: 18px;

    span {
      display: inline-block;
      &:not(:last-of-type) {
        padding-right: 16px;
        margin-right: 16px;
        // border-right: 1px dashed #45c6f0;
        background-image: url(../../assets/screen/border.png);
        background-size: 1px 16px;
        background-repeat: no-repeat;
        background-position: right center;
      }
    }
  }
}
</style>
