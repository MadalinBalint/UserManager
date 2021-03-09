package com.mendelin.usermanager.networking

import android.content.Context
import com.mendelin.usermanager.R
import com.mendelin.usermanager.common.OnAction
import com.mendelin.usermanager.common.ResourceHelper
import com.mendelin.usermanager.interfaces.NetworkErrors
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

typealias OnNetworkSucces = () -> Unit
typealias OnNetworkError = (exception: Throwable?) -> Unit

class NetworkErrorHandler(private val exception: Throwable?) : NetworkErrors {

    override fun onClientNotAuthorized(context: Context, action: OnAction?) {
        ResourceHelper.showAlertMsgAction(
            context,
            context.getString(R.string.alert_error),
            context.getString(R.string.err_user_relogin),
            action
        )
    }

    override fun onClientError(context: Context) {
        ResourceHelper.showAlertMsg(
            context,
            context.getString(R.string.alert_error),
            exception?.localizedMessage ?: context.getString(R.string.err_unknown)
        )
    }

    override fun onServerError(context: Context) {
        ResourceHelper.showAlertMsg(
            context,
            context.getString(R.string.alert_error),
            exception?.localizedMessage ?: context.getString(R.string.err_unknown)
        )
    }

    override fun onIoError(context: Context) {
        ResourceHelper.showAlertMsg(
            context,
            context.getString(R.string.alert_error),
            exception?.localizedMessage ?: context.getString(R.string.err_unknown)
        )
    }

    override fun onUnexpectedError(context: Context) {
        ResourceHelper.showAlertMsg(
            context,
            context.getString(R.string.alert_error),
            context.getString(R.string.err_unexpected)
        )
    }

    fun handleErrors(context: Context) {
        Timber.e(exception?.localizedMessage)
        when (exception) {
            is HttpException -> {
                Timber.e("HttpException")

                when (exception.code()) {
                    401 -> {
                        onClientNotAuthorized(context) {
                            // TODO Send user to login again with its credentials
                        }
                    }
                    400, in 402..499 -> {
                        onClientError(context)
                    }

                    in 500..599 -> {
                        onServerError(context)
                    }
                }
            }
            is IOException -> {
                // TODO Send message to user to check the internet connection
                Timber.e("IOException")
                onIoError(context)
            }
            else -> {
                Timber.e("Unexpected Error")
                onUnexpectedError(context)
            }
        }
    }
}