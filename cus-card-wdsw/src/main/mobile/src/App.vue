<template>
  <div class="m-notifyPost" v-loading="loading">
    <Header :title="'学生情况'">
      <template slot="more">
        <div>查看更多</div>
      </template>
    </Header>
  </div>
</template>
<script>
import Header from './components/Header.vue';
export default {
  components: {
    Header
  },
  data() {
    return {
      loading: false
    }
  }
}
</script>
<style lang="less" scoped>
.m-notifyPost {
  margin: 12px 12px 0;
  background: #fff;
  box-shadow: 0px 2px 4px rgba(0, 49, 99, 0.04);
  border-radius: 8px;
}
</style>