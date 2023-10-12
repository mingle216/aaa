<template>
  <div class="operate-apps" style="max-height: 350px">
    <AutoContainer :con-type="1" :con-height="350" :scroll="conScroll">
      <div class="operate-apps-body">
        <div class="operate-apps-body-list" v-if="newDataList.length">
          <draggable
            v-model="newDataList"
            v-bind="dragOptions"
            @start="dragStart"
            @end="dragging = false"
            class="ty_dragbox"
          >
            <transition-group
              :name="!dragging ? 'flip-list' : null"
              type="transition"
              tag="div"
              class="appsContainer"
            >
              <div
                v-for="item in newDataList"
                :key="item.itemWid"
                class="app-item"
              >
                <i class="we-icon-remove icon-del" @click="delApp(item)"></i>

                <img
                  class="img"
                  :is-dragging="dragging"
                  :src="item.iconLink"
                  @error="handleImgError"
                />

                <we-tooltip
                  class="item"
                  effect="dark"
                  :content="item.itemName"
                  :open-delay="800"
                  placement="top-start"
                >
                  <v-clamp
                    class="name portal-font-color-lv1"
                    autoresize
                    :max-lines="1"
                    >{{ item.itemName }}</v-clamp
                  >
                </we-tooltip>

                <img :src="icondrag" alt class="icon-drag" draggable="true" />
              </div>
            </transition-group>
          </draggable>
        </div>
        <EmptyCon
          :tip="$Lan(lanFunName, 'emptyConTip', '暂无数据')"
          :height="300"
          v-else
        ></EmptyCon>
      </div>
    </AutoContainer>
  </div>
</template>

<script>
import draggable from "vuedraggable";
export default {
  components: { draggable },
  props: {
    router: Object,
    dataList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      appname: "",
      icondrag: require(".././assets/img/icon-drag.png"),
      errorImage: require(".././assets/img/default.jpg"),
      conScroll: { barKeepShow: true, scrollingX: false, scrollingY: true },
      dragging: false,
      dragOptions: {
        animation: 150,
        group: "newDataList",
        disabled: false,
        ghostClass: "ghost",
        forceFallback: true,
      },
      newDataList: [],
      removeData:[]
    };
  },
  methods: {
    dragStart() {
      this.dragging = true;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName
        },
        startTime: new Date().getTime()
      });
    },

    handleImgError($event, newUrl = this.errorImage) {
      $event.target.src = newUrl;
    },
    delApp(item) {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName
        },
        startTime: new Date().getTime()
      });
      var index = this.newDataList.findIndex(function(it) {
        return it.itemWid === item.itemWid;
      });
      this.removeData=this.removeData.concat(this.newDataList.splice(index, 1));
    },
  },

  created() {
    this.newDataList = [...this.dataList];
  },
};
</script>

<style lang="less" scoped>
.operate-apps {
  width: 100%;
  height: 100%;
  /deep/ .auto-con {
    max-height: 350px !important;
  }
  .operate-apps-body {
    width: 100%;
  }
  .operate-apps-body-list {
    width: 100%;
  }
  .ty_dragbox {
    width: 100%;
  }
  &-body {
    /deep/.we-input {
      width: 234px;
      height: 36px;
    }

    &-list {
      display: flex;
      flex-wrap: wrap;
      //height: 330px;

      & > div {
        width: 100%;
      }

      .appsContainer {
        display: flex;
        flex-wrap: wrap;
        width: 100%;
        padding-bottom: 10px;
        /deep/.flip-list-move {
          transition: transform 0.5s;
        }

        /deep/.no-move {
          transition: transform 0s;
        }
        .app-item {
          width: 250px;
          margin: 0 16px 16px 0;
          padding: 0 12px;
          display: flex;
          align-items: center;
          height: 72px;
          border: 1px solid #f0f0f0;
          border-radius: 4px;

          .icon-del {
            color: #ff230c;
            font-size: 18px;
            cursor: pointer;
          }
          .icon-drag {
            width: 18px;
            cursor: move;
            margin-left: 4px;
          }

          &:hover {
            background-color: #f5f5f5;
          }

          &:nth-child(3n) {
            margin-right: 0;
          }

          &-edit {
            border: 1px dotted #d6dade;
            cursor: move;

            &:hover {
              background-color: transparent;
            }
          }

          .img {
            width: 44px;
            height: 44px;
            border-radius: 8px;
            margin-left: 12px;
          }

          .name {
            font-size: 14px;
            margin-left: 12px;
            flex-grow: 1;
          }

          .icon {
            margin-left: 12px;
            font-size: 24px;
            color: #ff6363;
            cursor: pointer;
          }

          &-drag {
            * {
              pointer-events: none;
            }
          }
        }
      }
    }
  }
}
</style>
