import "./index.css";
export default {
  render: function(createElement) {
    if (this.cardData && this.cardData.length) {
      return createElement(
        this.fatherTag,
        {
          class: {
            "flex-container-class": true,
          },
          ref: "f-container",
        },
        this.cloumnNum
          ? [
              this.$slots.default,
              Array.apply(null, { length: this.supplementNum }).map(() => {
                return createElement(this.sonTag, {
                  style: {
                    width: `${this.width}px`,
                  },
                });
              }),
            ]
          : ""
      );
    }
  },
  props: {
    width: {
      //固定宽度
      type: Number,
      default: 250,
    },
    cardData: {
      //数据源
      type: Array,
      default: [],
    },
    fatherTag: {
      //父容器标签
      type: String,
      default: "ul",
    },
    sonTag: {
      //填补子容器标签
      type: String,
      default: "li",
    },
  },
  data() {
    return {
      supplementNum: 0, //补充个数
      cloumnNum: 0, //一行个数
      fatherWidth: 0,
    };
  },
  watch: {
    cardData: {
      immediate: true,
      handler() {
        this.init();
      },
    },
  },
  mounted() {
    this.init();
    let _this = this;
    window.onresize = function() {
      _this.init();
    };
  },
  methods: {
    init() {
      if (this.cardData && this.cardData.length) {
        setTimeout(() => {
          if (this.fatherWidth) {
            this.setCloumnNum();
            this.setSupplementNum();
          } else {
            this.setFatherWidth();
            this.init();
          }
        }, 0);
        return;
      }
      this.supplementNum = 0;
      this.cloumnNum = 0;
    },
    setSupplementNum() {
      let index = 0;
      this.$slots.default.forEach((el) => {
        el.tag ? index++ : "";
      });
      const  { cloumnNum } =  this
      let num = index % cloumnNum;
      this.supplementNum = num === 0 ? 0 : cloumnNum - num;
    },
    setCloumnNum() {
      this.cloumnNum = Math.floor(this.fatherWidth / Number(this.width));
    },
    setFatherWidth() {
      this.fatherWidth = this.$refs["f-container"].offsetWidth;
    },
  },
};
