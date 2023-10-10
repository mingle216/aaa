<template>
  <div class="header-box" v-if="serviceItemInfo">
    <!--头部-->
    <div
      class="header"
      :class="[config.colorGroup === 'deep' ? 'deep-header' : 'light-header']"
    >
      <div class="h-container">
        <!-- <div class="pic"><img :src="serviceItemInfo.iconUrl || errorImg" @error="handleError" /></div> -->
        <div class="pic" v-if="config.pageConfigure.includes('icon')">
          <img
            :src="serviceItemInfo.iconUrl || errorImg"
            @error="handleError"
            alt
          />
        </div>
        <div class="title-evaluate">
          <!-- <div class="title">{{ serviceItemInfo.itemName }}</div> -->
          <we-tooltip
            class="item"
            effect="dark"
            placement="bottom-start"
            :open-delay="900"
          >
            <div slot="content" v-html="mainTitle"></div>
            <div
              class="title"
              :class="{
                'portal-font-color-lv1': config.colorGroup === 'deep',
              }"
              v-html="mainTitle"
            >
              <!-- {{ mainTitle }} -->
            </div>
          </we-tooltip>
          <div
            class="evaluate"
            :class="{
              'portal-font-color-lv3': config.colorGroup === 'deep',
            }"
            v-if="config.detailConfigure.includes('evaluation')"
          >
            <div class="star">
              <we-rate
                :value="score"
                disabled
                text-color="#ff9900"
                score-template="{value}"
              ></we-rate>
            </div>
            <span class="score">{{ serviceItemInfo.appraiseMark }}</span>
            <span
              class="countdetail"
              :class="{
                'portal-primary-color-hover-lv1':
                  config.showAppraise && config.colorGroup === 'deep',
                'show-appraise ': config.showAppraise,
              }"
              @click="clickEvHandle"
            >
              ({{ serviceItemInfo.appraiseNum }}
              {{ $Lan(lanFunName, "evCount", "条评论") }})
            </span>
          </div>
        </div>
        <div class="btn-group">
          <!-- <div class="greyZxbl" @click="handleOnline" :style="cStyle"></div> -->
          <div
          v-if="showOnlineButton != 0 && serviceList.length != 0"
            @click="onlinebtn()"
            class="btn-item"
            :class="[btnModel]"
          >
           在线办理
          </div>
          <div
            v-if="config.detailConfigure.includes('evaluation')"
            @click="showEv()"
            class="btn-item"
            :class="[btnModel]"
          >
            {{ $Lan(lanFunName, "evaluate", "评价") }}
          </div>
          <div
            v-if="config.detailConfigure.includes('favourite') && !favFlag"
            @click="updateData(1)"
            class="btn-item"
            :class="[btnModel]"
          >
            {{ $Lan(lanFunName, "collect", "收藏") }}
          </div>
          <div
            v-if="config.detailConfigure.includes('favourite') && favFlag"
            @click="updateData(0)"
            class="btn-item"
            :class="[btnModel]"
          >
            <span class="icon iconfont icon-shixiangshoucang myFav"></span
            >{{ $Lan(lanFunName, "collected", "已收藏") }}
          </div>
          <div
            v-if="config.detailConfigure.includes('share')"
            class="btn-item popoverClassname"
            @click="fx()"
            :class="[btnModel]"
          >
            {{ $Lan(lanFunName, "share", "分享") }}
            <we-popover
              popper-class="myPopover"
              abc="abcabc"
              placement="bottom"
              width="118"
              height="174"
              trigger="click"
              v-model="headerQr"
            >
              <div
                class="portal-primary-color-lv1 text-center copyText"
                @click="copyLink()"
              >
                <i
                  class="iconfont icon-lianjie "
                  style="font-size: inherit"
                ></i>
                {{ $Lan(lanFunName, "copyLink", "复制链接") }}
              </div>
              <div class="s2">
                <div id="qrcode2" ref="qrcode2"></div>
              </div>
              <div class="s3 text-center">
                <span>“{{ $Lan(lanFunName, "scan", "扫一扫") }}”</span>
              </div>
              <we-button class="real" slot="reference">click 激活</we-button>
            </we-popover>
          </div>
          <!-- <div
           v-if="showOnlineButton!=0"
            :class="[onlineBtnModel,showOnlineButton===1?'btn-disable':'']"
            @click="handleOnline"
          >
            在线办理
          </div> -->
        </div>
      </div>
    </div>
    <!--吸顶/滚动时的固定简约头部-->
    <div class="fix-header" :style="`display:${headercansee}`">
      <div class="fh-container" :style="`min-width:${itemdetail_width}px`">
        <div class="pic2" v-if="config.pageConfigure.includes('icon')">
          <img
            :src="serviceItemInfo.iconUrl || errorImg"
            @error="handleError"
            alt
          />
        </div>
        <div class="title-evaluate2">
          <we-tooltip
            class="item"
            effect="dark"
            placement="bottom-start"
            :open-delay="900"
          >
            <div slot="content" v-html="mainTitle"></div>
            <div class="title2" v-html="mainTitle"></div>
          </we-tooltip>
          <div
            class="evaluate2 portal-font-color-lv3"
            v-if="config.detailConfigure.includes('evaluation')"
          >
            <div class="star2">
              <we-rate
                :value="score"
                disabled
                text-color="#ff9900"
                score-template="{value}"
              ></we-rate>
            </div>
            <span class="score2">{{ serviceItemInfo.appraiseMark }}</span>
            <span
              class="countdetail2"
              :class="{
                'show-appraise portal-primary-color-hover-lv1':
                  config.showAppraise,
              }"
              @click="clickEvHandle"
              >({{ serviceItemInfo.appraiseNum }}
              {{ $Lan(lanFunName, "evCount", "条评论") }})</span
            >
          </div>
        </div>
        <div class="btn-group bg2">
          <!-- <div class="greyZxbl" @click="handleOnline" :style="cStyle"></div> -->

          <div
            v-if="config.detailConfigure.includes('evaluation')"
            @click="showEv()"
            :class="[fixedBtnModel]"
          >
            {{ $Lan(lanFunName, "evaluate", "评价") }}
          </div>
          <div
            v-if="config.detailConfigure.includes('favourite') && !favFlag"
            @click="updateData(1)"
            :class="[fixedBtnModel]"
          >
            {{ $Lan(lanFunName, "collect", "收藏") }}
          </div>
          <div
            v-if="config.detailConfigure.includes('share') && favFlag"
            @click="updateData(0)"
            :class="[fixedBtnModel]"
          >
            <span class="icon iconfont icon-shixiangshoucang myFav"></span
            >{{ $Lan(lanFunName, "collected", "已收藏") }}
          </div>
          <div
            v-if="config.detailConfigure.includes('share')"
            :class="[fixedBtnModel]"
            @click="fx()"
          >
            {{ $Lan(lanFunName, "share", "分享") }}
            <we-popover
              popper-class="myPopover"
              placement="bottom"
              width="118"
              height="174"
              trigger="click"
              v-model="fixedHeaderQr"
              getTooltipContainer="{()=> document.getElementsByClasssName('popoverClassname')[0]}"
            >
              <div
                class="portal-primary-color-lv1 text-center copyText"
                @click="copyLink()"
              >
                <i class="iconfont icon-lianjie" style="font-size: inherit"></i>
                {{ $Lan(lanFunName, "copyLink", "复制链接") }}
              </div>
              <div class="s2">
                <div id="qrcode" ref="qrcode"></div>
              </div>
              <div class="s3 text-center">
                <span>“{{ $Lan(lanFunName, "scan", "扫一扫") }}”</span>
              </div>
              <we-button class="real" slot="reference">click 激活</we-button>
            </we-popover>
          </div>
          <!-- <div
          v-if="showOnlineButton!=0"
            class="zxbl2 portal-primary-backgroundcolor-lv1"
            :class="[onlineBtnModel,showOnlineButton===1?'btn-disable':'']"
         
            @click="handleOnline"
          >
            在线办理
          </div> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import QRCode from "qrcodejs2";
export default {
  props: {
    itemWid: String,
    router: Object,
    mainTitle: {
      type: String,
      default: "",
    },
    showOnlineButton: {
      type: [Number, String],
      default: 0,
    },
    itemdetail_width: {
      type: [Number, String],
      default: 0,
    },
    config: {
      type: Object,
      default: () => {
        return {
          colorGroup: "light",
          pageConfigure: [],
          detailConfigure: [],
        };
      },
    },
    serviceItemInfo: Object,
    headercansee: {
      type: String,
      default: "none",
    },
    favFlag: {
      type: Boolean,
      default: false,
    },
    lanFunName: {
      type: String,
      default: "",
    },
    serviceList:{
      type: String,
      default: "",
    },
    urlname: {
      type: String,
      default: "",
    },
    urlid: {
      type: String,
      default: "",
    },
  },
  watch: {},
  computed: {
    score() {
      return Number(this.serviceItemInfo.appraiseMark) || 0;
    },
    btnModel() {
      if (this.config.colorGroup === "deep") {
        return " portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1 portal-font-color-lv1 deep-btn";
      } else {
        return "light-btn";
      }
    },
    onlineBtnModel() {
      if (this.config.colorGroup === "deep") {
        return "portal-primary-backgroundcolor-lv1 zxbl-deep";
      } else {
        return "portal-primary-color-lv1 zxbl-light portal-primary-backgroundcolor-hover-lv1";
      }
    },
    fixedBtnModel() {
      return "fixed-btn portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1 portal-font-color-lv1";
    },
  },
  data() {
    return {
      errorImg: window.shell.ErrorImg,
      // score: null,
      // cStyle: null,
      headerQr: false,
      fixedHeaderQr: false,
      isUse: null,
      isUse2: null,
    };
  },
  created() {
    window.shell.on("onScoll", () => {
      this.headerQr = false;
      this.fixedHeaderQr = false;
    });
  },
  beforeDestroy() {
    window.shell.off("onScoll");
  },
  methods: {
    
    onlinebtn(){
      if(this.serviceList.length!=0){
        if(this.serviceList.length==1){
          this.handleOnline(this.serviceList[0])
          console.log(0)
        }else{
          //console.log(this.serviceList[0]) 
        window.shell.openOnlineDeal(
          {
            wid: this.urlid,
            name: this.urlname,
          },
        );
        }
      }
    },
    handleOnline(item) {
      if (item.serviceStation === 1) {
        
        return;
      }
      if (item.pcAccessUrl && item.permission) {
        
        window.shell.openService(item);
      } else {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan(
            this.lanFunName,
            "showMessage",
            "暂无使用权限，请联系管理员"
          ),
        });
      }
    },
    crateQrcode(qrcode, text) {
      this.qr = new QRCode(qrcode, {
        width: 84,
        height: 84, // 高度
        text: text, // 二维码内容
      });
    },
    fx() {
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
            fucType: 5
          }
        },
        startTime: new Date().getTime()
      });
      // console.log("fdfs");
      this.$refs.qrcode.innerHTML = "";
      this.$refs.qrcode2.innerHTML = "";
      let qrcode = this.$parent.location.href;
      let qrcode2 = this.$parent.location.href;
      // 使用$nextTick确保数据渲染
      this.$nextTick(() => {
        this.crateQrcode("qrcode", qrcode);
        this.crateQrcode("qrcode2", qrcode2);
      });
    },
    copyLink() {
      this.$emit("copy-link");
    },
    // handleOnline(e) {
    //   if( this.showOnlineButton===2){
    //     this.$emit("go-online", e);
    //   }

    // },
    handleError(e) {
      // console.log('---');
      // console.log(e);
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null;
    },
    updateData(num) {
      this.$emit("collect-change", num);
    },
    showEv() {
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
      this.$emit("open-eval");
    },
    clickEvHandle() {
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
      if (this.config.showAppraise) {
        this.$emit("show-eval");
      }
    },
  },
};
</script>

<style lang="less" scoped>
.real {
  height: 36px;
  width: 100%;
  position: absolute;
  left: -1px;
  top: -1px;
  opacity: 0;
}
.star {
  position: relative;
  top: -2px;
}
.fixed-btn {
  border: 1px solid #d9d9d9;
  position: relative;
  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
    border: 1px solid;
  }
}
.bg2 {
  bottom: 22px;
}
.zxbl2 {
  color: #ffffff !important;
}
.btn-disable {
  cursor: not-allowed;
  color: #c1cbd4 !important;
  background-color: #dfe6ec !important;
}
.s1 {
  cursor: pointer;
  text-align: center;
  height: 30px;
}
.s2 {
  width: 84px;
  height: 84px;
  margin: 6px auto;
  margin-bottom: 8px;
}
.s3 {
  color: #8d8d8d;
  position: relative;
  // left: 5px;
  top: 1px;
  margin: auto;
  // text-align: center;
  display: flex;
  flex-wrap: nowrap;
  width: 84px;
  justify-content: center;
}
.btn-item {
  position: relative;
}
.light-btn {
  color: #fff !important;
  border: 1px solid #fff;
  &:hover {
    background: rgba(255, 255, 255, 0.1);
  }
}
.deep-btn {
  border: 1px solid #d9d9d9;
  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
    border: 1px solid;
  }
}

.header {
  display: flex;
  // height: 150px;
  overflow: hidden; /** pic的margin-top时，可不影响父容器移动 */
  &.deep-header {
    height: 108px;
  }
  &.light-header {
    height: 144px;
    margin-bottom: 48px;
  }
  .h-container {
    display: flex;
    /* background: lightgray; */
    margin-left: 1px;
    /* margin-top: 86px; */
    height: 76px;
    // overflow: hidden;
    width: 100%;
    position: relative;
    .pic {
      margin-right: 30px;
      width: 76px;
      height: 76px;
      border-radius: 10px;
      border-radius: 10px;
      overflow: hidden;
    }
    .title-evaluate {
      display: flex;
      flex-direction: column;
      justify-content: center;
      .title {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        /* border: 1px solid red; */
        width: 600px;
        height: 48px;
        font-size: 36px;
        color: #ffffff;
        letter-spacing: 0;
        line-height: 38px;
        font-weight: bold;
        &:focus {
          outline: none;
        }
        // font-family: PingFangSC-Medium;
      }
      .evaluate {
        top: 5px;
        display: flex;
        margin-top: 10px;
        // font-family: PingFangSC-Regular;
        color: #ffffff;
        position: relative;

        .countdetail {
          &.show-appraise {
            &.portal-primary-color-hover-lv1 {
              &:hover {
                text-decoration: none !important;
              }
            }
            cursor: pointer;
            &:hover {
              text-decoration: underline;
            }
          }
        }
      }
      .star {
        position: relative;
        top: -2px;
      }
      .score {
        margin-right: 15px;
      }
    }
  }
}
.btn-group {
  display: flex;
  margin-top: 42px;
  height: 36px;
  position: absolute;
  right: 0;

  .greyZxbl {
    opacity: 0;
    position: relative;
    left: 101px;
  }
  .zxbl-light {
    // box-shadow: 0 2px 8px 0 rgba(16, 38, 69, 0.4);
    background-color: #fff;
    &:hover {
      color: #ffffff !important;
    }
  }
  .zxbl-deep {
    color: #fff;
  }
  // .btn-item {
  //   border: 1px solid;
  // }
  & > div {
    max-width: 164px;
    padding-left: 20px;
    padding-right: 20px;
    height: 36px;
    border-radius: 4px;
    // font-family: PingFangSC-Medium;
    font-size: 14px;
    letter-spacing: 0;
    text-align: center;
    line-height: 36px;
    cursor: pointer;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    &:not(:last-child) {
      margin-right: 12px;
    }
  }
}
.fix-header {
  z-index: 998;
  padding-left: 20px;
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  margin-left: 1px;
  height: 80px;
  overflow: hidden;
  width: 100%;
  background: #ffffff;
  box-shadow: 0 4px 16px 0 rgba(66, 66, 68, 0.2);

  .title2 {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    /* border: 1px solid red; */
    width: 524px;
    letter-spacing: 0;
    // font-family: PingFangSC-Medium;
    height: 30px;
    position: relative;
    top: 3px;
  }
  .pic2 {
    position: relative;
    top: 16px;
    margin-right: 20px;
    width: 48px;
    height: 48px;
    border-radius: 10px;
    border-radius: 10px;
    overflow: hidden;
  }
  .fh-container {
    // min-width: 1024px;
    left: -15px;
    display: flex;
    position: relative;
    margin: 0 auto;
  }
  .score2,
  .countdetail2 {
    position: relative;
    top: 2px;
    &.show-appraise {
      cursor: pointer;
    }
  }
  .score2 {
    margin-right: 11px;
    margin-left: 3px;
  }
  .evaluate2 {
    top: 5px;
    // font-family: PingFangSC-Regular;
    font-size: 14px;
    display: flex;
    margin-top: 7px;
  }
  .title-evaluate2 {
    position: relative;
    top: -7px;
    display: flex;
    flex-direction: column;
    justify-content: center;

    // font-family: PingFang-SC-Heavy;
    font-size: 24px;
    /* color: #102645; */
    letter-spacing: 0;
    margin-top: 12px;
  }
}

.myFav {
  position: relative;
  left: -4px;
  font-size: 14px;
  color: #fa6444;
}
</style>
<style lang="less">
.myPopover {
  min-width: 118px;
  // height: 172px;
  .text-center {
    text-align: center;
  }
  .copyText {
    cursor: pointer;
  }
}
</style>
