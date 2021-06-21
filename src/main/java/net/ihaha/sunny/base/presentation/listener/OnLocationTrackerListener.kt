package net.ihaha.sunny.base.presentation.listener

import android.location.Location
import net.ihaha.sunny.location.ErrorMessage
import net.ihaha.sunny.location.SuccessMessage


/**
 * Date: 20/05/2021.
 * @author SANG.
 * @version 1.0.0.
 */
interface OnLocationTrackerListener {
    fun onLocation(networkLocation: Location?)
    fun onFailure(errorMessage: ErrorMessage)
    fun onSuccess(successMessage: SuccessMessage)
}