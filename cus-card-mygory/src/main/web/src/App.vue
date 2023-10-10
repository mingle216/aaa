<template>
  <div class="service-matters-con">
    <template v-if="tabList && tabList.length">
      <tabCon
        :loading="loading"
        v-if="config.showType === 0"
        @tab-change="tabChangeHandle"
        @clickItem="clickItemHandle"
        @category-change="categoryChangeHandle"
        :active="params"
        :config="config"
        :categoryList="categoryList"
        :tabList="tabList"
        :serviceList="serviceList"
        :lan-fun-name="lanFunName"
        :router="router"
      ></tabCon>
      <tiledCon
        v-if="config.showType === 1"
        :loading="loading"
        @category-change="categoryChangeHandle"
        @clickItem="clickItemHandle"
        :active="params"
        :config="config"
        :categoryList="categoryList"
        :tabList="tabList"
        :serviceList="serviceList"
        :lan-fun-name="lanFunName"
        :router="router"
      ></tiledCon>
    </template>
    <template v-else-if="!loading && !tabList.length">
      <emptyCon
        :tip="$Lan(lanFunName, 'tipMsg2', '暂无服务事项权限')"
        :img="permissionImg"
        :subTip="$Lan(lanFunName, 'tipMsg3', '请联系管理员')"
      />
    </template>
  </div>
</template>

<script>
import tabCon from "./components/TabCon";
import tiledCon from "./components/TiledCon";
import { initTrack } from './mixins/track.js';
const defaultCfg = {
  columns: 4, //列数
  content: 0, //内容显示方式0：事项，1:二级分类
  rows: 2, //行数
  showType: 0, //展示方式，0：tab,1:平铺
  departClassIcon: 0, //部门分类下是否显示图标，0：隐藏，1：字体，2：png
  otherClassIcon: 0, //其他分类是否显示图标，0：隐藏，1：显示
};

export default {
  components: {
    tabCon,
    tiledCon,
  },
  props: {
    index: Number,
    router: Object,
  },
  mixins: [initTrack],
  data() {
    return {
      lanFunName: this.router.cardId,
      config: defaultCfg,
      params: { categoryWid: "", roleWid: "" },
      isFirstLoad: true,
      categoryList: [],
      tabList: [],
      serviceList: [],
      loading: false,
      permissionImg: require("./assets/permission.png"),
      // tip: {
      //   en_US: {
      //     tipMsg: "There is no authority for service items",
      //     tipMsg2: "Please contact the administrator",
      //   },
      //   zh_CN: {
      //     tipMsg: "暂无服务事项权限",
      //     tipMsg2: "请联系管理员",
      //   },
      // },
    };
  },
  mounted() {},
  methods: {
    renderData() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "renderData",
          param: this.params,
        },
        (data) => {
          setTimeout(() => {
            this.loading = false;
            this.loadedEndTrack(); // 加载结束埋点
          }, 100);

          if (data.errcode === "0") {
            let m_data = data.data || { config: defaultCfg };

            this.config = {
              columns: m_data.config.columns,
              content: Array.isArray(m_data.config.resultWay) ? m_data.config.resultWay[0] || 0 : m_data.config.resultWay || 0,  // 3.3.3改成数组，要兼容老的配置
              rows: m_data.config.rows,
              showType: m_data.config.roleTab || 0,
              departClassIcon: m_data.config.departClassIcon || 0,
              otherClassIcon: m_data.config.otherClassIcon || 0,
            };

            this.categoryList = m_data.categoryItemInfo || [];
            this.tabList = m_data.tabInfo || [];

            let serviceList = m_data.serviceItemInfo || [];
            if (this.config.showType === 1) {
              if (!this.params.categoryWid) {
                for (let key in serviceList) {
                  serviceList[key].categoryWid =
                    m_data.categoryItemInfo[0].categoryWid;
                }
                this.serviceList = serviceList;
              } else {
                let index = serviceList.findIndex((item) => {
                  return item.roleWid === this.params.roleWid;
                });
                this.serviceList[index].categoryWid = this.params.categoryWid;
                this.serviceList[index].itemModelList =
                  serviceList[index].itemModelList;
              }
            } else {
              this.serviceList = serviceList;
            }
            
            if (this.isFirstLoad && this.tabList.length) {
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
  
              console.log('---', index)
              
              this.params = {
                roleWid: m_data.tabInfo[index].roleWid,
                categoryWid: m_data.categoryItemInfo[0].categoryWid,
              };
              if (index !== 0) {
                setTimeout(() => {
                  this.renderData()
                }, 100)
              }
            }
            this.isFirstLoad = false;
          }
        }
      );
    },
    tabChangeHandle(tab_id) {
      this.params.roleWid = tab_id;
      this.renderData();
    },
    categoryChangeHandle(ev) {
      this.params = ev;
      this.renderData();
    },
    clickItemHandle(item) {
      console.log(item)
      this.handleClickTrack({
        infoType: 4,
        filterInfo: {
          pageCode: 'itemCategoryDetail'
        }
      }); // 点击埋点
      window.shell.openPage(
        `/itemCategoryDetail?roleWid=${item.roleWid}&oneCategoryWid=${item.categoryWid}&categoryWid=${item.subjectWid}&cardWid=${this.router.cardWid}`,
        0,
        1
      );
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
  created() {
    this.renderData();
  },
  beforeDestroy() {},
};
</script>
<style lang="less">
*:focus {
  outline: none;
}
</style>
<style scoped>
.service-matters-con {
  position: relative;
}
</style>
