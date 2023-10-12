<template>
  <div class="cards-wrap" v-loading="cardLoading">
    <div class="cards-content" id="cusNav">
      <div class="cards-line" v-for="item in cardData" :key="item.id">
        <div class="cards-line-title" :id="item.id" v-if="item.data.length">{{item.id}}</div>
        <div class="cards-app-box">
          <div class="cards-app-item portal-primary-border-color-hover-lv1" v-for="app in item.data" :key="app.itemWid">
            <div class="app-top">
              <img :src="app.iconLink || errorImg" @error="handleError" />
              <div class="app-detail">
                <div class="itemName ellipsis" :title="app.itemName">{{app.itemName}}</div>
                <div class="itemDept ellipsis">{{app.itemDept}}</div>
                <div class="visitCount">累计访问量：{{app.visitCount}}次</div>
              </div>
              <i class="iconfont icon-favorites" v-if="currentUser" :class="app.favorite?'isFav':''" @click="collectItem(app)"></i>
              <!--<i class="we-icon-star-off" v-if="currentUser" :class="app.favorite?'isFav':''" @click="collectItem(app)"></i>-->
            </div>
            <div class="app-bot">
              <p class="bor portal-primary-backgroundcolor-hover-lv1" :class="app.workGuide && app.isAuthorized !== 0?'':'unClick'" @click="openServiceDetail(app)">办事指南</p>
              <span>|</span>
              <p class="portal-primary-backgroundcolor-hover-lv1" :class="app.onlineServiceType === 2?'':'unClick'" @click="handleOnline(app)">在线办理</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="cards-tab" :style="navStyle">
      <p v-for="(item,index) in tabList" :class="tabIndex==index?'portal-primary-color-lv1 portal-primary-backgroundcolor-lv5':''" @click="goNav(item,index)" :key="index">{{item}}</p>
    </div>
    <we-dialog
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :append-to-body="true"
            :title="detailObj.title"
            :visible.sync="dialogVisible"
            width="420px"
    >
      <div>
        <div class="detail-line">
          <p>服务内容：</p>
          <div>{{detailObj.content || '--'}}</div>
        </div>
        <div class="detail-line">
          <p>责任单位：</p>
          <div>{{detailObj.zrdw || '--'}}</div>
        </div>
        <div class="detail-line">
          <p>责任人：</p>
          <div>{{detailObj.lxr || '--'}}</div>
        </div>
        <div class="detail-line">
          <p>联系电话：</p>
          <div>{{detailObj.phone || '--'}}</div>
        </div>
        <div class="detail-bot"><p class="portal-primary-backgroundcolor-lv1" @click="goOnline">进入服务</p></div>
      </div>
    </we-dialog>
  </div>
</template>

<script>
export default {
  props: [
    "cardData",
    "content_width",
    "currentUser",
    "serviceItemListLimit",
    "showMore",
    "cardLoading",
    "router"
  ],
  data() {
    return {
      scrollNum: 0,
      tempTop: 0,
      detailObj: {},
      isFlags: true,
      dialogVisible: false,
      tabIndex: 0,
      navStyle: {},
      errorImg: window.shell.ErrorImg,
    };
  },
  computed: {
    tabList() {
      let arr = []
      this.cardData.forEach(item => {
        if(item.data && item.data.length) {
          arr.push(item.id)
        }
      })
      return arr
    }
  },
  watch: {
    scrollNum(newVal) {
      setTimeout(() => {
        if(newVal == this.tempTop) {
          if(!this.isFlags) {
            this.isFlags = true
          }
        }
      },20)
    }
  },
  mounted() {
    let con_el = document.getElementById('cusNav');
    if (con_el) {
      setTimeout(() => {
        this.startTop = window.shell.getElementTop(con_el);
      },1000)
    }
    window.shell.on("onScollNew", (ev) => {
      this.initNavScoll(ev.scrollTop);
    });
  },
  methods: {
    initNavScoll(topNum) {
      let indexNavTop = topNum;
      this.scrollNum = topNum;
      if(this.isFlags) {
        for(let i=0;i<this.tabList.length;i++) {
          if(this.tabList[i + 1] && document.getElementById(this.tabList[i]).getBoundingClientRect().top <= 72 && document.getElementById(this.tabList[i + 1]).getBoundingClientRect().top > 72) {
            this.tabIndex = i
            break;
          } else if(document.getElementById(this.tabList[0]).getBoundingClientRect().top >=72) {
            this.tabIndex = 0
            break;
          } else if(!this.tabList[i + 1]) {
            this.tabIndex = this.tabList.length - 1
          }
        }
      }

      if (indexNavTop + 72 >= this.startTop) {
        let con_el = document.getElementById('cusNav');
        let m_left = con_el.offsetWidth / 2;
        this.navStyle = {
          top: '80px',
          position: "fixed",
          left: "50%",
          "margin-left": `${m_left + 60}px`,
        };
      } else {
        this.navStyle = { top: '65px' };
      }
    },
    goNav(item,index) {
      let id = item
      this.isFlags = false
      let scTop = window.shell.getElementTop(document.getElementById(id))
      this.tempTop = scTop - 72
      this.tabIndex = index;
      window.shell.emit("vs-scroll-to-new", { y: scTop - 72 },500);
    },
    handleOnline(item) {
      if(item.onlineServiceType == 2) {
        this.tempObj = item
        window.shell.execCardMethod(
                {
                  cardId: "SYS_CARD_DETAILSOFSERVICEITEMS",
                  cardWid: "31762098404221595",
                  method: "renderData",
                  param: {
                    wid: item.itemWid,
                  },
                },
                (data) => {
                  let obj = data.data
                  this.detailObj = {
                    title: obj.itemName,
                  }
                  obj.serviceItemInfo.baseInfos.forEach(item => {
                    if(item.fieldWid == 'SERVICE_CONTENT') {
                      this.detailObj.content = item.fieldValue
                    } else if(item.fieldWid == '1085200981112393728') {
                      this.detailObj.lxr = item.fieldValue
                    } else if(item.fieldWid == 'DUTY_DEPT_ID') {
                      this.detailObj.zrdw = item.fieldValue
                    }
                  })
                  obj.serviceItemInfo.indptModuls.forEach(item => {
                    if(item.fieldWid == 'CONTACT_PHONE' && item.fieldValue && item.fieldValue.length) {
                      item.fieldValue.forEach((v,i) => {
                        if(i==0) {
                          this.detailObj.phone = v.phone
                        } else {
                          this.detailObj.phone = this.detailObj.phone + ',' + v.phone
                        }
                      })
                    }
                  })
                  this.dialogVisible = true
                }
        );
      } else {
        window.shell.openOnlineDeal(
                {
                  ...item,
                  wid: item.itemWid,
                  name: item.itemName,
                },
                null
        );
      }

    },
    goOnline() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: {
            infoType: 1,
            itemId: this.tempObj.itemWid,
          },
        },
        startTime: new Date().getTime(),
      });
      this.dialogVisible = false
      window.shell.openOnlineDeal(
              {
                ...this.tempObj,
                wid: this.tempObj.itemWid,
                name: this.tempObj.itemName,
              },
              null
      );
    },
    openServiceDetail(item) {
      if (!item.workGuide || item.isAuthorized === 0) {
        return;
      }
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: {
            infoType: 1,
            itemId: item.itemWid,
          },
        },
        startTime: new Date().getTime(),
      });
      window.shell.openServiceItem({
        ...item,
        wid: item.itemWid,
        name: item.itemName,
      });
    },
    collectItem(item) {
      this.$emit("collect-item", item);
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
  }
};
</script>

<style scoped lang="less">
  /deep/.we-dialog__title{
    max-width: 90%;
    overflow: hidden;
    display: block;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .cards-wrap{
    width: 100%;
    display: flex;
    margin-top: 20px;
    position: relative;

    .cards-content{
      width: calc(100% - 44px);
      .cards-line{
        width: 100%;
        .cards-app-box{
          display: flex;
          flex-wrap: wrap;
          .cards-app-item{
            width: calc((100% - 60px)/4);
            height: 148px;
            border: 1px dashed #D9D9D9;
            border-radius: 4px;
            margin-left: 20px;
            margin-bottom: 16px;
            cursor: pointer;
            &:hover{
              //border: 1px dashed #2468F2;
              box-shadow: 0px 12px 12px rgba(0, 0, 0, 0.1);
              .app-top i{
                display: block;
              }
            }
            .app-bot{
              width: 100%;
              height: 38px;
              background: #FAFAFA;
              border-radius: 0px 0px 4px 4px;
              display: flex;
              align-items: center;
              span{
                color: #D9D9D9;
                width: 1px;
              }
              p{
                width: calc(50% - 0.5px);
                height: 38px;
                font-size: 14px;
                line-height: 38px;
                color: #595959;
                text-align: center;
                &:hover{
                  color: #fff!important;
                }
                &.unClick{
                  cursor: not-allowed;
                  color: #BFBFBF;
                }
              }
            }
            .app-top{
              display: flex;
              height: 108px;
              width: 100%;
              position: relative;
              padding: 20px;
              i{
                position: absolute;
                right: 12px;
                top: 50px;
                display: none;
                font-size: 16px;
                color: #BFBFBF;
                &.isFav{
                  color: #ffbc00;
                }
              }
              .app-detail{
                width: calc(100% - 80px);
                margin-left: 15px;
                .visitCount{
                  font-size: 12px;
                  line-height: 18px;
                  margin-top: 12px;
                  color: #BFBFBF;
                }
                .itemDept{
                  font-size: 12px;
                  line-height: 18px;
                  color: #8C8C8C;
                  margin-top: 5px;
                }
                .itemName{
                  font-size: 14px;
                  line-height: 21px;
                  color: #262626;
                }
              }
              img{
                width: 44px;
                height: 44px;
              }
            }
            &:nth-of-type(4n + 1){
              margin-left: 0;
            }
          }
        }
        .cards-line-title{
          font-size: 16px;
          line-height: 21px;
          color: #909090;
          margin-bottom: 12px;
        }
      }
    }
    .cards-tab{
      width: 38px;
      position: absolute;
      top: 33px;
      right: 0;
      p{
        width: 38px;
        height: 20px;
        text-align: center;
        line-height: 20px;
        color: #595959;
        font-size: 12px;
        margin-bottom: 14px;
        border-radius: 4px;
        cursor: pointer;
        &.active{
          background: #F2F7FE;
          color: #2468F2;
        }
      }
    }
  }
  .detail-line{
    display: flex;
    margin-bottom: 14px;
    p{
      width: 74px;
      text-align: right;
      font-size: 14px;
      line-height: 22px;
      color: #8C8C8C;
    }
    div{
      font-size: 14px;
      line-height: 22px;
      color: #262626;
      width: calc(100% - 74px);
    }
  }
  .detail-bot{
    height: 60px;
    border-top: 1px solid #F0F0F0;
    display: flex;
    align-items: center;
    justify-content: center;
    p{
      width: 147px;
      height: 36px;
      text-align: center;
      line-height: 36px;
      color: #fff;
      border-radius: 18px;
      cursor: pointer;
    }
  }
</style>
