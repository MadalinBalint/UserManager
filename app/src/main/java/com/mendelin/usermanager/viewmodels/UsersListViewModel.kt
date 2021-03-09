package com.mendelin.usermanager.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mendelin.usermanager.R
import com.mendelin.usermanager.common.OnAction
import com.mendelin.usermanager.models.UserObject
import com.mendelin.usermanager.networking.NetworkErrorHandler
import com.mendelin.usermanager.networking.UserManagerServiceProvider
import com.mendelin.usermanager.repository.UserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

class UsersListViewModel : ViewModel() {
    private val isLoading = MutableLiveData(false)
    private val usersList = MutableLiveData<List<UserObject>>()
    private val disposables = CompositeDisposable()
    private val error = MutableLiveData<String>()

    fun fetchUsersList(context: Context) {
        val repository = UserRepository(UserManagerServiceProvider.userApiService())

        isLoading.value = true
        disposables.add(
            /* Get first page of user data */
            repository.getUsersList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        if (response != null && !response.data.isNullOrEmpty()) {
                            /* Load last page with user data */
                            if (response.meta.pagination.pages != 1) {
                                repository.getUsersList(response.meta.pagination.pages)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        {
                                            isLoading.value = false
                                            error.value = ""
                                            setUsersList(it.data)
                                        },
                                        { exception2 ->
                                            /* Handle exception */
                                            isLoading.value = false
                                            val handler = NetworkErrorHandler(exception2)
                                            handler.handleErrors(context)
                                        }
                                    )
                            } else {
                                isLoading.value = false
                                error.value = ""
                                setUsersList(response.data)
                            }
                        } else {
                            /* Empty list */
                            val handler = NetworkErrorHandler(Exception(context.getString(R.string.error_empty_list)))
                            handler.handleErrors(context)
                        }
                    },
                    { exception ->
                        /* Handle exception */
                        isLoading.value = false
                        val handler = NetworkErrorHandler(exception)
                        handler.handleErrors(context)
                    }
                )
        )
    }

    fun deleteUser(id: Int, context: Context, action: OnAction?) {
        val repository = UserRepository(UserManagerServiceProvider.userApiService())

        isLoading.value = true
        disposables.add(
            repository.deleteUser(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        isLoading.value = false
                        if (response.code == 204) {
                            Timber.e("User $id successfully deleted")
                            action?.invoke()
                        } else  {
                            val handler = NetworkErrorHandler(Exception(response?.message))
                            handler.handleErrors(context)
                        }
                    },
                    { exception ->
                        /* Handle exception */
                        isLoading.value = false
                        val handler = NetworkErrorHandler(exception)
                        handler.handleErrors(context)
                    }
                )
        )
    }

    fun getLoadingObservable(): LiveData<Boolean> = isLoading

    fun getUsersList(): LiveData<List<UserObject>> = usersList

    fun setErrorFilter(value: String) {
        error.value = value
    }

    fun getErrorFilter(): LiveData<String> = error

    private fun setUsersList(list: List<UserObject>) {
        usersList.postValue(list)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}