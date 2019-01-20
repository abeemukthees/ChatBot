package msa.chatbot

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions


/**
 * Created by Abhi Muktheeswarar.
 */


fun EpoxyRecyclerView.withModels(buildModelsCallback: EpoxyController.() -> Unit) {
    setControllerAndBuildModels(object : EpoxyController() {
        override fun buildModels() {
            buildModelsCallback()
        }
    })
}

@GlideModule
class ChatAppGlideModule : AppGlideModule()

fun ImageView.loadUrl(url: String?) {
    GlideApp.with(context)
        .load(url)
        .apply(RequestOptions().transforms(CenterCrop()))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}