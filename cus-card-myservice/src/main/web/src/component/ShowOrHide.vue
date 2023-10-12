<template>
  <div ref="FilterPopItem" class="filter_pop_item portal-font-color-lv1">
    <ul>
      <li
        v-for="(item, index) in list"
        :key="`${index}`"
        :style="currentTabIndex == index && li_second"
        :ref="`reference${item[wid]}`"
      >
        <div
          :class="[
            selectedTab.includes(item[wid])
              ? 'active_tab portal-primary-color-lv1 portal-primary-border-color-lv1'
              : '',
            'tabs_default_button'
          ]"
          @click="handleClickTab(item, 1)"
          @mouseenter="handleHoverTab(item, index)"
        >
          {{ item[name] }}
        </div>
        <div
          v-show="second_menu_show && currentTabIndex == index"
          class="triangle"
          :style="triangele_left"
        />
        <div
          v-show="second_menu_show && currentTabIndex == index"
          class="second_menu"
          :style="second_menu_top"
          ref="second_menu_show"
        >
          <ul v-if="second_menu.length">
            <li
              v-for="(item_second, index_second) in second_menu"
              :key="index_second"
              @click.stop="chooseTabs_secondMenu(item_second, item)"
              class="item-second-card portal-primary-color-hover-lv1 portal-primary-border-color-hover-lv1"
              :class="[
                selectedTab.includes(item_second[wid])
                  ? 'active_tab portal-primary-color-lv1 portal-primary-border-color-lv1'
                  : ''
              ]"
            >
              {{ item_second[name] }}
            </li>
          </ul>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    wid: {
      type: String,
      default: ''
    },
    name: {
      type: String,
      default: ''
    },
    alias: {
      type: String,
      default: ''
    },
    list: {
      type: Array,
      default: () => {
        return []
      }
    },
    selectedFilter: {
      type: Array,
      default: () => {
        return []
      }
    },
    containerWidth: {
      type: Number,
      default: 1128
    }
  },
  data() {
    return {
      currentTabIndex: -1, // 当前展开的tab索引
      li_second: {}, //

      textHeight: '', //展开高度

      second_menu: [],
      triangele_left: {},
      second_menu_show: false, // 数组显示判断
      second_menu_top: {} //二级菜单离父元素顶部的高度
    }
  },
  computed: {
    selectedTab() {
      const temp = this.selectedFilter.find(el => el.alias === this.alias)
      const data = (temp && temp.data) || []
      return data.map(el => el.wid)
    }
  },
  methods: {
    handleHoverTab(item, index) {
      this.currentTabIndex = index
      this.second_menu = this.list[index].children || []
      this.second_menu_show = false
      // 如果有二级 菜单就撑开
      this.li_second = {}
      const ref = this.$refs[`reference${item[this.wid]}`][0] // 当前hover的分类
      if (this.second_menu && this.second_menu.length) {
        this.second_menu_show = true
        // 如果出现二级菜单，需要恢复一级菜单之前的宽度
        this.li_second = {
          width: ref.offsetWidth + 'px'
        }
        // 定位二级菜单的位置
        this.second_menu_top = {
          // width: this.$refs.FilterPopItem.offsetWidth + 'px', // 容器的宽度
          width: this.containerWidth - 24 + 'px', // 容器的宽度：弹窗宽度-两边padding24
          'margin-left': 12 - ref.offsetLeft + 'px' // 向左偏移：当前分类左边距-padding的距离
        }
        let triangele_left = ref.offsetWidth / 2 - 8
        // 当前元素的位置定位三角形的位置
        this.triangele_left = { 'margin-left': triangele_left + 'px' }
      }
    },
    // 选择二级分类
    chooseTabs_secondMenu(item, parentItem) {
      if (this.wid === 'subjectWid') {
        this.chooseSecondSubject(item, parentItem)
      } else {
        this.handleChooseOther(item, parentItem)
      }
    },
    chooseSecondSubject(item, parentItem) {
      let newFilter = JSON.parse(JSON.stringify(this.selectedFilter))
      const flag = newFilter.some(el => el.alias === this.alias)
      if (flag) {
        newFilter.forEach(el => {
          if (el.alias === this.alias) {
            const hasSelected = el.data.some(d => d.wid === item[this.wid])
            if (hasSelected) {
              // 过滤一级分类同时过滤掉parent为一级wid的二级分类
              el.data = el.data.filter(
                d => d.wid !== item[this.wid] && d.wid !== parentItem[this.wid]
              )
            } else {
              el.data.push({
                wid: item[this.wid],
                name: item[this.name],
                parent: parentItem[this.wid]
              })
              const temp = el.data.filter(
                t => t.parent === parentItem[this.wid]
              )
              const total = parentItem.children && parentItem.children.length
              // 如果二级分类的数量等于一级分类children的数量，则选中一级分类
              if (total === temp.length) {
                el.data.push({
                  wid: parentItem[this.wid],
                  name: parentItem[this.name],
                  parent: this.wid
                })
              }
            }
          }
        })
      } else {
        const data = [
          {
            wid: item[this.wid],
            name: item[this.name],
            parent: parentItem[this.wid]
          }
        ]
        const total = parentItem.children && parentItem.children.length
        // 如果二级分类的数量等于一级分类children的数量，则选中一级分类
        if (total === data.length) {
          data.push({
            wid: parentItem[this.wid],
            name: parentItem[this.name],
            parent: this.wid
          })
        }
        newFilter.push({
          data,
          propertyName: this.name,
          propertyWid: this.wid,
          alias: this.alias
        })
      }
      this.$emit('update-filter', newFilter)
    },
    // 选择服务主题：选择一级分类默认勾选所有二级分类，取消一级分类默认取消所有二级分类，取消某一个二级分类，一级分类取消勾选
    handleChooseSubject(item) {
      let newFilter = JSON.parse(JSON.stringify(this.selectedFilter))
      const flag = newFilter.some(el => el.propertyWid === this.wid)
      if (flag) {
        newFilter.forEach(el => {
          if (el.propertyWid === this.wid) {
            const hasSelected = el.data.some(d => d.wid === item[this.wid])
            if (hasSelected) {
              // 过滤一级分类同时过滤掉parent为一级wid的二级分类
              el.data = el.data.filter(
                d => d.wid !== item[this.wid] && d.parent !== item[this.wid]
              )
            } else {
              const data = (item.children || [])
                .map(ele => ({
                  wid: ele[this.wid],
                  name: ele[this.name],
                  parent: item[this.wid]
                }))
                .concat({
                  wid: item[this.wid],
                  name: item[this.name],
                  parent: this.wid
                })
              el.data.push(...data)
            }
          }
        })
      } else {
        const data = (item.children || [])
          .map(ele => ({
            wid: ele[this.wid],
            name: ele[this.name],
            parent: item[this.wid]
          }))
          .concat({
            wid: item[this.wid],
            name: item[this.name],
            parent: this.wid
          })
        newFilter.push({
          data,
          propertyName: this.name,
          propertyWid: this.wid,
          alias: this.alias
        })
      }
      this.$emit('update-filter', newFilter)
    },
    // 选择其他分类主题
    handleChooseOther(item) {
      let newFilter = JSON.parse(JSON.stringify(this.selectedFilter))
      const flag = newFilter.some(el => el.alias === this.alias)
      if (flag) {
        newFilter.forEach(el => {
          if (el.alias === this.alias) {
            const hasSelected = el.data.some(d => d.wid === item[this.wid])
            if (hasSelected) {
              el.data = el.data.filter(d => d.wid !== item[this.wid])
            } else {
              el.data.push({
                wid: item[this.wid],
                name: item[this.name]
              })
            }
          }
        })
      } else {
        const data = []
        data.push({
          wid: item[this.wid],
          name: item[this.name]
        })
        newFilter.push({
          data,
          propertyName: this.name,
          propertyWid: this.wid,
          alias: this.alias
        })
      }
      this.$emit('update-filter', newFilter)
      // this.searchKey_ = item[this.wid];
    },
    // 选择一级分类
    handleClickTab(item) {
      if (this.wid === 'subjectWid') {
        this.handleChooseSubject(item)
      } else {
        this.handleChooseOther(item)
      }
    },
    hide() {
      this.currentTabIndex = -1
      this.second_menu = []
    }
  }
}
</script>

<style lang="less" scoped>
.filter_pop_item {
  display: flex;
  ul {
    display: flex;
    flex-wrap: wrap;
    // overflow: hidden;
    z-index: 1;
    .tabs_default_button {
      white-space: nowrap;
      height: 30px;
      padding: 4px 12px;
      cursor: pointer;
      border-radius: 4px;
      border: 1px solid transparent;
      &:hover {
        background: #f5f5f5;
      }
    }
    li {
      margin-right: 12px;
      margin-bottom: 12px;
    }

    .triangle {
      width: 0;
      height: 0;
      border: 6px solid #eef0f4;
      border-color: transparent transparent #eef0f4;
      // margin-top: -7px;
    }
    .second_menu {
      display: flex;
      height: auto;
      top: 200px;
      overflow: hidden;
      background: #f5f5f5;
      word-break: break-all;
      overflow: hidden;
      ul {
        li {
          cursor: pointer;
          margin: 10px 10px;
        }
        .second_active {
          color: #102645 !important;
          font-weight: bold;
        }
      }
    }
  }
  .item-second-card {
    height: 30px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    padding: 4px 12px;
  }
  .active_tab {
    background: #fff;
    &:hover {
      background: #fff !important;
    }
  }
}
</style>
