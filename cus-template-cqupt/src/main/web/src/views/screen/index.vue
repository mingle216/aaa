<template>
  <div class="screen-page">
    <top-header class="screen-header" :LanFunName="LanFunName"></top-header>
    <div class="screen-content">
      <div class="content-left">
        <div>
          <div class="box-title">
            <span>{{ $Lan(LanFunName, "applicationVisit", "应用访问与服务用户") }}</span>
            <img src="../../assets/screen/rect.png" alt="" />
          </div>
          <application-visit
            :LanFunName="LanFunName"
            :startDay="startDay"
            :endDay="endDay"
          ></application-visit>
        </div>
        <div>
          <div class="box-title">
            <span>{{ $Lan(LanFunName, "messagePlatform", "消息平台推送分析") }}</span>
            <img src="../../assets/screen/rect.png" alt="" />
          </div>
          <message-platform
            :LanFunName="LanFunName"
            class="content"
            :otherCount="otherCount"
          ></message-platform>
        </div>
        <div>
          <div class="box-title">
            <span>{{ $Lan(LanFunName, "browserAnalysis", "浏览器分析") }}</span>
            <img src="../../assets/screen/rect.png" alt="" />
          </div>
          <browser-analysis
            :LanFunName="LanFunName"
            class="content"
            :otherCount="otherCount"
          ></browser-analysis>
        </div>
      </div>
      <div class="content-center">
        <div>
          <div class="center-top">
            <div class="main-left">
              <visit-statistics
                :LanFunName="LanFunName"
                :portalLogin="portalLogin"
                :otherCount="otherCount"
                :startDay="startDay"
                :endDay="endDay"
              ></visit-statistics>
            </div>
            <div class="main-right">
              <service-count
                :LanFunName="LanFunName"
                :tdcCount="tdcCount"
              ></service-count>
            </div>
          </div>
          <div class="center-bottom">
            <div class="box-title">
              <span
                >{{ currentMonth
                }}{{ $Lan(LanFunName, "hotAppsMounth", "月热门应用") }}</span
              >
              <img src="../../assets/screen/rect2.png" alt="" />
            </div>
            <hot-apps
              :LanFunName="LanFunName"
              class="content"
              :serviceList="serviceList"
            ></hot-apps>
          </div>
        </div>
        <div class="center-lines">
          <div>
            <div class="box-title">
              <span class="cursor">{{
                $Lan(LanFunName, "deptApplications", "部门应用建设情况")
              }}</span>
              <img src="../../assets/screen/rect.png" alt="" />
            </div>
            <dept-applications
              :LanFunName="LanFunName"
              class="content"
              :otherCount="otherCount"
            ></dept-applications>
          </div>
          <div>
            <div class="box-title">
              <span>{{ $Lan(LanFunName, "serviceScene", "服务场景应用建设情况") }}</span>
              <img src="../../assets/screen/rect.png" alt="" />
            </div>
            <service-scene
              :LanFunName="LanFunName"
              class="content"
              :otherCount="otherCount"
            ></service-scene>
          </div>
        </div>
      </div>
      <div class="content-right">
        <div>
          <div class="box-title">
            <span>{{ $Lan(LanFunName, "reviewSpeed", "平均审核速度最快TOP5") }}</span>
            <img src="../../assets/screen/rect.png" alt="" />
          </div>
          <review-speed :LanFunName="LanFunName" class="content"></review-speed>
        </div>
        <div>
          <div class="box-title">
            <span>{{ $Lan(LanFunName, "hallDealNumber", "大厅办件量月走势") }}</span>
            <img src="../../assets/screen/rect.png" alt="" />
          </div>
          <hall-number
            :LanFunName="LanFunName"
            :startDay="startDay"
            :endDay="endDay"
          ></hall-number>
        </div>
        <div>
          <div class="box-title">
            <span>{{ $Lan(LanFunName, "departDealNumber", "各部门办件量TOP5") }}</span>
            <img src="../../assets/screen/rect.png" alt="" />
          </div>
          <depart-numbers
            :LanFunName="LanFunName"
            class="content"
            :otherCount="otherCount"
          ></depart-numbers>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import topHeader from "../../components/screen/Header";
import applicationVisit from "../../components/screen/ApplicationVisit";
import MessagePlatform from "../../components/screen/MessagePlatform";
import reviewSpeed from "../../components/screen/ReviewSpeed";
import BrowserAnalysis from "../../components/screen/BrowserAnalysis";
import VisitStatistics from "../../components/screen/VisitStatistics";
import ServiceCount from "../../components/screen/ServiceCount";
import HotApps from "../../components/screen/HotApps";
import DeptApplications from "../../components/screen/DeptApplications";
import ServiceScene from "../../components/screen/ServiceScene";
import DepartNumbers from "../../components/screen/DepartNumbers";
import HallNumber from "../../components/screen/HallNumber";
export default {
  name: "screen",
  components: {
    topHeader,
    applicationVisit,
    reviewSpeed,
    MessagePlatform,
    BrowserAnalysis,
    VisitStatistics,
    ServiceCount,
    HotApps,
    DeptApplications,
    ServiceScene,
    DepartNumbers,
    HallNumber,
  },
  computed: {
    startDay() {
      return this.getMonth("s", 0);
    },
    endDay() {
      return this.getMonth("e", 0);
    },
  },
  data() {
    return {
      LanFunName: "CUS_TEMPLATE_CQUPT",
      otherCount: {},
      portalLogin: {},
      serviceList: [],
      currentMonth: new Date().getMonth() + 1,
      tdcCount: {},
    };
  },
  created() {
    this.getServiceVisits();
    this.getPortalOtherCount();
    this.getPortalLogin();
    this.getTdcCountDto();
  },
  methods: {
    getPortalOtherCount() {
      let date = new Date();
      let y = date.getFullYear();
      let m = (date.getMonth() + 1).toString().padStart(2, "0");
      let d = date.getDate().toString().padStart(2, "0");
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_DP",
          cardWid: "5439364064588545",
          method: "getPortalOtherCount",
          param: {
            startTime: `2023-01-01 00:00:00`,
            endTime: `${y}-${m}-${d} 23:59:59`,
          },
        })
        .then((res) => {
          if (res.errcode === "0") {
            this.otherCount = res.data.data || {};
          }
        });
    },
    getPortalLogin() {
      let date = new Date();
      let y = date.getFullYear();
      let m = (date.getMonth() + 1).toString().padStart(2, "0");
      let d = date.getDate().toString().padStart(2, "0");
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_DP",
          cardWid: "5439364064588545",
          method: "getPortalLogin",
          param: {
            startTime: `2023010100`,
            endTime: `${y}${m}${d}24`,
          },
        })
        .then((res) => {
          if (res.errcode === "0") {
            this.portalLogin = res.data.data || {};
          }
        });
    },
    getServiceVisits() {
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_DP",
          cardWid: "5439364064588545",
          method: "getServiceVisits",
          param: {
            startTime: `${this.startDay} 00:00:00`,
            endTime: `${this.endDay} 23:59:59`,
          },
        })
        .then((res) => {
          if (res.errcode === "0") {
            this.serviceList = res.data.data || [];
          }
        });
    },
    getTdcCountDto() {
      let date = new Date();
      let y = date.getFullYear();
      let m = (date.getMonth() + 1).toString().padStart(2, "0");
      let d = date.getDate().toString().padStart(2, "0");
      window.shell
        .execCardMethod({
          cardId: "CUS_CARD_DP",
          cardWid: "5439364064588545",
          method: "getTdcCountDto",
          param: {
            startTime: `2023-01-01 00:00:00`,
            endTime: `${y}-${m}-${d} 23:59:59`,
          },
        })
        .then((res) => {
          if (res.errcode === "0") {
            this.tdcCount = res.data.data || {};
          }
        });
    },
    getMonth(type, months) {
      var d = new Date();
      var year = d.getFullYear();
      var month = d.getMonth() + 1;
      if (Math.abs(months) > 12) {
        months = months % 12;
      }
      if (months != 0) {
        if (month + months > 12) {
          year++;
          month = (month + months) % 12;
        } else if (month + months < 1) {
          year--;
          month = 12 + month + months;
        } else {
          month = month + months;
        }
      }
      month = month < 10 ? "0" + month : month;
      // var date = d.getDate();
      var firstday = year + "-" + month + "-" + "01";
      var lastday = "";
      if (
        month == "01" ||
        month == "03" ||
        month == "05" ||
        month == "07" ||
        month == "08" ||
        month == "10" ||
        month == "12"
      ) {
        lastday = year + "-" + month + "-" + 31;
      } else if (month == "02") {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0)) {
          lastday = year + "-" + month + "-" + 29;
        } else {
          lastday = year + "-" + month + "-" + 28;
        }
      } else {
        lastday = year + "-" + month + "-" + 30;
      }
      var day = "";
      if (type == "s") {
        day = firstday;
      } else {
        day = lastday;
      }
      return day;
    },
  },
};
</script>
<style lang="less" scoped>
.screen-page {
  height: 100%;
  color: #fff;
  background: url(../../assets/screen/bg.png) no-repeat center / cover;
  min-height: 800px;
  .screen-header {
    height: 78px;
    background: url(../../assets/screen/header.png) no-repeat top center / 100% 100%;
  }

  .screen-content {
    height: calc(100% - 78px);
    padding: 40px;
    display: flex;

    .content-left {
      width: 26%;
      & > div {
        height: calc(100% / 3 - 16px * 2 / 3);
        background: rgba(0, 0, 0, 0.3);
        mix-blend-mode: normal;
        border-radius: 16px;
        overflow: hidden;
        &:not(:last-of-type) {
          margin-bottom: 16px;
        }
        padding: 20px;
        display: flex;
        flex-direction: column;
        position: relative;
        .content {
          height: calc(100% - 20px);
          // flex: 1;
        }
      }
    }

    .content-center {
      flex: 1;
      margin: 0 16px;
      display: flex;
      flex-direction: column;
      max-width: calc(100% - 26% - 26% - 32px);
      & > div:not(.center-lines) {
        background: rgba(0, 0, 0, 0.3);
        mix-blend-mode: normal;
        border-radius: 16px;
        padding: 20px;
        display: flex;
        flex-direction: column;
        position: relative;
        .content {
          height: calc(100% - 20px);
        }
        &:nth-of-type(1) {
          flex: 1;
          margin-bottom: 16px;
          height: calc(100% / 3 - 16px * 2 / 3 * 2);
        }
      }
      .center-lines {
        height: calc(100% / 3 - 16px * 2 / 3);
        display: flex;
        & > div {
          background: rgba(0, 0, 0, 0.3);
          mix-blend-mode: normal;
          border-radius: 16px;
          width: calc(50% - 16px / 2);
          padding: 20px;
          display: flex;
          flex-direction: column;
          position: relative;
          .content {
            height: calc(100% - 20px);
          }
          &:first-of-type {
            margin-right: 16px;
          }
        }
      }
      .center-top {
        border-bottom: 1px dashed rgba(255, 255, 255, 0.16);
        display: flex;
        padding-bottom: 20px;

        .main-left {
          flex: 1;
          margin-right: 16px;
          // background: radial-gradient(
          //   100% 100% at 50% 50%,
          //   rgba(0, 212, 255, 0.36) 0%,
          //   rgba(0, 212, 255, 0.0001) 49.36%,
          //   rgba(1, 252, 180, 0.0001) 100%
          // );
          background: url(../../assets/screen/Rectangle.png) no-repeat;
          background-size: 100% 100%;
        }

        .main-right {
          // width: 17rem;
          height: 100%;
        }
      }
      .center-top,
      .center-bottom {
        height: 50%;
        position: relative;
      }

      .center-bottom {
        // padding: 20px;
        padding-top: 20px;
        display: flex;
        flex-direction: column;
        position: relative;
        .content {
          height: calc(100% - 20px);
        }
      }
    }

    .content-right {
      width: 26%;
      height: 100%;
      & > div {
        height: calc(100% / 3 - 16px * 2 / 3);
        background: rgba(0, 0, 0, 0.3);
        mix-blend-mode: normal;
        border-radius: 16px;
        padding: 20px;
        display: flex;
        flex-direction: column;
        position: relative;
        .content {
          height: calc(100% - 20px);
        }
        &:not(:last-of-type) {
          margin-bottom: 16px;
        }
      }
    }
  }

  .box-title {
    font-weight: 700;
    font-size: 16px;
    line-height: 20px;
    display: flex;
    align-items: center;
    span {
      max-width: 80%;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      -o-text-overflow: ellipsis;
      margin-right: 8px;
    }
  }
}
</style>
