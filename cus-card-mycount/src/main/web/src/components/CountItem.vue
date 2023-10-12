<template>
  <div class="count_item" :style="itemStyle">
    <span class="item_number portal-font-color-lv1">{{ num || 0 }}</span>
    <span class="bottom_line portal-primary-backgroundcolor-lv1"></span>
    <we-tooltip effect="dark" :content="langName" :open-delay="800">
      <span class="item_name portal-font-color-lv2">{{ langName }}</span>
    </we-tooltip>
  </div>
</template>

<script>
export default {
  props: [
    'rows',
    'num',
    'name',
    'showNum',
    'index',
    'len',
    'boxWidth',
    'dataId'
  ],
  computed: {
    langName() {
      const temp = this.name.find(el => el.langKey === this.$i18n.locale)
      const zhName = this.name.find(el => el.langKey === 'zh_CN')
      const result = temp?.langValue || zhName?.langValue
      // 如果dataId是数字并且name不是中文，则需要对"事项统计"做多语言处理
      const matterName = !isNaN(Number(this.dataId)) && temp?.langValue && (!this.$i18n.locale.includes('zh'))
        ? this.GetLanguageValue(
            'CUS_CARD_MYCOUNT',
            'serviceItemCount',
            '事项统计'
          )
        : ''
      return `${result} ${matterName}`
    },
    itemStyle() {
      // 如果一行总数%一行展示数量等于0或者一行两个则总宽度为1200
      let rowWidth =
        this.len % this.showNum === 0 || this.len === 2
          ? this.boxWidth
          : this.boxWidth - (6 - this.len) * 60
      // 总宽度减去item总宽度，去掉第一和最后一个item的padding，剩下来的padding均分
      const temp = (rowWidth - this.len * 180) / (this.len - 1) / 2
      if (this.index === 0) {
        return {
          paddingRight: this.len === 2 ? `${temp / 2}px` : `${temp}px`,
          paddingLeft: this.len === 2 ? `${temp / 2}px` : 0
        }
      } else if (this.index === this.len - 1) {
        return {
          paddingRight: this.len === 2 ? `${temp / 2}px` : 0,
          paddingLeft: this.len === 2 ? `${temp / 2}px` : `${temp}px`
        }
      } else {
        return {
          padding: `0 ${temp}px`
        }
      }
    }
  }
}
</script>

<style lang="less" scoped>
.count_item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px 0 14px;
  position: relative;
  width: 180px;
  box-sizing: content-box;
  .item_number {
    font-size: 32px;
    line-height: 40px;
    font-weight: bold;
  }
  .bottom_line {
    height: 4px;
    width: 20px;
    border-radius: 4px;
    margin: 6px 0;
  }
  .item_name {
    font-size: 14px;
    line-height: 22px;
    max-width: 160px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
</style>
