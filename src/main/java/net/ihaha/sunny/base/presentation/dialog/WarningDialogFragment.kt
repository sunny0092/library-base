//package com.delichill.shipper.utils.dialog
//
//import android.content.Context
//import android.graphics.drawable.ColorDrawable
//import android.graphics.drawable.InsetDrawable
//import android.os.Bundle
//import android.view.Gravity
//import android.view.View
//import android.view.WindowManager
//import android.view.animation.AnimationUtils
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.FragmentManager
//import com.delichill.shipper.utils.R
//import com.delichill.shipper.utils.extentions.setOnSingleClickListener
//
//
///**
// * Date: 25/10/2020.
// * @author SANG.
// * @version 1.0.0.
// */
//class WarningDialogFragment : BaseDialogFragment() {
//    //region Variables
//    private var activity: AppCompatActivity? = null
//    private var tvMessage: TextView? = null
//    private var tvTitle: TextView? = null
//    private var buttonNegative: TextView? = null
//    private var buttonPositive: TextView? = null
//    var onClickNegativeButton: View.OnClickListener? = null
//    var onClickPositiveButton: View.OnClickListener? = null
//    var message: String? = null
//    var title: String? = null
//    var positiveTitleButton: String? = context?.getString(R.string.text_ok)
//    var negativeTitleButton: String? = context?.getString(R.string.text_no)
//
//
//    private val mOutAnimation by lazy {
//        AnimationUtils.loadAnimation(activity, R.anim.anim_out)
//    }
//    private val mInAnimation by lazy {
//        AnimationUtils.loadAnimation(activity, R.anim.anim_in)
//    }
//
//    //endregion
//
//    //region Overrides
//
//
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        activity = context as AppCompatActivity?
//    }
//
//    override fun getResourcesId(): Int {
//        return R.layout.dialog_warning
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initComponents()
//        initEventListener()
//    }
//
//    override fun setupLayoutParams() {
//        dialog?.window?.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.MATCH_PARENT
//        )
//        dialog?.window?.setGravity(Gravity.CENTER)
//        val backgroundColor = ColorDrawable(ContextCompat.getColor(requireContext(),R.color.colorBackgroundDialog))
//        val margin = 40
//        val inset = InsetDrawable(backgroundColor, margin)
//        dialog!!.window!!.setBackgroundDrawable(inset)
//    }
//
//    //endregion
//    //region Methods
//
//    override fun show(manager: FragmentManager, tag: String?) {
//        val ft = manager.beginTransaction()
//        ft.add(this, tag)
//        ft.commitAllowingStateLoss()
//    }
//
//    private fun initComponents() {
//        tvMessage = mContentView?.findViewById(R.id.text_view_message)
//        tvTitle = mContentView?.findViewById(R.id.text_view_title)
//        buttonPositive = mContentView?.findViewById(R.id.button_positive)
//        buttonNegative = mContentView?.findViewById(R.id.button_negative)
//        ViewUtils.processEmptyStringInTextView(message, tvMessage, View.GONE)
//        ViewUtils.processEmptyStringInTextView(title, tvTitle, View.GONE)
//        ViewUtils.processEmptyStringInTextView(negativeTitleButton, buttonNegative, View.GONE)
//    }
//
//    private fun initEventListener() {
//        buttonNegative!!.setOnSingleClickListener { view: View? ->
//            dismissWithAnimation()
//            if (onClickNegativeButton != null) onClickNegativeButton!!.onClick(view)
//
//        }
//        buttonPositive!!.setOnSingleClickListener { view: View? ->
//            dismissWithAnimation()
//            if (onClickPositiveButton != null) onClickPositiveButton!!.onClick(view)
//        }
//    }
//}