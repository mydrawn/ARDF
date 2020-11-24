package com.mydrawn.lib_network.slefGlide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.mydrawn.lib_network.R
import jp.wasabeef.glide.transformations.BlurTransformation


/**
 * Author:drawn
 * Description: Glide工具类
 *  tip:注意使用的值是px,注意dp的转换，这里不做统一转换
 * date:2020/11/23
 */
object GlideUtil {

    var PLACEHOLDER_ERROR = R.drawable.network_default //加载出错时显示的图片
    var PLACEHOLDER_NORMAL = R.drawable.network_default //加载过程中显示的图片
    var placeholder_circle = R.drawable.network_default_circle//加载出错时显示的圆形图片

    /**
     * 统用的加载图片
     */
    fun show(
        context: Context,
        url: String,
        imageView: ImageView
    ) {
        Glide.with(context)
            .load(url)
            .error(PLACEHOLDER_ERROR)
            .placeholder(PLACEHOLDER_NORMAL)
            .into(imageView)
    }

    /**
     * 加载高斯模糊
     * radio :模糊度(在0.0到25.0之间),默认 25
     * sampling :图片缩放比例,默认 1
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
            .error(PLACEHOLDER_ERROR)
            .placeholder(PLACEHOLDER_NORMAL)
            .into(imageView)
    }

    /**
     * 加载圆形图片
     */
    fun showCircle(
        context: Context,
        url: String,
        imageView: ImageView
    ) {
        Glide.with(context!!)
            .load(url)
            .circleCrop()
            .error(PLACEHOLDER_ERROR)
            .placeholder(placeholder_circle)
            .into(imageView)
    }

    /**
     * 加载圆角图片
     * roundingRadius:圆角值
     */
    fun showRadius(
        context: Context,
        url: String,
        roundingRadius: Int,
        imageView: ImageView
    ) {

        val options = RequestOptions.bitmapTransform(RoundedCorners(roundingRadius))
        Glide.with(context)
            .load(url)
            .apply(options)
            .error(PLACEHOLDER_ERROR)
            .placeholder(PLACEHOLDER_NORMAL)
            .into(imageView)
    }


    /**
     * 显示圆形+高斯模糊
     * radio :模糊度(在0.0到25.0之间),默认 25
     * sampling :图片缩放比例,默认 1
     * roundingRadius:圆角值
     */
    fun showCircleBlurTrans(
        context: Context,
        url: String,
        imageView: ImageView,
        radio: Int,
        sampling: Int,
        roundingRadius: Int
    ) {
        val multi = MultiTransformation(
            BlurTransformation(radio, sampling),
            RoundedCorners(roundingRadius)
        )
        val options = RequestOptions.bitmapTransform(multi)
        Glide.with(context)
            .load(url)
            .apply(options)
            .error(PLACEHOLDER_ERROR)
            .placeholder(placeholder_circle)
            .into(imageView)
    }
}