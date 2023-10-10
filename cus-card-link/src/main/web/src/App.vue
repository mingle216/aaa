<template>
  <div
    class="link-container"
    :class="[
      { 'home-banner': cardType == '0' },
      { 'school-life': cardType == '1' },
      { 'service-bus': cardType == '2' },
    ]"
    v-loading="containerLoading"
    :style="
      cardType == '0'
        ? 'margin-left: -20px;width: calc(100% + 20px)'
        : cardType == '1'
        ? 'margin-bottom: 110px'
        : ''
    "
  >
    <EmptyCon
      v-if="configInfo.linkList && configInfo.linkList.length === 0"
      :tip="$Lan('CUS_CARD_LINK', 'noData', '暂无链接导航')"
      :height="configInfo.linkHeight.value"
    />
    <template v-else>
      <AutoContainer
        :con-type="configInfo.linkHeight.type"
        :con-height="configInfo.linkHeight.value"
      >
        <div ref="fatherLinkContainer">
          <div
            class="link-row"
            v-for="(configList, configIndex) in showDatas"
            :key="configIndex"
          >
            <!--图标在上-->
            <template
              v-if="
                configInfo.linkDisplayRadio === 2 && +configInfo.linkImgShowRadio === 1
              "
            >
              <div
                v-for="(link, index) in configList"
                :key="index"
                class="link-div icon-up"
                :style="getTopBottomStyle()"
              >
                <div @click="goLink(link)" class="link-inner-div">
                  <div class="link-icon-font-out">
                    <template v-if="link.iconType === '1'">
                      <img :src="link.iconUrl" @error="imgError" class="link-icon-img" />
                    </template>
                    <template v-else>
                      <i
                        :class="link.iconUrl + ' ' + link.iconClass"
                        class="link-icon-font iconfont"
                      ></i>
                    </template>
                  </div>
                  <div style="font-size: 16px" class="link-text">
                    <v-clamp autoresize :max-lines="2">
                      {{ getI18nMessage(link.label) }}
                    </v-clamp>
                  </div>
                </div>
              </div>
              <div
                v-if="showMore && configIndex == rowNum - 1"
                class="link-div icon-up"
                :style="getTopBottomStyle()"
              >
                <div @click="showMore = false" class="link-inner-div">
                  <div class="link-icon-font-out">
                    <img
                      :src="require('./assets/more1.png')"
                      @error="imgError"
                      class="link-icon-img"
                      v-if="cardType == 0"
                    />
                    <img
                      :src="require('./assets/more2.png')"
                      @error="imgError"
                      class="link-icon-img"
                      v-else-if="cardType == 1"
                    />
                    <img
                      :src="require('./assets/more3.png')"
                      @error="imgError"
                      class="link-icon-img"
                      v-else
                    />
                  </div>
                  <div style="font-size: 16px" class="link-text">
                    <v-clamp autoresize :max-lines="2">
                      {{ $Lan(lanFunName, "more2", "查看更多") }}
                    </v-clamp>
                  </div>
                </div>
              </div>
            </template>
            <!-- 名称在上 -->
            <template
              v-else-if="
                configInfo.linkDisplayRadio === 1 && +configInfo.linkImgShowRadio === 1
              "
            >
              <div
                v-for="(link, index) in configList"
                :key="index"
                class="link-div name-up"
                :style="getTopBottomStyle()"
              >
                <div @click="goLink(link)" class="link-inner-div">
                  <div style="font-size: 16px" class="link-text">
                    <!--<div class="text">-->
                    <v-clamp autoresize :max-lines="1">
                      {{ getI18nMessage(link.label) }}
                    </v-clamp>
                    <!--</div>-->
                  </div>
                  <div class="link-icon-font-out">
                    <template v-if="link.iconType === '1'">
                      <img :src="link.iconUrl" @error="imgError" class="link-icon-img" />
                    </template>
                    <template v-else>
                      <i
                        :class="link.iconUrl + ' ' + link.iconClass"
                        class="link-icon-font iconfont"
                      ></i>
                    </template>
                  </div>
                </div>
              </div>
              <div
                v-if="showMore && configIndex == rowNum - 1"
                class="link-div name-up"
                :style="getTopBottomStyle()"
              >
                <div @click="showMore = false" class="link-inner-div">
                  <div style="font-size: 16px" class="link-text">
                    <v-clamp autoresize :max-lines="2">
                      {{ $Lan(lanFunName, "more1", "更多") }}
                    </v-clamp>
                  </div>
                  <div class="link-icon-font-out">
                    <img
                      :src="require('./assets/more1.png')"
                      @error="imgError"
                      class="link-icon-img"
                      v-if="cardType == 0"
                    />
                    <img
                      :src="require('./assets/more2.png')"
                      @error="imgError"
                      class="link-icon-img"
                      v-else-if="cardType == 1"
                    />
                    <img
                      :src="require('./assets/more3.png')"
                      @error="imgError"
                      class="link-icon-img"
                      v-else
                    />
                  </div>
                </div>
              </div>
            </template>

            <!--左右布局-->
            <template v-else>
              <div
                v-for="(link, index) in configList"
                :key="index"
                class="link-div-inline"
                :style="{
                  width: linkWidth,
                  'min-width': +configInfo.linkImgShowRadio === 1 ? '145px' : '140px',
                }"
              >
                <div @click="goLink(link)" class="link-inner-div">
                  <div class="link-icon-col" v-if="+configInfo.linkImgShowRadio === 1">
                    <template v-if="link.iconType === '1'">
                      <img
                        :src="link.iconUrl"
                        @error="imgError"
                        class="link-icon-img-inline"
                      />
                    </template>
                    <template v-else>
                      <i
                        :class="link.iconUrl + ' ' + link.iconClass"
                        class="link-icon-font-inline iconfont"
                      ></i>
                    </template>
                  </div>
                  <div v-else class="card-link-io-sign-box" :class="link.bgClass"></div>
                  <div class="link-text-inline" :class="link.fontClass + ' '">
                    <div class="link-text-inner-inline">
                      <span>
                        {{ getI18nMessage(link.label) }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </div>
        </div>
      </AutoContainer>
    </template>
    <group-modal
      :linkDisplayRadio="configInfo.linkDisplayRadio"
      :linkImgShowRadio="configInfo.linkImgShowRadio"
      :router="router"
      ref="GroupModal"
    ></group-modal>
  </div>
</template>

<script>
import GroupModal from "./components/groupModal";
import mock from "./mock.js";
import TrackMixin from "./mixins/track.js";
export default {
  name: "CUS_CARD_LINK",
  props: {
    index: Number,
    router: Object,
    colWidth: Number,
  },
  mixins: [TrackMixin],
  components: {
    GroupModal,
  },
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
      cardType: "1",
      rowNum: "2",
      showMore: false,
      lanFunName: this.router.cardId,
    };
  },
  computed: {
    showDatas() {
      // const totalNum = this.configInfo?.linkList?.length;
      const configColumns = Number(this.configInfo?.linkDisplayNumSelect);
      const configRows = Number(this.rowNum) || 2;
      // const arrayNum = Math.ceil(totalNum / configColumns);
      if (this.showMore) {
        let lastArr = [];
        let tempArr = this.configInfo.linkList;
        for (let i = 0; i < configRows; i++) {
          lastArr[i] = tempArr.slice(i * configColumns, (i + 1) * configColumns);
          if (i == configRows - 1) {
            lastArr[i] = lastArr[i].slice(0, lastArr[i].length - 1);
          }
        }
        return lastArr;
      }
      return this.datas;
    },
  },
  methods: {
    getTopBottomStyle() {
      let style =
        "width:" +
        Math.round((1 / Number(this.configInfo.linkDisplayNumSelect)) * 10000) / 100.0 +
        "%;min-width:100px;";
      return style;
    },
    getListWidth() {
      this.linkWidth = `${100 / parseInt(this.configInfo.linkDisplayNumSelect)}%`;
    },

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
      link.disabled = false;
      if (link.type === "group") {
        //导航分组
        this.$refs.GroupModal.show(link);
      } else {
        window.shell.openPage(link.url, 1, 2);
      }

      link.disabled = true;
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
    getRightLeftStyle() {
      const width = this.$refs.fatherLinkContainer.offsetWidth;
      const totalNum = this.configInfo.linkList.length;
      if (totalNum === 0) {
        return;
      }
      if (width > 250 * Number(this.configInfo.linkDisplayNumSelect)) {
        //一行最大列数
        let oneLineMaxColumns = Math.floor(width / 250);
        const configColumns = Number(this.configInfo.linkDisplayNumSelect);
        if (oneLineMaxColumns > configColumns) {
          oneLineMaxColumns = configColumns;
        }
        if (totalNum > oneLineMaxColumns) {
          const margin = Math.floor(
            (width - 250 * oneLineMaxColumns) / oneLineMaxColumns
          );
          this.leftRightStyle = "margin-right:" + margin + "px";
        } else {
          const margin = Math.floor((width - 250 * totalNum) / totalNum);
          this.leftRightStyle = "margin-right:" + margin + "px";
        }
      } else {
        this.leftRightStyle = "";
      }
    },
    //将数据按照每行展示个数拆分成多个list
    formatData() {
      const totalNum = this.configInfo.linkList.length;
      const configColumns = Number(this.configInfo.linkDisplayNumSelect);
      const configRows = Number(this.rowNum) || 2;
      const arrayNum = Math.ceil(totalNum / configColumns);
      if (arrayNum > configRows) {
        this.showMore = true;
      }
      for (let i = 0; i < arrayNum; i++) {
        this.datas[i] = this.configInfo.linkList.slice(
          i * configColumns,
          (i + 1) * configColumns
        );
      }
    },
  },

  async created() {
    // 获取数据
    this.containerLoading = true;
    let resp = await window.shell.getCardConfig(this.router.cardId, this.router.cardWid);
    if (resp.errcode === "0") {
      let config = JSON.parse(JSON.parse(resp.data));
      this.cardType = config.cardType;
      this.rowNum = config.rowNum;
    }
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
      configInfo.linkList.forEach((link) => {
        link.fontClass = "portal-font-color-lv1";
        link.iconClass = "";
        link.borderClass = "";
        link.disabled = true;
        link.iconUrl =
          process.env.NODE_ENV === "development"
            ? link.iconUrl.split(".cn")[1]
            : link.iconUrl;
      });
      configInfo.linkList = configInfo.linkList.filter(
        (item) => !item.type || (item.type === "group" && item.linkList?.length)
      );
      this.configInfo = configInfo;

      this.$set(this.configInfo, "linkList", configInfo.linkList);
      this.formatData();
      this.$nextTick(() => {
        //  在dom渲染完成后调整左右间距
        if (configInfo.linkList.length > 1) {
          this.getRightLeftStyle();
          this.getListWidth();
        }
      });
    }
  },
};
</script>

<style scoped lang="less">
.link-container {
  width: 100%;
  background: #fff;
  .card-link-io-sign-box {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    margin-right: 6px;
    // margin-left: 12px;
    margin-top: 10px;
    background-color: #bfbfbf;
  }
  .link-row {
    display: flex;
    flex-wrap: nowrap;
    width: 100%;
    align-items: center;

    .link-div {
      display: flex;
      justify-content: center;
      align-items: center;
      color: #212a39;
      padding: 12px;

      .link-inner-div {
        cursor: pointer;
        display: flex;
        flex-direction: column;
        /*justify-content: center;*/
        align-items: center;
        width: 100%;

        .link-icon-font-out {
          width: 48px;
          height: 48px;
          display: flex;
          justify-content: center;
          align-items: center;

          .link-icon-font {
            background-color: transparent;
            font-size: 48px;
            color: #b3b8c2;
          }

          .link-icon-img {
            width: 100%;
            height: 100%;
          }
        }

        .link-text {
          width: 100%;
          font-size: 14px;
          display: -webkit-box;
          overflow: hidden;
          text-align: center;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          text-overflow: ellipsis;
          word-break: break-all;

          div {
            line-height: 20px;
          }
        }

        .ie-two-row {
          line-height: 2;
          height: 55px;
        }

        .two-row {
          height: 40px;
        }
      }
    }

    .name-up {
      .link-icon-font-out {
        margin-top: 10px;
      }
    }

    .icon-up {
      .link-icon-font-out {
        margin-bottom: 10px;
      }
    }

    .link-div-inline {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      color: #9aa1b0;
      // width: 246px;
      flex-shrink: 0;
      min-width: 224px;
      // flex: 1;

      .link-inner-div {
        cursor: pointer;
        display: flex;
        justify-content: flex-start;
        // align-items: center;
        height: 72px;
        width: 100%;
        padding: 0 12px;
        /*padding: 0 0 0 10px;*/

        .link-icon-font-inline {
          // margin-bottom: 15px;
          font-size: 20px;

          color: #b3b8c2;
        }

        .link-icon-col {
          // padding: 0 12px;
          // margin-left: 12px;
          margin-right: 8px;
          // margin-top: 12px;
          width: 24px;
          height: 24px;
          line-height: 24px;
          text-align: center;

          .link-icon-img-inline {
            width: 100%;
            height: 100%;
          }
        }

        .link-text-inline {
          // width: 186px;
          /*padding-right: 12px;*/
          // display: flex;
          // justify-content: flex-start;
          // align-items: center;
          // height: 20px;
          // line-height: 20px;
          // padding-top: 8.5px;
          flex: 1;
          overflow: hidden;

          .link-text-inner-inline {
            width: 100%;
            font-size: 16px;
            // white-space: nowrap;
            // overflow: hidden;
            // text-overflow: ellipsis;
            // color: #262626;
            line-height: 24px;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
          }
        }
      }
    }
  }
}

.home-banner {
  .link-div {
    height: 136px;
    border-right: 2px solid #f3f5f9;
    position: relative;

    &:hover {
      box-shadow: 0 10px 20px 0 #ecedf4;
      &::before {
        content: "";
        display: block;
        width: 100%;
        height: 4px;
        position: absolute;
        top: 0;
        left: 0;
      }
    }
  }

  .link-row {
    border-bottom: 2px solid #f3f5f9;

    &:last-of-type {
      border: none;
    }
  }
}

.school-life {
  .link-div {
    height: 144px;
    position: relative;
    border-right: 1px solid #f3f5f9;
    border-bottom: 1px solid #f3f5f9;
    &:hover {
      box-shadow: 0 10px 20px 0 #ecedf4;
      &::before {
        content: "";
        display: block;
        width: 100%;
        height: 4px;
        position: absolute;
        top: 0;
        left: 0;
      }
    }
  }
  .link-row .link-div:first-of-type {
    border-left: 1px solid #f3f5f9;
  }
}

.service-bus {
  border-radius: 8px;
  border: 1px solid #dee7fc;
  background: #fff;
  .link-div {
    height: 127px;
  }
}

.Sideline_Lv1 {
  color: #d6dad4;
}
</style>
