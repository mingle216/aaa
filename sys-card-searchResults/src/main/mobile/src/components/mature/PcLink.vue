<template>
  <div class="favTaskContent">
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
</template>

<script>
export default {
  props: ["pcViewUrl", "lanFunName"],
  data() {
    return {
      imgUrl: require("../../assets/PClink.png"),
    };
  },
  methods: {
    openPcView() {
      window.shell.openPage(this.pcViewUrl, 1, 2);
      this.$emit("closePc");
    },
  },
};
</script>

<style lang="less" scoped>
.favTaskContent {
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
