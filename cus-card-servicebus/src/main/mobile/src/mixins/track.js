export const initTrack = {
  data() {
    const { cardWid, cardId, cardName } = this.router;
    const pageInfoEntity = window.shell.getPageData().pageInfoEntity;
    const loadStartTime = new Date().getTime();
    return {
      hasLoadedTrack: false, // 是否初始化完成埋点过
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
