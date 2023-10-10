<template>
  <div class="matter-item" v-if="item">
    <div class="mattr-left">
      <we-image
        :src="item.iconLink ? item.iconLink : errImg"
        fit="cover"
        class="item-icon"
      >
        <div slot="error">
          <img :src="errImg" />
        </div>
      </we-image>
    </div>
    <div class="matter-center">
      <div class="item-name portal-font-color-lv1">
        <span v-html="name"></span>
      </div>
      <div class="item-fun">
        <span v-html="itemDept"></span>
      </div>
    </div>
    <div class="matter-right">
      <we-button
        v-if="item.workGuide && item.isAuthorized !== 0"
        class="default-btn"
        :class="[btnClass]"
        plain
        @click="goDetail(item)"
      >
        {{ $Lan(lanFunName, "businessGuide", "办事指南") }}
      </we-button>
      <we-tooltip
        :content="
          $Lan(
            'public',
            !item.workGuide ? 'workGuideHide' : 'workGuideNoAuth',
            !item.workGuide ? '此事项暂未配置办事指南' : '您无权限查看'
          )
        "
        :open-delay="500"
        placement="top"
        v-else
        ><we-button
          class="default-btn button-disabled portal-font-color-lv4"
          plain
        >
          {{ $Lan(lanFunName, "businessGuide", "办事指南") }}
        </we-button></we-tooltip
      >
      <!-- onlineServiceType 0 不显示， 1 禁用， 2 显示 -->
      <we-button
        v-if="item.onlineServiceType === 2"
        :class="[btnClass]"
        @click="goHandle"
        class="default-btn"
        plain
      >
        {{ $Lan(lanFunName, "dealOnline", "在线办理") }}
      </we-button>
      <we-tooltip
        :content="
          $Lan(
            'public',
            item.onlineServiceType === 0 ? 'onlineHide' : 'onlineNoAuth',
            item.onlineServiceType === 0
              ? '此事项暂未开通在线办理功能'
              : '您无权限在线办理此事项'
          )
        "
        :open-delay="500"
        placement="top"
        v-else
      >
        <we-button
          class="default-btn button-disabled portal-font-color-lv4"
          plain
        >
          {{ $Lan(lanFunName, "dealOnline", "在线办理") }}
        </we-button></we-tooltip
      >
    </div>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    item: Object,
    analyzeData: Array,
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
    };
  },
  created() {
    this.errImg = window.shell.ErrorImg;
  },
  computed: {
    name() {
      const data = JSON.parse(JSON.stringify(this.analyzeData));
      return window.shell.strHighlighted(this.item.itemName, data);
    },
    itemDept() {
      const data = JSON.parse(JSON.stringify(this.analyzeData));
      return window.shell.strHighlighted(this.item.itemDept, data);
    },
    btnClass() {
      return "portal-primary-border-color-lv1  portal-primary-color-lv1 portal-primary-backgroundcolor-hover-lv5";
    },
  },
  methods: {
    /**
     * 跳转办事指南
     */
    goDetail(item) {
      if ("isAuthorized" in item && item.isAuthorized === 0) {
        this.$message({ message: "无权限查看", type: "warning" });
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
            itemId: this.item.itemWid,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.openServiceItem({
        ...this.item,
        wid: this.item.itemWid,
        name: this.item.itemName,
      });
    },
    /**
     * 跳转在线服务
     */
    goHandle(e) {
      // if (this.item.onlineServiceType !== 1) {
      let parent = e.target;
      let name = parent.nodeName;
      while (name !== "BUTTON") {
        parent = parent.parentNode;

        if (parent) {
          name = parent.nodeName;
        }
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
            itemId: this.item.itemWid,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.openOnlineDeal(
        {
          ...this.item,
          wid: this.item.itemWid,
          name: this.item.itemName,
        },
        parent
      );
      // }
      // else {
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
.matter-item {
  height: 86px;
  border-bottom: 1px solid#E7EDF1;
  box-sizing: border-box;
  display: flex;
  padding: 20px 0;
  .mattr-left {
    width: 44px;
    .item-icon {
      height: 44px;
      width: 44px;
      // background-position: center;
      // background-repeat: no-repeat;
      // background-size: 80% auto;
      border-radius: 5px;
      // box-shadow: 0 0 10px 5px rgba(0, 0, 0, 0.03);
      // background-color: aqua;
    }
  }
  .matter-center {
    flex: 1;
    padding: 0 20px;
    width: 0;
    .item-name {
      font-size: 16px;
      color: #102645;
      margin-bottom: 4px;
      height: 24px;
      line-height: 24px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    .item-fun {
      font-size: 14px;
      height: 18px;
      line-height: 18px;
      color: #8c8c8c;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
  .matter-right {
    min-width: 220px;
    height: 45px;
    line-height: 45px;
    text-align: right;
    flex-wrap: nowrap;
    /deep/.we-button {
      padding: 10px 15px;
    }
    .default-btn {
      border: 1px solid;
    }
    .button-disabled,
    .button-disabled:hover {
      border-color: #d9d9d9;
    }
  }
}
</style>
