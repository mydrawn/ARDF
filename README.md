# ARDF：易用的Android快速开发框架
 Android rapid development framework (PS：蹩脚英语勿怪)

## 一、简介
	旨在打造一个快速易用的封装开发框库。
	提供网络数据加载、图片加载、视频录制播放、音乐播放、常用UI控件，res shape资源、
	Log日志手机、三方登录、三方分享、三方支付等等快速集成使用框架（ps:慢慢添加完善中，这里只是规划要添加的快速开发库和封装）
	各个功能模块库基于lib_XXX方式命名，并在module库下提供详细指导使用的markDownn文件，所以使用方式请参考lib_XXX下的markDownn文件
## 二、工程结构介绍
    -base.gradle：公共gradle 配置，新建的module直接 apply from:'../base.gradle' 方式配置统一的公共参数
    -base_config.gradle：统一版本,依赖配置
## 三、命名规则
    参照阿里巴巴Android开发手册命名，另外是该项目额外命名规则。
###  1.libm形式的Module库(不包含具体业务逻辑，仅作为依赖用)：lib_AAA 
    AAA为库名
    资源文件命名：AAA_XXXX方式命名，module build gradle中会用 resourcePrefix "AAA"限制命名
### 2.module库(包含业务逻辑的库,通过配置可以独立编译为apk)：module_BBB
    BBB为库名
    资源文件命名：BBB_XXXX方式命名，nodule build gradle中会用 resourcePrefix "BBB"限制命名
## 四、模块介绍
### -app
	壳模块
### -lib_base
	基类模块，主要放基类文件，工具库
### -lib_network
	基于rxJava2 + retrofit2 + okHttp3 + Glide4 网络请求封装，提供网络请求、图片加载快速集成
### -module_guide
	引导业务模块，登录、注册、引导等业务功能