package com.mendelin.usermanager.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.mendelin.usermanager.R
import com.mendelin.usermanager.constants.FragmentType
import com.mendelin.usermanager.databinding.ActivityUserManagerBinding
import com.mendelin.usermanager.interfaces.ActivityCallback
import com.mendelin.usermanager.ui.custom_views.CreateUserDialog
import com.mendelin.usermanager.viewmodels.UsersListViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserManagerActivity : AppCompatActivity(), ActivityCallback {
    companion object {
        const val DOUBLE_BACK_PRESS_DELAY = 2000L
    }

    private lateinit var binding: ActivityUserManagerBinding
    private var selectedItem = FragmentType.USERS_LIST_FRAGMENT
    private val usersListViewModel: UsersListViewModel by viewModels { defaultViewModelProviderFactory }

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityUserManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        binding.fabCreateUser.setOnClickListener {
            val createUserDialog = CreateUserDialog()
            createUserDialog.setOnSuccessListener {
                usersListViewModel.fetchUsersList(this)
            }
            createUserDialog.show(supportFragmentManager, "createUser")
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        usersListViewModel.getLoadingObservable().observe(this, ::setLoadingProgress)
    }

    private fun setLoadingProgress(status: Boolean) {
        binding.progressMain.visibility = if (status) View.VISIBLE else View.INVISIBLE
    }

    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        val fragments = navController.backStack.size

        when {
            fragments == 2 -> {
                val backToast =
                    Toast.makeText(this, R.string.users_list_press_back_again, Toast.LENGTH_SHORT)

                if (doubleBackToExitPressedOnce) {
                    backToast.cancel()
                    finish()
                }

                doubleBackToExitPressedOnce = true
                backToast.show()

                /* Reset variable after 2 seconds */
                GlobalScope.launch {
                    delay(DOUBLE_BACK_PRESS_DELAY)
                    doubleBackToExitPressedOnce = false
                    backToast.cancel()
                }
            }

            navController.backStack.size > 2 -> {
                navController.popBackStack()

                val frag = getVisibleFragment()
                if (frag != FragmentType.UNKNOWN) {
                    selectNavItem(frag)
                }
            }

            else -> {
                selectNavItem(getVisibleFragment())
                super.onBackPressed()
            }
        }
    }

    private fun selectNavItem(index: FragmentType) {
        selectedItem = index
    }

    private fun getVisibleFragment(): FragmentType {
        val navController = Navigation.findNavController(this, R.id.navHostFragment)

        return when (navController.currentDestination?.id ?: -1) {
            R.id.usersListFragment -> FragmentType.USERS_LIST_FRAGMENT

            else -> FragmentType.UNKNOWN
        }
    }

    override fun showToolbar(isShown: Boolean) {
        binding.toolbar.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    override fun setToolbarTitle(title: String) {
        binding.toolbar.title = title
    }
}