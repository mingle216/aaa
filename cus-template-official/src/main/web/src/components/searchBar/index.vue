<template>
  <div class="search__bar"
    :class="getpageName == '' || getpageName.indexOf('home') != -1 || getpageName.indexOf('grzx') != -1 ? '' : 'newactive'">
    <!-- :class="[showBreadCrumb ? 'flex-start' : '']" -->
    <div class="section">
      <div class="title text__ellipsis">
        <span :title="title">{{ title }}</span>
      </div>
      <div class="pos-relative" v-if="showSearch">
        <div class="search__btn__wrap">
          <we-input ref="InputKeyword" size="large" v-model="keyword" :placeholder="placeholderEllipsis"
            :maxlength="100" @focus="handleFocus" @blur="handleBlur" @input="handleInput" @keyup.native="handleKeyUp"
            style="width: 530px" />
          <div class="search__btn portal-primary-backgroundcolor-lv1 portal-primary-backgroundcolor-hover-lv2"
            @click="handleSearch">
            {{ $Lan('SYS_TEMPLATE_OFFICIAL', 'search', '搜索') }}
          </div>
        </div>
        <!-- 搜索结果/历史搜索下拉面板 -->
        <div class="search__dropdown portal-font-color-lv1" v-show="dropdownShow"
          :class="{ 'search__dropdown-show': dropdownShow }">
          <vue-scroll>
            <div class="search__dropdown__lists">
              <history-dropdown v-if="dropdownType === 'HistoryDropdown'" :historyData="hisDataDropdown"
                @change-search="handleChangeSearch" @clear-his="handleClearHis"></history-dropdown>
              <associative-dropdown v-else ref="AssociateDown" :keyword="searchKeyword"></associative-dropdown>
            </div>
          </vue-scroll>
        </div>
        <!-- 历史搜索 -->
        <history-list :historyData="hisDataLists" v-if="showHistory" @change-search="handleChangeSearch" />
      </div>
    </div>
    <div class="dzdbyb" v-show="isLogin && getpageName.indexOf('hall') != -1">
      <div>
        <img src="../../assets/images/wdbj1.png">
        <p>
          <we-tooltip class="item" effect="dark" content="您在办事大厅中发起的，并处于流转过程中的流程" placement="top">
            <span class="dzName">进行中办件</span>
          </we-tooltip>
          <span class="dznum" style="color: #2656F0;font-weight: 700;">{{ dataList.doing }}<span
              class="dw portal-font-color-lv1">/项</span></span>
        </p>
        <p>
          <we-tooltip class="item" effect="dark" content="您在办事大厅中发起的，并已经流转结束的流程" placement="top">
            <span class="dzName">已完成办件</span>
          </we-tooltip>
          <span class="dznum">{{ dataList.complete }}<span class="dw portal-font-color-lv1">/项</span></span>
        </p>
      </div>
      <div>
        <img src="../../assets/images/wdbj2.png">
        <p @click="openPage(lists[2])">
          <we-tooltip class="item" effect="dark" content="待您处理的任务" placement="top">
            <span class="dzName">待办任务</span>
          </we-tooltip>
          <span class="dznum" style="color: #FF7000;font-weight: 700;">{{ dataList.todo }}<span
              class="dw portal-font-color-lv1">/项</span></span>
        </p>
        <p @click="openPage(lists[3])">
          <we-tooltip class="item" effect="dark" content="您已经处理过的任务" placement="top">
            <span class="dzName">已办任务</span>
          </we-tooltip>
          <span class="dznum">{{ dataList.done }}<span class="dw portal-font-color-lv1">/项</span></span>
        </p>
      </div>
      <div>
        <img src="../../assets/images/wdbj3.png">
        <p @click="openPage(lists[4])">
          <we-tooltip class="item" effect="dark" content="在事项管理中心中您可维护的事项" placement="top">
            <span class="dzName">管理的事项</span>
          </we-tooltip>
          <span class="dznum" style="color: #1F9D98;font-weight: 700;">{{ dataList.mine }}<span
              class="dw portal-font-color-lv1">/项</span></span>
        </p>
        <p><span class="dzName" style="color:#fff">待审核事项</span><span class="dznum" style="color:#fff">0</span></p>
      </div>
    </div>
    <div class="dzgrxx"
      v-show="isLogin && (getpageName == '' || getpageName.indexOf('home') != -1 || getpageName.indexOf('grzx') != -1)">
      <div class="topimg">
        <div>
          <img @click="openimgurl()" v-if="peopleMsg.headImageIcon != ''" :src="peopleMsg.headImageIcon">
          <img @click="openimgurl()" v-else src="../../assets/images/people.png">
          <div>
            <p class="peopleName">{{ getTimeState }}好，{{ peopleMsg.userName }}！</p>
            <p class="peopleId">{{ zzjg }}</p>
          </div>
        </div>
        <p class="peopleCenter" @click="goPeopleCenter()">个人中心</p>
      </div>
      <div class="bottomList">
        <peopleData :DZcardID="DZcardID" :DZcardName="DZcardName" ></peopleData>
      </div>
    </div>
  </div>
</template>

<script>
import HistoryList from "./HistoryList";
import HistoryDropdown from "./HistoryDropdown";
import AssociativeDropdown from "./AssociativeDropdown";
import peopleData from "../peopleData/index";
export default {
  components: {
    HistoryList,
    HistoryDropdown,
    AssociativeDropdown,
    peopleData
  },
  name: "searchBar",
  props: {
    pageCode: {
      type: String,
      default: "home",
    },
    pageRenderData: {
      type: Object,
      default: () => {
        return {};
      },
    },
    currentUser: Object,
  },
  data() {
    let origin = window.shell.getLocation().origin;
    const currentRoute = window.shell.getCurrentSite();
    // location.origin IE不存在
    if (!origin) {
      origin =
        window.shell.getLocation().protocol +
        "//" +
        window.shell.getLocation().host;
    }
    const host = `${origin}${currentRoute?.siteRoute ? "/" + currentRoute?.siteRoute : ""
      }/index.html`;
    return {
      keyword: "",
      searchKeyword: "", // 联想搜索时的关键词
      hisDataDropdown: [],
      dropdownShow: false,
      dropdownType: "HistoryDropdown",
      timer: null,
      placeholderVal: "", // 搜索默认词
      placeholderValFetch: "", // 搜索默认词, 从接口获取
      loading: false,
      peopleMsg: {},
      dataList: {},
      zzjg: "",
      isLogin: window.shell.getUserInfo() ? true : false,
      lists: [
        {
          key: "doing",
          name: "nameDoing",
          defaultName: "进行中办件",
          tips: "tipDoing",
          defaultTips: "您在办事大厅中发起的，并处于流转过程中的流程",
          count: 0,
          allShow: true,
          canClick: false,
        },
        {
          key: "complete",
          name: "nameComplete",
          defaultName: "已完成办件",
          tips: "tipComplete",
          defaultTips: "您在办事大厅中发起的，并已经流转结束的流程",
          count: 0,
          allShow: true,
          canClick: false,
        },
        {
          key: "todo",
          name: "nameTodo",
          defaultName: "待办任务",
          tips: "tipTodo",
          defaultTips: "待您处理的任务",
          count: 0,
          allShow: true,
          canClick: true,
          href: "/taskCenter/*default/index.do#/todoTask",
          href2: `${host}/#/taskList?taskCardId=SYS_CARD_TODOTASKLIST`,
        },
        {
          key: "done",
          name: "nameDone",
          defaultName: "已办任务",
          tips: "tipDone",
          defaultTips: "您已经处理过的任务",
          count: 0,
          allShow: false,
          canClick: true,
          href: "/taskCenter/*default/index.do#/doneTask",
          href2: `${host}/#/taskList?taskCardId=SYS_CARD_DONETASKLIST`,
        },
        {
          key: "mine",
          name: "nameMine",
          defaultName: "我管理的事项",
          tips: "tipMine",
          defaultTips: "在事项管理中心中您可维护的事项",
          count: 0,
          allShow: false,
          canClick: true,
          href: "",
          photourl:"",
        },
      ],

      pageData: null,
      DZcardID:null,
      DZcardName:null
    };
  },
  created(){
    this.pageData=JSON.parse(JSON.stringify(window.shell.getPageData()))
    this.photourl = this.pageData ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig)).photoUrl[0].langValue : '',
    this.DZcardID=this.pageData ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig)).cardID[0].langValue : null,
    this.DZcardName=this.pageData ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig)).cardName[0].langValue : null
    // console.log('DZcardIDcreated',this.DZcardID,this. DZcardName)
  },
  mounted() {
    // this.pageData=JSON.parse(JSON.stringify(window.shell.getPageData()))
    // this.DZcardID=this.pageData ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig)).cardID[0].langValue : null,
    // this.DZcardName=this.pageData ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig)).cardName[0].langValue : null
    // console.log('DZcardID',this.DZcardID,this. DZcardName)
    window.shell.execCardMethod(
      {
        cardId:  this.DZcardName,
        cardWid: this.DZcardID,
        method: "configuredData",
        param: {
          lang: this.$i18n.locale,
          userAccount: window.shell.getUserInfo().userAccount || ''
        }
      },
      (data) => {
        let arr = data.data.organizationName.split('/')
        this.zzjg = arr[arr.length - 1] == '未分配组织机构' ? data.data.stuNumber : arr[arr.length - 1]
        console.log('configuredData', this.zzjg)
      }
    );
    this.getpeopleImg()
    this.getData()
    // 搜索结果页，默认填充搜索关键词
    this.pageCode === "search" && this.getUrlParams();
  },
  computed: {
    getTimeState() {
      // 获取当前时间
      let timeNow = new Date();
      // 获取当前小时
      let hours = timeNow.getHours();
      // 设置默认文字
      let state = "";
      // 判断当前时间段
      if (hours >= 0 && hours <= 10) {
        state = "早上";
      } else if (hours > 10 && hours <= 14) {
        state = "中午";
      } else if (hours > 14 && hours <= 18) {
        state = "下午";
      } else if (hours > 18 && hours <= 24) {
        state = "晚上";
      }
      return state;
    },
    getUserInfo() {
      console.log("getpageName", window.shell.getUserInfo())
      return window.shell.getUserInfo();

    },
    getpageName() {
      return this.pageRenderData.breadcrumb[0].pageCode;
    },
    pageConfig() {
      return this.pageRenderData.pageConfig || {};
    },
    // 多语言标题
    title() {
      const arr = this.pageConfig["header.titleLangs"] || [];
      const temp = arr.find((el) => el.key === this.$i18n.locale);
      const zhName = arr.find((el) => el.key === 'zh_CN')
      return temp?.value || zhName?.value;
      // return this.pageConfig[`header.title.${this.$i18n.locale}`];
    },
    // 是否显示搜索框
    showSearch() {
      return this.pageCode === "search"
        ? true
        : this.pageConfig["search.display"] === "Y" ||
        this.pageConfig["search.display"] === "";
    },
    // 是否展示面包屑
    showBreadCrumb() {
      return (
        this.pageConfig["breadcrumb.show"] === "Y" &&
        this.pageRenderData.breadcrumb &&
        this.pageRenderData.breadcrumb.length > 1
      );
    },
    placeholder() {
      // 搜索结果页使用其他页面带过来的默认词
      return this.pageCode === "search"
        ? this.placeholderVal
        : this.placeholderValFetch;
    },
    // 折叠展示的placeholder
    placeholderEllipsis() {
      const val = this.placeholder || "";
      const dom = document.createElement("span");
      dom.style.visibility = "hidden";
      dom.style.display = "inline-block";
      dom.textContent = val;
      document.body.appendChild(dom);
      let width = dom.clientWidth;
      let offset = val.length;
      let realText = val;
      if (width > 490) {
        while (width > 490) {
          realText = `${val.slice(0, offset)}...`;
          dom.textContent = realText;
          width = dom.clientWidth;
          offset = offset - 1;
        }
      }
      document.body.removeChild(dom);
      return realText;
    },
    // 未登录或者无历史数据时，不展示历史记录
    showHistory() {
      return (
        this.showSearch &&
        (this.pageConfig["search.history.display"] === "Y" ||
          this.pageConfig["search.history.display"] === "") &&
        this.currentUser &&
        this.hisDataDropdown.length
      );
    },
    // 输入框下的历史搜索数据，最多展示五条
    hisDataLists() {
      return this.hisDataDropdown.length > 5
        ? this.hisDataDropdown.slice(0, 5)
        : this.hisDataDropdown;
    },
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 6000);
      }
    },
    pageConfig: {
      handler(val) {
        if (
          val &&
          (val["search.display"] === "Y" || val["search.display"] === "")
        ) {
          this.getPlaceholderVal();
        }
        this.getSearchHis();
      },
      immediate: true,
    },
  },
  methods: {
    openimgurl(){
      if(this.photourl!=''){
        window.open(this.photourl)
      }
      
    },
    openPage(item) {
      if (!item.canClick || item.count === "-") {
        return;
      }
      console.log(item.href)
      window.shell.openPage(item.href, 1, 2);
    },
    goPeopleCenter() {
      let href = "/grzx"
      window.shell.openPage(href, 1, 1);
    },
    //获取用户照片
    getpeopleImg() {
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_PERSONALDATA",
          cardWid: "1412123987719769",
          method: "getPicInfo",
          param: { "lang": "zh_CN" }
        })
        .then((res) => {
          if (res.data.code == '0') {
            this.peopleMsg = res.data.data
          } else {
            this.$message({
              showClose: false,
              message: res.data.msg,
              type: "error",
            });
          }

        });
    },
    //获取待办已办等数据
    getData() {
      window.shell
        .execCardMethod({
          cardId: "SYS_CARD_STATISTICS",
          cardWid: "7617992939925711",
          method: "renderData",
          param: { "lang": "zh_CN" }
        })
        .then((res) => {
          if (res.errcode == '0') {
            let taskSource = parseInt(res?.data?.taskSource) || 1;
            this.lists = this.lists.map((item) => {
              const count = res.data[item.key] || 0;
              // 任务中心相关的，任务中心地址地址不配置或配置有误的情况下显示 -
              item.count =
                item.key !== "mine"
                  ? res.data.taskCenterUrlIsTrue
                    ? count
                    : "-"
                  : count;
              if (item.canClick) {
                if (taskSource === 1) {
                  item.href =
                    item.key === "mine"
                      ? res.data.url
                      : res.data.taskCenterUrl + item.href;
                } else {
                  item.href = item.key === "mine" ? res.data.url : item.href2;
                }
              }
              return item;
            });
            console.log(1111, this.lists)
            this.dataList = res.data
          } else {
            this.$message({
              showClose: false,
              message: res.msg,
              type: "error",
            });
          }

        });
    },
    getUrlParams() {
      const params = window.shell.getUrlParam();
      if (params && params.searchKey) {
        this.keyword = params.searchKey;
        this.searchKeyword = params.searchKey;
      }
      if (params && params.placeholder) {
        this.placeholderVal = params.placeholder;
      }
    },
    async getPlaceholderVal() {
      if (this.pageCode === "search") {
        return;
      }
      const [res] = await window.shell.getPlaceholderVal({
        wid: this.pageRenderData.wid,
      });
      if (res.errcode === "0" && res.data) {
        this.placeholderValFetch = res.data["search.placeholderVal"];
      } else {
        this.placeholderValFetch = "";
      }
    },
    handleFocus() {
      this.searchKeyword = this.keyword.trim();
      // 有关键词时，展示联想搜索；否则展示历史搜索
      this.dropdownType = this.searchKeyword
        ? "AssociativeDropdown"
        : "HistoryDropdown";
      this.dropdownShow =
        this.dropdownType === "HistoryDropdown" ? this.showHistory : true;
      if (this.searchKeyword) {
        this.$refs.AssociateDown && this.$refs.AssociateDown.handleSearch();
      }
    },
    handleBlur() {
      setTimeout(() => {
        this.dropdownShow = false;
      }, 300);
    },
    handleInput() {
      if (this.timer !== null) clearTimeout(this.timer);
      this.timer = setTimeout(() => {
        this.searchKeyword = this.keyword.trim();
        if (this.searchKeyword) {
          this.dropdownType = "AssociativeDropdown";
        } else {
          this.dropdownType = "HistoryDropdown";
        }
        this.dropdownShow =
          this.dropdownType === "HistoryDropdown" ? this.showHistory : true;
      }, 300);
    },
    handleKeyUp(e) {
      if (e.key === "Enter") {
        //按下回车
        this.searchKeyword = this.keyword.trim();
        this.handleSearch();
        // 失去焦点
        this.$refs.InputKeyword.blur();
      }
    },
    handleSearch() {
      if (this.loading) {
        return;
      }
      // 未输入关键词或者占位符不存在时，不触发搜索
      let keyword = this.searchKeyword || this.placeholder || "";
      keyword = keyword.trim();
      if (!keyword) {
        return;
      }
      if (!this.keyword.trim() && this.placeholder) {
        // 搜索结果页，直接以placeholder搜索时，填充到输入框中
        this.keyword = this.placeholder;
      }
      this.loading = true;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          extraInfo: {
            infoType: 3,
            filterInfo: {
              keyword: keyword
            }
          }
        },
        startTime: new Date().getTime()
      });
      window.shell.execTemplateMethod(
        "globalSearch",
        {
          keywords: keyword,
        },
        () => {
          setTimeout(() => {
            this.loading = false;
          }, 1000);
          const curr = window.shell.getRoute();
          if (curr === "/search") {
            window.shell.emit("update-search-results", keyword);
          }
          const reg = new RegExp("[%\\/?#&=]", "g");
          keyword = keyword.replace(reg, (match) => encodeURIComponent(match));
          const placeholder = this.placeholder.replace(reg, (match) =>
            encodeURIComponent(match)
          );
          window.shell.openPage(
            `search?searchKey=${keyword}&placeholder=${placeholder}`,
            0,
            1
          );
          this.getSearchHis();
        }
      );
    },
    handleChangeSearch(item) {
      this.keyword = item;
      this.searchKeyword = this.keyword.trim();
      this.handleSearch();
    },
    handleClearHis() {
      this.hisDataDropdown = [];
    },
    async getSearchHis() {
      if (
        this.showSearch &&
        (this.pageConfig["search.history.display"] === "Y" ||
          this.pageConfig["search.history.display"] === "") &&
        this.currentUser
      ) {
        const [res] = await window.shell.getSearchHisVal({
          wid: this.pageRenderData.wid,
        });
        if (res.errcode === "0" && res.data) {
          const data = res.data || []
          this.hisDataDropdown = data.length > 10 ? data.slice(0, 10) : data;
        } else {
          this.hisDataDropdown = [];
        }
      }
    },

  },
};
</script>

<style scoped>
.peopleCenter {
  padding: 5px 7px;
  background: #A40000 !important;
  color: #fff;
  border-radius: 5px;
  cursor: pointer;
}

.peopleCenter>a {
  color: #fff;

}

.dzdbyb {
  height: 66px;
  background: #fff;
  box-shadow: 0px 8px 24px rgb(29 29 31 / 12%);
  border-radius: 4px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: absolute;
  bottom: -33px;
}

.dzdbyb>div {
  width: calc((100% - 2px) / 3);
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dzdbyb>div>img {
  height: 28px;
  width: 28px;
  margin-right: 20px;
  ;
}

.dzdbyb>div>p>span:first-child {
  margin-right: 8px;
}

.dzdbyb>div>p {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.dzdbyb>div>p:nth-child(2) {
  margin-right: 20px;
}

.dzdbyb>div:nth-child(2) {
  border-left: 1px solid rgb(29 29 31 / 20%);
  border-right: 1px solid rgb(29 29 31 / 20%);
}

.dzName {
  font-weight: 400;
  font-size: 16px;
  line-height: 20px;
  color: #4A4A4C;
}

.dznum {
  font-weight: 400;
  font-size: 22px;
  line-height: 25px;

  color: #1D1D1F;
}

.dzgrxx {
  height: 222px;
  width: 423px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 4px;
  padding: 20px;
}

.dzdbyb>div>p .dw {
  font-weight: 400;
  font-size: 16px;
  line-height: 20px;
}

.topimg {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.topimg>div:first-child {
  display: flex;
  align-items: center;

}

.peopleName {
  font-weight: 700;
  font-size: 18px;
  line-height: 23px;
  color: #333333;
  margin-bottom: 10px;
  font-family: 'Microsoft YaHei UI';
  font-style: normal;
}

.peopleId {
  font-family: 'Microsoft YaHei UI';
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 18px;
  color: #767B8D;
}

.topimg>div:first-child>img {
  height: 64px;
  width: 52px;
  margin-right: 16px;
}

.search__bar {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.newactive {
  justify-content: center;
}

.section {
  /* position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%); */
  text-align: center;
  z-index: 999;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.flex-start .section {
  transform: translate(-50%, -60%);
}

.section .title {
  font-size: 36px;
  color: #fff;
  margin-bottom: 20px;
  text-align: center;
  max-width: 750px;
}

.search__btn__wrap /deep/.we-input__inner {
  height: 48px;
  line-height: 48px;
}

.search__btn__wrap {
  display: flex;
  align-items: center;
  justify-content: center;
}

.search__btn {
  min-width: 100px;
  padding: 0 20px;
  height: 48px;
  font-size: 16px;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: -2px;
  z-index: 1;
  border-bottom-right-radius: 4px;
  border-top-right-radius: 4px;
  cursor: pointer;
  user-select: none;
}

.pos-relative {
  position: relative;
  width: 630px;
}

.search__dropdown {
  position: absolute;
  box-sizing: border-box;
  top: 55px;
  padding: 14px 0;
  background: #ffffff;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  border-radius: 4px;
  transform: rotateX(90deg);
  transform-origin: 0 0;
  transition: transform 0.3s;
  transition-delay: 0.5s;
  width: 100%;
  text-align: left;
}

.search__dropdown__lists {
  max-height: 450px;
}

.search__dropdown-show {
  transform: rotateX(0deg);
}

/deep/.we-input__inner:focus {
  border-color: transparent;
}
</style>
