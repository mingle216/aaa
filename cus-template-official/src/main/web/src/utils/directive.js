import Vue from "vue";

Vue.directive("tabDisableBtn", {
  componentUpdated: function(el) {
    setTimeout(() => {
      const btnCon = el.querySelectorAll(".we-tabs__nav-wrap")[0];

      const scroll_x = function() {
        let tabs = el.querySelectorAll(".we-tabs__nav")[0];
        let style_val = tabs.style.transform;
        let x = parseInt(style_val.substring(style_val.indexOf("(") + 1));

        return Math.abs(x);
      };

      const btnHandle = function() {
        const prevBtn = el.querySelectorAll(".we-tabs__nav-prev")[0];
        const nextBtn = el.querySelectorAll(".we-tabs__nav-next")[0];
        const con_w = el.querySelectorAll(".we-tabs__nav-scroll")[0]
          .offsetWidth;
        const tabs_w = el.querySelectorAll(".we-tabs__nav")[0].offsetWidth;
        let x = scroll_x();
        if (!prevBtn || !nextBtn) {
          return;
        }
        console.log(x, tabs_w - con_w);
        if (x === 0) {
          prevBtn.classList.add("active-disable");
        } else {
          prevBtn.classList.remove("active-disable");
        }
        if (Math.abs(x - (tabs_w - con_w)) < 2) {
          nextBtn.classList.add("active-disable");
        } else {
          nextBtn.classList.remove("active-disable");
        }
      };

      if (
        btnCon.classList.contains("is-scrollable") &&
        !btnCon.classList.contains("is-bind")
      ) {
        btnHandle();
        btnCon.classList.add("is-bind");
        const clickHandle = () => {
          setTimeout(() => {
            btnHandle();
          }, 200);
        };
        btnCon.addEventListener("click", clickHandle);
      }
    }, 100);
  },
});
// v-dialogDrag: 弹窗拖拽
Vue.directive("dialogDrag", {
  bind(el) {
    const dialogHeaderEl = el.querySelector(".we-dialog__header");
    const dragDom = el.querySelector(".we-dialog");
    dialogHeaderEl.style.cursor = "move";

    // 获取原有属性 ie dom元素.currentStyle 火狐谷歌 window.getComputedStyle(dom元素, null);
    const sty = dragDom.currentStyle || window.getComputedStyle(dragDom, null);

    dialogHeaderEl.onmousedown = (e) => {
      // 鼠标按下，计算当前元素距离可视区的距离
      const disX = e.clientX - dialogHeaderEl.offsetLeft;
      const disY = e.clientY - dialogHeaderEl.offsetTop;

      // 获取到的值带px 正则匹配替换
      let styL, styT;

      // 注意在ie中 第一次获取到的值为组件自带50% 移动之后赋值为px
      if (sty.left.includes("%")) {
        styL = +document.body.clientWidth * (+sty.left.replace("%", "") / 100);
        styT = +document.body.clientHeight * (+sty.top.replace("%", "") / 100);
      } else {
        styL = +sty.left.replace(/\px/g, "");
        styT = +sty.top.replace(/\px/g, "");
      }

      document.onmousemove = function(e) {
        // 通过事件委托，计算移动的距离
        const l = e.clientX - disX;
        const t = e.clientY - disY;

        // 移动当前元素
        dragDom.style.left = `${l + styL}px`;
        dragDom.style.top = `${t + styT}px`;

        // 将此时的位置传出去
        // binding.value({x:e.pageX,y:e.pageY})
      };

      document.onmouseup = function() {
        document.onmousemove = null;
        document.onmouseup = null;
      };
    };
  },
});

Vue.directive("serviceLayout", {
  bind(el, binding) {
    const { columns = 0 } = binding.value;
    el.style.width = `${100 / columns}%`;
    el.style.minWidth = "154px";
  },
});
