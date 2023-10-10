<template>
  <we-header class="home__menu">
    <el-menu
      mode="horizontal"
      :default-active="activeIndex"
      style="display: flex"
      :unique-opened="true"
      ref="MenuTree"
    >
      <NavMenu
        style="display: flex;min-width:160px"
        :navMenus="getMenu.length > 5 ? getMenu.slice(0, 4) : getMenu"
        :activeIndex="activeIndex"
        :dom="mentDom"
      />
      <el-submenu
        v-if="getMenu.length > 5"
        index="more"
        :popper-append-to-body="true"
        class="more__menu"
        popper-class="customPopper"
      >
          <template slot="title">
            <i class="iconfont icon-menu-Universal" style="margin-right: 6px;font-size: 18px"></i>
            <span class="more__title">
              {{ $Lan('CUS_TEMPLATE_OFFICIAL', 'more', '更多') }}
            </span>
          </template>
        <div @mouseleave="handleLeave">
          <p
            :class="['arrow', offsetY ? 'active__arrow' : 'disable__arrow']"
            @click="handleUp(moreList.length)"
          >
            <i
              class="el-icon-caret-top"
              v-if="moreList.length >= 10"
            />
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
            <i
              class="el-icon-caret-bottom"
              v-if="moreList.length >= 10"
            />
          </p>
        </div>
      </el-submenu>
    </el-menu>
  </we-header>
</template>
<script>
import NavMenu from './NavMenu'
export default {
  components: {
    NavMenu
  },
  props: {
    menus: {
      type: Array,
      default: () => {
        return []
      }
    },
    activeMenuId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      offsetY: 0,
      maxHeight: 360,
      mentDom: null
    }
  },
  computed: {
    getMenu() {
      return this.menus.map(el => this.mapTree(el, 0))
    },
    activeIndex() {
      return this.activeMenuId
    },
    moreList() {
      return this.getMenu.slice(4, this.getMenu.length)
    },
    downArrow() {
      return this.moreList.length * 40 - Math.abs(this.offsetY) > this.maxHeight
    }
  },
  methods: {
    // 递归menus，添加level标识
    mapTree(obj, index) {
      const haveChildren = Array.isArray(obj.menu) && obj.menu.length > 0
      return {
        ...obj,
        level: index++,
        offsetY: 0,
        menu: haveChildren ? obj.menu.map(d => this.mapTree(d, index++)) : []
      }
    },
    hideMenu() {
      const menuTree = this.$refs.MenuTree
      if (menuTree && menuTree.openedMenus && menuTree.openedMenus.length) {
        menuTree.openedMenus = []
      }
    },
    handleUp(num) {
      if (this.offsetY) {
        const height = num * 40 // 菜单高度
        if (height - Math.abs(this.offsetY) >= this.maxHeight) {
          this.offsetY += 40
        }
      }
    },
    handleDown(num) {
      const height = num * 40 // 菜单高度
      if (height - Math.abs(this.offsetY) > this.maxHeight) {
        this.offsetY -= 40
      }
    },
    handleLeave() {
      const menuTree = this.$refs.MenuTree
      setTimeout(() => {
        if (menuTree && menuTree.openedMenus && menuTree.openedMenus.length === 0) {
          this.offsetY = 0
        }
      }, 500)
    }
  },
  mounted() {
    this.mentDom = this.$refs.MenuTree;
    window.addEventListener("click", this.hideMenu);
    window.shell.on("onScoll", this.hideMenu);
    this.$once("hook:beforeDestroy", () => {
      window.removeEventListener("click", this.hideMenu, true);
      window.shell.off("onScoll", this.hideMenu);
    });
  }
}
</script>
<style scoped lang="less">
.home__menu {
  display: flex;
  align-items: center;
  flex-grow: 2;
  padding: 0 12px 0 0;
  color: #212A39;
	position: relative;

	&::after{
		content: "";
		display: inline-block;
		width: 1px;
		height: 24px;
		background: #ddd;
		position: absolute;
		right: 0;
		top: 50%;
		transform: translateY(-50%);
	}
}
.more__title {
  color: #212A39;
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
.more__menu {
  margin-left: 15px;
}
/deep/.el-submenu.is-active .el-submenu__title {
  border-color: transparent!important;
}
</style>
<style>
.el-menu--popup {
  min-width: 160px !important;
  margin: 0;
  padding: 0;
}
.el-submenu__icon-arrow {
  right: 5px;
  margin-top: -5px;
}
.el-menu--horizontal > .el-submenu .el-submenu__icon-arrow {
  margin-top: 4px !important;
}
.customPopper .el-submenu__title {
  height: 40px!important;
  line-height: 40px!important;
}
</style>