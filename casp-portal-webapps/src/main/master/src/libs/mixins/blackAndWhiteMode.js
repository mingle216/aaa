import globalObj from "../../utils/global";
export default {
  data() {
    return {
      hasInited: false
    }
  },
  methods: {
    setGrayMode(blackAndWhiteMode) {
      if (!this.hasInited) {
        if (blackAndWhiteMode === '1') {
          globalObj.addCSS(document, 'html {-webkit-filter: grayscale(100%);-moz-filter: grayscale(100%);-ms-filter: grayscale(100%);-o-filter: grayscale(100%);filter: progid:DXImageTransform.Microsoft.BasicImage(grayscale=1);filter: grayscale(100%);}')
        }
        this.hasInited = true;
      }
    }
  }
}