<template>
  <div class="main-con">
    <template v-if="dataList.length">
      <we-row
        v-for="(f_item, index) in items"
        :key="index"
        :gutter="20"
        :class="[index === items.length - 1 ? 'last-row' : '']"
      >
        <we-col :span="colNum" v-for="item in f_item" :key="item.subjectWid">
          <service-item
            :data-item="item"
            @clickItem="clickItemHandle"
            :config="config"
            :is-dept="isDept"
            v-if="item.subjectWid"
          ></service-item>
          <ServiceItemMore
            v-else
            :data-item="item"
            @clickItem="clickItemHandle"
            :config="config"
            :is-dept="isDept"
          >
          </ServiceItemMore>
        </we-col>
      </we-row>
    </template>
    <template v-else>
      <emptyCon :tip="$Lan(lanFunName, 'tipMsg2', '暂无服务事项权限')" />
    </template>
  </div>
</template>

<script>
import serviceItem from "./ServiceItem";
import ServiceItemMore from "./ServiceItemMore";
export default {
  components: {
    serviceItem,
    ServiceItemMore,
  },
  props: {
    isDept: {
      type: Boolean,
      default: true,
    },
    dataList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    config: {
      type: Object,
      default: () => {
        return {
          columns: 1,
          rows: 2,
          content: 2,
          departClassIcon: 0, //部门分类下是否显示图标，0：隐藏，1：字体，2：png
          otherClassIcon: 0, //其他分类是否显示图标，0：隐藏，1：显示
        };
      },
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      // tip: {
      //   en_US: {
      //     tipMsg: "No relevant services at present",
      //   },
      //   zh_CN: {
      //     tipMsg: "暂无相关服务事项",
      //   },
      // },
    };
  },
  computed: {
    colNum() {
      if (this.config.columns === 1) {
        return 24;
      } else if (this.config.columns === 2) {
        return 12;
      } else if (this.config.columns === 3) {
        return 8;
      } else if (this.config.columns === 4) {
        return 6;
      } else {
        return 24;
      }
    },
    items() {
      if (this.dataList && this.dataList.length) {
        let arr = [];
        let moreData = {
          iconLink: null,
          itemList: null,
          picLink: null,
          secondCategory: "",
          subjectName: this.$Lan(this.lanFunName, "subjectName", "查看更多"),
          subjectWid: "",
        };
        for (let index in this.dataList) {
          let n = parseInt(index / this.config.columns);
          if (arr.length <= n) {
            arr.push([]);
          }
          if (this.config.columns === 1 && this.config.rows === 1) {
            if (this.dataList.length > 1) {
              arr[n].push(moreData);
            } else {
              arr[n].push(this.dataList[index]);
            }
            return arr;
          } else {
            arr[n].push(this.dataList[index]);
          }

          if (this.config.columns > 1) {
            if (
              arr.length === this.config.rows &&
              this.dataList.length >
                this.config.columns * this.config.rows - 1 &&
              arr[arr.length - 1].length === this.config.columns - 1
            ) {
              arr[n].push(moreData);

              return arr;
            }
          } else if (
            arr.length === this.config.rows - 1 &&
            this.dataList.length > this.config.columns * this.config.rows - 1
          ) {
            arr[n].push(moreData);
            return arr;
          }
        }
        return arr;
      } else {
        return [];
      }
    },
  },
  methods: {
    clickItemHandle(item) {
      this.$emit("clickItem", item);
    },
  },
};
</script>

<style lang="less" scoped>
.main-con {
  /deep/.last-row .ser-item {
    margin-bottom: 0 !important;
  }
}
</style>
