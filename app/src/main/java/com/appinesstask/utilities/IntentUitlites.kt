package com.appinesstask.utilities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.appinesstask.R
import com.appinesstask.extensions.showToast

fun Context.openDialPad(phone: String) {

    kotlin.runCatching {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
            putExtra(Intent.EXTRA_PHONE_NUMBER, phone)
        }
        ContextCompat.startActivity(this, Intent.createChooser(intent, getString(R.string.dial_pad)), null)
    }.onSuccess {
        showToast(getString(R.string.opening_dial_pad))
    }.onFailure {
        showToast(getString(R.string.no_application_found))
    }
}

fun Context.sendMessage(phone: String){
    kotlin.runCatching {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("sms:$phone")
        }
        ContextCompat.startActivity(this, Intent.createChooser(intent, getString(R.string.messaging_app)), null)
    }.onSuccess {
        showToast(getString(R.string.opening_messaging_app))
    }.onFailure {
        showToast(getString(R.string.no_application_found))
    }
}