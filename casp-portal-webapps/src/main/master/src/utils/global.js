import Vue from "vue";
import router from "../router";
import * as dd from "dingtalk-jsapi";
import wx from "weixin-js-sdk"; //微信小程序sdk
import axios from "axios";
import api from "../api/api";
import { _sessionStorage } from './storage.js'

import {
  execCardMethod,
  execTemplateMethod,
  login,
  logout,
  serviceShow,
  queryI18nList,
  collectService,
  batchCollectService,
  collectServiceItem,
  collectNews,
  getPlaceholderVal,
  getMessageCount,
  getProgrammeLocalStyle,
  queryServiceByWid,
  getSearchHisVal,
  getScene,
  getStaticData,
  switchUserLanguage,
  getLoginUser,
  getSidebarCount,
  getUserPermissionRouters,
  appraiseSummary,
  queryAppraiseByPage,
  saveAppraise,
  saveSiteitching,
  getServiceHealth,
  saveAppraiseWithPic,
  saveAppraiseWithPicByItem,
  saveAppraiseWithPicOfOneThing,
  submitFeedbackWithPic,
  saveAppraiseWithPicBase64,
  saveAppraiseWithPicByItemBase64,
  saveAppraiseWithPicOfOneThingBase64,
  submitFeedbackWithPicBase64,
  queryAppraiseByPageNew,
  queryOrgUsers,
  searchUserByKeywordAndOrgwid,
  collectOneThing,
  collectTask,
  collectMessage
} from "../api/service";
import { getPageDatas } from "./mock";
import { addCSS, loadAsyncJs, addCssByLink } from "../libs/tools";
import { cutStr, getStrLen } from "../libs/strUtils";
import ErrorImg from "../assets/images/icon-err.png";
import VueI18n from "vue-i18n";
const i18n = new VueI18n({
  locale: sessionStorage.getItem('locale') || "zh_CN", // 定义默认语言为中文
  fallbackLocale: 'zh_CN',
  silentFallbackWarn: true,
  messages: {
    zh_CN: {},
    en: {},
  },
});

const eventBus = new Vue();
let currentRoute = "";
let pageData = null;
let pageLoadingFlag = false;
let staticData = [];
let loginUser = null;
let urlFlag = false;
let currentSite = "";
let siteList = [];
let openRandomSite = '0'; // 是否开启随机站点,1开启
const filesToBase64 = async function(files) {   //file转bse64
  const arr = []
  const toBase64 = function(file) {
    return new Promise((resolve, reject) => {
      let reader = new FileReader();  
      reader.readAsDataURL(file);  
      reader.onload = (e) => {
        resolve(e.target.result)  
      }
      reader.onerror = () => reject(null)
    })
  }
  for (let index = 0; index < files.length; index++) {
    const file = await toBase64(files[index]);
    file && arr.push(file)
  }
  return arr
}

export default {
  ErrorImg,
  openRandomSite,
  serviceShow,
  queryServiceByWid,
  /**
   * @description 获取页面配置数据
   */
  getPageData() {
    return {
      ...pageData,
      ...pageData.pageContext,
    };
  },

  /**
   * @description 获取页面配置数据loading状态
   */
  getPageLoading() {
    return pageLoadingFlag;
  },

  /**
   * @description 获取页面配置数据请求
   */
  async setPageData() {
    pageData = await getPageDatas(this.getRoute().substring(1));
    // 最新服务无数据时，删除该卡片
    let cardLayout = pageData.pageContext?.pageInfoEntity?.cardLayout;
    let match =
      cardLayout &&
      cardLayout.match(/"cardId":"SYS_CARD_LASTESTAPP".*?(cardWid":".*?")/g);
    if (match) {
      const noData = [];
      const promiseArr = [];
      const wids = [];
      try {
        match.forEach((item) => {
          const obj = JSON.parse(`{${item}}`);
          wids.push(obj.cardWid);
          promiseArr.push(
            this.execCardMethod({
              cardId: obj.cardId,
              cardWid: obj.cardWid,
              method: "renderData",
              param: {
                fromMaster: true, //区分和卡片内的方法调用，防止被cancel
              },
            })
          );
        });
        const resArr = await Promise.all(promiseArr);
        resArr.forEach((resp, index) => {
          if (
            resp.errcode == "0" &&
            resp.data?.config?.noDataDisplay == "0" &&
            (!resp.data.appList || !resp.data.appList.length)
          ) {
            noData.push(wids[index]);
          }
        });
        if (noData.length) {
          cardLayout = JSON.parse(cardLayout);
          const resuive = function(data) {
            for (let index = 0; index < data.length; index++) {
              const element = data[index];
              if (element.columns || element.rows) {
                resuive(element.columns || element.rows);
              } else if (
                element.card &&
                noData.includes(element.card.cardWid)
              ) {
                console;
                data.splice(index, 1);
                index--;
              } else if (element.cards) {
                element.cards.cards = element.cards.cards.filter(
                  (card) => !noData.includes(card.cardWid)
                );
                if (!element.cards.cards.length) {
                  data.splice(index, 1);
                  index--;
                }
              }
            }
          };
          resuive(cardLayout);
          pageData.pageContext.pageInfoEntity.cardLayout = JSON.stringify(
            cardLayout
          );
        }
      } catch {
        console.log("处理卡片出错了");
      }
    }
  },
  /**
   * @description 判断当前是否为safari浏览器
   */
  isSafari() {
    let ua = navigator.userAgent.toLowerCase();
    let isChrome = ua.indexOf("chrome") != -1;
    let isSafari = ua.indexOf("safari") != -1;
    if (!isChrome && isSafari) {
      return true;
    }
    return false;
  },
  /**
   * @description 判断当前是否为ie浏览器
   */
  isIE() {
    return navigator.userAgent.toLowerCase().indexOf("trident") > -1
      ? true
      : false;
  },
  /**
   * @description 判断当前是否为firefox浏览器
   */
  isFireFox() {
    return window.navigator.userAgent.match(/firefox/i);
  },
  /**
   * @description 判断当前是否为360浏览器
   */
  is360() {
    var where = "suffixes", value = "dll", name = "description", nameReg = /DLL/;
    var mimeTypes = window.navigator.mimeTypes, i;
    let flag = false
    for (i in mimeTypes) {
      if (mimeTypes[i][where] == value) {
        if (nameReg.test(mimeTypes[i][name])) {
          flag = true
          break;
        }
      }
    }
    return flag;
  },
  /**
   * @description 判断当前是否为Edge浏览器
   */
  isEdge() {
    return window.navigator.userAgent.match(/Edg/i)
  },
  /**
   * @description 判断当前是否为Chrome浏览器
   */
   isChrome() {
    return window.navigator.userAgent.match(/Chrome/i) && !this.is360() && !this.isEdge()
  },
  /**
   * @description 获取侧边栏消息数
   */
  async getSidebarCount(params, callback) {
    const [res] = await getSidebarCount({ params });
    callback && typeof callback === "function" && callback(res);
    return res;
  },
  /**
   * @description 获取站点
   */
  async getUserPermissionRouters(params, callback) {
    const [res] = await getUserPermissionRouters({ params });
    callback && typeof callback === "function" && callback(res);
    return res;
  },
  /**
   * @description 获取模板code
   */
  getTemplateCode() {
    const pageData = this.getPageData();
    return pageData?.pageContext?.templateEntity?.templateId || "";
  },

  /**
   * @description 获取全局配置
   */
  getGlobalConfig() {
    return {
      title: "门户标题（来自全局配置）",
    };
  },

  /**
   * @description 将url参数解析，拼装成json结构返回
   */
  getUrlParam() {
    if (location.href.indexOf("?") === -1) return null; //如果url中没有传参直接返回空
    let query = location.search.substring(1);
    if (location.hash && !location.search) {
      query = location.hash.split("?")[1];
    }
    return query.split("&").reduce((ret, item) => {
      const [key, value] = item.split("=");
      return { ...ret, [key]: decodeURIComponent(value) };
    }, {});
  },
  useAxios() {
    return axios;
  },
  getDownUrl() {
    return api.downloadFileByUrl;
  },
  setI18nList() {
    const pgData = this.getPageData();
    // const programmeId = pgData.pageInfoEntity.programmeId
    const i18nList = _sessionStorage.get(`i18nList`);
    if (i18nList) {
      i18n.mergeLocaleMessage(i18n.locale, i18nList);
      if (pgData.pageInfoEntity.pageCode !== "itemDetail") {
        this.setBroswerTitle(pgData.pageTitle || "");
      }
    } else {
      this.getLanguageList(i18n.locale, (res) => {
        if (res.errcode === "0") {
          _sessionStorage.set(`i18nList`, res.data, 1000*60*30);
          i18n.mergeLocaleMessage(i18n.locale, res.data);
          if (pgData.pageInfoEntity.pageCode !== "itemDetail") {
            this.setBroswerTitle(pgData.pageTitle || "");
          }
        }
      })
    }
  },

  /**
   * @description 更新路由
   * @param {String} route 路由名称
   */
  updateRouter(route) {
    currentRoute = route;
    pageLoadingFlag = false;
    this.setPageData().then(async () => {
      pageLoadingFlag = true;
      let pgData = this.getPageData();
      await this.setI18nList();
      await this.getLoginUser();
      await this.getCurrentRouteSite();
      await this.emit("startPopup");
      await this.emit("page-change");
      await this.pageCollect();
      let pageconfig = pgData.pageContext.pageInfoEntity.pageConfig || "";
      let strUrl = "";
      let arr = pageconfig.split("\n") || [];
      arr.forEach((v) => {
        if (v.indexOf("title.imgUrl") >= 0) {
          strUrl = v.split("title.imgUrl=")[1];
        }
      });
      let $favicon = document.querySelector('link[rel="icon"]');
      if (strUrl !== "") {
        $favicon.href = strUrl;
      } else {
        $favicon.href = "/favicon.ico";
      }
      if (!urlFlag) {
        window.minosStataCollect = new window.MinosStataCollect({
          modelId: "casp-portal",
          modelVersion: this.getPageData().portalVersion,
          modelName: this.getPageData().portalName,
          baseUrl: this.getPageData().stataAddress.split("/minos-stata")[0],
        });
        urlFlag = true;
      }
      let pageLoadId = `page_load_${new Date().getTime()}`,
        pageBrowseId = `page_browse_${new Date().getTime()}`;
      await window.minosStataCollect.init({
        //SDK实例每次加载当前url页面时初始化
        allCardsNumber: (() => {
          const {
            pageContext: {
              pageInfoEntity: { cardLayout },
            },
          } = pageData;
          let cardArr = JSON.parse(cardLayout) || [],
            num = 0;
          deep(cardArr);
          function deep(cardArr) {
            cardArr.forEach((item) => {
              item.columns.forEach((el) => {
                for (let key in el) {
                  if (key === "rows") {
                    deep(el[key]);
                  } else if (key === "card") {
                    num++;
                  } else if (key === "cards") {
                    num++;
                  }
                }
              });
            });
          }
          return num;
        })(),
        userInfo: this.getUserInfo(),
        pageInfo: { ...this.getPageData(), ...{ pageLoadId, pageBrowseId } },
        siteInfo: {
          siteId: currentSite.wid || "",
          siteName: currentSite.siteName || "",
        },
      });
      window.minosStataCollect.loadStart({
        //页面加载开始
        listId: pageLoadId,
        actionType: 3,
        functionType: 0,
        actionParams: {
          pageCode: pageData.pageContext.pageInfoEntity.pageCode,
          pageName: pageData.pageContext.pageInfoEntity.pageName,
        },
        startTime: new Date().getTime(),
      });
      window.minosStataCollect.browseStart({
        //页面浏览开始
        listId: pageBrowseId,
        actionType: 1,
        functionType: 0,
        actionParams: {
          pageCode: pageData.pageContext.pageInfoEntity.pageCode,
          pageName: pageData.pageContext.pageInfoEntity.pageName,
          activeTime: 1000,
        },
        startTime: new Date().getTime(),
      });
    });
  },

  /**
   * @description 获取当前路由
   */
  getRoute() {
    return currentRoute;
  },

  /**
   * @description 修改面包屑名称
   * @param {String} name 修改名称
   * @param {String} key 待修改面包屑的索引，不传默认修改最后一级
   */
  changeBreadcrumbName(name, index) {
    this.emit("update-breadcrumb", name, index);
  },

  /**
   * @description 调用卡片后端方法
   * @param {String} cardId 卡片ID
   * @param {String} cardWid 卡片的运行时ID
   * @param {String} method 卡片方法名
   * @param {Object} param 参数
   * @param {Function} callback 回调方法
   */
  async execCardMethod(param, callback) {
    const req = {
      ...param,
      param: {
        ...param.param,
        lang: sessionStorage.getItem("locale") || "zh_CN",
      },
    };
    const [res] = await execCardMethod(req);
    callback && typeof callback === "function" && callback(res);
    // if (
    //   [
    //     "render",
    //     "renderData",
    //     "getPersonalDataList",
    //     "getChannelNews",
    //     "getDeptList",
    //   ].includes(param.method)
    // ) {
    //   this.emit("change-tabs", {
    //     wid: param.cardWid,
    //     loaded: true,
    //   });
    // }
    return res;
  },

  async getCardConfig(cardId, cardWid, callback) {
    const [res] = await execCardMethod({
      cardId,
      cardWid,
      method: "getConfig",
    });
    callback && typeof callback === "function" && callback(res);
    return res;
  },

  /**
   * @description 调用模板后端方法
   * @param {String} method 卡片方法名
   * @param {Object} param 参数
   * @param {Function} callback 回调方法
   */
  async execTemplateMethod(method, param, callback) {
    const [res] = await execTemplateMethod({
      method,
      param,
    });
    if (callback && typeof callback === "function") {
      callback(res);
    } else {
      return res;
    }
  },

  /**
   * @description 重新渲染角标
   * @param {String} cardId 卡片ID
   * @param {String} cardWid 卡片的运行时ID
   * @param {Object} param 参数
   */
  async renderMark(cardId, cardWid, param) {
    const [res] = await execCardMethod({
      cardId,
      cardWid,
      method: "getMarkNumber",
      param,
      target: null,
    });
    if (res.errcode === "0") {
      this.emit("render-mark", {
        data: res.data,
        cardId,
        cardWid,
      });
    }
  },

  /**
   * @description 移动端当前页打开
   * @param {Sting} url 链接
   */
  openUrl(url) {
    if (this.isMobile()) {
      location.href = url || "";
    } else {
      window.open(url || "about:blank");
    }
  },

  /**
   * @description 链接跳转
   * @param {String} url 跳转链接url
   * @param {Number} openType 0当前页/1新开窗口
   * @param {Number} menuType 0无链接、1内部页面、2第三方链接
   */
  openPage(url, openType, menuType) {
    switch (menuType) {
      case 0:
        return false;
      case 1:
        if (openType) {
          let origin = location.origin;
          let currentRoute = this.getCurrentSite();
          // location.origin IE不存在
          if (!origin) {
            origin = location.protocol + "//" + location.host;
          }
          this.openUrl(
            `${origin}${
              currentRoute?.siteRoute ? "/" + currentRoute?.siteRoute : ""
            }/index.html#${url}`
          );
        } else {
          router.push(url);
        }
        break;
      case 2:
        if (openType) {
          this.openUrl(url || "about:blank");
        } else {
          location.href = url;
          // location.replace(url);
        }
    }
  },

  /**
   * 需先检测服务是否可访问，不可访问时展示异常信息
   * @param {*} serviceWid
   */
  async checkServiceHealth(isMobile, serviceWid) {
    const [res] = await getServiceHealth({
      params: {
        serviceWid: serviceWid,
      },
    });
    const code = res?.data?.[isMobile == 1 ? "mobileHttpCode" : "pcHttpCode"];
    if (/^[4|5]\d*$/.test(code)) {
      return false;
    } else {
      return true;
    }
  },

  /**
   * @description 打开服务
   * @param {Function} callback 回调
   * @param {Boolean} isSkip 是否直接跳转服务
   */
  async openService(serviceItem, callback, isSkip, isCurrentPage) {
    this.serviceCollect(serviceItem);
    let isMobile = this.isMobile();
    let serviceStation = serviceItem.serviceStation; // 0:pc 1:mobile 2:pc+mobile
    let url = "";
    if (serviceStation === 0) {
      url = serviceItem.pcAccessUrl;
    } else if (serviceStation === 1) {
      url = serviceItem.mobileAccessUrl;
    } else {
      url = isMobile ? serviceItem.mobileAccessUrl : serviceItem.pcAccessUrl;
    }
    // 如果是移动端且是pc服务，则打开是否跳转pc弹窗
    if (isMobile && serviceStation === 0 && !isSkip) {
      this.emit("show-pc-service-modal", serviceItem);
      return;
    }
    // 移动端还走之前的逻辑
    if (isMobile) {
      const isValid = await this.checkServiceHealth(
        serviceStation === 0 ? 0 : 1,
        serviceItem.wid
      );
      if (!isValid) {
        this.openPage(
          `/ServiceShow?isMobile=${serviceStation}&wid=${serviceItem.wid}`,
          0,
          1
        );
        return;
      }
      const [res] = await serviceShow({
        params: {
          serviceId: serviceItem.wid,
          isMobile: serviceStation,
        },
      });
      if (res.errcode === "0") {
        this.visitService(serviceItem.wid, "service");
        const grantData = (res.data && res.data.grantData) || [];
        if (grantData.length > 1) {
          this.emit("show-choose-group", serviceItem);
        } else if (grantData.length === 1) {
          this.openUrl(grantData[0] && grantData[0].serviceUrl);
        } else {
          this.openUrl(url);
        }
      }
    }
    // PC端不再请求接口，先打开新窗口，后续操作在新窗口中实现
    else {
      let { href } = router.resolve({
        path: "/ServiceShow",
        query: {
          isMobile: serviceStation,
          wid: serviceItem.wid,
        },
      });
      // 当前页面打开
      if (isCurrentPage) {
        // IE11仅改变hash时，不会重新渲染页面
        if (
          (navigator.userAgent.indexOf("compatible") > -1 &&
            navigator.userAgent.indexOf("MSIE") > -1) ||
          (navigator.userAgent.indexOf("Trident") > -1 &&
            navigator.userAgent.indexOf("rv:11.0") > -1)
        ) {
          href =
            (window.location.origin || "") +
            (window.location.pathname || "") +
            "?_t=" +
            new Date().getTime() +
            href;
        } else {
          href =
            (window.location.origin || "") +
            (window.location.pathname || "") +
            href;
        }
        location.href = href;
      } else {
        href =
          (window.location.origin || "") +
          (window.location.pathname || "") +
          href;
        window.open(href);
      }
    }
  },

  /**
   * @description 打开服务事项，配置无需办理相关指南后，跳转显示关联服务弹窗
   * @param {Object} serviceItem 服务事项详情
   * @param {String} isOnline 是否是在线办理
   */
  openServiceItem(serviceItem, isOnline = "0") {
    const onlineServiceType = serviceItem.onlineServiceType;
    const workGuide = serviceItem.isShow || serviceItem.workGuide;
    const flag = !onlineServiceType && onlineServiceType !== 0; // 如果没有onlineServiceType字段走有权限逻辑，兼容定开
    this.serviceItemCollect(serviceItem);
    // isShow: 1跳转详情 0跳转关联服务页面
    if (workGuide && isOnline === "0") {
      this.goToServiceItemDetail(serviceItem);
    } else {
      if (serviceItem.isEnabled === 0) {
        this.showMessage({
          type: "warning",
          message: this.getLanguageValue(
            "public",
            "stopService",
            "此服务事项已经停止使用，如有问题请联系管理员"
          ),
        });
      } else {
        if (onlineServiceType === 2 || flag) {
          document.activeElement.blur();
          const lang = sessionStorage.getItem('locale') || 'zh_CN'
          let href = `/simJump?id=${serviceItem.wid}&name=${serviceItem.name}&isOnline=${isOnline}&langCountry=${lang}`
          if (this.isMobile()) {
            this.emit("show-service-item-modal", serviceItem);
          } else {
            href = (window.location.origin || "") + href;
            window.open(href);
          }
        } else {
          this.showMessage({
            type: "warning",
            message: this.getLanguageValue(
              "public",
              "noPermission",
              "暂无使用权限，请联系管理员"
            ),
          });
        }
      }
    }
  },

  /**
   * 跳转事项详情
   * @param {Object} item 事项参数
   */
  goToServiceItemDetail(item) {
    if (item.isEnabled) {
      // console.log('goToServiceItemDetail', item)
      this.visitService(item.wid, "serviceItem");
      const reg = new RegExp("[%\\/?#&=]", "g");
      let keyword = item.name || "";
      keyword = keyword.replace(reg, (match) => encodeURIComponent(match));
      let url = `/itemDetail?wid=${item.wid}&name=${keyword}`;
      this.openPage(url, 1, 1);
    } else {
      this.showMessage({
        type: "warning",
        message: this.getLanguageValue(
          "public",
          "stopService",
          "此服务事项已经停止使用，如有问题请联系管理员"
        ),
      });
    }
  },

  /**
   * @description 打开在线办理弹窗
   * @param {Object} serviceItem 服务事项详情
   */
  openOnlineDeal(serviceItem, targetEle) {
    if (this.isMobile()) {
      this.emit("show-online-deal", serviceItem, targetEle);
    } else {
      this.openServiceItem(serviceItem, "1");
    }
    // this.visitService(serviceItem.itemWid, "serviceItem");
  },

  /**
   * @description 关闭在线办理弹窗
   */
  closeServiceModal() {
    this.emit("close-service-modal");
  },

  /**
   * @description 服务计数方法
   * @param {String} id 服务事项id
   * @param {String} type 服务service/服务对象serviceItem/一件事OneThing
   */
  async visitService(id, type) {
    const [res] = await execTemplateMethod({
      method: "visitService",
      param: {
        id,
        type,
      },
    });
    if (res.errcode === "0") {
      this.emit("render-recuseapp-card");
    }
  },

  /**
   * @description 修改多语言
   * @param name  设置语言对应的key
   */
  async setLanguage(name) {
    const [res] = await switchUserLanguage({
      params: {
        userLanguage: name,
        pageCode: pageData?.globalPageParam?.pageCode,
      },
    });
    if (res.errcode === "0" && res.data) {
      this.emit("change-language", name);
      sessionStorage.removeItem('staticData');
      location.reload();
    }
  },

  /**
   * @description 获取多语言列表
   */
  async getLanguageList(lang, callback) {
    const [res] = await queryI18nList({
      params: {
        lang,
      },
    });
    callback && typeof callback === "function" && callback(res);
    return res;
  },

  /**
   * @description 设置浏览器标题
   * @param {String} pageTitle 浏览器标题
   */
  setBroswerTitle(pageTitle) {
    document.title = pageTitle;
    if (dd.env.platform !== "notInDingTalk") {
      //进行钉钉登录操作
      dd.ready(() => {
        dd.biz.navigation.setTitle({
          title: pageTitle,
        });
      });
    }
    this.emit("page-title-change", pageTitle);
  },

  /**
   * @description 回到顶部
   */
  handleBackTop() {
    this.emit("handle-back-top");
  },

  /**
   * @description 显示message提示
   * @param {Object|String} option message配置，参考element api
   */
  showMessage(option) {
    this.emit("show-message", {
      ...option,
      offset: 80, // 距离顶部80px
    });
  },
  /**
   *
   */
  isMobile() {
    if (navigator.userAgent.match(/(iPhone|iPod|Android)/i)) {
      return true;
    } else {
      return false;
    }
  },
  isAndroid() {
    return navigator.userAgent.indexOf('Android') > -1
  },
  /**
   *
   */
  isIphone() {
    if (navigator.userAgent.match(/(iPhone|iPod|iPad|mac)/i)) {
      return true;
    } else {
      return false;
    }
  },
  /**
   * @description 收藏服务
   * @param {String} id 服务ID
   * @param {Number} operate 0:取消收藏 1:收藏
   */
  collectService(param) {
    let that = this;
    return new Promise(function(resolve, reject) {
      collectService({
        params: {
          id: param.id, // 服务事项ID
          operate: param.operate, //  0:取消收藏 1:收藏
        },
      })
        .then(([res]) => {
          let data = {
            ...param,
            res,
          };
          // console.log(res)
          if (res.errcode === "0") {
            that.showMessage({
              type: "success",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionSuccessTip",
                    "收藏成功"
                  )
                : that.getLanguageValue(
                    "public",
                    "cancelCollectionSucceeded",
                    "取消收藏成功"
                  ),
            });
            resolve(data);
            if (res.data?.objectList?.length) {
              res.data.objectList.forEach(item => {
                if (item.objectType == 1) {
                  item.widList.forEach(id => {
                    that.emit("collectServiceItem", {
                      id,
                      operate: param.operate,
                      res: {
                        errcode: '0'
                      }
                    });
                  })
                }
              })
            }
          } else {
            that.showMessage({
              type: "error",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionFailure",
                    "收藏失败"
                  )
                : that.getLanguageValue(
                    "public",
                    "failedCancelCollection",
                    "取消收藏失败"
                  ),
            });
            reject(data);
          }
          that.emit("collectApp", data);
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
/**
   * @description 收藏服务
   * @param {String} id 服务ID
   * @param {Number} operate 0:取消收藏 1:收藏
   */
 async batchCollectService(wids, callback) {
  let that = this;
  const [res] = await batchCollectService(wids)
  let data = { wids, res }
  that.emit("batchCollectApp", data);
  callback && typeof callback === 'function' && callback(res)
  // 服务和事项收藏同步
  if (res.data?.addObject?.objectList?.length) {
    res.data.addObject.objectList.forEach(item => {
      if (item.objectType == 1) {
        item.widList.forEach(id => {
          that.emit("collectServiceItem", {
            id,
            operate: 1,
            res: {
              errcode: '0'
            }
          });
        })
      }
    })
  }
  if (res.data?.removeObject?.objectList?.length) {
    res.data.removeObject.objectList.forEach(item => {
      if (item.objectType == 1) {
        item.widList.forEach(id => {
          that.emit("collectServiceItem", {
            id,
            operate: 0,
            res: {
              errcode: '0'
            }
          });
        })
      }
    })
  }
},

  /**
   * @description 收藏服务事项
   * @param {String} id 服务事项ID
   * @param {Number} operate 0:取消收藏 1:收藏
   */
  collectServiceItem(param) {
    let that = this;
    return new Promise(function(resolve, reject) {
      collectServiceItem({
        params: {
          id: param.id, // 服务事项ID
          operate: param.operate, //  0:取消收藏 1:收藏
        },
      })
        .then(([res]) => {
          let data = {
            ...param,
            res,
          };
          if (res.errcode === "0") {
            that.showMessage({
              type: "success",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionSuccessTip",
                    "收藏成功"
                  )
                : that.getLanguageValue(
                    "public",
                    "cancelCollectionSucceeded",
                    "取消收藏成功"
                  ),
            });
            resolve(data);
            if (res.data?.objectList?.length) {
              res.data.objectList.forEach(item => {
                if (item.objectType == 0) {
                  item.widList.forEach(id => {
                    that.emit("collectApp", {
                      id,
                      operate: param.operate,
                      res: {
                        errcode: '0'
                      }
                    });
                  })
                }
              })
            }
          } else {
            that.showMessage({
              type: "error",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionFailure",
                    "收藏失败"
                  )
                : that.getLanguageValue(
                    "public",
                    "failedCancelCollection",
                    "取消收藏失败"
                  ),
            });
            reject(data);
          }
          that.emit("collectServiceItem", {
            ...param,
            res,
          });
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
  /**
   * @description 收藏服务事项
   * @param {String} id 服务事项ID
   * @param {Number} operate 0:取消收藏 1:收藏
   */
  collectOneThing(param) {
    let that = this;
    return new Promise(function(resolve, reject) {
      collectOneThing({
        params: {
          id: param.id, // 服务事项ID
          operate: param.operate, //  0:取消收藏 1:收藏
        },
      })
        .then(([res]) => {
          let data = {
            ...param,
            res,
          };
          if (res.errcode === "0") {
            that.showMessage({
              type: "success",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionSuccessTip",
                    "收藏成功"
                  )
                : that.getLanguageValue(
                    "public",
                    "cancelCollectionSucceeded",
                    "取消收藏成功"
                  ),
            });
            resolve(data);
          } else {
            that.showMessage({
              type: "error",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionFailure",
                    "收藏失败"
                  )
                : that.getLanguageValue(
                    "public",
                    "failedCancelCollection",
                    "取消收藏失败"
                  ),
            });
            reject(data);
          }
          that.emit("collectOneThing", {
            ...param,
            res,
          });
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
  /**
   * @description 收藏消息
   * @param {String} id 消息ID
   * @param {Number} operate 0:取消收藏 1:收藏
   */
   collectMessage(param) {
    let that = this;
    return new Promise(function(resolve, reject) {
      collectMessage({
        params: {
          id: param.id, // 服务事项ID
          operate: param.operate, //  0:取消收藏 1:收藏
        },
      })
        .then(([res]) => {
          let data = {
            ...param,
            res,
          };
          if (res.errcode === "0") {
            that.showMessage({
              type: "success",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionSuccessTip",
                    "收藏成功"
                  )
                : that.getLanguageValue(
                    "public",
                    "cancelCollectionSucceeded",
                    "取消收藏成功"
                  ),
            });
            resolve(data);
          } else {
            that.showMessage({
              type: "error",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionFailure",
                    "收藏失败"
                  )
                : that.getLanguageValue(
                    "public",
                    "failedCancelCollection",
                    "取消收藏失败"
                  ),
            });
            reject(data);
          }
          that.emit("collectMessage", {
            ...param,
            res,
          });
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
  /**
   * @description 收藏新闻
   * @param {String} id 新闻ID
   * @param {Number} operate 0:取消收藏 1:收藏
   */
   collectNews(param) {
    let that = this;
    return new Promise(function(resolve, reject) {
      collectNews({
        params: {
          id: param.id, // 新闻ID
          operate: param.operate, //  0:取消收藏 1:收藏
        },
      })
        .then(([res]) => {
          let data = {
            ...param,
            res,
          };
          if (res.errcode === "0") {
            that.showMessage({
              type: "success",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionSuccessTip",
                    "收藏成功"
                  )
                : that.getLanguageValue(
                    "public",
                    "cancelCollectionSucceeded",
                    "取消收藏成功"
                  ),
            });
            resolve(data);
          } else {
            that.showMessage({
              type: "error",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionFailure",
                    "收藏失败"
                  )
                : that.getLanguageValue(
                    "public",
                    "failedCancelCollection",
                    "取消收藏失败"
                  ),
            });
            reject(data);
          }
          that.emit("collectNews", {
            ...param,
            res,
          });
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
  /**
   * @description 收藏任务
   * @param {String} id 任务ID
   * @param {Number} operate 0:取消收藏 1:收藏
   */
  collectTask(param) {
    let that = this;
    return new Promise(function(resolve, reject) {
      collectTask({
        params: {
          id: param.id, // 任务ID
          operate: param.operate, //  0:取消收藏 1:收藏
        },
      })
        .then(([res]) => {
          let data = {
            ...param,
            res,
          };
          if (res.errcode === "0") {
            that.showMessage({
              type: "success",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionSuccessTip",
                    "收藏成功"
                  )
                : that.getLanguageValue(
                    "public",
                    "cancelCollectionSucceeded",
                    "取消收藏成功"
                  ),
            });
            resolve(data);
          } else {
            that.showMessage({
              type: "error",
              message: Number(param.operate)
                ? that.getLanguageValue(
                    "public",
                    "collectionFailure",
                    "收藏失败"
                  )
                : that.getLanguageValue(
                    "public",
                    "failedCancelCollection",
                    "取消收藏失败"
                  ),
            });
            reject(data);
          }
          that.emit("collectTask", {
            ...param,
            res,
          });
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
  /**
   * @description 获取当前站点
   */
  async getCurrentRouteSite() {
    const locale = sessionStorage.getItem("locale") || "zh_CN";
    const routeSite = _sessionStorage.get('routeSite')
    if (routeSite && routeSite.length) {
      siteList = routeSite
    } else {
      const [res] = await getUserPermissionRouters({
        params: {
          langCountry: locale,
        },
      });
      if (res.errcode === "0") {
        siteList = res.data || [];
        _sessionStorage.set('routeSite', siteList, 1000*60*30)
      }
    }
    const pathname = location.pathname;
    const siteRoute =
      pathname === "/" ? "" : pathname.split("/index.html")[0];
    currentSite = siteList.find(
      (el) => siteRoute === `/${el.siteRoute}` || (!siteRoute && el.isMaster)
    );
  },

  async getLoginUser() {
    const userData = _sessionStorage.get('loginUser')
    if (userData) {
      loginUser = userData
    } else {
      const [res] = await getLoginUser();
      if (res.errcode === "0") {
        loginUser = res.data;
        this.emit("update-login", loginUser);
        _sessionStorage.set('loginUser', loginUser, 1000*60*30)
      }
    }
    let defaultLanKey = navigator.language.replace("-", "_");
    defaultLanKey = defaultLanKey.includes("zh") ? "zh_CN" : defaultLanKey.split("_")[0]; // 所有中文都处理成简体中文
    const languageKey = defaultLanKey || "zh_CN";
    const preferredLanguage = loginUser?.preferredLanguage;
    if (preferredLanguage) {
      sessionStorage.setItem("locale", preferredLanguage || defaultLanKey);
    } else {
      if (sessionStorage.getItem("locale") !== defaultLanKey) {
        sessionStorage.removeItem('staticData');
      }
      sessionStorage.setItem("locale", defaultLanKey);
    }
    const locale = sessionStorage.getItem("locale") || languageKey;
    await this.getStaticData({
      languageKey: locale,
      modelKey: "casp-portal",
    });
    if (loginUser) {
      const userInfo = {
        userAccount: loginUser?.userAccount,
        userName: loginUser?.userName,
      };
      if (window.tracker) {
        setTimeout(() => {
          try {
            window.tracker.identify(userInfo.userAccount, userInfo);
          } catch (e) {
            console.log(e);
          }
        }, 100);
      }
    }
  },

  /**
   * @description 获取用户信息
   */
  getUserInfo() {
    return loginUser;
  },

  /**
   * @description 获取当前站点
   */
  getCurrentSite() {
    return currentSite || {};
  },
  getSiteList() {
    return siteList;
  },

  /**
   * @description 切换站点
   * @param {String} siteRoute 站点路由
   */
  changeSite(siteRoute) {
    let origin = location.origin;
    if (!origin) {
      origin = location.protocol + "//" + location.host;
    }
    const select = siteList.find((item) => item.siteRoute === siteRoute);
    if (select) {
      if (openRandomSite == "1") {
        //开启随机站点
        if (loginUser) {
          saveSiteitching({
            userAccount: loginUser?.userAccount,
            siteWid: select.wid,
          });
        } else {
          sessionStorage.setItem("siteRoute", JSON.stringify(select));
        }
      } else {
        loginUser && saveSiteitching({
          userAccount: loginUser?.userAccount,
          siteWid: select.wid,
        });
      }
    }
    this.openPage(`${origin}/${siteRoute}/index.html`, 0, 2);
  },

  async login(page) {
    const [res] = await login(page);
    location.href = res.data;
  },

  /**
   * @description 退出登录
   */
  async logout(page) {
    const [res] = await logout(page);
    // &被编码成&amp%3B，故需先uri解码再html解码
    let ele = document.createElement("div");
    ele.innerHTML = decodeURIComponent(res.data);
    sessionStorage.removeItem("locale");
    sessionStorage.removeItem("staticData");
    if (this.isMobile()) {
      location.replace(ele.innerText);
    } else {
      location.href = ele.innerText;
    }
  },

  /**
   * @description 复制
   * @param {String} text 复制的文字
   */
  copyText(text) {
    this.emit("copy-text", text);
  },

  /**
   * @description 返回第三方js库url列表
   */
  getExtraJs() {
    // TODO:
  },

  /**
   * @description 获取底座的location
   */
  getLocation() {
    return window.location;
  },

  /**
   * 设置主题色
   * @param {Object} document 文档对象
   * @param {Object} pageData 页面配置
   */
  setThemeColor(document, pageData) {
    const templateConfig = pageData
      ? pageData.showProgrammeEntity.templateConfig
      : "";
    const configObj = JSON.parse(JSON.parse(templateConfig || "{}"));
    const themeColor = configObj.themeColorSetting || {};
    let primaryStr = "";
    let fontStr = "";
    for (let el in themeColor) {
      const color = themeColor[el];
      const index = el.substring(el.length - 1);
      // 背景主题色
      if (el.includes("portal-primary")) {
        primaryStr += `.${el}{color: ${color}!important}
                .portal-primary-backgroundcolor-lv${index}{background: ${color}!important}
                .portal-primary-color-hover-lv${index}:hover{color: ${color}!important}
                .portal-primary-color-active-lv${index}:active{color: ${color}!important}
                .portal-primary-color-focus-lv${index}:focus{color: ${color}!important}
                .portal-primary-backgroundcolor-hover-lv${index}:hover{background: ${color}!important}
                .portal-primary-border-color-lv${index}{border-color: ${color}!important}
                .portal-primary-border-left-color-lv${index}{border-color: ${color}!important}
                .portal-primary-border-color-active-lv${index}:active{border-color: ${color}!important}
                .portal-primary-border-color-focus-lv${index}:focus{border-color: ${color}!important}
                .portal-primary-border-color-hover-lv${index}:hover{border-color: ${color}!important}
                .portal-primary-border-bottom-color-lv${index}{border-bottom-color: ${color}!important}
                .portal-primary-border-bottom-color-hover-lv${index}:hover{border-bottom-color: ${color}!important}
                .portal-primary-border-top-color-lv${index}{border-top-color: ${color}!important}
                .portal-primary-border-top-color-hover-lv${index}:hover{border-top-color: ${color}!important}
                .portal-primary-after-backgroundcolor-lv${index}:after{background: ${color}!important}
                .portal-primary-before-backgroundcolor-lv${index}:before{background: ${color}!important}
                .portal-primary-border-bottom-color-after-lv${index}:after{border-bottom-color: ${color}!important}`;
      } else {
        // 字体主题色
        fontStr += `.${el}{color: ${color}!important}.portal-font-backgroundcolor-lv${index}{background: ${color}!important}.portal-font-backgroundcolor-hover-lv${index}:hover{background: ${color}!important}.portal-font-color-hover-lv${index}:hover{color: ${color}!important}.portal-font-after-backgroundcolor-lv${index}:after{background-color: ${color}!important}`;
      }
    }
    addCSS(document, `${primaryStr}${fontStr}`);
  },
  /**
   * 隐藏卡片容器
   * @param {Sting} cardId 卡片Id
   */
  hideCardContainer(cardId) {
    console.log(cardId);
    this.emit("hide-card-container", cardId);
  },
  /**
   * 显示卡片容器
   * @param {Sting} cardId 卡片Id
   */
  showCardContainer(cardId) {
    this.emit("show-card-container", cardId);
  },
  /**
   * url参数编码
   */
  urlParamsEncode(str = "") {
    const reg = new RegExp("[%\\/?#&=]", "g");
    return str.replace(reg, (match) => encodeURIComponent(match));
  },
  /**
   * 获取某个元素到页面顶部距离
   * @param {Object} el html标签
   */
  getElementTop(el) {
    let actualTop = el.offsetTop;
    let current = el.offsetParent;
    while (current !== null) {
      actualTop += current.offsetTop;
      current = current.offsetParent;
    }
    return actualTop;
  },
  htmlEncodeToStr(html) {
    let temp = document.createElement("div");
    temp.textContent != null
      ? (temp.textContent = html)
      : (temp.innerText = html);
    let htmlStr = temp.innerHTML;
    return htmlStr;
  },
  /**
   *
   * @param {*} html 原始字符串
   * @param {*} keyWord 需要高亮的字符
   */
  strHighlighted(html, keyWord = "", keyClass = "portal-primary-color-lv1") {
    let temp = document.createElement("div");
    let temp2 = document.createElement("div");

    let newStr = "";
    temp.textContent != null
      ? (temp.textContent = html)
      : (temp.innerText = html);
    let htmlStr = temp.innerHTML;
    if (keyWord || (Array.isArray(keyWord) && keyWord.length)) {
      temp2.textContent != null
        ? (temp2.textContent = keyWord)
        : (temp2.innerText = keyWord);

      let keyWordStr = temp2.innerText.replace(
        /[-\\/\\^$*+?.()|[\]{}]/g,
        "\\$&"
        );
      if (Array.isArray(keyWord)) {
        let a = keyWord.sort((a, b) => b.length - a.length).map(el => el.replace(
          /[-\\/\\^$*+?.()|[\]{}]/g,
          "\\$&"
          ));
        keyWordStr = a.join("|");
      }
      let re = new RegExp(keyWordStr, "gi");
      newStr = htmlStr.replace(
        re,
        (match) => `<span class="${keyClass}">${match}</span>`
      );
    } else {
      newStr = htmlStr;
    }
    temp = null;
    temp2 = null;
    return newStr;
  },
  /**
   * 设置本地样式，用于覆盖卡片模板中的某些样式，在管控台中配置
   * @param {Object} document 文档对象
   * @param {Object} pageData 页面配置
   */
  async setLocalStyle(document, wid) {
    const [res] = await getProgrammeLocalStyle({
      wid,
      localStyle: "",
      isQuery: true,
    });
    if (res.errcode === "0" && res.data) {
      addCSS(document, res.data);
    }
  },
  async getStaticData(params, callback) {
    const langList = _sessionStorage.get('staticData');
    if (langList) {
      staticData = langList || [];
    } else {
      const [res] = await getStaticData(params);
      if (res.errcode === "0" && res.data) {
        staticData = res.data || [];
        _sessionStorage.set('staticData', staticData, 1000*60*30);
      }
    }
    callback && typeof callback === "function" && callback(staticData);
  },
  /**
   *
   * @param {*} functionName 模块Id
   * @param {*} dataKey 多语言Key
   * @param {*} defualtValue 多语言默认值
   * @param {*} obj 需要替换的变量对象 如：{ total: xxx, start: xxx, end: xxx }
   */
  getLanguageValue(functionName, dataKey, defualtValue, obj) {
    const temp = staticData.filter(
      (el) => el.functionKey === functionName && el.dataKey === dataKey
    )[0];
    let text = (temp && temp.dataValue) || defualtValue;
    if (obj && Object.keys(obj).length) {
      return text.replace(/\{(.+?)\}/g, (matched, ...arg) => {
        return obj[arg[0]];
      });
    }
    return text;
  },
  // 服务访问量数据
  serviceVisitData(e) {
    try {
      window.tracker &&
        window.tracker.load(
          "",
          "amp",
          "应用管理平台",
          this.isMobile() ? "h5" : "pc"
        );
      this.serviceCollect(e);
    } catch (e) {
      console.log(e);
    }
  },
  // 门户页面访问数据
  pageCollect() {
    if (window.tracker) {
      try {
        // load(schoolId, appId, desc, clientType)
        window.tracker.load(
          "",
          "amp",
          "应用管理平台",
          this.isMobile() ? "h5" : "pc"
        );
        window.tracker.track("访问应用");
      } catch (e) {
        console.log(e);
      }
    }
  },
  // 服务访问数据
  serviceCollect(e) {
    try {
      const n = {
        appId: e.serviceId || e.wid || e.serviceWid,
        appName: e.name || e.serviceName,
      };
      window.tracker && window.tracker.track("访问服务", n);
      window.minosStataCollect?.collect({
        actionType: 2,
        functionType: 2,
        startTime: new Date().getTime(),
        actionParams: {
          serviceId: e.serviceId || e.wid || e.serviceWid,
        },
      });
    } catch (e) {
      console.log(e);
    }
  },
  // 服务事项访问数据
  serviceItemCollect(e) {
    try {
      const n = {
        appId: e.wid,
        appName: e.name,
      };
      window.tracker && window.tracker.track("访问服务事项", n);
      window.minosStataCollect.collect({
        actionType: 2,
        functionType: 3,
        startTime: new Date().getTime(),
        actionParams: {
          itemId: e.wid || e.itemWid,
        },
      });
    } catch (e) {
      console.log(e);
    }
  },
  /**
   *
   * @param {String} placeStr placeholder内容
   * @param {Number} inputWidth placeholder最大宽度
   */
  placeholderEllipsis(placeStr, inputWidth) {
    if (!placeStr) {
      return "";
    }
    const dom = document.createElement("span");
    dom.style.visibility = "hidden";
    dom.style.display = "inline-block";
    dom.textContent = placeStr;
    document.body.appendChild(dom);
    let width = dom.clientWidth;
    let offset = placeStr.length;
    let realText = placeStr;
    if (width > inputWidth) {
      while (width > inputWidth) {
        realText = `${placeStr.slice(0, offset)}...`;
        dom.textContent = realText;
        width = dom.clientWidth;
        offset = offset - 1;
      }
    }
    document.body.removeChild(dom);
    return realText || placeStr;
  },
  openSitesModal() {
    this.emit("open-sites-modal");
  },
  /**
   * @description 获取服务总体评价
   */
  async getServiceAppraiseSummary(data, callback) {
    const [res] = await appraiseSummary({
      params: data,
    });
    callback && typeof callback === "function" && callback(res);
    return res;
  },

  /**
   * @description 获取服务评价列表
   */
  async queryServiceAppraiseByPage(data, callback) {
    const [res] = await queryAppraiseByPage({
      params: data,
    });
    callback && typeof callback === "function" && callback(res);
    return res;
  },

  /**
   * @description 提交服务评价
   */
  async saveAppraise(data, callback) {
    const [res] = await saveAppraise(data);
    callback && typeof callback === "function" && callback(res);
    return res;
  },
  /**
   * @description 提交服务评价新
   */
  async saveAppraiseWithPic(data, callback) {
    let param = data
    let config = {}
    let fetchMethod =  null
    if (dd.env.platform !== "notInDingTalk") {
      const files = await filesToBase64(data.uploadLists)
      param = {
        uploadFile: files,
        data: data.data
      }
      fetchMethod = saveAppraiseWithPicBase64
    } else {
      config = {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        transformRequest: [
          (data) => {
            let formData = new FormData();
            data.uploadLists.forEach((item) => {
              formData.append("uploadFile", item);
            });
            formData.append("data", data.data);
            return formData;
          },
        ],
      }
      fetchMethod = saveAppraiseWithPic
    }
    const [res] = await fetchMethod(param, config);
    callback && typeof callback === "function" && callback(res);
    return res;
  },
  /**
   * @description 提交服务评价新（事项）
   */
  async saveAppraiseWithPicByItem(data, callback) {
    let param = data
    let config = {}
    let fetchMethod =  null
    if (dd.env.platform !== "notInDingTalk") {
      const files = await filesToBase64(data.uploadLists)
      param = {
        uploadFile: files,
        data: data.data
      }
      fetchMethod = saveAppraiseWithPicByItemBase64
    } else {
      config = {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        transformRequest: [
          (data) => {
            let formData = new FormData();
            data.uploadLists.forEach((item) => {
              formData.append("uploadFile", item);
            });
            formData.append("data", data.data);
            return formData;
          },
        ],
      }
      fetchMethod = saveAppraiseWithPicByItem
    }
    const [res] = await fetchMethod(param, config);
    callback && typeof callback === "function" && callback(res);
    return res;
  },
  /**
   * @description 提交服务评价新（一件事）
   */
  async saveAppraiseWithPicOfOneThing(data, callback) {
    let param = data
    let config = {}
    let fetchMethod =  null
    if (dd.env.platform !== "notInDingTalk") {
      const files = await filesToBase64(data.uploadLists)
      param = {
        uploadFile: files,
        data: data.data
      }
      fetchMethod = saveAppraiseWithPicOfOneThingBase64
    } else {
      config = {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        transformRequest: [
          (data) => {
            let formData = new FormData();
            data.uploadLists.forEach((item) => {
              formData.append("uploadFile", item);
            });
            formData.append("data", data.data);
            return formData;
          },
        ],
      }
      fetchMethod = saveAppraiseWithPicOfOneThing
    }
    const [res] = await fetchMethod(param, config);
    callback && typeof callback === "function" && callback(res);
    return res;
  },
  /**
   * @description 提交意见反馈
   */
  async submitFeedbackWithPic(data, callback) {
    let param = data
    let config = {}
    let fetchMethod =  null
    if (dd.env.platform !== "notInDingTalk") {
      const files = await filesToBase64(data.uploadFile)
      param = {
        uploadFile: files,
        data: data.data
      }
      fetchMethod = submitFeedbackWithPicBase64
    } else {
      config = {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        transformRequest: [
          (data) => {
            let formData = new FormData();
            data.uploadFile.forEach((item) => {
              formData.append("uploadFile", item);
            });
            formData.append("data", data.data);
            return formData;
          },
        ],
      }
      fetchMethod = submitFeedbackWithPic
    }
    const [res] = await fetchMethod(param, config);
    callback && typeof callback === "function" && callback(res);
    return res;
  },
  /**
   * @description 获取服务评价列表
   */
  async queryAppraiseByPageNew(data, callback) {
    const [res] = await queryAppraiseByPageNew({
      params: data,
    });
    callback && typeof callback === "function" && callback(res);
    return res;
  },
  /**
   * @description 通过机构查询用户
   */
  async queryOrgUsers(data, callback) {
    const [res] = await queryOrgUsers(data);
    callback && typeof callback === "function" && callback(res);
    return res;
  },
  /**
   * @description 搜索用户
   */
  async searchUserByKeywordAndOrgwid(data, callback) {
    const [res] = await searchUserByKeywordAndOrgwid(data);
    callback && typeof callback === "function" && callback(res);
    return res;
  },

  /**
   * @description 加载字体图标
   */
  loadIconfont(document) {
    const pageData = this.getPageData();
    const domain = pageData?.portalDomain;
    const url = `${domain}/iconfont/iconfont.css`;
    addCssByLink(document, url);
  },
  loadAsyncJs,
  addCSS,
  /**
   * 截取字符串
   * str:原始字符串
   * len：截取长度
   * symbol：缩略符号默认‘...’
   * 返回值{newStr:已经截取的字符串,isSymbol:是否截取了}
   */
  cutStr,
  getStrLen, //获取字串符长度，英文按一个长度，中文2个长度，emoji 3个长度
  getPlaceholderVal,
  getMessageCount,
  getSearchHisVal,
  getScene,
  // event
  on(...args) {
    eventBus.$on(...args);
  },
  off(...args) {
    eventBus.$off(...args);
  },
  emit(...args) {
    // console.log("event emit", ...args);
    eventBus.$emit(...args);
  },
  isWxMiniProgram() {
    //是否是微信小程序
    let miniprogram = false;
    wx.miniProgram.getEnv(function(res) {
      miniprogram = res.miniprogram;
    });
    return miniprogram;
  },
  wxMiniProgramOpenMap(l) {
    //微信小程序打开地图
    wx.miniProgram.navigateTo({
      url: `/pages/map/map?latitude=${l.latitude}&longitude=${l.longitude}&name=${l.siteName}`,
    });
  },
  // 是否是微信小程序或者钉钉小程序
  isMiniProgram() {
    const userAgent = navigator.userAgent;
    return userAgent.indexOf("AlipayClient") > -1 ||
      (/miniProgram/i.test(userAgent) && /micromessenger/i.test(userAgent))
      ? true
      : false;
  },
  // 安卓微信小程序下载文件
  wxMiniProgramDownload(file, name = "") {
    let type = name
      .substring(name.lastIndexOf(".") + 1)
      .toLowerCase()
      .trim();
    if (type === "zip" || type === "rar") {
      this.showMessage({
        type: "warning",
        message: this.getLanguageValue(
          "public",
          "downloadErrorTip",
          "当前浏览器暂不支持下载zip格式"
        ),
      });
      return;
    }
    /* eslint-disable */
    wx.miniProgram.navigateTo({
      url: `/pages/downloadFile/index?fileUrl=${encodeURIComponent(file)}&fileType=${type}`,
    });
    /* eslint-enable */
  },
};
