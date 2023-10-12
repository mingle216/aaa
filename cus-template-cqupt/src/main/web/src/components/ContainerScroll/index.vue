<template>
  <vue-scroll
    ref="vscroll"
    class="con-scroll"
    :class="{'native-model':nativeModel}"
    :ops="ops"
    @handle-scroll="handleScroll"
    @handle-resize="resizeHandle"
    @handle-scroll-complete="completeHandle"
     v-on="$listeners"
  >
    <div class="vs-con-main">
      <div class="vs-con-body">
        <!-- 放置在滚动条中的内容 -->
        <slot />
        <div
          v-if="!scrollCover && scrollingX"
          class="h-scroll"
          :style="{ height: `${size + 4}px` }"
        />
      </div>
      <div
        v-if="!scrollCover && scrollingY"
        class="v-scroll"
        :style="{ width: `${size + 4}px` }"
      />
    </div>
  </vue-scroll>
</template>

<script>
import vueScroll from 'vuescroll/dist/vuescroll-native'
export default {
  name: 'ContainerScroll',
  components: { vueScroll },
  props: {
    /**
     * 滚动条形式，0：虚拟，可以设置滚动条样式；1：原生，显示浏览器原生滚动条
     */
    nativeModel:{
      type:Boolean,
      default:false
    },
    /**
     * 滚动条大小
     */
    size: {
      type: Number,
      default: 6
    },
    /**
     * 滚动条滑轨背景
     */
    railBgColor: {
      type: String,
      default: ''
    },
    /**
     * 滚动条背景
     */
    barBgColor: {
      type: String,
      default: 'rgba(191,191,191,0.4)'
    },
    /**
     * 是否一直显示滚动条
     */
    barKeepShow: {
      type: Boolean,
      default: false
    },
    /**
     * 滚动条最小值：0~1
     */
    barMinLen: {
      type: Number,
      default: 0.2
    },
    /**
     * 滚动条动画时间
     */
    scrollDuration: {
      type: Number,
      default: 300
    },
    /**
     * 容器最大高度
     */
    maxHeight: {
      type: Number,
      default: null
    },
    /**
     * 滚动速度
     */
    speed: {
      type: Number,
      default: 300
    },
    /**
     * 是否显示横向滚动条
     */
    scrollingX: {
      type: Boolean,
      default: false
    },
    /**
     * 是否显示竖向滚动条
     */
    scrollingY: {
      type: Boolean,
      default: true
    },
    /**
     * 是否允许滚动条盖住内容
     */
    scrollCover: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      content: '',
      ops: {
        rail: {
          opacity: '0.1',
          background: null,
          border: 'none',
          size: '6px'
        },
        bar: {
          background: 'rgba(191,191,191,0.4)',
          keepShow: false,
          size: '6px',
          minSize: 0.2,
          onlyShowBarOnScroll: true,
          showDelay: 500
        },
        scrollButton: {
          enable: false
        },

        vuescroll: {
          wheelDirectionReverse: false,
          locking: false,
          checkShifKey: true,
          wheelScrollDuration: 200
        },
        scrollPanel: {
          scrollingX: false, // 是否开启横向滚动
          scrollingY: true, // 是否开启竖向滚动
          maxHeight: null,
          speed: 300,
          easing: 'easeInQuad'
        }
      }
    }
  },
  watch: {
    scrollingX: {
      handler (val) {
        this.ops.scrollPanel.scrollingX = val
      },
      immediate: true
    },
    scrollingY: {
      handler (val) {
        this.ops.scrollPanel.scrollingY = val
      },
      immediate: true
    },
    size: {
      handler (val) {
        if (val) {
          this.ops.rail.size = val + 'px'
          this.ops.bar.size = val + 'px'
        } else {
          this.ops.rail.size = '8px'
          this.ops.bar.size = '8px'
        }
      },
      immediate: true
    },

    railBgColor: {
      handler (val) {
        if (val) {
          this.ops.rail.background = val
        } else {
          this.ops.bar.background = null
        }
      },
      immediate: true
    },
    barBgColor: {
      handler (val) {
        if (val) {
          this.ops.bar.background = val
        } else {
          this.ops.bar.background = 'rgba(191,191,191,0.4)'
        }
      },
      immediate: true
    },
    barKeepShow: {
      handler (val) {
        this.ops.bar.keepShow = !!val
      },
      immediate: true
    },
    barMinLen: {
      handler (val) {
        if (val) {
          this.ops.bar.minSize = val
        } else {
          this.ops.bar.minSize = 0.1
        }
      },
      immediate: true
    },
    scrollDuration: {
      handler (val) {
        if (val) {
          this.ops.vuescroll.wheelScrollDuration = val
        } else {
          this.ops.vuescroll.wheelScrollDuration = 200
        }
      },
      immediate: true
    },
    maxHeight: {
      handler (val) {
        if (val) {
          this.ops.scrollPanel.maxHeight = val
        } else {
          this.ops.scrollPanel.maxHeight = null
        }
      },
      immediate: true
    },
    speed: {
      handler (val) {
        if (val) {
          this.ops.scrollPanel.speed = val
        } else {
          this.ops.scrollPanel.speed = 100
        }
      },
      immediate: true
    }
  },
  mounted () {},

  methods: {
    /**
     * 获取滚动条对象
     */
    getVscroll(){
      return this.$refs['vscroll']
    },
    /**
     * 获取当前滚动条位置
     */
    getPosition(){
      return this.$refs['vscroll'].getPosition()
    },
    handleScroll (vertical, horizontal, nativeEvent) {
      /**
       * 滚动事件
       */
      this.$emit('vshandle-scroll', vertical, horizontal, nativeEvent)
    },

    resizeHandle (vertical, horizontal, nativeEvent) {
      /**
       * 大小发生改变
       */
      this.$emit('vshandle-resize', vertical, horizontal, nativeEvent)
    },
    /**
     *完成滚动触发事件
     */
    completeHandle (vertical, horizontal) {
      this.$emit('vshandle-complete', vertical, horizontal)
    },
    /**
     * 指定滚动条滚动到指定位置
     */
    scrollTo (params, speed, easing = 'easeInQuad') {
      this.$refs.vscroll.scrollTo(params, speed, easing)
    },
    /**
     * 刷新滚动条
     */
    refresh () {
      this.$refs.vscroll.refresh()
    }
  }
}
</script>
<style lang="less" >

.con-scroll {
  width:100%;
  &.hasVBar {
    & > .__panel > .__view > .vs-con-main > .v-scroll {
      display: block;
    }
  }
  &.hasHBar {
    & > .__panel > .__view > .vs-con-main > .vs-con-body > .h-scroll {
      display: block;
    }
    & > .__panel > .__view > .vs-con-main > .vs-con-body {
      width: auto !important;
    }
  }
  .v-scroll {
    display: none;
  }
  .h-scroll {
    display: none;
  }
  .vs-con-main {
    display: flex;
    .vs-con-body {
      flex: 1;
      width: 0;
      position: relative;
    }
  }
  &.native-model{
    &.hasVBar {
      & > .__panel  {
       margin-right: 0 !important;
      }
      & > .__rail-is-vertical{
        display: none;
      }
    }
    &.hasHBar{
      & > .__panel  {
         height: 100%  !important;
      }
      & > .__rail-is-horizontal{
        display: none;
      }
    }
  }
}
</style>
