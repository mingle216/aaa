!function(e,r){"object"==typeof exports&&"object"==typeof module?module.exports=r(require("Vue")):"function"==typeof define&&define.amd?define(["Vue"],r):"object"==typeof exports?exports.__REMOTE_LOADED_APP__=r(require("Vue")):e.__REMOTE_LOADED_APP__=r(e.Vue)}(window,(function(e){return function(e){var r={};function t(o){if(r[o])return r[o].exports;var n=r[o]={i:o,l:!1,exports:{}};return e[o].call(n.exports,n,n.exports,t),n.l=!0,n.exports}return t.m=e,t.c=r,t.d=function(e,r,o){t.o(e,r)||Object.defineProperty(e,r,{enumerable:!0,get:o})},t.r=function(e){"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},t.t=function(e,r){if(1&r&&(e=t(e)),8&r)return e;if(4&r&&"object"==typeof e&&e&&e.__esModule)return e;var o=Object.create(null);if(t.r(o),Object.defineProperty(o,"default",{enumerable:!0,value:e}),2&r&&"string"!=typeof e)for(var n in e)t.d(o,n,function(r){return e[r]}.bind(null,n));return o},t.n=function(e){var r=e&&e.__esModule?function(){return e.default}:function(){return e};return t.d(r,"a",r),r},t.o=function(e,r){return Object.prototype.hasOwnProperty.call(e,r)},t.p="/CUS_CARD_MYDATA/pc/",t(t.s=1)}({1:function(e,r,t){e.exports=t("db49")},2877:function(e,r,t){"use strict";function o(e,r,t,o,n,s,u,i){var a,l="function"==typeof e?e.options:e;if(r&&(l.render=r,l.staticRenderFns=t,l._compiled=!0),o&&(l.functional=!0),s&&(l._scopeId="data-v-"+s),u?(a=function(e){(e=e||this.$vnode&&this.$vnode.ssrContext||this.parent&&this.parent.$vnode&&this.parent.$vnode.ssrContext)||"undefined"==typeof __VUE_SSR_CONTEXT__||(e=__VUE_SSR_CONTEXT__),n&&n.call(this,e),e&&e._registeredComponents&&e._registeredComponents.add(u)},l._ssrRegister=a):n&&(a=i?function(){n.call(this,(l.functional?this.parent:this).$root.$options.shadowRoot)}:n),a)if(l.functional){l._injectStyles=a;var c=l.render;l.render=function(e,r){return a.call(r),c(e,r)}}else{var f=l.beforeCreate;l.beforeCreate=f?[].concat(f,a):[a]}return{exports:e,options:l}}t.d(r,"a",(function(){return o}))},"8bbf":function(r,t){r.exports=e},db49:function(e,r,t){"use strict";t.r(r),t.d(r,"bootstrap",(function(){return l}));var o=t("ed08"),n={data(){return{ruleForm:{pass:"",checkPass:"",age:""},rules:{pass:[{validator:(e,r,t)=>{""===r?t(new Error("请输入密码")):(""!==this.ruleForm.checkPass&&this.$refs.ruleForm.validateField("checkPass"),t())},trigger:"blur"}],checkPass:[{validator:(e,r,t)=>{""===r?t(new Error("请再次输入密码")):r!==this.ruleForm.pass?t(new Error("两次输入密码不一致!")):t()},trigger:"blur"}],age:[{validator:(e,r,t)=>{if(!r)return t(new Error("年龄不能为空"));setTimeout((()=>{Number.isInteger(r)?r<18?t(new Error("必须年满18岁")):t():t(new Error("请输入数字值"))}),1e3)},trigger:"blur"}]}}},methods:{submitForm(e){this.$refs[e].validate((e=>{if(!e)return console.log("error submit!!"),!1;console.log("form submit: emit event"),this.$emit("submit",this.ruleForm)}))},resetForm(e){this.$refs[e].resetFields()}}},s=n,u=t("2877"),i=Object(u.a)(s,(function(){var e=this,r=e._self._c;return r("we-form",{ref:"ruleForm",staticClass:"demo-ruleForm",attrs:{model:e.ruleForm,"status-icon":"",rules:e.rules,"labwe-width":"100px"}},[r("we-form-item",{attrs:{label:"cardLink密码",prop:"pass"}},[r("we-input",{attrs:{type:"password",autocomplete:"off"},model:{value:e.ruleForm.pass,callback:function(r){e.$set(e.ruleForm,"pass",r)},expression:"ruleForm.pass"}})],1),r("we-form-item",{attrs:{label:"cardLink确认密码",prop:"checkPass"}},[r("we-input",{attrs:{type:"password",autocomplete:"off"},model:{value:e.ruleForm.checkPass,callback:function(r){e.$set(e.ruleForm,"checkPass",r)},expression:"ruleForm.checkPass"}})],1),r("we-form-item",{attrs:{label:"cardLink年龄",prop:"age"}},[r("we-input",{model:{value:e.ruleForm.age,callback:function(r){e.$set(e.ruleForm,"age",e._n(r))},expression:"ruleForm.age"}})],1),r("we-form-item",[r("we-button",{attrs:{type:"primary"},on:{click:function(r){return e.submitForm("ruleForm")}}},[e._v("提交")]),r("we-button",{on:{click:function(r){return e.resetForm("ruleForm")}}},[e._v("重置")])],1)],1)}),[],!1,null,null,null),a=i.exports;function l(e,r){return Object(o.a)(a,e,r)}},ed08:function(e,r,t){"use strict";t.d(r,"a",(function(){return s}));var o=t("8bbf"),n=t.n(o);function s(e,r,t){return new n.a({i18n:t.props.i18n,render:r=>r(e,t)}).$mount(r)}}})}));
//# sourceMappingURL=config.js.map