/**
 * @description 截断文字显示title
 * @param {string} name 截断的名称
 * @param {number} len 展示截断的字数
 */
export const showTitle = (name, a = 4) => {
  let len = 0;
  for (let i = 0; i < name.length; i++) {
    const m = name.charAt(i);
    if (m.match(/[\u4e00-\u9fa5]/gi) != null) {
      len += 2;
    } else {
      len += 1;
    }
  }
  return len > a
};