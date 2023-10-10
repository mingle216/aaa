import Vue from "vue";
import router from "../router";
import * as dd from "dingtalk-jsapi";
import {
  execCardMethod,
  execTemplateMethod,
  login,
  logout,
  serviceShow,
  queryI18nList,
  collectService,
  collectServiceItem,
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
  // getServiceAccessEnable,
  // getServiceAccessCheck,
} from "../api/service";
import { getPageDatas } from "./mock";
import { addCSS, loadAsyncJs, addCssByLink } from "../libs/tools";
import { cutStr, getStrLen } from "../libs/strUtils";
import ErrorImg from "../assets/images/icon-err.png";

const eventBus = new Vue();
let currentRoute = "";
let pageData = null;
let pageLoadingFlag = false;
let staticData = [];
let loginUser = null;
let urlFlag = false;
let currentSite = "";
let siteList = [];

export default {
  ErrorImg,
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

  /**
   * @description 更新路由
   * @param {String} route 路由名称
   */
  updateRouter(route) {
    currentRoute = route;
    pageLoadingFlag = false;
    this.setPageData().then(async () => {
      pageLoadingFlag = true;
      await this.getLoginUser();
      await this.getCurrentRouteSite();
      await this.emit("startPopup");
      await this.emit("page-change");
      await this.pageCollect();
      let pgData = this.getPageData();
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
   * 先查询是否开启服务url查询功能，1为开启；开启后需先检测服务的url是否可访问，不可访问时展示异常信息
   * @param {*} url
   */
  async checkServiceUrl() {
    return true;
    // if (!url) {
    //   return true;
    // }
    // let serviceAccessEnable = sessionStorage.getItem("serviceAccessEnable");
    // if (!serviceAccessEnable) {
    //   const [res] = await getServiceAccessEnable();
    //   if (res?.errcode === "0") {
    //     serviceAccessEnable = res.data;
    //     sessionStorage.setItem("serviceAccessEnable", res.data);
    //   }
    // }
    // if (serviceAccessEnable === "1") {
    //   const [res1] = await getServiceAccessCheck({
    //     url,
    //   });
    //   if (res1?.errcode === "0" && !res1.data) {
    //     return false;
    //   } else {
    //     return true;
    //   }
    // } else {
    //   return true;
    // }
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
      const isValid = await this.checkServiceUrl(url);
      if (isValid) {
        this.emit("show-pc-service-modal", serviceItem);
      } else {
        this.openPage(
          `/ServiceShow?isMobile=${serviceStation}&wid=${
            serviceItem.wid
          }&url=${encodeURIComponent(url)}`,
          0,
          1
        );
      }
      return;
    }
    // 移动端还走之前的逻辑
    if (isMobile) {
      const isValid = await this.checkServiceUrl(url);
      if (!isValid) {
        this.openPage(
          `/ServiceShow?isMobile=${serviceStation}&wid=${
            serviceItem.wid
          }&url=${encodeURIComponent(url)}`,
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
          url: encodeURIComponent(url),
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
          let { href } = router.resolve({
            path: "/chooseService",
            query: {
              wid: serviceItem.wid,
              name: serviceItem.name,
              isOnline,
            },
          });
          if (this.isMobile()) {
            this.emit("show-service-item-modal", serviceItem);
          } else {
            href =
              (window.location.origin || "") +
              (window.location.pathname || "") +
              href;
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
      let keyword = item.name;
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
   * @param {String} type 服务service/服务对象serviceItem
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
   * @description 获取当前站点
   */
  async getCurrentRouteSite() {
    const locale = sessionStorage.getItem("locale") || "zh_CN";
    const [res] = await getUserPermissionRouters({
      params: {
        langCountry: locale,
      },
    });
    if (res.errcode === "0") {
      siteList = res.data || [];
      const pathname = location.pathname;
      const siteRoute =
        pathname === "/" ? "" : pathname.split("/index.html")[0];
      // console.log(pathname, siteRoute)
      currentSite = siteList.find(
        (el) => siteRoute === `/${el.siteRoute}` || (!siteRoute && el.isMaster)
      );
    }
  },

  async getLoginUser() {
    const [res] = await getLoginUser();
    if (res.errcode === "0") {
      loginUser = res.data;
      // console.log(loginUser)
      this.emit("update-login", loginUser);
    }
    let defaultLanKey = navigator.language.replace("-", "_");
    defaultLanKey = defaultLanKey.includes("zh") ? "zh_CN" : defaultLanKey; // 所有中文都处理成简体中文
    const languageKey = defaultLanKey;
    const preferredLanguage = loginUser?.preferredLanguage;
    console.log("preferredLanguage", preferredLanguage);
    if (preferredLanguage) {
      // console.log('preferredLanguage',preferredLanguage)
      sessionStorage.setItem("locale", preferredLanguage || defaultLanKey);
    } else {
      sessionStorage.setItem("locale", defaultLanKey);
    }
    console.log("browserLan", languageKey);
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
      saveSiteitching({
        userAccount: loginUser?.userAccount,
        siteWid: select.wid,
      });
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
        fontStr += `.${el}{color: ${color}!important}.portal-font-backgroundcolor-hover-lv${index}:hover{background: ${color}!important}.portal-font-color-hover-lv${index}:hover{color: ${color}!important}`;
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

      let keyWordStr = temp2.innerHTML.replace(
        /[-\\/\\^$*+?.()|[\]{}]/g,
        "\\$&"
      );
      if (Array.isArray(keyWord)) {
        let a = keyWord.sort((a, b) => b.length - a.length);
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
    const [res] = await getStaticData(params);
    if (res.errcode === "0" && res.data) {
      staticData = res.data || [];
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
};
