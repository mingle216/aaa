<template>
  <div class="card__personalData">
    <AutoContainer class="width-full" :con-type="type" :con-height="height">
      <template v-if="hasLogin">
        <div class="card__personalData_box flex flex-wrap" v-if="personalInfo.length">
          <div class="card__personalData__item_wrapper" v-for="(item,index) in personalInfo" 
            :key="index"
            :class="{'ml-20':index%2,'mb-20':index < personalInfo.length-2}">
            <div v-if="!(item.isEmail && (!item.mainInfo || !item.mainInfo.length))"
             @click="handlerEnterClick(item)" 
             :class="{'portal-primary-backgroundcolor-hover-lv5':filterClickUrlAble(item),'cursor-pointer':filterClickUrlAble(item)}">
              <div class="card__personalData__item_title">
                <div class="flex items-center flex-1 overflow-hidden">   
                  <img :src="item.iconUrl"  @error="handleError"/>
                  <span class="card__personalData__item_title_text overflow-ellipsis portal-font-color-lv2" :title="item.title">{{item.title}}</span>
                </div>
              </div>
              <TLoading class="card__personalData__item_body" :loading="loading || item.loading">
                <div
                  class="width-full overflow-ellipsis" 
                  :class="(item.linkUrl || item.isEmail) ? 'portal-primary-color-lv1':'portal-font-color-lv1'" 
                  >
                  {{item.mainInfo}}
                </div>
              </TLoading>
              <div class="card__personalData__item_bottom portal-font-color-lv2">
                <p>
                  <TLoading :loading="loading || item.loading">
                    <span class="width-full overflow-ellipsis inline-block" :title="item.subInfo">{{item.subInfo}}</span>
                  </TLoading>
                </p>
              </div>
            </div>
          
            <div v-else>
              <div class="card__personalData__item_title">
                <div class="flex items-center width-full overflow-hidden">   
                  <img :src="item.iconUrl"  @error="handleError"/>
                  <span class="card__personalData__item_title_text overflow-ellipsis portal-font-color-lv2" :title="item.title">{{item.title}}</span>
                </div>
              </div>
              <div class="card__personalData__item_body portal-font-color-lv1">
                <TLoading :loading="loading || item.loading">
                  <span>当前无邮箱</span>
                </TLoading>
              </div>
              <div class="card__personalData__item_bottom portal-font-color-lv2">
                <TLoading :loading="loading || item.loading">请联系管理员</TLoading>
              </div>
            </div>
          </div>
        </div>
        <empty-con v-else :tip="lg.noPersonalData"></empty-con>
      
      </template>

      <TNoLogin v-else></TNoLogin>
    </AutoContainer>
  </div>
</template>

<script>
import TLoading from './components/TLoading'
import TNoLogin from './components/TNoLogin'
import languages from './utils/languages'
export default {
  components:{
    TLoading,
    TNoLogin
  },
  data(){
    return {
      isOnEmail:false,
      emailConfig:{
        show:false,
        options:{}
      },
      lg:languages[this.$i18n.locale],
      hasLogin:window.shell.getUserInfo() ? true : false,
      personalInfo:[],
      // router: {
      //   cardId:'SYS_CARD_PERSONALDATA',
      //   cardWid:'SYS_CARD_PERSONALDATA'
      // },
      // rightScroll: { barKeepShow: true, scrollingX: true },
      height: 500, //容器高度，type为1时该值无效，type为2时最大高度，type为3时固定高度
      type: 1, //1：代表自适应（无最大限制）2：代表自适应（有最大限制）3：固定高度
      errorImg: window.shell.ErrorImg,
      notHasEmail:false,
      loading:false,
      cardData:{},
      getListInterval:{},
    }
  },
  props: {
    index: Number,
    router: Object,
  },
  mounted(){
    this.initData()
    this.getAllInfo()
    this.getListInterval = setInterval(()=>{
      this.getAllInfo()
    },300000)
  },
  destroyed(){
    clearInterval(this.getListInterval)
  },
  methods:{
    initData(){
        window.shell.execCardMethod(
        {
          ...this.router,
          method: "renderData",
        },
        (data) => {
          this.cardData = data.data;
          console.log(this.cardData)
        })
    },
    filterClickUrlAble(item){
      return item.linkUrl || item.isEmail
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null;
    },
    async handlerEnterClick(item){
      let linkUrl = item.linkUrl
      if(item.isEmail){
        let {cardId, cardWid} = this.router
        let { data } = await window.shell.execCardMethod(
        { cardId, 
          cardWid, 
          method:"getLinkUrl",
        })
        linkUrl = data
      }
      if(linkUrl){
        window.open(linkUrl,'_blank')
      }
    },
    async setPersonDetail(item,index){
      if(+item.needRetrieve !== 1){
         this.$set(this.personalInfo,index,item)
        return
      }
      this.$set(this.personalInfo,index,{title:item.title,iconUrl:item.iconUrl,loading:true})
      let {cardId, cardWid} = this.router
      let { data } = await window.shell.execCardMethod(
        { cardId, 
          cardWid, 
          method: "getPersonalDataDetail",
          param:{wid:item.wid}
        })
      this.$set(this.personalInfo,index,data || [])
    },
    getAliiInfoByItem(item){
      let {cardId, cardWid} = this.router
      window.shell.execCardMethod(
        { cardId, cardWid, method: "getPersonalDataList" },
        ({ data }) => {
          data.forEach((el,i) => {
            if(item.wid === el.wid) {
              this.setPersonDetail(el,i)
            } 
          })
        }
      );
    },
    getAllInfo(){
      this.loading = true
      let {cardId, cardWid} = this.router
      window.shell.execCardMethod(
        { cardId, cardWid, method: "getPersonalDataList" },
        ({ data }) => {
          this.loading = false
          data = data || []
          if(data.length === 0 || (this.personalInfo.length !== data.length)){
            this.personalInfo = []
          }
          data.forEach((el,index) => {
            this.setPersonDetail(el,index)
          })
        }
      );
    }
  }
}
</script>

<style lang="less">
.card__personalData {
  max-height: 500px;
  min-height: 100px;
  padding-top: 12px;
  display: flex;
  // .card__personalData_box {

  //   justify-content: space-between;
  // }
  .text-right {
    text-align: right;
  }
  .card__personalData__item_title_btn-status {
    display: inline-block;
    width: 60px;
  }
  .event-none {
    pointer-events: none;
  }
  .overflow-hidden {
    overflow: hidden;
  }
  .overflow-ellipsis {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .justify-between {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .inline-block {
    display: inline-block;
  }
  .flex-1 {
    flex: 1 1 auto;
  }
  .mx-4 {
    margin: 0 4px;
  }
  .cursor-pointer {
    cursor: pointer;
  }
  .flex {
    display: flex;
  }
  .width-full {
    width: 100%;
  }
  .height-full {
    height: 100%;
  }
  .flex-wrap {
    display: flex;
    flex-wrap: wrap;
  }
  .justify-center {
    justify-content: center;
    display: flex;
  }
  .ml-4 {
    margin-left: 4px;
  }
  .ml-20 {
    margin-left: 20px;
  }
  .mb-20 {
    margin-bottom: 20px;
  }
  .card__personalData__item_wrapper {
    background: #f9fafb;
    border-radius: 4px;
    width: 218px;
    height: 100px;
    padding: 15px 14px 11px 20px;
    font-size: 14px;
    display: flex;
    flex-direction: column;
    &:first-child {
      margin-left: 0;
    }
    .card__personalData__item_title {
      display: flex;
      justify-content: space-between;
      img {
        width: 14px;
        height: 14px;
        margin-right: 8px;
      }
      .card__personalData__item_title_text {
        width: calc(100% - 26px);
      }
    }
    .card__personalData__item_body {
      font-size: 16px;
      margin-top: 6px;
      font-weight: 600;
      &.lesson__body {
        display: flex;
        align-items: center;
        margin-top: 0;
        height: 100%;
      }
    }
    .card__personalData__item_bottom {
      margin-top: 6px;
      line-height: 20px;
      font-size: 12px;
    }
  }
}
</style>