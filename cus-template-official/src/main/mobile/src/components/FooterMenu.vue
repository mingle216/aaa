<template>
  <we-tabbar v-model="indexMenu" @change="tabChange" class="footer-menu">
    <we-tabbar-item
      v-for="(item, index) in menus"
      :key="index"
      :name="item.wid"
    >
      <span  :class="[indexMenu==item.wid?'portal-primary-color-lv1':'']">
        {{ $t(item.menuNameLangKey) }}
      </span>
      <template #icon="props">
        <template v-if="item.iconType">
          <i class="iconfont" :class="[item.iconUrl,indexMenu==item.wid?'portal-primary-color-lv1':'']"></i>
        </template>
        <template v-else>
          <img
            class="img-icon"
            :src="props.active ? menuIcon(item, true) : menuIcon(item, false)"
          />
        </template>
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
    active:{
      immediate: true,
      handler(val) {
         this.indexMenu = val;
      }
    } 
  },
  computed: {
    menuIcon() {
      return function (item, bo) {
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
  },
  data() {
    return {
      indexMenu: "",
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
      // openType: 0当前页、1新开页签
      // menuType: 0无连接、1内部页面、2第三方链接
      let url = item.linkUrl;
      if (item.menuType === 1) {
        url = item.linkUrl.replace("/portal", "");
      }
      window.shell.openPage(url, item.openType, item.menuType);
    },
  },
};
</script>

<style lang="less" scoped>
.iconfont {
  font-size: 20px;
}
.img-icon {
  object-fit: contain;
  width: 22px;
  height: 22px;
}
.footer-menu {
  touch-action: none;
  /deep/.we-tabbar-item {
    width: 0;
    .we-tabbar-item__text {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      width: 80%;
      text-align: center;
      
    }
  }
}
</style>