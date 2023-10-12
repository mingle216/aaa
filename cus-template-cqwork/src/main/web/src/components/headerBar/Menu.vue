<template>
  <we-header ref="HeaderMenu" class="home__menu">
    <el-menu
      mode="horizontal"
      :default-active="activeIndex"
      style="display: flex"
      :unique-opened="true"
      ref="MenuTree"
    >
      <NavMenu
        style="display: flex;min-width:160px"
        :navMenus="navMenu"
        :activeIndex="activeIndex"
        :dom="mentDom"
      />
      <el-submenu
        v-if="moreList.length"
        index="more"
        :popper-append-to-body="true"
        class="more__menu"
        popper-class="customPopper"
      >
        <template slot="title">
          <div class="more__menu_item_box">
            <i
              class="iconfont icon-menu-Universal"
              style="margin-right: 6px;font-size: 18px"
            ></i>
            <span class="more__title">
              {{ $Lan("CUS_TEMPLATE_CQWORK", "more", "更多") }}
            </span>
          </div>
        </template>
        <div @mouseleave="handleLeave">
          <p
            :class="['arrow', offsetY ? 'active__arrow' : 'disable__arrow']"
            @click="handleUp(moreList.length)"
          >
            <i class="el-icon-caret-top" v-if="moreList.length >= 10" />
          </p>
          <!-- 递归 -->
          <div class="nav__box">
            <NavMenu
              menuIndex="more"
              :navMenus="moreList"
              :style="{ transform: `translateY(${offsetY}px)` }"
            />
          </div>
          <p
            :class="['arrow', downArrow ? 'active__arrow' : 'disable__arrow']"
            @click="handleDown(moreList.length)"
          >
            <i class="el-icon-caret-bottom" v-if="moreList.length >= 10" />
          </p>
        </div>
      </el-submenu>
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
  },
  data() {
    return {
      offsetY: 0,
      maxHeight: 360,
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
    downArrow() {
      return (
        this.moreList.length * 40 - Math.abs(this.offsetY) > this.maxHeight
      );
    },
  },
  watch: {
    getMenu() {
      this.handleMenu();
    },
  },
  methods: {
    // 递归menus，添加level标识
    mapTree(obj, index) {
      const haveChildren = Array.isArray(obj.menu) && obj.menu.length > 0;
      return {
        ...obj,
        level: index++,
        offsetY: 0,
        menu: haveChildren ? obj.menu.map((d) => this.mapTree(d, index++)) : [],
      };
    },
    hideMenu() {
      const menuTree = this.$refs.MenuTree;
      if (menuTree && menuTree.openedMenus && menuTree.openedMenus.length) {
        menuTree.openedMenus = [];
      }
    },
    handleUp(num) {
      if (this.offsetY) {
        const height = num * 40; // 菜单高度
        if (height - Math.abs(this.offsetY) >= this.maxHeight) {
          this.offsetY += 40;
        }
      }
    },
    handleDown(num) {
      const height = num * 40; // 菜单高度
      if (height - Math.abs(this.offsetY) > this.maxHeight) {
        this.offsetY -= 40;
      }
    },
    handleLeave() {
      const menuTree = this.$refs.MenuTree;
      setTimeout(() => {
        if (
          menuTree &&
          menuTree.openedMenus &&
          menuTree.openedMenus.length === 0
        ) {
          this.offsetY = 0;
        }
      }, 500);
    },
    handleMenu() {
      const wid = this.$refs.HeaderMenu.$el.offsetWidth || 0;
      // 单个菜单最大宽度为140px
      const num = Math.floor(wid / 140);
      if (this.getMenu.length > num) {
        this.navMenu = this.getMenu.slice(0, num - 1);
        this.moreList = this.getMenu.slice(num - 1, this.getMenu.length);
      } else {
        this.navMenu = this.getMenu;
        this.moreList = [];
      }
    },
  },
  mounted() {
    this.mentDom = this.$refs.MenuTree;
    this.handleMenu();
    window.addEventListener("click", this.hideMenu);
    window.shell.on("onScoll", this.hideMenu);
    window.addEventListener("resize", this.handleMenu);
    this.$once("hook:beforeDestroy", () => {
      window.removeEventListener("click", this.hideMenu, true);
      window.removeEventListener("resize", this.handleMenu);
      window.shell.off("onScoll", this.hideMenu);
    });
  },
};
</script>
<style lang="less" scoped>
.home__menu {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
  padding: 0 24px 0 0;
  color: rgba(255, 255, 255, 0.6);
  height: 100% !important;
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
.disable__arrow {
  color: #ccc;
  cursor: not-allowed;
}
.more__menu /deep/.el-submenu__title {
  border-bottom: none !important;
}
.more__menu_item_box {
  height: 44px;
  line-height: 44px;
  font-size: 18px;
}
/deep/.el-menu--horizontal {
  background: transparent !important;
  border: none;

  .nav__menus:focus {
    outline: none;
  }

  .el-submenu__title {
    padding: 0 12px !important;
    display: flex;
    align-items: center;
    justify-content: center;
    background: transparent !important;
    color: rgba(255, 255, 255, 0.6);
    i {
      color: rgba(255, 255, 255, 0.6);
    }
    .el-submenu__icon-arrow {
      margin-top: 4px;
    }
    .menu_item_box {
      padding: 0 6px 0 0;
      font-size: 18px;
    }
  }

  .el-menu-item {
    height: 100%;
    margin: 0 12px 0 0;
    padding: 0;
    display: flex;
    align-items: center;
    color: rgba(255, 255, 255, 0.6);
    background: transparent !important;
    &:not(.is-disabled):hover,
    &:not(.is-disabled):focus {
      color: inherit !important;
    }
    i {
      color: rgba(255, 255, 255, 0.6);
    }
  }

  .el-menu-item.is-active {
    .menu_item_box {
      font-weight: bold;
      opacity: 1;
      color: #fff !important;
      border-radius: 32px;
      background: rgba(255, 255, 255, 0.1);
    }
    &:hover,
    &:focus {
      color: #fff !important;
    }
    i {
      color: #fff !important;
    }
  }

  .active__menu .el-submenu__title {
    font-weight: 700;
    opacity: 1;
    color: #fff !important;
    border-radius: 32px;
    background: hsla(0, 0%, 100%, 0.1) !important;
    height: 44px;
    i {
      color: #fff !important;
    }
  }
}
/deep/.el-submenu.is-active .el-submenu__title {
  border-color: transparent !important;
}
</style>
<style>
.el-menu--popup {
  min-width: 160px !important;
  margin: 0;
  padding: 0;
}
.el-menu--horizontal.customPopper .menu_item_box {
  height: 40px;
  line-height: 40px;
  padding: 0;
}
.el-menu--horizontal.customPopper .el-submenu__title {
  height: 40px !important;
  line-height: 40px !important;
}
.el-menu--horizontal.customPopper .menu_item_box {
  padding-left: 12px;
}
.el-menu--horizontal.customPopper .el-submenu__icon-arrow {
  position: absolute;
  top: 50%;
  right: 5px;
  margin-top: -5px;
}
</style>
