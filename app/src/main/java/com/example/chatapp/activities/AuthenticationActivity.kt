package com.example.chatapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivityAuthenticationBinding
import com.example.chatapp.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    private var selectedPhotoUri: Uri? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mDbref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()
       // VerifyUserIsNotLoggedIn()


        binding.avatarImageviewRegister.setOnClickListener {
            toGallery()
        }
        binding.fabImg.setOnClickListener {
            toGallery()
        }
        binding.alreadyGotAccount.setOnClickListener {
            val intent = Intent(this@AuthenticationActivity, LogInActivity::class.java)
            finish()
            startActivity(intent)
        }

        binding.registerBtn.setOnClickListener {
            performRegister()
        }

    }

    @SuppressLint("IntentReset")
    private fun toGallery(){
        // this function when called will allow you to access your phone gallery.
        val gallery =  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        gallery.type = "image/*"
        startActivityForResult(gallery,  REQUEST_CODE) //request code is a companion object
    }

    //this code allows the image to be drawn to the imageview  of the activity U.I
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            binding.avatarImageviewRegister.setImageURI(data?.data)
            selectedPhotoUri = (data ?: return).data
            Log.d("MESSAGE", selectedPhotoUri.toString())
        }
    }

    companion object {
        const val REQUEST_CODE = 100
    }


    private fun performRegister() {

        val email = binding.EmailTxt.text.toString()
        val username = binding.usernameSignUp.text.toString()
        val password = binding.passwordSignsUp.text.toString()

        if(email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            binding.EmailTxt.error = " Fill email forms!"
            binding.usernameSignUp.error = " Fill username forms!"
            binding.passwordSignsUp.error = " Fill password forms!"
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    addUserToDatabase(username, email,
                        firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                    )

                    val intent = Intent(this@AuthenticationActivity, HomeActivity::class.java)
                    finish()
                    startActivity(intent)
                }
            }
            .addOnFailureListener {

                Toast.makeText(this,it.localizedMessage?.toString()!!, Toast.LENGTH_LONG).show()

            }
    }

    override fun onStart() {
        super.onStart()
        VerifyUserIsNotLoggedIn()
    }

    private fun addUserToDatabase(name: String, email: String, uid: String ){
        mDbref = FirebaseDatabase.getInstance().reference
        mDbref.child("user").child(uid).setValue (User(name,email,uid,selectedPhotoUri.toString()))
    }

    private fun VerifyUserIsNotLoggedIn(){

        val uid = firebaseAuth.currentUser?.uid
        if (uid != null){
            if (uid.isNotEmpty()) {
                val intent = Intent(this, LogInActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
    }
   /* if(user != null){
        //Go to dashboard
        splash_logo.alpha = 0f
        splash_logo.animate().setDuration(3000).alpha(1f).withEndAction {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()
        }*/
}
