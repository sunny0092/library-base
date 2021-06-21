package net.ihaha.sunny.base.extention

import net.ihaha.sunny.base.BuildConfig
import timber.log.Timber

const val DEFAULT_TAG = "LOG ------> "

inline fun Any.log(lambda: () -> String?) {
    if (BuildConfig.DEBUG) {
        Timber.d("$DEFAULT_TAG${lambda().orEmpty()}")
    }
}

fun Any.log(message: String?, tag: String = DEFAULT_TAG) {
    if (BuildConfig.DEBUG) {
        Timber.d("$tag${message.orEmpty()}")
    }
}

inline fun <reified I : Any> Any.applyForType(block: (I) -> Unit): Any  {
    if (this::class == I::class) block(this as I)
    return this
}

inline fun <T, R> T?.withBy(block: T.() -> R): R? {
    return this?.block()
}

inline fun <reified T, reified R> R.unsafeLazy(noinline init: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, init)
