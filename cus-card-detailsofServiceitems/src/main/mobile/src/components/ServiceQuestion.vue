<template>
  <div class="service-question-content">
    <div
      v-for="(question, key) in data.slice(0, 2)"
      :key="key"
      class="service-question-content-item"
    >
      <div class="service-question-content-item-title portal-font-color-lv1">
        {{ question.question }}
      </div>
      <div class="service-question-content-item-answer">
        <div>
          {{ $Lan(lanFunName, "answer", "答") }}：{{ question.answer.trim() }}
        </div>
      </div>
    </div>
    <template v-if="showMoreQuestion">
      <div
        v-for="question in data.slice(2)"
        :key="question.id"
        class="service-question-content-item"
      >
        <div class="service-question-content-item-title portal-font-color-lv1">
          {{ question.question }}
        </div>
        <div class="service-question-content-item-answer">
          <div>
            {{ $Lan(lanFunName, "answer", "答") }}：{{ question.answer.trim() }}
          </div>
        </div>
      </div>
    </template>
    <button
      v-if="!showMoreQuestion && data.length > 2"
      class="service-question-content-more portal-primary-color-lv1"
      @click="showHide(true)"
    >
      {{ $Lan(lanFunName, "downmore", "展开更多")
      }}<we-icon name="arrow-down" class="icon-arrow" />
      <!-- <img class="more-icon more-icon-down" :src="moreicon" alt /> -->
    </button>
    <button
      v-if="showMoreQuestion && data.length > 2"
      class="service-question-content-more portal-primary-color-lv1"
      @click="showHide(false)"
    >
      {{ $Lan(lanFunName, "upmore", "收起")
      }}<we-icon name="arrow-up" class="icon-arrow" />
    </button>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    lanFunName: String,
    data: {
      type: Array,
    },
  },
  data() {
    return {
      showMoreQuestion: false,
      moreicon: require(".././assets/images/icon-more.png"),
    };
  },
  created() {
    // this.showMoreQuestion = this.data.length > 2 ? true : false;
  },
  methods: {
    showHide(flag) {
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
      this.showMoreQuestion = flag;
    },
  },
};
</script>

<style lang="less" scoped>
.service-question-content {
  &-item {
    margin-top: 12px;
    background-color: #f6f6f8;
    border-radius: 4px;
    padding: 16px 12px;

    &-title {
      display: flex;
      align-items: center;
      //height: 56px;
      font-size: 16px;
      // color: #102645;
      font-weight: bold;
      padding-bottom: 16px;
      border-bottom: 1px dashed #d7dde7;
      // color: #707D8F;
      color: #102645;
    }

    &-answer {
      margin-top: 12px;
      position: relative;
      // padding-left: 28px;
      word-break: break-all;
      // white-space: pre-wrap;
      font-size: 14px;
      text-align: left;
      color: #707d8f;
      // .answer-text {
      //   position: absolute;
      //   top: 0;
      //   left: 0;
      // }
    }
  }

  &-more {
    margin-top: 12px;
    width: 100%;
    height: 48px;
    font-size: 16px;
    font-weight: 600;
    background-color: #f9fafb;
    border: none;
    border-radius: 4px;

    .more-icon {
      width: 14px;
      height: 14px;
    }
    .more-icon-up {
      transform: rotate(180deg);
    }
    .icon-arrow {
      position: absolute;
      margin-left: 4px;
    }
  }
}
</style>
