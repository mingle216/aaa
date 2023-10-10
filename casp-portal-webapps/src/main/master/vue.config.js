const path = require("path");

require('events').EventEmitter.defaultMaxListeners = 0; // 代理配置过多导致溢出了监听器的最大值，解除限制

const resolve = (p) => {
  return path.resolve(__dirname, p);
};
const cookie1 = (proxyReq) => {
  proxyReq.setHeader("Cookie", "COM.WISEDU.CASP.IS_RANDOM=0; COM.WISEDU.CASP.SITEWID=-100; WISCPSID=a6c89444-6679-48ec-b313-ce70a333838b; SL_G_WPT_TO=zh-CN; SL_GWPT_Show_Hide_tmp=1; SL_wptGlobTipTmp=1; MOD_AUTH_CAS=MOD_AUTH_ST-611487-8YqjZ0-nis-YuQytacSBcJN553wciapserver4; route=311d7d0ba32259752327632d8e3f592c");
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
      "/manager": {
        // target: 'http://172.17.236.65:8116',
        // target: 'http://172.31.24.34:8116',
        // target: "http://172.31.24.194:8116",
        // target: "http://172.31.24.88:8116",
        target: 'https://ehall.lzjtu.edu.cn/',
        // target: 'http://172.16.72.218:8116', // 申海霞
        // target: 'http://172.16.72.222:8116',
        // target: "http://172.16.73.128:8116",
        // target: "http://172.16.72.218:8116", // xia
        // target: 'http://172.31.24.180:8116', // 陈洪亮
        // target: 'http://172.16.34.93:8116',
        // target: 'http://172.16.72.161:8116',
        // Referer: "http://caspportal.wisedu.com",
        // target: "http://caspportal.wisedu.com",
         headers: {
        //   cookie: 'SESSION=8cbc4925-42b1-462a-8427-27a3cbd06768',
           Referer: 'https://ehall.lzjtu.edu.cn/'
         },
        // target: "http://172.16.34.144:8116",

        changeOrigin: true,
        onProxyReq: cookie1,

        pathRewrite: {
          // 去掉 路径中的  /api  的这一截
          "^/manager": "",
        },
      },
      SYS_TEMPLATE_OFFICIAL: {
        target: "http://localhost:8001",
        pathRewrite: { "/SYS_TEMPLATE_OFFICIAL": "" },
      },
      SYS_TEMPLATE_DESIGN: {
        target: "http://localhost:8002",
        pathRewrite: { "/SYS_TEMPLATE_DESIGN": "" },
      },
      // SYS_TEMPLATE_STANDARDS: {
      //   target: "http://localhost:8003",
      //   pathRewrite: { "/SYS_TEMPLATE_STANDARDS": "" },
      // },
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
      CUS_TEMPLATE_LZJTUS: {
        target: "http://localhost:8003",
        pathRewrite: { "/CUS_TEMPLATE_LZJTUS": "" },
      },
      CUS_CARD_MYGORY: {
        target: "http://localhost:6004",
        pathRewrite: { "/CUS_CARD_MYGORY": "" },
      },
      CUS_CARD_MYGORYDETAIL: {
        target: "http://localhost:7002",
        pathRewrite: { "/CUS_CARD_MYGORYDETAIL": "" },
      },
      CUS_CARD_DETAILSOFSERVICEITEMS: {
        target: "http://localhost:5001",
        pathRewrite: { "/CUS_CARD_DETAILSOFSERVICEITEMS": "" },
      },
      SYS_CARD_UNDONE: {
        target: "http://localhost:5000",
        pathRewrite: { "/SYS_CARD_UNDONE": "" },
      },
      SYS_CARD_SERVICEITEMCATEGORY: {
        target: "http://localhost:6004",
        pathRewrite: { "/SYS_CARD_SERVICEITEMCATEGORY": "" },
      },
     
      SYS_CARD_FEEDBACK: {
        target: "http://localhost:5002",
        pathRewrite: { "/SYS_CARD_FEEDBACK": "" },
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
    },
  },
};
