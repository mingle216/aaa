<template>
  <we-main style="padding-top: 12px" class="main__center">
    <slot></slot>
  </we-main>
</template>

<script>
export default {
  props: {
    cardArea: {
      type: String,
      default: "all",
    },
  },
};
</script>

<style scoped>
.main__center {
  width: 1200px;
  margin: 0 auto;
  background: #ffffff;
  border-radius: 8px;
  overflow: hidden;
  padding:0!important;
}
.main__default {
  overflow-x: hidden;
}
</style>