<template>
  <div>
    <!-- v-if="bbb.length > 0" -->
    <div class="nav_name_wrap" >
      <div class="nav_name portal-primary-after-backgroundcolor-lv1">
        {{ itemInfo.navName }} ({{ (itemInfo.datas || []).length }})
      </div>
    </div>
    <div class="wrap">
      <template v-if="itemDisplayInfo.length">
        <li
          v-for="item in itemInfo.datas"
          :key="item.itemWid"
          class="service_item"
          :class="[itemDisplayInfo.length ? 'hasSub' : '']"
          @click="openServiceDetail(item)"
        >
          <div class="imgWrap" v-if="itemDisplayInfo.includes('0')">
            <img
              :src="getIconUrl(item) || errorImg"
              @error="handleError"
              width="36"
              height="36"
            />
          </div>
          <div class="service_info">
            <div class="service_name portal-font-color-lv1">
              <span v-html="highLight(item)" class="ellipsis"></span>
            </div>
            <!-- 服务事项列表展示信息 0图标 1服务主题 2服务对象 3责任部门 4访问量 -->
            <div class="service_subInfo" v-if="itemDisplayInfo.includes('1')">
              <div class="service_subInfo_label portal-font-color-lv2 ellipsis">
                {{ getLanguageValue('serviceItemCategory', '服务主题') }}:
              </div>
              <div class="service_subInfo_value ellipsis">
                {{ item.itemCategory || '-' }}
              </div>
            </div>
            <div class="service_subInfo" v-if="itemDisplayInfo.includes('2')">
              <div class="service_subInfo_label portal-font-color-lv2 ellipsis">
                {{ getLanguageValue('roleName', '服务对象') }}:
              </div>
              <div class="service_subInfo_value ellipsis">
                {{ item.roleName || '-' }}
              </div>
            </div>
            <div class="service_subInfo" v-if="itemDisplayInfo.includes('3')">
              <div class="service_subInfo_label portal-font-color-lv2 ellipsis">
                {{ getLanguageValue('serviceItemDept', '责任部门') }}:
              </div>
              <div class="service_subInfo_value ellipsis">
                {{ item.itemDept || '-' }}
              </div>
            </div>
            <div class="service_subInfo" v-if="itemDisplayInfo.includes('4')">
              <div class="service_subInfo_label portal-font-color-lv2 ellipsis">
                {{ getLanguageValue('visitCount', '访问量') }}:
              </div>
              <div class="service_subInfo_value ellipsis">
                {{ item.visitCount | toThousand }}
              </div>
            </div>
          </div>
          <div class="optBtn">
            <!-- <we-icon
              class="favorite_btn"
              :class="[
                item.favorite ? 'favorite_font_color' : 'unfavorite_font'
              ]"
              :name="item.favorite ? 'like' : 'like-o'"
              @click.stop="collectApp(item)"
              v-if="currentUser"
            /> -->
            <!-- onlineServiceType 0 不显示， 1 禁用， 2 显示 -->
            <we-button
              plain
              class="onlineBtn"
              :class="[
                item.onlineServiceType == 1
                  ? 'button-disabled portal-primary-color-lv1 portal-primary-border-color-lv1'
                  : 'portal-primary-color-lv1 portal-primary-border-color-lv1'
              ]"
              v-if="item.onlineServiceType != 0"
              @click.stop="handleOnline(item)"
              ><div class="ellipsis">
                {{ getLanguageValue('online', '在线办理') }}
              </div></we-button
            >
          </div>
        </li>
      </template>
      <template v-else>
        <li
          v-for="item in itemInfo.datas"
          :key="item.itemWid"
          class="service_item_new"
          :class="[!itemDisplayInfo.length ? 'hasSub' : '']"
          @click="openServiceDetail(item)"
        >
          <div class="service_name portal-font-color-lv1">
            <span v-html="highLight(item)" class="ellipsis"></span>
          </div>
          <div class="optBtn">
            <!-- onlineServiceType 0 不显示， 1 禁用， 2 显示 -->
            <we-button
              plain
              class="onlineBtn"
              :class="[
                item.onlineServiceType == 1
                  ? 'button-disabled portal-primary-color-lv1 portal-primary-border-color-lv1'
                  : 'portal-primary-color-lv1 portal-primary-border-color-lv1'
              ]"
              v-if="item.onlineServiceType != 0"
              @click.stop="handleOnline(item)"
            >
              <div class="ellipsis">
                {{ getLanguageValue('online', '在线办理') }}
              </div>
            </we-button>
            <!-- <we-icon
              class="favorite_btn"
              :class="[
                item.favorite ? 'favorite_font_color' : 'unfavorite_font'
              ]"
              :name="item.favorite ? 'like' : 'like-o'"
              @click.stop="collectApp(item)"
              v-if="currentUser"
            /> -->
          </div>
        </li>
      </template>
    </div>
  </div>
</template>

<script>
export default {
  props: ['itemInfo', 'searchKey', 'itemDisplayInfo'],
  data() {
    return {
      errorImg: window.shell.ErrorImg,
      currentUser: window.shell.getUserInfo(),
      bbb:[]
    }
  },
  computed: {
    highLight() {
      return function(item) {
        return window.shell.strHighlighted(item.itemName, this.searchKey)
      }
    }
  },
  filters: {
    toThousand: num => {
      if (typeof num !== 'number') {
        num = Number(num)
      }
      if (isNaN(num)) {
        return 0
      }
      return num.toLocaleString('en')
    }
  },
  methods: {
    handleError(e) {
      let img = e.srcElement
      img.src = this.errorImg
      img.onerror = null
    },
    getIconUrl(item) {
      const { serviceStation, mobileIconLink, iconLink } = item
      if (serviceStation === 1) {
        return /^((ht|f)tps?):/.test(mobileIconLink)
          ? mobileIconLink
          : `data:image/png/jpeg;base64,${mobileIconLink}`
      } else if (serviceStation === 0) {
        return /^((ht|f)tps?):/.test(iconLink)
          ? iconLink
          : `data:image/png/jpeg;base64,${iconLink}`
      } else {
        return mobileIconLink
          ? /^((ht|f)tps?):/.test(mobileIconLink)
            ? mobileIconLink
            : `data:image/png/jpeg;base64,${mobileIconLink}`
          : /^((ht|f)tps?):/.test(iconLink)
          ? iconLink
          : `data:image/png/jpeg;base64,${iconLink}`
      }
    },
    openServiceDetail(item) {
     this.$emit('openServiceDetail', item)
    },
    handleOnline(item) {
      this.$emit('handleOnline', item)
    },
    collectApp(item) {
      this.$emit('collectApp', item)
    }
  },
  mounted(){
   
  }
}
</script>

<style lang="less" scoped>
.favorite_font_color {
  color: #fa6444;
}
.nav_name_wrap {
  font-size: 18px;
  font-weight: bold;
  line-height: 48px;
  box-shadow: inset 0px -1px 0px 0px #e7edf1;
  padding: 0 17px;
  .nav_name {
    display: inline-block;
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      height: 0.05333rem;
      left: 0;
      right: 0;
    }
  }
}

.wrap {
  display: flex;
  flex-wrap: wrap;
  padding: 0 17px;
  .service_item {
    min-height: 60px;
    background: #fff;
    box-shadow: inset 0px -1px 0px 0px #e7edf1;
    border-radius: 4px;
    width: 100%;
    padding: 16px 0;
    margin: 0;
    font-size: 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    &.hasSub {
      align-items: flex-start;
      .service_info .service_name {
        margin-bottom: 12px;
      }
      .optBtn {
        flex-direction: column;
        align-items: flex-end;
        height: 100%;
        justify-content: center;
      }
    }
    .imgWrap {
      margin-right: 12px;
    }
    .service_info {
      flex: 1;
      min-width: 0;
      .service_name {
        display: flex;
        font-size: 16px;
        font-weight: bold;
        line-height: 22px;
      }
      .service_subInfo {
        display: flex;
        align-items: center;
        line-height: 18px;
        margin-top: 4px;
        font-size: 14px;
      }
      .service_subInfo_label {
        max-width: 80px;
        margin-right: 8px;
      }
      .service_subInfo_value {
        flex: 1;
        min-width: 0;
      }
      img {
        width: 36px;
        height: 36px;
      }
    }
    .button-disabled,
    .button-disabled:hover {
      opacity: 0.4;
      // border-color: #c1cbd4;
      // color: #c1cbd4;
    }
    .optBtn {
      margin-left: 20px;
      display: flex;
    }
  }
  .service_item_new {
    width: 100%;
    height: 58px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 11px 0;
    box-shadow: inset 0px -1px 0px 0px #e7edf1;
    .service_name {
      flex: 1;
      min-width: 0;
      display: flex;
      font-size: 16px;
      line-height: 22px;
    }
    .button-disabled,
    .button-disabled:hover {
      opacity: 0.4;
      // border-color: #c1cbd4;
      // color: #c1cbd4;
    }
    .optBtn {
      margin-left: 25px;
      display: flex;
      align-items: center;
      .favorite_btn {
        margin-left: 20px;
      }
    }
  }
  /deep/.we-button {
    width: 78px;
    height: 36px;
    padding: 0 10px;
    border-radius: 4px;
    .we-button__text {
      display: block;
      width: 100%;
    }
  }
}
</style>
