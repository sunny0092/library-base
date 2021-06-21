package net.ihaha.sunny.base.presentation.dialog.materialdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.callbacks.onShow
import com.afollestad.materialdialogs.customview.customView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.ihaha.sunny.base.R


/**
 * Date: 27/01/2021.
 * @author SANG.
 * @version 1.0.0.
 */
abstract class BaseMaterialDialog<T : ViewDataBinding>(
    protected val context: Context
) : LifecycleOwner {
    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }
    val binding: T by lazy { createBinding() }
    protected val dialog: MaterialDialog by lazy { createDialog() }
    private val mOutAnimation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.fade_out_dialog)
    }
    private val mInAnimation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.fade_in_dialog)
    }

    private fun createBinding(): T {
        val inflater = LayoutInflater.from(context)
        return DataBindingUtil.inflate<T>(inflater, getLayout(), null, false)
            .apply { lifecycleOwner = this@BaseMaterialDialog }
            .also { lifecycleScope.launchWhenCreated { onCreateBinding() } }
    }

    private fun createDialog(): MaterialDialog {
        binding.root.startAnimation(mInAnimation)
        return getMaterialDialog()
            .customView(view = binding.root, noVerticalPadding = true)
            .onCancel { onCancel() }
            .onDismiss { onDismiss() }
            .onShow { onShow() }
            .cornerRadius(10f)
    }

    /**
     * Called after the binding is created,
     * either by a need of the dialog or for
     * another reason
     */
    abstract suspend fun onCreateBinding()

    fun show(scope: CoroutineScope): Job {
        return scope.launch { show() }
    }

    fun show() {
        dialog.show()
    }

    fun isShow(): Boolean {
        return dialog.isShowing
    }

    fun dismiss() {
        binding.root.startAnimation(mOutAnimation
            .apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        dialog.dismiss()
                    }

                    override fun onAnimationStart(animation: Animation?) {}

                })
            })

    }

    open fun onShow() {
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    open fun onCancel() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    open fun onDismiss() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    open fun getMaterialDialog(): MaterialDialog = MaterialDialog(context)

    @LayoutRes
    abstract fun getLayout(): Int
    override fun getLifecycle(): Lifecycle = lifecycleRegistry
}