import { queryPopupWindowDisplay, updatePopupWindowStatus } from "@api/service";
const lanDefalut = { notice: "公告", noTip: "下次不再提醒" };
export default class Popup {
  constructor(pageData, currentUser) {
    this.pageData = pageData;
    this.currentUser = currentUser;
    this.resData = null;
    this.$lan = lanDefalut;
  }
  init(lanObj) {
    this.$lan = lanObj ? lanObj : lanDefalut;
    if (this.currentUser && this.currentUser !== null) {
      this.queryPopupWindowDisplay();
    }
  }
  createDom() {
    //生成dom树
    let div = document.createElement("div");
    div.id = "ty_box";
    div.innerHTML =
      `<div class="strong_box">` +
      // 标题
      `<div class="strong_top">` +
      //背景色
      `<div class="tixing portal-primary-border-top-color-lv3"></div>` +
      //圆圈色
      `<div class="change_1 portal-primary-backgroundcolor-lv3"></div>` +
      //其余颜色
      `<div class="change_circle portal-primary-backgroundcolor-lv3"></div>` +
      `<div class="co co1 portal-primary-backgroundcolor-lv3"></div>` +
      `<div class="co co2 portal-primary-backgroundcolor-lv3"></div>` +
      `<div class="co co3 portal-primary-backgroundcolor-lv3"></div>` +
      `<div class="co co4 portal-primary-backgroundcolor-lv3"></div>` +
      //颜色不变部分
      `<div class="white_circle"></div>` +
      `<img class="laba" src="${require("../assets/images/laba.png")}" />` +
      `<img class="yc1" src="${require("../assets/images/yc1.png")}" />` +
      `<div class="s_close_box">` +
      `<img class="strong_close s_to_close" src="${require("../assets/images/p_close.png")}" />` +
      `</div>` +
      //标题内容
      (this.resData.popTitle &&
      this.resData.popTitle.length > 0 &&
      this.resData.popTitle[0].langValue &&
      this.resData.popTitle[0].langValue !== null
        ? `<div class="s_title portal-primary-color-lv1" >${this.resData.popTitle[0].langValue}</div>`
        : `<div class="s_title portal-primary-color-lv1"  >${this.$lan.notice}</div>`) +
      `</div>` +
      // 主体内容
      `<div class="strong_main ${
        this.resData.popType && this.resData.popType === 1 ? "" : ""
      }" style="height: ${
        this.resData.popType && this.resData.popType === 1 ? "107px" : "159px"
      }">` +
      `<div class="strong_wrap">` +
      `<div class="strong_con">${
        this.resData.popContents &&
        this.resData.popContents.length > 0 &&
        this.resData.popContents[0].langValue &&
        this.resData.popContents[0].langValue !== null
          ? this.resData.popContents[0].langValue
          : ""
      }</div>` +
      `<div class="strong_bar_box">` +
      `<div class="strong_bar"></div>` +
      `</div>` +
      `</div>` +
      `</div>` +
      // 底部内容
      `<div class="strong_bottom ${
        this.resData.popType && this.resData.popType === 1
          ? "boxShadowActive"
          : ""
      }" style="display: ${
        this.resData.popType && this.resData.popType === 1 ? "flex" : "none"
      }">` +
      `<div class="no_tips">` +
      `<div class="s_no_choose"></div>` +
      `<div class="s_choose strong_hide portal-primary-backgroundcolor-lv1">` +
      `<img src="${require("../assets/images/gou.png")}" />` +
      `</div>` +
      `<div class="s_no_again">${this.$lan.noTip}</div>` +
      `</div>` +
      `</div>` +
      `</div>`;
    return div;
  }
  setPosition() {
    //设置弹窗定位
    let winWidth, winHeight;
    if (window.innerWidth) {
      winWidth = window.innerWidth;
      winHeight = window.innerHeight;
    } else if (document.body && document.body.clientWidth) {
      winWidth = document.body.clientWidth;
      winHeight = document.body.clientHeight;
    }
    if (
      document.documentElement &&
      document.documentElement.clientHeight &&
      document.documentElement.clientWidth
    ) {
      winWidth = document.documentElement.clientWidth;
      winHeight = document.documentElement.clientHeight;
    }
    document.getElementById("ty_box").style.left = `${(winWidth - 450) / 2}px`;
    document.getElementById("ty_box").style.top = `${(winHeight - 297) / 2}px`;
  }
  initEvent() {
    //初始化弹窗所有事件
    let _this = this,
      no_choose = document.getElementsByClassName("s_no_choose")[0],
      choose = document.getElementsByClassName("s_choose")[0],
      close = document.getElementsByClassName("s_close_box")[0];
    no_choose.addEventListener("click", function() {
      this.classList.add("strong_hide");
      choose.classList.remove("strong_hide");
    });
    choose.addEventListener("click", function() {
      this.classList.add("strong_hide");
      no_choose.classList.remove("strong_hide");
    });
    close.addEventListener("click", function() {
      if (_this.resData.popType && _this.resData.popType === 1) {
        //允许忽略
        if (no_choose.getAttribute("class").indexOf("strong_hide") >= 0) {
          _this.updatePopupWindowStatus();
        } else {
          _this.removeNode("ty_box");
        }
      } else {
        _this.removeNode("ty_box");
      }
    });
  }
  initDrag() {
    //初始化拖拽效果
    let box = document.getElementById("ty_box"),
      nav = document.getElementsByClassName("strong_top")[0],
      distX = 0,
      distY = 0,
      isMove = false;
    const iframe = document.getElementById('template-container')
    nav.addEventListener("mousedown", function(e) {
      distX = e.clientX - box.offsetLeft;
      distY = e.clientY - box.offsetTop;
      isMove = true;
      if (iframe) {
        iframe.style.pointerEvents = 'none'; //解决拖动时因为iframe卡顿的问题
      }
    });
    document.addEventListener("mousemove", function(e) {
      if (!isMove) return;
      let oL = e.clientX - distX,
        oT = e.clientY - distY,
        maxL = document.documentElement.clientWidth - box.offsetWidth,
        maxT = document.documentElement.clientHeight - box.offsetHeight;
      if (oL < 0) {
        oL = 0;
      }
      if (oL > maxL) {
        oL = maxL;
      }
      if (oT < 0) {
        oT = 0;
      }
      if (oT > maxT) {
        oT = maxT;
      }
      box.style.left = oL + "px";
      box.style.top = oT + "px";
    });
    document.addEventListener("mouseup", function() {
      isMove = false;
      if (iframe) {
        iframe.style.pointerEvents = 'auto';
      }
    });
  }
  initScroll() {
    //初始化自定义滚动条
    let wrap = document.getElementsByClassName("strong_wrap")[0],
      content = document.getElementsByClassName("strong_con")[0],
      barBox = document.getElementsByClassName("strong_bar_box")[0],
      bar = document.getElementsByClassName("strong_bar")[0],
      step = 10,
      disY = 0,
      isMove = false;
    //比例
    let scale = wrap.clientHeight / content.clientHeight;
    //滑块高度
    let barH = barBox.clientHeight * scale;
    //设置滑块最小高度
    if (barH < 20) {
      barH = 20;
    }
    //不需要滑块
    if (scale >= 1) {
      barBox.style.display = "none";
    } else {
      bar.style.height = `${barH}px`;
      //鼠标滚轮事件
      wrap.onmousewheel = function(e) {
        let ev = e || window.event;
        let topValue = bar.offsetTop;
        if (ev.wheelDelta < 0) {
          topValue += step;
        }
        if (ev.wheelDelta > 0) {
          topValue -= step;
        }
        if (topValue < 0) {
          topValue = 0;
        }
        if (topValue >= barBox.clientHeight - bar.clientHeight) {
          topValue = barBox.clientHeight - bar.clientHeight;
        }
        bar.style.top = `${topValue}px`;
        content.style.top = `-${topValue / scale}px`;
      };
      //拖拽滑块事件
      bar.onmousedown = function(e) {
        let ev = e || window.event;
        disY = ev.clientY - bar.offsetTop;
        isMove = true;
        document.onmousemove = function(eve) {
          if (!isMove) return;
          let even = eve || window.event;
          let oT = even.clientY - disY;
          if (oT < 0) {
            oT = 0;
          }
          if (oT >= barBox.clientHeight - bar.clientHeight) {
            oT = barBox.clientHeight - bar.clientHeight;
          }
          bar.style.top = `${oT}px`;
          content.style.top = `-${oT / scale}px`;
        };
        document.onmouseup = function() {
          isMove = false;
        };
      };
    }
  }
  async queryPopupWindowDisplay() {
    //门户前端查询弹窗
    const [res] = await queryPopupWindowDisplay({
      wid: this.currentUser.wid,
      pageWid: this.pageData.pageContext.showProgrammeEntity.wid,
    });
    if (res.errcode === "0") {
      if (res.data && res.data !== null) {
        if (res.data.isEnabled && res.data.isEnabled === 1) {
          //允许启用
          this.resData = res.data;
          document.body.appendChild(this.createDom());
          this.setPosition();
          this.initEvent();
          this.initDrag();
          this.initScroll();
        }
      }
    }
  }
  async updatePopupWindowStatus() {
    //设置弹窗不再显示
    const [res] = await updatePopupWindowStatus({
      wid: this.currentUser.wid,
    });
    if (res.errcode === "0") {
      this.removeNode("ty_box");
    }
  }
  removeNode(id) {
    //删除节点
    if (this.isIE() || this.isIE11()) {
      document.getElementById(id).removeNode(true);
    } else {
      document.getElementById(id).remove();
    }
  }
  isIE() {
    //判断是否为IE浏览器
    if (!!window.ActiveXobject || "ActiveXObject" in window) {
      return true;
    } else {
      return false;
    }
  }
  isIE11() {
    //是否为IE11
    if (/Trident\/7\./.test(navigator.userAgent)) {
      return true;
    } else {
      return false;
    }
  }
}
