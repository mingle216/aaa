<template>
  <div
    class="itemCategoryDetai__body"
    ref="CardCategoryDetai"
    v-loading="!hasInited && loading"
  >
    <template v-if="treeData.length || fromSearch">
      <!-- 左侧分类树 -->
      <div class="itemCategoryDetai__body-left" v-loading="loading">
        <!--<we-input
          v-model="keyword"
          :placeholder="placeholder"
          :maxlength="20"
          class="itemCategoryDetail-input"
          @keyup.native="handleKeyUp"
          v-if="categroyType"
        >
          <i
            slot="suffix"
            class="we-input__icon iconfont icon-CardSearch"
            @click="handleKeywordSearch"
          ></i>
        </we-input>-->
        <ContainerScroll
          class="itemCategoryDetai__tree"
          :bar-keep-show="true"
          :scrollCover="true"
          :max-height="850"
          v-if="treeData.length"
        >
          <left-tree
            @change-node="handleSelect"
            :treeData="treeData"
            :router="router"
            :deptIconType="configInfo.deptIconType"
            :otherClassIcon="configInfo.otherClassIcon"
            :isDept="categroyType.categoryWid === 'dept-subject'"
          ></left-tree>
        </ContainerScroll>
        <empty-con
          v-else
          :height="300"
          :tip="
            $Lan(
              'CUS_CARD_MYGORYDETAIL',
              'noServiceCategory',
              '暂无相关服务分类'
            )
          "
        ></empty-con>
      </div>
      <!-- 右侧服务事项列表 -->
      <right-lists
        class="itemCategoryDetai__body-right"
        ref="RightList"
        :optRow="optRow"
        :configInfo="configInfo"
        :categroyType="categroyType"
        :router="router"
        @loadEnd="$emit('loadEnd')"
      ></right-lists>
    </template>
    <!-- 非搜索情况下，无数据时展示 -->
    <empty-con
      v-else-if="hasInited"
      :tip="
        $Lan(
          'CUS_CARD_MYGORYDETAIL',
          'noServiceItem',
          '暂无相关服务事项'
        )
      "
      style="width: 100%"
    ></empty-con>
  </div>
</template>

<script>
import RightLists from "./RightLists";
import LeftTree from "./LeftTree";
import { clickTrack } from "../mixins/track.js";
export default {
  components: {
    RightLists,
    LeftTree,
  },
  props: ["categroyType", "configInfo", "router"],
  mixins: [clickTrack],
  data() {
    return {

      optRow: null,
      categoryLists: [],
      hasInited: false,
      loading: false,
      keyword: "",
      searchKeyword: "",
      treeData: [],
      treeMap: {},
      fromSearch: false,
      placeholder: "",
    };
  },
  watch: {
    categroyType: {
      handler(val) {
        this.keyword = "";
        this.searchKeyword = "";
        val && this.handleSearch(true);
        val &&
          this.$nextTick(function() {
            this.placeholderEllipsis();
          });
      },
      immediate: true,
    },
  },
  methods: {
    // 折叠展示的placeholder
    placeholderEllipsis() {
      let name =
        this.categroyType &&
        this.categroyType.categoryName.match(/按([\s\S]*)分类/);
      if (
        this.categroyType &&
        this.categroyType.categoryWid === "dept-subject"
      ) {
        name = [
          "",
          this.$Lan("CUS_CARD_MYGORYDETAIL", "department", "部门"),
        ];
      }
      let placeStr = this.$Lan(
        "CUS_CARD_MYGORYDETAIL",
        "categoryPlaceholder",
        "请输入{name}名称",
        {
          name: name && name.length > 1 ? name[1] : "",
        }
      );
      this.placeholder = window.shell.placeholderEllipsis(
        placeStr,
        this.$refs.CardCategoryDetai.clientWidth * 0.2 - 55
      );
    },
    handleKeyUp(e) {
      if (e.key === "Enter") {
        //按下回车
        this.handleKeywordSearch();
      }
    },
    handleKeywordSearch() {
      this.fromSearch = true;
      this.searchKeyword = this.keyword.trim().toLowerCase();
      this.handleSearch();
      this.handleClickTrack({
        infoType: 3,
        filterInfo: {
          keyword: this.keyword.trim(),
        },
      }); // 点击埋点
    },
    pureUpdateData() {
      window.shell.execCardMethod(
        {
          cardId: this.configInfo && this.configInfo.cardId,
          cardWid: this.configInfo && this.configInfo.cardWid,
          method: "allCateGoryList",
          param: {
            isRelate: "0",
            roleWid: this.configInfo && this.configInfo.roleWid,
            oneCategoryWid: this.categroyType.categoryWid,
            searchValue: this.searchKeyword,
          },
        },
        (resp) => {
          if (resp && resp.errcode === "0") {
            const data =
              resp.data && resp.data.data ? resp.data.data.children || [] : [];
            this.treeMap = {};
            // 组装树形结构数据，添加deep、select等属性
            data.length && this.buildTree(data, 1, null);
            this.treeData = data;
            // 选中默认选中的数据
            this.treeData.length && this.checkNode(false);
          }
        }
      );
      this.$refs.RightList.fetchLists(true);
    },
    handleSearch(isInit) {
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.configInfo && this.configInfo.cardId,
          cardWid: this.configInfo && this.configInfo.cardWid,
          method: "allCateGoryList",
          param: {
            isRelate: "0",
            roleWid: this.configInfo && this.configInfo.roleWid,
            oneCategoryWid: this.categroyType.categoryWid,
            searchValue: this.searchKeyword,
          },
        },
        (resp) => {
          this.loading = false;
          if (resp && resp.errcode === "0") {
            const data =
              resp.data && resp.data.data ? resp.data.data.children || [] : [];
            this.treeMap = {};
            // 组装树形结构数据，添加deep、select等属性
            data.length && this.buildTree(data, 1, null);
            this.treeData = data;
            if (isInit) {
              this.$nextTick(function() {
                // 选中默认选中的数据
                if (this.treeData.length) {
                  this.checkNode(isInit);
                } else {
                  this.optRow = null;
                }
                this.hasInited = true;
              });
              this.fromSearch = false;
            } else {
              // 选中默认选中的数据
              this.treeData.length && this.checkNode(isInit);
              // 二级匹配时，默认展开其父节点
              this.treeData.length && this.expandNode();
            }
          } else {
            this.treeData = [];
          }
        }
      );
    },
    buildTree(data, deep, parent) {
      data.forEach((element) => {
        element.deep = deep || 1;
        element.parentId = parent && parent.categoryWid;
        element.select = false;
        element.childSelect = false;
        this.treeMap[`${deep}-${element.categoryWid}`] = element;
        if (element.children && element.children.length) {
          this.buildTree(element.children, element.deep + 1, element);
        }
      });
    },
    checkNode(isInit) {
      if (isInit) {
        let curr = null;
        if (!this.hasInited && this.configInfo.categoryWid) {
          // 初始化选中路由携带的分类
          curr = this.treeMap[`1-${this.configInfo.categoryWid}`];
          if (!curr) {
            curr = this.treeData[0];
          }
          curr.select = true;
        } else {
          // 切换一级分类，默认选中第一条数据
          if (this.treeData.length) {
            curr = this.treeData[0];
            curr.select = true;
          }
        }
        this.optRow = Object.assign({}, curr);
      } else {
        // 搜索数据，选中之前选中的数据
        const _self = this;
        let recursion = function(id, childSelect) {
          const ele = _self.treeMap[id];
          if (ele) {
            ele.select = childSelect ? false : true;
            ele.childSelect = childSelect ? true : false;
            if (ele.parentId) {
              // 父节点存在时，递归选中父节点
              const parentId = `${ele.deep - 1}-${ele.parentId}`;
              _self.$set(_self.treeMap[parentId], "expanded", true);
              recursion(parentId, true);
            }
          }
        };
        recursion(`${this.optRow.deep}-${this.optRow.categoryWid}`);
        recursion = null;
      }
    },
    handleSelect(curr) {
      const _self = this;
      this.handleClickTrack({
        infoType: 8,
        filterInfo: {
          categoryWid: this.categroyType.categoryWid,
        },
      }); // 点击埋点
      // 子节点和父节点存在categoryWid相同的情况
      if (
        curr.categoryWid === this.optRow.categoryWid &&
        curr.deep === this.optRow.deep
      )
        return;
      this.optRow = Object.assign({}, curr);
      let recursion = function(data) {
        data.forEach((item) => {
          if (
            (item.categoryWid === curr.categoryWid &&
              item.deep === curr.deep) ||
            item.categoryWid === curr.parentId
          ) {
            //选中父节点和点击节点
            _self.$set(item, "select", item.categoryWid === curr.categoryWid);
            _self.$set(item, "childSelect", item.categoryWid === curr.parentId);
          } else {
            // 取消选中其他节点
            _self.$set(item, "select", false);
            _self.$set(item, "childSelect", false);
          }
          if (item.children && item.children.length) {
            recursion(item.children);
          }
        });
      };
      recursion(this.treeData);
      recursion = null;
    },
    expandNode() {
      if (!this.searchKeyword) return;
      const _self = this;
      let recursion = function(data) {
        _self.$set(data, "expanded", true);
        if (data.parentId) {
          // 存在父节点
          recursion(_self.treeMap[`${data.deep - 1}-${data.parentId}`]);
        }
      };
      for (const key in this.treeMap) {
        const element = this.treeMap[key];
        if (
          element.deep !== 1 &&
          element.categoryName.toLowerCase().includes(this.searchKeyword)
        ) {
          recursion(element);
        }
      }
      recursion = null;
    },
  },
};
</script>

<style scoped lang="less">
.itemCategoryDetai__body {
  width: 100%;
  margin-top: 30px;
  display: flex;
}
.itemCategoryDetai__body-left {
  width: 20%;
  margin-right: 36px;
  border-right: 1px solid #f5f5f5;
  /deep/.we-input__inner {
    height: 36px;
    line-height: 36px;
  }
  /deep/.we-input--suffix .we-input__inner {
    padding-right: 35px;
  }
  /deep/.we-input__icon {
    line-height: 36px;
  }
}
.itemCategoryDetai__body-right {
  flex: 1;
  min-width: 0;
}
.itemCategoryDetail-input {
  margin-bottom: 16px;
  /deep/.we-input__suffix {
    right: 12px;
    cursor: pointer;
  }
}
</style>
