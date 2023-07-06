package com.example.tkt.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tkt.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth.currentUser?.let { navigateToFeedActivity() }

        binding.createNewAccountTextView.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        with(binding) {
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                logIn(email, password)
                finish()
            }
        }
    }

    private fun navigateToFeedActivity() {
        val intent = Intent(this, TicketFeedActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun logIn(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                navigateToFeedActivity()
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        } else Toast.makeText(this, "შეავსეთ ცარიელი ველები", Toast.LENGTH_SHORT).show()

    }
}