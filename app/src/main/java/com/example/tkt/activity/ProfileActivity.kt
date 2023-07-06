package com.example.tkt.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tkt.activity.adapter.TicketRecyclerAdapter
import com.example.tkt.databinding.ActivityProfileBinding
import com.example.tkt.model.Ticket
import com.example.tkt.model.User
import com.example.tkt.util.FirebaseConstants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val auth = Firebase.auth
    private val userDB = FirebaseDatabase.getInstance().getReference(FirebaseConstants.USERS_DB_PATH)
    private val currentUser = auth.currentUser!!
    private lateinit var currentUserData: User

    private val adapter = TicketRecyclerAdapter(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ticketRecycler.adapter = adapter
        observeTickets()
        awaitCurrentUserData()
        binding.signOutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun observeTickets() {
        userDB.child(currentUser.uid).child(FirebaseConstants.BOUGHT_TICKETS_DB_PATH).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tickets = snapshot.children.map { it.getValue(Ticket::class.java)!! }
                adapter.submitList(tickets)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun awaitCurrentUserData() {
        userDB.child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUserData = snapshot.getValue(User::class.java)!!
                binding.fullNameTextView.text = currentUserData.fullName
                binding.emailTextView.text = currentUserData.email
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}