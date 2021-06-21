package net.ihaha.sunny.base.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import net.ihaha.sunny.base.lifecycle.LifeCycleObserverUtils

abstract class BaseDialogFragment<T: ViewDataBinding>:  DialogFragment() {

    //region variable
    protected abstract val layoutId: Int
    protected lateinit var dataBinding: T

    private val autoLifeCycleObserver by lazy { LifeCycleObserverUtils(lifecycle) }

    protected val loadingDialog: BaseLoadingDialog by lazy {
        BaseLoadingDialog(requireContext())
    }
    //endregion

    //region override
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        autoLifeCycleObserver.init(this)
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    override fun onDestroy() {
        loadingDialog.dismiss()
        super.onDestroy()
    }

    //endregion

    //region method

    //endreigon
}