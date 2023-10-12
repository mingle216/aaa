<template>
  <div class="nav__menus" :class="type">
    <template v-for="navMenu in navMenus">
      <!-- 最后一级菜单 -->
      <el-menu-item v-if="!navMenu.menu.length" :key="navMenu.wid" :data="navMenu" :index="navMenu.wid"
        :disabled="navMenu.menuType === 0" :notUpdateActive="navMenu.menuType === 2 || navMenu.openType === 1">
        <div class="menu_item_box" @click="handleClickMenu(navMenu)">
          <img v-if="navMenu.iconType === 0" width="18px" height="18px" :src="navMenu.iconUrl || errImg" alt=""
            :style="type=='fold'?'':'margin-right:6px'" @error="handleError" />
          <i v-else-if="navMenu.iconUrl" :class="['iconfont', navMenu.iconUrl]"
            :style="type=='expand'?'margin-right: 6px':''" />
          <span slot="title" class="nav__title text__ellipsis" :title="
              isShowTitle(
                $t(navMenu.menuNameLangKey),
                navMenu.level > 0 || menuIndex === 'more' ? 14 : 8
              )
                ? $t(navMenu.menuNameLangKey)
                : ''
            ">
            <label v-if="type=='fold'">

              {{ navMenu.menuName.replace(/\s/g,"").length>2?navMenu.menuName.slice(0,2):$t(navMenu.menuNameLangKey) }}
            </label>
            <label v-else>
              {{ $t(navMenu.menuNameLangKey)}}
            </label>

          </span>
        </div>
      </el-menu-item>
    </template>
  </div>
</template>

<script>
import { showTitle } from "../../utils/tools";
export default {
  name: "NavMenu",
  props: ["navMenus", "menuIndex", "activeIndex", "dom", "type"],
  data () {
    return {

    };
  },
  computed: {
    errImg () {
      return window.shell.ErrorImg;
    },

  },

  methods: {
    handleError (e) {
      let img = e.srcElement;
      img.src = this.errImg;
      img.onerror = null; //防止闪图
    },
    handleClickMenu (item) {
      console.log('item', item)
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

    isShowTitle (name, len) {
      return showTitle(name, len);
    },
  },
  watch: {
    navMenus: {
      handler (res) {
        console.log('resnavMenu', res)

      }, deep: true
    }
  }
};
</script>

<style scoped lang="less">
/deep/.we-image__inner {
  vertical-align: top;
}

.nav__title {
  max-width: 66px;
  label {
    cursor: pointer;
  }
}
.expand {
  .nav__title {
    margin-left: 8px;
  }
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

.nav__menus {
  &.fold {
    /deep/.el-menu-item {
      padding: 0 0 !important;
      margin-bottom: 16px;
      width:44px;
      height:44px;
    }
    /deep/.el-menu-item.is-active {
      border-radius: 4px;
      background: #f4f6f8;
    }
    .menu_item_box {
      width: 44px;
      height: 44px;
      font-size: 12px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      .nav__title {
        margin-top: 2px;
      }
    }
  }

  &.expand {
    /deep/.el-menu-item.is-active {
      .menu_item_box {
        font-weight: bold;
      }
    }
    .menu_item_box {
      width: 100%;
      height: 36px;
      line-height: 32px;
      padding: 0 6px;
      font-size: 14px;
      text-align: left;
    }
    /deep/.el-menu-item {
      margin-bottom: 16px;
      border-radius: 30px;
      cursor: pointer;
      &:hover,
      &:focus {
        border-radius: 30px;
      }
    }
  }
}
</style>
