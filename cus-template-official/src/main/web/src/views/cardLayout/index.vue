<template>
  <rows :rowsData="cardLayout" :pageData="pageData"></rows>
</template>

<script>
import rows from "./components/rows";

export default {
  name: "cardLayout",
  components: { rows },
  props: {
    cardLayout: Array,
    pageData: Array,
  },
};
</script>
