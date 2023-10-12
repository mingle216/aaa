<template>
  <we-header  class="home__menu" :class="type" style="height:auto;">
    <el-menu
      :default-active="activeIndex" 
      :unique-opened="true"
      ref="MenuTree"
    >
      <NavMenu
        :navMenus="navMenu"
        :activeIndex="activeIndex"
        :dom="mentDom"
        :type="type"
      />
    </el-menu>
  </we-header>
</template>
<script>
import NavMenu from "./NavMenu";
export default {
  components: {
    NavMenu,
  },
  props: {
    menus: {
      type: Array,
      default: () => {
        return [];
      },
    },
    activeMenuId: {
      type: String,
      default: "",
    },
    type:{
      type:String
    }
  },
  data() {
    return {
      mentDom: null,
      navMenu: [],
      moreList: [],
    };
  },
  computed: {
    getMenu() {
      return this.menus.map((el) => this.mapTree(el, 0));
    },
    activeIndex() {
      return this.activeMenuId;
    },
    
  },
  watch: {
  
  },
  methods: {
    // 递归menus，添加level标识
    mapTree(obj, index) {
      const haveChildren = Array.isArray(obj.menu) && obj.menu.length > 0;
      return {
        ...obj,
        level: index++,
        menu: haveChildren ? obj.menu.map((d) => this.mapTree(d, index++)) : [],
      };
    },
 
    handleMenu() {
      this.navMenu = this.getMenu;
     // const wid = this.$refs.HeaderMenu.$el.offsetWidth || 0;
      // 单个菜单最大宽度为140px
      // const num = Math.floor(wid / 140);
      // if (this.getMenu.length > num) {
      //   this.navMenu = this.getMenu.slice(0, num - 1);
      //  this.moreList = this.getMenu.slice(num - 1, this.getMenu.length);
      // } else {
      //   this.navMenu = this.getMenu;
      //   this.moreList = [];
      // }
    },
  },
  mounted() {
    this.mentDom = this.$refs.MenuTree;
    this.handleMenu();
    
  },
};
</script>
<style lang="less" scoped>
.home__menu {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
  min-width: 0;
  // padding: 0 24px 0 0;
  color: rgba(255, 255, 255, 0.6);
  // height: 100%;
  margin-top:16px;
}
.home__menu.expand {
 
  margin-top:16px;
}
 

.more__title {
  color: rgba(255, 255, 255, 0.6);
}
.nav__box {
  max-height: 360px;
  min-height: 40px;
  overflow: hidden;
  border-radius: 4px;
}
.arrow {
  text-align: center;
}
.active__arrow {
  cursor: pointer;
}

/deep/.el-menu {
  border-right:0px solid #ffffff;
  position: inherit;
}
 
 
</style>
 
