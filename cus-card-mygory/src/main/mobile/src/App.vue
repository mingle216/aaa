<template>
  <div class="card-content">
    <roleTab
      v-if="tabInfo.length"
      v-bind="{
        active: params,
        serviceItemInfo,
        tabInfo,
        categoryItemInfo,
        config,
        loading,
        lanFunName,
        router
      }"
      @role-change="roleChangeFunc"
      @category-change="categoryChangeFunc"
    />
    <EmptyCon
      v-else-if="!loading"
      :img="permissionImg"
      :tip="$Lan(lanFunName, 'tipMsg', '暂无相关服务事项')"
    ></EmptyCon>
  </div>
</template>

<script>
import roleTab from "./components/RoleTab";
export default {
  components: { roleTab },
  name: "",
  props: {
    index: Number,
    router: Object,
  },
  data() {
    const { cardWid, cardId } = this.router;
    return {
      listId: `${cardId}_${cardWid}_${new Date().getTime()}`,
      lanFunName: "CUS_CARD_MYGORY_h5",
      loading: false,
      serviceItemInfo: [],
      categoryItemInfo: [],
      tabInfo: [],
      config: null,
      permissionImg: require("./asstes/permission.png"),
      params: { categoryWid: "", roleWid: "" },
      isFirstLoad: true,
      // tip: {
      //   en_US: {
      //     tipMsg:
      //       "No service item permission, please contact the administrator",
      //   },
      //   zh_CN: {
      //     tipMsg: "暂无服务事项权限，请联系管理员",
      //   },
      // },
    };
  },
  created() {
     
    if (window.shell) {
      this.getCardData('init');
    }
    console.log(this.lanFunName);
  },
  beforeDestroy() {},
  methods: {
    roleChangeFunc(id) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      this.params.roleWid = id;
      this.getCardData();
    },
    categoryChangeFunc(id) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: ''
        },
        startTime: new Date().getTime()
      });
      this.params.categoryWid = id;
      this.getCardData();
    },
    getCardData(isInit) {
      if(isInit && isInit === 'init') {
        window.minosStataCollect.loadStart({
          listId: this.listId,
          actionType: 3,
          functionType: 1,
          actionParams: {
            pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
            pageName: window.shell.getPageData().pageInfoEntity.pageName,
            cardWid: this.router.cardWid,
            cardId: this.router.cardId,
            cardName: this.router.cardName,
            extraInfo: ''
          },
          startTime: new Date().getTime()
        });
      }
      this.loading = true;
      window.shell.execCardMethod({
        cardId: this.router.cardId,
        cardWid: this.router.cardWid,
        method: "renderData",
        param: this.params,
      })
      .then(data=> {
        if(isInit && isInit === 'init') {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime()
          });
        }
        this.loading = false;
        if (data.errcode === "0") {
          let m_data = data.data;
          this.serviceItemInfo = m_data.serviceItemInfo;
          this.categoryItemInfo = m_data.categoryItemInfo;
          this.tabInfo = m_data.tabInfo;
          this.config = m_data.config;
          if (
            this.isFirstLoad &&
            this.tabInfo.length &&
            this.categoryItemInfo.length
          ) {
            console.log('11')
            let index = -1
            for (let i = 0; i < m_data.relateList.length; i++) {
              for (let j = 0; j < m_data.tabInfo.length; j++) {
                if (m_data.relateList[i] === m_data.tabInfo[j].roleWid) {
                  index = j
                  break
                }
              }
              if (index !== -1) {
                break
              }
            }
            index = index === -1 ? 0 : index
            this.params = {
              categoryWid: this.categoryItemInfo[0].categoryWid,
              roleWid: this.tabInfo[index].roleWid,
            };
            this.isFirstLoad = false;
            if (index !== 0) {
              setTimeout(() => {
                this.getCardData()
              }, 0)
            }
          }
        }
      })
      .catch(()=> {
        if(isInit && isInit === 'init') {
          window.minosStataCollect.loadEnd({
            listId: this.listId,
            endTime: new Date().getTime()
          });
        }
      });
    },
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
.card-content {
  position: relative;
  width: 100%;
  user-select: none;
  // height: 100px;
}
</style>
