<template>
  <div class="footer portal-font-color-lv1" v-if="isshow">
    <div
      v-if="isEvaluate"
      class="button button-evaluate"
      @click="openSheetEvaluate"
      :class="[ishasService ? 'btnWidth' : 'btnWidthflex']"
    >
      <img :src="evaluteicon" class="evalute-icon" alt />
      <p>{{ $Lan(lanFunName, "evalute", "评价") }}</p>
    </div>
    <div
      v-if="isFavorite"
      class="button button-favorite"
      @click="handleFavorite"
      :class="[ishasService ? 'btnWidth' : 'btnWidthflex']"
    >
      <i
        class="iconfont icon-favorite"
        :class="[
          favorite
            ? 'icon-icon-mianxingshoucang'
            : 'icon-icon-xiankuangxingshoucang',
        ]"
      ></i>
      <!-- <img :src="getFavoriteIcon" class="evalute-icon" alt /> -->
      <p>{{ $Lan(lanFunName, "favorite", "收藏") }}</p>
    </div>

    <we-button
      v-if="linkService.length"
      class="
        button button-online-transaction
        portal-primary-backgroundcolor-lv1
      "
      :disabled="onlineflag"
      @click.stop="openSheetOnline(linkService)"
    >
      {{ $Lan(lanFunName, "handleOnline", "在线办理") }}
    </we-button>

    <customActionSheet
      v-model="oSheetEvaluate.show"
      class="MatterDetail-sheet-evaluate"
      :title="$Lan(lanFunName, 'sheetTitle', '评价事项')"
      @closed="digClose"
    >
      <div class="MatterDetail-content">
        <ul class="evaluate-form portal-font-color-lv1">
          <li class="evaluate-form-item">
            <div class="evaluate-form-item-label ellipsis">
              {{ $Lan(lanFunName, "servicename", "服务事项") }}：
            </div>
            <div class="evaluate-form-item-value ellipsis">
              {{ footData.serviceName }}
            </div>
          </li>
          <li class="evaluate-form-item" v-for="item in appraiseNameNew" :key="item.code">
            <div class="evaluate-form-item-label ellipsis">
              {{ item.name }}：
            </div>
            <div class="evaluate-form-item-value evaluate-form-item-rate">
              <we-rate
                color="#ffd21e"
                v-model="item.score"
                :count="5"
              ></we-rate>
              <div class="rate-content">
                {{ getRateText(item.score) }}
              </div>
            </div>
          </li>
          <!-- <li class="evaluate-form-item">
            <div class="evaluate-form-item-label ellipsis">
              {{
                nameDic["SERVICE_MANNER"] ||
                  $Lan(lanFunName, "serviceManner", "服务态度")
              }}：
            </div>
            <div class="evaluate-form-item-value evaluate-form-item-rate">
              <we-rate
                color="#ffd21e"
                v-model="oSheetEvaluate.form.serviceManner"
                :count="5"
              ></we-rate>
              <div class="rate-content">
                {{ getRateText(oSheetEvaluate.form.serviceManner) }}
              </div>
            </div>
          </li>
          <li class="evaluate-form-item">
            <div class="evaluate-form-item-label ellipsis">
              {{
                nameDic["SERVICE_SPEED"] ||
                  $Lan(lanFunName, "servicesd", "办事速度")
              }}：
            </div>
            <div class="evaluate-form-item-value evaluate-form-item-rate">
              <we-rate
                color="#ffd21e"
                v-model="oSheetEvaluate.form.serviceSpeed"
                :count="5"
              ></we-rate>
              <div class="rate-content">
                {{ getRateText(oSheetEvaluate.form.serviceSpeed) }}
              </div>
            </div>
          </li>
          <li class="evaluate-form-item">
            <div class="evaluate-form-item-label ellipsis">
              {{
                nameDic["EVENT_INTEGRITY"] ||
                  $Lan(lanFunName, "servieinfo", "信息完整度")
              }}：
            </div>
            <div class="evaluate-form-item-value evaluate-form-item-rate">
              <we-rate
                color="#ffd21e"
                v-model="oSheetEvaluate.form.eventIntegrity"
                :count="5"
              ></we-rate>
              <div class="rate-content">
                {{ getRateText(oSheetEvaluate.form.eventIntegrity) }}
              </div>
            </div>
          </li>
          <li class="evaluate-form-item">
            <div class="evaluate-form-item-label ellipsis">
              {{
                nameDic["TOTAL_SATISFY"] ||
                  $Lan(lanFunName, "servicemanyi", "整体满意度")
              }}：
            </div>
            <div class="evaluate-form-item-value evaluate-form-item-rate">
              <we-rate
                color="#ffd21e"
                v-model="oSheetEvaluate.form.totalSatisfy"
                :count="5"
              ></we-rate>
              <div class="rate-content">
                {{ getRateText(oSheetEvaluate.form.totalSatisfy) }}
              </div>
            </div>
          </li> -->
          <li class="evaluate-form-item">
            <div class="evaluate-form-item-label">
              {{ $Lan(lanFunName, "sugger", "意见或建议(选填)") }}：
            </div>
            <textarea
              v-model="oSheetEvaluate.form.suggest"
              maxlength="500"
              class="evaluate-form-item-value evaluate-form-item-suggest"
              :placeholder="$Lan(lanFunName, 'inputplaceholder', '请在此输入…')"
            ></textarea>
            <div class="counter">
              {{ oSheetEvaluate.form.suggest.length }}/500
            </div>
          </li>
        </ul>
        <we-button
          class="evaluate-submit-button portal-primary-backgroundcolor-lv1"
          type="info"
          :disabled="!appraiseNameNew.every(v=> v.score !== 0)"
          @click="submitEvaluate"
        >
          <div class="button-content ellipsis">
            {{ $Lan(lanFunName, "commit", "提交") }}
          </div>
        </we-button>
      </div>
    </customActionSheet>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    footData: {
      type: Object,
      default: () => {
        return {};
      },
    },
    appraiseName: {
      type: Array,
      default: () => {
        return [];
      },
    },
    isLogin: Boolean,
    lanFunName: String,
    loginUrl: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      appraiseNameNew: [],
      evaluteicon: require("../.././public/img/icon-evaluate.png"),
      favoriteicon: require("../.././public/img/icon-favorite.png"),
      unfavoriteicon: require("../.././public/img/icon-unfavorite.png"),
      linkService: [],
      favorite: false,
      isEvaluate: false,
      isFavorite: false,
      ishasService: false,
      oSheetOnline: {
        show: false,
        apps: [],
      },
      oSheetEvaluate: {
        show: false,
        form: {
          serviceManner: 0,
          serviceSpeed: 0,
          totalSatisfy: 0,
          eventIntegrity: 0,
          suggest: "",
        },
      },
      isValid: false,
      canCommit: false,
      onlineflag: false,
      loadingcomplete: {},
      nameDic: {},
    };
  },
  computed: {
    getFavoriteIcon() {
      return this.favorite ? this.favoriteicon : this.unfavoriteicon;
    },
    isshow() {
      return this.isEvaluate || this.isFavorite || this.linkService.length > 0;
    },
  },
  methods: {
    digClose() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName
        },
        startTime: new Date().getTime()
      });
    },
    openSheetEvaluate() {
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
      if (!this.isLogin) {
        console.log("未登录");
        window.shell.login({
          params: {
            localHref: this.loginUrl,
          },
        });
        return;
      }
      this.oSheetEvaluate.show = true;
      //清空所有信息
      Object.assign(
        this.$data.oSheetEvaluate.form,
        this.$options.data().oSheetEvaluate.form
      );
      this.canCommit = false;
    },
    handleFavorite() {
      if (!this.isLogin) {
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
        console.log("未登录");
        window.shell.login({
          params: {
            localHref: this.loginUrl,
          },
        });
        return;
      }

      // if (this.loadingcomplete[this.footData.serviceItemWid]) {
      //   return;
      // }
      // this.loadingcomplete[this.footData.serviceItemWid] = true;
      if (this.favorite) {
        //取消收藏
        this.favorite = false;
      } else {
        this.favorite = true;
      }
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
            infoType: 1,
            itemId: this.footData.serviceItemWid,
            fucType: this.favorite ? 2 : 3,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.collectServiceItem({
        id: this.footData.serviceItemWid, //事项ID 收藏或取消收藏时用
        operate: this.favorite ? 1 : 0, // 收藏操作标识 0：取消收藏 1：收藏
      });
    },
    openSheetOnline() {
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
            infoType: 1,
            itemId: this.footData.serviceItemWid,
            fucType: 1,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.openOnlineDeal({
        ...this.footData,
        wid: this.footData.serviceItemWid,
        name: this.footData.serviceName,
      });
    },
    openAppService(app) {
      // 在线办理跳转服务
      window.shell.openOnlineDeal(
        {
          wid: app.wid,
          name: app.serviceName,
        },
        ""
      );
    },
    handleSheetEvaluateClose() {
      this.oSheetEvaluate.show = false;
    },
    getRateText(rate) {
      switch (rate) {
        case 0:
          return "";
        case 1:
          return this.$Lan(this.lanFunName, "satislevel_1", "非常不满意");
        case 2:
          return this.$Lan(this.lanFunName, "satislevel_2", "不满意");
        case 3:
          return this.$Lan(this.lanFunName, "satislevel_3", "一般");
        case 4:
          return this.$Lan(this.lanFunName, "satislevel_4", "满意");
        case 5:
          return this.$Lan(this.lanFunName, "satislevel_5", "非常满意");
      }
    },
    async submitEvaluate() {
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
            infoType: 1,
            itemId: this.footData.serviceItemWid,
            fucType: 4,
          },
        },
        startTime: new Date().getTime(),
      });
      this.isValid = true;
      if (
        this.appraiseNameNew.some(v=> v.score === 0)
      ) {
        return;
      }
      let param = {
        suggest: this.oSheetEvaluate.form.suggest,
        appraiseDetail: JSON.stringify({
          score: this.appraiseNameNew.map(v=> { return {
            dimenWid: v.code,
            dimenScore: `${v.score}`
          } })
        })
      }
      this.$emit("submitEvaluate", param);
    },
  },
  watch: {
    appraiseName: {
      handler(val) {
        if (val && val.length) {
          this.appraiseNameNew = this.appraiseName.map(v=> { return {
            ...v,
            score: 0
          } });
          let obj = {};
          val.forEach((item) => {
            obj[item.code] = item.name;
          });
          this.nameDic = obj;
        }
      },
      immediate: true,
    },
    footData: {
      handler(dt) {
        console.log("dtdtdt", dt);
        this.config = dt.config;
        this.linkService = dt.linkService;
        this.favorite = dt.favorite;
        this.isEvaluate =
          dt.config.detailConfigure &&
          dt.config.detailConfigure.includes("evaluation")
            ? true
            : false;
        this.isFavorite =
          dt.config.detailConfigure &&
          dt.config.detailConfigure.includes("favourite")
            ? true
            : false;
        this.ishasService = dt.linkService.length > 0 ? true : false;

        // console.log('showOnlineButton',this.footData.showOnlineButton)
        this.onlineflag = this.footData.showOnlineButton == 1 ? true : false;
      },
      deep: true,
    },
    "oSheetEvaluate.form": {
      handler(val) {
        if (
          val.serviceManner &&
          val.serviceSpeed &&
          val.totalSatisfy &&
          val.eventIntegrity
        ) {
          this.canCommit = true;
        }
      },
      deep: true,
    },
  },
};
</script>

<style lang="less" scoped>
/deep/ .we-action-sheet__content {
  height: 100%;
  overflow: hidden;
}
.footer {
  // position: absolute;
  width: 100%;
  position: fixed;
  left: 0;
  bottom: 0;
  display: flex;
  // margin-top: 40px;
  height: 56px;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
  padding: 8px 16px;
  .evalute-icon {
    width: 22px;
    margin-bottom: 2px;
  }
  .button {
    height: 100%;
    border: none;
    text-align: center;
  }

  .btnWidth {
    width: 75px;
  }
  .btnWidthflex {
    flex: 1;
  }

  .button-evaluate {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    //color: #102645;
    font-size: 12px;

    // font-weight: 600;
    .van-icon {
      font-size: 20px;
      font-weight: 500;
      margin-bottom: 4px;
    }
  }

  .button-favorite {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    //color: #102645;
    font-size: 12px;
    .icon-icon-mianxingshoucang {
      color: rgb(255, 210, 30);
    }
    .icon-icon-xiankuangxingshoucang {
      color: #707d8f;
    }

    .icon-favorite {
      font-size: 20px;
      margin-bottom: 2px;
    }
  }
  .button-online-transaction {
    flex: 1;
    border-radius: 4px;
  }
  .button-online-transaction,
  .button-urge-online {
    // width: calc(100% - 95px);
    font-size: 16px;
    color: #ffffff;
    //background-color: #307afb;
  }

  .button-offline-transaction,
  .is-urge-online {
    // width: calc(100% - 95px);
    font-size: 16px;
    color: #9fa8b5;
    background-color: #d7dde7;
    border-radius: 0;
    pointer-events: none;
  }
}
.app {
  display: flex;
  align-items: center;
  margin-left: 12px;
  padding: 10px 12px;
  box-shadow: inset 0 -1px 0 0 #f1f2f4;
  font-size: 14px;

  &:last-child {
    box-shadow: none;
  }

  &-img {
    width: 36px;
    height: 36px;
    border-radius: 4px;
    flex-shrink: 0;
  }

  &-name {
    color: #102645;
    margin-left: 12px;
  }

  &-icon {
    font-size: 18px;
    margin-left: auto;
    color: #102645;
  }
}
.MatterDetail-sheet-evaluate {
  max-height: 100%;
  // height: auto;
  .evaluate-form {
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    padding: 16px;
    &-item {
      margin-top: 20px;
      display: flex;
      font-size: 16px;

      &:first-child {
        margin-top: 16px;

        .evaluate-form-item-value {
          display: block;
          color: #102645;
        }
      }

      &-label {
        color: #102645;
        font-size: 16px;
        width: 98px;
        flex-shrink: 0;
        bottom: -2px;
      }

      &-value {
        position: relative;
        display: flex;
        width: 100px;
        flex-grow: 1;
        align-items: center;
        color: #707d8f;
        margin-left: 12px;

        // >>> .van-rate__icon {
        //   font-size: 16px;
        // }

        span {
          margin-left: 16px;
        }

        .empty-tip {
          position: absolute;
          top: 20px;
          color: #ed4014;
          height: 24px;
          font-size: 12px;
          padding-top: 2px;
          margin-left: 0;
        }
      }
      &-rate {
        top: -1px;
      }

      &:last-child {
        flex-grow: 1;
        margin-top: 24px;
        padding-top: 16px;
        border-top: 1px dotted #d7dde7;
        position: relative;
        display: flex;
        flex-direction: column;

        .evaluate-form-item-label {
          width: 100%;
        }

        .evaluate-form-item-value {
          display: block;
          flex-grow: 1;
          width: 100%;
          margin-left: 0;
          border: 1px solid #d7dde7;
          border-bottom: none;
          border-top-left-radius: 4px;
          border-top-right-radius: 4px;
          margin-top: 8px;
          padding: 12px;
          font-size: 16px;
          color: #102645;
          resize: none;
        }
        .evaluate-form-item-suggest {
          height: 80px;
        }

        .counter {
          padding: 4px 12px 6px 0;
          color: #9fa8b5;
          text-align: right;
          border: 1px solid #d7dde7;
          border-top: none;
          border-bottom-left-radius: 4px;
          border-bottom-right-radius: 4px;
        }
      }
    }
  }
  .rate-content {
    padding-left: 20px;
    width: 120px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .button-content {
    width: 340px;
    padding-left: 20px;
    padding-right: 20px;
  }
  .evaluate-submit-button {
    width: calc(100% - 32px);
    min-height: 48px;
    font-size: 16px;
    margin: 4px 0 16px 16px;
  }
  /deep/.we-button--info {
    border: 0 solid #1989fa !important;
  }
  .MatterDetail-title {
    font-size: 18px;
    height: 56px;
    box-shadow: 1px 1px 1px #e7edf1;
    display: flex;
    align-items: center;
    padding-left: 16px;
    h3 {
      font-weight: bold;
    }
    .close {
      margin-left: auto;
      margin-right: 16px;
      font-size: 22px;
      color: #c1bfbf;
    }
  }
  .MatterDetail-content {
    // height: calc(100% - 56px);
    height: 100%;
    max-height: 486px;
    overflow: auto;
  }
}
</style>
