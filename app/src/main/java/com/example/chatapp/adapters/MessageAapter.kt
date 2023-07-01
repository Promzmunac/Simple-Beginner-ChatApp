package com.example.chatapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatapp.databinding.RecieveMessageBinding
import com.example.chatapp.databinding.SendMessageBinding
import com.example.chatapp.dataclass.Messge
import com.google.firebase.auth.FirebaseAuth

class MessageAapter(val context: Context, var messageList: ArrayList<Messge>) :
    RecyclerView.Adapter<ViewHolder>() {
    val ITEM_RECEIVED = 1
    val ITEM_SEND = 2


    class SentViewHolder(binding: SendMessageBinding) : ViewHolder(binding.root) {
        val sentMessage = binding.sentMessages
    }

    class RecievedViewHolder(binding: RecieveMessageBinding) :
        ViewHolder(binding.root) {
        val recievedMessages = binding.recievedMessages

    }

    //override a method to help return the view
    override fun getItemViewType(position: Int): Int {
        val current = messageList[position]
        //if the uid of the current user matches the sender id. then we will inflate send viewHolder

        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(current.senderId)) {
            ITEM_SEND

        } else {
            ITEM_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflate the layout according to the return type
        return if (viewType == 1) {
            val layout = RecieveMessageBinding.inflate(LayoutInflater.from(context), parent, false)
            RecievedViewHolder(layout)
        } else {
            val layout = SendMessageBinding.inflate(LayoutInflater.from(context), parent, false)
            SentViewHolder(layout)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java){
            //Do send view holder functions
            holder as SentViewHolder
            holder.sentMessage.text = current.message

        } else {
            //Do stuff for Receiver holder functions
            holder as RecievedViewHolder
            holder.recievedMessages.text = current.message
        }

    }


}