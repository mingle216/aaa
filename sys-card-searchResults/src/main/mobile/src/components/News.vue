<template>
  <div>
    <div class="filter_wrap">
      <span
        :class="{
          filter_btn: true,
          'portal-primary-color-lv1': sortType === item.key,
          'portal-primary-backgroundcolor-lv5': sortType === item.key
        }"
        v-for="item in filterList"
        :key="item.key"
        @click="handleCommand(item.key)"
      >
        {{ item.name }}
      </span>
    </div>
    <we-list
      class="news-list"
      v-model="loading"
      :finished="finished"
      :finished-text="$Lan(lanFunName, 'finishedText', '没有更多了')"
      @load="loadData"
    >
      <template v-for="(item, index) in list">
        <div
          class="news_item"
          v-if="index < currentPage * pageSize"
          :key="index"
          @click="goHandle(item)"
        >
          <div class="image_item" v-if="item.headpicurl">
            <div class="flex_wrap">
              <div class="wrap">
                <span class="clamp3 title" v-html="highLight(item.title)"> </span>
                <span class="column_time">
                  <i
                    class="text_ellipsis column_name"
                    v-html="highLight(item.channel)"
                  >
                  </i>
                  <i class="text_ellipsis">
                    {{ item.publishTime && item.publishTime.slice(0, -3) }}
                  </i>
                </span>
              </div>
              <img
                :src="item.headpicurl || errorImg"
                alt=""
                @error="handleError"
                width="109"
                height="82"
              />
            </div>
            <template v-if="currentUser && showCollectNews">
              <we-icon
                v-if="!item.favoriteFlag"
                name="star-o"
                color="#CCD0D3"
                @click.stop="handleCollect(item)"
              />
              <we-icon
                v-else
                name="star"
                color="#FFBC00"
                @click.stop="handleCollect(item)"
              />
            </template>
          </div>
          <div class="no_image_item" v-else>
            <div class="flex_wrap">
              <p class="text_ellipsis title" v-html="highLight(item.title)"></p>
              <template v-if="currentUser && showCollectNews">
                <we-icon
                  v-if="!item.favoriteFlag"
                  name="star-o"
                  color="#CCD0D3"
                  @click.stop="handleCollect(item)"
                />
                <we-icon
                  v-else
                  name="star"
                  color="#FFBC00"
                  @click.stop="handleCollect(item)"
                />
              </template>
            </div>
            <p class="column_time">
              <span
                class="text_ellipsis column_name"
                v-html="highLight(item.channel)"
              >
              </span>
              <span>
                {{ item.publishTime && item.publishTime.slice(0, -3) }}
              </span>
            </p>
          </div>
        </div>
      </template>
    </we-list>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    analyzeData: Array,
    list: {
      type: Array,
      default: () => {
        return [];
      }
    },
    keyword: {
      type: String,
      default: ""
    },
    lanFunName: {
      type: String,
      default: ""
    },
    item: {
      type: Object,
      default: () => {
        return {};
      }
    },
    currentPage: {
      type: Number,
      default: 1
    },
    sortType: {
      type: Number,
      default: 1
    },
    newsJump: String,
    showCollectNews: Number
  },
  watch: {
    list: {
      immediate: true,
      handler() {
        if (this.item.total > this.currentPage * this.pageSize) {
          this.finished = false;
        } else {
          this.finished = true;
        }
      }
    }
  },
  computed: {
    highLight() {
      return val => {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(val, data);
      };
    }
  },
  data() {
    return {
      errorImg: window.shell.ErrorImg,
      loading: false,
      finished: true,
      pageSize: 10,
      filterList: [
        {
          key: 1,
          name: '按匹配度排序'
        },
        {
          key: 2,
          name: '按时间排序'
        }
      ],
      currentUser: window.shell.getUserInfo(),
      isPosting: false
    };
  },
  methods: {
    handleCommand(key) {
      this.$emit('change-sort', key)
      this.$emit("update:currentPage", 1);
      window.shell
        .execCardMethod({
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "sortNewsForPortal",
          param: {
            keyword: this.keyword,
            pageNumber: 1,
            pageSize: this.pageSize,
            lange: sessionStorage.getItem("locale") || "zh_CN",
            sortType: key
          }
        })
        .then(res => {
          const list = res.data?.searchData?.newsData || [];
          this.$emit("update:list", list);
        });
    },
    loadData() {
      if (this.currentPage * this.pageSize < this.item.total) {
        window.shell
          .execCardMethod({
            cardId: this.router.cardId,
            cardWid: this.router.cardWid,
            method: "moreNewsForPortal",
            param: {
              keyword: this.keyword,
              pageNumber: this.currentPage,
              pageSize: this.pageSize,
              sortType: this.sortType
            }
          })
          .then(res => {
            const list = res.data?.searchData?.newsData || [];
            this.$emit("update:list", this.list.concat(list));
            this.finished = false;
            this.loading = false;
            this.$emit("update:currentPage", this.currentPage + 1);
          });
      } else {
        this.finished = true;
      }
    },
    goHandle(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ""
        },
        startTime: new Date().getTime()
      });
      if (item.sideFlag == 2 || (item.sideFlag != 1 && this.newsJump === "1")) {
        //跳转到本地新闻详情页面
        this.$emit('record-cache')
        window.shell.openPage(`/newsDetail?wid=${item.wid}`, 1, 1);
      } else {
        if (item.url) {
          this.$emit('record-cache')
          window.shell.openUrl(item.url);
        } else {
          window.shell.showMessage({
            type: "error",
            message: "暂无跳转链接",
          });
        }
      }
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null;
    },
    handleCollect(item) {
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
    }
  }
};
</script>

<style lang="less" scoped>
.text_ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  width: 100%;
}
.clamp3 {
  display: -webkit-box;
  overflow: hidden;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.filter_wrap {
  display: flex;
  margin-bottom: 12px;
  .filter_btn {
    padding: 8px 10px;
    border-radius: 4px;
    color: #000;
    background-color: #f6f6f8;
    margin-right: 12px;
  }
}
.news-list {
  .news_item {
    border-radius: 4px;
    margin-bottom: 12px;
    padding: 16px 12px;
    background-color: #f6f6f8;
    .title {
      font-size: 16px;
      font-weight: bold;
      text-align: justify;
      letter-spacing: 0;
      line-height: 20px;
    }
    .column_time {
      font-size: 12px;
      color: #707d8f;
      letter-spacing: 0;
      line-height: 16px;
      display: flex;
      justify-content: space-between;
      margin-top: 8px;
    }
    .flex_wrap {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .we-icon {
      margin-left: 10px;
      font-size: 16px;
    }
    .image_item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .wrap {
        width: calc(100% - 125px);
        .title {
          letter-spacing: 0.5px;
          margin-bottom: 10px;
        }
        .column_time {
          i {
            font-style: normal;
          }
          .column_name, i {
            max-width: 85px;
          }
        }
      }
      img {
        width: 109px;
        height: 82px;
        border-radius: 4px;
      }
    }
    .no_image_item {
      .column_time {
        font-size: 12px;
        color: #707d8f;
        letter-spacing: 0;
        line-height: 16px;
        display: flex;
        justify-content: space-between;
        .column_name {
          max-width: calc(100% - 105px);
        }
      }
    }
  }
}
</style>
