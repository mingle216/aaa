<template>
  <div
    ref="serviceItem"
    v-if="dataItem && config"
    class="ser-item   portal-primary-backgroundcolor-hover-lv5 portal-font-color-lv1"
    :class="{ 'has-icon': isShowIcon }"
    @click="clickItem"
  >
    <div class="show-icon" v-if="isShowIcon">
      <div class="icon-con">
        <i class="item-icon-font iconfont icon-dep-Clf-Other"></i>
      </div>
      <div class="title">{{ dataItem.subjectName }}</div>
    </div>
    <div class="none-icon" v-else>
      <div class="title">
        {{ dataItem.subjectName }}
        <i class="arrow iconfont icon-shixiangfenleijiantou"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    dataItem: {
      type: Object,
      default: () => {
        return {
          secondCategory: "",
          subjectName: "",
          picLink: "",
          iconLink: "",
          itemList: [],
        };
      },
    },

    config: {
      type: Object,
      default: () => {
        return {
          columns: 1,
          rows: 2,
          content: 2,
          departClassIcon: 0, //部门分类下是否显示图标，0：隐藏，1：字体，2：png
          otherClassIcon: 0, //其他分类是否显示图标，0：隐藏，1：显示
        };
      },
    },
  },

  computed: {
    isShowIcon() {
      //是否显示图标
      if (
        (this.isDept && this.config.departClassIcon === 0) ||
        (!this.isDept && this.config.otherClassIcon === 0)
      ) {
        return false;
      }
      return true;
    },
  },
  methods: {
    clickItem() {
      this.$emit("clickItem", this.dataItem);
    },
  },
};
</script>

<style lang="less" scoped>
.ser-item {
  cursor: pointer;
  border: 2px dotted #f0f0f0;
  height: 166px;
  box-sizing: border-box;
  border-radius: 4px;
  margin-bottom: 20px;
  padding: 72px 40px;
  text-align: center;
  font-size: 14px;
  &:hover {
    border-color: transparent !important;
  }
  &.has-icon {
    padding: 46px;
    .show-icon {
      .icon-con {
        margin-bottom: 8px;
        .item-icon-font {
          font-size: 38px;
        }
      }
    }
  }
  .title {
    font-weight: bold;
    .iconfont {
      color: #bcbcbc;
    }
    &:hover .iconfont {
      color: inherit !important;
    }
  }
}
</style>
