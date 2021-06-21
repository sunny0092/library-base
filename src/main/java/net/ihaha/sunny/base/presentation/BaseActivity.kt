package net.ihaha.sunny.base.presentation

import android.content.Context
import android.content.res.Resources
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.delichill.shipper.Constants
import com.delichill.shipper.R
import net.ihaha.sunny.base.extention.makeStatusBarTransparent
import net.ihaha.sunny.base.presentation.listener.OnCallBackCountDownTimer
import net.ihaha.sunny.base.presentation.listener.OnLocationTrackerListener
import net.ihaha.sunny.base.presentation.listener.UICommunicationListener
import net.ihaha.sunny.base.utils.FragmentConstants
import net.ihaha.sunny.base.settings.services.KeyboardManagement
import net.ihaha.sunny.base.settings.permission.StoragePermissionInterface
import com.delichill.shipper.core.Constants.Companion.STATUS_ACTIVE
import com.delichill.shipper.core.data.util.network.*
import com.delichill.shipper.dialog.SuccessDialogFragment
import com.delichill.shipper.listener.OnListenerNavigation
import net.ihaha.sunny.location.*
import com.delichill.shipper.ui.MainActivity
import com.delichill.shipper.ui.SplashScreenActivity
import com.delichill.shipper.utils.databinding.DialogAreYouSureBinding
import com.delichill.shipper.utils.dialog.materialdialog.BaseMaterialDialog
import com.delichill.shipper.utils.dialog.materialdialog.CustomMaterialDialog
import net.ihaha.sunny.base.extention.string
import com.delichill.shipper.utils.prefs.SharePrefsManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

@ExperimentalCoroutinesApi
@FlowPreview
abstract class BaseActivity : AppCompatActivity(),
    UICommunicationListener,
    KeyboardManagement,
    StoragePermissionInterface,
    OnLocaleChangedListener {

    //region Variant
    @get:LayoutRes
    abstract val layoutId: Int
    protected val sharePrefsManager: SharePrefsManager by inject()
    protected val sessionManager: SessionManager by inject()

    private var dialogInView: DialogFragment? = null
    private val localizationDelegate = LocalizationActivityDelegate(this)
    private var locationTracker: LocationTracker? = null
    protected var dialogWarning: BaseMaterialDialog<DialogAreYouSureBinding>? = null

    open var onLocationTrackerListener: OnLocationTrackerListener? = null
    open var permissionCheck: PermissionCheck? = null
    //endregion

    //region Override
    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()
        disableAutoFill()
        if (this is MainActivity) {
            permissionCheck = PermissionCheck(this)
            initLocationTracker()
        }
        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(newBase: Context) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase))
        super.attachBaseContext(newBase)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(super.getResources())
    }

    override fun onResponseReceived(
        response: Response,
        onListenerNavigation: OnListenerNavigation?,
        stateMessageCallback: StateMessageCallback
    ) {
        stateMessageCallback.removeMessageFromStack()
        when (response.uiComponentType) {
            is UIComponentType.Toast -> {
                response.message?.let {
                    displayToast(message = it, stateMessageCallback = stateMessageCallback)
                }
            }
            is UIComponentType.Network -> {
                return CustomMaterialDialog(this).show {
                    title("")
                    message(response.message ?: "")
                    positiveTitle(string(R.string.text_retry))
                }
            }
            is UIComponentType.Dialog -> {
                displayDialog(
                    response = response,
                    stateMessageCallback = stateMessageCallback,
                    onListenerNavigation = onListenerNavigation
                )
            }
            is UIComponentType.None -> {
            }
            is UIComponentType.Message -> {

            }
            else -> {
            }
        }
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun isStoragePermissionGranted(): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        dialogWarning?.onDismiss()
        localizationDelegate.onResume(this)
    }

    private fun disableAutoFill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.importantForAutofill =
                View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    abstract override fun displayProgressBar(isLoading: Boolean)
    //endregion

    //region Method

    protected fun checkPermission(): Boolean {
        var isPermission = true
        if (permissionCheck?.checkSelfPermission(PermissionCheck.LOCATION_PERMISSIONS) == false) {
            showWarningDialog(
                message = string(R.string.text_accept_using_shipper),
                positiveButtonTitle = string(R.string.text_ok),
                negativeButtonTitle = string(R.string.text_cancel),
                onClickPositiveButton = {

                },
                onClickNegativeButton = {
                    isPermission = false
                    sharePrefsManager.setIsRunning(false)
                    stopLocationTracker()
                }
            )
            return isPermission
        } else {
            return isPermission
        }
    }

    private fun initLocationTracker() {
        if (locationTracker == null) {
            locationTracker = object : LocationTracker(this) {
                override fun onLocationChanged(
                    networkLocation: Location?,
                    networkLocationSpeed: Double,
                    locationUpdate: LocationUpdate
                ) {
                    onLocationTrackerListener?.onLocation(networkLocation)
                }

                override fun onFailure(errorMessage: ErrorMessage) {
                    onLocationTrackerListener?.onFailure(errorMessage)
                }

                override fun onSuccess(successMessage: SuccessMessage) {
                    onLocationTrackerListener?.onSuccess(successMessage)
                }
            }
        }
    }

    private fun checkUserIsRunning() {
        sessionManager.getUserValue()?.observe(this, { userData ->
            if (userData?.status == STATUS_ACTIVE) {
                sharePrefsManager.setIsRunning(true)
                startLocationTracker()
            } else {
                sharePrefsManager.setIsRunning(false)
                stopLocationTracker()
            }
        })
    }

    fun startLocationTracker() {
        locationTracker?.startLocationTracker(LocationUpdate.ALL)
    }

    fun stopLocationTracker() {
        locationTracker?.stopLocationTracker(LocationUpdate.ALL)
    }

    protected open fun setCustomToolbar(
        view: View? = null,
        appBarLayout: AppBarLayout? = null,
        toolbar: MaterialToolbar? = null
    ) {
        setSupportActionBar(toolbar)
        this.makeStatusBarTransparent()
        try {
            if (this !is SplashScreenActivity) {
                if (view != null) {
                    ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                        val menuLayoutParams =
                            appBarLayout?.layoutParams as ViewGroup.MarginLayoutParams
                        menuLayoutParams.setMargins(0, insets.systemWindowInsetTop, 0, 0)
                        appBarLayout.layoutParams = menuLayoutParams
                        insets.consumeSystemWindowInsets()
                    }
                }
            }
        } catch (e: Exception) {
            e("ERROR", e.message)
        }
    }

    private fun displayDialog(
        response: Response,
        onListenerNavigation: OnListenerNavigation? = null,
        stateMessageCallback: StateMessageCallback
    ) {
        response.message?.let { message ->
            when (response.messageType) {
                is MessageType.Error -> {
                    showWarningDialog(
                        title = string(R.string.text_error),
                        message = message,
                    )
                }
                is MessageType.Login -> {
                    showWarningDialog(
                        title = string(R.string.text_notice),
                        message = message,
                        positiveButtonTitle = string(R.string.text_login),
                        negativeButtonTitle = string(R.string.text_no),
                        onClickPositiveButton = {
                            onListenerNavigation?.onNavigation(Constants.SIGNIN)
                            sessionManager.logout()
                        }
                    )
                }
                is MessageType.Verify -> {
                    showWarningDialog(
                        title = string(R.string.text_notice),
                        message = response.message ?: "",
                        positiveButtonTitle = string(R.string.text_ok),
                        negativeButtonTitle = string(R.string.text_no),
                        onClickPositiveButton = {
                            onListenerNavigation?.onNavigation(Constants.VERIFY)
                        }
                    )
                }
                is MessageType.Warning -> {
                    showWarningDialog(
                        title = string(R.string.text_notice),
                        message = message
                    )
                }
                is MessageType.Success -> {
                    showSuccessDialog(message) { onListenerNavigation?.onNavigation(Constants.HOME) }
                }
                is MessageType.Info -> {
                    null
                }
                else -> {
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }
        } ?: stateMessageCallback.removeMessageFromStack()
    }

    protected open fun showWarningDialog(
        title: String? = null,
        message: String? = null,
        negativeButtonTitle: String? = null,
        positiveButtonTitle: String? = null,
        onClickNegativeButton: ((materialDialog: CustomMaterialDialog) -> Unit)? = null,
        onClickPositiveButton: ((materialDialog: CustomMaterialDialog) -> Unit)? = null,
        onCallBackPositiveButton: ((button: AppCompatButton) -> Unit)? = null,
        hiddenButton: Boolean = false
    ) {
        dialogWarning?.dismiss()
        dialogWarning = CustomMaterialDialog(this)
        (dialogWarning as CustomMaterialDialog)?.show {
            title(title ?: "")
            message(message ?: "")
            negativeTitle(negativeButtonTitle ?: "")
            positiveTitle(positiveButtonTitle ?: "")
            positiveButton(onClickPositiveButton ?: {})
            negativeButton(onClickNegativeButton ?: {})
            positiveCallBack { onCallBackPositiveButton?.invoke(it) }
            hiddenEnable(hiddenButton)
        }
    }

    protected open fun showSuccessDialog(
        message: String?,
        onClickPositiveButton: View.OnClickListener?,
    ): SuccessDialogFragment? {
        dialogWarning?.dismiss()
        val successDialogFragment = SuccessDialogFragment()
        successDialogFragment.message = message
        successDialogFragment.onClickPositiveButton = onClickPositiveButton
        successDialogFragment.show(
            supportFragmentManager,
            FragmentConstants.WARNING_DIALOG_FRAGMENT
        )
        return successDialogFragment
    }

    override fun onAfterLocaleChanged() {}
    override fun onBeforeLocaleChanged() {}

    fun setLanguage(language: String) {
        localizationDelegate.setLanguage(this, language)
    }

    fun setLanguage(language: String, country: String) {
        localizationDelegate.setLanguage(this, language, country)
    }

    fun setLanguage(locale: Locale) {
        localizationDelegate.setLanguage(this, locale)
    }

    fun getCurrentLanguage(): Locale {
        return localizationDelegate.getLanguage(this)
    }

    //endregion

    //region Navigation

    //endregion

    //Countdown
    var countdownTimer: CountDownTimer? = null
    private val _counter = MutableLiveData(0)
    var onCallBackCountDownTimer: OnCallBackCountDownTimer? = null
    var counter: LiveData<Int> = _counter

    fun startTimer(timeCountDown: Long) {
        if (countdownTimer == null) {
            countdownTimer = object : CountDownTimer(timeCountDown * 1000, 1000) {
                override fun onTick(counter: Long) {
                    Timber.d("CountDownTimer: =======> Tick: ${(counter / 1000).toInt()}")
                    _counter.value = (counter / 1000).toInt()
                    onCallBackCountDownTimer?.onTick(counter / 1000)
                }

                override fun onFinish() {
                    Timber.d("CountDownTimer: =======> Tick: Finish")
                    _counter.value = 0
                    onCallBackCountDownTimer?.onTick(0)
                    onCallBackCountDownTimer?.onFinish()
                    stopTimer()
                }
            }.start()
        }
    }

    fun stopTimer() {
        countdownTimer?.cancel()
        countdownTimer = null
    }
    //
}