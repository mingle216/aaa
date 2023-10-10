<template>
  <img :src="rankImg[rank]" alt="" v-if="rank <= 3" />
  <div v-else class="rank_div">{{ rank }}</div>
</template>

<script>
export default {
  props: {
    rank: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      rankImg: {
        1: require("../../assets/images/1.png"),
        2: require("../../assets/images/2.png"),
        3: require("../../assets/images/3.png")
      }
    };
  }
};
</script>

<style lang="less" scoped>
.rank_div {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #f5f5f5;
  color: #8c8c8c;
  text-align: center;
  line-height: 20px;
  font-size: 12px;
}
</style>
