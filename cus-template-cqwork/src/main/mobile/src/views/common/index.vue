<template>
  <div class="container" :style="headerStyle">
    <div class="common-head">
      <div class="text__wrap">
        <p class="title text__ellipsis" v-if="langTitle">
          <span>
            {{ langTitle }}
          </span>
        </p>
        <p class="sub__title text__ellipsis" v-if="langSubTitle">
          <span>
            {{ langSubTitle }}
          </span>
        </p>
      </div>
    </div>

    <div class="common-content" ref="commonContent">
      <card-layout :cardLayout="cardLayout" />
    </div>
  </div>
</template>

<script>
import common from "@/mixins/common";
import CardLayout from "../cardLayout";
export default {
  mixins: [common],
  components: {
    CardLayout
  },
  props: {
    pageData: Object
  }
};
</script>

<style lang="less" scoped>
.common-head {
  padding: 16px 0;
  .text__wrap {
    display: flex;
    flex-direction: column;
    padding: 0 16px;
    .title {
      color: #fff;
      font-size: 26px;
      font-weight: 600;
      line-height: 34px;
      letter-spacing: 0px;
      margin-bottom: 8px;
    }
    .sub__title {
      opacity: 0.8;
      color: #ffffff;
      font-size: 13px;
      line-height: 17px;
      letter-spacing: 0px;
    }
  }
}
</style>
