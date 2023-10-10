<template>
  <div>
    <p
      :class="['arrow', offsetY ? 'active__arrow' : 'disable__arrow']"
      @click="handleUp(navMenus.length)"
      v-if="navMenus.length >= 10"
    >
      <i class="we-icon-caret-top" />
    </p>

    <div class="cascader-content">
      <div v-for="navMenu in navMenus" :key="navMenu.wid" :style="{ transform: `translateY(${offsetY}px)` }">
        <!-- 最后一级菜单 -->
        <div
          :key="navMenu.wid"
          v-if="!navMenu.menu.length"
          class="pop_item portal-font-color-lv1 portal-primary-color-hover-lv1 portal-primary-backgroundcolor-hover-lv5"
          @click="handleClickMenu(navMenu)"
        >
          <div class="left">
            <we-image
              v-if="navMenu.iconType === 0"
              style="margin-right: 6px"
              :src="navMenu.iconUrl"
            >
              <div slot="error" class="image-slot">
                <img :src="errImg" alt="" width="18" height="18" />
              </div>
            </we-image>
            <i
              v-else
              :class="['iconfont', navMenu.iconUrl]"
              style="margin-right: 6px"
            />
            <span
              class="nav__title text__ellipsis" 
              :title="isShowTitle($t(navMenu.menuNameLangKey), 14) ? $t(navMenu.menuNameLangKey) : ''"
            >
              {{ $t(navMenu.menuNameLangKey) }}
            </span>
          </div>
          <span style="display:inline-block;width:16px "></span>
        </div>

        <div v-if="navMenu.menu.length" :key="navMenu.wid">
          <we-popover
            placement="left-start"
            trigger="hover"
            :visible-arrow="false"
            :offset="0"
            :boundariesPadding="200"
            :open-delay="300"
            popper-class="customPopper"
          >
            <pop-menu :navMenus="navMenu.menu"></pop-menu>

            <div
              slot="reference"
              class="pop_item portal-font-color-lv1 portal-primary-color-hover-lv1 portal-primary-backgroundcolor-hover-lv5"
              @click="handleClickMenu(navMenu)"
            >
              <div class="left">
                <we-image
                  v-if="navMenu.iconType === 0"
                  style="margin-right: 6px"
                  :src="navMenu.iconUrl"
                >
                  <div slot="error" class="image-slot">
                    <img :src="errImg" alt="" width="18" height="18" />
                  </div>
                </we-image>
                <i
                  v-else
                  :class="['iconfont', navMenu.iconUrl]"
                  style="margin-right: 6px"
                />
                <span
                  class="nav__title text__ellipsis"
                  :title="isShowTitle($t(navMenu.menuNameLangKey), 14) ? $t(navMenu.menuNameLangKey) : ''"
                >
                  {{ $t(navMenu.menuNameLangKey) }}
                </span>
              </div>
              <i class="we-icon-arrow-right"></i>
            </div>
          </we-popover>
        </div>
      </div>
    </div>
    <p
      :class="[
        'arrow',
        navMenus.length * 40 - Math.abs(offsetY) > maxHeight
          ? 'active__arrow'
          : 'disable__arrow'
      ]"
      @click="handleDown(navMenus.length)"
      v-if="navMenus.length >= 10"
    >
      <i class="we-icon-caret-bottom" />
    </p>
  </div>
</template>

<script>
import { showTitle } from '../../utils/tools'
export default {
  name: 'PopMenu',
  props: ['navMenus'],
  data() {
    return {
      offsetY: 0,
      maxHeight: 360 // 菜单最大高度：9个菜单的高度
    }
  },
  computed: {
    errImg() {
      return window.shell.ErrorImg
    },
    openedMenus() {
      return (
        (this.dom && this.dom.openedMenus && this.dom.openedMenus.length) || 0
      )
    }
  },
  methods: {
    handleClickMenu(item) {
      // openType: 0当前页、1新开页签
      // menuType: 0无连接、1内部页面、2第三方链接
      let url = item.linkUrl
      if (item.menuType === 1) {
        url = item.linkUrl.replace('/portal', '')
      }
      window.shell.openPage(url, item.openType, item.menuType)
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
      console.log(num)
      const height = num * 40 // 菜单高度
      if (height - Math.abs(this.offsetY) > this.maxHeight) {
        this.offsetY -= 40
      }
    },
    isShowTitle(name, len) {
      return showTitle(name, len)
    },
    handleResetOffset() {
      this.offsetY = 0
    }
  }
}
</script>

<style>
.customPopper {
  padding: 0;
  margin: 0 !important;
}
.cascader-content {
  max-height: 360px;
  overflow: hidden;
}
.pop_item {
  width: 160px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding: 0 10px;
  height: 40px;
  line-height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
}
.pop_item .left {
  display: flex;
  align-items: center;
}
.pop_item .we-image {
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
}
.pop_item img {
  vertical-align: middle;
}
.pop_item .nav__title {
  max-width: 110px;
  font-size: 14px;
  /* color: #102645; */
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
</style>
