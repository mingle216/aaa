<!--
 * @Author: 杨芯悦 xyyang01@wisedu.com
 * @Date: 2023-05-19 10:17:17
 * @LastEditors: 杨芯悦 xyyang01@wisedu.com
 * @LastEditTime: 2023-05-30 16:55:16
-->
<template>
  <div class="message-platform">
    <p class="total-num">
      {{ $Lan(LanFunName, "numOfMsgplatform", "消息平台总数") }}
      {{ otherCount.msgSendCount || 0 }}
    </p>
    <div class="header">
      <p>{{ $Lan(LanFunName, "msgService", "消息服务") }}</p>
      <p>{{ $Lan(LanFunName, "msgTotal", "消息总数") }}</p>
    </div>
    <div class="container">
      <vue-seamless-scroll :data="dataList" :class-option="option">
        <div>
          <div class="line" v-for="(item, index) in dataList" :key="index">
            <p :title="item.appName">{{ item.appName }}</p>
            <p>{{ item.totalNum }}</p>
          </div>
        </div>
      </vue-seamless-scroll>
    </div>
  </div>
</template>
<script>
import vueSeamlessScroll from "vue-seamless-scroll";
export default {
  name: "messagePlatform",
  props: ["otherCount", "LanFunName"],
  components: {
    vueSeamlessScroll,
  },
  data() {
    return {
      dataList: [],
      total: 0,
    };
  },
  computed: {
    option() {
      return {
        step: 0.3, // 数值越大速度滚动越快
        limitMoveNum: 5, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      };
    },
  },
  watch: {
    // otherCount: {
    //   handler(val) {
    //     this.dataList = val.appMsgSendCount || [];
    //   },
    //   deep: true,
    // },
  },
  mounted() {
    // this.dataList = this.otherCount.appMsgSendCount || [];
    this.getData();
  },
  methods: {
    getData() {
      // window.shell.execCardMethod({
      //   cardId: "CUS_CARD_DP",
      //   cardWid: "5439364064588545",
      //   method: 'getAppMsgHour',
      // }).then(res=>{
      //   if(res.errcode === "0"){
      //     if(res.data.code === "0"){
      //       this.dataList = res.data.data || [];
      //     }
      //   }
      // });
      let date = new Date();
      let y = date.getFullYear();
      let m = (date.getMonth() + 1).toString().padStart(2, "0");
      let d = date.getDate().toString().padStart(2, "0");
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_DP",
          cardWid: "5439364064588545",
          method: "getAppMsgHour",
          param: {
            startTime: `2023-01-01`,
            endTime: `${y}-${m}-${d}`,
            topCount: 5,
          },
        })
        .then((res) => {
          console.log(res);
          if (res.errcode === "0") {
            this.dataList = res.data.data || [];
            // this.total = res.data.data.msgSendCount || 0;
          }
        });
    },
  },
};
</script>
<style lang="less" scoped>
.message-platform {
  margin-top: 12px;
  display: flex;
  flex-direction: column;
  .header {
    border-radius: 8px 8px 0px 0px;
    background: rgba(6, 60, 255, 0.2);
    display: flex;
    p {
      color: rgba(255, 255, 255, 0.8);
      font-size: 14px;
      line-height: 36px;
      flex: 1;
      padding: 0 30px;
      &:first-of-type {
        border-right: 1px solid rgba(6, 60, 255, 0.2);
      }
    }
  }

  .container {
    height: calc(100% - 36px);
    overflow: hidden;
    .line {
      display: flex;
      background: rgba(0, 0, 0, 0.09);
      border: 1px solid rgba(6, 60, 255, 0.2);
      p {
        color: rgba(255, 255, 255, 0.8);
        font-size: 14px;
        line-height: 36px;
        flex: 1;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
        padding: 0 30px;
        &:first-of-type {
          border-right: 1px solid rgba(6, 60, 255, 0.2);
        }
      }
    }
  }
}
.total-num {
  position: absolute;
  top: 20px;
  right: 20px;
}
</style>
