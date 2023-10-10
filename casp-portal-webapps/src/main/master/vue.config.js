const path = require("path");
const TerserPlugin = require("terser-webpack-plugin");

require('events').EventEmitter.defaultMaxListeners = 0; // 代理配置过多导致溢出了监听器的最大值，解除限制

const resolve = (p) => {
  return path.resolve(__dirname, p);
};
const cookie1 = (proxyReq) => {
  proxyReq.setHeader("Cookie", "Secure; route=246a26d29bc11f06eb8ea7fc2e306fc1; casp-portal=8713c4a25ae87c314487b2c2d2f7bb7e; Secure; COM.WISEDU.CASP.IS_RANDOM=0; COM.WISEDU.CASP.SITEWID=-100; WISCPSID=97370d3c-92e6-4f43-893f-463e473cf40f");
};
module.exports = {
  outputDir: "../resources/static",
  configureWebpack: {
    devtool: "cheap-source-map",
    resolve: {
      alias: {
        "@components": resolve("src/components"),
        "@views": resolve("src/views"),
        "@router": resolve("src/router"),
        "@store": resolve("src/store"),
        "@libs": resolve("src/libs"),
        "@utils": resolve("src/libs/utils"),
        "@tools": resolve("src/libs/tools"),
        "@api": resolve("src/api"),
        "@service": resolve("src/api/service"),
        "@time": resolve("src/libs/time"),
      },
    },
    externals: {
      vue: "Vue",
      "vue-router": "VueRouter",
      'vue-i18n': 'VueI18n'
    },
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
  chainWebpack: (config) => {
    config.plugin("html").tap((args) => {
      args[0].title = "网上办事服务大厅";
      return args;
    });
  },

  devServer: {
    progress: false,
    port: 8008,
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
    proxy: {
      // "/execCardMethod": {
      //   target: 'http://172.31.24.180:8116',
      //   changeOrigin: true,
      // onProxyReq: cookie1,
      //   pathRewrite: {
      //     // 去掉 路径中的  /api  的这一截
      //     "^/manager": "",
      //   },
      // },
      "/manager|/oss|/iconfont": {
        target: 'https://ehall.hainanu.edu.cn/',
        // Referer: "http://caspportal.wisedu.com",
        headers: {
          Referer: 'https://ehall.hainanu.edu.cn/'
        },

        changeOrigin: true,
        onProxyReq: cookie1,

        pathRewrite: {
          // 去掉 路径中的  /api  的这一截
          "^/manager": "",
        },
      },
      // SYS_TEMPLATE_OFFICIAL: {
      //   target: "http://localhost:8001",
      //   pathRewrite: { "/SYS_TEMPLATE_OFFICIAL": "" },
      // },
      CUS_TEMPLATE_OFFICIAL: {
        target: "http://localhost:8001",
        pathRewrite: {
          "/CUS_TEMPLATE_OFFICIAL": ""
        },
      },
      SYS_TEMPLATE_DESIGN: {
        target: "http://localhost:8002",
        pathRewrite: { "/SYS_TEMPLATE_DESIGN": "" },
      },
      SYS_TEMPLATE_STANDARDS: {
        target: "http://localhost:8003",
        pathRewrite: { "/SYS_TEMPLATE_STANDARDS": "" },
      },
      SYS_TEMPLATE_VIOLET: {
        target: "http://localhost:8010",
        pathRewrite: { "/SYS_TEMPLATE_VIOLET": "" },
      },
      SYS_TEMPLATE_MAPLE: {
        target: "http://localhost:8004",
        pathRewrite: { "/SYS_TEMPLATE_MAPLE": "" },
      },
      SYS_TEMPLATE_WORK: {
        target: "http://localhost:8005",
        pathRewrite: { "/SYS_TEMPLATE_WORK": "" },
      },
      SYS_TEMPLATE_CONCISE: {
        target: "http://localhost:8006",
        pathRewrite: { "/SYS_TEMPLATE_CONCISE": "" },
      },
      CUS_CARD_ALLSERVICEITEM: {
        target: "http://localhost:6100",
        pathRewrite: {
          "/CUS_CARD_ALLSERVICEITEM": ""
        },
      },
      CUS_CARD_CAROUSEL: {
        target: "http://localhost:6011",
        pathRewrite: {
          "/CUS_CARD_CAROUSEL": ""
        },
      },
      CUS_CARD_LINK: {
        target: "http://localhost:6101",
        pathRewrite: {
          "/CUS_CARD_LINK": ""
        },
      },
      CUS_CARD_PERSONALINFO: {
        target: "http://localhost:6102",
        pathRewrite: {
          "/CUS_CARD_PERSONALINFO": ""
        },
      },
      CUS_CARD_WDSW: {
        target: "http://localhost:6103",
        pathRewrite: {
          "/CUS_CARD_WDSW": ""
        },
      },
      CUS_CARD_ZTZL: {
        target: "http://localhost:6104",
        pathRewrite: {
          "/CUS_CARD_ZTZL": ""
        },
      },
      CUS_CARD_CALENDAR: {
        target: "http://localhost:6105",
        pathRewrite: {
          "/CUS_CARD_CALENDAR": ""
        },
      },
      CUS_CARD_RECOMMENDAPP: {
        target: "http://localhost:6106",
        pathRewrite: {
          "/CUS_CARD_RECOMMENDAPP": ""
        },
      },
      CUS_CARD_RECOMMENDSERVICEITEMS: {
        target: "http://localhost:6107",
        pathRewrite: {
          "/CUS_CARD_RECOMMENDSERVICEITEMS": ""
        },
      },
      CUS_CARD_MY_TODO: {
        target: "http://localhost:6108",
        pathRewrite: {
          "/CUS_CARD_MY_TODO": ""
        },
      },
      CUS_CARD_SERVICEBUS: {
        target: "http://localhost:6109",
        pathRewrite: {
          "/CUS_CARD_SERVICEBUS": ""
        },
      },
      CUS_CARD_ROLESERVICEITEM: {
        target: "http://localhost:6110",
        pathRewrite: {
          "/CUS_CARD_ROLESERVICEITEM": ""
        },
      },
      CUS_CARD_HOTAPP: {
        target: "http://localhost:6111",
        pathRewrite: {
          "/CUS_CARD_HOTAPP": ""
        },
      },
      CUS_CARD_TODOTASK: {
        target: "http://localhost:6112",
        pathRewrite: {
          "/CUS_CARD_TODOTASK": ""
        },
      },
      SYS_CARD_UNDONE: {
        target: "http://localhost:5000",
        pathRewrite: { "/SYS_CARD_UNDONE": "" },
      },
      SYS_CARD_SERVICEITEMCATEGORYDETAIL: {
        target: "http://localhost:7002",
        pathRewrite: { "/SYS_CARD_SERVICEITEMCATEGORYDETAIL": "" },
      },
      SYS_CARD_SERVICEITEMCATEGORY: {
        target: "http://localhost:6004",
        pathRewrite: { "/SYS_CARD_SERVICEITEMCATEGORY": "" },
      },
      SYS_CARD_DETAILSOFSERVICEITEMS: {
        target: "http://localhost:5001",
        pathRewrite: { "/SYS_CARD_DETAILSOFSERVICEITEMS": "" },
      },
      SYS_CARD_FEEDBACK: {
        target: "http://localhost:5002",
        pathRewrite: { "/SYS_CARD_FEEDBACK": "" },
      },
      SYS_CARD_NEWSDETAIL: {
        target: "http://localhost:5060",
        pathRewrite: { "/SYS_CARD_NEWSDETAIL": "" },
      },
      SYS_CARD_CALENDAR_DETAIL: {
        target: "http://localhost:9018",
        pathRewrite: { "/SYS_CARD_CALENDAR_DETAIL": "" },
      },
      SYS_CARD_LINK: {
        target: "http://localhost:5003",
        pathRewrite: { "/SYS_CARD_LINK": "" },
      },
      SYS_CARD_NEWSANNOUNCEMENT: {
        target: "http://localhost:5004",
        pathRewrite: { "/SYS_CARD_NEWSANNOUNCEMENT": "" },
      },
      SYS_CARD_NEWSLISTANNOUNCEMENT: {
        target: "http://localhost:7003",
        pathRewrite: { "/SYS_CARD_NEWSLISTANNOUNCEMENT": "" },
      },
      SYS_CARD_MYFAVORITENEWS: {
        target: "http://localhost:7004",
        pathRewrite: { "/SYS_CARD_MYFAVORITENEWS": "" },
      },
      SYS_CARD_CAROUSEL: {
        target: "http://localhost:5005",
        pathRewrite: { "/SYS_CARD_CAROUSEL": "" },
      },
      SYS_CARD_HOTSERVICEITEMS: {
        target: "http://localhost:5006",
        pathRewrite: { "/SYS_CARD_HOTSERVICEITEMS": "" },
      },
      SYS_CARD_LATESTSERVICEITEMS: {
        target: "http://localhost:5007",
        pathRewrite: { "/SYS_CARD_LATESTSERVICEITEMS": "" },
      },
      SYS_CARD_SERVICEITEMCOUNT: {
        target: "http://localhost:5008",
        pathRewrite: { "/SYS_CARD_SERVICEITEMCOUNT": "" },
      },
      SYS_CARD_MYFAVORITESSERVICEITEM: {
        target: "http://localhost:5009",
        pathRewrite: { "/SYS_CARD_MYFAVORITESSERVICEITEM": "" },
      },
      SYS_CARD_RECOMMENDSERVICEITEMS: {
        target: "http://localhost:5010",
        pathRewrite: { "/SYS_CARD_RECOMMENDSERVICEITEMS": "" },
      },
      SYS_CARD_RECOMMENDONETHING: {
        target: "http://localhost:6007",
        pathRewrite: { "/SYS_CARD_RECOMMENDONETHING": "" },
      },
      SYS_CARD_ALLSERVICEITEM: {
        target: "http://localhost:5011",
        pathRewrite: { "/SYS_CARD_ALLSERVICEITEM": "" },
      },
	    SYS_CARD_APPROVALLIST: {
		    target: "http://localhost:5016",
		    pathRewrite: { "/SYS_CARD_APPROVALLIST": "" },
	    },
	    SYS_CARD_SERVICELIST: {
		    target: "http://localhost:5017",
		    pathRewrite: { "/SYS_CARD_SERVICELIST": "" },
	    },
	    SYS_CARD_DUTYLIST: {
		    target: "http://localhost:5018",
		    pathRewrite: { "/SYS_CARD_DUTYLIST": "" },
	    },
      SYS_CARD_ALLSERVICE: {
        target: "http://localhost:5013",
        pathRewrite: { "/SYS_CARD_ALLSERVICE": "" },
      },
      SYS_CARD_MESSAGECENTER: {
        target: "http://localhost:6001",
        pathRewrite: { "/SYS_CARD_MESSAGECENTER": "" },
      },
      SYS_CARD_HOTAPP: {
        target: "http://localhost:5015",
        pathRewrite: { "/SYS_CARD_HOTAPP": "" },
      },
      SYS_CARD_FAVORITEAPP: {
        target: "http://localhost:9004",
        pathRewrite: { "/SYS_CARD_FAVORITEAPP": "" },
      },
      SYS_CARD_LASTESTAPP: {
        target: "http://localhost:9005",
        pathRewrite: { "/SYS_CARD_LASTESTAPP": "" },
      },
      SYS_CARD_RECOMMENDAPP: {
        target: "http://localhost:9006",
        pathRewrite: { "/SYS_CARD_RECOMMENDAPP": "" },
      },
      SYS_CARD_RECUSEAPP: {
        target: "http://localhost:9007",
        pathRewrite: { "/SYS_CARD_RECUSEAPP": "" },
      },
      SYS_CARD_SEARCHRESULTS: {
        target: "http://localhost:6002",
        pathRewrite: { "/SYS_CARD_SEARCHRESULTS": "" },
      },
      SYS_CARD_SERVICEBUS: {
        target: "http://localhost:6003",
        pathRewrite: { "/SYS_CARD_SERVICEBUS": "" },
      },

      SYS_CARD_TODOTASKLIST: {
        target: "http://localhost:9010",
        pathRewrite: { "/SYS_CARD_TODOTASKLIST": "" },
      },
      SYS_CARD_MYTASKLIST: {
        target: "http://localhost:9011",
        pathRewrite: { "/SYS_CARD_MYTASKLIST": "" },
      },
      SYS_CARD_DONETASKLIST: {
        target: "http://localhost:9015",
        pathRewrite: { "/SYS_CARD_DONETASKLIST": "" },
      },
      SYS_CARD_TODOTASK: {
        target: "http://localhost:7000",
        pathRewrite: { "/SYS_CARD_TODOTASK": "" },
      },
      SYS_CARD_MYTASK: {
        target: "http://localhost:7001",
        pathRewrite: { "/SYS_CARD_MYTASK": "" },
      },
      SYS_CARD_DONETASK: {
        target: "http://localhost:7012",
        pathRewrite: { "/SYS_CARD_DONETASK": "" },
      },

      SYS_CARD_STATISTICS: {
        target: "http://localhost:5014",
        pathRewrite: { "/SYS_CARD_STATISTICS": "" },
      },
      SYS_CARD_MYOFFICE: {
        target: "http://localhost:8103",
        pathRewrite: { "/SYS_CARD_MYOFFICE": "" },
      },
      SYS_CARD_PROCESSINGPROGRESS: {
        target: "http://localhost:8104",
        pathRewrite: { "/SYS_CARD_PROCESSINGPROGRESS": "" },
      },

      SYS_CARD_PRODUCTSCHOOLCALENDAR: {
        target: "http://localhost:9008",
        pathRewrite: { "/SYS_CARD_PRODUCTSCHOOLCALENDAR": "" },
      },
      SYS_CARD_PERSONALDATA: {
        target: "http://localhost:5555",
        pathRewrite: { "/SYS_CARD_PERSONALDATA": "" },
      },
      SYS_CARD_CALENDAR: {
        target: "http://localhost:9016",
        pathRewrite: { "/SYS_CARD_CALENDAR": "" },
      },
      SYS_CARD_RICHTEXT: {
        target: "http://localhost:9017",
        pathRewrite: { "/SYS_CARD_RICHTEXT": "" },
      },
      SYS_CARD_CALENDAR_DETAIL: {
        target: "http://localhost:9018",
        pathRewrite: { "/SYS_CARD_CALENDAR_DETAIL": "" },
      },
      SYS_CARD_RECENTSERVICEITEMS: {
        target: "http://localhost:9019",
        pathRewrite: { "/SYS_CARD_RECENTSERVICEITEMS": "" }, // 最近使用事项
      },
      SYS_CARD_LATESTSERVICEITEMS: {
        target: "http://localhost:9020",
        pathRewrite: { "/SYS_CARD_LATESTSERVICEITEMS": "" }, // 最新事项
      },
      SYS_CARD_MYAPP: {
        target: "http://localhost:9021",
        pathRewrite: { "/SYS_CARD_MYAPP": "" }, // 我的服务
      },
      SYS_CARD_ALLONETHING: {
        target: "http://localhost:5050",
        pathRewrite: { "/SYS_CARD_ALLONETHING": "" }, // 全部一件事
      },
      SYS_CARD_DETAILOFONETHING: {
        target: "http://localhost:5051",
        pathRewrite: { "/SYS_CARD_DETAILOFONETHING": "" }, // 一件事详情
      },
      SYS_CARD_MYFAVOURATE_ONETHING: {
        target: "http://localhost:5052",
        pathRewrite: { "/SYS_CARD_MYFAVOURATE_ONETHING": "" }, // 一件事详情
      },
      SYS_CARD_MYFAVORITEMESSAGE: {
        target: "http://localhost:5053",
        pathRewrite: { "/SYS_CARD_MYFAVORITEMESSAGE": "" }, // 我收藏的消息
      },
      SYS_CARD_MYFAVORITE_TASK: {
        target: "http://localhost:5054",
        pathRewrite: { "/SYS_CARD_MYFAVORITE_TASK": "" }, // 我收藏的任务
      },
    },
  },
};
