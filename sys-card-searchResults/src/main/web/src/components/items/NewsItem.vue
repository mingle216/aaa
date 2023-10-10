<template>
  <div
    :class="{
      news_item: true,
      'portal-font-color-lv1': true,
    }"
    :style="{ width: '100%' }"
  >
    <img
      v-if="item.headpicurl"
      :src="item.headpicurl || errImg"
      alt=""
      @error="handleError"
      width="92"
      height="70"
    />
    <div :class="{ news_item_wrap: true, hasImg: item.headpicurl }">
      <div class="flex_wrap">
        <p
          class="news_item_title text_ellipsis"
          :class="{
            'link_url portal-primary-color-hover-lv1': item.url,
          }"
          v-html="highLight('title')"
          :title="item.title"
          @click="linkUrl(item)"
        ></p>
        <i
          v-if="currentUser && showCollectNews"
          class="iconfont icon-favorites collect"
          :class="[
            item.favoriteFlag
              ? 'favorite_font_color'
              : 'unfavorite_font'
          ]"
          @click.stop="favoriteHandle(item)"
        />
      </div>
      <div>
        <p
          class="news_item_content text_ellipsis"
          v-html="highLight('contents')"
          :title="item.contents"
        ></p>
      </div>
      <p class="news_item_info">
        <span
          class="column_name"
          :title="item.channel"
          v-html="highLight('channel')"
        ></span>
        <span class="time">{{
          item.publishTime && item.publishTime.slice(0, -3)
        }}</span>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    item: Object,
    analyzeData: Array,
    keyword: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
    showNav: {
      type: Boolean,
      default: true,
    },
    searchResultWidth: {
      type: Number,
      default: 1026,
    },
    newsJump: String,
    showCollectNews: Number
  },
  computed: {
    highLight() {
      return (type) => {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(this.item[type], data);
      };
    },
    newsItemWidth() {
      const num = window.shell.isIE() ? 18 : 8;
      return this.showNav
        ? `${this.searchResultWidth - 126}px`
        : `${this.searchResultWidth - num}px`;
    },
  },
  data() {
    return {
      errImg: window.shell.ErrorImg,
      currentUser: window.shell.getUserInfo(),
      isPosting: false
    };
  },
  methods: {
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errImg;
      img.onerror = null;
    },
    linkUrl(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: "",
        },
        startTime: new Date().getTime(),
      });
      if (item.sideFlag == 2 || (item.sideFlag != 1 && this.newsJump === "1")) {
        //跳转到本地新闻详情页面
        window.shell.openPage(`/newsDetail?wid=${item.wid}`, 1, 1);
      } else {
        if (item.url) {
          window.shell.openUrl(item.url);
        } else {
          window.shell.showMessage({
            type: "error",
            message: "暂无跳转链接",
          });
        }
      }
    },
    favoriteHandle(item) {
      if (this.isPosting) {
        return;
      }
      this.isPosting = true;
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
            infoType: 20,
            newId: item.wid,
            fucType: item.favoriteFlag ? 3 : 2
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell
        .collectNews({
          id: item.wid,
          operate: item.favoriteFlag ? "0" : "1",
        })
        .then(() => {
          this.isPosting = false;
        })
        .catch(() => {
          this.isPosting = false;
        });
    },
  },
};
</script>

<style lang="less" scoped>
.text_ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  max-width: 100%;
}
.showNav {
  max-width: 1026px;
}
.link_url {
  cursor: pointer;
}
.news_item {
  padding: 14px 0;
  border-bottom: 1px solid #e7edf1;
  display: flex;
  justify-content: space-between;
  .collect {
    font-size: 14px;
    cursor: pointer;
    margin-left: 10px;
  }
  .favorite_font_color {
    color: #ffbc00;
  }
  img {
    width: 92px;
    height: 70px;
    margin-right: 16px;
    border-radius: 4px;
  }
  .news_item_wrap {
    width: 100%;
    .flex_wrap {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .news_item_title {
      font-size: 16px;
      letter-spacing: 0;
      line-height: 24px;
    }
    .news_item_content {
      font-size: 14px;
      color: #8c8c8c;
      letter-spacing: 0;
      line-height: 22px;
    }
    .news_item_info {
      font-size: 14px;
      color: #8c8c8c;
      letter-spacing: 0;
      line-height: 22px;
      display: flex;
      justify-content: space-between;
      .column_name {
        max-width: 800px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
  .hasImg {
    width: calc(100% - 108px);
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    .column_name {
      max-width: 692px;
    }
  }
}
</style>
