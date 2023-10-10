<template>
  <div v-loading="cardLoading">
    <div class="allServiceItem_content">
      <ul :style="content_width">
        <EmptyCon
          :tip="getLanguageValue('noServiceItem', '未找到相关服务事项')"
          :height="300"
          v-if="cardData.serviceItemList && !cardData.serviceItemList.length"
          style="flex: 1"
        />
        <li v-for="(item, index) in serviceItemListLimit" :key="index">
          <template v-if="item.datas.length">
            <div v-show="item.navName" class="nav_name portal-font-color-lv1">
              {{ item.navName }}
            </div>
            <div class="service_item">
              <flexContainer
                fatherTag="ul"
                sonTag="li"
                :width="230"
                :cardData="item.datas"
              >
                <li
                  class="
                    portal-font-color-lv1
                    portal-primary-backgroundcolor-hover-lv5
                    portal-primary-color-hover-lv1
                  "
                  v-for="el in item.datas"
                  :key="el.itemWid"
                  @click="openServiceItem(el)"
                >
                  <div v-if="showItem.includes('0')" class="service_item_left">
                    <img :src="el.iconLink || errorImg" @error="handleError" />
                  </div>
                  <div
                    v-else
                    class="
                      service_item_left_nopic
                      portal-primary-backgroundcolor-lv1
                    "
                  >
                    <p
                      class="
                        portal-font-color-lv1 portal-primary-backgroundcolor-lv1
                      "
                    />
                  </div>
                  <div class="service_item_center">
                    <we-tooltip
                      class="item"
                      effect="dark"
                      :content="el.itemName"
                      :open-delay="800"
                      placement="bottom-start"
                    >
                      <h3 class="ellipsis" style="max-width: 122px">
                        {{ el.itemName }}
                      </h3>
                    </we-tooltip>
                    <!-- 服务主题 -->
                    <div
                      v-if="showItem.includes('1')"
                      class="portal-font-color-lv3 content-icon"
                    >
                      <i class="iconfont icon-serviceSubject icon-default"></i>
                      <span class="ellipsis">
                        {{ el.itemCategory }}
                      </span>
                    </div>
                    <!-- 责任部门 -->
                    <div
                      v-if="showItem.includes('3')"
                      class="portal-font-color-lv3 content-icon"
                    >
                      <i class="iconfont icon-responsibleDepartment"></i>
                      <span class="ellipsis">
                        {{ el.itemDept }}
                      </span>
                    </div>
                    <!-- 访问次数 -->
                    <div
                      class="portal-font-color-lv3 content-icon"
                      v-if="showItem.includes('4')"
                    >
                      <i class="iconfont icon-views"></i>
                      <span>
                        {{ el.visitCount | toThousand }}
                        {{ getLanguageValue("visits", "次访问") }}
                      </span>
                    </div>
                  </div>
                  <div
                    v-if="currentUser"
                    class="collect_wrap"
                    :style="collectStyle"
                    @click.stop="collectItem(el)"
                  >
                    <favorite-icon
                      class="star"
                      :fillColor="el.favorite ? '#FA6444' : '#BFBFBF'"
                    />
                  </div>
                </li>
              </flexContainer>
            </div>
          </template>
        </li>
      </ul>
    </div>
    <div class="allServiceItem_bottom" v-if="showMore" @click="showMoreItems">
      <span>{{ getLanguageValue("unfold", "展开")}}</span>
      <i class="iconfont icon-menu-BackTop" />
    </div>
  </div>
</template>

<script>
import FavoriteIcon from "./FavoriteIcon.vue";
import getLanguageValue from "../mixins";
export default {
  mixins: [getLanguageValue],
  components: {
    FavoriteIcon,
  },
  props: [
    "cardData",
    "content_width",
    "currentUser",
    "serviceItemListLimit",
    "showMore",
    "cardLoading",
    "router"
  ],
  data() {
    return {
      errorImg: window.shell.ErrorImg,
    };
  },
  computed: {
    cardConfig() {
      return this.cardData.config || {};
    },
    allServiceItemsDisplay() {
      return this.cardConfig.allServiceItemsDisplay || {};
    },
    // 服务事项列表展示信息 0图标 1服务主题 2服务对象 3责任部门 4访问量
    showItem() {
      return this.allServiceItemsDisplay.itemDisplayInfo || [];
    },
    itemNameWidth() {
      return {
        width: !this.showItem.includes("0") ? "178px" : "122px",
      };
    },
    collectStyle() {
      return {
        paddingLeft: !this.showItem.includes("0") ? "48px" : "8px",
      };
    },
  },
  methods: {
    handleError(e) {
      let img = e.srcElement;
      img.src = this.errorImg;
      img.onerror = null; //防止闪图
    },
    openServiceItem(item) {
      if(item.workGuide && 'isAuthorized' in item && item.isAuthorized === 0) {
        window.shell.showMessage({
          type: "warning",
          message: `${this.$Lan("public", "workGuideNoAuth", "您无权限查看")}`,
        });
        return false;
      }
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName,
          extraInfo: {
            infoType: 1,
            itemId: item.itemWid
          }
        },
        startTime: new Date().getTime()
      });
      window.shell.openServiceItem(item);
    },
    collectItem(item) {
      this.$emit("collect-item", item);
    },
    // 展开更多
    showMoreItems() {
      this.$emit("list-to-limit");
    },
  },
};
</script>

<style scoped lang="less">
.allServiceItem_content {
  margin-top: 25px;
  display: flex;
  width: 100%;
  position: relative;
  min-height: 500px;
  ul {
    flex: 1;
    li {
      .nav_name {
        height: 50px;
        display: flex;
        align-items: center;
        font-size: 18px;
        font-weight: bold;
        // color: #102645;
        letter-spacing: 0;
        box-shadow: inset 0 -1px 0 0 #f0f0f0;
      }
      .ellipsis {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .service_item {
        width: 100%;
        ul {
          li {
            display: flex;
            height: auto;
            padding: 14px 12px;
            width: 230px;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 12px;
            &:hover {
              .collect_wrap {
                // > .iconfont {
                //   display: block;
                //   font-size: 14px;
                //   margin-top: 2px;
                // }
                .star {
                  display: block;
                  width: 16px;
                  height: 16px;
                  cursor: pointer;
                  margin-top: 2px;
                }
              }
            }
            .service_item_left {
              height: 44px;
              width: 44px;
              flex-shrink: 0;
              img {
                height: 100%;
                width: 100%;
              }
            }
            .service_item_left_nopic {
              border-radius: 2px;
              width: 4px;
              height: 12px;
              margin-top: 4px;
            }
            .service_item_center {
              // width: calc(100% - 68px);
              padding-left: 12px;
              h3 {
                font-size: 16px;
                line-height: 20px;
                margin-bottom: 4px;
              }
              p {
                font-size: 12px;
                // color: #707d8f;
                line-height: 17px;
                width: 100%;
              }
              i {
                font-size: 11px;
                // color: #707d8f;
              }
              span {
                display: inline-block;
                width: 100px;
                // color: #707d8f;
                line-height: 17px;
                padding-left: 3px;
                font-size: 14px;
              }
              .content-icon {
                line-height: 17px;
                display: flex;
                align-items: center;
                margin-bottom: 4px;
              }
            }
            .collect_wrap {
              width: 24px;
              flex-shrink: 0;
              // margin-left: 41px;
              .star {
                display: none;
              }
            }
          }
        }
      }
    }
  }
  .allServiceItem_point {
    width: 20px;
    display: flex;
    flex-direction: column;
    text-align: center;
    margin-left: 10px;
    position: absolute;
    top: 40px;
    right: -30px;
    ul {
      li {
        margin: 5px 0;
        .index_showed {
          font-size: 12px;
          letter-spacing: 0;
          text-align: center;
          cursor: pointer;
        }
        .index_disabled {
          color: #9fa8b5;
          cursor: not-allowed;
        }
      }
    }
  }
}
.allServiceItem_bottom {
  width: 100%;
  height: 48px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f9fafb;
  color: #3f83fb;
  cursor: pointer;
  border-radius: 4px;
  span {
    margin-right: 8px;
    // font-family: PingFangSC-Medium;
    font-size: 14px;
    letter-spacing: 0;
  }
  i {
    font-size: 10px;
    display: inline-block;
    transform: rotate(180deg);
  }
}
</style>
