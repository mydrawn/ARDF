package com.mydrawn.lib_network.example

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mydrawn.lib_network.NetWorkManager
import com.mydrawn.lib_network.bean.BaseResponse
import com.mydrawn.lib_network.selfRxjava.NetWorkObserver
import com.mydrawn.lib_network.slefGlide.GlideUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

class ExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//     //   setContentView(R.layout.activity_main)
//        var imgrul = "https://pic2.52pk.com/files/201025/7914002_104026_1.png"
//        GlideUtil.show(this, imgrul, findViewById<ImageView>(R.id.imageView))
//        GlideUtil.showCircle(this, imgrul, findViewById<ImageView>(R.id.imageView1))
//        GlideUtil.showRadius(this, imgrul, 30, findViewById<ImageView>(R.id.imageView2))
//        GlideUtil.showBlurTrans(this, imgrul, findViewById<ImageView>(R.id.imageView3), 25, 1)
//        GlideUtil.showCircleBlurTrans(
//            this,
//            imgrul,
//            findViewById<ImageView>(R.id.imageView4),
//            25,
//            1,
//            30
//        )
    }

    fun testPost_RxRetrofit() {
//        NetWorkManager.instance
//            .buildRequest(RequestInterfaceTest::class.java)gaos
//            .login("闻风", "buzhidao")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : NetWorkObserver<BaseResponse<loginInfo>>() {
//                override fun onNext(response: BaseResponse<loginInfo>) {
//                }
//            })

//        NetWorkManager.instance
//            .buildRequest(RequestInterfaceTest::class.java)
//            .login(loginRequestBody("闻风", "buzhidao"))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : NetWorkObserver<BaseResponse<loginInfo>>() {
//                override fun onNext(response: BaseResponse<loginInfo>) {
//                    super.onNext(response)
//                }
//
//                override fun onError(e: Throwable) {
//                    super.onError(e)
//                }
//            })

        val map: MutableMap<String, String> =
            HashMap()
        map["channelId"] = "89781cf0-ca31-4913-8c93-4c93e8e74f01"
        NetWorkManager.instance
            .buildRequest(RequestInterfaceTest::class.java)
            .getChannelIdExist(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetWorkObserver<BaseResponse<String>>() {
                override fun onNext(response: BaseResponse<String>) {
                    super.onNext(response)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
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

        @POST("lingxi-test/user/login")
        fun login(
            @Body requestBody: loginRequestBody
        ): Observable<BaseResponse<loginInfo>>


        @POST("http://175.6.97.174:8787/accurateCall/getChannelIdExist")
        fun getChannelIdExist(@Body maps: Map<String, String>): Observable<BaseResponse<String>>
    }

    data class loginInfo(val id: String, val username: String, val token: String) {

    }

    data class loginRequestBody(val username: String, val password: String) {

    }

    fun start_rquest(view: View) {
        testPost_RxRetrofit()
    }
}