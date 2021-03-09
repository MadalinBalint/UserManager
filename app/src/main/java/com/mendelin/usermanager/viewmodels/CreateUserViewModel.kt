package com.mendelin.usermanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mendelin.usermanager.networking.OnNetworkError
import com.mendelin.usermanager.networking.OnNetworkSucces
import com.mendelin.usermanager.networking.UserManagerServiceProvider
import com.mendelin.usermanager.repository.UserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CreateUserViewModel : ViewModel() {
    private val isLoading = MutableLiveData(false)
    private val disposables = CompositeDisposable()

    fun getLoadingObservable(): LiveData<Boolean> = isLoading

    fun createUser(
        name: String, email: String,
        handlerSuccess: OnNetworkSucces?,
        handlerError: OnNetworkError?
    ) {
        val repository = UserRepository(UserManagerServiceProvider.userApiService())

        isLoading.value = true
        disposables.add(
            repository.createUser(name, email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        isLoading.value = false

                        /* User created successfully */
                        if (response.code == 201) {
                            handlerSuccess?.invoke()
                        } else {
                            /* Error or missing parameter */
                            handlerError?.invoke(Exception("Error when trying to create user (empty parameter or existing email)"))
                        }
                    },
                    { exception ->
                        isLoading.value = false
                        handlerError?.invoke(exception)
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}