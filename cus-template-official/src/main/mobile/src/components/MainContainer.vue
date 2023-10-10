<template>
  <we-main :class="[cardArea === 'default' ? 'main__center' : '']">
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
  padding: 0 calc(50% - 37.5rem);
  min-width: 75rem;
  overflow: hidden;
  width: 0;
}
</style>