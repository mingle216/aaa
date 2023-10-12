<template>
  <div
    class="card-detailsofservice"
    v-loading="loading"
    :id="templateCode === 'SYS_TEMPLATE_WORK' ? 'work_detail' : ''"
  >
    <div
      v-show="isFixed"
      class="serviceName portal-font-color-lv1"
      v-if="isnotEmpty"
    >
      {{ eName }}
    </div>
    <div class="header" :class="[isnotEmpty ? '' : 'header-empty']">
      <div class="header-content">
        <div class="title">
          <template v-if="isnotEmpty">
            <img
              :src="serviceItemInfo.iconUrl || errorImg"
              @error="handleError"
              alt
            />
          </template>

          <we-popover
            v-model="showPopover"
            trigger="click"
            :text="eName"
            v-if="eName && eName.length > 20"
          >
            <template #reference>
              <span class="title-info">{{ eName }}</span>
            </template>
            <p class="title-detail-info portal-font-color-lv1">
              {{ eName }}
            </p>
          </we-popover>
          <div v-else class="title-info-name">
            <span class="title-info">{{ eName }}</span>
          </div>

          <!-- <we-popover v-model="showPopover" trigger="click">
            <template #default>
              <span class="title-info">{{ serviceItemInfo.itemName }}</span>
            </template>
          </we-popover> -->
        </div>
        <div class="mt-24" v-if="isnotEmpty">
          <we-rate
            :value="Number(serviceItemInfo.appraiseMark)"
            readonly
            :count="5"
            color="#ffd21e"
            void-icon="star"
            void-color="#ddd"
            allow-half
          />
          <span class="score">{{
            serviceItemInfo.appraiseMark
              ? Number(serviceItemInfo.appraiseMark).toFixed(1)
              : "0.0"
          }}</span>
          <span class="comment"
            >({{
              $Lan(lanFunName, "appraise", "{num}条评论", {
                num: serviceItemInfo.appraiseNum,
              })
            }})</span
          >
        </div>
      </div>
    </div>
    <div
      class="body portal-font-color-lv1"
      v-if="isnotEmpty"
      :class="[isshowFooter ? '' : 'hasnoFooter']"
    >
      <!--基本信息 -->
      <area-title
        :title="$Lan(lanFunName, 'baseinfo', '基本信息')"
      ></area-title>
      <base-info
        :data="serviceItemInfo.baseInfos"
        :lan-fun-name="lanFunName"
      ></base-info>
      <div
        v-for="(it, key) in serviceItemInfo.indptModuls"
        :key="key"
        class="body-item"
      >
        <!--咨询电话 -->
        <template
          v-if="it.fieldWid == 'CONTACT_PHONE' && isShow(it.fieldValue)"
        >
          <area-title :title="it.fieldName" />
          <service-phone :data="it.fieldValue" :router="router" />
        </template>

        <!--办理须知 -->
        <template
          v-else-if="it.fieldWid == 'INSTRUCTIONS' && isShow(it.fieldValue)"
        >
          <area-title :title="it.fieldName" />
          <p
            class="service-note-content"
            v-html="it.fieldValue"
            @click="aClick"
          ></p>
        </template>

        <!--办理流程 -->
        <template
          v-else-if="it.fieldWid == 'PROCESS_FLOW' && isShow(it.fieldValue)"
        >
          <area-title :title="it.fieldName" />
          <div
            class="flow-chart-content-desc"
            @click="aClick"
            v-html="
              it.fieldValue && it.fieldValue.comments.replace(/\n/, '<br/>')
            "
          ></div>
          <we-button
            plain
            type="primary"
            class="
              flowchartBtn
              portal-primary-color-lv1 portal-primary-border-color-lv1
            "
            v-if="it.fieldValue.image"
            @click="previewFlowChart(it.fieldValue.image)"
            >{{ $Lan(lanFunName, "checkflow", "查看流程图") }}</we-button
          >
        </template>

        <!--相关材料-->
        <template
          v-else-if="
            it.fieldWid == 'RELATED_MATERIALS' && isShow(it.fieldValue)
          "
        >
          <area-title :title="it.fieldName" />
          <div class="related-materials">
            <ul class="related-materials-content">
              <li
                v-for="(it, key) in it.fieldValue"
                :key="key"
                class="related-materials-content-item"
              >
                <span>{{ key + 1 }}.</span>
                <a
                  class="name"
                  :title="it.material_comments"
                  @click="openAttachment(it)"
                  :class="[it.material_path ? 'portal-primary-color-lv1' : '']"
                  >{{ it.material_comments }}</a
                >
              </li>
            </ul>
          </div>
        </template>

        <!--办理地点-->
        <template
          v-else-if="it.fieldWid == 'DEAL_ADDRESS' && isShow(it.fieldValue)"
        >
          <area-title :title="it.fieldName" />
          <ul class="service-location-content">
            <li
              v-for="(location, key) in it.fieldValue"
              :key="key"
              class="service-location-content-item"
              @click="openMap(location)"
            >
              <img :src="iconAddress" alt class="icon" />
              <div
                class="
                  service-location-content-item-value
                  portal-primary-color-lv1
                "
              >
                {{ location.siteName }}
              </div>
            </li>
          </ul>
        </template>

        <!--办理时间-->
        <template
          v-else-if="it.fieldWid == 'DEAL_TIME' && isShow(it.fieldValue)"
        >
          <area-title :title="it.fieldName" />
          <div v-html="it.fieldValue" class="mt-12" @click="aClick"></div>
        </template>

        <!--常见问题-->
        <template v-else-if="it.fieldWid == 'FAQS' && isShow(it.fieldValue)">
          <area-title :title="it.fieldName" />
          <service-question
            :data="it.fieldValue"
            :lanFunName="lanFunName"
            :router="router"
          />
        </template>

        <!-- 独立字段-->
        <template v-else-if="it.type == 1 && isShow(it.fieldValue)">
          <area-title :title="it.fieldName" />
          <div>{{ it.fieldValue }}</div>
        </template>
        <template v-else-if="it.type == 3 && isShow(it.fieldValue)">
          <area-title ::title="it.fieldName" />
          <p class="info-wrapper" v-html="it.fieldValue"></p>
        </template>
        <template v-else-if="it.type == 4 && isShow(it.fieldValue)">
          <area-title :title="it.fieldName" />
          <div
            v-for="(e, i) in it.fieldValue"
            :key="i + e.material_comments"
            class="phone-item"
          >
            <span v-if="!e.material_path">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              {{ e.material_comments }}
            </span>
            <span v-if="e.material_path">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              <a
                class="portal-primary-color-lv1"
                :href="e.material_path"
                target="_blank"
                >{{ e.material_comments }}</a
              >
            </span>
          </div>
        </template>
        <template v-else-if="it.type == 5 && isShow(it.fieldValue)">
          <area-title :title="it.fieldName" />
          <div style="margin-bottom: 15px">{{ it.fieldValue.text }}</div>
          <div
            v-for="(e, i) in it.fieldValue.files"
            :key="i + it.file"
            class="phone-item"
          >
            <span v-if="!e.file">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              {{ e.name }}
            </span>
            <span v-if="e.file">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              <a
                class="portal-primary-color-lv1"
                :href="e.file"
                target="_blank"
                >{{ e.name }}</a
              >
            </span>
          </div>
        </template>
        <template v-else-if="it.type == 6 && isShow(it.fieldValue)">
          <area-title :title="it.fieldName" />
          <p
            class="info-wrapper"
            v-html="it.fieldValue.text"
            style="margin-bottom: 12px"
          ></p>
          <div
            v-for="(e, i) in it.fieldValue.files"
            :key="e.file + i"
            class="phone-item"
          >
            <span v-if="!e.file">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              {{ e.name }}
            </span>
            <span v-if="e.file">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              <a
                class="portal-primary-color-lv1"
                :href="e.file"
                target="_blank"
                >{{ e.name }}</a
              >
            </span>
          </div>
        </template>
      </div>
    </div>
    <div class="body-empty portal-font-color-lv1" v-if="!isnotEmpty">
      <EmptyCon
        :tip="
          $Lan(
            lanFunName,
            'emptytips',
            '此服务事项暂时无法使用,如有问题请联系管理员'
          )
        "
        :height="200"
        style="width: 100%"
      />
    </div>

    <footers
      ref="footers"
      :footData="footData"
      @submitEvaluate="submitEvaluate"
      :isLogin="isLogin"
      :loginUrl="loginUrl"
      :lanFunName="lanFunName"
      :appraiseName="appraiseName"
      :router="router"
    ></footers>

    <we-image-preview
      v-model="processflow.show"
      :images="processflow.images"
      :showIndex="false"
    ></we-image-preview>
    <customActionSheet v-model="mapShow" style="height: auto">
      <div class="as-con">
        <div
          class="as-item"
          v-for="item in actions"
          :key="item.name"
          @click="onSelect(item)"
          :class="[
            item.name == $Lan(lanFunName, 'cancel', '取消') ? 'mt-6' : '',
          ]"
        >
          {{ item.name }}
        </div>
      </div>
    </customActionSheet>
  </div>
</template>

<script>
import AreaTitle from "./components/AreaTitl.vue";
import BaseInfo from "./components/BaseInfo.vue";
import ServicePhone from "./components/ServicePhone.vue";
import ServiceQuestion from "./components/ServiceQuestion.vue";
import Footers from "./components/Footer.vue";
export default {
  components: { AreaTitle, BaseInfo, ServicePhone, ServiceQuestion, Footers },
  name: "",
  props: {
    index: Number,
    router: Object,
  },
  data() {
    const { cardWid, cardId } = this.router;
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      loading: false,
      eName: "",
      dataList: [],
      serviceItemInfo: {},
      configinfo: {},
      linkService: [],
      lanFunName: cardId + "_h5",
      footData: {
        cardWid: "",
        config: {},
        linkService: [],
        favorite: false,
        serviceName: "",
        serviceItemWid: "",
        showOnlineButton: 2,
      },
      processflow: { show: false, images: [] },

      iconAddress: require(".././public/img/icon-address.png"),
      isFixed: false,
      scrollTop: 0,
      mapShow: false,
      actions: [],
      location: { lat: "", lng: "", siteName: "" },
      isLogin: null,
      errorImg: window.shell.ErrorImg,
      showPopover: false,
      emptyFlag: true,
      isnotEmpty: true,
      hreflocation: {},
      appraiseName: [],
    };
  },
  computed: {
    loginUrl() {
      const hash = this.hreflocation.hash || "";
      return hash.replace("#", "");
    },
    isshowFooter() {
      return (this.footData.config.detailConfigure &&
        this.footData.config.detailConfigure.length > 0) ||
        this.footData.linkService.length > 0
        ? true
        : false;
    },
    templateCode() {
      return window.shell.getTemplateCode();
    },
    // getcardHeight() {
    //   return {
    //     height: `${window.innerHeight}px`,
    //   };
    // },
  },
  created() {
    this.actions = [
      { name: this.$Lan(this.lanFunName, "baidumap", "百度地图") },
      { name: this.$Lan(this.lanFunName, "gaodemap", "高德地图") },
      { name: this.$Lan(this.lanFunName, "texunmap", "腾讯地图") },
      { name: this.$Lan(this.lanFunName, "cancel", "取消") },
    ];
    this.hreflocation = window.shell.getLocation() || {};
    // this.hreflocation.href = this.hreflocation.href.split("&name")[0];
    this.rederData("init");
    window.shell.on("getScrollTop", (it) => {
      this.scrollTop = it;
    });
    this.miniprogram = window.shell.isWxMiniProgram(); //是否是微信小程序
    // const pageData = JSON.parse(JSON.stringify(window.shell.getPageData()));
    // this.listenerFunction();
  },
  beforeDestroy() {
    window.shell.off("getScrollTop");
    //  document.removeEventListener("scroll", this.listenerFunction);
  },
  methods: {
    aClick(event) {
      if (event.path.some((v) => v.tagName === "A")) {
        window.minosStataCollect.collect({
          actionType: 0,
          functionType: 1,
          actionParams: {
            pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
            pageName: window.shell.getPageData().pageInfoEntity.pageName,
            cardWid: this.router.cardWid,
            cardId: this.router.cardId,
            cardName: this.router.cardName,
          },
          startTime: new Date().getTime(),
        });
      }
    },
    rederData(isInit) {
      if (window.shell) {
        let urlParam = window.shell.getUrlParam();
        const wid = urlParam && urlParam.wid;
        this.getCardData({ wid: wid, lang: this.$i18n.locale }, isInit);
      }
    },
    getCardData(params, isInit) {
      if (isInit && isInit === "init") {
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
            extraInfo: "",
          },
          startTime: new Date().getTime(),
        });
      }
      this.loading = true;
      window.shell
        .execCardMethod({
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "renderData",
          param: params,
        })
        .then((data) => {
          if (isInit && isInit === "init") {
            window.minosStataCollect.loadEnd({
              listId: this.listId,
              endTime: new Date().getTime(),
            });
          }
          if (data.errcode == 0) {
            this.isLogin = !data.data.hasToLogin;
            this.loading = false;
            this.eName = data.data.itemName;

            this.serviceItemInfo = data.data.serviceItemInfo;
            this.appraiseName = this.serviceItemInfo.dimenAppraiseNames || [];
            // 浏览器标题
            window.shell.setBroswerTitle(data.data.serviceItemInfo.itemName);
            if (
              this.serviceItemInfo &&
              this.serviceItemInfo.baseInfos &&
              this.serviceItemInfo.baseInfos.length
            ) {
              this.isnotEmpty = true;
            } else {
              this.isnotEmpty = false;
            }
            if (this.isnotEmpty) {
              this.footData.config = data.data.config;
              this.footData.linkService = data.data.linkService;
              this.footData.favorite = data.data.favorite;
              this.footData.cardWid = data.data.cardWid;
              this.footData.serviceName = data.data.itemName;
              this.footData.serviceItemWid = data.data.serviceItemWid;
              this.footData.showOnlineButton = data.data.showOnlineButton;
            }
          } else {
            window.shell.showMessage({
              message: this.$Lan(
                this.lanFunName,
                "errormessage",
                "获取事项详情错误"
              ),
              type: "error",
            });
          }
        })
        .catch(() => {
          if (isInit && isInit === "init") {
            window.minosStataCollect.loadEnd({
              listId: this.listId,
              endTime: new Date().getTime(),
            });
          }
        });
    },
    previewFlowChart(image) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: "",
        },
        startTime: new Date().getTime(),
      });
      this.processflow.show = true;
      this.processflow.images.push(image);
    },
    ellipsis(value) {
      console.log(this.get_length(value), this.cut_str(value, 20));
      //   return this.cut_str(value, 16);
      let len = value.length;
      if (!value) return "";
      if (this.get_length(value) > 20) {
        return this.cut_str(value, 16) + "..." + value.substring(len - 8, len);
        // return this.cut_str(value, 16);
      }
      return value;

      // let len = value.length;
      // console.log("value", value.replace(/[^\x00-\xff]/g, "01").length);
      // if (!value) return "";
      // console.log("value", value, this.getLength(value));
      //  if (this.getLength(value) > 48) {
      // return value.substring(0, 16) + "..." + value.substring(len - 8, len);
      //  return this.cut_str(value,16)
      //   }
      //  return value;
    },
    get_length(s) {
      var char_length = 0;
      for (var i = 0; i < s.length; i++) {
        var son_char = s.charAt(i);
        encodeURI(son_char).length > 2
          ? (char_length += 1)
          : (char_length += 0.5);
      }
      return char_length;
    },
    cut_str(str, len) {
      let char_length = 0;
      for (let i = 0; i < str.length; i++) {
        let son_str = str.charAt(i);
        encodeURI(son_str).length > 2
          ? (char_length += 1)
          : (char_length += 0.5);
        if (char_length >= len) {
          let sub_len = char_length == len ? i + 1 : i;
          return str.substr(0, sub_len);
          // break;
        }
      }
    },
    getLength(str) {
      let realLength = 0,
        len = str.length,
        charCode = -1;
      for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);

        if (charCode >= 0 && charCode <= 128) realLength += 1;
        else realLength += 2;
      }

      return realLength;
    },
    async submitEvaluate({params, uploadLists}) {
      //提交评价
      let param = {
        serviceWid: this.footData.serviceItemWid,
      };
      param = Object.assign(param, params);
      await window.shell
        .saveAppraiseWithPicByItem(
          {
            uploadLists: uploadLists,
            data: JSON.stringify(param),
          },
          (res) => {
            if(res.errcode === '0') {
              //评价成功
              window.shell
                .execCardMethod({
                  cardId: this.router.cardId,
                  cardWid: this.router.cardWid,
                  method: "evaluationResponse",
                  param: {
                    wid: this.itemWid,
                  },
                })
                .then((res) => {
                  if (res.errcode == 0) {
                    this.serviceItemInfo.appraiseNum = res.data.data.appraiseNum;
                    this.serviceItemInfo.appraiseMark =
                      res.data.data.appraiseMark;
                    window.shell.showMessage({
                      message: this.$Lan(this.lanFunName, "message", "感谢评价"),
                      type: "success",
                    });
                    //关闭弹框
                    this.$refs.footers.oSheetEvaluate.show = false;
                    this.rederData();
                  }
                });
            }else {
              window.shell.showMessage({
                message: res.errmsg,
                type: "warning",
              });
            }
          }
        )
        .catch(() => {
          
        });

      // console.log("param", param);
      // window.shell
      //   .execCardMethod({
      //     cardId: this.router.cardId,
      //     cardWid: this.router.cardWid,
      //     method: "evaluation",
      //     param,
      //   })
      //   .then((res) => {
      //     if (res.data.errcode == 0) {
      //       //评价成功
      //       window.shell
      //         .execCardMethod({
      //           cardId: this.router.cardId,
      //           cardWid: this.router.cardWid,
      //           method: "evaluationResponse",
      //           param: {
      //             wid: this.itemWid,
      //           },
      //         })
      //         .then((res) => {
      //           if (res.errcode == 0) {
      //             this.serviceItemInfo.appraiseNum = res.data.data.appraiseNum;
      //             this.serviceItemInfo.appraiseMark =
      //               res.data.data.appraiseMark;
      //             window.shell.showMessage({
      //               message: this.$Lan(this.lanFunName, "message", "感谢评价"),
      //               type: "success",
      //             });
      //             //关闭弹框
      //             this.$refs.footers.oSheetEvaluate.show = false;
      //             this.rederData();
      //           }
      //         });
      //     } else {
      //       window.shell.showMessage({
      //         message: res.data.errmsg,
      //         type: "warning",
      //       });
      //     }
      //   });
    },
    openMap(l) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: "",
        },
        startTime: new Date().getTime(),
      });
      if (this.miniprogram) {
        //微信小程序
        window.shell.wxMiniProgramOpenMap(l);
        return;
      }
      this.mapShow = true;
      this.location.lat = l.latitude;
      this.location.lng = l.longitude;
      this.location.siteName = l.siteName;
    },
    onSelect(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: "",
        },
        startTime: new Date().getTime(),
      });
      const l = this.location;
      switch (item.name) {
        case this.$Lan(this.lanFunName, "baidumap", "百度地图"):
          window.parent.location.href = `http://api.map.baidu.com/marker?location=${l.lat},${l.lng}&title=${l.siteName}&content=${l.siteName}&output=html&src=webapp.baidu.openAPIdemo`;
          break;
        case this.$Lan(this.lanFunName, "texunmap", "腾讯地图"):
          window.parent.location.href = `http://apis.map.qq.com/uri/v1/marker?marker=coord:${l.lat},${l.lng};title=${l.siteName};addr:${l.siteName}&referer=yellowpage`;
          break;
        case this.$Lan(this.lanFunName, "gaodemap", "高德地图"):
          window.parent.location.href = `https://uri.amap.com/marker?position=${l.lng},${l.lat}&name=${l.siteName}`;
          break;
        default:
          this.mapShow = false;
      }
      this.mapShow = false;
    },
    isShow(data) {
      if (data) {
        if (Array.isArray(data)) {
          if (data.length) {
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
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null;
    },
    openAttachment(attachment) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: "",
        },
        startTime: new Date().getTime(),
      });
      // const comments = attachment.material_comments;
      const url = attachment.material_path;

      if (url) {
        window.shell.openPage(url, 1, 2);
        //产品确认不做处理，让浏览器处理
        // if ((/\.(doc|docx)$/i.test(comments)) && window.shell.isIphone()) {
        //   window.shell.showMessage({
        //     message: "此手机系统不支持该附件预览及下载",
        //     type: "warning",
        //   });
        // } else {
        //   window.shell.openPage(url, 1, 2);
        // }
      } else {
        return;
      }
    },
    // listenerFunction() {
    //   document.addEventListener("scroll", this.handleScroll, true);
    // },
    // handleScroll() {
    //   if (this.$refs.container.scrollTop > 150) {
    //     this.isFixed = true;
    //   } else {
    //     this.isFixed = false;
    //   }
    // },
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 5000);
      }
    },
    scrollTop(val) {
      this.isFixed = val >= 180;
    },
  },
  filters: {},
};
</script>
<style lang="less">
.we-popover[data-popper-placement^="bottom"] .we-popover__arrow {
  top: 1px !important;
}
.we-popover[data-popper-placement="bottom"] .we-popover__arrow {
  left: 90%;
}
</style>
<style lang="less" scoped>
.card-detailsofservice {
  .mt-12 {
    margin-top: 12px;
  }
  .mt-24 {
    margin-top: 24px;
  }
  .info-wrapper {
    white-space: pre-wrap;
    word-break: break-all;
  }
  .phone-item {
    height: 48px;
    border-bottom: 1px dashed #e7edf1;
    line-height: 48px;
    position: relative;
  }
  /deep/.we-rate__icon {
    font-size: 13px !important;
  }
  // .container-box {
  //   height: 100%;
  //   overflow-y: auto;
  // }
  .serviceName {
    position: fixed;
    top: 0;
    left: 0;
    margin-top: -1px;
    width: 100%;
    padding: 16px 12px;
    font-size: 18px;
    font-weight: 600;
    background: #ffffff;
    z-index: 99;
    box-shadow: 0 4px 12px 0 rgba(112, 125, 143, 0.2);
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .header {
    background: url(.././public/img/header.png) 0 0 no-repeat;
    background-size: 100% 100%;
    height: 186px;
    color: #fff;
    position: relative;
    &-content {
      position: absolute;
      margin: 40px 16px;
      .title-info {
        font-size: 26px;
      }
      .title-info-name {
        display: flex;
        align-items: center;
      }
    }

    .title {
      display: flex;
      img {
        width: 56px;
        height: 56px;
        margin-right: 16px;
      }
      &-info {
        word-break: break-all;
        display: -webkit-box;
        overflow: hidden;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }
    }
    .comment,
    .score {
      font-size: 14px;
    }
    .score {
      margin: 0 16px 0 8px;
    }
  }
  .header-empty {
    display: flex;
    align-items: center;
  }
  .body-empty {
    margin-top: 60px;
  }
  .body {
    padding: 16px;
    display: flex;
    flex-direction: column;
    font-size: 14px;
    margin-bottom: 60px;

    &-item {
      /deep/.block-title {
        margin-top: 32px;
      }

      .service-note {
        &-content {
          margin-top: 12px;
          color: #102645;
          line-height: 20px;
          word-break: break-all;
          white-space: pre-wrap;
        }
      }

      .flow-chart {
        &-content {
          &-desc {
            display: block;
            margin-top: 16px;
            margin-bottom: 16px;
            color: #102645;
            word-break: break-all;
            white-space: pre-wrap;
          }
        }
      }

      .flowchartBtn {
        width: 100%;
        height: 36px;
        font-size: 14px;
        border-radius: 4px;
      }

      .related-materials {
        &-content {
          &-item {
            width: 100%;
            height: 48px;
            display: flex;
            align-items: center;
            padding: 14px 0;
            box-shadow: inset 0 -1px 0 #f1f2f4;

            .name {
              width: 100%;
              margin-left: 8px;
              font-size: 14px;
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
            }
          }
        }
      }

      .service-location-content {
        margin-top: 12px;

        &-item {
          display: flex;
          align-items: center;
          padding: 12px 0;
          box-shadow: inset 0 -1px 0 #f1f2f4;

          .icon {
            width: 16px;
            // height:16px;
          }

          // &-icon {
          //   font-size: 16px;
          //   min-width: 28px;
          // }
          &-value {
            margin-left: 8px;
          }
        }
      }
    }
    /deep/.we-button--normal {
      padding: 0 0 !important;
    }
  }
  .hasnoFooter {
    margin-bottom: 0;
  }
  .sheet-adress {
    /deep/.we-action-sheet__item {
      //border-bottom:1px solid #E7EDF1;
      box-shadow: 1px 1px 1px #e7edf1;
    }
  }
}
.title-detail-info {
  width: 260px;
  font-size: 14px;
  letter-spacing: 0;
  text-align: justify;
  padding: 12px;
}
.as-con {
  height: 100%;
  overflow-y: auto;
  width: 100%;
  box-sizing: border-box;
  background: #f6f6f8;
  .as-item {
    width: 100%;
    height: 50px;
    line-height: 50px;
    text-align: center;
    font-size: 16px;
    color: #102645;
    // box-shadow: 1px 1px 1px #e7edf1;
    border-bottom: 1px solid #e7edf1;
    background: #ffffff;
  }
  .mt-6 {
    margin-top: 6px;
  }
}

#work_detail {
  .header {
    height: 96px;
    padding: 16px 0;
    box-sizing: content-box;
    background: transparent;
    .title-info {
      line-height: 34px;
    }
    .header-content {
      margin: 0;
    }
    .mt-24 {
      margin-top: 19px;
    }
  }
  .body {
    border-radius: 24px;
    background: #fff;
    margin-bottom: 0;
  }
}
</style>
