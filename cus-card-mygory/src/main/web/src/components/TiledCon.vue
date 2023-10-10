<template>
  <div class="tiled-con" v-if="serviceList.length">
    <template v-for="item in serviceList">
      <div :key="item.roleWid" class="tiled-row">
        <div class="tiled-header portal-font-color-lv1">
          {{
            $Lan(lanFunName, "labelText", "{name}办事", { name: item.roleName })
          }}
        </div>
        <category
          :categoryList="categoryList"
          :categoryWid="item.categoryWid"
          :lan-fun-name="lanFunName"
          :router="router"
          @category-change="
            (ev) => {
              categoryHandle(ev, item.roleWid);
            }
          "
        ></category>
        <serviceList
          v-loading="loading && active.roleWid === item.roleWid"
          :isDept="item.categoryWid === 'dept-subject'"
          @clickItem="
            (s_item) => {
              clickItemHandle(s_item, item.roleWid, item.categoryWid);
            }
          "
          :dataList="item.itemModelList"
          :config="config"
        />
      </div>
    </template>
  </div>
</template>

<script>
import category from "./common/Category";
import serviceList from "./common/ServiceList";
export default {
  components: {
    category,
    serviceList,
  },
  props: {
    active: Object,
    config: Object,
    categoryList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    tabList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    serviceList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    loading: {
      type: Boolean,
      default: false,
    },
    lanFunName: {
      type: String,
      default: "",
    },
    router: Object,
  },
  computed: {},
  methods: {
    categoryHandle(ev, roleWid) {
      this.$emit("category-change", { categoryWid: ev, roleWid: roleWid });
    },
    clickItemHandle(item, roleWid, categoryWid) {
      this.$emit("clickItem", {
        subjectWid: item.subjectWid,
        roleWid: roleWid,
        categoryWid: categoryWid,
      });
    },
  },
  created() {
    console.log(this.lanFunName + "儿子");
  },
};
</script>

<style lang="less" scoped>
.tiled-con {
  .tiled-row:not(:last-child) {
    margin-bottom: 24px;
  }
  .tiled-header {
    height: 42px;
    line-height: 42px;
    border-bottom: 1px solid #f0f0f0;
    font-size: 14px;
    font-weight: bold;
  }
}
</style>
