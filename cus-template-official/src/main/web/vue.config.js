const { CleanWebpackPlugin } = require('clean-webpack-plugin')
const { SourceMapDevToolPlugin } = require('webpack')

console.log('process.env.RUN_MODE', process.env.RUN_MODE)
module.exports = {
  publicPath: process.env.NODE_ENV === 'development' ? 'http://localhost:8001/' : './',
  outputDir: '../resources/static/CUS_TEMPLATE_OFFICIAL/pc',
  configureWebpack:{
    devtool: false,
    externals: {
      vue: 'Vue',
      fetch: 'fetch',
      'vue-i18n': 'VueI18n'
    },
    plugins: [

      new SourceMapDevToolPlugin({ //修改soumap对应的文件路径
        filename: '[file].map',
        publicPath: process.env.NODE_ENV === 'development' ?  '' : '/CUS_TEMPLATE_OFFICIAL/pc',
        columns: false, // 表示是否应该使用 column mapping（默认为 true）
        module: false,
        lineToLine: false
      })
      // new webpack.DefinePlugin({
      //   NODE_ENV: JSON.stringify(process.env.NODE_ENV),
      //   RUN_MODE: JSON.stringify(process.env.RUN_MODE),
      // })
    ]
  },
  chainWebpack: config => {
    config.module
      .rule("i18n")
      .resourceQuery(/blockType=i18n/)
      .type('javascript/auto')
      .use("i18n")
        .loader("@kazupon/vue-i18n-loader")
        .end();
		config.plugins.delete('progress')
    config.plugin('define').tap((definitions) => {
      definitions[0]['process.env']['RUN_MODE'] = JSON.stringify(process.env.RUN_MODE)
      return definitions
    })
  },
  devServer: {
    port: 8001,
    progress: false,
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  },
  transpileDependencies: ['vue-clamp', 'resize-detector']
}
