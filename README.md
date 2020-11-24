# ARDF：易用的Android快速开发框架
 Android rapid development framework (PS：蹩脚英语勿怪)

##简介
	旨在打造一个快速易用的封装开发框库。
	提供网络数据加载、图片加载、视频录制播放、音乐播放、常用UI控件，res shape资源、Log日志手机、三方登录、三方分享、三方支付等等快速集成使用框架（ps:慢慢添加完善中，这里只是规划要添加的快速开发库和封装）
	各个功能模块库基于lib_XXX方式命名，并在module库下提供详细指导使用的markDownn文件，所以使用方式请参考lib_XXX下的markDownn文件

##模块介绍
###**app**
	壳模块
###**lib_base**
	基类模块，主要放基类文件，和一些常用基础依赖
###**lib_network**
	基于rxJava2 + retrofit2 + okHttp3 + Glide4 网络请求封装，提供网络请求、图片加载快速集成