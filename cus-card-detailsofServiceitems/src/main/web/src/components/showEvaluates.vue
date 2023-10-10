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
        v-if="showFixedFilter && !badAppraiseHidden"
        :isFixed="showFixedFilter"
        :lan-fun-name="lanFunName"
        @filter-change="setMarkType"
      />
      <vueScroll @vshandle-scroll="evScroll">
        <div ref="listCon" class="list-con-main">
          <div class="ev-filter-con" v-if="!badAppraiseHidden">
            <EvFilter
              :markType="evParams.markType"
              :lan-fun-name="lanFunName"
              v-if="!showFixedFilter"
              @filter-change="setMarkType"
            />
          </div>
          <div class="ev-list">
            <div class="ev-list-body">
              <template v-for="(item, index) in dataList">
                <EvItem
                  v-bind="{ appraiseName, item, lanFunName }"
                  :key="index"
                />
              </template>
            </div>
          </div>
          <div v-if="dataList.length" class="no-more">
            {{ $Lan(lanFunName, "evTipMsg", "没有更多数据了") }}
          </div>
          <emptyCon
            v-else
            :tip="$Lan(lanFunName, 'evTipMsg2', '暂无数据')"
            :height="200"
          />
        </div>
      </vueScroll>
    </div>
  </we-dialog>
</template>

<script>
import EvItem from "./childComponents/EvItem";
import EvFilter from "./childComponents/EvFilter";
export default {
  components: {
    EvItem,
    EvFilter,
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
  watch: {
    value(val) {
      this.showEva = val;
      if (val) {
        this.dataList = [];
        this.evParams = {
          markType: 0,
          pageNumber: 1,
          pageSize: 5,
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
    evScroll(ev) {
      this.showFixedFilter = ev.scrollTop > 20 ? true : false;
      // console.log(this.$refs['evCon'])
      let ev_h = this.$refs["evCon"].offsetHeight;
      let list_h = this.$refs["listCon"].offsetHeight;

      if (
        ev_h + ev.scrollTop > list_h - 150 &&
        !this.isLoading &&
        this.evParams.pageNumber <
          Math.ceil(this.total / this.evParams.pageSize)
      ) {
        this.evParams.pageNumber++;
        // console.log('第',this.evParams.pageNumber)
        this.getServiceItemsEv();
      }
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
            this.dataList = this.dataList.concat(data.data.appraiseDetail.data);
            this.badAppraiseHidden = data.data.badAppraiseHidden;
            this.total = data.data.appraiseDetail.total;
          }
        });
    },
  },
};
</script>
<style lang="less">
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
    height: 376px;
    position: relative;
    padding: 0 5px 20px 20px;
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
<style lang="less" scoped></style>
