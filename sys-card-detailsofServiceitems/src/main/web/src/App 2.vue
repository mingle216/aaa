<i18n>
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
</i18n>
<template>
  <div
    class="itemdetails portal-font-color-lv1"
    id="itemdetails"
    ref="itemdetails"
    :style="`width:${iWidth}`"
  >
    <div v-if="!emptyFlag" class="emptyName">{{ eName }}</div>
    <div class="emptyImg" v-if="!emptyFlag">
      <img src="../public/img/empty.png" alt v-if="!emptyFlag" />
    </div>
    <div class="w1" v-if="!emptyFlag">{{ $t("emptyTip1") }}</div>
    <div class="w2" v-if="!emptyFlag">{{ $t("emptyTip2") }}</div>
    <!--头部-->
    <div class="header" v-if="emptyFlag">
      <div class="h-container">
        <!-- <div class="pic"><img :src="dataList.iconUrl || errorImg" @error="handleError" /></div> -->
        <div class="pic" v-if="pageConfigure.includes('icon') && emptyFlag">
          <img :src="dataList.iconUrl || errorImg" @error="handleError" alt />
        </div>
        <div class="title-evaluate" v-if="emptyFlag && timeFlag">
          <!-- <div class="title">{{ dataList.itemName }}</div> -->

          <we-tooltip
            class="item"
            effect="dark"
            placement="bottom-start"
            :open-delay="900"
          >
            <div slot="content">{{ xName }}</div>
            <div
              class="title"
              :class="{
                'portal-font-color-lv1': colorGroup === 'deep',
              }"
            >
              {{ xName }}
            </div>
          </we-tooltip>
          <div
            class="evaluate"
            :class="{
              'portal-font-color-lv2': colorGroup === 'deep',
            }"
            v-if="detailConfigure.includes('evaluation')"
          >
            <div class="star">
              <we-rate
                v-model="value"
                disabled
                text-color="#ff9900"
                score-template="{value}"
              ></we-rate>
            </div>
            <span class="score">{{ dataList.appraiseMark }}</span>
            <span class="countdetail">({{ dataList.appraiseNum }}条评论)</span>
          </div>
        </div>
        <div
          class="btn-group"
          :class="{
            'portal-primary-color-lv1 portal-primary-border-color-lv1':
              colorGroup === 'deep',
          }"
          v-if="emptyFlag"
        >
          <div class="greyZxbl" @click="handleOnline" :style="cStyle"></div>
          <div
            id="zxbl"
            :class="{
              'portal-primary-backgroundcolor-lv1 zxbl-deep':
                colorGroup === 'deep',
              'portal-primary-color-lv1 zxbl-light': colorGroup === 'light',
            }"
            :style="[isLink, isUse]"
            @click="handleOnline"
            v-if="timeFlag"
          >
            在线办理
          </div>
          <div
            v-if="detailConfigure.includes('evaluation')"
            @click="showEv()"
            class="ccc"
            :class="{
              'portal-primary-backgroundcolor-lv5': colorGroup === 'deep',
              cc: colorGroup === 'light',
            }"
          >
            评价
          </div>
          <div
            v-if="detailConfigure.includes('favourite') && !favFlag"
            @click="updateData(itemWid, 1)"
            class="ccc"
            :class="{
              'portal-primary-backgroundcolor-lv5': colorGroup === 'deep',
              cc: colorGroup === 'light',
            }"
          >
            收藏
          </div>
          <div
            v-if="detailConfigure.includes('favourite') && favFlag"
            @click="updateData(itemWid, 0)"
            class="ccc"
            :class="{
              'portal-primary-backgroundcolor-lv5': colorGroup === 'deep',
              cc: colorGroup === 'light',
            }"
          >
            <span class="icon iconfont icon-favorites myFav"></span>已收藏
          </div>
          <div
            v-if="detailConfigure.includes('share')"
            class="ccc popoverClassname"
            style="position: relative"
            @click="fx()"
            :class="{
              'portal-primary-backgroundcolor-lv5': colorGroup === 'deep',
              cc: colorGroup === 'light',
            }"
          >
            分享
            <we-popover
              popper-class="myPopover"
              abc="abcabc"
              placement="bottom"
              width="118"
              height="174"
              trigger="click"
            >
              <div class="portal-primary-color-lv1 s1" @click="share()">
                <i class="iconfont icon-lianjie" style="font-size: inherit;"></i>
                复制链接
              </div>
              <div class="s2">
                <div id="qrcode2" ref="qrcode2"></div>
              </div>
              <div class="s3">“扫一扫”</div>
              <we-button class="real" slot="reference">click 激活</we-button>
            </we-popover>
          </div>
        </div>
      </div>
    </div>
    <!--内容信息-->
    <div
      class="content"
      v-if="emptyFlag"
      id="content"
      ref="content"
      :style="`width:${conWidth}`"
    >
      <!--基本信息-->
      <div
        class="basicInformation"
        style="position: relative"
        id="000000"
        v-if="timeFlag"
      >
        <div class="title1" style="margin-bottom: 13px">基本信息</div>

        <we-table
          class="table portal-font-color-lv1"
          :data="tableData"
          border
          :cell-style="cellStyle"
          :show-header="false"
        >
          <we-table-column
            prop="fieldName"
            label="name"
            width="120"
          ></we-table-column>
          <we-table-column
            prop="fieldValue"
            label="value"
            :width="itemdetail_width - 271"
          ></we-table-column>
        </we-table>
      </div>

      <div
        v-for="item in data2"
        :key="item.fieldWid"
        style="position: relative"
        :id="item.fieldWid"
      >
        <!--咨询电话-->
        <div
          v-if="
            item.fieldWid === 'CONTACT_PHONE' && itemCanSee(item.fieldValue)
          "
        >
          <div class="title1">{{ item.fieldName }}</div>
          <div
            v-for="(it, i) in item.fieldValue"
            :key="i + it.phone"
            class="tel-item"
          >
            <we-tooltip
              class="item"
              effect="dark"
              placement="bottom-start"
              :open-delay="900"
            >
              <span slot="content">{{ it.phone }}</span>
              <span class="tel-left">{{ it.phone }}</span>
            </we-tooltip>
            <span class="tel-num">{{ it.comments }}</span>
          </div>
        </div>
        <!--办理须知-->
        <div
          v-else-if="
            item.fieldWid === 'INSTRUCTIONS' && itemCanSee(item.fieldValue)
          "
        >
          <div class="title1" style="margin-bottom: 12px">
            {{ item.fieldName }}
          </div>
          <p class="detail-wrapper" v-html="item.fieldValue"></p>
        </div>
        <!--办理流程-->
        <div
          v-else-if="
            item.fieldWid === 'PROCESS_FLOW' && itemCanSee(item.fieldValue)
          "
        >
          <div class="title1" style="margin-bottom: 12px">
            {{ item.fieldName }}
          </div>
          <div class="detail-wrapper" v-html="item.fieldValue.comments"></div>
          <div
            v-if="item.fieldValue.image"
            class="lct-btn portal-primary-color-lv1 portal-primary-border-color-lv1"
            style="overflow: hidden; position: relative"
            @click="showLct()"
          >
            查看流程图
          </div>
          <div v-if="item.fieldValue.image">
            <transition name="fade">
              <div @click="toggle" v-show="lctshow" class="img-bg">
                <div class="t-title"></div>
                <div class="t-content">
                  <div class="xx">
                    <i class="we-icon-close xxx"></i>
                  </div>
                  <img :src="item.fieldValue.image" alt />
                </div>
                <div class="t-bottom"></div>
              </div>
            </transition>
          </div>
        </div>
        <!--相关材料-->
        <div
          v-else-if="
            item.fieldWid === 'RELATED_MATERIALS' && itemCanSee(item.fieldValue)
          "
        >
          <div class="title1">{{ item.fieldName }}</div>
          <div
            v-for="(it, i) in item.fieldValue"
            :key="i + it.material_comments"
            class="tel-item"
          >
            <span v-if="!it.material_path">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              <span>{{ it.material_comments }}</span>
            </span>
            <span v-if="it.material_path">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              <a
                class="portal-primary-color-lv1"
                :download="it.material_comments"
                :href="it.material_path"
                target="_blank"
                >{{ it.material_comments }}</a
              >
            </span>
          </div>
        </div>
        <!--办理地点-->
        <div
          v-else-if="
            item.fieldWid === 'DEAL_ADDRESS' && itemCanSee(item.fieldValue)
          "
        >
          <div class="title1" style="margin-bottom: 20px">
            {{ item.fieldName }}
          </div>
          <div style="display: flex">
            <div class="locationMap" style="margin-right: 34px" v-if="mapFlag">
              <!--地图是否可见，test之后把！去掉-->
              <div id="allmap" style="height: 280px; width: 500px">
                <baidu-map
                  class="map"
                  style="width: 100%; height: 100%"
                  :scroll-wheel-zoom="true"
                  :zoom="15"
                  :center="{ lng: lng, lat: lat }"
                >
                  <!-- <bm-local-search
                    style="display:none"
                    keyword="南京大学(鼓楼校区)"
                    :auto-viewport="true"
                    :location="location"
                  ></bm-local-search>-->
                  <bm-marker
                    :position="{ lng: lng, lat: lat }"
                    :dragging="true"
                    animation="BMAP_ANIMATION_BOUNCE"
                  />
                </baidu-map>
              </div>
            </div>
            <div class="locationName" :style="locStyle">
              <div v-for="(it, i) in item.fieldValue" :key="i + it.siteName">
                <div
                  class="lname-item"
                  @click="changePlace(it)"
                  :class="{
                    'portal-primary-color-lv1': activePlace == it.siteName,
                  }"
                >
                  <i class="icon iconfont icon-place" id="place-icon"></i>
                  {{ it.siteName }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <!--办理时间-->
        <div
          v-else-if="
            item.fieldWid === 'DEAL_TIME' && itemCanSee(item.fieldValue)
          "
        >
          <div class="title1" style="margin-bottom: 20px">
            {{ item.fieldName }}
          </div>
          <div class="detail-wrapper" v-html="item.fieldValue"></div>
        </div>
        <!--常见问题-->
        <div
          v-else-if="item.fieldWid === 'FAQS' && itemCanSee(item.fieldValue)"
          style="word-wrap: break-word"
        >
          <div class="title1">{{ item.fieldName }}</div>
          <div>
            <div
              v-for="(it, i) in showFlag
                ? item.fieldValue.slice(0, 5)
                : item.fieldValue"
              :key="it.question + i"
              class="ques-item"
            >
              <div style="margin-bottom: 8px">问： {{ it.question }}</div>
              <div class="portal-font-color-lv2">答： {{ it.answer }}</div>
            </div>
          </div>
          <!--常见问题大于5条的时候多余的隐藏并显示展示更多-->
          <div
            v-if="showFlagSee"
            class="showmore portal-primary-color-lv1"
            @click="showmore"
          >
            <span class="downIcon" v-if="showFlag">
              <i class="icon iconfont icon-menu-BackTop"></i>
            </span>
            <span class="upIcon" v-if="!showFlag">
              <i class="icon iconfont icon-menu-BackTop"></i>
            </span>
            {{ showFlag ? "展开" : "收起" }}
          </div>
        </div>
        <!--其他字段-->
        <!--1 文本-->
        <div v-else-if="item.type == 1 && itemCanSee(item.fieldValue)">
          <div class="title1" style="margin-bottom: 20px">
            {{ item.fieldName }}
          </div>
          <div>{{ item.fieldValue }}</div>
        </div>
        <!--3 富文本-->
        <div v-else-if="item.type == 3 && itemCanSee(item.fieldValue)">
          <div class="title1" style="margin-bottom: 12px">
            {{ item.fieldName }}
          </div>
          <p class="detail-wrapper" v-html="item.fieldValue"></p>
        </div>
        <!--附件-->
        <div v-else-if="item.type == 4 && itemCanSee(item.fieldValue)">
          <div class="title1">{{ item.fieldName }}</div>
          <div
            v-for="(it, i) in item.fieldValue"
            :key="i + it.material_comments"
            class="tel-item"
          >
            <span v-if="!it.material_path">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              {{ it.material_comments }}
            </span>
            <span v-if="it.material_path">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              <a
                class="portal-primary-color-lv1"
                :href="it.material_path"
                target="_blank"
                >{{ it.material_comments }}</a
              >
            </span>
          </div>
        </div>
        <!--文本 + 附件-->
        <div v-else-if="item.type == 5 && itemCanSee(item.fieldValue)">
          <div class="title1" style="margin-bottom: 20px">
            {{ item.fieldName }}
          </div>
          <div style="margin-bottom: 15px">{{ item.fieldValue.text }}</div>
          <div
            v-for="(it, i) in item.fieldValue.files"
            :key="i + it.file"
            class="tel-item"
          >
            <span v-if="!it.file">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              {{ it.name }}
            </span>
            <span v-if="it.file">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              <a
                class="portal-primary-color-lv1"
                :href="it.file"
                target="_blank"
                >{{ it.name }}</a
              >
            </span>
          </div>
        </div>
        <!--富文本 + 附件-->
        <div v-else-if="item.type == 6 && itemCanSee(item.fieldValue)">
          <div class="title1" style="margin-bottom: 12px">
            {{ item.fieldName }}
          </div>
          <p
            class="detail-wrapper"
            v-html="item.fieldValue.text"
            style="margin-bottom: 12px"
          ></p>
          <div
            v-for="(it, i) in item.fieldValue.files"
            :key="it.file + i"
            class="tel-item"
          >
            <span v-if="!it.file">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              {{ it.name }}
            </span>
            <span v-if="it.file">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              <a
                class="portal-primary-color-lv1"
                :href="it.file"
                target="_blank"
                >{{ it.name }}</a
              >
            </span>
          </div>
        </div>
      </div>
    </div>
    <we-dialog
      style="z-index: 9999"
      :style="dStyle"
      title="评价事项"
      :visible.sync="eDialog"
      width="510px"
      :close-on-click-modal="false"
    >
      <div class="line1"></div>
      <div class="eContent">
        <div class="eTop" style="display: flex">
          <div class="eLeft">
            <div>
              <span>服务事项：</span>
            </div>
            <div>
              <span>服务态度：</span>
            </div>
            <div>
              <span>办事速度：</span>
            </div>
            <div>
              <span>信息完整度：</span>
            </div>
            <div>
              <span>整体满意度：</span>
            </div>
          </div>
          <div class="eRight" style="margin-left: 15px">
            <we-tooltip
              class="item"
              effect="dark"
              placement="bottom-start"
              :open-delay="900"
            >
              <div slot="content">{{ itemName }}</div>
              <div class="evname" style="margin-bottom: 19px">
                {{ itemName }}
              </div>
            </we-tooltip>
            <div class="eStar">
              <div>
                <we-rate
                  v-model="value1"
                  show-text
                  :texts="texts"
                  :text-color="tcolor"
                ></we-rate>
              </div>
              <div>
                <we-rate
                  v-model="value2"
                  show-text
                  :texts="texts"
                  :text-color="tcolor"
                ></we-rate>
              </div>
              <div>
                <we-rate
                  v-model="value3"
                  show-text
                  :texts="texts"
                  :text-color="tcolor"
                ></we-rate>
              </div>
              <div>
                <we-rate
                  v-model="value4"
                  show-text
                  :texts="texts"
                  :text-color="tcolor"
                ></we-rate>
              </div>
            </div>
          </div>
        </div>
        <div style="margin-bottom: 16px">意见或建议（选填）：</div>
      </div>
      <div class="suText">
        <we-input
          type="textarea"
          placeholder="请在此处写下您的意见或建议"
          v-model="value5"
          maxlength="500"
          show-word-limit
          resize="none"
          rows="7"
        ></we-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <we-button
          @click="eDialog = false"
          style="margin-right: 11px"
          class="btn"
          >取消</we-button
        >
        <we-button
          type="primary"
          @click="ecommit()"
          class="btn btn-commit portal-primary-backgroundcolor-lv1"
          :disabled="!canCommit"
          >提交</we-button
        >
      </span>
    </we-dialog>

    <!--导航栏-->
    <div
      v-if="pageConfigure.includes('navbar') && emptyFlag"
      class="navbar portal-font-color-lv2"
      :style="navStyle"
    >
      <ul class="nAside portal-font-color-lv2">
        <li
          v-for="(item, i) in data3"
          :key="i"
          class="anchor-item"
          :class="{
            'anchor-item-active portal-primary-color-lv1':
              activeKey == item.fieldWid,
          }"
          style="position: relative"
          @click="getPlace(item)"
        >
          <span
            class="box portal-primary-backgroundcolor-lv1"
            :class="{ isBox: activeKey == item.fieldWid }"
          ></span>
          <a :title="item.fieldName" class="navItem">
            <div class="nav-name">{{ item.fieldName }}</div>
          </a>
        </li>
      </ul>
    </div>
    <!--吸顶/滚动时的固定简约头部-->
    <div class="fix-header" :style="`display:${headercansee}`" v-if="emptyFlag">
      <div class="fh-container" :style="`min-width:${itemdetail_width}px`">
        <div class="pic2" v-if="pageConfigure.includes('icon')">
          <img :src="dataList.iconUrl || errorImg" @error="handleError" alt />
        </div>
        <div class="title-evaluate2">
          <we-tooltip
            class="item"
            effect="dark"
            placement="bottom-start"
            :open-delay="900"
          >
            <div slot="content">{{ xName }}</div>
            <div class="title2">{{ xName }}</div>
          </we-tooltip>
          <div class="evaluate2" v-if="detailConfigure.includes('evaluation')">
            <div class="star2">
              <we-rate
                v-model="value"
                disabled
                text-color="#ff9900"
                score-template="{value}"
              ></we-rate>
            </div>
            <span class="score2">{{ dataList.appraiseMark }}</span>
            <span class="countdetail2">({{ dataList.appraiseNum }}条评论)</span>
          </div>
        </div>
        <div class="btn-group bg2" v-if="emptyFlag">
          <div class="greyZxbl" @click="handleOnline" :style="cStyle"></div>
          <div
            id="zxbl2"
            class="portal-primary-backgroundcolor-lv1"
            :style="[isLink, isUse2]"
            @click="handleOnline"
          >
            在线办理
          </div>
          <div
            v-if="detailConfigure.includes('evaluation')"
            @click="showEv()"
            class="bbb"
          >
            评价
          </div>
          <div
            v-if="detailConfigure.includes('favourite') && !favFlag"
            @click="updateData(itemWid, 1)"
            class="bbb"
          >
            收藏
          </div>
          <div
            v-if="detailConfigure.includes('share') && favFlag"
            @click="updateData(itemWid, 0)"
            class="bbb"
          >
            <span class="icon iconfont icon-favorites myFav"></span>已收藏
          </div>
          <div
            v-if="detailConfigure.includes('share')"
            class="bbb"
            style="position: relative"
            @click="fx()"
          >
            分享
            <we-popover
              popper-class="myPopover"
              placement="bottom"
              width="118"
              height="174"
              trigger="click"
              getTooltipContainer="{()=> document.getElementsByClasssName('popoverClassname')[0]}"
            >
              <div class="portal-primary-color-lv1 s1" @click="share()">
                <i class="iconfont icon-lianjie" style="font-size: inherit;"></i>
                复制链接
              </div>
              <div class="s2">
                <div id="qrcode" ref="qrcode"></div>
              </div>
              <div class="s3">“扫一扫”</div>
              <we-button class="real" slot="reference">click 激活</we-button>
            </we-popover>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import QRCode from "qrcodejs2";
import BaiduMap from "vue-baidu-map";
import Vue from "vue";
Vue.use(BaiduMap, { ak: "" });
export default {
  name: "detailsofserviceitems",
  props: {
    index: Number,
    router: Object,
  },
  data() {
    const { cardWid, cardId } = this.router;
    return {
      iTime: 0,
      card: {
        cardWid,
        cardId,
      },
      lctshow: false,
      tableData: [],
      eName: "",
      top: null,
      errorImg: window.shell.ErrorImg,
      curWid: "",
      lat: null,
      lng: null,
      activePlace: "",
      activeKey: "",
      navTop: 175,
      navStyle: "top: 175px;",
      headercansee: "none",
      tcolor: "#999",
      texts: ["非常不满意", "不满意", "一般", "比较满意", "非常满意"],
      value: null,
      value1: null,
      value2: null,
      value3: null,
      value4: null,
      value5: null,
      showFlag: null, // 展示更多
      showFlagSee: null, // 展示更多/收起 是否显示
      eDialog: false,
      isLink: null,
      isUse: null,
      isUse2: null,
      itemWid: "",
      itemName: "",
      favFlag: null,
      detailConfigure: [], //
      pageConfigure: [], //
      mapFlag: null,
      checked: false,
      fontClass: "",
      dataList: [],
      data1: [],
      data2: [],
      data3: [],
      data4: [],
      locationData: [],
      emptyName: "",
      emptyFlag: true,
      xName: "",
      isLogin: null,
      // loginUrl:'',
      location: {},
      v_top: 0,
      v_height: 0,
      startTop: 0,
      timeFlag: null,
      locStyle: "",
      dStyle: "",
      cStyle: null,
      new_scrollTop: 0,
      timeCommit: true,
      scrollFlag: null,
      isScroll: null,
      con_width: null,
      itemdetail_width: null,
      colorGroup: null,
      iWidth:null,
    };
  },
  filters: {},
  computed: {
    conWidth() {
      // return this.pageConfigure.includes("navbar") ? "1050px" : "";
      return this.pageConfigure.includes("navbar") ? "calc(100% - 150px)" : "";
    },
    canCommit() {
      return (
        this.value1 &&
        this.value2 &&
        this.value3 &&
        this.value4 &&
        this.timeCommit
      );
    },
    loginUrl() {
      const hash = this.location.hash || "";
      return hash.replace("#", "");
    },
  },
  methods: {
    crateQrcode(qrcode, text) {
      this.qr = new QRCode(qrcode, {
        width: 84,
        height: 84, // 高度
        text: text, // 二维码内容
      });
    },
    fx() {
      this.$refs.qrcode.innerHTML = "";
      this.$refs.qrcode2.innerHTML = "";
      this.qrcode = this.location.href;
      this.qrcode2 = this.location.href;
      // 使用$nextTick确保数据渲染
      this.$nextTick(() => {
        this.crateQrcode("qrcode", this.qrcode);
        this.crateQrcode("qrcode2", this.qrcode2);
      });
    },
    resizeNav(val) {
      this.top = val.scrollTop || this.new_scrollTop;
      let top = val.scrollTop || this.new_scrollTop;
      if (top > 200) {
        let c_h = document.documentElement.clientWidth;
        this.con_width = this.$refs.content.offsetWidth;
        let h_w =
          c_h > 1200
            ? this.con_width + (c_h - this.con_width) / 2 - 40
            : c_h - 20;
        this.headercansee = "";
        this.navStyle = `position: fixed; top: 110px;left:${h_w}px;`;
      } else {
        this.headercansee = "none";
        this.navStyle = "top: 175px;";
      }
    },
    htmlDecode(text) {
      //1.首先动态创建一个容器标签元素，如DIV
      var temp = document.createElement("div");
      //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
      temp.innerHTML = text;
      //3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
      var output = temp.innerText || temp.textContent;
      temp = null;
      return output;
    },
    showLct() {
      this.lctshow = true;
    },
    toggle() {
      // console.log(1);
      this.lctshow = !this.lctshow;
    },
    cellStyle({ columnIndex }) {
      // 给表格第一列设置颜色
      if (columnIndex === 0) {
        return {
          background: "#f6f6f8",
        };
      }
      return {};
    },
    handleError(e) {
      // console.log('---');
      // console.log(e);
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null;
    },
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
    changePlace(val) {
      this.activePlace = val.siteName;
      this.locationData.forEach((item) => {
        if (item.siteName === this.activePlace) {
          this.lng = item.longitude;
          this.lat = item.latitude;
        }
      });
      // console.log(val);
    },
    openCard() {
      window.shell.openPage("https://www.baidu.com");
    },
    check() {
      this.checked = !this.checked;
      window.shell.emit("check-card", this.checked);
    },
    goLink(link) {
      window.shell.openPage(link.linkUrl, true);
    },
    overShow(link) {
      link.fontClass = "portal-primary-color-lv1";
    },
    outHide(link) {
      link.fontClass = "";
    },
    handleOnline(e) {
      // 在线办理跳转服务
      window.shell.openOnlineDeal(
        {
          wid: this.itemWid,
          name: this.itemName,
        },
        e.target
      );
    },
    getPlace(item) {
      this.scrollFlag = true;
      // 导航跳转
      this.activeKey = item.fieldWid;
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
      return function (e) {
        clearTimeout(timer);
        var that = this;
        var args = arguments; //保存此处的arguments，因为setTimeout是全局的，arguments不是防抖函数需要的。
        timer = setTimeout(function () {
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
      for (let n = 0; n < this.data4.length; n++) {
        con_el = document.getElementById(this.data4[n]);
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
              this.activeKey = this.data4[n];
            }
          }
        }
      }
    },
    showEv() {
      // 评价
      if (!this.isLogin) {
        console.log("未登录");
        window.shell.login({
          params: {
            localHref: this.loginUrl,
          },
        });
        return;
      }
      this.timeCommit = true;
      this.eDialog = true;
      this.value1 = null;
      this.value2 = null;
      this.value3 = null;
      this.value4 = null;
      this.value5 = null;
    },
    ecommit(param) {
      this.timeCommit = false;
      console.log(1);
      this.eDialog = false;
      param = {
        appId: this.itemWid,
        serviceManner: this.value1,
        serviceSpeed: this.value2,
        totalSatisfy: this.value3,
        eventIntegrity: this.value4,
        suggest: this.value5,
      };
      window.shell
        .execCardMethod({
          ...this.card,
          method: "evaluation",
          param,
        })
        .then((data) => {
          // console.log(data)
          if (data.data.errcode == 0) {
            // 评价成功调用方法
            window.shell
              .execCardMethod({
                ...this.card,
                method: "evaluationResponse",
                param: {
                  wid: this.itemWid,
                },
              })
              .then((data) => {
                // console.log(data)
                if (data.errcode == 0 && data.errmsg == "请求成功") {
                  if (data.data.errcode == 0 && data.data.errmsg == "success") {
                    this.dataList.appraiseNum = data.data.data.appraiseNum;
                    this.dataList.appraiseMark = data.data.data.appraiseMark;
                    this.value = Number(this.dataList.appraiseMark);
                    // console.log(data)
                    // console.log('666666');
                  }
                  this.$message.success({
                    message: "感谢评价",
                    duration: 3000,
                  });
                } else {
                  this.$message({
                    showClose: false,
                    message: data.data.errmsg,
                    type: "error",
                  });
                }
              });
            // console.log(data)
          } else {
            this.$message({
              showClose: false,
              message: data.data.errmsg,
              type: "error",
            });
          }
        });
    },
    warningTip() {
      window.shell.showMessage({
        type: "warning",
        message: "暂无使用权限，请联系管理员",
      });
    },
    showmore() {
      this.showFlag = !this.showFlag;
    },
    evaluate() {
      // 评价
      // console.log('评价窗口');
    },
    share() {
      // 复制分享链接
      const node = document.createElement("span");
      node.innerText = this.location.href;
      node.style.opacity = 0;
      document.body.appendChild(node);
      window.getSelection().selectAllChildren(node);
      document.execCommand("Copy");
      document.body.removeChild(node);
      this.$message.success({
        message: "复制链接成功",
        duration: 3000,
      });
      // console.log(this.linkUrl1);
    },
    updateData(v1, v2) {
      if (!this.isLogin) {
        console.log("未登录");
        window.shell.login({
          params: {
            localHref: this.loginUrl,
          },
        });
        return;
      }
      window.shell.collectServiceItem({
        id: v1, //事项ID 收藏或取消收藏时用
        operate: v2, // 收藏操作标识 0：取消收藏 1：收藏
      });
    },
    getData(v1, v2, v3) {
      window.shell
        .execCardMethod({
          ...this.card,
          method: "renderData",
          param: {
            appId: v1, //事项ID 收藏或取消收藏时用
            operate: v2, // 收藏操作标识 0：取消收藏 1：收藏
            wid: v3, //wid 为事项ID 必传
          },
        })
        .then((data) => {
          if (data.errcode == 0) {
            if (data.data.serviceItemInfo) {
              this.timeFlag = true;
              // 未登录跳转页面
              this.isLogin = !data.data.hasToLogin;
              // console.log(this.isLogin);
              this.xName = data.data.serviceItemInfo.itemName; // 大标题
              this.xName = this.htmlDecode(this.xName); // 转码
              this.itemName = data.data.itemName;

              // 浏览器标题
              window.shell.setBroswerTitle(this.xName || "");

              let a = data.data.showOnlineButton; // !!!!!!!!!!!!!! 【在线办理】展示的三种样式：可见/隐藏/禁用
              this.isLink = a != 0 ? {} : { visibility: "hidden" }; // 关联服务数量为0时隐藏【在线办理】

              this.isUse =
                a == 1
                  ? {
                      pointerEvents: "none",
                      color: "#c1cbd4!important",
                      background: "#dfe6ec!important",
                    }
                  : {};
              this.isUse2 =
                a == 1
                  ? {
                      pointerEvents: "none",
                      background: "#dfe6ec!important",
                    }
                  : {};
              this.cStyle = a == 1 ? { display: "" } : { display: "none" };

              this.itemWid = data.data.serviceItemWid;
              this.favFlag = data.data.favorite;
              this.detailConfigure = data.data.config.detailConfigure;
              this.colorGroup = data.data.config.colorGroup; // 展示：深色模式、浅色模式
              // this.colorGroup = "light";
              if (this.colorGroup === "deep") {
                console.log("deep");
              } else {
                console.log("light");
              }
              this.pageConfigure = data.data.config.pageConfigure;
              this.mapFlag = data.data.akKey !== "false"; // 地图是否可见
              this.dataList = data.data.serviceItemInfo;
              if (!this.dataList.baseInfos.length) {
                this.emptyFlag = false;
              }
              this.value = Number(this.dataList.appraiseMark);
              this.dataList.baseInfos.forEach((item) => {
                if (!item.fieldValue) {
                  item.fieldValue = "-";
                }
              });
              this.tableData = this.dataList.baseInfos; // 基本信息
              this.data2 = this.dataList.indptModuls; // 咨询电话——办理地点
              if (this.data2.length) {
                this.data2.forEach((it) => {
                  if (it.fieldWid === "DEAL_ADDRESS") {
                    // it.fieldValue = it.fieldValue.filter(
                    //   (item) => item.latitude
                    // );
                    if (it.fieldValue.length) {
                      this.locationData = it.fieldValue;
                      this.activePlace = this.mapFlag
                        ? it.fieldValue[0].siteName
                        : ""; // 地图默认选中第一个地点
                      this.locStyle = this.mapFlag
                        ? ""
                        : "pointer-events: none;";
                      this.lat = it.fieldValue[0].latitude || 0;
                      this.lng = it.fieldValue[0].longitude || 0;
                    }
                  }
                });
              }
              this.data3 = [
                { fieldName: "基本信息", fieldWid: "000000" },
              ].concat(
                this.dataList.indptModuls.filter((it) =>
                  this.itemCanSee(it.fieldValue)
                )
              );
              this.activeKey = this.data3[0].fieldWid;
              this.data3.forEach((it) => {
                this.data4.push(it.fieldWid);
              });
              this.data2.forEach((item) => {
                if (item.fieldWid === "FAQS") {
                  this.showFlagSee = item.fieldValue.length > 5;
                  this.showFlag = item.fieldValue.length > 5;
                  // console.log(item.fieldValue.length) // 6
                  // console.log(this.showFlag) // 5
                }
              });
            } else {
              this.emptyFlag = false;
              this.eName = data.data.itemName;
              window.shell.setBroswerTitle(this.eName || "");
            }
          } else {
            this.$message({
              showClose: false,
              message: "获取事项详情错误",
              type: "error",
            });
          }
        });
    },
  },
  watch: {
    isScroll() {
      var button = document.getElementById("000000");
      button.click();
    },
    checked() {
      console.log("aaa");
    },
  },
  mounted() {
    let pWidth = this.$refs.itemdetails.parentNode.offsetWidth;
    this.iWidth = pWidth >= 1200 ? "1200px" : "100%";
    this.itemdetail_width = this.$refs.itemdetails.offsetWidth;
    this.$nextTick(() => {
      let con_el = document.getElementById("content");
      if (con_el) {
        this.startTop = window.shell.getElementTop(con_el); // content容器起始高度
        // console.log("content容器起始高度: " + this.startTop);
      }
    });
    window.shell.on("onScoll", (ev) => {
      this.isScroll = ev.scrollTop;
      let sc_top = ev.scrollTop; // 滚动条距离顶部的距离
      // console.log(sc_top)
      this.new_scrollTop = sc_top;
      // console.log("滚动条距离顶部的距离: " + sc_top);
      !this.scrollFlag && this.initActive(sc_top);
    });

    let m = this;
    window.onresize = function (val) {
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
    let h1 = window.screen.height; // 屏幕分辨率高度
    let h2 = document.body.offsetHeight; // 网页可见区域高度
    // console.log((h2 - 552) / 2);
    if (h1 <= 768) {
      console.log(1);
      this.dStyle = `margin-top:${(h2 - 552) / 2}px!important;`; // 552 为评价弹框高度
      console.log(this.dStyle);
    } else {
      this.dStyle = "margin-top: 15vh!important;";
    }
    // console.log(123123123);
    this.location = window.shell.getLocation() || {};
    this.location.href = this.location.href.split("&name")[0];
    // url获取
    let aa = window.shell.getUrlParam();
    this.emptyName = (aa && aa.name) || "";
    this.curWid = (aa && aa.wid) || "";

    this.getData("", "", this.curWid);
    window.shell.on("collectServiceItem", (val) => {
      this.favFlag = val.operate ? true : false;
    });
  },
  beforeDestroy() {
    window.shell.off("collectServiceItem");
    window.shell.off("card-scroll");
    window.shell && window.shell.off("check-card");
  },
};
</script>
 
<style lang="less" scoped>
.real {
  height: 36px;
  width: 88px;
  position: absolute;
  left: -1px;
  top: -1px;
  opacity: 0;
}
.s1 {
  cursor: pointer;
  text-align: center;
  height: 30px;
}
.s2 {
  width: 84px;
  height: 84px;
  margin: 2px auto;
  margin-bottom: 8px;
}
.s3 {
  color: #8d8d8d;
  position: relative;
  left: 5px;
  top: 1px;
  text-align: center;
}
.detail-wrapper {
  white-space: pre-wrap;
  word-break: break-all;
}

.greyZxbl {
  opacity: 0;
  position: relative;
  left: 101px;
}
.lname-item {
  cursor: pointer;
  line-height: 22px;
  position: relative;
  left: 22px;
  padding-right: 22px;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s ease-out;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
.fade-enter-to,
.fade-leave {
  opacity: 1;
}
.xx {
  cursor: pointer;
  position: absolute;
  top: 8px;
  right: 8px;
}
.xxx {
  font-size: 36px;
  font-weight: 600;
  color: #fff;
  opacity: 0.7;
}
.t-content {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  img {
    position: relative;
    // top: 40px;
    max-width: calc(100% - 80px);
    max-height: calc(100% - 80px);
  }
}
.img-bg {
  z-index: 2000;
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.7);
  opacity: 1;
}
.evname {
  max-width: 370px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.fh-container {
  // min-width: 1024px;
  left: -15px;
  display: flex;
  position: relative;
  margin: 0 auto;
}
.downIcon {
  margin-right: 3px;
  display: inline-block;
  transform: rotate(180deg);
}
.upIcon {
  margin-right: 3px;
  display: inline-block;
  /* transform: rotate(180deg); */
}
.w1 {
  // font-family: PingFangSC-Medium;
  font-size: 18px;
  letter-spacing: 0;
  text-align: center;
  margin-top: 40px;
  margin-bottom: 12px;
  font-weight: 600;
  /* margin: 0 auto; */
  color: #102645;
}
.w2 {
  // font-family: PingFangSC-Regular;
  font-size: 14px;
  letter-spacing: 0;
  text-align: center;
  color: #707d8f;
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
.target {
  position: absolute;
  top: -80px;
}
.navItem {
  cursor: pointer;
}
.isBox {
  opacity: 1 !important;
}
.anchor-item-active {
  font-weight: 600;
  position: relative;
}
.box {
  border-radius: 2px;
  opacity: 0;
  position: absolute;
  display: inline-block;
  width: 4px;
  height: 10px;
  left: -17px;
  top: 15px;
}
/* .anchor-item-active::before {
  display: block;
  position: absolute;
  content: '';
  width: 6px;
  height: 6px;
} */
.score2,
.countdetail2 {
  position: relative;
  top: 2px;
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
}
.nav-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.nAside {
  position: relative;
  top: 7px;
}
.navbar {
  width: 128px;
  border-left: 1px solid #e7edf1;
  padding-left: 16px;
  position: absolute;
  right: -17px;
  z-index: 100;
  background-color: #fff;
}
.anchor-item {
  width: 110px;
  height: 40px;
  line-height: 40px;
  position: relative;
  top: -8px;
}
.btn {
  width: 84px;
  height: 36px;
  line-height: 0px;
}
.btn-commit  {
  border-color:#ffffff;
   &:hover {
      border-color:#ffffff!important;
   }
}
.star {
  position: relative;
  top: -2px;
}
.eStar > div:not(:last-child) {
  margin-bottom: 16px;
}
.eLeft > div {
  margin-bottom: 20px;
}
.line1 {
  border-top: 1px solid #eee;
  position: relative;
  top: -20px;
  width: calc(100% + 40px);
  left: -20px;
}
.myFav {
  position: relative;
  left: -4px;
  font-size: 14px;
  color: #ffbc00;
}
.lctLink {
  position: relative;
  float: left;
  top: 11px;
  left: 15px;
  opacity: 0;
}
.showmore {
  cursor: pointer;
  width: 100%;
  background: #f9fafb;
  height: 40px;
  text-align: center;
  line-height: 40px;
  margin-top: 15px;
}
#place-icon {
  font-size: 16px;
  margin-right: 8px;
  // top: 1px;
  position: absolute;
  left: -22px;
}
.locationName > div:not(:last-child) {
  margin-bottom: 20px;
}
.lct-btn {
  border: 1px solid #3f83fb;
  background: #ffffff;
  border-radius: 3px;
  // font-family: PingFangSC-Medium;
  font-size: 14px;
  color: #3f83fb;
  letter-spacing: 0;
  width: 100px;
  height: 38px;
  text-align: center;
  line-height: 36px;
  cursor: pointer;
  margin-top: 20px;
}
.tel-item {
  height: 48px;
  border-bottom: 1px dashed #e7edf1;
  line-height: 48px;
  position: relative;
}
.tel-num {
  display: inline-block;
  position: absolute;
  left: 145px;
}
.tel-left {
  width: 130px;
  display: inline-block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.ques-item {
  padding-top: 15px;
  padding-bottom: 15px;
  border-bottom: 1px dashed #e7edf1;
  line-height: 20px;
  font-size: 14px;
}
.ques-item > div {
}
.itemlist > div:last-child {
  border-bottom: 1px solid #e7edf1;
}
.content > div {
  width: 100%;
}
.content > div:nth-child(n + 2) {
  margin-top: 40px;
}
.fname {
  text-indent: 13.3%;
  width: 120px;
  /* height: 48px; */
  background: pink;
  background: #f6f6f8;
  border-top: 1px solid #e7edf1;
  border-left: 1px solid #e7edf1;
  line-height: 48px;
}
.fvalue {
  text-indent: 16px;
  width: 929px;
  line-height: 48px;
  background: skyblue;
  /* height: 48px; */
  background: #ffffff;
  border-top: 1px solid #e7edf1;
  border-left: 1px solid #e7edf1;
  border-right: 1px solid #e7edf1;
}
.title1 {
  border-bottom: 1px solid #e7edf1;
  height: 50px;
  // font-family: PingFangSC-Medium;
  font-size: 18px;
  color: #102645;
  /* border:1px solid red; */
  line-height: 50px;
  /* margin-bottom: 13px; */
  font-weight: 600;
}
.itemdetails {
  width: 1200px;
  // width: 100%;
  margin: 0 auto;
  position: relative;
}
.header {
  display: flex;
  height: 150px;
  overflow: hidden; /** pic的margin-top时，可不影响父容器移动 */
}
.h-container {
  display: flex;
  /* background: lightgray; */
  margin-left: 1px;
  /* margin-top: 86px; */
  height: 78px;
  // overflow: hidden;
  width: 100%;
  position: relative;
}
.content {
  /* background: skyblue; */
  margin-top: 20px;
  // width: 1050px;
  /* border: 1px solid red; */
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
.pic {
  margin-right: 30px;
  width: 76px;
  height: 76px;
  border-radius: 10px;
  border-radius: 10px;
  overflow: hidden;
}
.title {
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
}
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
.evaluate {
  top: 5px;
  display: flex;
  margin-top: 10px;
  // font-family: PingFangSC-Regular;
  color: #ffffff;
  position: relative;
}
.score {
  margin-right: 15px;
}
.title-evaluate {
  /* margin-right: 489px; */
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.btn-group {
  display: flex;
  margin-top: 42px;
  /* background: orange; */
  // width: 388px;
  height: 36px;
  position: absolute;
  right: 20px;
}
.bg2 {
  bottom: 22px;
}
.bbb {
  background: #ffffff !important;
  color: #637389 !important;
  border: 1px solid #acb4bc !important;
}
.btn-group > div {
  width: 88px;
  height: 36px;
  border-radius: 3px;
  // font-family: PingFangSC-Medium;
  font-size: 14px;
  letter-spacing: 0;
  text-align: center;
  line-height: 36px;
  cursor: pointer;
}
.zxbl-light {
  background: #ffffff;
  box-shadow: 0 2px 8px 0 rgba(16, 38, 69, 0.4);
}
.zxbl-deep {
  color: #fff;
}
.pj-h {
  /* border: 1px solid red; */
  left: 100px;
}
.sc-h {
  /* border: 1px solid red; */
  left: 200px;
}
.ysc-h {
  /* border: 1px solid red; */
  left: 200px;
}
.fx-h {
  /* border: 1px solid red; */
  left: 300px;
}
.lct-h {
  cursor: pointer;
  /* border: 1px solid red; */
  width: 100px;
  height: 38px;
  border-radius: 3px;
  position: absolute;
  height: 38px;
  width: 100px;
  border-radius: 3px;
  bottom: 0;
}

#zxbl2 {
  color: #ffffff !important;
}
.ccc {
  border: 1px solid;
}
.cc {
  background: rgba(255, 255, 255, 0.2);
  color: #ffffff;
  border: 1px solid #ffffff;
}
.cc:hover {
  background: rgba(255, 255, 255, 0.4);
  color: #ffffff;
  border: 1px solid #ffffff;
}
.btn-group > div:not(:last-child) {
  margin-right: 12px;
}
.link-icon-font {
  background-color: transparent;
  border-radius: 50%;
  font-size: 64px;
}

.link-text {
  margin-top: 10px;
  height: 35px;
  font-size: 13px;
  text-overflow: ellipsis;
  word-break: break-all;
  overflow: hidden;
  white-space: nowrap;
  text-align: center;
}

.link-div {
  display: flex;
  justify-content: center;
  align-items: center;
  color: #9aa1b0;
}

.link-inner-div {
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

/deep/.we-table__body {
  width: 100% !important;
}
/deep/.we-dialog__wrapper {
  overflow: hidden !important;
}
/deep/.we-dialog {
  margin-top: 5vh !important;
}
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

<style lang="less">
.myPopover {
  min-width: 118px;
  height: 172px;
}
</style>