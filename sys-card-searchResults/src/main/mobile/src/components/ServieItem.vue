<template>
  <we-list
    class="item-list"
    v-model="loading"
    :finished="finished"
    :finished-text="$Lan(lanFunName, 'finishedText', '没有更多了')"
    @load="loadData"
  >
    <template v-for="(item, index) in list">
      <div
        class="item-item"
        :key="index"
        v-if="index < page * pageSize"
        @click="goDetail(item)"
      >
        <div class="item-left">
          <div
            class="item-name ellipsis portal-font-color-lv1"
            v-html="name(item)"
          ></div>
          <div
            class="item-source ellipsis portal-font-color-lv2"
            v-html="itemDept(item)"
          ></div>
        </div>
        <div class="item-right" v-if="item.onlineServiceType">
          <div
            class="
              item-handle ellipsis
              portal-primary-border-color-lv1 portal-primary-color-lv1
            "
            :class="[item.onlineServiceType === 1 ? 'disable-btn' : '']"
            @click.stop="goHandle(item)"
          >
            {{ $Lan(lanFunName, "dealOnline", "在线办理") }}
          </div>
        </div>
      </div>
    </template>
  </we-list>
</template>

<script>
export default {
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
  },
  data() {
    return {
      errImg: "",
      loading: false,
      finished: true,
      page: 1,
      pageSize: 20,
    };
  },
  computed: {
    name() {
      return function(item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.itemName, data);
      };
    },
    itemDept() {
      return function(item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.itemDept, data);
      };
    },
  },
  watch: {
    list: {
      immediate: true,
      handler(val) {
        this.page = 1;
        if (val.length > this.pageSize) {
          this.finished = false;
        } else {
          this.finished = true;
        }
      },
    },
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 200);
      }
    },
  },
  methods: {
    loadData() {
      if (this.page * this.pageSize < this.list.length) {
        this.finished = false;
        this.page++;
      } else {
        this.finished = true;
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
.item-item {
  display: flex;
  padding: 18px 12px;
  border-radius: 4px;
  margin-bottom: 12px;
  background-color: #f6f6f8;
  .item-left {
    flex: 1;
    width: 0;
    .item-name {
      font-size: 16px;
      line-height: 22px;
      margin-bottom: 10px;
      font-weight: bold;
    }
    .item-source {
      font-size: 14px;
    }
  }
  .item-right {
    width: 108px;
    text-align: right;
    // height: 100%;
    position: relative;
    .item-handle {
      height: 36px;
      padding-left: 10px;
      padding-right: 10px;
      max-width: 108px;
      // width: 76px;
      //   display: inline-block;
      border-radius: 4px;
      text-align: center;
      line-height: 36px;
      font-size: 14px;
      border: 1px solid #307afb;
      color: #307afb;
      top: 50%;
      right: 0;
      margin-top: -18px;
      position: absolute;
    }
    .disable-btn {
      opacity: 0.3;
    }
  }
}
</style>
