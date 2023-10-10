<template>
  <we-tabs
    class="role-tab"
    v-model="tabIndex"
    @change="tabChange"
    swipe-threshold="1"
  >
    <we-tab
      class="role-info"
      v-for="(item, index) in tabInfo"
      :name="item.roleWid"
      :key="index"
      :title="
        $Lan(lanFunName, 'labelText', '{name}办事', { name: item.roleName })
      "
    >
      <CategoryTab
        v-bind="$attrs"
        :lan-fun-name="lanFunName"
        :roleWid="active.roleWid"
        :roleName="indexRoleName"
        :router="router"
        :categoryWid="active.categoryWid"
        v-on="$listeners"
      />
    </we-tab>
  </we-tabs>
</template>

<script>
import CategoryTab from "./CategoryTab";
export default {
  components: {
    CategoryTab,
  },
  props: {
    router: Object,
    active: {
      type: Object,
      default: () => {
        return {
          categoryWid: "",
          roleWid: "",
        };
      },
    },
    tabInfo: {
      type: Array,
      default: () => {
        return [];
      },
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  watch: {
    active: {
      immediate: true,
      handler(val) {
        this.tabIndex = val.roleWid;
      },
    },
  },
  computed: {
    indexRoleName() {
      let index = this.tabInfo.findIndex((item) => {
        return item.roleWid === this.tabIndex;
      });
      if (index !== -1) {
        return this.tabInfo[index].roleName || "";
      }
      return "";
    },
  },
  data() {
    return {
      tabIndex: "",
    };
  },
  methods: {
    tabChange(name) {
      this.$emit("role-change", name);
    },
  },
};
</script>

<style lang="less" scoped>
.role-tab {
  //   padding-top: 12px;
  //   display: flex;
  //   flex-flow: column;
  /deep/.we-tabs__wrap {
    height: 48px;
    border-bottom: 1px solid #e7edf1;
    &::before,
    &::after {
      content: "";
      position: absolute;
      z-index: 10;
      height: 46px;
      width: 17px;
      top: 0;
    }
    &::before {
      left: 0;
      background: linear-gradient(to right, #fff, rgba(255, 255, 255, 0));
    }
    &::after {
      right: 0;
      background: linear-gradient(to left, #fff, rgba(255, 255, 255, 0));
    }
    .we-tab {
      flex: none;
      height: 48px;
      line-height: 48px !important;
      font-size: 16px;
      margin-right: 10px;
      z-index: 1;
      padding: 0 3px;
      &:after {
        content: "";
        position: absolute;
        bottom: 0px;
        height: 3px;
        left: 3px;
        right: 3px;
      }
      &.we-tab--active {
        font-size: 18px;
      }
    }
    .we-tabs__line {
      display: none;
    }
    .we-tabs__nav {
      // box-sizing: border-box;

      padding: 0 17px 15px 17px;
      .we-tab__text {
        font-weight: bold !important;
      }
      .we-tab:nth-last-child(2) {
        padding-right: 20px !important;
        &:after {
          right: 23px;
        }
      }
    }
  }
}
</style>
