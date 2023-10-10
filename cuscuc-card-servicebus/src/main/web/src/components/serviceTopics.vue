<template>
  <we-dialog
    :visible="isShowTopics"
    :close-on-click-modal="false"
    :append-to-body="true"
    width="610px"
    @close="handleClose(true)"
    class="serviceTopics"
  >
    <template #title>
      <h3 class="portal-font-color-lv1">
        {{ $Lan(LanFunName, "servicemanage", "专题服务管理") }}
      </h3>
    </template>
    <AutoContainer :con-type="1" :con-height="198">
      <ul class="typeList">
        <li
          v-for="item in list"
          :key="item.typeName"
          @click="handleClick(item)"
          class="portal-font-color-lv1 "
        >
          <span class="list-rf">
            <span
              class="list-rf-son"
              :class="{ 'portal-primary-backgroundcolor-lv1': item.show }"
            ></span>
          </span>
          <we-tooltip
            class="item"
            effect="dark"
            :content="item.typeName"
            :open-delay="800"
            :hide-after="500"
            placement="top"
          >
            <p class="ellipsis">{{ item.typeName }}</p>
          </we-tooltip>
        </li>
      </ul>
    </AutoContainer>
    <div slot="footer" class="dialog-footer">
      <div class="footer-button">
        <we-button
          @click="handleClose()"
          class="text-ellipsis portal-font-color-lv2"
        >
          {{ $Lan(LanFunName, "cancel", "取消") }}
        </we-button>
        <we-button
          class="text-ellipsis portal-primary-backgroundcolor-lv1 portal-primary-backgroundcolor-hover-lv3-rg5"
          @click="handleSubmit"
          :disabled="!this.single"
          :style="{ opacity: this.single ? 1 : 0.5 }"
        >
          {{ $Lan(LanFunName, "confirm", "确定") }}
        </we-button>
      </div>
    </div>
  </we-dialog>
</template>
<script>
import { clickTrack } from '../mixins/track.js';
export default {
  props: {
    isShowTopics: Boolean,
    sourceList: Array,
    card: Object,
    router: Object,
  },
  mixins: [clickTrack],
  data() {
    return {
      list: [],
      LanFunName: "SYS_CAR_SERVICEBUS",
    };
  },
  model: {
    prop: "isShowTopics",
  },
  watch: {
    isShowTopics(newVal) {
      if (newVal) {
        this.list = this.sourceList.map((v) => {
          return { ...v };
        });
        let typeList = document.querySelector(".serviceTopics .typeList");
        typeList && (typeList.offsetTop = 0);
      }
    },
  },
  computed: {
    params() {
      return this.list.reduce((v, w) => {
        if (!w.show) {
          return v ? `${v},${w.typeId}` : `${w.typeId}`;
        }
        return v;
      }, "");
    },
    single() {
      return this.list.reduce((v, w) => {
        if (w.show) {
          return (v += 1);
        }
        return v;
      }, 0);
    },
  },
  created() {},
  methods: {
    handleClose(isTrack) {
      this.$emit("input", false);
      isTrack && this.handleClickTrack();
    },
    handleClick(item) {
      // if (this.single === 1 && item.show) {
      //   return;
      // }
      item.show = !item.show;
      this.handleClickTrack();
    },
    handleSubmit() {
      if (this.single === 0) {
        return;
      }
      window.shell.execCardMethod(
        {
          ...this.card,
          method: "collectClassify",
          param: {
            classifyIds: this.params,
          },
        },
        (res) => {
          if (res.errcode === "0") {
            this.$parent.renderData(null, true);
          }
          this.handleClose();
        }
      );
    },
  },
};
</script>
<style scoped lang="less">
.serviceTopics {
  /deep/ .we-dialog__header {
    padding: 19px 20px;
    // border-bottom: 1px solid #f0f0f0;
    > h3 {
      font-size: 18px;
      letter-spacing: 0;
      line-height: 22px;
      font-weight: 700;
    }
    .we-dialog__close {
      font-size: 14px;
    }
  }
  /deep/ .we-dialog__body {
    padding: 20px 20px 0 20px;
  }
  /deep/ .we-dialog__footer {
    padding: 0px 20px 20px;
    .we-button {
      border-radius: 4px;
      font-size: 14px;
      color: #595959;
      max-width: 275px;
      height: 40px;

      &:hover {
        background-color: white;
        border: 1px solid #d9d9d9;
      }
    }
    .we-button:first-child {
      border: 1px solid #d9d9d9;
      background-color: white !important;
    }
    .we-button:last-child {
      border: none;
      color: #fff;
    }
    .footer-button {
      display: flex;
      justify-content: flex-end;
      width: 100%;
      flex-wrap: nowrap;
    }
    .text-ellipsis {
      white-space: nowrap; //当列表内容长度抵达容器边界时不转到下一行
      overflow: hidden; //不显示超过对象尺寸的内容，数据无论有多少，都不会换行
      text-overflow: ellipsis; //将被隐藏的那部分用省略号代替
    }
  }
}

.typeList {
  display: flex;
  flex-wrap: wrap;
  > li {
    cursor: pointer;
    background: #f5f5f5;
    border-radius: 4px;
    padding: 14px 16px;
    width: 128px;
    margin-right: 16px;
    text-align: center;
    font-size: 14px;
    overflow: hidden;
    position: relative;
    line-height: 22px;
    margin-bottom: 16px;
    > p {
      width: 100%;
    }
    > .list-rf {
      height: 26px;
      width: 26px;
      background: transparent;
      position: absolute;
      right: 0;
      top: 0;
      &::before {
        position: absolute;
        content: "";
        top: 5px;
        right: 2.2px;
        width: 9.6px;
        height: 7.2px;
        background: url("../assets/勾@2x.png") no-repeat center/100% 100%;
        z-index: 10;
      }
      > .list-rf-son {
        position: absolute;
        background: #d9d9d9;
        left: 0;
        right: 0;
        height: 36.78px;
        width: 36.78px;
        transform-origin: 0 0;
        transform: rotate(-45deg);
      }
    }
  }
  > li:nth-of-type(4n + 4) {
    margin-right: 0px;
  }
}
</style>
