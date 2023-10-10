<template>
  <div class="matter-item" v-if="item">
    <div class="mattr-left">
      <we-image :src="item.iconLink" fit="cover" class="item-icon">
        <div slot="error">
          <img :src="errImg" />
        </div>
      </we-image>
    </div>
    <div class="matter-center">
      <div class="item-name portal-font-color-lv1">
        <div
          class="ellipsis"
          style="font-size:16px;line-height:24px;"
          v-html="name"
        ></div>
        <div
          class="item-tag ellipsis portal-primary-color-lv1 portal-primary-backgroundcolor-lv5"
          v-if="oneThingClassify"
        >
          {{ oneThingClassify }}
        </div>
        <div class="item-tag ellipsis role" v-if="roleList">{{ roleList }}</div>
      </div>
      <div class="item-fun">
        <span v-html="oneThingDesc"></span>
      </div>
    </div>
    <div class="matter-right">
      <we-button
        class="default-btn"
        :class="[btnClass]"
        plain
        @click="goDetail(item)"
      >
        {{ $Lan(lanFunName, "businessGuide", "办事指南") }}
      </we-button>
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
      return window.shell.strHighlighted(this.item.oneThingName, data);
    },
    oneThingDesc() {
      const data = JSON.parse(JSON.stringify(this.analyzeData));
      return window.shell.strHighlighted(this.item.oneThingDesc, data);
    },
    btnClass() {
      return "portal-primary-border-color-lv1 portal-primary-color-lv1 portal-primary-backgroundcolor-hover-lv5";
    },
    oneThingClassify() {
      const oneThingClassify = this.item.oneThingClassify;
      return oneThingClassify ? oneThingClassify.join("/") : "";
    },
    roleList() {
      const roleList = this.item.roleList;
      return roleList ? roleList.join("/") : "";
    },
  },
  methods: {
    /**
     * 跳转办事指南
     */
    goDetail(item) {
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
            itemId: this.item.oneThingWid,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.visitService(item.oneThingWid, "OneThing"); // 一件事详情
      window.shell.openPage(`/thingdetail?wid=${item.oneThingWid}`, 1, 1);
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
      border-radius: 5px;
    }
  }
  .matter-center {
    flex: 1;
    padding: 0 20px;
    width: 0;
    .item-name {
      overflow: hidden;
      margin-bottom: 4px;
      display: flex;
      align-items: center;
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
  }

  .item-tag {
    font-size: 12px;
    line-height: 20px;
    border: 0.5px solid;
    border-radius: 2px;
    padding: 0 4px;
    margin-left: 6px;
    max-width: 140px;
    min-width: 34px;
    text-align: center;
    &.role {
      color: #ff9000;
      background-color: rgba(255, 144, 0, 0.1);
    }
  }
}
</style>
