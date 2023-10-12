<template>
  <div class="personApp" :class="[lists.length === 6 ? 'personApp-6' : '']">
    <div
      class="personApp__item"
      v-for="(item, index) in lists"
      :key="item.serviceWid"
      :class="[
        lists.length % 2 && index === lists.length - 1
          ? 'personApp__item-full'
          : '',
        `personApp__item${lists.length > 3 ? 4 : lists.length}`,
        `personApp__item-${index}`,
        item.linkUrl ? 'cursor' : '',
      ]"
      :style="
        `borderColor:${colors[index].border};background:${colors[index].background}`
      "
      @click="handleClick(item)"
    >
      <we-tooltip
        class="item"
        effect="dark"
        :content="item.title"
        :open-delay="800"
        placement="top"
      >
        <!-- :class="[index % 2 ? 'personApp__itemNews' : '']" -->
        <div class="personApp__itemInfo">
          <div
            class="personApp__itemIcon"
            :class="[!item.iconUrl ? 'hidden' : '']"
          >
            <img
              :src="item.iconUrl"
              v-if="item.iconUrl"
              style="width: 100%;height: 100%;"
            />
          </div>
          <div
            class="personApp__itemName ellipsis portal-primary-color-hover-lv1"
            :class="[
              (lists.length === 1 || lists.length === 5) &&
              index === lists.length - 1
                ? 'personApp__itemName-show'
                : '',
            ]"
          >
            {{ item.title }}
          </div>
        </div>
      </we-tooltip>
    </div>
  </div>
</template>

<script>
export default {
  props: ["lists"],
  data() {
    return {
      colors: [
        {
          font: "#40A9FF",
          border: "#B3DDFF",
          background: "#F7FCFF",
        },
        {
          font: "#9254DE",
          border: "#D3BBF2",
          background: "#FBF8FE",
        },
        {
          font: "#68D093",
          border: "#C3ECD4",
          background: "#F9FDFB",
        },
        {
          font: "#FF7A45",
          border: "#FFCAB5",
          background: "#FFFAF8",
        },
        {
          font: "#36CFC9",
          border: "#AFECE9",
          background: "#F7FDFD",
        },
        {
          font: "#F759AB",
          border: "#FCBDDD",
          background: "#FFF8FC",
        },
      ],
    };
  },
  methods: {
    handleClick(item) {
      const pageData = window.shell.getPageData().pageInfoEntity;
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 0,
        actionParams: {
          pageCode: pageData.pageCode,
          pageName: pageData.pageName,
          extraInfo: {
            infoType: 16,
            wid: item.wid,
            name: item.title,
          },
        },
        startTime: new Date().getTime(),
      });
      if (item.linkUrl) {
        window.shell.openPage(item.linkUrl, 1, 2);
      }
    },
  },
};
</script>

<style scoped>
.personApp {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}
.personApp__item {
  width: 128px;
  flex-shrink: 0;
  border-radius: 6px;
  border: 1px solid;
  padding: 10px 20px;
  margin-bottom: 16px;
}
.personApp__item.cursor {
  cursor: pointer;
}
.personApp__itemInfo {
  display: flex;
  align-items: center;
  justify-content: center;
}
.personApp__itemIcon {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
  position: relative;
}
.personApp__itemName {
  margin-left: 5px;
  line-height: 22px;
  position: relative;
}
.personApp__itemNews .personApp__itemName:after {
  content: "";
  position: absolute;
  width: 6px;
  height: 6px;
  border-radius: 3px;
  background: #ff230c;
  top: 2px;
  right: 0;
}
@media screen and (min-width: 1282px) {
  .personApp__item-full {
    flex: 1;
  }
  .hidden {
    display: none;
  }
}
@media screen and (max-width: 1281px) {
  .personApp-6 {
    justify-content: flex-start;
  }
  .personApp-6 .personApp__item:not(.personApp__item-3) {
    margin-right: 10px;
  }
  .personApp__item {
    margin-bottom: 12px;
    padding: 6px 0;
  }
  .personApp__item4 {
    width: 37px;
    flex-shrink: 0;
  }
  .personApp__item4.personApp__item-full {
    flex: 1;
    padding: 6px 20px;
  }
  .personApp__item3 {
    width: 52px;
    flex-shrink: 0;
  }
  .personApp__item2 {
    width: 84px;
  }
  .personApp__item1 {
    flex: 1;
    padding: 6px 20px;
  }
  .personApp__itemName {
    display: none;
  }
  .personApp__itemName-show {
    display: initial;
  }
  .personApp__itemNews .personApp__itemIcon:after {
    content: "";
    position: absolute;
    width: 6px;
    height: 6px;
    border-radius: 3px;
    background: #ff230c;
    top: 2px;
    right: 0;
  }
}
</style>
