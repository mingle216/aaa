<template>
  <!--常见问题-->
  <div>
    <div class="title1 portal-font-color-lv1">{{ fieldName }}</div>

    <div
      v-for="(it, i) in showFlag
        ? item.fieldValue.slice(0, 5)
        : item.fieldValue"
      :key="it.question + i"
      class="ques-item"
    >
      <div class="q-title">
        {{ $Lan(lanFunName, "question", "问") }}： {{ it.question }}
      </div>
      <div class="q-desc portal-font-color-lv2">
        {{ $Lan(lanFunName, "answer", "答") }}： {{ it.answer }}
      </div>
    </div>

    <!--常见问题大于5条的时候多余的隐藏并显示展示更多-->
    <div
      v-if="showFlagSee"
      class="showmore portal-primary-color-lv1"
      @click="showmore"
    >
      <span class="downIcon" v-if="showFlag">
        <i class="icon iconfont icon-menu-BackTop"></i>
      </span>
      <span class="upIcon" v-if="!showFlag">
        <i class="icon iconfont icon-menu-BackTop"></i>
      </span>
      {{
        showFlag
          ? $Lan(lanFunName, "expand", "展开")
          : $Lan(lanFunName, "collapse", "收起")
      }}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    item: {
      type: Object,
      default: () => {
        return {};
      },
    },
    lanFunName: {
      type: String,
      default: "",
    },
    fieldName:String
  },
  watch: {
    item(item) {
      if (item) {
        this.showFlagSee = item.fieldValue.length > 5;
        this.showFlag = item.fieldValue.length > 5;
      }
    },
  },
  data() {
    return {
      showFlag: false,
      showFlagSee: false,
    };
  },
  methods: {
    showmore() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName
        },
        startTime: new Date().getTime()
      });
      this.showFlag = !this.showFlag;
    },
  },
};
</script>

<style lang="less" scoped>
.ques-item {
  padding-top: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
  word-wrap: break-word;
  &:last-child {
    border: none;
  }

  .q-title {
    line-height: 24px;
    font-size: 16px;
    margin-bottom: 8px;
  }
  .q-desc {
    font-size: 14px;
    line-height: 22px;
  }
}
.showmore {
  cursor: pointer;
  width: 100%;
  background: #f9fafb;
  height: 40px;
  text-align: center;
  line-height: 40px;
  margin-top: 15px;
}
.downIcon {
  margin-right: 3px;
  display: inline-block;
  transform: rotate(180deg);
}
.upIcon {
  margin-right: 3px;
  display: inline-block;
  /* transform: rotate(180deg); */
}
</style>
