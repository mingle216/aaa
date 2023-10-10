<template>
  <div class="page_num">
    {{ currentIndex + 1 }} / {{ total }}
  </div>
</template>

<script>
export default {
  props: {
    currentIndex: Number,
    total: Number
  }
}
</script>

<style lang="less">
  .page_num {
    color: #fff;
    position: absolute;
    right: 20px;
    bottom: 15px;
  }
</style>
