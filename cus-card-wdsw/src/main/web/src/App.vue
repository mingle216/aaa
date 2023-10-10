<template>
  <div class="card_wdsw" v-loading="loading">
    <AutoContainer
      :conType="containerType.type"
      :conHeight="containerType.value"
      :conMaxHeight="0"
    >
      <div class="card-wrapper">
        <we-row v-for="(item, index) in showList" :key="index">
          <we-col
            :span="colSpan"
            v-for="value in item"
            :key="value.id"
            :style="`background: #fff;width: ${itemWidth}`"
          >
            <div
              class="card-item"
              :style="`border-color:${hexToRgba(value.color, 0.12)};background: ${
                value.background
              }`"
              @click="goLink(value.link)"
            >
              <div class="card-item-left">
                <p class="portal-font-color-lv1 item-title">{{ value.title }}</p>
                <p>
                  <span class="item-num" :style="`color: ${value.color}`">{{
                    value.num
                  }}</span>
                  <span class="portal-font-color-lv3">{{ value.info }}</span>
                </p>
              </div>
              <div class="card-item-right">
                <img :src="value.icon" alt="" />
              </div>
            </div>
          </we-col>
        </we-row>
      </div>
    </AutoContainer>
  </div>
</template>

<script>
export default {
  props: ["router"],
  data() {
    return {
      loading: false,
      containerType: {
        type: 0,
        value: 0,
      },
      cardConfig: {},
      colSpan: 24,
      dataList: [
        {
          id: "todo",
          title: this.$Lan(this.router.cardId, "todoTitle", "我的审批"),
          num: 0,
          info: this.$Lan(this.router.cardId, "todoInfo", "待审批事项"),
          icon: require("./assets/todo.png"),
          color: "#2A86FF",
          background:
            "linear-gradient(180deg, rgba(42, 134, 255, 0.16) 0%, rgba(255, 255, 255, 0) 100%)",
          link: "",
        },
        {
          id: "done",
          title: this.$Lan(this.router.cardId, "doneTitle", "审批记录"),
          num: 0,
          info: this.$Lan(this.router.cardId, "doneInfo", "已审批事项"),
          icon: require("./assets/done.png"),
          color: "#9D6CE0",
          background:
            "linear-gradient(224deg, rgba(157, 108, 224, 0.20) 0%, rgba(255, 255, 255, 0.00) 100%)",
          link: "",
        },
        {
          id: "my",
          title: this.$Lan(this.router.cardId, "myTitle", "我的发起"),
          num: 0,
          info: this.$Lan(this.router.cardId, "myInfo", "已发起的事项"),
          icon: require("./assets/my.png"),
          color: "#EE7472",
          background:
            "linear-gradient(225deg, rgba(238, 116, 114, 0.15) 0%, rgba(255, 255, 255, 0.00) 100%)",
          link: "",
        },
      ],
      showList: [],
      itemWidth: "100%",
    };
  },
  methods: {
    init() {
      this.loading = true;
      window.shell.execCardMethod(
        {
          cardId: this.router && this.router.cardId,
          cardWid: this.router && this.router.cardWid,
          method: "renderData",
          param: null,
        },
        (data) => {
          this.$nextTick(function () {
            this.loading = false;
          });
          if (data && data.errcode === "0") {
            let config = data.data.config || "";
            config = JSON.parse(config) || {};
            if (typeof config === "string") {
              config = JSON.parse(config) || {};
            }
            this.cardConfig = config;
            this.containerType = Object.assign(this.containerType, config.containerType);
            this.$set(this.dataList[0], "link", config.todoLink);
            this.$set(this.dataList[1], "link", config.doneLink);
            this.$set(this.dataList[2], "link", config.myLink);
            this.setData();
          }
        }
      );
    },
    async setData() {
      const todoData = await window.shell.execCardMethod({
        cardId: "SYS_CARD_TODOTASK",
        cardWid: this.cardConfig.todoCardWid || "6121868871325196",
        method: "getMarkNumber",
      });
      if (todoData.errcode === "0") {
        const num = todoData.data || "0";
        this.$set(this.dataList[0], "num", num);
      }
      const doneData = await window.shell.execCardMethod({
        cardId: "SYS_CARD_DONETASK",
        cardWid: this.cardConfig.doneCardWid || "5186040487255663",
        method: "getMarkNumber",
      });
      if (doneData.errcode === "0") {
        const num = doneData.data || "0";
        this.$set(this.dataList[1], "num", num);
      }
      const myData = await window.shell.execCardMethod({
        cardId: "SYS_CARD_MYTASK",
        cardWid: this.cardConfig.myCardWid || "0867302183254055",
        method: "getMarkNumber",
      });
      if (myData.errcode === "0") {
        const num = myData.data || "0";
        this.$set(this.dataList[2], "num", num);
      }
      const colNum = Number(this.cardConfig.linkDisplayNumSelect);
      const rowNum = Number(this.cardConfig.rowNum);
      let result = [];
      for (var i = 0; i < this.dataList.length; i += colNum) {
        result.push(this.dataList.slice(i, i + colNum));
      }
      this.showList = result.slice(0, rowNum);
      this.itemWidth = `calc(100% / ${colNum} - 12px * ${colNum - 1} / ${colNum})`;
      this.colSpan = 24 / colNum;
    },
    hexToRgba(val, opacity) {
      let color = val.toLowerCase();
      let pattern = /^#([0-9|a-f]{3}|[0-9|a-f]{6})$/;
      if (color && pattern.test(color)) {
        if (color.length == 4) {
          // 将三位转换为六位
          color = "#" + color[1] + color[1] + color[2] + color[2] + color[3] + color[3];
        }
        //处理六位的颜色值
        let colorNew = [];
        for (let i = 1; i < 7; i += 2) {
          colorNew.push(parseInt("0x" + color.slice(i, i + 2)));
        }
        return `rgba(${colorNew.join(",")},${opacity})`;
      }
      return color;
    },
    goLink(url) {
      if (!url) {
        return;
      }
      window.shell.openPage(url, 1, 2);
    },
  },

  mounted() {
    // 获取配置信息
    this.init();
  },
};
</script>

<style lang="less" scoped>
.card-wrapper {
  .we-row {
    margin-bottom: 12px;
    &:last-of-type {
      margin-bottom: 0;
    }

    .we-col {
      margin-right: 12px;
      &:last-of-type {
        margin-right: 0;
      }
    }
  }
  .card-item {
    height: 96px;
    padding: 16px;
    display: flex;
    justify-content: space-between;
    border-width: 1px;
    border-style: solid;
    border-radius: 6px;
    cursor: pointer;

    &-right {
      width: 46px;
      height: 46px;

      img {
        width: 100%;
        height: 100%;
      }
    }

    &-left {
      flex: 1;

      .item-title {
        font-size: 16px;
        margin-bottom: 10px;
      }

      .item-num {
        font-size: 30px;
        font-weight: 700;
        margin-right: 8px;
      }
    }
  }
}
</style>
