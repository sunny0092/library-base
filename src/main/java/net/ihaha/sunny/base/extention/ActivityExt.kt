package net.ihaha.sunny.base.extention

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import net.ihaha.sunny.base.core.repository.network.StateMessageCallback

fun Activity.hideKeyboard() {
    val view = currentFocus ?: View(this)
    if (view is EditText)
//        view.clearFocus()
    if(view.resources.getResourceEntryName(view.id) != "text_input_birthday") {
        view.windowToken?.let {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
                it,
                0
            )
        }
    }
}

fun Activity.showKeyboard(view: View) {
    val v = currentFocus ?: view
    if (v is EditText) v.requestFocus()
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
        v,
        SHOW_IMPLICIT
    )
}

fun Activity.displayToast(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.displayToast(@StringRes msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.displayToast(
    message: String,
    stateMessageCallback: StateMessageCallback,
) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    stateMessageCallback.removeMessageFromStack()
}

fun Activity.makeStatusBarTransparent() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        statusBarColor = Color.TRANSPARENT
    }
}

fun Fragment.makeStatusBarTransparent(activity: Activity) {
    activity.window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
//        statusBarColor = Color.TRANSPARENT
    }
}

/**
 * Apply window insets to the top and bottom padding using [ViewCompat] for [View]
 * that are not edge-to-edge aware.
 *
 * Do note that this will consume the padding set in that view.
 * @param view View apply insets to that view.
 */
fun applyInsets(view: View) {
    ViewCompat.setOnApplyWindowInsetsListener(view, null)
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        v.updatePadding(top = insets.systemWindowInsets.top)
        v.updatePadding(bottom = insets.systemWindowInsets.bottom)
        insets
    }
}

/**
 * Apply window insets to the top and bottom padding using [ViewCompat] for [View]
 * that are not edge-to-edge aware.
 *
 * Do note that this will consume the padding set in that view.
 * @param view View apply insets to that view.
 */
fun removeInsets(view: View) {
    ViewCompat.setOnApplyWindowInsetsListener(view, null)
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        v.updatePadding(top = 0)
        v.updatePadding(bottom = 0)
        insets
    }
}

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}

fun View.setMargin(
    marginLeft: Int? = 0,
    marginTop: Int? = 0,
    marginRight: Int? = 0,
    marginBottom: Int? = 0,
) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(marginLeft ?: 0,
        marginTop ?: 0,
        marginRight ?: 0,
        marginBottom ?: 0)
    this.layoutParams = menuLayoutParams
}

