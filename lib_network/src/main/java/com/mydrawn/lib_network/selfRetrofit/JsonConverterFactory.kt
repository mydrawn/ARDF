package com.mydrawn.lib_network.selfRetrofit

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 * Author:drawn
 * Description: 自定义转换器，拦截请求和响应数据，自行转换，添加log或者加解密等 操作
 * date:2020/11/23
 */
class JsonConverterFactory private constructor(gson: Gson?) :
    Converter.Factory() {
    private val mGson: Gson

    init {
        if (gson == null) {
            throw NullPointerException("gson == null")
        }
        this.mGson = gson
    }

    override fun responseBodyConverter(
        type: Type?, annotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *> {
        val adapter: TypeAdapter<*> = mGson.getAdapter(TypeToken.get(type))
        return ResponseBodyConverter(mGson, adapter)
    }

    override fun requestBodyConverter(
        type: Type?,
        parameterAnnotations: Array<Annotation?>?,
        methodAnnotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<*, RequestBody> {
        val adapter: TypeAdapter<*> = mGson.getAdapter(TypeToken.get(type))
        return RequestBodyConverter(mGson, adapter)
    }

    companion object {
        @JvmOverloads
        fun create(gson: Gson? = Gson()): JsonConverterFactory {
            return JsonConverterFactory(
                gson
            )
        }
    }

}