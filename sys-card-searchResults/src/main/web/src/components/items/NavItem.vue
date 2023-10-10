<template>
  <div
    class="nav-item portal-font-color-lv2 portal-primary-color-hover-lv1"
    @click="setScroll"
  >
    <div class="nav-text" :class="{ 'portal-primary-color-lv1': active }">
      <span>{{ navText }}</span>
    </div>
  </div>
</template>

<script>
/* eslint-disable no-debugger */
export default {
  name: "navItem",
  model: {
    prop: "clickScrolling",
    event: "change",
  },
  props: {
    router: Object,
    navId: {
      type: String,
      default: "",
    },
    navText: {
      type: String,
      default: "",
    },
    startTop: {
      type: Number,
      default: 0,
    },
    indexNavId: {
      type: String,
      default: "",
    },
    clickScrolling: {
      type: Boolean,
      default: false,
    },
  },

  watch: {
    startTop: {
      handler: function(val) {
        // console.log(val)
        this.navStartTop = val;
        this.$nextTick(() => {
          this.initActive(0);
        });
      },
      immediate: true,
    },
    indexNavId: {
      handler: function(val) {
        if (val !== "") {
          if (this.navId === val) {
            this.active = true;
          } else {
            this.active = false;
          }
        }
      },
      immediate: true,
    },
  },
  data() {
    return {
      hoverClass: "",
      active: false,
      navStartTop: 0,
      v_top: 0,
      v_height: 0,
    };
  },
  mounted() {
    window.shell.on("onScoll", (ev) => {
      let sc_top = ev.scrollTop;
      //   console.log(this.clickScrolling,sc_top)
      if (!this.clickScrolling) {
        this.initActive(sc_top);
      }
    });
  },

  methods: {
    initActive(sc_top) {
      let con_el = document.getElementById(this.navId);
      if (con_el) {
        this.v_top = window.shell.getElementTop(con_el);
        this.v_height = con_el.offsetHeight;
        // console.log("navStartTop",this.navStartTop)
        // console.log("v_top",this.v_top)
        // console.log("v_height",this.v_height)
        if (
          (sc_top >= this.v_top && sc_top < this.v_top + this.v_height) ||
          (this.v_top === this.navStartTop &&
            sc_top < this.v_top + this.v_height)
        ) {
          this.active = true;
          this.$emit("activeItem", this.navId);
        } else {
          this.active = false;
        }
      }
    },
    setScroll() {
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
      this.$emit("change", true);
      this.$emit("activeItem", this.navId);
      window.shell.emit("vs-scroll-to", { y: this.v_top });
      window.shell.on("page-scroll-complete", () => {
        this.$emit("activeItem", this.navId);
        // console.log("activeItem")

        this.$emit("change", false);
        window.shell.off("page-scroll-complete");
      });
    },
  },
};
</script>

<style lang="less" scoped>
.nav-item {
  height: 32px;
  box-sizing: border-box;
  padding: 5px 0;
  font-size: 14px;
  color: #102645;
  margin-bottom: 6px;
  position: relative;
  cursor: pointer;

  .nav-text {
    height: 22px;
    line-height: 22px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    text-indent: 12px;
    margin-right: -1px;
    padding: 0 20px 0 0;
    box-sizing: border-box;
    &.portal-primary-color-lv1 {
      border-right: 2px solid;
    }
  }
}
</style>
