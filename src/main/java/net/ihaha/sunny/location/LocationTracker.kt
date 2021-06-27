package net.ihaha.sunny.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.ihaha.sunny.location.PermissionCheck.Companion.LOCATION_PERMISSIONS

abstract class LocationTracker(var activity: Activity) {

    private var mLocationRequest: LocationRequest? = null
    private var gpsLocationManager: LocationManager? = null
    private var networkLocationManager: LocationManager? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    private var previousApiLocation: Location? = null
    private var gpsLocation: Location? = null
    private var previousGpsLocation: Location? = null
    private var previousNetworkLocation: Location? = null

    private var locationCallback: LocationCallback? = null
    private var gpsLocationListener: LocationListener? = null
    private var networkLocationListener: LocationListener? = null

    private var permissionCheck: PermissionCheck = PermissionCheck(activity)

    private var updateInterval: Long = 10 * 1000
    private var fastestInterval: Long = 10 * 1000
    private var minimumDistance: Float = 1f

    private val isGPSEnabled: Boolean
        get() {
            gpsLocationManager =
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return gpsLocationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        }

    private val isNetworkEnabled: Boolean
        get() {
            networkLocationManager =
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return networkLocationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                ?: false
        }

    private fun initLocationCallBack() {
        if(locationCallback == null) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (previousApiLocation == null) {
                        previousApiLocation = locationResult.lastLocation
                    }
                    val locationDetail = LocationDetail(
                        locationResult.lastLocation,
                        previousApiLocation ?: locationResult.lastLocation
                    )
                    val speed: Double = locationDetail.getCalculatedSpeed()
                    onLocationChanged(
                        locationResult.lastLocation,
                        speed,
                        LocationUpdate.FUSED_LOCATION
                    )
                }
            }
        }
    }

    private fun initGpsLocation() {
        if(gpsLocationListener == null) {
            gpsLocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    if (previousGpsLocation == null) {
                        previousGpsLocation = location
                    }
                    gpsLocation = location
                    val locationDetail = LocationDetail(location, previousGpsLocation ?: location)
                    val gpsCalculatedSpeed: Double = locationDetail.getCalculatedSpeed()
                    onLocationChanged(location, gpsCalculatedSpeed, LocationUpdate.GPS)
                    previousGpsLocation = location
                }

                override fun onStatusChanged(
                    provider: String,
                    status: Int,
                    extras: Bundle
                ) {
                }

                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {
                    Toast.makeText(activity, "Please Enable GPS", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initNetworkLocation() {
        if(networkLocationListener == null) {
            networkLocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    if (permissionCheck.checkSelfPermission(LOCATION_PERMISSIONS)) {
                        if (previousNetworkLocation == null) {
                            previousNetworkLocation = location
                        }
                        val locationDetail =
                            LocationDetail(location, previousNetworkLocation ?: location)
                        val networkCalculatedSpeed: Double =
                            locationDetail.getCalculatedSpeed()
                        onLocationChanged(location, networkCalculatedSpeed, LocationUpdate.NETWORK)
                        previousNetworkLocation = location
                    }
                }

                override fun onStatusChanged(
                    provider: String,
                    status: Int,
                    extras: Bundle
                ) {
                }

                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {
                    onFailure(ErrorMessage.NETWORK_NOT_AVAILABLE)
                    return
                }
            }
        }
    }

    private fun getGpsLocation() {
        if (isGPSEnabled) {
            if (gpsLocation == null) {
                if (permissionCheck.checkSelfPermission(LOCATION_PERMISSIONS)) {
                    GlobalScope.launch(Dispatchers.IO) {
                        gpsLocationListener?.let {
                            gpsLocationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                updateInterval,
                                minimumDistance,
                                it,
                                Looper.getMainLooper()
                            )
                        }
                    }
                }
            } else {
                onSuccess(SuccessMessage.SERVICE_RUNNING)
            }
        } else {
            onFailure(ErrorMessage.GPS_NOT_AVAILABLE)
            return
        }
    }

    private fun getNetworkLocationUpdates() {
        if (isNetworkEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!permissionCheck.checkSelfPermission(LOCATION_PERMISSIONS)) {
                    return
                }
            }
            GlobalScope.launch(Dispatchers.IO) {
                networkLocationListener?.let {
                    networkLocationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        updateInterval,
                        minimumDistance,
                        it,
                        Looper.getMainLooper()
                    )
                }
            }
        } else {
            onFailure(ErrorMessage.NETWORK_NOT_AVAILABLE)
            return
        }
    }


    private fun getFusedLocationUpdates() {
        if (!permissionCheck.checkSelfPermission(LOCATION_PERMISSIONS)) {
            onFailure(ErrorMessage.PERMISSION_DENIED)
            return
        }
        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = updateInterval
        mLocationRequest?.fastestInterval = fastestInterval
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        val permission =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            GlobalScope.launch(Dispatchers.IO) {
                mFusedLocationProviderClient?.requestLocationUpdates(
                    mLocationRequest, locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            onFailure(ErrorMessage.PERMISSION_DENIED)
            return
        }
    }

    fun startLocationTracker(locationUpdate: LocationUpdate) {
        if (permissionCheck.checkSelfPermission(LOCATION_PERMISSIONS)) {
            when (locationUpdate) {
                LocationUpdate.GPS -> {
                    initGpsLocation()
                    getGpsLocation()
                }
                LocationUpdate.NETWORK -> {
                    initNetworkLocation()
                    getNetworkLocationUpdates()
                }
                LocationUpdate.FUSED_LOCATION -> {
                    initLocationCallBack()
                    getFusedLocationUpdates()
                }
                LocationUpdate.ALL -> {
                    initLocationCallBack()
                    getFusedLocationUpdates()
                }
            }
            onSuccess(SuccessMessage.SERVICE_STARTED)
        } else {
            onFailure(errorMessage = ErrorMessage.PERMISSION_DENIED)
        }
    }

    fun stopLocationTracker(locationUpdate: LocationUpdate) {
        when (locationUpdate) {
            LocationUpdate.GPS -> {
                gpsLocationListener?.let { gpsLocationManager?.removeUpdates(it) }
            }
            LocationUpdate.NETWORK -> {
                networkLocationListener?.let { networkLocationManager?.removeUpdates(it) }
            }
            LocationUpdate.FUSED_LOCATION -> {
                mFusedLocationProviderClient?.removeLocationUpdates(locationCallback)
            }
            LocationUpdate.ALL -> {
                gpsLocationListener?.let {
                    gpsLocationManager?.removeUpdates(it)
                    gpsLocationManager = null
                }
                networkLocationListener?.let {
                    networkLocationManager?.removeUpdates(it)
                    networkLocationManager = null
                }
                mFusedLocationProviderClient?.removeLocationUpdates(locationCallback)
            }
        }
    }

    protected abstract fun onLocationChanged(
        networkLocation: Location?,
        networkLocationSpeed: Double,
        locationUpdate: LocationUpdate
    )

    protected abstract fun onFailure(errorMessage: ErrorMessage)

    protected abstract fun onSuccess(successMessage: SuccessMessage)

}
