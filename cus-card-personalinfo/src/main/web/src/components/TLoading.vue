
<template>
  <div class="card__personalData__loading inline-block width-full" @click="handlerClick">
    <div v-if="loading" class="sign inline-block"><img :src="loadingGif" alt=""/></div>
    <slot v-else></slot>
  </div>
</template>

<script>
import loadingGif from '../assets/loading.gif'
export default {
  props:['loading'],
  data(){
    return{
      loadingGif
    }
  },
  methods:{
    handlerClick(){
      if(!this.loading){
        this.$emit('click')
      }
    }
  }
}
</script>
<style scoped lang="less">
.card__personalData__loading {
  .sign {
    height: 14px;
    overflow: hidden;
    img {
      margin-top: -12px;
      height: 40px;
    }
  }
}
.inline-block {
  display: inline-block;
}
</style>