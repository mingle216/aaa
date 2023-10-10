<template>
  <div class="cus__personalInfo">
    <AutoContainer class="width-full" :con-type="type" :con-height="height">
      <div class="cus__personalInfoCon" v-if="hasLogin">
        <!--个人信息-->
        <div class="cuspersonal-info" v-if="personalObjConfig.isDisplay">
          <img
            alt="个人图像"
            class="cuspersonal-info_img"
            :src="personalInfoImg || defaultImg"
            @error="handlePersonalError"
          />
          <div class="cuspersonal-info_box">
            <h2 class="portal-font-color-lv1" :title="personalObj.name">
              {{ personalObj.name }}
            </h2>
            <p class="portal-font-color-lv3 user-account" :title="userAccount">
              {{ userAccount }}
            </p>
            <div class="cuspersonal-info_details">
              <p v-if="personalObjConfig.infoList.includes(1)">
                <img :src="require('./assets/icon1.png')" alt="" />
                <span class="portal-font-color-lv3 info-name">{{
                  $Lan(lanFunName, "lastTime", "上次登录时间")
                }}</span>
                <span class="portal-font-color-lv1 info-value">{{
                  personalObj.lastLogTime || "-"
                }}</span>
              </p>
              <p v-if="personalObjConfig.infoList.includes(0)" class="ellipsis">
                <img :src="require('./assets/icon2.png')" alt="" />
                <span class="portal-font-color-lv3 info-name">{{
                  $Lan(lanFunName, "structure", "组织机构")
                }}</span>
                <span class="portal-font-color-lv1 info-value">{{
                  personalObj.organizationName || "-"
                }}</span>
              </p>
              <p v-if="personalObjConfig.infoList.includes(2)">
                <img :src="require('./assets/icon1.png')" alt="" />
                <span class="portal-font-color-lv3 info-name">{{
                  $Lan(lanFunName, "loginIp", "登录的IP地址")
                }}</span>
                <span class="portal-font-color-lv1 info-value">{{
                  personalObj.lastLogIp || "-"
                }}</span>
              </p>
            </div>
          </div>
        </div>
        <div class="cus__personalInfo_box" v-if="personalInfo.length">
          <div class="left-wrap">
            <div
              class="cus__personalInfo__item_wrapper"
              v-for="(item, index) in personalInfo.slice(0, 2)"
              :key="index"
              :class="[
                filterClickUrlAble(item)
                  ? 'portal-primary-backgroundcolor-hover-my cursor-pointer'
                  : '',
              ]"
            >
              <div
                v-if="!(item.isEmail && (!item.mainInfo || !item.mainInfo.length))"
                @click="handlerEnterClick(item)"
              >
                <div class="cus__personalInfo__item_title">
                  <div class="flex items-center flex-1">
                    <img
                      v-if="item.iconUrl"
                      :src="item.iconUrl"
                      style="flex-shrink: 0"
                      @error="handleError"
                    />
                    <div
                      class="cus__personalInfo__item_title_text overflow-ellipsis portal-font-color-lv1"
                      :title="item.title"
                    >
                      {{ item.title }}
                    </div>
                    <div
                      v-if="item.isEmail && showBindMailFlag"
                      :title="$Lan(lanFunName, 'textBindOff', '解绑')"
                      @click.stop="handlerOffEmail(item, index)"
                      class="cus__personalInfo__item_title_btn-status cursor-pointer portal-primary-color-lv1"
                      style="margin-left: 12px"
                    >
                      {{ $Lan(lanFunName, "textBindOff", "解绑") }}
                    </div>
                    <div
                      v-if="item.isEmail && showBindMailFlag"
                      :title="$Lan(lanFunName, 'textAddEmail', '新增')"
                      @click.stop="handlerOnEmail(item, index)"
                      class="cus__personalInfo__item_title_btn-status cursor-pointer portal-primary-color-lv1"
                    >
                      {{ $Lan(lanFunName, "textAddEmail", "新增") }}
                    </div>
                  </div>
                  <!-- 隐私模式 -->
                  <div
                    v-if="hiddenMainInfo[`${item.wid}-${item.extraInfo}`]"
                    class="iconfont cursor-pointer portal-font-color-lv4"
                    :class="[
                      hiddenMainInfo[`${item.wid}-${item.extraInfo}`].hidden
                        ? 'icon-yincang'
                        : 'icon-yulan',
                    ]"
                    style="font-size: 18px"
                    @click.stop="handleHidden(item)"
                  ></div>
                </div>
                <div>
                  <TLoading
                    class="cus__personalInfo__item_body portal-font-color-lv1"
                    :loading="loading || item.loading"
                  >
                    <div class="width-full overflow-ellipsis">
                      {{
                        (hiddenMainInfo[`${item.wid}-${item.extraInfo}`] || {}).hidden
                          ? hiddenMainInfo[`${item.wid}-${item.extraInfo}`]
                              .originalMainInfo
                          : item.mainInfo
                      }}
                    </div>
                  </TLoading>
                  <div class="cus__personalInfo__item_bottom portal-font-color-lv3">
                    <TLoading :loading="loading || item.loading">
                      <span
                        class="width-full overflow-ellipsis inline-block"
                        :title="item.subInfo"
                        >{{ item.subInfo }}</span
                      >
                    </TLoading>
                  </div>
                </div>
              </div>

              <div v-else-if="showBindMailFlag" @click.stop="handlerOnEmail(item, index)">
                <div class="cus__personalInfo__item_title">
                  <div class="flex items-center width-full overflow-hidden">
                    <img v-if="item.iconUrl" :src="item.iconUrl" @error="handleError" />
                    <span
                      class="cus__personalInfo__item_title_text overflow-ellipsis portal-font-color-lv1 flex-1"
                      :title="item.title"
                      >{{ item.title }}</span
                    >
                  </div>
                </div>
                <div>
                  <div class="cus__personalInfo__item_body">
                    <TLoading :loading="loading || item.loading">
                      <div class="cursor-pointer overflow-ellipsis">
                        {{ $Lan(lanFunName, "textBindEmail", "绑定邮箱") }}
                      </div>
                    </TLoading>
                  </div>
                  <div class="cus__personalInfo__item_bottom portal-font-color-lv3">
                    <TLoading :loading="loading || item.loading">
                      <span
                        class="width-full overflow-ellipsis inline-block"
                        :title="
                          $Lan(
                            lanFunName,
                            'textBindOnWaring',
                            '绑定邮箱可收到未读邮件提醒'
                          )
                        "
                        >{{
                          $Lan(
                            lanFunName,
                            "textBindOnWaring",
                            "绑定邮箱可收到未读邮件提醒"
                          )
                        }}</span
                      >
                    </TLoading>
                  </div>
                </div>
              </div>

              <!--不显示解绑绑定功能-->
              <div v-else>
                <div class="cus__personalInfo__item_title">
                  <div class="flex items-center width-full overflow-hidden">
                    <img v-if="item.iconUrl" :src="item.iconUrl" @error="handleError" />
                    <span
                      class="cus__personalInfo__item_title_text overflow-ellipsis portal-font-color-lv1 flex-1"
                      :title="item.title"
                      >{{ item.title }}</span
                    >
                  </div>
                </div>
                <div>
                  <div class="cus__personalInfo__item_body overflow-ellipsis">
                    <TLoading :loading="loading || item.loading">
                      <span>{{ $Lan(lanFunName, "itemBody", "当前无邮箱") }}</span>
                    </TLoading>
                  </div>
                  <div class="cus__personalInfo__item_bottom portal-font-color-lv3">
                    <TLoading :loading="loading || item.loading">
                      <span class="width-full overflow-ellipsis inline-block">{{
                        $Lan(lanFunName, "itemBottom", "请联系管理员")
                      }}</span>
                    </TLoading>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 外层循环 -->
          <div
            v-for="(pItem, pIndex) in showPersonalInfo"
            :key="pItem.index"
            style="
              display: flex;
              flex-wrap: nowrap;
              border-radius: 4px;
              border: 1px solid #f0f0f0;
              flex: 1;
            "
            :style="{ 'margin-top': pIndex === 0 ? '0px' : '12px' }"
          >
            <!-- 内层循环 -->
            <div
              class="cus__personalInfo__item_wrapper"
              v-for="(item, index) in pItem.children"
              @mouseover="
                (e) => {
                  activeIndex = index;
                }
              "
              @mouseout="(e) => {}"
              :key="index"
              :class="[
                filterClickUrlAble(item)
                  ? 'portal-primary-backgroundcolor-hover-my cursor-pointer'
                  : '',
                index == activeIndex ? 'active-item' : '',
              ]"
            >
              <div
                v-if="!(item.isEmail && (!item.mainInfo || !item.mainInfo.length))"
                @click="handlerEnterClick(item)"
              >
                <div class="cus__personalInfo__item_title">
                  <div class="flex items-center flex-1">
                    <img
                      v-if="item.iconUrl"
                      :src="item.iconUrl"
                      style="flex-shrink: 0"
                      @error="handleError"
                    />
                    <div
                      class="cus__personalInfo__item_title_text overflow-ellipsis portal-font-color-lv1"
                      :title="item.title"
                    >
                      {{ item.title }}
                    </div>
                    <div
                      v-if="item.isEmail && showBindMailFlag"
                      :title="$Lan(lanFunName, 'textBindOff', '解绑')"
                      @click.stop="handlerOffEmail(item, index)"
                      class="cus__personalInfo__item_title_btn-status cursor-pointer portal-primary-color-lv1"
                      style="margin-left: 12px"
                    >
                      {{ $Lan(lanFunName, "textBindOff", "解绑") }}
                    </div>
                    <div
                      v-if="item.isEmail && showBindMailFlag"
                      :title="$Lan(lanFunName, 'textAddEmail', '新增')"
                      @click.stop="handlerOnEmail(item, index)"
                      class="cus__personalInfo__item_title_btn-status cursor-pointer portal-primary-color-lv1"
                    >
                      {{ $Lan(lanFunName, "textAddEmail", "新增") }}
                    </div>
                  </div>
                  <!-- 隐私模式 -->
                  <div
                    v-if="hiddenMainInfo[`${item.wid}-${item.extraInfo}`]"
                    class="iconfont cursor-pointer portal-font-color-lv4"
                    :class="[
                      hiddenMainInfo[`${item.wid}-${item.extraInfo}`].hidden
                        ? 'icon-yincang'
                        : 'icon-yulan',
                    ]"
                    style="font-size: 18px"
                    @click.stop="handleHidden(item)"
                  ></div>
                </div>
                <div>
                  <TLoading
                    class="cus__personalInfo__item_body portal-font-color-lv1"
                    :loading="loading || item.loading"
                  >
                    <div class="width-full overflow-ellipsis">
                      {{
                        (hiddenMainInfo[`${item.wid}-${item.extraInfo}`] || {}).hidden
                          ? hiddenMainInfo[`${item.wid}-${item.extraInfo}`]
                              .originalMainInfo
                          : item.mainInfo
                      }}
                    </div>
                  </TLoading>
                  <div class="cus__personalInfo__item_bottom portal-font-color-lv3">
                    <TLoading :loading="loading || item.loading">
                      <span
                        class="width-full overflow-ellipsis inline-block"
                        :title="item.subInfo"
                        >{{ item.subInfo }}</span
                      >
                    </TLoading>
                  </div>
                </div>
              </div>

              <div v-else-if="showBindMailFlag" @click.stop="handlerOnEmail(item, index)">
                <div class="cus__personalInfo__item_title">
                  <div class="flex items-center width-full overflow-hidden">
                    <img v-if="item.iconUrl" :src="item.iconUrl" @error="handleError" />
                    <span
                      class="cus__personalInfo__item_title_text overflow-ellipsis portal-font-color-lv1 flex-1"
                      :title="item.title"
                      >{{ item.title }}</span
                    >
                  </div>
                </div>
                <div>
                  <div class="cus__personalInfo__item_body">
                    <TLoading :loading="loading || item.loading">
                      <div class="cursor-pointer overflow-ellipsis">
                        {{ $Lan(lanFunName, "textBindEmail", "绑定邮箱") }}
                      </div>
                    </TLoading>
                  </div>
                  <div class="cus__personalInfo__item_bottom portal-font-color-lv3">
                    <TLoading :loading="loading || item.loading">
                      <span
                        class="width-full overflow-ellipsis inline-block"
                        :title="
                          $Lan(
                            lanFunName,
                            'textBindOnWaring',
                            '绑定邮箱可收到未读邮件提醒'
                          )
                        "
                        >{{
                          $Lan(
                            lanFunName,
                            "textBindOnWaring",
                            "绑定邮箱可收到未读邮件提醒"
                          )
                        }}</span
                      >
                    </TLoading>
                  </div>
                </div>
              </div>

              <!--不显示解绑绑定功能-->
              <div v-else>
                <div class="cus__personalInfo__item_title">
                  <div class="flex items-center width-full overflow-hidden">
                    <img v-if="item.iconUrl" :src="item.iconUrl" @error="handleError" />
                    <span
                      class="cus__personalInfo__item_title_text overflow-ellipsis portal-font-color-lv1 flex-1"
                      :title="item.title"
                      >{{ item.title }}</span
                    >
                  </div>
                </div>
                <div>
                  <div class="cus__personalInfo__item_body overflow-ellipsis">
                    <TLoading :loading="loading || item.loading">
                      <span>{{ $Lan(lanFunName, "itemBody", "当前无邮箱") }}</span>
                    </TLoading>
                  </div>
                  <div class="cus__personalInfo__item_bottom portal-font-color-lv3">
                    <TLoading :loading="loading || item.loading">
                      <span class="width-full overflow-ellipsis inline-block">{{
                        $Lan(lanFunName, "itemBottom", "请联系管理员")
                      }}</span>
                    </TLoading>
                  </div>
                </div>
              </div>

              <img
                v-if="index == activeIndex"
                class="icon-hover"
                :src="item.iconHover"
                alt=""
              />
            </div>
          </div>
        </div>

        <empty-con
          v-if="!personalObjConfig.isDisplay && !personalInfo.length"
          :tip="$Lan(lanFunName, 'noPersonalData', '暂无相关配置')"
          :height="height"
        ></empty-con>
        <TBindEmailModal
          v-model="emailConfig.show"
          :options="emailConfig.options"
          @on-ok="refreshEmailData"
          :router="router"
        >
        </TBindEmailModal>
      </div>
      <TNoLogin v-else :lan-fun-name="lanFunName" :router="router"></TNoLogin>
    </AutoContainer>
  </div>
</template>

<script>
import TLoading from "./components/TLoading";
import TNoLogin from "./components/TNoLogin";
import TBindEmailModal from "./components/TBindEmailModal";
import { initTrack } from "./mixins/track.js";
import icon1Hover from "./assets/icon1Hover.png";
import icon2Hover from "./assets/icon2Hover.png";
import icon3Hover from "./assets/icon3Hover.png";
import icon4Hover from "./assets/icon4Hover.png";
export default {
  components: {
    TLoading,
    TBindEmailModal,
    TNoLogin,
  },
  mixins: [initTrack],
  watch: {
    // personalInfo() {
    //   this.$nextTick(() => {
    //     let div = document.getElementsByClassName("cus__personalInfo__item_wrapper");
    //     for (let i = 0; i < div.length; i++) {
    //       div[i].style.width = `${div[0].clientWidth}px`;
    //     }
    //   });
    // },
  },
  data() {
    return {
      defaultImg: require("./assets/default.png"),
      isOnEmail: false,
      emailConfig: {
        show: false,
        options: {},
      },
      lanFunName: this.router.cardId,
      hasLogin: window.shell.getUserInfo() ? true : false,
      personalInfo: [],
      height: 500, //容器高度，type为1时该值无效，type为2时最大高度，type为3时固定高度
      type: 0, //0：代表自适应（无最大限制）1：代表自适应（有最大限制）2：固定高度
      errorImg: window.shell.ErrorImg,
      notHasEmail: false,
      loading: false,
      cardData: { showBindMail: 0 },
      getListInterval: {},
      columns: 4,
      personalObj: {},
      personalObjConfig: {},
      hiddenMainInfo: {},
      userAccount: window.shell.getUserInfo()?.userAccount,
      activeIndex: 0,
      hoverIcons: [icon1Hover, icon2Hover, icon3Hover, icon4Hover],
    };
  },
  props: {
    index: Number,
    router: Object,
  },
  computed: {
    showBindMailFlag() {
      return +this.cardData.showBindMail === 1;
    },
    personalInfoImg() {
      const userIcon = this.personalObj && this.personalObj.picUrl;
      return userIcon
        ? /^http(s)?:\/\//.test(userIcon)
          ? userIcon
          : `data:image/png;base64,${userIcon}`
        : this.defaultImg;
    },
    showPersonalInfo() {
      let tempArr =
        this.personalInfo && this.personalInfo.length && this.personalInfo.slice(2) || [];
      let arr = [],
        zIndex = Math.ceil(tempArr.length / parseInt(this.columns));
      for (let i = 0; i < zIndex; i++) {
        arr.push({
          index: i,
          children: [],
        });
        for (let j = 0; j < tempArr.length; j++) {
          if (Math.floor(j / parseInt(this.columns)) === i) {
            arr[i].children.push(tempArr[j]);
          }
        }
      }
      return arr;
    },
  },
  mounted() {
    this.initData();
    this.initPersonalInfo();
    this.getAllInfo();
    this.getListInterval = setInterval(() => {
      this.getAllInfo();
    }, 300000);
  },
  destroyed() {
    clearInterval(this.getListInterval);
  },
  methods: {
    initPersonalInfo() {
      window.shell.execCardMethod(
        {
          ...this.router,
          method: "configuredData",
          param: {
            lang: this.$i18n.locale,
            userAccount: window.shell.getUserInfo()?.userAccount || "",
          },
        },
        (data) => {
          this.personalObj = data.data;
        }
      );
    },
    initData() {
      window.shell.execCardMethod(
        {
          ...this.router,
          method: "renderData",
        },
        (data) => {
          this.cardData = data.data || {};
          if (this.cardData && this.cardData.columns) {
            this.columns = parseInt(this.cardData.columns);
          }
          this.height = this.cardData.serviceCarHeight?.value || 500;
          this.type = this.cardData.serviceCarHeight?.type || 0;
          this.personalObjConfig = this.cardData.personalInfo;
        }
      );
    },
    filterClickUrlAble(item) {
      /*邮箱显示解绑和绑定才可以点击*/
      return item.linkUrl || (item.isEmail && +this.cardData.showBindMail === 1);
    },
    handleHidden(el) {
      const widKey = `${el.wid}-${el.extraInfo}`;
      const item = this.hiddenMainInfo[widKey];
      item.hidden = !item.hidden;
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null;
    },
    handlePersonalError(e) {
      let img = e.srcElement;
      img.src = this.defaultImg;
      img.onerror = null;
    },
    refreshEmailData() {
      this.getAliiInfoByItem(this.emailConfig.options, this.emailConfig.options.index);
    },
    showEmailModal(params) {
      this.emailConfig = {
        show: true,
        options: { ...params },
      };
    },
    async handlerEnterClick(item) {
      let linkUrl = item.linkUrl;
      this.handleClickTrack(); //点击埋点
      if (item.isEmail) {
        let { cardId, cardWid } = this.router;
        let { data } = await window.shell.execCardMethod({
          cardId,
          cardWid,
          method: "getLinkUrl",
          param: { mailAccount: item.extraInfo },
        });
        linkUrl = data;
      }
      if (linkUrl) {
        window.open(linkUrl, "_blank");
      }
    },
    async handlerOnEmail(item, index) {
      let { cardId, cardWid } = this.router;
      this.handleClickTrack(); //点击埋点
      let { data } = await window.shell.execCardMethod({
        cardId,
        cardWid,
        method: "existsAccount",
        param: { wid: item.wid },
      });
      if (!data) {
        return;
      }
      if (!data.status) {
        this.showEmailModal({ ...item, index });
        return;
      }
      this.$confirm(
        this.$Lan(this.lanFunName, "textAskIsBindEmail", "是否绑定该邮箱？"),
        this.$Lan(this.lanFunName, "textHasEmail", "检测到已有邮箱") + data.account,
        {
          confirmButtonText: this.$Lan(this.lanFunName, "textEnsure", "确 定"),
          cancelButtonText: this.$Lan(this.lanFunName, "textCancel", "取 消"),
          distinguishCancelAndClose: true,
          closeOnClickModal: false,
          confirmButtonClass:
            "portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1",
          cancelButtonClass:
            "bgHoverWhite portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1 ",
          type: "warning",
          center: true,
        }
      )
        .then(() => {
          this.submitOnEmail(data, item, index);
          this.handleClickTrack(); //点击埋点
        })
        .catch((action) => {
          setTimeout(() => {
            if (action === "cancel") {
              this.showEmailModal({ ...item, index });
              this.handleClickTrack(); //点击埋点
            }
          }, 500);
        });
    },
    async submitOnEmail(email, item, index) {
      let { cardId, cardWid } = this.router;
      let res = await window.shell.execCardMethod({
        cardId,
        cardWid,
        method: "bindMail",
        param: {
          directBind: "1",
          mailAccount: email.account,
        },
      });
      if (res.data && +res.data.errcode === 0) {
        window.shell.showMessage({
          type: "success",
          message: this.$Lan(this.lanFunName, "textBindSuccess", "绑定成功"),
        });
        this.getAliiInfoByItem(item, index);
      } else {
        this.$message.error({ message: res.data.errmsg });
      }
    },
    handlerOffEmail(item, index) {
      let { cardId, cardWid } = this.router;
      this.handleClickTrack(); //点击埋点
      this.$confirm(
        this.$Lan(
          this.lanFunName,
          "textAfterBindOffWarning",
          "解绑后将无法收到未读邮件提示"
        ),
        this.$Lan(this.lanFunName, "textBindOffWarning", "确认解绑邮箱？"),
        {
          confirmButtonText: this.$Lan(this.lanFunName, "textEnsure", "确 定"),
          cancelButtonText: this.$Lan(this.lanFunName, "textCancel", "取 消"),
          closeOnClickModal: false,
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
          let res = await window.shell.execCardMethod({
            cardId,
            cardWid,
            method: "unBindMail",
            param: {
              wid: item.wid,
              mailAccount: item.extraInfo,
            },
          });
          if (res.data && +res.data.errcode !== 0) {
            this.$message.error({ message: res.data.errmsg });
            return;
          }
          window.shell.showMessage({
            message: this.$Lan(this.lanFunName, "unbindingSucceeded", "解绑成功"),
            type: "success",
          });
          this.getAliiInfoByItem(item, index);
        })
        .catch((action) => {
          if (action === "cancel") {
            this.handleClickTrack(); //点击埋点
          }
        });
    },
    async setPersonDetail(item, index) {
      if (+item.needRetrieve !== 1) {
        this.$set(this.personalInfo, index, {
          ...item,
          iconUrl:
            process.env.NODE_ENV === "development"
              ? item.iconUrl && item.iconUrl.split(".cn")[1]
              : item.iconUrl,
          iconHover: this.hoverIcons[index % 4],
        });
        return;
      }
      this.$set(this.personalInfo, index, {
        title: item.title,
        iconUrl: item.iconUrl,
        loading: true,
      });
      let { cardId, cardWid } = this.router;
      let { data } = await window.shell.execCardMethod({
        cardId,
        cardWid,
        method: "getPersonalDataDetail",
        param: { wid: item.wid, extraInfo: item.extraInfo },
      });
      this.$set(
        this.personalInfo,
        index,
        {
          ...data,
          iconUrl:
            process.env.NODE_ENV === "development"
              ? item.iconUrl && item.iconUrl.split(".cn")[1]
              : item.iconUrl,
          iconHover: this.hoverIcons[index % 4],
        } || {}
      );
    },
    getAliiInfoByItem(item) {
      let { cardId, cardWid } = this.router;
      window.shell.execCardMethod(
        { cardId, cardWid, method: "getPersonalDataList" },
        ({ data }) => {
          let flag = 0;
          data.forEach((el, i) => {
            if (item.wid === el.wid) {
              flag++;
              if (this.personalInfo.length < data.length && flag === 1) {
                this.personalInfo.splice(i, 0, { item });
              } else if (data.length < this.personalInfo.length && flag === 1) {
                this.personalInfo.splice(i, 1);
              }
              this.setPersonDetail(el, i);
            }
          });
        }
      );
    },
    getAllInfo() {
      this.loading = true;
      let { cardId, cardWid } = this.router;
      window.shell.execCardMethod(
        { cardId, cardWid, method: "getPersonalDataList" },
        ({ data }) => {
          this.loading = false;
          data = data || [];
          if (data.length === 0 || this.personalInfo.length !== data.length) {
            this.personalInfo = [];
          }
          data.forEach((el, index) => {
            const widKey = `${el.wid}-${el.extraInfo}`;
            if (
              this.cardData.isHiddenPrivacy &&
              el.isHiddenPrivacy &&
              el.originalMainInfo &&
              !this.hiddenMainInfo[widKey]
            ) {
              // 开启隐私模式
              this.$set(this.hiddenMainInfo, widKey, {
                hidden: true,
                originalMainInfo: el.originalMainInfo.replace(/【(\S*)】/g, " ** "),
              });
            }
            this.setPersonDetail(el, index);
          });
          this.loadedEndTrack(); // 加载结束埋点
        }
      );
    },
  },
};
</script>

<style lang="less">
.we-message-box--center {
  padding-bottom: 20px !important;
  .we-message-box__header {
    padding: 40px 15px 0 15px;
    .we-message-box__title {
      justify-content: flex-start;
      padding-left: 15px;
      font-size: 16px;
      color: #262626;
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
.portal-primary-backgroundcolor-hover-my:hover {
  background-color: #f0f0f0 !important;
}
.cus__personalInfo {
  // min-height: 100px;
  // min-width: 360px;
  display: flex;
  .cus__personalInfo__item_title_btn-status {
    display: none;
    margin-right: 6px;
    max-width: 40px;
    line-height: 20px;
    font-size: 12px;
    letter-spacing: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    flex-shrink: 0;
  }
  .event-none {
    pointer-events: none;
  }
  .overflow-hidden {
    overflow: hidden;
  }
  .overflow-ellipsis {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .justify-between {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .inline-block {
    display: inline-block;
    white-space: nowrap;
  }
  .flex-1 {
    flex: 1;
    min-width: 0;
  }
  .mx-4 {
    margin: 0 4px;
  }
  .cursor-pointer {
    cursor: pointer;
  }
  .flex {
    display: flex;
  }
  .items-center {
    align-items: center;
  }
  .width-full {
    width: 100%;
  }
  .height-full {
    height: 100%;
  }
  .flex-wrap {
    display: flex;
    flex-wrap: wrap;
  }
  .justify-center {
    justify-content: center;
    display: flex;
  }
  .ml-4 {
    margin-left: 4px;
  }
  .ml-20 {
    margin-left: 20px;
  }
  .mb-20 {
    margin-bottom: 20px;
  }

  .cus__personalInfo__item_wrapper {
    flex: 1;
    width: 0;
    background: #fff;
    height: 162px;
    padding: 20px;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-shadow: -1px 0px 0px 0px #f0f0f0 inset;
    transition: all 0.3s;
    &:first-child {
      margin-left: 0;
    }
    &:last-of-type {
      box-shadow: none;
    }
    &:hover .cus__personalInfo__item_title_btn-status {
      display: inline-block;
    }

    & > div {
      flex: 1;
      width: 0;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }

    .icon-hover {
      width: 101px;
      height: 101px;
      flex-shrink: 0;
    }
    .cus__personalInfo__item_title {
      display: flex;
      justify-content: space-between;
      img {
        width: 32px;
        height: 32px;
        margin-left: 12px;
      }
      .cus__personalInfo__item_title_text {
        font-size: 16px;
        color: #212a39;
        letter-spacing: 0;
        line-height: 20px;
        font-weight: 700;
      }

      & > div {
        flex-direction: row-reverse;
        justify-content: space-between;
      }
    }
    .cus__personalInfo__item_body {
      font-weight: bold;
      font-size: 16px;
      color: #262626;
      letter-spacing: 0;
      line-height: 24px;
      margin-top: 4px;
      &.lesson__body {
        display: flex;
        align-items: center;
        margin-top: 0;
        height: 100%;
      }
    }
    .cus__personalInfo__item_bottom {
      font-size: 14px;
      color: #8c8c8c;
      letter-spacing: 0;
      line-height: 22px;
      margin-top: 4px;
    }

    &.active-item {
      flex: 2;
      .cus__personalInfo__item_title {
        & > div {
          flex-direction: row;
          flex: none;
        }

        img {
          margin-right: 12px;
          margin-left: 0;
        }
      }
    }
  }

  .cus__personalInfo_box {
    display: flex;
    .left-wrap {
      width: 223px;
      margin-right: 16px;

      .cus__personalInfo__item_wrapper {
        height: 73px;
        width: 100%;
        margin-bottom: 16px;
        border-radius: 4px;
        border: 1px solid #f0f0f0;
        background: #fff;
        box-shadow: none;
        padding: 12px;
        &:last-of-type {
          margin-bottom: 0;
        }

        .cus__personalInfo__item_title {
          & > div {
            flex: none;
            flex-direction: row;
          }

          img {
            width: 22px;
            height: 22px;
            margin-left: 0;
            margin-right: 12px;
          }
        }

        .cus__personalInfo__item_body {
          display: none;
        }

        .cus__personalInfo__item_bottom {
          margin-top: 5px;
          span {
            color: #a1a4bb;
            font-size: 13px;
          }
        }
      }
    }
  }
}
.cus__personalInfoCon {
  min-height: 100px;
  min-width: 270px;
}
.bgHoverWhite:hover {
  background-color: white !important;
}
.cuspersonal-info {
  width: 100%;
  // height: 168px;
  display: flex;
  border-radius: 4px;
  padding: 16px 16px 38px;
  align-items: center;
  background: url("./assets/cusbg.png") no-repeat;
  background-size: cover;
  background-position: top center;
  flex-direction: column;
  position: relative;

  .cuspersonal-info_img {
    width: 70px;
    height: 70px;
    border-radius: 50%;
    flex-shrink: 0;
    //height: 126px;
  }
  .user-account {
    text-align: center;
    margin-top: 5px;
  }
  .cuspersonal-info_box {
    flex: 1;
    width: 100%;
    h2 {
      color: #262626;
      font-weight: bold;
      font-size: 18px;
      line-height: 26px;
      letter-spacing: 0px;
      text-align: center;
      width: 100%;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      margin-top: 12px;
    }
    .cuspersonal-info_details {
      display: flex;
      flex-direction: column;
      margin-top: 30px;
      color: #8c8c8c;
      padding: 0 10px;
      font-size: 12px;
      line-height: 22px;
      letter-spacing: 0px;
      text-align: left;
      p {
        display: flex;
        align-items: center;
        margin-bottom: 10px;

        &:last-of-type {
          margin-bottom: 0;
        }
      }
      img {
        width: 12px;
        height: 12px;
      }

      .info-name {
        margin-left: 7px;
        margin-right: 12px;
      }

      .info-value {
        font-size: 13px;
      }
    }
    .ellipsis-text {
      display: block;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      width: 100%;
    }
  }
}
</style>
