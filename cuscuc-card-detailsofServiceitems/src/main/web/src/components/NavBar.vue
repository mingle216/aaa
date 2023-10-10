<template>
  <!--导航栏-->
  <!-- :style="navStyle" -->
  <div class="navbar portal-font-color-lv2">
    <!-- <AutoContainer :con-type="2" :con-height="height"> -->
    <ul class="nAside portal-font-color-lv2">
      <li
        v-for="(item, i) in navList"
        :key="i"
        class="anchor-item"
        @click="getPlace(item)"
      >
        <!-- <span
          class="box portal-primary-backgroundcolor-lv1"
          :class="{ isBox: activeKey == item.fieldWid }"
        ></span> -->
        <a :title="item.fieldName" class="navItem">
          <div
            :fieldWid="item.fieldWid"
            class="nav-name"
            :class="{
              'anchor-item-active portal-primary-color-lv1 portal-primary-border-color-lv1':
                activeKey == item.fieldWid,
            }"
          >
            {{ item.fieldName }}
          </div>
        </a>
      </li>
    </ul>
    <!-- </AutoContainer> -->
  </div>
</template>

<script>
export default {
  props: {
    navStyle: {
      type: String,
      default: "",
    },
    // navBarLeft: {
    //   type: String,
    //   default: "",
    // },
    navList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    activeKey: {
      type: String,
      default: "",
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      height: 0,
    };
  },
  computed: {
    // marginLeft: function() {
    //   return this.navStyle + ";" + this.navBarLeft;
    // },
  },
  created() {
    // this.getHeight();
  },
  methods: {
    getPlace(item) {
      this.$emit("nav-change", item);
    },
    // getHeight() {
    //   const screenHeight = window.screen.availWidth;
    //   if (screenHeight <= 1440) {
    //     this.height = 202;
    //   } else if (screenHeight > 1440 && screenHeight <= 1920) {
    //     this.height = 472;
    //   } else {
    //     this.height = 562;
    //   }
    // },
  },
};
</script>

<style lang="less" scoped>
.navItem {
  cursor: pointer;
}
.isBox {
  opacity: 1 !important;
}
.anchor-item {
  width: 100%;
  height: 32px;
  line-height: 32px;
  position: relative;
  left: 0px;
  margin-bottom: 8px;
  padding: 5px 0;
  .nav-name {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    height: 22px;
    line-height: 22px;
    &.anchor-item-active {
      font-weight: 600;
      position: relative;
      border-left: 2px solid;
    }
  }
  &:last-child {
    margin-bottom: 0;
  }
}

// .box {
//   border-radius: 2px;
//   opacity: 0;
//   position: absolute;
//   display: inline-block;
//   width: 4px;
//   height: 10px;
//   left: -17px;
//   top: 15px;
// }
.nAside {
  position: relative;
  // top: 7px;
}
.navbar {
  width: 90px;
  border-left: 1px solid #e7edf1;
  // padding-left: 16px;
  text-indent: 16px;
  position: absolute;
  right: 0px;
  top: 0;
  z-index: 100;
  background-color: #fff;
}
</style>
