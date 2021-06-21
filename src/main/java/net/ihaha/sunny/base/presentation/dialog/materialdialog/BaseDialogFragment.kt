package net.ihaha.sunny.base.presentation.dialog.materialdialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import net.ihaha.sunny.base.R
import net.ihaha.sunny.base.presentation.dialog.callback.OnDialogCallBack
import net.ihaha.sunny.base.extention.hideAnimation


/**
 * Date: 21/01/2021.
 * @author SANG.
 * @version 1.0.0.
 */
abstract class BaseDialogFragment: DialogFragment() {
    var mContentView: View? = null
    protected abstract fun getResourcesId(): Int
    var onDialogCallBack: OnDialogCallBack? = null
    private val mOutAnimation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.fade_out_dialog)
    }
    private val mInAnimation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.fade_in_dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView = inflater.inflate(getResourcesId(), container)
        return mContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContentView?.startAnimation(mInAnimation)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, R.style.TransparentDialog)
        val dialog = object : Dialog(requireContext(), this.theme) {
            override fun dismiss() {
                dismissWithAnimation()
            }
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        if (dialog.window != null) dialog.window!!
            .setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.warning_dialog_background_radius))
        return dialog
    }

    override fun onStart() {
        super.onStart()
        setupLayoutParams()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        try {
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    open fun setupLayoutParams() {
        val dialogWindow = dialog?.window
        val lp = dialogWindow!!.attributes
        dialogWindow.setGravity(Gravity.CENTER)
        dialogWindow.attributes = lp
    }


    fun dismissWithAnimation() {
        mContentView?.startAnimation(mOutAnimation
            .apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        mContentView?.apply {
                            hideAnimation()
                            post {
                                onDialogCallBack?.onCallBack()
                                this@BaseDialogFragment.dismiss()
                            }
                        }
                    }
                    override fun onAnimationStart(animation: Animation?) {}

                })
            })
    }
}