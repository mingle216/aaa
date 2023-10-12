<template>
  <rows :rowsData="cardLayout" :todoNum="todoNum"></rows>
</template>

<script>
import rows from "./components/rows";

export default {
  name: "cardLayout",
  components: { rows },
  props: {
    cardLayout: Array,
    todoNum: Number
  }
};
</script>
