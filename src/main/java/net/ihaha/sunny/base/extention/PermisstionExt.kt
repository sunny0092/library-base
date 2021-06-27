package net.ihaha.sunny.base.extention

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import net.ihaha.sunny.location.PermissionCheck


/**
 * Date: 13/06/2021.
 * @author SANG.
 * @version 1.0.0.
 */


//region Multiple permissions
inline fun Fragment.askForMultiplePermissions(
    crossinline onDenied: () -> Unit = {},
    crossinline onPermissionsGranted: () -> Unit = {}
) =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        val granted = result.map { it.value }.filter { it == false }
        if (granted.isNullOrEmpty()) {
            onPermissionsGranted()
        } else {
            onDenied()
        }
    }

inline fun FragmentActivity.askForMultiplePermissions(
    crossinline onDenied: () -> Unit = {},
    crossinline onPermissionsGranted: () -> Unit = {}
) =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        val granted = result.map { it.value }.filter { it == false }
        if (granted.isNullOrEmpty()) {
            onPermissionsGranted()
        } else {
            onDenied()
        }
    }
//endregion

//region Single permission
inline fun Fragment.askForSinglePermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onPermissionsGranted: () -> Unit = {}
) =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            onPermissionsGranted()
        } else {
            onDenied()
        }
    }

inline fun FragmentActivity.askForSinglePermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onPermissionsGranted: () -> Unit = {}
) =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            onPermissionsGranted()
        } else {
            onDenied()
        }
    }
//endregion

//region GPS
var enableLocationRetryCount = 1
inline fun Fragment.enableGPS(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) = registerForActivityResult(
    LocationSettingsContract()
) {
    if (enableLocationRetryCount <= 2) {
        onLocationGranted()
        enableLocationRetryCount++
    } else {
        onDenied()
        enableLocationRetryCount = 1
    }
}

inline fun FragmentActivity.enableGPS(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) = registerForActivityResult(
    LocationSettingsContract()
) {
    if (enableLocationRetryCount <= 2) {
        onLocationGranted()
        enableLocationRetryCount++
    } else {
        onDenied()
        enableLocationRetryCount = 1
    }
}
//endregion


//region Foreground location
inline fun Fragment.getForegroundLocationPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) =
    askForMultiplePermissions(
        onDenied,
        onLocationGranted
    ).launch(PermissionCheck.LOCATION_PERMISSIONS)

inline fun FragmentActivity.getForegroundLocationPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) =
    askForMultiplePermissions(
        onDenied,
        onLocationGranted
    ).launch(PermissionCheck.LOCATION_PERMISSIONS)
//endregion


//region Background location
inline fun Fragment.getBackgroundLocationPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) {
     when {
        Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
            askForSinglePermission(onDenied, onLocationGranted).launch(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
            getForegroundLocationPermission(onDenied, onLocationGranted)
        }
        Build.VERSION.SDK_INT == Build.VERSION_CODES.R -> {
            getForegroundLocationPermission(onDenied) {
                askForSinglePermission(onDenied, onLocationGranted).launch(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }
}


inline fun FragmentActivity.getBackgroundLocationPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) =
    when {
        Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
            askForSinglePermission(onDenied, onLocationGranted).launch(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
            getForegroundLocationPermission(onDenied, onLocationGranted)
        }
        Build.VERSION.SDK_INT == Build.VERSION_CODES.R -> {
            getForegroundLocationPermission(onDenied) {
                askForSinglePermission(onDenied, onLocationGranted).launch(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }
        else -> {

        }
    }
//endregion

//region camera permission
inline fun Fragment.getCameraPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onGranted: () -> Unit = {}
) =
    askForSinglePermission(onDenied, onGranted).launch(Manifest.permission.CAMERA)


inline fun FragmentActivity.getCameraPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onGranted: () -> Unit = {}
) =
    askForSinglePermission(onDenied, onGranted).launch(Manifest.permission.CAMERA)

inline fun Fragment.getStoragePermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) =
    askForMultiplePermissions(
        onDenied,
        onLocationGranted
    ).launch(PermissionCheck.STORAGE_PERMISSIONS)

inline fun FragmentActivity.getStoragePermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) =
    askForMultiplePermissions(
        onDenied,
        onLocationGranted
    ).launch(PermissionCheck.STORAGE_PERMISSIONS)

inline fun Fragment.getCallPhonePermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onGranted: () -> Unit = {}
) =
    askForSinglePermission(onDenied, onGranted).launch(Manifest.permission.CALL_PHONE)
//endregion

class LocationSettingsContract : ActivityResultContract<Nothing, Nothing>() {

    override fun createIntent(context: Context, input: Nothing?): Intent {
        return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Nothing? {
        return null
    }
}