/**
 * 第三方组件主题样式覆盖
 * key为选择器名称
 * value为对应的样式，颜色值为@符号加颜色变量标识
 */
// pc模板的组件样式
export const themeMapPc = {
  ".we-select-dropdown__item.hover,.we-select-dropdown__item:hover,.we-select-dropdown__item.selected,.we-tree-node__content:hover,.we-tree-node:focus>.we-tree-node__content":
    "{background-color: @portal-primary-color-lv5;}",
  ".we-select-dropdown__item":
    "{padding:0 12px;height:36px;line-height:36px;color:@portal-font-color-lv1}",
  ".we-input-group__append, .we-input-group__prepend":
    "{border-color: #d9d9d9 !important;}",
  ".we-select .we-input .we-select__caret": "{color: @portal-font-color-lv4!important;}",
  ".we-cascader-node": "{color: @portal-font-color-lv1}",
  ".we-cascader-node__postfix": "{color: @portal-font-color-lv2}",
  ".we-cascader-node:not(.is-disabled):focus, .we-cascader-node:not(.is-disabled):hover": "{background: @portal-primary-color-lv5}",
  ".we-cascader-node:not(.is-disabled):hover .we-cascader-node__label": "{color: @portal-primary-color-lv1}",
  ".we-cascader-node.in-active-path, .we-cascader-node.is-active, .we-cascader-node.is-selectable.in-checked-path": "{color: @portal-primary-color-lv1; background: @portal-primary-color-lv5}",
  ".we-message-box__headerbtn .we-message-box__close": "{color:#8c8c8c}",
  ".we-message-box__title": "{color: @portal-font-color-lv1}",
  ".we-message-box__message": "{color: @portal-font-color-lv3}",
  ".we-message-box__status+.we-message-box__message": "{padding-left:24px}",
  ".we-message-box__status": "{font-size:16px!important}"
};
// h5模板的组件样式
export const themeMapH5 = {
  ".we-dialog__header": "{font-weight:bold}",
  ".we-dialog__confirm, .we-dialog__confirm:active": "{color:@portal-primary-color-lv1}",
  ".we-search .we-icon-search": "{color:@portal-font-color-lv3}",
};
