package net.ihaha.sunny.base.settings.network

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import net.ihaha.sunny.base.R

class OfflineStatusBarView @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null)
    : FrameLayout(ctx, attrs) {
    init {
        View.inflate(context, R.layout.offline_status_bar, this)
        setOnClickListener {
            NetworkAnimUtils.animateBottomBar(this, false)
            postDelayed({
                if (NetworkUtils.isConnectedWifi(context)) {
                    hide()
                } else {
                    show()
                }
            }, 2000)
        }
    }

    fun show() {
        if (visibility != View.VISIBLE) {
            NetworkAnimUtils.animateBottomBar(this, show = true)
        }
    }

    fun hide() {
        if (visibility == View.VISIBLE) {
            NetworkAnimUtils.animateBottomBar(this, show = false)
        }
    }
}
