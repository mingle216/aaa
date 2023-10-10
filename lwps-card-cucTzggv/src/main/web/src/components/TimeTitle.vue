<template>
  <div class="timeTitle" :class="['title-row', lastRow ? '' : 'borderBootom']">
    <div class="timeTitle__titleWrap">
      <a
        class="timeTitle__title ellipsis"
        :class="[itemInfo.read ? 'portal-font-color-lv3' : '']"
        target="_blank"
        @click="
          () => {
            $emit('go-link', itemInfo);
          }
        "
        >
        <div class="lwpsTzggvCardTimeCon">
          <div class="lwpsTzggvCardTimeDetail">{{ itemInfo.pubTime&&itemInfo.pubTime.substr(11,5)||'' }}</div>
          <div class="lwpsTzggvCardTimeDate">{{ itemInfo.pubTime&&itemInfo.pubTime.substr(0,10)||'' }}</div>
        </div>
        <div class="lwpsTzggvCardInfoCon">
          <span class="newstitle" :style="{'max-width':'calc(100% - '+(itemInfo.isTop?40:0)+'px)'}">{{ itemInfo.title && itemInfo.title.replace(/\n/g, " ") }}</span>
          <div class="tag topTag" v-if="itemInfo.isTop" alt="" />
          <div class="tag newTag" v-if="itemInfo.newDisplay" alt="" />
          <span class="newssub" v-if="!itemInfo.FILE_NAME">{{ itemInfo.sub && itemInfo.sub.replace(/\n/g, " ") }}</span>
          <span class="newsfile" v-if="!!itemInfo.FILE_NAME"><img src="../../public/img/file-icon.svg" class="file-icon"/>{{ itemInfo.FILE_NAME }}</span>
          <span class="newspuber">{{ itemInfo.FBBM }}</span>
        </div>
      </a>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    itemInfo: Object,
    config: Object,
    cardWidth: Number,
    lastRow: Boolean,
  },
  computed: {
  },
};
</script>

<style scoped lang="less">
.file-icon{
  width:16px;
  position: relative;
  top: 4px;
  margin-right: 4px;
}
.timeTitle {
  background: #ffffff;
  display: flex;
  &.borderBootom {
    box-shadow: inset 0 -1px 0 0 #f0f0f0;
  }
  .timeDisplay {
    letter-spacing: 0;
  }
}
.timeTitle__titleWrap {
  display: flex;
  align-items: center;
  .timeTitle__title {
    min-width: 0;
    max-width: 1000px;
    width: 100%;
    display: block;
    font-size: 16px;
    white-space: pre;
    line-height: 24px;
    height: 66px;
    cursor: pointer;
  }
  .tag {
    width: 28px;
    height: 24px;
    margin-left: 6px;
    flex-shrink: 0;
    background-size: 100% 100%;
    display: inline-block;
    float: left;
  }
  .topTag {
    background-image: url("../assets/topTag.svg");
  }
  .newTag {
    background-image: url("../assets/newTag.svg");
  }
  .lwpsTzggvCardInfoCon{
    display: inline-block;
    float: left;
    width: calc(100% - 90px);
    .newspuber,.newssub,.newsfile{
      height: 22px;
      font-size: 14px;
      line-height: 22px;
      color: #888;
      display: block;
      width: 100%;
      clear: both;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .newstitle{
      height: 24px;
      display: inline-block;
      float: left;
      max-width: calc(100% - 40px);
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  .lwpsTzggvCardTimeCon {
    display: inline-block;
    text-align: center;
    border-radius: 8px;
    overflow: hidden;
    float: left;
    margin-right: 8px;
    font-family: 'Arial';
    max-width: 80px;
    .lwpsTzggvCardTimeDetail {
      height: 32px;
      background: #ddd;
      padding-top: 7px;
      font-size: 16px;
      font-weight: bold;
    }
    .lwpsTzggvCardTimeDate {
      height: 32px;
      background: #f2f2f2;
      font-size: 12px;
      padding: 0 5px;
    }
  }
}
.title-row {
  height: 75px;
  align-items: center;
  .timeTitle__titleWrap {
    flex: 1;
    min-width: 0;
  }
  .timeDisplay {
    margin-left: 16px;
  }
}
.timeTitle__title:hover {
  .lwpsTzggvCardTimeDetail {
    color:#FFF;
    background: #bf4c4c;
  }
  .lwpsTzggvCardTimeDate {
    color:#FFF;
    background:#da9999;
  }
}
</style>
