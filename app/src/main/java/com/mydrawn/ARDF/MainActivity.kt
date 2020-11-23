package com.mydrawn.ARDF

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mydrawn.lib_network.NetWorkManager
import com.mydrawn.lib_network.bean.BaseResponse
import com.mydrawn.lib_network.selfRxjava.NetWorkObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun testPost_RxRetrofit() {
//        NetWorkManager.instance
//            .buildRequest(RequestInterfaceTest::class.java)
//            .login("闻风", "buzhidao")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : NetWorkObserver<BaseResponse<loginInfo>>() {
//                override fun onNext(response: BaseResponse<loginInfo>) {
//                }
//            })

        NetWorkManager.instance
            .buildRequest(RequestInterfaceTest::class.java)
            .login(loginRequestBody("闻风", "buzhidao"))
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

        @POST("lingxi-test/user/login")
        fun login(
            @Body requestBody: loginRequestBody
        ): Observable<BaseResponse<loginInfo>>
    }

    data class loginInfo(val id: String, val username: String, val token: String) {

    }
    data class loginRequestBody(val username: String, val password: String) {

    }

    fun start_rquest(view: View) {
        testPost_RxRetrofit()
    }
}