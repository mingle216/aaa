<template>
  <div class="popPreview" v-if="show">
    <div class="main">
      <img class="previewImg" :src="src" alt="">
    </div>
    <i @click="close" class="we-icon-circle-close closeP"></i>
  </div>
</template>

<script>
export default {
  data() {
    return {
      show: false,
      src: ''
    }
  },
  methods: {
    async init(src) {
      this.show = true;
      this.src = src;
    },
    close() {
      this.src = '';
      this.show = false;
    }
  }
}
</script>

<style lang="less" scoped>
.popPreview{
  position: fixed; width: 100%; height: 100%; background-color: rgba(0, 0, 0, 0.7); z-index: 3000; top: 0; left: 0; display: flex; justify-content: center; align-items: center;
  .main{
    width: 90%; height: 90%; display: flex; justify-content: center; align-items: center;
    .previewImg{
      max-width: 100%; max-height: 100%; height: auto;
    }
  }
  .closeP{
    position: absolute; top: 20px; right: 20px; font-size: 25px; color: white; cursor: pointer;
  }
}
</style>
