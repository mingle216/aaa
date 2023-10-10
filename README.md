# 门户引擎


### 前端开发模式

远程加载 vue 应用的简单方案，类似微前端，适合加载大量轻量的远程 vue 应用到一个页面上。

基本实现是给远程应用增加一个 bootstrap 的外部方法，并打包为 umd 格式。外壳应用远程获取打包的 js 文件并执行 bootstrap 入口函数。

css 隔离通过 vue scope 方式解决。

1. 运行前端底座master

cd casp-portal-webapps/src/main/master

yarn # 安装当前依赖

yarn dev # 运行

yarn build # 打包

2. 运行模板

cd sys-template-official/src/main/web

yarn # 安装当前依赖

yarn dev:shell # 运行

yarn build # 打包

3. 运行卡片

cd sys-card-link\src\main\web

yarn # 安装当前依赖

yarn dev:shell # 运行

yarn build # 打包


# FAQ
若只能启主应用 不能启子应用 确认执行过命令：yarn global add serve


## 语言包
本地语言包目录：official 模板下 src/assets/languages
合并语言包：this.$i18n.mergeLocaleMessage(语言环境, 新的语言包)

## 配置
卡片需要在src/utils/index.js中获取从模板props中传递的i18n添加到Vue实例上
```js
import Vue from 'vue'

export function mount(component, container, options) {
  // eslint-disable-next-line no-debugger
  return new Vue({
    i18n: options.props.i18n,
    render: h => h(component, options)
  }).$mount(container)
}
```

## 使用
- 获取当前语言环境：this.$i18n.locale

- 获取当前语言包：this.$i18n.messages

- 格式化：this.$t(定义的语言变量, 可选传参)

如：
```js
en_US 语言环境
{
  common: {
    hello: "{msg} world",
  }
}
```
模板如下：
```js
  <p>{{ $t('common.hello', { msg: 'hello' }) }}</p>
```
输出如下
```js
<p>hello world</p>
```



## 单文件组件（可以自定义卡片自身的语言环境）
  如果使用单文件组件构建 Vue 组件或 Vue 应用程序，则可以管理 i18n 自定义块的语言环境信息。 以下是单文件组件示例:
```js
<i18n>
{
  "en_US": {
    "hello": "hello world!"
  },
  "zh_CN": {
    "hello": "你好世界！"
  }
}
</i18n>

<template>
  <p>{{ $t('message') }}</p>  // 卡片自身语言环境
  <p>{{ $t('common.lgoin') }}</p> // 公共语言环境
</template>
```

## 安装 vue-i18n-loader(使用单文本组件)
```js
npm i --save-dev @kazupon/vue-i18n-loader
```
vue.config.js配置
```js
module.exports = {
  chainWebpack: config => {
    config.module
      .rule("i18n")
      .resourceQuery(/blockType=i18n/)
      .type('javascript/auto')
      .use("i18n")
        .loader("@kazupon/vue-i18n-loader")
        .end();
  }
}
```


### 前端开发 

以链接导航卡片为例：

cd sys-card-link\src\main\SYS_CARD_LINK

yarn # 安装当前依赖

yarn dev:shell # 运行

yarn build # 打包


### 卡片模板对照关系 

| 卡片/模板ID                        | 卡片/模板模块名称                  | 卡片/模板名称      |
| ---------------------------------- | ---------------------------------- | ------------------ |
| SYS_CARD_ALLSERVICEITEM            | sys-card-allServiceItem            | 全部事项           |
| SYS_CARD_CAROUSEL                  | sys-card-carousel                  | 轮播图             |
| SYS_CARD_CLASSTIMETABLE            | sys-card-classtimetable            | 课表               |
| SYS_CARD_UNDONE                    | sys-card-undone                    | 待办              |
| SYS_CARD_TODOTASK                  | sys-card-todoTask                  | 待办任务           |
| SYS_CARD_DONETASK                  | sys-card-doneTask                  | 已办任务           |
| SYS_CARD_MYTASK                    | sys-card-myTask                    | 我发起的           |
| SYS_CARD_FAVORITEAPP               | sys-card-favoriteApp               | 我收藏的服务       |
| SYS_CARD_HOTAPP                    | sys-card-hotApp                    | 热门服务           |
| SYS_CARD_HOTSERVICEITEMS           | sys-card-hotServiceItems           | 热门事项           |
| SYS_CARD_LINK                      | sys-card-link                      | 链接导航           |
| SYS_CARD_MESSAGECENTER             | sys-card-messageCenter             | 消息中心           |
| SYS_CARD_MYFAVORITESSERVICEITEM    | sys-card-myfavoritesserviceItem    | 我收藏的事项       |
| SYS_CARD_NEWSANNOUNCEMENT          | sys-card-newsAnnouncement          | 新闻通知公告       |
| SYS_CARD_RECOMMENDAPP              | sys-card-recommendapp              | 推荐服务           |
| SYS_CARD_RECOMMENDSERVICEITEMS     | sys-card-recommendServiceItems     | 推荐事项           |
| SYS_CARD_SERVICEBUS                | sys-card-servicebus                | 业务直通车         |
| SYS_CARD_ALLSERVICE                | sys-card-allService                | 全部服务           |
| SYS_CARD_SERVICEITEMCATEGORY       | sys-card-serviceItemCategory       | 事项分类           |
| SYS_CARD_SERVICEITEMCOUNT          | sys-card-serviceItemCount          | 事项统计           |
| SYS_CARD_DETAILSOFSERVICEITEMS     | sys-card-detailsofServiceitems     | 事项详情           |
| SYS_CARD_SEARCHRESULTS             | sys-card-searchResults             | 搜索结果           |
| SYS_CARD_SERVICEITEMCATEGORYDETAIL | sys-card-serviceItemCategoryDetail | 事项分类详情       |
| SYS_CARD_RICHTEXT                  | sys-card-richtext                  | 富文本卡片         |
| SYS_CARD_LASTESTAPP                | sys-card-lastestapp                | 最新服务           |
| SYS_CARD_LATESTSERVICEITEMS        | sys-card-latestServiceItems        | 最新事项           |
| SYS_CARD_PERSONALCENTER            | sys-card-personalcenter            | 个人中心 （未实现）|
| SYS_CARD_PERSONALDATA              | sys-card-personaldata              | 个人数据
| SYS_CARD_THREELIST                 | sys-card-threeList                 | 三张清单（未实现）  |
| SYS_CARD_TOPICS                    | sys-card-topics                    | 专题推荐（未实现）  |
| SYS_CARD_FEEDBACK                  | sys-card-feedback                  | 意见反馈        |
| SYS_TEMPLATE_OFFICIAL              | sys-template-official              | official模板       |
| SYS_CARD_STATISTICS                | sys-card-statistics                | 办理统计          |
| SYS_CARD_RECUSEAPP                 | sys-card-recuseapp                 | 最近使用服务            |
| SYS_CARD_NEWSLISTANNOUNCEMENT      | sys-card-newsListAnnouncement      | 新闻列表            |
| SYS_CARD_PRODUCTSCHOOLCALENDAR     | sys-card-productSchoolCalendar     | 校历            |
| SYS_CARD_PROCESSINGPROGRESS        | sys-card-processingProgress        | 我的办理进度            |
| SYS_CARD_MYOFFICE                  | sys-card-myoffice                  | 我的办件           |
