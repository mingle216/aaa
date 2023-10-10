<template>
  <div>
    <ul class="second_wrap">
      <li class="second_title">二级分类：</li>
      <li
        title="全部分类"
        :class="{
          second_item: true,
          'active portal-primary-color-lv1': activeTab === 'all',
        }"
        @click="changeSecondClassify('all')"
      >
        全部分类
      </li>
      <li
        v-for="item in secondSubject"
        :key="item.subjectWid"
        :title="item.subjectName"
        :class="{
          second_item: true,
          'portal-primary-backgroundcolor-hover-lv4': true,
          'portal-primary-backgroundcolor-lv4 portal-primary-color-lv1':
            activeTab === item.subjectWid,
        }"
        @click="changeSecondClassify(item.subjectWid)"
      >
        {{ item.subjectName.replace(/\"/g, "") }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    params: Object,
    serviceSubject: Array,
    activeSubject: String,
    activeSecondSubject: String,
  },
  watch: {
    activeSubject(val) {
      if (val) {
        this.activeTab = "all";
        this.$emit("update:params", {
          ...this.params,
          categoryWids: this.secondSubject.map((e) => e.subjectWid).join(","),
        });
        this.$emit("fetch-table-data");
      }
    },
  },
  data() {
    return {
      activeTab: this.activeSecondSubject,
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
    changeSecondClassify(wid) {
      this.activeTab = wid;

      this.$emit("update:params", {
        ...this.params,
        categoryWids:
          wid === "all" ? this.secondSubject.map((e) => e.subjectWid).join(",") : wid,
      });
      this.$emit("fetch-table-data");
    },
  },
};
</script>

<style lang="less" scoped>
.second_wrap {
  margin-bottom: 10px;
  overflow: hidden;

  .second_title {
    color: #212a39;
    font-weight: bold;
    float: left;
    line-height: 30px;
    margin: 0 10px 10px 0;
  }

  .second_item {
    float: left;
    height: 30px;
    background: #f4f6f9;
    border-radius: 4px;
    text-align: center;
    cursor: pointer;
    padding: 6px 12px 7px;
    margin: 0 8px 10px 0;

    &:hover {
      color: #276eed;
      background: rgba(39, 110, 237, 0.1);
    }
  }

  .active {
    background: rgba(39, 110, 237, 0.1);
  }
}
</style>
