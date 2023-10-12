<template>
  <div>
    <div
      class="personApp"
      :class="[personalInfo.length === 6 ? 'personApp-6' : '']"
    >
      <div
        class="personApp__item"
        v-for="(item, index) in personalInfo"
        :key="item.serviceWid"
        :class="[
          personalInfo.length % 2 && index === personalInfo.length - 1
            ? 'personApp__item-full'
            : '',
          `personApp__item${personalInfo.length > 3 ? 4 : personalInfo.length}`,
          `personApp__item-${index}`,
          item.linkUrl || item.isEmail === 1 ? 'cursor' : '',
        ]"
        :style="
          `borderColor:${colors[index].border};background:${colors[index].background}`
        "
        @click="handleClick(item)"
      >
        <we-tooltip
          class="item"
          effect="dark"
          :open-delay="800"
          placement="bottom"
        >
          <div slot="content">
            <!-- 邮箱未读消息 -->
            <div v-if="item.isEmail === 1 && item.mainInfo">
              {{ item.emailUnReadCount > 0 ? item.mainInfo : "" }}
            </div>
            <div v-else>{{ item.mainInfo || item.title }}</div>
            <div v-if="item.isEmail === 1 && item.mainInfo">
              <div
                class="portal-primary-color-lv3"
                @click="handlerOffEmail(item)"
                style="cursor:pointer;text-decoration:underline;"
              >
                {{ $Lan(lanFunName, "textBindOffEmail", "解绑邮箱") }}
              </div>
            </div>
          </div>
          <div
            class="personApp__itemInfo"
            :class="[
              item.isEmail === 1 && item.emailUnReadCount > 0
                ? 'personApp__itemNews'
                : '',
            ]"
          >
            <div
              class="personApp__itemIcon"
              :class="[!item.iconUrl ? 'hidden' : '']"
            >
              <img
                :src="item.iconUrl || errorImg"
                style="width: 100%;height: 100%;"
              />
            </div>
            <div
              class="personApp__itemName ellipsis portal-primary-color-hover-lv1"
              :class="[
                (personalInfo.length === 1 || personalInfo.length === 5) &&
                index === personalInfo.length - 1
                  ? 'personApp__itemName-show'
                  : '',
              ]"
            >
              {{ item.title }}
            </div>
          </div>
        </we-tooltip>
      </div>
    </div>
    <bind-email-modal
      ref="BindEmailModal"
      @on-ok="getAllInfo"
    ></bind-email-modal>
  </div>
</template>

<script>
import BindEmailModal from "./bindEmailModal";
export default {
  props: ["personalDataConfig"],
  components: {
    BindEmailModal,
  },
  data() {
    return {
      lanFunName: "CUS_TEMPLATE_CQWORK",
      personalInfo: [],
      colors: [
        {
          font: "#40A9FF",
          border: "#B3DDFF",
          background: "#F7FCFF",
        },
        {
          font: "#9254DE",
          border: "#D3BBF2",
          background: "#FBF8FE",
        },
        {
          font: "#68D093",
          border: "#C3ECD4",
          background: "#F9FDFB",
        },
        {
          font: "#FF7A45",
          border: "#FFCAB5",
          background: "#FFFAF8",
        },
        {
          font: "#36CFC9",
          border: "#AFECE9",
          background: "#F7FDFD",
        },
        {
          font: "#F759AB",
          border: "#FCBDDD",
          background: "#FFF8FC",
        },
      ],
      errorImg: require("../../assets/images/imgErr.png"),
    };
  },
  mounted() {
    this.getAllInfo();
    this.getListInterval = setInterval(() => {
      this.getAllInfo();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.getListInterval);
  },
  methods: {
    async handleClick(item) {
      this.handleClickTrack({
        infoType: 16,
        wid: item.wid,
        name: item.title,
      }); //点击埋点
      if (item.isEmail === 1) {
        if (item.mainInfo) {
          //已绑定邮箱
          let { data } = await window.shell.execTemplateMethod("getLinkUrl", {
            mailAccount: item.extraInfo,
          });
          const linkUrl = data;
          linkUrl && window.shell.openPage(linkUrl, 1, 2);
        } else {
          this.handlerOnEmail(item);
        }
      } else {
        if (item.linkUrl) {
          window.shell.openPage(item.linkUrl, 1, 2);
        }
      }
    },
    getAllInfo() {
      window.shell.execTemplateMethod(
        "getPersonalDataList",
        {
          personalDataConfig: this.personalDataConfig,
        },
        ({ data }) => {
          this.personalInfo = data || [];
          data.forEach((el, index) => {
            this.setPersonDetail(el, index);
          });
        }
      );
    },
    async setPersonDetail(item, index) {
      if (+item.needRetrieve !== 1) {
        return;
      }
      let { data } = await window.shell.execTemplateMethod(
        "getPersonalDataDetail",
        {
          wid: item.wid,
          extraInfo: item.extraInfo,
        }
      );
      this.$set(this.personalInfo, index, data || {});
    },
    async handlerOnEmail(item) {
      let { data } = await window.shell.execTemplateMethod("existsAccount", {
        wid: item.wid,
      });
      if (!data) {
        return;
      }
      if (!data.status) {
        this.$refs.BindEmailModal.show();
        return;
      }
      this.$confirm(
        this.$Lan(this.lanFunName, "textAskIsBindEmail", "是否绑定该邮箱？"),
        this.$Lan(this.lanFunName, "textHasEmail", "检测到已有邮箱") +
          data.account,
        {
          confirmButtonText: this.$Lan(this.lanFunName, "confirm", "确 定"),
          cancelButtonText: this.$Lan(this.lanFunName, "cancel", "取 消"),
          distinguishCancelAndClose: true,
          closeOnClickModal: false,
          customClass: "portal-font-color-lv1 custom-personal-confirm",
          confirmButtonClass:
            "portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1",
          cancelButtonClass:
            "bgHoverWhite portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1 ",
          type: "warning",
          center: true,
        }
      )
        .then(() => {
          this.submitOnEmail(data);
          this.handleClickTrack(); //点击埋点
        })
        .catch((action) => {
          setTimeout(() => {
            if (action === "cancel") {
              this.$refs.BindEmailModal.show();
              this.handleClickTrack(); //点击埋点
            }
          }, 500);
        });
    },
    handlerOffEmail(item) {
      this.handleClickTrack(); //点击埋点
      this.$confirm(
        this.$Lan(
          this.lanFunName,
          "textAfterBindOffWarning",
          "解绑后将无法收到未读邮件提示"
        ),
        this.$Lan(
          this.lanFunName,
          "textBindOffWarning",
          `确认解绑邮箱${item.extraInfo}？`,
          {
            account: item.extraInfo,
          }
        ),
        {
          confirmButtonText: this.$Lan(this.lanFunName, "confirm", "确 定"),
          cancelButtonText: this.$Lan(this.lanFunName, "cancel", "取 消"),
          closeOnClickModal: false,
          customClass: "portal-font-color-lv1 custom-personal-confirm",
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
          let res = await window.shell.execTemplateMethod("unBindMail", {
            wid: item.wid,
            mailAccount: item.extraInfo,
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
          this.getAllInfo();
        })
        .catch((action) => {
          if (action === "cancel") {
            this.handleClickTrack(); //点击埋点
          }
        });
    },
    async submitOnEmail(email) {
      let res = await window.shell.execTemplateMethod("bindMail", {
        directBind: "1",
        mailAccount: email.account,
      });
      if (res.data && +res.data.errcode === 0) {
        window.shell.showMessage({
          type: "success",
          message: this.$Lan(this.lanFunName, "textBindSuccess", "绑定成功"),
        });
        this.getAllInfo();
      } else {
        this.$message.error({ message: res.data.errmsg });
      }
    },
    handleClickTrack(extraInfo) {
      const pageData = window.shell.getPageData().pageInfoEntity;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: pageData.pageCode,
          pageName: pageData.pageName,
          extraInfo,
        },
        startTime: new Date().getTime(),
      });
    },
  },
};
</script>

<style scoped>
.personApp {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}
.personApp__item {
  width: 128px;
  flex-shrink: 0;
  border-radius: 6px;
  border: 1px solid;
  padding: 10px 20px;
  margin-bottom: 16px;
}
.personApp__item.cursor {
  cursor: pointer;
}
.personApp__itemInfo {
  display: flex;
  align-items: center;
  justify-content: center;
}
.personApp__itemIcon {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
  position: relative;
  margin-right: 5px;
}
.personApp__itemName {
  line-height: 22px;
  position: relative;
}
.personApp__itemNews .personApp__itemName:after {
  content: "";
  position: absolute;
  width: 6px;
  height: 6px;
  border-radius: 3px;
  background: #ff230c;
  top: 2px;
  right: 0;
}
@media screen and (min-width: 1282px) {
  .personApp__item-full {
    flex: 1;
  }
  .hidden {
    display: none;
  }
  .personApp__itemName {
    padding-right: 4px;
  }
}
@media screen and (max-width: 1281px) {
  .personApp-6 {
    justify-content: flex-start;
  }
  .personApp__itemIcon.hidden {
    margin-right: 0;
  }
  .personApp-6 .personApp__item:not(.personApp__item-3) {
    margin-right: 10px;
  }
  .personApp__item {
    margin-bottom: 12px;
    padding: 6px 0;
  }
  .personApp__item4 {
    width: 37px;
    flex-shrink: 0;
  }
  .personApp__item4.personApp__item-full {
    flex: 1;
    padding: 6px 20px;
  }
  .personApp__item3 {
    width: 52px;
    flex-shrink: 0;
  }
  .personApp__item2 {
    width: 84px;
  }
  .personApp__item1 {
    flex: 1;
    padding: 6px 20px;
  }
  .personApp__itemName {
    display: none;
  }
  .personApp__itemName-show {
    display: initial;
  }
  .personApp__itemNews .personApp__itemIcon:after {
    content: "";
    position: absolute;
    width: 6px;
    height: 6px;
    border-radius: 3px;
    background: #ff230c;
    top: 0;
    right: 0;
  }
}
</style>

<style lang="less">
.bgHoverWhite:hover {
  background-color: white !important;
}
.custom-personal-confirm {
  padding-bottom: 20px !important;
  .we-message-box__header {
    padding: 40px 15px 0 15px;
    .we-message-box__title {
      justify-content: flex-start;
      padding-left: 15px;
      font-size: 16px;
      color: inherit;
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
</style>
