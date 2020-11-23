# **rxJava + retrofit + okHttp网络请求封装  图片加载轻度封装**

## 介绍
    
## 使用说明
###1.添加网络请求权限
   <uses-permission android:name="android.permission.INTERNET" />
   
###2.证书设置
    android P及以上 需要进行正式证书设置 在XML目录下新增network_security_config文件，并在application标签中配置
    否则会有异常：java.net.UnknownServiceException: CLEARTEXT communication to xx.xxx.xxx.xxx not permitted by network security policy 
    
    <application
           android:allowBackup="true"
           android:icon="@mipmap/ic_launcher"
           android:label="@string/app_name"
           android:networkSecurityConfig="@xml/network_security_config"
           
    network_security_config 文件内容如下 
        <?xml version="1.0" encoding="utf-8"?>
        <network-security-config>
            <base-config cleartextTrafficPermitted="true" />
        </network-security-config>

###3.配置 NetWorkConfigs
   主要配置 baseUrl 和 Authorization的值，其他参数可使用默认设置值
   
###4.请求使用示例
    fun testPost_RxRetrofit() {
            NetWorkManager.instance
                .buildRequest(RequestInterfaceTest::class.java)
                .login("闻风", "buzhidao")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NetWorkObserver<BaseResponse<loginInfo>>() {
                    override fun onNext(response: BaseResponse<loginInfo>) {
                    }
                })
    }
    interface RequestInterfaceTest {
        //以表单方式提交
        @POST("lingxi-test/user/login")
        fun login(
            @Query("username") username: String,
            @Query("password") password: String
        ): Observable<BaseResponse<loginInfo>>
    }
    data class loginInfo(val id: String, val username: String, val token: String) {}

#### 纯retrofit请求方式
    fun testPostRetrofit() {
        var request: RequestInterface = retrofit!!.create(RequestInterface::class.java)
        var call: Call<RxResponse<LoginInfo>> = request.test_retrofit_login("闻风", "buzhidao")
        call.enqueue(object : Callback<RxResponse<LoginInfo>> {
            override fun onResponse(
                call: Call<RxResponse<LoginInfo>>?,
                response: Response<RxResponse<LoginInfo>>
            ) {
            }
    
            override fun onFailure(call: Call<RxResponse<LoginInfo>>, t: Throwable?) {
                KLog.d(Log.getStackTraceString(t))
            }
        })
    }
## 图片加载使用