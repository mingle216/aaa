<template>
  <div class="associtive__dropDown-loading">
    <div class="mb-20" v-for="el in dropDownList" :key="el.label">
      <div class="mb-20" v-if="el.value.length">
        <div class="associtive__dropDown-info portal-font-color-lv3">
          {{ el.name }}
        </div>
        <div
          class="associtive__dropDown-list portal-primary-backgroundcolor-hover-lv5"
          v-for="(item, index) in el.value"
          :key="index"
          @click="openUrl(el.label, item)"
        >
      
          <template v-if="el.label === 'newsItem'">
            <div
              class="associtive__dropDown-service ellipsis portal-font-color-lv1"
              v-html="handleHighLighter(item.title)"
            ></div>
            <div class="favorite-right" v-if="showAssociation">
              <i
                class="iconfont icon-favorites"
                :class="{
                  favorite_font: item.favorite === 'true',
                  unfavorite_font: item.favorite !== 'true',
                }"
                @click.stop="collectNews(item)"
              ></i>
            </div>
          </template>
          <template v-else-if="el.label === 'onlineService'">
        
            <div :class="{ 'no-permission-service': !item.permission }">
              <img
                :src="item.iconLink || errorImg"
                class="associtive__dropDown-service-img"
                @error="handleError"
              />
            </div>
            <div
              class="ml-12 associtive__dropDown-service ellipsis"
              :class="[
                item.permission
                  ? 'portal-font-color-lv1'
                  : 'portal-font-color-lv4',
              ]"
              v-html="handleHighLighter(item.serviceName)"
            ></div>
        
            <div class="favorite-right" v-if="showAssociation">
              <i
                class="iconfont icon-favorites"
                :class="{
                  favorite_font: item.favorite,
                  unfavorite_font: !item.favorite,
                }"
                @click.stop="collectApp(item)"
              ></i>
            </div>
          </template>
          <template v-else-if="el.label === 'oneThing'">
            <div
              class="associtive__dropDown-service ellipsis portal-font-color-lv1"
              v-html="handleHighLighter(item.oneThingName)"
            ></div>
            <div class="favorite-right" v-if="showAssociation">
              <i
                class="iconfont icon-shixiangshoucang"
                :class="[item.favorite ? 'favorite_font favorite_font_color' : 'unfavorite_font']"
                @click.stop="collectOneThing(item)"
              ></i>
            </div>
          </template>
          <template v-else-if="el.label === 'dept'">
            <div
              class="associtive__dropDown-service ellipsis portal-font-color-lv1"
              v-html="handleHighLighter(item.deptName)"
            ></div>
          </template>
          <template v-else>
            <div
              class="associtive__dropDown-service ellipsis portal-font-color-lv1"
              v-html="handleHighLighter(item.itemName)"
            ></div>
            <div class="favorite-right" v-if="showAssociation">
              <i
                class="iconfont icon-shixiangshoucang"
                :class="[item.favorite ? 'favorite_font favorite_font_color' : 'unfavorite_font']"
                @click.stop="collectItem(item)"
              ></i>
            </div>
          </template>
        </div>
      </div>
    </div>
    <div
      class="associate-empty portal-font-color-lv3"
      v-if="dropDownList.length === 0"
    >
      {{ $Lan("SYS_TEMPLATE_OFFICIAL", "noSearchResult", "暂无搜索内容") }}
    </div>
  </div>
</template>

<script>
export default {
  props: ["keyword", "showAssociation"],
  data() {
    return {
      newsItem: [],
      serviceLists: [],
      guidanceLists: [],
      errorImg: window.shell.ErrorImg,
      dropDownList: [],
      analyzeData: [],
      isCollectApp: true,
      isCollectItem: true,
      isCollectNews: true,
      isCollectOneThing: true
    };
  },
  computed: {
    reg() {
      return new RegExp(`(${this.keyword.trim()})`, "ig");
    },
  },
  watch: {
    keyword: {
      async handler() {
        await this.getAnalyzeData();
        await this.handleSearch();
      },
      immediate: true,
    },
  },
  methods: {
    openUrl(label, item) {
      if (label === "newsItem") {
        this.openNews(item);
      } else if (label === "onlineService") {
        this.openService(item);
      } else if (label === "oneThing") {
        this.openOneThingDetail(item);
      } else if (label === "dept") {
        this.openDept(item);
      } else {
        this.openServiceDetail(item);
      }
    },
    // 打开新闻
    openNews(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 11,
            newId: item.wid,
            newName: item.title
          },
        },
        startTime: new Date().getTime(),
      });
      if (item.sideFlag == 2) {
        //跳转到本地新闻详情页面
        window.shell.openPage(`/newsDetail?wid=${item.wid}`, 1, 1);
      } else {
        window.open(item.url);
      }
    },
    // 打开服务
    openService(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 11,
            serviceId: item.serviceWid,
          },
        },
        startTime: new Date().getTime(),
      });
      if (!item.permission) {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan(
            "public",
            "noServicePermission",
            "暂无使用权限，请联系管理员"
          ),
        });
        return;
      }
      window.shell.openService({
        ...item,
        wid: item.serviceWid,
        name: item.serviceName,
      });
    },
    // 打开服务事项详情
    openServiceDetail(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 11,
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
    // 打开一件事详情
    openOneThingDetail(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 11,
            itemId: item.oneThingWid,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.visitService(item.oneThingWid, "OneThing"); // 一件事详情
      window.shell.openPage(`/thingdetail?wid=${item.oneThingWid}`, 1, 1);
    },
    // 打开部门
    openDept(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 11,
            deptId: item.deptWid,
          },
        },
        startTime: new Date().getTime(),
      });
      if (item.deptLinkUrl) {
        window.open(item.deptLinkUrl);
      }
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    handleHighLighter(item) {
      const data = JSON.parse(JSON.stringify(this.analyzeData));
      return window.shell.strHighlighted(item, data);
    },
    getAnalyzeData() {
      window.shell
        .execTemplateMethod("analyzeSearch", {
          keywords: this.keyword,
          lang: sessionStorage.getItem("locale") || "zh_CN",
        })
        .then((res) => {
          this.analyzeData = res.data?.analyzeData?.map((el) => el.token) || [];
        });
    },
    handleSearch() {
      window.shell.execTemplateMethod(
        "matchSearch",
        {
          keywords: this.keyword,
          lang: sessionStorage.getItem("locale") || "zh_CN",
        },
        (data) => {
          let oneThing = [];
          let dept = [];
          if (data.errcode === "0" && data.data) {
            const newsItem = data.data.newsItem || [];
            const services = data.data.service || [];
            const serviceItem = data.data.serviceItem || [];
            this.newsItem =
              newsItem.length > 5 ? newsItem.slice(0, 5) : newsItem;
            this.serviceLists =
              services.length > 5 ? services.slice(0, 5) : services;
            this.guidanceLists =
              serviceItem.length > 5 ? serviceItem.slice(0, 5) : serviceItem;
            oneThing = data.data.oneThing || [];
            oneThing = oneThing.length > 5 ? oneThing.slice(0, 5) : oneThing;
            dept = data.data.dept || [];
            dept = dept.length > 5 ? dept.slice(0, 5) : dept;
          } else {
            this.newsItem = [];
            this.serviceLists = [];
            this.guidanceLists = [];
          }
          if (
            this.newsItem.length ||
            this.serviceLists.length ||
            this.guidanceLists.length ||
            oneThing.length ||
            dept.length
          ) {
            this.dropDownList = [
              {
                label: "onlineService",
                name: data.data.serviceGroupName ?? "在线服务",
                value: this.serviceLists,
                orderNumber: data.data.serviceOrderNumber,
              },
              {
                label: "serviceItem",
                name: data.data.serviceItemGroupName ?? "服务事项",
                value: this.guidanceLists,
                orderNumber: data.data.serviceItemOrderNumber,
              },
              {
                label: "newsItem",
                name: data.data.newsGroupName ?? "资讯",
                value: this.newsItem,
                orderNumber: data.data.newsOrderNumber,
              },
              {
                label: "oneThing",
                name: data.data.oneThingGroupName ?? "一件事",
                funcName: "public",
                value: oneThing,
                orderNumber: data.data.oneThingOrderNumber,
              },
              {
                label: "dept",
                name: data.data.deptGroupName ?? "部门",
                value: dept,
                orderNumber: data.data.deptOrderNumber,
              },
            ].sort((a, b) => a.orderNumber - b.orderNumber);
          } else {
            this.dropDownList = [];
          }
        }
      );
    },
    // 收藏新闻
    collectNews(item) {
      if (!this.isCollectNews) {
        return;
      }
      this.isCollectNews = false;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 20,
            newId: item.wid,
            fucType: item.favorite === 'true' ? 3 : 2,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.collectNews({
        id: item.wid,
        operate: item.favorite === 'true' ? 0 : 1,
      });
    },
    // 收藏服务
    collectApp(item) {
      if (!this.isCollectApp) {
        return;
      }
      this.isCollectApp = false;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 0,
            serviceId: item.serviceWid,
            fucType: item.favorite ? 3 : 2,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.collectService({
        operate: item.favorite ? 0 : 1,
        id: item.serviceWid,
        showFolderPop: true
      });
    },
    // 收藏事项
    collectItem(item) {
      if (!this.isCollectItem) {
        return;
      }
      this.isCollectItem = false;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 1,
            itemId: item.itemWid,
            fucType: item.favorite ? 3 : 2,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.collectServiceItem({
        id: item.itemWid, //事项ID 收藏或取消收藏时用
        operate: item.favorite ? 0 : 1, // 收藏操作标识 0：取消收藏 1：收藏
      });
    },
    // 收藏一件事
    collectOneThing(item) {
      if (!this.isCollectOneThing) {
        return;
      }
      this.isCollectOneThing = false;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 17,
            onethingId: item.oneThingWid,
            fucType: item.favorite ? 3 : 2,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.collectOneThing({
        id: item.oneThingWid, //一件事ID 收藏或取消收藏时用
        operate: item.favorite ? 0 : 1, // 收藏操作标识 0：取消收藏 1：收藏
      });
    },
    updateAppList({ id, res, operate }) {
      this.isCollectApp = true;
      if (res.errcode === "0") {
        this.dropDownList.forEach((el) => {
          if (el.label === "onlineService") {
            (el.value || []).forEach((it) => {
              if (it.serviceId === id) {
                it.favorite = operate;
              }
            });
          }
        });
      }
    },
    updateItemList({ id, res, operate }) {
      this.isCollectItem = true;
      if (res.errcode === "0") {
        this.dropDownList.forEach((el) => {
          if (el.label === "serviceItem") {
            (el.value || []).forEach((it) => {
              if (it.itemWid === id) {
                it.favorite = operate;
              }
            });
          }
        });
      }
    },
    updateNewsList({ id, res, operate }) {
      this.isCollectNews = true;
      if (res.errcode === "0") {
        this.dropDownList.forEach((el) => {
          if (el.label === "newsItem") {
            (el.value || []).forEach((it) => {
              if (it.wid === id) {
                it.favorite = operate ? 'true' : 'false';
              }
            });
          }
        });
      }
    },
    updateOneThingList({ id, res, operate }) {
      this.isCollectOneThing = true;
      if (res.errcode === "0") {
        this.dropDownList.forEach((el) => {
          if (el.label === "oneThing") {
            (el.value || []).forEach((it) => {
              if (it.oneThingWid === id) {
                it.favorite = operate;
              }
            });
          }
        });
      }
    },
  },
  mounted() {
    window.shell.on("collectApp", this.updateAppList);
    window.shell.on("collectServiceItem", this.updateItemList);
    window.shell.on("collectNews", this.updateNewsList);
    window.shell.on("collectOneThing", this.updateOneThingList);
  },
  destroyed() {
    window.shell.off("collectApp", this.updateAppList);
    window.shell.off("collectServiceItem", this.updateItemList);
    window.shell.off("collectNews", this.updateNewsList);
    window.shell.off("collectOneThing", this.updateOneThingList);
  },
};
</script>

<style lang="less" scoped>
.ml-12 {
  margin-left: 12px;
}
.mb-20 {
  margin-bottom: 20px;
}
.associtive__dropDown-info {
  font-size: 12px;
  padding: 0 12px;
  margin-bottom: 8px;
}
.associtive__dropDown-service-img {
  width: 28px;
  height: 28px;
  border-radius: 4px;
  vertical-align: middle;
}
.associtive__dropDown-list {
  cursor: pointer;
  padding: 8px 12px;
  font-size: 14px;
  display: flex;
  align-items: center;
  .favorite-right {
    font-size: 16px;
    flex-shrink: 0;
    .favorite_font_color {
      color: #fa6444;
    }
  }
}
.associtive__dropDown-service {
  flex: 1;
  min-width: 0;
}
.associate-empty {
  font-size: 16px;
  padding: 15px 12px;
  text-align: center;
}
</style>
