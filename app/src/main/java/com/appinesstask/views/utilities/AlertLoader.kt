package com.appinesstask.views.utilities

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.appinesstask.databinding.ProgressLayoutBinding

object AlertLoader {

    //Loader
    private var customDialog: AlertDialog? = null

    fun showProgress(context: Context) {
        hideProgress()
        val customAlertBuilder = AlertDialog.Builder(context)
        val customAlertView =
            ProgressLayoutBinding.inflate(LayoutInflater.from(context), null, false)
        customAlertBuilder.setView(customAlertView.root)
        customAlertBuilder.setCancelable(false)

        if (customDialog == null) {
            customDialog = customAlertBuilder.create()
        }

        customDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialog?.show()
    }

    fun hideProgress() {
        try {
            if (customDialog != null && customDialog?.isShowing == true) {
                customDialog?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}