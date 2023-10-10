<template>
  <div class="filter_wrap">
    {{homeRoleWid}}
    <span class="times">
      <span class="title">我要办</span>
      截止<span> {{ currentDate }} </span>
      共上线
      <span style="color: rgb(255, 123, 5)">{{ allServiceItemCount }}</span>
      项服务
    </span>
    <div class="onlineRight">
      <div class="serviceObj">
        <span>服务对象：</span>
        <we-dropdown @command="handleCommand">
          <span class="we-dropdown-link">
            {{ selectedObj }}
            <i class="we-icon-arrow-down we-icon--right"></i>
          </span>
          <we-dropdown-menu slot="dropdown">
            <we-dropdown-item command="all"> 全部</we-dropdown-item>
            <we-dropdown-item
              :command="item.roleWid"
              v-for="item in serviceObject"
              :key="item.roleWid"
            >
              {{ item.roleName }}
            </we-dropdown-item>
          </we-dropdown-menu>
        </we-dropdown>
      </div>
      <div class="checkboxs">
        <we-checkbox v-model="isOnline" @change="handleOnline"> 可在线办理 </we-checkbox>
      </div>
      <div class="checkboxs" style="margin: 0 12px">
        <we-checkbox v-model="isHotSort" @change="changeHotSort">
          {{ $Lan("CUS_CARD_MY_TODO", "hotSort", "按服务热度排序") }}
        </we-checkbox>
      </div>
      <div class="inputSearch">
        <we-input
          class="portal-primary-border-color-lv1"
          placeholder="请输入服务名称关键字"
          v-model="searchKey"
          @keyup.enter.native="filterTableData"
          @input="modifySearchKey"
        >
          <i
            slot="suffix"
            class="we-input__icon we-icon-search portal-primary-color-lv1"
            @click.stop="filterTableData"
          />
        </we-input>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    params: Object,
    allServiceItemCount: Number,
    serviceObject: Array,
    activeSubject: String,
    homeRole: String,
  },
  watch: {
  },
  data() {
    return {
      selectedObj: this.homeRole.roleName ? this.homeRole.roleName : "全部",
      isOnline: false,
      searchKey: "",
      hotSort: "",
      timeSort: "",
      isHotSort: false,
    };
  },
  mounted() {
    
  },
  computed: {
    currentDate() {
      const now = new Date();
      const year = now.getFullYear(); //得到年份
      const month = now.getMonth() + 1; //得到月份
      const date = now.getDate(); //得到日期
      return `${year}-${month < 10 ? "0" + month : month}-${
        date < 10 ? "0" + date : date
      }`;
    },
  },
  methods: {
    handleCommand(name) {
      const temp = this.serviceObject.find((el) => el.roleWid === name);
      this.selectedObj = name === "all" ? "全部" : temp?.roleName;
      this.$emit("update:params", {
        ...this.params,
        roleWids: name === "all" ? "" : name,
      });
      this.filterTableData();
    },
    handleHotSort() {
      if (!this.hotSort) {
        this.hotSort = "asc";
      } else if (this.hotSort === "asc") {
        this.hotSort = "desc";
      } else {
        this.hotSort = "asc";
      }
      this.$emit("update:params", {
        ...this.params,
        visitOrder: this.hotSort,
      });
      this.filterTableData();
    },
    changeHotSort() {
      this.$emit("update:params", {
        ...this.params,
        orderByVisitCount: this.isHotSort,
      });
      this.filterTableData();
    },
    handleTimeSort() {
      if (!this.timeSort) {
        this.timeSort = "asc";
      } else if (this.timeSort === "asc") {
        this.timeSort = "desc";
      } else {
        this.timeSort = "asc";
      }
      // this.filterTableData();
    },
    handleOnline() {
      this.$emit("update:params", {
        ...this.params,
        availableOnline: this.isOnline,
      });
      this.filterTableData();
    },
    changeSearchKey() {
      this.$emit("update:params", {
        ...this.params,
        searchKey: this.searchKey,
        categoryWids: "",
      });
      this.$emit("update:activeSubject", "");
      this.filterTableData();
    },
    modifySearchKey() {
      this.$emit("update:params", {
        ...this.params,
        searchKey: this.searchKey,
      });
    },
    filterTableData() {
      // const params = {
      //   availableOnline: this.isOnline,
      //   orderByVisitCount: this.hotSort,
      //   timeOrder: this.timeSort,
      //   searchKey: this.searchKey,
      // };
      this.$emit("search-table-data");
    },
  },
};
</script>

<style lang="less" scoped>
.filter_wrap {
  height: 76px;
  border-bottom: 1px solid #e5e5e5;
  margin-bottom: 20px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .times {
    color: #a1a4bb;
    // float: left;
  }

  .title {
    font-size: 24px;
    font-weight: bold;
    color: #000;
  }

  .onlineRight {
    display: flex;
    align-items: center;

    /deep/ .we-input__inner {
      width: 232px;
      height: 35px;
      border: 1px solid #4495f6;
      border-radius: 18px;
      padding-right: 40px;
    }

    .serviceObj {
      margin-right: 24px;

      .we-dropdown-link {
        cursor: pointer;
      }

      .we-icon-arrow-down {
        font-size: 12px;
      }
    }

    .onlineTime {
      display: flex;
      align-items: center;
      line-height: 35px;
      margin: 0 40px;
      cursor: pointer;

      .sort_wrap {
        display: inline-flex;
        flex-direction: column;
        height: 24px;

        .tops,
        .bottoms {
          color: #c4c4c4;
          width: 8px;
          height: 8px;
        }
      }
    }

    .onlineHot {
      margin: 0 14px 0 40px;
    }
  }
}
</style>
