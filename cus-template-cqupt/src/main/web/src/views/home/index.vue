<template>
  <div class="container">
    <div class="home" :style="homeStyle">
      <!-- header -->
      <header-bar :pageData="pageData" />
      <div class="content__wrapper" :class="[leftAbsolute ? 'content__wrapper-absolute' : '']">
        <div class="content__left" ref="LeftContent">
          <LeftInfo :currentUser="currentUser" :leftConfig="leftConfig" :pageData="pageData"></LeftInfo>
        </div>
        <we-container class="content__right" ref="RightContent" :style="rightContainerStyle">
          <!-- <div ref="weMain" @scroll="scroll" :style="mainStyle"> -->
             <ContainerScroll ref="newvs" :maxHeight="scrollHeight" :barKeepShow="true" :size="6"  @vshandle-scroll="scroll">
            <we-main style="overflow-y: auto;padding-right: 12px!important;overflow-x: hidden;">
              <card-layout :todoNum="todoNum" :cardLayout="cardLayout"></card-layout>
            </we-main>
              <we-footer class="home__footer" style="height: auto" v-html="footer"></we-footer>
                </ContainerScroll>
          <!-- </div> -->

        </we-container>

      </div>
    </div>
  </div>
</template>

<script>
/* eslint-disable no-debugger */
import HeaderBar from "../../components/headerBar/index";
import CardLayout from "../cardLayout";
import LeftInfo from "./leftInfo";
export default {
  name: "Home",
  components: {
    HeaderBar,
    CardLayout,
    LeftInfo,
  },
  props: {
    pageData: Object,
  },
  data () {
    return {
      currentUser: window.shell.getUserInfo(),
      leftAbsolute: false,
      // leftStyle: "",
      scrollTop: 0,
      todoNum: 1,


    };
  },
  computed: {
    homeStyle () {
      return `height:${document.documentElement.clientHeight - 56}px`
    },
    mainStyle () {
      let h = document.documentElement.clientHeight - 56 - 16
      if(this.scrollTop){
        h = document.documentElement.clientHeight - 56
      }
      return `height:${h}px;overflow-y:auto;width:100%;`
    },
    scrollHeight() {
      let h = document.documentElement.clientHeight - 56 - 16
      if(this.scrollTop){
        h = document.documentElement.clientHeight - 56
      }
      return h
    },
    rightContainerStyle () {
      if (!this.scrollTop) {
        return `margin-top:16px!important;`
      } else {
        return `margin-top:0px!important;`
      }
    },

    pageRenderData () {
      const rendedata = (this.pageData && this.pageData.renderData) || {};
      return {
        ...rendedata,
        wid: this.pageData && this.pageData.pageInfoEntity.wid,
      };
    },
    cardLayout () {
      return JSON.parse(
        (this.pageData &&
          this.pageData.pageInfoEntity &&
          this.pageData.pageInfoEntity.cardLayout) ||
        "[]"
      );
    },
    footer () {
      const footerConfig = this.pageData
        ? JSON.parse(
          JSON.parse(this.pageData.showProgrammeEntity.templateConfig || "")
        ).footerConfig
        : "";
      if (Object.prototype.toString.call(footerConfig) === "[object Array]") {
        const temp = (footerConfig || []).find(
          (el) => el.langKey === this.$i18n.locale
        );
        const zh = (footerConfig || []).find((el) => el.langKey === "zh_CN");
        return temp?.langValue || zh?.langValue;
      } else {
        return footerConfig;
      }
    },
    cardArea () {
      return (
        this.pageData &&
        this.pageData.renderData &&
        this.pageData.renderData.pageConfig["card.area"]
      );
    },

    leftConfig () {
      const templateConfig = this.pageData
        ? JSON.parse(
          JSON.parse(this.pageData.showProgrammeEntity.templateConfig || "")
        )
        : {};
      return {
        dataSourceConfig: templateConfig.dataSourceConfig || "",
        leftMenuAutoOpen: templateConfig.leftMenuAutoOpen || "0",
      };
    },
  },
  methods: {
    handleScroll (e) {
      const ele = this.$refs.LeftContent;
      const rightEle = this.$refs.RightContent;
      if (ele) {
        const eleHeight = ele.clientHeight; // 元素高度
        if (
          rightEle === undefined ||
          rightEle.$el.scrollHeight <= ele.scrollHeight
        ) {
          //右侧内容高度少于左侧高度时，不改变布局
          this.leftAbsolute = false;
          // this.leftStyle = "";
          return;
        }
        const wrapHeight = document.body.clientHeight; //当前窗口可视区域的高度
        const flag =
          eleHeight < wrapHeight
            ? e.scrollTop > 300
            : e.scrollTop > eleHeight - wrapHeight + 180; // 左侧内容一屏展示不下时，滚动条滚动到距离元素底部20px时改变位置；否则滚动300px就改变位置
        if (flag) {
          this.leftAbsolute = true;
          // this.leftStyle =
          //   eleHeight < wrapHeight ? "top: 20px" : "bottom: 20px";
        } else {
          this.leftAbsolute = false;
          // this.leftStyle = "";
        }
      }
    },
    scroll (vertical) {
      window.shell.emit("onScollNew",vertical);
      this.scrollTop = vertical.scrollTop

    }
  },
  created () {
    window.shell.on("update-login", (data) => {
      this.currentUser = data;
    });
    window.shell.on("onScoll", this.handleScroll);
    window.shell.on("vs-scroll-to-new", (params, speed) => {
      this.$refs.newvs.scrollTo(params, speed);
    });
    window.shell.execCardMethod(
            { cardId:'SYS_CARD_TODOTASK', cardWid:'06248737091599699', method: "getMarkNumber" },
            ({ errcode, data }) => {
              if (errcode === "0") {
                this.todoNum = data
              }
            }
    );
  },
  beforeDestroy () {
    window.shell.off("update-login");
    window.shell.off("onScoll");
  },
};
</script>

<style lang="less" scoped>
.container {
  display: flex;
  background: #f4f6f8;
  .home {
    min-height: 100vh;
    width: 100%;
    flex-direction: column;
    display: flex;
    justify-content: space-between;
    .content__wrapper {
      // margin-top: 24px;
      // padding: 0 24px;
      height: calc(100% - 56px);
      display: flex;
      flex-grow: 1;
      /deep/.gateway-card {
        border-radius: 8px;
        padding: 0 16px 16px;
        &.calenderCard {
          padding: 0;
          border-radius: 8px;
          padding-top:0!important;
        }
      }
      .content__right {
        flex: 1;
        min-width: 0;
        margin: 16px 16px 0 0;
      }
    }
    .content__wrapper-absolute {
      .content__left {
        position: fixed;
        left: 24px;
      }
      .content__right {
        margin-left: 312px;
      }
    }
    .content__left {
      flex-shrink: 0;
      // max-width:176px;
      // min-width:68px;
      // width:176px;
      height: 100%;
      margin: 0 16px 0 0;
      align-self: flex-start;
      background: #ffffff;
      // padding: 16px;
      position: relative;
      //  padding-top: 16px;
    }
    @media screen and (max-width: 1281px) {
      .content__wrapper-absolute {
        .content__left {
          left: 24px;
        }
        .content__right {
          margin-left: 204px;
        }
      }
      .content__left {
        width: 180px;
        margin: 0 24px 0 0;
      }
    }
    .home__footer {
      padding: 0;
    }
    // 事项统计卡片宽度固定1200px，不知道什么原因，work模板暂时取消这一限制
    /deep/.item_count_wrap {
      width: 100%;
    }
  }
}
/deep/.we-card__body,
.we-main {
  padding: 0 !important;
}
</style>
