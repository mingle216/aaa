<template>
  <div class="filter_pop portal-font-color-lv1" v-if="dataItem.data && dataItem.data.length > 0">
    <we-popover
      placement="bottom-end"
      trigger="click"
      popper-class="filterPopClass"
      @hide="handleClose"
      @show="handleOpen"
      :width="containerWidth"
      :offset="15"
      :append-to-body="false"
    >
      <div
        slot="reference"
        class="filter_name ellipsis portal-primary-color-hover-lv1 portal-primary-backgroundcolor-hover-lv5 portal-primary-border-color-hover-lv1"
      >
        {{ dataItem.alias }}
      </div>
      <vue-scroll :barKeepShow="true" ref="vs">
        <div class="filter_item_wrap" :style="{width: `${containerWidth - 2}px`}">
          <ShowOrHide
            ref="FilterPopItem"
            :list="dataItem.data"
            :wid="dataItem.propertyWid"
            :name="dataItem.propertyName"
            :alias="dataItem.alias"
            :selectedFilter="selectedFilter"
            :containerWidth="containerWidth"
            @update-filter="updateFilter"
          />
        </div>
      </vue-scroll>
    </we-popover>
  </div>
</template>

<script>
import ShowOrHide from './ShowOrHide'
export default {
  props: ['dataItem', 'searchKey', 'selectedFilter', 'containerWidth', 'router'],
  components: {
    ShowOrHide
  },
  data() {
    return {}
  },
  methods: {
    handleOpen() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName
        },
        startTime: new Date().getTime()
      });
    },
    handleClose() {
      this.$refs.FilterPopItem.hide()
    },
    updateFilter(newFilter) {
      this.$emit('update:selectedFilter', newFilter)
      this.$emit('update-list')
    }
  }
}
</script>

<style lang="less" scoped>
.filter_pop {
  float: left;
  display: block;
  margin: 0 16px 16px 0;
  .filter_item_wrap {
    // min-width: 300px;
    // width: 1152px;
    padding: 12px 12px 0;
    max-height: 300px;
    // overflow: auto;
  }
}
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.filter_name {
  // width: 116px;
  height: 36px;
  padding: 0 30px;
  font-size: 14px;
  // color: #262626;
  letter-spacing: 0;
  text-align: center;
  line-height: 36px;
  background: #f5f5f5;
  border-radius: 4px;
  border-left: 4px solid #d9d9d9;
  cursor: pointer;
}
</style>

<style lang="less">
.we-popover:focus, .we-popover:focus:active, .we-popover__reference:focus:hover, .we-popover__reference:focus:not(.focusing) {
  outline: none;
}
.filterPopClass {
  padding: 0;
  left: 0px!important;
  &:focus, &:focus:active { // 解决safari在focus时会出现outline
    outline: none;
  }
  .popper__arrow {
    transform: translateX(-12px)!important;
  }
}
</style>
