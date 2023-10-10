const { CleanWebpackPlugin } = require('clean-webpack-plugin')
const { SourceMapDevToolPlugin } = require('webpack')
const TerserPlugin = require('terser-webpack-plugin')
 
console.log('process.env.RUN_MODE', process.env.RUN_MODE)
module.exports = {
  publicPath: process.env.NODE_ENV === 'development' ? 'http://localhost:8001/' : './',
  outputDir: '../resources/static/CUS_TEMPLATE_OFFICIAL/mobile',
  configureWebpack: {
    devtool: false,
    externals: {
      vue: 'Vue',
      fetch: 'fetch',
      'vue-i18n': 'VueI18n'
    },
    plugins: [
        new SourceMapDevToolPlugin({ //修改soumap对应的文件路径
          filename: '[file].map',
          publicPath: process.env.NODE_ENV === 'development' ? '' : '/CUS_TEMPLATE_OFFICIAL/mobile/',
          columns: false, // 表示是否应该使用 column mapping（默认为 true）
          module: false,
          lineToLine: false
        })
    ],
    optimization: {
      minimizer: process.env.NODE_ENV === "development"
        ? undefined
        : [
            new TerserPlugin({
              sourceMap: true,
              extractComments: true,
              terserOptions: {
                output: {
                  comments: false,
                },
                compress: {
                  drop_console: true,
                  drop_debugger: true,
                  pure_funcs: ['console.log']//移除console,
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
  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'less',
      patterns: [],
    },
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


