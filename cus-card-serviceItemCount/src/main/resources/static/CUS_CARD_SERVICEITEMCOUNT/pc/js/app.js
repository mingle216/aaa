!function(e,t){"object"==typeof exports&&"object"==typeof module?module.exports=t(require("Vue")):"function"==typeof define&&define.amd?define(["Vue"],t):"object"==typeof exports?exports.__REMOTE_LOADED_APP__=t(require("Vue")):e.__REMOTE_LOADED_APP__=t(e.Vue)}(window,(function(e){return function(e){var t={};function n(i){if(t[i])return t[i].exports;var o=t[i]={i:i,l:!1,exports:{}};return e[i].call(o.exports,o,o.exports,n),o.l=!0,o.exports}return n.m=e,n.c=t,n.d=function(e,t,i){n.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:i})},n.r=function(e){"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},n.t=function(e,t){if(1&t&&(e=n(e)),8&t)return e;if(4&t&&"object"==typeof e&&e&&e.__esModule)return e;var i=Object.create(null);if(n.r(i),Object.defineProperty(i,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var o in e)n.d(i,o,function(t){return e[t]}.bind(null,o));return i},n.n=function(e){var t=e&&e.__esModule?function(){return e.default}:function(){return e};return n.d(t,"a",t),t},n.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},n.p="/CUS_CARD_SERVICEITEMCOUNT/pc/",n(n.s=0)}({0:function(e,t,n){e.exports=n("56d7")},"0b56":function(e,t,n){"use strict";function i(e,t,n,i,o,r,a,s){var c,d="function"==typeof e?e.options:e;if(t&&(d.render=t,d.staticRenderFns=n,d._compiled=!0),i&&(d.functional=!0),r&&(d._scopeId="data-v-"+r),a?(c=function(e){(e=e||this.$vnode&&this.$vnode.ssrContext||this.parent&&this.parent.$vnode&&this.parent.$vnode.ssrContext)||"undefined"==typeof __VUE_SSR_CONTEXT__||(e=__VUE_SSR_CONTEXT__),o&&o.call(this,e),e&&e._registeredComponents&&e._registeredComponents.add(a)},d._ssrRegister=c):o&&(c=s?function(){o.call(this,(d.functional?this.parent:this).$root.$options.shadowRoot)}:o),c)if(d.functional){d._injectStyles=c;var u=d.render;d.render=function(e,t){return c.call(t),u(e,t)}}else{var l=d.beforeCreate;d.beforeCreate=l?[].concat(l,c):[c]}return{exports:e,options:d}}n.d(t,"a",(function(){return i}))},"0fc0":function(e,t,n){(t=n("a1a8")(!1)).push([e.i,'.item_count_container[data-v-5027612e]{background-size:cover;padding:20px 0;display:flex;flex-direction:column;align-items:center}.item_count_wrap[data-v-5027612e]{height:102px;display:flex;align-items:center;justify-content:center;width:1200px}.item_count_wrap+.item_count_wrap[data-v-5027612e]{padding-top:10px;box-sizing:content-box}.item_col:not(:last-child) .count_item[data-v-5027612e]:after{content:"";position:absolute;right:0;top:calc(50% - 17px);width:0;height:34px}',""]),e.exports=t},"2bd0":function(e,t,n){"use strict";n("8c91")},"56d7":function(e,t,n){"use strict";n.r(t),n.d(t,"bootstrap",(function(){return f}));var i=n("ed08"),o={props:["rows","num","name","showNum","index","len","boxWidth","dataId"],computed:{langName(){const e=this.name.find((e=>e.langKey===this.$i18n.locale)),t=this.name.find((e=>"zh_CN"===e.langKey));return`${(null==e?void 0:e.langValue)||(null==t?void 0:t.langValue)} ${isNaN(Number(this.dataId))||null==e||!e.langValue||this.$i18n.locale.includes("zh")?"":this.GetLanguageValue("SYS_CARD_SERVICEITEMCOUNT","serviceItemCount","事项统计")}`},itemStyle(){const e=((this.len%this.showNum==0||2===this.len?this.boxWidth:this.boxWidth-60*(6-this.len))-180*this.len)/(this.len-1)/2;return 0===this.index?{paddingRight:2===this.len?e/2+"px":e+"px",paddingLeft:2===this.len?e/2+"px":0}:this.index===this.len-1?{paddingRight:2===this.len?e/2+"px":0,paddingLeft:2===this.len?e/2+"px":e+"px"}:{padding:`0 ${e}px`}}}},r=o,a=(n("9cc7"),n("0b56")),s=Object(a.a)(r,(function(){var e=this,t=e._self._c;return t("div",{staticClass:"count_item",style:e.itemStyle},[t("span",{staticClass:"item_number portal-font-color-lv1"},[e._v(e._s(e.num||0))]),t("span",{staticClass:"bottom_line portal-primary-backgroundcolor-lv1"}),t("we-tooltip",{attrs:{effect:"dark",content:e.langName,"open-delay":800}},[t("span",{staticClass:"item_name portal-font-color-lv2"},[e._v(e._s(e.langName))])])],1)}),[],!1,null,"20a78350",null),c={data(){const{cardWid:e,cardId:t,cardName:n}=this.router,i=window.shell.getPageData().pageInfoEntity,o=(new Date).getTime();return{hasLoadedTrack:!1,trackParam:{actionType:3,functionType:1,startTime:o,endTime:"",listId:`${t}_${e}_${o}`,actionParams:{pageCode:i.pageCode,pageName:i.pageName,cardWid:e,cardId:t,cardName:n}}}},methods:{handleClickTrack(e){const t={...this.trackParam.actionParams};e&&(t.extraInfo=e),window.minosStataCollect.collect({actionType:0,functionType:1,startTime:(new Date).getTime(),actionParams:t})},loadedEndTrack(){this.hasLoadedTrack||(this.hasLoadedTrack=!0,window.minosStataCollect.loadEnd({listId:this.trackParam.listId,endTime:(new Date).getTime()}))}},created(){window.minosStataCollect.loadStart(this.trackParam)}},d={name:"serviceitemcounts",components:{CountItem:s.exports},props:{index:Number,router:Object},mixins:[c],data:()=>({checked:!1,serviceItemList:[],showNum:6,config:{heightFlag:1,height:0},boxWidth:0,templateCode:window.shell.getTemplateCode()}),mounted(){"SYS_TEMPLATE_WORK"===this.templateCode?this.boxWidth=this.$refs.serviceCount.offsetWidth:this.boxWidth=1152},computed:{rows(){return Math.ceil(this.serviceItemList.length/this.showNum)}},methods:{check(){this.checked=!this.checked,window.shell.emit("check-card",this.checked)},getCardData(){window.shell.execCardMethod({cardId:this.router.cardId,cardWid:this.router.cardWid,method:"render2",param:{}},(e=>{if("0"===e.errcode){this.showNum=e.data.columns||6;const t=e.data.serviceItemList||[];this.serviceItemList=t.map((e=>({...e,count:["onlinePercent","itemScore"].includes(e.dataId)?e.count:Number(e.count).toLocaleString("en")}))),this.$nextTick((()=>{this.boxWidth=this.$refs.serviceCount&&this.$refs.serviceCount.offsetWidth||1152}))}this.loadedEndTrack()}))},handleResize(){this.boxWidth=this.$refs.serviceCount.offsetWidth}},async created(){"SYS_TEMPLATE_WORK"===this.templateCode&&(window.addEventListener("resize",this.handleResize),this.$once("hook:beforeDestroy",(()=>{window.removeEventListener("resize",this.handleResize)}))),await this.getCardData()}},u=d,l=(n("2bd0"),Object(a.a)(u,(function(){var e=this,t=e._self._c;return t("div",{ref:"serviceCount",staticClass:"service_item_count"},[e.serviceItemList.length?t("div",{staticClass:"item_count_container"},e._l(e.rows,(function(n){return t("div",{key:n,staticClass:"item_count_wrap"},e._l(e.serviceItemList.slice((n-1)*e.showNum,e.showNum*n),(function(i,o){return t("div",{key:o,staticClass:"item_col"},[t("count-item",{attrs:{rows:e.rows,num:i.count,name:i.dataName,showNum:e.showNum,index:o,len:e.serviceItemList.slice((n-1)*e.showNum,e.showNum*n).length,boxWidth:e.boxWidth,dataId:i.dataId}})],1)})),0)})),0):t("empty-con",{attrs:{tip:e.GetLanguageValue("SYS_CARD_SERVICEITEMCOUNT","nodata","暂无数据")}})],1)}),[],!1,null,"5027612e",null)),p=l.exports;function f(e,t){return Object(i.a)(p,e,t)}},5925:function(e,t,n){"use strict";function i(e,t){for(var n=[],i={},o=0;o<t.length;o++){var r=t[o],a=r[0],s={id:e+":"+o,css:r[1],media:r[2],sourceMap:r[3]};i[a]?i[a].parts.push(s):n.push(i[a]={id:a,parts:[s]})}return n}n.r(t),n.d(t,"default",(function(){return h}));var o="undefined"!=typeof document;if("undefined"!=typeof DEBUG&&DEBUG&&!o)throw new Error("vue-style-loader cannot be used in a non-browser environment. Use { target: 'node' } in your Webpack config to indicate a server-rendering environment.");var r={},a=o&&(document.head||document.getElementsByTagName("head")[0]),s=null,c=0,d=!1,u=function(){},l=null,p="data-vue-ssr-id",f="undefined"!=typeof navigator&&/msie [6-9]\b/.test(navigator.userAgent.toLowerCase());function h(e,t,n,o){d=n,l=o||{};var a=i(e,t);return m(a),function(t){for(var n=[],o=0;o<a.length;o++){var s=a[o],c=r[s.id];c.refs--,n.push(c)}for(t?m(a=i(e,t)):a=[],o=0;o<n.length;o++)if(0===(c=n[o]).refs){for(var d=0;d<c.parts.length;d++)c.parts[d]();delete r[c.id]}}}function m(e){for(var t=0;t<e.length;t++){var n=e[t],i=r[n.id];if(i){i.refs++;for(var o=0;o<i.parts.length;o++)i.parts[o](n.parts[o]);for(;o<n.parts.length;o++)i.parts.push(g(n.parts[o]));i.parts.length>n.parts.length&&(i.parts.length=n.parts.length)}else{var a=[];for(o=0;o<n.parts.length;o++)a.push(g(n.parts[o]));r[n.id]={id:n.id,refs:1,parts:a}}}}function v(){var e=document.createElement("style");return e.type="text/css",a.appendChild(e),e}function g(e){var t,n,i=document.querySelector("style["+p+'~="'+e.id+'"]');if(i){if(d)return u;i.parentNode.removeChild(i)}if(f){var o=c++;i=s||(s=v()),t=x.bind(null,i,o,!1),n=x.bind(null,i,o,!0)}else i=v(),t=b.bind(null,i),n=function(){i.parentNode.removeChild(i)};return t(e),function(i){if(i){if(i.css===e.css&&i.media===e.media&&i.sourceMap===e.sourceMap)return;t(e=i)}else n()}}var _=function(){var e=[];return function(t,n){return e[t]=n,e.filter(Boolean).join("\n")}}();function x(e,t,n,i){var o=n?"":i.css;if(e.styleSheet)e.styleSheet.cssText=_(t,o);else{var r=document.createTextNode(o),a=e.childNodes;a[t]&&e.removeChild(a[t]),a.length?e.insertBefore(r,a[t]):e.appendChild(r)}}function b(e,t){var n=t.css,i=t.media,o=t.sourceMap;if(i&&e.setAttribute("media",i),l.ssrId&&e.setAttribute(p,t.id),o&&(n+="\n/*# sourceURL="+o.sources[0]+" */",n+="\n/*# sourceMappingURL=data:application/json;base64,"+btoa(unescape(encodeURIComponent(JSON.stringify(o))))+" */"),e.styleSheet)e.styleSheet.cssText=n;else{for(;e.firstChild;)e.removeChild(e.firstChild);e.appendChild(document.createTextNode(n))}}},"81b2":function(e,t,n){var i=n("a9a3");i.__esModule&&(i=i.default),"string"==typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals),(0,n("5925").default)("004a6b16",i,!0,{sourceMap:!1,shadowMode:!1})},"8bbf":function(t,n){t.exports=e},"8c91":function(e,t,n){var i=n("0fc0");i.__esModule&&(i=i.default),"string"==typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals),(0,n("5925").default)("62c73430",i,!0,{sourceMap:!1,shadowMode:!1})},"9cc7":function(e,t,n){"use strict";n("81b2")},a1a8:function(e,t,n){"use strict";function i(e,t){var n=e[1]||"",i=e[3];if(!i)return n;if(t&&"function"==typeof btoa){var o=function(e){var t=btoa(unescape(encodeURIComponent(JSON.stringify(e)))),n="sourceMappingURL=data:application/json;charset=utf-8;base64,".concat(t);return"/*# ".concat(n," */")}(i),r=i.sources.map((function(e){return"/*# sourceURL=".concat(i.sourceRoot||"").concat(e," */")}));return[n].concat(r).concat([o]).join("\n")}return[n].join("\n")}e.exports=function(e){var t=[];return t.toString=function(){return this.map((function(t){var n=i(t,e);return t[2]?"@media ".concat(t[2]," {").concat(n,"}"):n})).join("")},t.i=function(e,n,i){"string"==typeof e&&(e=[[null,e,""]]);var o={};if(i)for(var r=0;r<this.length;r++){var a=this[r][0];null!=a&&(o[a]=!0)}for(var s=0;s<e.length;s++){var c=[].concat(e[s]);i&&o[c[0]]||(n&&(c[2]?c[2]="".concat(n," and ").concat(c[2]):c[2]=n),t.push(c))}},t}},a9a3:function(e,t,n){(t=n("a1a8")(!1)).push([e.i,".count_item[data-v-20a78350]{display:flex;flex-direction:column;align-items:center;justify-content:center;padding:12px 0 14px;position:relative;width:180px;box-sizing:content-box}.count_item .item_number[data-v-20a78350]{font-size:32px;line-height:40px;font-size:40px;line-height:48px;font-weight:700;color:#1d1d1f!important}.count_item .bottom_line[data-v-20a78350]{height:4px;width:20px;border-radius:4px;margin:6px 0}.count_item .item_name[data-v-20a78350]{font-size:16px;line-height:20px;color:#1d1d1f!important;max-width:160px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}",""]),e.exports=t},ed08:function(e,t,n){"use strict";n.d(t,"a",(function(){return r}));var i=n("8bbf"),o=n.n(i);function r(e,t,n){return new o.a({i18n:n.props.i18n,render:t=>t(e,n)}).$mount(t)}}})}));
//# sourceMappingURL=app.js.map