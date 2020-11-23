package com.mydrawn.lib_network

import com.mydrawn.lib_network.interceptor.CommonInterceptor
import com.mydrawn.lib_network.interceptor.HeadUrlInterceptor
import com.mydrawn.lib_network.selfRetrofit.JsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * Author:juhua
 * Description: okHttpClient,retrofit 初始化和配置
 * date:2020/6/13
 */
class NetWorkManager {

    companion object {
        val instance: NetWorkManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetWorkManager()
        }
    }

    private var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    constructor() {
        initOkHttpClient()
        initRetrofit()
    }

    fun <T> buildRequest(service: Class<T>): T {
        return instance!!.retrofit!!.create(service)
    }

    fun getOkHttpClient(): OkHttpClient {
        initOkHttpClient()
        return okHttpClient!!
    }

    private fun initOkHttpClient() {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(HeadUrlInterceptor())
            .addInterceptor(CommonInterceptor())
            .sslSocketFactory(getSSLSocketFactory()!!, TrustAllManager)
            .connectTimeout(NetWorkConfigs.connectTimeout, TimeUnit.SECONDS)
            .readTimeout(NetWorkConfigs.readTimeout, TimeUnit.SECONDS)
            .writeTimeout(NetWorkConfigs.writeTimeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(NetWorkConfigs.retryOnConnectionFailure)//错误重连
            .build()
    }

    private fun initRetrofit() {
        // 初始化Retrofit
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NetWorkConfigs.baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //Rx Java Observable支持
            .addConverterFactory(JsonConverterFactory.create()) //自定义转换处理
            .build()
    }

    private var TrustAllManager = object : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(
            chain: Array<X509Certificate>,
            authType: String
        ) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(
            chain: Array<X509Certificate>,
            authType: String
        ) {
            //验证证书
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }

    private fun getSSLSocketFactory(): SSLSocketFactory? {
        //创建一个不验证证书链的证书信任管理器。
        val trustAllCerts =
            arrayOf<TrustManager>(TrustAllManager)
        val sslContext: SSLContext
        try {
            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(
                null, trustAllCerts,
                SecureRandom()
            )
            return sslContext
                .socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}