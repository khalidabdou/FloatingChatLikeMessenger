package com.example.floatingchatlikemessenger.Model

data class Message (
    val id: Int,
    val text: String,
    val user: Int,
    val created: String,
    var isSended: Boolean = true,
)