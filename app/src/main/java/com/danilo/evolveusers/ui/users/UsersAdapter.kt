package com.danilo.evolveusers.ui.users

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danilo.evolveusers.data.model.User
import com.danilo.evolveusers.databinding.UserBinding
import android.view.LayoutInflater


class UsersAdapter (private val users: List<User>,
                    private val onItemClick:((user : User) -> Unit)): RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding, onItemClick)

    }

    override fun onBindViewHolder(holder: UsersAdapter.UserViewHolder, position: Int) {
        holder.bindView(users[position])
    }

    override fun getItemCount(): Int {

        return users.count()
    }


    inner class UserViewHolder(private val binding : UserBinding, private val onItemClick: (user : User) -> Unit): RecyclerView.ViewHolder(binding.root){

        fun bindView(user : User) {

            binding.txtNome.text = user.nome
            binding.txtEmail.text = user.email

            itemView.setOnClickListener{
                onItemClick.invoke(user)
            }

        }
    }
}
