<template>
  <div v-if="this.showOnlineButton != 0 && this.serviceList.length != 0">
    <div class="title1 portal-font-color-lv1">
      {{ dealitem }}
    </div>
    <div class="deal-item">
      <div
        v-for="item in serviceData"
        :key="item.serviceName"
        class="deal-flex"
        @click="
          (ev) => {
            handleOnline(item, ev);
          }
        "
      >
        <div
          class="item-content"
          :class="{
            'portal-font-color-lv1': item.permission,
            'portal-font-color-lv4': !item.permission,
            'portal-primary-backgroundcolor-hover-lv5': true,
            'portal-primary-color-hover-lv1': item.permission,
          }"
        >
          <div
            class="content-leftIcon"
            :class="{ 'no-permission-service': !item.permission }"
          >
            <img
              class="left_img"
              :src="getIconUrl(item) || errorImg"
              :alt="$Lan(lanFunName, 'imgAlt', '加载失败')"
              @error="
                () => {
                  item.pcIconUrl = errorImg;
                }
              "
            />
          </div>
          <div class="content-text">
            <we-tooltip
              v-if="isClickItem || item.serviceStation != 1"
              class="item"
              effect="dark"
              :content="serviceNameHandle(item)"
              placement="top-start"
              :open-delay="800"
            >
              <h3 class="text-ellipsis">
                {{ serviceNameHandle(item) }}
                <i
                  class="we-icon-mobile-phone"
                  style="margin:3px 0px 0px 6px"
                  v-if="item.serviceStation === 1"
                ></i>
              </h3>
            </we-tooltip>
            <h3
              class="text-ellipsis"
              v-if="item.serviceStation == 1 && !isClickItem"
            >
              {{ serviceNameHandle(item) }}
              <i
                class="we-icon-mobile-phone"
                style="margin:3px 0px 0px 6px"
                v-if="item.serviceStation === 1"
              ></i>
            </h3>
          </div>

          <div class="content-righticon">
            <i
              v-if="isLogin"
              class=" icon iconfont icon-favorites"
              :class="[
                item.favorite
                  ? 'favorite_font favorite_font_color'
                  : 'unfavorite_font',
              ]"
              @click.stop="collectService(item)"
            ></i>
          </div>
        </div>
      </div>
    </div>
    <mobile-app-popover
      ref="MobileAppPopover"
      :itemInfo="clickInfo"
      :serviceList="[clickInfo]"
      :lan-fun-name="lanFunName"
      @popClose="popClose()"
    ></mobile-app-popover>
  </div>
</template>

<script>
import MobileAppPopover from "./childComponents/MobileAppPopover";
export default {
  props: {
    router: Object,
    serviceList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    showOnlineButton: {
      type: Number,
      default: 0,
    },
    isLogin: {
      type: Boolean,
      default: false,
    },
    dealId: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
    fieldName: String,
  },
  components: {
    MobileAppPopover,
  },
  data() {
    return {
      dealitem: this.$Lan(this.lanFunName, "dealItem", "在线办理"),
      errorImg: window.shell.ErrorImg,
      serviceData: this.serviceList,
      langKeyList: {},
      isClick: true,
      clickInfo: {},
      isClickItem: true,
    };
  },
  watch: {
    serviceList: function(data) {
      this.serviceData = data;
      const obj = {};
      this.serviceData.forEach((item) => {
        let langKeys = item.serviceNameLangKeys || [];
        langKeys.forEach((c_item) => {
          if (this.$i18n.locale === c_item.langCountry) {
            obj[c_item.langKey] = c_item.langValue;
          }
        });
      });
      this.langKeyList = obj;
    },
  },
  methods: {
    serviceNameHandle(item) {
      return this.langKeyList[item.serviceNameLangKey]
        ? this.langKeyList[item.serviceNameLangKey]
        : item.serviceName;
    },

    handleOnline(item, ev) {
      this.isClickItem = false;
      this.clickInfo = item;
      if (item.serviceStation === 1) {
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
              serviceId: item.serviceId,
            },
          },
          startTime: new Date().getTime(),
        });
        // 移动端服务
        let parent = ev.target;
        let name = parent.className;
        while (!name.includes("deal-flex")) {
          parent = parent.parentNode;
          if (parent) {
            name = parent.className;
          }
        }
        this.$refs.MobileAppPopover.show(parent);
        return;
      }
      if (item.pcAccessUrl && item.permission) {
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
              serviceId: item.serviceId,
            },
          },
          startTime: new Date().getTime(),
        });
        // this.$emit("go-online", item.pcAccessUrl, item.permission);
        window.shell.openService(item);
      } else {
        window.minosStataCollect.collect({
          actionType: 0,
          functionType: 1,
          actionParams: {
            pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
            pageName: window.shell.getPageData().pageInfoEntity.pageName,
            cardWid: this.router.cardWid,
            cardId: this.router.cardId,
            cardName: this.router.cardName,
          },
          startTime: new Date().getTime(),
        });
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan(
            this.lanFunName,
            "showMessage",
            "暂无使用权限，请联系管理员"
          ),
        });
      }
    },
    popClose() {
      this.isClickItem = true;
    },
    collectService(item) {
      if (!this.isClick) {
        return;
      }
      this.isClick = false;
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
            serviceId: item.wid,
            fucType: item.favorite ? 3 : 2,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.collectService({
        operate: item.favorite ? 0 : 1,
        id: item.wid,
      });
      this.updateGetServiceList(item);
    },
    updateGetServiceList(item) {
      this.isClick = true;
      this.serviceData.some((element) => {
        if (element.wid === item.wid) {
          element.favorite = !element.favorite;
        }
      });
    },
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
  },
};
</script>

<style lang="less" scoped>
.deal-item {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  align-items: center;
  .deal-flex {
    width: 25%;
  }
  .item-content:extend(.deal-item) {
    justify-content: space-evenly;
    border-radius: 4px;
    height: 72px;
    cursor: pointer;
    .icon-favorites {
      display: none;
    }
    &:hover {
      .icon-favorites {
        display: block;
      }
    }
    .content-leftIcon {
      margin: 0px 12px;
      width: 44px;
      height: 44px;
      .left_img{
        width: 100%;
        height: 100%;
        border-radius: 4px;
      }
    }
    .content-text {
      margin-right:12px ;
      width: calc(100% - 108px);
      font-size: 16px;
      letter-spacing: 0;
      line-height: 24px;
    }
    .content-righticon {
      margin-right:12px ;
      width: 16px;
      height: 16px;
    }
    .text-ellipsis {
      white-space: nowrap; //当列表内容长度抵达容器边界时不转到下一行
      overflow: hidden; //不显示超过对象尺寸的内容，数据无论有多少，都不会换行
      text-overflow: ellipsis; //将被隐藏的那部分用省略号代替
    }
  }
}
</style>
