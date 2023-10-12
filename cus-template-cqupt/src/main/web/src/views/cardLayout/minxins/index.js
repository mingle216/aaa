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
      if (process.env.NODE_ENV === 'development') {
        return "/" + card.cardId + "/js/app.js";
      } else {
        return "/" + card.cardId + "/pc/js/app.js";
      }
      
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
    getMarkNumber({ cardId, cardWid }) {
      if (!this.refreshCardIds.find((card) => card.cardId === cardId)) {
        return;
      }
      window.shell.execCardMethod(
        { cardId, cardWid, method: "getMarkNumber" },
        ({ errcode, data }) => {
          if (errcode === "0") {
            const num = data ? (data > 99 ? "99+" : data) : "";
            this.$set(this.num, cardId, { num });
          }
        }
      );
    },
  },
};
