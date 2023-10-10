<template>
  <div class="card-config">
    <remote-component :url="getCardConfigUrl()" @submit="handleSubmit"/>
  </div>
</template>

<script>
import RemoteComponent from '../../components/RemoteComponent'

export default {
  props: {
    cardId: String,
  },
  components: {RemoteComponent},
  methods: {
    handleSubmit(value) {
      alert('submit', value)
    },
    getCardConfigUrl() {
      // return this.cardId + "/pc/js/config.js"
      if (process.env.NODE_ENV === 'development') {
        return "/" + this.cardId + "/js/config.js";
      } else {
        return "/" + this.cardId + "/pc/js/config.js";
      }
    },
  },
  created() {
  }
}
</script>

<style scoped>
.card-config {
  padding: 20px;
}
</style>
