export default {
  computed: {
    getLanguageValue() {
      return (dataKey, defaultValue, obj) => {
        return window.shell.getLanguageValue('CUS_CARD_ROLESERVICEITEM', dataKey, defaultValue, obj)
      }
    }
  }
};