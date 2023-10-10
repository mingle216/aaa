<template>
  <div>
    <!-- <auto-container :conType="containerType" :conHeight="conHeight" :scroll="scroll"> -->
      <div class="rightPanel">
        <div class="rightPanel__header">
          <template v-if="headerInfo">
            <we-tooltip effect="dark" :content="
              `${$Lan(
                'SYS_CARD_CALENDAR_DETAIL',
                'term',
                `${headerInfo.xn}-${headerInfo.xn + 1}学年`,
                {
                  start: headerInfo.xn,
                  end: headerInfo.xn + 1,
                }
              )} ${$Lan(
                'SYS_CARD_CALENDAR_DETAIL',
                headerInfo.xq == 1 ? 'firstSemester' : 'sencondSemester',
                headerInfo.xq == 1 ? '第一学期' : '第二学期'
              )}`
            " placement="bottom">
              <div class="text__ellipsis" style="margin-bottom: 3px">
                {{
                  $Lan(
                    "SYS_CARD_CALENDAR_DETAIL",
                    "term",
                    `${headerInfo.xn}-${headerInfo.xn + 1}学年`,
                    {
                      start: headerInfo.xn,
                      end: headerInfo.xn + 1,
                    }
                  )
                }}&nbsp;{{
                        $Lan(
                          "SYS_CARD_CALENDAR_DETAIL",
                          headerInfo.xq == 1 ? "firstSemester" : "sencondSemester",
                          headerInfo.xq == 1 ? "第一学期" : "第二学期"
                        )
                      }}
              </div>
            </we-tooltip>
            <v-clamp v-if="headerInfo.weekNumberUnder || headerInfo.weekNumberPost" class="portal-font-color-lv3"
              style="margin-bottom: 3px" autoresize :max-lines="2">
              {{ weekInfo }}
            </v-clamp>
          </template>
          <!-- 时间 -->
          <div class="rightPanel__header__time">{{ shortDate }}</div>
        </div>
        <div class="rightPanel__btn ellipsis portal-primary-color-lv1"
          v-if="isLogin && scheduleLists.length && canCreateSchedule" @click="handleAddSchedule()">
          <i class="we-icon-circle-plus-outline" style="margin-right: 4px; flex-shrink: 0"></i>{{
            $Lan("SYS_CARD_CALENDAR_DETAIL", "addSchedule", "添加日程") }}
        </div>
        <div class="rightPanel__lists" v-if="scheduleLists.length">
          <schedule-lists :scheduleLists="scheduleLists" :router="router" :themeColor="themeColor"
            :canCreateSchedule="config.canCreateSchedule" :canCreateGroupSche="config.canCreateGroupSche"
            :allChannels="allChannels" :containerType="containerType" :conHeight="conHeight"
            @edit-schedule="handleAddSchedule" @update-data="$emit('update-data')"
            @update-cal="$emit('update-cal')"></schedule-lists>
        </div>
        <auto-container :conType="containerType" :conHeight="(conHeight+34)" :scroll="scroll" v-if="!scheduleLists.length">
        <div class="rightPanel__empty" >
          <img width="110" src="../assets/images/noData.png" />
          <div class="rightPanel__btn-empty ellipsis portal-primary-color-lv1 portal-primary-border-color-lv1"
            v-if="isLogin && canCreateSchedule" @click="handleAddSchedule()">
            <i class="we-icon-circle-plus-outline" style="margin-right: 4px; flex-shrink: 0"></i>{{
              $Lan("SYS_CARD_CALENDAR_DETAIL", "addSchedule", "添加日程") }}
          </div>
          <div v-else class="portal-font-color-lv4" style="
              margin-top: 6px;
              font-size: 12px;
              line-height: 20px;
              text-align: center;
            ">
            {{ $Lan("SYS_CARD_CALENDAR_DETAIL", "noDaycircle", "暂无日程") }}
          </div>
        </div>
      </auto-container>
        <!-- 添加、编辑日程 -->
        <add-schedule ref="AddScheduleModal" :router="router" :scheduleRemindChannel="config.scheduleRemindChannel || []"
          :canCreateSchedule="config.canCreateSchedule" :canCreateGroupSche="config.canCreateGroupSche"
          @update-data="$emit('update-data')" @update-cal="$emit('update-cal')"></add-schedule>
      </div>
    <!-- </auto-container> -->
  </div>
</template>
  
<script>
import ScheduleLists from "./schedule/scheduleLists";
import AddSchedule from "./schedule/addSchedule";
import { formatDate, getWeekDay } from "../utils/date-util";
export default {
  props: [
    "router",
    "headerInfo",
    "scheduleLists",
    "selectedDate",
    "config",
    "themeColor",
    "allChannels",
    "containerType",
    "conHeight"
  ],
  components: {
    ScheduleLists,
    AddSchedule,
  },
  computed: {
    shortDate() {
      return `${formatDate(this.selectedDate, "MM-dd")} ${getWeekDay(
        this.selectedDate
      )}`;
    },
    weekInfo() {
      const arr = [];
      if (this.headerInfo && this.headerInfo.weekNumberUnder) {
        arr.push(
          this.$Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            "underWeek",
            `第${this.headerInfo.weekNumberUnder}周(本)`,
            {
              under: this.headerInfo.weekNumberUnder,
            }
          )
        );
      }
      if (this.headerInfo && this.headerInfo.weekNumberPost) {
        arr.push(
          this.$Lan(
            "SYS_CARD_CALENDAR_DETAIL",
            "postWeek",
            `第${this.headerInfo.weekNumberPost}周(研)`,
            {
              post: this.headerInfo.weekNumberPost,
            }
          )
        );
      }
      return arr.join(" | ");
    },
    canCreateSchedule() {
      return (
        this.config.canCreateSchedule == 1 ||
        this.config.canCreateGroupSche == 1
      );
    },
  },
  data() {
    return {
      isLogin: window.shell.getUserInfo() ? true : false,
      scroll: { barKeepShow: true },
    };
  },
  methods: {
    handleAddSchedule(item, range) {
      this.$refs.AddScheduleModal.show(item, this.selectedDate, range);
    },
  },
};
</script>
  
<style lang="less" scoped>
.rightPanel {
  display: flex;
  flex-direction: column;
}

.rightPanel__header {
  min-height: 50px;
  padding: 10px 16px;
  margin-bottom: 12px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: #f8f8f8;
  background-image: url("../assets/images/headerBg.png");
  background-repeat: no-repeat;
  background-position: bottom right;
  font-size: 12px;
  line-height: 20px;

  .rightPanel__week {
    margin: 0 4px;

    &:first-child {
      margin-left: 0;
    }
  }
}

.rightPanel__header__time {
  font-weight: bold;
  font-size: 14px;
  line-height: 22px;
}

.rightPanel__btn {
  padding: 0 16px 0 16px;
  line-height: 22px;
  text-align: center;
  cursor: pointer;
}

.rightPanel__btn-empty {
  border: 1px solid;
  padding: 7px 12px;
  text-align: center;
  cursor: pointer;
  line-height: 22px;
  border-radius: 4px;
  margin: 13px 16px 0;
  max-width: calc(100% - 32px);
}

.rightPanel__lists {
  margin-top: 12px;
  flex: 1;
  min-height: 0;
}

.rightPanel__empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>
  