<i18n src="./languages/locale.json"></i18n>
<template>
  <div ref="AllServiceItem" :class="['allServiceItem', router.showTitle ? 'pt-16' : '']">
    <div class="cus-page-tab">
      <div class="page-tab-item" :class="pageId=='card'?'active portal-primary-color-lv1 portal-primary-backgroundcolor-lv5':''" @click="changePage('card')">
        <img v-if="pageId=='card'" src="./assets/cards.png" alt="">
        <img v-else src="./assets/card.png" alt="">
        <p>卡片视图</p>
      </div>
      <div class="page-tab-item" :class="pageId=='list'?'active portal-primary-color-lv1 portal-primary-backgroundcolor-lv5':''" @click="changePage('list')">
        <img v-if="pageId=='list'" src="./assets/lists.png" alt="">
        <img v-else src="./assets/list.png" alt="">
        <p>列表视图</p>
      </div>
    </div>
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
    <div class="cus-filter">
      <div class="cus-filter-item">
        <div class="cus-filter-item-lable">服务主题:</div>
        <div class="fliter-items">
          <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.categoryWids == ''?'active portal-primary-color-lv1 portal-primary-backgroundcolor-lv5':''" @click="changeSub('')">全部</div>
          <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.categoryWids == item.subjectWid?'active portal-primary-color-lv1 portal-primary-backgroundcolor-lv5':''" v-for="item in subList" :key="item.subjectWid" @click="changeSub(item.subjectWid)">{{item.subjectName}}</div>
        </div>
      </div>
      <div class="cus-filter-item">
        <div class="cus-filter-item-lable">责任部门:</div>
        <div class="fliter-items">
          <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.deptWids == ''?'active portal-primary-color-lv1 portal-primary-backgroundcolor-lv5':''" @click="changeDept('')">全部</div>
          <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.deptWids == item.deptWid?'active portal-primary-color-lv1 portal-primary-backgroundcolor-lv5':''" @click="changeDept(item.deptWid)" v-for="item in deptList" :key="item.deptWid">{{item.deptName}}</div>
        </div>
      </div>
      <div class="cus-filter-item">
        <div class="cus-filter-item-lable">服务对象:</div>
        <div class="fliter-items">
          <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.roleWids == ''?'active portal-primary-color-lv1 portal-primary-backgroundcolor-lv5':''" @click="changeRole('')">全部</div>
          <div class="portal-primary-backgroundcolor-hover-lv5 " :class="searchServiceItemParam.roleWids == item.roleWid?'active portal-primary-color-lv1 portal-primary-backgroundcolor-lv5':''" @click="changeRole(item.roleWid)" v-for="item in roleList" :key="item.roleWid">{{item.roleName}}</div>
        </div>
      </div>
    </div>
    <div class="filter-checkbox">
      <we-checkbox
        v-if="filterFields.includes('-1')"
        v-model="searchServiceItemParam.availableOnline"
        @change="searchParams"
      >
        {{ getLanguageValue('availableOnline', '可在线办理事项') }}
      </we-checkbox>
      <we-checkbox
        v-if="filterFields.includes('0') && pageId=='list'"
        v-model="searchServiceItemParam.orderByVisitCount"
        @change="searchParams"
      >
        {{ getLanguageValue('orderByVisitCount', '按访问量排序') }}
      </we-checkbox>
    </div>
    <service-item-card
      v-if="pageId === 'card'"
      ref="ServiceItemContent"
      :cardData="appDataList"
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
      :tableData="showList"
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
import ServiceItemCard from './component/ServiceItemCards'
import ServiceItemTable from './component/ServiceItemTables'
import {pinyin} from "./utils/pinyin";
export default {
  name: 'CardAllServiceItem',
  props: {
    index: Number,
    router: Object
  },
  components: {
    ServiceItemCard,
    ServiceItemTable
  },
  data() {
    const { cardId, cardWid } = this.router
    return {
      hasPerId: [],
      pageId: 'card',
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      keyWord: '',
      selectedVal: 1,
      errorImg: window.shell.ErrorImg,
      cardData: {},
      subList: [],
      deptList: [],
      roleList: [],
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
      placeholder: '',
      appDataList: [{
        id: 'ABC',
        data: []
      },{
        id: 'DEF',
        data: []
      },{
        id: 'GHI',
        data: []
      },{
        id: 'JKL',
        data: []
      },{
        id: 'MNO',
        data: []
      },{
        id: 'PQR',
        data: []
      },{
        id: 'STU',
        data: []
      },{
        id: 'VWX',
        data: []
      },{
        id: 'YZ#',
        data: []
      }],
    }
  },
  computed: {
    showList() {
      return this.cardData.serviceItemList.slice((this.page.current - 1)*this.page.size,this.page.current*this.page.size)
    },
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
    changeRole(id) {
      if(this.searchServiceItemParam.roleWids != id) {
        this.page.current = 1
        this.searchServiceItemParam.roleWids = id
        this.searchParams('search')
      }
    },
    changeDept(id) {
      if(this.searchServiceItemParam.deptWids != id) {
        this.page.current = 1
        this.searchServiceItemParam.deptWids = id
        this.searchParams('search')
      }
    },
    changeSub(id) {
      if(this.searchServiceItemParam.categoryWids != id) {
        this.page.current = 1
        this.searchServiceItemParam.categoryWids = id
        this.searchParams('search')
      }
    },
    changePage(str) {
      if(this.pageId != str) {
        this.pageId = str
      }
    },
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
      //this.searchServiceitem()
    },
    handlePageSizeChange(size) {
      this.page.size = size
      //this.searchServiceitem()
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
    chineseToPinYin(str) {
      let l2 = str.length
      let i1 = ''
      let reg = new RegExp('[a-zA-Z0-9]');
      for (let i=0;i<l2;i++) {
        let val = str.substr(i,1)
        let name = this.arraySearch(val,pinyin)
        if(reg.test(val)) {
          i1 += val
        } else if(name !== false) {
          i1 += name
        }
      }
      i1 = i1.replace(/ /g,'-')
      while (i1.indexOf('--') > 0) {
        i1 = i1.replace('--','-')
      }
      return i1
    },
    arraySearch(l1,l2) {
      for(let name in l2) {
        if(pinyin[name].indexOf(l1) !== -1) {
          return this.ucfirst(name)
        }
      }
    },
    ucfirst(l1) {
      if(l1.length > 0) {
        let first = l1.substr(0,1).toUpperCase()
        let spare = l1.substr(1,l1.length)
        return first + spare
      }
    },
    initdatas() {
      window.shell.execCardMethod({
        cardId: "SYS_CARD_ALLSERVICEITEM",
        cardWid: "4390829058110519",
        method: 'renderData',
        param: {
          pageNumber: this.page.current,
          pageSize: this.page.size
        }
      }).then(res => {
        let list = []
        if(res.data.serviceItemList) {
          res.data.serviceItemList.forEach(item => {
            let temp = []
            item.datas.forEach(apps => {
              if(this.hasPerId.indexOf(apps.itemWid) > -1) {
                temp.push(apps)
              }
            })
            list = [...list,...temp]
          })
        }
        // console.log(list ,'lllllllllllllllllllll');
        const a = list.filter(item => !(item.itemCategory.indexOf('数字驾驶舱')>-1))
        // console.log(a,'aaaaaaaaaaaaaaa');
        list = a
        list.forEach(app => {
          let pyStr = this.chineseToPinYin(app.itemName).substr(0,1)
          if(pyStr == 'A') {
            this.appDataList[0].data.push(app)
          } else if(pyStr == 'B') {
            this.appDataList[0].data.push(app)
          } else if(pyStr == 'C') {
            this.appDataList[0].data.push(app)
          } else if(pyStr == 'D') {
            this.appDataList[1].data.push(app)
          } else if(pyStr == 'E') {
            this.appDataList[1].data.push(app)
          } else if(pyStr == 'F') {
            this.appDataList[1].data.push(app)
          } else if(pyStr == 'G') {
            this.appDataList[2].data.push(app)
          } else if(pyStr == 'H') {
            this.appDataList[2].data.push(app)
          } else if(pyStr == 'I') {
            this.appDataList[2].data.push(app)
          } else if(pyStr == 'J') {
            this.appDataList[3].data.push(app)
          } else if(pyStr == 'K') {
            this.appDataList[3].data.push(app)
          } else if(pyStr == 'L') {
            this.appDataList[3].data.push(app)
          } else if(pyStr == 'M') {
            this.appDataList[4].data.push(app)
          } else if(pyStr == 'N') {
            this.appDataList[4].data.push(app)
          } else if(pyStr == 'O') {
            this.appDataList[4].data.push(app)
          } else if(pyStr == 'P') {
            this.appDataList[5].data.push(app)
          } else if(pyStr == 'Q') {
            this.appDataList[5].data.push(app)
          } else if(pyStr == 'R') {
            this.appDataList[5].data.push(app)
          } else if(pyStr == 'S') {
            this.appDataList[6].data.push(app)
          } else if(pyStr == 'T') {
            this.appDataList[6].data.push(app)
          } else if(pyStr == 'U') {
            this.appDataList[6].data.push(app)
          } else if(pyStr == 'V') {
            this.appDataList[7].data.push(app)
          } else if(pyStr == 'W') {
            this.appDataList[7].data.push(app)
          } else if(pyStr == 'X') {
            this.appDataList[7].data.push(app)
          } else if(pyStr == 'Y') {
            this.appDataList[8].data.push(app)
          } else if(pyStr == 'Z') {
            this.appDataList[8].data.push(app)
          } else {
            this.appDataList[8].data.push(app)
          }
        })
      })
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
          pageNumber: 1,
          pageSize: 10000
        }
      })
      .then(res=> {
        window.minosStataCollect.loadEnd({
          listId: this.listId,
          endTime: new Date().getTime()
        });
        this.cardData = res.data
        if(res.data.filterItem) {
          res.data.filterItem.forEach(item => {
            if(item.propertyWid == 'subjectWid') {
              this.subList = item.data.filter(item => item.subjectName != '数字驾驶舱')
              console.log(this.subList,'this.subListthis.subListthis.subList');
            }
            if(item.propertyWid == 'deptWid') {
              this.deptList = item.data
            }
            if(item.propertyWid == 'roleWid') {
              this.roleList = item.data
            }
          })
        }
        let list = []
        res.data.serviceItemList.forEach(item => {
          if(this.hasPerId.indexOf(item.itemWid) > -1) {
            list.push(item)
          }
        })
        this.cardData.serviceItemList = list || []
        this.page.total = list.length || 0
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
        this.$nextTick(() => {
          this.loading = false
        })
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
      this.itemLoading[item.itemWid] = false
      item.favorite = item.favorite ? false : true
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
            pageNumber: 1,
            pageSize: 10000
          }
        },
        res => {
          let list = []
          res.data.serviceItemList.forEach(item => {
            if(this.hasPerId.indexOf(item.itemWid) > -1 && !(item.itemCategory.indexOf('数字驾驶舱')>-1)) {
              list.push(item)
            }
          })
          this.cardData.serviceItemList = list || []
          this.page.total = list.length || 0

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
          this.$nextTick(() => {
            this.loading = false
          })
        }
      )
      this.appDataList = [{
        id: 'ABC',
        data: []
      },{
        id: 'DEF',
        data: []
      },{
        id: 'GHI',
        data: []
      },{
        id: 'JKL',
        data: []
      },{
        id: 'MNO',
        data: []
      },{
        id: 'PQR',
        data: []
      },{
        id: 'STU',
        data: []
      },{
        id: 'VWX',
        data: []
      },{
        id: 'YZ#',
        data: []
      }]
      window.shell.execCardMethod(
              {
                cardId: "SYS_CARD_ALLSERVICEITEM",
                cardWid: "4390829058110519",
                method: 'searchServiceItem',
                param: {
                  ...this.searchServiceItemParam,
                  pageNumber: 1,
                  pageSize: 10000
                }
              },
              res => {
                let list = []
                if(res.data.serviceItemList) {
                  res.data.serviceItemList.forEach(item => {
                    let temp = []
                    item.datas.forEach(apps => {
                      if(this.hasPerId.indexOf(apps.itemWid) > -1 && !(apps.itemCategory.indexOf('数字驾驶舱')>-1)) {
                        temp.push(apps)
                      }
                    })
                    list = [...list,...temp]
                  })
                }
                list.forEach(app => {
                  let pyStr = this.chineseToPinYin(app.itemName).substr(0,1)
                  if(pyStr == 'A') {
                    this.appDataList[0].data.push(app)
                  } else if(pyStr == 'B') {
                    this.appDataList[0].data.push(app)
                  } else if(pyStr == 'C') {
                    this.appDataList[0].data.push(app)
                  } else if(pyStr == 'D') {
                    this.appDataList[1].data.push(app)
                  } else if(pyStr == 'E') {
                    this.appDataList[1].data.push(app)
                  } else if(pyStr == 'F') {
                    this.appDataList[1].data.push(app)
                  } else if(pyStr == 'G') {
                    this.appDataList[2].data.push(app)
                  } else if(pyStr == 'H') {
                    this.appDataList[2].data.push(app)
                  } else if(pyStr == 'I') {
                    this.appDataList[2].data.push(app)
                  } else if(pyStr == 'J') {
                    this.appDataList[3].data.push(app)
                  } else if(pyStr == 'K') {
                    this.appDataList[3].data.push(app)
                  } else if(pyStr == 'L') {
                    this.appDataList[3].data.push(app)
                  } else if(pyStr == 'M') {
                    this.appDataList[4].data.push(app)
                  } else if(pyStr == 'N') {
                    this.appDataList[4].data.push(app)
                  } else if(pyStr == 'O') {
                    this.appDataList[4].data.push(app)
                  } else if(pyStr == 'P') {
                    this.appDataList[5].data.push(app)
                  } else if(pyStr == 'Q') {
                    this.appDataList[5].data.push(app)
                  } else if(pyStr == 'R') {
                    this.appDataList[5].data.push(app)
                  } else if(pyStr == 'S') {
                    this.appDataList[6].data.push(app)
                  } else if(pyStr == 'T') {
                    this.appDataList[6].data.push(app)
                  } else if(pyStr == 'U') {
                    this.appDataList[6].data.push(app)
                  } else if(pyStr == 'V') {
                    this.appDataList[7].data.push(app)
                  } else if(pyStr == 'W') {
                    this.appDataList[7].data.push(app)
                  } else if(pyStr == 'X') {
                    this.appDataList[7].data.push(app)
                  } else if(pyStr == 'Y') {
                    this.appDataList[8].data.push(app)
                  } else if(pyStr == 'Z') {
                    this.appDataList[8].data.push(app)
                  } else {
                    this.appDataList[8].data.push(app)
                  }
                })
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
    window.shell.execCardMethod({
      "cardId": "CUS_CARD_DP",
      "cardWid": "5439364064588545",
      "method": "querySimInfoForRoleAuth",
      param: {
        pageNumber: 1,
        pageSize: 10000
      }
    }).then(res => {
      let list = res.data.data.data
      if(list && list.length) {
        list.forEach(item => {
          this.hasPerId.push(item.itemWid)
        })
        this.initdata()
        this.initdatas()
      }
    })
    /*window.shell.execCardMethod({
      "cardId": "CUS_CARD_DP",
      "cardWid": "5439364064588545",
      "method": "querySimInfoForRole",
      param: {
        pageNumber: 1,
        pageSize: 10000
      }
    }).then(res => {
      let list = res.data.data
      if(list && list.length) {
        list.forEach(item => {
          this.hasPerId.push(item.wid)
        })
        this.initdata()
        this.initdatas()
      }
    })*/

    this.currentUser = window.shell.getUserInfo()
    this.placeholder = window.shell.placeholderEllipsis(
      this.getLanguageValue('search_placeholder', '请输入服务事项名称'),
      360
    )
    /*window.shell.on('collectServiceItem', this.process)*/
  },
  mounted() {
    window.shell.on("onScoll", (ev) => {
      console.log(ev,2333)
    });
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
  position: relative;

  .cus-filter{
    .cus-filter-item{
      display: flex;
      align-items: flex-start;
      .fliter-items{
        display: flex;
        flex-wrap: wrap;
        flex: 1;
        width: 0;
        div{
          margin-left: 8px;
          height: 30px;
          padding: 0 12px;
          line-height: 30px;
          color: #262626;
          font-size: 14px;
          cursor: pointer;
          margin-bottom: 12px;
          &.active{
            background: #F2F7FE;
            border-radius: 4px;
          }
        }
      }
      .cus-filter-item-lable{
        color: #595959;
        font-size: 14px;
        line-height: 30px;
        width: 70px;
      }
    }
  }
  .cus-page-tab{
    position: absolute;
    display: flex;
    align-items: center;
    top: -40px;
    right: 0;
    .page-tab-item{
      width: 98px;
      height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      color: #595959;
      font-size: 14px;
      line-height: 20px;
      &.active{
        border-radius: 4px;
      }
      img{
        width: 16px;
        margin-right: 4px;
      }
    }
  }
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
