<template>
  <div class="link-container" v-loading="containerLoading">
    <EmptyCon
      v-if="configInfo.linkList && configInfo.linkList.length === 0"
      :tip="$Lan('CUS_CARD_ZTZL', 'noData', '暂无链接导航')"
      :height="configInfo.linkHeight.value"
    />
    <template v-else>
      <AutoContainer
        :con-type="configInfo.linkHeight.type"
        :con-height="configInfo.linkHeight.value"
      >
        <div class="link-wrap">
          <div
            class="link-wrap-item"
            v-for="(item, index) in configInfo.linkList"
            :key="index"
          >
            <div
              class="link-wrap-item-top"
              :style="`background-image: url(${item.iconUrl || imgError})`"
            >
              <p class="link-wrap-item-top-title">
                <v-clamp autoresize :max-lines="2" :title="getI18nMessage(item.label)">
                  {{ getI18nMessage(item.label) }}
                </v-clamp>
              </p>
              <span class="rect-line"></span>
            </div>
            <div class="link-wrap-item-bottom">
              <div class="link-item portal-font-color-lv1">
                <p
                  v-for="(link, ind) in item.linkList"
                  :key="ind"
                  class="portal-primary-color-hover-lv1"
                  @click="goLink(link)"
                >
                  <v-clamp autoresize :max-lines="1" :title="getI18nMessage(link.label)">
                    {{ getI18nMessage(link.label) }}
                  </v-clamp>
                </p>
              </div>
            </div>
          </div>
        </div>
      </AutoContainer>
    </template>
  </div>
</template>

<script>
import mock from "./mock.js";
import TrackMixin from "./mixins/track.js";
export default {
  name: "CUS_CARD_ZTZL",
  props: {
    index: Number,
    router: Object,
    colWidth: Number,
  },
  mixins: [TrackMixin],
  components: {},
  data() {
    return {
      //容器加载
      containerLoading: false,
      checked: false,
      fontClass: "",
      leftRightStyle: "",
      datas: [],
      configInfo: mock,
      linkWidth: "initial",
    };
  },
  computed: {},
  methods: {
    imgError(event) {
      let img = event.srcElement;
      img.src = window.shell.ErrorImg;
      img.onerror = null;
    },
    goLink(link) {
      this.handleClickTrack({
        infoType: 4,
        filterInfo: {
          linkName: this.getI18nMessage(link.label),
        },
      });
      window.shell.openPage(link.url, 1, 2);
    },
    async execCardMethod(params) {
      return new Promise((resolve, reject) => {
        try {
          window.shell.execCardMethod(params, (data) => {
            resolve(data);
          });
        } catch (e) {
          reject(e);
        }
      });
    },
    getI18nMessage(labels) {
      if (!this.$i18n.locale) {
        return labels[0].langValue;
      }
      const res = labels.filter((label) => label.langKey === this.$i18n.locale);
      const defaultLabel = labels.filter((label) => label.langKey === "zh_CN");
      if (res.length > 0) {
        return res[0].langValue || defaultLabel[0].langValue;
      }
      return defaultLabel[0].langValue;
    },
  },

  async created() {
    // 获取数据
    this.containerLoading = true;
    let res = await this.execCardMethod({
      cardWid: this.router.cardWid,
      cardId: this.router.cardId,
      method: "renderData",
    });
    this.$nextTick(() => {
      this.containerLoading = false;
      this.loadedEndTrack();
    });

    if (res.errcode === "0") {
      let configInfo = res.data.configInfo;
      // let configInfo = mock;
      if (configInfo.linkList == null) {
        configInfo.linkList = [];
      }
      configInfo.linkList = configInfo.linkList.map((item) => {
        return {
          ...item,
          iconUrl:
            process.env.NODE_ENV === "development"
              ? item.iconUrl.split(".cn")[1]
              : item.iconUrl,
        };
      });
      this.configInfo = configInfo;

      this.$set(this.configInfo, "linkList", configInfo.linkList);
      console.log(this.configInfo.linkList);
    }
  },
};
</script>

<style scoped lang="less">
.link-container {
  .link-wrap {
    display: flex;

    &-item {
      flex: 1;
      margin-right: 20px;
      border-radius: 6px;
      border: 1px solid #f0f0f0;
      background: linear-gradient(180deg, #f4f6f9 0%, #fff 49.18%, #fff 100%);
      box-shadow: 8px 8px 20px 0px rgba(29, 64, 127, 0.05);
      &:last-of-type {
        margin-right: 0;
      }
      &-top {
        height: 155px;
        background-size: 100% 100%;
        padding: 20px;
        display: flex;
        flex-direction: column;
        justify-content: center;

        &-title {
          font-size: 24px;
          color: #fff;
          font-weight: 700;
          letter-spacing: 1px;
        }

        .rect-line {
          display: block;
          width: 23px;
          height: 4px;
          border-radius: 4px;
          margin-top: 14px;
          background: rgba(255, 255, 255, 0.45);
        }
      }

      &-bottom {
        padding: 20px;

        .link-item {
          p {
            font-size: 16px;
            letter-spacing: 1px;
            margin-bottom: 18px;
            cursor: pointer;
            display: flex;
            align-items: center;

            &:last-of-type {
              margin-bottom: 0;
            }

            &::before {
              content: "";
              display: block;
              width: 7px;
              height: 7px;
              border-radius: 50%;
              border-width: 2px;
              border-style: solid;
              margin-right: 7px;
            }
          }
        }
      }
    }
  }
}
</style>
