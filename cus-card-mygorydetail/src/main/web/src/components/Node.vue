<template>
  <div>
    <div
      class="itemCategoryDetai__node__wrap"
      :class="[
        data.select || data.childSelect
          ? ''
          : 'portal-font-color-lv1',
        data.childSelect ? 'childSelect' : '',
        data.select ? 'active' : '',
        data.deep === 1
          ? 'itemCategoryDetai__node__wrap-1'
          : 'itemCategoryDetai__node__wrap-2',
      ]"
      @click.stop="handleClick"
    >
      <div
        class="itemCategoryDetai__node"
        :class="[
          data.deep === 1
            ? 'itemCategoryDetai__node-1'
            : 'itemCategoryDetai__node-2',
        ]"
        :style="{ 'padding-left': `${data.deep * 20}px` }"
      >
        <div
          style="flex:1;display:flex;align-items:center;min-width:0;"
        >
          <!-- 部门分类，图标展示由事项分类卡片的配置控制 -->
          <template v-if="isDept && deptIconType !== 0">
            <i
              v-if="deptIconType == 1 && data.iconLink"
              class="itemCategoryDetai__node-icon"
              :class="data.iconLink"
            ></i>
            <img
              v-else-if="deptIconType == 2 && data.picLink"
              class="itemCategoryDetai__node-img"
              :src="data.picLink"
              @error="handleError"
            />
            <i
              v-else
              class="iconfont icon-default itemCategoryDetai__node-icon"
            ></i>
          </template>
          <template v-if="!isDept && otherClassIcon !== 0">
            <i
              v-if="data.iconLink"
              class="itemCategoryDetai__node-icon"
              :class="data.iconLink"
            ></i>
            <img
              v-else-if="data.picLink"
              class="itemCategoryDetai__node-img"
              :src="data.picLink"
              @error="handleError"
            />
            <i
              v-else
              class="iconfont icon-default itemCategoryDetai__node-icon"
            ></i>
          </template>
          <!-- 节点名称 -->
          <div class="itemCategoryDetai__node-name ellipsis">
            {{ data.categoryName }}
          </div>
        </div>
        <span style="color: #97a2b1;">{{data.total}}</span>
        <!-- 展开收起图标 -->
        <i
          v-if="data.children && data.children.length"
          class="itemCategoryDetai__node-expand"
          :class="[data.expanded ? 'we-icon-arrow-up' : 'we-icon-arrow-down']"
          @click.stop="handleExpand"
        ></i>
      </div>
    </div>
    <!-- 子节点 -->
    <collapse-transition :appear="data.expanded">
      <div v-if="data.expanded && data.children && data.children.length">
        <category-node
          v-for="(item, i) in data.children"
          :key="i"
          :data="item"
          :isDept="isDept"
          :deptIconType="deptIconType"
          :otherClassIcon="otherClassIcon"
        ></category-node>
      </div>
    </collapse-transition>
  </div>
</template>

<script>
import CollapseTransition from "./collapse-transition";
export default {
  name: "CategoryNode",
  props: {
    data: Object,
    appear: {
      type: Boolean,
      default: false,
    },
    isDept: Boolean, // 是否是部门下的分类
    deptIconType: Number,
    otherClassIcon: Number  // 其他分类是否显示图标，0：隐藏 1：显示
  },
  components: {
    CollapseTransition,
  },
  data() {
    return {
      errorImg: require("../assets/default.svg"),
    };
  },
  methods: {
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    // 选中节点
    handleClick() {
      this.dispatch("CategoryTree", "handleSelect", this.data);
    },
    handleExpand() {
      this.$set(this.data, "expanded", !this.data.expanded);
      this.dispatch("CategoryTree", "handleExpanded", !this.data.expanded);
    },
    dispatch(componentName, methodName, params) {
      let parent = this.$parent || this.$root;
      let name = parent.$options.name;

      while (parent && (!name || name !== componentName)) {
        parent = parent.$parent;

        if (parent) {
          name = parent.$options.name;
        }
      }
      if (parent && parent[methodName]) {
        parent[methodName](params);
      }
    },
  },
};
</script>

<style scoped lang="less">
.itemCategoryDetai__node__wrap {
  cursor: pointer;
}
.itemCategoryDetai__node__wrap-1.active {
  background: #f5f5f5;
  position: relative;
  &:before{
    content: "";
    display: inline-block;
    width: 4px;
    border-radius: 2px;
    height: 30px;
    background: #3F83F8;
    position: absolute;
    top: 17px;
    left: 0;
  }
  .itemCategoryDetai__node-name {
    font-weight: bold;
  }
}
.itemCategoryDetai__node__wrap-1.childSelect {
  background: #ffffff;
  .itemCategoryDetai__node-name {
    font-weight: normal;
  }
}
.itemCategoryDetai__node__wrap-2.active {
  background: #f5f5f5;
  .itemCategoryDetai__node-name {
    font-weight: bold;
  }
}
.itemCategoryDetai__node {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-right: 20px;
}
.itemCategoryDetai__node-name {
  flex: 1;
  min-width: 0;
  font-size: 16px;
}
.itemCategoryDetai__node-expand {
  margin-left: 10px;
  font-size: 14px;
  font-weight: bold;
}
.itemCategoryDetai__node-1 {
  height: 64px;
  .itemCategoryDetai__node-icon {
    font-size: 36px;
    margin-right: 12px;
  }
  .itemCategoryDetai__node-img {
    width: 36px;
    height: 36px;
    margin-right: 12px;
  }
}
.itemCategoryDetai__node-2 {
  height: 56px;
  .itemCategoryDetai__node-icon {
    font-size: 28px;
    margin-right: 12px;
  }
  .itemCategoryDetai__node-img {
    width: 28px;
    height: 28px;
    margin-right: 12px;
  }
}
</style>
