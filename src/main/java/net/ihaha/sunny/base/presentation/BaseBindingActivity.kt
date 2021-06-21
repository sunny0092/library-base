package net.ihaha.sunny.base.presentation

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
abstract class BaseBindingActivity<T : ViewDataBinding> : BaseActivity() {

    protected lateinit var viewBinding: T
    abstract fun initShowViews()
    abstract fun initComponents(savedInstanceState: Bundle?)
    abstract fun initEventListeners()

    open fun isBindingEnabled(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (::viewBinding.isInitialized.not()) {
            viewBinding = DataBindingUtil.setContentView(this, layoutId)
            viewBinding.apply {
                root.isClickable = true
                executePendingBindings()
            }
            initShowViews()
            initComponents(savedInstanceState)
            initEventListeners()
        }
    }
}