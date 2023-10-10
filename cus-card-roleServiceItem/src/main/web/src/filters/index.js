import Vue from 'vue'

// 转千分位
Vue.filter('toThousand', num => {
  if (typeof(num) !== 'number') {
    num = Number(num)
  }
  if (isNaN(num)) {
    return 0
  }
  return num.toLocaleString('en')
})
