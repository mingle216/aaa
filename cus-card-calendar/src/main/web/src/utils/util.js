export const handleColorHex = function(val) {
  if (!val) {
    return "";
  }
  let color = val.toLowerCase();
  let opacity = 0.05;
  let pattern = /^#([0-9|a-f]{3}|[0-9|a-f]{6})$/;
  if (color && pattern.test(color)) {
    if (color.length == 4) {
      // 将三位转换为六位
      color =
        "#" + color[1] + color[1] + color[2] + color[2] + color[3] + color[3];
    }
    //处理六位的颜色值
    let colorNew = "#";
    for (let i = 1; i < 7; i += 2) {
      colorNew += (
        "0" +
        Math.floor(
          opacity * parseInt("0x" + color.slice(i, i + 2)) + (1 - opacity) * 255
        ).toString(16)
      ).slice(-2);
    }
    return colorNew;
  }
  return color;
};
