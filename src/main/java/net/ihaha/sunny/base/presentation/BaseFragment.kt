package net.ihaha.sunny.base.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.delichill.shipper.Constants
import com.delichill.shipper.R
import com.delichill.shipper.core.data.model.UserModel
import com.delichill.shipper.core.data.util.network.SessionManager
import com.delichill.shipper.dialog.CalendarDialogFragment
import com.delichill.shipper.dialog.CameraDialogFragment
import com.delichill.shipper.dialog.SuccessDialogFragment
import com.delichill.shipper.listener.OnListenerNavigation
import net.ihaha.sunny.location.PermissionCheck
import com.delichill.shipper.ui.account.AccountViewModel
import com.delichill.shipper.ui.account.fragment.AccountFragment
import com.delichill.shipper.ui.auth.fragment.*
import com.delichill.shipper.ui.home.fragment.BookingDetailMapFragment
import com.delichill.shipper.ui.home.fragment.HomeFragment
import net.ihaha.sunny.base.extention.*
import net.ihaha.sunny.base.lifecycle.LifeCycleObserverUtils
import net.ihaha.sunny.base.presentation.listener.UICommunicationListener
import net.ihaha.sunny.base.utils.FragmentConstants
import net.ihaha.sunny.base.settings.services.TakePhotoService
import com.delichill.shipper.utils.databinding.DialogAreYouSureBinding
import com.delichill.shipper.utils.dialog.DialogUtils
import com.delichill.shipper.utils.dialog.OnDialogCallBack
import com.delichill.shipper.utils.dialog.OnShowSnackBarComplete
import com.delichill.shipper.utils.dialog.materialdialog.BaseMaterialDialog
import com.delichill.shipper.utils.dialog.materialdialog.CustomMaterialDialog
import net.ihaha.sunny.base.extention.closeAlert
import com.delichill.shipper.utils.network.NetworkUtils
import com.delichill.shipper.utils.prefs.SharePrefsManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*


@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseFragment : Fragment() {
    //region variable
    protected open val contentViewLayout: View? = null
    protected open val loadingViewLayout: View? = null
    protected var permissionCheck: PermissionCheck? = null
    protected var takePhotoService: TakePhotoService? = null
    protected var onListenerNavigation: OnListenerNavigation? = null
    protected val sessionManager: SessionManager by inject()
    protected var userData: UserModel? = null
    private val accountViewModel: AccountViewModel by inject()
    private var requestPermissionLauncher: ActivityResultLauncher<Array<String>>? = null

    @get:LayoutRes
    abstract val layoutId: Int

    open fun initShowViews() = Unit
    open fun initComponents(savedInstanceState: Bundle?) = Unit
    open fun initEventListeners() = Unit
    open fun initResume() = Unit

    lateinit var uiCommunicationListener: UICommunicationListener

    protected val sharePrefsManager: SharePrefsManager by inject()
    private val autoLifeCycleObserver by lazy { LifeCycleObserverUtils(lifecycle) }
    private var dialogUtils: Snackbar? = null
    private var dialogWarning: BaseMaterialDialog<DialogAreYouSureBinding>? = null
    //endregion

    //region override

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            e("ERROR", e.message)
        }
    }

    override fun onStart() {
        super.onStart()
        setupToolbar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        permissionCheck = PermissionCheck(requireActivity())
        takePhotoService = TakePhotoService(this, requireActivity())
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    when (this@BaseFragment) {
                        is VerifyOTPFragment,
                        is ForgetPasswordFragment -> {
                            makeStatusBarTransparent(requireActivity())
                        }
                    }
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (this) {
            is BookingDetailMapFragment -> {
                getForegroundLocationPermission()
            }
            is HomeFragment -> {
                requestPermissionLauncher = askForMultiplePermissions(
                    onPermissionsGranted = {
                        dialogWarning?.dismiss()
                        this.checkStatusEventActive()
                    },
                    onDenied = {
                        dialogWarning?.dismiss()
                        sharePrefsManager.setIsRunning(false)
                        onListenerNavigation?.onNavigation(Constants.RUNNING)
                    }
                )
            }
        }
        setupUI(view)
        initDataLayout(savedInstanceState)
    }

    private fun initDataLayout(bundle: Bundle? = null) {
        if (NetworkUtils.isConnectedMobile(requireContext())) {
            dialogWarning?.dismiss()
            dialogUtils?.dismiss()
            when (this) {
                is SignInFragment,
                is SignUpFragment,
                is VerifyOTPFragment,
                is ForgetPasswordFragment,
                is ResetPasswordFragment,
                is UpdatePasswordFragment,
                is VerifySuccessFragment -> unCheckUserInvalidate(bundle = bundle)
                else -> checkUserInvalidate(bundle = bundle)
            }
        } else {
            showWarningNetwork()
        }
    }

    override fun onResume() {
        super.onResume()
        if (NetworkUtils.isConnectedMobile(requireContext())) {
            dialogUtils?.dismiss()
            sessionManager.getUserValue()?.observe(viewLifecycleOwner, { userData ->
                if (userData != null) {
                    this.userData = userData
                    initResume()
                }
            })
        } else {
            showWarningNetwork()
        }
    }

    override fun onDestroyView() {
        context?.closeAlert()
        super.onDestroyView()
    }

    override fun onPause() {
        super.onPause()
        dialogWarning?.dismiss()
    }

    //endregion

    //region method
    protected fun checkPermission(): Boolean {
        var isPermission = true
        if (permissionCheck?.checkSelfPermission(PermissionCheck.LOCATION_PERMISSIONS) == false) {
            showWarningDialog(
                message = string(R.string.text_accept_using_shipper),
                positiveButtonTitle = string(R.string.text_ok),
                negativeButtonTitle = string(R.string.text_cancel),
                onClickPositiveButton = {
                    isPermission = true
                    requestPermissionLauncher?.launch(PermissionCheck.LOCATION_PERMISSIONS)
                },
                onClickNegativeButton = {
                    isPermission = false
                    sharePrefsManager.setIsRunning(false)
                    onListenerNavigation?.onNavigation(Constants.RUNNING)
                }
            )
            return isPermission
        } else {
            return isPermission
        }
    }

    private fun unCheckUserInvalidate(bundle: Bundle? = null) {
        initShowViews()
        initComponents(bundle)
        initEventListeners()
    }

    private fun checkUserInvalidate(bundle: Bundle?) {
        sessionManager.getUserValue()?.observe(viewLifecycleOwner, { userData ->
            this.userData = userData
            if (userData?.authToken?.isNotEmpty() == true) {
                if (sessionManager.getAdminVerifyValue()) {
                    if (userData.walletBalance!! < 500000L) {
                        if (this !is AccountFragment && sharePrefsManager.getCheckMoneyRunning() == true) {
                            sharePrefsManager.setCheckMoneyRunning(false)
                            showWarningDialog(
                                message = string(R.string.text_des_account),
                                positiveButtonTitle = string(R.string.text_ok),
                                negativeButtonTitle = string(R.string.text_cancel),
                                onClickPositiveButton = {
                                    findNavController().navigate(R.id.action_global_payment)
                                }
                            )
                        }
                    }
                }
                initShowViews()
                initComponents(bundle)
                initEventListeners()
            } else {
                showWarningDialog(
                    title = string(R.string.text_notice),
                    message = string(R.string.error_login_force),
                    positiveButtonTitle = string(R.string.text_ok),
                    negativeButtonTitle = string(R.string.text_no),
                    onClickPositiveButton = {
                        onListenerNavigation?.onNavigation(Constants.SIGNIN)
                    }
                )
            }
        })
    }

    open fun getWifiIPAddress(): String? {
        val wifiMgr = activity?.applicationContext?.getSystemService(WIFI_SERVICE) as WifiManager?
        val wifiInfo = wifiMgr!!.connectionInfo
        val ip = wifiInfo.ipAddress
        return Formatter.formatIpAddress(ip)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(viewRoot: View) {
        if (viewRoot !is EditText) {
            viewRoot.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }
        if (viewRoot is ViewGroup) {
            for (i in 0 until viewRoot.childCount) {
                val innerView: View = viewRoot.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    fun setupToolbar() {
        Timber.tag("NAME-ACTIVITY").d(activity?.javaClass?.simpleName)
        when (this) {
            is SignUpFragment,
            is SignInFragment -> this.makeStatusBarTransparent(requireActivity())
        }
    }

    fun setupFullScreen(isShow: Boolean) {
        if (isShow) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    protected open fun showWarningNetwork() {
        dialogUtils?.dismiss()
        dialogUtils = DialogUtils().showSnackBarAboveBottomNavigation(
            requireContext(),
            this.requireView(),
            string(R.string.error_no_internet_connection),
            string(R.string.text_retry),
            onClickListener = { onResume() },
            onShowSnackBarComplete = object : OnShowSnackBarComplete {
                override fun onShowSnackBarComplete(snackbar: Snackbar?) {
                    snackbar?.show()
                }
            },
            bottomNavigationView = activity?.findViewById(R.id.bottom_navigation_view)

        )
    }

    protected open fun showWaringBookingConfirm(onClickListener: View.OnClickListener?) {
        dialogUtils?.dismiss()
        dialogUtils = DialogUtils().showSnackBarBookingNavigation(
            requireContext(),
            this.requireView(),
            string(R.string.text_you_have_order_doing_confirm),
            string(R.string.text_continuel),
            onClickListener,
            onShowSnackBarComplete = object : OnShowSnackBarComplete {
                override fun onShowSnackBarComplete(snackbar: Snackbar?) {
                    snackbar?.show()
                }
            },
            bottomNavigationView = activity?.findViewById(R.id.bottom_navigation_view)
        )
    }

    protected open fun showWarningDialog(
        title: String? = null,
        message: String?,
        negativeButtonTitle: String? = null,
        positiveButtonTitle: String? = null,
        onClickNegativeButton: ((materialDialog: CustomMaterialDialog) -> Unit)? = null,
        onClickPositiveButton: ((materialDialog: CustomMaterialDialog) -> Unit)? = null,
        hiddenButton: Boolean = false
    ) {
        dialogWarning?.dismiss()
        dialogWarning = CustomMaterialDialog(requireContext())
        (dialogWarning as CustomMaterialDialog).show {
            title(title ?: "")
            message(message ?: "")
            negativeTitle(negativeButtonTitle ?: "")
            positiveTitle(positiveButtonTitle ?: "")
            positiveButton(onClickPositiveButton ?: {})
            negativeButton(onClickNegativeButton ?: {})
            hiddenEnable(hiddenButton)
        }
    }

    protected open fun showSuccessDialog(
        message: String?,
        onClickPositiveButton: View.OnClickListener?,
    ): SuccessDialogFragment? {
        val successDialogFragment = SuccessDialogFragment()
        successDialogFragment.message = message
        successDialogFragment.onClickPositiveButton = onClickPositiveButton
        successDialogFragment.show(
            activity?.supportFragmentManager!!,
            FragmentConstants.WARNING_DIALOG_FRAGMENT
        )
        return successDialogFragment
    }

    open fun showCameraDialog(
        textViewPhoto: String? = null,
        onClickViewImageButton: View.OnClickListener?,
        onClickViewCameraButton: View.OnClickListener?,
        onClickViewLibraryButton: View.OnClickListener?,
    ): CameraDialogFragment {

        val cameraDialogFragment: CameraDialogFragment = CameraDialogFragment.newInstance(
            textViewPhoto,
            onClickViewImageButton,
            onClickViewCameraButton,
            onClickViewLibraryButton
        )
        cameraDialogFragment.show(
            activity?.supportFragmentManager!!,
            FragmentConstants.WARNING_DIALOG_FRAGMENT
        )
        return cameraDialogFragment
    }

    protected open fun showCalendarDialog(
        editText: EditText?,
        positionX: Float = -1F,
        positionY: Float = -1F,
        fromDate: Calendar? = null,
        toDate: Calendar? = null,
        onDialogCallBack: OnDialogCallBack? = null
    ): CalendarDialogFragment? {
        val calendarDialogFragment = CalendarDialogFragment()
        calendarDialogFragment.show(
            activity?.supportFragmentManager!!,
            FragmentConstants.WARNING_DIALOG_FRAGMENT
        )
        calendarDialogFragment.editText = editText
        calendarDialogFragment.positionX = positionX
        calendarDialogFragment.positionY = positionY
        calendarDialogFragment.fromDate = fromDate
        calendarDialogFragment.toDate = toDate
        calendarDialogFragment.onDialogCallBack = onDialogCallBack
        return calendarDialogFragment
    }


    //endregion
}

