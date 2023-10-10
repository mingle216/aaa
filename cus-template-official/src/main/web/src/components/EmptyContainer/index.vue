<template>
  <div
    class="empty-con"
    :class="{ 'small-height': isSmallHeight, 'no-sub-tip': !subTip }"
    :style="conHeight"
  >
    <div
      v-if="!isSmallHeight"
      :class="{ 'no-img': !img }"
      class="empty-img"
      :style="{ 'background-image': `url(${img})` }"
    >
      <div class="empty-tip-con">
        <slot name="operate"></slot>
        <p v-if="tip" class="empty-tip" :class="mainTipClass">
          {{ tip }}
        </p>
        <p v-if="subTip" class="empty-sub-tip" :class="subTipClass">
          {{ subTip }}
        </p>
      </div>
    </div>
    <template v-else>
      <div
        v-if="img"
        class="small-empty-img"
        :style="{ 'background-image': `url(${img})` }"
      />
      <div class="small-empty-tip-con">
        <slot name="operate"></slot>
        <p v-if="tip" class="empty-tip" :class="mainTipClass">
          {{ tip }}
        </p>
        <p v-if="subTip" class="empty-sub-tip " :class="subTipClass">
          {{ subTip }}
        </p>
      </div>
    </template>
  </div>
</template>

<script>
const imgData = require("./imgs/empty.png");
export default {
  name: "EmptyContainer",
  props: {
    /**
     * 为空时图标
     */
    img: {
      type: String,
      default: imgData,
    },
    /**
     *主要提示文字
     */
    tip: {
      type: String,
      default: "",
    },
    /**
     *次要提示文字
     */
    subTip: {
      type: String,
      default: "",
    },
    /**
     *容器高度
     */
    height: {
      type: Number,
      default: 300,
    },
    /**
     *主要提示文字class名称
     */
    mainTipClass: {
      type: String,
      default: "portal-font-color-lv3",
    },
    /**
     *次要提示文字class名称
     */
    subTipClass: {
      type: String,
      default: "portal-font-color-lv3",
    },
    /**
     * 临界值
     */
    switchSize: {
      type: Number,
      default: 150,
    },
  },
  data() {
    return {
      isSmallHeight: false,
      // switchSize: 150
    };
  },
  computed: {
    conHeight() {
      if (this.height) {
        return { height: this.height + "px" };
      }
      return { height: "300px" };
    },
  },
  watch: {
    height: {
      immediate: true,
      handler(val) {
        if (val <= this.switchSize) {
          this.isSmallHeight = true;
        } else {
          this.isSmallHeight = false;
        }
      },
    },
  },
};
</script>

<style lang="less" scoped>
.empty-con {
  height: 300px;
  text-align: center;
  box-sizing: border-box;
  position: relative;
  color: #102645;
  overflow: hidden;
  &.no-sub-tip {
    .empty-img {
      -webkit-transform: translateY(-50%) translateY(-16px);
      -ms-transform: translateY(-50%) translateY(-16px);
      transform: translateY(-50%) translateY(-16px);
    }
  }
  .empty-img {
    height: 50%;
    max-height: 150px;
    width: 100%;
    background-position: bottom center;
    background-repeat: no-repeat;
    background-size: contain;
    position: absolute;
    top: 50%;
    -webkit-transform: translateY(-50%) translateY(-25px);
    -ms-transform: translateY(-50%) translateY(-25px);
    transform: translateY(-50%) translateY(-25px);
    overflow: visible;
    &.no-img {
      .empty-tip-con {
        top: 50%;
      }
    }
    .empty-tip-con {
      position: absolute;
      width: 100%;
      padding: 0 20px;
      box-sizing: border-box;
      font-size: 14px;
      top: calc(100% + 10px);
      text-align: center;
      margin: 0;

      p {
        margin: 0;
        display: block;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        &.empty-tip {
          margin-bottom: 6px;
          height: 22px;
        }
        &.empty-sub-tip {
          font-size: 12px;
          height: 20px;
          line-height: 20px;
        }
      }
    }
  }
  &.small-height {
    display: flex;
    align-items: center;
    text-align: justify;
    .small-empty-img {
      flex: 1;
      background-size: auto 100%;
      background-position: right center;
      background-repeat: no-repeat;
      height: 100%;
    }
    .small-empty-tip-con {
      flex: 1;
      padding-left: 10px;
      display: table-cell;
      vertical-align: middle;
      text-align: left;
      margin: 0;
      width: 0;
      p {
        margin: 0;
        display: block;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;

        &.empty-sub-tip {
          font-size: 12px;
        }
      }
    }
  }
}
</style>
