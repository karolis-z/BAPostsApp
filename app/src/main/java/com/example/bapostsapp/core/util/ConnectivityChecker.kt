package com.example.bapostsapp.core.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ConnectivityChecker {
    /**
     * @return [Boolean] indicating whether the device is online
     */
    fun isOnline(): Boolean
}

class ConnectivityCheckerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ConnectivityChecker {

    override fun isOnline(): Boolean {
        val cm = ContextCompat.getSystemService(context, ConnectivityManager::class.java)
        val capabilities = cm?.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}