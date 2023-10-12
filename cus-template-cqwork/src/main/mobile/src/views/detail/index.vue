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
      padding: 16px 4px 16px 4px;
      .service-appraise{
        margin-top: 14px;
      }
    }
    .body{
      border-radius: 24px; background: #fff; margin-bottom: 0;
    }
  }
  .detailOfOneThing{
    .Header{
      padding: 16px 4px 16px 4px;
      .score_box{
        margin-top: 14px;
      }
    }
    .main_body{
      border-radius: 16px; background: #fff; margin-bottom: 0; overflow: hidden;
      .blank_p{
        display: none;
      }
    }
  }
}
</style>