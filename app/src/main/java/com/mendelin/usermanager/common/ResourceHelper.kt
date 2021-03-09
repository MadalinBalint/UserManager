package com.mendelin.usermanager.common

import android.content.Context
import android.text.format.DateUtils
import com.mendelin.usermanager.R
import com.mendelin.usermanager.ui.custom_views.AlertBox
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


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

    fun getCreatedAtRelative(createdAt: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("IST")
        var relative: CharSequence? = null
        try {
            relative = DateUtils.getRelativeTimeSpanString(
                df.parse(createdAt).time,
                Date().time,
                0L,
                DateUtils.FORMAT_ABBREV_ALL
            )
        } catch (e: ParseException) {
            Timber.e(e.localizedMessage)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return relative?.toString()?.replace(".", " ")?.replace("  "," ") ?: createdAt
    }
}