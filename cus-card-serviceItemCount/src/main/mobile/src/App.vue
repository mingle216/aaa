
<i18n src="./languages/locales.json"></i18n>
<template>
  <div
    class="serviceItemCount align-center"
    :class="{ 'serviceItemCount-padding': showHtml !== 2 }"
  >
    <ul v-if="showHtml === 3">
      <li
        class="align-center"
        v-for="(item, index) in serviceItemList"
        :key="index"
      >
        <h3 class="portal-font-color-lv1">{{ item.count }}</h3>
        <span class="portal-primary-backgroundcolor-lv1"></span>
        <p class="portal-font-color-lv2 ellipsis">
          {{ langName(item.dataName) }}
        </p>
      </li>
    </ul>
    <empty-con :tip="GetLanguageValue('SYS_CARD_SERVICEITEMCOUNT_h5', 'nodata', '暂无数据')" v-if="showHtml === 2" />
    <!-- 未登录 -->
    <!-- <div
      class="card__serviceItemCount__login align-center"
      v-if="showHtml === 1"
    >
      <div>
        <span
          class="card__serviceItemCount__login-btn portal-primary-color-lv1"
          @click="goLogin"
        >
          {{ $t("login") }}
        </span>
        <p>{{ $t("loginTip") }}</p>
      </div>
    </div> -->
  </div>
</template>

<script>
import TrackMixin from './mixins/track.js';
export default {
  name: "serviceitemcounts",
  props: {
    index: Number,
    router: Object,
  },
  mixins: [TrackMixin],
  data() {
    return {
      checked: false,
      serviceItemList: [],
      isLogin: window.shell.getUserInfo() ? true : false,
      hasData: true,
      config: { heightFlag: 1, height: 0 },
    };
  },
  computed: {
    showHtml() {
      return this.hasData ? (this.serviceItemList.length ? 3 : 2) : 4;
    },
  },
  methods: {
    goLogin() {
      const page = window.shell.getLocation().hash.replace("#", "");
      window.shell.login({
        params: {
          localHref: page,
        },
      });
    },
    langName(item) {
      const temp = item.find(el => el.langKey === this.$i18n.locale)
      const zhName = item.find(el => el.langKey === 'zh_CN')
      const result = temp?.langValue || zhName?.langValue
      // 如果dataId是数字并且name不是中文，则需要对"事项统计"做多语言处理
      const matterName = !isNaN(Number(this.dataId)) && temp?.langValue && (!this.$i18n.locale.includes('zh'))
        ? this.GetLanguageValue(
            'SYS_CARD_SERVICEITEMCOUNT_h5',
            'serviceItemCount',
            '事项统计'
          )
        : ''
      return `${result} ${matterName}`
    },
    check() {
      this.checked = !this.checked;
      window.shell.emit("check-card", this.checked);
    },
    getCardData() {
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "render2",
          param: {},
        },
        (res) => {
          if (res.errcode === "0") {
            const list = res.data.serviceItemList || [];
            this.serviceItemList = list.map((el) => {
              return {
                ...el,
                count: !["onlinePercent", "itemScore"].includes(el.dataId)
                  ? Number(el.count).toLocaleString('en')
                  : el.count,
              };
            });
          }
          this.loadedEndTrack(); // 加载结束埋点
        }
      );
    },
  },
  async created() {
    await this.getCardData();
  },
};
</script>

<style lang="less" scoped>
.align-center {
  display: flex;
  align-items: center;
}
.serviceItemCount-padding {
  padding: 11px 17px;
}
.serviceItemCount {
  width: 100%;

  font-size: 14px;
  min-height: 148px;
  justify-content: space-around;
  .card__serviceItemCount__login {
    height: 100%;
    justify-content: center;
    text-align: center;
    .card__serviceItemCount__login-btn {
      font-size: 16px;
      line-height: 20px;
    }
    p {
      color: #9fa8b5;
      margin-top: 5px;
      line-height: 18px;
    }
  }
  > ul {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    padding: 12px 0;
    background: url("./assest/images/bg.png") no-repeat center/100% 100%;
    > li {
      width: calc(50% - 8.5px);
      text-align: center;
      flex-direction: column;
      letter-spacing: 0;
      padding: 12px 17px;
      > h3 {
        font-size: 26px;
        letter-spacing: -0.76px;
        text-align: center;
        line-height: 30px;
        font-weight: 700;
      }
      > p {
        font-size: 14px;
        line-height: 18px;
        width: 100%;
      }
      > span {
        height: 2px;
        width: 20px;
        margin: 5px 0 8px 0;
        border-radius: 2px;
      }
    }
    > li:nth-of-type(2n + 1) {
      position: relative;
      &::after {
        position: absolute;
        top: 0;
        bottom: 0;
        margin: auto 0;
        right: -9px;
        height: 34.7px;
        border-right: 1px dashed #c8ced6;
        content: "";
      }
    }
    > li:last-child {
      &::after {
        display: none;
      }
    }
  }
}
</style>
