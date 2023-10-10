<template>
  <div class="empty-content" :style="{ height: `${height}px` }">
    <img class="empty-img" :src="img" />
    <div   class="tip-con">
       {{ tip }}
      <slot></slot>
    </div>
    
    
    
    
   
  </div>
</template>

<script>
export default {
  name: "Empty",
  components: {},
  directives: {},
  props: {
    /**
     * 为空时图标
     */
    img: {
      type: String,
      default: require("../../assets/images/empty.png"),
    },
    /**
     * 文字
     */
    tip: {
      type: String,
      default: "",
    },
    /**
     * 高度
     */
    height: {
      type: Number,
      default: 148,
    },
  },
  data() {
    return {};
  },
  mounted() {},
  methods: {},
};
</script>

<style lang="less" scoped>
.empty-content {
  height: 148px;
  width: 100%;
  text-align: center;
  padding: 19px 0 0 0;
  .empty-img {
    object-fit: contain;
    margin-bottom: 8px;
    width: 162px;
  }
  .tip-con {
    text-align: center;
    font-size: 14px;
    color: #9fa8b5;
    letter-spacing: 0;
    line-height: 18px;
  }
}
</style>