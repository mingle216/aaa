const { CleanWebpackPlugin } = require('clean-webpack-plugin')
const { SourceMapDevToolPlugin } = require('webpack')
const TerserPlugin = require('terser-webpack-plugin')
module.exports = {
  publicPath: process.env.NODE_ENV === 'development' ? 'http://localhost:8001/' : './',
  outputDir: '../resources/static/CUS_TEMPLATE_OFFICIAL/pc',
  lintOnSave: false,
  configureWebpack:{
    devtool: 'cheap-source-map',
    externals: {
      vue: 'Vue',
      fetch: 'fetch',
      'vue-i18n': 'VueI18n'
    },
    plugins: [

      new SourceMapDevToolPlugin({ //修改soumap对应的文件路径
        filename: '[file].map',
        publicPath: process.env.NODE_ENV === 'development' ?  '' : '/CUS_TEMPLATE_OFFICIAL/pc/',
        columns: false, // 表示是否应该使用 column mapping（默认为 true）
        module: false,
        lineToLine: false
      })
      // new webpack.DefinePlugin({
      //   NODE_ENV: JSON.stringify(process.env.NODE_ENV),
      //   RUN_MODE: JSON.stringify(process.env.RUN_MODE),
      // })
    ],
    optimization: {
      minimizer: process.env.NODE_ENV === "development"
        ? undefined
        : [
            new TerserPlugin({
              // sourceMap: true,
              // extractComments: true,
              terserOptions: {
                output: {
                  // comments: false,
                },
                compress: {
                  // drop_console: true,
                  // drop_debugger: true,
                  // pure_funcs: ['console.log']//移除console,
                },
              },
            }),
          ],
    },
  },
  chainWebpack: config => {
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
