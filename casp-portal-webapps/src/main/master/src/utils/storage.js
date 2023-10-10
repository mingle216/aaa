/**
 * sessionStorage
 * @调用：_sessionStorage.set('access_token', '123456', 5000);
 * @调用：_sessionStorage.get('access_token');
 */

export const _sessionStorage = {
  //存储,可设置过期时间
  set(key, value, expires) {
    let params = { key, value, expires };
    if (navigator.userAgent.match(/(iPhone|iPod|Android)/i)) {
      return
    }
    if (expires) {
      // 记录何时将值存入缓存，毫秒级
      var data = Object.assign(params, { startTime: new Date().getTime() });
      sessionStorage.setItem(key, JSON.stringify(data));
    } else {
      if (Object.prototype.toString.call(value) == "[object Object]") {
        value = JSON.stringify(value);
      }
      if (Object.prototype.toString.call(value) == "[object Array]") {
        value = JSON.stringify(value);
      }
      sessionStorage.setItem(key, value);
    }
  },
  //取出
  get(key) {
    if (navigator.userAgent.match(/(iPhone|iPod|Android)/i)) {
      return null
    }
    let item = sessionStorage.getItem(key);
    // 先将拿到的试着进行json转为对象的形式
    try {
      item = JSON.parse(item);
    } catch (error) {
      // eslint-disable-next-line no-self-assign
      item = item;
    }
    // 如果有startTime的值，说明设置了失效时间
    if (item && item.startTime) {
      let date = new Date().getTime();
      // 如果大于就是过期了，如果小于或等于就还没过期
      if (date - item.startTime > item.expires) {
        sessionStorage.removeItem(key);
        return false;
      } else {
        return item.value;
      }
    } else {
      return item;
    }
  }
};