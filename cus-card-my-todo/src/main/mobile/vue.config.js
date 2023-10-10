/*
 * @Author: 杨芯悦 xyyang01@wisedu.com
 * @Date: 2023-07-18 17:16:52
 * @LastEditors: 杨芯悦 xyyang01@wisedu.com
 * @LastEditTime: 2023-07-19 16:16:54
 */
const TerserPlugin = require('terser-webpack-plugin')

const devMode = process.env.NODE_ENV === 'development'

module.exports = {
    publicPath: devMode ? "http://localhost:5013/" : "/CUS_CARD_MY_TODO/mobile",
    outputDir: '../resources/static/CUS_CARD_MY_TODO/mobile',
    configureWebpack:{
        devtool: 'cheap-source-map',
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
        ],
        optimization: {
            minimize: !devMode,
            minimizer:  devMode ? undefined : [new TerserPlugin({
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
        port: 5013,
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    }
}
