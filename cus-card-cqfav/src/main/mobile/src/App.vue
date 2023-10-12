 <template>
  <div v-loading="loading">
    <div
      class="card-myfavoritesserviceItem"
      v-if="serviceItemsInfoCopies.length"
    >
      <div
        v-show="manageFavorite"
        class="myfavorite-manage"
        @click="manageFavoriteValue"
      >
        <we-icon
          id="card-myfavoritesserviceItem-icon"
          :style="{ color: defaultColor, display: 'block' }"
          name="setting-o"
        />
        <div class="glsc" :style="{ color: defaultColor }">
          {{ $Lan(lanFunNanme, "collectManage", "管理收藏") }}
        </div>
      </div>
      <div v-show="!manageFavorite" class="myfavorite-manage">
        <div
          id="card-myfavoritesserviceItem-btn1"
          class="btn cancel text-ellipsis"
          @click="cancelSave"
        >
          {{ $Lan(lanFunNanme, "callOff", "取消") }}
        </div>
        <div
          id="card-myfavoritesserviceItem-btn2"
          :style="{ 'background-color': defaultColor }"
          class="btn commit text-ellipsis"
          @click="keepSave"
        >
          {{ $Lan(lanFunNanme, "save", "保存") }}
        </div>
        <!-- <we-button @click="cancelSave">
          {{ tip[this.$i18n.locale].callOff }}
        </we-button>
        <we-button :style="{'background-color': defaultColor}" @click="keepSave" class="button-save">
          {{ tip[this.$i18n.locale].save }}
        </we-button> -->
      </div>
      <div class="myfavorite-list">
        <draggable
          v-model="serviceItemsInfo"
          v-bind="dragOptions"
          handle=".drag-icon"
          @change="dragChange"
        >
          <transition-group
            type="transition"
            tag="ul"
            class="card-setting__item"
          >
            <div
              class="myfavorite__item"
              v-for="(item, index) in serviceItemsInfo.slice(0, showNum)"
              :key="item.itemWid"
              @click="openServiceItem(item)"
            >
              <div class="myfavorite__item__before">
                <i
                  v-if="!manageFavorite"
                  class="iconfont icon-delete myfavorite__item__delete"
                  @click.stop="deleteCollect(index)"
                ></i>
                <img
                  v-if="config.iconShow == '1'"
                  class="myfavorite__item__img"
                  :src="item.iconLink || errorImg"
                  @error="handleError"
                />
                <div
                  class="myfavorite__item__detail"
                  :style="{ width: getWidth }"
                >
                  <span
                    class="myfavorite__item__name text__ellipsis portal-font-color-lv1"
                  >
                    {{ item.itemName }}
                  </span>
                  <span class="myfavorite__item__dept text__ellipsis">
                    {{ item.itemDept }}
                  </span>
                </div>
              </div>
              <div
                class="icon__area"
                v-if="manageFavorite"
                @click.stop="sureCollect(item, index)"
              >
                <i
                  class="iconfont icon-favorites favorite_font myfavorite__item__favorite"
                ></i>
              </div>
              <span v-if="!manageFavorite" class="drag-icon"
                ><img :src="dragIcon" height="12" width="28" alt=""
              /></span>
              <!-- <div class="icon__area sort-handler" v-if="!manageFavorite">
                <i class="iconfont icon-More"></i>
              </div> -->
            </div>
          </transition-group>
        </draggable>
      </div>
      <template v-if="showMore">
        <we-button
          :style="{ color: defaultColor }"
          class="moreButton"
          @click="showMoreButton"
        >
          {{ $Lan(lanFunNanme, "moreCollect", "更多收藏") }} >
        </we-button>
      </template>
    </div>
    <EmptyCon
      :tip="$Lan(lanFunNanme, 'noData', '还没有收藏服务事项哦')"
      v-if="!serviceItemsInfo.length || !serviceItemsInfoCopies.length"
    ></EmptyCon>
    <!-- 更多收藏弹窗 -->
    <custom-action-sheet
      v-model="showMoreModel"
      class="all-list-dialog1"
      :close-on-click-overlay="false"
    >
      <div class="all-content">
        <div class="we-action-sheet__header">
          {{
            manageFavorite
              ? cardName
              : $Lan(lanFunNanme, "collectManage", "管理收藏")
          }}<i
            @click="popClose"
            class="we-icon we-icon-cross we-action-sheet__close"
            ><!----></i
          >
        </div>
        <div class="card-content">
          <div class="myfavorite-list">
            <draggable
              v-model="serviceItemsInfo"
              v-bind="dragOptions"
              handle=".drag-icon"
              @change="dragChange"
            >
              <transition-group>
                <div
                  class="myfavorite__item"
                  v-for="(item, index) in serviceItemsInfo.slice(0)"
                  :key="item.itemWid"
                  @click="openServiceItem(item)"
                >
                  <div
                    class="myfavorite__item__before"
                    :style="{
                      width: manageFavorite ? '100%' : 'calc(100% - 1rem)',
                    }"
                  >
                    <i
                      v-if="!manageFavorite"
                      class="iconfont icon-delete myfavorite__item__delete"
                      @click.stop="deleteCollect(index)"
                    ></i>
                    <img
                      v-if="config.iconShow == '1'"
                      class="myfavorite__item__img"
                      :src="item.iconLink || errorImg"
                      @error="handleError"
                    />
                    <div
                      class="myfavorite__item__detail"
                      :style="{ width: getWidth }"
                    >
                      <span
                        class="myfavorite__item__name text__ellipsis portal-font-color-lv1"
                      >
                        {{ item.itemName }}
                      </span>
                      <span class="myfavorite__item__dept text__ellipsis">
                        {{ item.itemDept }}
                      </span>
                    </div>
                  </div>
                  <div
                    class="icon__area"
                    v-if="manageFavorite"
                    @click.stop="sureCollect(item, index)"
                    style="display: none"
                  >
                    <i
                      class="iconfont icon-favorites favorite_font myfavorite__item__favorite"
                    ></i>
                  </div>
                  <span v-if="!manageFavorite" class="drag-icon"
                    ><img :src="dragIcon" height="12" width="28" alt=""
                  /></span>
                  <!-- <div class="icon__area sort-handler" v-if="!manageFavorite">
                    <i class="iconfont icon-More"></i>
                  </div> -->
                </div>
              </transition-group>
            </draggable>
          </div>
          <EmptyCon
            v-if="!serviceItemsInfo.length"
            :tip="$Lan(lanFunNanme, 'noData', '还没有收藏服务事项哦')"
          ></EmptyCon>
        </div>
        <!-- footer -->
        <div class="showMoreManage__button">
          <span class="moreButton__white btn" @click="manageOrCancel">
            {{
              manageFavorite
                ? $Lan(lanFunNanme, "collectManage", "管理收藏")
                : $Lan(lanFunNanme, "callOff", "取消")
            }}
          </span>
          <span
            :style="{ 'background-color': defaultColor }"
            class="moreButton__primary btn"
            @click="closeOrSave"
          >
            {{
              manageFavorite
                ? $Lan(lanFunNanme, "close", "关闭")
                : $Lan(lanFunNanme, "save", "保存")
            }}
          </span>
        </div>
      </div>
    </custom-action-sheet>
  </div>
</template>

<script>
import draggable from "vuedraggable";
export default {
  components: {
    // [Dialog.Component.name]: Dialog.Component,
    draggable,
  },
  name: "",
  props: {
    index: Number,
    router: Object,
  },
  data() {
    const { cardWid, cardId } = this.router;
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      lanFunNanme: this.router.cardId + "_h5",
      dragIcon: require("./assets/drag.png"),
      loading: false,
      serviceItemsInfo: [],
      serviceItemsInfoCopies: [], //取消保存后恢复原状
      config: [],
      showMore: false, //展示更多按钮
      showMoreModel: false, //显示更多弹窗
      showNum: 1, //展示个数
      errorImg: window.shell.ErrorImg,
      manageFavorite: true, //管理收藏
      dragOptions: {
        animation: 200,
        group: "serviceInfo",
        disabled: false,
        ghostClass: "ghost",
        forceFallback: true,
        handle: ".sort-handler",
        chosenClass: "draggable-item",
      },
      // tip: {
      //   en_US: {
      //     nodata: "No Data",
      //     moreCollect: "More Collect",
      //     collectManage: "Collect Manage",
      //     close: "Close",
      //     callOff: "Call Off",
      //     save: "Save",
      //   },
      //   zh_CN: {
      //     nodata: "还没有收藏服务事项哦",
      //     moreCollect: "更多收藏",
      //     collectManage: "管理收藏",
      //     close: "关闭",
      //     callOff: "取消",
      //     save: "保存",
      //   },
      // },
      defaultColor: "white",
      cardName:""
    };
  },
  computed: {
    getWidth() {
      if (this.config && this.config !== "") {
        if (!this.manageFavorite) {
          if (this.config.iconShow == "0") {
            return "calc(100% - 0.75rem)";
          } else {
            return "calc(100% - 1.95rem)";
          }
        } else {
          if (this.config.iconShow == "0") {
            return "calc(100% - 0.05rem)";
          } else {
            return "calc(100% - 1.2rem)";
          }
        }
      } else {
        return "";
      }
    },
  },
  created() {
    window.shell.on("collectServiceItem", this.getCardData);
    if (window.shell) {
      this.getCardData('init');
      this.SETModalTitle();
    }
    window.shell.on("card-tab-change", () => {
      this.manageFavorite = true;
      this.getCardData();
    });
  },
  mounted() {
    // console.log(JSON.parse(JSON.parse(window.shell.getPageData().pageContext.showProgrammeEntity.templateConfig)))
    this.defaultColor = JSON.parse(
      JSON.parse(
        window.shell.getPageData().pageContext.showProgrammeEntity
          .templateConfig
      )
    ).themeColorSetting["portal-primary-color-lv1"];
    setTimeout(() => {
      let userAgent = window.navigator.userAgent;
      //安卓机，微信浏览器 && 钉钉浏览器  样式需要微调
      if (userAgent.indexOf("Android") > -1 || userAgent.indexOf("Adr") > -1) {
        if (userAgent.indexOf("MicroMessenger") > -1) {
          //微信浏览器
          document.getElementById(
            "card-myfavoritesserviceItem-icon"
          ).style.marginTop = "3px";
          document.getElementById(
            "card-myfavoritesserviceItem-btn1"
          ).style.paddingTop = "3px";
          document.getElementById(
            "card-myfavoritesserviceItem-btn2"
          ).style.paddingTop = "3px";
        }
        if (userAgent.indexOf("DingTalk") > -1) {
          //钉钉浏览器
          document.getElementById(
            "card-myfavoritesserviceItem-icon"
          ).style.marginTop = "3px";
          document.getElementById(
            "card-myfavoritesserviceItem-btn1"
          ).style.paddingTop = "3px";
          document.getElementById(
            "card-myfavoritesserviceItem-btn2"
          ).style.paddingTop = "3px";
        }
      }
    }, 1000);
  },
  beforeDestroy() {
    window.shell.off("collectServiceItem");
    window.shell.off("card-tab-change");
  },
  methods: {
    getCardData(isInit) {
      if(isInit && isInit === 'init') {
        window.minosStataCollect.loadStart({
          listId: this.listId,
          actionType: 3,
          functionType: 1,
          actionParams: {
            pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
            pageName: window.shell.getPageData().pageInfoEntity.pageName,
            cardWid: this.router.cardWid,
            cardId: this.router.cardId,
            cardName: this.router.cardName,
            extraInfo: ''
          },
          startTime: new Date().getTime()
        });
      }
      this.loading = true;
      window.shell.execCardMethod({
        cardId: this.router.cardId,
        cardWid: this.router.cardWid,
        method: "renderData",
        param: {},
      })
      .then(data=> {
        if(isInit && isInit === 'init') {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime()
          });
        }
        this.loading = false;
        this.setData(data);
      })
      .catch(()=> {
        if(isInit && isInit === 'init') {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime()
          });
        }
      });
    },
    setData(data) {
      this.serviceItemsInfo =
        (data.data && data.data.serviceItemsInfo.slice(0)) || [];
      this.serviceItemsInfo =
        (this.serviceItemsInfo &&
          this.serviceItemsInfo.map((v) => {
            return {
              ...v,
              wid: v.itemWid,
              name: v.itemName,
            };
          })) ||
        [];
      this.serviceItemsInfoCopies = this.serviceItemsInfo.slice(0);
      this.config = data.data && data.data.config;
      this.showTheMoreButton();
    },
    // 判断需不需要展示更多按钮
    showTheMoreButton() {
      this.showNum = this.config && this.config.columns;
      if (this.showNum < this.serviceItemsInfo.length) {
        this.showMore = true;
      } else {
        this.showMore = false;
      }
    },
    // 加载默认图片
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    // 确认取消收藏
    sureCollect(item, index) {
      if (!this.loading) {
        this.isOrNotCollect(item);
        this.deleteCollect(index, "single");
        this.showTheMoreButton();
      }
    },
    // 取消收藏
    async isOrNotCollect(item) {
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
            itemId: item.itemWid,
            fucType: 3
          }
        },
        startTime: new Date().getTime()
      });
      this.loading = true;
      try {
        await window.shell.collectServiceItem({
          id: item.itemWid,
          operate: 0,
          cardId: this.router.cardId,
        });
        this.loading = false;
      } catch (err) {
        this.loading = false;
      }
    },
    dragChange() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
    },
    //  process(el) {
    //   if (el.cardId !== this.router.cardId) {
    //     this.getCardData();
    //   }
    // },
    popClose() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      //弹窗关闭
      this.cancelManage();
    },
    // 取消管理收藏
    cancelManage() {
      // window.shell.setBroswerTitle(window.shell.getPageData().pageTitle);
      if (!this.manageFavorite) {
        this.$dialog
          .confirm({
            message: this.$Lan(
              this.lanFunNanme,
              "message",
              "是否保存对收藏的修改?"
            ),
            cancelButtonText: this.$Lan(
              this.lanFunNanme,
              "cancelButtonText",
              "放弃更改"
            ),
            cancelButtonColor: "#707D8F",
            confirmButtonText: this.$Lan(
              this.lanFunNanme,
              "confirmButtonText",
              "保存修改"
            ),
            confirmButtonColor: this.defaultColor,
          })
          .then(() => {
            this.closeOrSave();
            this.showMoreModel = false;
          })
          .catch(() => {
            this.serviceItemsInfo = this.serviceItemsInfoCopies.slice(0);
            this.showMoreModel = false;
            this.manageFavorite = true;
            this.showTheMoreButton();
            window.minosStataCollect.collect({
              actionType: 0,
              functionType: 1,
              actionParams: {
                pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
                pageName: window.shell.getPageData().pageInfoEntity.pageName,
                cardWid: this.router.cardWid,
                cardId: this.router.cardId,
                cardName: this.router.cardName,
                extraInfo: ''
              },
              startTime: new Date().getTime()
            });
          });
      } else {
        this.showMoreModel = false;
        this.showTheMoreButton();
      }
    },
    // 从数据中去除
    deleteCollect(index, type) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      this.serviceItemsInfo.splice(index, 1);
      if (type && type === "single") {
        this.serviceItemsInfoCopies = this.serviceItemsInfo.slice(0);
      }
      this.showTheMoreButton();
    },
    manageFavoriteValue() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      // if (this.showNum < this.serviceItemsInfo.length) {
      //   this.showMoreButton()
      //   return;
      // }
      this.manageFavorite = false;
    },
    //取消保存
    cancelSave() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      this.manageFavorite = true;
      this.serviceItemsInfo = this.serviceItemsInfoCopies.slice(0);
      this.showTheMoreButton();
    },
    keepSave() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      this.manageFavorite = true;
      this.serviceItemsInfoCopies = this.serviceItemsInfo.slice(0);
      this.showTheMoreButton();
      this.saveSort();
    },
    //全部收藏弹出
    showMoreButton() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      this.showMoreModel = true;
    },
    // 点击更多收藏，后展示保存
    manageOrCancel() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      //管理收藏
      if (this.manageFavorite) {
        this.manageFavorite = false;
        // window.shell.setBroswerTitle("管理收藏");
      } else {
        //取消保存
        this.manageFavorite = true;
        // window.shell.setBroswerTitle("我的收藏");
        this.serviceItemsInfo = this.serviceItemsInfoCopies.slice(0);
      }
    },
    // 点击关闭，回到更多收藏按钮
    closeOrSave() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      //关闭model
      if (this.manageFavorite) {
        this.showMoreModel = false;
        // window.shell.setBroswerTitle(window.shell.getPageData().pageTitle);
        this.showTheMoreButton();
      } else {
        //保存
        this.manageFavorite = true;
        this.serviceItemsInfoCopies = this.serviceItemsInfo.slice(0);
        // window.shell.setBroswerTitle("我的收藏");
        this.saveSort();
      }
    },
    // 保存修改
    saveSort() {
      var itemWidList = this.serviceItemsInfo.map((item) => {
        return item.itemWid;
      });
      this.sort(itemWidList.join(","));
    },
    sort(widList) {
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "sort",
          param: {
            itemIds: widList,
          },
        },
        (data) => {
          if (data.errcode == 0) {
            window.shell.showMessage({
              type: "success",
              message: this.$Lan(this.lanFunNanme, "success", "保存成功"),
            });
            window.shell.emit("collectServiceItem-mobile", {
              operate: false,
              cardId: this.router.cardId,
            });
          }
        }
      );
    },

    // 跳转事项
    openServiceItem(item) {
      if (this.manageFavorite) {
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
            itemId: item.itemWid
          }
        },
        startTime: new Date().getTime()
      });
        window.shell.openServiceItem(item);
      }
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
  },
};
</script>

<style lang="less" scoped>
/deep/ .we-action-sheet__content {
  height: 100%;
  overflow: hidden;
}
.draggable-item {
  padding: 0 12px !important;
  background: #ffffff;
  box-shadow: 2px 0 10px 0 rgba(14, 18, 64, 0.15);
  border-radius: 4px;
}
.card-myfavoritesserviceItem {
  position: relative;
  width: 100%;
  padding: 0 17px;
  .myfavorite-manage {
    width: 100%;
    height: 46px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    line-height: initial;
    // color: #307afb;
    .glsc {
      // font-family: PingFangSC-Regular;
      font-size: 14px;
      letter-spacing: 0;
    }
    .we-button__content {
      .we-button__text {
        font-size: 12px;
      }
    }
    i {
      font-size: 16px;
      margin-right: 4px;
    }
    span {
      // font-family: PingFangSC-Regular;
      font-size: 14px;
      letter-spacing: 0;
      text-align: center;
      line-height: 18px;
    }
    .btn {
      background: #ffffff;
      border-radius: 4px;
      // font-family: PingFangSC-Regular;
      font-size: 14px;
      letter-spacing: 0;
      // text-align: justify;
      // line-height: 28px;
      // display: flex;
      // width: 60px;
      height: 28px;
      // text-align: center;
      justify-content: center;
      align-items: center;
      line-height: initial;
      display: block;
    }
    .cancel {
      color: #707d8f;
      border: 1px solid #d6dade;
      margin-right: 8px;
    }
    .commit {
      color: #fff;
      // background: #307afb;
    }
    .text-ellipsis {
      line-height: 28px;
      max-width: 166px;
      padding-left: 16px;
      padding-right: 16px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    button {
      width: 60px;
      height: 28px;
      margin-right: 8px;
      border: 1px solid #d6dade;
      border-radius: 4px;
      border-radius: 4px;
      span {
        // font-family: PingFangSC-Regular;
        font-size: 14px;
        letter-spacing: 0;
        text-align: justify;
        line-height: 18px;
      }
    }
    .button-save {
      // background: #307afb;
      color: white;
    }
  }
  .myfavorite-list {
    .myfavorite__item {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .myfavorite__item__before {
        padding: 11px 0;
        width: calc(100% - 45px); //减去星星的size
        display: flex;
        align-items: center;
        .myfavorite__item__delete {
          color: red;
          font-size: 16px;
          margin-right: 12px;
        }
        .myfavorite__item__img {
          width: 36px;
          height: 36px;
          margin-right: 12px;
        }
        .myfavorite__item__detail {
          flex-direction: column;
          display: flex;
          width: calc(100% - 48px); // 减去图标的宽度
          .myfavorite__item__name {
            // font-family: PingFangSC-Regular;
            font-size: 16px;
            color: #102645;
            letter-spacing: 0;
            text-align: justify;
            line-height: 20px;
            padding-bottom: 2px;
            margin-right: 12px;
          }
          .myfavorite__item__dept {
            // font-family: PingFangSC-Regular;
            font-size: 12px;
            color: #707d8f;
            letter-spacing: 0;
            text-align: justify;
            line-height: 16px;
            margin-right: 25px;
          }
        }
      }
      .icon__area {
        display: flex;
        .myfavorite__item__favorite {
          font-size: 14px;
        }
      }
    }
  }
  .moreButton {
    width: 100%;
    // margin-right: 17px;
    background: #f9fafb;
    border: none;
    border-radius: 4px;
    margin-bottom: 12px;
    span {
      // font-family: PingFangSC-Regular;
      font-size: 16px;
      // color: #307afb;
      letter-spacing: 0;
      text-align: right;
      line-height: 20px;
    }
  }
}
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
  .card-content {
    padding: 0 17px;
    height: calc(100% - 2.986667rem);
    overflow-y: auto;
    .myfavorite-list {
      .myfavorite__item {
        width: 100%;
        display: flex;

        align-items: center;
        .myfavorite__item__before {
          padding: 11px 0;
          width: calc(100% - 45px); //减去星星的size
          display: flex;
          align-items: center;
          .myfavorite__item__delete {
            color: red;
            font-size: 16px;
            margin-right: 12px;
          }
          .myfavorite__item__img {
            width: 36px;
            height: 36px;
            margin-right: 12px;
          }
          .myfavorite__item__detail {
            flex-direction: column;
            display: flex;
            width: calc(100% - 48px); // 减去图标的宽度
            .myfavorite__item__name {
              // font-family: PingFangSC-Regular;
              font-size: 16px;
              color: #102645;
              letter-spacing: 0;
              text-align: justify;
              line-height: 20px;
              padding-bottom: 2px;
              padding-right: 5px;
            }
            .myfavorite__item__dept {
              // font-family: PingFangSC-Regular;
              font-size: 12px;
              color: #707d8f;
              letter-spacing: 0;
              text-align: justify;
              line-height: 16px;
              margin-right: 25px;
            }
          }
        }
        .icon__area {
          height: 58px;
          // width: 45px;
          display: flex;
          justify-content: center;
          align-items: center;
          .myfavorite__item__favorite {
            font-size: 14px;
          }
        }
      }
    }
    .moreButton {
      width: 100%;
      background: #f9fafb;
      border: none;
      border-radius: 4px;
      span {
        // font-family: PingFangSC-Regular;
        font-size: 16px;
        // color: #307afb;
        letter-spacing: 0;
        text-align: right;
        line-height: 20px;
      }
    }
  }
  .showMoreManage__button {
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
    .moreButton__white {
      background: #ffffff;
      border: 1px solid #e7edf1;
      color: #707d8f;
    }
    .moreButton__primary {
      // background: #307afb;
      color: #ffffff;
    }
  }
}
</style>
