package com.mydrawn.lib_network.slefGlide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.mydrawn.lib_network.NetWorkManager
import java.io.File
import java.io.InputStream


/**
 * Author:drawn
 * Description: Glide 配置类
 * date:2020/11/20
 */
@GlideModule
class CustomGlideModule : AppGlideModule() {

    /**
     *  缓存配置
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        var diskCacheSize: Int = 250 * 1024 * 1024
        var diskCacheFolder: File = File("")

        //磁盘缓存配置（默认缓存大小250M，默认保存在内部存储中 ）
        // sdcard/Android/包名/cache/image_manager_disk_cache
        //builder.setDiskCache(ExternalCacheDiskCacheFactory(context, diskCacheSize))

        //设置指定目录的磁盘缓存，指定缓存大小
//        builder.setDiskCache(DiskLruCacheFactory(object : DiskLruCacheFactory.CacheDirectoryGetter {
//            override fun getCacheDirectory(): File {
//                return diskCacheFolder
//            }
//        }), diskCacheSize)


        //内存缓存配置（不建议配置，Glide会自动根据手机配置进行分配）
//        builder.setMemoryCache(LruResourceCache(memoryCacheSize))

        //设置Bitmap池大小（不建议配置，Glide会自动根据手机配置进行分配）
//        builder.setBitmapPool(LruBitmapPool(bitmapPoolSize))
    }

    override fun registerComponents(
        context: Context,
        glide: Glide,
        registry: Registry
    ) {
        //替换网络请求组件
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(NetWorkManager.instance.getOkHttpClient())
        )
    }

    /**
     * 禁止清单解析Module，避免Module重复加载
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}