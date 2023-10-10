const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const TerserPlugin = require('terser-webpack-plugin')

const devMode = process.env.NODE_ENV === 'development'

module.exports = {
  publicPath: process.env.NODE_ENV === 'development' ? 'http://localhost:6110/' : '/CUS_CARD_ROLESERVICEITEM/pc',
  outputDir: '../resources/static/CUS_CARD_ROLESERVICEITEM/pc',
  configureWebpack: {
    devtool: 'cheap-source-map',
    watch: false,
    output: {
      // 把子应用打包成 umd 库格式，名称需要按约定写死
      library: '__REMOTE_LOADED_APP__',
      filename: 'js/[name].js',
      libraryTarget: 'umd',
      // globalObject: 'this'
    },
    externals: {
      vue: 'Vue'
    },
    plugins: [
      // new BundleAnalyzerPlugin()
    ],
    optimization: {
      minimize: !devMode,
      minimizer: devMode ? undefined : [new TerserPlugin({
        sourceMap: true,
        extractComments: true,
        terserOptions: {
          output: {
            comments: false
          },
          compress: {
            drop_console: true,
            drop_debugger: true,
            pure_funcs: ['console.log']//移除console
          }
        }
      })]
    }
  },
  chainWebpack: (config) => {
    config.optimization.delete('splitChunks')
    config.plugin('define').tap((definitions) => {
      definitions[0]['process.env']['RUN_MODE'] = JSON.stringify(process.env.RUN_MODE)
      return definitions
    })
  },
  css: {
    extract: false
  },
  devServer: {
    progress: false,
    port: 6110,
    headers: {
      'Access-Control-Allow-Origin': '*'
    }
  }
}