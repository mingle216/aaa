<template>
  <div class="chooseTree">
    <div class="chooseBox__header plr-12 chooseFlex">
      <span class="ellipsis" style="flex: 1;font-weight:bold">
        {{ $Lan(funcName, "organization", "组织机构") }}</span
      >
      <we-input
        v-model="keyword"
        :placeholder="placeholder"
        size="small"
        @keyup.native.enter="handleSearch"
        style="flex-shrink:0;width:352px"
      >
        <we-select
          v-model="filterType"
          slot="prepend"
          popper-class="filterTypePopper"
          @change="handleSearch"
        >
          <we-option
            :label="$Lan(funcName, 'user', '成员')"
            value="user"
          ></we-option>
          <we-option
            :label="$Lan(funcName, 'organization', '组织机构')"
            value="org"
          ></we-option>
        </we-select>
        <span slot="suffix">
          <span v-if="keyword.trim()" @click.stop="handleClear">
            <i
              class="we-input__icon iconfont icon-toastfailure portal-font-color-lv4"
              style="font-size: 14px;margin-right:12px"
            ></i>
          </span>
          <span @click.stop="handleSearch">
            <i
              class="we-input__icon iconfont icon-CardSearch portal-font-color-lv4"
              style="font-size: 14px;"
            ></i>
          </span>
        </span>
      </we-input>
    </div>
    <div class="chooseTree__body" v-show="!showSearch">
      <div class="chooseTree__body-col">
        <ContainerScroll style="height:100%" :barKeepShow="true">
          <we-tree
            ref="Tree"
            v-if="treeLists.length"
            class="portal-font-color-lv1"
            node-key="wid"
            :data="treeLists"
            :props="defaultProps"
            :expand-on-click-node="false"
            :default-expanded-keys="defaultExpand"
            :highlight-current="true"
            :current-node-key="checkedNode.wid"
            :render-content="renderContent"
            @node-click="handleChangeOrg"
          >
          </we-tree>
          <EmptyCon
            v-if="!treeLists.length"
            :tip="$Lan(funcName, 'noData', '暂无内容')"
            mainTipClass="portal-font-color-lv3"
          ></EmptyCon>
        </ContainerScroll>
      </div>
      <div class="chooseTree__body-col" v-loading="userLoading">
        <ContainerScroll
          style="height:100%"
          :barKeepShow="true"
          @vshandle-scroll="handleScroll"
        >
          <div ref="UserScroll">
            <we-checkbox
              v-if="page.total > 0"
              class="plr-12 chooseUser__row"
              v-model="checkAll"
              :disabled="allDisabled"
              @change="handleChangeAll"
              >{{ $Lan(funcName, "all", "快捷选择") }} ({{
                page.total || 0
              }})</we-checkbox
            >
            <we-checkbox
              v-for="item in userLists"
              :key="item.wid"
              :disabled="!item.checked && checkedIds.length >= maxNum"
              v-model="item.checked"
              class="plr-12 chooseUser__row"
              @change="handleChangeUser(item, true)"
              >{{ item.userName }}&nbsp;<span class="portal-font-color-lv3"
                >({{ item.userAccount }})</span
              ></we-checkbox
            >
          </div>
          <EmptyCon
            v-if="!userLoading && !userLists.length"
            :tip="$Lan(funcName, 'noData', '暂无内容')"
            mainTipClass="portal-font-color-lv3"
          ></EmptyCon>
        </ContainerScroll>
      </div>
    </div>
    <!-- 搜索用户 -->
    <div
      class="chooseTree__body"
      v-if="showSearch"
      v-loading="searchUserLoading"
    >
      <ContainerScroll
        style="height:100%"
        :scrollingX="false"
        :barKeepShow="true"
        ><we-checkbox
          v-for="item in searchUserLists"
          :key="item.wid"
          v-model="item.checked"
          class="plr-12 chooseUser__row"
          :disabled="!item.checked && checkedIds.length >= maxNum"
          @change="handleChangeSearchUser(item)"
          ><div
            class="ellipsis"
            @mouseenter="(e) => handleMouseEnter(e, item)"
            @mouseleave="handleMouseLeave"
          >
            <span v-html="strHighlighted(item.userName)"></span>&nbsp;<span
              class="portal-font-color-lv3"
              v-html="
                `(${strHighlighted(item.userAccount)}) &nbsp;${
                  item.deptName ? item.deptName : ''
                }`
              "
            ></span></div
        ></we-checkbox>
        <we-tooltip
          ref="Tooltip"
          effect="dark"
          :content="tooltipContent"
          placement="bottom"
        >
        </we-tooltip>
        <EmptyCon
          v-if="!searchUserLoading && !searchUserLists.length"
          :tip="$Lan(funcName, 'searchEmpty', '暂无搜索结果')"
          mainTipClass="portal-font-color-lv3"
        ></EmptyCon
      ></ContainerScroll>
    </div>
  </div>
</template>

<script>
const debounce = function(fn, delay) {
  let timeoutID = null;
  return function() {
    let context = this;
    let args = arguments;
    if (timeoutID) {
      clearTimeout(timeoutID);
    }
    timeoutID = setTimeout(function() {
      fn.apply(context, args);
      timeoutID = null;
    }, delay);
  };
};
export default {
  props: ["router", "lists", "checkedIds"],
  data() {
    return {
      funcName: this.router.cardId,
      keyword: "",
      showSearch: false,
      treeLists: [],
      userLists: [],
      userLoading: false,
      searchUserLists: [],
      searchUserLoading: false,
      defaultExpand: [],
      defaultProps: {
        label: "name",
      },
      checkedNode: {},
      page: {
        total: 0,
        current: 1,
        pageSize: 10,
      },
      maxNum: 1000,
      checkAll: false,
      filterType: "user",
      tooltipContent: "",
    };
  },
  computed: {
    allDisabled() {
      const uncheck = this.userLists.filter((item) => !item.checked);
      return (
        !this.checkAll &&
        (this.checkedIds.length >= this.maxNum ||
          this.checkedIds.length + uncheck.length > this.maxNum)
      );
    },
    placeholder() {
      if (this.filterType == "user") {
        return this.$Lan(this.funcName, "searchPlaceholder", "请输入姓名/工号");
      }
      return this.$Lan(
        this.funcName,
        "searchOrgPlaceholder",
        "请输入组织机构名称"
      );
    },
  },
  mounted() {
    this.fetchAuthData();
  },
  created() {
    this.activateTooltip = debounce(
      (tooltip) => tooltip.handleShowPopper(),
      50
    );
  },
  methods: {
    strHighlighted(key) {
      if (!this.searchKey) {
        return key;
      }
      return window.shell.strHighlighted(key, this.searchKey);
    },
    renderContent(h, { node }) {
      return h("span", {
        class: "we-tree-node__label",
        domProps: {
          innerHTML: this.strHighlighted(node.label),
        },
      });
    },
    handleChangeOrg(data) {
      this.checkedNode = data;
      this.page.current = 1;
      if (data.canChoose == 1) {
        this.fetchOrgUser();
      } else {
        this.userLists = [];
        this.page.total = 0;
      }
    },
    handleChangeUser(item, calAll) {
      this.$emit("check-change", item);
      if (calAll) {
        if (item.checked) {
          this.$nextTick(function() {
            this.calAllChecked();
          });
        } else {
          this.checkAll = false;
        }
      }
    },
    handleChangeSearchUser(item) {
      this.$emit("check-change", item);
      const obj = this.userLists.find((curr) => curr.wid === item.wid);
      let updateAll = false;
      if (obj) {
        obj.checked = item.checked;
        updateAll = true;
      } else if (this.checkedNode.userArr) {
        updateAll = this.checkedNode.userArr.find((wid) => wid === item.wid)
          ? true
          : false;
      }
      if (updateAll) {
        if (item.checked) {
          this.$nextTick(function() {
            this.calAllChecked();
          });
        } else {
          this.checkAll = false;
        }
      }
    },
    handleChangeAll() {
      if (this.checkAll && this.page.total > this.userLists.length) {
        this.page.current = 1;
        this.fetchOrgUser(false, true);
      } else {
        this.userLists.forEach((element) => {
          element.checked = this.checkAll ? true : false;
          this.handleChangeUser(element);
        });
      }
    },
    calAllChecked() {
      if (this.checkedNode.userArr) {
        const checked = this.checkedNode.userArr.filter(
          (wid) => this.checkedIds.findIndex((curr) => curr === wid) > -1
        );
        this.checkAll = checked.length && checked.length === this.page.total;
      } else {
        this.checkAll = false;
      }
    },
    resetChecked(wid) {
      if (wid) {
        if (this.showSearch) {
          const searchItem = this.searchUserLists.find(
            (item) => item.wid === wid
          );
          if (searchItem) {
            searchItem.checked = false;
          }
        }
        const item = this.userLists.find((item) => item.wid === wid);
        if (item) {
          item.checked = false;
          this.checkAll = false;
        }
      } else {
        this.userLists.forEach((element) => {
          element.checked = false;
        });
        if (this.showSearch) {
          this.searchUserLists.forEach((element) => {
            element.checked = false;
          });
        }
        this.checkAll = false;
      }
    },
    handleScroll(vertical) {
      if (
        vertical.process > 0.8 &&
        this.page.total > this.userLists.length &&
        !this.userLoading
      ) {
        const scollHeight = this.$refs.UserScroll.scrollHeight;
        if (vertical.scrollTop + 370 > scollHeight - 10) {
          this.page.current += 1;
          this.fetchOrgUser(true);
        }
      }
    },
    handleClear() {
      this.keyword = "";
      this.handleSearch();
    },
    handleSearch() {
      this.searchKey = this.keyword.trim();
      if (this.filterType === "org") {
        this.showSearch = false;
        this.searchUserLoading = false;
        this.handleTreeFilter();
        return;
      }
      if (this.searchKey) {
        this.showSearch = true;
        this.searchUserLoading = true;
        window.shell
          .searchUserByKeywordAndOrgwid({
            orgIds: this.orgIds,
            keyword: this.searchKey,
          })
          .then((res) => {
            this.searchUserLoading = false;
            this.searchUserLists = (res?.data || []).map((item) => ({
              ...item,
              checked: this.checkedIds.find((curr) => curr === item.wid)
                ? true
                : false,
            }));
          });
      } else {
        this.showSearch = false;
      }
    },
    handleTreeFilter() {
      const lists = JSON.parse(this.orginLists);
      this.defaultExpand = lists.length ? [lists[0].wid] : [];
      if (this.checkedNode) {
        this.defaultExpand.push(this.checkedNode.pwid);
      }
      if (!lists.length || !this.searchKey) {
        this.treeLists = lists;
        this.$nextTick(function() {
          this.checkedNode &&
            lists.length &&
            this.$refs.Tree.setCurrentKey(this.checkedNode.wid);
        });
        return;
      }
      const searchKey = this.searchKey.toLowerCase();
      let traverse = (nodes) => {
        for (let i = 0; i < nodes.length; i++) {
          const node = nodes[i];
          if (nodes.wid === this.checkedNode.wid) {
            node.isCurrent = true;
          }
          if (node.name.toLowerCase().includes(searchKey)) {
            this.defaultExpand.push(node.pwid);
            continue;
          }
          const childNodes = node.children || [];
          traverse(childNodes);
          if (!childNodes.length) {
            nodes.splice(i, 1);
            i--;
          }
        }
      };
      traverse(lists);
      traverse = null;

      this.treeLists = lists;
      this.$nextTick(function() {
        this.checkedNode &&
          lists.length &&
          this.$refs.Tree.setCurrentKey(this.checkedNode.wid);
      });
    },
    handleMouseLeave() {
      const tooltip = this.$refs.Tooltip;
      if (tooltip) {
        tooltip.setExpectedState(false);
        tooltip.handleClosePopper();
      }
    },
    handleMouseEnter(event, row) {
      // 判断是否text-overflow, 如果是就显示tooltip
      const cellChild = event.target;
      const range = document.createRange();
      range.setStart(cellChild, 0);
      range.setEnd(cellChild, cellChild.childNodes.length);
      const rangeWidth = range.getBoundingClientRect().width; //有实际元素宽度有不到1px偏差
      if (
        (rangeWidth - 1 > cellChild.offsetWidth ||
          cellChild.scrollWidth > cellChild.offsetWidth) &&
        this.$refs.Tooltip
      ) {
        const tooltip = this.$refs.Tooltip;
        this.tooltipContent = `${row.userName} (${
          row.userAccount
        }) ${row.deptName || ""}`;
        tooltip.referenceElm = event.target;
        tooltip.$refs.popper && (tooltip.$refs.popper.style.display = "none");
        tooltip.doDestroy();
        tooltip.setExpectedState(true);
        this.activateTooltip(tooltip);
      }
    },
    toTree(data) {
      let result = [];
      let map = {};
      const orgIds = [];
      data.forEach((item) => {
        map[item.wid] = item;
        item.canChoose == 1 && orgIds.push(item.wid);
      });
      this.orgIds = orgIds;
      data.forEach((item) => {
        item.checked = false;
        let parent = map[item.pwid];
        item.parentName = parent?.name;
        if (parent) {
          (parent.children || (parent.children = [])).push(item);
        } else {
          result.push(item);
        }
      });
      return result;
    },
    fetchOrgUser(fromPage, checkAll) {
      this.userLoading = true;
      window.shell
        .queryOrgUsers({
          orgId: this.checkedNode.wid,
          pageNumber: this.page.current,
          pageSize: this.page.pageSize,
          isAll: checkAll ? 1 : 0,
        })
        .then((data) => {
          this.userLoading = false;
          if (data && data.errcode === "0") {
            this.page.total = data.page?.total || 0;
            if (fromPage) {
              this.userLists = this.userLists.concat(
                (data.page?.records || []).map((item) => {
                  item.checked = this.checkedIds.find(
                    (curr) => curr === item.wid
                  )
                    ? true
                    : false;
                  return item;
                })
              );
            } else {
              if (checkAll) {
                // 全选
                const checkedArr = (data.page?.records || []).filter(
                  (item) =>
                    this.checkedIds.findIndex((curr) => curr === item.wid) ===
                    -1
                );
                if (checkedArr.length + this.checkedIds.length > this.maxNum) {
                  window.shell.showMessage({
                    type: "warning",
                    message: this.$Lan(
                      this.cardId,
                      "selectPeopleExceed",
                      "已达到上限1000人"
                    ),
                  });
                  this.checkAll = false;
                  return;
                }
              }
              this.userLists = (data.page?.records || []).map((item) => {
                if (checkAll) {
                  item.checked = true;
                  this.handleChangeUser(item);
                } else {
                  item.checked = this.checkedIds.find(
                    (curr) => curr === item.wid
                  )
                    ? true
                    : false;
                }
                return item;
              });
              this.page.current = Math.ceil(
                this.userLists.length / this.page.pageSize
              );
            }
            if (
              !this.checkedNode.userArr ||
              (this.page.total && this.userLists.length === this.page.total)
            ) {
              this.checkedNode.userArr = this.userLists.map((item) => item.wid);
            }
            // 更新全选按钮状态
            if (checkAll) {
              return;
            }
            if (this.checkedNode.userArr && this.checkedNode.userArr.length) {
              this.calAllChecked();
            } else {
              this.checkAll = false;
            }
          }
        });
    },
    fetchAuthData() {
      window.shell
        .execCardMethod({
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "getGroupScheduleConfig",
        })
        .then((data) => {
          if (data && data.errcode === "0") {
            const res = data.data?.groupCalReceivers || {};
            const lists = this.toTree(res.orgs || []);
            this.orginLists = JSON.stringify(lists);
            this.treeLists = lists;
            if (this.treeLists.length) {
              const first = this.treeLists[0];
              this.defaultExpand = [first.wid];
              this.checkedNode = first;
              if (first.canChoose == 1) {
                this.fetchOrgUser();
              }
            }
          }
        });
    },
  },
};
</script>

<style lang="less" scoped>
.plr-12 {
  padding: 0 12px;
}
.chooseFlex {
  display: flex;
  align-items: center;
}
.chooseBox__header {
  background: #fafafa;
}
.chooseTree {
  /deep/.we-input__prefix {
    left: 12px;
  }
  /deep/.we-input--prefix .we-input__inner {
    padding-left: 35px;
  }
  /deep/.empty-con .empty-img {
    max-height: 90px;
  }
  /deep/.we-input-group__prepend {
    width: 94px;
    background-color: #ffffff;
    padding: 0;
    .we-input__inner {
      border: none;
      padding-left: 8px;
      height: 30px;
      line-height: 30px;
    }
    .we-input__suffix {
      right: 2.5px;
      top: 0;
    }
    .we-select {
      margin: 0;
    }
    .we-input__icon {
      line-height: 30px;
    }
  }
  /deep/.we-input-group > .we-input__inner {
    padding-right: 50px;
  }
  /deep/.we-input-group > .we-input__suffix {
    pointer-events: auto;
  }
  /deep/.we-input__suffix {
    background-color: #ffffff;
    height: 30px;
    top: 1px;
    .we-input__icon {
      cursor: pointer;
    }
  }
  /deep/.we-tree {
    padding-top: 6px;
    .we-tree-node__content {
      height: 38px;
    }
    .we-tree-node__expand-icon {
      font-size: 18px;
      &.is-leaf {
        color: transparent;
      }
    }
    .we-tree-node__content > .we-tree-node__expand-icon {
      padding: 8px;
    }
    .we-tree-node__label {
      padding-right: 5px;
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      word-break: break-all;
      margin-left: -4px;
    }
  }
  /deep/.__rail-is-vertical,
  /deep/.__rail-is-horizontal {
    background: #fff !important;
  }
  /deep/.we-checkbox {
    margin-right: 0;
    display: flex;
    align-items: center;
    .we-checkbox__label {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      word-break: break-all;
      line-height: 22px;
      margin-top: -1px;
      font-weight: initial;
    }
  }
}
.chooseTree__body {
  height: 370px;
  display: flex;
}
.chooseTree__body-col {
  &:first-child {
    border-right: 1px solid #f0f0f0;
    flex: 1;
  }
  &:last-child {
    width: 235px;
    flex-shrink: 0;
  }
}
.chooseUser__row {
  margin-top: 16px;
}
</style>

<style lang="less">
.filterTypePopper {
  .we-select-dropdown__list {
    padding: 0;
  }
  &.we-popper[x-placement^="bottom"] {
    margin-top: 4px;
    .popper__arrow {
      display: none;
    }
  }
}
</style>
