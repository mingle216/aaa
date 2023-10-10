<template>
  <div class="allItem portal-font-color-lv1">
    <we-sticky>
      <div :class="['search_bar', router.showTitle ? 'title_search' : '']">
        <div class="searchWrap" v-if="showSearch">
          <we-search
            v-model="keyword"
            :show-action="showSearchBtn"
            :placeholder="placeholder"
            @clear="handleClear"
            @focus="handleFocus"
            @blur="handleBlur"
            @search="handleSearch"
          >
            <span
              v-if="showSearchBtn"
              slot="action"
              class="search_btn portal-font-color-lv2"
              @click="handleSearch"
            >
              {{ getLanguageValue('searchButton', '搜索') }}
            </span>
          </we-search>
        </div>
        <!-- 筛选项 -->
        <filter-condition
          v-if="filterItem.length || availableOnline"
          :filterItem="filterItem"
          :availableOnline="availableOnline"
          @updateSearch="handleUpdateSearch"
        ></filter-condition>
      </div>
    </we-sticky>
    <div style="positon: relative">
      <we-list
        ref="List"
        class="card_content"
        v-if="serviceItemListLimit && serviceItemListLimit.length"
        v-model="listLoading"
        :finished="finished"
        :immediate-check="false"
        :finished-text="getLanguageValue('noMore', '— 没有更多内容了 —')"
        :loading-text="getLanguageValue('loading', '加载中...')"
        :offset="0"
        @load="onLoad"
      >
        <ul
          v-for="nav in serviceItemListLimit"
          :key="nav.navId"
          :class="[loading ? 'hidden' : '']"
        >
          <ListItem
            v-if="nav.datas && nav.datas.length"
            :itemInfo="nav"
            :searchKey="searchServiceItemParam.searchKey"
            :itemDisplayInfo="itemDisplayInfo"
            @collectApp="collectApp"
            @openServiceDetail="openServiceDetail"
            @handleOnline="handleOnline"
          ></ListItem>
        </ul>
      </we-list>
      <div
        v-loading="
          (!serviceItemListLimit.length && serviceItemList.length) || loading
        "
        v-if="
          (!serviceItemListLimit.length && serviceItemList.length) || loading
        "
        class="no_data"
      />
    </div>
    <EmptyCon
      v-show="!serviceItemList.length && !loading && !listLoading"
      :tip="getLanguageValue('tipMsg', '未找到相关事项')"
    />
  </div>
</template>

<script>
import FilterCondition from './components/FilterCondition.vue'
import ListItem from './components/ListItem'
import TrackMixin from './mixins/track.js'
// import mockData from "./mock";
export default {
  components: {
    ListItem,
    FilterCondition
  },
  name: '',
  props: {
    index: Number,
    router: Object
  },
  mixins: [TrackMixin],
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false
        }, 5000)
      }
    }
  },
  data() {
    return {
      keyword: '',
      loading: false,
      listLoading: false,
      finished: false,
      card: {
        cardId: this.router.cardId,
        cardWid: this.router.cardWid
      },
      //搜索条件
      searchServiceItemParam: {
        searchKey: '',
        categoryWids: '', //服务主题wid（多个逗号隔开）
        deptWids: '', //责任部门wid（多个逗号隔开）
        roleWids: '', //服务对象wid（多个逗号隔开）
        dimensions: '', //分类维度wid（多个逗号隔开）
        availableOnline: false // 可在线办理
      },
      total: 0,
      placeholder: '',
      availableOnline: false,
      filterItem: [],
      serviceItemList: [],
      serviceItemListLimit: [], // 分页展示数据
      showServiceItemsLine: 0,
      itemDisplayInfo: [], //列表项展示信息
      itemLoading: {},
      showSearch: true,
      showSearchBtn: false // 是否显示筛选弹窗
    }
  },
  methods: {
    handleFocus() {
      this.showSearchBtn = true
    },
    handleClear() {
      this.keyword = ''
      this.handleClickTrack({
        infoType: 3,
        filterInfo: {
          keyword: this.keyword
        }
      }) // 点击埋点
      this.handleSearch()
    },
    handleBlur() {
      this.showSearchBtn = false
    },
    handleSearch() {
      this.searchServiceItemParam.searchKey = this.keyword.trim()
      this.searchData()
    },
    handleUpdateSearch(ops) {
      this.searchServiceItemParam = {
        ...this.searchServiceItemParam,
        categoryWids: ops.subjectWid.join(','), //服务主题wid（多个逗号隔开）
        deptWids: ops.deptWid.join(','), //责任部门wid（多个逗号隔开）
        roleWids: ops.roleWid.join(','), //服务对象wid（多个逗号隔开）
        dimensions: ops.categoryWid.join(','), //分类维度wid（多个逗号隔开）
        availableOnline: ops.isOnline // 可在线办理
      }
      this.searchData()
    },
    initData() {
      this.loading = true
      window.shell.execCardMethod(
        {
          ...this.card,
          method: 'renderData'
        },
        res => {
          // const res = mockData;
          if (res.errcode === '0' && res.data) {
            // 组装筛选条件
            this.setFilterFileds(res.data)
            this.itemDisplayInfo =
              res.data.config.allServiceItemsDisplay.itemDisplayInfo || []
            this.showSearch = res.data.config && res.data.config.search === '1'
            this.setData(res.data)
          } else {
            this.availableOnline = false
            this.itemDisplayInfo = []
            this.filterItem = []
            this.serviceItemList = []
            this.serviceItemListLimit = []
            this.total = 0
            this.finished = this.total <= 20
            this.showServiceItemsLine = 20
            this.listLoading = false
          }
          this.loading = false
        }
      )
    },
    searchData() {
      this.loading = true
      window.shell.execCardMethod(
        {
          ...this.card,
          method: 'searchServiceItem',
          param: {
            ...this.searchServiceItemParam
          }
        },
        res => {
          if (res.errcode === '0' && res.data) {
            this.setData(res.data)
          } else {
            this.serviceItemList = []
            this.serviceItemListLimit = []
            this.total = 0
            this.finished = this.total <= 20
            this.showServiceItemsLine = 20
            this.listLoading = false
          }
          this.loading = false
        }
      )
    },
    setData(data) {
      this.serviceItemList = data.serviceItemList || []
      this.total = data.total || 0
      this.finished = this.total <= 20
      this.showServiceItemsLine = 20
      // 排序服务事项
      this.sortServiceItemList(this.serviceItemList)
      this.serviceItemListToLimit(this.showServiceItemsLine)
    },
    setFilterFileds(data) {
      const filterFields = data.config.allServiceItemsDisplay.filterFields || []
      //是否勾选可在线办理事项
      this.availableOnline = filterFields.includes('-1') ? true : false
      this.filterItem = data.filterItem || []
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
    serviceItemListToLimit(haveBeanShowRows) {
      this.serviceItemListLimit = []
      let rows = 0
      let list = JSON.parse(JSON.stringify(this.serviceItemList))
      for (let item of list.slice(0)) {
        if (rows >= haveBeanShowRows) {
          break
        }
        // 单个二级分类下的行数
        let datas = item.datas || []
        let lineCount = datas.length // 直接挂在当前二级分类下的服务所占的行数
        if (rows + lineCount >= haveBeanShowRows) {
          datas.splice(haveBeanShowRows - rows, lineCount)
          rows += lineCount
          this.serviceItemListLimit.push(item)
          break
        } else {
          rows += lineCount
          this.serviceItemListLimit.push(item)
        }
      }
      this.listLoading = false
    },
    // 展开更多
    showMoreItems() {
      this.showServiceItemsLine = this.showServiceItemsLine + 20
      this.serviceItemListToLimit(this.showServiceItemsLine)
    },
    onLoad() {
      if (this.showServiceItemsLine < this.total) {
        this.finished = false
        setTimeout(() => {
          this.showMoreItems()
        }, 500)
      } else {
        this.finished = true
      }
    },
    openServiceDetail(item) {
      if(item.workGuide && 'isAuthorized' in item && item.isAuthorized === 0) {
        window.shell.showMessage({
          type: "warning",
          message: `${this.$Lan("public", "workGuideNoAuth", "您无权限查看")}`,
        });
        return false;
      }
      if (item.workGuide) {
        this.handleClickTrack({
          infoType: 1,
          itemId: item.itemWid,
          fucType: 0
        }); // 点击埋点
        window.shell.openServiceItem({
          ...item,
          wid: item.itemWid,
          name: item.itemName,
        });
      } else {
        this.handleOnline(item);
      }
    },
    handleOnline(item) {
      this.handleClickTrack({
        infoType: 1,
        itemId: item.itemWid,
        fucType: 1
      }); // 点击埋点
      if (item.onlineServiceType == 1) {
        window.shell.showMessage({
          type: 'warning',
          message: `${this.$Lan(
            'public',
            'noPermission',
            '暂无使用权限，请联系管理员'
          )}`
        })
        return
      }
      window.shell.openOnlineDeal({
        ...item,
        wid: item.itemWid,
        name: item.itemName
      })
    },
    collectApp(item) {
      if (this.itemLoading[item.itemWid]) {
        return
      }
      this.itemLoading[item.itemWid] = true
      window.shell.collectServiceItem({
        id: item.itemWid,
        operate: item.favorite ? 0 : 1 //  0:取消收藏 1:收藏
      })
    }
  },
  created() {
    if (window.shell) {
      this.initData()
      this.placeholder = window.shell.placeholderEllipsis(
        this.getLanguageValue('searchPlaceholder', '请输入服务事项名称'),
        245
      )
    }
  },
  mounted() {
    window.shell.on('collectServiceItem', ({ id, res }) => {
      this.itemLoading[id] = false
      if (res.errcode === '0') {
        for (let serviceType of this.serviceItemListLimit) {
          for (let serviceItem of serviceType.datas) {
            if (serviceItem.itemWid === id) {
              serviceItem.favorite = !serviceItem.favorite
            }
          }
        }
      }
    })
  },
  destroyed() {
    window.shell.off('collectServiceItem')
  }
}
</script>

<style lang="less" scoped>
/deep/.we-search {
  padding: 0;
  border-radius: 2px;
  .we-search__content {
    height: 36px;
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
    background: #f7f8fa;
  }
  .we-search__action {
    padding: 0;
    width: 44px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    text-align: center;
    line-height: 36px;
  }
  .we-field__body {
    height: 26px;
  }
}
/deep/.we-sticky--fixed {
  top: -1px;
}
.allItem {
  position: relative;
  width: 100%;
  .search_bar {
    background: #fff;
    .searchWrap {
      padding: 6px 17px;
      border-bottom: 1px solid #e7edf1;
    }
    .search_btn {
      font-size: 16px;
      height: 20px;
    }
  }
  .card_content {
    // padding: 0 17px;
    .hidden {
      visibility: hidden;
    }
  }
  .no_data {
    height: 148px;
    width: 100%;
    position: absolute;
    top: 0;
  }
}
</style>
