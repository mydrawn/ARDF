# **基于rxJava2 + retrofit2 + okHttp3 + Glide4 网络请求封装**

## 介绍
    该库主要是负责网络请求的库，包括了常用的网络请求和图片加载，旨在打造一个易用快速的网络封装库
	基于rxJava2 + retrofit2 + okHttp3 封装实现网络请求，配置化的封装方式，简易的日志打印方式，更加易用
	Glide4的简单封装，一行代码进行加载网络图片
## 使用说明
### 1.添加权限
   	网络权限
	<uses-permission android:name="android.permission.INTERNET" />
	加载本地图片，记得加上SD卡读取权限

### 2.证书设置
    android P及以上使用非HttpS请求，需要进行证书设置.
	否则会有异常：java.net.UnknownServiceException: CLEARTEXT communication to xx.xxx.xxx.xxx not permitted by network security policy 
	
	在XML目录下新增 network_security_config文件，并在application标签中配置
    <application
           android:allowBackup="true"
           android:icon="@mipmap/ic_launcher"
           android:label="@string/app_name"
           android:networkSecurityConfig="@xml/network_security_config"
		   ...
           
    network_security_config 文件内容如下 
        <?xml version="1.0" encoding="utf-8"?>
        <network-security-config>
            <base-config cleartextTrafficPermitted="true" />
        </network-security-config>

### 3.初始化配置 
	配置项的值都统一放在NetWorkConfigs类中，一般情况都使用默认设置值
	网络请求根地址NetWorkConfigs.baseUrl 和 Token NetWorkConfigs.Authorization需要手动配置
        a.baseUrl 建议使用buildConfig的方式配置,如下
			 productFlavors {
        		dev {
            		buildConfigField "String", "BASE_URL", "\"https://XXXX\""
        		}
       		 	local {
           		 	buildConfigField "String", "BASE_URL", "\"https://XXXX\""
        	 	}
       		 	product {
            		buildConfigField "String", "BASE_URL", "\"https://XXXX\""
        		}
    		 }
		 NetWorkConfigs.baseUrl = BuildConfig.BASE_URL

        b.Authorization需要调用NetWorkManager类中initCacheToken()中初始化token值（建议在application初始化中调用）
		c.没有缓存token值情况下，需要获取token值后，调用 NetWorkManager类中refreshToken()刷新token值
   
### 4.请求使用示例
#### rxJava + retrofit请求方式
    fun testPost_RxRetrofit() {
		NetWorkManager.instance
        	.buildRequest(RequestInterfaceTest::class.java)
            .login("XX", "XXXX")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetWorkObserver<BaseResponse<loginInfo>>() {
                override fun onNext(response: BaseResponse<loginInfo>) {
					 super.onError(e)
					//do something
            	}
                override fun onError(e: Throwable) {
                    super.onError(e)
					//do something
                }
         	})
    }
	//retrofit 请求接口
    interface RequestInterfaceTest {
        //以表单方式提交
        @POST("lingxi-test/user/login")
        fun login(
            @Query("username") username: String,
            @Query("password") password: String
        ): Observable<BaseResponse<loginInfo>>
    }
	//实体类
    data class loginInfo(val id: String, val username: String, val token: String) {}

#### 纯retrofit请求方式
    fun testPostRetrofit() {
        var request: RequestInterface = retrofit!!.create(RequestInterface::class.java)
        var call: Call<RxResponse<LoginInfo>> = request.test_retrofit_login("XX", "XXXX")
        call.enqueue(object : Callback<RxResponse<LoginInfo>> {
            override fun onResponse(
                call: Call<RxResponse<LoginInfo>>?,
                response: Response<RxResponse<LoginInfo>>
            ) {
				//do something
            }
    
            override fun onFailure(call: Call<RxResponse<LoginInfo>>, t: Throwable?) {
                //do something
            }
        })
    }
## 图片加载使用
	GlideUtil.show(this, imgrul, imgView) //普通加载
    GlideUtil.showCircle(this, imgrul, imgView)//加载为圆形
    GlideUtil.showRadius(this, imgrul,30 ,imgView)//加载为圆角
    GlideUtil.showBlurTrans(this, imgrul, imgView,25,1) //加载为高斯模糊
    GlideUtil.showCircleBlurTrans(this, imgrul, imgView,25,1,30)//加载为圆角高斯模糊

	自定义缓存参数设置可以通过AppGlideModule的方式进行，详见CustomGlideModule类

##注意
	使用了config.gradle方式管理依赖包，如果单独使用lib_netWork网络请求包的话，需要修改该库build.gradle文件依赖
