<template>
  <div>
    <!-- 可在线办理 -->
    <!-- <div class="selFilters selFilters__online" v-if="availableOnline">
      <we-checkbox
        v-model="isOnline"
        shape="square"
        @change="handleChangeOnline"
      >
        {{ getLanguageValue('availableOnline', '可在线办理') }}
      </we-checkbox>
    </div> -->
    <div class="selFilters">
      <we-dropdown-menu class="selFilters__dropdown">
        <!-- 一级分类 -->
        <we-dropdown-item
          v-for="(item, index) in filters"
          :key="index"
          :ref="`${item.propertyWid}${index}`"
          :title="item.alias"
          :title-class="
            hasFilters(checkedFiltersOk, item.propertyWid, index)
              ? 'we-dropdown-menu__title--active'
              : ''
          "
          @open="handleOpen(item)"
        >
          <div style="display: flex;flex-direction:column;height:100%;">
            <div class="treeSelect">
              <div class="treeSelect__left" :class="{ hasThird: hasThird }">
                <!-- 二级分类的全部 -->
                <div
                  class="treeSelect__item"
                  :class="[
                    hasFilters(checkedFilters, item.propertyWid, index)
                      ? ''
                      : 'portal-primary-color-lv1',
                    !selectdSecond ? 'selected' : ''
                  ]"
                  @click="handleClickAll(item)"
                >
                  <div class="treeSelect__itemName ellipsis">
                    {{ getLanguageValue('all', '全部') }}
                  </div>
                  <we-icon
                    name="success"
                    v-if="!hasFilters(checkedFilters, item.propertyWid, index)"
                  />
                </div>
         
                <!-- 二级分类 -->
                 <div v-if="item.alias == '服务主题'">
                      <div
                        class="treeSelect__item"
                        v-for="second in getsubjectname" 
                        :key="second[item.propertyWid]"
                        :class="[
                          hasCheckedSecond(item.propertyWid, second)
                            ? 'portal-primary-color-lv1'
                            : '',
                          selectdSecond &&
                          selectdSecond[item.propertyWid] === second[item.propertyWid]
                            ? 'selected'
                            : ''
                        ]"
                        @click="handleClickSecond(item.propertyWid, second)"
                      >
                
                        <div class="treeSelect__itemName ellipsis">
                         {{ second[item.propertyName] }}
                        </div>
                        <we-icon
                          name="success"
                          v-if="hasCheckedSecond(item.propertyWid, second)"
                          class="portal-primary-color-lv1"
                        />
                      </div>
                 </div>
                 <div v-else>
                     <div
                      class="treeSelect__item"
                      v-for="second in item.data" 
                      :key="second[item.propertyWid]"
                      :class="[
                        hasCheckedSecond(item.propertyWid, second)
                          ? 'portal-primary-color-lv1'
                          : '',
                        selectdSecond &&
                        selectdSecond[item.propertyWid] === second[item.propertyWid]
                          ? 'selected'
                          : ''
                      ]"
                      @click="handleClickSecond(item.propertyWid, second)"
                    >
                      <div class="treeSelect__itemName ellipsis">
                        {{ second[item.propertyName] }}
                      </div>
                      <we-icon
                        name="success"
                        v-if="hasCheckedSecond(item.propertyWid, second)"
                        class="portal-primary-color-lv1"
                      />
                    </div>
                 </div>
               
              </div>
              <!-- 三级分类 -->
              <div v-if="hasThird" class="treeSelect__right">
                <div
                  class="treeSelect__item"
                  v-for="third in thirdClassify"
                  :key="third[item.propertyWid]"
                  :class="[
                    hasCheckedThird(item.propertyWid, third)
                      ? 'portal-primary-color-lv1'
                      : ''
                  ]"
                  @click="handleClickThird(item.propertyWid, third)"
                >
                  <div class="treeSelect__itemName ellipsis">
                    {{ third[item.propertyName] }}
                  </div>
                  <we-icon
                    name="success"
                    v-if="hasCheckedThird(item.propertyWid, third)"
                    class="portal-primary-color-lv1"
                  />
                </div>
              </div>
            </div>
            <div class="treeSelect__opt">
              <we-button
                class="treeSelect__btn portal-font-color-lv1"
                type="default"
                @click="handleReset(item)"
              >
                {{ getLanguageValue('reset', '重 置') }}
              </we-button>
              <we-button
                type="info"
                @click="handleOk(`${item.propertyWid}${index}`)"
                class="treeSelect__btn confirm_btn portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
              >
                {{ getLanguageValue('confirm', '确 定') }}
              </we-button>
            </div>
          </div>
        </we-dropdown-item>
        <we-dropdown-item
          ref="availableOnline"
          :title="getLanguageValue('online', '在线办理')"
          :title-class="isOnline ? 'we-dropdown-menu__title--active' : ''"
          v-if="availableOnline"
        >
          <div style="display: flex;flex-direction:column;height:100%;">
            <div class="treeSelect">
              <div class="treeSelect__left">
                <!-- 二级分类 -->
                <div
                  class="treeSelect__item"
                  v-for="item in onlineOptions"
                  :key="item.value"
                  :class="[
                    isOnline === item.value
                      ? 'portal-primary-color-lv1 selected'
                      : ''
                  ]"
                  @click="isOnline = item.value"
                >
                  <div class="treeSelect__itemName ellipsis">
                    {{ item.text }}
                  </div>
                  <we-icon
                    name="success"
                    v-if="isOnline === item.value"
                    class="portal-primary-color-lv1"
                  />
                </div>
              </div>
            </div>
            <div class="treeSelect__opt">
              <we-button
                class="treeSelect__btn portal-font-color-lv1"
                type="default"
                @click="isOnline = false"
              >
                {{ getLanguageValue('reset', '重 置') }}
              </we-button>
              <we-button
                type="info"
                @click="handleChangeOnline"
                class="treeSelect__btn confirm_btn portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
              >
                {{ getLanguageValue('confirm', '确 定') }}
              </we-button>
            </div>
          </div>
        </we-dropdown-item>
      </we-dropdown-menu>
    </div>
  </div>
</template>

<script>
export default {
  props: ['filterItem', 'availableOnline'],
  data() {
    return {
      getsubjectname:[],
      thirdClassify: [],
      checkedFilters: {
        subjectWid: [], //服务主题wid
        deptWid: [], //责任部门wid
        roleWid: [], //服务对象wid
        categoryWid: [] //分类维度wid
      },
      hasThird: false,
      selectdSecond: null,
      checkedFiltersOk: {
        subjectWid: [], //服务主题wid
        deptWid: [], //责任部门wid
        roleWid: [], //服务对象wid
        categoryWid: [] //分类维度wid
      },
      isOnline: false
    }
  },
  computed: {
    filters() {
      return (this.filterItem || []).map(el => {
        const staticName = ['subjectName', 'deptName', 'roleName']
        const staticMap = {
          subjectName: 'serviceItemCategory',
          deptName: 'serviceItemDept',
          roleName: 'roleName'
        }
        if (staticName.includes(el.propertyName)) {
          el.alias = this.getLanguageValue(staticMap[el.propertyName], el.alias)
        }
        return {
          ...el
        }
      })
    },
    onlineOptions() {
      return [
        {
          text: this.getLanguageValue('all', '全部'),
          value: false
        },
        {
          text: this.getLanguageValue('availableOnline', '可在线办理'),
          value: true
        }
      ]
    }
  },
  methods: {
    handleOpen(item) {
      this.checkedFilters = this.deepClone(this.checkedFiltersOk)
      let hasThird = false
      for (let index = 0; index < item.data.length; index++) {
        const element = item.data[index]
        if (element.children && element.children.length) {
          hasThird = true
          break
        }
      }
      this.hasThird = hasThird
    },
    handleChangeOnline() {
      this.$emit('updateSearch', {
        ...this.checkedFiltersOk,
        isOnline: this.isOnline
      })
      this.$refs.availableOnline.toggle(false)
    },
    handleResetOnline() {
      this.isOnline = false
      this.handleChangeOnline()
    },
    handleClickAll(item) {
      this.handleReset(item)
      this.selectdSecond = null
      this.thirdClassify = []
    },
    handleClickSecond(propertyWid, item) {
      if (item.children && item.children.length) {
        this.thirdClassify = item.children
      } else {
        this.thirdClassify = []
      }
      this.selectdSecond = item
      let checkedArr = this.checkedFilters[propertyWid].concat()
      if (checkedArr.includes(item[propertyWid])) {
        // 已选中当前二级分类
        checkedArr = checkedArr.filter(curr => curr !== item[propertyWid])
      } else {
        checkedArr.push(item[propertyWid])
      }
      // 过滤掉已选的三级
      this.thirdClassify.forEach(element => {
        if (checkedArr.includes(element[propertyWid])) {
          checkedArr = checkedArr.filter(curr => curr !== element[propertyWid])
        }
      })
      this.checkedFilters[propertyWid] = checkedArr
    },
    handleClickThird(propertyWid, item) {
      let checkedArr = this.checkedFilters[propertyWid].concat()
      const secondWid = this.selectdSecond[propertyWid]
      if (checkedArr.includes(secondWid)) {
        const thirdWid = this.thirdClassify.map(curr => curr[propertyWid])
        checkedArr = checkedArr
          .concat(thirdWid)
          .filter(curr => curr !== secondWid)
      }
      if (checkedArr.includes(item[propertyWid])) {
        // 取消选中
        checkedArr = checkedArr.filter(curr => curr !== item[propertyWid])
      } else {
        checkedArr.push(item[propertyWid])
      }
      // 三级分类都选中时，选中二级分类
      if (this.hasThirdAllCheck(this.thirdClassify, checkedArr, propertyWid)) {
        this.thirdClassify.forEach(element => {
          if (checkedArr.includes(element[propertyWid])) {
            checkedArr = checkedArr.filter(
              curr => curr !== element[propertyWid]
            )
          }
        })
        checkedArr.push(secondWid)
      }
      this.checkedFilters[propertyWid] = checkedArr
    },
    // 判断三级分类是否全部选中
    hasThirdAllCheck(data, first, propertyWid) {
      let flag = true
      for (let index = 0; index < data.length; index++) {
        const element = data[index]
        if (!first.includes(element[propertyWid])) {
          flag = false
          break
        }
      }
      return flag
    },
    // 判断是否有筛选条件
    hasFilters(target, propertyWid, index) {
      if (propertyWid === 'categoryWid') {
        // 自定义分类
        const checkedArr = target[propertyWid]
        const first = this.filters[index].data // 一级筛选条件
        let checked = false
        let traverse = data => {
          data.forEach(element => {
            if (checkedArr.includes(element[propertyWid])) {
              checked = true
              return
            } else {
              if (data.children && data.children.length) {
                traverse(data.children)
              }
            }
          })
        }
        traverse(first)
        traverse = null

        return checked
      }
      return target[propertyWid].length
    },
    // 是否有选中的二级筛选项
    hasCheckedSecond(propertyWid, second) {
      const parent = this.checkedFilters[propertyWid]
      if (parent) {
        return parent.includes(second[propertyWid])
      } else {
        return false
      }
    },
    // 是否有选中的三级筛选项
    hasCheckedThird(propertyWid, third) {
      const parent = this.checkedFilters[propertyWid]
      if (parent && this.selectdSecond) {
        return (
          parent.includes(this.selectdSecond[propertyWid]) ||
          parent.includes(third[propertyWid])
        )
      } else {
        return false
      }
    },
    handleReset(item) {
      if (item.propertyWid === 'categoryWid') {
        // 自定义分类
        let checked = this.checkedFilters.categoryWid.concat()
        item.data.forEach(element => {
          if (checked.includes(element.categoryWid)) {
            checked = checked.filter(curr => curr !== element.categoryWid)
          } else {
            if (element.children && element.children.length) {
              element.children.forEach(child => {
                if (checked.includes(child.categoryWid)) {
                  checked = checked.filter(curr => curr !== child.categoryWid)
                }
              })
            }
          }
        })
        this.checkedFilters[item.propertyWid] = checked
      } else {
        this.checkedFilters[item.propertyWid] = []
      }
    },
    handleOk(refKey) {
      this.checkedFiltersOk = this.deepClone(this.checkedFilters)
      this.$emit('updateSearch', {
        ...this.checkedFiltersOk,
        isOnline: this.isOnline
      })
      this.$refs[refKey][0].toggle(false)
    },
    deepClone(target) {
      let result
      if (typeof target === 'object') {
        if (Array.isArray(target)) {
          result = [] // 将result赋值为一个数组，并且执行遍历
          for (let i in target) {
            result.push(this.deepClone(target[i]))
          }
        } else if (target === null) {
          result = null
        } else if (target.constructor === RegExp) {
          result = target
        } else {
          result = {}
          for (let i in target) {
            if (Object.prototype.hasOwnProperty.call(target, i)) {
              result[i] = this.deepClone(target[i])
            }
          }
        }
      } else {
        result = target
      }
      return result
    }
  },
  mounted(){
    
     this.getsubjectname= this.filterItem[0].data.filter(item => item.subjectName == '数字驾驶舱')
     console.log(this.getsubjectname,'44444');
  }
}
</script>

<style lang="less" scoped>
.selFilters {
  padding: 0 17px;
  height: 46px;
  // border-bottom: 1px solid #e7edf1;
  background: #fff;
}
.selFilters__online {
  display: flex;
  align-items: center;
  /deep/.we-checkbox__icon {
    font-size: 16px;
    .we-icon {
      width: 16px;
      height: 16px;
      display: table;
      line-height: 1;
      border-radius: 2px;
      border-color: #d6dade;
    }
    .we-icon-success:before {
      width: 1em;
      height: 1em;
      display: table-cell;
      vertical-align: middle;
    }
  }
  /deep/.we-checkbox__label {
    height: 20px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    word-break: break-all;
    max-width: 80px;
  }
}
/deep/.we-dropdown-menu {
  height: 100%;
  .we-dropdown-menu__bar {
    box-shadow: inset 0 -0.02667rem 0 0 #eef2f5;
    height: 100%;
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
    // 隐藏滚动条
    scrollbar-width: none;
    /* Firefox */
    -ms-overflow-style: none;
    &::-webkit-scrollbar {
      display: none;
    }
  }
  .we-dropdown-menu__title {
    font-size: 14px;
    max-width: 90px;
    &::after {
      border-color: transparent transparent #707d8f #707d8f;
    }
  }
  .we-dropdown-menu__item {
    margin-right: 18px;
    flex: none;
    &:first-child .we-dropdown-menu__title {
      padding-left: 0;
    }
  }
  .we-dropdown-item__content {
    height: 70%;
    border-top: 1px solid #e7edf1;
  }
}
.treeSelect {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  display: flex;
  &::-webkit-scrollbar {
    width: 0;
    background: transparent;
  }
  .treeSelect__left {
    width: 100%;
    &.hasThird {
      width: 32%;
      background: #f6f6f8;
      .treeSelect__item {
        background: #f6f6f8;
      }
      .selected {
        background: #fff;
      }
    }
  }
  .treeSelect__right {
    flex: 1;
    min-width: 0;
  }
  .treeSelect__item {
    padding: 0 8px 0 17px;
    height: 50px;
    display: flex;
    align-items: center;
    .treeSelect__itemName {
      flex: 1;
      min-width: 0;
    }
  }
}
.treeSelect__opt {
  box-shadow: inset 0 1px 0 0 #eef2f5;
  padding: 8px 17px;
  display: flex;
  .treeSelect__btn {
    flex: 1;
    margin-left: 13px;
    height: 40px;
    min-width: 0;
    /deep/.we-button__text {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      word-break: break-all;
    }
  }
}
</style>
