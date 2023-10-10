/**
 * 第三方组件主题样式覆盖
 * key为选择器名称
 * value为对应的样式，颜色值为@符号加颜色变量标识
 */
export default {
  ".we-tabs .we-tab--active:after": "{background-color:@portal-primary-color-lv1;}",
  ".we-calendar__day--start,.we-calendar__day--end,.we-button--danger":"{background-color:@portal-primary-color-lv1;}",
  ".we-calendar__day--middle":"{color:@portal-primary-color-lv1;}",
  ".we-button--danger":"{border-color:@portal-primary-color-lv1;}",
  ".we-tab": "{color:@portal-font-color-lv2;}",
  ".subscribe-tabs .we-tab--active":"{color:@portal-font-color-lv2 !important;}",
  ".subscribe-tabs .we-tab": "{color:@portal-primary-color-lv1;}",
  ".card-newsAnnouncement .we-tab":"{color:@portal-font-color-lv1;}",
  ".we-tab.we-tab--active": "{color:@portal-font-color-lv1;}",
  ".card-newsAnnouncement .we-tab.we-tab--active": "{color:@portal-primary-color-lv1;}",
  ".card-sys-title .card-name:after": "{background-color:@portal-primary-color-lv1;}",
  ".we-field__control::-webkit-input-placeholder": "{color: @portal-font-color-lv3}", //谷歌输入框placeholder文字颜色
  ".we-field__control::-moz-placeholder": "{color: @portal-font-color-lv3}", //火狐输入框placeholder文字颜色
  ".we-field__control": "{color:@portal-font-color-lv1;}",
  ".we-checkbox__label": "{color:@portal-font-color-lv2;}",
  ".we-checkbox__icon--checked .we-icon": "{background-color:@portal-primary-color-lv1;border-color: @portal-primary-color-lv1!important;}",
  ".category-tab .we-tab":"{color:@portal-font-color-lv1;}",
  ".category-tab .we-tab--active":"{color:@portal-primary-color-lv1;}",
  '.we-action-sheet .we-action-sheet__header':"{color:@portal-font-color-lv1;}",
  ".footer-menu .we-tabbar-item":"{color:@portal-font-color-lv2;}",
  ".newsLists__item .newsLists__item__title": "{color: @portal-font-color-lv1}", // 新闻已读样式
  ".newsLists__item:visited .newsLists__item__title": "{color: @portal-font-color-lv2 !important}", // 新闻已读样式
  ".we-dropdown-menu__title": "{color: @portal-font-color-lv1}",
  ".we-dropdown-menu__title--active": "{color: @portal-primary-color-lv1;}",
  ".we-dropdown-menu__title--active::after": "{border-color: transparent transparent @portal-primary-color-lv1 @portal-primary-color-lv1 !important}"
};