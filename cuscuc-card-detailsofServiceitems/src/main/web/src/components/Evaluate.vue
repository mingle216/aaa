<template>
  <we-dialog
    custom-class="ev-dialog"
    :title="$Lan(lanFunName, 'dialogTitle', '评价事项')"
    :visible.sync="eDialog"
    width="510px"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="false"
    :before-close="cancelD"
  >
    <div class="e-content">
      <div class="e-top">
        <div class="e-row">
          <div class="e-left ellipsis">
            <span>{{ $Lan(lanFunName, "dialogContent", "服务事项") }}：</span>
          </div>
          <div class="e-right ellipsis">
            <we-tooltip
              class="item"
              effect="dark"
              placement="bottom-start"
              :open-delay="900"
            >
              <div slot="content">{{ itemName }}</div>
              <div class="evname" style="margin-bottom: 19px">
                {{ itemName }}
              </div>
            </we-tooltip>
          </div>
        </div>
        <div class="e-row" v-for="item in appraiseNameNew" :key="item.code">
          <div class="e-left ellipsis">
            <span>{{ item.name }}：</span
            >
          </div>
          <div class="e-right ellipsis">
            <we-rate
              v-model="item.score"
              show-text
              :texts="texts"
              :text-color="tcolor"
            ></we-rate>
          </div>
        </div>
        <!-- <div class="e-row">
          <div class="e-left ellipsis">
            <span
              >{{
                nameDic["SERVICE_MANNER"] ||
                  $Lan(lanFunName, "serviceManner", "服务态度")
              }}：</span
            >
          </div>
          <div class="e-right ellipsis">
            <we-rate
              v-model="serviceManner"
              show-text
              :texts="texts"
              :text-color="tcolor"
            ></we-rate>
          </div>
        </div>
        <div class="e-row">
          <div class="e-left ellipsis">
            <span
              >{{
                nameDic["SERVICE_SPEED"] ||
                  $Lan(lanFunName, "serviceSpeed", "办事速度")
              }}：</span
            >
          </div>
          <div class="e-right ellipsis">
            <we-rate
              v-model="serviceSpeed"
              show-text
              :texts="texts"
              :text-color="tcolor"
            ></we-rate>
          </div>
        </div>
        <div class="e-row">
          <div class="e-left ellipsis">
            <span
              >{{
                nameDic["EVENT_INTEGRITY"] ||
                  $Lan(lanFunName, "eventIntegrity", "信息完整度")
              }}：</span
            >
          </div>
          <div class="e-right ellipsis">
            <we-rate
              v-model="eventIntegrity"
              show-text
              :texts="texts"
              :text-color="tcolor"
            ></we-rate>
          </div>
        </div>
        <div class="e-row">
          <div class="e-left ellipsis">
            <span
              >{{
                nameDic["TOTAL_SATISFY"] ||
                  $Lan(lanFunName, "totalSatisfy", "整体满意度")
              }}：</span
            >
          </div>
          <div class="e-right ellipsis">
            <we-rate
              v-model="totalSatisfy"
              show-text
              :texts="texts"
              :text-color="tcolor"
              class="ellipsis"
            ></we-rate>
          </div>
        </div> -->
      </div>
      <div style="margin-bottom: 16px" class="ellipsis">
        {{ $Lan(lanFunName, "comments", "意见或建议（选填）") }}：
      </div>
    </div>
    <div class="suText">
      <we-input
        type="textarea"
        :placeholder="
          $Lan(lanFunName, 'textarea', '请在此处写下您的意见或建议')
        "
        v-model="suggest"
        maxlength="500"
        show-word-limit
        resize="none"
        rows="7"
      ></we-input>
    </div>
    <div slot="footer" class="dialog-footer">
      <div class="footer-button">
        <we-button
          @click="cancelD"
          class="ellipsis btn cancel-btn portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1"
          >{{ $Lan(lanFunName, "buttonCancel", "取消") }}</we-button
        >
        <we-button
          type="primary"
          @click="ecommit()"
          class="ellipsis btn btn-commit portal-primary-backgroundcolor-lv1"
          :disabled="!canCommit"
          >{{ $Lan(lanFunName, "buttonCommit", "提交") }}</we-button
        >
      </div>
    </div>
  </we-dialog>
</template>

<script>
export default {
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    router: Object,
    itemWid: {
      type: String,
      default: "",
    },
    value: {
      type: Boolean,
      default: false,
    },
    itemName: {
      type: String,
      default: "",
    },
    card: {
      type: Object,
      default: () => {
        return {
          cardWid: "",
          cardId: "",
        };
      },
    },
    appraiseName: {
      type: Array,
      default: () => {
        return [];
      },
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  computed: {
    canCommit() {
      return (
        this.appraiseNameNew.every(v=> v.score !== 0) &&
        !this.timeCommit
      );
    },
  },
  watch: {
    value(val) {
      this.eDialog = val;
      if (val) {
        this.serviceManner = null;
        this.serviceSpeed = null;
        this.totalSatisfy = null;
        this.eventIntegrity = null;
        this.suggest = null;
        this.timeCommit = false;
        this.appraiseNameNew = this.appraiseName.map(v=> { return {
          ...v,
          score: 0
        } });
      }
    },
    eDialog(val) {
      this.$emit("change", val);
    },
    // timeCommit(val) {
    //   if (val) {
    //     setTimeout(() => {
    //       this.timeCommit = false;
    //       this.eDialog = false;
    //     }, 3000);
    //   }
    // },
    appraiseName: {
      handler(val) {
        if (val && val.length) {
          let obj = {};
          val.forEach((item) => {
            obj[item.code] = item.name;
          });
          this.nameDic = obj;
        }
      },
      immediate: true,
    },
  },
  data() {
    return {
      tcolor: "#999",
      texts: [
        this.$Lan(this.lanFunName, "satislevel_1", "非常不满意"),
        this.$Lan(this.lanFunName, "satislevel_2", "不满意"),
        this.$Lan(this.lanFunName, "satislevel_3", "一般"),
        this.$Lan(this.lanFunName, "satislevel_4", "比较满意"),
        this.$Lan(this.lanFunName, "satislevel_5", "非常满意"),
      ],
      serviceManner: null,
      serviceSpeed: null,
      totalSatisfy: null,
      eventIntegrity: null,
      suggest: null,
      eDialog: false,
      timeCommit: false,
      nameDic: "",
      appraiseNameNew: []
    };
  },
  methods: {
    cancelD() {
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
      this.eDialog = false
    },
    ecommit() {
      //   this.timeCommit = false;

      if (this.timeCommit) {
        return;
      }
      this.timeCommit = true;
      let param = {
        appId: this.itemWid,
        // serviceManner: this.serviceManner,
        // serviceSpeed: this.serviceSpeed,
        // totalSatisfy: this.totalSatisfy,
        // eventIntegrity: this.eventIntegrity,
        suggest: this.suggest,
        appraiseDetail: JSON.stringify({
          score: this.appraiseNameNew.map(v=> { return {
            dimenWid: v.code,
            dimenScore: `${v.score}`
          } })
        })

      };
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
            itemId: this.itemWid,
            fucType: 4
          }
        },
        startTime: new Date().getTime()
      });
      window.shell
        .execCardMethod({
          ...this.card,
          method: "evaluation",
          param,
        })
        .then((data) => {
          // console.log(data)
          if (data.data.errcode == 0) {
            // 评价成功调用方法
            window.shell
              .execCardMethod({
                ...this.card,
                method: "evaluationResponse",
                param: {
                  wid: this.itemWid,
                },
              })
              .then((data) => {
                // console.log(data)
                setTimeout(() => {
                  this.timeCommit = false;
                  this.eDialog = false;
                }, 200);
                if (data.errcode == 0 && data.errmsg == "请求成功") {
                  if (data.data.errcode == 0 && data.data.errmsg == "success") {
                    this.$parent.serviceItemInfo.appraiseNum =
                      data.data.data.appraiseNum;
                    this.$parent.serviceItemInfo.appraiseMark =
                      data.data.data.appraiseMark;
                  }

                  this.$message.success({
                    message: this.$Lan(this.lanFunName, "success", "感谢评价"),
                    duration: 3000,
                  });
                } else {
                  this.$message({
                    showClose: false,
                    message: data.data.errmsg,
                    type: "error",
                  });
                }
              })
              .catch(() => {
                this.timeCommit = false;
              });
            // console.log(data)
          } else {
            this.timeCommit = false;
            this.$message({
              showClose: false,
              message: data.data.errmsg,
              type: "error",
            });
          }
        })
        .catch(() => {
          this.timeCommit = false;
        });
    },
  },
};
</script>
<style lang="less">
.ev-dialog {
  top: 50%;
  margin-top: -276px !important;
  z-index: 9999;
  .we-dialog__header {
    border-bottom: 1px solid #eee;
    height: 66px;
    .we-dialog__title {
      font-size: 18px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      width: 414px;
      display: block;
    }
  }
  .we-dialog__body {
    padding: 20px;
  }
}
</style>
<style lang="less" scoped>
.evname {
  max-width: 400px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.btn {
  // width: 84px;
  height: 36px;
  line-height: 0px;
  &.cancel-btn {
    border-color: #d9d9d9;
    // margin-right: 10px;
    &:hover {
      background-color: #fff;
    }
  }
}
.btn-commit {
  border-color: #ffffff;
  &:hover {
    border-color: #ffffff !important;
  }
}
.we-button {
  max-width: 230px;
}
.footer-button {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  flex-wrap: nowrap;
}
.eStar > div:not(:last-child) {
  margin-bottom: 16px;
}
.e-content {
  .e-top {
    .e-row {
      display: flex;
      height: 22px;
      line-height: 22px;
      margin-bottom: 14px;
      .e-left {
        width: 140px;
      }
      .e-right {
        flex: 1;
        /deep/.we-rate:focus {
          outline: none;
        }
      }
    }
  }
}
</style>
