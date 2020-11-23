package com.mydrawn.lib_network.slefGlide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mydrawn.lib_network.R
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * Author:drawn
 * Description: Glide工具类
 * date:2020/11/23
 */
object GlideUtil {
    /**
     * 加载原始图片
     */
    fun show(
        context: Context,
        url: String,
        imageView: ImageView
    ) {
        Glide.with(context)
            .load(url)
            .error(R.drawable.network_default)
            .placeholder(R.drawable.network_default)
            .into(imageView)
    }

    /**
     * 加载高斯模糊
     * 设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”
     */
    fun showBlurTrans(
        context: Context,
        url: String,
        imageView: ImageView,
        radio: Int,
        sampling: Int
    ) {
        val options =
            RequestOptions.bitmapTransform(BlurTransformation(radio, sampling))
        Glide.with(context)
            .load(url)
            .apply(options)
            .error(R.drawable.network_default)
            .placeholder(R.drawable.network_default)
            .into(imageView)
    }

    /**
     * 显示圆形图片
     */
    fun showCircle(
        context: Context,
        url: String,
        imageView: ImageView
    ) {
        Glide.with(context!!)
            .load(url)
            .circleCrop()
            .error(R.drawable.network_default_circle)
            .placeholder(R.drawable.network_default_circle)
            .into(imageView)
    }

    /**
     * 显示圆角图片
     * tip:radius是px，注意单位转换
     */
    fun showRadius(
        context: Context,
        url: String,
        radius: Int,
        imageView: ImageView
    ) {

        val options = RequestOptions.bitmapTransform(RoundedCorners(radius))
        Glide.with(context)
            .load(url)
            .apply(options)
            .error(R.drawable.network_default)
            .placeholder(R.drawable.network_default)
            .into(imageView)
    }
}