<template>
  <div>
    <div class="depart__list portal-font-color-lv1">
      <div class="title portal-font-color-lv3">{{ name }} ({{ list.length }})</div>
      <template v-for="(item, index) in list">
        <div :key="index" class="depart__item" v-if="index < page * pageSize">
          <div @click="goDetail(item)">
            <div class="flex">
              <div class="depart__item_title ellipsis" v-html="nameHighLight(item.deptName)"></div>
              <span
                v-if="item.deptLinkUrl"
                class="iconfont icon-lianjie depart__item_icon portal-primary-color-lv1"
                @click.stop="visitUrl(item)"
              ></span>
            </div>
            <div class="flex depart__item_info portal-font-color-lv3" v-if="item.deptDesc">
              <span class="depart__info_label ellipsis">{{
                $Lan(lanFunName, "introduction", "简介")
              }}</span
              >：<span
                class="depart__info_value ellipsis"
                v-html="nameHighLight(item.deptDesc)"
              ></span>
            </div>
            <div class="flex depart__item_info portal-font-color-lv3" v-if="item.deptAddress">
              <span class="depart__info_label ellipsis">{{
                $Lan(lanFunName, "location", "地点")
              }}</span
              >：<span
                class="depart__info_value ellipsis"
                v-html="nameHighLight(item.deptAddress)"
              ></span>
            </div>
            <!-- 电话 -->
            <div
              class="flex depart__item_info portal-font-color-lv3"
              v-for="(phone, index) in JSON.parse(item.deptPhone || '[]')"
              :key="index"
            >
              <template v-if="phone.comments">
                <span class="depart__info_label ellipsis">{{ phone.comments }}</span
                >：</template
              ><span class="depart__info_value ellipsis" v-html="nameHighLight(phone.phone)"></span>
            </div>
          </div>
          <!-- 子部门 -->
          <div
            class="depart-child"
            v-for="child in item.childDept || []"
            :key="child.deptWid"
            @click="goDetail(child)"
          >
            <div class="flex">
              <div class="depart__item_title ellipsis" v-html="nameHighLight(child.deptName)"></div>
              <span
                v-if="child.deptLinkUrl"
                class="iconfont icon-lianjie depart__item_icon portal-primary-color-lv1"
                @click.stop="visitUrl(child)"
              ></span>
            </div>
            <div class="flex depart__item_info portal-font-color-lv3" v-if="child.deptDesc">
              <span class="depart__info_label ellipsis">{{
                $Lan(lanFunName, "introduction", "简介")
              }}</span
              >：<span
                class="depart__info_value ellipsis"
                v-html="nameHighLight(child.deptDesc)"
              ></span>
            </div>
            <div class="flex depart__item_info portal-font-color-lv3" v-if="child.deptAddress">
              <span class="depart__info_label ellipsis">{{
                $Lan(lanFunName, "location", "地点")
              }}</span
              >：<span
                class="depart__info_value ellipsis"
                v-html="nameHighLight(child.deptAddress)"
              ></span>
            </div>
            <div
              class="flex depart__item_info portal-font-color-lv3"
              v-for="(phone, index) in JSON.parse(child.deptPhone || '[]')"
              :key="index"
            >
              <template v-if="phone.comments">
                <span class="depart__info_label portal-font-color-lv2 ellipsis">{{
                  phone.comments
                }}</span
                >：</template
              ><span class="depart__info_value ellipsis" v-html="nameHighLight(phone.phone)"></span>
            </div>
          </div>
        </div>
      </template>
      <moreData
        :lan-fun-name="lanFunName"
        @click="pageAdd"
        :isOpen="page * pageSize < list.length"
        v-if="pageSize < list.length"
      />
    </div>
    <depart-detail-modal
      :lanFunName="lanFunName"
      :router="router"
      ref="DepartDetailModal"
      @visit-url="visitUrl"
    ></depart-detail-modal>
  </div>
</template>

<script>
import DepartDetailModal from "./DepartDetailModal";
import MoreData from "./MoreData";
export default {
  props: {
    router: Object,
    analyzeData: Array,
    list: {
      type: Array,
      default: () => {
        return [];
      },
    },
    keyword: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
    name: String,
  },
  components: {
    DepartDetailModal,
    MoreData,
  },
  data() {
    return {
      errImg: window.shell.ErrorImg,
      loading: false,
      finished: true,
      page: 1,
      pageSize: 6,
    };
  },
  computed: {
    nameHighLight() {
      return function (str) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(str, data);
      };
    },
    oneThingDesc() {
      return function (item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.oneThingDesc, data);
      };
    },
  },
  methods: {
    pageAdd(isAdd) {
      if (isAdd) {
        this.page++;
      } else {
        this.page = 1;
      }
    },
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
      this.$emit("record-cache");
      this.recordTrack(item);
      window.shell.openUrl(item.deptLinkUrl);
    },
  },
};
</script>

<style lang="less" scoped>
.flex {
  display: flex;
  align-items: center;
}
.depart__list {
  padding: 12px;
  background: #ffffff;
  border-radius: 8px;
  .title {
    // margin-bottom: 12px;
  }
}
.depart__item {
  padding: 12px 0;
  box-shadow: inset 0px -1px 0px #f0f0f0;
  &:first-child {
    padding-top: 4px;
  }
  &:last-child {
    box-shadow: none;
  }
}
.depart__item_title {
  flex: 1;
  width: 0;
  font-size: 16px;
  line-height: 22px;
  font-weight: bold;
}
.depart__item_icon {
  // font-size: 17px;
  margin-left: 20px;
}
.depart__item_info {
  margin-top: 4px;
  line-height: 18px;
  &:nth-child(2) {
    margin-top: 12px;
  }
}
.depart__info_label {
  max-width: 112px;
  flex-shrink: 0;
}
.depart-child {
  margin-top: 12px;
  padding: 12px 0 12px 12px;
  background: linear-gradient(90.13deg, #f6f6f8 49.99%, rgba(246, 246, 248, 0) 99.88%);
  border-radius: 4px;
  .depart__item_title {
    font-size: 14px;
    line-height: 20px;
  }
  .depart__item_info:nth-child(2) {
    margin-top: 8px;
  }
}
</style>
