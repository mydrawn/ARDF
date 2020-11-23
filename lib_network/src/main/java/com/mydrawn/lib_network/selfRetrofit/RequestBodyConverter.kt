package com.mydrawn.lib_network.selfRetrofit

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.mydrawn.lib_network.NetLogUtil
import com.mydrawn.lib_network.NetWorkConfigs
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.Charset

/**
 * Author:drawn
 * Description: RequestBody请求头
 * date:2020/11/20
 */
class RequestBodyConverter<T> internal constructor(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
) :
    Converter<T, RequestBody> {

    companion object {
        private val MEDIA_TYPE: MediaType? =
            ("application/json;charset=utf-8").toMediaTypeOrNull()
        private val UTF_8 = Charset.forName("UTF-8")
    }

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer: Writer = OutputStreamWriter(
            buffer.outputStream(),
            UTF_8
        )
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return buffer.readByteString()
            .toRequestBody(MEDIA_TYPE)
    }
}