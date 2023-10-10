<template>
  <div ref="AllServiceItem" :class="['allServiceItem', router.showTitle ? 'pt-16' : '']">
    <!-- 搜索框 -->
    <div class="search_wrap">
      <template v-if="showSearch">
        <we-input
          class="allServiceItem_input"
          :placeholder="placeholder"
          v-model="keyWord"
          maxlength="100"
          @keyup.enter.native="handleSearch"
          autocomplete="off"
        >
        </we-input>
        <we-button
          class="allServiceItem_input_button portal-primary-backgroundcolor-lv1"
          @click="handleSearch"
        >
          {{ getLanguageValue('search', '搜索') }}
        </we-button>
      </template>
    </div>
    <!-- {{ selectedFilter }} -->
    <!-- 筛选项 -->
    <div class="filter_tag_wrap">
      <filter-tag
        v-for="item in selectedFilter"
        :key="item.wid"
        :selectedItem="item"
        @delete-filter="handleDeleteFilter"
      />
      <we-button
        type="text"
        icon="we-icon-delete"
        v-show="hasFilterParams"
        :class="['reset', hasFilterParams ? 'portal-primary-color-lv1' : '']"
        @click="handleReset"
      >
        {{ getLanguageValue('reset', '重置') }}
      </we-button>
    </div>

    <filter-pop
      v-for="(item, index) in cardData.filterItem"
      :key="index"
      :dataItem="item"
      :containerWidth="containerWidth"
      :searchKey="searchServiceItemParam[item.propertyWid]"
      :selectedFilter.sync="selectedFilter"
      :router="router"
      @update-list="handleUpdateList"
    />
    <div class="filter-checkbox">
      <we-checkbox
        v-if="filterFields.includes('-1')"
        v-model="searchServiceItemParam.availableOnline"
        @change="searchParams"
      >
        {{ getLanguageValue('availableOnline', '可在线办理事项') }}
      </we-checkbox>
      <we-checkbox
        v-if="filterFields.includes('0')"
        v-model="searchServiceItemParam.orderByVisitCount"
        @change="searchParams"
      >
        {{ getLanguageValue('orderByVisitCount', '按访问量排序') }}
      </we-checkbox>
    </div>
    <service-item-card
      v-if="cardOrTable === '0'"
      ref="ServiceItemContent"
      :cardData="cardData"
      :showMore="showMore"
      :serviceItemListLimit="serviceItemListLimit"
      :content_width="content_width"
      :currentUser="currentUser"
      :cardLoading="loading"
      :router="router"
      @collect-item="collectItem"
      @list-to-limit="showMoreItems"
    />
    <service-item-table
      v-else
      :tableData="cardData.serviceItemList"
      :showItem="showItem"
      :currentUser="currentUser"
      :page="page"
      :tableLoading="loading"
      :router="router"
      @collect-item="collectItem"
      @size-change="handlePageSizeChange"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script>
import FilterPop from './component/FilterPop'
import FilterTag from './component/FilterTag'
import ServiceItemCard from './component/ServiceItemCard'
import ServiceItemTable from './component/ServiceItemTable'
export default {
  name: 'CardAllServiceItem',
  props: {
    index: Number,
    router: Object
  },
  components: {
    FilterPop,
    FilterTag,
    ServiceItemCard,
    ServiceItemTable
  },
  data() {
    const { cardId, cardWid } = this.router
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      keyWord: '',
      selectedVal: 1,
      errorImg: window.shell.ErrorImg,
      cardData: {},
      serviceItemListLimit: [], //展示20行数据
      card: {
        cardId: cardId,
        cardWid: cardWid
      },
      //服务事项
      currentUser: null,
      content_width: {},
      //搜索条件
      searchServiceItemParam: {
        searchKey: '',
        categoryWids: '', //服务主题wid（多个逗号隔开）
        deptWids: '', //责任部门wid（多个逗号隔开）
        roleWids: '', //服务对象wid（多个逗号隔开
        srvDeptWids: '', //服务部门wid（多个逗号隔开）
        dimensions: '', //分类维度wid（多个逗号隔开）
        availableOnline: false, // 可在线办理
        orderByVisitCount: false // 访问量排序
      },
      selectedFilter: [], // 已选的过滤条件
      dimensionWid: {}, //分类维度
      loading: false,
      showMore: false,
      showServiceItemsLine: 20, //点击更多加20行
      itemLoading: {},

      page: {
        total: 0,
        current: 1,
        size: 10
      },
      containerWidth: 1128,
      placeholder: ''
    }
  },
  computed: {
    cardConfig() {
      return this.cardData.config || {}
    },
    allServiceItemsDisplay() {
      return this.cardConfig.allServiceItemsDisplay || {}
    },
    // 0平铺、1列表
    cardOrTable() {
      return this.allServiceItemsDisplay.displayType || '0'
    },
    // 筛选项展示信息 -1在线办理、0访问量、1服务主题、2责任部门、3服务对象
    filterFields() {
      return this.allServiceItemsDisplay.filterFields || []
    },
    // 是否展示搜索
    showSearch() {
      return this.cardConfig && this.cardConfig.search === '1'
    },
    // 服务事项列表展示信息 0图标 1服务主题 2服务对象 3责任部门 4访问量
    showItem() {
      return this.allServiceItemsDisplay.itemDisplayInfo || []
    },
    // 是否有查询条件，有的话重置筛选项高亮
    hasFilterParams() {
      return this.selectedFilter.some(el => {
        return el.data.length > 0
      })
      // const val = Object.values(this.searchServiceItemParam)
      // return val.some(el => el)
    }
  },
  methods: {
    handleError(e) {
      let img = e.srcElement
      img.src = this.errorImg
      img.onerror = null //防止闪图
    },
    handleSearch() {
      this.page.current = 1
      this.searchServiceItemParam.searchKey = this.keyWord.trim()
      this.searchParams('search')
    },
    handlePageChange(page) {
      this.page.current = page
      this.searchServiceitem()
    },
    handlePageSizeChange(size) {
      this.page.size = size
      this.searchServiceitem()
    },
    // 清空筛选项
    handleReset() {
      this.selectedFilter = []
      for (let i in this.searchServiceItemParam) {
        if (i !== 'orderByVisitCount' && i !== 'availableOnline' && i !== 'searchKey') {
          this.searchServiceItemParam[i] = ''
        }
      }
      this.handleUpdateList()
    },
    // 删除已选的筛选项展示信息过滤条件
    handleDeleteFilter(param) {
      const { item, selectedItem } = param
      this.selectedFilter.forEach(el => {
        if (el.propertyWid === 'subjectWid') {
          el.data = el.data.filter(
            t => t.wid !== item.wid && t.parent !== item.wid
          )
        } else {
          if (el.alias === selectedItem.alias) {
            el.data = el.data.filter(t => t.wid !== item.wid)
          }
        }
      })
      this.handleUpdateList()
    },
    initdata() {
      window.minosStataCollect.loadStart({
        listId: this.listId,
        actionType: 3,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      this.loading = true
      window.shell.execCardMethod({
        ...this.card,
        method: 'renderData',
        param: {
          pageNumber: this.page.current,
          pageSize: this.page.size
        }
      })
      .then(res=> {
        window.minosStataCollect.loadEnd({
          listId: this.listId,
          endTime: new Date().getTime()
        });
        this.cardData = res.data
        this.cardData.filterItem = (res.data?.filterItem || []).map(el => {
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
        this.cardData.serviceItemList = res.data.serviceItemList || []
        this.page.total = res.data.total || 0
        if (this.cardOrTable === '0') {
          for (var item of this.cardData.serviceItemList) {
            for (var demo of item.datas) {
              this.$set(demo, 'wid', demo.itemWid)
              this.$set(demo, 'name', demo.itemName)
            }
          }
          // 排序服务事项
          this.sortServiceItemList(this.cardData.serviceItemList)
          // 前面操作处理数据，后面的将serviceItemList赋给 副本用于显示
          this.serviceItemListToLimit()

          this.$nextTick(() => {
            this.content_width = {
              width: `${this.$refs.ServiceItemContent &&
                this.$refs.ServiceItemContent.offsetWidth}px`
            }
          })
        }
        setTimeout(() => {
          this.loading = false;
        }, 10);
      })
      .catch(()=> {
        window.minosStataCollect.loadEnd({
          listId: this.listId,
          endTime: new Date().getTime()
        });
      })
    },
    serviceItemListToLimit() {
      this.serviceItemListLimit = []
      this.showMore = false
      var rows = 0
      var list = JSON.parse(JSON.stringify(this.cardData.serviceItemList))
      for (var item of list.slice(0)) {
        var itemRows = Math.ceil(item.datas.length / 5)
        if (itemRows + rows > this.showServiceItemsLine) {
          item.datas.splice(
            (this.showServiceItemsLine - rows) * 5,
            item.datas.length
          )
          rows = rows + itemRows
          this.serviceItemListLimit.push(item) // 副标题下有多的话，则截5*剩余行数的数量
          break
        } else {
          rows = rows + itemRows
          this.serviceItemListLimit.push(item)
        }
      }
      if (rows > this.showServiceItemsLine) {
        this.showMore = true
      }
    },
    async collectItem(item) {
      if (this.itemLoading[item.itemWid]) {
        return
      }
      this.itemLoading[item.itemWid] = true
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: {
            infoType: 1,
            itemId: item.itemWid,
            fucType: item.favorite ? 3 : 2
          }
        },
        startTime: new Date().getTime()
      });
      await window.shell.collectServiceItem({
        cardId: this.$options.name,
        id: item.itemWid, //事项ID 收藏或取消收藏时用
        operate: item.favorite ? 0 : 1 // 收藏操作标识 0：取消收藏 1：收藏
      })
      // item.favorite = item.favorite ? false : true
    },
    process(el) {
      if (this.cardOrTable === '0') {
        for (let item of this.serviceItemListLimit) {
          if (item.datas?.length) {
            item.datas.forEach(ele => {
              if (el.id === ele.wid) {
                ele.favorite = !ele.favorite
              }
            })
          }
        }
      } else {
        for (let item of this.cardData.serviceItemList) {
          if (item.itemWid === el.id) {
            item.favorite = item.favorite ? false : true
          }
        }
      }
      this.itemLoading[el.id] = false
      if (el.cardId !== this.$options.name) {
        this.searchServiceitem()
      }
    },
    handleUpdateList() {
      let dimensions = []
      this.selectedFilter.forEach(el => {
        if (el.propertyWid === 'subjectWid') {
          const first = (el.data || []).filter(el => el.parent === 'subjectWid') // 一级分类
          if (first.length) {
            // 如果一级分类存在则清空二级
            const pId = first.map(el => el.wid)
            const temp = (el.data || [])
              .filter(el => !pId.includes(el.parent))
              .map(t => t.wid)
            this.searchServiceItemParam.categoryWids = temp.join(',')
          } else {
            this.searchServiceItemParam.categoryWids = (el.data || [])
              .map(el => el.wid)
              .join(',')
          }
        } else if (el.propertyWid === 'deptWid') {
          this.searchServiceItemParam.deptWids = (el.data || [])
            .map(el => el.wid)
            .join(',')
        } else if (el.propertyWid === 'roleWid') {
          this.searchServiceItemParam.roleWids = (el.data || [])
            .map(el => el.wid)
            .join(',')
        } else {
          const temp = (el.data || []).map(el => el.wid)
          dimensions.push(...temp)
        }
      })
      this.searchServiceItemParam.dimensions = dimensions.join(',')
      this.page.current = 1
      this.searchServiceitem()
    },
    searchParams(isSearch=false) {
      this.page.current = 1
      this.searchServiceitem(isSearch)
    },
    // 查询
    searchServiceitem(isSearch) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: {
            infoType: isSearch && isSearch === 'search' ? 3 : 2,
            filterInfo: {
              ...this.searchServiceItemParam,
              pageNumber: this.page.current,
              pageSize: this.page.size
            }
          }
        },
        startTime: new Date().getTime()
      });
      this.loading = true
      window.shell.execCardMethod(
        {
          ...this.card,
          method: 'searchServiceItem',
          param: {
            ...this.searchServiceItemParam,
            pageNumber: this.page.current,
            pageSize: this.page.size
          }
        },
        res => {
          this.cardData.serviceItemList = res.data.serviceItemList || []
          this.page.total = res.data.total || 0

          if (this.cardOrTable === '0') {
            this.showServiceItemsLine = 20
            for (var item of this.cardData.serviceItemList) {
              for (var demo of item.datas) {
                this.$set(demo, 'wid', demo.itemWid)
                this.$set(demo, 'name', demo.itemName)
              }
            }
            // 排序服务事项
            this.sortServiceItemList(this.cardData.serviceItemList)
            this.serviceItemListToLimit()
          }
          setTimeout(() => {
            this.loading = false;
          }, 10);
        }
      )
    },
    openServiceItem(item) {
      window.shell.openServiceItem(item)
    },
    //排序，数字-特殊字符
    sortServiceItemList(list) {
      if (list[list.length - 1] && list[list.length - 1].navValue == 'other') {
        var a = []
        for (var i = 0; i < 10; i++) {
          a[i] = i + ''
        }
        var other = list[list.length - 1].datas.slice(0)
        var numArray = []
        var otherArray = []
        for (var j = 0; j < other.length; j++) {
          if (a.includes(other[j].itemName.substring(0, 1))) {
            numArray.push(other[j])
          } else {
            otherArray.push(other[j])
          }
        }
        list[list.length - 1].datas = numArray.concat(otherArray)
      }
    },
    // 展开更多
    showMoreItems() {
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
      this.showMore = false
      this.showServiceItemsLine += 20
      this.serviceItemListToLimit()
    }
  },
  created() {
    this.initdata()
    this.currentUser = window.shell.getUserInfo()
    this.placeholder = window.shell.placeholderEllipsis(
      this.getLanguageValue('search_placeholder', '请输入服务事项名称'),
      360
    )
    window.shell.on('collectServiceItem', this.process)
  },
  mounted() {
    this.containerWidth = this.$refs.AllServiceItem.offsetWidth
  },

  beforeDestroy() {
    window.shell.off('collectServiceItem')
  }
}
</script>

<style scoped lang="less">
.text_ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.pt-16 {
  padding-top: 16px;
}
.itemDiv {
  display: inline-block;
  border: 1px solid #ddd;
}
.allServiceItem {
  .search_wrap {
    margin-bottom: 16px;
    display: flex;
    align-items: center;
    .allServiceItem_input {
      width: 392px;
      /deep/.we-input__inner {
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
      }
    }
    .allServiceItem_input::-webkit-input-placeholder {
      color: #9fa8b5;
    }
    .allServiceItem_input:-ms-input-placeholder {
      /* Internet Explorer 10-11 */
      color: #dadada;
    }
    .allServiceItem_input:focus {
      border: 1px solid #dcdfe6;
      border-radius: 4px 0 0 4px;
      outline: none;
    }
    .allServiceItem_input_button {
      min-width: 68px;
      color: white;
      border-radius: 0 4px 4px 0;
      line-height: inherit;
      border: 1px solid;
      border-left: 0;
      margin-left: -1px;
      z-index: 1;
      position: relative;
      padding: 12px 20px;
      // margin-right: 24px;
    }
    /deep/.we-checkbox {
      margin-right: 16px;
    }
    .reset {
      margin-left: 28px;
    }
  }

  .filter_tag_wrap {
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    // margin-bottom: 16px;
    /deep/.we-button {
      padding: 10px 0;
      border: none;
      margin-bottom: 16px;
      &:focus,
      &:hover {
        background: transparent;
      }
    }
  }

  .filter-checkbox {
    float: right;
    height: 36px;
    display: flex;
    align-items: center;
    margin-bottom: 12px;
  }
}
</style>
