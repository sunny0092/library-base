package net.ihaha.sunny.base.presentation.dialog.materialdialog

import android.content.Context
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.delichill.shipper.utils.R
import com.delichill.shipper.utils.databinding.DialogAreYouSureBinding
import com.delichill.shipper.utils.extentions.hideAnimation
import com.delichill.shipper.utils.extentions.invalidString
import com.delichill.shipper.utils.extentions.showAnimation


/**
 * Date: 27/01/2021.
 * @author SANG.
 * @version 1.0.0.
 */
class CustomMaterialDialog(context: Context) : BaseMaterialDialog<DialogAreYouSureBinding>(context) {

    private var positiveListeners: ((materialDialog: CustomMaterialDialog) -> Unit)? = null
    private var negativeListeners: ((materialDialog: CustomMaterialDialog) -> Unit)? = null

    override suspend fun onCreateBinding() {
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)

        binding.buttonNegative.setOnClickListener { view -> onActionButtonClicked(view as Button) }
        binding.buttonPositive.setOnClickListener { view -> onActionButtonClicked(view as Button) }
        binding.buttonHidden.setOnClickListener { dismiss() }
    }

    override fun getLayout(): Int {
        return R.layout.dialog_are_you_sure
    }

    private fun onActionButtonClicked(which: Button) {
        when (which) {
            binding.buttonNegative -> {
                dismiss()
                negativeListeners?.invoke(this)
            }
            binding.buttonPositive -> {
                dismiss()
                positiveListeners?.invoke(this)
            }
        }
    }

    fun title(res: String): CustomMaterialDialog {
        if(res.invalidString()) {
            binding.textViewTitle.showAnimation()
            binding.textViewTitle.text = res
        }else{
            binding.textViewTitle.hideAnimation()
        }
        return this
    }

    fun message(res: String): CustomMaterialDialog {
        if(res.invalidString()) {
            binding.textViewMessage.showAnimation()
            binding.textViewMessage.text = res
        }else{
            binding.textViewMessage.hideAnimation()
        }
        return this
    }

    fun negativeTitle(res: String): CustomMaterialDialog {
        if(res.invalidString()) {
            binding.buttonNegative.text = res
            binding.buttonNegative.showAnimation()
        }else{
            binding.buttonNegative.hideAnimation()
        }
        return this
    }

    fun positiveTitle(res: String): CustomMaterialDialog {
        if(res.invalidString()) {
            binding.buttonPositive.text = res
        }
        return this
    }

    fun hiddenEnable(isShow: Boolean) : CustomMaterialDialog {
        if(isShow) {
            binding.buttonHidden.showAnimation()
        }else{
            binding.buttonHidden.hideAnimation()
        }
        return this
    }

    fun positiveCallBack(onCallBack: (button : AppCompatButton) -> Unit){
        onCallBack.invoke(binding.buttonPositive)
    }

    fun positiveButton(positiveListeners: (materialDialog: CustomMaterialDialog) -> Unit): CustomMaterialDialog {
        this.positiveListeners = positiveListeners
        return this
    }

    fun negativeButton(negativeListeners: (materialDialog: CustomMaterialDialog) -> Unit): CustomMaterialDialog {
        this.negativeListeners = negativeListeners
        return this
    }

    fun show(block: CustomMaterialDialog.() -> Unit) {
        this.block()
        super.show()
    }

}