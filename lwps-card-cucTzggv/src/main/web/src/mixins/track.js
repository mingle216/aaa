export const initTrack = {
  data() {
    const { cardWid, cardId, cardName } = this.router;
    const pageInfoEntity = window.shell.getPageData().pageInfoEntity;
    const loadStartTime = new Date().getTime();
    return {
      trackParam: {
        actionType: 3,
        functionType: 1,
        startTime: loadStartTime,
        endTime: "",
        listId: `${cardId}_${cardWid}_${loadStartTime}`,
        actionParams: {
          pageCode: pageInfoEntity.pageCode,
          pageName: pageInfoEntity.pageName,
          cardWid: cardWid,
          cardId: cardId,
          cardName: cardName,
        },
      },
    };
  },
  methods: {
    //点击埋点
    handleClickTrack(extraInfo) {
      const actionParams = {
        ...this.trackParam.actionParams,
      };
      if (extraInfo) {
        actionParams.extraInfo = extraInfo;
      }
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        startTime: new Date().getTime(),
        actionParams,
      });
    },
    // 加载完成埋点
    loadedEndTrack() {
      if (this.hasLoadedTrack) {
        return;
      }
      this.hasLoadedTrack = true;
      window.minosStataCollect.loadEnd({
        listId: this.trackParam.listId,
        endTime: new Date().getTime(),
      }); // 页面加载结束
    },
    // 搜索筛选埋点
    handlefilterTrack(isSearch) {
      const extraInfo = {
        infoType: isSearch ? 3 : 2,
      };
      if (isSearch) {
        extraInfo.filterInfo = {
          keyword: this.searchKey.trim(),
        };
      } else {
        const classifyList = Object.values({...this.searchServiceParam})
          .filter((e) => e.length > 0)
          .join(",");
        extraInfo.filterInfo = {
          classifyList,
        };
      }
      this.handleClickTrack(extraInfo);
    },
  },
  created() {
    window.minosStataCollect.loadStart(this.trackParam); // 页面加载开始
  },
};

export const clickTrack = {
  methods: {
    //点击埋点
    handleClickTrack(extraInfo) {
      const { cardWid, cardId, cardName } = this.router;
      const pageInfoEntity = window.shell.getPageData().pageInfoEntity;
      const actionParams = {
        pageCode: pageInfoEntity.pageCode,
        pageName: pageInfoEntity.pageName,
        cardWid: cardWid,
        cardId: cardId,
        cardName: cardName,
      };
      if (extraInfo) {
        actionParams.extraInfo = extraInfo;
      }
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        startTime: new Date().getTime(),
        actionParams,
      });
    },
  },
};
