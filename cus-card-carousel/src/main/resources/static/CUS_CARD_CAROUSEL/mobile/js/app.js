!function(e,t){"object"==typeof exports&&"object"==typeof module?module.exports=t(require("Vue")):"function"==typeof define&&define.amd?define(["Vue"],t):"object"==typeof exports?exports.__REMOTE_LOADED_APP__=t(require("Vue")):e.__REMOTE_LOADED_APP__=t(e.Vue)}(window,(function(e){return function(e){var t={};function r(n){if(t[n])return t[n].exports;var a=t[n]={i:n,l:!1,exports:{}};return e[n].call(a.exports,a,a.exports,r),a.l=!0,a.exports}return r.m=e,r.c=t,r.d=function(e,t,n){r.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},r.r=function(e){"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},r.t=function(e,t){if(1&t&&(e=r(e)),8&t)return e;if(4&t&&"object"==typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(r.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var a in e)r.d(n,a,function(t){return e[t]}.bind(null,a));return n},r.n=function(e){var t=e&&e.__esModule?function(){return e.default}:function(){return e};return r.d(t,"a",t),t},r.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},r.p="/SYS_CARD_CAROUSEL/mobile/",r(r.s=0)}({0:function(e,t,r){e.exports=r("56d7")},"0da2":function(e,t,r){"use strict";r("efc9")},"24fb":function(e,t,r){"use strict";function n(e,t){var r=e[1]||"",n=e[3];if(!n)return r;if(t&&"function"==typeof btoa){var a=function(e){var t=btoa(unescape(encodeURIComponent(JSON.stringify(e)))),r="sourceMappingURL=data:application/json;charset=utf-8;base64,".concat(t);return"/*# ".concat(r," */")}(n),o=n.sources.map((function(e){return"/*# sourceURL=".concat(n.sourceRoot||"").concat(e," */")}));return[r].concat(o).concat([a]).join("\n")}return[r].join("\n")}e.exports=function(e){var t=[];return t.toString=function(){return this.map((function(t){var r=n(t,e);return t[2]?"@media ".concat(t[2]," {").concat(r,"}"):r})).join("")},t.i=function(e,r,n){"string"==typeof e&&(e=[[null,e,""]]);var a={};if(n)for(var o=0;o<this.length;o++){var i=this[o][0];null!=i&&(a[i]=!0)}for(var c=0;c<e.length;c++){var s=[].concat(e[c]);n&&a[s[0]]||(r&&(s[2]?s[2]="".concat(r," and ").concat(s[2]):s[2]=r),t.push(s))}},t}},2877:function(e,t,r){"use strict";function n(e,t,r,n,a,o,i,c){var s,d="function"==typeof e?e.options:e;if(t&&(d.render=t,d.staticRenderFns=r,d._compiled=!0),n&&(d.functional=!0),o&&(d._scopeId="data-v-"+o),i?(s=function(e){(e=e||this.$vnode&&this.$vnode.ssrContext||this.parent&&this.parent.$vnode&&this.parent.$vnode.ssrContext)||"undefined"==typeof __VUE_SSR_CONTEXT__||(e=__VUE_SSR_CONTEXT__),a&&a.call(this,e),e&&e._registeredComponents&&e._registeredComponents.add(i)},d._ssrRegister=s):a&&(s=c?function(){a.call(this,(d.functional?this.parent:this).$root.$options.shadowRoot)}:a),s)if(d.functional){d._injectStyles=s;var u=d.render;d.render=function(e,t){return s.call(t),u(e,t)}}else{var l=d.beforeCreate;d.beforeCreate=l?[].concat(l,s):[s]}return{exports:e,options:d}}r.d(t,"a",(function(){return n}))},"499e":function(e,t,r){"use strict";function n(e,t){for(var r=[],n={},a=0;a<t.length;a++){var o=t[a],i=o[0],c={id:e+":"+a,css:o[1],media:o[2],sourceMap:o[3]};n[i]?n[i].parts.push(c):r.push(n[i]={id:i,parts:[c]})}return r}r.r(t),r.d(t,"default",(function(){return h}));var a="undefined"!=typeof document;if("undefined"!=typeof DEBUG&&DEBUG&&!a)throw new Error("vue-style-loader cannot be used in a non-browser environment. Use { target: 'node' } in your Webpack config to indicate a server-rendering environment.");var o={},i=a&&(document.head||document.getElementsByTagName("head")[0]),c=null,s=0,d=!1,u=function(){},l=null,f="data-vue-ssr-id",p="undefined"!=typeof navigator&&/msie [6-9]\b/.test(navigator.userAgent.toLowerCase());function h(e,t,r,a){d=r,l=a||{};var i=n(e,t);return g(i),function(t){for(var r=[],a=0;a<i.length;a++){var c=i[a],s=o[c.id];s.refs--,r.push(s)}for(t?g(i=n(e,t)):i=[],a=0;a<r.length;a++)if(0===(s=r[a]).refs){for(var d=0;d<s.parts.length;d++)s.parts[d]();delete o[s.id]}}}function g(e){for(var t=0;t<e.length;t++){var r=e[t],n=o[r.id];if(n){n.refs++;for(var a=0;a<n.parts.length;a++)n.parts[a](r.parts[a]);for(;a<r.parts.length;a++)n.parts.push(b(r.parts[a]));n.parts.length>r.parts.length&&(n.parts.length=r.parts.length)}else{var i=[];for(a=0;a<r.parts.length;a++)i.push(b(r.parts[a]));o[r.id]={id:r.id,refs:1,parts:i}}}}function m(){var e=document.createElement("style");return e.type="text/css",i.appendChild(e),e}function b(e){var t,r,n=document.querySelector("style["+f+'~="'+e.id+'"]');if(n){if(d)return u;n.parentNode.removeChild(n)}if(p){var a=s++;n=c||(c=m()),t=_.bind(null,n,a,!1),r=_.bind(null,n,a,!0)}else n=m(),t=w.bind(null,n),r=function(){n.parentNode.removeChild(n)};return t(e),function(n){if(n){if(n.css===e.css&&n.media===e.media&&n.sourceMap===e.sourceMap)return;t(e=n)}else r()}}var v=function(){var e=[];return function(t,r){return e[t]=r,e.filter(Boolean).join("\n")}}();function _(e,t,r,n){var a=r?"":n.css;if(e.styleSheet)e.styleSheet.cssText=v(t,a);else{var o=document.createTextNode(a),i=e.childNodes;i[t]&&e.removeChild(i[t]),i.length?e.insertBefore(o,i[t]):e.appendChild(o)}}function w(e,t){var r=t.css,n=t.media,a=t.sourceMap;if(n&&e.setAttribute("media",n),l.ssrId&&e.setAttribute(f,t.id),a&&(r+="\n/*# sourceURL="+a.sources[0]+" */",r+="\n/*# sourceMappingURL=data:application/json;base64,"+btoa(unescape(encodeURIComponent(JSON.stringify(a))))+" */"),e.styleSheet)e.styleSheet.cssText=r;else{for(;e.firstChild;)e.removeChild(e.firstChild);e.appendChild(document.createTextNode(r))}}},"4d2b":function(e,t,r){(t=r("24fb")(!1)).push([e.i,".card_carousel[data-v-13ba2f3d]{position:relative;width:100%;height:3.73333rem;padding:.32rem .45333rem}.card_carousel .my_swipe[data-v-13ba2f3d]{width:100%;height:100%;border-radius:.10667rem}.card_carousel .my_swipe .bg_wrap[data-v-13ba2f3d]{width:100%;height:1.76rem;line-height:.58667rem;position:absolute;bottom:0;left:0;opacity:.8;background-image:linear-gradient(180deg,transparent,rgba(0,0,0,.75) 66%,#000)}.card_carousel .my_swipe .title_wrap[data-v-13ba2f3d]{position:absolute;width:100%;padding:0 .4rem;bottom:.42667rem;left:0;color:#fff;font-size:.42667rem;line-height:.58667rem;text-align:left;overflow:hidden;white-space:nowrap;text-overflow:ellipsis}.card_carousel .we-swipe-item[data-v-13ba2f3d]{background-size:cover;background-repeat:no-repeat;background-position:50%}.card_carousel[data-v-13ba2f3d] .we-swipe__indicators{bottom:.21333rem}.card_carousel[data-v-13ba2f3d] .we-swipe__indicators .we-swipe__indicator{background-color:#fff;opacity:.5}.card_carousel[data-v-13ba2f3d] .we-swipe__indicators .we-swipe__indicator--active{opacity:1}.card_carousel[data-v-13ba2f3d] .we-empty__image img{width:4.32rem;height:2.26667rem}.card_carousel[data-v-13ba2f3d] .empty-content{padding-top:0;height:100%}",""]),e.exports=t},"56d7":function(e,t,r){"use strict";r.r(t),r.d(t,"bootstrap",(function(){return p}));var n=r("ed08");function a(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function o(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?a(Object(r),!0).forEach((function(t){i(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):a(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function i(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function c(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function s(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?c(Object(r),!0).forEach((function(t){d(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):c(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function d(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}var u={name:"swipe",props:{index:Number,router:Object},mixins:[{data:function(){var e=this.router,t=e.cardWid,r=e.cardId,n=e.cardName,a=window.shell.getPageData().pageInfoEntity,o=(new Date).getTime();return{hasLoadedTrack:!1,trackParam:{actionType:3,functionType:1,startTime:o,endTime:"",listId:"".concat(r,"_").concat(t,"_").concat(o),actionParams:{pageCode:a.pageCode,pageName:a.pageName,cardWid:t,cardId:r,cardName:n}}}},methods:{handleClickTrack:function(){window.minosStataCollect.collect({actionType:0,functionType:1,startTime:(new Date).getTime(),actionParams:o({},this.trackParam.actionParams)})},loadedEndTrack:function(){this.hasLoadedTrack||(this.hasLoadedTrack=!0,window.minosStataCollect.loadEnd({listId:this.trackParam.listId,endTime:(new Date).getTime()}))}},created:function(){window.minosStataCollect.loadStart(this.trackParam)}}],data:function(){return{loading:!1,dataList:[]}},watch:{loading:function(e){var t=this;e&&setTimeout((function(){t.loading=!1}),5e3)}},computed:{errorImg:function(){return window.shell.ErrorImg}},methods:{getLang:function(e){var t=this,r=(e||[]).find((function(e){return e.langKey===t.$i18n.locale})),n=(e||[]).find((function(e){return"zh_CN"===e.langKey}));return(null==r?void 0:r.langValue)||(null==n?void 0:n.langValue)},handleGoPage:function(e){this.handleClickTrack(),e.url&&window.shell.openUrl(e.url)},getCardData:function(){var e=this;window.shell.execCardMethod({cardId:this.router.cardId,cardWid:this.router.cardWid,method:"renderData",param:{}},(function(t){if(e.loading=!1,e.loadedEndTrack(),"0"===t.errcode){if(t.data.config){var r=JSON.parse(JSON.parse(t.data.config||"[]")||"[]").slideList.datas||[];e.dataList=r.map((function(t){return s(s({},t),{},{title:e.getLang(t.title)})}))}}else window.shell.showMessage({message:"获取轮播图错误",type:"error"})}))}},created:function(){window.shell&&this.getCardData()}},l=(r("0da2"),r("2877")),f=Object(l.a)(u,(function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticClass:"card_carousel"},[e.dataList.length?r("we-swipe",{staticClass:"my_swipe",attrs:{autoplay:3e3,"indicator-color":"white","show-indicators":e.dataList.length>1}},e._l(e.dataList,(function(t){return r("we-swipe-item",{key:t.name,style:{backgroundImage:"url("+t.image+")"},nativeOn:{click:function(r){return e.handleGoPage(t)}}},[t.title?r("div",{staticClass:"bg_wrap"}):e._e(),t.title?r("div",{staticClass:"title_wrap"},[e._v(" "+e._s(t.title)+" ")]):e._e()])})),1):e._e(),e.dataList.length?e._e():r("EmptyCon",{attrs:{tip:e.GetLanguageValue("SYS_CARD_CAROUSEL_h5","nodata","暂无数据")}})],1)}),[],!1,null,"13ba2f3d",null).exports;function p(e,t){return Object(n.a)(f,e,t)}},"8bbf":function(t,r){t.exports=e},ed08:function(e,t,r){"use strict";r.d(t,"a",(function(){return o}));var n=r("8bbf"),a=r.n(n);function o(e,t,r){return new a.a({i18n:r.props.i18n,render:function(t){return t(e,r)}}).$mount(t)}},efc9:function(e,t,r){var n=r("4d2b");n.__esModule&&(n=n.default),"string"==typeof n&&(n=[[e.i,n,""]]),n.locals&&(e.exports=n.locals),(0,r("499e").default)("24263ca2",n,!0,{sourceMap:!1,shadowMode:!1})}})}));
//# sourceMappingURL=/SYS_CARD_CAROUSEL/mobile/js/app.js.map
//# sourceMappingURL=app.js.map