package com.mendelin.usermanager

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.mendelin.usermanager.networking.UserManagerServiceProvider
import com.mendelin.usermanager.repository.UserRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception
import java.util.*

@RunWith(AndroidJUnit4ClassRunner::class)
class UserRepositoryTest {
    lateinit var repository: UserRepository

    @Before
    fun initEach() {
        val gson = UserManagerServiceProvider.getGson()
        val gsonFactory = UserManagerServiceProvider.getGsonFactory(gson)
        val rxJavaFactory = UserManagerServiceProvider.getRxJavaFactory()
        val apiService = UserManagerServiceProvider.buildUserApiService(gsonFactory, rxJavaFactory)
        repository = UserRepository(apiService)
    }

    @Test
    fun getUsersList_correctDefaultStartPage_returnsResponse() {
        val testObserver = repository
            .getUsersList()
            .test()
        testObserver.await()

        testObserver
            .assertNoErrors()
            .assertValue {
                it.code == 200 && it.data.isNotEmpty()
            }
    }

    @Test
    fun getUsersList_wrongStartPage_returnsResponseWithEmptyData() {
        val testObserver = repository
            .getUsersList(Int.MAX_VALUE)
            .test()
        testObserver.await()

        testObserver
            .assertNoErrors()
            .assertValue {
                it.code == 200 && it.data.isEmpty()
            }
    }

    @Test
    fun deleteUser_wrongId_returnsDataWithErrorMessages() {
        val testObserver = repository
            .deleteUser(Int.MAX_VALUE)
            .test()
        testObserver.await()

        testObserver
            .assertError(Exception::class.java)
    }

    @Test
    fun createUser_correctData_returnsSuccess() {
        val userName = UUID.randomUUID().toString()
        val email = UUID.randomUUID().toString() + "@test.com"

        val testObserver = repository
            .createUser(userName, email)
            .test()
        testObserver.await()

        testObserver
            .assertNoErrors()
            .assertValue {
                it.code == 201 && it.data.name == userName && it.data.email == email
            }
    }

    @Test
    fun createUser_emptyData_returnsDataWithErrorMessages() {
        val userName = ""
        val email = ""

        val testObserver = repository
            .createUser(userName, email)
            .test()
        testObserver.await()

        testObserver
            .assertError(Exception::class.java)
    }
}