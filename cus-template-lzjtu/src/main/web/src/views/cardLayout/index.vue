<template>
  <rows :rowsData="cardLayout"></rows>
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
