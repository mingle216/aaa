<template>
  <div class="subscribeModal">
    <div class="subscribeModal__left">
      <div class="subscribeModal__header portal-font-color-lv2">
        <we-checkbox
          v-model="checkAll"
          :disabled="allDisabled"
          @change="handleChangeAll"
          style="flex: 1"
          ><span
            class="text__ellipsis"
            style="max-width: 70px; vertical-align: middle;"
            >{{ $Lan("SYS_CARD_NEWSANNOUNCEMENT", "checkeAll", "全选") }}</span
          >
          <span v-if="!isScribeProgramme" style="vertical-align: middle;margin-left: 4px">
            {{ channelLists.length | formatNum }}
          </span></we-checkbox
        >
        <we-input
          v-model="keyword"
          :placeholder="
            $Lan('SYS_CARD_NEWSANNOUNCEMENT', 'placeholder', '请输入栏目名称')
          "
          style="width: 200px;margin-left: 5px"
          @keyup.native="handleKeyUp"
        >
          <i
            slot="suffix"
            class="we-input__icon iconfont icon-CardSearch portal-font-color-lv4"
            style="font-size: 14px;"
            @click="handleSearch"
          ></i>
        </we-input>
      </div>
      <ContainerScroll
        style="height: calc(70vh - 230px);"
        :scrollingX="true"
        :barKeepShow="true"
      >
        <we-tree
          v-show="treeLists.length"
          ref="ChannelTree"
          node-key="id"
          :data="treeLists"
          :props="defaultProps"
          :render-after-expand="false"
          :check-strictly="isScribeProgramme"
          show-checkbox
          class="portal-font-color-lv1"
          style="margin: 8px 0"
          :default-checked-keys="treeDefaultChecked"
          :default-expanded-keys="treeDefaultExpand"
          @check-change="handleCheck"
          @check="handleClickTrack"
          @node-click="handleClickTrack"
        >
        </we-tree>
        <EmptyCon
          v-if="!treeLists.length"
          :tip="$Lan('SYS_CARD_NEWSANNOUNCEMENT', 'noData', '暂无内容')"
          mainTipClass="portal-font-color-lv2"
        ></EmptyCon>
      </ContainerScroll>
    </div>
    <div class="subscribeModal__right">
      <div
        class="subscribeModal__header portal-font-color-lv2"
        style="padding: 0 16px;"
      >
        <div
          style="flex: 1; min-width: 0; display: flex; align-items: center; margin-right: 12px"
        >
          <span class="ellipsis" v-if="isScribeProgramme">
            {{ $Lan("SYS_CARD_NEWSANNOUNCEMENT", "checked", "已选") }}
          </span>
          <span class="ellipsis" v-else>
            {{ $Lan("SYS_CARD_NEWSANNOUNCEMENT", "checkedPrograms", "已选栏目") }}
          </span>
          <span style="flex-shrink: 0;margin-left: 4px">
            {{ checkedLists.length | formatNum }}
          </span>
        </div>
        <div
          class="portal-font-color-lv3 portal-primary-color-hover-lv1"
          style="cursor: pointer;display: flex; align-items: center;"
          @click="handleReset"
        >
          <i
            class="iconfont icon-Ashcan"
            style="margin-right: 4px;"
          ></i
          ><span
            class="text__ellipsis"
            style="max-width: 110px;"
            >{{ $Lan("SYS_CARD_NEWSANNOUNCEMENT", "reset", "重置") }}</span
          >
        </div>
      </div>
      <ContainerScroll style="height: calc(70vh - 230px);" :barKeepShow="true">
        <div style="padding: 16px" v-if="checkedLists.length">
          <div
            class="subscribeModal__tag"
            v-for="(item, index) in checkedLists"
            :key="item.id"
          >
            <div class="subscribeModal__tag__name">
              <span class="portal-font-color-lv3">{{ item.parentName }}</span
              ><span class="portal-font-color-lv1">{{ item.name }}</span>
            </div>
            <div
              class="subscribeModal__tag__close"
              v-if="!item.isFixed"
              @click="handleCancle(item, index)"
            >
              <i
                class="iconfont icon-toastfailure portal-font-color-lv4 portal-primary-color-hover-lv1"
                style="font-size: 12px; cursor: pointer"
              ></i>
            </div>
          </div>
        </div>
        <EmptyCon
          v-else
          :tip="
            $Lan(
              'SYS_CARD_NEWSANNOUNCEMENT',
              'selectChannels',
              '请选择要订阅的栏目'
            )
          "
        ></EmptyCon>
      </ContainerScroll>
    </div>
  </div>
</template>

<script>
import { clickTrack } from '../mixins/track.js';
export default {
  props: {
    dataLists: Array,
    defaultSelected: Array,
    router: Object,
    isScribeProgramme: Boolean
  },
  mixins: [clickTrack],
  data() {
    return {
      treeLists: [],
      flatTreeList: [],
      checkAll: false,
      keyword: "",
      checkedLists: [],
      channelLists: [],
      programLists: [],
      treeDefaultChecked: [],
      treeDefaultExpand: [],
    };
  },
  computed: {
    allDisabled() {
      if (this.isScribeProgramme) {
        return !this.channelLists.length || this.checkedLists.some(el => !el.isChannel);
      } else {
        return !this.channelLists.length;
      }
    },
    defaultProps() {
      const isScribeProgramme = this.isScribeProgramme
      return {
        label: "name",
        disabled(data, node) {
          const child = node.childNodes;
          const disabledChild = child.filter((item) => item.disabled);
          const flag = isScribeProgramme ? data.disabled : (child.length && child.length === disabledChild.length)
          return (
            data.isFixed || flag
          );
        },
      }
    }
  },
  watch: {
    dataLists: {
      immediate: true,
      handler() {
        this.initData();
      },
    },
  },
  methods: {
    handleReset() {
      this.checkedLists = this.resetLists.concat();
      this.$refs.ChannelTree.setCheckedKeys(this.defaultSelected);
      this.handleIsCheckAll();
      this.$parent.$parent.disabled = !this.checkedLists.length;
      this.handleClickTrack(); //点击埋点
    },
    handleChangeAll(flag) {
      if (flag) {
        const keys = this.channelLists.map((item) => item.id);
        this.$refs.ChannelTree.setCheckedKeys(keys);
      } else {
        const keys = this.channelLists
          .filter((item) => item.isFixed)
          .map((item) => item.id);
        this.$refs.ChannelTree.setCheckedKeys(keys);
      }
      this.handleClickTrack(); //点击埋点
    },
    handleCancle(item) {
      this.$refs.ChannelTree.setChecked(item.id, false);
      // 过滤不在当前树节点中的已选中数据
      this.checkedLists = this.checkedLists.filter(
        (curr) => item.id !== curr.id
      );
      this.handleIsCheckAll();
      this.$parent.$parent.disabled = !this.checkedLists.length;
      this.handleClickTrack(); //点击埋点
    },
    handleCheck(node, checked) {
      if (this.isScribeProgramme) {
        this.handleCheckSiteChange(node, checked)
      } else {
        this.handleCheckChange(node, checked)
      }
    },
    handleCheckChange(node, checked) {
      if (node.isChannel) {
        if (checked) {
          if (!this.checkedLists.find((item) => item.id === node.id)) {
            this.checkedLists.push({
              id: node.id,
              name: node.name,
              isFixed: node.isFixed,
              isChannel: node.isChannel,
              parentName: this.findParentName(node),
            });
          }
        } else {
          this.checkedLists = this.checkedLists.filter(
            (item) => item.id !== node.id
          );
        }
      }
      this.handleIsCheckAll();
      this.$parent.$parent.disabled = !this.checkedLists.length;
    },
    handleCheckSiteChange(node, checked) {
      if (node.isChannel) {
        if (checked) {
          if (!this.checkedLists.find((item) => item.id === node.id)) {
            this.checkedLists.push({
              id: node.id,
              name: node.name,
              isFixed: node.isFixed,
              isChannel: node.isChannel,
              parentName: this.findParentName(node),
            });
          }
        } else {
          this.checkedLists = this.checkedLists.filter(
            (item) => (item.id !== node.id) || item.isFixed
          );
        }
      } else {
        const childrenList = this.flatTreeList.filter(el => el.parentId === node.id)
        const childrenIds = childrenList.map(el => el.id)
        // this.checkedLists = this.checkedLists.filter(el => !childrenIds.includes(el.id))
        if (checked) {
          this.checkedLists.push({
            id: node.id,
            name: node.name,
            isFixed: node.isFixed,
            isChannel: node.isChannel,
            parentName: this.findParentName(node),
          })
          this.$nextTick(() => {
            childrenList.forEach(el => {
              this.$set(el, 'disabled', true)
              if (!el.isFixed) {
                this.$refs.ChannelTree.setChecked(el, false)
              } else {
                this.$refs.ChannelTree.setChecked(el, true)
              }
            })
          })
        } else {
          this.checkedLists = this.checkedLists.filter(
            (item) => (item.id !== node.id) || item.isFixed
          );
          this.$nextTick(() => {
            childrenList.forEach(el => {
              if (!el.isFixed) {
                this.$set(el, 'disabled', false)
              }
            })
          })
        }
      }
      this.handleIsCheckAll();
      this.$parent.$parent.disabled = !this.checkedLists.length;
    },
    handleKeyUp(e) {
      if (e.key === "Enter") {
        this.handleSearch();
      }
    },
    handleSearch() {
      const expandKeys = [];
      let traverse = (data, parent, parentIndex) => {
        for (let i = data.length - 1; i >= 0; i--) {
          const child = data[i];
          const match = child.name
            .toLowerCase()
            .includes(this.keyword.trim().toLowerCase());
          if (!match) {
            if (child.children && child.children.length) {
              traverse(child.children, data, i);
            } else {
              data.splice(i, 1);
            }
          } else {
            expandKeys.push(child.id);
          }
        }
        if (parent && !data.length) {
          parent.splice(parentIndex, 1);
        } else {
          parent && expandKeys.push(parent[parentIndex].id);
        }
      };
      const data = JSON.parse(JSON.stringify(this.originData));
      traverse(data);
      traverse = null;
      // 统计筛选后的栏目
      this.handleFilterChannels(data);
      // 选中筛选前一选中的数据
      this.treeDefaultChecked = this.checkedLists.map((item) => item.id);
      // 搜索结果展示规则：如果关键字是一级，展示一级及下面所有子级（默认不展开）；如果关键字是子级，展示子级关联的父级（默认展开），及下面所有子级（默认不展开）
      if (this.keyword.trim()) {
        this.treeDefaultExpand = expandKeys;
      } else {
        this.treeDefaultExpand = [];
      }
      this.treeLists = data;
      // 计算是否已经全选
      this.handleIsCheckAll();
      this.handleClickTrack({
        infoType: 3,
        filterInfo: {
          keyword: this.keyword.trim()
        }
      }); //点击埋点
    },
    handleFilterChannels(data) {
      const channelLists = [];
      let node,
        list = [...data];
      while ((node = list.shift())) {
        if (node.isChannel) {
          channelLists.push({
            id: node.id,
            name: node.name,
            parentName: this.findParentName(node),
            isFixed: node.isFixed,
          });
        }
        node.children && list.push(...node.children);
      }
      this.channelLists = channelLists;
    },
    handleIsCheckAll() {
      if (!this.channelLists.length) {
        this.checkAll = false;
        return;
      }
      if (this.channelLists.length > this.checkedLists.length) {
        this.checkAll = false;
      } else {
        this.checkAll = this.channelLists.every((item) =>
          this.checkedLists.find((curr) => curr.id === item.id)
        );
      }
    },
    initData() {
      this.handleClear();
      this.flatTreeList = JSON.parse(JSON.stringify(this.dataLists))
      const data = this.tranformToTree(this.flatTreeList.concat());
      this.treeLists = data;
      this.originData = JSON.parse(JSON.stringify(data));
      this.$nextTick(function() {
        this.setDisabled(this.treeLists)
        this.$refs.ChannelTree.setCheckedKeys(this.defaultSelected);
        this.$nextTick(function() {
          this.resetLists = this.checkedLists.concat();
        });
      });
    },
    setDisabled(data) {
      data.forEach(el => {
        this.$set(el, 'disabled', false)
        el.children && this.setDisabled(el.children)
      })
    },
    tranformToTree(data) {
      const obj = {};
      const treeData = [];
      const channelLists = [];
      const programLists = [];
      //将数组中数据转为键值对结构
      data.forEach((el) => {
        obj[el.id] = el;
      });
      for (let i = 0, len = data.length; i < len; i++) {
        const parentId = data[i].parentId;
        const parent = obj[parentId];
        const element = data[i];
        if (element.isChannel) {
          channelLists.push(element);
        } else {
          programLists.push(element)
        }
        if (parentId === "-1") {
          treeData.push(element);
          continue;
        }
        if (parent.children) {
          parent.children.push(element);
        } else {
          parent.children = [element];
        }
      }
      this.channelLists = channelLists;
      this.programLists = programLists;
      return treeData;
    },
    findParentName(item) {
      if (item.parentId === "-1") {
        return "";
      }
      let arr = [""];
      let dataHandle = (t_item) => {
        let t_arr = this.dataLists.filter(
          (m_item) => m_item.id === t_item.parentId
        );
        if (t_arr.length) {
          let dataItem = t_arr[0];
          arr.unshift(dataItem.name);
          if (dataItem.parentId !== "-1") {
            dataHandle(dataItem);
          }
        }
      };
      dataHandle(item);
      return arr.length > 1 ? arr.join("-") : "";
    },
    handleClear() {
      this.keyword = "";
      this.treeDefaultChecked = [];
      this.treeDefaultExpand = [];
      this.checkedLists = [];
      this.checkAll = false;
    },
  },
  filters: {
    formatNum(value) {
      let num = value;
      if (typeof num !== "number") {
        num = Number(num);
      }
      if (isNaN(num)) {
        return 0;
      }
      return num > 99 ? "99+" : num;
    },
  },
};
</script>

<style lang="less" scoped>
.subscribeModal {
  display: flex;
  height: calc(70vh - 174px);
  .subscribeModal__header {
    height: 48px;
    border-bottom: 1px solid #f0f0f0;
    display: flex;
    align-items: center;
    padding: 0 12px;
    /deep/.we-input__suffix {
      cursor: pointer;
      right: 12px;
    }
    /deep/.we-input__inner {
      height: 32px;
      line-height: 32px;
      border-radius: 4px;
      border: 1px solid #d9d9d9;
      padding: 0 30px 0 12px;
    }
    /deep/.we-input__icon {
      line-height: 32px;
    }
  }
  .subscribeModal__left {
    width: 40%;
    margin-right: 16px;
    border-radius: 4px;
    border: 1px solid #f0f0f0;
    /deep/.we-tree {
      display: inline-block;
      min-width: 100%;
    }
    /deep/.__rail-is-vertical,
    /deep/.__rail-is-horizontal {
      background: #fff !important;
    }
  }
  .subscribeModal__right {
    flex: 1;
    min-width: 0;
    border-radius: 4px;
    border: 1px solid #f0f0f0;
    .subscribeModal__tag {
      display: inline-flex;
      background: #f5f5f5;
      border-radius: 4px;
      padding: 4px 12px;
      margin: 0 8px 8px 0;
      align-items: center;
      .subscribeModal__tag__name {
        flex: 1;
        min-width: 0;
        line-height: 22px;
        flex-basis: auto;
      }
      .subscribeModal__tag__close {
        margin-left: 6px;
      }
    }
  }
  /deep/.we-checkbox__label {
    padding-left: 8px;
  }
  /deep/.we-tree-node__content {
    height: 38px;
  }
  /deep/.we-tree-node__expand-icon {
    font-size: 16px;
    &.is-leaf {
      color: transparent;
    }
  }
  /deep/.we-tree .we-checkbox__inner {
    width: 16px;
    height: 16px;
    vertical-align: middle;
  }
  /deep/.we-tree-node__content > .we-tree-node__expand-icon {
    padding: 6px 8px;
  }
  /deep/.we-tree .we-checkbox__inner::after {
    top: 2px;
    left: 5px;
  }
  /deep/.we-tree
    .we-checkbox__input.is-indeterminate
    .we-checkbox__inner::before {
    top: 6px;
  }
  /deep/.we-tree-node__label {
    padding-right: 5px;
  }
}
</style>
