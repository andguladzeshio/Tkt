package com.example.tkt.activity

import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler.Value
import androidx.appcompat.app.AppCompatActivity
import com.example.tkt.activity.adapter.TicketRecyclerAdapter
import com.example.tkt.databinding.ActivityTicketFeedBinding
import com.example.tkt.model.Ticket
import com.example.tkt.model.User
import com.example.tkt.util.FirebaseConstants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.UUID

class TicketFeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketFeedBinding

    private val auth = Firebase.auth
    private val userDB = FirebaseDatabase.getInstance().getReference(FirebaseConstants.USERS_DB_PATH)
    private val ticketDB = FirebaseDatabase.getInstance().getReference(FirebaseConstants.TICKETS_DB_PATH)
    private val currentUser = auth.currentUser!!
    private lateinit var currentUserData: User

    private val adapter = TicketRecyclerAdapter(true) {
        userDB
            .child(currentUser.uid)
            .child(FirebaseConstants.BOUGHT_TICKETS_DB_PATH)
            .child(UUID.randomUUID().toString())
            .setValue(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        awaitCurrentUserData()
        binding.ticketRecycler.adapter = adapter
        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        observeTickets()
    }

    private fun observeTickets() {
        ticketDB.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val tickets = snapshot.children.map { it.getValue(Ticket::class.java)!! }
                adapter.submitList(tickets)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun awaitCurrentUserData() {
        userDB.child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUserData = snapshot.getValue(User::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}