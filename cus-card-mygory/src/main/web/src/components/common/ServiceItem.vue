<template>
  <div
    ref="serviceItem"
    v-if="dataItem && config"
    class="ser-item  portal-primary-backgroundcolor-hover-lv5"
    @click="clickItem"
  >
    <div class="item-icon" v-if="isShowIcon">
      <template v-if="isDept">
        <template v-if="config.departClassIcon === 1">
          <i
                  class="item-icon-font"
                  :class="
                dataItem.iconLink ? dataItem.iconLink : 'iconfont icon-default'
              "
          ></i>
        </template>
        <template v-else>
          <we-image
                  class="item-icon"
                  :src="dataItem.picLink ? dataItem.picLink : defaultImg"
          >
            <div slot="error">
              <img :src="defaultImg" />
            </div>
          </we-image>
        </template>
      </template>
      <template v-else>
        <template v-if="dataItem.iconLink">
          <i class="item-icon-font" :class="dataItem.iconLink"></i>
        </template>
        <template v-else>
          <we-image
                  class="item-icon"
                  :src="dataItem.picLink ? dataItem.picLink : defaultImg"
          >
            <div slot="error">
              <img :src="defaultImg" />
            </div>
          </we-image>
        </template>
      </template>
    </div>
    <div class="item-title dataItem">
      <!-- <div v-else class="item-none-icon">
          <div class="none-icon portal-primary-backgroundcolor-lv1"></div>
      </div> -->
      <div class="title portal-font-color-lv1">
        {{ dataItem.subjectName }}
      </div>
    </div>
    <div class="item-list portal-font-color-lv2">
      <div class="item-con">
        <template v-if="config.content === 1">
          <template v-for="(item, index) in secondCategory">
            <div class="item-item" :key="index" v-if="index < 3">
              <v-clamp autoresize
                       :max-lines="2">
                {{ item }}
              </v-clamp>
            </div>
          </template>
        </template>
        <template v-else>
          <template v-for="(item, index) in dataItem.itemList">
            <!-- @click.stop="openItemDetail(item)" -->
            <div class="item-item" :key="index" v-if="index < 3">
              <v-clamp autoresize
                       :max-lines="2">
                {{ item.itemName }}
              </v-clamp>
            </div>
          </template>
        </template>
      </div>
      <!--<div class="arrow-con">
        <div class="arrow">
          <i class="iconfont icon-shixiangfenleijiantou"></i>
        </div>
      </div>-->
    </div>
  </div>
</template>

<script>
export default {
  props: {
    dataItem: {
      type: Object,
      default: () => {
        return {
          secondCategory: "",
          subjectName: "",
          picLink: "",
          iconLink: "",
          itemList: [],
        };
      },
    },
    isDept: {
      type: Boolean,
      default: true,
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
  },

  computed: {
    isShowIcon() {
      //是否显示图标
      if (
        (this.isDept && this.config.departClassIcon === 0) ||
        (!this.isDept && this.config.otherClassIcon === 0)
      ) {
        return false;
      }
      return true;
    },
    secondCategory() {
      if (this.dataItem?.secondCategory) {
        let arr = this.dataItem?.secondCategory.split(" | ") || [];
        return arr.map((item) => {
          return item.trim();
        });
      }
      return [];
    },
  },
  mounted() {
    console.log(this.dataItem);
  },
  data() {
    return {
      defaultImg: require("../../assets/default.svg"),
    };
  },
  methods: {
    clickItem() {
      this.$emit("clickItem", this.dataItem);
    },
    openItemDetail(item) {
      window.shell.openServiceItem({
        ...item,
        wid: item.itemWid,
        name: item.itemName
      })
    }
  },
};
</script>

<style lang="less" scoped>
.ser-item {
  cursor: pointer;
  border: 2px dotted #d6dade;
  height: 211px;
  box-sizing: border-box;
  border-radius: 4px;
  margin-bottom: 20px;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;

  &:hover {
    border-color: transparent !important;
  }
  .item-icon {
    height: 50px;
    width: 50px;
    margin-bottom: 10px;
    .iconfont {
      font-size: 50px;
    }
    .item-icon {
      width: 50px;
      height: 50px;
      object-fit: contain;
    }
  }

  .item-title {
    height: 27px;
    line-height: 27px;
    margin-bottom: 5px;
    width: 100%;
    text-align: center;
    .title {
      height: 100%;
      font-size: 18px;
      color: #102645;
      font-weight: bold;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  .item-list {
    display: flex;
    .item-item {
      color: rgba(16,38,69,.6)!important;
      font-size: 14px;
      height: 20px;
      line-height: 20px;
      padding: 0 20px;
    }
    .item-con {
    }
    .arrow-con {
      position: relative;
      width: 56px;
      .arrow {
        width: 38px;
        height: 38px;
        line-height: 38px;
        text-align: center;
        border-radius: 4px;
        position: absolute;
        right: 0;
        bottom: 0;
        background-color: #f5f5f5;
        color: #bcbcbc;
      }
    }
  }
}
</style>
