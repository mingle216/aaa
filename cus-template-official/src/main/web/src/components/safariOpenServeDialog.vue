<template>
  <we-dialog
    v-dialogDrag
    width="500px"
    top="calc(50vh - 120px)"
    :append-to-body="true"
    custom-class="safari-dialog"
    :visible.sync="dialogVisible"
    :close-on-click-modal="false"
  >
    <div class="dialog-body">
      <div class="serve-info">
        <div class="serve-icon">
          <we-image class="serve-img" :src="serveInfo.img" fit="cover">
            <img class="img-err" slot="error" :src="errImg" />
          </we-image>
        </div>
        <we-tooltip
          class="serve-name ellipsis portal-font-color-lv1"
          effect="dark"
          placement="bottom-start"
          :open-delay="800"
        >
          <div slot="content">
            {{ serveInfo.name }}
          </div>
          <div>{{ serveInfo.name }}</div>
        </we-tooltip>
      </div>
      <div class="dialog-tip portal-font-color-lv1">
        {{
          $Lan(
            "CUS_TEMPLATE_OFFICIAL",
            "safariOpen",
            "此服务需跳转新页面打开，是否打开？"
          )
        }}
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <we-button
        class="btn-cancel portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1"
        @click="dialogVisible = false"
      >
        {{ $Lan("CUS_TEMPLATE_OFFICIAL", "cancel", "取 消") }}
      </we-button>
      <we-button
        class="btn-commit  portal-primary-backgroundcolor-lv1"
        type="primary"
        @click="goLink"
      >
        {{ $Lan("CUS_TEMPLATE_OFFICIAL", "confirm", "确 定") }}
      </we-button>
    </span>
  </we-dialog>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      errImg: "",
      serveInfo: {
        img: "",
        name: "",
        link: "",
      },
    };
  },
  mounted() {
    this.errImg = window.shell.ErrorImg;
    window.shell.on("safari-open-serve", (item) => {
      console.log(item);
      this.serveInfo.img = item.iconLink || item.pcIconUrl;
      this.serveInfo.name = item.serviceName || item.name;
      this.serveInfo.link = item.pcAccessUrl;
      this.dialogVisible = true;
    });
  },
  methods: {
    goLink() {
      this.dialogVisible = false;
      //    console.log(this.serveInfo)
      window.shell.openPage(this.serveInfo.link, 1, 2);
    },
  },
  beforeDestroy() {
    window.shell.off("safari-open-serve");
  },
};
</script>

<style lang="less">
.we-dialog.safari-dialog {
  border-radius: 4px;
  // top: -135px !important;
  // margin-top: 50vh !important;
  .we-dialog__header {
    padding: 0 !important;
    box-shadow: none !important;
    height: 26px !important;
    .we-dialog__headerbtn {
      top: 20px;
      right: 20px;
    }
  }
  .we-dialog__body {
    padding: 20px 40px 40px 40px;
  }
  .dialog-body {
    .serve-info {
      height: 36px;
      line-height: 36px;
      display: flex;
      margin-bottom: 24px;
      .serve-icon {
        width: 36px;
        height: 36px;
        .serve-img {
          width: 36px;
          height: 36px;
          border-radius: 4px;
          overflow: hidden;
          .img-err {
            width: 36px;
            height: 36px;
          }
        }
      }
      .serve-name {
        font-size: 16px;
        text-indent: 12px;
      }
    }
    .dialog-tip {
      font-size: 18px;
      height: 25px;
      line-height: 25px;
      font-weight: bold;
    }
  }

  .we-button.btn-commit {
    border-color: #fff;
  }
  .we-button.btn-cancel {
    border-color: #d9d9d9;
    margin-right: 10px;
    &:hover {
      background-color: #fff !important;
    }
  }
}
</style>
