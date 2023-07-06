package com.example.tkt.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tkt.databinding.ActivitySignUpBinding
import com.example.tkt.model.User
import com.example.tkt.util.FirebaseConstants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val auth = Firebase.auth
    private val db = FirebaseDatabase.getInstance().getReference(FirebaseConstants.USERS_DB_PATH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() = with(binding) {
        val fullName = fullNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val repeatPassword = repeatPasswordEditText.text.toString()

        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(this@SignUpActivity, "შეავსეთ ცარიელი ველები", Toast.LENGTH_SHORT).show()
            return@with
        }

        if (repeatPassword != password) {
            Toast.makeText(this@SignUpActivity, "პაროლები არ ემთხვევა", Toast.LENGTH_SHORT).show()
            return@with
        }

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            it.user?.let { user-> saveUserInfo(user.uid, fullName, email)}
            val intent = Intent(this@SignUpActivity, TicketFeedActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this@SignUpActivity, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserInfo(uid: String, userName: String, email: String) {
        val user = User(uid, userName, email)
        db.child(uid).setValue(user)
    }
}