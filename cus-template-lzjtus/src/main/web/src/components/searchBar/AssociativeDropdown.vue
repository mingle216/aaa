<template>
  <div class="associtive__dropDown-loading">
    <div class="mb-20" v-for="el in dropDownList" :key="el.label">
      <div class="mb-20" v-if="el.value.length">
        <div class="associtive__dropDown-info portal-font-color-lv3">
          {{ $Lan($TEMPLATE_NAME, el.label, el.name) }}
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
          <template v-else>
            <div
              class="associtive__dropDown-service ellipsis portal-font-color-lv1"
              v-html="handleHighLighter(item.itemName)"
            ></div>
          </template>
        </div>
      </div>
    </div>
    <div
      class="associate-empty portal-font-color-lv3"
      v-if="dropDownList.length === 0"
    >
      {{ $Lan($TEMPLATE_NAME, "emptytips_data", "暂无搜索内容") }}
    </div>
  </div>
</template>

<script>
export default {
  props: ["keyword"],
  data() {
    return {
      serviceLists: [],
      guidanceLists: [],
      errorImg: window.shell.ErrorImg,
      dropDownList: [],
      analyzeData: [],
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
      } else {
        this.openServiceDetail(item);
      }
    },
    // 打开新闻
    openNews(item) {
      this.handleClickTrack({
        infoType: 11,
        newId: item.wid,
      }); // 点击埋点
      window.open(item.url);
    },
    // 打开服务
    openService(item) {
      this.handleClickTrack({
        infoType: 11,
        serviceId: item.serviceWid
      });// 点击埋点
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
      this.handleClickTrack({
        infoType: 11,
        itemId: item.itemWid,
      }); // 点击埋点
      window.shell.openServiceItem({
        ...item,
        wid: item.itemWid,
        name: item.itemName,
      });
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
          lang: localStorage.getItem("locale") || "zh_CN",
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
          if (data.errcode === "0" && data.data) {
            const newsItem = data.data.newsItem || [];
            const services = data.data.service || [];
            const serviceItem = data.data.serviceItem || [];

            console.log(this.dropDownList);
            this.newsItem =
              newsItem.length > 5 ? newsItem.slice(0, 5) : newsItem;
            this.serviceLists =
              services.length > 5 ? services.slice(0, 5) : services;
            this.guidanceLists =
              serviceItem.length > 5 ? serviceItem.slice(0, 5) : serviceItem;
          } else {
            this.newsItem = [];
            this.serviceLists = [];
            this.guidanceLists = [];
          }
          if (
            this.newsItem.length ||
            this.serviceLists.length ||
            this.guidanceLists.length
          ) {
            this.dropDownList = [
              {
                label: "onlineService",
                name: "在线服务",
                value: this.serviceLists,
                orderNumber: data.data.serviceOrderNumber,
              },
              {
                label: "serviceItem",
                name: "服务事项",
                value: this.guidanceLists,
                orderNumber: data.data.serviceItemOrderNumber,
              },
              {
                label: "newsItem",
                name: "资讯",
                value: this.newsItem,
                orderNumber: data.data.newsOrderNumber,
              },
            ].sort((a, b) => a.orderNumber - b.orderNumber);
          } else {
            this.dropDownList = [];
          }
        }
      );
    },
    // 点击埋点
    handleClickTrack(extraInfo) {
      const pageInfoEntity = window.shell.getPageData().pageInfoEntity;
      const actionParams = {
        pageCode: pageInfoEntity.pageCode,
        pageName: pageInfoEntity.pageName,
      };
      if (extraInfo) {
        actionParams.extraInfo = extraInfo;
      }
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        startTime: new Date().getTime(),
        actionParams,
      });
    },
  },
};
</script>

<style scoped>
.ml-12 {
  margin-left: 12px;
}
.associtive__dropDown-info {
  font-size: 12px;
  line-height: 20px;
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
  padding: 7px 12px;
  font-size: 14px;
  line-height: 22px;
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
