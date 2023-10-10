<template>
  <div style="width: 100%" v-loading="loading" class="dz-servicebus" ref="servicebus">
    <ServiceTab v-if="tabPosition === '1'" :sourceList="classifyData" :serviceList="appData"
      :inSelectedTypeId="selectedTypeId" @click-type="clickType" @collect-app="collectApp" :containerType="containerType"
      :containerHeight="containerHeight" :appLoading="appLoading" :servicesData="servicesData"
      :showSubscribe="showSubscribe" :allClassifyData="allClassifyData" :itemWidth="getItemWidth" :router="router"
      ref="serviceTab">
    </ServiceTab>
    <serviceTransverse v-else :sourceList="classifyData" :serviceList="appData" :inSelectedTypeId="selectedTypeId"
      :containerType="containerType" :containerHeight="containerHeight" :appLoading="appLoading"
      :servicesData="servicesData" :allClassifyData="allClassifyData" :itemWidth="getItemWidth" :router="router"
      :showSubscribe="showSubscribe" @click-type="clickType" @collect-app="collectApp" ref="serviceTab">
    </serviceTransverse>
    <service-topics v-model="isShowTopics" :sourceList="allClassifyData" :card="card" :router="router"></service-topics>
  </div>
</template>

<script>
import ServiceTab from "./components/ServiceTab";
import serviceTopics from "./components/serviceTopics";
import serviceTransverse from "./components/serviceTransverse";
import { TOPIC } from "./utils/constant";
import { initTrack } from './mixins/track.js';
export default {
  name: "ServiceBus",
  components: { ServiceTab, serviceTopics, serviceTransverse },
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
      containerType: 2, //初始化卡片高度选项 1：代表自适应（无最大限制）2：代表自适应（有最大限制）3：固定高度
      containerHeight: 300, // 高度 只有当heightFlag为2，3时，此值才生效
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
      showSubscribe: false,
    };
  },
  methods: {
    recordCache(typeId) {
      let obj = {
        cardWid: this.router.cardWid,
        typeId: typeId
      }
      sessionStorage.setItem(`cache_${this.router.cardId}`, JSON.stringify(obj))
    },
    getConfig() {
      window.shell.execCardMethod({
        cardId: this.router.cardId,
        cardWid: this.router.cardWid,
        method: "getConfig",
      }, (data) => {
        if (data.errcode === '0') {
          const datas = data.data;
          if (typeof datas === 'string') {
            let parseData = JSON.parse(JSON.parse(datas));
            this.showSubscribe = parseData?.showSubscribe == 1 ? true : false;
          }
        }
      })
    },
    clickType(typeId) {
      this.appLoading = true;
      this.renderData(typeId);
      this.recordCache(typeId)
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
    renderData(typeId, init) {
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
            this.classifyData = res.data.classifyData.filter(
              (v) => v.show && v
            );
            // this.classifyData = [
            //   ...this.classifyData,
            //   ...this.classifyData,
            //   ...this.classifyData,
            // ];
            // this.classifyData = [
            //   ...this.classifyData,
            //   ...this.classifyData,
            //   ...this.classifyData,
            // ];
            // this.classifyData = [
            //   ...this.classifyData,
            //   ...this.classifyData,
            //   ...this.classifyData,
            // ];
            this.allClassifyData = res.data.classifyData;
            this.appData = res.data.appData;
            //  this.appData =[...this.appData,...this.appData,...this.appData,...this.appData,...this.appData,...this.appData ]
            this.containerType = res.data.heightFlag;
            this.containerHeight = res.data.height + "";
            this.selectedTypeId = typeId
              ? typeId
              : this.classifyData && this.classifyData.length > 0
                ? this.classifyData[0].typeId
                : "";

            this.tabPosition = res.data.tabPosition || "1";
            this.$nextTick(() => {
              this.formatData();
              let serviceTab = this.$refs.serviceTab;
              serviceTab.refreshParent();
              init && serviceTab.init && serviceTab.init();
            });
            this.columns = res.data.columns || 1
          }
        }
      );
    },
    formatData() {
      const totalNum = this.appData.length;
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
    this.getConfig();
    // 初始化时默认选中记录的分类
    if (sessionStorage.getItem(`cache_${this.router.cardId}`)) {
      const parseData = JSON.parse(sessionStorage.getItem(`cache_${this.router.cardId}`))
      if (parseData.cardWid === this.router.cardWid) {
        this.renderData(parseData.typeId, true);
      } else {
        this.renderData(null, true);
      }
    } else {
      this.renderData(null, true);
    }

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
.dz-servicebus {
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
