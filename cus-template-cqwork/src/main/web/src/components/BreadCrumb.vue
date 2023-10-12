<template>
  <div class="breadcrumb__wrap" v-if="showBreadCrumb">
    <span
      class="breadcrumb-item portal-font-color-lv3"
      v-for="(item, index) in breadLists"
      :key="item.uri"
      @click="goPage(item.uri, index)"
    >
      {{ index ? "&nbsp;>&nbsp;" : ""
      }}<span :class="[index === breadLists.length - 1 ? '' : 'cursor']">{{
        item.nameChanged ? item.pageName : $t(item.i18nKey)
      }}</span>
    </span>
  </div>
</template>

<script>
export default {
  props: {
    pageRenderData: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  computed: {
    // 是否显示面包屑
    showBreadCrumb() {
      return (
        this.pageRenderData.pageConfig &&
        this.pageRenderData.pageConfig["breadcrumb.show"] === "Y" &&
        this.breadLists.length > 1
      );
    },
    breadLists() {
      return this.pageRenderData.breadcrumb || [];
    },
  },
  mounted() {
    window.shell.on("update-breadcrumb", (name, index) => {
      if (!this.showBreadCrumb) return;
      let item = this.breadLists[this.breadLists.length - 1];
      if (index || index === 0) {
        if (this.breadLists[index]) {
          item = this.breadLists[index];
        }
      }
      this.$set(item, "pageName", name);
      this.$set(item, "nameChanged", true);
    });
  },
  methods: {
    goPage(to, index) {
      if (index === this.breadLists.length - 1) return;
      window.shell.openPage(to, 0, 1);
    },
  },
  beforeDestroy() {
    window.shell.off("update-breadcrumb");
  },
};
</script>

<style scoped>
.breadcrumb__wrap {
  margin-bottom: 20px;
}
.breadcrumb-item {
  font-size: 14px;
  line-height: 22px;
}
.cursor {
  cursor: pointer;
}
</style>
