<template>
  <div style="width: 100%" v-loading="loading" class="servicebus" ref="servicebus">
    <serviceTransverse :moreLink="moreLink" :tabPosition="tabPosition" :sourceList="classifyData" :serviceList="appData"
      :inSelectedTypeId="selectedTypeId" :containerType="containerType" :containerHeight="containerHeight"
      :appLoading="appLoading" :servicesData="servicesData" :allClassifyData="allClassifyData" :itemWidth="getItemWidth"
      :router="router" @click-type="clickType" @collect-app="collectApp" ref="serviceTab"></serviceTransverse>
    <service-topics v-model="isShowTopics" :sourceList="allClassifyData" :card="card" :router="router"></service-topics>
  </div>
</template>

<script>
  import serviceTopics from "./components/serviceTopics";
  import serviceTransverse from "./components/serviceTransverse";
  import { TOPIC } from "./utils/constant";
  import { initTrack } from './mixins/track.js';
  export default {
    name: "ServiceBus",
    components: { serviceTopics, serviceTransverse },
    props: {
      index: Number,
      router: Object,
    },
    mixins: [initTrack],
    data() {
      const { cardId, cardWid } = this.router;
      return {
        checked: false,
        servicesData: [],
        fontClass: "",
        allClassifyData: [],
        classifyData: [],
        appData: [],
        selectedTypeId: "",
        containerType: "", //初始化卡片高度选项 1：代表自适应（无最大限制）2：代表自适应（有最大限制）3：固定高度
        containerHeight: "", // 高度 只有当heightFlag为2，3时，此值才生效
        appLoading: true,
        loading: true,
        isShowTopics: false,
        card: {
          cardWid,
          cardId,
        },
        tabPosition: null,
        isCollect: true,
        columns: 4,
        containerWidth: 0,
        classify_id:"",
        moreLink:"",
      };
    },
    methods: {
      dzInit(type) {
        window.shell.execCardMethod(
          {
            cardId: 'CUS_CARD_SERVICE',
            cardWid: '3237071044484381',
            method: "getServiceClassify",
            param: {
              lang: "zh_CN",
              n:new Date().getTime()
            }
          },
          (res) => {
            let list = res.data.data
            if (type == '0') {
              let result = list.find(item => { return item.classify_name == '服务' })
              this.classify_id = result.classify_id
              this.getTab(result.classify_id)
            } else {
              let result = list.find(item => { return item.classify_name == '系统' })
              this.classify_id = result.classify_id
              this.getTab(result.classify_id)
            }
          }
        );
      },
      getTab(classifyId) {
        window.shell.execCardMethod(
          {
            cardId: 'CUS_CARD_SERVICE',
            cardWid: '3237071044484381',
            method: 'getServiceClassifyAndService',
            param: {
              lang: "zh_CN",
              classifyId: classifyId
            }

          },
          (res) => {
            this.classifyData = res.data.data[0].classifyChildWids
            this.selectedTypeId = this.classifyData[0].classifyName
            this.getData(classifyId,this.selectedTypeId)
            //this.appData = res.data.data;
            

          }
        );
      },
      getData(classifyId,classifyChildId){
        window.shell.execCardMethod(
          {
            cardId: 'CUS_CARD_SERVICE',
            cardWid: '3237071044484381',
            method: 'getServiceClassifyAndService',
            param: {
              lang: "zh_CN",
              classifyId: classifyId,
              classifyChildId:classifyChildId
            }

          },
          (res) => {
             this.appData = res.data.data;
             this.$nextTick(() => {
                this.formatData();
                // let serviceTab = this.$refs.serviceTab;
                // serviceTab.refreshParent();
                // init && serviceTab.init && serviceTab.init();
              });
            console.log('appData',this.appData)

          }
        );

      },
      clickType(typeId) {
        console.log(7878,this.classify_id,typeId)
        this.getData(this.classify_id,typeId)
        // this.renderData(typeId);
      },
      collectApp(item) {
        this.handleClickTrack({
          infoType: 0,
          serviceId: item.appId,
          fucType: item.operate ? 2 : 3,
        });
        if (!this.isCollect) {
          return;
        }
        this.isCollect = false;
        window.shell.collectService({
          operate: item.operate,
          id: item.appId,
        });
      },
      handleUpdateFavorite({ id, res }) {
        this.isCollect = true;
        if (res.errcode === "0") {
          this.appData.some((element) => {
            if (element.appId === id) {
              element.appFavorite = !element.appFavorite;
            }
          });
        }
      },
      renderData(typeId) {
        let param = typeId ? { typeId } : {};
        window.shell.execCardMethod(
          {
            cardId: this.router.cardId,
            cardWid: this.router.cardWid,
            method: "renderData",
            param,
          },
          (res) => {
            this.$nextTick(() => {
              this.loading = false;
              this.appLoading = false;
              this.loadedEndTrack();
            });
            if (res.errcode === "0") {
              this.allClassifyData = res.data.classifyData;
              // this.appData = res.data.appData;
              //  this.appData =[...this.appData,...this.appData,...this.appData,...this.appData,...this.appData,...this.appData ]
              this.containerType = res.data.heightFlag;
              this.containerHeight = res.data.height + "";
              // this.selectedTypeId = typeId
              //   ? typeId
              //   : this.classifyData && this.classifyData.length > 0
              //     ? this.classifyData[0].typeId
              //     : "";
              //this.moreLink = res.data.moreLink 
              this.tabPosition = res.data.tabPosition || "1";
              // this.selectedTypeId = this.classifyData[0].typeId
              this.dzInit(this.tabPosition)
              
              this.columns = res.data.columns || 1
            }
          }
        );
        window.shell.execCardMethod(
          {
            cardId: this.router.cardId,
            cardWid: this.router.cardWid,
            method: "getConfig",
            param,
          },
          (data) => {
            if (data.errcode === "0") {
              let config = data.data || ""
              config = JSON.parse (JSON.parse(config));
              this.moreLink = config.moreLink[0].langValue
            }
          }
        );
      },
      formatData() {
        const totalNum = this.appData.length;
        console.log(9999,totalNum)
        this.servicesData = [];
        if (this.appData.length === 0) {
          this.servicesData = [];
          return;
        }
        // const width = this.$refs.serviceTab.getServiceDivWidth();
        // const singleServiceWidth =
        //   this.tabPosition === "0"
        //     ? 204
        //     : this.classifyData && this.classifyData.length > 1
        //     ? 210
        //     : 250;
        // const rowServiceNum = Math.floor(width / singleServiceWidth);
        const rowServiceNum = this.columns;
        const arrayNum = Math.ceil(totalNum / rowServiceNum);

        const remainer =
          totalNum >= rowServiceNum
            ? totalNum % rowServiceNum === 0
              ? 0
              : rowServiceNum - (totalNum % rowServiceNum)
            : rowServiceNum - totalNum;
        for (let i = 0; i < arrayNum; i++) {
          this.$set(
            this.servicesData,
            i,
            this.appData.slice(i * rowServiceNum, (i + 1) * rowServiceNum)
          );
        }
        let tempServicesData = this.servicesData[arrayNum - 1];

        for (let i = 0; i < remainer; i++) {
          tempServicesData.push({ serviceTabItemNoShow: true });
        }

        this.$set(this.servicesData, arrayNum - 1, tempServicesData);
        console.log(999,this.servicesData)
        //此处不可以直接修改servicesData因为vue监听不到
      },
    },
    computed: {
      getItemWidth() {
        //判断 导航栏是否在左侧显示，如果在左侧，减去152px
        console.log('this.containerWidth', this.containerWidth)
        let f_w =
          this.tabPosition === "0"
            ? this.containerWidth
            : Number(this.containerWidth) - 152;
        let w = this.columns * 154;
        console.log("f_w", f_w);
        return `width:${f_w > w ? f_w / this.columns : 154}px;`;
      },
    },
    created() {
      window.shell.on("collectApp", this.handleUpdateFavorite);
      this.renderData(null, true);
      this.$nextTick(() => {
        this.containerWidth = this.$refs.servicebus
          ? this.$refs.servicebus.offsetWidth
          : 0;
      });
    },
    beforeDestroy() {
      window.shell.off(TOPIC.COLLECT_APP);
    },
  };
</script>

<style scoped lang='less'>
  .servicebus {
    overflow-x: auto;
    width: 100%;

    &::-webkit-scrollbar {
      height: 6px;
      background-color: #f5f5f5;
    }

    /*定义滚动条轨道 内阴影+圆角*/
    &::-webkit-scrollbar-track {
      /* -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3); */
      border-radius: 6px;
      background-color: transparent;
    }

    /*定义滑块 内阴影+圆角*/
    &::-webkit-scrollbar-thumb {
      border-radius: 6px;
      -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.1);
      background-color: rgba(0, 0, 0, 0.2);
    }
  }
</style>