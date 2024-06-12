package com.dicoding.suitcheck.presentation.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.suitcheck.data.model.Data
import com.dicoding.suitcheck.databinding.ItemUserBinding

class UserAdapter(private val users: MutableList<Data>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var onItemClick: ((Data) -> Unit)? = null

    inner class UserViewHolder(private val binding:ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Data) {
            with(binding) {
                firstNameTextView.text = user.firstName
                lastNameTextView.text = user.lastName
                emailTextView.text = user.email
                Glide.with(root.context)
                    .load(user.avatar)
                    .into(avatarImageView)

                root.setOnClickListener {
                    onItemClick?.invoke(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun submitList(newUsers: List<Data>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    fun addUsers(newUsers: List<Data>) {
        val startPosition = users.size
        users.addAll(newUsers)
        notifyItemRangeInserted(startPosition, newUsers.size)
    }
}
