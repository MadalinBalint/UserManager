package com.mendelin.usermanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mendelin.usermanager.ItemUserBinding
import com.mendelin.usermanager.common.ResourceHelper
import com.mendelin.usermanager.models.UserObject
import com.mendelin.usermanager.viewmodels.UsersListViewModel
import timber.log.Timber

class UsersListAdapter(private val usersListViewModel: UsersListViewModel) :
    ListAdapter<UserObject, UsersListAdapter.UsersViewHolder>(DiffCallbackSearchAdapter) {
    private val userList: ArrayList<UserObject> = arrayListOf()
    lateinit var context: Context

    class UsersViewHolder(var binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(property: UserObject) {
            binding.property = property
            binding.executePendingBindings()
        }
    }

    companion object DiffCallbackSearchAdapter : DiffUtil.ItemCallback<UserObject>() {
        override fun areItemsTheSame(oldItem: UserObject, newItem: UserObject): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserObject, newItem: UserObject): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        context = parent.context
        return UsersViewHolder(
            ItemUserBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = userList[position]
        holder.bind(item)

        with(holder.binding) {
            layoutUser.setOnLongClickListener {
                ResourceHelper.showAlertMsgAction(
                    context,
                    "Delete user ${item.name}",
                    "Are you sure you want to remove this user?"
                ) {
                    // Delete user
                    Timber.e("ID = ${item.id}")
                    usersListViewModel.deleteUser(item.id, context) {
                        usersListViewModel.fetchUsersList(context)
                    }
                }
                return@setOnLongClickListener true
            }
        }
    }

    fun setList(list: List<UserObject>, sorted: Boolean = false) {
        if (list.isEmpty()) {
            usersListViewModel.setErrorFilter("empty list")
        } else {
            if (sorted) list.sortedBy { it.name }

            userList.apply {
                clear()
                addAll(list)
            }

            submitList(userList)
            notifyDataSetChanged()
        }
    }
}