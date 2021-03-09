package com.mendelin.usermanager.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mendelin.usermanager.FragmentUsersListBinding
import com.mendelin.usermanager.R
import com.mendelin.usermanager.adapters.UsersListAdapter
import com.mendelin.usermanager.interfaces.ActivityCallback
import com.mendelin.usermanager.ui.custom_views.MarginItemDecoration
import com.mendelin.usermanager.viewmodels.UsersListViewModel

class UsersListFragment : Fragment() {
    private var binding: FragmentUsersListBinding? = null
    private val usersListViewModel: UsersListViewModel by activityViewModels { defaultViewModelProviderFactory }
    private lateinit var usersAdapter: UsersListAdapter
    private var listFetched = false

    var activityCallback: ActivityCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ActivityCallback) {
            activityCallback = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding?.root!!
    }

    override fun onResume() {
        super.onResume()
        activityCallback?.showToolbar(true)
        activityCallback?.setToolbarTitle(getString(R.string.users_list_title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersAdapter = UsersListAdapter(usersListViewModel)

        binding?.recyclerUsers?.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            isNestedScrollingEnabled = true

            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recyclerview_padding).toInt(),
                    resources.getDimension(R.dimen.recyclerview_padding).toInt()
                )
            )
        }

        if (!listFetched) {
            usersListViewModel.fetchUsersList(requireContext())
            listFetched = true
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        usersListViewModel.getLoadingObservable().observe(viewLifecycleOwner, ::setLoadingProgress)

        /* Show the list of users */
        usersListViewModel.getUsersList().observe(viewLifecycleOwner, { devices ->
            devices?.let {
                usersAdapter.setList(it, true)
                binding?.layoutUsersList?.visibility = View.VISIBLE
            }
        })
    }

    private fun setLoadingProgress(status: Boolean) {
        binding?.progressUsersList?.visibility = if (status) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}