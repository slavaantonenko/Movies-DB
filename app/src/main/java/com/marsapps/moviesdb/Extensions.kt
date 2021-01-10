package com.marsapps.moviesdb

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.marsapps.moviesdb.Constants.WebView.YOUTUBE_SEARCH_QUERY
import com.marsapps.moviesdb.Constants.WebView.YOUTUBE_URL
import kotlinx.android.synthetic.main.dialog_webview.view.*
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.math.roundToInt

val Int.dp2px: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

fun LatLng.getAddressFromLocation(context: Context): Address? {
    val gcd = Geocoder(context, Locale.ENGLISH)
    var addresses: MutableList<Address>? = null

    try {
        addresses = gcd.getFromLocation(this.latitude, this.longitude, 10)
    }
    catch (e: InvocationTargetException) {
        e.printStackTrace()
    }
    catch (e: IOException) {
        e.printStackTrace()
    }

    addresses?.let {
        if (addresses.size > 0) {
            val address = addresses[0]
            var searchLocality = address.locality.isNullOrEmpty()
            var searchCountry = address.countryName.isNullOrEmpty()

            if (searchLocality || searchCountry) {
                if (addresses.size > 1) {
                    for (i in 1 until addresses.size) {
                        if (searchLocality && !addresses[i].locality.isNullOrEmpty()) {
                            address.locality = addresses[i].locality
                            searchLocality = false
                        }

                        if (searchCountry && !addresses[i].countryName.isNullOrEmpty()) {
                            address.countryName = addresses[i].countryName
                            searchCountry = false
                        }

                        if (!(searchLocality || searchCountry)) {
                            return address
                        }
                    }

                    return null
                }
                else {
                    return null
                }
            }

            return address
        }
    }

    return null
}

fun ArrayList<String>.convertToString(): String {
    return this.joinToString(separator = ",")
}

fun String.openYoutube(context: Context, title: String, pageLoaded: (() -> Unit) = {}) {
    val alertDialog: AlertDialog
    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_webview, null)
    val builder = AlertDialog.Builder(context)


    builder.setView(dialogView)
    alertDialog = builder.create()

    dialogView.web_view_youtube.settings.javaScriptEnabled = true
    dialogView.web_view_youtube.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            pageLoaded()
            alertDialog.show()
            super.onPageFinished(view, url)
        }
    }

    dialogView.web_view_youtube.loadUrl("$YOUTUBE_URL$YOUTUBE_SEARCH_QUERY${title.replace(" ", "+")}")
}

/*
 * PERMISSIONS
 */
fun String.getCode(): Int = this.toByteArray().sum()

fun Activity.checkPermissionGranted(permission: String): Boolean = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
fun Activity.requestLocationPermission(): Boolean = askForPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION, locationPermissionCode())

fun locationPermissionCode(): Int = android.Manifest.permission.ACCESS_FINE_LOCATION.getCode()

fun askForPermission(activity: Activity, permission: String, requestCode: Int): Boolean {
    return if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
        activity.requestPermissions(arrayOf(permission), requestCode)
        false
    }
    else {
        true
    }
}
