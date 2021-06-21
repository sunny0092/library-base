package net.ihaha.sunny.base.presentation

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import net.ihaha.sunny.base.R

class BaseLoadingDialog(context: Context, theme: Int = R.style.TransparentDialog) : Dialog(context, theme) {

    init {
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        super.setContentView(R.layout.layout_loading)
        setCanceledOnTouchOutside(false)
    }

}