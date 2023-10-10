<template>
  <div class="homeService" ref="AllServiceItem">
    <div class="cat-top portal-primary-backgroundcolor-lv1">
      <span>{{ $Lan(lanFunName, "title", "服务事项分类") }}</span>
      <span
        style="font-size: 12px; cursor: pointer"
        @click="handleMore('hall', '', '')"
        >{{ $Lan(lanFunName, "more", "MORE+") }}</span
      >
    </div>
    <div class="server-content" v-if="filterList.length" @mouseleave="hideRight">
      <AutoContainer :con-type="2" :con-height="612">
        <div
          class="server-filter-item"
          @mouseenter="showRight(item)"
          @click="handleMore('hall', item.subjectWid, '')"
          v-for="item in filterList"
          :key="item.subjectWid"
        >
          <div class="filter-box">
            <div class="filter-name portal-font-color-lv1">
              <p
                class="ellipsis"
                :class="{ 'portal-primary-color-lv1': categoryWid === item.subjectWid }"
              >
                {{ item.subjectName }}
              </p>
              <i class="we-icon-arrow-right"></i>
            </div>
            <div class="filter-child portal-font-color-lv3" v-if="item.children">
              <p>
                <span
                  class="portal-primary-color-hover-lv1"
                  v-for="app in item.children.slice(0, 3)"
                  :key="app.subjectWid"
                  @click.stop="handleMore('hall', item.subjectWid, app.subjectWid)"
                >
                  {{ app.subjectName }}
                </span>
              </p>
            </div>
          </div>
        </div>
      </AutoContainer>
      <div class="cus-right-wrap" v-if="isShowRight">
        <empty-con
        v-if="!navList || !navList.length"
          :tip="$Lan(lanFunName, 'noServiceItem', '暂无相关服务事项')"
          :height="500"
        ></empty-con>
        <div class="service-app-box" v-loading="loading">
          <div class="right-item" v-for="item in navList" :key="item.subjectWid">
            <h3 class="portal-font-color-lv1">{{ item.subjectName }}</h3>
            <div class="right-app-box portal-font-color-lv3">
              <div
                class="app-list-item ellipsis portal-primary-color-hover-lv1"
                v-for="app in item.serviceList"
                :key="app.itemWid"
                @click="openServiceDetail(app)"
                :title="app.itemName"
              >
                {{ app.itemName }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CardAllServiceItem",
  props: {
    index: Number,
    router: Object,
  },
  components: {},
  data() {
    const { cardId, cardWid } = this.router;
    return {
      lanFunName: cardId,
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      keyWord: "",
      selectedVal: 1,
      errorImg: window.shell.ErrorImg,
      cardData: {},
      serviceItemListLimit: [], //展示20行数据
      card: {
        cardId: cardId,
        cardWid: cardWid,
      },
      //服务事项
      currentUser: null,
      content_width: {},
      //搜索条件
      searchServiceItemParam: {
        searchKey: "",
        categoryWids: "", //服务主题wid（多个逗号隔开）
        deptWids: "", //责任部门wid（多个逗号隔开）
        roleWids: "", //服务对象wid（多个逗号隔开
        srvDeptWids: "", //服务部门wid（多个逗号隔开）
        dimensions: "", //分类维度wid（多个逗号隔开）
        availableOnline: false, // 可在线办理
        orderByVisitCount: false, // 访问量排序
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
        size: 100,
      },
      containerWidth: 1128,
      placeholder: "",
      filterList: [],
      isShowRight: false,
      navList: [],
      categoryWid: "",
    };
  },
  computed: {
    cardConfig() {
      return this.cardData.config || {};
    },
    allServiceItemsDisplay() {
      return this.cardConfig.allServiceItemsDisplay || {};
    },
    // 0平铺、1列表
    cardOrTable() {
      return this.allServiceItemsDisplay.displayType || "0";
    },
    // 筛选项展示信息 -1在线办理、0访问量、1服务主题、2责任部门、3服务对象
    filterFields() {
      return this.allServiceItemsDisplay.filterFields || [];
    },
    // 是否展示搜索
    showSearch() {
      return this.cardConfig && this.cardConfig.search === "1";
    },
    // 服务事项列表展示信息 0图标 1服务主题 2服务对象 3责任部门 4访问量
    showItem() {
      return this.allServiceItemsDisplay.itemDisplayInfo || [];
    },
    // 是否有查询条件，有的话重置筛选项高亮
    hasFilterParams() {
      return this.selectedFilter.some((el) => {
        return el.data.length > 0;
      });
      // const val = Object.values(this.searchServiceItemParam)
      // return val.some(el => el)
    },
  },
  methods: {
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    handleSearch() {
      this.page.current = 1;
      this.searchServiceItemParam.searchKey = this.keyWord.trim();
      this.searchParams("search");
    },
    handlePageChange(page) {
      this.page.current = page;
      this.searchServiceitem();
    },
    handlePageSizeChange(size) {
      this.page.size = size;
      this.searchServiceitem();
    },
    // 清空筛选项
    handleReset() {
      this.selectedFilter = [];
      for (let i in this.searchServiceItemParam) {
        if (i !== "orderByVisitCount" && i !== "availableOnline" && i !== "searchKey") {
          this.searchServiceItemParam[i] = "";
        }
      }
      this.handleUpdateList();
    },
    // 删除已选的筛选项展示信息过滤条件
    handleDeleteFilter(param) {
      const { item, selectedItem } = param;
      this.selectedFilter.forEach((el) => {
        if (el.propertyWid === "subjectWid") {
          el.data = el.data.filter((t) => t.wid !== item.wid && t.parent !== item.wid);
        } else {
          if (el.alias === selectedItem.alias) {
            el.data = el.data.filter((t) => t.wid !== item.wid);
          }
        }
      });
      this.handleUpdateList();
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
          extraInfo: "",
        },
        startTime: new Date().getTime(),
      });
      this.loading = true;
      window.shell
        .execCardMethod({
          ...this.card,
          method: "renderData",
          param: {
            pageNumber: this.page.current,
            pageSize: this.page.size,
          },
        })
        .then((res) => {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime(),
          });
          this.cardData = res.data;
          this.filterList =
            (res.data?.filterItem || []).find((i) => {
              return i.propertyName == "subjectName";
            })?.data || [];
          this.cardData.filterItem = (res.data?.filterItem || []).map((el) => {
            const staticName = ["subjectName", "deptName", "roleName"];
            const staticMap = {
              subjectName: "serviceItemCategory",
              deptName: "serviceItemDept",
              roleName: "roleName",
            };
            if (staticName.includes(el.propertyName)) {
              el.alias = this.getLanguageValue(staticMap[el.propertyName], el.alias);
            }
            return {
              ...el,
            };
          });
          this.cardData.serviceItemList = res.data.serviceItemList || [];
          this.page.total = res.data.total || 0;
          if (this.cardOrTable === "0") {
            for (var item of this.cardData.serviceItemList) {
              for (var demo of item.datas) {
                this.$set(demo, "wid", demo.itemWid);
                this.$set(demo, "name", demo.itemName);
              }
            }
            // 排序服务事项
            this.sortServiceItemList(this.cardData.serviceItemList);
            // 前面操作处理数据，后面的将serviceItemList赋给 副本用于显示
            this.serviceItemListToLimit();

            this.$nextTick(() => {
              this.content_width = {
                width: `${
                  this.$refs.ServiceItemContent &&
                  this.$refs.ServiceItemContent.offsetWidth
                }px`,
              };
            });
          }
          this.$nextTick(() => {
            this.loading = false;
          });
        })
        .catch(() => {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime(),
          });
        });
    },
    serviceItemListToLimit() {
      this.serviceItemListLimit = [];
      this.showMore = false;
      var rows = 0;
      var list = JSON.parse(JSON.stringify(this.cardData.serviceItemList));
      for (var item of list.slice(0)) {
        var itemRows = Math.ceil(item.datas.length / 5);
        if (itemRows + rows > this.showServiceItemsLine) {
          item.datas.splice((this.showServiceItemsLine - rows) * 5, item.datas.length);
          rows = rows + itemRows;
          this.serviceItemListLimit.push(item); // 副标题下有多的话，则截5*剩余行数的数量
          break;
        } else {
          rows = rows + itemRows;
          this.serviceItemListLimit.push(item);
        }
      }
      if (rows > this.showServiceItemsLine) {
        this.showMore = true;
      }
    },
    async collectItem(item) {
      if (this.itemLoading[item.itemWid]) {
        return;
      }
      this.itemLoading[item.itemWid] = true;
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
            fucType: item.favorite ? 3 : 2,
          },
        },
        startTime: new Date().getTime(),
      });
      await window.shell.collectServiceItem({
        cardId: this.$options.name,
        id: item.itemWid, //事项ID 收藏或取消收藏时用
        operate: item.favorite ? 0 : 1, // 收藏操作标识 0：取消收藏 1：收藏
      });
      // item.favorite = item.favorite ? false : true
    },
    process(el) {
      if (this.cardOrTable === "0") {
        for (let item of this.serviceItemListLimit) {
          if (item.datas?.length) {
            item.datas.forEach((ele) => {
              if (el.id === ele.wid) {
                ele.favorite = !ele.favorite;
              }
            });
          }
        }
      } else {
        for (let item of this.cardData.serviceItemList) {
          if (item.itemWid === el.id) {
            item.favorite = item.favorite ? false : true;
          }
        }
      }
      this.itemLoading[el.id] = false;
      if (el.cardId !== this.$options.name) {
        this.searchServiceitem();
      }
    },
    handleUpdateList() {
      let dimensions = [];
      this.selectedFilter.forEach((el) => {
        if (el.propertyWid === "subjectWid") {
          const first = (el.data || []).filter((el) => el.parent === "subjectWid"); // 一级分类
          if (first.length) {
            // 如果一级分类存在则清空二级
            const pId = first.map((el) => el.wid);
            const temp = (el.data || [])
              .filter((el) => !pId.includes(el.parent))
              .map((t) => t.wid);
            this.searchServiceItemParam.categoryWids = temp.join(",");
          } else {
            this.searchServiceItemParam.categoryWids = (el.data || [])
              .map((el) => el.wid)
              .join(",");
          }
        } else if (el.propertyWid === "deptWid") {
          this.searchServiceItemParam.deptWids = (el.data || [])
            .map((el) => el.wid)
            .join(",");
        } else if (el.propertyWid === "roleWid") {
          this.searchServiceItemParam.roleWids = (el.data || [])
            .map((el) => el.wid)
            .join(",");
        } else {
          const temp = (el.data || []).map((el) => el.wid);
          dimensions.push(...temp);
        }
      });
      this.searchServiceItemParam.dimensions = dimensions.join(",");
      this.page.current = 1;
      this.searchServiceitem();
    },
    searchParams(isSearch = false) {
      this.page.current = 1;
      this.searchServiceitem(isSearch);
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
            infoType: isSearch && isSearch === "search" ? 3 : 2,
            filterInfo: {
              ...this.searchServiceItemParam,
              pageNumber: this.page.current,
              pageSize: this.page.size,
            },
          },
        },
        startTime: new Date().getTime(),
      });
      this.loading = true;
      window.shell.execCardMethod(
        {
          ...this.card,
          method: "searchServiceItem",
          param: {
            ...this.searchServiceItemParam,
            pageNumber: this.page.current,
            pageSize: this.page.size,
          },
        },
        (res) => {
          this.cardData.serviceItemList = res.data.serviceItemList || [];
          this.page.total = res.data.total || 0;

          if (this.cardOrTable === "0") {
            this.showServiceItemsLine = 20;
            for (var item of this.cardData.serviceItemList) {
              for (var demo of item.datas) {
                this.$set(demo, "wid", demo.itemWid);
                this.$set(demo, "name", demo.itemName);
              }
            }
            // 排序服务事项
            this.sortServiceItemList(this.cardData.serviceItemList);
            this.serviceItemListToLimit();
          }
          this.$nextTick(() => {
            this.loading = false;
          });
        }
      );
    },
    openServiceItem(item) {
      window.shell.openServiceItem(item);
    },
    //排序，数字-特殊字符
    sortServiceItemList(list) {
      if (list[list.length - 1] && list[list.length - 1].navValue == "other") {
        var a = [];
        for (var i = 0; i < 10; i++) {
          a[i] = i + "";
        }
        var other = list[list.length - 1].datas.slice(0);
        var numArray = [];
        var otherArray = [];
        for (var j = 0; j < other.length; j++) {
          if (a.includes(other[j].itemName.substring(0, 1))) {
            numArray.push(other[j]);
          } else {
            otherArray.push(other[j]);
          }
        }
        list[list.length - 1].datas = numArray.concat(otherArray);
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
          cardName: this.router.cardName,
        },
        startTime: new Date().getTime(),
      });
      this.showMore = false;
      this.showServiceItemsLine += 20;
      this.serviceItemListToLimit();
    },

    hideRight() {
      this.isShowRight = false;
      this.navList = [];
      this.categoryWid = "";
    },
    showRight(item) {
      this.isShowRight = true;
      this.navList = item.children;
      this.categoryWid = item.subjectWid;
      this.getAppList();
    },
    async getAppList() {
      if(!this.navList || !this.navList.length){
        return;
      }
      this.loading = true;
      await this.navList.forEach(async (item) => {
        await window.shell.execCardMethod(
          {
            ...this.card,
            method: "searchServiceItem",
            param: {
              ...this.searchServiceItemParam,
              categoryWids: item.subjectWid,
              pageNumber: this.page.current,
              pageSize: this.page.size,
            },
          },
          (res) => {
            let resp = res.data.serviceItemList || [];
            this.$set(item, "serviceList", resp);
            this.$nextTick(() => {
              this.loading = false;
            });
          }
        );
      });
      this.loading = false;
    },
    openServiceDetail(item) {
      if (item.workGuide && "isAuthorized" in item && item.isAuthorized === 0) {
        window.shell.showMessage({
          type: "warning",
          message: `${this.$Lan("public", "workGuideNoAuth", "您无权限查看")}`,
        });
        return false;
      }
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
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.openServiceItem({
        ...item,
        wid: item.itemWid,
        name: item.itemName,
      });
    },
    handleChangeTab({ index }) {
      const item = this.categoryDatas[Number(index)] || {};
      this.categroyType = {
        categoryName: item.categoryName,
        categoryWid: item.categoryWid,
      };
      this.handleClickTrack({
        infoType: 8,
        filterInfo: {
          categoryWid: item.categoryWid,
        },
      }); // 点击埋点
    },
    handleMore(url, itemWid, secondWid) {
      localStorage.removeItem("filterRole");
      window.shell.openPage(url, 0, 1);
      let obj = {
        url,
        itemWid,
        secondWid,
      };
      localStorage.setItem("myTodo", JSON.stringify(obj));
    },
  },
  created() {
    this.initdata();
    this.currentUser = window.shell.getUserInfo();
    this.placeholder = window.shell.placeholderEllipsis(
      this.getLanguageValue("search_placeholder", "请输入服务事项名称"),
      360
    );
    window.shell.on("collectServiceItem", this.process);
  },
  mounted() {
    this.containerWidth = this.$refs.AllServiceItem.offsetWidth;
  },

  beforeDestroy() {
    window.shell.off("collectServiceItem");
  },
};
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
.server-content {
  width: 100%;
  overflow: auto;
  background: #ffffff;
  border-radius: 0px 0px 0px 0px;
  z-index: 11;
  transition: all 0.3s;
  .server-filter-item {
    height: 68px;
    width: 100%;
    padding: 0 16px;
    cursor: pointer;
    .filter-box {
      height: 100%;
      box-shadow: inset 0px -1px 0px rgba(157, 173, 193, 0.16);
      padding: 10px 0;
      .filter-child {
        font-size: 13px;
        height: 22px;
        color: #767992;
        display: flex;
        align-items: center;
        margin-top: 4px;
        p {
          // width: 100%;
          display: flex;
        }
        span {
          display: inline-block;
          position: relative;
          border-right: 1px solid #a1a4bb;
          padding: 0 8px;
          &:nth-of-type(1) {
            padding-left: 0;
          }
          &:nth-last-of-type(1) {
            border: 0;
          }
        }
      }
      .filter-name {
        display: flex;
        align-items: center;
        font-size: 16px;
        line-height: 24px;
        color: #222a38;
        i {
          margin-left: auto;
        }
        p {
          width: 90%;
          font-size: 15px;
        }
      }
    }
    &:hover {
      box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.1);
      .filter-box {
        box-shadow: none;
      }
      .filter-name {
        color: #215ed0;
      }
    }
  }
}
.cat-top {
  height: 50px;
  color: #fff;
  font-size: 16px;
  border-radius: 4px 4px 0 0;
  padding: 0 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.homeService {
  position: relative;
  top: -8px;
}
.cus-right-wrap {
  width: 445px;
  height: 612px;
  overflow-y: auto;
  position: absolute;
  top: 50px;
  left: 100%;
  background: #ffffff;
  box-shadow: 0px 0px 20px #e6e7ee;
  padding: 30px 24px;
  z-index: 12;
}
.service-app-box {
  width: 100%;
  column-count: 2;
  .right-item {
    width: 100%;
    break-inside: avoid;
    margin-bottom: 20px;
    .right-app-box {
      div {
        width: 100%;
        font-weight: 400;
        font-size: 12px;
        line-height: 15px;
        margin-bottom: 15px;
        color: #767992;
        cursor: pointer;
        &:hover {
          color: #215ed0;
        }
      }
    }
    h3 {
      font-weight: 700;
      font-size: 12px;
      line-height: 15px;
      color: #222a38;
      margin-bottom: 20px;
    }
  }
}
</style>
