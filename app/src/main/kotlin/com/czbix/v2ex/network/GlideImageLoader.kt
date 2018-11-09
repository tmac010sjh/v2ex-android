package com.czbix.v2ex.network

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * Create by Liminhao on 2018/7/9
 * 使用Glide加载图片
 */
object GlideImageLoader {

    data class ImageConfig(var defaultDrawable: Int, var errorDrawable: Int) {
        constructor() : this(0, 0)
    }

    private var imageConfig: ImageConfig = ImageConfig()

    @JvmStatic
    fun init(default: Int, error: Int) {
        imageConfig.defaultDrawable = default
        imageConfig.errorDrawable = error
    }

    @JvmStatic
    fun loadImage(context: Context, target: ImageView, resId: Int) {
        loadImage(context = context, target = target, resId = resId, config = imageConfig)
    }

    @JvmStatic
    fun loadImage(context: Context, target: ImageView, picUrl: String) {
        loadImage(context = context, target = target, picUrl = picUrl, config = imageConfig)
    }

    @JvmStatic
    fun loadCircleImage(context: Context, target: ImageView, resId: Int) {
        loadCircleImage(context = context, target = target, resId = resId, config = imageConfig)
    }

    @JvmStatic
    fun loadCircleImage(context: Context, target: ImageView, picUrl: String) {
        loadCircleImage(context = context, target = target, picUrl = picUrl, config = imageConfig)
    }

    @JvmStatic
    fun loadImage(context: Context, target: ImageView, resId: Int, config: ImageConfig = imageConfig) {

        if (resId <= 0) return

        val options = RequestOptions().centerCrop()
                .placeholder(config.defaultDrawable)
                .error(config.errorDrawable)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.DATA)

        GlideApp.with(context).load(resId)
                .fitCenter()
                .apply(options)
                .into(target)
    }

    @JvmStatic
    fun loadCircleImage(context: Context, target: ImageView, resId: Int, config: ImageConfig = imageConfig) {
        if (resId <= 0) return

        val options = RequestOptions().centerCrop()
                .placeholder(config.defaultDrawable)
                .error(config.errorDrawable)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transform(CircleCrop())

        // 加载圆角图片，不要动画过渡
        GlideApp.with(context).load(resId)
                .fitCenter()
                .dontAnimate()
                .apply(options)
                .into(target)
    }

    @JvmStatic
    fun loadImage(context: Context, target: ImageView, picUrl: String, config: ImageConfig = imageConfig) {

//        if (TextUtils.isEmpty(picUrl)) return //空字符串可以清除已经加载的图片

        val options = RequestOptions().centerCrop()
                .placeholder(config.defaultDrawable)
                .error(config.errorDrawable)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.DATA)

        GlideApp.with(context).load(picUrl)
                .fitCenter()
                .apply(options)
                .into(target)
    }

    @JvmStatic
    fun loadCircleImage(context: Context, target: ImageView, picUrl: String, config: ImageConfig = imageConfig) {
//        if (TextUtils.isEmpty(picUrl)) return//空字符串可以清除已经加载的图片

        val options = RequestOptions().centerCrop()
                .placeholder(config.defaultDrawable)
                .error(config.errorDrawable)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transform(CircleCrop())

        // 加载圆角图片，不要动画过渡
        GlideApp.with(context).load(picUrl)
                .fitCenter()
                .dontAnimate()
                .apply(options)
                .into(target)
    }

    @JvmStatic
    fun loadRoundImage(context: Context, target: ImageView, resId: Int, radius: Int, config: ImageConfig = imageConfig) {

        val options = RequestOptions()
                .placeholder(config.defaultDrawable)
                .error(config.errorDrawable)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))

        GlideApp.with(context).load(resId)
                .dontAnimate()
                .apply(options)
                .into(target)
    }

    @JvmStatic
    fun loadRoundImage(context: Context, target: ImageView, picUrl: String, radius: Int, config: ImageConfig = imageConfig) {
        if (TextUtils.isEmpty(picUrl)) return

        val options = RequestOptions()
                .placeholder(config.defaultDrawable)
                .error(config.errorDrawable)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))

        GlideApp.with(context).load(picUrl)
                .dontAnimate()
                .apply(options)
                .into(target)
    }
}