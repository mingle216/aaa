<template>
  <div class="service_item_count" ref="serviceCount">
    <div class="item_count_container" v-if="serviceItemList.length">
      <div class="item_count_wrap" v-for="row in rows" :key="row">
        <div
          class="item_col"
          v-for="(item, index) in serviceItemList.slice(
            (row - 1) * showNum,
            showNum * row
          )"
          :key="index"
        >
          <count-item
            :rows="rows"
            :num="item.count"
            :name="item.dataName"
            :showNum="showNum"
            :index="index"
            :len="
              serviceItemList.slice((row - 1) * showNum, showNum * row).length
            "
            :boxWidth="boxWidth"
            :dataId="item.dataId"
          />
        </div>
      </div>
    </div>
    <empty-con :tip="GetLanguageValue('SYS_CARD_SERVICEITEMCOUNT', 'nodata', '暂无数据')" v-else />
  </div>
</template>

<script>
import CountItem from './components/CountItem'
import TrackMixin from './mixins/track.js';
export default {
  name: 'serviceitemcounts',
  components: {
    CountItem
  },
  props: {
    index: Number,
    router: Object
  },
  mixins: [TrackMixin],
  data() {
    return {
      checked: false,
      serviceItemList: [],
      showNum: 6,
      config: { heightFlag: 1, height: 0 },
      boxWidth: 0,
      templateCode: window.shell.getTemplateCode()
    }
  },
  mounted () {
    // work模板单独处理
    if (this.templateCode === 'SYS_TEMPLATE_WORK') {
      this.boxWidth = this.$refs.serviceCount.offsetWidth;
    } else {
      this.boxWidth = 1152
    }
  },
  computed: {
    rows() {
      return Math.ceil(this.serviceItemList.length / this.showNum)
    }
  },
  methods: {
    check() {
      this.checked = !this.checked
      window.shell.emit('check-card', this.checked)
    },
    getCardData() {
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: 'render2',
          param: {}
        },
        res => {
          if (res.errcode === '0') {
            this.showNum = res.data.columns || 6
            const list = res.data.serviceItemList || []
            this.serviceItemList = list.map(el => {
              return {
                ...el,
                count: !['onlinePercent', 'itemScore'].includes(el.dataId)
                  ? Number(el.count).toLocaleString('en')
                  : el.count
              }
            })
            this.$nextTick(() => {
              this.boxWidth = this.$refs.serviceCount && this.$refs.serviceCount.offsetWidth || 1152
            })
          }
          this.loadedEndTrack(); // 加载结束埋点
        }
      )
    },
    handleResize() {
      this.boxWidth = this.$refs.serviceCount.offsetWidth;
    }
  },
  async created() {
    if (this.templateCode === 'SYS_TEMPLATE_WORK') {
      window.addEventListener("resize", this.handleResize);
      this.$once("hook:beforeDestroy", () => {
        window.removeEventListener("resize", this.handleResize);
      });
    }
    
    await this.getCardData()
  }
}
</script>

<style lang="less" scoped>
.item_count_container {
  /* background: url("./assets/images/bg.png") no-repeat center center; */
  background-size: cover;
  padding: 20px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.item_count_wrap {
  height: 102px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 1200px;
}
.item_count_wrap + .item_count_wrap {
  padding-top: 10px;
  box-sizing: content-box;
}
.item_col:not(:last-child) .count_item {
  &:after {
    content: '';
    position: absolute;
    right: 0;
    top: calc(50% - 17px);
    width: 0;
    height: 34px;
    /* border-right: 1px dashed #d9d9d9; */
  }
}
</style>
