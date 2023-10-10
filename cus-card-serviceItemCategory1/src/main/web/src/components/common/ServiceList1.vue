<template>
  <div class="main-con">
    <template v-if="dataList.length">
      <div v-for="(item,index) in items[0]" :key="index" style="margin: 12px 0;">
        <div class="serverListTab" @click="clickItemHandle(item)">
          <p>{{item.subjectName}}</p>
          <p><img src="../../assets/more.png"><span>更多</span></p>
        </div>
        <div class="serverList">
          <service-item v-for="(f_item, index) in item.itemList" :key="index" :data-item="f_item"
             :config="config" :is-dept="isDept"></service-item>
        </div>
      </div>
      <!-- <we-row v-for="(f_item, index) in items" :key="index" :gutter="20"
        :class="[index === items.length - 1 ? 'last-row' : '']">
        <we-col class="dzwecol" v-for="item in f_item" :key="item.subjectWid">
          :span="colNum"
          <service-item :data-item="item" @clickItem="clickItemHandle" :config="config" :is-dept="isDept"
            v-if="item.subjectWid"></service-item>
          <ServiceItemMore v-else :data-item="item" @clickItem="clickItemHandle" :config="config" :is-dept="isDept">
          </ServiceItemMore>
        </we-col>
      </we-row> -->
    </template>
    <template v-else>
      <emptyCon :tip="$Lan(lanFunName, 'tipMsg2', '暂无服务事项权限')" />
    </template>
  </div>
</template>

<script>
  import serviceItem from "./ServiceItem1";
  // import ServiceItemMore from "./ServiceItemMore";
  export default {
    components: {
      serviceItem,
      //ServiceItemMore,
    },
    props: {
      isDept: {
        type: Boolean,
        default: true,
      },
      dataList: {
        type: Array,
        default: () => {
          return [];
        },
      },
      newserviceList: {
        type: Array,
        default: () => {
          return [];
        },
      },
      config: {
        type: Object,
        default: () => {
          return {
            columns: 1,
            rows: 2,
            content: 2,
            departClassIcon: 0, //部门分类下是否显示图标，0：隐藏，1：字体，2：png
            otherClassIcon: 0, //其他分类是否显示图标，0：隐藏，1：显示
          };
        },
      },
      lanFunName: {
        type: String,
        default: "",
      },
    },
    data() {
      return {
        itemsLast: [],
        // tip: {
        //   en_US: {
        //     tipMsg: "No relevant services at present",
        //   },
        //   zh_CN: {
        //     tipMsg: "暂无相关服务事项",
        //   },
        // },
      };
    },
    computed: {
      colNum() {
        if (this.config.columns === 1) {
          return 24;
        } else if (this.config.columns === 2) {
          return 12;
        } else if (this.config.columns === 3) {
          return 8;
        } else if (this.config.columns === 4) {
          return 6;
        } else {
          return 24;
        }
      },
      items() {
        if (this.dataList && this.dataList.length) {
          let arr = [];
          let moreData = {
            iconLink: null,
            itemList: null,
            picLink: null,
            secondCategory: "",
            subjectName: this.$Lan(this.lanFunName, "subjectName", "查看更多"),
            subjectWid: "",
          };
          for (let index in this.dataList) {
            let n = parseInt(index / 50);
            if (arr.length <= n) {
              arr.push([]);
            }
            if (this.config.columns === 1 && this.config.rows === 1) {
              if (this.dataList.length > 1) {
                arr[n].push(moreData);
              } else {
                arr[n].push(this.dataList[index]);
              }
              return arr;
            } else {
              arr[n].push(this.dataList[index]);
            }

            if (this.config.columns > 1) {
              if (
                arr.length === this.config.rows &&
                this.dataList.length >
                this.config.columns * this.config.rows - 1 &&
                arr[arr.length - 1].length === this.config.columns - 1
              ) {
                arr[n].push(moreData);

                return arr;
              }
            } else if (
              arr.length === this.config.rows - 1 &&
              this.dataList.length > this.config.columns * this.config.rows - 1
            ) {
              arr[n].push(moreData);
              return arr;
            }
          }
          // let NewArr = []
          // arr.forEach(val => {
          //   val.forEach(el => {
          //     el.itemList.forEach(res => {
          //       res.subjectName = el.subjectName
          //       res.subjectWid = el.subjectWid
          //       NewArr.push(res)
          //     })
          //   })
          // })
          console.log('arr',arr)
          return arr;
        } else {
          return [];
        }
      },
    },
    mounted() {
      // setTimeout(()=>{
      //   this.itemsLast = this.handle()
      // }, 3000)
    },
    methods: {
      // up(x, y) {
      //   return Number(x.roleWid) - Number(y.roleWid);
      // },
      clickItemHandle(item) {
        this.$emit("clickItem", item);
      },
    //   clickItemHandle1(item) {
    //     this.$emit("clickItem", {
    //       subjectWid: item.subjectWid,
    //       roleWid: this.serviceTab.replace("tab", ""),
    //       categoryWid: this.active.categoryWid,
    //     });
    // },
      // handle(){
      //   console.log(this.items.sort(this.up),'****')
      //   return this.items.sort(this.up)
      // }
    },
  };
</script>

<style lang="less" scoped>
  .serverListTab {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 48px;
    border-bottom: 1px #f0f0f0 solid;
    cursor: pointer;
    margin-bottom:  10px;
  }

  .serverListTab>p:first-child {
    font-weight: bold;
    font-size: 18px;
    color: #102645;
    height: 48px;
    line-height: 48px;
    border-bottom: 2px solid #a40000;
  }

  .serverListTab>p:last-child {
    font-weight: 400;
    font-size: 15px;
    line-height: 19px;
    text-align: right;
    color: #A40000;
    display: flex;
    align-items: center;

  }
  .serverListTab>p:last-child>img{
    margin-right: 6px;
    height: 14px;
    width: 14px;
  }

  .serverList {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
  }

  .dzwecol {
    width: 20%;
  }

  .main-con {
    margin-top: 16px;
    display: flex;
    justify-content: space-between;
    flex-wrap:  wrap;

    /deep/.last-row .ser-item {
      margin-bottom: 0 !important;
    }
  }
  .main-con>div{
    width: calc(50% - 20px);
    .ser-item{
      width: calc(100% / 3);
    }
  }
</style>