<template>
  <div
    :class="[
      'sidebar__wrap',
      direction === 'left' ? 'left__side__pos' : 'right__side__pos',
      direction === 'right' && rightOffset ? 'right__side__posIE' : '',
    ]"
    :style="{ transform: `translateY(-${transformHeight}px)` }"
    v-if="sideBarLists.length"
  >
    <div v-if="isExpand" class="side_box">
      <div
        class="expand__side"
        v-for="(el, i) in Math.ceil(sideBarLists.length / 5)"
        :key="i"
      >
        <div
          v-for="item in sideBarLists.slice(i * 5, 5 * (i + 1))"
          :key="item.id"
          class="item__side portal-font-color-lv2 portal-primary-color-hover-lv1"
          :style="itemWidthStyle"
          @click="handleClickSide(item)"
        >
          <span v-if="item.iconType === 0" class="portal-primary-color-lv1">
            <we-image style="width: 18px; height: 18px" :src="item.iconUrl">
              <div slot="error" class="image-slot">
                <img :src="errImg" alt="" width="18" height="18" />
              </div>
            </we-image>
          </span>
          <span v-else class="portal-primary-color-lv1">
            <i :class="['icon', 'iconfont', item.iconUrl]"></i>
          </span>
          <we-popover
            popper-class="myPopover"
            abc="abcabc"
            :placement="direction"
            width="143"
            height="36"
            trigger="hover"
            :ref="$t(item.displayNameLangKey)"
            v-if="getTextWidth($t(item.displayNameLangKey)) > 50"
            @show="showPopover($t(item.displayNameLangKey), i, direction)"
          >
            <span class="popover-content">
              {{ $t(item.displayNameLangKey) }}
            </span>
            <we-button class="real" slot="reference"
              >&nbsp;&nbsp;&nbsp;&nbsp;</we-button
            >
          </we-popover>
          <span
            class="desc text__ellipsis"
            :title="$t(item.displayNameLangKey)"
          >
            {{ $t(item.displayNameLangKey) }}
          </span>
          <sup v-show="item.count" class="we-badge we-badge__content">{{
            item.count
          }}</sup>
        </div>
      </div>
      <div
        :class="[
          'collapse__button portal-font-color-lv1 portal-primary-color-hover-lv1',
          direction === 'left' ? 'left__btn' : 'right__btn',
        ]"
        @click="handleExpand"
      >
        <i
          :class="[
            'iconfont',
            direction === 'left' ? 'icon-menu-PutAway' : 'icon-menu-Unfold',
          ]"
        ></i>
        <span class="desc text__ellipsis">
          {{ $Lan("CUS_TEMPLATE_OFFICIAL", "fold", "收起") }}
        </span>
      </div>
    </div>
    <div
      class="expand__side"
      :style="expandStyle"
      v-show="showExpand && !isExpand"
    >
      <div
        v-for="item in sideBarLists.slice(0, 4)"
        :key="item.id"
        class="item__side portal-font-color-lv2 portal-primary-color-hover-lv1"
        :style="itemWidthStyle"
        @click="handleClickSide(item)"
      >
        <span v-if="item.iconType === 0" class="portal-primary-color-lv1">
          <we-image style="width: 18px; height: 18px" :src="item.iconUrl">
            <div slot="error" class="image-slot">
              <img :src="errImg" alt="" width="18" height="18" />
            </div>
          </we-image>
        </span>
        <span v-else class="portal-primary-color-lv1">
          <i :class="['icon', 'iconfont', item.iconUrl]"></i>
        </span>

        <we-popover
          popper-class="myPopover"
          abc="abcabc"
          :placement="direction"
          width="143"
          height="36"
          trigger="hover"
          v-if="getTextWidth($t(item.displayNameLangKey)) > 50"
        >
          <span class="popover-content">
            {{ $t(item.displayNameLangKey) }}
          </span>
          <we-button class="real" slot="reference"
            >&nbsp;&nbsp;&nbsp;&nbsp;</we-button
          >
        </we-popover>

        <span class="desc text__ellipsis" :title="$t(item.displayNameLangKey)">
          {{ $t(item.displayNameLangKey) }}
        </span>
        <sup v-show="item.count" class="we-badge we-badge__content">{{
          item.count
        }}</sup>
      </div>
      <div
        class="item__side portal-font-color-lv1 portal-primary-backgroundcolor-hover-lv1"
        :style="itemWidthStyle"
        @click="handleExpand"
      >
        <i
          :class="[
            'iconfont',
            direction === 'left' ? 'icon-menu-Unfold' : 'icon-menu-PutAway',
          ]"
        ></i>
        <span class="desc text__ellipsis">
          {{ $Lan("CUS_TEMPLATE_OFFICIAL", "showAll", "查看全部") }}
        </span>
      </div>
    </div>
    <div
      class="expand__side"
      :style="expandStyle"
      v-if="!showExpand && !isExpand"
    >
      <div
        v-for="item in sideBarLists"
        :key="item.id"
        class="item__side portal-font-color-lv2 portal-primary-color-hover-lv1"
        :style="itemWidthStyle"
        @click="handleClickSide(item)"
      >
        <span v-if="item.iconType === 0" class="portal-primary-color-lv1">
          <we-image style="width: 18px; height: 18px" :src="item.iconUrl">
            <div slot="error" class="image-slot">
              <img :src="errImg" alt="" width="18" height="18" />
            </div>
          </we-image>
        </span>
        <span v-else class="portal-primary-color-lv1">
          <i :class="['icon', 'iconfont', item.iconUrl]"></i>
        </span>
        <we-popover
          popper-class="myPopover"
          abc="abcabc"
          :placement="direction"
          width="143"
          height="36"
          trigger="hover"
          v-if="getTextWidth($t(item.displayNameLangKey)) > 50"
        >
          <span class="popover-content">
            {{ $t(item.displayNameLangKey) }}
          </span>
          <we-button class="real" slot="reference"
            >&nbsp;&nbsp;&nbsp;&nbsp;</we-button
          >
        </we-popover>
        <span class="desc text__ellipsis" :title="$t(item.displayNameLangKey)">
          {{ $t(item.displayNameLangKey) }}
        </span>
        <sup v-show="item.count" class="we-badge we-badge__content">{{
          item.count
        }}</sup>
      </div>
    </div>
  </div>
</template>

<script>
import { isIE, isFirefox } from "../utils/util.js";
export default {
  props: {
    direction: {
      type: String,
      default: "left",
    },
    sideList: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
  data() {
    return {
      itemWidth: 66,
      itemHeight: 77,
      isExpand: false,
      rightOffset: isIE() || isFirefox(),
      sideBarLists: [],
    };
  },
  computed: {
    transformHeight() {
      const height =
        (this.sideBarLists.length > 5 ? 5 : this.sideBarLists.length) *
        this.itemHeight;
      return Math.ceil(height / 2);
    },
    expandSideWidth() {
      return this.isExpand
        ? Math.ceil(this.sideBarLists.length / 5) * this.itemWidth + "px"
        : this.itemWidth + "px";
    },
    expandStyle() {
      return {
        width: this.expandSideWidth,
        background: "#fff",
        borderRadius: "3px",
      };
    },
    showExpand() {
      return this.sideBarLists.length > 5;
    },
    errImg() {
      return window.shell.ErrorImg;
    },
    itemWidthStyle() {
      return {
        width: `${this.itemWidth}px`,
      };
    },
    currentUser() {
      return window.shell.getUserInfo() || null;
    },
  },
  watch: {
    sideList: {
      handler: function() {
        this.sideBarLists = JSON.parse(JSON.stringify(this.sideList));
        if (this.sideBarLists && this.sideBarLists.length > 0) {
          this.currentUser && this.getSidebarCount();
        }
      },
      immediate: true,
    },
  },
  methods: {
    getSidebarCount() {
      this.sideBarLists.forEach((item, index) => {
        if (!item.countAddress) return;
        window.shell.getSidebarCount({ wid: item.wid }, (res) => {
          this.$set(
            this.sideBarLists[index],
            "count",
            (res && res.data !== "0" && res.data) || 0
          );
        });
      });
    },
    handleExpand() {
      this.isExpand = !this.isExpand;
    },
    handleClickSide(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 9,
            filterInfo: {
              wid: item.wid,
              name: item.columnName,
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
    getTextWidth(str = "") {
      const dom = document.createElement("span");
      dom.style.display = "inline-block";
      dom.textContent = str;
      dom.style.fontSize = "12px";
      document.body.appendChild(dom);
      const width = dom.clientWidth;
      document.body.removeChild(dom);
      return width;
    },
    showPopover(dom, i, dir) {
      this.$nextTick(() => {
        if (!this.$refs[dom]) {
          return;
        }
        const left = this.$refs[dom][0].$refs.popper.style.left.split("px")[0];
        let d_left = 0;
        if (dir == "right") {
          d_left = Number(left) - 66 * i;
        } else {
          d_left =
            Number(left) +
            66 * (Math.ceil(this.sideBarLists.length / 5) - (i + 1));
        }
        this.$refs[dom][0].$refs.popper.style.left = d_left + "px";
      });
    },
  },
};
</script>

<style scoped>
.sidebar__wrap {
  position: fixed;
  top: 47%;
  /* top: 26%; */
  /* transform: translate(0, -50%); */
  box-shadow: 0 0.125rem 0.9375rem 0.25rem rgba(22, 29, 51, 0.05);
  z-index: 1000;
  border-radius: 3px;
}
.side_box {
  display: flex;
  background: #fff;
  border-radius: 4px;
}

.left__side__pos {
  left: 32px;
}

.right__side__pos {
  right: 32px;
}
.right__side__posIE {
  right: 17px;
}
.expand__side {
  max-height: 400px;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  position: relative;
}
.item__side {
  /* width: 66px; */
  position: relative;
  height: 77px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  /* color: #102645; */
  letter-spacing: 0;
  text-align: center;
  border-radius: 3px;
  background: #fff;
  border-bottom: 1px solid #F0F0F0;
}

.item__side:last-of-type{
  border-bottom: none;
}

.we-badge {
  position: absolute;
  top: 0;
  right: 0;
  height: 20px;
  line-height: 18px;
}

.item__side i {
  font-size: 18px;
}
.desc {
  margin-top: 6px;
  max-width: 48px;
}
.collapse__button {
  position: absolute;
  top: calc(50% - 40px);
  background: #fff;
  height: 80px;
  width: 40px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  cursor: pointer;
}
.collapse__button:hover {
  color: #fff !important;
}
.left__btn {
  right: -40px;
  border-radius: 0px 4px 4px 0px;
  box-shadow: 4px 2px 2px 0 rgba(22, 29, 51, 0.05);
}
.right__btn {
  left: -40px;
  border-radius: 4px 0 0 4px;
  box-shadow: -4px 2px 2px 0 rgba(22, 29, 51, 0.05);
}

.myPopover {
  text-align: center;
}
.real {
  height: 77px;
  width: 66px;
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0;
}
.popover-content {
  display: block;
  max-height: 56px;
  line-height: 19px;
  overflow-y: auto;
  text-align: center;
}
</style>
<style>
.we-popover {
  padding: 10px 16px !important;
}
</style>
