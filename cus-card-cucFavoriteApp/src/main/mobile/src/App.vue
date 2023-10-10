<template>
  <div class="card-favoriteApp" v-loading="loading">
    <div class="manage-btn" v-show="appListRes.length">
      <div
        class="glsc_box"
        :style="{ color: defaultColor }"
        @click="
          () => {
            //this.showManage = true;
            this.checkMore(true)
            this.handleClickTrack();
          }
        "
      >
        <we-icon
          id="card-favoriteApp-icon"
          :style="{ display: 'block' }"
          class="s_icon"
          name="setting-o"
        />
        <div class="text11" fs-14>
          {{ $Lan(lanFunName, "manageCollections", "管理收藏") }}
        </div>
      </div>
      <!-- <div
        v-show="showManage"
        style="display: flex; height: 100%; align-items: center"
      >
        <div
          id="card-favoriteApp-btn1"
          class="btn_div cancel"
          @click="cancelManage('cancel')"
        >
          {{ $Lan(lanFunName, "cancel", "取消") }}
        </div>
        <div
          id="card-favoriteApp-btn2"
          :style="{ 'background-color': defaultColor }"
          class="btn_div commit"
          @click="closeOrSave"
        >
          {{ $Lan(lanFunName, "confirm", "保存") }}
        </div>
      </div> -->
    </div>

    <!-- 列表 一行一个 -->
    <template v-if="showColumns === 1">
      <we-row class="content-list">
        <draggable
          v-model="appList"
          handle=".drag-icon"
          delay="0"
          ghostClass="drag-ghost"
          dragClass="drag-handle"
          chosenClass="drag-chosen"
          forceFallback="true"
          animation="500"
          @update="updateLocation(true)"
        >
          <transition-group>
            <we-col
              span="24"
              v-for="(item, index) in appList.filter(
                (v, i) => i < showTotalNum
              )"
              :key="`fa_${index}`"
              @click="openLink(item)"
            >
              <div class="list-item" v-if="index < showTotalNum">
                <i
                  v-show="showManage"
                  class="iconfont icon-delete"
                  fs-16
                  @click.stop="collectAppBatch(item, index)"
                ></i>
                <span class="item-icon">
                  <img
                    v-if="item.serviceStation === 0"
                    :src="item.iconLink ? item.iconLink : ''"
                    :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                    :onerror="errorImg"
                  />
                  <img
                    v-else
                    :src="
                      item.mobileIconLink ? item.mobileIconLink : item.iconLink
                    "
                    :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                    :onerror="errorImg"
                  />
                  <div
                    v-if="!item.permission"
                    style="
                      position: absolute;
                      width: 100%;
                      height: 100%;
                      top: 0;
                      left: 0;
                      background-color: rgba(0, 0, 0, 0.5);
                      display: flex;
                      justify-content: center;
                      align-items: center;
                    "
                  >
                    <img
                      style="width: 0.533333rem; height: 0.533333rem"
                      :src="require('./assets/lock.png')"
                      :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                    />
                  </div>
                </span>
                <p class="title text__ellipsis1">
                  {{ item.serviceName }}
                </p>
                <i
                  v-show="!showManage"
                  class="iconfont icon-favorites favorite_font"
                  @click.stop="collectApp(item, index)"
                ></i>
                <span v-show="showManage" class="drag-icon"
                  ><img :src="dragIcon" height="12" width="28" alt=""
                /></span>
              </div>
            </we-col>
          </transition-group>
        </draggable>
      </we-row>
    </template>

    <!-- 一行2个 -->
    <template v-else>
      <we-row
        class="content-list temp2"
        type="flex"
        justify="space-between"
        gutter="13"
      >
        <we-col
          v-for="(item, index) in appList.filter((v, i) => i < showTotalNum)"
          :key="`fa_${index}`"
          @click="openLink(item)"
        >
          <div class="list-item" v-if="index < showTotalNum">
            <span class="item-icon">
              <img
                v-if="item.serviceStation === 0"
                :src="item.iconLink ? item.iconLink : ''"
                :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                :onerror="errorImg"
              />
              <img
                v-else
                :src="item.mobileIconLink ? item.mobileIconLink : item.iconLink"
                :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                :onerror="errorImg"
              />
              <div
                v-if="!item.permission"
                style="
                  position: absolute;
                  width: 100%;
                  height: 100%;
                  top: 0;
                  left: 0;
                  background-color: rgba(0, 0, 0, 0.5);
                  display: flex;
                  justify-content: center;
                  align-items: center;
                "
              >
                <img
                  style="width: 0.533333rem; height: 0.533333rem"
                  :src="require('./assets/lock.png')"
                  :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                />
              </div>
            </span>
            <p class="title">{{ item.serviceName }}</p>
            <i
              class="iconfont icon-favorites favorite_font"
              @click.stop="collectApp(item, index)"
            ></i>
          </div>
        </we-col>
      </we-row>
    </template>

    <we-button
      :style="{ color: defaultColor }"
      class="expand-btn"
      block
      v-show="moreShow"
      @click="checkMore(false)"
    >
      {{ $Lan(lanFunName, "moreCollections", "更多收藏") }}
      <we-icon name="arrow" size="12" />
    </we-button>
    <!--  缺省 -->
    <EmptyCon
      v-if="!appList.length || !appListRes.length"
      :tip="$Lan(lanFunName, 'tipMsg', '还没有收藏服务哦')"
    ></EmptyCon>
    <!-- 更多弹框 -->
    <custom-action-sheet
      v-model="dialogShow"
      class="all-list-dialog1"
      :close-on-click-overlay="false"
    >
      <div class="all-content">
        <div class="we-action-sheet__header">
          {{
            showManage
              ? $Lan(lanFunName, "manageCollections", "管理收藏")
              : cardName
          }}<i
            @click="popClose"
            class="we-icon we-icon-cross we-action-sheet__close"
            ><!----></i
          >
        </div>
        <we-row class="content-list list-all">
          <EmptyCon
            style="margin-top: 4rem"
            v-if="!appList.length || !appListRes.length"
            :tip="$Lan(lanFunName, 'tipMsg', '还没有收藏服务哦')"
          ></EmptyCon>
          <draggable
            v-model="appList"
            handle=".drag-icon"
            delay="0"
            ghostClass="drag-ghost"
            dragClass="drag-handle"
            chosenClass="drag-chosen"
            forceFallback="true"
            animation="500"
            @update="updateLocation(true)"
          >
            <transition-group>
              <we-col
                span="24"
                class="list-item"
                v-for="(item, index) in appList"
                :key="`fa_${index}`"
                :data-wid="item.serviceWid"
                @click="openLink(item)"
              >
                <div
                  :style="{ width: showManage ? 'calc(100% - 1rem)' : '100%' }"
                  style="
                    display: flex;
                    width: 100%;
                    height: 100%;
                    align-items: center;
                    overflow: hidden;
                  "
                >
                  <i
                    v-show="showManage"
                    class="iconfont icon-delete"
                    fs-16
                    @click.stop="collectAppBatch(item, index)"
                  ></i>
                  <span class="item-icon" style="margin-right: 0.32rem">
                    <img
                      v-if="item.serviceStation === 0"
                      :src="item.iconLink ? item.iconLink : ''"
                      :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                      :onerror="errorImg"
                    />
                    <img
                      v-else
                      :src="
                        item.mobileIconLink
                          ? item.mobileIconLink
                          : item.iconLink
                      "
                      :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                      :onerror="errorImg"
                    />
                    <div
                      v-if="!item.permission"
                      style="
                        position: absolute;
                        width: 100%;
                        height: 100%;
                        top: 0;
                        left: 0;
                        background-color: rgba(0, 0, 0, 0.5);
                        display: flex;
                        justify-content: center;
                        align-items: center;
                      "
                    >
                      <img
                        style="width: 0.533333rem; height: 0.533333rem"
                        :src="require('./assets/lock.png')"
                        :alt="$Lan(lanFunName, 'altTip', '加载失败')"
                      />
                    </div>
                  </span>
                  <p class="title text__ellipsis1">
                    {{ item.serviceName }}
                  </p>
                </div>
                <i
                  style="display: none"
                  class="iconfont icon-favorites favorite_font"
                  @click.stop="collectApp(item, index)"
                ></i>
                <span v-show="showManage" class="drag-icon"
                  ><img :src="dragIcon" height="12" width="28" alt=""
                /></span>
              </we-col>
            </transition-group>
          </draggable>
        </we-row>

        <!-- footer -->
        <div class="all-footer">
          <span class="btn default" @click="manageOrCancel">{{
            showManage
              ? $Lan(lanFunName, "cancel", "取消")
              : $Lan(lanFunName, "manageCollections", "管理收藏")
          }}</span>
          <span
            :style="{ 'background-color': defaultColor }"
            class="btn primary"
            @click="closeOrSave"
            >{{
              showManage
                ? $Lan(lanFunName, "confirm", "保存")
                : $Lan(lanFunName, "close", "取消")
            }}</span
          >
        </div>
      </div>
    </custom-action-sheet>
  </div>
</template>

<script>
import draggable from "vuedraggable";
import TrackMixin from "./mixins/track.js";
export default {
  components: { draggable },
  name: "FavoriteApp",
  props: {
    index: Number,
    router: Object,
  },
  mixins: [TrackMixin],
  data() {
    const { cardWid, cardId } = this.router;
    return {
      lanFunName: this.router.cardId + "_h5",
      loading: false,
      showManage: false,
      moreShow: false,
      dialogShow: false,
      pageTitle: window.shell.getPageData().pageTitle,
      card: {
        cardWid,
        cardId,
      },
      errorImg: 'this.src="' + require("./assets/default.jpg") + '"',
      dragIcon: require("./assets/drag.png"),
      appList: [],
      appListRes: [],
      appIds: [],
      showColumns: 1,
      configInfo: {},
      defaultColor: "white",
      cardName: "",
    };
  },
  created() {
    window.shell.on("collectApp", this.getCardData);
    window.shell.on("card-tab-change", () => {
      this.showManage = false;
      this.getCardData();
    });
    this.SETModalTitle();
  },
  mounted() {
    this.defaultColor = JSON.parse(
      JSON.parse(
        window.shell.getPageData().pageContext.showProgrammeEntity
          .templateConfig
      )
    ).themeColorSetting["portal-primary-color-lv1"];
    if (window.shell) {
      this.getCardData();
    }
    setTimeout(() => {
      let userAgent = window.navigator.userAgent;
      //安卓机，微信浏览器 && 钉钉浏览器  样式需要微调
      if (userAgent.indexOf("Android") > -1 || userAgent.indexOf("Adr") > -1) {
        if (userAgent.indexOf("MicroMessenger") > -1) {
          //微信浏览器
          document.getElementById("card-favoriteApp-icon").style.marginTop =
            "3px";
          document.getElementById("card-favoriteApp-btn1").style.paddingTop =
            "3px";
          document.getElementById("card-favoriteApp-btn2").style.paddingTop =
            "3px";
        }
        if (userAgent.indexOf("DingTalk") > -1) {
          //钉钉浏览器
          document.getElementById("card-favoriteApp-icon").style.marginTop =
            "3px";
          document.getElementById("card-favoriteApp-btn1").style.paddingTop =
            "3px";
          document.getElementById("card-favoriteApp-btn2").style.paddingTop =
            "3px";
        }
      }
    }, 1000);
  },
  beforeDestroy() {
    window.shell.off("collectApp");
    window.shell.off("card-tab-change");
  },
  computed: {
    showTotalNum() {
      return this.configInfo.columns * this.configInfo.rows;
    },
  },
  methods: {
    getCardData() {
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "renderData",
          param: {},
        },
        (data) => {
          this.loading = false;
          this.loadedEndTrack(); // 页面加载结束埋点
          if (data.errcode === 200 || data.errcode === "0") {
            this.configInfo = data.data.config;
            this.showColumns = data.data.config.columns;
            this.appList = !data.data.appList ? [] : data.data.appList;
            this.appListRes = !data.data.appList ? [] : data.data.appList;
            this.updateLocation();
            this.showMoreBtn();
          }
        }
      );
    },

    // 取消收藏
    collectApp(it) {
      this.loading = true;
      let { serviceWid: appId } = it;

      window.shell.collectService(
        {
          id: appId,
          operate: 0, //  0:取消收藏 1:收藏
        },
        async (res) => {
          if (res.errcode === "0") {
            this.setCollect(it, "single");
          }
        }
      );
      this.handleClickTrack({
        infoType: 0,
        serviceId: appId,
        fucType: 3,
      }); // 点击埋点

      // this.execCardMethod(
      //   {
      //     ...this.card,
      //     method: "collectApp",
      //     param: {
      //       appId,
      //       operate: 0,
      //       id: appId,
      //     },
      //   },
      //   (data) => {
      //     this.loading = false;
      //     console.log("data2222222", data);
      //     if (data.errcode === 200 || data.errcode === "0") {
      //       this.setCollect(it, "single");
      //     } else {
      //       window.shell.showMessage({
      //         type: "warning",
      //         message: data.errmsg,
      //       });
      //     }
      //   }
      // );
      // this.$dialog.confirm({
      //   message: '取消收藏此服务？',
      //   cancelButtonText:'取消',
      //   cancelButtonColor:'#707D8F',
      //   confirmButtonText:'确定',
      //   confirmButtonColor:'#307AFB'
      // }).then(() => {

      // })
    },

    // 取消收藏
    setCollect(it, type) {
      // this.appListRes.splice(index, 1);
      // this.appList.splice(index, 1);
      window.shell.showMessage({
        type: "success",
        message: type
          ? this.$Lan(this.lanFunName, "collectMessage1", "取消收藏成功")
          : this.$Lan(this.lanFunName, "collectMessage2", "保存成功"),
        duration: 3000,
      });
      window.shell.emit("collectApp", {
        cardId: this.card.cardId,
        wid: typeof it === "string" || !it ? it : it.serviceWid,
        favorite: false,
      });
    },

    // 管理批量删除
    collectAppBatch(item, index) {
      let cacheAppList = JSON.parse(JSON.stringify(this.appList));
      cacheAppList.splice(index, 1);
      this.appList = cacheAppList;
      this.updateLocation();
      this.showMoreBtn();
      this.handleClickTrack(); // 点击埋点
    },

    // 管理/取消收藏
    manageOrCancel() {
      if (this.showManage) {
        this.showManage = false;
        this.appList = JSON.parse(JSON.stringify(this.appListRes));
      } else {
        this.showManage = true;
      }
      this.showMoreBtn();
      this.handleClickTrack(); // 点击埋点
    },

    // 保存/关闭收藏
    closeOrSave() {
      if (this.showManage) {
        this.showManage = false;
        this.sortApp();
      } else {
        this.showManage = false;
        this.dialogShow = false;
        this.showMoreBtn();
      }
      this.handleClickTrack(); // 点击埋点
    },

    sortApp() {
      this.execCardMethod(
        {
          ...this.card,
          method: "sort",
          param: {
            serviceItemIds: this.appIds.join(","),
          },
        },
        (data) => {
          if (data.errcode === 200 || data.errcode === "0") {
            this.setCollect(this.appIds.join(","));
          } else {
            window.shell.showMessage({
              type: "warning",
              message: data.errmsg,
            });
          }
        }
      );
    },
    popClose() {
      this.cancelManage("");
    },
    // 取消管理收藏
    cancelManage(type) {
      this.handleClickTrack(); // 点击埋点
      if (type && type === "cancel") {
        //直接取消
        this.appList = JSON.parse(JSON.stringify(this.appListRes));
        this.showManage = false;
        this.dialogShow = false;
        this.showMoreBtn();
      } else {
        //弹窗关闭
        if (this.showManage) {
          this.$dialog
            .confirm({
              message: this.$Lan(
                this.lanFunName,
                "manageMessage",
                "是否保存对收藏的修改?"
              ),
              cancelButtonText: this.$Lan(
                this.lanFunName,
                "cancelText",
                "放弃更改"
              ),
              cancelButtonColor: "#707D8F",
              confirmButtonText: this.$Lan(
                this.lanFunName,
                "confirmText",
                "保存修改"
              ),
              confirmButtonColor: this.defaultColor || "#307AFB",
            })
            .then(() => {
              this.showManage = false;
              this.dialogShow = false;
              this.sortApp();
              this.handleClickTrack(); // 点击埋点
            })
            .catch(() => {
              this.appList = JSON.parse(JSON.stringify(this.appListRes));
              this.showManage = false;
              this.dialogShow = false;
              this.showMoreBtn();
              this.handleClickTrack(); // 点击埋点
            });
        } else {
          this.dialogShow = false;
          this.showMoreBtn();
        }
      }
    },

    openLink(item) {
      this.handleClickTrack({
        infoType: 0,
        serviceId: item.serviceWid,
      }); // 点击埋点
      if (this.showManage) {
        return;
      }
      if (!item.permission) {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan(
            this.lanFunName,
            "goLinkMessage",
            "暂无使用权限，请联系管理员"
          ),
        });
        return false;
      }
      window.shell.openService({
        ...item,
        wid: item.serviceWid,
        name: item.serviceName,
      });
    },

    // 查看更多收藏
    checkMore(isEdit) {
      this.dialogShow = true;
      if(isEdit){
        this.showManage = true;
      }else{
        this.showManage = false;
      }
      window.shell.emit("more-favoriteApp", this.dialogShow);
      this.handleClickTrack(); // 点击埋点
    },

    // 位置更新
    updateLocation(isTrack) {
      this.appIds = [];
      this.appList.forEach((item) => {
        this.appIds.push(item.serviceWid);
      });
      isTrack && this.handleClickTrack(); // 点击埋点
    },

    // 校验是否满足展开条件
    showMoreBtn() {
      const rowsNum = this.configInfo.rows;
      const columnNum = this.configInfo.columns;
      const taskLen = this.appList.length;
      this.moreShow = taskLen > rowsNum * columnNum ? true : false;
    },

    execCardMethod(params, callback) {
      window.shell.execCardMethod(params, (data) => {
        callback && typeof callback === "function" && callback(data);
      });
    },
    SETModalTitle() {
      let card_title = this.router.layoutCardTitle;
      const zhTitle = card_title.cardTitle || "";
      const cardTitleLang = (card_title && card_title.cardTitleLang) || [];
      const temp = cardTitleLang.find(
        (el) => el.langCode === this.$i18n.locale
      );
      this.cardName = (temp && temp.langName) || zhTitle;
    },
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 5000);
      }
    },
    showManage(n) {
      if (n) {
        this.showColumns = 1;
        if (this.dialogShow) {
          // window.shell.setBroswerTitle('管理收藏')
        }
      } else {
        this.showColumns = this.configInfo.columns;
        if (this.dialogShow) {
          // window.shell.setBroswerTitle('我的收藏')
        }
      }
    },
    dialogShow(n) {
      window.shell.emit("more-favoriteApp", n);
      if (!n) {
        // window.shell.setBroswerTitle(this.pageTitle)
      } else {
        if (this.showManage) {
          // window.shell.setBroswerTitle('管理收藏')
        } else {
          // window.shell.setBroswerTitle('我的收藏')
        }
      }
    },
  },
};
</script>

<style lang="less" scoped>
[fs-14] {
  font-size: 14px;
}
[fs-16] {
  font-size: 16px;
}
/deep/ .we-action-sheet__content {
  height: 100%;
  overflow: hidden;
}
.card-favoriteApp {
  width: 100%;
  padding: 0 17px;
  .manage-btn {
    height: 46px;
    line-height: 46px;
    .glsc_box {
      display: flex;
      align-items: center;
      width: 100%;
      height: 100%;
      line-height: initial;
      .s_icon {
        font-size: 0.426667rem;
      }
    }
    .text11 {
      font-size: 14px;
      line-height: initial;
      margin-left: 0.106667rem;
    }
    span.btn {
      background: #ffffff;
      border-radius: 4px;
      font-size: 14px;
      letter-spacing: 0;
      text-align: justify;
      display: flex;
      width: 60px;
      height: 28px;
      justify-content: center;
      align-items: center;
    }
    .btn_div {
      background: #ffffff;
      border-radius: 4px;
      font-size: 14px;
      letter-spacing: 0;
      max-width: 164px;
      min-width: 60px;
      height: 28px;
      line-height: 28px;
      justify-content: center;
      align-items: center;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      text-align: center;
    }
    .cancel {
      color: #707d8f;
      border: 1px solid #d6dade;
      margin-right: 8px;
    }
    .commit {
      color: #fff;
    }
  }

  .content-list {
    .list-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 11px 0;
      .title {
        flex: 1;
        min-width: 0;
        margin: 0 8px 0 12px;
        font-size: 16px;
        color: #102645;
        letter-spacing: 0;
        line-height: 20px;
      }
      .item-icon {
        flex-shrink: 0;
        position: relative;
        display: inline-block;
        width: 36px;
        height: 36px;
        font-size: 12px;
        border-radius: 0.18667rem;
        overflow: hidden;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .iconfont.icon-delete {
        color: #f00;
        margin-right: 12px;
      }
      .iconfont.icon-favorites {
        font-size: 13px;
        flex-shrink: 0;
      }
    }
  }
  .temp2 {
    .we-col {
      width: calc(~"(100% -  13px) / 2 ");
      padding: 0 !important;
    }
    .list-item {
      height: 56px;
      background: #f6f6f8;
      border-radius: 4px;
      width: 100%;
      display: flex;
      align-items: center;
      padding: 10px 6px 10px 12px !important;
      margin-bottom: 12px;
      .title {
        font-size: 14px;
        line-height: 18px;
        overflow: hidden;
        text-overflow: ellipsis;
        word-break: break-all;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }
    }
  }
}
// 全部收藏弹框
.all-list-dialog1 {
  .all-content {
    height: 100%;
    /deep/ .we-icon {
      color: #102645;
    }
    /deep/ .we-nav-bar__title {
      // font-family: PingFangSC-Semibold;
      font-size: 18px;
      color: #102645;
      letter-spacing: 0;
      text-align: center;
      line-height: 22px;
      font-weight: bold;
    }
  }
  .list-all {
    padding: 0 17px;
    height: calc(100% - 2.986667rem);
    overflow-y: auto;
  }
  .list-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 11px 0;
    .title {
      width: calc(~"100% - 80px");
      // font-family: PingFangSC-Regular;
      font-size: 16px;
      color: #102645;
      letter-spacing: 0;
      text-align: justify;
      line-height: 20px;
    }
    .item-icon {
      position: relative;
      display: inline-block;
      width: 36px;
      height: 36px;
      font-size: 12px;
      border-radius: 0.18667rem;
      overflow: hidden;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .iconfont.icon-delete {
      color: #f00;
      margin-right: 12px;
    }
    .iconfont.icon-favorites {
      font-size: 13px;
    }
  }
  .all-footer {
    height: 56px;
    width: 100%;
    background: #ffffff;
    box-shadow: 0 -4px 8px 0 rgba(112, 125, 143, 0.1);
    // position: fixed;
    // bottom: 0;
    padding: 0 17px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .btn {
      display: inline-block;
      width: calc(~"(100% - 13px) / 2");
      height: 40px;
      border-radius: 4px;
      // font-family: PingFangSC-Regular;
      font-size: 16px;
      letter-spacing: 0;
      text-align: center;
      line-height: 40px;
    }
    .btn.default {
      background: #ffffff;
      border: 1px solid #e7edf1;
      color: #707d8f;
    }
    .btn.primary {
      // background: #307afb;
      color: #ffffff;
    }
  }
}
.expand-btn {
  height: 48px;
  background: #f9fafb;
  border-radius: 4px;
  // color: #307afb;
  border: none;
  font-size: 16px;
  margin-bottom: 12px;
}
.drag-chosen {
  background: #ffffff !important;
  box-shadow: 2px 0 10px 0 rgba(14, 18, 64, 0.15);
  border-radius: 4px;
  width: calc(~"100% - 12px");
  padding-left: 12px !important;
  padding-right: 12px !important;
  margin-left: 5px;
}
.drag-ghost {
  opacity: 0;
}
.text__ellipsis1 {
  // display: -webkit-box;
  overflow: hidden;
  // -webkit-box-orient: vertical;
  // -webkit-line-clamp: 1;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
