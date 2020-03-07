@file:JvmName("Util")

package com.fermimn.gamewishlist.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

fun convertDpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}

fun isNetworkAvailable(context: Context): Boolean {

    val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager.activeNetwork != null
    } else {
        connectivityManager.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }
}
