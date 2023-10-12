const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const TerserPlugin = require('terser-webpack-plugin')

const devMode = process.env.NODE_ENV === 'development'

module.exports = {
  publicPath: process.env.NODE_ENV === 'development' ? 'http://localhost:5011/' : '/CUS_CARD_MYSERVICE/pc',
  outputDir: '../resources/static/CUS_CARD_MYSERVICE/pc',
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
            // drop_console: false,
            // drop_debugger: false
          }
        }
      })]
    }
  },
  chainWebpack: (config) => {
    config.module
      .rule("i18n")
      .resourceQuery(/blockType=i18n/)
      .type('javascript/auto')
      .use("i18n")
        .loader("@kazupon/vue-i18n-loader")
        .end();
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
    port: 5011,
    headers: {
      'Access-Control-Allow-Origin': '*'
    }
  }
}