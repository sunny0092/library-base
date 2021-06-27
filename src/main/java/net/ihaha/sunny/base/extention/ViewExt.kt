package net.ihaha.sunny.base.extention

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Html
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.github.abdularis.civ.CircleImageView
import com.google.android.material.tabs.TabLayout
import net.ihaha.sunny.base.R
import net.ihaha.sunny.base.presentation.listener.BindAdapter
import net.ihaha.sunny.base.utils.GlideApp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.fadeIn() {
    AnimationUtils.loadAnimation(context, R.anim.fade_in_dialog)
}


fun bindImageFromUrl(view: ImageView, progressBar: ProgressBar?, imageUrl: String?) {
    view.invisible()
    progressBar?.let { it.visible() }
    if (!imageUrl.isNullOrEmpty()) {
        GlideApp.with(view.context).load(imageUrl).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                view.visible()
                progressBar?.let { it.invisible() }
                view.setImageDrawable(resource)
            }
        })
    }
}

@BindingAdapter("loadImage")
fun AppCompatImageView.loadImage(imageUrl: String?) {
    if (imageUrl.invalidString()) {
        imageUrl?.let {
            GlideApp.with(this).load("https://delichill.s3-ap-southeast-1.amazonaws.com/$imageUrl")
                .placeholder(R.drawable.ic_image_account)
                .error(R.drawable.ic_image_account)
                .into(this)
        }
    } else {
        imageUrl?.let { GlideApp.with(this).load(R.drawable.ic_image_account).into(this) }
    }
}

@BindingAdapter("bind:loadImageZ")
fun AppCompatImageView.loadImageZ(imageUrl: String?) {
    if (imageUrl.invalidString()) {
        imageUrl?.let {
            GlideApp.with(this).load("https://delichill.s3-ap-southeast-1.amazonaws.com/$imageUrl")
                .placeholder(R.drawable.ic_account)
                .into(this)
        }
    } else {
        imageUrl?.let { GlideApp.with(this).load(R.drawable.ic_account).into(this) }
    }
}

@BindingAdapter("qrcode")
fun AppCompatImageView.qrCodeImage(imageUrl: Bitmap?) {
    imageUrl?.let { GlideApp.with(this).asBitmap().load(imageUrl) }
}

@BindingAdapter("loadImage")
fun CircleImageView.loadImage(imageUrl: String?) {
    if (imageUrl.invalidString()) {
        imageUrl?.let {
            GlideApp.with(this).load("https://delichill.s3-ap-southeast-1.amazonaws.com/$imageUrl")
                .placeholder(R.drawable.ic_image_add)
                .error(R.drawable.ic_image_add)
                .into(this)
        }
    } else {
        GlideApp.with(this).load(R.drawable.ic_image_add)
            .into(this)
    }
}

@BindingAdapter("loadImage")
fun AppCompatImageButton.loadImageDrawable(image: Drawable?) {
    if (image != null) {
        image?.let {
            GlideApp.with(this).load(image)
                .placeholder(R.drawable.ic_image_add)
                .error(R.drawable.ic_image_add)
                .into(this)
        }
    } else {
        GlideApp.with(this).load(R.drawable.ic_image_add)
            .into(this)
    }
}

@BindingAdapter("loadImageURI")
fun AppCompatImageButton.loadImageURI(imageUri: Uri?) {
    if (imageUri != null) {
        imageUri.let {
            GlideApp.with(this).load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.ic_image_add)
                .error(R.drawable.ic_image_add)
                .into(this)
        }
    } else {
        GlideApp.with(this).load(R.drawable.ic_image_add)
            .into(this)
    }
}

@BindingAdapter("loadImageURI")
fun CircleImageView.loadImageURI(imageUri: Uri?) {
    if (imageUri != null) {
        imageUri.let {
            GlideApp.with(this).load(imageUri)
                .fitCenter()
                .placeholder(R.drawable.ic_image_add)
                .error(R.drawable.ic_image_add)
                .into(this)
        }
    } else {
        GlideApp.with(this).load(R.drawable.ic_image_add)
            .apply(RequestOptions().override(24, 24))
            .into(this)
    }
}

@BindingAdapter("recyclerAdapter")
fun setRecyclerAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
    if (adapter != null) {
        recyclerView.adapter = adapter
    }
}

@BindingAdapter("recyclerData")
fun <T> setRecyclerItems(recyclerView: RecyclerView, items: MutableList<T>?) {
    if (items?.isNotEmpty() == true) {
        if (recyclerView.adapter is BindAdapter<*, *>) {
            (recyclerView.adapter as BindAdapter<T, *>).setItems(items)
        }
    }
}

@BindingAdapter("app:onItemClickListener")
fun <K> setPagerListener(recyclerView: RecyclerView, onItemClickListener: K) {
    if (recyclerView.adapter is BindAdapter<*, *>) {
        if (onItemClickListener != null) {
            (recyclerView.adapter as BindAdapter<*, K>).setOnItemClickListener(
                onItemClickListener = onItemClickListener
            )
        }
    }
}

@BindingAdapter("app:textHtml")
fun setTextHtml(textView: AppCompatTextView, content: String?) {
    if (content.invalidString()) {
        textView.text = Html.fromHtml(content)
    }
}

//@BindingAdapter("app:isChecked")
//fun isSwitchChecked(switch: Switch, isChecked: Boolean) {
//    switch.isChecked = isChecked
//}

//@BindingAdapter("app:onCheckedChangeListener")
//fun isSwitchChecked(
//    switch: Switch,
//    onCheckedChangeListener: CompoundButton.OnCheckedChangeListener,
//) {
//    switch.setOnCheckedChangeListener(onCheckedChangeListener)
//}

@SuppressLint("SetTextI18n")
@BindingAdapter("app:price")
fun AppCompatTextView.priceFormat(number: Long?) {
    if (number != null && number != 0L) {
        this.text = number.formatPrice()
    } else {
        this.text = "-----"
    }
}

@BindingAdapter("app:date")
fun AppCompatTextView.dateFormat(number: String) {
    if (number.isNotEmpty()) {
        this.text = getRealDayTimeFromTime(
            number.toTimeLong("yyyy.MM.dd HH:mm") ?: System.currentTimeMillis()
        )
    }
}

@BindingAdapter("bindTimeAgo")
fun bindTimeAgo(view: TextView, time: String?) {
    if (time == null) return
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val ago: String
    try {
        val past = format.parse(time)
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past!!.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
        val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
        ago = when {
            seconds < 60 -> seconds.toString() + "giây"
            minutes < 60 -> minutes.toString() + "phút"
            hours < 24 -> hours.toString() + "giây"
            else -> days.toString() + "ngày"
        }
        view.text = ago
    } catch (e: ParseException) {
        e.printStackTrace()
    }
}


@SuppressLint("SetJavaScriptEnabled", "WrongConstant")
@BindingAdapter("wvSetContent")
fun WebView.wvSetContent(content: String?) {
    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.settings.javaScriptEnabled = true
    this.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    this.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
    this.settings.cacheMode = WebSettings.LOAD_DEFAULT
    this.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

    this.settings.domStorageEnabled = true
    this.settings.loadsImagesAutomatically = true
    this.settings.setAppCacheEnabled(true)
    this.settings.databaseEnabled = true
    this.settings.setSupportMultipleWindows(false)
    this.loadDataWithBaseURL(
        null,
        "<style>img{display: inline;height: auto;max-width: 100%;}</style>$content",
        "text/html",
        "UTF-8",
        null
    )

}

data class ScrollPosition(var index: Int = 0, var top: Int = 0) {
    fun drop() {
        index = 0
        top = 0
    }
}

fun View.toast(
    message: Any,
    duration: Int = Toast.LENGTH_SHORT
) = context?.toast(message, duration)

fun Fragment.toast(
    message: Any,
    duration: Int = Toast.LENGTH_SHORT
) = context?.toast(message, duration)

fun TextView.clear() {
    this.text = null
    this.setOnClickListener(null)
}

fun ImageView.clear(isOnlyImage: Boolean = false) {
    this.setImageResource(0)
    this.setImageBitmap(null)
    this.setImageDrawable(null)
    if (isOnlyImage) this.setOnClickListener(null)
}

fun Button.clear(isClearText: Boolean = true) {
    if (isClearText) this.text = null
    this.setOnClickListener(null)
}

fun CheckBox.clear() {
    this.setOnCheckedChangeListener(null)
}

fun Toolbar.clear() {
    this.title = null
    this.setNavigationOnClickListener(null)
}

fun AutoCompleteTextView.clear() {
    onFocusChangeListener = null
    setOnEditorActionListener(null)
    setOnClickListener(null)
    setAdapter(null)
}

//fun ViewGroup.clear() {
//    this.children
//        .asSequence()
//        .forEach { it.clearView() }
//}

fun TabLayout.clear() {
    this.clearOnTabSelectedListeners()
    this.removeAllTabs()
}

fun EditText.clear() {
    setOnEditorActionListener(null)
    onFocusChangeListener = null
    setOnClickListener(null)
}

fun View.clearView() {
    when (this) {
//        is ViewGroup -> this.clear()
        is ImageView -> this.clear()
        is Button -> this.clear()
        is AutoCompleteTextView -> this.clear()
        is EditText -> this.clear()
        is TextView -> this.clear()
        is CheckBox -> this.clear()
        is Toolbar -> this.clear()
        is TabLayout -> this.clear()
    }
}

fun View.hideAnimation(gone: Boolean = true) {
    visibility = if (gone) View.GONE else View.INVISIBLE
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out_custom))
}

fun View.showAnimation() {
    visibility = View.VISIBLE
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_custom))
}

fun View.showAnimationLogo() {
    visibility = View.VISIBLE
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_logo))
}

fun View.hide(gone: Boolean = true) {
    visibility = if (gone) View.GONE else View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

// slide the view from below itself to the current position
fun View.slideUp() {
    this.visibility = View.VISIBLE
    val animate = TranslateAnimation(
        0f, // fromXDelta
        0f, // toXDelta
        this.height.toFloat(), // fromYDelta
        0f
    )                // toYDelta
    animate.duration = 500
    this.startAnimation(animate)
}

fun View.slideUp(isShow: Int = View.VISIBLE) {
    this.visibility = isShow
    val animate = TranslateAnimation(
        0f, // fromXDelta
        0f, // toXDelta
        this.height.toFloat(), // fromYDelta
        0f
    )                // toYDelta
    animate.duration = 500
    this.startAnimation(animate)
}

// slide the view from its current position to below itself
fun View.slideDown(isShow: Int = View.GONE) {
    val animate = TranslateAnimation(
        0f, // fromXDelta
        0f, // toXDelta
        0f, // fromYDelta
        this.height.toFloat()
    ) // toYDelta
    animate.duration = 500
    this.startAnimation(animate)
    this.visibility = isShow
}

fun View.setOnSingleClickListener(l: View.OnClickListener) {
    setOnClickListener(OnSingleClickListener(l))
}

fun View.setOnSingleClickListener(l: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(l))
}

class OnSingleClickListener : View.OnClickListener {
    private val onClickListener: View.OnClickListener
    constructor(listener: View.OnClickListener) {
        onClickListener = listener
    }
    constructor(listener: (View) -> Unit) {
        onClickListener = View.OnClickListener { listener.invoke(it) }
    }
    override fun onClick(v: View) {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis >= previousClickTimeMillis + DELAY_MILLIS) {
            previousClickTimeMillis = currentTimeMillis
            onClickListener.onClick(v)
        }
    }
    companion object {
        // Tweak this value as you see fit. In my personal testing this
        // seems to be good, but you may want to try on some different
        // devices and make sure you can't produce any crashes.
        private const val DELAY_MILLIS = 200L
        private var previousClickTimeMillis = 0L
    }

}

fun processEmptyStringInTextView(title1: String?, textView: TextView?, displayView: Int) {
    textView?.visibility = View.VISIBLE
    if (title1 == null || title1 == "") {
        textView?.visibility = displayView
    } else {
        textView?.text = title1
    }
}




