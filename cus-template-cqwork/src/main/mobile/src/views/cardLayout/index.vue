<template>
  <rows :rowsData="cardLayout" v-bind="$attrs"></rows>
</template>

<script>
import rows from "./components/rows";

export default {
  name: "cardLayout",
  components: { rows },
  props: {
    cardLayout: Array,
  },
};
</script>
