# **Base 库**

## 介绍
    该库主要是一些基础库和一些库使用封装
	a.一些base基类
	b.常用三方库的依赖封装
##  详细列表
### 1. Base包：基类包
+ BaseApplication：非MultiDex Application基类
+ BaseMultiDexApplication：MultiDex Application基类
+ ApplicationProxy：BaseApplication和BaseMultiDexApplication代理类，实际业务实现
+ BaseActivity：activity 基类
+ IActivity：activity中新增拓展方法抽象类

### 2. EventBus包：基于RxJava封装的事件总线
+ 接收事件：BaseActivity中已经封装，需要接受Event事件，在Activity中直接调用registerRxEvent()注册监听，然后覆盖父类的onRxEventHandle方法即可接受事件

		override fun onCreate(savedInstanceState: Bundle?) {
			super.onCreate(savedInstanceState)
			setContentView(R.layout.activity_main)
			//注册事件总线
			registerRxEvent()
			...
		}

	    override fun onRxEventHandle(rxEvent: RxEvent<*>) {
	        when (rxEvent.eventType) {
	            RxEventType.TEST -> {
					//处理对应事件
	            }
	        }
	    }

+ 发送事件
		直接调BaseActivity的封装方法,注意要反注册，在BaseActivity中已经封装自动反注册了.

		 postRxEvent(RxEvent(RxEventType.TEST,"onResume"))

+ 在其他地方使用，注意要反注册.以上是基于BaseActivity的封装，也可以直接使用，例如：

        //注册事件监听
        var compositeDisposable = RxBus.registerRxEvent(object :RxBus.RxEventHandle{
            override fun onRxEventHandle(rxEvent: RxEvent<*>) {
                RxEventType.TEST -> {
					//处理对应事件
	            }
            }
        })
        //取消事件监听
        RxBus.unregisterRxEvent(compositeDisposable)
### 3. Log包：日志封装库

	支持行号、调用类名打印
	LogUtils.d("XXXX")
	LogUtils.d("TAG","XXXX")
	例如：
		2020-12-15 09:16:00.296 15959-15959/com.mydrawn.ARDF D/ARDF_: MainActivity:line-21<->: XXXX
		2020-12-15 09:16:00.296 15959-15959/com.mydrawn.ARDF D/ARDF_: TAG : MainActivity:line-22<->: XXXX

### 4. 权限申请：permissionX 开源库
		PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
                scope.showRequestReasonDialog(deniedList, "为了保证程序正常工作，请您同意以下权限申请", "我已明白")

            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                } else {
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT)
                }
            }

### 5. 模块化支持：阿里ARouter
	使用见：https://github.com/alibaba/ARouter/blob/master/README_CN.md

### 6. 屏幕适配：autosize
	使用见：https://codechina.csdn.net/mirrors/jessyancoding/androidautosize?utm_source=csdn_github_accelerator

### 7. 三方webView：AgentWeb
	使用见：https://www.jianshu.com/p/789f3a473c67
	        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(common_ll_web_js as LinearLayout, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebViewClient(object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }


                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }
            })
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(
                    view: WebView,
                    title: String
                ) {
                    super.onReceivedTitle(view, title)
                }
            })
            .createAgentWeb()
            .ready()
            .go("http://XXXXXX")
        //加载js方法
        mAgentWeb.jsInterfaceHolder.addJavaObject("android", AndroidInterface(mAgentWeb, this))
        //调用js方法
        mAgentWeb.jsAccessEntrace.quickCallJs("getNativeSetUptheData", paramsStr)

		class SystemUIInterface constructor(var mAgent: AgentWeb, var mContext: Context) {
	    	@JavascriptInterface
	    	fun postMessage() {}
	    }