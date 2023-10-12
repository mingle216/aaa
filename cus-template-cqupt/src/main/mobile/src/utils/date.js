import Vue from 'vue'
/**
 * @param fmt
 * @returns {*}
 * @constructor 日期的格式化
 */
 Vue.prototype.FormatDate = function (dataTime) {
    let fordate = null;
	let sTime = new Date( dataTime );
      let eTime = new Date();
      let sDay = sTime.getDate();
      let eDay = eTime.getDate();
      let min = parseInt((eTime - sTime) / 60000);
      let hour = parseInt((eTime - sTime) / 3600000);
      if (min >= 0) {
        if (min < 15) {
            fordate = "刚刚";
        } else if (min >= 15 && min < 60) {
            fordate = `${min}分钟前`;
        } else if (min > 60 && hour < 24 && sDay === eDay) {
            fordate = `${hour}小时前`;
        } else if (hour < 48 && eDay - sDay === 1) {
            fordate = "昨天";
        } else {
           fordate =  dataTime;
        }
        return fordate;
      }
    return fordate;
}
