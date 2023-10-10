<template>
  <div class="container">
      
      <card-layout :cardLayout="cardLayout" />
     
  </div>
</template>

<script>
import CardLayout from "../cardLayout";
export default {
  name: 'Search',
  components: {
    CardLayout
  },
  props: {
    pageData: Object
  },
  computed: {
     cardLayout() {
          return JSON.parse(
            (this.pageData &&
              this.pageData.pageInfoEntity &&
              this.pageData.pageInfoEntity.cardLayout) ||
              "[]"
          );
        },
  }
}
</script>

<style scoped>
 
</style>
