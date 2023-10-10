<template>
  <div class="card-recommendServiceItems" v-if="serviceItemsInfo.length">
    <div class="recommend-list">
      <div
        class="recommend__item"
        v-for="(item, index) in serviceItemsInfo"
        :key="item.itemWid"
        @click="openServiceItem(item)"
      >
        <div
          class="recommend__item__before"
          :style="currentUser ? '' : 'width : calc(100% - 17px)'"
        >
          <img
            class="recommend__item__img"
            :src="item.iconLink || errorImg"
            @error="handleError"
          />
          <div class="recommend__item__detail">
            <span
              class="recommend__item__name text__ellipsis portal-font-color-lv1"
            >
              {{ item.itemName }}
            </span>
            <span class="recommend__item__dept text__ellipsis">
              {{ item.itemDept }}
            </span>
          </div>
        </div>
        <div
          class="icon__area"
          @click.stop="isOrNotCollect(item, index)"
          v-if="currentUser"
        >
          <we-icon
            class="recommend__item__favorite"
            :class="[item.favorite ? 'favorite_font_color' : 'unfavorite_font']"
            :name="item.favorite ? 'like' : 'like-o'"
          />
        </div>
      </div>
    </div>
  </div>
  <EmptyCon v-else :tip="$Lan(lanFunName, 'noData', '暂无事项')"></EmptyCon>
</template>

<script>
export default {
  components: {},
  name: "",
  props: {
    index: Number,
    router: Object
  },
  data() {
    const { cardWid, cardId } = this.router;
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      lanFunName: this.router.cardId + "_h5",
      loading: false,
      serviceItemsInfo: [],
      errorImg: window.shell.ErrorImg,
      currentUser: null
      // tip: {
      //   en_US: {
      //     nodata: "No Data",
      //   },
      //   zh_CN: {
      //     nodata: "暂无事项",
      //   },
      // },
    };
  },
  created() {
    if (window.shell) {
      this.getCardData("init");
    }
    window.shell.on("collectServiceItem", this.process);
    this.currentUser = window.shell.getUserInfo();
  },
  beforeDestroy() {
    window.shell.off("collectServiceItem");
  },
  methods: {
    getCardData(isInit) {
      if (isInit && isInit === "init") {
        window.minosStataCollect.loadStart({
          listId: this.listId,
          actionType: 3,
          functionType: 1,
          actionParams: {
            pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
            pageName: window.shell.getPageData().pageInfoEntity.pageName,
            cardWid: this.router.cardWid,
            cardId: this.router.cardId,
            cardName: this.router.cardName,
            extraInfo: ""
          },
          startTime: new Date().getTime()
        });
      }
      this.loading = true;
      window.shell
        .execCardMethod({
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "renderData",
          param: {}
        })
        .then(data => {
          if (isInit && isInit === "init") {
            window.minosStataCollect.loadEnd({
              listId: this.listId,
              endTime: new Date().getTime()
            });
          }
          this.loading = false;
          this.setData(data);
        })
        .catch(() => {
          if (isInit && isInit === "init") {
            window.minosStataCollect.loadEnd({
              listId: this.listId,
              endTime: new Date().getTime()
            });
          }
        });
    },
    setData(data) {
      this.serviceItemsInfo =
        (data.data.serviceItemsInfo && data.data.serviceItemsInfo.slice(0)) ||
        [];
      this.serviceItemsInfo =
        (this.serviceItemsInfo &&
          this.serviceItemsInfo.map(v => {
            return {
              ...v,
              wid: v.itemWid,
              name: v.itemName
            };
          })) ||
        [];
      // this.showTheMoreButton();
    },
    openServiceItem(item) {
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
            itemId: item.itemWid
          }
        },
        startTime: new Date().getTime()
      });
      window.shell.openServiceItem(item);
    },
    // 加载默认图片
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    // 取消收藏
    async isOrNotCollect(item, index) {
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
            fucType: item.favorite ? 3 : 2
          }
        },
        startTime: new Date().getTime()
      });
      await window.shell.collectServiceItem({
        id: item.itemWid,
        operate: item.favorite ? 0 : 1,
        cardId: this.router.cardId
      });
      item.favorite = item.favorite ? false : true;
      this.$set(this.serviceItemsInfo, index, item);
    },
    process(el) {
      if (el.cardId !== this.router.cardId) {
        this.getCardData();
      }
    }
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 5000);
      }
    }
  }
};
</script>

<style lang="less" scoped>
.favorite_font_color {
  color: #fa6444;
}
.card-recommendServiceItems {
  position: relative;
  width: 100%;
  padding: 12px 0 0 17px;
  .recommend-list {
    .recommend__item {
      width: 100%;
      display: flex;

      align-items: center;
      .recommend__item__before {
        padding: 11px 0;
        width: calc(100% - 45px); //减去星星的size
        display: flex;
        align-items: center;
        .recommend__item__delete {
          color: red;
          font-size: 16px;
          margin-right: 12px;
        }
        .recommend__item__img {
          width: 36px;
          height: 36px;
          margin-right: 12px;
        }
        .recommend__item__detail {
          flex-direction: column;
          display: flex;
          width: calc(100% - 48px); // 减去图标的宽度
          .recommend__item__name {
            font-family: PingFangSC-Regular;
            font-size: 16px;
            color: #102645;
            letter-spacing: 0;
            text-align: justify;
            line-height: 20px;
            padding-bottom: 2px;
            margin-right: 12px;
          }
          .recommend__item__dept {
            font-family: PingFangSC-Regular;
            font-size: 12px;
            color: #707d8f;
            letter-spacing: 0;
            text-align: justify;
            line-height: 16px;
            margin-right: 25px;
          }
        }
      }
      .icon__area {
        height: 58px;
        width: 45px;
        display: flex;
        justify-content: center;
        align-items: center;
        padding-right: 17px;
        .recommend__item__favorite {
          font-size: 14px;
        }
      }
      // .icon__area:hover {
      //   opacity: 0.5;
      //   background: #ffd5d5;
      // }
    }
  }
}
</style>
