module.exports = {
    plugins: 
        {
        'postcss-pxtorem': {
            // 设计稿 375:37.5
            // 设计稿：750:75
            // Vant 是基于 375
            rootValue: 37.5,
            selectorBlackList: ['weui', 'mu'], //忽略转换正则匹配项
            propList: ['*'],
        },
    },
};
