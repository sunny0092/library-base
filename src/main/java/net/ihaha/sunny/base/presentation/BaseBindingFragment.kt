package net.ihaha.sunny.base.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.delichill.shipper.BR
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
abstract class BaseBindingFragment<T : ViewDataBinding, out VM : BaseViewModel<*>> :
    BaseFragment() {

    protected lateinit var viewBinding: T
    protected open val viewModel: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel?.cancelActiveJobs()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        if (::viewBinding.isInitialized.not()) {
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewBinding.apply {
            setVariable(BR.viewModel, viewModel)
            root.isClickable = true
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
        }
//        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.setupChannel()
    }

    override fun onDestroyView() {
        viewBinding.unbind()
        super.onDestroyView()
    }
}
