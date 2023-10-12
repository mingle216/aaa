<template>
  <div class="associtive__dropDown-loading" v-loading="loading">
    <div v-for="el in dropDownList" :key="el.label">
      <div
        v-if="el.value.length"
        :class="[searchTab.groupWid === 'all' ? 'mb-20' : '']"
      >
        <div
          class="associtive__dropDown-info portal-font-color-lv3"
          v-if="searchTab.groupWid === 'all'"
        >
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
          </template>
          <template v-else-if="el.label === 'serviceItem'">
            <div
              class="associtive__dropDown-service ellipsis portal-font-color-lv1"
              v-html="handleHighLighter(item.itemName)"
            ></div>
          </template>
          <template v-else-if="el.label === 'oneThing'">
            <div
              class="associtive__dropDown-service ellipsis portal-font-color-lv1"
              v-html="handleHighLighter(item.oneThingName)"
            ></div>
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
              v-html="handleHighLighter(item.title)"
            ></div>
          </template>
        </div>
      </div>
    </div>
    <div
      class="associate-empty portal-font-color-lv3"
      v-if="dropDownList.length === 0"
    >
      {{ $Lan("CUS_TEMPLATE_CQWORK", "noSearchResult", "暂无搜索内容") }}
    </div>
  </div>
</template>

<script>
export default {
  props: ["keyword", "searchTab"],
  data() {
    return {
      errorImg: window.shell.ErrorImg,
      dropDownList: [],
      analyzeData: [],
      loading: false,
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
      } else if (label === "serviceItem") {
        this.openServiceDetail(item);
      } else if (label === "oneThing") {
        this.openOneThingDetail(item);
      } else if (label === "dept") {
        this.openDept(item);
      } else {
        this.openCustom(item);
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
    // 打开自定义分组
    openCustom(item) {
      if (!item.url) {
        return;
      }
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 11,
            customWid: item.wid,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.openUrl(item.url);
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
      this.dropDownList = [];
      this.loading = true;
      window.shell.execTemplateMethod(
        "matchSearch",
        {
          keywords: this.keyword,
          lang: sessionStorage.getItem("locale") || "zh_CN",
          groupWid:
            this.searchTab.groupWid === "all" ? "" : this.searchTab.groupWid,
          groupType:
            this.searchTab.groupType === "all" ? "" : this.searchTab.groupType,
        },
        (data) => {
          this.loading = false;
          if (data.errcode === "0" && data.data) {
            let newsItem = data.data.newsItem || [];
            let serviceLists = data.data.service || [];
            let guidanceLists = data.data.serviceItem || [];

            let customGroup = data.data.customGroup || {};
            newsItem = newsItem.length > 5 ? newsItem.slice(0, 5) : newsItem;
            serviceLists =
              serviceLists.length > 5 ? serviceLists.slice(0, 5) : serviceLists;
            guidanceLists =
              guidanceLists.length > 5
                ? guidanceLists.slice(0, 5)
                : guidanceLists;
            let oneThing = data.data.oneThing || [];
            oneThing = oneThing.length > 5 ? oneThing.slice(0, 5) : oneThing;
            let dept = data.data.dept || [];
            dept = dept.length > 5 ? dept.slice(0, 5) : dept;
            if (
              newsItem.length ||
              serviceLists.length ||
              guidanceLists.length ||
              customGroup.groupDataSize ||
              oneThing.length ||
              dept.length
            ) {
              this.dropDownList = [
                {
                  label: "onlineService",
                  name: data.data.serviceGroupName ?? "在线服务",
                  value: serviceLists,
                  orderNumber: data.data.serviceOrderNumber,
                },
                {
                  label: "serviceItem",
                  name: data.data.serviceItemGroupName ?? "服务事项",
                  value: guidanceLists,
                  orderNumber: data.data.serviceItemOrderNumber,
                },
                {
                  label: "newsItem",
                  name: data.data.newsGroupName ?? "资讯",
                  value: newsItem,
                  orderNumber: data.data.newsOrderNumber,
                },
                {
                  label: "oneThing",
                  name: data.data.oneThingGroupName ?? "一件事",
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
              if (customGroup.groupDataSize) {
                this.dropDownList = this.dropDownList.concat({
                  label: "customGroup",
                  value: this.handleCustomLists(customGroup),
                  orderNumber: null,
                });
              }
            } else {
              this.dropDownList = [];
            }
          } else {
            this.dropDownList = [];
          }
        }
      );
    },
    handleCustomLists(data) {
      const lists = [];
      data.groupDataList.forEach((element) => {
        const title =
          element.sourceAsMap.paramAttr[data.groupParamList[0]?.paramWid];
        if (title) {
          lists.push({
            url: element.sourceAsMap.url,
            title,
            wid: element.sourceAsMap.wid,
          });
        }
      });
      return lists.length > 5 ? lists.slice(0, 5) : lists;
    },
  },
};
</script>

<style scoped>
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
