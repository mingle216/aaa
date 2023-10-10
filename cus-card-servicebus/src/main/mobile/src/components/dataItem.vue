<template>
  <div>
    <div v-if="appData.length">
      <template v-if="mobileConfig.serviceBusDisplayNumSelect == '1'">
        <app-item
          styleclass="app"
          :appData="appData"
          :maxappsnum="maxappsnum"
          :router="router"
          @handleFavorite="handleFavorite"
          @getFavoriteIcon="getFavoriteIcon"
        ></app-item>
      </template>
      <template v-if="mobileConfig.serviceBusDisplayNumSelect == '2'">
        <app-item
          styleclass="app-line"
          :appData="appData"
          :maxappsnum="maxappsnum"
          :router="router"
          @handleFavorite="handleFavorite"
          @getFavoriteIcon="getFavoriteIcon"
        ></app-item>
      </template>
      <div
        class="more portal-primary-color-lv1"
        v-if="appData.length > maxappsnum"
        @click="handleMore"
      >
        {{$Lan(LanFunName, 'more', '查看全部')}} <we-icon name="arrow" />
      </div>
    </div>
  </div>
</template>

<script>
import appItem from "./appItem.vue";
export default {
  components: { appItem },
  props: {
    appData: {
      type: Array,
      default: () => {
        return [];
      },
    },
    mobileConfig: {
      type: Object,
    },
    maxappsnum: {
      type: Number,
    },
    LanFunName:String,
    router: Object,
  },
  data() {
    return {};
  },
  methods: {
    handleFavorite(param) {
      this.$emit("handleFavorite", param);
    },
    getFavoriteIcon(app) {
      this.$emit("getFavoriteIcon", app);
    },
    handleMore() {
      this.$emit("handleMore");
    },
  },
};
</script>

<style lang="less" scoped>
.more {
  width: 100%;
  height: 48px;
  background: #f9fafb;
  border-radius: 4px;
  letter-spacing: 0;
  text-align: right;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  margin-top: 12px;
}
</style>