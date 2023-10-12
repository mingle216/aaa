<template>
  <we-tabbar v-model="indexMenu" @change="tabChange" class="footer-menu">
    <we-tabbar-item
      v-for="(item, index) in menuList"
      :key="index"
      :name="item.wid"
    >
      <span :class="[indexMenu == item.wid ? 'portal-primary-color-lv1' : '']">
        {{ $t(item.menuNameLangKey) }}
      </span>
      <template #icon="props">
        <template v-if="item.iconType">
          <i
            class="iconfont"
            :class="[
              item.iconUrl,
              indexMenu == item.wid ? '' : '',
            ]"
          ></i>
        </template>
        <template v-else>
          <img
            class="img-icon"
            :src="props.active ? menuIcon(item, true) : menuIcon(item, false)"
          />
        </template>
        <we-badge
          v-show="item.count"
          class="badge"
          :content="item.count"
          max="99"
        />
      </template>
    </we-tabbar-item>
  </we-tabbar>
</template>

<script>
export default {
  props: {
    active: {
      type: String,
      default: "",
    },
    menus: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
  watch: {
    active: {
      immediate: true,
      handler(val) {
        this.indexMenu = val;
        this.menuList = JSON.parse(JSON.stringify(this.menus));
        if (this.menus && this.menus.length > 0) {
          this.currentUser && this.getSidebarCount();
        }
      },
    },
  },
  computed: {
    menuIcon() {
      return function(item, bo) {
        let arr = item.iconUrl.split("|");
        if (!arr[0] || !arr[1]) {
          arr = [
            arr[0] || window.shell.ErrorImg,
            arr[1] || window.shell.ErrorImg,
          ];
        }
        return bo ? arr[0] : arr[1];
      };
    },
    currentUser() {
      return window.shell.getUserInfo() || null;
    },
  },
  data() {
    return {
      indexMenu: "",
      menuList: []
    };
  },
  methods: {
    tabChange(wid) {
      let index = this.menus.findIndex((item) => {
        return item.wid === wid;
      });
      if (index !== -1) {
        this.handleClickMenu(this.menus[index]);
      }
    },
    handleClickMenu(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 5,
            filterInfo: {
              menuWid: item.wid,
              menuName: item.menuName,
            },
          },
        },
        startTime: new Date().getTime(),
      });
      // openType: 0当前页、1新开页签
      // menuType: 0无连接、1内部页面、2第三方链接
      let url = item.linkUrl;
      if (item.menuType === 1) {
        url = item.linkUrl.replace("/portal", "");
      }
      window.shell.openPage(url, item.openType, item.menuType);
    },
    getSidebarCount() {
      this.menus.forEach((item, index) => {
        if (!item.countAddress) return;
        window.shell.getSidebarCount({ type: 2, wid: item.wid }, (res) => {
          this.$set(
            this.menuList[index],
            "count",
            (res && res.data !== "0" && res.data) || 0
          );
        });
      });
    },
  },
  mounted() {
    if (this.menus && this.menus.length > 0) {
      this.currentUser && this.getSidebarCount();
    }
  },
};
</script>

<style lang="less" scoped>
.iconfont {
  font-size: 22px;
}
.img-icon {
  object-fit: contain;
  width: 22px;
  height: 22px;
}
.footer-menu {
  touch-action: none;
  box-shadow: 1px -1px 4px #f0f0f0;
  /deep/.we-tabbar-item {
    width: 0;
    position: relative;
    .we-tabbar-item__text {
      position: absolute;
      font-size: 12px;
      line-height: 18px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      width: 60px;
      text-align: center;
      bottom: 7px;
    }
    .we-tabbar-item__icon {
      position: absolute;
      top: 7px;
      margin-bottom: 0;
    }

    .badge {
      position: absolute;
      top: -6px;
      right: -16px;
      display: inline-block;
      box-sizing: border-box;
      min-width: 16px;
      padding: 0 3px;
      color: #fff;
      font-weight: 500;
      font-size: 12px;
      line-height: 1.2;
      text-align: center;
      background-color: #ee0a24;
      border: 1px solid #fff;
      border-radius: 999px;
    }
  }
  /deep/.we-tabbar-item--active {
    position: relative;
    &::after {
      position: absolute;
      width: 56px;
      height: 28px;
      background: #fff;
      content: "";
      top: -28px;
      border-radius: 28px 28px 0 0;
      box-shadow: 1px -1px 4px #f0f0f0;
    }
    .we-tabbar-item__icon {
      top: -20.5px;
      z-index: 1;
      width: 42px;
      min-height: 42px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      .iconfont {
        color: #fff;
      }
    }
    .badge {
      top: 2px;
      right: -4px;
    }
  }
}
[class*="we-hairline"]::after {
  border-width: 0 0 !important;
}
</style>
