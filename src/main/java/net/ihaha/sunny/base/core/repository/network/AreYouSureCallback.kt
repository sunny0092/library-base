package net.ihaha.sunny.base.core.repository.network

interface AreYouSureCallback {
    fun proceed()
    fun cancel()
}