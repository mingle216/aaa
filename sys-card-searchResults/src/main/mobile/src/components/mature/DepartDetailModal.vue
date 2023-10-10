<template>
  <custom-action-sheet v-model="dialogShow" class="departDetail">
    <div class="we-action-sheet__header">
      <span>{{ $Lan(lanFunName, "departDetails", "部门详情") }}</span>
      <i
        @click="dialogShow = false"
        class="we-icon we-icon-cross we-action-sheet__close portal-font-color-lv3"
      ></i>
    </div>
    <div
      class="departDetail__info portal-font-color-lv1"
      id="departDetail__info"
      v-loading="loading"
    >
      <div
        class="departDetail__info_title portal-primary-before-backgroundcolor-lv1"
      >
        {{ $Lan(lanFunName, "baseInfo", "基本信息") }}
      </div>
      <div class="departDetail__row">
        <span class="departDetail__row_label portal-font-color-lv2">{{
          $Lan(lanFunName, "departName", "部门名称")
        }}</span
        ><span class="departDetail__row_value">{{ editItem.deptName }}</span>
      </div>
      <div class="departDetail__row" v-if="editItem.parentDeptName">
        <span class="departDetail__row_label portal-font-color-lv2">{{
          $Lan(lanFunName, "parentDepart", "父部门名称")
        }}</span
        ><span class="departDetail__row_value">{{
          editItem.parentDeptName
        }}</span>
      </div>
      <div class="departDetail__row" v-if="editItem.deptPhone">
        <span class="departDetail__row_label portal-font-color-lv2">{{
          $Lan(lanFunName, "departPhone", "部门电话")
        }}</span>
        <div class="departDetail__row_value">
          <div
            class="departDetail__row_phone"
            v-for="(phone, index) in JSON.parse(editItem.deptPhone || '[]')"
            :key="index"
          >
            <div class="departDetail__row_phone_label" v-if="phone.comments">
              {{ phone.comments }}
            </div>
            <div class="departDetail__row_value">
              <a
                @click="callPhone(phone.phone)"
                class="portal-primary-color-lv1"
                >{{ phone.phone }}</a
              >
            </div>
          </div>
        </div>
      </div>
      <div class="departDetail__row">
        <span class="departDetail__row_label portal-font-color-lv2">{{
          $Lan(lanFunName, "departLocation", "办公地点")
        }}</span
        ><span class="departDetail__row_value">{{
          editItem.deptAddress || "-"
        }}</span>
      </div>
      <div class="departDetail__row">
        <span class="departDetail__row_label portal-font-color-lv2">{{
          $Lan(lanFunName, "departDesc", "部门简介")
        }}</span
        ><span class="departDetail__row_value">{{
          editItem.deptDesc || "-"
        }}</span>
      </div>
      <!-- 子部门 -->
      <template v-if="editItem.childDept && editItem.childDept.length">
        <div
          class="departDetail__info_title mt-16 portal-primary-before-backgroundcolor-lv1"
        >
          {{ $Lan(lanFunName, "subDepartment", "子部门") }} ({{
            editItem.childDept.length
          }})
        </div>
        <div
          class="departDetail__child"
          v-for="(child, index) in editItem.childDept"
          :key="index"
        >
          <div class="departDetail__row">
            <span class="departDetail__row_label portal-font-color-lv2">{{
              $Lan(lanFunName, "departName", "部门名称")
            }}</span
            ><span class="departDetail__row_value">{{ child.deptName }}</span>
          </div>
          <div class="departDetail__row" v-if="child.deptPhone">
            <span class="departDetail__row_label portal-font-color-lv2">{{
              $Lan(lanFunName, "departPhone", "部门电话")
            }}</span>
            <div class="departDetail__row_value">
              <div
                class="departDetail__row_phone"
                v-for="(phone, index) in JSON.parse(child.deptPhone || '[]')"
                :key="index"
              >
                <div
                  class="departDetail__row_phone_label"
                  v-if="phone.comments"
                >
                  {{ phone.comments }}
                </div>
                <div class="departDetail__row_value">
                  <a
                    @click="callPhone(phone.phone)"
                    class="portal-primary-color-lv1"
                    >{{ phone.phone }}</a
                  >
                </div>
              </div>
            </div>
          </div>
          <div class="departDetail__row">
            <span class="departDetail__row_label portal-font-color-lv2">{{
              $Lan(lanFunName, "departLocation", "办公地点")
            }}</span
            ><span class="departDetail__row_value">{{
              child.deptAddress || "-"
            }}</span>
          </div>
          <div class="departDetail__row">
            <span class="departDetail__row_label portal-font-color-lv2">{{
              $Lan(lanFunName, "departDesc", "部门简介")
            }}</span
            ><span class="departDetail__row_value">{{
              child.deptDesc || "-"
            }}</span>
          </div>
        </div>
      </template>
    </div>
    <!-- 回到顶部 -->
    <div
      v-if="showBackTop"
      class="departDetail__backTop portal-primary-color-lv1"
      @click="handleBack"
    >
      <span class="iconfont icon-fanhuidingbu"></span>
    </div>
    <div class="departDetail__footer" v-if="editItem.deptLinkUrl">
      <div
        class="departDetail__footer_btn portal-primary-backgroundcolor-lv1 ellipsis"
        @click="$emit('visit-url', editItem)"
      >
        {{ $Lan(lanFunName, "visitUrl", "访问网址") }}
      </div>
    </div>
  </custom-action-sheet>
</template>

<script>
export default {
  props: ["lanFunName", "router"],
  data() {
    return {
      dialogShow: false,
      editItem: {},
      loading: true,
      emitScroll: false,
      showBackTop: false,
    };
  },
  beforeDestroy() {
    const dom = document.querySelector("#departDetail__info");
    dom && dom.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    show(item) {
      this.dialogShow = true;
      this.loading = true;
      this.editItem = {};
      this.showBackTop = false;
      window.shell
        .execCardMethod({
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getDeptInfoData",
          param: {
            deptWid: item.deptWid,
          },
        })
        .then((resp) => {
          this.loading = false;
          this.editItem = resp.data?.data || {};
        })
        .catch(() => {
          this.loading = false;
        });
      !this.emitScroll &&
        this.$nextTick(function() {
          const dom = document.querySelector("#departDetail__info");
          if (dom) {
            dom.addEventListener("scroll", this.handleScroll);
            this.emitScroll = true;
          }
        });
    },
    handleScroll(e) {
      if (e.target.scrollTop > 100) {
        this.showBackTop = true;
      } else {
        this.showBackTop = false;
      }
    },
    handleBack() {
      const dom = document.querySelector("#departDetail__info");
      dom.scrollTop = 0;
    },
    callPhone(number) {
      window.parent.location.href = `tel://${number}`;
    },
  },
};
</script>

<style lang="less" scoped>
.mt-16 {
  margin-top: 16px;
}
.departDetail {
  height: auto;
  /deep/.we-action-sheet__content {
    height: 100%;
    display: flex;
    flex-direction: column;
    position: relative;
  }
}
.departDetail__footer {
  flex-shrink: 0;
  padding: 8px 17px;
  box-shadow: 0px -4px 8px rgba(112, 125, 143, 0.1);
}
.departDetail__footer_btn {
  flex-shrink: 0;
  border-radius: 4px;
  font-size: 16px;
  line-height: 20px;
  color: #ffffff;
  padding: 10px 17px;
  text-align: center;
}
.departDetail__info {
  flex: 1;
  height: 0;
  overflow-y: auto;
  padding: 17px;
  line-height: 20px;
}
.departDetail__info_title {
  font-size: 16px;
  line-height: 22px;
  font-weight: bold;
  padding-left: 10px;
  position: relative;
  margin-bottom: 20px;
  &::before {
    content: "";
    width: 4px;
    height: 14px;
    border-radius: 4px;
    position: absolute;
    left: 0;
    top: 4px;
  }
}
.departDetail__row {
  display: flex;
  padding-bottom: 20px;
}
.departDetail__row_label {
  width: 72px;
  margin-right: 12px;
  flex-shrink: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  word-break: break-all;
}
.departDetail__row_value {
  flex: 1;
  word-break: break-all;
}
.departDetail__row_phone {
  display: flex;
  align-items: center;
  &:not(:last-child) {
    margin-bottom: 10px;
    padding-bottom: 10px;
    border-bottom: 1px dashed #e7edf1;
  }
}
.departDetail__row_phone_label {
  max-width: 112px;
  margin-right: 12px;
}
.departDetail__child:not(:last-child) {
  margin-bottom: 20px;
  box-shadow: inset 0px -0.5px 0px #e7edf1;
}
.departDetail__backTop {
  position: absolute;
  right: 17px;
  bottom: 104px;
  z-index: 1;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 2px 2px 10px rgba(14, 18, 64, 0.15);
  border-radius: 60px;
  background-color: #ffffff;
  /deep/.iconfont {
    font-size: 21px;
  }
}
</style>
