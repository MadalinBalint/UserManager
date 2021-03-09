package com.mendelin.usermanager.common

import android.content.Context
import com.mendelin.usermanager.R
import com.mendelin.usermanager.ui.custom_views.AlertBox

typealias OnAction = () -> Unit

object ResourceHelper {

    fun showAlertMsg(context: Context, title: String, msg: String) {
        AlertBox().apply {
            setPositiveButtonListener { dialog, _ ->
                dialog.dismiss()
            }

            showAlert(context, title, msg, context.getString(R.string.alert_ok), null)
        }
    }

    fun showAlertMsgAction(context: Context, title: String, msg: String, action: OnAction?) {
        AlertBox().apply {
            setPositiveButtonListener { dialog, _ ->
                dialog.dismiss()
                action?.invoke()
            }

            setNegativeButtonListener { dialog, _ ->
                dialog.dismiss()
            }

            showAlert(
                context,
                title,
                msg,
                context.getString(R.string.alert_yes),
                context.getString(R.string.alert_no)
            )
        }
    }
}