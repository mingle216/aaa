import RemoteComponent from "../../../components/RemoteComponent";
import cardLink from "../components/cardLink";
export default {
  components: { RemoteComponent, cardLink },
  data() {
    return {
      getMarkNumberArr: [
        "SYS_CARD_DONETASK",
        "SYS_CARD_MYTASK",
        "SYS_CARD_TODOTASK",
        "SYS_CARD_MESSAGECENTER",
      ],
      num: {},
      isAddevt: true,
    };
  },
  beforeDestroy() {
    window.shell.off("getMarkNumber", this.getMarkNumber);
  },
  methods: {
    getCardUrl: (card) => {
      if (process.env.NODE_ENV === "development") {
        return "/" + card.cardId + "/js/app.js";
      } else {
        const version = window.shell.getPageData().portalVersion;
        return "/" + card.cardId + "/mobile/js/app.js" + `?version=${version}`;
      }
    },
    getDataNum(card) {
      // console.log(this.num[card.cardId])
      let t_num = (this.num[card.cardId] && this.num[card.cardId]["num"]) || 0;
      let t_str = t_num ? (t_num > 99 ? "99+" : t_num) : "";
      return t_str;
    },
    getNum(cards) {
      this.refreshCardIds = cards.filter((card) =>
        this.getMarkNumberArr.includes(card.cardId)
      );
      this.refreshCardIds.forEach((card) => {
        const { cardId, cardWid } = card;
        this.getMarkNumber({ cardId, cardWid });
        this.isAddevt && window.shell.on("getMarkNumber", this.getMarkNumber);
        this.isAddevt = false;
      });
    },
    getMarkNumber({ cardId, cardWid, data }) {
      if (!this.refreshCardIds.find((card) => card.cardId === cardId)) {
        return;
      }
      if (data !== undefined) {
        this.$set(this.num, cardId, { num: data || 0 });
        return;
      }
      window.shell.execCardMethod(
        { cardId, cardWid, method: "getMarkNumber" },
        ({ errcode, data }) => {
          if (errcode === "0") {
            this.$set(this.num, cardId, { num: data || 0 });
          }
        }
      );
    },
  },
};
