(function (global, factory) {
  if (typeof define === "function" && define.amd) {
    define('element/locale/zh-CN', ['module', 'exports'], factory);
  } else if (typeof exports !== "undefined") {
    factory(module, exports);
  } else {
    var mod = {
      exports: {}
    };
    factory(mod, mod.exports);
    global.WE.lang = global.WE.lang || {}; 
    global.WE.lang.zhCN = mod.exports;
  }
})(this, function (module, exports) {
  'use strict';

  exports.__esModule = true;
  exports.default = {
    name: '姓名',
    tel: '电话',
    save: '保存',
    confirm: '确认',
    cancel: '取消',
    delete: '删除',
    complete: '完成',
    loading: '加载中...',
    telEmpty: '请填写电话',
    nameEmpty: '请填写姓名',
    nameInvalid: '请输入正确的姓名',
    confirmDelete: '确定要删除吗',
    telInvalid: '请输入正确的手机号',
    weCalendar: {
      end: '结束',
      start: '开始',
      title: '日期选择',
      confirm: '确定',
      startEnd: '开始/结束',
      weekdays: ['日', '一', '二', '三', '四', '五', '六'],
      monthTitle: (year, month) => `${year}年${month}月`,
      rangePrompt: (maxRange) => `选择天数不能超过 ${maxRange} 天`,
    },
    weCascader: {
      select: '请选择',
    },
    weContactCard: {
      addText: '添加联系人',
    },
    weContactList: {
      addText: '新建联系人',
    },
    wePagination: {
      prev: '上一页',
      next: '下一页',
    },
    wePullRefresh: {
      pulling: '下拉即可刷新...',
      loosing: '释放即可刷新...',
    },
    weSubmitBar: {
      label: '合计：',
    },
    weCoupon: {
      unlimited: '无使用门槛',
      discount: (discount) => `${discount}折`,
      condition: (condition) => `满${condition}元可用`,
    },
    weCouponCell: {
      title: '优惠券',
      tips: '暂无可用',
      count: (count) => `${count}张可用`,
    },
    weCouponList: {
      empty: '暂无优惠券',
      exchange: '兑换',
      close: '不使用优惠券',
      enable: '可用',
      disabled: '不可用',
      placeholder: '请输入优惠码',
    },
    weAddressEdit: {
      area: '地区',
      postal: '邮政编码',
      areaEmpty: '请选择地区',
      addressEmpty: '请填写详细地址',
      postalEmpty: '邮政编码格式不正确',
      defaultAddress: '设为默认收货地址',
      telPlaceholder: '收货人手机号',
      namePlaceholder: '收货人姓名',
      areaPlaceholder: '选择省 / 市 / 区',
    },
    weAddressEditDetail: {
      label: '详细地址',
      placeholder: '街道门牌、楼层房间号等信息',
    },
    weAddressList: {
      add: '新增地址',
    },
  };
  module.exports = exports['default'];
});