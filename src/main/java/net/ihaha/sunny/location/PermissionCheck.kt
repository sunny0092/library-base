package net.ihaha.sunny.location

import android.Manifest.permission
import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


class PermissionCheck(private val activity: Activity) {

    fun checkSelfPermission(permissions: Array<String>): Boolean {
        var permissionCheck = true
        for (permission in permissions) {
            permissionCheck =
                permissionCheck and (ContextCompat.checkSelfPermission(activity, permission)
                        == PackageManager.PERMISSION_GRANTED)
        }
        return permissionCheck
    }

    companion object {
        var STORAGE_PERMISSIONS = arrayOf(
            permission.WRITE_EXTERNAL_STORAGE,
            permission.READ_EXTERNAL_STORAGE
        )
        var CAMERA_PERMISSIONS = CAMERA
        var LOCATION_PERMISSIONS = arrayOf(
            permission.ACCESS_FINE_LOCATION,
            permission.ACCESS_COARSE_LOCATION
        )
    }
}

