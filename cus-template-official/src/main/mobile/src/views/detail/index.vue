<template>
  <div class="detail" :style="headerStyle">
    <card-layout
      :cardLayout="cardLayout"
      :templateCode="templateCode"
    ></card-layout>
  </div>
</template>

<script>
import CardLayout from "../cardLayout";
import common from "@/mixins/common";
export default {
  mixins: [common],
  components: {
    CardLayout
  },
  props: {
    pageData: Object
  },
  computed: {
    cardLayout() {
      return JSON.parse(this.pageData?.pageInfoEntity?.cardLayout || "[]");
    },
    templateCode() {
      return this.pageData?.pageInfoEntity?.templateCode;
    }
  }
};
</script>

<style lang="less" scoped>
.detail {
  padding-bottom: 60px;
}
</style>

<style lang="less">
.detail {
  .card-detailsofservice{
    .header{
      padding: 45px 16px 38px 16px;
      .hd1{
        padding-bottom: 7px;
      }
      .service-appraise{
        margin-top: 18px;
      }
    }
    .body{
      background: #fff; margin-bottom: 0;
    }
  }
  .detailOfOneThing{
    .Header{
      padding: 45px 16px 38px 16px;
      .hd1{
        padding-bottom: 7px;
      }
      .score_box{
        margin-top: 18px;
      }
    }
    .main_body{
      background: #fff; margin-bottom: 0;
    }
  }
}
</style>