export default {
  computed: {
    getLanguageValue() {
      return (dataKey, defaultValue, obj) => {
        return window.shell.getLanguageValue('SYS_CARD_ALLSERVICE', dataKey, defaultValue, obj)
      }
    }
  }
};