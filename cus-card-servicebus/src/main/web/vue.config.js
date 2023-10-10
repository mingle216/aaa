// const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const TerserPlugin = require('terser-webpack-plugin')

const devMode = process.env.NODE_ENV === 'development'

module.exports = {
  publicPath: devMode ? 'http://localhost:6777' : '/CUS_CARD_SERVICEBUS/pc',
  outputDir: '../resources/static/CUS_CARD_SERVICEBUS/pc',
  configureWebpack:{
    devtool: 'cheap-source-map',
    watch: true,
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
            drop_console: false,
            drop_debugger: false
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
    config.entry('config').add('./src/config.js')
  },
  css: {
    extract: false
  },
  devServer: {
    progress: false,
    port: 6777,
    headers: {
      'Access-Control-Allow-Origin': '*'
    }
  }
}
