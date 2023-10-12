<template>
  <we-dialog
    custom-class="show-ev-dialog"
    :title="$Lan(lanFunName, 'showEvDialog', '服务事项评价')"
    :visible.sync="showEva"
    width="610px"
    :modal-append-to-body="true"
    :append-to-body="true"
    :close-on-click-modal="false"
    @close="digClose"
  >
    <div ref="evCon" class="ev-list-con" v-loading="isLoading">
      <EvFilter
        :markType="evParams.markType"
        :isFixed="showFixedFilter"
        :lan-fun-name="lanFunName"
        @filter-change="setMarkType"
        v-if="!badAppraiseHidden"
      />
      <!-- 列表区域 -->
      <div v-if="total > 0" style="width: 100%; margin-top: 16px;">
        <AutoContainer :conMaxHeight="376">
          <template v-for="(item, index) in dataList">
            <EvItem
              v-bind="{ appraiseName, item, lanFunName }"
              :key="index"
              @toPreview="toPreview"
            />
          </template>
        </AutoContainer>
      </div>
      <div v-else class="tbTable">
        <emptyCon
          :tip="$Lan(lanFunName, 'evTipMsg2', '暂无数据')"
          :height="200"
        />
      </div>
      <!-- 分页 -->
      <div class="tySpace" v-if="total > 0">
        <div class="portal-font-color-lv3">{{ $Lan( lanFunName, "pageInfo", "共 {total} 条，显示第 {start} ~ {end} 条", { total: total, start: pageStart, end: pageEnd, } ) }}</div>
        <we-pagination
          background
          size="medium"
          layout="prev, pager, next"
          :total="total"
          :current-page="evParams.pageNumber"
          :page-size="evParams.pageSize"
          @current-change="handlePageChange"
        >
        </we-pagination>
      </div>
    </div>
    <!-- 预览 -->
    <!-- <PopPreview ref="popPreview" /> -->
    <we-image
      ref="PreviewImg"
      :src="previewImg"
      :preview-src-list="[previewImg]"
      style="width:0.01px;height:0.01px"
      :z-index="8888"
    ></we-image>
  </we-dialog>
</template>

<script>
import EvItem from "./childComponents/EvItem";
import EvFilter from "./childComponents/EvFilter";
export default {
  components: {
    EvItem,
    EvFilter
  },
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    router: Object,
    value: {
      type: Boolean,
      default: false,
    },
    itemWid: {
      type: String,
      default: "",
    },
    card: {
      type: Object,
      default: () => {
        return {};
      },
    },
    appraiseName: {
      type: Array,
      default: () => {
        return [];
      },
    },
    lanFunName: {
      type: String,
      default: "SYS_CARD_DETAILSOFSERVICEITEMS",
    },
  },
  computed: {
    pageStart() {
      return (this.evParams.pageNumber - 1) * this.evParams.pageSize + 1;
    },
    pageEnd() {
      return this.total <= this.evParams.pageNumber * this.evParams.pageSize
        ? this.total
        : this.evParams.pageNumber * this.evParams.pageSize;
    },
  },
  watch: {
    value(val) {
      this.showEva = val;
      if (val) {
        this.dataList = [];
        this.evParams = {
          markType: 0,
          pageNumber: 1,
          pageSize: 10,
        };
        this.getServiceItemsEv();
      }
    },
    showEva(val) {
      this.$emit("change", val);
    },
  },
  data() {
    return {
      showEva: false,
      evParams: "",
      total: 0,
      badAppraiseHidden: 0,
      dataList: [],
      isLoading: false,
      showFixedFilter: false,
      previewImg: '',
      // tip: {
      //   en_US: {
      //     tipMsg: "No more data",
      //     tipMsg2: "No data",
      //   },
      //   zh_CN: {
      //     tipMsg: "没有更多数据了",
      //     tipMsg2: "暂无数据",
      //   },
      // },
    };
  },

  methods: {
    toPreview(item) {
      // this.$refs.popPreview.init(src);
      this.previewImg = item;
      this.$refs.PreviewImg.showViewer = true;
    },
    handlePageChange(page) {
      this.evParams.pageNumber = page;
      this.getServiceItemsEv();
    },
    digClose() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName
        },
        startTime: new Date().getTime()
      });
    },
    setMarkType(num) {
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
            itemId: this.itemWid,
            fucType: 4
          }
        },
        startTime: new Date().getTime()
      });
      this.evParams.markType = num;
      this.evParams.pageNumber = 1;
      this.dataList = [];
      this.getServiceItemsEv();
    },
    getServiceItemsEv() {
      if (this.isLoading) {
        return;
      }
      this.isLoading = true;
      window.shell
        .execCardMethod({
          ...this.card,
          method: "getAppraise",
          param: {
            ...this.evParams,
            itemWid: this.itemWid,
          },
        })
        .then((data) => {
          // let {errcode,res}=data;
          setTimeout(() => {
            this.isLoading = false;
          }, 100);
          if (data.errcode === "0") {
            this.dataList = data.data.appraiseDetail.data;
            // this.dataList = this.dataList.concat(data.data.appraiseDetail.data);
            this.badAppraiseHidden = data.data.badAppraiseHidden;
            this.total = data.data.appraiseDetail.total;
          }
        });
    },
  },
};
</script>
<style lang="less">
.tySpace{
  display: flex; align-items: center; justify-content: space-between; margin-top: 15px;
}
.tbTable{
  height: 376px; width: 100%; overflow-y: auto; margin-top: 16px; padding-right: 10px;
}
.show-ev-dialog {
  z-index: 9999;
  top: 50%;
  margin-top: -246px !important;
  .we-dialog__header {
    border-bottom: 1px solid #eee;
    height: 66px;
    .we-dialog__title {
      font-size: 18px;
    }
  }
  .we-dialog__body {
    padding: 0;
    //  padding-top: 0;
  }
  .ev-list-con {
    // height: 376px;
    position: relative;
    padding: 10px 20px 10px 20px;
    // padding-top: 20px;
    .list-con-main {
      margin-right: 5px;
      .ev-filter-con {
        padding-top: 20px;
        height: 60px;
        box-sizing: border-box;
      }
      .no-more {
        height: 60px;
        line-height: 60px;
        text-align: center;
        color: #ccc;
      }
    }

    .ev-list {
      min-height: 50px;
    }
  }
}
</style>
<style lang="less" scoped>
/deep/.we-image-viewer__close {
  color: #ffffff;
}
</style>
