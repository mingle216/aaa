<template>
  <div class="my_todo" id="MyTodo">
    <FilterHead
      ref="filterHead"
      :params.sync="params"
      :serviceObject="serviceObject"
      :activeSubject.sync="activeSubject"
      :allServiceItemCount="Number(allServiceItemCount)"
      :homeRole="homeRole"
      @search-table-data="searchTableData"
    />

    <div class="classify_content">
      <ThemeTab
        :loading="loading"
        :params.sync="params"
        :serviceSubject="serviceSubject"
        :activeSubject="activeSubject"
        @change-theme-tab="changeThemeTab"
      />
      <div class="theme_content">
        <SecondClassify
          :params.sync="params"
          :serviceSubject="serviceSubject"
          :activeSubject="activeSubject"
          :activeSecondSubject="activeSecondSubject"
          @fetch-table-data="searchTableData"
        />
        <ServiceTable
          :page.sync="page"
          :tableData="tableData"
          :tableLoading="tableLoading"
          @collect-item="collectItem"
          @fetch-table-data="searchTableData"
        />
      </div>
    </div>
  </div>
</template>

<script>
import FilterHead from "./components/FilterHead";
import ThemeTab from "./components/ThemeTab";
import SecondClassify from "./components/SecondClassify";
import ServiceTable from "./components/ServiceTable";

export default {
  components: {
    FilterHead,
    ThemeTab,
    SecondClassify,
    ServiceTable,
  },
  props: {
    index: Number,
    router: Object,
  },
  data() {
    return {
      loading: false,
      itemLoading: {},
      tableLoading: false,
      activeSubject: "", // 当前选中的服务主题
      activeSecondSubject: "all", // 二级分类主题
      serviceObject: [], // 服务对象
      serviceSubject: [], // 服务主题
      allServiceItemCount: 0, // 上线数量
      tableData: [],
      params: {
        categoryWids: "",
        searchKey: "",
        roleWids: "",
        orderByVisitCount: false,
        availableOnline: false,
      },
      page: {
        total: 0,
        current: 1,
        size: 10,
      },
      homeRole: "",
    };
  },
  computed: {
    secondSubject() {
      const temp = this.serviceSubject.find((el) => el.subjectWid === this.activeSubject);
      const children = temp?.children ?? [];
      return children.map((el) => ({
        ...el,
        subjectName: el.subjectName?.replace(/"/g, ""),
      }));
    },
  },
  methods: {
    changeThemeTab(wid) {
      this.activeSubject = wid;
      this.page.current = 1;
      this.page.size = 10;
    },
    init() {
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "getConfigs",
        },
        (res) => {
          if (res.errcode === "0" && res.data) {
            this.serviceObject = res.data.serviceObject || [];
            this.serviceSubject = res.data.serviceSubject || [];
            this.allServiceItemCount = res.data.allServiceItemCount || 0;
          }
          const strObj = localStorage.getItem("myTodo");
          const obj = JSON.parse(strObj || "{}");
          const filterRole = localStorage.getItem("filterRole");
          const roleObj = JSON.parse(filterRole || "{}");
          let tempWid = "";
          if (this.serviceSubject.length) {
            if (obj && obj.itemWid) {
              let temp = this.serviceSubject.find(
                (item) => item.subjectWid === obj.itemWid
              );
              const children = temp?.children ?? [];
              this.activeSubject = obj?.itemWid;
              tempWid = children.map((e) => e.subjectWid).join(",");
            } else {
              this.activeSubject = this.serviceSubject[0].subjectWid;
            }
            this.activeSecondSubject = obj?.secondWid || "all";
            this.params.categoryWids =
              this.activeSecondSubject === "all"
                ? tempWid || ""
                : this.activeSecondSubject;
          }
          this.$nextTick(() => {
            this.loading = false;
            if (obj && obj.url === "hall" || roleObj && roleObj.roleWid) {
              this.searchTableData();
              setTimeout(() => {
                this.getPlace();
              }, 500);
            }
          });
        }
      );
    },
    searchTableData() {
      this.tableLoading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router.cardId,
          cardWid: this.router.cardWid,
          method: "searchServiceItem",
          param: {
            ...this.params,
            pageNumber: this.page.current,
            pageSize: this.page.size,
          },
        },
        (res) => {
          if (res.errcode === "0" && res.data) {
            const data = res.data;
            this.tableData = (data && data.data) || [];
            this.page.total = res.data.total || 0;
          }
          this.$nextTick(() => {
            this.tableLoading = false;
          });
        }
      );
    },
    async collectItem(item) {
      if (this.itemLoading[item.itemWid]) {
        return;
      }
      this.itemLoading[item.itemWid] = true;
      await window.shell.collectServiceItem({
        cardId: this.$options.name,
        id: item.itemWid, //事项ID 收藏或取消收藏时用
        operate: item.favorite ? 0 : 1, // 收藏操作标识 0：取消收藏 1：收藏
      });
      item.favorite = item.favorite ? false : true;
    },
    test() {
      localStorage.removeItem("myTodo");
      localStorage.removeItem("filterRole");
    },
    getPlace() {
      let con_el = document.getElementById("MyTodo"); // 当前导航定位处
      if (con_el) {
        const v_top = window.shell.getElementTop(con_el);
        window.shell.emit("vs-scroll-to", { y: v_top - 100 }, 400);
      }
    },
    process(el) {
      this.itemLoading[el.id] = false;
      if (el.cardId !== this.$options.name) {
        this.searchTableData();
      }
    },
  },
  created() {
    const strObj = localStorage.getItem("myTodo");
    const obj = JSON.parse(strObj || "{}");
    if (obj && obj.itemWid) {
      this.activeSubject = obj.itemWid;
    }
    const filterRole = localStorage.getItem("filterRole");
    const roleObj = JSON.parse(filterRole || "{}");
    if (roleObj && roleObj.roleWid) {
      this.homeRole = roleObj;
      this.params.roleWids = this.homeRole.roleWid;
    }
    this.activeSecondSubject = obj?.secondWid || "all";
    this.params.categoryWids =
      this.activeSecondSubject === "all"
        ? this.secondSubject.map((e) => e.subjectWid).join(",")
        : this.activeSecondSubject;
    this.init();
    window.addEventListener("beforeunload", (e) => this.test(e));
    window.shell.on("collectServiceItem", this.process);
  },
  mounted() {},
  beforeDestroy() {
    window.removeEventListener("beforeunload", (e) => this.test(e));
    window.shell.off("collectServiceItem");
  },
};
</script>

<style lang="less" scoped>
.my_todo {
  height: 100%;
  /*background: #fff;*/

  .classify_content {
    display: flex;
  }

  .theme_content {
    width: calc(100% - 280px);
  }
}
</style>
