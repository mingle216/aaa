<template>
  <div class="service-phone">
    <!--咨询电话-->
    <ul class="service-phone-content">
      <li
        v-for="(oPhone, key2) in data"
        :key="key2"
        class="service-phone-content-item"
      >
        <img :src="iconPhone" alt class="icon" />
        <div class="service-phone-content-item-telephone">
          <a @click="callPhone(oPhone.phone)" class="portal-primary-color-lv1">{{
            oPhone.phone
          }}</a>
        </div>
        <div class="service-phone-content-item-name">{{ oPhone.comments }}</div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
    data: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
  data() {
    return {
      iconPhone: require("../.././public/img/icon-phone.png"),
    };
  },
  methods: {
    callPhone(number) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      window.parent.location.href = `tel://${number}`;
    },
  },
};
</script>

<style lang="less" scoped>
.service-phone {
  &-content {
    margin-top: 12px;

    &-item {
      display: flex;
      align-items: center;
      padding: 18px 0;
      box-shadow: inset 0 -1px 0 #f1f2f4;
      font-size: 14px;
      .icon {
        width: 16px;
        // height:18px;
        margin-right: 8px;
      }
      &-telephone {
        word-break: break-all;
        width: 117px;
        flex-shrink: 0;
      }

      &-name {
        word-break: break-all;
        margin-left: 12px;
      }
    }
  }
}
</style>