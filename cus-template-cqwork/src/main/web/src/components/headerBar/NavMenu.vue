<template>
  <div class="nav__menus">
    <template v-for="navMenu in navMenus">
      <!-- 最后一级菜单 -->
      <el-menu-item
        v-if="!navMenu.menu.length"
        :key="navMenu.wid"
        :data="navMenu"
        :index="navMenu.wid"
        :class="[
          navMenu.level > 0 || menuIndex === 'more'
            ? 'custom__item portal-primary-color-hover-lv1 portal-font-color-lv1 portal-font-backgroundcolor-hover-lv5'
            : '',
        ]"
        :disabled="navMenu.menuType === 0"
        :notUpdateActive="navMenu.menuType === 2 || navMenu.openType === 1"
      >
        <div class="menu_item_box" @click="handleClickMenu(navMenu)">
          <img
            v-if="navMenu.iconType === 0"
            width="18px"
            height="18px"
            :src="navMenu.iconUrl || errImg"
            alt=""
            style="margin-right:6px"
            @error="handleError"
          />
          <i
            v-else-if="navMenu.iconUrl"
            :class="['iconfont', navMenu.iconUrl]"
            style="margin-right: 6px"
          />
          <span
            slot="title"
            class="nav__title text__ellipsis"
            :title="
              isShowTitle(
                $t(navMenu.menuNameLangKey),
                navMenu.level > 0 || menuIndex === 'more' ? 14 : 8
              )
                ? $t(navMenu.menuNameLangKey)
                : ''
            "
          >
            {{ $t(navMenu.menuNameLangKey) }}
          </span>
        </div>
      </el-menu-item>

      <!-- 此菜单下还有子菜单 -->
      <el-submenu
        v-if="navMenu.menu.length"
        :key="navMenu.wid"
        :data="navMenu"
        :index="navMenu.wid"
        :popper-append-to-body="true"
        :placement="menuIndex === 'more' ? 'left-start' : 'right-start'"
        popper-class="customPopper"
        :class="[
          navMenu.level === 0 && menuIndex !== 'more' ? 'menu__mr' : '',
          navMenu.level === 0 && activeIndex === navMenu.wid
            ? 'active__menu'
            : '',
        ]"
      >
        <p
          :class="['arrow', offsetY ? 'active__arrow' : 'disable__arrow']"
          @click="handleUp(navMenu.menu.length)"
          v-if="navMenu.menu.length >= 10"
        >
          <i class="we-icon-caret-top" />
        </p>
        <template slot="title">
          <div
            :class="[
              'menu_item_box',
              navMenu.level === 0 && menuIndex !== 'more' ? 'menu__pd' : '',
              navMenu.level > 0 || menuIndex === 'more'
                ? 'custom__item portal-primary-color-hover-lv1 portal-font-color-lv1 portal-font-backgroundcolor-hover-lv5'
                : '',
            ]"
            :style="
              `height: ${navMenu.level === 0 ? 44 : 40}px;line-height: ${
                navMenu.level === 0 ? 44 : 40
              }px;`
            "
            @click="handleClickMenu(navMenu)"
          >
            <img
              v-if="navMenu.iconType === 0"
              width="18px"
              height="18px"
              :src="navMenu.iconUrl || errImg"
              alt=""
              style="margin-right: 6px"
              @error="handleError"
            />
            <i
              v-else
              :class="['iconfont', navMenu.iconUrl]"
              style="margin-right: 6px"
            />
            <span
              class="nav__title text__ellipsis"
              :title="
                isShowTitle(
                  $t(navMenu.menuNameLangKey),
                  navMenu.level > 0 || menuIndex === 'more' ? 14 : 8
                )
                  ? $t(navMenu.menuNameLangKey)
                  : ''
              "
            >
              {{ $t(navMenu.menuNameLangKey) }}
            </span>
          </div>
        </template>
        <!-- 递归 -->
        <div class="nav__box">
          <NavMenu
            :menuIndex="menuIndex"
            :navMenus="navMenu.menu"
            :style="{ transform: `translateY(${offsetY}px)` }"
          />
        </div>
        <p
          :class="[
            'arrow',
            navMenu.menu.length * 40 - Math.abs(offsetY) > maxHeight
              ? 'active__arrow'
              : 'disable__arrow',
          ]"
          @click="handleDown(navMenu.menu.length)"
          v-if="navMenu.menu.length >= 10"
        >
          <i class="we-icon-caret-bottom" />
        </p>
      </el-submenu>
    </template>
  </div>
</template>

<script>
import { showTitle } from "../../utils/tools";
export default {
  name: "NavMenu",
  props: ["navMenus", "menuIndex", "activeIndex", "dom"],
  data() {
    return {
      offsetY: 0,
      maxHeight: 360, // 菜单最大高度：9个菜单的高度
    };
  },
  computed: {
    errImg() {
      return window.shell.ErrorImg;
    },
    openedMenus() {
      return (
        (this.dom && this.dom.openedMenus && this.dom.openedMenus.length) || 0
      );
    },
  },
  watch: {
    openedMenus(val) {
      if (!val) {
        this.offsetY = 0;
      }
    },
  },
  methods: {
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errImg;
      img.onerror = null; //防止闪图
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
    isShowTitle(name, len) {
      return showTitle(name, len);
    },
  },
};
</script>

<style scoped>
/deep/.we-image__inner {
  vertical-align: top;
}
.nav__title {
  max-width: 86px;
}
.nav__menus i {
  font-size: 18px;
}
.nav__menus .we-image {
  width: 18px;
  height: 18px;
}
.custom__item {
  width: 100%;
  padding: 0 10px;
  height: 40px !important;
  display: flex;
  align-items: center;
}
.custom__item .nav__title {
  max-width: 110px;
  font-size: 14px;
}
.custom__item i {
  color: #c6cedb;
  font-size: 14px;
}
.custom__item:hover i {
  color: inherit;
}
.custom__item .we-image {
  width: 14px;
  height: 14px;
}
.menu__pd {
  padding: 0 16px 0 0;
}
.menu__mr {
  margin: 0 12px 0 0;
  display: flex;
  align-items: center;
}
.nav__box {
  max-height: 360px;
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
.el-menu-item.is-disabled {
  opacity: 1 !important;
  cursor: pointer;
}
/deep/.image-slot {
  display: flex;
}
.menu_item_box {
  width: 100%;
  height: 44px;
  line-height: 44px;
  padding: 0 12px;
}
</style>
