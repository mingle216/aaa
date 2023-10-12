<template>
  <div class="cus_item_count" ref="serviceCount">
    <div class="count-item">
      <div class="count-item-left">
        <p>入驻办理事项</p>
        <div><span>{{rzblsx}}</span>个</div>
      </div>
      <img src="./assets/icon1.png" alt="">
    </div>
    <div class="count-item">
      <div class="count-item-left">
        <p>教师可办事项</p>
        <div><span>{{jssxtj}}</span>个</div>
      </div>
      <img src="./assets/icon5.png" alt="">
    </div>
    <div class="count-item">
      <div class="count-item-left">
        <p>学生可办事项</p>
        <div><span>{{bkssx}}</span>个</div>
      </div>
      <img src="./assets/icon6.png" alt="">
    </div>
    <div class="count-item">
      <div class="count-item-left">
        <p>进驻服务部门</p>
        <div><span>{{jzfwbm}}</span>个</div>
      </div>
      <img src="./assets/icon4.png" alt="">
    </div>

    <div class="count-item">
      <div class="count-item-left">
        <p>服务大厅正在办理事项</p>
        <div><span>{{zzblsx}}</span>个</div>
      </div>
      <img src="./assets/icon2.png" alt="">
    </div>
    <div class="count-item">
      <div class="count-item-left">
        <p>服务大厅完成办理事项</p>
        <div><span>{{wcblsx}}</span>个</div>
      </div>
      <img src="./assets/icon3.png" alt="">
    </div>
  </div>
</template>

<script>
export default {
  name: 'serviceitemcounts',
  components: {
  },
  props: {
    index: Number,
    router: Object
  },
  data() {
    return {
      rzblsx: 0,
      zzblsx: 0,
      wcblsx: 0,
      jzfwbm: 0,
      jssxtj: 0,
      bkssx: 0,
      checked: false,
      serviceItemList: [],
      showNum: 6,
      config: { heightFlag: 1, height: 0 },
      boxWidth: 1152
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
            list.forEach(item => {
              if(item.dataId == 'currentItem') {
                this.rzblsx = item.count
              }
              if(item.dataId == 'currentDept') {
                this.jzfwbm = item.count
              }
              if(item.dataId == '1') {
                this.jssxtj = item.count
              }
              if(item.dataId == '1049824356289753088') {
                this.bkssx = item.count
              }
              if(item.dataId == 'currentCount') {
                this.zzblsx = item.count
              }
              if(item.dataId == 'doneCount') {
                this.wcblsx = item.count
              }
            })
          }
        }
      )
    }
  },
  async created() {
    await this.getCardData()
  },
}
</script>

<style lang="less" scoped>
  .cus_item_count{
    display: flex;
    flex-wrap: wrap;
    .count-item{
      width: calc((100% - 24px)/3);
      height: 67px;
      display: flex;
      align-items: center;
      border-radius: 8px;
      margin-left: 12px;
      position: relative;
      &:nth-of-type(3n + 1){
        margin-left: 0;
      }
      &:nth-of-type(1){
        background: #FFF4ED;
      }
      &:nth-of-type(2){
        background: #FAF4FE;
      }
      &:nth-of-type(3){
        background: #F0F5FF;
      }
      &:nth-of-type(4){
        background: #F0F5FF;
        margin-top: 12px;
      }
      &:nth-of-type(5){
        background: #F4FBEF;
        margin-top: 12px;
      }
      &:nth-of-type(6){
        background: #FFF4ED;
        margin-top: 12px;
      }
      img{
        position: absolute;
        right: 20px;
        top: 27px;
        width: 34px;
        height: 34px;
      }
      .count-item-left{
        margin-left: 20px;
        position: relative;
        z-index: 1;
        div{
          color: #8C8C8C;
          font-size: 14px;
          line-height: 22px;
          margin-top: 8px;
          span{
            font-weight: 700;
            font-size: 18px;
            line-height: 26px;
            color: #16181A;
            margin-right: 4px;
          }
        }
        p{
          color: #595959;
          font-size: 14px;
          line-height: 22px;
        }
      }
    }
  }
</style>
