 
<template>
  <div class="card-link" v-loading="loading">
    <div class="link-content">
    <template v-if="configInfo.linkDisplayNumSelect === '2'">
      <ul class="link-list-temp2" >
         <li v-for="(link,index) in linkList" :key="`link_${index}`" flex-col-center @click="openLink(link)">
            <template v-if="link.iconType ==='1'">
                <img :src="link.iconUrl" @error="imgError" height="24" width="24" mr-8>
            </template>
            <template v-else>
                <i style="color: #9aa1b0" :class="link.iconUrl +' '+ link.iconClass" mr-8 class="link-icon-font iconfont"></i>
            </template>
            <span class="title text__ellipsis">{{getI18nMessage(link.label)}}</span>
          </li>
      </ul>
    </template>
      <template v-else>
        <ul class="link-list-temp">
          <li v-for="(link,index) in linkList" :key="`link_${index}`" flex-col-center  @click="openLink(link)">
            <template v-if="link.iconType ==='1'">
                <img :src="link.iconUrl" @error="imgError" height="32" width="32" mr-8>
            </template>
            <template v-else>
                <i style="color: #9aa1b0" :class="link.iconUrl +' '+ link.iconClass" mr-8 class="link-icon-font iconfont"></i>
            </template>
            <span class="title text__ellipsis">{{getI18nMessage(link.label)}}</span>
          </li>
        </ul>
      </template>
      <we-button :style="{'color': defaultColor, 'margin-top': configInfo.linkDisplayNumSelect === '2' ? '0.32rem' : '0px'}" class="expand-btn"  block v-show="expandShow" @click="expandLinkList">
        {{isExpand ? $Lan('CUS_CARD_ZTZL_h5', 'packUp', '收起') : $Lan('CUS_CARD_ZTZL_h5', 'showMore', '查看更多')}}
        <we-icon :name="`arrow-${isExpand?'up':'down'}`" size="12"/>
      </we-button>
    </div>
    <EmptyCon v-if="!linkList.length" :tip="$Lan('CUS_CARD_ZTZL_h5', 'noData', '暂无数据')"></EmptyCon>
  </div>
</template>

<script>
import TrackMixin from './mixins/track.js';
export default {
  components: {  },
  name: "",
  props: {
    index: Number,
    router: Object,
  },
  mixins: [TrackMixin],
  data() {
    return {
     loading:false,
     expandShow:false,
     isExpand:false,
     linkList:[],
     configInfo: {},
     defaultColor: 'white',
    }
  },
  mounted() {
    this.defaultColor = JSON.parse(JSON.parse(window.shell.getPageData().pageContext.showProgrammeEntity.templateConfig)).themeColorSetting['portal-primary-color-lv1']
    if (window.shell) {
      this.getCardData();
    }
  },
  beforeDestroy() {},
  methods: {
    getCardData() {
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "renderData",
          param: {},
        },
        (data) => {
          this.loading = false;
          this.configInfo = data.data.configInfo;
          this.showLinkExpand()
          this.loadedEndTrack(); // 加载结束埋点
        }
      );
    },

    imgError(event) {
        let img = event.srcElement;
        img.src = window.shell.ErrorImg;
        img.onerror = null;
      },

    getI18nMessage(labels) {
        if (!this.$i18n.locale) {
          return labels[0].langValue
        }
        const res = labels.filter(label => label.langKey === this.$i18n.locale)
        const defaultLabel = labels.filter(label => label.langKey === 'zh_CN')
        if (res.length > 0) {
          return res[0].langValue || defaultLabel[0].langValue
        }
        return defaultLabel[0].langValue
      },

    openLink(link) {
      this.handleClickTrack({
        infoType: 4,
        filterInfo: {
          linkName: this.getI18nMessage(link.label)
        }
      });// 点击埋点
      window.shell.openPage(link.url, 1, 2)
    },

    // 展开/收取
    expandLinkList(){
      switch (this.isExpand) {
        case false:
            this.linkList = this.configInfo.linkList
            this.isExpand = true
          break;
        case true:
            if(this.configInfo.linkDisplayRadio === 1 ){
               this.linkList = this.configInfo.linkList.slice(0,10)
            }else{
               this.linkList = this.configInfo.linkList.slice(0,20)
            }
            this.isExpand = false
          break;
        default:
          break;
      }
      this.handleClickTrack();// 点击埋点
    },

    // 校验是否满足展开条件
    showLinkExpand(){
      const total = this.configInfo.linkList.length;
      if(this.configInfo.linkDisplayRadio === 1){
        this.linkList = total > 10 ? this.configInfo.linkList.slice(0,10) : this.configInfo.linkList 
        this.expandShow = total > 10 ? true : false
      }
      if(this.configInfo.linkDisplayRadio === 2 ){
        this.linkList = total > 20 ? this.configInfo.linkList.slice(0,20) : this.configInfo.linkList 
        this.expandShow = total > 20 ? true : false
      }
    }
  },
  watch: {
    loading(val) {
      if (val) {
        setTimeout(() => {
          this.loading = false;
        }, 5000);
      }
    },
  },
};
</script>

<style lang="less" scoped>
[flex-col-center]{
  display: flex;
  align-items: center;
}
[mr-8]{
  margin-right: 8px;
}
.card-link {
  .link-content{
    padding:12px 17px 0;
    .link-list-temp{
      margin-bottom:12px;
      li{
        height: 50px;
        .link-icon-font{
          font-size:32px;
        }
      }
    }
    .link-list-temp2{
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      li{
        width: calc(~'(100% -  13px) / 2 ');
        background: #F6F6F8;
        border-radius: 4px;
        margin-bottom:12px;
        height: 48px;
        padding: 0 12px;
        .link-icon-font{
          font-size:24px;
        }
      }
    }
    
    .title{
      // font-family: PingFangSC-Regular;
      font-size: 16px;
      color: #102645;
      letter-spacing: 0;
      width: calc(~'100% - 40px');
    }

    .expand-btn{
      height: 48px;
      background: #F9FAFB;
      border-radius: 4px;
      color:#307AFB;
      border:none;
      font-size:16px;
    }
  }
}
</style>
