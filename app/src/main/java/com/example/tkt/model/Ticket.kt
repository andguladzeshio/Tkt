package com.example.tkt.model

data class Ticket(
    val id: Int = 0,
    val name: String = "",
    val place: String = "",
    val time: Long = 0,
    val promoCode: String = ""
)