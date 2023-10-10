<template>
  <div class="servie-list">
    <div class="title portal-font-color-lv3">{{ name }} ({{ list.length }})</div>
    <div class="wrap">
      <template v-for="(item, index) in list">
        <div
          v-if="index < page * pageSize"
          :key="index"
          @click="goService(item)"
          class="service-item"
        >
          <div class="item-left" :class="{ 'no-permission-service': !item.permission }">
            <img :src="iconLink(item)" class="item-icon" />
          </div>
          <div class="item-center">
            <div class="item-name portal-font-color-lv1">
              <div class="name-con">
                <span v-html="nameHighLight(item)"></span>
              </div>
            </div>
          </div>
        </div>
      </template>
    </div>
    <moreData
      :lan-fun-name="lanFunName"
      @click="pageAdd"
      :isOpen="page * pageSize < list.length"
      v-if="pageSize < list.length"
    />
  </div>
</template>

<script>
import MoreData from "./MoreData";
export default {
  components: {
    MoreData,
  },
  props: {
    router: Object,
    analyzeData: Array,
    list: {
      type: Array,
      default: () => {
        return [];
      },
    },
    keyword: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
    name: String,
  },
  computed: {
    nameHighLight() {
      return function (item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.serviceName, data);
      };
    },
    iconLink() {
      return function (item) {
        return item.mobileIconLink
          ? item.mobileIconLink
          : item.iconLink
          ? item.iconLink
          : this.errImg;
      };
    },
  },
  data() {
    return {
      errImg: "",
      page: 1,
      pageSize: 6,
    };
  },
  created() {
    this.errImg = window.shell.ErrorImg;
  },
  methods: {
    pageAdd(isAdd) {
      if (isAdd) {
        this.page++;
      } else {
        this.page = 1;
      }
    },
    goService(item) {
      if (!item.permission) {
        window.minosStataCollect.collect({
          actionType: 0,
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
        window.shell.showMessage({
          message: this.$Lan(this.lanFunName, "showMessage", "暂无使用权限，请联系管理员"),
          type: "warning",
        });
        return;
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
            infoType: 0,
            serviceId: item.serviceWid,
          },
        },
        startTime: new Date().getTime(),
      });
      this.$emit("record-cache");
      window.shell.openService({
        ...item,
        wid: item.serviceWid,
        name: item.serviceName,
      });
    },
  },
};
</script>

<style lang="less" scoped>
.servie-list {
  padding: 12px;
  background: #ffffff;
  border-radius: 8px;
  .title {
    margin-bottom: 12px;
  }
}
.wrap {
  display: flex;
  flex-wrap: wrap;
}
.service-item {
  display: flex;
  flex-direction: column;
  width: 103px;
  padding: 16px 12px;
  box-sizing: border-box;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  margin-bottom: 8px;
  margin-right: 9px;
  &:nth-child(3n) {
    margin-right: 0;
  }
  &:nth-child(5n + 1) {
    background: url("../../assets/images/service1.png") no-repeat left top / 100%;
  }
  &:nth-child(5n + 2) {
    background: url("../../assets/images/service2.png") no-repeat left top / 100%;
  }
  &:nth-child(5n + 3) {
    background: url("../../assets/images/service3.png") no-repeat left top / 100%;
  }
  &:nth-child(5n + 4) {
    background: url("../../assets/images/service4.png") no-repeat left top / 100%;
  }
  &:nth-child(5n + 5) {
    background: url("../../assets/images/service5.png") no-repeat left top / 100%;
  }
  &:nth-last-child(-n+3) {
    margin-bottom: 0;
  }
  .item-left {
    width: 36px;
    height: 36px;
    background: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    overflow: hidden;
    border-radius: 50%;
    .item-icon {
      height: 22px;
      width: 22px;
      border-radius: 50%;
    }
  }
  .item-center {
    margin-top: 8px;
    .item-name {
      font-size: 14px;
      color: #102645;
      height: 36px;
      display: table;
      .name-con {
        font-size: 15px;
        line-height: 20px;
        display: table-cell;
        vertical-align: middle;
        span {
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
          word-break: break-all;
        }
      }
    }
  }
}
</style>
