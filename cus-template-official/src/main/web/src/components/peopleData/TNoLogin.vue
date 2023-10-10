<template>
  <div class="card__personalData__login">
    <img class="card__personalData__login-img" :src="noLogin" alt="" />
    <div>
      <p
        class="card__personalData__login-btn portal-primary-color-lv1"
        @click="goLogin"
      >
        {{ $Lan(lanFunName, "pleaseLogin", "请登录") }}
      </p>
      <p>
        {{ $Lan(lanFunName, "noLoginWarning", "登录后可查看个人数据内容") }}
      </p>
    </div>
  </div>
</template>

<script>
import noLogin from "../../assets/noLogin.png";
// import { clickTrack } from '../../mixins/track.js';
export default {
  props: {
    lanFunName: {
      type: String,
      default: "",
    },
    router: Object,
  },
  // mixins: [clickTrack],
  data() {
    return {
      noLogin,
      // languages: languages[this.$i18n.locale],
    };
  },
  methods: {
    goLogin() {
      //this.handleClickTrack(); //点击埋点
      const page = window.shell.getLocation().hash.replace("#", "");
      window.shell.login({
        params: {
          localHref: page,
        },
      });
    },
  },
};
</script>

<style scoped lang="less">
.card__personalData__login {
  width: 100%;
  min-width: 300px;
  display: flex;
  margin-top: 10px;
  align-items: center;
  justify-content: center;
  .card__personalData__login-img {
    width: 180px;
    margin-right: 30px;
  }
  .card__personalData__login-btn {
    font-size: 18px;
    cursor: pointer;
    font-weight: 600;
    margin-bottom: 8px;
  }
}
</style>
