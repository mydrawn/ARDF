package com.mydrawn.lib_network.selfRetrofit

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.mydrawn.lib_network.NetLogUtil
import com.mydrawn.lib_network.ResponseApiException
import com.mydrawn.lib_network.bean.BaseResponse
import com.mydrawn.lib_network.constants.ResponseCode
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

/**
 * Author:drawn
 * Description:解析ResponseBody
 *      打印响应数据
 *      抛出非成功异常
 *      数据解密操作等
 * date:2020/11/20
 */
class ResponseBodyConverter<T> internal constructor(
    private val mGson: Gson,
    private val adapter: TypeAdapter<T>
) :
    Converter<ResponseBody, T> {
    @Throws(IOException::class, ResponseApiException::class)
    override fun convert(value: ResponseBody): T {
        val response = value.string()

        //打印Json格式响应数据
        NetLogUtil.printJson(response, "response")

        val rxResponse: BaseResponse<T> =
            mGson.fromJson(response, BaseResponse::class.java) as BaseResponse<T>

        //响应code不是约定的成功状态，则抛出异常，
        if (rxResponse.code.toInt() != ResponseCode.RESPONSE_CODE_200) {
            value.close()
            throw ResponseApiException(rxResponse.code.toInt(), rxResponse.msg)
        }

        return value.use { _ ->
            adapter.fromJson(response)
        }
    }
}