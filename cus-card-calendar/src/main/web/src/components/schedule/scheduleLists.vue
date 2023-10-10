<template>
  <div>
    <auto-container :conType="containerType" :conHeight="conHeight" :scroll="scroll">
      <div style="padding: 0 16px;">
        <div class="scheduleItem" v-for="item in scheduleLists" :key="item.eventId" :style="
          `background:${handleColorHex(
            item.calWid == '1' || item.calWid == '2'
              ? themeColor
              : item.primaryColor
          )}`
        ">
          <div style="postion: relative" @click="handleGo(item)">
            <div class="scheduleItem__border" :style="
              `background:${item.calWid == '1' || item.calWid == '2'
                ? themeColor
                : item.primaryColor
              }`
            "></div>
            <span v-if="item.isDingdingCal == 1" class="iconfont icon-yuanxingdingding portal-primary-color-lv1"
              style="font-size: 14px;float:left;margin:3px 6px 0 0"></span>
            <v-clamp class="scheduleItem__title" autoresize :max-lines="2">
              {{ item.eventTitle }}
            </v-clamp>
            <!-- <div
            class="scheduleItem__info portal-font-color-lv3 ellipsis"
            style="margin-bottom: 4px;"
            v-if="item.eventDesc"
          >
            {{ item.eventDesc }}
          </div> -->
            <!-- 节假日时间 -->
            <!-- <div
            class="scheduleItem__info portal-font-color-lv3 ellipsis"
            v-if="item.holidayDateTime"
          >
            <i
              class="iconfont icon-duration"
              style="font-size: 12px;margin-right:6px"
            ></i>
            <span>{{ formatTime(item.holidayDateTime, "yyyy-MM-dd") }}</span>
          </div> -->
            <!-- 假期日程且不放假时不展示该字段 -->
            <template v-if="!(item.calWid === '3' && item.festivalType === 1)">
              <div class="scheduleItem__info portal-font-color-lv3 ellipsis">
                <i class="iconfont icon-qishishijian" style="font-size: 12px;margin-right:6px"></i>
                <span>{{ formatTime(item.startTime) }}</span><span v-if="item.endTime || item.endTimeOrigin"> -- {{
                  formatTime(
                    item.calWid == "1" || item.calWid == "2" || item.calWid == "3"
                      ? item.endTimeOrigin
                      : item.endTime
                  )
                }}</span>
              </div>
              <!-- <div
              class="scheduleItem__info portal-font-color-lv3 ellipsis"
              v-if="item.endTime || item.endTimeOrigin"
            >
              <i
                class="iconfont icon-jieshushijian"
                style="font-size: 12px;margin-right:6px"
              ></i>
              <span>{{
                formatTime(
                  item.calWid == "1" || item.calWid == "2" || item.calWid == "3"
                    ? item.endTimeOrigin
                    : item.endTime
                )
              }}</span>
            </div> -->
            </template>
            <div
            class="scheduleItem__info portal-font-color-lv3 ellipsis"
            v-if="item.location"
          >
            <i
              class="iconfont icon-location"
              style="font-size: 12px;margin-right:6px"
            ></i>
            {{ item.location }}
          </div>
            <!-- 外链图标 -->
            <div v-if="item.linkUrl" class="scheduleItem__link portal-primary-border-bottom-color-lv1">
              <i class="iconfont icon-lianjie"
                style="font-size: 12px;transform: rotate(45deg);display: inline-block;position: relative;top: -2px;"></i>
            </div>
          </div>
          <!-- 底部操作栏 -->
          <div class="scheduleItem__opt">
            <!-- 查看详情 -->
            <we-popover placement="left" width="400" trigger="click" :open-delay="800" :visible-arrow="false"
              popper-class="calendarDetailPop" @show="handleClickTrack()">
              <detail-schedule :router="router" :itemInfo="item" :themeColor="themeColor" :allChannels="allChannels"
                :canEditSchedule="canEditSchedule(item)" :canOptSchedule="canOptSchedule(item)"
                @edit-schedule="handleEdit" @share-schedule="handleShareSchedule"
                @delete-schedule="handleDelete"></detail-schedule>
              <div class="scheduleItem__opt__icon scheduleItem__opt__icon-detail" slot="reference"></div>
            </we-popover>
            <div class="scheduleItem__opt__icon" v-if="canEditSchedule(item)" @click="handleEdit(item)">
              <span class="iconfont icon-bianji" style="font-size: 14px"></span>
            </div>
            <!-- 分享 -->
            <div class="scheduleItem__opt__icon" v-if="canOptSchedule(item)" @click="handleShareSchedule(item)">
              <span class="iconfont icon-fenxiang" style="font-size: 14px"></span>
            </div>
            <!-- 删除 -->
            <div class="scheduleItem__opt__icon" v-if="canOptSchedule(item)" @click="handleDelete(item)">
              <span class="iconfont icon-shanchu" style="font-size: 14px"></span>
            </div>
          </div>
        </div>
      </div>
      </auto-container>
      <!-- 分享 -->
      <share-schedule ref="ShareScheduleModal" :router="router"></share-schedule>
      <!-- 删除 -->
      <delete-schedule ref="DeleteScheduleModal" :router="router" @update-data="handleDeleteOk"></delete-schedule>
      <!-- 重复日程的编辑和删除 -->
      <opt-repeat-schedule-modal ref="OptRepeatScheduleModal" :router="router" @update-data="handleDeleteOk"
        @edit-ok="(item, range) => $emit('edit-schedule', item, range)"></opt-repeat-schedule-modal>
  </div>
</template>

<script>
import ShareSchedule from "./shareSchedule";
import DetailSchedule from "./detailSchedule";
import DeleteSchedule from "./deleteSchedule";
import OptRepeatScheduleModal from "./optRepeatScheduleModal";
import { formatDate } from "../../utils/date-util";
import { clickTrack } from "../../mixins/track.js";
import { handleColorHex } from "../../utils/util";
export default {
  props: [
    "router",
    "scheduleLists",
    "themeColor",
    "canCreateSchedule",
    "canCreateGroupSche",
    "allChannels",
    "containerType",
    "conHeight"
  ],
  mixins: [clickTrack],
  components: {
    ShareSchedule,
    DetailSchedule,
    DeleteSchedule,
    OptRepeatScheduleModal,
  },
  data() {
    return {
      isLogin: window.shell.getUserInfo() ? true : false,
      scroll: { barKeepShow: true },
    };
  },
  methods: {
    handleColorHex,
    canEditSchedule(item) {
      return (
        this.isLogin &&
        ((item.calWid == "1" && this.canCreateSchedule == 1) ||
          (item.calWid == "2" &&
            this.canCreateGroupSche == 1 &&
            item.isInGroup == 1)) &&
        item.isOrganizer == 1
      );
    },
    canOptSchedule(item) {
      return (
        this.isLogin &&
        (item.calWid == "1" || item.calWid == "2") &&
        item.isOrganizer == 1
      );
    },
    handleShareSchedule(item) {
      this.$refs.ShareScheduleModal.show(item);
      this.handleClickTrack(); // 点击埋点
    },
    handleDelete(item) {
      if (item.repeatType !== 0) {
        this.$refs.OptRepeatScheduleModal.show(item, "delete");
      } else {
        this.$refs.DeleteScheduleModal.show(item);
      }
      this.handleClickTrack(); // 点击埋点
    },
    handleEdit(item) {
      if (item.repeatType !== 0) {
        this.$refs.OptRepeatScheduleModal.show(item, "edit");
      } else {
        this.$emit("edit-schedule", item, 0);
      }
    },
    handleDeleteOk() {
      this.$emit("update-data");
      this.$emit("update-cal");
    },
    handleGo(item) {
      if (item.linkUrl) {
        this.handleClickTrack(); // 点击埋点
        window.shell.openPage(item.linkUrl, 1, 2);
      }
    },
    formatTime(time, format = "yyyy-MM-dd HH:mm") {
      if (time) {
        return formatDate(time, format);
      }
      return "";
    },
  },
};
</script>

<style lang="less" scoped>
.scheduleItem {
  border-radius: 4px;
  padding: 10px 20px 10px 16px;
  margin-bottom: 12px;
  position: relative;
  cursor: default;

  &:hover {
    .scheduleItem__opt {
      visibility: visible;
    }
  }
}

.scheduleItem__border {
  height: 100%;
  position: absolute;
  width: 3px;
  left: 0;
  top: 0;
  border-top-left-radius: 3px;
  border-bottom-left-radius: 3px;
}

.scheduleItem__title {
  font-size: 14px;
  line-height: 22px;
  margin-bottom: 4px;
  word-break: break-all;
}

.scheduleItem__info {
  font-size: 12px;
  line-height: 20px;
  margin-bottom: 2px;
}

.scheduleItem__link {
  display: block;
  height: 0;
  border-width: 0px 12px 12px;
  border-style: none solid solid;
  border-color: transparent transparent red;
  position: absolute;
  transform: rotate(45deg);
  right: -10px;
  top: 4px;
  width: 40px;
  color: #fff;
  text-align: center;
}

.scheduleItem__opt {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: space-around;
  visibility: hidden;
  border-radius: 0px 0px 4px 4px;
  background: rgba(0, 0, 0, 0.5);
}

.scheduleItem__opt__icon {
  color: #fff;
  cursor: pointer;
  width: 14px;
  height: 14px;
  font-size: 14px;
  line-height: 14px;
}

.scheduleItem__opt__icon-detail {
  background: url("../../assets/images/detailSvg.svg") no-repeat center;
  background-size: 100% 100%;
}
</style>

<style lang="less">
.calendarDetailPop {
  padding: 20px 0;

  &:focus,
  &:focus:active {
    // 解决safari在focus时会出现outline
    outline: none;
  }
}
</style>
