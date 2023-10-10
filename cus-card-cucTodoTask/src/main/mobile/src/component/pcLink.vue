<template>
  <we-popup
    v-model="isPcUrlShow"
    round
    position="bottom"
    :style="{ height: '80%' }"
  >
    <div class="todoTask__header">
      <span class="portal-font-color-lv1 title ellipsis">
        {{ $Lan(lanFunName, "popTitle", "跳转详情") }}
      </span>
      <span
        class="iconfont icon-danchuangclose portal-font-color-lv3"
        @click="isPcUrlShow = false"
      ></span>
    </div>
    <div class="todoTask__body">
      <div class="c-main">
        <div>
          <img :src="imgUrl" alt="icon" width="50" height="50" />
        </div>
        <h3 class="title portal-font-color-lv1">
          {{ $Lan(lanFunName, "pcTask", "当前为PC端地址") }}
        </h3>
        <div class="sub-title portal-font-color-lv2">
          {{ $Lan(lanFunName, "mobileOpen", "是否在移动端打开？") }}
        </div>
        <div>
          <a
            href="javascript:void(0);"
            class="open-btn portal-primary-backgroundcolor-lv1"
            @click="openPcView"
            >{{ $Lan(lanFunName, "openBtn", "打开") }}</a
          >
        </div>
      </div>
    </div>
  </we-popup>
</template>

<script>
export default {
  props: ["pcViewUrl", "lanFunName"],
  data() {
    return {
      imgUrl: require("../assets/PClink.png"),
      isPcUrlShow: false,
    };
  },
  methods: {
    show() {
      this.isPcUrlShow = true;
    },
    openPcView() {
      window.shell.openPage(this.pcViewUrl, 1, 2);
      this.isPcUrlShow = false;
    },
  },
};
</script>

<style lang="less" scoped>
.todoTask__header {
  padding: 0 17px;
  background: rgba(0, 0, 0, 0.0001);
  box-shadow: inset 0px -1px 0px #e7edf1;
  height: 56px;
  display: flex;
  align-items: center;
  .title {
    font-weight: bold;
    font-size: 18px;
    line-height: 22px;
    flex: 1;
  }
}
.todoTask__body {
  height: calc(100% - 56px);
  display: flex;
  justify-content: center;
  align-items: center;

  .c-main {
    text-align: center;
  }
  .title {
    font-size: 20px;
    color: #102645;
    font-weight: bold;
    margin-top: 22px;
  }

  .sub-title {
    margin-top: 8px;
    font-size: 14px;
    color: #707d8f;
  }
  .open-btn {
    display: block;
    width: 275px;
    height: 40px;
    line-height: 40px;
    border-radius: 4px;
    font-size: 16px;
    color: #ffffff;
    margin-top: 32px;
    text-align: center;
  }
}
</style>
