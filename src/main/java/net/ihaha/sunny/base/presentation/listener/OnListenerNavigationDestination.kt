package net.ihaha.sunny.base.presentation.listener

import android.os.Bundle
import androidx.navigation.fragment.FragmentNavigator


/**
 * Date: 27/04/2021.
 * @author SANG.
 * @version 1.0.0.
 */
interface OnListenerNavigationDestination {
    fun onNavigation(destination: FragmentNavigator.Destination, arguments: Bundle?)
}