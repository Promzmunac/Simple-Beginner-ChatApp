package com.example.chatapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.adapters.MessageAapter
import com.example.chatapp.databinding.ActivityChatMessageBinding
import com.example.chatapp.dataclass.Messge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatMessageBinding
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageText: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var messageAapter: MessageAapter
    private lateinit var messageList: ArrayList<Messge>
    private lateinit var mDbref: DatabaseReference


    //create an object of sender room and to create a unique room so that
    // the message is private and des not reflect on every user chat page
    var receiverRoom: String? = null
    var senderRoom: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //get intents from putExtra in user Adapter
        val name = intent.getStringExtra("NAME")
        val receiverUid = intent.getStringExtra("UID")

        supportActionBar?.title = name

        //initialise database references
        mDbref = FirebaseDatabase.getInstance().reference

        //update the rooms
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid


        messageRecyclerView = binding.messageChatRv
        messageText = binding.messageChatMsg
        sendBtn = binding.sendImg
        messageList = ArrayList()
        messageAapter = MessageAapter(this, messageList)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAapter


        //logic to add data to the recyclerview
        mDbref.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (i in snapshot.children){
                        val message = i.getValue(Messge::class.java)
                        messageList.add(message!!)
                    }
                    messageAapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendBtn.setOnClickListener {
            //we send the message to the database and from there the messages
            // is sent or received by the user
            val msg = messageText.text.toString()
            val msgObject = Messge(msg, senderUid)

            if (msg == "") return@setOnClickListener

            mDbref.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(msgObject) .addOnSuccessListener {
                    mDbref.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(msgObject)
                }
            //when message is sent,clear the message box so another message could be sent
            messageText.setText("")
        }
    }
}
