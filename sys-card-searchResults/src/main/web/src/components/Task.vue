<template>
  <layout
    :id="id"
    :title="title"
    :total="item.total"
    :showTitle="showTitle"
  >
    <template v-if="list.length">
      <div class="item-list">
        <template v-for="(item, index) in list">
          <task-item
            :router="router"
            :lan-fun-name="lanFunName"
            :key="index"
            :item="item"
            :keyword="keyword"
            :showNav="showNav"
            :searchResultWidth="searchResultWidth"
            :analyzeData="analyzeData"
            :newsJump="newsJump"
            :showFavoriteTask="showFavoriteTask"
            v-if="index < page * pageSize"
            :createQrcode="createQrcode"
          />
        </template>
      </div>
      <moreData
        v-if="pageSize < item.total"
        :lan-fun-name="lanFunName"
        @click="pageAdd"
        :isOpen="page * pageSize < item.total"
      />
    </template>
    <EmptyCon
      v-else
      :tip="$Lan(lanFunName, 'tipMsg', '未找到相关数据')"
      mainTipClass="portal-font-color-lv2"
    />
    <we-popover
      ref="MobilePop"
      popper-class="favTaskPopoverTask"
      placement="bottom"
      :width="195"
    >
      <div class="MobilePop">
        <div class="portal-font-color-lv3 ellipsis">
          {{ $Lan("public", "mobileUrl", "当前为移动地址扫一扫查看") }}
        </div>
        <div class="popQrcode portal-primary-border-color-lv3">
          <div :id="`${listId}Qrcode`" ref="qrcode2"></div>
        </div>
        <div
          class="popCopy portal-primary-color-lv1 portal-primary-backgroundcolor-lv5 ellipsis"
          @click="copyLink"
        >
          <i class="iconfont icon-lianjie" style="font-size: inherit"></i>
          {{ $Lan("public", "copyUrl", "复制链接") }}
        </div>
      </div>
    </we-popover>
  </layout>
</template>

<script>
import layout from "./Layout";
import moreData from "./MoreData";
import TaskItem from "./items/TaskItem";
import QRCode from "qrcodejs2";
export default {
  components: {
    layout,
    moreData,
    TaskItem,
  },
  props: {
    router: Object,
    analyzeData: Array,
    id: {
      type: String,
      default: "matter" + new Date().getTime(),
    },
    keyword: {
      type: String,
      default: "",
    },
    list: {
      type: Array,
      default: () => {
        return [];
      },
    },
    lanFunName: {
      type: String,
      default: "",
    },
    noSearchResult: {
      type: Boolean,
      default: true
    },
    item: {
      type: Object,
      default: () => {
        return {}
      }
    },
    page: {
      type: Number,
      default: 1
    },
    showNav: {
      type: Boolean,
      default: true
    },
    hasExpand: {
      type: Boolean,
      default: false
    },
    searchResultWidth: {
      type: Number,
      default: 1026
    },
    sortType: {
      type: Number,
      default: 1
    },
    newsJump: String,
    title: String,
    showFavoriteTask: Number || String
  },
  data() {
    const { cardWid, cardId } = this.router;
    return {
      pageSize: 10,
      btnLoading: false,
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      clickMobileUrl: ''
    };
  },
  computed: {
    showTitle() {
      return this.noSearchResult || this.list.length > 0
    }
  },
  mounted(){
    document.addEventListener("click", this.handleDocumentClick);
  },
  beforeDestroy() {
    document.removeEventListener("click", this.handleDocumentClick);
    this.reference = null;
  },
  methods: {
    copyLink() {
      window.shell.copyText(this.clickMobileUrl);
    },
    handleDocumentClick(e) {
      const reference = this.reference;
      const popper = this.$refs.MobilePop;
      if (
        !reference ||
        reference.contains(e.target) ||
        !popper ||
        !popper.$refs.popper ||
        popper.$refs.popper.contains(e.target)
      )
        return;
      popper.doClose();
    },
    createQrcode(row, event) {
      const pop = this.$refs.MobilePop;
      if (pop) {
        let ele = event.target;
        pop.doClose();
        pop.referenceElm = ele;
        this.reference = ele;
        pop.$refs.popper && (pop.$refs.popper.style.display = "none");
        pop.doDestroy();
        setTimeout(() => {
          pop.doShow();
          const el = document.getElementById(`${this.listId}Qrcode`);
          if (el) {
            el.innerHTML = "";
            // 使用$nextTick确保数据渲染
            this.$nextTick(() => {
              new QRCode(el, {
                width: 84,
                height: 84, // 高度
                text: row.h5ViewUrl, // 二维码内容
              });
            });
          }
          this.clickMobileUrl = row.h5ViewUrl;
        }, 10);
      }
    },
    pageAdd(isAdd) {
      if (isAdd) {
        // 已经全部展开后，不需要再请求接口，直接改变分页
        if (this.hasExpand) {
          this.$emit('update:page', this.page + 1)
          return
        } else {
          if (this.btnLoading) {
            return
          }
          this.btnLoading = true
          window.shell
            .execCardMethod({
              cardId: this.router.cardId,
              cardWid: this.router.cardWid,
              method: "moreTdcForPortal",
              param: { 
                keyword: this.keyword,
                pageNumber: this.page,
                pageSize: this.pageSize
              },
            })
            .then(res => {
              this.btnLoading = false
              const list = res.data?.searchData?.tdcData || []
              this.$emit('update:list', this.list.concat(list))
              this.$emit('update:page', this.page + 1)
            })
        }
      } else {
        this.$emit('update:page', 1)
        this.$emit('update:hasExpand', true)
      }
    }
  }
};
</script>

<style lang="less">
  .favTaskPopoverTask{
    padding-bottom: 0px;
    position: fixed !important;
  }
</style>
<style lang="less" scoped>
.favTaskPopover{
  padding-bottom: 0px;
}
.item-list {
  margin-bottom: 16px;
}
/deep/.we-dropdown {
  height: 35px;
}
.drop_link {
  cursor: pointer;
  font-weight: normal;
}

.MobilePop {
  font-size: 12px;
  line-height: 20px;
  text-align: center;
}
.popQrcode {
  width: 92px;
  height: 92px;
  border: 1px solid #abd2ff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin: 8px auto;
}
.popCopy {
  cursor: pointer;
  height: 30px;
  line-height: 30px;
  text-align: center;
  margin: 0 -12px;
}
</style>
