<template>
  <div>
    <div class="serviceBus">
      <div
        class="serviceBus__item"
        v-for="item in shortLists"
        :key="item.serviceWid"
        :class="{
          'portal-font-color-lv1': item.permission,
          'portal-font-color-lv4': !item.permission,
          'portal-primary-color-hover-lv1': item.permission,
        }"
        @click="handleClick(item)"
      >
        <div
          class="serviceBus__itemImgWrap"
          :class="{ 'no-permission-service': !item.permission }"
        >
          <img
            :src="item.iconLink || item.iconUrl || errorImg"
            class="serviceBus__itemImg"
            @error="handleError"
          />
        </div>
        <div class="serviceBus__itemName ellipsis">
          {{ item.serviceName }}
        </div>
      </div>
      <!-- 查看更多 -->
      <div
        class="serviceBus__item portal-primary-color-hover-lv1"
        v-if="lists.length > 9"
        @click="handleMore"
      >
        <template v-if="isSmall">
          <div
            class="portal-primary-color-lv1"
            style="font-size:12px;margin-top:1px"
          >
            {{ $Lan("CUS_TEMPLATE_CQWORK", "showMore", "查看更多") }}
          </div>
          <div
            style="font-size:12px;margin-left: 4px;margin-top: 1px"
            class="icon iconfont icon-xinwentongzhigonggaochakangengduo portal-primary-color-lv1"
          ></div>
        </template>
        <template v-else>
          <div class="serviceBus__itemImgWrap" style="background: transparent">
            <span class="serviceBus__more"
              ><i class="we-icon-arrow-right portal-font-color-lv3"></i
            ></span>
          </div>
          <div class="serviceBus__itemName ellipsis ">
            {{ $Lan("CUS_TEMPLATE_CQWORK", "showMore", "查看更多") }}
          </div>
        </template>
      </div>
    </div>
    <we-drawer
      :visible.sync="drawer"
      :append-to-body="true"
      direction="ltr"
      :size="316"
    >
      <div slot="title" class="">
        <div class="drawer-title portal-font-color-lv1">
          {{ $Lan("CUS_TEMPLATE_CQWORK", "allApps", "全部应用系统") }}
        </div>
      </div>
      <vue-scroll :barKeepShow="true" style="width: 316px"
        ><div class="serviceBus__draw">
          <div
            class="serviceBus__drawItem"
            v-for="item in lists"
            :key="item.serviceWid"
            :class="{
              'portal-font-color-lv1': item.permission,
              'portal-font-color-lv4': !item.permission,
              'portal-primary-color-hover-lv1': item.permission,
            }"
            @click="handleClick(item)"
          >
            <div
              class="serviceBus__drawItemImgWrap"
              :class="{ 'no-permission-service': !item.permission }"
            >
              <img
                :src="item.iconLink || item.iconUrl || errorImg"
                class="serviceBus__itemImg"
                @error="handleError"
              />
            </div>
            <div class="serviceBus__drawItemName ellipsis">
              {{ item.serviceName }}
            </div>
          </div>
        </div></vue-scroll
      >
    </we-drawer>
  </div>
</template>

<script>
export default {
  props: ["lists"],
  data() {
    return {
      drawer: false,
      isSmall: document.body.clientWidth <= 1281,
      errorImg: require("../../assets/images/imgErr.png"),
    };
  },
  computed: {
    shortLists() {
      return this.lists.length > 9 ? this.lists.slice(0, 9) : this.lists;
    },
  },
  methods: {
    handleResize() {
      this.isSmall = document.body.clientWidth <= 1281;
    },
    handleMore() {
      this.drawer = true;
      this.handleClickTrack({
        infoType: 16,
      });
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    handleClick(item) {
      this.handleClickTrack({
        infoType: 16,
        serviceId: item.serviceWid,
      });
      if (!item.permission) {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan(
            "public",
            "noServicePermission",
            "暂无使用权限，请联系管理员"
          ),
        });
        return;
      }
      window.shell.openService({
        ...item,
        wid: item.serviceWid,
      });
    },
    //点击埋点
    handleClickTrack(extraInfo) {
      const pageData = window.shell.getPageData().pageInfoEntity;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: pageData.pageCode,
          pageName: pageData.pageName,
          extraInfo,
        },
        startTime: new Date().getTime(),
      });
    },
  },
  created() {
    window.addEventListener("resize", this.handleResize);
    this.$once("hook:beforeDestroy", () => {
      window.removeEventListener("resize", this.handleResize);
    });
  },
};
</script>

<style scoped>
.serviceBus {
  margin-top: 8px;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}
.serviceBus__item {
  width: 48%;
  flex-shrink: 0;
  margin-bottom: 24px;
  text-align: center;
  cursor: pointer;
}
.serviceBus__itemImg {
  width: 100%;
  height: 100%;
  border-radius: 6px;
}
.serviceBus__more {
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 10px;
  background: #ffffff;
  border: 1px solid #dfe6ec;
  text-align: center;
  line-height: 20px;
  font-size: 12px;
  margin-top: 12px;
}
.drawer-title {
  font-weight: bold;
  font-size: 18px;
  line-height: 26px;
}
.serviceBus__draw {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-top: 16px;
  padding: 0 20px;
}
.serviceBus__drawItem {
  width: 50%;
  flex-shrink: 0;
  margin-bottom: 24px;
  text-align: center;
  cursor: pointer;
  padding: 0 8px;
}
.serviceBus__drawItemImgWrap {
  width: 44px;
  height: 44px;
  border-radius: 6px;
  margin: 0 auto;
}
.serviceBus__drawItemName {
  margin-top: 8px;
  line-height: 22px;
}
@media screen and (min-width: 1282px) {
  .serviceBus__itemImgWrap {
    width: 44px;
    height: 44px;
    margin: 0 auto;
  }
  .serviceBus__itemName {
    margin-top: 8px;
    line-height: 22px;
  }
}
@media screen and (max-width: 1281px) {
  .serviceBus {
    margin-top: 4px;
  }
  .serviceBus__item {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    line-height: 20px;
  }
  .serviceBus__itemImgWrap {
    width: 20px;
    height: 20px;
    margin-right: 3px;
    flex-shrink: 0;
  }
  .serviceBus__itemName {
    flex: 1;
    font-size: 12px;
    text-align: left;
    margin-top: 2px;
  }
  /deep/.no-permission-service.serviceBus__itemImgWrap::after {
    background-size: 20px 20px;
  }
}

/deep/.we-drawer {
  background: #f6f9fe;
}
/deep/.we-drawer__body {
  min-height: 0;
}
/deep/.we-drawer__header {
  padding: 20px;
  margin-bottom: 0;
  box-shadow: inset 0px -1px 0px 0px #dde6ed;
  width: 316px;
}
/deep/.we-drawer:focus,
/deep/.we-drawer__close-btn:focus {
  outline: none;
}
</style>
