<template>
  <div class="theme_tab" v-loading="loading">
    <ul class="tabs">
      <li class="tab_item tabsNames">主题分类</li>
      <!-- <li class="tab_item noClick">部门分类</li> -->
    </ul>
    <ul class="itemThemes">
      <li
        v-for="item in serviceSubject"
        :key="item.classifyWid"
        :class="{
          active_tab: false,
          item_tab: true,
          'portal-primary-color-lv1': activeSubject === item.subjectWid,
          'portal-primary-color-hover-lv1': true
        }"
        @click="searchTableData(item.subjectWid)"
      >
        <i class="icons icons0"></i>
        <span>{{ item.subjectName }}</span>
        <i class="we-icon-right rights"></i>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    serviceSubject: Array,
    activeSubject: String,
    loading: Boolean
  },
  data() {
    return {
      classifyList: []
    }
  },
  methods: {
    searchTableData(wid) {
      this.$emit('change-theme-tab', wid)
    }
  },
}
</script>

<style lang="less" scoped>
.theme_tab {
  width: 260px;
  margin-right: 20px;
  .tabs {
    .tab_item {
      float: left;
      width: 100%;
      height: 50px;
      background: #e5e5e5;
      font-size: 15px;
      color: #4a98f6;
      line-height: 50px;
      text-align: center;
    }
    .tabsNames {
      font-size: 16px;
      font-weight: bold;
      color: #fff;
      background: #276EED;
      border-radius: 4px 4px 0 0;
    }
    .noClick {
      color: #4a98f6;
      background: #e5e5e5;
    }
  }
  .itemThemes {
    padding: 15px 20px;
    margin-bottom: 50px;
    background: #F6F8FA;
    .item_tab {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 20px;
      height: 49px;
      border-bottom: 1px solid #e3e3e3;
      cursor: pointer;
      &:hover {
        font-weight: bold;
      }
      &:last-child {
        border-bottom: none;
      }
    }
  }
}
</style>
