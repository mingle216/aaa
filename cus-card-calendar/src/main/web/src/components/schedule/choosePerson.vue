<template>
  <div @click.native.stop>
    <we-dialog
      v-if="visible"
      :title="$Lan(funcName, 'chooseParticipants', '选择参与人员')"
      :append-to-body="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="visible"
      custom-class="addScheduleModal"
      width="800px"
      @click.native.stop
    >
      <div class="chooseFlex portal-font-color-lv1">
        <choose-tree
          ref="ChooseTree"
          :router="router"
          :checkedIds="checkedIds"
          class="chooseBox"
          @check-change="handleCheckChange"
        ></choose-tree>
        <div class="chooseBox">
          <div class="chooseBox__header plr-12">
            <div class="ellipsis portal-font-color-lv2">
              {{ $Lan(funcName, "selectedObject", "已选对象") }}
              <span style="margin-left:2px"
                >{{ checkedLists.length || 0 }}/1000</span
              >
            </div>
            <div
              class="cursor-pointer"
              @mouseover="clearHover = true"
              @mouseout="clearHover = false"
              @click="handleReset"
            >
              <span
                class="iconfont icon-shanchu"
                :class="[
                  clearHover
                    ? 'portal-primary-color-lv1'
                    : 'portal-font-color-lv4',
                ]"
              ></span>
              <span
                class="ellipsis"
                :class="[
                  clearHover
                    ? 'portal-primary-color-lv1'
                    : 'portal-font-color-lv3',
                ]"
                style="flex-shrink: 0;"
                >{{ $Lan(funcName, "clear", "清空") }}</span
              >
            </div>
          </div>
          <ContainerScroll style="height: 370px" :barKeepShow="true">
            <div style="padding:12px">
              <div
                class="chooseBox__tag"
                v-for="(item, index) in checkedLists"
                :key="index"
              >
                <div>
                  {{ item.userName }}&nbsp;
                  <span class="portal-font-color-lv3"
                    >({{ item.userAccount }})</span
                  >
                </div>
                <span
                  class="iconfont icon-toastfailure portal-font-color-lv4 cursor-pointer"
                  @click="handleClose(item, index)"
                ></span>
              </div>
            </div>
          </ContainerScroll>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <we-button
          class="ellipsis portal-primary-color-hover-lv1 portal-primary-border-color-hover-lv1"
          size="medium"
          @click="visible = false"
          >{{ $Lan(funcName, "cancelModal", "取 消") }}</we-button
        >
        <we-button
          type="primary"
          size="medium"
          class="ellipsis portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
          :loading="okLoading"
          @click="handleOk"
          >{{ $Lan(funcName, "okModal", "确 定") }}</we-button
        >
      </div>
    </we-dialog>
  </div>
</template>

<script>
import ChooseTree from "./chooseTree";
export default {
  props: ["router", "defaultChecked"],
  components: {
    ChooseTree,
  },
  data() {
    return {
      funcName: this.router.cardId,
      visible: false,
      okLoading: false,
      clearHover: false,
      checkedLists: [],
    };
  },
  computed: {
    checkedIds() {
      return this.checkedLists.map((item) => item.wid);
    },
  },
  methods: {
    show() {
      this.checkedLists = JSON.parse(JSON.stringify(this.defaultChecked || []));
      this.visible = true;
    },
    handleSearch() {
      if (this.keyword.trim()) {
        this.showSearch = true;
      } else {
        this.userLists = [];
        this.showSearch = false;
      }
    },
    handleCheckChange(item) {
      const index = this.checkedLists.findIndex(
        (curr) => curr.wid === item.wid
      );
      if (item.checked) {
        index === -1 && this.checkedLists.push(item);
      } else {
        index > -1 && this.checkedLists.splice(index, 1);
      }
    },
    handleClose(item, index) {
      item.checked = false;
      this.checkedLists.splice(index, 1);
      this.$refs.ChooseTree.resetChecked(item.wid);
    },
    handleReset() {
      this.checkedLists = [];
      this.$refs.ChooseTree.resetChecked();
    },
    handleOk() {
      if (this.checkedLists.length > 1000) {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan(
            this.funcName,
            "selectPeopleExceed",
            "已达到上限1000人"
          ),
        });
        return;
      }
      this.$emit("change-check", this.checkedLists);
      this.visible = false;
    },
  },
};
</script>

<style lang="less">
.chooseFlex {
  display: flex;
  align-items: center;
}
.cursor-pointer {
  cursor: pointer;
}
.plr-12 {
  padding: 0 12px;
}
.chooseBox {
  border: 1px solid #f0f0f0;
  border-radius: 4px;
  &:first-child {
    flex: 1;
    width: 0;
    margin-right: 16px;
  }
  &:last-child {
    width: 244px;
    flex-shrink: 0;
  }
  // .tabItem-active:after {
  //   content: "";
  //   position: absolute;
  //   width: 100%;
  //   height: 2px;
  //   left: 0;
  //   bottom: 0;
  // }
  // /deep/.we-tabs {
  //   height: 418px;
  //   display: flex;
  //   flex-direction: column;
  //   .we-tabs__header {
  //     margin: 0 0 12px;
  //   }
  //   .we-tabs__content {
  //     flex: 1;
  //     min-height: 0;
  //   }
  //   .we-tabs__item {
  //     height: 48px;
  //     line-height: 48px;
  //     padding: 0 30px !important;
  //     position: relative;
  //     &.is-active {
  //       font-weight: bold;
  //     }
  //   }
  //   .we-tabs__nav-wrap::after {
  //     height: 1px;
  //     background: #f0f0f0;
  //   }
  //   .we-tabs__active-bar {
  //     display: none;
  //   }
  // }
}
.chooseBox__header {
  height: 48px;
  border-bottom: 1px solid #f0f0f0;
  border-radius: 4px 4px 0px 0px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  .iconfont {
    font-size: 14px;
    margin-right: 4px;
  }
}
.chooseBox__tag {
  background: #f5f5f5;
  border-radius: 4px;
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  line-height: 22px;
  margin: 0 8px 8px 0;
  word-break: break-all;
  .iconfont {
    margin-left: 6px;
    font-size: 12px;
  }
  &.hidden {
    display: none;
  }
}
.dialog-footer {
  .we-button {
    max-width: 475px;
    &:first-child:hover {
      background-color: #ffffff;
    }
  }
}
</style>
