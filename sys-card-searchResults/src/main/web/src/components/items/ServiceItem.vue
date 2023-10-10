<template>
  <div
    v-if="item"
    @click="goService"
    class="service-item portal-primary-backgroundcolor-hover-lv5"
    :style="{ width: `${width}px` }"
  >
    <div
      class="item-left"
      :class="{ 'no-permission-service': !item.permission }"
    >
      <we-image :src="getIconUrl(item)" fit="cover" class="item-icon">
        <div slot="error">
          <img :src="errImg" />
        </div>
      </we-image>
    </div>
    <div class="item-center">
      <we-tooltip
        :content="item.serviceName"
        :open-delay="500"
        placement="bottom-start"
      >
        <div
          class="item-name"
          :class="
            item.permission ? 'portal-font-color-lv1' : 'portal-font-color-lv4'
          "
        >
          <div class="item-cell">
            <span class="item-text" v-html="name"></span>
          </div>
        </div>
      </we-tooltip>
    </div>
    <div
      class="item-right"
      v-if="currentUser"
      :class="{ favorites: item.favorite, 'not-favorites': !item.favorite }"
      @click.stop="favoriteHandle"
    >
      <i class="iconfont icon-favorites"></i>
    </div>
    <div
      v-if="currentUser && serviceAppraise == '1'"
      class="app-tag portal-primary-backgroundcolor-lv1"
      @click.stop="$emit('open-comment', item)"
    >
      <i class="we-icon-edit" />
    </div>
  </div>
</template>

<script>
export default {
  name: "serviceItem",
  props: {
    router: Object,
    analyzeData: Array,
    width: {
      type: Number,
      default: 240,
    },
    item: Object,
    keyword: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
    serviceAppraise: String,
  },
  computed: {
    name() {
      //  this.item.serviceName
      const data = JSON.parse(JSON.stringify(this.analyzeData));
      if (window.shell.isIE()) {
        return window.shell.strHighlighted(
          window.shell.cutStr(this.item.serviceName, 32).newStr,
          data
        );
      } else {
        return window.shell.strHighlighted(this.item.serviceName, data);
      }
    },
  },
  data() {
    return {
      errImg: "",
      currentUser: window.shell.getUserInfo(),
      isPosting: false,
    };
  },
  created() {
    this.errImg = window.shell.ErrorImg;
  },
  methods: {
    getIconUrl(item) {
      const { serviceStation, mobileIconLink, iconLink } = item;
      if (serviceStation === 1) {
        return /^((ht|f)tps?):/.test(mobileIconLink)
          ? mobileIconLink
          : `data:image/png/jpeg;base64,${mobileIconLink}`;
      } else if (serviceStation === 0) {
        return /^((ht|f)tps?):/.test(iconLink)
          ? iconLink
          : `data:image/png/jpeg;base64,${iconLink}`;
      } else {
        return iconLink
          ? /^((ht|f)tps?):/.test(iconLink)
            ? iconLink
            : `data:image/png/jpeg;base64,${iconLink}`
          : /^((ht|f)tps?):/.test(mobileIconLink)
          ? mobileIconLink
          : `data:image/png/jpeg;base64,${mobileIconLink}`;
      }
    },
    goService() {
      if (!this.item.permission) {
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
            serviceId: this.item.serviceWid,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.openService({
        ...this.item,
        wid: this.item.serviceWid,
        name: this.item.serviceName,
      });
    },
    favoriteHandle() {
      // window.shell.emit('favorite',this.item)
      if (this.isPosting) {
        return;
      }
      this.isPosting = true;
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
            serviceId: this.item.serviceWid,
            fucType: this.item.favorite ? 3 : 2,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell
        .collectService({
          id: this.item.serviceWid,
          operate: this.item.favorite ? "0" : "1",
          showFolderPop: true
        })
        .then(() => {
          this.isPosting = false;
        })
        .catch(() => {
          this.isPosting = false;
        });
    },
  },
};
</script>

<style lang="less" scoped>
.service-item {
  display: flex;
  padding: 12px;
  box-sizing: border-box;
  min-width: 160px;
  cursor: pointer;
  position: relative;
  overflow: hidden;

  &:hover {
    .item-right {
      visibility: visible;
    }
    .app-tag {
      display: block;
    }
  }
  .item-left {
    width: 44px;
    height: 44px;
    position: relative;
    overflow: hidden;
    border-radius: 5px;
    // padding: 2px 0;
    /deep/.we-image {
      height: 44px;
    }
    .item-icon {
      height: 44px;
      width: 44px;
      border-radius: 5px;
      // box-shadow: 0 0 10px 5px rgba(0, 0, 0, 0.03);
    }
  }
  .item-center {
    flex: 1;
    padding: 0 10px;
    width: 0;
    height: 48px;
    overflow: hidden;
    .item-name {
      font-size: 16px;
      color: #102645;
      display: table;
      height: 48px;
      line-height: 24px;
      // line-height: 44px;
      // white-space: nowrap;
      // overflow: hidden;
      // text-overflow: ellipsis;
      position: relative;
      .item-cell {
        display: table-cell;
        vertical-align: middle;
        overflow: hidden;
        word-break: break-all;
        max-height: 48px;
        .item-text {
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }
      }
    }
    // .item-fun {
    //     font-size: 12px;
    //     height: 16px;
    //     line-height: 16px;
    //     color: #707d8f;
    //     white-space: nowrap;
    //     overflow: hidden;
    //     text-overflow: ellipsis;
    //     .info-item:not(:last-child):after {
    //         content: '、';
    //     }
    // }
  }
  .item-right {
    width: 20px;
    visibility: hidden;
    color: #ccd0d3;
    line-height: 48px;
    // &.collect {
    //     color: #ffbc00;

    // }
    &.iconfont {
      font-size: 14px;
    }
  }
  .app-tag {
    display: none;
    width: 50px;
    height: 50px;
    position: absolute;
    top: -25px;
    right: -25px;
    transform: rotate(45deg);

    > i {
      position: absolute;
      left: 20px;
      bottom: 4px;
      transform: rotate(-45deg);
      color: #fff;
    }
  }
}
</style>
