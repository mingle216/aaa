/**
 * 检测图片是否存在
 * @param url
 */
let imageIsExist = function (url) {
  return new Promise((resolve) => {
    var img = new Image();
    img.onload = function () {
      if (this.complete) {
        resolve(true);
        img = null;
      }
    }
    img.onerror = function () {
      console.log('资源error', img);
      resolve(false);
      img = null;
    }
    img.src = url;
  })
};



// 单独作用于应用模块的管理
// 传的是错误的图片
export default (Vue)=> {
  Vue.directive('err-img', async function (el, binding) {
    let imgURL = binding.value;//获取图片地址
    let realURL = el.src;
    if (imgURL) {
      let exist = await imageIsExist(realURL);
      if (!exist) {
        el.setAttribute('src', imgURL);
      }
    }
  });
}
