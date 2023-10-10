import RemoteComponent from "../../../components/RemoteComponent";
import cardLink from "../components/cardLink";
let isAddevt = true;
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
    };
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
      cards.forEach((card) => {
        const { cardId, cardWid } = card;
        if (this.getMarkNumberArr.includes(cardId)) {
          this.getMarkNumber({ cardId, cardWid });
          isAddevt &&
            window.shell.on("getMarkNumber", ({ cardId, cardWid }) => {
              this.getMarkNumber({ cardId, cardWid });
            });
          isAddevt = false;
        }
      });
    },
    getMarkNumber({ cardId, cardWid }) {
      window.shell.execCardMethod(
        { cardId, cardWid, method: "getMarkNumber" },
        ({ errcode, data }) => {
          if (errcode === "0") {
            console.log(cardId);
            this.$set(this.num, cardId, { num: 0 });
            this.num[cardId]["num"] = data ? (data > 99 ? "99+" : data) : "";
          }
        }
      );
    },
  },
};
