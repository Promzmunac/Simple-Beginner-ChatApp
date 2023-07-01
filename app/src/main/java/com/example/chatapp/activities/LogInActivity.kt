package com.example.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.loginButtonLogin.setOnClickListener {
            performLogin()
        }

        binding.dontHaveAccount.setOnClickListener {
            val intent = Intent(this@LogInActivity, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun performLogin() {
        val email = binding.emailTextEditLogin.text.toString()
        val password = binding.passwordTextEditLogin.text.toString()
        if (email.isEmpty() || password.isEmpty()) {

            binding.emailTextEditLogin.error ="Email is Empty"
            binding.passwordTextEditLogin.error = "Password is Empty"

            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this@LogInActivity, HomeActivity::class.java)
                    finish()
                    startActivity(intent)

                    Toast.makeText(this@LogInActivity, "Login successfully", Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(this@LogInActivity, it.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }

            }
    }
}