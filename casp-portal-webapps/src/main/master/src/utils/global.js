import Vue from 'vue';
import router from '../router';
import * as dd from 'dingtalk-jsapi'
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
    getSidebarCount
} from '../api/service';
import { getPageDatas } from './mock';
import { addCSS, loadAsyncJs } from '../libs/tools';
import { cutStr, getStrLen } from '../libs/strUtils';
import ErrorImg from '../assets/images/icon-err.png';

const eventBus = new Vue();
let currentRoute = '';
let pageData = null;
let pageLoadingFlag = false;
let staticData = []
let loginUser = null

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
            return true
        }
        return false

    },
     /**
     * @description 判断当前是否为ie浏览器
     */
    isIE() {
        return navigator.userAgent.toLowerCase().indexOf('trident')>-1?true:false;
    },
    /**
     * @description 判断当前是否为firefox浏览器
     */
     isFireFox() {
        return window.navigator.userAgent.match(/firefox/i)
    },
    /**
     * @description 获取侧边栏消息数
     */
    async getSidebarCount(params, callback) {
        const [res] = await getSidebarCount({ params });
        callback && typeof callback === 'function' && callback(res);
        return res;
    },
    /**
     * @description 获取模板code
     */
    getTemplateCode() {
        return 'cardLayout';
    },

    /**
     * @description 获取全局配置
     */
    getGlobalConfig() {
        return {
            title: '门户标题（来自全局配置）',
        };
    },

    /**
     * @description 将url参数解析，拼装成json结构返回
     */
    getUrlParam() {
        if (location.href.indexOf('?') === -1) return null; //如果url中没有传参直接返回空
        let query = location.search.substring(1);
        if (location.hash && !location.search) {
            query = location.hash.split('?')[1];
        }
        return query.split('&').reduce((ret, item) => {
            const [key, value] = item.split('=');
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
            await this.getLoginUser()
            await this.emit('startPopup');
            await this.emit('page-change');
            await this.pageCollect()
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
        this.emit('update-breadcrumb', name, index);
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
                lang: sessionStorage.getItem('locale') || "zh_CN",
            }
        }
        const [res] = await execCardMethod(req);
        callback && typeof callback === 'function' && callback(res);
        return res;
    },

    async getCardConfig(cardId, cardWid, callback) {
        const [res] = await execCardMethod({cardId, cardWid, method: 'getConfig'});
        callback && typeof callback === 'function' && callback(res);
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
        if (callback && typeof callback === 'function') {
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
            method: 'getMarkNumber',
            param,
            target: null,
        });
        if (res.errcode === '0') {
            this.emit('render-mark', {
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
            location.href = url
        } else {
            window.open(url)
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
                    // location.origin IE不存在
                    if (!origin) {
                        origin =  location.protocol + "//" + location.host;
                    }
                    this.openUrl(`${origin}/#${url}`);
                } else {
                    router.push(url);
                }
                break;
            case 2:
                if (openType) {
                    this.openUrl(url || 'about:blank');
                } else {
                    location.href = url
                    // location.replace(url);
                }
        }
    },

    /**
     * @description 打开服务
     * @param {Function} callback 回调
     * @param {Boolean} isSkip 是否直接跳转服务
     */
    async openService(serviceItem, callback, isSkip) {
        this.serviceCollect(serviceItem)
        // console.log(serviceItem)
        let isMobile = this.isMobile()
        let serviceStation = serviceItem.serviceStation // 0:pc 1:mobile 2:pc+mobile
        // document.activeElement.blur()
        // 如果是移动端且是pc服务，则打开是否跳转pc弹窗
        if (isMobile && serviceStation === 0 && !isSkip) {
            this.emit('show-pc-service-modal', serviceItem);
            return
        }
        let url = ''
        if (serviceStation === 0) {
            url = serviceItem.pcAccessUrl
        } else if (serviceStation === 1) {
            url = serviceItem.mobileAccessUrl
        } else {
            url = isMobile ? serviceItem.mobileAccessUrl : serviceItem.pcAccessUrl
        }
        const [res] = await serviceShow({
            params: {
                serviceId: serviceItem.wid,
                serviceUrl: url
            },
        });
        if (callback && typeof callback === 'function') {
            callback(res)
        }
        // 跳转登录
        // if (res.errcode === '1000') {
            // const page = window.location.hash.replace('#', '');
            // this.login({
            //     params: {
            //         localHref: page,
            //     },
            // });
            // 可见性不需跳转登陆直接跳服务
            // window.open(serviceItem.pcAccessUrl);
        // }
        if (res.errcode === '0') {
            // 如果用户组大于1跳到选择用户组页面
            const grantData = (res.data && res.data.grantData) || [];
            if (grantData.length > 1) {
                if (isMobile) {
                    this.emit('show-choose-group', serviceItem)
                    // router.push({
                    //     path: '/chooseGroup',
                    //     query: {
                    //         isMobile,
                    //         wid: serviceItem.wid,
                    //         // url: serviceItem.pcAccessUrl,
                    //     },
                    // })
                } else {
                    const { href } = router.resolve({
                        path: '/chooseGroup',
                        query: {
                            isMobile,
                            wid: serviceItem.wid,
                            // url: serviceItem.pcAccessUrl,
                        },
                    });
                    if (this.isSafari() || this.isFireFox()) {
                        serviceItem.pcAccessUrl = window.location.origin+window.location.pathname+href
                        this.emit('safari-open-serve', serviceItem)
                    } else {
                        window.open(href);
                    }
                }
            } else if (grantData.length === 1) {
                let serviceUrl = grantData[0] && grantData[0].serviceUrl;
                if ((this.isSafari() || this.isFireFox()) && !isMobile) {
                    serviceItem.pcAccessUrl = serviceUrl
                    this.emit('safari-open-serve', serviceItem)
                } else {
                    this.openUrl(grantData[0] && grantData[0].serviceUrl);
                }
            } else {
                
                if ((this.isSafari() || this.isFireFox()) && !isMobile) {
                    this.emit('safari-open-serve', serviceItem)
                } else {
                    this.openUrl(url);
                }
            }
            this.visitService(serviceItem.wid, 'service');
        }
    },

    /**
     * @description 打开服务事项，配置无需办理相关指南后，跳转显示关联服务弹窗
     * @param {Object} serviceItem 服务事项详情
     */
    openServiceItem(serviceItem) {
        document.activeElement.blur()
        this.emit('show-service-item-modal', serviceItem);
        this.serviceItemCollect(serviceItem)
    },

    /**
     * @description 打开在线办理弹窗
     * @param {Object} serviceItem 服务事项详情
     */
    openOnlineDeal(serviceItem, targetEle) {
        // this.visitService(serviceItem.itemWid, "serviceItem");
        this.emit('show-online-deal', serviceItem, targetEle);
    },

    /**
     * @description 关闭在线办理弹窗
     */
    closeServiceModal() {
        this.emit('close-service-modal');
    },

    /**
     * @description 服务计数方法
     * @param {String} id 服务事项id
     * @param {String} type 服务service/服务对象serviceItem
     */
    async visitService(id, type) {
        const [res] = await execTemplateMethod({
            method: 'visitService',
            param: {
                id,
                type,
            },
        });
        if (res.errcode === '0') {
            this.emit('render-recuseapp-card');
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
                pageCode: pageData?.globalPageParam?.pageCode
            }
        });
        if (res.errcode === '0' && res.data) {
            this.emit('change-language', name);
            location.reload()
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
        callback && typeof callback === 'function' && callback(res);
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
                    title: pageTitle
                })
            });
        }
        this.emit('page-title-change', pageTitle);
    },

    /**
     * @description 回到顶部
     */
    handleBackTop() {
        this.emit('handle-back-top')
    },

    /**
     * @description 显示message提示
     * @param {Object|String} option message配置，参考element api
     */
    showMessage(option) {
        this.emit('show-message', {
            ...option,
            offset: 80, // 距离顶部80px
        });
    },
    /**
     * 
     */
    isMobile() {
        if (navigator.userAgent.match(/(iPhone|iPod|Android)/i)) {
            return true
        } else {
            return false
        }
    },
    /**
     * 
     */
    isIphone() {
        if (navigator.userAgent.match(/(iPhone|iPod|iPad|mac)/i)) {
            return true
        } else {
            return false
        }
    },
    /**
     * @description 收藏服务
     * @param {String} id 服务ID
     * @param {Number} operate 0:取消收藏 1:收藏
     */
     collectService(param) {
         let that=this;
        return  new Promise(function(resolve, reject){
            collectService({
                params: {
                    id: param.id, // 服务事项ID
                    operate: param.operate, //  0:取消收藏 1:收藏
                },
            }).then(([res])=>{
                let data={
                    ...param,
                    res,
                }
                console.log(res)
                if (res.errcode === '0') {
                    that.showMessage({
                        type: 'success',
                        message: Number(param.operate) ?that.getLanguageValue('public', 'collectionSuccessTip', '收藏成功') :that.getLanguageValue('public', 'cancelCollectionSucceeded', '取消收藏成功'),
                    });
                    resolve(data)
                } else {
                    that.showMessage({
                        type: 'error',
                        message: Number(param.operate) ?  that.getLanguageValue('public', 'collectionFailure', '收藏失败') : that.getLanguageValue('public', 'failedCancelCollection', '取消收藏失败') ,
                    });
                    reject(data)
                }
                that.emit('collectApp', data);
            }).catch((err)=>{
                reject(err)
            });
            
        })
       
    },

    /**
     * @description 收藏服务事项
     * @param {String} id 服务事项ID
     * @param {Number} operate 0:取消收藏 1:收藏
     */
     collectServiceItem(param) {
        let that=this;
        return  new Promise(function(resolve, reject){
         collectServiceItem({
            params: {
                id: param.id, // 服务事项ID
                operate: param.operate, //  0:取消收藏 1:收藏
            },
        }).then(([res])=>{
            let data={
                ...param,
                res,
            }
            if (res.errcode === '0') {
                that.showMessage({
                    type: 'success',
                    message: Number(param.operate) ? that.getLanguageValue('public', 'collectionSuccessTip', '收藏成功') :that.getLanguageValue('public', 'cancelCollectionSucceeded', '取消收藏成功'),
                });
                resolve(data)
            } else {
                that.showMessage({
                    type: 'error',
                    message: Number(param.operate) ? that.getLanguageValue('public', 'collectionFailure', '收藏失败') : that.getLanguageValue('public', 'failedCancelCollection', '取消收藏失败') ,
                });
                reject(data)
            }
            that.emit('collectServiceItem', {
                ...param,
                res,
            });
        }).catch(err=>{
            reject(err)
        });
        
    })
    },

    async getLoginUser() {
        const [res] = await getLoginUser()
        if (res.errcode === '0') {
            loginUser = res.data
            this.emit('update-login', loginUser)
        }
        let defaultLanKey=  navigator.language.replace('-','_')
        defaultLanKey = defaultLanKey.includes('zh') ? 'zh_CN' : defaultLanKey  // 所有中文都处理成简体中文
        const languageKey = defaultLanKey
        const preferredLanguage=loginUser?.preferredLanguage;
        console.log('preferredLanguage',preferredLanguage)
        if(preferredLanguage){
            // console.log('preferredLanguage',preferredLanguage)
            sessionStorage.setItem('locale',preferredLanguage||defaultLanKey)
        }
        else  {
            sessionStorage.setItem('locale',defaultLanKey)
        }
        console.log('browserLan', languageKey)
        const locale = sessionStorage.getItem('locale') || languageKey
        await this.getStaticData({
            languageKey: locale,
            modelKey: "casp-portal",
        })
        if (loginUser) {
            const userInfo = {
                userAccount: loginUser?.userAccount,
                userName: loginUser?.userName
            }
            if (window.tracker) {
                setTimeout(() => {
                    try {
                        window.tracker.identify(userInfo.userAccount, userInfo)
                    } catch (e) {
                        console.log(e)
                    }
                }, 100)
            }
        }
    },

    /**
     * @description 获取用户信息
     */
    getUserInfo() {
        return loginUser;
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
        let ele = document.createElement('div');
        ele.innerHTML = decodeURIComponent(res.data);
        sessionStorage.removeItem('locale')
        if(this.isMobile()){
            location.replace(ele.innerText);
        }else
        {
            location.href = ele.innerText;
        }
        
    },

    /**
     * @description 复制
     * @param {String} text 复制的文字
     */
    copyText(text) {
        this.emit('copy-text', text);
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
            : '';
        const configObj = JSON.parse(JSON.parse(templateConfig || '{}'));
        const themeColor = configObj.themeColorSetting || {};
        let primaryStr = '';
        let fontStr = '';
        for (let el in themeColor) {
            const color = themeColor[el]
            const index = el.substring(el.length - 1)
            // 背景主题色
            if (el.includes('portal-primary')) {
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
                .portal-primary-border-bottom-color-after-lv${index}:after{border-bottom-color: ${color}!important}`
                
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
        console.log(cardId)
        this.emit('hide-card-container', cardId)
    },
    /**
     * 显示卡片容器
     * @param {Sting} cardId 卡片Id
     */
    showCardContainer(cardId) {
        this.emit('show-card-container', cardId)
    },
    /**
     * url参数编码
     */
    urlParamsEncode(str=''){
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
    htmlEncodeToStr(html){
        let temp = document.createElement('div');
        temp.textContent != null
            ? (temp.textContent = html)
            : (temp.innerText = html);
        let htmlStr = temp.innerHTML;
        return htmlStr
    },
    /**
     *
     * @param {*} html 原始字符串
     * @param {*} keyWord 需要高亮的字符
     */
    strHighlighted(html, keyWord = '', keyClass = 'portal-primary-color-lv1') {
        let temp = document.createElement('div');
        let temp2 = document.createElement('div');

        let newStr = '';
        temp.textContent != null
            ? (temp.textContent = html)
            : (temp.innerText = html);
        let htmlStr = temp.innerHTML;
        if (keyWord) {
            temp2.textContent != null
                ? (temp2.textContent = keyWord)
                : (temp2.innerText = keyWord);

            let keyWordStr = temp2.innerHTML.replace(/[-\\/\\^$*+?.()|[\]{}]/g, '\\$&');
            // console.log(keyWordStr)
            let re = new RegExp(keyWordStr, 'gi');
            newStr = htmlStr.replace(
                re,
                match => `<span class="${keyClass}">${match}</span>`
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
            localStyle: '',
            isQuery: true
        })
        if (res.errcode === '0' && res.data) {
            addCSS(document, res.data);
        }
    },
    async getStaticData(params) {
        const [res] = await getStaticData(params)
        if (res.errcode === '0' && res.data) {
            staticData = res.data || []
        }
    },
    /**
     * 
     * @param {*} functionName 模块Id
     * @param {*} dataKey 多语言Key
     * @param {*} defualtValue 多语言默认值
     * @param {*} obj 需要替换的变量对象 如：{ total: xxx, start: xxx, end: xxx }
     */
    getLanguageValue(functionName, dataKey, defualtValue, obj) {
        const temp = staticData.filter(el => el.functionKey === functionName && el.dataKey === dataKey)[0]
        let text = temp && temp.dataValue || defualtValue
        if (obj && Object.keys(obj).length) {
            return text.replace(/\{(.+?)\}/g,(matched, ...arg)=>{
                return obj[arg[0]]
            })
        }
        return text
    },
    // 服务访问量数据
    serviceVisitData(e) {
        if (window.tracker) {
            try {
                window.tracker.load('', 'amp', '应用管理平台', this.isMobile() ? 'h5' : 'pc')
                this.serviceCollect(e)
            } catch (e) {
                console.log(e)
            }
        }
    },
    // 门户页面访问数据
    pageCollect() {
        if (window.tracker) {
            try {
                // load(schoolId, appId, desc, clientType)
                window.tracker.load('', 'amp', '应用管理平台', this.isMobile() ? 'h5' : 'pc')
                window.tracker.track('访问应用')
            } catch (e) {
                console.log(e)
            }
        }
    },
    // 服务访问数据
    serviceCollect(e) {
        if (window.tracker) {
            try {
                const n = {
                    appId: e.wid || e.serviceWid,
                    appName: e.name || e.serviceName
                }
                window.tracker.track('访问服务', n)
            } catch (e) {
                console.log(e)
            }
        }
    },
    // 服务事项访问数据
    serviceItemCollect(e) {
        if (window.tracker) {
            try {
                const n = {
                    appId: e.wid,
                    appName: e.name
                }
                window.tracker.track('访问服务事项', n)
            } catch (e) {
                console.log(e)
            }
        }
    },
    /**
     * 
     * @param {String} placeStr placeholder内容
     * @param {Number} inputWidth placeholder最大宽度
     */
    placeholderEllipsis(placeStr, inputWidth) {
        if (!placeStr){
            return '';
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
        return realText || placeStr
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
