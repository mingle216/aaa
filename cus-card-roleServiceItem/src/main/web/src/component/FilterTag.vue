<template>
  <div class="selected_wrap portal-font-color-lv2" v-if="selectedItem.data && selectedItem.data.length">
    <span>{{ selectedItem.alias }}：</span>
    <div v-for="item in filterSubjectChild" :key="item.wid" class="mr-6">
      <span class="tag_name">{{ item.name }}</span>
      <i class="we-icon-error error_icon" @click="handleDelete(item)"></i>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    selectedItem: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      handleDelete(item) {
        this.$emit('delete-filter', {
          item,
          selectedItem: this.selectedItem
        })
      }
    }
  },
  computed: {
    // 如果是服务主题且二级分类全选的情况下只展示一级分类
    filterSubjectChild() {
      const first = (this.selectedItem.data || []).filter(el => el.parent === 'subjectWid')  // 一级分类
      if (this.selectedItem.propertyWid === 'subjectWid' && first.length) {
        // 如果一级分类存在则清空二级
        const pId = first.map(el => el.wid)
        return (this.selectedItem.data || []).filter(el => !pId.includes(el.parent))
      } else {
        return this.selectedItem.data
      }
    }
  }
}
</script>

<style lang="less" scoped>
  .mr-6 {
    margin-right: 6px;
  }
  .selected_wrap {
    padding: 5px 12px;
    line-height: 22px;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    border: 1px solid #D9D9D9;
    border-radius: 4px;
    margin-right: 12px;
    margin-bottom: 16px;

    .tag_name {
      margin-right: 6px;
    }
    .error_icon {
      color: #BFBFBF;
      cursor: pointer;
    }
  }
</style>