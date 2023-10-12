<template>
  <div
    class="gateway-card position-relateive"
    :class="[cardData.showTitle?'':'card-t-16',cardData.cardId === 'SYS_CARD_CALENDAR' && !cardData.showTitle ? 'calenderCard' : '']"
  >
    <h3
      class="card-sys-title portal-font-color-lv1 ellipsis"
      v-if="cardData.showTitle"
    >
      <span>
        {{ cardTitle }}
        <span style="margin-left: 4px">{{
          num[cardData.cardId] && num[cardData.cardId]["num"]
        }}</span></span
      >
      <template v-if="cardData.cardId == 'SYS_CARD_MYFAVORITESSERVICEITEM' || cardData.cardId == 'CUS_CARD_SEUMYFAVORITESX'">
        <span class="fav-dingyue portal-primary-color-lv1" @click="showSer"><i class="iconfont icon-icon-dingyue"></i>收藏</span>
      </template>
      <template
        v-if="
          cardData.layoutCardTitle.layoutCardLink &&
            cardData.layoutCardTitle.layoutCardLink.length
        "
      >
        <card-link
          :cardLinkData="cardData.layoutCardTitle.layoutCardLink"
          :cardData="cardData"
          :isshow="true"
        ></card-link>
      </template>
    </h3>
    <remote-component
      :url="getCardUrl(cardData)"
      :router="cardData"
      :colWidth="colWidth"
      :i18n="$i18n"
    />
    <we-dialog
            title="全部事项"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :append-to-body="true"
            :visible.sync="manageDialogshow"
            :width="dialogWidth"
    >
      <div style="height: calc(85vh - 150px);overflow: auto;">
        <we-input placeholder="请输入内容" v-model="searchServiceItemParam.searchKey" class="input-with-select" @keyup.enter.native="handleSearch">
          <we-button slot="append" type="primary" @click.native="handleSearch">搜索</we-button>
        </we-input>
        <div class="cus-filter">
          <div class="cus-filter-item">
            <div class="cus-filter-item-lable">服务主题:</div>
            <div class="fliter-items">
              <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.categoryWids == ''?'active':''" @click="changeSub('')">全部</div>
              <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.categoryWids == item.subjectWid?'active':''" v-for="item in subList" :key="item.subjectWid" @click="changeSub(item.subjectWid)">{{item.subjectName}}</div>
            </div>
          </div>
          <div class="cus-filter-item">
            <div class="cus-filter-item-lable">责任部门:</div>
            <div class="fliter-items">
              <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.deptWids == ''?'active':''" @click="changeDept('')">全部</div>
              <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.deptWids == item.deptWid?'active':''" @click="changeDept(item.deptWid)" v-for="item in deptList" :key="item.deptWid" :title="item.deptName">{{item.deptName}}</div>
            </div>
          </div>
          <div class="cus-filter-item">
            <div class="cus-filter-item-lable">服务对象:</div>
            <div class="fliter-items">
              <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.roleWids == ''?'active':''" @click="changeRole('')">全部</div>
              <div class="portal-primary-backgroundcolor-hover-lv5" :class="searchServiceItemParam.roleWids == item.roleWid?'active':''" @click="changeRole(item.roleWid)" v-for="item in roleList" :key="item.roleWid">{{item.roleName}}</div>
            </div>
          </div>
        </div>
        <div class="cards-content" v-loading="lodings">
          <div class="cards-line" v-for="item in appDataList" :key="item.id">
            <div class="cards-line-title" :id="item.id" v-if="item.data.length">{{item.id}}</div>
            <div class="cards-app-box">
              <div class="cards-app-item" v-for="app in item.data" :key="app.itemWid">
                <div class="app-remark">
                  <img :src="app.iconLink || errorImg" @error="handleError" />
                  <p class="ellipsis" :title="app.itemName">{{app.itemName}}</p>
                </div>
                <div class="app-fav-item">
                  <img :src="app.iconLink || errorImg" @error="handleError" />
                  <div class="app-fav-cneter">
                    <div class="itemName ellipsis">{{app.itemName}}</div>
                    <div class="itemDept ellipsis">{{app.itemDept}}</div>
                    <div class="visitCount">{{app.visitCount}}次访问</div>
                  </div>
                  <i class="iconfont icon-favorites" :class="app.favorite?'isFav':''" @click="collectItem(app)"></i>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </we-dialog>
  </div>
</template>

<script>
/* eslint-disable no-debugger */
import getCardUrl from "../minxins/index.js";
import {pinyin} from "../../../utils/pinyin";
export default {
  name: "card",
  mixins: [getCardUrl], //混入
  props: {
    cardData: Object,
    colWidth: Number,
  },
  computed: {
    cardTitle() {
      const zhTitle = this.cardData.layoutCardTitle.cardTitle || "";
      const cardTitleLang =
        (this.cardData.layoutCardTitle &&
          this.cardData.layoutCardTitle.cardTitleLang) ||
        [];
      const temp = cardTitleLang.find(
        (el) => el.langCode === this.$i18n.locale
      );
      return (temp && temp.langName) || zhTitle;
    },
  },
  data() {
    return {
      searchKey: '',
      errorImg: window.shell.ErrorImg,
      dialogWidth: '1100px',
      manageDialogshow: false,
      lodings: false,
      appDataList: [{
        id: 'ABC',
        data: []
      },{
        id: 'DEF',
        data: []
      },{
        id: 'GHI',
        data: []
      },{
        id: 'JKL',
        data: []
      },{
        id: 'MNO',
        data: []
      },{
        id: 'PQR',
        data: []
      },{
        id: 'STU',
        data: []
      },{
        id: 'VWX',
        data: []
      },{
        id: 'YZ',
        data: []
      }],
      subList: [],
      deptList: [],
      roleList: [],
      searchServiceItemParam: {
        searchKey: '',
        categoryWids: '', //服务主题wid（多个逗号隔开）
        deptWids: '', //责任部门wid（多个逗号隔开）
        roleWids: '', //服务对象wid（多个逗号隔开
        srvDeptWids: '', //服务部门wid（多个逗号隔开）
        dimensions: '', //分类维度wid（多个逗号隔开）
        availableOnline: false, // 可在线办理
        orderByVisitCount: false // 访问量排序
      },
    };
  },
  created() {
    this.getNum([this.cardData]);
  },
  methods: {
    collectItem(item) {
      window.shell.collectServiceItem({
        cardId: this.$options.name,
        id: item.itemWid, //事项ID 收藏或取消收藏时用
        operate: item.favorite ? 0 : 1 // 收藏操作标识 0：取消收藏 1：收藏
      }).then(() => {
        item.favorite = !item.favorite
      })
    },
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    changeRole(id) {
      if(this.searchServiceItemParam.roleWids != id) {
        this.searchServiceItemParam.roleWids = id
        this.searchParams('search')
      }
    },
    changeDept(id) {
      if(this.searchServiceItemParam.deptWids != id) {
        this.searchServiceItemParam.deptWids = id
        this.searchParams('search')
      }
    },
    handleSearch() {
      this.searchParams('search')
    },
    changeSub(id) {
      if(this.searchServiceItemParam.categoryWids != id) {
        this.searchServiceItemParam.categoryWids = id
        this.searchParams('search')
      }
    },
    searchParams() {
      this.appDataList = [{
        id: 'ABC',
        data: []
      },{
        id: 'DEF',
        data: []
      },{
        id: 'GHI',
        data: []
      },{
        id: 'JKL',
        data: []
      },{
        id: 'MNO',
        data: []
      },{
        id: 'PQR',
        data: []
      },{
        id: 'STU',
        data: []
      },{
        id: 'VWX',
        data: []
      },{
        id: 'YZ',
        data: []
      }]
      this.lodings = true
      window.shell.execCardMethod(
              {
                cardId: "SYS_CARD_ALLSERVICEITEM",
                cardWid: "4390829058110519",
                method: 'searchServiceItem',
                param: {
                  ...this.searchServiceItemParam,
                  pageNumber: 1,
                  pageSize: 1
                }
              },
              res => {
                let list = []
                if(res.data.serviceItemList) {
                  res.data.serviceItemList.forEach(item => {
                    list = [...list,...item.datas]
                  })
                }
                list.forEach(app => {
                  let pyStr = this.chineseToPinYin(app.itemName).substr(0,1)
                  if(pyStr == 'A') {
                    this.appDataList[0].data.push(app)
                  } else if(pyStr == 'B') {
                    this.appDataList[0].data.push(app)
                  } else if(pyStr == 'C') {
                    this.appDataList[0].data.push(app)
                  } else if(pyStr == 'D') {
                    this.appDataList[1].data.push(app)
                  } else if(pyStr == 'E') {
                    this.appDataList[1].data.push(app)
                  } else if(pyStr == 'F') {
                    this.appDataList[1].data.push(app)
                  } else if(pyStr == 'G') {
                    this.appDataList[2].data.push(app)
                  } else if(pyStr == 'H') {
                    this.appDataList[2].data.push(app)
                  } else if(pyStr == 'I') {
                    this.appDataList[2].data.push(app)
                  } else if(pyStr == 'J') {
                    this.appDataList[3].data.push(app)
                  } else if(pyStr == 'K') {
                    this.appDataList[3].data.push(app)
                  } else if(pyStr == 'L') {
                    this.appDataList[3].data.push(app)
                  } else if(pyStr == 'M') {
                    this.appDataList[4].data.push(app)
                  } else if(pyStr == 'N') {
                    this.appDataList[4].data.push(app)
                  } else if(pyStr == 'O') {
                    this.appDataList[4].data.push(app)
                  } else if(pyStr == 'P') {
                    this.appDataList[5].data.push(app)
                  } else if(pyStr == 'Q') {
                    this.appDataList[5].data.push(app)
                  } else if(pyStr == 'R') {
                    this.appDataList[5].data.push(app)
                  } else if(pyStr == 'S') {
                    this.appDataList[6].data.push(app)
                  } else if(pyStr == 'T') {
                    this.appDataList[6].data.push(app)
                  } else if(pyStr == 'U') {
                    this.appDataList[6].data.push(app)
                  } else if(pyStr == 'V') {
                    this.appDataList[7].data.push(app)
                  } else if(pyStr == 'W') {
                    this.appDataList[7].data.push(app)
                  } else if(pyStr == 'X') {
                    this.appDataList[7].data.push(app)
                  } else if(pyStr == 'Y') {
                    this.appDataList[8].data.push(app)
                  } else if(pyStr == 'Z') {
                    this.appDataList[8].data.push(app)
                  }
                })
                this.lodings = false
              }
      )
    },
    initdatas() {
      this.lodings = true
      window.shell.execCardMethod({
        cardId: "SYS_CARD_ALLSERVICEITEM",
        cardWid: "4390829058110519",
        method: 'renderData',
        param: {
          pageNumber:1,
          pageSize: 1
        }
      }).then(res => {
        let list = []
        if(res.data.serviceItemList) {
          res.data.serviceItemList.forEach(item => {
            list = [...list,...item.datas]
          })
        }
        if(res.data.filterItem) {
          res.data.filterItem.forEach(item => {
            if(item.propertyWid == 'subjectWid') {
              this.subList = item.data
            }
            if(item.propertyWid == 'deptWid') {
              this.deptList = item.data
            }
            if(item.propertyWid == 'roleWid') {
              this.roleList = item.data
            }
          })
        }
        list.forEach(app => {
          let pyStr = this.chineseToPinYin(app.itemName).substr(0,1)
          if(pyStr == 'A') {
            this.appDataList[0].data.push(app)
          } else if(pyStr == 'B') {
            this.appDataList[0].data.push(app)
          } else if(pyStr == 'C') {
            this.appDataList[0].data.push(app)
          } else if(pyStr == 'D') {
            this.appDataList[1].data.push(app)
          } else if(pyStr == 'E') {
            this.appDataList[1].data.push(app)
          } else if(pyStr == 'F') {
            this.appDataList[1].data.push(app)
          } else if(pyStr == 'G') {
            this.appDataList[2].data.push(app)
          } else if(pyStr == 'H') {
            this.appDataList[2].data.push(app)
          } else if(pyStr == 'I') {
            this.appDataList[2].data.push(app)
          } else if(pyStr == 'J') {
            this.appDataList[3].data.push(app)
          } else if(pyStr == 'K') {
            this.appDataList[3].data.push(app)
          } else if(pyStr == 'L') {
            this.appDataList[3].data.push(app)
          } else if(pyStr == 'M') {
            this.appDataList[4].data.push(app)
          } else if(pyStr == 'N') {
            this.appDataList[4].data.push(app)
          } else if(pyStr == 'O') {
            this.appDataList[4].data.push(app)
          } else if(pyStr == 'P') {
            this.appDataList[5].data.push(app)
          } else if(pyStr == 'Q') {
            this.appDataList[5].data.push(app)
          } else if(pyStr == 'R') {
            this.appDataList[5].data.push(app)
          } else if(pyStr == 'S') {
            this.appDataList[6].data.push(app)
          } else if(pyStr == 'T') {
            this.appDataList[6].data.push(app)
          } else if(pyStr == 'U') {
            this.appDataList[6].data.push(app)
          } else if(pyStr == 'V') {
            this.appDataList[7].data.push(app)
          } else if(pyStr == 'W') {
            this.appDataList[7].data.push(app)
          } else if(pyStr == 'X') {
            this.appDataList[7].data.push(app)
          } else if(pyStr == 'Y') {
            this.appDataList[8].data.push(app)
          } else if(pyStr == 'Z') {
            this.appDataList[8].data.push(app)
          }
        })
        this.lodings = false
      })
    },
    chineseToPinYin(str) {
      let l2 = str.length
      let i1 = ''
      let reg = new RegExp('[a-zA-Z0-9]');
      for (let i=0;i<l2;i++) {
        let val = str.substr(i,1)
        let name = this.arraySearch(val,pinyin)
        if(reg.test(val)) {
          i1 += val
        } else if(name !== false) {
          i1 += name
        }
      }
      i1 = i1.replace(/ /g,'-')
      while (i1.indexOf('--') > 0) {
        i1 = i1.replace('--','-')
      }
      return i1
    },
    arraySearch(l1,l2) {
      for(let name in l2) {
        if(pinyin[name].indexOf(l1) !== -1) {
          return this.ucfirst(name)
        }
      }
    },
    ucfirst(l1) {
      if(l1.length > 0) {
        let first = l1.substr(0,1).toUpperCase()
        let spare = l1.substr(1,l1.length)
        return first + spare
      }
    },
    showSer() {
      if(!this.subList.length) {
        this.initdatas()
      }
      this.manageDialogshow = true
    }
  }
};
</script>

<style scoped lang="less">
  .input-with-select{
    width: 400px;
    margin-bottom: 20px;
  }
  .cards-content{
    width: 100%;
    margin-top: 10px;

    .cards-line{
      width: 100%;
      .cards-app-box{
        display: flex;
        flex-wrap: wrap;
        .cards-app-item{
          width: calc((100% - 48px)/4);
          height: 76px;
          position: relative;
          margin-left: 16px;
          cursor: pointer;
          &:hover{
            .app-fav-item{
              display: flex;
            }
            .app-remark{
              display: none;
            }
          }
          .app-fav-item{
            display: none;
            width: 100%;
            height: 102px;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 2;
            padding-top: 16px;
            background: #FFFFFF;
            box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
            border-radius: 4px;
            i{
              margin-top: 14px;
              color: #BFBFBF;
              &.isFav{
                color: #ffbc00;
              }
            }
            .app-fav-cneter{
              width: calc(100% - 100px);
              .visitCount{
                font-size: 14px;
                line-height: 18px;
                margin-top: 8px;
                color: #BFBFBF;
              }
              .itemDept{
                font-size: 14px;
                line-height: 18px;
                color: #8C8C8C;
              }
              .itemName{
                font-size: 16px;
                line-height: 21px;
                color: #262626;
                margin-bottom: 5px;
              }
            }
            img{
              width: 44px;
              height: 44px;
              margin: 0 12px;
            }
          }
          .app-remark{
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            p{
              width: calc(100% - 72px);
              color: #262626;
            }
            img{
              width: 44px;
              padding-left: 12px;
              margin-right: 12px;
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
  .cus-filter{
    .cus-filter-item{
      display: flex;
      align-items: flex-start;
      .fliter-items{
        display: flex;
        flex-wrap: wrap;
        flex: 1;
        width: 0;
        div{
          margin-left: 8px;
          height: 30px;
          padding: 0 12px;
          line-height: 30px;
          color: #262626;
          font-size: 15px;
          cursor: pointer;
          margin-bottom: 12px;
          &.active{
            background: #F2F7FE;
            border-radius: 4px;
            color: #2468F2;
          }
        }
      }
      .cus-filter-item-lable{
        color: #595959;
        font-size: 14px;
        line-height: 30px;
        width: 70px;
      }
    }
  }
.gateway-card {
  background: #fff;
}
.card-sys-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  line-height: 50px;
  font-weight: bold;
  position: relative;
  box-shadow: inset 0 -1px 0 0 #f0f0f0;
}
.fav-dingyue{
  flex-shrink: 0;
  text-align: center;
  padding: 0 8px;
  font-size: 12px;
  margin-left: auto;
  border-radius: 2px;
  cursor: pointer;
  height: 21px;
  line-height: 21px;
  background: #F0F7FF!important;
}
.fav-dingyue i{
  margin-right: 2px;
  font-size: 12px;
}
.card-sys-title span {
  cursor: pointer;
}
.card-t-16 {
  padding-top:16px!important;
}

</style>
