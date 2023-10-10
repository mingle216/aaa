<template>
  <div class="dealShare" v-if="inited">
    <div v-if="loginUser && scheduleInfo">
      <!-- shareState  0:初始状态  1：已接收  2：已拒绝   3：已失效 -->
      <div class="dealShare-undeal" v-if="scheduleInfo.shareState == 0">
        <img width="365px" src="../assets/images/shareSchedule.png" />
        <div class="dealShare__block">
          <div class="dealShare__title">
            {{ scheduleInfo.title }}
          </div>
          <span class="dealShare__time portal-font-color-lv3">
            <i
              class="we-icon-time portal-font-color-lv4"
              style="margin-right: 4px"
            ></i
            >{{ scheduleInfo.startTime }}
            <template v-if="scheduleInfo.endTime">
              - {{ scheduleInfo.endTime }}</template
            >
          </span>
          <div class="dealShare__info portal-font-color-lv2">
            {{ scheduleInfo.content }}
          </div>
          <div class="dealShare__footer">
            <div
              class="dealShare__btn dealShare__btn-reject portal-primary-border-color-hover-lv1 portal-primary-color-hover-lv1"
              @click="handleOk(2)"
            >
              {{ $Lan("public", "reject", "拒 收") }}
            </div>
            <div
              class="dealShare__btn dealShare__btn-accept portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
              @click="handleOk(1)"
            >
              {{ $Lan("public", "accept", "接 收") }}
            </div>
          </div>
        </div>
      </div>
      <div class="dealShare-deal" v-else-if="scheduleInfo.shareState == 1">
        <img src="../assets/images/shareAccept.png" />
        <div class="dealShare__status portal-font-color-lv3">
          {{ $Lan("public", "acceptSchedule", "此日程已接受") }}
        </div>
      </div>
      <div class="dealShare-deal" v-else-if="scheduleInfo.shareState == 2">
        <img src="../assets/images/shareRefused.png" />
        <div class="dealShare__status portal-font-color-lv3">
          {{ $Lan("public", "rejectSchedule", "此日程已拒绝") }}
        </div>
      </div>
      <div class="dealShare-deal" v-else>
        <img src="../assets/images/shareExpired.png" />
        <div class="dealShare__status portal-font-color-lv3">
          {{ $Lan("public", "expiredSchedule", "此日程已失效") }}
        </div>
      </div>
    </div>
    <div class="dealShare-deal" v-if="!loginUser">
      <img src="../assets/images/noLogin.png" />
      <p class="dealShare__login portal-primary-color-lv1" @click="goLogin">
        {{ $Lan("public", "signIn", "请登录") }}
      </p>
      <p
        class="portal-font-color-lv3"
        style="font-size: 12px; line-height: 20px; margin-top: 2px"
      >
        {{ $Lan("public", "noLoginTip", "登录后可查看被分享的日程") }}
      </p>
    </div>
  </div>
</template>

<script>
import {
  getThemeData,
  getLoginUser,
  getShareSchedule,
  handleShareSchedule,
} from "../api/service";
import globalObj from "../utils/global";
import blackAndWhiteMode from "../libs/mixins/blackAndWhiteMode.js";
export default {
  mixins: [blackAndWhiteMode],
  data() {
    return {
      loading: false,
      loginUser: null,
      scheduleInfo: null,
      inited: false,
    };
  },
  methods: {
    async getThemeData() {
      const [res] = await getThemeData();
      if (res.errcode === "0") {
        const data = res.data;
        globalObj.setThemeColor(document, data);
      }
    },
    async fetchShareSchedule() {
      const [res] = await getShareSchedule({
        wid: this.scheduleId,
      });
      if (res.errcode === "0") {
        this.scheduleInfo = res.data || {};
      }
    },
    async handleOk(flag) {
      if (this.loading) {
        return;
      }
      this.loading = true;
      const [res] = await handleShareSchedule({
        eventWid: this.scheduleId,
        shareState: flag,
      });
      this.loading = false;
      if (res.errcode === "0") {
        this.fetchShareSchedule();
      }
    },
    goLogin() {
      globalObj.login({
        params: {
          localHref: this.$route.path,
        },
      });
    },
  },
  async created() {
    this.getThemeData();
    const [res] = await getLoginUser();
    this.inited = true;
    if (res.errcode === "0") {
      this.loginUser = res.data;
      await globalObj.getStaticData({
        languageKey: this.loginUser?.preferredLanguage || "zh_CN",
        modelKey: "casp-portal",
      });
      this.scheduleId = this.$route.params && this.$route.params.id;
      if (this.scheduleId) {
        await this.fetchShareSchedule();
      }
    }
  },
};
</script>

<style scoped>
.dealShare {
  width: 100%;
  height: 100%;
  background: #fff;
  padding-top: 90px;
}
.dealShare-undeal {
  display: flex;
  align-items: center;
  justify-content: center;
}
.dealShare-deal {
  display: flex;
  align-items: center;
  flex-direction: column;
}
.dealShare__block {
  width: 370px;
  margin-left: 36px;
  align-self: start;
}
.dealShare__header {
  display: flex;
}
.dealShare__title {
  font-weight: bold;
  font-size: 18px;
  line-height: 26px;
  margin-bottom: 8px;
}
.dealShare__time {
  border-radius: 4px;
  background: #f5f5f5;
  padding: 3px 8px;
  font-size: 12px;
  line-height: 20px;
  font-weight: normal;
}
.dealShare__info {
  font-size: 14px;
  line-height: 24px;
  margin-top: 12px;
}
.dealShare__footer {
  margin-top: 16px;
  display: flex;
}
.dealShare__btn {
  padding: 7px 20px;
  border-radius: 4px;
  line-height: 22px;
  border: 1px solid;
  cursor: pointer;
}
.dealShare__btn-reject {
  border-color: #d9d9d9;
}
.dealShare__btn-accept {
  color: #fff;
  margin-left: 8px;
}
.dealShare__status {
  margin-top: 4px;
  line-height: 22px;
}
.dealShare__login {
  margin-top: 6px;
  font-size: 16px;
  line-height: 24px;
  cursor: pointer;
}
</style>
