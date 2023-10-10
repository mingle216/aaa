<template>
  <we-dialog
    :title="$Lan('SYS_CARD_CALENDAR_DETAIL', 'shareSchedule', '分享至')"
    :append-to-body="true"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :visible.sync="visible"
    width="800px"
    custom-class="shareScheduleModal"
    @closed="handleClickTrack()"
    @click.native.stop
  >
    <!-- 弹窗高度，最大580px -->
    <div class="shareSchedule">
      <div class="shareSchedule__left">
        <div class="shareSchedule__header portal-font-color-lv2">
          <we-input
            v-model="keyword"
            :placeholder="
              $Lan('SYS_CARD_CALENDAR_DETAIL', 'shareSearch', '搜索姓名或工号')
            "
            @keyup.native="handleKeyUp"
          >
            <i
              slot="suffix"
              class="we-input__icon iconfont icon-CardSearch portal-font-color-lv4"
              style="font-size: 14px;"
              @click="handleSearch"
            ></i>
          </we-input>
        </div>
        <ContainerScroll
          style="flex: 1;min-height: 0;"
          :barKeepShow="true"
          v-if="dataLists.length"
        >
          <div style="padding: 16px 16px 0 16px" v-loading="loading">
            <we-checkbox
              v-model="item.checked"
              v-for="item in dataLists"
              :key="item.userAccount"
              :disabled="!item.canChoose"
              class="shareSchedule__checkbox"
              @change="handleChangeCheck(item)"
              >{{ item.userName }}({{ item.userAccount }})</we-checkbox
            >
          </div>
        </ContainerScroll>
        <div class="shareSchedule__empty" v-if="!loading && !dataLists.length">
          <img width="110" src="../../assets/images/noData.png" />
          <div
            class="portal-font-color-lv4"
            style="margin-top: 6px; font-size: 12px; line-height: 20px"
          >
            {{
              $Lan(
                "SYS_CARD_CALENDAR_DETAIL",
                searchKeyword ? "noData" : "shareSearchEmpty",
                searchKeyword ? "暂无内容" : "请输入姓名或工号搜索"
              )
            }}
          </div>
        </div>
      </div>
      <div class="shareSchedule__right">
        <div
          class="shareSchedule__header portal-font-color-lv2"
          style="padding: 0 16px;"
        >
          <div
            style="flex: 1; min-width: 0; display: flex; align-items: center; margin-right: 12px"
          >
            <span class="ellipsis">{{
              $Lan(
                "SYS_CARD_CALENDAR_DETAIL",
                "shareChecked",
                "已选 {num} 项",
                { num: checkedLists.length }
              )
            }}</span>
          </div>
          <div
            class="portal-font-color-lv3 portal-primary-color-hover-lv1"
            style="cursor: pointer;display: flex; align-items: center;"
            @click="handleReset"
          >
            <i class="we-icon-delete" style="margin-right: 4px;"></i
            ><span class="text__ellipsis" style="max-width: 110px;">{{
              $Lan("SYS_CARD_CALENDAR_DETAIL", "shareReset", "重置")
            }}</span>
          </div>
        </div>
        <ContainerScroll style="flex: 1;min-height: 0;" :barKeepShow="true">
          <div style="padding: 16px" v-if="checkedLists.length">
            <div
              class="shareSchedule__tag"
              v-for="item in checkedLists"
              :key="item.userAccount"
            >
              <div class="shareSchedule__tag__name">
                <span class="portal-font-color-lv2">{{ item.userName }}</span>
              </div>
              <div
                class="shareSchedule__tag__close"
                @click="handleCancle(item)"
              >
                <i
                  class="iconfont icon-toastfailure portal-font-color-lv4 portal-primary-color-hover-lv1"
                  style="font-size: 12px; cursor: pointer"
                ></i>
              </div>
            </div>
          </div>
        </ContainerScroll>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <we-button
        size="medium"
        class="ellipsis portal-primary-color-hover-lv1 portal-primary-border-color-hover-lv4 portal-primary-backgroundcolor-hover-lv5"
        @click="visible = false"
        >{{
          $Lan("SYS_CARD_CALENDAR_DETAIL", "cancelModal", "取 消")
        }}</we-button
      >
      <we-button
        type="primary"
        size="medium"
        class="ellipsis portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
        :loading="okLoading"
        :disabled="!checkedLists.length"
        @click="handleOk"
        >{{ $Lan("SYS_CARD_CALENDAR_DETAIL", "okModal", "确 定") }}</we-button
      >
    </div>
  </we-dialog>
</template>

<script>
import { formatDate } from "../../utils/date-util";
import { clickTrack } from "../../mixins/track.js";
export default {
  props: ["router"],
  mixins: [clickTrack],
  data() {
    return {
      visible: false,
      loading: false,
      okLoading: false,
      keyword: "",
      searchKeyword: "",
      itemInfo: null,
      dataLists: [],
      checkedLists: [],
    };
  },
  methods: {
    show(item) {
      this.visible = true;
      this.itemInfo = item;
      this.okLoading = false;
      this.loading = false;
      this.checkedLists = [];
      this.dataLists = [];
      this.keyword = "";
      this.searchKeyword = "";
    },
    handleChangeCheck(item) {
      if (item.checked) {
        this.checkedLists.push(item);
      } else {
        this.checkedLists = this.checkedLists.filter(
          (curr) => curr.userAccount !== item.userAccount
        );
      }
      this.handleClickTrack(); // 点击埋点
    },
    handleKeyUp(e) {
      if (e.key === "Enter") {
        this.handleSearch();
      }
    },
    handleSearch() {
      this.searchKeyword = this.keyword.trim();
      if (this.searchKeyword === "") {
        this.dataLists = [];
      } else {
        this.fetchData();
      }
    },
    handleReset() {
      this.dataLists.forEach((element) => {
        element.checked = false;
      });
      this.checkedLists = [];
      this.handleClickTrack(); // 点击埋点
    },
    handleCancle(item) {
      for (let i = 0; i < this.dataLists.length; i++) {
        const ele = this.dataLists[i];
        if (ele.userAccount === item.userAccount) {
          ele.checked = false;
          break;
        }
      }
      this.checkedLists = this.checkedLists.filter(
        (curr) => curr.userAccount !== item.userAccount
      );
      this.handleClickTrack(); // 点击埋点
    },
    handleOk() {
      const user = this.checkedLists.map((item) => item.userAccount);
      this.okLoading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "shareSchedule",
          param: {
            wid: this.itemInfo.eventId,
            sharedUser: user.join(","),
            startTime: formatDate(
              this.itemInfo.startTime,
              "yyyy-MM-dd HH:mm:ss"
            ),
            endTime:
              this.itemInfo.endTimeOrigin &&
              formatDate(this.itemInfo.endTimeOrigin, "yyyy-MM-dd HH:mm:ss"),
          },
        },
        (data) => {
          this.$nextTick(function() {
            this.okLoading = false;
          });
          if (data && data.errcode === "0") {
            this.visible = false;
            window.shell.showMessage({
              type: "success",
              message: this.$Lan(
                "SYS_CARD_CALENDAR_DETAIL",
                "shared",
                "已分享"
              ),
            });
          }
        }
      );
      this.handleClickTrack({
        infoType: 15,
      }); // 点击埋点
    },
    fetchData() {
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "searchUser",
          param: {
            keyword: this.searchKeyword,
          },
        },
        (data) => {
          this.$nextTick(function() {
            this.loading = false;
          });
          if (data && data.errcode === "0") {
            const respData = data.data.data.data || [];
            const ids = this.checkedLists.map((item) => item.userAccount);
            respData.forEach((element) => {
              element.checked = ids.includes(element.userAccount);
            });
            this.dataLists = respData;
          } else {
            this.dataLists = [];
          }
        }
      );
    },
  },
};
</script>

<style lang="less" scoped>
/deep/.shareScheduleModal {
  margin-top: calc(50vh - 310px) !important;
  .we-dialog__title {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    word-break: break-all;
    display: inline-block;
    max-width: 720px;
  }
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    width: 100%;
    flex-wrap: nowrap;
    .we-button {
      max-width: 675px;
    }
  }
}
.shareSchedule {
  height: 452px;
  background: #ffffff;
  display: flex;
}
.shareSchedule__left {
  width: 45%;
  margin-right: 16px;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
}
.shareSchedule__header {
  height: 48px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  padding: 0 16px;
  /deep/.we-input__suffix {
    cursor: pointer;
    right: 12px;
  }
  /deep/.we-input__inner {
    height: 32px;
    line-height: 32px;
    border-radius: 4px;
    border: 1px solid #d9d9d9;
    padding: 0 30px 0 12px;
  }
  /deep/.we-input__icon {
    line-height: 32px;
  }
}
.shareSchedule__page {
  padding: 10px 0 16px 0;
  display: flex;
}
.shareSchedule__right {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  border-radius: 4px;
  border: 1px solid #f0f0f0;
  .shareSchedule__tag {
    display: inline-flex;
    background: #f5f5f5;
    border-radius: 4px;
    padding: 4px 12px;
    margin: 0 8px 8px 0;
    align-items: center;
    .shareSchedule__tag__name {
      flex: 1;
      min-width: 0;
      line-height: 22px;
      flex-basis: auto;
    }
    .shareSchedule__tag__close {
      margin-left: 8px;
    }
  }
}
/deep/.we-checkbox__label {
  padding-left: 8px;
}
.shareSchedule__checkbox {
  width: 100%;
  margin-right: 0;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  /deep/.we-checkbox__label {
    line-height: 22px;
    flex: 1;
    min-width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    word-break: break-all;
  }
}
.shareSchedule__empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>
