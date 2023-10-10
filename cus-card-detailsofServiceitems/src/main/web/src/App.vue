<!-- <i18n>
{
  "en_US": {
    "emptyTip1": "This service item is temporarily unavailable",
    "emptyTip2": "If you have any questions, please contact the administrator"
  },
  "zh_CN": {
    "emptyTip1": "此服务事项暂时无法使用",
    "emptyTip2": "如有问题请联系管理员"
  }
}
</i18n> -->
<template>
  <div class="itemdetails" ref="itemdetails">
    <!-- {{ $i18n.locale }} -->
    <template v-if="!emptyFlag">
      <div class="emptyName portal-font-color-lv1">{{ itemName }}</div>
      <div class="emptyImg">
        <img src="../public/img/empty.png" alt />
      </div>
      <div class="w1 portal-font-color-lv3">
        {{ $Lan(lanFunName, "appEmptyTip1", "此服务事项暂时无法使用") }}
      </div>
      <div class="w2 portal-font-color-lv3">
        {{ $Lan(lanFunName, "appEmptyTip2", "如有问题请联系管理员") }}
      </div>
    </template>
    <Header
      v-else
      v-bind="{
        itemdetail_width,
        mainTitle,
        serviceItemInfo,
        config,
        headercansee,
        showOnlineButton,
        favFlag,
        lanFunName,
        router,
        itemWid
      }"
      @collect-change="updateData"
      @open-eval="showEv"
      @copy-link="copyLink"
      @show-eval="isShowEvList = true"
    />
    <!--内容信息-->
    <div class="content" v-if="emptyFlag" ref="content" :id="initConId">
      <div class="content-left" v-if="!isLoading">
        <BaseInfo v-bind="{ itemdetail_width, serviceItemInfo, lanFunName }" />
        <DealOnline
          v-bind="{
            serviceList,
            showOnlineButton,
            isLogin,
            lanFunName,
            router
          }"
          :id="dealId"
        />
        <div
          v-for="item in indptModuls"
          :key="item.fieldWid"
          style="position: relative"
          :id="item.fieldWid"
        >
          <ContactPhone
            v-bind="{
              item,
              fieldName: item.fieldName,
            }"
            v-if="
              item.fieldWid === 'CONTACT_PHONE' && itemCanSee(item.fieldValue)
            "
          />
          <Instructions
            v-bind="{
              item,
              fieldName: item.fieldName,
              router
            }"
            v-else-if="
              item.fieldWid === 'INSTRUCTIONS' && itemCanSee(item.fieldValue)
            "
          />
          <ProcessFlow
            v-bind="{
              item,
              lanFunName,
              fieldName: item.fieldName,
              router
            }"
            v-else-if="
              item.fieldWid === 'PROCESS_FLOW' && itemCanSee(item.fieldValue)
            "
          />
          <RelatedMaterials
            v-bind="{
              item,
              fieldName: item.fieldName,
              router
            }"
            v-else-if="
              item.fieldWid === 'RELATED_MATERIALS' &&
                itemCanSee(item.fieldValue)
            "
          />
          <DealAddress
            v-bind="{
              item,
              mapFlag,
              fieldName: item.fieldName,
              router
            }"
            v-else-if="
              item.fieldWid === 'DEAL_ADDRESS' && itemCanSee(item.fieldValue)
            "
          />
          <DealTime
            v-bind="{
              item,
              fieldName: item.fieldName,
              router
            }"
            v-else-if="
              item.fieldWid === 'DEAL_TIME' && itemCanSee(item.fieldValue)
            "
          />
          <FAQS
            v-bind="{
              item,
              lanFunName,
              fieldName: item.fieldName,
              router
            }"
            v-else-if="item.fieldWid === 'FAQS' && itemCanSee(item.fieldValue)"
          />
          <Other
            v-else
            v-bind="{
              item,
              lanFunName,
            }"
          />
        </div>
      </div>
      <div class="content-right" v-if="config.pageConfigure.includes('navbar')">
        <NavBar
          v-bind="{ navStyle, navList, activeKey, lanFunName }"
          @nav-change="getPlace"
        />
      </div>
    </div>
    <template v-if="!isLoading">
      <Evaluate
        v-model="isShowEv"
        v-bind="{ card, itemName, itemWid, appraiseName, lanFunName, router }"
      />
    </template>

    <showEvaluates
      v-model="isShowEvList"
      v-bind="{ itemWid, card, appraiseName, lanFunName, router }"
    />
  </div>
</template>

<script>
import Header from "./components/Header";
import ContactPhone from "./components/ContactPhone";
import Other from "./components/Other";
import ProcessFlow from "./components/ProcessFlow";
import RelatedMaterials from "./components/RelatedMaterials";
import DealAddress from "./components/DealAddress";
import DealTime from "./components/DealTime";
import FAQS from "./components/FAQS";
import Instructions from "./components/Instructions";
import NavBar from "./components/NavBar";
import Evaluate from "./components/Evaluate";
import BaseInfo from "./components/BaseInfo";
import showEvaluates from "./components/showEvaluates";
import DealOnline from "./components/DealOnline.vue";
export default {
  name: "detailsofserviceitems",
  components: {
    Header,
    BaseInfo,
    ContactPhone,
    Other,
    ProcessFlow,
    RelatedMaterials,
    DealAddress,
    DealTime,
    FAQS,
    Instructions,
    NavBar,
    Evaluate,
    showEvaluates,
    DealOnline,
  },
  props: {
    index: Number,
    router: Object,
  },
  data() {
    const { cardWid, cardId } = this.router;
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      iTime: 0,
      card: {
        cardWid,
        cardId,
      },
      lanFunName: this.router.cardId,
      initConId: `id_${new Date().getTime()}`,
      dealId: `deal_${new Date().getTime()}`,
      itemWid: "",
      mainTitle: "",
      itemName: "",
      isShowEv: false,
      isShowEvList: false,
      favFlag: false,
      top: 0,
      activeKey: "",
      navStyle: "",
      headercansee: "none",
      config: {
        colorGroup: "light",
        pageConfigure: [],
        detailConfigure: [],
      },
      mapFlag: false,
      serviceItemInfo: null,
      indptModuls: [],
      navList: [],
      navFieldWids: [],
      emptyFlag: true,
      isLogin: false,
      // loginUrl:'',
      location: {},
      v_top: 0,
      v_height: 0,
      startTop: 0,
      new_scrollTop: 0,
      scrollFlag: null,
      isScroll: null,
      itemdetail_width: null,
      showOnlineButton: 0,
      isLoading: false,
      appraiseName: [],
      serviceList: [],
      serviceItemWid: "",
      // navBarLeft: "",
    };
  },
  filters: {},
  computed: {
    loginUrl() {
      const hash = this.location.hash || "";
      return hash.replace("#", "");
    },
  },
  methods: {
    resizeNav(val) {
      this.top = val.scrollTop || this.new_scrollTop;
      let top = val.scrollTop || this.new_scrollTop;
      // console.log("top", top);
      if (top > this.startTop) {
        this.headercansee = "";
        let m_left = this.itemdetail_width / 2 - 90;
        if (navigator.userAgent.indexOf("AppleWebKit") > -1) {
          m_left = m_left - 4;
        } else {
          m_left = m_left - 8;
        }
        // this.navBarLeft = `margin-left:${m_left - 4}px`;
        this.navStyle = `position: fixed; top: 110px;left:50%;margin-left:${m_left}px;width:90px`;
      } else {
        this.headercansee = "none";
        this.navStyle = "";
      }
    },
    // htmlDecode(text) {
    //   //1.首先动态创建一个容器标签元素，如DIV
    //   var temp = document.createElement("div");
    //   //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
    //   temp.innerHTML = text;
    //   //3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
    //   var output = temp.innerText || temp.textContent;
    //   temp = null;
    //   return output;
    // },
    itemCanSee(a) {
      if (a) {
        if (Array.isArray(a)) {
          if (a.length) {
            return true;
          } else {
            return false;
          }
        }
        return true;
      } else {
        return false;
      }
    },

    showEv() {
      // 评价
      if (!this.isLogin) {
        window.shell.login({
          params: {
            localHref: this.loginUrl,
          },
        });
        return;
      }
      this.isShowEv = true;
    },
    getPlace(item) {
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
      this.scrollFlag = true;
      // 导航跳转
      this.activeKey = item.fieldWid;
      console.log("activeKey", this.activeKey);
      let con_el = document.getElementById(this.activeKey); // 当前导航定位处
      if (con_el) {
        this.v_top = window.shell.getElementTop(con_el);
        window.shell.emit("vs-scroll-to", { y: this.v_top - 80 }, 300);
        //  window.shell.emit("vs-scroll-to", this.v_top - 80);
      }

      clearTimeout(this.iTime);
      this.iTime = setTimeout(this.scrollFlagChange, 500);
    },
    scrollFlagChange() {
      this.scrollFlag = false;
    },
    debounce(fn, interval) {
      var timer;
      var gapTime = interval;
      return function(e) {
        clearTimeout(timer);
        var that = this;
        var args = arguments; //保存此处的arguments，因为setTimeout是全局的，arguments不是防抖函数需要的。
        timer = setTimeout(function() {
          fn.call(that, e, args);
        }, gapTime);
      };
    },

    initNavScoll(topNum) {
      let indexNavTop = topNum;
      if (indexNavTop >= this.startTop) {
        this.navStyle = { top: "110px", position: "fixed" };
      } else {
        this.navStyle = { top: "175px" };
      }
    },
    initActive(sc_top) {
      let con_el = ""; // 当前导航定位处
      for (let n = 0; n < this.navFieldWids.length; n++) {
        con_el = document.getElementById(this.navFieldWids[n]);
        if (con_el) {
          this.v_top = window.shell.getElementTop(con_el);
          // console.log(this.v_top);
          this.v_height = con_el.offsetHeight;
          // console.log("当前内容div块距离顶部的距离: " + this.v_top); // 当前内容div块距离顶部的距离
          // console.log("当前内容div块的高度: " + this.v_height); // 当前内容div块的高度
          if (sc_top >= this.startTop - 100) {
            if (
              sc_top + 80 <= this.v_top + this.v_height &&
              sc_top + 120 > this.v_top
            ) {
              this.activeKey = this.navFieldWids[n];
            }
          }
        }
      }
    },
    copyLink() {
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
      // 复制分享链接
      const node = document.createElement("span");
      node.innerText = this.location.href;
      node.style.opacity = 0;
      document.body.appendChild(node);
      window.getSelection().selectAllChildren(node);
      document.execCommand("Copy");
      document.body.removeChild(node);
      this.$message.success({
        message: this.$Lan(this.lanFunName, "copyMessage", "复制链接成功"),
        duration: 3000,
      });
      // console.log(this.linkUrl1);
    },
    updateData(operate) {
      if (!this.isLogin) {
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
        console.log("未登录");
        window.shell.login({
          params: {
            localHref: this.loginUrl,
          },
        });
        return;
      }
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
            fucType: operate == 0 ? 3 : 2
          }
        },
        startTime: new Date().getTime()
      });
      window.shell.collectServiceItem({
        id: this.itemWid, //事项ID 收藏或取消收藏时用
        operate: operate, // 收藏操作标识 0：取消收藏 1：收藏
      });
      this.favFlag = operate ? true : false;
      // console.log(this.favFlag)
    },
    getData() {
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
      this.isLoading = true;
      window.shell
        .execCardMethod({
          ...this.card,
          method: "renderData",
          param: {
            wid: this.itemWid, //wid 为事项ID 必传
            lang: this.$i18n.locale,
          },
        })
        .then((data) => {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime()
          });
          this.$nextTick(() => {
            this.isLoading = false;
          });
          if (data.errcode == 0) {
            if (data.data.serviceItemInfo) {
              // 未登录跳转页面
              this.isLogin = !data.data.hasToLogin;
              // console.log(this.isLogin);
              this.mainTitle = window.shell.htmlEncodeToStr(
                data.data.serviceItemInfo.itemName || ""
              ); // 转码
              this.itemName = data.data.itemName;
              // 浏览器标题
              window.shell.setBroswerTitle(
                data.data.serviceItemInfo.itemName || ""
              );

              this.showOnlineButton = parseInt(data.data.showOnlineButton); // !!!!!!!!!!!!!! 【在线办理】展示的三种样式：可见/隐藏/禁用

              this.favFlag = data.data.favorite;
              this.config = data.data.config;
              this.mapFlag = data.data.akKey !== "false"; // 地图是否可见
              this.serviceItemInfo = data.data.serviceItemInfo;
              this.appraiseName = this.serviceItemInfo.appraiseNames;
              this.linkService = data.data.linkService;
              this.serviceItemWid = data.data.serviceItemWid;
              if (!this.serviceItemInfo.baseInfos.length) {
                this.emptyFlag = false;
              }
              this.serviceItemInfo.baseInfos.forEach((item) => {
                if (!item.fieldValue) {
                  item.fieldValue = "-";
                }
              });
              // this.tableData = this.serviceItemInfo.baseInfos; // 基本信息
              this.indptModuls = this.serviceItemInfo.indptModuls;
              this.navList = [
                {
                  fieldName: this.$Lan(
                    this.lanFunName,
                    "basicInformation",
                    "基本信息"
                  ),
                  fieldWid: this.initConId,
                },
                {
                  fieldName: this.$Lan(this.lanFunName, "dealItem", "在线办理"),
                  fieldWid: this.dealId,
                },
              ].concat(
                this.serviceItemInfo.indptModuls.filter((it) =>
                  this.itemCanSee(it.fieldValue)
                )
              );
              if (this.showOnlineButton == 0) {
                this.navList.splice(1, 1);
              } else {
                this.getServiceList();
              }
              this.activeKey = this.navList[0].fieldWid;
              this.navList.forEach((it) => {
                this.navFieldWids.push(it.fieldWid);
              });
            } else {
              this.emptyFlag = false;
              this.itemName = data.data.itemName;
              window.shell.setBroswerTitle(this.itemName || "");
            }
          } else {
            this.$message({
              showClose: false,
              message: this.$Lan(
                this.lanFunName,
                "copyMessage",
                "获取事项详情错误"
              ),
              type: "error",
            });
          }
        })
        .catch(()=> {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime()
          });
        });
    },
    getServiceList() {
      window.shell.execTemplateMethod(
        "getServiceItemRelService",
        {
          id: this.serviceItemWid,
          langCountry: "zh_CN",
        },
        (res) => {
          if (res.errcode === "0") {
            this.serviceList = (res.data && res.data.serviceList) || [];
            // this.serviceList = data.filter((item) => {
            //   return item.serviceStation != 1;
            // });
            if (this.serviceList.length == 0) {
              this.navList.splice(1, 1);
            }
          }
        }
      );
    },
  },
  watch: {
    // isScroll() {
    //   var button = document.getElementById(this.initConId);
    //   button.click();
    // },
  },
  mounted() {
    window.shell.on("collectService", this.handleUpdateFavorite);
    this.$nextTick(() => {
      this.itemdetail_width = this.$refs.itemdetails.offsetWidth;
      // console.log(this.itemdetail_width);
    });
    window.shell.on("onScoll", (ev) => {
      let con_el = this.$refs.content;
      this.startTop = window.shell.getElementTop(con_el);
      this.isScroll = ev.scrollTop;
      let sc_top = ev.scrollTop; // 滚动条距离顶部的距离
      // console.log(sc_top)
      this.new_scrollTop = sc_top;
      // console.log("滚动条距离顶部的距离: " + sc_top);
      !this.scrollFlag && this.initActive(sc_top);
    });

    let m = this;
    window.onresize = function(val) {
      m.resizeNav(val);
    };

    // 吸顶效果
    const val = 0;
    window.shell.emit("card-scroll", val);
    window.shell.on("card-scroll", (val) => {
      this.resizeNav(val);
    });
  },
  created() {
    this.location = window.shell.getLocation() || {};
    this.location.href = this.location.href.split("&name")[0];
    // url获取
    let params = window.shell.getUrlParam();

    this.itemWid = (params && params.wid) || "";

    this.getData();
    window.shell.on("collectServiceItem", (val) => {
      this.favFlag = val.operate ? true : false;
    });
  },
  beforeDestroy() {
    window.shell.off("collectServiceItem");
    window.shell.off("card-scroll");
    window.shell && window.shell.off("check-card");
    window.shell.off("collectService");
  },
};
</script>

<style lang="less" scoped>
.detail-wrapper {
  white-space: pre-wrap;
  word-break: break-all;
}

.greyZxbl {
  opacity: 0;
  position: relative;
  left: 101px;
}

.w1 {
  // font-family: PingFangSC-Medium;
  font-size: 14px;
  letter-spacing: 0;
  text-align: center;
  margin-top: 40px;
  margin-bottom: 12px;
  font-weight: bold;
  /* margin: 0 auto; */
}
.w2 {
  // font-family: PingFangSC-Regular;
  font-size: 12px;
  letter-spacing: 0;
  text-align: center;
}
.emptyImg {
  margin: 0 auto;
  margin-top: 200px;
  width: 447px;
}
.emptyName {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  /* border: 1px solid red; */
  width: 600px;
  height: 48px;
  font-size: 40px;
  color: #ffffff;
  letter-spacing: 0;
  line-height: 38px;
  // font-family: PingFangSC-Medium;
  position: relative;
  left: 100px;
}
/* .anchor-item-active::before {
  display: block;
  position: absolute;
  content: '';
  width: 6px;
  height: 6px;
} */

.itemdetails {
  // width: 1200px;
  // width: 100%;
  margin: 0 auto;
  position: relative;
  min-height: 600px;
  padding-top: 20px;
}
.content {
  /* background: skyblue; */
  // margin-top: 20px;
  display: flex;
  position: relative;
  .content-left {
    flex: 1;
    width: 0;
    & > div:nth-child(n + 2) {
      margin-top: 20px;
    }
  }
  .content-right {
    width: 126px;
    overflow: hidden;
  }
  /deep/.title1 {
    border-bottom: 1px solid #f0f0f0;
    height: 50px;
    // font-family: PingFangSC-Medium;
    font-size: 18px;
    /* border:1px solid red; */
    line-height: 50px;
    margin-bottom: 12px;
    font-weight: bold;
  }
}

/deep/.we-dialog__wrapper {
  overflow: hidden !important;
}
// /deep/.we-dialog {
//   margin-top: 5vh !important;
// }
.item:focus {
  outline: none;
}
a:link {
  text-decoration: none;
}
a:visited {
  text-decoration: none;
}
a:active {
  text-decoration: none;
}
</style>
