package com.mendelin.usermanager.networking

import com.mendelin.usermanager.BuildConfig
import com.mendelin.usermanager.models.CreateUserResponse
import com.mendelin.usermanager.models.UserCreationObject
import com.mendelin.usermanager.models.UserListResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

/* User information */
interface UserApiService {
    @GET(BuildConfig.ENDPOINT_LIST_USERS)
    fun listUsers(@Query(BuildConfig.QUERY_PAGE) page: Int): Single<UserListResponse>

    @POST(BuildConfig.ENDPOINT_CREATE_USER)
    fun createUser(@Body user: UserCreationObject): Single<CreateUserResponse>

    @DELETE(BuildConfig.ENDPOINT_DELETE_USER)
    fun deleteUser(@Path(BuildConfig.QUERY_ID) id: Int): Single<UserListResponse>
}