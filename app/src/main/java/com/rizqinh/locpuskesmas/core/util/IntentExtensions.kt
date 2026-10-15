package com.rizqinh.locpuskesmas.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.dialPhone(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    startActivity(intent)
}

fun Context.openGoogleMapsDirection(lat: Double, lng: Double) {
    val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
    } else {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$lat,$lng"))
        startActivity(browserIntent)
    }
}