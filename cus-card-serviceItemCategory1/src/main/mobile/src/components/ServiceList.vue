<template>
  <div
    class="service-con"
    v-if="config"
    v-loading="loading"
    :class="{ 'show-icon': isShowIcon }"
    :id="templateCode === 'SYS_TEMPLATE_WORK' ? 'work_category' : ''"
  >
    <template v-for="(item, index) in list">
      <div
        class="service-item sideline-border-color-lv1 portal-font-color-lv1"
        :key="index"
        :class="{ 'more-item': !item.subjectWid }"
        @click="clickItemHandle(item)"
      >
        <template v-if="item.subjectWid">
          <div class="subject-icon" v-if="isShowIcon">
            <template v-if="isDept">
              <we-image
                v-if="config.departClassIcon === 2"
                width="1.12rem"
                height="1.12rem"
                fit="contain"
                :src="item.picLink ? item.picLink : defaultImg"
              />
              <i
                v-else
                class="iconfont"
                :class="[item.iconLink ? item.iconLink : 'icon-default']"
              ></i>
            </template>
            <template v-else>
              <we-image
                v-if="item.picLink"
                width="1.12rem"
                height="1.12rem"
                fit="contain"
                :src="item.picLink ? item.picLink : defaultImg"
              />
              <i
                v-else
                class="iconfont"
                :class="[item.iconLink ? item.iconLink : 'icon-default']"
              ></i>
            </template>
          </div>
          <div class="subject-name portal-font-color-lv1">
            {{ item.subjectName }}
          </div>
          <div class="item-list portal-font-color-lv2">
            <template v-if="resultWay == 0">
              <span :class="{ 'empty-item': !(item.itemList && item.itemList.length) }">
                {{ itemList(item.itemList) }}</span
              >
            </template>
            <template v-else-if="resultWay == 2">
              <div>{{ item.itemCount }}项服务</div>
              <div>{{ item.onlineDealItemCount }}项可在线办理</div>
            </template>
            <template v-else>
              <span :class="{ 'empty-item': !item.secondCategory }">
                {{
                  item.secondCategory
                    ? item.secondCategory
                    : $Lan(lanFunName, "secondCategory", "暂无分类")
                }}
              </span>
            </template>
          </div>
        </template>
        <template v-else>
          <div class="subject-icon">
            <we-image
              :width="moreIconSize"
              :height="moreIconSize"
              fit="contain"
              :src="moreImg"
            />
          </div>
          <div class="subject-name portal-font-color-lv1">
            {{ item.subjectName }}
          </div>
        </template>
      </div>
    </template>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    config: Object,
    serviceItemInfo: {
      type: Array,
      default: () => {
        return [];
      },
    },
    roleWid: {
      type: String,
      default: "",
    },
    categoryWid: {
      type: String,
      default: "",
    },
    loading: {
      type: Boolean,
      default: false,
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  watch: {
    serviceItemInfo: {
      immediate: true,
      handler(val) {
        this.currentPage = 1;
        if (val && val.length <= 4) {
          this.list = val;
        } else {
          this.list = val.slice(0, 3);
          this.list.push(this.moreData);
          // console.log(this.list)
        }
      },
    },
    loading: {
      immediate: true,
      handler(val) {
        if (val) {
          this.list = [];
        }
      },
    },
  },
  computed: {
    // 说明文字展示信息兼容老配置：如果是数组则取第一个 0从属事项 1从属二级分类
    resultWay() {
      console.log('%c [ this.config.resultWay ]-144', 'font-size:13px; background:pink; color:#bf2c9f;', this.config.resultWay)
      if (Array.isArray(this.config.resultWay)) {
        return this.config.resultWay[0]
      } else {
        return this.config.resultWay
      }
    },
    moreIconSize() {
      if (this.isShowIcon) {
        return "1.12rem";
      } else {
        return "0.7467rem";
      }
    },
    isDept() {
      return this.categoryWid === "dept-subject" ? true : false;
    },
    isShowIcon() {
      if (this.config) {
        return this.isDept
          ? this.config.departClassIcon > 0
          : this.config.otherClassIcon > 0;
      }
      return false;
    },

    itemList() {
      return function (items = []) {
        // console.log(items);
        if (items && items.length) {
          let str = "";
          items.forEach((item, index) => {
            let v_line = index < items.length - 1 ? " | " : "";
            str += item.itemName + v_line;
          });
          return str;
        }
        return this.$Lan(this.lanFunName, "itemList", "暂无服务事项");
      };
    },
    templateCode() {
      return window.shell.getTemplateCode()
    }
  },
  data() {
    return {
      list: [],
      allList: [],
      defaultImg: require("../asstes/default.png"),
      moreImg: require("../asstes/more.svg"),
      currentPage: 1,
      moreData:{
        iconLink: null,
        itemList: null,
        picLink: null,
        secondCategory: "",
        subjectName: this.$Lan(this.lanFunName, "subjectName", "查看更多"),
        subjectWid: "",
        isShowMore: true,
      },
       
    };
  },
  created(){
    
  },
  methods: {
    clickItemHandle(item) {
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
            infoType: 4,
            filterInfo: {
              pageCode: 'itemCategoryDetail'
            }
          }
        },
        startTime: new Date().getTime()
      });
      if (item.isShowMore) {
        // 点击查看更多

        this.currentPage += 1;
        const total = this.serviceItemInfo.length;
        const pageTotal = parseInt(this.currentPage * 4, 10);
        if (total > pageTotal) {
          this.list = this.serviceItemInfo.slice(0, pageTotal - 1);
          this.list.push(this.moreData);
        } else {
          this.list = this.serviceItemInfo.slice(0, total);
        }
        return;
      }
      window.shell.openPage(
        `/itemCategoryDetail?roleWid=${this.roleWid}&oneCategoryWid=${
          this.categoryWid
        }&categoryWid=${item.subjectWid}&categoryName=${encodeURIComponent(
          item.subjectName
        )}`,
        0,
        1
      );
    },
  },
};
</script>

<style lang="less" scoped>
.service-con {
  min-height: 236px;
  overflow: hidden;
  box-sizing: border-box;
  &.show-icon {
    min-height: 346px;
    .service-item {
      height: 152px;
      width: 162px;
      .subject-icon {
        width: 42px;
        height: 42px;
      }
      &.more-item {
        padding: 40px 17px;
      }
    }
  }

  .service-item {
    height: 98px;
    width: 162px;
    float: left;
    border-radius: 4px;
    font-size: 16px;
    text-align: center;
    padding: 20px 17px;
    border: 1px dashed;
    border-radius: 4px;
    margin-bottom: 16px;
    &:nth-child(odd) {
      margin-right: 17px;
    }
    .subject-icon {
      width: 28px;
      height: 28px;
      margin: 0 auto 8px auto;
      text-align: center;
      .iconfont {
        font-size: 42px;
      }
    }
    .subject-name {
      line-height: 22px;
      margin-bottom: 4px;
      width: 126px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      font-weight: bold !important;
    }
    .item-list {
      font-size: 12px;
      line-height: 134%;
      width: 126px;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
      .empty-item {
        line-height: 32px;
      }
    }
  }
}
#work_category {
  .service-item {
    width: 154px;
  }
  .subject-name, .item-list {
    width: 118px;
  }
}
</style>
