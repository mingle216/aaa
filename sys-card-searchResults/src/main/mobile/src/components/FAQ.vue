<template>
  <we-list
    class="faq-list"
    v-model="loading"
    :finished="finished"
    :finished-text="$Lan(lanFunName, 'finishedText', '没有更多了')"
    @load="loadData"
  >
    <template v-for="(item, index) in list">
      <div
        class="faq-item"
        v-if="index < page * pageSize"
        :key="index"
        @click="goHandle(item)"
      >
        <div class="question">
          <div
            class="question-name portal-font-color-lv1"
            v-html="question(item)"
          ></div>
          <div class="qustion-item portal-font-color-lv2">
            {{ $Lan(lanFunName, "responses", "对应事项") }}：<span
              class="item-name portal-font-color-lv1"
              >{{ item.serviceItemName }}</span
            >
          </div>
        </div>
        <div class="answer portal-font-color-lv2">
          {{ $Lan(lanFunName, "answer", "答") }}：<span
            v-html="answer(item)"
          ></span>
        </div>
      </div>
    </template>
  </we-list>
</template>

<script>
export default {
  props: {
    router: Object,
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
  computed: {
    question() {
      return function(item) {
        return window.shell.strHighlighted(item.question, this.keyword);
      };
    },
    answer() {
      return function(item) {
        return window.shell.strHighlighted(item.answer, this.keyword);
      };
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
  methods: {
    loadData() {
      if (this.page * this.pageSize < this.list.length) {
        this.finished = false;
        this.page++;
      } else {
        this.finished = true;
      }
    },
    goHandle(item) {
      if(item.workGuide && 'isAuthorized' in item && item.isAuthorized === 0) {
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
            infoType: 4,
            filterInfo: { linkName: item.serviceItemName }
          }
        },
        startTime: new Date().getTime()
      });
      this.$emit("record-cache");
      window.shell.openServiceItem({
        wid: item.serviceItemWid,
        name: item.serviceItemName,
      });
    },
  },
};
</script>

<style lang="less" scoped>
.faq-list {
  .faq-item {
    border-radius: 4px;
    overflow: hidden;
    margin-bottom: 12px;
    padding: 0 12px;
    background-color: #f6f6f8;
    .question {
      padding: 17px 0;
      border-bottom: 1px dashed #d6dade;
      margin-bottom: 12px;
      .question-name {
        line-height: 20px;
        font-size: 16px;
        font-weight: bold;
        line-height: 20px;
        margin-bottom: 8px;
        word-break: break-all;
      }
      .qustion-item {
        font-size: 14px;
        line-height: 16px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }

    .answer {
      font-size: 14px;
      margin-bottom: 12px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
}
</style>
