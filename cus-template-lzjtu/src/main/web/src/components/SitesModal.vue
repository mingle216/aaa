<template>
  <div>
    <we-dialog
      :title="$Lan('CUS_TEMPLATE_LZJTU', 'changeSites', '切换站点')"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="visible"
      top="0"
      @close="close"
      custom-class="sites-dialog"
      width="450px"
    >
      <vue-scroll :barKeepShow="true">
        <ul class="modal__wrap">
          <li
            v-for="item in siteList"
            :key="item.wid"
            :class="{
              site__item: true,
              'portal-primary-backgroundcolor-hover-lv5': true,
              'portal-primary-border-color-hover-lv1': true,
              'portal-primary-border-color-lv1 portal-primary-backgroundcolor-lv5':
                selectedSite === item.wid,
            }"
            @click="selectedSite = item.wid"
          >
            <we-radio v-model="selectedSite" :label="item.wid" :title="item.siteName">
              <span :class="{'portal-primary-color-lv1': selectedSite === item.wid}">{{ item.siteName }}</span>
            </we-radio>
            <span
              class="active__tag"
              v-if="(currentSite && currentSite.wid) === item.wid"
            >
              {{ $Lan("CUS_TEMPLATE_LZJTU", "using", "使用中") }}
            </span>
          </li>
        </ul>
      </vue-scroll>
      <div slot="footer" class="dialog__footer">
        <we-button size="medium" @click="close">
          {{ $Lan("CUS_TEMPLATE_LZJTU", "cancel", "取消") }}
        </we-button>
        <we-button
          size="medium"
          type="primary"
          @click="handleSubmit"
          class="portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
        >
          {{ $Lan("CUS_TEMPLATE_LZJTU", "confirmChoice", "确认选择") }}
        </we-button>
      </div>
    </we-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      selectedSite: "",
      siteList: [],
    };
  },
  computed: {
    currentSite() {
      return window.shell.getCurrentSite();
    },
  },
  methods: {
    show() {
      this.visible = true;
      window.shell.getUserPermissionRouters(
        {
          langCountry: this.$i18n.locale || "zh_CN",
        },
        (res) => {
          if (res.errcode === "0") {
            this.siteList = res.data || [];
            console.log(this.currentSite);
            this.selectedSite = this.currentSite?.wid || this.siteList[0]?.wid;
          }
        }
      );
    },
    close() {
      this.selectedSite = "";
      this.visible = false;
    },
    handleSubmit() {
      const temp = this.siteList.find((el) => el.wid === this.selectedSite);
      const siteRoute = temp?.siteRoute;
      window.shell.changeSite(siteRoute);
    },
  },
};
</script>

<style lang="less" scoped>
/deep/.we-dialog {
  top: 50%;
  left: 50%;
  margin: 0;
  transform: translate(-50%, -50%);
  .we-dialog__title {
    font-size: 18px;
  }
  .we-dialog__body {
    padding: 20px 0;
    // border-bottom: 1px solid #F0F0F0;
  }
  .we-dialog__header {
    padding: 18px 20px;
  }
  .we-dialog__wrapper {
    overflow: hidden;
  }
  .we-dialog__footer {
    padding-top: 20px;
    box-shadow: inset 0 1px 0 0 #f0f0f0;
  }
}
.modal__wrap {
  padding: 0 20px;
  max-height: 220px;
  .site__item {
    width: 410px;
    height: 50px;
    padding: 0 12px 0 20px;
    box-sizing: border-box;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-radius: 4px;
    border: 1px solid #f0f0f0;
    margin-bottom: 12px;
    &:last-child {
      margin-bottom: 0;
    }
    .active__tag {
      background: #25b14d;
      border-radius: 12px;
      width: 56px;
      height: 24px;
      line-height: 24px;
      text-align: center;
      font-size: 12px;
      color: #ffffff;
      letter-spacing: 0;
    }
  }
  /deep/.we-radio__label {
    max-width: 270px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: inline-block;
    vertical-align: middle;
  }
  /deep/.we-radio__inner {
    background: rgba(0, 0, 0, 0.04);
  }
}
</style>
