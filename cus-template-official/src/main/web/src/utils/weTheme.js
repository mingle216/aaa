/**
 * 第三方组件主题样式覆盖
 * key为选择器名称
 * value为对应的样式，颜色值为@符号加颜色变量标识
 */
export default {
  ".we-date-table td.today span":"{color: @portal-primary-color-lv1;}",
  ".we-date-table td.end-date span, .we-date-table td.start-date span":"{background-color: @portal-primary-color-lv1;}",
  ".we-date-table td.available:hover":"{color: @portal-primary-color-lv1;}",
  ".cancel-btn:focus, .cancel-btn:hover":
    "{color: @portal-primary-color-lv1 !important; border-color: @portal-primary-color-lv1 !important;  background-color: #fff !important;}",
  ".submit-btn":
    "{ border-color: @portal-primary-color-lv1 !important;  background-color: @portal-primary-color-lv1 !important; }",
  ".detail-wrapper a": "{color: @portal-primary-color-lv1;}",
  ".ser-item:hover .title,.ser-item:hover .arrow,.ser-item:hover .item-icon":
    "{color: @portal-primary-color-lv1 !important;}",
  ".ser-item:hover .arrow":
    "{background-color: @portal-primary-color-lv5 !important;color: @portal-primary-color-lv1 !important;}",
  ".gateway-card .we-tabs__item": "{color: @portal-font-color-lv1 !important;}",
  ".we-tabs__item": "{color: @portal-font-color-lv2 !important;}",
  ".we-tabs__item.is-active": "{color: @portal-font-color-lv1 !important;}",
  ".we-tabs__item:hover ": "{color: @portal-primary-color-lv1 !important;}",
  ".service-tab .we-tabs__item.is-active":"{color: @portal-primary-color-lv1 !important;}",
  ".we-tabs__nav-prev:hover,.we-tabs__nav-next:hover":
    "{color: @portal-primary-color-lv1 !important;border-color:@portal-primary-color-lv1 !important;}",
  ".we-tabs__active-bar::before":
    "{background-color: @portal-primary-color-lv1 !important;}",
  ".we-checkbox, .we-checkbox-button__inner":
    "{color: @portal-font-color-lv1;}", // checkbox 文字
  ".we-checkbox__input.is-checked+.we-checkbox__label":
    "{color: @portal-font-color-lv1;}", // checkbox 选中文字
  ".we-checkbox__input.is-checked .we-checkbox__inner, .we-checkbox__input.is-indeterminate .we-checkbox__inner":
    "{background-color: @portal-primary-color-lv1;border-color: @portal-primary-color-lv1}", // checkbox 选中复选框
  ".we-checkbox__inner:hover,.we-checkbox__input.is-focus .we-checkbox__inner":
    "{border-color: @portal-primary-color-lv1}", // checkbox 复选框 悬浮颜色
  ".we-button.is-plain:focus, .we-button.is-plain:hover":
    "{border-color: @portal-primary-color-lv1; color: @portal-font-color-lv1;}", // 按钮样式
  ".we-pagination.is-background .btn-next, .we-pagination.is-background .btn-prev, .we-pagination.is-background .we-pager li":
    "{border: 1px solid #D9D9D9;background-color: #fff;color: @portal-font-color-lv3;}", // 分页边框
  ".we-pager li:hover,.we-pagination.is-background .we-pager li:not(.disabled):hover":
    "{color: @portal-primary-color-lv1 !important;}", // 分页 文字
  ".we-pagination, .we-pagination__jump": "{color: @portal-font-color-lv3;}", //分页内文字
  ".we-range-editor.is-active, .we-range-editor.is-active:hover, .we-select .we-input.is-focus .we-input__inner, .we-pagination__sizes .we-input .we-input__inner:hover":
    "{border-color: @portal-primary-color-lv1;}", // 分页条数切换 文字
  ".we-select-dropdown__item.selected": "{color: @portal-primary-color-lv1;}", // 下拉框选中文字
  ".we-pagination.is-background .we-pager li:not(.disabled).active":
    "{background-color: #fff;border-color: @portal-primary-color-lv1;color:@portal-primary-color-lv1 !important;}", // 分页边框
  ".we-message__content": "{color:@portal-font-color-lv1!important}",
  ".we-menu--horizontal>.we-submenu.is-active .we-submenu__title":
    "{border-bottom: none!important}",
  ".service-bus-source-item-active::before":
    "{background: @portal-font-color-lv1;}", // 业务直通车border
  ".custom__item:hover .nav__title": "{color: @portal-primary-color-lv1}",
  ".we-input__inner::-webkit-input-placeholder":
    "{color: @portal-font-color-lv4;}", //谷歌输入框placeholder文字颜色
  ".we-input__inner::-moz-placeholder": "{color: @portal-font-color-lv4}", //火狐输入框placeholder文字颜色
  ".we-input__inner:-ms-input-placeholder": "{color: @portal-font-color-lv4}", //IE输入框placeholder文字颜色
  ".we-pagination .we-input__inner": "{color: @portal-font-color-lv3}", // 分页器 输入框内文字
  ".newsLink-visited": "{color: @portal-font-color-lv1}",
  ".newsLink-visited:visited": "{color: @portal-font-color-lv3 !important}", // 新闻已读样式
  ".newsLink-visited:hover": "{color:  @portal-primary-color-lv1 !important}", // 新闻已读样式
  ".subscribeModal .we-checkbox, .we-checkbox-button__inner":
    "{color: @portal-font-color-lv2;}", // 新闻 checkbox 文字
  ".we-dialog .we-dialog__header .we-dialog__title":
    "{color: @portal-font-color-lv1 !important;}",
  ".myOffice .we-tabs__item.is-active,.myOffice .we-tabs__item:hover":
    "{color:@portal-primary-color-lv1 !important;}",
  ".myOffice .we-tabs__item": "{color: @portal-font-color-lv2 !important;}",
  ".myOffice .cell": "{color: @portal-font-color-lv1 !important;}",
  ".myOffice .we-pager .number.active": "{border-color: @portal-primary-color-lv1 !important;}",
  ".myOffice-tab .we-input__inner":"{color: @portal-font-color-lv1 !important;}",
  '.we-tree-node__expand-icon': "{color: @portal-font-color-lv4;}",
  ".we-table, .we-table thead": "{color: @portal-font-color-lv1 !important;}"
};
