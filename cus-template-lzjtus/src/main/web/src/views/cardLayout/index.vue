<template>
  <rows :rowsData="cardLayout" :containerMargin="upDownMargin"></rows>
</template>

<script>
import rows from "./components/rows";

export default {
  name: "cardLayout",
  components: { rows },
  props: {
    cardLayout: Array,
    upDownMargin: {
      type: String,
      default: "0",
    },
  },
};
</script>
