# 代码模板——制造业销售下单搬上钉钉

## 1. 背景介绍

制造业企业下达销售订单是企业营销业务当中最具分量的一环，通过结合钉钉业务移动化、在线化的能力，在钉钉宜搭低代码上实现了销售订单的下达，驱动了企业各项业务的行动执行，从而完成公司获利的目标。

## 2. 项目结构

> **fronted**：前端模块，使用vue构建，主要功能有：接入钉钉JSAPI获取authCode、获取并保存用户信息、展示表单数据、数据录入页面等。
>
> **backend**：后端模块，使用springboot构建，主要功能有：获取access_token、获取用户信息等、查询宜搭表单实例数据、新增宜搭表单实例、删除宜搭表单实例等。

以下为目录结构与部分核心代码文件：

```
.
├── README.md     ----- 说明文档
├── backend
│   ├── dingBoot-linux.sh     ----- 启动脚本（linux）
│   ├── dingBoot-mac.sh    ----- 启动脚本（mac）
│   ├── dingBoot-windows.bat     ----- 启动脚本（windows）
│   ├── pom.xml
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── aliyun
│           │           └── dingtalk
│           │               ├── Application.java     ----- 启动类
│           │               ├── config
│           │               │   └── AppConfig.java     ----- 应用配置类
│           │               ├── controller
│           │               │   └──  BizController.java     ----- 核心业务接口
│           │               └── service
│           │                   └── BizServiceImpl.java     ----- 核心业务代码
│           └── resources
│               └── application.yml     ----- 应用配置文件
├── fronted
│   └── src     ----- 前端展示页面和交互代码
└── pom.xml
```

## 3. 开发环境准备

1. 需要有一个钉钉注册企业，如果没有可以创建：https://oa.dingtalk.com/register_new.htm#/

2. 成为钉钉开发者，参考文档：https://open.dingtalk.com/document/org/become-a-dingtalk-developer

3. 登录钉钉开放平台后台创建一个H5应用： https://open-dev.dingtalk.com/#/index

4. 配置应用：

   ① 配置开发管理，参考文档：https://open.dingtalk.com/document/org/configure-orgapp

   此处配置“应用首页地址”需公网地址，若无公网ip，可使用钉钉内网穿透工具： https://open.dingtalk.com/document/resourcedownload/http-intranet-penetration

   ![img](https://img.alicdn.com/imgextra/i4/O1CN01QGY87t1lOZN65XHqR_!!6000000004809-2-tps-2870-1070.png)

   ② 配置相关权限，参考文档：https://open.dingtalk.com/document/orgapp-server/address-book-permissions

   本demo使用接口权限：

   -  “通讯录部门成员读权限” 
   -  “成员信息读权限”
   -  “宜搭表单数据读权限”
   -  “宜搭表单数据写权限”

   ![img](https://img.alicdn.com/imgextra/i2/O1CN01n0QZM321k7rcBwfsr_!!6000000007022-2-tps-2822-1080.png)

   

5. 启用宜搭模板“移动销售”（必要步骤）：https://www.aliwork.com/newApp.html#/template/TPL_U6P1SUHL6AQ5C5RRFQ4T?_k=ufv7k4

   ![](https://img.alicdn.com/imgextra/i4/O1CN01TXBNnj1pG6YPVkfVE_!!6000000005332-2-tps-1197-614.png)

## 4. 启动项目

推荐使用ide来启动和调试。

1、下载项目

```shell
git clone https://github.com/open-dingtalk/h5app-manufacturing-sales-system-demo.git
```

2、 配置应用参数

获取到以下参数，修改后端application.yaml

```yaml
server:
		port:  ${port} 
dingTalk:
		appKey:      ${appKey}   
		appSecret:   ${appSecret}   
    agentId:     ${agentId}   
    corpId:      ${corpId}
yida:
  # 宜搭的应用编码
  appCode: ${appCode}
  # 宜搭的应用密钥
  appSecretKey: ${appSecretKey}
  # 模板应用中订货单表单id
  orderFormId: ${orderFormId}
```

参数获取方法：登录开发者后台

- port自行设置

- 获取corpId——开发者后台首页：https://open-dev.dingtalk.com/#/index ![](https://img.alicdn.com/imgextra/i2/O1CN01amtWue1l5nAYRc2hd_!!6000000004768-2-tps-1414-321.png)

- 获取appKey、appSecret、agentId——开发者后台 -> 应用开发 -> 企业内部开发 -> 进入应用 -> 基础信息 

  ![](https://img.alicdn.com/imgextra/i3/O1CN01Rpfg001aSjEIczA85_!!6000000003329-2-tps-905-464.png)

- 获取宜搭应用参数appCode、appSecretKey和orderFormId（需先启用模板，参考上方启用步骤）

  - 进入宜搭，在“我的应用”中找到启动的移动销售应用，进入应用

    ![](https://img.alicdn.com/imgextra/i3/O1CN01S2sLhK20ZlKZPsg4N_!!6000000006864-2-tps-1232-886.png)

  - 进入应用后，点击应用设置，点击左侧部署运维，获取相应的参数，本demo操作的表单为“订货单”

  ![](https://img.alicdn.com/imgextra/i2/O1CN01WYxnoa1mc8lasUach_!!6000000004974-2-tps-1440-689.png)

3、修改前端页面（**非必要步骤**）

此demo中，前端项目已经编译成静态资源加入到后端模块中，可直接启动体验，如不修改页面，此步骤可跳过。

① 前端开发环境：

如需修改前端页面，请安装node.js环境，参考文档：https://help.aliyun.com/document_detail/139207.html

② 编译命令：

修改完页面后，命令行中（前端项目目录下）执行以下命令，编译打包生成静态资源文件

```shell
npm install
npm run build
```

③ 将编译好的静态资源放入后端：

![image-20210706173224172](https://img.alicdn.com/imgextra/i2/O1CN01QLp1Qw1TCVrPddfjZ_!!6000000002346-2-tps-322-521.png)

4、启动项目

- 启动SpringBoot（运行启动类Application.java）

- 启动内网穿透工具
    - 钉钉内网穿透工具，下载地址：https://open.dingtalk.com/document/resourcedownload/http-intranet-penetration
    - 配置应用访问地址、发布应用：
    
    ① **配置访问地址**
    
    脚本启动后会自动生成临时域名，配置方法：复制域名 -> 进入开发者后台 -> 进入应用 -> 开发管理 -> 粘贴到“应用首页地址”、“PC端首页地址”
    
    - 生成的域名： ![](https://img.alicdn.com/imgextra/i3/O1CN01lN8Myr1XIFJmlDSWf_!!6000000002900-2-tps-898-510.png)
    
    - 配置地址： ![](https://img.alicdn.com/imgextra/i1/O1CN01IWleEp1Kw0hX9suby_!!6000000001227-2-tps-1408-489.png)
    
    ② **发布应用**
    
    配置好地址后进入“版本管理与发布页面”，发布应用，发布后即可在PC钉钉或移动钉钉工作台访问应用
    
    - 发布应用： ![](https://img.alicdn.com/imgextra/i4/O1CN01DTtp4E1qAtfDeGORj_!!6000000005456-2-tps-1414-479.png)
    
- 发布后，在移动端/PC端钉钉点击工作台，找到应用，进入应用体验demo

## 5. 页面展示

订货单列表

![](https://img.alicdn.com/imgextra/i3/O1CN0150q3oX1tK0OGReVUR_!!6000000005882-2-tps-700-407.png)

新增订货单

![](https://img.alicdn.com/imgextra/i2/O1CN01w0oPAM1XnOFxwQkRm_!!6000000002968-2-tps-700-398.png)

宜搭应用查看订货单数据

![](https://img.alicdn.com/imgextra/i1/O1CN0143TimO1ZX1KgHfweJ_!!6000000003203-2-tps-700-335.png)


## 6. 参考文档

1. 获取企业内部应用access_token，文档链接：https://open.dingtalk.com/document/orgapp-server/obtain-orgapp-token
2. 通过免登码获取用户信息，文档链接：https://open.dingtalk.com/document/orgapp-server/obtain-the-userid-of-a-user-by-using-the-log-free
3. 通过userid获取用户详情，文档链接：https://open.dingtalk.com/document/orgapp-server/query-user-details
4. 查询表单实例数据，文档链接：https://open.dingtalk.com/document/isvapp-server/querying-form-instance-data
5. 保存表单数据，文档链接：https://open.dingtalk.com/document/orgapp-server/save-form-data
6. 删除表单数据，文档链接：https://open.dingtalk.com/document/orgapp-server/delete-form-data

## 7.免责声明

- 此代码示例仅用于学习参考，请勿直接用作生产线上代码，如线上使用出现故障损失，后果自行承担。
- 此示例中“内网穿透工具”仅用于开发测试阶段，请勿用于生成线上环境，如出现稳定性问题，后果自行承担。
