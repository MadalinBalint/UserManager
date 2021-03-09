package com.mendelin.usermanager.repository

import com.mendelin.usermanager.models.UserCreationObject
import com.mendelin.usermanager.networking.UserApiService

class UserRepository(private val service: UserApiService) {
    fun getUsersList(page: Int = 1) =
        service.listUsers(page)

    fun createUser(name: String, email: String) =
        service.createUser(UserCreationObject(name, "Male", email, "Active"))

    fun deleteUser(id: Int) =
        service.deleteUser(id)
}