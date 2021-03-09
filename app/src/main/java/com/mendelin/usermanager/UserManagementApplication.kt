package com.mendelin.usermanager

import android.app.Application
import com.mendelin.usermanager.logging.TimberPlant

class UserManagementApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        TimberPlant.plantTimberDebugLogger()
    }
}