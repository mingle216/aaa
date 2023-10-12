<!--
 * @Author: 杨芯悦 xyyang01@wisedu.com
 * @Date: 2023-05-19 10:17:17
 * @LastEditors: 杨芯悦 xyyang01@wisedu.com
 * @LastEditTime: 2023-05-23 09:58:01
-->
<template>
  <div class="hot-apps">
    <p class="flag">{{ $Lan(LanFunName, "unitOfHotapps", "单位 : 人次") }}</p>
    <div
      class="app-box"
      v-for="(item, index) in serviceList.slice(0, 10)"
      :key="index"
    >
      <div class="main-con">
        <span class="rank">{{ index + 1 }}</span>
        <img
          class="app-icon"
          :src="require('../../assets/screen/app' + (index + 1) + '.png')"
          alt=""
        />
        <p class="app-name">{{ item.serviceName }}</p>
      </div>
      <div class="app-nums">
        <span>{{ item.visit || 0 }}</span>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "hotApps",
  props: ["serviceList","LanFunName"],
  data() {
    return {
      dataList: [],
    };
  },
  mounted() {
  },
  methods: {
  },
};
</script>
<style lang="less" scoped>
.hot-apps {
  margin-top: 12px;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  align-content: space-between;

  .app-box {
    width: calc(50% - 16px);
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 20%;
    padding-right: 20px;

    .main-con {
      flex: 1;
      display: flex;
      align-items: center;
      width: 0;

      .rank {
        background: linear-gradient(
          154.98deg,
          #0bacff 11.05%,
          rgba(11, 172, 255, 0) 95.97%
        );
        border-radius: 16px;
        width: 22px;
        height: 22px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;
      }
      .app-icon {
        width: 28px;
        height: 28px;
        margin-right: 8px;
      }
      .app-name {
        flex: 1;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
        color: rgba(255, 255, 255, 0.8);
      }
    }
    .app-nums {
      font-size: 20px;
    }
  }

  .flag {
    position: absolute;
    top: 20px;
    right: 20px;
  }
}
</style>