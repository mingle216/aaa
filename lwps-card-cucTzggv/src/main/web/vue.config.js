// const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const TerserPlugin = require('terser-webpack-plugin')
const { SourceMapDevToolPlugin } = require('webpack')
const devMode = process.env.NODE_ENV === 'development'

module.exports = {
  publicPath: process.env.NODE_ENV === "development" ? "http://localhost:5014/" : "/LWPS_CARD_CUCTZGGV/pc",
  outputDir: '../resources/static/LWPS_CARD_CUCTZGGV/pc',
  configureWebpack:{
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
      new SourceMapDevToolPlugin({ //修改soumap对应的文件路径
        filename: '[file].map',
        publicPath: '/LWPS_CARD_CUCTZGGV/pc/',
        columns: false, // 表示是否应该使用 column mapping（默认为 true）
        module: false,
        lineToLine: false
      })
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
    config.module
      .rule("i18n")
      .resourceQuery(/blockType=i18n/)
      .type('javascript/auto')
      .use("i18n")
        .loader("@kazupon/vue-i18n-loader")
        .end();
  },
  css: {
    extract: false
  },
  devServer: {
    progress: false,
    port: 5014,
    // proxy: {
    //     '^/api': {
    //         target: 'http://172.16.72.225:8081/emap/',//接口的前缀
    //         ws:true,//代理websocked
    //         changeOrigin:true,//虚拟的站点需要更管origin
    //         pathRewrite:{
    //             '^/api':''//重写路径
    //         }
    //     }
    // },
    headers: {
      'Access-Control-Allow-Origin': '*'
    }
  }
}
