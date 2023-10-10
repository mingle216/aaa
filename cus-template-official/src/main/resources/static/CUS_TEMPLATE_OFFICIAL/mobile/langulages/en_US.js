(function (global, factory) {
  if (typeof define === "function" && define.amd) {
    define('element/locale/en', ['module', 'exports'], factory);
  } else if (typeof exports !== "undefined") {
    factory(module, exports);
  } else {
    var mod = {
      exports: {}
    };
    factory(mod, mod.exports);
    global.WE.lang = global.WE.lang || {};
    global.WE.lang.en = mod.exports;
  }
})(this, function (module, exports) {
  'use strict';

  exports.__esModule = true;
  exports.default = {
    name: 'Name',
    tel: 'Phone',
    save: 'Save',
    confirm: 'Confirm',
    cancel: 'Cancel',
    delete: 'Delete',
    complete: 'Complete',
    loading: 'Loading...',
    telEmpty: 'Please fill in the tel',
    nameEmpty: 'Please fill in the name',
    nameInvalid: 'Malformed name',
    confirmDelete: 'Are you sure you want to delete?',
    telInvalid: 'Malformed phone number',
    weCalendar: {
      end: 'End',
      start: 'Start',
      title: 'Calendar',
      startEnd: 'Start/End',
      weekdays: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
      monthTitle: (year, month) => `${year}/${month}`,
      rangePrompt: (maxRange) => `Choose no more than ${maxRange} days`,
    },
    weCascader: {
      select: 'Select',
    },
    weContactCard: {
      addText: 'Add contact info',
    },
    weContactList: {
      addText: 'Add new contact',
    },
    wePagination: {
      prev: 'Previous',
      next: 'Next',
    },
    wePullRefresh: {
      pulling: 'Pull to refresh...',
      loosing: 'Loose to refresh...',
    },
    weSubmitBar: {
      label: 'Total：',
    },
    weCoupon: {
      unlimited: 'Unlimited',
      discount: (discount) => `${discount * 10}% off`,
      condition: (condition) => `At least ${condition}`,
    },
    weCouponCell: {
      title: 'Coupon',
      tips: 'No coupons',
      count: (count) => `You have ${count} coupons`,
    },
    weCouponList: {
      empty: 'No coupons',
      exchange: 'Exchange',
      close: 'Close',
      enable: 'Available',
      disabled: 'Unavailable',
      placeholder: 'Coupon code',
    },
    weAddressEdit: {
      area: 'Area',
      postal: 'Postal',
      areaEmpty: 'Please select a receiving area',
      addressEmpty: 'Address can not be empty',
      postalEmpty: 'Wrong postal code',
      defaultAddress: 'Set as the default address',
      telPlaceholder: 'Phone',
      namePlaceholder: 'Name',
      areaPlaceholder: 'Area',
    },
    weAddressEditDetail: {
      label: 'Address',
      placeholder: 'Address',
    },
    weAddressList: {
      add: 'Add new address',
    },
  };
  module.exports = exports['default'];
});