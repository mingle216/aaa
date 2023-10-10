<template>
  <div class="associtive__dropDown-loading">
    <div class="mb-20" v-if="serviceLists.length">
      <div class="associtive__dropDown-info portal-font-color-lv3">
        {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'onlineService', '在线服务') }}
      </div>
      <div
        class="associtive__dropDown-list portal-primary-backgroundcolor-hover-lv5"
        v-for="(item, index) in serviceLists"
        :key="index"
        @click="openService(item)"
      >
        <div style="display: inline-block" :class="{ 'no-permission-service': !item.permission }">
          <img
            :src="item.iconLink || errorImg"
            class="associtive__dropDown-service-img"
            @error="handleError"
          />
        </div>
        <div
          class="ml-12 associtive__dropDown-service ellipsis"
          :class="[item.permission ? 'portal-font-color-lv1' : 'portal-font-color-lv4']"
          v-html="handleHighLighter(item.serviceName)"
        ></div>
      </div>
    </div>
    <div v-if="guidanceLists.length">
      <div class="associtive__dropDown-info portal-font-color-lv3">
        {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'serviceItem', '服务事项') }}
      </div>
      <div
        class="associtive__dropDown-list portal-font-color-lv1 portal-primary-backgroundcolor-hover-lv5 ellipsis"
        v-for="(item, index) in guidanceLists"
        :key="index"
        v-html="handleHighLighter(item.itemName)"
        @click="openServiceDetail(item)"
      ></div>
    </div>
    <div
      class="associate-empty portal-font-color-lv3"
      v-if="serviceLists.length === 0 && guidanceLists.length === 0"
    >
      {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'noSearchResult', '暂无搜索内容') }}
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
    };
  },
  computed: {
    reg() {
      return new RegExp(`(${this.keyword.trim()})`, "ig");
    },
  },
  watch: {
    keyword: {
      handler() {
        this.handleSearch();
      },
      immediate: true,
    },
  },
  methods: {
    // 打开服务
    openService(item) {
      window.shell.openService({
        ...item,
        wid: item.serviceWid,
        name: item.serviceName,
      });
    },
    // 打开服务事项详情
    openServiceDetail(item) {
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
      return window.shell.strHighlighted(item, this.keyword);
    },
    handleSearch() {
      window.shell.execTemplateMethod(
        "matchSearch",
        {
          keywords: this.keyword,
        },
        (data) => {
          if (data.errcode === "0" && data.data) {
            const services = data.data.service || [];
            const serviceItem = data.data.serviceItem || [];
            this.serviceLists =
              services.length > 5 ? services.slice(0, 5) : services;
            this.guidanceLists =
              serviceItem.length > 5 ? serviceItem.slice(0, 5) : serviceItem;
          } else {
            this.serviceLists = [];
            this.guidanceLists = [];
          }
        }
      );
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
}
.associtive__dropDown-service {
  display: inline-block;
  vertical-align: middle;
  width: calc(100% - 42px);
}
.associate-empty {
  font-size: 16px;
  padding: 15px 12px;
  text-align: center;
}
</style>
