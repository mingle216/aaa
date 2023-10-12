<template>
  <we-popup
    v-model="isShow"
    position="bottom"
    :style="{
      width: '100%',
      height: 'calc(100% - 61px)',
      borderRadius: '10px 10px 0 0',
    }"
    :overlay-style="{ opacity: 0.5, background: 'rgba(0, 0, 0, 0.5)' }"
    :close-on-click-overlay="false"
    get-container="body"
    @close="close"
    v-loading="loading"
  >
    <div class="subscribe">
      <header>
        <div class="header-top">
          <h3 class="portal-font-color-lv1">
            {{
              $Lan("CUS_CARD_CQNEWS_h5", "mySubscribe", "我的订阅")
            }}
          </h3>
          <span class="header-close" @click="handleClose"></span>
        </div>
      </header>
      <main>
        <div class="main-top">
          <div class="top-left portal-font-color-lv2">
            <span class="text__ellipsis">{{
              $Lan("CUS_CARD_CQNEWS_h5", "checkedChannels", "已选")
            }}</span>
            {{ list.length | formatNum }}
          </div>
          <div class="top-right" v-if="list.length">
            <we-button
              plain
              hairline
              class="graybtn portal-font-color-lv2"
              @click="handleClose"
              >{{
                $Lan("CUS_CARD_CQNEWS_h5", "cancel", "取消")
              }}</we-button
            >
            <we-button
              class="bluebth portal-primary-backgroundcolor-lv1"
              @click="handleComplete"
              >{{
                $Lan("CUS_CARD_CQNEWS_h5", "ok", "完成")
              }}</we-button
            >
          </div>
        </div>
        <template v-if="list.length">
          <div class="addChannel portal-primary-color-lv1" @click="add">
            <i class="icon-icon-xinzeng iconfont" style="margin-right: 6px"></i
            ><span class="text__ellipsis">{{
              $Lan("CUS_CARD_CQNEWS_h5", "addChannel", "订阅")
            }}</span>
          </div>
          <div class="channelList">
            <ul>
              <li
                :class="`portal-font-color-lv${!item.isFixed ? 1 : 2}`"
                v-for="(item, index) in list"
                :key="index"
              >
                <p class="ellipsis">{{ item.name }}</p>
                <span
                  class="del-icon"
                  v-if="!item.isFixed"
                  @click="handleDel(index)"
                ></span>
              </li>
            </ul>
          </div>
        </template>

        <template v-else>
          <EmptyCon
            :tip="$Lan('CUS_CARD_CQNEWS_h5', 'noChannel', '暂无栏目')"
          ></EmptyCon>
          <we-button
            class="bluebth nodata-addChannel portal-primary-backgroundcolor-lv1"
            @click="add"
            >{{
              $Lan("CUS_CARD_CQNEWS_h5", "addChannel", "订阅")
            }}</we-button
          >
        </template>
        <we-popup
          v-model="isShowAdd"
          position="right"
          :style="{ width: '100%', height: '100%' }"
          :overlay="false"
          class="updateChannel"
        >
          <div class="updateChannel-content">
            <main>
              <we-tabs
                v-model="active"
                :ellipsis="false"
                :sticky="true"
                class="subscribe-tabs"
                ref="tabs"
                @click="
                  (index) => {
                    changeChannel(tabList[index]);
                  }
                "
              >
                <we-tab
                  :title="item.name"
                  v-for="item in tabList"
                  :key="item.id"
                >
                  <ul class="updateChannel-list">
                    <li v-for="item in channelList" :key="item.id">
                      <div class="list-left">
                        <div class="list-check" @click="clickChecked(item)">
                          <div
                            class="check-son"
                            :class="
                              className[item.isFixed || item.disabled ? 1 : 0][
                                item.disabled ? 3 : item.checked
                              ]
                            "
                          ></div>
                        </div>
                        <p class="portal-font-color-lv2 ellipsis">
                          {{ item.name }}
                        </p>
                      </div>

                      <div
                        class="list-right portal-font-color-lv2"
                        @click="changeChannel(item)"
                        v-if="!item.isChannel"
                      >
                        {{ item.channelNum | formatNum }}
                        <div class="ellipsis">
                          {{
                            $Lan(
                              "CUS_CARD_CQNEWS_h5",
                              "channelTotal",
                              "个栏目"
                            )
                          }}
                        </div>
                      </div>
                    </li>
                  </ul>
                </we-tab>
              </we-tabs>
            </main>
            <footer>
              <we-button
                class="portal-primary-backgroundcolor-lv1"
                @click="submit"
                >{{
                  $Lan("CUS_CARD_CQNEWS_h5", "submit", "我选好了")
                }}</we-button
              >
            </footer>
          </div>
        </we-popup>
      </main>
    </div>
  </we-popup>
</template>
<script>
import { clickTrack } from "../mixins/track.js";
export default {
  props: {
    isShow: Boolean,
    tabs: {
      type: Array,
      default() {
        return [];
      },
    },
    card: {
      type: Object,
      default() {
        return {};
      },
    },
    router: Object,
    subscribeProgramme: String, //是否开启站点和频道不关联，1为不关联
  },
  model: {
    prop: "isShow",
  },
  mixins: [clickTrack],
  watch: {
    isShow(newval) {
      if (newval) {
        this.resetLists(); // 重置数据的选中状态
        this.list =
          (this.originalList.length &&
            this.originalList.reduce((v, m) => {
              let res = this.tabs.some((w) => w.wid === m.id);
              res && v.push(m);
              return v;
            }, [])) ||
          [];
        this.list.forEach((v) => {
          v.checked = 1;
          this.changeCheck(v);
        });
        // this.currentId = "-1";
        return;
      }
    },
    currentId(newVal) {
      this.getChannelList(newVal);
      let index = this.tabList.findIndex((v) => v.id === newVal);

      if (index === -1) {
        this.tabList.push({
          name: this.currentName,
          id: newVal,
        });
      } else {
        this.tabList = this.tabList.slice(0, index + 1);
      }
      this.active = this.tabList.length - 1;
      this.$refs.tabs && this.$refs.tabs.scrollTo(this.active);
    },
  },
  data() {
    const firstLevel = this.$Lan(
      "CUS_CARD_CQNEWS_h5",
      "firstLevel",
      "一级站点"
    );
    return {
      loading: false,
      isShowAdd: false,
      active: 0,
      currentId: "",
      currentName: "",
      checked: null,
      originalList: [],
      channelList: [],
      list: [],
      allList: [],
      tabList: [{ name: firstLevel, id: "-1" }],
      className: [
        [
          "",
          "isChecked portal-primary-backgroundcolor-lv1",
          "selective portal-primary-backgroundcolor-lv1",
        ],
        ["", "fixChecked fixBg", "fixSelective fixBg", "fixBg"],
      ],
    };
  },
  methods: {
    close() {
      this.$emit("input", false);
    },
    handleClose() {
      this.close();
      this.handleClickTrack(); // 点击埋点
    },
    execCardMethod(params, callback) {
      window.shell.execCardMethod(params, (data) => {
        callback && typeof callback === "function" && callback(data);
      });
    },
    getAllList(data) {
      this.filterData(data);
      this.currentId = "-1";
    },
    changeChannel({ id, name }) {
      this.currentId = id;
      this.currentName = name;
      this.handleClickTrack(); // 点击埋点
    },
    filterData(data) {
      let list = data.map((v) => {
        return {
          ...v,
          disabled: false,
          childrenList: [],
          channelNum: v.isChannel ? 1 : 0,
          checked: this.tabs.some((w) => w.wid === v.id)
            ? 1
            : v.isChannel
            ? 0
            : 1,
        };
      });
      const setTreeList = (node) => {
        if (node.parentId === "-1") return;
        list.some((v) => {
          if (v.id === node.parentId) {
            node.parent = v;
            node.disabled =
              this.subscribeProgramme === "1" && v.checked && !node.isFixed
                ? true
                : false;
            if (node.isFixed && !this.subscribeProgramme === "1") {
              v.isFixed = true;
            }

            if (v.childrenList.some((v) => v.id === node.id)) {
              v.channelNum++;
              setTreeList(v);
              return true;
            }

            v.childrenList.push(node);
            v.channelNum += node.channelNum;
            setTreeList(v);
            return true;
          }
        });
      };
      list.forEach((v) => {
        v.isChannel && setTreeList(v);
      });
      this.allList = list.filter((v) => v.parentId === "-1");
      list.filter((v) => v.isChannel).forEach((v) => this.changeCheck(v));
      this.originalList = list;
    },
    resetLists() {
      let recursion = (data) => {
        data.forEach((element) => {
          element.checked = 0;
          if (element.childrenList && element.childrenList.length) {
            recursion(element.childrenList);
          }
        });
      };
      recursion(this.originalList);
      recursion = null;
    },
    clickChecked(item) {
      if (item.disabled) {
        return;
      }
      if (item.checked === 1) {
        if (item.isChannel && item.isFixed) {
          return window.shell.showMessage({
            message: this.$Lan(
              "CUS_CARD_CQNEWS_h5",
              "fixedChannelCancelTip",
              "固定栏目不可取消订阅"
            ),
            type: "success",
          });
        }
        if (!item.isChannel && item.isFixed) {
          return window.shell.showMessage({
            message: this.$Lan(
              "CUS_CARD_CQNEWS_h5",
              "fixedColumnCancelTip",
              "该站点包含固定栏目不可取消订阅"
            ),
            type: "success",
          });
        }
        item.checked = 0;
      } else {
        item.checked = 1;
      }
      this.changeCheck(item);
      this.handleClickTrack(); // 点击埋点
    },
    changeCheck(item) {
      let setChecked = (arr) => {
        if ((arr.includes(0) && arr.includes(1)) || arr.includes(2)) {
          return 2;
        } else if (!arr.includes(1)) {
          return 0;
        } else {
          return 1;
        }
      };
      let setParent = (node) => {
        if (!node.parent) return;
        let arr = [];
        node.parent.childrenList.forEach((v) => {
          arr.push(v.checked);
        });
        node.parent.checked = setChecked(arr);
        setParent(node.parent);
      };
      let setSon = (node) => {
        if (!node.childrenList.length) return;
        node.childrenList.forEach((v) => {
          if (this.subscribeProgramme === "1") {
            v.checked = v.isFixed ? 1 : 0;
          } else {
            v.checked = node.checked;
          }
          v.disabled =
            this.subscribeProgramme === "1" && node.checked && !v.isFixed
              ? true
              : false;
          setSon(v);
        });
      };
      if (this.subscribeProgramme !== "1") {
        setParent(item);
      }
      setSon(item);
    },
    getChannelList(id) {
      let flag = true;
      if (id === "-1") return (this.channelList = this.allList);
      let fn = (node) => {
        if (!flag) {
          return;
        }
        if (node.id === id) {
          flag = false;
          return (this.channelList = node.childrenList);
        } else {
          node.childrenList.forEach((w) => fn(w));
        }
      };
      this.allList.some((v) => {
        fn(v);
        return !flag;
      });
      this.channelList = [
        ...this.channelList.filter((v) => !v.isChannel),
        ...this.channelList.filter((v) => v.isChannel),
      ];
    },

    add() {
      this.isShowAdd = true;
      this.currentId = "-1";
      this.handleClickTrack(); // 点击埋点
    },
    submit() {
      if (this.subscribeProgramme === "1") {
        this.list = this.originalList.filter((v) => v.checked) || [];
      } else {
        this.list =
          this.originalList.filter((v) => v.isChannel && v.checked) || [];
      }

      this.isShowAdd = false;
      this.handleClickTrack(); // 点击埋点
      // this.handleComplete();
    },
    handleDel(index) {
      this.list[index].checked = 0;
      this.changeCheck(this.list[index]);
      this.list.splice(index, 1);
      this.handleClickTrack(); // 点击埋点
    },
    handleComplete() {
      this.handleClickTrack(); // 点击埋点
      if (this.loading) return;
      this.loading = true;
      const channelIds = [];
      const programIds = [];
      this.list.forEach((element) => {
        if (element.isChannel) {
          channelIds.push(element.id);
        } else {
          programIds.push(element.id);
        }
      });

      this.execCardMethod(
        {
          ...this.card,
          method: "subscribeChannel",
          param: {
            channelIds: channelIds.join(","),
            programIds: programIds.join(","),
          },
        },
        (data) => {
          if (data.errcode === "0") {
            window.shell.emit("update-news");
            // this.$emit("renderTab");
          }
          this.isShowAdd = false;
          this.close();
          this.loading = false;
        }
      );
    },
  },

  filters: {
    formatNum(value) {
      let num = value;
      if (typeof num !== "number") {
        num = Number(num);
      }
      if (isNaN(num)) {
        return 0;
      }
      return num > 99 ? "99+" : num;
    },
  },
};
</script>
<style lang="less" scoped>
.graybtn {
  background: #ffffff;
  border-color: #d6dade;
  border-radius: 4px;
}
.bluebth {
  border-radius: 4px;
  color: white;
}
.nodata-addChannel {
  font-size: 16px;
  line-height: 20px;
  width: 164px;
  height: 40px;
  margin: 0 auto;
  display: block;
}
.subscribe {
  font-size: 14px;
  letter-spacing: 0;
  // font-family: PingFangSC-Regular;
  height: 100%;
  > header {
    > .header-top {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 56px;
      line-height: 56px;
      // font-family: PingFangSC-Semibold;
      font-weight: bold;
      font-size: 18px;
      padding: 0 17px;
      border-bottom: 1px solid #e7edf1;
      > .header-close {
        height: 24px;
        width: 24px;
        background: url("../assest/images/icon-清除@3x.png") no-repeat
          center/100% 100%;
      }
    }
  }
  > main {
    height: calc(100% - 56px);
    transform: translateX(0px);
    > .main-top {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 48px;
      padding: 10px 17px;
      > .top-left {
        line-height: 18px;
        margin-right: 8px;
        flex: 1;
        min-width: 0;
        display: flex;
        align-items: center;
        &::before {
          content: "";
          background: url("../assest/images/icon-site.png") no-repeat
            center/100% 100%;
          height: 12.8px;
          width: 14px;
          margin-right: 6px;
          display: inline-block;
          flex-shrink: 0;
        }
      }
      > .top-right {
        > button {
          height: 28px;
          // width: 60px;
          max-width: 88px;
          /deep/.we-button__text {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
        > button:first-child {
          margin-right: 8px;
        }
      }
    }
    > .addChannel {
      margin: 0 17px;
      padding: 0 20px;
      border: 1px dashed #d6dade;
      border-radius: 4px;
      height: 42px;
      font-size: 16px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    > .channelList {
      height: calc(100% - 102px);
      overflow-y: auto;
      padding-top: 12px;
      > ul {
        padding: 0 17px 0 17px;
        display: flex;
        flex-wrap: wrap;

        > li {
          width: calc(33% - 8.667px);
          background: #f6f6f8;
          border-radius: 4px;
          height: 36px;
          text-align: center;
          line-height: 36px;
          padding: 0 6px;
          margin-bottom: 12px;
          position: relative;
          margin-right: 13px;
          > p {
            letter-spacing: 0;
          }
          > .del-icon {
            display: inline-block;
            height: 16px;
            width: 16px;
            background: url("../assest/images/icon-搜索-关闭备份 3@3x.png")
              no-repeat center/100% 100%;
            position: absolute;
            right: -8px;
            top: -8px;
          }
        }
        > li:nth-of-type(3n + 3) {
          margin-right: 0px;
        }
      }
    }

    .updateChannel {
      font-size: 16px;
      /deep/ .we-tabs--line .we-tabs__wrap {
        border-bottom: 1px solid #e7edf1;
        position: relative;
        height: 48px;

        &::before {
          position: absolute;
          top: 0;
          left: 0;
          content: "";
          height: 100%;
          width: 17px;
          background-image: linear-gradient(
            90deg,
            #ffffff 0%,
            rgba(255, 255, 255, 0) 100%
          );
          z-index: 1;
        }
        .we-tabs__nav--complete {
          padding: 0;
          padding-left: 17px;
        }
        .we-tab {
          padding: 0 8px 0 0;
          font-size: 16px;

          flex: none;
          &::after {
            background: url("../assest/images/icon-list-right@3x.png") no-repeat
              center/100% 100%;
            height: 14px;
            width: 14px;
            margin-left: 2px;
            content: "";
          }
        }
        .we-tab--active {
          &::after {
            display: none;
          }
        }
      }
      /deep/ .we-tabs__line {
        display: none;
      }
      > .updateChannel-content {
        display: flex;
        flex-direction: column;
        height: 100%;
        > main {
          flex: 1;
          height: calc(100% - 56px);
          overflow-y: auto;
          .updateChannel-list {
            padding: 0 17px;
            width: 100vw;
            > li {
              height: 50px;
              line-height: 50px;
              display: flex;
              justify-content: space-between;
              border-bottom: 1px solid #e7edf1;
              width: 100%;
              > .list-left {
                display: flex;
                overflow: hidden;
                // width: calc(100% - 88px);
                > .list-check {
                  width: 32px;
                  display: flex;
                  align-items: center;
                  flex-shrink: 0;
                  .check-son {
                    height: 16px;
                    width: 16px;
                    border: 1px solid #d6dade;
                    border-radius: 2px;
                    position: relative;
                    &::before {
                      content: "";
                      display: none;
                    }
                  }
                  .check-son.isChecked {
                    border: none;
                    &::before {
                      display: block;
                      background: url("../assest/images/icon-白勾.png")
                        no-repeat center/100% 100%;
                      height: 100%;
                      width: 100%;
                    }
                  }
                  .check-son.selective {
                    border: none;
                    &::before {
                      display: block;
                      background: white;
                      height: 1px;
                      width: 8px;
                      position: absolute;
                      top: 0;
                      left: 0;
                      right: 0;
                      bottom: 0;
                      margin: auto;
                    }
                  }
                  .check-son.fixChecked {
                    border: none;
                    &::before {
                      display: block;
                      background: url("../assest/images/icon-灰勾.png")
                        no-repeat center/100% 100%;
                      height: 100%;
                      width: 100%;
                    }
                  }
                  .check-son.fixSelective {
                    border: none;
                    &::before {
                      display: block;
                      background: #d6dade;
                      height: 1px;
                      width: 8px;
                      position: absolute;
                      top: 0;
                      left: 0;
                      right: 0;
                      bottom: 0;
                      margin: auto;
                    }
                  }
                  .check-son.fixBg {
                    background: rgba(223, 230, 236, 0.5);
                  }
                }
                > p {
                  flex: 1;
                  overflow: hidden;
                }
              }
              > .list-right {
                width: 88px;
                font-size: 12px;
                display: flex;
                align-items: center;
                flex-shrink: 0;
                justify-content: flex-end;
                &::after {
                  background: url("../assest/images/icon-list-right@3x.png")
                    no-repeat center/100% 100%;
                  height: 14px;
                  width: 14px;
                  margin-left: 6px;
                  content: "";
                  display: inline-block;
                  flex-shrink: 0;
                }
              }
            }
          }
        }
        > footer {
          background: #ffffff;
          box-shadow: 0 -4px 8px 0 rgba(112, 125, 143, 0.1);
          height: 56px;
          flex-shrink: 0;
          padding: 8px 17px;
          > .we-button {
            width: 100%;
            font-size: 16px;
            color: #ffffff;
            line-height: 20px;
            border: none;
            border-radius: 4px;
          }
        }
      }
    }
  }
}
</style>
