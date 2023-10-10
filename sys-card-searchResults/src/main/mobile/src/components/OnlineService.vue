<template>
  <we-list
    class="servie-list"
    v-model="loading"
    :finished="finished"
    :finished-text="$Lan(lanFunName, 'finishedText', '没有更多了')"
    @load="loadData"
  >
    <template v-for="(item, index) in list">
      <div
        v-if="index < page * pageSize"
        :key="index"
        @click="goService(item)"
        class="service-item"
      >
        <div
          class="item-left"
          :class="{ 'no-permission-service': !item.permission }"
        >
          <we-image :src="iconLink(item)" fit="cover" class="item-icon">
            <div slot="error">
              <img :src="errImg" />
            </div>
          </we-image>
        </div>
        <div class="item-center">
          <div class="item-name portal-font-color-lv1">
            <div class="name-con">
              <span v-html="name(item)"></span>
            </div>
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
  computed: {
    name() {
      return function(item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData))
        return window.shell.strHighlighted(item.serviceName, data);
      };
    },
    iconLink() {
      return function(item) {
        return item.mobileIconLink
          ? item.mobileIconLink
          : item.iconLink
          ? item.iconLink
          : this.errImg;
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
  data() {
    return {
      errImg: "",
      loading: false,
      finished: true,
      page: 1,
      pageSize: 20,
    };
  },
  created() {
    this.errImg = window.shell.ErrorImg;
  },
  methods: {
    loadData() {
      this.loading = true;

      if (this.page * this.pageSize < this.list.length) {
        this.finished = false;
        this.page++;
      } else {
        this.finished = true;
      }
      console.log("page", this.page);
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
            extraInfo: ''
          },
          startTime: new Date().getTime()
        });
        window.shell.showMessage({
          message: this.$Lan(
            this.lanFunName,
            "showMessage",
            "暂无使用权限，请联系管理员"
          ),
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
            serviceId: item.serviceWid
          }
        },
        startTime: new Date().getTime()
      });
      this.$emit('record-cache')
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
  display: flex;
  flex-wrap: wrap;

  /deep/.we-list__finished-text,
  .we-list__placeholder {
    width: 100%;
  }
}
.service-item {
  display: flex;
  padding: 10px;
  box-sizing: border-box;
  width: 164px;
  height: 56px;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  margin-bottom: 12px;
  background-color: #f6f6f8;
  &:nth-child(odd) {
    /*奇数*/
    margin-right: 12px;
  }
  .item-left {
    width: 36px;
    height: 36px;
    position: relative;
    overflow: hidden;
    border-radius: 4px;
    .item-icon {
      height: 36px;
      width: 36px;
      border-radius: 4px;
      // box-shadow: 0 0 10px 5px rgba(0, 0, 0, 0.03);
    }
  }
  .item-center {
    flex: 1;
    padding: 0 10px;
    width: 0;
    .item-name {
      font-size: 14px;
      color: #102645;
      height: 36px;
      display: table;
      .name-con {
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
