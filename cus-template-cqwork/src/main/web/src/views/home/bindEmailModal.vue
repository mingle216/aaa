<template>
  <div>
    <we-dialog
      @closed="closeD"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :append-to-body="true"
      class="work__personalData__BindEmailModal"
      :title="$Lan(lanFunName, 'textBindEmail', '绑定邮箱')"
      :visible.sync="dialogVisible"
      width="420px"
    >
      <we-form :model="formData" ref="emailForm" :rules="rules">
        <we-form-item
          :label="$Lan(lanFunName, 'textEmailAccount', '邮箱账号：')"
          prop="mailAccount"
        >
          <we-input
            :placeholder="
              $Lan(lanFunName, 'textEmailAccountWarning', '请输入邮箱账号')
            "
            v-model="formData.mailAccount"
          >
            <template slot="append">
              <div ref="emailSuffix" class="work__personalData__email__suffix">
                <div v-if="emailSuffixList.length > 1">
                  <div
                    class="cursor-pointer justify-between"
                    @click="handlerToggleEmailSuffix(true, true)"
                  >
                    <span>{{ formData.emailSuffix }}</span>
                    <i class="we-icon-caret-bottom"></i>
                  </div>
                  <div
                    v-if="showChooseEmail"
                    class="work__personalData__email__drop"
                  >
                    <div
                      v-for="(item, index) in emailSuffixList"
                      :key="index"
                      @click="handlerChooseEmailSuffix(item)"
                      :class="{
                        work__personalData__email__drop__item: true,
                        'portal-primary-backgroundcolor-hover-lv5': true,
                        'portal-primary-color-lv1':
                          formData.emailSuffix === item,
                      }"
                    >
                      {{ item }}
                    </div>
                  </div>
                </div>
                <div v-else>
                  <span>{{ formData.emailSuffix || "-" }}</span>
                </div>
              </div>
            </template>
          </we-input>
        </we-form-item>
        <we-form-item
          :label="$Lan(lanFunName, 'textEmailCode', '邮箱验证码：')"
          prop="code"
        >
          <we-input
            class="email-input"
            style="margin-top: 8px;"
            :placeholder="
              $Lan(lanFunName, 'textEmailCodeWarning', '请输入邮箱验证码')
            "
            v-model="formData.code"
          >
            <template slot="suffix">
              <div
                v-if="!loadingCode"
                :class="{
                  'portal-primary-color-lv1': canSendCode,
                  'portal-font-color-lv2': !canSendCode,
                  'event-none': !canSendCode,
                }"
                class="portal-primary-color-lv1 cursor-pointer truncate-text"
                @click="validationEmail"
                :title="$Lan(lanFunName, 'textGetCode', '获取验证码')"
              >
                {{ $Lan(lanFunName, "textGetCode", "获取验证码") }}
              </div>
              <div v-else class="portal-font-color-lv2 truncate-text">
                {{ $Lan(lanFunName, "textResend", "重新发送") }}({{
                  timeCount
                }})
              </div>
            </template>
          </we-input>
        </we-form-item>
      </we-form>
      <span slot="footer" class="dialog-footer">
        <we-button
          class="bgHoverWhite portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1"
          @click="dialogVisible = false"
          >{{ $Lan(lanFunName, "cancel", "取 消") }}</we-button
        >
        <we-button
          class="portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
          :style="{ opacity: formData.mailAccount == '' ? '0.7' : '1' }"
          :disabled="formData.mailAccount == ''"
          type="primary"
          @click="handlerSubmit"
          >{{ $Lan(lanFunName, "confirm", "确 定") }}</we-button
        >
      </span>
    </we-dialog>
  </div>
</template>
<script>
export default {
  computed: {
    canSendCode() {
      return (
        this.formData.mailAccount &&
        this.formData.mailAccount.length > 0 &&
        this.emailValidate
      );
    },
  },
  data() {
    var validateCode = (rule, value, callback) => {
      if (value.trim() === "") {
        this.unCorrectCode = false;
        callback(
          new Error(
            this.$Lan(
              this.lanFunName,
              "textEmailCodeWarning",
              "请输入邮箱验证码"
            )
          )
        );
      } else if (value.length > 6) {
        this.unCorrectCode = false;
        callback(
          new Error(
            this.$Lan(
              this.lanFunName,
              "textCodeLength",
              "验证码输入长度不超过六位!"
            )
          )
        );
      } else if (this.unCorrectCode) {
        callback(
          new Error(
            this.$Lan(this.lanFunName, "textIncorrectCode", "验证码输入错误！")
          )
        );
      } else if (this.validateResult) {
        this.emailValidate = false;
        callback(
          new Error(
            this.$Lan(this.lanFunName, "textEmailExisted", "邮箱账号已存在")
          )
        );
      } else {
        this.unCorrectCode = false;
        callback();
      }
    };
    var validateEmail = (rule, value, callback) => {
      var reg = /^[a-zA-Z0-9_.-]+$/;
      if (value.trim() === "") {
        this.emailValidate = false;
        callback(
          new Error(
            this.$Lan(
              this.lanFunName,
              "textEmailAccountWarning",
              "请输入邮箱账号"
            )
          )
        );
      } else if (!reg.test(value) || value.length > 256) {
        this.emailValidate = false;
        callback(
          new Error(
            this.$Lan(this.lanFunName, "textIncorrectEmail", "邮箱输入错误！")
          )
        );
      } else if (this.validateResult) {
        this.emailValidate = false;
        callback(
          new Error(
            this.$Lan(this.lanFunName, "textEmailExisted", "邮箱账号已存在")
          )
        );
      } else {
        this.emailValidate = true;
        callback();
      }
    };
    return {
      dialogVisible: false,
      formData: {
        mailAccount: "",
        code: "",
      },
      lanFunName: "CUS_TEMPLATE_CQWORK",
      unCorrectCode: false,
      loadingCode: false,
      timeCount: 60,
      emailValidate: true,
      showChooseEmail: false,
      validateResult: false,
      dynamicCode: "",
      errCode: 0,
      rules: {
        mailAccount: [
          { validator: validateEmail, trigger: ["blur", "change"] },
        ],
        code: [{ validator: validateCode, trigger: ["blur", "change"] }],
      },
      emailSuffixList: [],
    };
  },
  mounted() {
    document.addEventListener("click", this.handleClick);
  },
  beforeDestroy () {
    document.removeEventListener("click", this.handleClick);
  },
  methods: {
    show() {
      this.dialogVisible = true;
      this.getEmailSuffixList();
    },
    handleClick(e) {
      if (this.$refs.emailSuffix) {
        let isSelf = this.$refs.emailSuffix.contains(e.target);
        if (!isSelf) {
          this.handlerToggleEmailSuffix(false);
        }
      }
    },
    handleClickTrack() {
      const pageData = window.shell.getPageData().pageInfoEntity;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: pageData.pageCode,
          pageName: pageData.pageName,
        },
        startTime: new Date().getTime(),
      });
    },
    closeD() {
      this.$refs["emailForm"].resetFields();
    },
    getEmailSuffixList() {
      window.shell.execTemplateMethod("getMailSuffix", null, ({ data }) => {
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
      });
    },
    handlerToggleEmailSuffix(val, needTrack) {
      this.showChooseEmail = val;
      needTrack && this.handleClickTrack(); //点击埋点
    },
    handlerChooseEmailSuffix(item) {
      this.$set(this.formData, "emailSuffix", item);
      this.handlerToggleEmailSuffix(false, true);
    },
    reset() {
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
      this.$refs.emailForm.resetFields();
      this.dynamicCode = "";
    },
    async getCode() {
      let { data } = await window.shell.execTemplateMethod(
        "sendVerificationMessage",
        {
          mailAccount: this.formData.mailAccount + this.formData.emailSuffix,
        }
      );
      this.errCode = parseInt(data.errcode || -1);
      if (data && +data.errcode === 0) {
        this.dynamicCode = data || "";
      } else {
        this.$message.error({ message: data.errmsg });
      }
    },
    async handlerGetCode() {
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
    async validationEmail() {
      this.handleClickTrack(); //点击埋点
      if (!this.canSendCode) return;

      let res = await window.shell.execTemplateMethod("hasAccountBound", {
        mailAccount: this.formData.mailAccount + this.formData.emailSuffix,
      });
      this.emailValidate = false;
      if (res && +res.errcode === 0) {
        this.validateResult = res.data;
      } else {
        this.$message.error({ message: res.errmsg });
      }
      if (res.data) {
        this.validateResult = true;
        await this.$refs.emailForm.validateField("mailAccount", () => {
          this.emailValidate = false;
        });
        this.validateResult = false;
      } else {
        this.handlerGetCode();
      }
    },
    async submitOnEmail() {
      let res = await window.shell.execTemplateMethod("bindMail", {
        directBind: "0",
        mailAccount: this.formData.mailAccount + this.formData.emailSuffix,
        code: this.formData.code,
      });
      let errorCode = res.data && +res.data.errcode;
      this.unCorrectCode = false;
      if (errorCode === 0) {
        window.shell.showMessage({
          type: "success",
          message: this.$Lan(this.lanFunName, "textBindSuccess", "绑定成功"),
        });
        this.dialogVisible = false;
        this.$emit("on-ok");
        this.reset();
      } else if (errorCode === 402) {
        this.unCorrectCode = true;
        await this.$refs.emailForm.validate(() => {
          this.unCorrectCode = false;
        });
      } else {
        this.$message.error({ message: res.data.errmsg });
      }
    },
    handlerSubmit() {
      this.handleClickTrack(); //点击埋点
      this.$refs.emailForm.validate((valid) => {
        if (valid) {
          this.submitOnEmail();
        } else {
          return false;
        }
      });
    },
  },
};
</script>
<style lang="less" scoped>
.cursor-pointer {
  cursor: pointer;
}
.work__personalData__BindEmailModal {
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  /deep/ .we-dialog__header {
    padding: 0 !important;
    display: flex;
    align-items: center;
    padding-left: 20px !important;
  }
  /deep/ .we-form-item__label {
    // font-family: MicrosoftYaHei;
    font-size: 14px;
    color: #262626;
    letter-spacing: 0;
    text-align: justify;
    line-height: 22px;
  }
  /deep/ .we-dialog__body {
    padding-bottom: 16px;
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
  /deep/ .we-dialog {
    margin: 15vh;
    // margin-left: auto !important; margin-top: 50% !important; transform: translate(0, -50%);
  }
  .email-input /deep/.we-input__inner {
    padding-right: 160px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .we-input__inner {
    padding: 0 12px;
  }
  .we-input-group__append {
    padding: 0 12px;
    width: 138px;
  }
  .we-input__suffix {
    margin-right: 7px;
    .truncate-text {
      max-width: 107px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  .work__personalData__email__drop {
    position: absolute;
    width: 138px;
    left: 0;
    line-height: 32px;
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    top: 40px;
    z-index: 999;
  }
  .work__personalData__email__drop__item {
    padding: 0 12px;
    cursor: pointer;
  }
}
</style>
