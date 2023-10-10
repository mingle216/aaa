<template>
  <div>
    <div class="detailSchedule__header portal-font-color-lv3">
      <we-tooltip
        effect="dark"
        :content="$Lan('SYS_CARD_CALENDAR_DETAIL', 'editTooltip', '编辑')"
        placement="bottom"
        :open-delay="400"
      >
        <div
          class="detailSchedule__optIcon portal-primary-color-hover-lv1"
          v-if="canEditSchedule"
          @click.stop="$emit('edit-schedule', itemInfo)"
        >
          <span class="iconfont icon-bianji" style="font-size: 18px"></span>
        </div>
      </we-tooltip>
      <!-- 分享 -->
      <we-tooltip
        effect="dark"
        :content="$Lan('SYS_CARD_CALENDAR_DETAIL', 'shareTooltip', '分享')"
        placement="bottom"
        :open-delay="400"
      >
        <div
          class="detailSchedule__optIcon portal-primary-color-hover-lv1"
          v-if="canOptSchedule"
          @click.stop="$emit('share-schedule', itemInfo)"
        >
          <span class="iconfont icon-fenxiang" style="font-size: 18px"></span>
        </div>
      </we-tooltip>
      <!-- 删除 -->
      <we-tooltip
        effect="dark"
        :content="$Lan('SYS_CARD_CALENDAR_DETAIL', 'deleteTooltip', '删除')"
        placement="bottom"
        :open-delay="400"
      >
        <div
          class="detailSchedule__optIcon portal-primary-color-hover-lv1"
          v-if="canOptSchedule"
          @click.stop="$emit('delete-schedule', itemInfo)"
        >
          <span class="iconfont icon-shanchu" style="font-size: 18px"></span>
        </div>
      </we-tooltip>
      <!-- 关闭 -->
      <we-tooltip
        effect="dark"
        :content="$Lan('SYS_CARD_CALENDAR_DETAIL', 'closeTooltip', '关闭')"
        placement="bottom"
        :open-delay="400"
      >
        <div
          class="detailSchedule__optIcon portal-primary-color-hover-lv1"
          @click.stop="handleClose"
        >
          <span
            class="iconfont icon-danchuangclose"
            style="font-size: 18px"
          ></span>
        </div>
      </we-tooltip>
    </div>
    <ContainerScroll :barKeepShow="true">
      <div style="min-height: 116px;max-height:316px;padding:0 20px">
        <div class="detailSchedule__title">
          <span
            v-if="itemInfo.isDingdingCal == 1"
            class="iconfont icon-yuanxingdingding portal-primary-color-lv1"
            style="font-size: 14px;margin:-1px 6px 0 0;font-weight:normal"
          ></span>
          <div
            v-else
            class="detailSchedule__dot"
            :style="
              `background:${
                itemInfo.calWid == '1' || itemInfo.calWid == '2'
                  ? themeColor
                  : itemInfo.primaryColor
              }`
            "
          ></div>
          <div>
            {{ itemInfo.eventTitle }}
            <span class="detailSchedule__tag" v-if="itemInfo.calWid == '2'">{{
              $Lan(cardId, "groupSchedule", "群组日程")
            }}</span>
            <span
              class="detailSchedule__tag holiday"
              v-if="itemInfo.calWid == '3'"
              >{{ $Lan(cardId, "holidaySchedule", "假期日程") }}</span
            >
          </div>
        </div>
        <!-- 节假日时间 -->
        <div class="detailSchedule__info" v-if="itemInfo.holidayDateTime">
          <div class="detailSchedule__icon portal-font-color-lv3">
            <i class="iconfont icon-duration"></i>
          </div>
          <span>{{ formatTime(itemInfo.holidayDateTime, "yyyy-MM-dd") }}</span>
        </div>
        <!-- 假期日程且不放假时不展示该字段 -->
        <div
          class="detailSchedule__info"
          v-if="!(itemInfo.calWid === '3' && itemInfo.festivalType === 1)"
        >
          <div class="detailSchedule__icon portal-font-color-lv3">
            <i class="iconfont icon-riqi"></i>
          </div>
          <span>{{ formatTime(itemInfo.startTime) }}</span>
          <template v-if="itemInfo.endTime || itemInfo.endTimeOrigin">
            &nbsp;~&nbsp;
            <span>{{
              formatTime(
                itemInfo.calWid == "1" ||
                  itemInfo.calWid == "2" ||
                  itemInfo.calWid == "3"
                  ? itemInfo.endTimeOrigin
                  : itemInfo.endTime
              )
            }}</span>
          </template>
        </div>
        <div class="detailSchedule__info" v-if="itemInfo.location">
          <div class="detailSchedule__icon portal-font-color-lv3">
            <i class="iconfont icon-location" style="font-size: 14px"></i>
          </div>
          {{ itemInfo.location }}
        </div>
        <!-- 发起人 -->
        <div class="detailSchedule__info" v-if="itemInfo.userName">
          <div class="detailSchedule__icon portal-font-color-lv3">
            <span class="iconfont icon-geren" style="font-size: 14px"></span>
          </div>
          <span
            :class="[
              itemInfo.creatorAccount === (loginUser && loginUser.userAccount)
                ? 'scheduleUserHighlighted'
                : '',
            ]"
            >{{ itemInfo.userName }}</span
          >
        </div>
        <!-- 参与人 -->
        <div class="detailSchedule__info" v-if="participants">
          <div class="detailSchedule__icon portal-font-color-lv3">
            <span class="iconfont icon-qunzu" style="font-size: 14px"></span>
          </div>
          <span v-html="participants"></span>
        </div>
        <div class="detailSchedule__info" v-if="itemInfo.eventDesc">
          <div class="detailSchedule__icon">
            <DescSvg class="portal-font-color-lv3"></DescSvg>
          </div>
          {{ itemInfo.eventDesc }}
        </div>
        <div class="detailSchedule__info" v-if="itemInfo.linkUrl">
          <div class="detailSchedule__icon portal-font-color-lv3">
            <i
              class="iconfont icon-lianjie"
              style="font-size: 12px;transform: rotate(90deg);"
            ></i>
          </div>
          <a
            class="portal-primary-color-lv1"
            :href="itemInfo.linkUrl"
            target="_blank"
            >{{ itemInfo.linkUrl }}</a
          >
        </div>
        <!-- 仅自建日历展示重复 -->
        <div
          class="detailSchedule__info"
          v-if="itemInfo.calWid == '1' || itemInfo.calWid == '2'"
        >
          <div class="detailSchedule__icon portal-font-color-lv3">
            <i class="we-icon-refresh" style="font-size: 14px"></i>
          </div>
          {{ eventRepeatData }}
        </div>
        <div class="detailSchedule__info" v-if="remindTimes">
          <div class="detailSchedule__icon portal-font-color-lv3">
            <i class="we-icon-bell" style="font-size: 14px"></i>
          </div>
          {{ remindTimes }}
        </div>
        <div class="detailSchedule__info" v-if="remindChannel">
          <div class="detailSchedule__icon">
            <ChannelSvg class="portal-font-color-lv3"></ChannelSvg>
          </div>
          {{ remindChannel }}
        </div>
      </div>
    </ContainerScroll>
  </div>
</template>

<script>
import DescSvg from "../icon/descSvg";
import ChannelSvg from "../icon/channelSvg";
import { clickTrack } from "../../mixins/track.js";
import { formatDate } from "../../utils/date-util";
import repeatRemindData from "../../mixins/repeatRemindData.js";
export default {
  props: [
    "router",
    "itemInfo",
    "themeColor",
    "canEditSchedule",
    "canOptSchedule",
    "allChannels",
  ],
  mixins: [clickTrack, repeatRemindData],
  components: {
    DescSvg,
    ChannelSvg,
  },
  data() {
    return {
      loginUser: window.shell.getUserInfo(),
      isDetail: true,
      cardId: "SYS_CARD_CALENDAR_DETAIL",
    };
  },
  computed: {
    eventRepeatData() {
      return this.formatRepeatType(this.itemInfo);
    },
    remindTimes() {
      const remindTimes = this.itemInfo.remindTime || [];
      if (!remindTimes.length) {
        return this.$Lan("SYS_CARD_CALENDAR_DETAIL", "remindNever", "无提醒");
      }
      return this.remindData
        .filter((item) => remindTimes.includes(item.value))
        .map((item) => item.label)
        .join(",");
    },
    remindChannel() {
      const remindTimes = this.itemInfo.remindTime || [];
      if (
        !remindTimes.length ||
        !this.itemInfo.remindChannel ||
        !this.allChannels
      ) {
        return "";
      }
      const remindChannel = this.itemInfo.remindChannel.split(",");
      let arr = [];
      remindChannel.forEach((element) => {
        const curr = this.allChannels.find((item) => item.value === element);
        curr && arr.push(curr.label);
      });
      return arr.join(",");
    },
    participants() {
      return (this.itemInfo.dubboCalGroupReceiver?.userInfos || [])
        .map((item) => {
          const isSelf =
            this.loginUser && this.loginUser.userAccount === item.userAccount;
          return isSelf
            ? `<span class="scheduleUserHighlighted">${item.userName}</span>`
            : item.userName;
        })
        .join("、");
    },
  },
  methods: {
    handleClose() {
      this.$parent.doClose();
      this.handleClickTrack(); // 点击埋点
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
.detailSchedule__header {
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin-bottom: 20px;
}
.detailSchedule__optIcon {
  cursor: pointer;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  margin-left: 15px;
  &:hover {
    background: #f5f5f5;
  }
}
.detailSchedule__title {
  font-weight: bold;
  font-size: 16px;
  line-height: 24px;
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}
.detailSchedule__dot {
  width: 8px;
  height: 8px;
  border-radius: 4px;
  margin: 8px 10px 0 2px;
  flex-shrink: 0;
}
.detailSchedule__info {
  display: flex;
  align-items: flex-start;
  line-height: 22px;
  &:not(:last-child) {
    margin-bottom: 12px;
  }
}
.detailSchedule__icon {
  width: 14px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 14px;
  margin-right: 6px;
  .iconfont {
    font-size: 14px;
  }
}
.detailSchedule__tag {
  margin-left: 8px;
  padding: 1px 4px;
  font-size: 12px;
  line-height: 16px;
  color: #ff9000;
  background: rgba(255, 144, 0, 0.1);
  border: 0.5px solid #ff9000;
  border-radius: 2px;
  font-weight: normal;
  display: inline-block;
  &.holiday {
    color: #25b14d;
    background: #e9f7ed;
    border-color: #25b14d;
  }
}
/deep/.scheduleUserHighlighted {
  color: #ff9000;
}
</style>
