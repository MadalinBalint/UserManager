package com.mendelin.usermanager.ui.custom_views

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class AlertBox : AppCompatActivity() {
    private var listenerPositiveButton: DialogInterface.OnClickListener? = null
    private var listenerNegativeButton: DialogInterface.OnClickListener? = null

    fun setPositiveButtonListener(listener: DialogInterface.OnClickListener) {
        listenerPositiveButton = listener
    }

    fun setNegativeButtonListener(listener: DialogInterface.OnClickListener) {
        listenerNegativeButton = listener
    }

    fun showAlert(
        context: Context,
        title: String,
        message: String,
        btnPositive: String?,
        btnNegative: String?
    ) {
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)

            listenerPositiveButton?.let { setPositiveButton(btnPositive, it) }
            listenerNegativeButton?.let { setNegativeButton(btnNegative, it) }

            create().apply {
                if (!isShowing) {
                    show()
                }
            }
        }
    }
}