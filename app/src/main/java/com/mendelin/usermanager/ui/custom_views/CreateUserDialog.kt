package com.mendelin.usermanager.ui.custom_views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.mendelin.usermanager.FragmentCreateUserBinding
import com.mendelin.usermanager.networking.NetworkErrorHandler
import com.mendelin.usermanager.viewmodels.CreateUserViewModel
import timber.log.Timber

typealias CreateUserSuccess = () -> Unit

class CreateUserDialog() : AppCompatDialogFragment() {

    private var onSuccess: CreateUserSuccess? = null
    private var binding: FragmentCreateUserBinding? = null
    private val createUserViewModel: CreateUserViewModel by viewModels { defaultViewModelProviderFactory }

    fun setOnSuccessListener(listener: CreateUserSuccess) {
        onSuccess = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentCreateUserBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())

        builder.setView(binding?.root)
            .setCancelable(false)

        binding?.btnOk?.setOnClickListener {
            observeViewModel()
        }

        binding?.btnCancel?.setOnClickListener {
            dismiss()
        }

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun observeViewModel() {
        createUserViewModel.getLoadingObservable().observe(this, ::setLoadingProgress)

        val name = binding?.editName?.text.toString().trim()
        val email = binding?.editEmail?.text.toString().trim()

        createUserViewModel.createUser(name, email,
            /* User created succesfully */
            handlerSuccess = {
                dismiss()
                onSuccess?.invoke()
            },

            /* Error when creating user */
            handlerError = { exception ->
                dismiss()

                val handler = NetworkErrorHandler(exception)
                handler.handleErrors(requireContext())
            })
    }

    private fun setLoadingProgress(status: Boolean) {
        binding?.progressCreateUser?.visibility = if (status) View.VISIBLE else View.INVISIBLE
    }
}