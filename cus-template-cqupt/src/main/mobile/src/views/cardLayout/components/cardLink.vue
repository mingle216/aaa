<template>
  <div class="cardLink" v-show="isshow">
    <ul @click="show = true" class="moreBtn" v-if="cardLinkData.length > 1">
      <li v-for="index in 3" :key="index"></li>
    </ul>
    <div
      @click="open(cardLinkData[0].linkUrl)"
      v-else
      class="single portal-font-color-lv2 align-center"
    >
      <p class="ellipsis">{{ linkTitle(cardLinkData[0]) }}</p>
      <i class="iconfont cardLink-icon" :class="cardLinkData[0].linkIcon"></i>
    </div>
    <we-popup
      v-model="show"
      position="bottom"
      get-container="body"
      class="cardLink-pop"
      :overlay-style="{ opacity: 0.5, background: 'rgba(0, 0, 0, 0.6)',animation:'none' }"
      ><ul class="portal-card-btn">
        <li
          v-for="(btn, index) in cardLinkData"
          :key="`${btn.linkUrl}_${index}`"
          @click="open(btn.linkUrl)"
          class="portal-card-btn-item portal-font-color-lv1"
        >
          <span class="align-center">
            <i class="iconfont cardLink-icon" :class="btn.linkIcon"></i
            >{{ linkTitle(btn) }}</span
          >
        </li>
        <li class="portal-font-color-lv2" @click="show = false">
          {{ $Lan('SYS_TEMPLATE_OFFICIAL_h5', 'cancel', '取消') }}
        </li>
      </ul></we-popup
    >
  </div>
</template>

<script>
/* eslint-disable no-debugger */

export default {
  name: "cardLink",
  props: {
    cardData: Object,
    cardLinkData: Array,
    isshow: Boolean,
  },
  data() {
    return {
      show: false,
    };
  },
  computed: {
    linkTitle() {
      return (btn) => {
        const zhTitle = btn.linkTitle || ''
        const linkLang = btn.linkLang || []
        console.log(linkLang)
        const temp = linkLang.find(el => el.langCode === this.$i18n.locale)
        return temp && temp.langName || zhTitle
      }
    }
  },
  methods: {
    open(url) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.cardData.cardWid,
          cardId: this.cardData.cardId,
          cardName: this.cardData.cardName
        },
        startTime: new Date().getTime()
      });
      document.activeElement.blur();
      this.asModel = false;
      window.shell.openPage(url, 1, 2);
    },
    openMenus() {
      this.asModel = true;
    },
  },
};
</script>

<style scoped lang='less'>
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.cardname{
  font-size: 19px;
}
.align-center {
  display: flex;
  align-items: center;
}
.cardLink-icon {
  font-size: 14px;
  margin-right: 6px;
}
.cardLink-pop {
  border-radius: 10px 10px 0 0;

  .portal-card-btn {
    border-radius: 10px 10px 0 0;
    background-color: #f6f6f8;
    > li {
      height: 50px;
      line-height: 50px;
      text-align: center;
      background-color: white;
      border-bottom: 1px solid #e7edf1;
      font-size: 16px;
      display: flex;
      justify-content: space-around;
      // > i {
      //   vertical-align: middle;
      // }
    }
    > li:nth-last-of-type(2) {
      border: none;
    }
    > li:last-child {
      margin-top: 6px;
      border: none;
    }
  }
}
.cardLink {
  height: 30px;
  display: flex;
  justify-content: flex-end;

  .moreBtn {
    .align-center;
    height: 100%;
    > li {
      height: 5px;
      width: 5px;
      border-radius: 50%;
      background-color: #acb4be;
      margin-right: 3px;
    }
  }
  .single {
    font-size: 14px;
    justify-content: flex-end;
    > p {
      max-width: 70px;
    }
  }
  .portal-card-btn-item:focus {
    outline: none;
  }
  li:focus {
    outline: none;
  }
}
</style>
