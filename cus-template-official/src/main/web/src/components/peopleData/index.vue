<template>
    <div class="card__personalData">
        <AutoContainer class="width-full" :con-type="type" :con-height="height">
            <template v-if="hasLogin">

                <!--个人信息-->
                <div class="personal-info" style="display: none;" v-if="personalObjConfig.isDisplay">
                    <img alt="个人图像" class="personal-info_img" :src="personalInfoImg||defaultImg"
                        @error="handlePersonalError" />
                    <div class="personal-info_box">
                        <h2 class="portal-font-color-lv1" :title="personalObj.name">
                            {{personalObj.name }}，{{ $Lan(lanFunName, "hello", "你好") }}~</h2>
                        <div class="personal-info_details portal-font-color-lv3">
                            <span v-if="personalObjConfig.infoList.includes(0)"
                                class="ellipsis-text">{{ $Lan(lanFunName, "structure", "组织机构") }} :
                                {{personalObj.organizationName || '-'}}</span>
                            <span
                                v-if="personalObjConfig.infoList.includes(1)">{{ $Lan(lanFunName, "lastTime", "上次登录时间") }}
                                :
                                {{personalObj.lastLogTime || '-'}}</span>
                            <span
                                v-if="personalObjConfig.infoList.includes(2)">{{ $Lan(lanFunName, "loginIp", "登录的IP地址") }}
                                :
                                {{personalObj.lastLogIp || '-'}}</span>
                        </div>
                    </div>
                </div>
                <div class="card__personalData_box" v-if="personalInfo.length">
                    <div v-for="(pItem, pIndex) in realLists" :key="pItem.index"
                        style="display: flex; flex-wrap: wrap"
                        :style="{ 'margin-top': pIndex === 0 ? '0px' : '12px' }">
                        <!-- 外层循环 -->
                        <!-- <div v-for="(pItem, pIndex) in (() => {
                    //生成实际渲染列表数据
                    let arr = [],
                      zIndex = Math.ceil(personalInfo.length / parseInt(columns));
                    for (let i = 0; i < zIndex; i++) {
                      arr.push({
                        index: i,
                        children: [],
                      });
                      for (let j = 0; j < personalInfo.length; j++) {
                        if (Math.floor(j / parseInt(columns)) === i) {
                          arr[i].children.push(personalInfo[j]);
                        }
                      }
                    }
                    return arr;
                  })()" :key="pItem.index" style="display: flex; flex-wrap: nowrap"
                            :style="{ 'margin-top': pIndex === 0 ? '0px' : '12px' }"> -->
                        <!-- 内层循环 -->
                        <div class="card__personalData__item_wrapper" v-for="(item, index) in pItem.children"
                            @mouseover="
                      (e) => {
                        if (filterClickUrlAble(item)) {
                          let dom = e.currentTarget;
                          dom
                            .getElementsByClassName(
                              'card__personalData__item_body'
                            )[0]
                            .classList.add('portal-primary-color-lv1');
                        }
                      }
                    " @mouseout="
                      (e) => {
                        let dom = e.currentTarget;
                        dom
                          .getElementsByClassName('card__personalData__item_body')[0]
                          .classList.remove('portal-primary-color-lv1');
                      }
                    " :key="index" :class="
                      filterClickUrlAble(item)
                        ? 'portal-primary-backgroundcolor-hover-my cursor-pointer'
                        : ''
                    ">
                            <div v-if="
                        !(item.isEmail && (!item.mainInfo || !item.mainInfo.length))
                      " @click="handlerEnterClick(item)">
                                <div class="card__personalData__item_title">
                                    <div class="flex items-center flex-1 overflow-hidden">
                                        <img v-if="item.iconUrl" :src="item.iconUrl" @error="handleError" />
                                        <span class="
                              card__personalData__item_title_text
                              overflow-ellipsis
                              portal-font-color-lv3
                            " :title="item.title">{{ item.title }}</span>
                                    </div>
                                    <div v-if="item.isEmail && showBindMailFlag"
                                        :title="$Lan(lanFunName, 'textBindOff', '解绑')"
                                        @click.stop="handlerOffEmail(item, index)" class="
                            card__personalData__item_title_btn-status
                            cursor-pointer
                            portal-primary-color-lv1
                            text-right
                          ">
                                        {{ $Lan(lanFunName, "textBindOff", "解绑") }}
                                    </div>
                                    <div v-if="item.isEmail && showBindMailFlag"
                                        :title="$Lan(lanFunName, 'textAddEmail', '新增')"
                                        @click.stop="handlerOnEmail(item, index)" class="
                            card__personalData__item_title_btn-status
                            cursor-pointer
                            portal-primary-color-lv1
                            text-right
                          ">
                                        {{ $Lan(lanFunName, "textAddEmail", "新增") }}
                                    </div>
                                </div>
                                <TLoading class="card__personalData__item_body portal-font-color-lv1"
                                    :loading="loading || item.loading">
                                    <div class="width-full overflow-ellipsis">
                                        {{ item.mainInfo }}
                                    </div>
                                </TLoading>
                                <div class="card__personalData__item_bottom portal-font-color-lv3">
                                    <TLoading :loading="loading || item.loading">
                                        <span class="width-full overflow-ellipsis inline-block"
                                            :title="item.subInfo">{{ item.subInfo }}</span>
                                    </TLoading>
                                </div>
                            </div>

                            <div v-else-if="showBindMailFlag" @click.stop="handlerOnEmail(item, index)">
                                <div class="card__personalData__item_title ">
                                    <div class="flex items-center width-full overflow-hidden">
                                        <img v-if="item.iconUrl" :src="item.iconUrl" @error="handleError" />
                                        <span class="
                              card__personalData__item_title_text
                              overflow-ellipsis
                              portal-font-color-lv3
                            " :title="item.title">{{ item.title }}</span>
                                    </div>
                                </div>
                                <div class="card__personalData__item_body ">
                                    <TLoading :loading="loading || item.loading">
                                        <div class="cursor-pointer overflow-ellipsis">
                                            {{ $Lan(lanFunName, "textBindEmail", "绑定邮箱") }}
                                        </div>
                                    </TLoading>
                                </div>
                                <div class="card__personalData__item_bottom portal-font-color-lv3">
                                    <TLoading :loading="loading || item.loading">
                                        <span class="width-full overflow-ellipsis inline-block" :title="
                              $Lan(
                                lanFunName,
                                'textBindOnWaring',
                                '绑定邮箱可收到未读邮件提醒'
                              )
                            ">{{
                              $Lan(
                                lanFunName,
                                "textBindOnWaring",
                                "绑定邮箱可收到未读邮件提醒"
                              )
                            }}</span>
                                    </TLoading>
                                </div>
                            </div>

                            <!--不显示解绑绑定功能-->
                            <div v-else>
                                <div class="card__personalData__item_title">
                                    <div class="flex items-center width-full overflow-hidden ">
                                        <img v-if="item.iconUrl" :src="item.iconUrl" @error="handleError" />
                                        <span class="
                              card__personalData__item_title_text
                              overflow-ellipsis
                              portal-font-color-lv3
                            " :title="item.title">{{ item.title }}</span>
                                    </div>
                                </div>
                                <div class="card__personalData__item_body overflow-ellipsis">
                                    <TLoading :loading="loading || item.loading">
                                        <span>{{
                            $Lan(lanFunName, "itemBody", "当前无邮箱")
                          }}</span>
                                    </TLoading>
                                </div>
                                <div class="card__personalData__item_bottom portal-font-color-lv3">
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

                <empty-con v-if="!personalObjConfig.isDisplay && !personalInfo.length"
                    :tip="$Lan(lanFunName, 'noPersonalData', '暂无相关配置')" :height="height"></empty-con>
                <TBindEmailModal v-model="emailConfig.show" :DZcardID="DZcardID" :DZcardName="DZcardName" :options="emailConfig.options" @on-ok="refreshEmailData"
                    :router="router">
                </TBindEmailModal>
            </template>

            <TNoLogin v-else :lan-fun-name="lanFunName" :router="router"></TNoLogin>
        </AutoContainer>
    </div>
</template>

<script>
    import TLoading from "./TLoading";
    import TNoLogin from "./TNoLogin";
    import TBindEmailModal from "./TBindEmailModal";
    // import { initTrack } from '../../mixins/track.js';
    export default {
        components: {
            TLoading,
            TBindEmailModal,
            TNoLogin,
        },
        // mixins: [initTrack],
        watch: {
            personalInfo() {
                this.$nextTick(() => {
                    let div = document.getElementsByClassName(
                        "card__personalData__item_wrapper"
                    );
                    for (let i = 0; i < div.length; i++) {
                        //div[i].style.width = `${div[0].clientWidth}px`;
                    }
                });
            },
        },
        data() {
            return {
                defaultImg: require("../../assets/default.png"),
                isOnEmail: false,
                emailConfig: {
                    show: false,
                    options: {},
                },
                lanFunName: "SYS_CARD_PERSONALDATA",
                // lg: languages[this.$i18n.locale],
                hasLogin: window.shell.getUserInfo() ? true : false,
                personalInfo: [],
                // router: {
                //   cardId:'SYS_CARD_PERSONALDATA',
                //   cardWid:'SYS_CARD_PERSONALDATA'
                // },
                // rightScroll: { barKeepShow: true, scrollingX: true },
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
            }
        },
        props: {
            index: Number,
            router: Object,
            DZcardID:String,
            DZcardName:String,
        },
        computed: {
            realLists() {
                let arr = [],
                    zIndex = Math.ceil(this.personalInfo.length / parseInt(this.columns));

                for (let i = 0; i < zIndex; i++) {
                    arr.push({
                        index: i,
                        children: [],
                    });
                    for (let j = 0; j < this.personalInfo.length; j++) {
                        if (Math.floor(j / parseInt(this.columns)) === i) {
                            arr[i].children.push(this.personalInfo[j]);
                        }
                    }
                }
                return arr;
            },
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
        },
        mounted() {
            console.log('DZcardID',this.DZcardID,this. DZcardName)
            console.log(this.hasLogin, 88);
            this.initData();
            this.initPersonalInfo()
            console.log(this.personalInfo, 11);
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
                        cardId:  this.DZcardName,
                        cardWid: this.DZcardID,
                        method: "configuredData",
                        param: {
                            lang: this.$i18n.locale,
                            userAccount: window.shell.getUserInfo().userAccount || ''
                        }
                    },
                    (data) => {
                        this.personalObj = data.data
                        console.log('configuredData', data)
                    }
                );
            },
            initData() {
                window.shell.execCardMethod(
                    {
                        cardId:  this.DZcardName,
                        cardWid: this.DZcardID,
                        method: "renderData",
                    },
                    (data) => {
                        this.cardData = data.data || {};
                        if (this.cardData && this.cardData.columns) {
                            this.columns = parseInt(this.cardData.columns);
                        }
                        this.height = this.cardData.serviceCarHeight.value || 500;
                        this.type = this.cardData.serviceCarHeight.type || 0;
                        this.personalObjConfig = this.cardData.personalInfo
                    }
                );
            },
            filterClickUrlAble(item) {
                /*邮箱显示解绑和绑定才可以点击*/
                return (
                    item.linkUrl || (item.isEmail && +this.cardData.showBindMail === 1)
                );
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
                this.getAliiInfoByItem(
                    this.emailConfig.options,
                    this.emailConfig.options.index
                );
            },
            showEmailModal(params) {
                this.emailConfig = {
                    show: true,
                    options: { ...params },
                };
            },
            async handlerEnterClick(item) {
                console.log(99,this.DZcardID,this.DZcardName)
                let linkUrl = item.linkUrl;
                //this.handleClickTrack(); //点击埋点
                if (item.isEmail) {
                    let { data } = await window.shell.execCardMethod({
                        //cardId: "SYS_CARD_PERSONALDATA",
                        // cardWid: "6093284542296009",
                        cardId:this.DZcardName,
                        cardWid:this.DZcardID,
                        method: "getLinkUrl",
                        param: { mailAccount: item.extraInfo },
                    });
                    linkUrl = data;
                    console.log(linkUrl)
                }
                if (linkUrl) {
                    window.open(linkUrl, "_blank");
                }
            },
            async handlerOnEmail(item, index) {
                //this.handleClickTrack(); //点击埋点
                let { data } = await window.shell.execCardMethod({

                    cardId:this.DZcardName,
                    cardWid: this.DZcardID,
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
                    this.$Lan(this.lanFunName, "textHasEmail", "检测到已有邮箱") +
                    data.account,
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
                       //this.handleClickTrack(); //点击埋点
                    })
                    .catch((action) => {
                        setTimeout(() => {
                            if (action === "cancel") {
                                this.showEmailModal({ ...item, index });
                               //this.handleClickTrack(); //点击埋点
                            }
                        }, 500);
                    });
            },
            async submitOnEmail(email, item, index) {
                let res = await window.shell.execCardMethod({

                    cardId:  this.DZcardName,
                        cardWid: this.DZcardID,
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
               //this.handleClickTrack(); //点击埋点
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
                       //this.handleClickTrack(); //点击埋点
                        let res = await window.shell.execCardMethod({

                            cardId:  this.DZcardName,
                        cardWid: this.DZcardID,
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
                           //this.handleClickTrack(); //点击埋点
                        }
                    });
            },
            async setPersonDetail(item, index) {
                if (+item.needRetrieve !== 1) {
                    this.$set(this.personalInfo, index, item);
                    return;
                }
                this.$set(this.personalInfo, index, {
                    title: item.title,
                    iconUrl: item.iconUrl,
                    loading: true,
                });
                let { data } = await window.shell.execCardMethod({

                    cardId:  this.DZcardName,
                        cardWid: this.DZcardID,
                    method: "getPersonalDataDetail",
                    param: { wid: item.wid, extraInfo: item.extraInfo },
                });
                this.$set(this.personalInfo, index, data || []);
            },
            getAliiInfoByItem(item) {
                var self = this
                window.shell.execCardMethod(
                    {
                        cardId:  this.DZcardName,
                        cardWid: this.DZcardID,
                        method: "getPersonalDataList"
                    },
                    ({ data }) => {
                        let flag = 0;
                        data.forEach((el, i) => {
                            if (item.wid === el.wid) {
                                flag++;
                                if (self.personalInfo.length < data.length && flag === 1) {
                                    self.personalInfo.splice(i, 0, { item });
                                } else if (data.length < self.personalInfo.length && flag === 1) {
                                    self.personalInfo.splice(i, 1);
                                }
                                self.setPersonDetail(el, i);
                            }
                        });
                        console.log(self.personalInfo, 77);
                        console.log(data);
                    }
                );
            },
            getAllInfo() {
                this.loading = true;
                var self = this
                console.log(self.personalInfo, 44);
                window.shell.execCardMethod(
                    {
                        cardId:  this.DZcardName,
                        cardWid: this.DZcardID,
                         method: "getPersonalDataList"
                    },
                    ({ data }) => {
                        self.loading = false;
                        data = data || [];
                        if (data.length === 0 || self.personalInfo.length !== data.length) {
                            self.personalInfo = [];
                        }
                        data.forEach((el, index) => {
                            self.setPersonDetail(el, index);
                        });
                        //self.loadedEndTrack(); // 加载结束埋点
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
        /* background-color: #f0f0f0 !important; */
    }

    .card__personalData {
        // max-height: 500px;
        min-height: 100px;
        padding-top: 20px;
        min-width: 360px;
        display: flex;
        .card__personalData_box {

            overflow: auto;
            height: 113px;
        }

        .text-right {
            text-align: right;
        }

        .card__personalData__item_title_btn-status {
            display: none;
            width: 40px;
            line-height: 20px;
            // font-family: MicrosoftYaHei;
            font-size: 12px;
            letter-spacing: 0;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
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
            flex: 1 1 auto;
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
        .card__personalData__item_wrapper:nth-child(3n){
            margin-right: 0;
        }

        .card__personalData__item_wrapper {
            /* margin-right:12px; */
            /* background: #f5f5f5; */
            /* border-radius: 4px; */
            width: calc(100% / 3);
            /* min-width: 174px; */
            /* height: 98px; */
            // border: 1px solid;
            /* padding: 12px 16px 0 16px; */
            font-size: 14px;
            display: flex;
            flex-direction: column;
            margin-bottom: 12px;

            &:first-child {
                margin-left: 0;
            }

            &:hover .card__personalData__item_title_btn-status {
                display: inline-block;
            }

            .card__personalData__item_title {
                display: flex;
                justify-content: space-between;

                img {
                    width: 14px;
                    height: 14px;
                    margin-right: 8px;
                    margin-top: 3px;
                }

                .card__personalData__item_title_text {
                    width: calc(100% - 26px);
                    // font-family: MicrosoftYaHei;
                    font-size: 12px;
                    letter-spacing: 0;
                    line-height: 20px;
                    font-weight: 400;
                    font-size: 15px;
                    line-height: 19px;
                    color: #1D1D1F!important;
                }
            }

            .card__personalData__item_body {
                // font-size: 16px;
                // margin-top: 6px;
                // height: 20px;
                // font-weight: 600;
                // font-family: MicrosoftYaHei-Bold;
                font-weight: bold;
                font-size: 16px;
                color: #262626;
                letter-spacing: 0;
                line-height: 24px;
                margin-top: 4px;
                font-weight: 400;
                font-size: 14px;
                line-height: 18px;
                color: #595959;

                &.lesson__body {
                    display: flex;
                    align-items: center;
                    margin-top: 0;
                    height: 100%;
                }
            }

            .card__personalData__item_bottom {
                // margin-top: 3px;
                // line-height: 20px;
                // font-size: 12px;
                // font-family: MicrosoftYaHei;
                font-size: 14px;
                color: #8c8c8c;
                letter-spacing: 0;
                line-height: 22px;
                margin-top: 4px;
                display: none;
            }
        }
    }

    .bgHoverWhite:hover {
        background-color: white !important;
    }

    .personal-info {
        width: 100%;
        height: 168px;
        display: flex;
        border-radius: 4px;
        margin-bottom: 12px;
        padding: 18px 16px;
        align-items: center;
        background: #f9f9f9 url("../../assets/bg.png") no-repeat right center;

        img {
            width: 90px;
            //height: 126px;
        }

        .personal-info_box {
            margin-left: 16px;
            width: calc(100% - 106px);

            h2 {
                color: #262626;
                font-weight: bold;
                font-size: 18px;
                line-height: 26px;
                letter-spacing: 0px;
                text-align: left;
                width: 100%;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
            }

            .personal-info_details {
                display: flex;
                flex-direction: column;
                margin-top: 12px;
                color: #8c8c8c;

                font-size: 14px;
                line-height: 22px;
                letter-spacing: 0px;
                text-align: left;

                span {
                    margin-top: 2px;
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