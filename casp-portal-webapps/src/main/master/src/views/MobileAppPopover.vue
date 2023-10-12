<template>
  <div
    class="mobileAppPopover we-popper"
    id="mobileAppPopover"
    ref="mobileAppPopover"
    :style="[placementStyle, hwStyle]"
    :x-placement="placement"
    :class="{ 'mobileAppPopover-show': showPop }"
  >
    <div class="mobileAppPopover__content portal-font-color-lv1">
      <div v-if="!flag" class="iconAtitle">
        <div class="imgDiv">
          <img class="mobileIcon" :src="itemInfo.mobileIconUrl" alt="" />
        </div>
        <div class="titleword">{{ itemInfo.serviceName }}</div>
      </div>
      <div class="mobileAppPopover__qrcode" ref="MobileQrcode"></div>
      <div class="word">
        <p>
          {{
            $Lan("public", "mobileService", "此服务为移动端服务")
          }}
        </p>
        <p>
          {{ $Lan("public", "scanCode", "请手机扫二维码打开") }}
        </p>
      </div>
    </div>
    <div class="popper__arrow"></div>
  </div>
</template>
<script>
import globalObj from "../utils/global";
import QRCode from "qrcodejs2";
export default {
  props: {
    itemInfo: {
      type: Object,
      default: () => {},
    },
    serviceList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      placement: "bottom",
      isShow: false,
      showPop: false,
      placementStyle: {},
      hwStyle: {},
      flag: null,
      isOneMobile: null,
    };
  },
  mounted() {
    // 点击其他区域时, 隐藏指定区域(cDom)
    document.addEventListener("click", (event) => {
      let cDom = document.querySelector("#mobileAppPopover");
      let tDom = event.target;
      if (
        this.isShow &&
        !(
          cDom == tDom ||
          cDom.contains(tDom) ||
          tDom == this.targetEle ||
          this.targetEle.contains(tDom)
        )
      ) {
        this.handleMobileclose();
      }
    });
    window.addEventListener("resize", this.setPlacement);
    // 监听滚动事件，目标元素滚出可视区域时，隐藏弹窗
    globalObj.on("onScoll", () => this.handleScroll());
  },
  beforeDestroy() {
    document.removeEventListener("click", () => {});
    document.removeEventListener("resize", () => {});
    globalObj.off("onScoll");
  },
  watch: {
    itemInfo() {
      this.createQrCode();
    },
    serviceList(val) {
      this.isOneMobile = val.length === 1 && val[0].serviceType === 0;
    },
    isShow(val) {
      setTimeout(() => {
        this.showPop = val;
      }, 20);
    },
  },
  methods: {
    show(targetEle) {
      if (!targetEle) {
        return;
      }
      this.targetEle = targetEle;
      this.flag =
        this.targetEle?.classList?.value?.indexOf("service__item") !== -1
          ? true
          : false; // 有图标显示的二维码框
      this.isShow = true;
      this.setPlacement();
    },
    setPlacement() {
      if (!this.isShow) {
        return;
      }
      const {
        width,
        height,
        top,
        bottom,
        left,
        right,
      } = this.targetEle.getBoundingClientRect();
      this.hwStyle = this.flag
        ? { width: "186px", height: "191px" }
        : { width: "186px", height: "240px" };
      let cHeight = this.flag ? 191 : 240;
      const clientWidth = window.innerWidth || document.body.clientWidth; //网页可见区域宽
      const clientHeight = window.innerHeight || document.body.clientHeight; //网页可见区域高
      const popHeight = cHeight;
      const popWidth = 186;
      // console.log(this.targetEle.getBoundingClientRect());
      // console.log(width, height);
      // console.log(left, top, right, bottom);
      // console.log(clientWidth, clientHeight);
      // console.log(popWidth, popHeight);
      if (left + popWidth < clientWidth && bottom + popHeight < clientHeight) {
        console.log("bottom");
        this.placement = "bottom";
      } else if (left + popWidth < clientWidth && top > popHeight) {
        console.log("top");
        this.placement = "top";
      } else if (right + popWidth < clientWidth) {
        console.log("right");
        this.placement = "right";
      } else {
        console.log("left");
        this.placement = "left";
      }
      if (this.placement === "bottom") {
        let curLeft =
          this.flag && !this.isOneMobile
            ? `${left + 112}px`
            : `${left + width / 2 - popWidth / 2}px`;
        let curTop = height === 60 ? `${bottom - 27}px` : `${bottom}px`;
        this.placementStyle = {
          // top: `${bottom - 27}px`,
          // left: `${left + width / 2 - popWidth / 2}px`,
          top: curTop,
          left: curLeft,
        };
      } else if (this.placement === "top") {
        console.log(this.flag);
        let curLeft =
          this.flag && !this.isOneMobile
            ? `${left + 12}px`
            : `${left + width / 2 - popWidth / 2}px`;
        let curTop = height === 60 ? `${bottom - 242}px` : `${bottom - 223}px`;
        this.placementStyle = {
          top: curTop,
          left: curLeft,
        };
      } else if (this.placement === "left") {
        this.placementStyle = {
          top: `${top + height / 2 - popHeight / 2}px`,
          left: `${left - popWidth - 10}px`,
        };
      } else {
        this.placementStyle = {
          top: `${top + height / 2 - popHeight / 2}px`,
          left: `${right + 5}px`,
        };
      }
    },
    createQrCode() {
      if (!this.itemInfo.mobileAccessUrl) {
        return;
      }
      if (this.qrcode) {
        this.qrcode.makeCode(this.itemInfo.mobileAccessUrl);
      } else {
        this.qrcode = new QRCode(this.$refs.MobileQrcode, {
          text: this.itemInfo.mobileAccessUrl,
          width: 92,
          height: 92,
          colorDark: "#000000",
          colorLight: "#ffffff",
          correctLevel: QRCode.CorrectLevel.L,
        });
      }
    },
    handleMobileclose() {
      this.isShow = false;
      this.qrcode && this.qrcode.clear();
    },
    handleMobileOk() {
      globalObj.openService(this.itemInfo, null, true);
      this.handleMobileclose();
    },
    handleScroll() {
      if (!this.isShow) {
        return;
      }
      this.handleMobileclose();
      // const viewHeight =
      //   window.innerHeight || document.documentElement.clientHeight;
      // const { top, bottom } = this.targetEle.getBoundingClientRect();
      // if (top >= 0 && bottom <= viewHeight) {
      //   this.setPlacement();
      // } else {
      //   this.placementStyle = {
      //     top: "-9999px",
      //     left: "-9999px",
      //   };
      // }
    },
  },
};
</script>

<style scoped>
.titleword {
  font-size: 16px;
  line-height: 30px;
  max-width: 112px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-weight: bold;
}
.iconAtitle {
  display: flex;
  justify-content: center;
  width: 156px;
  margin-top: 13px;
}
.mobileIcon {
  width: 100%;
  vertical-align: middle;
}
.imgDiv {
  overflow: hidden;
  margin-right: 12px;
  min-width: 32px;
  min-height: 32px;
  width: 32px;
  height: 32px;
  border-radius: 5px;
}
.word {
  font-size: 12px;
  color: #707d8f;
  text-align: center;
  margin-top: 10px;
}
.mobileAppPopover {
  position: fixed;
  top: 50px;
  left: 50px;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #ebeef5;
  padding: 12px;
  z-index: -1;
  line-height: 1.4;
  text-align: justify;
  box-shadow: 0 5px 10px 1px rgba(0, 0, 0, 0.1);
  word-break: break-all;
  display: none;
}
.mobileAppPopover[x-placement="bottom"] .popper__arrow {
  margin-left: -6px;
}
.mobileAppPopover[x-placement="left"] .popper__arrow,
.mobileAppPopover[x-placement="right"] .popper__arrow {
  margin-top: -6px;
}

.mobileAppPopover.mobileAppPopover-show {
  display: block;
  z-index: 2999;
}
.mobileAppPopover__header {
  font-weight: 600;
  font-size: 18px;
}
.mt-20 {
  margin-top: 20px;
}
.mobileAppPopover__qrcode {
  margin-top: 16px;
  margin-left: 34px;
  display: inline-block;
}
.mobileAppPopover__arrow {
  border-width: 6px;
  filter: drop-shadow(0 2px 12px rgba(0, 0, 0, 0.03));
}
.mobileAppPopover__arrow::after {
  position: absolute;
  display: block;
  width: 0;
  height: 0;
  border-color: transparent;
  border-style: solid;
}
.mobileAppPopover__footer {
  margin-top: 25px;
  display: flex;
  justify-content: flex-end;
}
.btn {
  border-radius: 4px;
  color: #ffffff;
  letter-spacing: 0;
  font-size: 14px;
  width: 84px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  cursor: pointer;
}
.cancel__btn {
  background: #ffffff;
  border: 1px solid #d6dade;
  color: #637389;
  margin-right: 10px;
}
.confirm__btn {
  color: #ffffff;
}
</style>
