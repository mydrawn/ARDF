package com.mydrawn.lib_network

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mydrawn.lib_network.bean.BaseResponse
import com.mydrawn.lib_network.selfRxjava.NetWorkObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.mydrawn.lib_network.test", appContext.packageName)
    }

    @Test
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

    data class loginInfo(val id: String, val username: String, val token: String) {

    }

}