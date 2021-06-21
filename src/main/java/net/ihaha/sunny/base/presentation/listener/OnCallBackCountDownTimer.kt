package net.ihaha.sunny.base.presentation.listener


/**
 * Date: 15/06/2021.
 * @author SANG.
 * @version 1.0.0.
 */
interface OnCallBackCountDownTimer {
    fun onTick(timer: Long)
    fun onFinish()
}