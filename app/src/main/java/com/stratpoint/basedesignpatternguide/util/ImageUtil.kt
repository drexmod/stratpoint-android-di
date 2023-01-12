package com.stratpoint.basedesignpatternguide.util

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey

object ImageUtil {

    @SuppressLint("CheckResult")
    fun load(imageUrl: String?, imageView: ImageView, placeholder: Int = -1, errorPlaceholder: Int = -1, timestamp: String? = null) {

        var glide = Glide.with(imageView.context).load(imageUrl)

        timestamp?.let {
            glide.signature(ObjectKey(it))
        }

        if (placeholder != -1) {
            glide = glide.clone().placeholder(placeholder)
        }

        if (errorPlaceholder != -1) {
            glide = glide.clone().error(errorPlaceholder)
        }

        glide.into(imageView)
    }

    @SuppressLint("CheckResult")
    fun load(imageRes: Int, imageView: ImageView, placeholder: Int = -1, errorPlaceholder: Int = -1, timestamp: String? = null) {

        var glide = Glide.with(imageView.context).load(imageRes)

        timestamp?.let {
            glide.signature(ObjectKey(it))
        }

        if (placeholder != -1) {
            glide = glide.clone().placeholder(placeholder)
        }

        if (errorPlaceholder != -1) {
            glide = glide.clone().error(errorPlaceholder)
        }

        glide.into(imageView)
    }
}
