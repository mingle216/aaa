<template>
  <div class="card__personalData">
    <!-- <AutoContainer class="width-full"> -->
    <template v-if="hasLogin">
      <!--个人信息-->
      <div class="personal-info" v-if="personalObjConfig.isDisplay">
        <img
          alt="个人图像"
          class="personal-info_img"
          :src="personalInfoImg || defaultImg"
          @error="handlePersonalError"
        />
        <div class="personal-info_box">
          <h2 class="portal-font-color-lv1">
            {{ personalObj.name }}，{{ $Lan(lanFunName, "hello", "你好") }}~
          </h2>
          <div class="personal-info_details portal-font-color-lv3">
            <span
              v-if="personalObjConfig.infoList.includes(0)"
              class="ellipsis-text"
            >
              {{ personalObj.organizationName || "-" }}</span
            >
            <span v-if="personalObjConfig.infoList.includes(1)">
              {{ personalObj.lastLogTime || "-" }}</span
            >
            <span v-if="personalObjConfig.infoList.includes(2)">{{
              personalObj.lastLogIp || "-"
            }}</span>
          </div>
        </div>
      </div>
      <div class="card__personalData_box" v-if="personalInfo.length">
        <!-- 外层循环 -->
        <div
          v-for="(pItem, pIndex) in showPersonalInfo"
          :key="pItem.index"
          style="display: flex"
          :style="{ 'margin-top': pIndex === 0 ? '0px' : '12px' }"
        >
          <!-- 内层循环 -->
          <div
            class="card__personalData__item_wrapper"
            v-for="(item, index) in pItem.children"
            @touchstart="
              (e) => {
                if (filterClickUrlAble(item)) {
                  let dom = e.currentTarget;
                  dom
                    .getElementsByClassName('card__personalData__item_body')[0]
                    .classList.add('portal-primary-color-lv1');
                }
              }
            "
            @touchend="
              (e) => {
                let dom = e.currentTarget;
                dom
                  .getElementsByClassName('card__personalData__item_body')[0]
                  .classList.remove('portal-primary-color-lv1');
              }
            "
            :key="index"
            :style="{
              width: getItemWidth(pItem.children),
              'margin-right': index === 1 ? '0px' : '12px',
            }"
            :class="[
              filterClickUrlAble(item)
                ? 'portal-primary-backgroundcolor-hover-my cursor-pointer'
                : '',
              hiddenMainInfo[`${item.wid}-${item.extraInfo}`] ? 'pl-22' : '',
            ]"
          >
            <!-- 隐私模式 -->
            <div
              v-if="hiddenMainInfo[`${item.wid}-${item.extraInfo}`]"
              class="hiddenIcon"
              @click.stop="handleHidden(item)"
            >
              <span
                class="iconfont portal-font-color-lv4"
                :class="[
                  hiddenMainInfo[`${item.wid}-${item.extraInfo}`].hidden
                    ? 'icon-yincang'
                    : 'icon-yulan',
                ]"
              ></span>
            </div>
            <div
              v-if="
                !(item.isEmail && (!item.mainInfo || !item.mainInfo.length))
              "
              @click="handlerEnterClick(item)"
            >
              <div class="card__personalData__item_title">
                <img
                  v-if="item.iconUrl"
                  :src="item.iconUrl"
                  @error="handleError"
                  style="flex-shrink: 0"
                />
                <div
                  class="card__personalData__item_title_text overflow-ellipsis portal-font-color-lv3"
                  :title="item.title"
                >
                  {{ item.title }}
                </div>
                <div
                  v-if="item.isEmail && showBindMailFlag"
                  @click.stop="handlerOffEmail(item, index)"
                  class="card__personalData__item_title_btn-status ml-4 cursor-pointer portal-primary-color-lv1 overflow-ellipsis"
                >
                  {{ $Lan(lanFunName, "textBindOff", "解绑") }}
                </div>
                <div
                  v-if="item.isEmail && showBindMailFlag"
                  class="splitLine portal-primary-backgroundcolor-lv1"
                ></div>
                <div
                  v-if="item.isEmail && showBindMailFlag"
                  @click.stop="handlerOnEmail(item, index)"
                  class="card__personalData__item_title_btn-status cursor-pointer portal-primary-color-lv1 overflow-ellipsis"
                >
                  {{ $Lan(lanFunName, "textAddEmail", "新增") }}
                </div>
              </div>
              <TLoading
                class="card__personalData__item_body portal-font-color-lv1"
                :loading="loading || item.loading"
              >
                <div class="width-full overflow-ellipsis">
                  {{
                    (hiddenMainInfo[`${item.wid}-${item.extraInfo}`] || {})
                      .hidden
                      ? hiddenMainInfo[`${item.wid}-${item.extraInfo}`]
                          .originalMainInfo
                      : item.mainInfo
                  }}
                </div>
              </TLoading>
              <div
                class="card__personalData__item_bottom portal-font-color-lv3"
              >
                <TLoading :loading="loading || item.loading">
                  <span
                    class="width-full overflow-ellipsis inline-block"
                    :title="item.subInfo"
                    >{{ item.subInfo }}</span
                  >
                </TLoading>
              </div>
            </div>

            <div
              v-else-if="showBindMailFlag"
              @click.stop="handlerOnEmail(item, index)"
            >
              <div class="card__personalData__item_title">
                <div class="flex items-center width-full overflow-hidden">
                  <img
                    v-if="item.iconUrl"
                    :src="item.iconUrl"
                    @error="handleError"
                  />
                  <span
                    class="card__personalData__item_title_text overflow-ellipsis portal-font-color-lv3"
                    :title="item.title"
                    >{{ item.title }}</span
                  >
                </div>
              </div>
              <div class="card__personalData__item_body">
                <TLoading :loading="loading || item.loading">
                  <div class="cursor-pointer overflow-ellipsis">
                    {{ $Lan(lanFunName, "textBindEmail", "绑定邮箱") }}
                  </div>
                </TLoading>
              </div>
              <div
                class="card__personalData__item_bottom portal-font-color-lv3"
              >
                <TLoading :loading="loading || item.loading">
                  <span
                    class="width-full overflow-ellipsis inline-block"
                    :title="
                      $Lan(
                        lanFunName,
                        'textBindOnWaring',
                        '绑定邮箱可收到未读邮件提醒'
                      )
                    "
                    >{{
                      $Lan(
                        lanFunName,
                        "textBindOnWaring",
                        "绑定邮箱可收到未读邮件提醒"
                      )
                    }}</span
                  >
                </TLoading>
              </div>
            </div>

            <!--不显示解绑绑定功能-->
            <div v-else>
              <div class="card__personalData__item_title">
                <div class="flex items-center width-full overflow-hidden">
                  <img
                    v-if="item.iconUrl"
                    :src="item.iconUrl"
                    @error="handleError"
                  />
                  <span
                    class="card__personalData__item_title_text overflow-ellipsis portal-font-color-lv3"
                    :title="item.title"
                    >{{ item.title }}</span
                  >
                </div>
              </div>
              <div class="card__personalData__item_body overflow-ellipsis">
                <TLoading :loading="loading || item.loading">
                  <span>{{ $Lan(lanFunName, "itemBody", "当前无邮箱") }}</span>
                </TLoading>
              </div>
              <div
                class="card__personalData__item_bottom portal-font-color-lv3"
              >
                <TLoading :loading="loading || item.loading">
                  <span class="width-full overflow-ellipsis inline-block">{{
                    $Lan(lanFunName, "itemBottom", "请联系管理员")
                  }}</span>
                </TLoading>
              </div>
            </div>
          </div>
        </div>
      </div>

      <empty-con
        v-if="!personalObjConfig.isDisplay && !personalInfo.length"
        :tip="$Lan(lanFunName, 'noPersonalData', '暂无相关配置')"
        :height="height"
      ></empty-con>
      <custom-action-sheet
        v-model="emailConfig.show"
        class="dialog1"
        :close-on-click-overlay="false"
      >
        <TBindEmail
          v-if="emailConfig.show"
          @update:show="toggleShow"
          :value="emailConfig.show"
          :options="emailConfig.options"
          @on-ok="refreshEmailData"
          :router="router"
        >
        </TBindEmail>
      </custom-action-sheet>
    </template>

    <TNoLogin v-else :lan-fun-name="lanFunName" :router="router"></TNoLogin>
    <!-- </AutoContainer> -->
  </div>
</template>

<script>
import TLoading from "./components/TLoading";
import TNoLogin from "./components/TNoLogin";
import TBindEmail from "./components/TBindEmail";
import { initTrack } from "./mixins/track.js";
export default {
  components: {
    TLoading,
    TBindEmail,
    TNoLogin,
  },
  mixins: [initTrack],
  data() {
    return {
      defaultImg: require("./assets/default.png"),
      isOnEmail: false,
      emailConfig: {
        show: false,
        options: {},
      },
      lanFunName: this.router.cardId,
      // lg: languages[this.$i18n.locale],
      hasLogin: window.shell.getUserInfo() ? true : false,
      personalInfo: [],
      errorImg: window.shell.ErrorImg,
      notHasEmail: false,
      loading: false,
      cardData: { showBindMail: 0 },
      getListInterval: {},
      columns: 2,
      personalObj: {},
      personalObjConfig: {},
      hiddenMainInfo: {},
    };
  },
  props: {
    index: Number,
    router: Object,
  },
  computed: {
    showPersonalInfo() {
      let arr = [],
        zIndex = Math.ceil(this.personalInfo.length / parseInt(this.columns));
      for (let i = 0; i < zIndex; i++) {
        arr.push({
          index: i,
          children: [],
        });
        for (let j = 0; j < this.personalInfo.length; j++) {
          if (Math.floor(j / parseInt(this.columns)) === i) {
            arr[i].children.push(this.personalInfo[j]);
          }
        }
      }
      return arr;
    },
    showBindMailFlag() {
      return +this.cardData.showBindMail === 1;
    },
    personalInfoImg() {
      const userIcon = this.personalObj && this.personalObj.picUrl;
      return userIcon
        ? /^http(s)?:\/\//.test(userIcon)
          ? userIcon
          : `data:image/png;base64,${userIcon}`
        : this.defaultImg;
    },
  },
  mounted() {
    this.initData();
    this.initPersonalInfo();
    this.getAllInfo();
    this.getListInterval = setInterval(() => {
      this.getAllInfo();
    }, 300000);
  },
  destroyed() {
    clearInterval(this.getListInterval);
  },
  methods: {
    initPersonalInfo() {
      window.shell.execCardMethod(
        {
          ...this.router,
          method: "configuredData",
          param: {
            lang: this.$i18n.locale,
            userAccount: window.shell.getUserInfo()?.userAccount || "",
          },
        },
        (data) => {
          this.personalObj = data.data;
          console.log("configuredData", data);
        }
      );
    },
    initData() {
      window.shell.execCardMethod(
        {
          ...this.router,
          method: "renderData",
        },
        (data) => {
          this.cardData = data.data || {};
          this.personalObjConfig = this.cardData.personalInfo;
        }
      );
    },
    filterClickUrlAble(item) {
      /*邮箱显示解绑和绑定才可以点击*/
      return (
        item.linkUrl || (item.isEmail && +this.cardData.showBindMail === 1)
      );
    },
    handleHidden(el) {
      const widKey = `${el.wid}-${el.extraInfo}`;
      const item = this.hiddenMainInfo[widKey];
      item.hidden = !item.hidden;
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null;
    },
    handlePersonalError(e) {
      let img = e.srcElement;
      img.src = this.defaultImg;
      img.onerror = null;
    },
    refreshEmailData() {
      this.$set(this.emailConfig, "show", false);
      this.getAliiInfoByItem(
        this.emailConfig.options,
        this.emailConfig.options.index
      );
    },
    showEmailModal(params) {
      this.emailConfig = {
        show: true,
        options: { ...params },
      };
    },
    async handlerEnterClick(item) {
      let linkUrl = item.linkUrl;
      this.handleClickTrack(); //点击埋点
      if (item.isEmail) {
        let { cardId, cardWid } = this.router;
        let { data } = await window.shell.execCardMethod({
          cardId,
          cardWid,
          method: "getLinkUrl",
          param: { mailAccount: item.extraInfo },
        });
        linkUrl = data;
      }
      if (linkUrl) {
        window.open(linkUrl, "_blank");
      }
    },
    async handlerOnEmail(item, index) {
      let { cardId, cardWid } = this.router;
      this.handleClickTrack(); //点击埋点
      let { data } = await window.shell.execCardMethod({
        cardId,
        cardWid,
        method: "existsAccount",
        param: { wid: item.wid },
      });
      if (!data) {
        return;
      }
      if (!data.status) {
        this.showEmailModal({ ...item, index });
        return;
      }
      this.$dialog
        .confirm({
          title: this.$Lan(
            this.lanFunName,
            "textAskIsBindEmail",
            "是否绑定该邮箱？"
          ),
          message:
            this.$Lan(this.lanFunName, "textHasEmail", "检测到已有邮箱") +
            data.account,
          confirmButtonText: this.$Lan(this.lanFunName, "textEnsure", "确 定"),
          cancelButtonText: this.$Lan(this.lanFunName, "textCancel", "取 消"),
          distinguishCancelAndClose: true,
          closeOnClickModal: false,
          confirmButtonClass:
            "portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1",
          cancelButtonClass:
            "bgHoverWhite portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1 ",
        })
        .then(() => {
          this.submitOnEmail(data, item, index);
          this.handleClickTrack(); //点击埋点
        })
        .catch((action) => {
          setTimeout(() => {
            if (action === "cancel") {
              this.showEmailModal({ ...item, index });
              this.handleClickTrack(); //点击埋点
            }
          }, 500);
        });
    },
    async submitOnEmail(email, item, index) {
      let { cardId, cardWid } = this.router;
      let res = await window.shell.execCardMethod({
        cardId,
        cardWid,
        method: "bindMail",
        param: {
          directBind: "1",
          mailAccount: email.account,
        },
      });
      if (res.data && +res.data.errcode === 0) {
        window.shell.showMessage({
          message: this.$Lan(this.lanFunName, "textBindSuccess", "绑定成功"),
          type: "success",
        });
        this.getAliiInfoByItem(item, index);
        // this.$message.success({
        //   message: this.$Lan(this.lanFunName, "textBindSuccess", "绑定成功"),
        // });
      } else {
        // this.$message.error({ message: res.data.errmsg });
        window.shell.showMessage({
          message: res.data.errmsg,
          type: "error",
        });
      }
    },
    handlerOffEmail(item, index) {
      let { cardId, cardWid } = this.router;
      this.handleClickTrack(); //点击埋点
      this.$dialog
        .confirm({
          title: this.$Lan(
            this.lanFunName,
            "textBindOffWarning",
            "确认解绑邮箱？"
          ),
          message: this.$Lan(
            this.lanFunName,
            "textAfterBindOffWarning",
            "解绑后将无法收到未读邮件提示"
          ),
          confirmButtonText: this.$Lan(this.lanFunName, "textUnbound", "解绑"),
          cancelButtonText: this.$Lan(this.lanFunName, "textCancel", "取 消"),
          closeOnClickModal: false,
          confirmButtonClass:
            "portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1",
          cancelButtonClass:
            "bgHoverWhite portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1 ",
        })
        .then(async () => {
          this.handleClickTrack(); //点击埋点
          let res = await window.shell.execCardMethod({
            cardId,
            cardWid,
            method: "unBindMail",
            param: {
              wid: item.wid,
              mailAccount: item.extraInfo,
            },
          });
          if (res.data && +res.data.errcode !== 0) {
            // this.$message.error({ message: res.data.errmsg });
            window.shell.showMessage({
              message: res.data.errmsg,
              type: "error",
            });
            return;
          }
          window.shell.showMessage({
            message: this.$Lan(
              this.lanFunName,
              "unbindingSucceeded",
              "解绑成功"
            ),
            type: "success",
          });
          this.getAliiInfoByItem(item, index);
        })
        .catch((action) => {
          if (action === "cancel") {
            this.handleClickTrack(); //点击埋点
          }
        });
    },
    async setPersonDetail(item, index) {
      if (+item.needRetrieve !== 1) {
        this.$set(this.personalInfo, index, item);
        return;
      }
      this.$set(this.personalInfo, index, {
        title: item.title,
        iconUrl: item.iconUrl,
        loading: true,
      });
      let { cardId, cardWid } = this.router;
      let { data } = await window.shell.execCardMethod({
        cardId,
        cardWid,
        method: "getPersonalDataDetail",
        param: { wid: item.wid, extraInfo: item.extraInfo },
      });
      this.$set(this.personalInfo, index, data || []);
    },
    getAliiInfoByItem(item) {
      let { cardId, cardWid } = this.router;
      window.shell.execCardMethod(
        { cardId, cardWid, method: "getPersonalDataList" },
        ({ data }) => {
          let flag = 0;
          data.forEach((el, i) => {
            if (item.wid === el.wid) {
              flag++;
              if (this.personalInfo.length < data.length && flag === 1) {
                this.personalInfo.splice(i, 0, { item });
              } else if (data.length < this.personalInfo.length && flag === 1) {
                this.personalInfo.splice(i, 1);
              }
              this.setPersonDetail(el, i);
            }
          });
        }
      );
    },
    getAllInfo() {
      this.loading = true;
      let { cardId, cardWid } = this.router;
      window.shell.execCardMethod(
        { cardId, cardWid, method: "getPersonalDataList" },
        ({ data }) => {
          this.loading = false;
          data = data || [];

          if (data.length === 0 || this.personalInfo.length !== data.length) {
            this.personalInfo = [];
          }
          data.forEach((el, index) => {
            const widKey = `${el.wid}-${el.extraInfo}`;
            if (
              this.cardData.isHiddenPrivacy &&
              el.isHiddenPrivacy &&
              el.originalMainInfo &&
              !this.hiddenMainInfo[widKey]
            ) {
              // 开启隐私模式
              this.$set(this.hiddenMainInfo, widKey, {
                hidden: true,
                originalMainInfo: el.originalMainInfo.replace(
                  /【(\S*)】/g,
                  " ** "
                ),
              });
            }
            this.setPersonDetail(el, index);
          });
          this.loadedEndTrack(); // 加载结束埋点
        }
      );
    },
    getItemWidth() {
      // return children.length > 1 ? `${100 / this.columns}%` : `calc(${100 / this.columns}% - 6px)`
      return `calc(${100 / this.columns}% - 6px)`;
    },
    toggleShow(val) {
      this.$set(this.emailConfig, "show", val);
    },
  },
};
</script>

<style lang="less">
.ml-12 {
  margin-left: 12px;
}
.ml-4 {
  margin-left: 4px;
}
.ml-20 {
  margin-left: 20px;
}
.mb-20 {
  margin-bottom: 20px;
}
.pl-22 {
  padding-right: 22px !important;
}
.we-message-box--center {
  padding-bottom: 20px !important;
  .we-message-box__header {
    padding: 40px 15px 0 15px;
    .we-message-box__title {
      justify-content: flex-start;
      padding-left: 15px;
      font-size: 16px;
      color: #262626;
      letter-spacing: 0;
      line-height: 24px;
      font-weight: bold;
      .we-message-box__status {
        padding-right: 8px;
      }
    }
    .we-message-box__headerbtn {
      display: none;
    }
  }
  .we-message-box__content {
    padding: 12px 64px 30px 64px;
    text-align: start;
    font-size: 14px;
    color: #8c8c8c;
    letter-spacing: 0;
    line-height: 22px;
  }
  .we-message-box__btns {
    padding: 0 20px 0 20px;
    text-align: end;
  }
}
.portal-primary-backgroundcolor-hover-my:hover {
  background-color: #f0f0f0 !important;
}
.card__personalData {
  min-height: 100px;
  padding: 16px;
  min-width: 330px;
  .card__personalData__item_title_btn-status {
    display: block;
    max-width: 34px;
    line-height: 20px;
    font-size: 12px;
    letter-spacing: 0;
    flex-shrink: 0;
  }
  .splitLine {
    margin: 0 4px;
    width: 1px;
    height: 8px;
  }
  .event-none {
    pointer-events: none;
  }
  .overflow-hidden {
    overflow: hidden;
  }
  .overflow-ellipsis {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .justify-between {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .inline-block {
    display: inline-block;
    white-space: nowrap;
  }
  .flex-1 {
    flex: 1;
    min-width: 0;
  }
  .mx-4 {
    margin: 0 4px;
  }
  .cursor-pointer {
    cursor: pointer;
  }
  .flex {
    display: flex;
  }
  .width-full {
    width: 100%;
  }
  .height-full {
    height: 100%;
  }
  .flex-wrap {
    display: flex;
    flex-wrap: wrap;
  }
  .justify-center {
    justify-content: center;
    display: flex;
  }

  .hiddenIcon {
    background: #eeeeee;
    width: 20px;
    height: 20px;
    position: absolute;
    top: 0;
    right: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 4px;
    .iconfont {
      font-size: 12px;
    }
  }
  .card__personalData__item_wrapper {
    background: #f5f5f5;
    border-radius: 4px;
    height: 98px;
    padding: 12px 12px 0 12px;
    font-size: 14px;
    display: flex;
    flex-direction: column;
    position: relative;
    &:first-child {
      margin-left: 0;
    }
    .card__personalData__item_title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      img {
        width: 14px;
        height: 14px;
        margin-right: 8px;
        margin-top: 3px;
      }
      .card__personalData__item_title_text {
        flex: 1;
        min-width: 0;
        // font-family: MicrosoftYaHei;
        font-size: 12px;
        color: #8c8c8c;
        letter-spacing: 0;
        line-height: 20px;
      }
    }
    .card__personalData__item_body {
      // font-size: 16px;
      // margin-top: 6px;
      // height: 20px;
      // font-weight: 600;
      // font-family: MicrosoftYaHei-Bold;
      font-weight: bold;
      font-size: 16px;
      color: #102645;
      letter-spacing: 0;
      line-height: 24px;
      margin-top: 4px;
      &.lesson__body {
        display: flex;
        align-items: center;
        margin-top: 0;
        height: 100%;
      }
    }
    .card__personalData__item_bottom {
      // margin-top: 3px;
      // line-height: 20px;
      // font-size: 12px;
      // font-family: MicrosoftYaHei;
      font-size: 14px;
      color: #8c8c8c;
      letter-spacing: 0;
      line-height: 22px;
      margin-top: 4px;
    }
  }
}
.bgHoverWhite:hover {
  background-color: white !important;
}

.personal-info {
  width: 100%;
  height: 130px;
  display: flex;
  border-radius: 4px;
  margin-bottom: 12px;
  padding: 18px 16px;
  align-items: center;
  background: #f9f9f9 url("./assets/bg.png") no-repeat right center;
  img {
    width: 66px;
    // height: 95px;
  }
  .personal-info_box {
    margin-left: 16px;
    width: calc(100% - 106px);
    h2 {
      color: #262626;
      font-weight: bold;
      font-size: 17px;
      line-height: 24px;
      letter-spacing: 0px;
      text-align: left;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .personal-info_details {
      display: flex;
      flex-direction: column;
      margin-top: 6px;
      color: #8c8c8c;

      font-size: 14px;
      line-height: 22px;
      letter-spacing: 0px;
      text-align: left;
    }
  }
  .ellipsis-text {
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 100%;
  }
}
.dialog1 {
  height: 380px !important;
}
</style>
