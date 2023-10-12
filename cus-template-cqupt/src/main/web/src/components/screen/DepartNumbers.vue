<template>
  <div class="dept-number" ref="limitHeight">
    <div class="column">
      <p class="dept-name">{{ (dataList[0] && dataList[0].bmmc) || "" }}</p>
      <p class="dept-num">{{ (dataList[0] && dataList[0].sl) || "" }}</p>
      <img src="../../assets/screen/dept1.png" alt="" />
    </div>
    <div class="column">
      <p class="dept-name">{{ (dataList[1] && dataList[1].bmmc) || "" }}</p>
      <p class="dept-num">{{ (dataList[1] && dataList[1].sl) || "" }}</p>
      <img src="../../assets/screen/dept2.png" alt="" />
    </div>
    <div class="column">
      <p class="dept-name">{{ (dataList[2] && dataList[2].bmmc) || "" }}</p>
      <p class="dept-num">{{ (dataList[2] && dataList[2].sl) || "" }}</p>
      <img src="../../assets/screen/dept3.png" alt="" />
    </div>
    <div class="column">
      <p class="dept-name">{{ (dataList[3] && dataList[3].bmmc) || "" }}</p>
      <p class="dept-num">{{ (dataList[3] && dataList[3].sl) || "" }}</p>
      <img src="../../assets/screen/dept4.png" alt="" />
    </div>
    <div class="column">
      <p class="dept-name">{{ (dataList[4] && dataList[4].bmmc) || "" }}</p>
      <p class="dept-num">{{ (dataList[4] && dataList[4].sl) || "" }}</p>
      <img src="../../assets/screen/dept5.png" alt="" />
    </div>
  </div>
</template>
<script>
export default {
  name: "departNumbers",
  props: ["otherCount"],
  data() {
    return {
      dataList: [],
    };
  },
  watch: {},
  mounted() {
    this.getData();
    window.onresize = () => {
      this.checkDom();
    };
  },
  methods: {
    getData() {
      const self = this;
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_DP",
          cardWid: "5439364064588545",
          method: "getDeptStatistics",
        })
        .then((res) => {
          if (res.errcode === "0") {
            if (res.data.code === "0") {
              this.dataList = res.data.data || [];
              setTimeout(()=>{
                self.checkDom();
              }, 500)
            }
          }
        });
    },
    checkDom() {
      let flag = 0;
      let arr = [];
      document.querySelectorAll(".column").forEach((item) => {
        arr.push(item.clientHeight);
        if (item.clientHeight > this.$refs.limitHeight.clientHeight) {
          flag = 1;
        }
      });
      if (flag) {
        let maxHeight = Math.max(...arr);
        let scale = Number(this.$refs.limitHeight.clientHeight / maxHeight);
        document.querySelectorAll(".column").forEach((item) => {
          item.style.transform = `scale(${scale})`;
          item.style.transformOrigin = "bottom";
        });
      } else {
        document.querySelectorAll(".column").forEach((item) => {
          item.style.transform = `scale(1)`;
        });
      }
    },
  },
};
</script>
<style lang="less" scoped>
.dept-number {
  display: flex;
  align-items: flex-end;
  text-align: center;
  .column {
    width: calc(100% / 5);
    padding: 0 10px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    img {
      width: 100%;
    }
    .dept-name {
      color: rgba(255, 255, 255, 0.8);
      overflow: hidden; //超出文本隐藏
      text-overflow: ellipsis; ///超出部分省略号显示
      display: -webkit-box; //弹性盒模型
      -webkit-box-orient: vertical; //上下垂直
      -webkit-line-clamp: 2;
      width: 100%;
    }
    .dept-num {
      color: rgba(255, 255, 255, 1);
      margin: 6px 0;
      font-size: 18px;
    }
  }
}
</style>
