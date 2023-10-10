<template>
  <div class="item-list">
    <div class="title portal-font-color-lv3">{{ name }} ({{ list.length }})</div>
    <div>
      <template v-for="(item, index) in list">
        <div class="item-item" :key="index" v-if="index < page * pageSize" @click="goDetail(item)">
          <img :src="iconLink(item)" class="item-icon" />
          <div class="wrap">
            <div class="item-left">
              <div
                class="item-name ellipsis portal-font-color-lv1"
                v-html="nameHighLight(item)"
              ></div>
              <div class="item-dept portal-font-color-lv3">
                <span>{{ $Lan(lanFunName, "dept", "责任部门") }}：</span>
                <div class="item-source ellipsis" v-html="itemDept(item)"></div>
              </div>
            </div>
            <div class="item-right" v-if="item.onlineServiceType">
              <div
                class="item-handle ellipsis portal-primary-border-color-lv1 portal-primary-color-lv1"
                :class="[item.onlineServiceType === 1 ? 'disable-btn' : '']"
                @click.stop="goHandle(item)"
              >
                {{ $Lan(lanFunName, "dealOnline", "在线办理") }}
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
  data() {
    return {
      errImg: "",
      page: 1,
      pageSize: 6,
    };
  },
  computed: {
    nameHighLight() {
      return function (item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.itemName, data);
      };
    },
    itemDept() {
      return function (item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.itemDept || '-', data);
      };
    },
    iconLink() {
      return function (item) {
        return item.iconLink || window.shell.ErrorImg;
      };
    },
  },
  methods: {
    pageAdd(isAdd) {
      if (isAdd) {
        this.page++;
      } else {
        this.page = 1;
      }
    },
    /**
     * 跳转办事指南
     */
    goDetail(item) {
      if (!item.workGuide) {
        this.goHandle(item);
        return;
      }
      if ("isAuthorized" in item && item.isAuthorized === 0) {
        window.shell.showMessage({
          type: "warning",
          message: `${this.$Lan("public", "noPermission", "无权限查看")}`,
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
            fucType: 0,
          },
        },
        startTime: new Date().getTime(),
      });
      this.$emit("record-cache");
      window.shell.openServiceItem({
        ...item,
        wid: item.itemWid,
        name: item.itemName,
      });
    },
    goHandle(item) {
      // if (item.onlineServiceType !== 1) {
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
            fucType: 1,
          },
        },
        startTime: new Date().getTime(),
      });
      this.$emit("record-cache");
      window.shell.openOnlineDeal({
        ...item,
        wid: item.itemWid,
        name: item.itemName,
      });
      // } else {
      //   window.shell.showMessage({
      //     message: "暂无使用权限，请联系管理员",
      //     type: "warning",
      //   });
      // }
    },
  },
};
</script>

<style lang="less" scoped>
.item-list {
  padding: 12px;
  background: #ffffff;
  border-radius: 8px;
  margin-bottom: 12px;
}
.item-item {
  display: flex;
  align-items: center;
  .item-icon {
    height: 36px;
    width: 36px;
    border-radius: 4px;
    margin-right: 12px;
  }
  .wrap {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 0;
    width: 100%;
    box-shadow: inset 0px -0.5px 0px #f0f0f0;
    .item-left {
      flex: 1;
      width: 0;
      .item-name {
        font-size: 16px;
        line-height: 22px;
        margin-bottom: 4px;
        font-weight: bold;
      }
      .item-source {
        font-size: 14px;
        margin-left: 4px;
      }
      .item-dept {
        font-size: 14px;
        line-height: 20px;
        display: flex;
        align-items: center;
      }
    }
    .item-right {
      // width: 108px;
      text-align: right;
      // height: 100%;
      position: relative;
      .item-handle {
        height: 30px;
        padding: 5px 12px;
        max-width: 88px;
        border-radius: 100px;
        text-align: center;
        font-size: 14px;
        line-height: 20px;
        border: 1px solid #307afb;
        color: #307afb;
      }
      .disable-btn {
        opacity: 0.3;
      }
    }
  }
  &:last-child .wrap {
    box-shadow: none;
  }
}
</style>
