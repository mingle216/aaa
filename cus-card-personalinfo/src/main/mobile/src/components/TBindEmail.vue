<template>
  <div class="tbind-email">
    <div class="all-content">
      <div class="we-action-sheet__header">
        {{
            $Lan(lanFunName, 'textBindEmail', '绑定邮箱')
          }}<i @click="popClose" class="we-icon we-icon-cross we-action-sheet__close">
        </i>
      </div>
      <div class="form-content">

        <we-form>
          <div class="form-item">
            <div class="form-title">{{$Lan(lanFunName, 'textEmailAccount', '邮箱账号：')}}</div>
            <div>
              <input v-model="formData.mailAccount" class="form-item-value email-input"
                :placeholder="$Lan(lanFunName, 'textEmailAccountWarning', '请输入邮箱账号')" @blur="verifyAccount" />
              <div ref="emailSuffix" class="card__personalData__email__suffix">
                <div v-if="emailSuffixList.length > 1">
                  <div class="cursor-pointer justify-between card__personalData__email__suffix__box" @click="handlerToggleEmailSuffix(true, true)">
                    <span class="card__personalData__email__suffix__text">{{ formData.emailSuffix }}</span>
                   <img alt="图像" :src="downSrcImg" class="down-img"/>
                  </div>
                  <div v-if="showChooseEmail" class="card__personalData__email__drop">
                    <div v-for="(item, index) in emailSuffixList" :key="index" @click="handlerChooseEmailSuffix(item)"
                      :class="{
                        card__personalData__email__drop__item: true,
                        'portal-primary-backgroundcolor-hover-lv5': true,
                        'portal-primary-color-lv1':
                          formData.emailSuffix === item,
                      }">
                      {{ item }}
                    </div>
                  </div>
                </div>
                <div v-else>
                  <span style="padding:0 8px">{{ formData.emailSuffix || "-" }}</span>
                </div>
              </div>
              <div class="errortip">{{emailErrorTip}}</div>
            </div>
          </div>
          <div class="form-item">
            <div class="form-title">{{$Lan(lanFunName, 'textEmailCode', '邮箱验证码：')}}</div>
            <div>
              <input v-model="formData.code" class="form-item-value code-input" @blur="verifyCode" :placeholder="
              $Lan(lanFunName, 'textEmailCodeWarning', '请输入邮箱验证码')" />
              <div class="send-btn">
                <div v-if="!loadingCode" :class="{
                  'portal-primary-color-lv1': canSendCode,
                  'portal-font-color-lv2': !canSendCode,
                  'event-none': !canSendCode,
                }" class="portal-primary-color-lv1 cursor-pointer truncate-text" @click="validationEmail">
                  {{ $Lan(lanFunName, "textGetCode", "获取验证码") }}
                </div>
                <div v-else class="portal-font-color-lv2 truncate-text">
                  {{ $Lan(lanFunName, "textResend", "重新发送") }}({{
                  timeCount
                }})
                </div>
              </div>
            </div>
            <div class="errortip">{{codeErrorTip}}</div>
          </div>

        </we-form>
        <we-button class="confirm portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
          :style="{ opacity: formData.mailAccount == '' ? '0.7' : '1' }" :disabled="formData.mailAccount == ''"
          type="primary" @click="handlerSubmit">{{ $Lan(lanFunName, "confirmEnsure", "确认绑定") }}</we-button>
      </div>

    </div>

  </div>
</template>

<script>
import { clickTrack } from '../mixins/track.js';
export default {
  props: {
    value: {
      type: Boolean,
      default: true,
    },
    router: Object,
  },
  mixins: [clickTrack],
  computed: {
    canSendCode () {
      return (
        this.formData.mailAccount &&
        this.formData.mailAccount.length > 0 &&
        this.emailValidate
      );
    },
  },
  data () {
    return {
      formData: {
        mailAccount: "",
        code: "",
      },
      lanFunName: this.router.cardId,
      unCorrectCode: false,
      loadingCode: false,
      timeCount: 60,
      emailValidate: true,
      showChooseEmail: false,
      validateResult: false,
      dynamicCode: "",
      errCode: 0,
      // lg: languages[this.$i18n.locale],
      // rules: {
      //   mailAccount: [
      //     { validator: validateEmail, trigger: ["blur", "change"] },
      //   ],
      //   code: [{ validator: validateCode, trigger: ["blur", "change"] }],
      // },
      emailSuffixList: [],
      emailErrorTip: "",
      codeErrorTip: "",
      downSrcImg:require("../assets/down.png")
    }
  },
  mounted () {
    this.getEmailSuffixList();
    document.addEventListener("touchend", (e) => {
      if (this.$refs.emailSuffix) {
        let isSelf = this.$refs.emailSuffix.contains(e.target);
        if (!isSelf) {
          this.handlerToggleEmailSuffix(false);
        }
      }
    });
  },
  methods: {
    popClose () {
      this.$emit('update:show', false)
    },
    getEmailSuffixList () {
      let { cardId, cardWid } = this.router;
      window.shell.execCardMethod(
        { cardId, cardWid, method: "getMailSuffix" },
        ({ data }) => {
          this.emailSuffixList = data || [];
          if (this.emailSuffixList.length >= 1 && !this.formData.emailSuffix) {
            let defaultObj = null;
            this.emailSuffixList.forEach((el) => {
              if (el.indexOf("@stu") === -1 && !defaultObj) {
                defaultObj = el;
              }
            });
            this.$set(
              this.formData,
              "emailSuffix",
              defaultObj ? defaultObj : this.emailSuffixList[0]
            );
          }
        }
      );
    },
    handlerToggleEmailSuffix (val, needTrack) {
      this.showChooseEmail = val;
      needTrack && this.handleClickTrack(); //点击埋点
    },
    handlerChooseEmailSuffix (item) {
      this.$set(this.formData, "emailSuffix", item);
      this.handlerToggleEmailSuffix(false, true);
    },
    reset () {
      //绑定成功后重置状态信息
      this.timeCount = 60;
      this.unCorrectCode = false;
      this.emailValidate = true;
      this.formData = {
        mailAccount: "",
        code: "",
      };
      this.showChooseEmail = false;
      this.loadingCode = false;
      clearInterval(this.timeInterval);
      // this.$refs.emailForm.resetFields();
      this.dynamicCode = "";
    },
    async getCode () {
      let { cardId, cardWid } = this.router;
      let { data } = await window.shell.execCardMethod({
        cardId,
        cardWid,
        method: "sendVerificationMessage",
        param: {
          mailAccount: this.formData.mailAccount + this.formData.emailSuffix,
        },
      });
      this.errCode = parseInt(data.errcode || -1);
      if (data && +data.errcode === 0) {
        this.dynamicCode = data || "";
      } else {
        // this.$message.error({ message: data.errmsg });
        window.shell.showMessage({
          message: data.errmsg,
          type: "error",
        });
      }
    },
    async handlerGetCode () {
      await this.getCode();
      if (this.errCode === 0) {
        this.loadingCode = true;
        this.timeCount = 60;
        this.timeInterval = setInterval(() => {
          if (this.timeCount > 0) {
            this.timeCount--;
          } else {
            this.loadingCode = false;
            clearInterval(this.timeInterval);
          }
        }, 1000);
      }
    },
    async validationEmail () {
      this.handleClickTrack(); //点击埋点
      if (!this.canSendCode) return;
      let { cardId, cardWid } = this.router
      let res = await window.shell.execCardMethod(
        {
          cardId,
          cardWid,
          method: "hasAccountBound",
          param: {
            mailAccount: this.formData.mailAccount + this.formData.emailSuffix
          }
        })
      this.emailValidate = false
      if (res && +res.errcode === 0) {
        this.validateResult = res.data
      } else {
        // this.$message.error({ message: res.errmsg })
        window.shell.showMessage({
          message: res.errmsg,
          type: "error",
        });
      }
      if (res.data) {
        this.validateResult = true
        this.verifyAccount()
        this.emailValidate = false
        // await this.$refs.emailForm.validateField("mailAccount", () => {
        //   this.emailValidate = false
        // })
        this.validateResult = false
      } else { this.handlerGetCode() }
    },
    async submitOnEmail () {
      let { cardId, cardWid } = this.router;
      let res = await window.shell.execCardMethod({
        cardId,
        cardWid,
        method: "bindMail",
        param: {
          directBind: "0",
          mailAccount: this.formData.mailAccount + this.formData.emailSuffix,
          code: this.formData.code,
        },
      });
      let errorCode = res.data && +res.data.errcode;
      this.unCorrectCode = false;
      if (errorCode === 0) {
        window.shell.showMessage({
          message: this.$Lan(this.lanFunName, "textBindSuccess", "绑定成功"),
          type: "success",
        });
        this.$emit("on-ok");
        this.reset();
      } else if (errorCode === 402) {
        this.unCorrectCode = true;
        this.verifyCode()
        // await this.$refs.emailForm.validate(() => {
        this.unCorrectCode = false;
        // });
      } else {
       // this.$message.error({ message: res.data.errmsg });
          window.shell.showMessage({
          message: res.data.errmsg,
          type: "error",
        });
      }
    },
    handlerSubmit () {
      this.handleClickTrack(); //点击埋点
      this.verifyAccount();
      this.verifyCode()
      if (!this.emailErrorTip && !this.codeErrorTip) {
        this.submitOnEmail();
      }

    },
    verifyAccount () {
      this.validateEmail()
    },
    validateEmail () {
      let value = this.formData.mailAccount
      var reg = /^[a-zA-Z0-9_.-]+$/;
      if (value.trim() === "") {
        this.emailValidate = false;
        this.emailErrorTip = this.$Lan(
          this.lanFunName,
          "textEmailAccountWarning",
          "请输入邮箱账号"
        )
      } else if (!reg.test(value) || value.length > 256) {
        this.emailValidate = false;
        this.emailErrorTip = this.$Lan(this.lanFunName, "textIncorrectEmail", "邮箱输入错误！")

      } else if (this.validateResult) {
        this.emailValidate = false
        this.emailErrorTip = this.$Lan(this.lanFunName, "textEmailExisted", "邮箱账号已存在")

      } else {
        this.emailValidate = true;
        this.emailErrorTip = "";

      }

    },
    verifyCode () {
      this.validateCode()
    },
    validateCode () {
      let value = this.formData.code
      if (value.trim() === "") {
        this.unCorrectCode = false;
        this.codeErrorTip = this.$Lan(
          this.lanFunName,
          "textEmailCodeWarning",
          "请输入邮箱验证码"
        )

      } else if (value.length > 6) {
        this.unCorrectCode = false;
        this.codeErrorTip = this.$Lan(
          this.lanFunName,
          "textCodeLength",
          "验证码输入长度不超过六位!"
        )

      } else if (this.unCorrectCode) {
        console.log(this.unCorrectCode, 2324);
        this.codeErrorTip = this.$Lan(this.lanFunName, "textIncorrectCode", "验证码输入错误！")

      } else if (this.validateResult) {
        this.emailValidate = false
        this.codeErrorTip = this.$Lan(this.lanFunName, "textEmailExisted", "邮箱账号已存在")

      } else {
        this.unCorrectCode = false;
        this.codeErrorTip = ""
      }

    }
  },


}
</script>

<style lang="less" scoped>
.tbind-email {
  width: 100%;
  // height: 180px;
  // overflow: hidden;
  // display: flex;
  // justify-content: center;
  // align-items: center;

  .card__personalData__email__suffix {
    max-width: calc(100% - 160px);
    height: 42px;
    position: absolute;
    right: 0;
    top: 0;
    background: #f1f2f4;
    border: 1px solid #d7dde7;
    box-sizing: border-box;
    border-radius: 0px 4px 4px 0px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  /deep/ .we-input-group {
    margin-top: 8px;
  }
  /deep/ .we-form-item {
    margin-bottom: 24px;
  }
  /deep/ .we-input__suffix {
    right: 12px;
  }
  /deep/ .we-button {
    padding: 10px 20px;
  }

  .we-input__suffix {
    margin-right: 7px;
  }
  .card__personalData__email__drop {
    position: absolute;
    width: 110px;
    left: -1px;
    line-height: 32px;
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    top: 30px;
    z-index: 999;
  }
  .card__personalData__email__drop__item {
    padding: 0 12px;
    cursor: pointer;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .footer-btns {
    margin: 16px;
    display: flex;
    justify-content: center;
    .confirm {
      margin-left: 16px;
    }
  }

  .form-content {
    padding: 24px;
    .form-title {
      font-style: normal;
      font-weight: bold;
      font-size: 16px;
      line-height: 24px;
      color: #102645;
    }
    .confirm {
      margin-top: 20px;
      width: 100%;
    }
    .form-item {
      margin-top: 16px;
      .send-btn {
        position: absolute;
        right: 22px;
        top: 13px;
        .truncate-text{
          max-width: 90px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }


      }
    }
    .form-item-value {
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
      font-size: 14px;
      background: #ffffff;
      border: 1px solid #d7dde7;
      box-sizing: border-box;
      border-radius: 4px;
      &.email-input {
        width: 240px;
        height:42px;
      }
      &.code-input {
        padding-right: 120px;
      }
    }
  }
  .errortip {
    color: #ee3f15;
    font-size: 12px;
    line-height: 1;
    margin-top: 6px;
  }
  .down-img {
    width: 16px;
    vertical-align: middle;
  }
  .card__personalData__email__suffix__box{
    display: flex;
    padding:0 4px;
    align-items: center;
  }
  .card__personalData__email__suffix__text {
    //  width: 85px; 
     overflow: hidden; 
     text-overflow: ellipsis; 
     white-space: nowrap;
  }
}
</style>