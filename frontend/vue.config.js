module.exports={
    //基本路径，vue.cli 3.3以前请使用baseUrl
    publicPath:'',
    //输出文件目录
    outputDir:'dist',
    //用于嵌套生产的静态资产（js,css,img,fonts）的目录
    assetsDir:'',
    //生产环境sourceMap
    productionSourceMap:true,
    //webpack配置
    configureWebpack: () => {},
    chainWebpack: () => {},
    //css相关配置
    css:{
        //启用长沙市modulesmodules
        modules:false,
        //是否启用css分离插件
        extract:true,
        //开启css source maps
        sourceMap:false,
        //css预设器配置项
        loaderOptions:{},
    },
    //webpack-dev-server相关配置
    devServer:{
      host: '0.0.0.0',
      port:8080,
      proxy: {
        // 设置代理
        ['/api']: {
          target: "http://172.18.0.131:8080",
          autoRewrite: true,
          changeOrigin: true,
          cookieDomainRewrite: {
            "*": "",
          },
          pathRewrite: {
            ["^" + '/api']: "",
          },
        },
      },
    },
    //第三方插件配置
    pluginOptions:{
        //.......
    }
}