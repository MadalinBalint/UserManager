package com.mendelin.usermanager.interfaces

import android.content.Context
import com.mendelin.usermanager.common.OnAction

interface NetworkErrors {
    /* HTTP error codes 401 */
    fun onClientNotAuthorized(context: Context, action: OnAction?)

    /* HTTP error codes 400, 402-499 */
    fun onClientError(context: Context)

    /* HTTP error codes 500-599 */
    fun onServerError(context: Context)

    /* for IO errors - a network or conversion error happened */
    fun onIoError(context: Context)

    /* other errors that can't be included in any category */
    fun onUnexpectedError(context: Context)
}