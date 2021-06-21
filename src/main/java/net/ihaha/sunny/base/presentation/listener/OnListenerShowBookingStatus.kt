package net.ihaha.sunny.base.presentation.listener


/**
 * Date: 27/05/2021.
 * @author SANG.
 * @version 1.0.0.
 */
interface OnListenerShowBookingStatus {
    fun showBookingStatusRejected()
    fun showBookingStatusRejectedItem()
    fun showBookingStatusCancelled()
    fun showBookingStatusPending()
    fun showBookingStatusConfirm()
    fun showBookingStatusDelivering()
    fun showBookingStatusDelivered()
}