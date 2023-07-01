package com.example.chatapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.activities.ChatMessageActivity
import com.example.chatapp.databinding.UserItemBinding
import com.example.chatapp.dataclass.User


class UserAdapter(val context: Context, var userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layout = UserItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return UserViewHolder(layout)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = userList[position]

        //holder.binding.userListImage.setImageURI(Uri.parse(current.selectedPhotoUri.toString()))

        holder.binding.userListName.text = current.name

        holder.itemView.setOnClickListener {

            val intent = Intent(context, ChatMessageActivity::class.java)
            intent.putExtra("NAME", current.name)
            intent.putExtra("IMAGE", current.selectedPhotoUri)
            intent.putExtra("UID", current.uid)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size

    }

    class UserViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)


    /* fun deleteItem(pos:Int){
          userList.removeAt(pos)
          notifyItemRemoved(pos)
      }*/

    //Picasso.get().load(current.selectedPhotoUri).into(holder.binding.userListImage)
    /*  Glide.with(context)
          .load(userList[position].selectedPhotoUri)
          .into(holder.binding.userListImage)
*/

    //set onClick listener to the rv items. this allows user to click and edit the recyclerview

}


