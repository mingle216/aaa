<template>
  <div class="his__dropDown__wrap">
    <div class="his__dropDown-info clear portal-font-color-lv3">
      {{ $Lan("CUS_TEMPLATE_CQUPT", "historySearch", "历史搜索") }}
      <i
        class="we-icon-delete portal-font-color-lv4 his__dropDown-delete portal-primary-color-hover-lv1"
        @click="deleteHis"
      ></i>
    </div>
    <div class="his__dropDown__con">
      <div class="his__dropDown__col">
        <div
          class="his__dropDown-list portal-primary-color-hover-lv1"
          v-for="(item, index) in leftData"
          :key="index"
          @click="openSearchResult(item)"
        >
          <div
            class="his__dropDown-index"
            :class="[index > 2 ? 'portal-font-color-lv3' : '']"
            :style="
              `background: url(${
                hisIcon[index > 2 ? 3 : index]
              }) no-repeat center;background-size: contain;`
            "
          >
            {{ index + 1 }}
          </div>
          <div class="his__dropDown-context ellipsis">{{ item.searchKey }}</div>
        </div>
      </div>
      <div class="his__dropDown__col" v-if="rightData.length">
        <div
          class="his__dropDown-list portal-primary-color-hover-lv1"
          v-for="(item, index) in rightData"
          :key="index"
          @click="openSearchResult(item)"
        >
          <div
            class="his__dropDown-index portal-font-color-lv3"
            :style="`background: url(${hisIcon[3]}) no-repeat center;background-size: contain;`"
          >
            {{ index + 6 }}
          </div>
          <div class="his__dropDown-context ellipsis">{{ item.searchKey }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import hisIcon1 from "../../assets/images/hisIcon-1.png";
import hisIcon2 from "../../assets/images/hisIcon-2.png";
import hisIcon3 from "../../assets/images/hisIcon-3.png";
import hisIcon4 from "../../assets/images/hisIcon-4.png";
export default {
  props: ["historyData"],
  data() {
    return {
      hisIcon: [hisIcon1, hisIcon2, hisIcon3, hisIcon4],
    };
  },
  computed: {
    leftData() {
      return this.historyData.length > 5
        ? this.historyData.slice(0, 5)
        : this.historyData;
    },
    rightData() {
      return this.historyData.length > 5 ? this.historyData.slice(5) : [];
    },
  },
  methods: {
    openSearchResult(item) {
      this.$emit("change-search", item.searchKey);
    },
    deleteHis() {
      window.shell.execTemplateMethod("clearSearchHis", {}, (data) => {
        if (data.errcode === "0") {
          this.$emit("clear-his");
        }
      });
    },
  },
};
</script>

<style scoped>
.his__dropDown__wrap {
  padding: 2px 16px;
}
.his__dropDown-info {
  font-size: 12px;
}
.his__dropDown__con {
  display: flex;
}
.his__dropDown__col {
  flex: 1;
  min-width: 0;
}
.his__dropDown-list {
  cursor: pointer;
  line-height: 22px;
  margin-top: 14px;
  display: flex;
  align-items: center;
}
.his__dropDown-delete {
  cursor: pointer;
  margin-left: 5px;
}
.his__dropDown-index {
  color: #fff;
  font-size: 12px;
  text-align: center;
  width: 18px;
  height: 18px;
  line-height: 18px;
  flex-shrink: 0;
}
.his__dropDown-context {
  flex: 1;
  margin-left: 12px;
}
</style>
