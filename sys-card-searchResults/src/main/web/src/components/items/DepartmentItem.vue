<template>
  <div class="depart">
    <div
      class="depart__flex"
      :class="[
        item.deptDesc ||
        item.deptAddress ||
        (item.deptPhone && item.deptPhone !== '[]')
          ? ''
          : 'onlyTitle',
      ]"
    >
      <div class="depart__left">
        <we-tooltip :open-delay="500" placement="top">
          <div slot="content"><span v-html="name(item.deptName)"></span></div>
          <span
            class="depart__title ellipsis portal-font-color-lv1"
            v-html="name(item.deptName)"
          ></span
        ></we-tooltip>
        <div class="depart__info portal-font-color-lv3" v-if="item.deptDesc">
          <span class="depart__info_label ellipsis">{{
            $Lan(lanFunName, "introduction", "简介")
          }}</span
          >：<span
            class="depart__info_value ellipsis"
            v-html="name(item.deptDesc)"
          ></span>
        </div>
        <div class="depart__info portal-font-color-lv3" v-if="item.deptAddress">
          <span class="depart__info_label ellipsis">{{
            $Lan(lanFunName, "location", "地点")
          }}</span
          >：<span
            class="depart__info_value ellipsis"
            v-html="name(item.deptAddress)"
          ></span>
        </div>
        <!-- 电话 -->
        <div class="depart__info__phone" v-if="item.deptPhone">
          <div
            class="depart__info portal-font-color-lv3"
            v-for="(phone, index) in JSON.parse(item.deptPhone)"
            :key="index"
          >
            <template v-if="phone.comments">
              <span class="depart__info_label ellipsis">{{
                phone.comments
              }}</span
              >：</template
            ><span class="depart__info_value" v-html="name(phone.phone)"></span>
          </div>
        </div>
      </div>
      <div class="depart__right">
        <we-button
          v-if="item.deptLinkUrl"
          class="default-btn"
          :class="[btnClass]"
          plain
          size="medium"
          @click="visitUrl(item)"
        >
          {{ $Lan(lanFunName, "visitUrl", "访问网址") }}
        </we-button>
        <we-tooltip
          :content="$Lan(lanFunName, 'noVisitUrl', '此部门没有配置网址')"
          :open-delay="500"
          placement="top"
          v-else
          ><we-button
            class="default-btn button-disabled portal-font-color-lv4"
            size="medium"
            plain
          >
            {{ $Lan(lanFunName, "visitUrl", "访问网址") }}
          </we-button></we-tooltip
        >
        <we-button
          class="default-btn"
          :class="[btnClass]"
          plain
          size="medium"
          @click="goDetail(item)"
          style="margin-left:8px"
        >
          {{ $Lan(lanFunName, "checkDetail", "查看详情") }}
        </we-button>
      </div>
    </div>
    <!-- 子部门 -->
    <div
      class="depart-child"
      v-for="(child, index) in item.childDept || []"
      :key="index"
      :class="[
        child.deptDesc ||
        child.deptAddress ||
        (child.deptPhone && child.deptPhone !== '[]')
          ? ''
          : 'onlyTitle',
      ]"
    >
      <div class="depart__left">
        <we-tooltip :open-delay="500" placement="top">
          <div slot="content"><span v-html="name(child.deptName)"></span></div>
          <span
            class="depart__title ellipsis portal-font-color-lv1"
            v-html="name(child.deptName)"
          ></span
        ></we-tooltip>
        <div class="depart__info portal-font-color-lv3" v-if="child.deptDesc">
          <span class="depart__info_label ellipsis">{{
            $Lan(lanFunName, "introduction", "简介")
          }}</span
          >：<span
            class="depart__info_value ellipsis"
            v-html="name(child.deptDesc)"
          ></span>
        </div>
        <div
          class="depart__info portal-font-color-lv3"
          v-if="child.deptAddress"
        >
          <span class="depart__info_label ellipsis">{{
            $Lan(lanFunName, "location", "地点")
          }}</span
          >：<span
            class="depart__info_value ellipsis"
            v-html="name(child.deptAddress)"
          ></span>
        </div>
        <!-- 电话 -->
        <div class="depart__info__phone" v-if="child.deptPhone">
          <div
            class="depart__info portal-font-color-lv3"
            v-for="(phone, index) in JSON.parse(child.deptPhone)"
            :key="index"
          >
            <template v-if="phone.comments">
              <span class="depart__info_label ellipsis">{{
                phone.comments
              }}</span
              >：</template
            ><span class="depart__info_value" v-html="name(phone.phone)"></span>
          </div>
        </div>
      </div>
      <div class="depart__right">
        <we-button
          v-if="child.deptLinkUrl"
          class="default-btn"
          :class="[btnClass]"
          plain
          size="medium"
          @click="visitUrl(child)"
        >
          {{ $Lan(lanFunName, "visitUrl", "访问网址") }}
        </we-button>
        <we-tooltip
          :content="$Lan(lanFunName, 'noVisitUrl', '此部门没有配置网址')"
          :open-delay="500"
          placement="top"
          v-else
          ><we-button
            class="default-btn button-disabled portal-font-color-lv4"
            size="medium"
            plain
          >
            {{ $Lan(lanFunName, "visitUrl", "访问网址") }}
          </we-button></we-tooltip
        >
        <we-button
          class="default-btn"
          :class="[btnClass]"
          plain
          size="medium"
          @click="goDetail(child)"
          style="margin-left:8px"
        >
          {{ $Lan(lanFunName, "checkDetail", "查看详情") }}
        </we-button>
      </div>
    </div>
    <depart-detail-modal
      ref="DepartDetailModal"
      :router="router"
      :lanFunName="lanFunName"
    ></depart-detail-modal>
  </div>
</template>

<script>
import DepartDetailModal from "./DepartDetailModal";
export default {
  props: {
    router: Object,
    item: Object,
    analyzeData: Array,
    keyword: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  components: {
    DepartDetailModal,
  },
  computed: {
    name() {
      return function(str) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(str, data);
      };
    },
    oneThingDesc() {
      const data = JSON.parse(JSON.stringify(this.analyzeData));
      return window.shell.strHighlighted(this.item.oneThingDesc, data);
    },
    btnClass() {
      return "portal-primary-border-color-lv1 portal-primary-color-lv1 portal-primary-backgroundcolor-hover-lv5";
    },
  },
  methods: {
    recordTrack(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: {
            infoType: 18,
            deptId: item.wid,
          },
        },
        startTime: new Date().getTime(),
      });
    },
    goDetail(item) {
      this.recordTrack(item);
      this.$refs.DepartDetailModal.show(item);
    },
    visitUrl(item) {
      this.recordTrack(item);
      window.shell.openUrl(item.deptLinkUrl);
    },
  },
};
</script>

<style lang="less" scoped>
.depart {
  padding: 20px 0;
  box-shadow: inset 0px -1px 0px #f0f0f0;
}
.depart__flex {
  display: flex;
}
.depart-child {
  padding: 16px 0 16px 20px;
  background: linear-gradient(
    90.13deg,
    #f5f5f5 0.1%,
    rgba(245, 245, 245, 0) 99.88%
  );
  border-radius: 4px;
  display: flex;
  margin-top: 14px;
  .depart__title {
    font-size: 14px;
    line-height: 22px;
  }
}
.depart__title {
  font-size: 16px;
  line-height: 24px;
  display: inline-block;
  max-width: 100%;
  margin-bottom: 8px;
}
.onlyTitle {
  align-items: center;
  .depart__title {
    margin-bottom: 0;
  }
}
.depart__left {
  flex: 1;
  width: 0;
}
.depart__right {
  margin-left: 24px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  .button-disabled,
  .button-disabled:hover {
    border-color: #d9d9d9;
  }
}
.depart__info {
  font-size: 14px;
  line-height: 22px;
  display: flex;
}
.depart__info_label {
  max-width: 112px;
  flex-shrink: 0;
}
.depart__info__phone {
  display: flex;
  flex-wrap: wrap;
  .depart__info {
    margin-right: 20px;
  }
}
</style>
