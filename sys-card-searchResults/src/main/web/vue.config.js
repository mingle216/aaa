// const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const TerserPlugin = require('terser-webpack-plugin')

const devMode = process.env.NODE_ENV === 'development'
const { SourceMapDevToolPlugin } = require('webpack')
module.exports = {
  publicPath: devMode ? 'http://localhost:6002' : '/SYS_CARD_SEARCHRESULTS/pc',
  outputDir: '../resources/static/SYS_CARD_SEARCHRESULTS/pc',
  configureWebpack:{
    devtool: false,
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
    plugins:  [
      new SourceMapDevToolPlugin({ //修改soumap对应的文件路径
        filename: '[file].map',
        publicPath: '/SYS_CARD_SEARCHRESULTS/pc/',
        columns: false, // 表示是否应该使用 column mapping（默认为 true）
        module: false,
        lineToLine: false
      })
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
  productionSourceMap: false,
  css: {
    extract: false
  },
  devServer: {
    progress: false,
    port: 6002,
    headers: {
      'Access-Control-Allow-Origin': '*'
    }
  }
}
