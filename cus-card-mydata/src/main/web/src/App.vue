<template>
  <div class="card__personalData">
    <template v-if="hasLogin">
      <div class="cus-data-box">
        <div class="data-item" @click="goDbrw">
          <div class="data-item-left">
            <div>待办任务</div>
            <p>{{todoNum}}个待办</p>
          </div>
          <img src="./assets/task.png" alt="">
        </div>
        <div class="data-item" v-for="(item,index) in personalInfo" :key="item.wid" @click="handlerEnterClick(item)">
          <div class="data-item-left">
            <div>{{item.title}} <span v-if="item.isEmail == 1 && item.mainInfo != ''" @click="handlerOffEmail(item,index)">解绑</span></div>
            <p>{{item.mainInfo}}</p>
            <p v-if="item.mainInfo == '' && item.isEmail == 1">绑定邮箱</p>
          </div>
          <img src="./assets/ykt.png" v-if="item.isEmail == 1" alt="">
          <img src="./assets/zcxx.png" v-else-if="item.title.indexOf('资产') > -1" alt="">
          <img src="./assets/tsxx.png" v-else-if="item.title.indexOf('图书') > -1" alt="">
          <img v-else src="./assets/task.png" alt="">
        </div>
        <!--<div class="data-item">
          <div class="data-item-left">
            <div>邮件</div>
            <p>1未读邮件</p>
          </div>
          <img src="./assets/ykt.png" alt="">
        </div>
        <div class="data-item">
          <div class="data-item-left">
            <div>资产信息</div>
            <p>0台设备</p>
          </div>
          <img src="./assets/zcxx.png" alt="">
        </div>
        <div class="data-item">
          <div class="data-item-left">
            <div>图书信息</div>
            <p>0本未还</p>
          </div>
          <img src="./assets/tsxx.png" alt="">
        </div>-->
      </div>
      <TBindEmailModal
              v-model="emailConfig.show"
              :options="emailConfig.options"
              @on-ok="refreshEmailData"
              :router="router"
      >
      </TBindEmailModal>
    </template>

    <TNoLogin v-else :lan-fun-name="lanFunName" :router="router"></TNoLogin>
  </div>
</template>

<script>
import TNoLogin from "./components/TNoLogin";
import TBindEmailModal from "./components/TBindEmailModal";
import { initTrack } from "./mixins/track.js";
export default {
  components: {
    TBindEmailModal,
    TNoLogin,
  },
  mixins: [initTrack],
  watch: {
    personalInfo() {
      this.$nextTick(() => {
        let div = document.getElementsByClassName(
          "card__personalData__item_wrapper"
        );
        for (let i = 0; i < div.length; i++) {
          div[i].style.width = `${div[0].clientWidth}px`;
        }
      });
    },
  },
  data() {
    return {
      defaultImg: require("./assets/default.png"),
      isOnEmail: false,
      emailConfig: {
        show: false,
        options: {},
      },
      lanFunName: this.router.cardId,
      hasLogin: window.shell.getUserInfo() ? true : false,
      personalInfo: [],
      height: 220, //容器高度，type为1时该值无效，type为2时最大高度，type为3时固定高度
      type: 0, //0：代表自适应（无最大限制）1：代表自适应（有最大限制）2：固定高度
      errorImg: window.shell.ErrorImg,
      notHasEmail: false,
      loading: false,
      cardData: { showBindMail: 0 },
      getListInterval: {},
      columns: 4,
      todoNum: 0,
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
  },
  mounted() {
    this.getDbrw()
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
    goDbrw() {
      window.shell.openPage('taskList',0,1)
    },
    getDbrw() {
      window.shell.execCardMethod(
              { cardId:'SYS_CARD_TODOTASK', cardWid:'06248737091599699', method: "getMarkNumber" },
              ({ errcode, data }) => {
                if (errcode === "0") {
                  this.todoNum = data ? (data > 99 ? "99+" : data) : 0;
                }
              }
      );
    },
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
          if (this.cardData && this.cardData.columns) {
            this.columns = parseInt(this.cardData.columns);
          }
          this.type = this.cardData.serviceCarHeight?.type || 0;
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
        if(item.mainInfo == '') {
          this.showEmailModal()
          return
        } else {
          let { cardId, cardWid } = this.router;
          let { data } = await window.shell.execCardMethod({
            cardId,
            cardWid,
            method: "getLinkUrl",
            param: { mailAccount: item.extraInfo },
          });
          linkUrl = data;
        }

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
      this.$confirm(
        this.$Lan(this.lanFunName, "textAskIsBindEmail", "是否绑定该邮箱？"),
        this.$Lan(this.lanFunName, "textHasEmail", "检测到已有邮箱") +
          data.account,
        {
          confirmButtonText: this.$Lan(this.lanFunName, "textEnsure", "确 定"),
          cancelButtonText: this.$Lan(this.lanFunName, "textCancel", "取 消"),
          distinguishCancelAndClose: true,
          closeOnClickModal: false,
          confirmButtonClass:
            "portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1",
          cancelButtonClass:
            "bgHoverWhite portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1 ",
          type: "warning",
          center: true,
        }
      )
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
          type: "success",
          message: this.$Lan(this.lanFunName, "textBindSuccess", "绑定成功"),
        });
        this.getAliiInfoByItem(item, index);
      } else {
        this.$message.error({ message: res.data.errmsg });
      }
    },
    handlerOffEmail(item, index) {
      let { cardId, cardWid } = this.router;
      this.handleClickTrack(); //点击埋点
      this.$confirm(
        this.$Lan(
          this.lanFunName,
          "textAfterBindOffWarning",
          "解绑后将无法收到未读邮件提示"
        ),
        this.$Lan(this.lanFunName, "textBindOffWarning", "确认解绑邮箱？"),
        {
          confirmButtonText: this.$Lan(this.lanFunName, "textEnsure", "确 定"),
          cancelButtonText: this.$Lan(this.lanFunName, "textCancel", "取 消"),
          closeOnClickModal: false,
          confirmButtonClass:
            "portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1",
          cancelButtonClass:
            "bgHoverWhite portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1 ",
          type: "warning",
          center: true,
        }
      )
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
            this.$message.error({ message: res.data.errmsg });
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
      this.$set(this.personalInfo, index, data || {});
      console.log(this.personalInfo,23333555)
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
  },
};
</script>

<style lang="less">
  .cus-data-box{
    width: 100%;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    .data-item{
      width: calc(50% - 6px);
      margin-left: 12px;
      height: 67px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      cursor: pointer;
      &:nth-of-type(2n + 1){
        margin-left: 0;
      }
      img{
        margin-left: auto;
        margin-right: 18px;
        width: 44px;
      }
      .data-item-left{
        margin-left: 20px;
        p{
          font-size: 14px;
          line-height: 22px;
          margin-top: 8px;
          color: #fff;
        }
        div{
          font-weight: 700;
          font-size: 16px;
          line-height: 24px;
          color: #fff;
          span{
            font-weight: 400;
            font-size: 14px;
            margin-left: 20px;
          }
        }
      }
      &:nth-of-type(4n + 1){
        background: linear-gradient(270deg, rgba(72, 137, 254, 0.7) 0%, #4889FE 98.44%);
      }
      &:nth-of-type(4n + 2){
        background: linear-gradient(270deg, rgba(122, 202, 109, 0.7) 0%, #7ACA6D 100%);
      }
      &:nth-of-type(4n + 3){
        background: linear-gradient(270deg, rgba(176, 111, 222, 0.7) 0%, #B06FDE 100%);
        margin-top: 12px;
      }
      &:nth-of-type(4n + 4){
        background: linear-gradient(90deg, #FF9C54 0%, rgba(255, 156, 84, 0.7) 100%);
        margin-top: 12px;
      }
    }
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
    text-align: left;
    font-size: 14px;
    color: #8c8c8c;
    letter-spacing: 0;
    line-height: 22px;
  }
  .we-message-box__btns {
    padding: 0 20px 0 20px;
    text-align: right;
  }
}
.portal-primary-backgroundcolor-hover-my:hover {
  background-color: #f0f0f0 !important;
}
.card__personalData {
  min-height: 100px;
  min-width: 360px;
  display: flex;
  .card__personalData__item_title_btn-status {
    display: none;
    margin-right: 6px;
    max-width: 40px;
    line-height: 20px;
    font-size: 12px;
    letter-spacing: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    flex-shrink: 0;
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
  .ml-4 {
    margin-left: 4px;
  }
  .ml-20 {
    margin-left: 20px;
  }
  .mb-20 {
    margin-bottom: 20px;
  }
  .card__personalData__item_wrapper {
    background: #f5f5f5;
    border-radius: 4px;
    min-width: 220px;
    height: 98px;
    padding: 12px 16px 0 16px;
    font-size: 14px;
    display: flex;
    flex-direction: column;
    &:first-child {
      margin-left: 0;
    }
    &:hover .card__personalData__item_title_btn-status {
      display: inline-block;
    }
    .card__personalData__item_title {
      display: flex;
      justify-content: space-between;
      img {
        width: 14px;
        height: 14px;
        margin-right: 8px;
        margin-top: 3px;
      }
      .card__personalData__item_title_text {
        font-size: 12px;
        color: #8c8c8c;
        letter-spacing: 0;
        line-height: 20px;
      }
    }
    .card__personalData__item_body {
      font-weight: bold;
      font-size: 16px;
      color: #262626;
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
  height: 168px;
  display: flex;
  border-radius: 4px;
  margin-bottom: 12px;
  padding: 18px 16px;
  align-items: center;
  background: #f9f9f9 url("./assets/bg.png") no-repeat right center;
  img {
    width: 90px;
    //height: 126px;
  }
  .personal-info_box {
    margin-left: 16px;
    width: calc(100% - 106px);
    h2 {
      color: #262626;
      font-weight: bold;
      font-size: 18px;
      line-height: 26px;
      letter-spacing: 0px;
      text-align: left;
      width: 100%;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    .personal-info_details {
      display: flex;
      flex-direction: column;
      margin-top: 12px;
      color: #8c8c8c;

      font-size: 14px;
      line-height: 22px;
      letter-spacing: 0px;
      text-align: left;
      span {
        margin-top: 2px;
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
}
</style>
