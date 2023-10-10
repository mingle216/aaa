<template>
  <div>
    <we-list
      class="depart__list portal-font-color-lv1"
      v-model="loading"
      :finished="finished"
      :finished-text="$Lan(lanFunName, 'finishedText', '没有更多了')"
      @load="loadData"
    >
      <template v-for="(item, index) in list">
        <div :key="index" class="depart__item" v-if="index < page * pageSize">
          <div @click="goDetail(item)">
            <div class="flex">
              <div
                class="depart__item_title ellipsis"
                v-html="name(item.deptName)"
              ></div>
              <span
                v-if="item.deptLinkUrl"
                class="iconfont icon-lianjie depart__item_icon portal-primary-color-lv1"
                @click.stop="visitUrl(item)"
              ></span>
            </div>
            <div class="flex depart__item_info" v-if="item.deptDesc">
              <span class="depart__info_label portal-font-color-lv2 ellipsis">{{
                $Lan(lanFunName, "introduction", "简介")
              }}</span
              >：<span
                class="depart__info_value ellipsis"
                v-html="name(item.deptDesc)"
              ></span>
            </div>
            <div class="flex depart__item_info" v-if="item.deptAddress">
              <span class="depart__info_label portal-font-color-lv2 ellipsis">{{
                $Lan(lanFunName, "location", "地点")
              }}</span
              >：<span
                class="depart__info_value ellipsis"
                v-html="name(item.deptAddress)"
              ></span>
            </div>
            <!-- 电话 -->
            <div
              class="flex depart__item_info"
              v-for="(phone, index) in JSON.parse(item.deptPhone || '[]')"
              :key="index"
            >
              <template v-if="phone.comments">
                <span
                  class="depart__info_label portal-font-color-lv2 ellipsis"
                  >{{ phone.comments }}</span
                >：</template
              ><span
                class="depart__info_value ellipsis"
                v-html="name(phone.phone)"
              ></span>
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
              <div
                class="depart__item_title ellipsis"
                v-html="name(child.deptName)"
              ></div>
              <span
                v-if="child.deptLinkUrl"
                class="iconfont icon-lianjie depart__item_icon portal-primary-color-lv1"
                @click.stop="visitUrl(child)"
              ></span>
            </div>
            <div class="flex depart__item_info" v-if="child.deptDesc">
              <span class="depart__info_label portal-font-color-lv2 ellipsis">{{
                $Lan(lanFunName, "introduction", "简介")
              }}</span
              >：<span
                class="depart__info_value ellipsis"
                v-html="name(child.deptDesc)"
              ></span>
            </div>
            <div class="flex depart__item_info" v-if="child.deptAddress">
              <span class="depart__info_label portal-font-color-lv2 ellipsis">{{
                $Lan(lanFunName, "location", "地点")
              }}</span
              >：<span
                class="depart__info_value ellipsis"
                v-html="name(child.deptAddress)"
              ></span>
            </div>
            <div
              class="flex depart__item_info"
              v-for="(phone, index) in JSON.parse(child.deptPhone || '[]')"
              :key="index"
            >
              <template v-if="phone.comments">
                <span
                  class="depart__info_label portal-font-color-lv2 ellipsis"
                  >{{ phone.comments }}</span
                >：</template
              ><span
                class="depart__info_value ellipsis"
                v-html="name(phone.phone)"
              ></span>
            </div>
          </div>
        </div>
      </template>
    </we-list>
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
  },
  components: {
    DepartDetailModal,
  },
  data() {
    return {
      errImg: window.shell.ErrorImg,
      loading: false,
      finished: true,
      page: 1,
      pageSize: 20,
    };
  },
  computed: {
    name() {
      return function(str) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(str, data);
      };
    },
    oneThingDesc() {
      return function(item) {
        const data = JSON.parse(JSON.stringify(this.analyzeData));
        return window.shell.strHighlighted(item.oneThingDesc, data);
      };
    },
  },
  watch: {
    list: {
      immediate: true,
      handler(val) {
        this.page = 1;
        if (val.length > this.pageSize) {
          this.finished = false;
        } else {
          this.finished = true;
        }
      },
    },
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 200);
      }
    },
  },
  methods: {
    loadData() {
      if (this.page * this.pageSize < this.list.length) {
        this.finished = false;
        this.page++;
      } else {
        this.finished = true;
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
.depart__item {
  padding: 16px 0;
  box-shadow: inset 0px -0.5px 0px #e7edf1;
  &:first-child {
    padding-top: 4px;
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
  background: linear-gradient(
    90.13deg,
    #f6f6f8 49.99%,
    rgba(246, 246, 248, 0) 99.88%
  );
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
